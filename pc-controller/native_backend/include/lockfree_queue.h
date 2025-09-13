#pragma once

/**
 * @file lockfree_queue.h
 * @brief High-performance lock-free queue for real-time data streaming
 * 
 * Implements a thread-safe, lock-free queue optimized for single-producer,
 * single-consumer scenarios common in real-time sensor data streaming.
 * 
 * Key Features:
 * - Lock-free operations for minimal latency
 * - Memory efficient with circular buffer design
 * - Thread-safe for SPSC (Single Producer Single Consumer)
 * - Optimized for high-frequency data (100Hz+)
 */

#include <atomic>
#include <memory>
#include <array>

namespace ircamera {

/**
 * @brief Lock-free queue optimized for real-time sensor data
 * @tparam T Data type to store in queue
 * @tparam Size Maximum queue capacity (must be power of 2)
 */
template<typename T, size_t Size = 1024>
class lockfree_queue {
    static_assert((Size & (Size - 1)) == 0, "Size must be power of 2");
    static_assert(Size > 1, "Size must be greater than 1");
    
public:
    /**
     * @brief Construct a new lockfree queue
     */
    lockfree_queue() : head_(0), tail_(0) {
        // Initialize all slots as empty
        for (size_t i = 0; i < Size; ++i) {
            slots_[i].sequence.store(i, std::memory_order_relaxed);
        }
    }
    
    /**
     * @brief Attempt to push an item to the queue (non-blocking)
     * @param item Item to push
     * @return true if successful, false if queue is full
     */
    bool try_push(const T& item) {
        return emplace(item);
    }
    
    /**
     * @brief Attempt to push an item to the queue (non-blocking, move version)
     * @param item Item to push
     * @return true if successful, false if queue is full
     */
    bool try_push(T&& item) {
        return emplace(std::move(item));
    }
    
    /**
     * @brief Construct and push an item in-place (non-blocking)
     * @tparam Args Constructor argument types
     * @param args Constructor arguments
     * @return true if successful, false if queue is full
     */
    template<typename... Args>
    bool emplace(Args&&... args) {
        const size_t head = head_.load(std::memory_order_relaxed);
        slot_t& slot = slots_[head & mask_];
        
        if (slot.sequence.load(std::memory_order_acquire) != head) {
            return false;  // Queue is full
        }
        
        // Construct item in place
        new (&slot.item) T(std::forward<Args>(args)...);
        
        slot.sequence.store(head + 1, std::memory_order_release);
        head_.store(head + 1, std::memory_order_relaxed);
        
        return true;
    }
    
    /**
     * @brief Attempt to pop an item from the queue (non-blocking)
     * @param item Reference to store popped item
     * @return true if successful, false if queue is empty
     */
    bool try_pop(T& item) {
        const size_t tail = tail_.load(std::memory_order_relaxed);
        slot_t& slot = slots_[tail & mask_];
        
        if (slot.sequence.load(std::memory_order_acquire) != tail + 1) {
            return false;  // Queue is empty
        }
        
        // Move item out
        item = std::move(slot.item);
        
        // Destroy the item in the slot
        slot.item.~T();
        
        slot.sequence.store(tail + mask_ + 1, std::memory_order_release);
        tail_.store(tail + 1, std::memory_order_relaxed);
        
        return true;
    }
    
    /**
     * @brief Get approximate number of items in queue
     * @return Approximate queue size
     */
    size_t size() const {
        const size_t head = head_.load(std::memory_order_relaxed);
        const size_t tail = tail_.load(std::memory_order_relaxed);
        return head - tail;
    }
    
    /**
     * @brief Check if queue is approximately empty
     * @return true if empty, false otherwise
     */
    bool empty() const {
        return size() == 0;
    }
    
    /**
     * @brief Check if queue is approximately full
     * @return true if full, false otherwise
     */
    bool full() const {
        return size() >= Size - 1;
    }
    
    /**
     * @brief Get maximum queue capacity
     * @return Maximum capacity
     */
    constexpr size_t capacity() const {
        return Size;
    }
    
    /**
     * @brief Clear all items from the queue
     * Note: This is not thread-safe with concurrent operations
     */
    void clear() {
        T item;
        while (try_pop(item)) {
            // Items are automatically destroyed
        }
    }

private:
    static constexpr size_t mask_ = Size - 1;
    
    struct slot_t {
        std::atomic<size_t> sequence;
        alignas(T) char storage[sizeof(T)];
        
        T& item {
            return reinterpret_cast<T&>(storage);
        }
        
        const T& item const {
            return reinterpret_cast<const T&>(storage);
        }
    };
    
    // Cache line alignment to avoid false sharing
    alignas(64) std::atomic<size_t> head_;
    alignas(64) std::atomic<size_t> tail_;
    alignas(64) std::array<slot_t, Size> slots_;
};

/**
 * @brief Multi-producer, single-consumer lock-free queue
 * @tparam T Data type to store in queue
 * @tparam Size Maximum queue capacity (must be power of 2)
 */
template<typename T, size_t Size = 1024>
class mpsc_lockfree_queue {
    static_assert((Size & (Size - 1)) == 0, "Size must be power of 2");
    
public:
    mpsc_lockfree_queue() : head_(0), tail_(0) {}
    
    bool try_push(const T& item) {
        const size_t head = head_.fetch_add(1, std::memory_order_relaxed);
        const size_t index = head & mask_;
        
        // Wait for slot to be available
        while (slots_[index].sequence.load(std::memory_order_acquire) != head) {
            std::this_thread::yield();
        }
        
        slots_[index].item = item;
        slots_[index].sequence.store(head + 1, std::memory_order_release);
        
        return true;
    }
    
    bool try_pop(T& item) {
        const size_t tail = tail_.load(std::memory_order_relaxed);
        const size_t index = tail & mask_;
        
        if (slots_[index].sequence.load(std::memory_order_acquire) != tail + 1) {
            return false;
        }
        
        item = std::move(slots_[index].item);
        slots_[index].sequence.store(tail + Size, std::memory_order_release);
        tail_.store(tail + 1, std::memory_order_relaxed);
        
        return true;
    }
    
    size_t size() const {
        const size_t head = head_.load(std::memory_order_relaxed);
        const size_t tail = tail_.load(std::memory_order_relaxed);
        return head - tail;
    }
    
    bool empty() const { return size() == 0; }
    constexpr size_t capacity() const { return Size; }

private:
    static constexpr size_t mask_ = Size - 1;
    
    struct slot_t {
        std::atomic<size_t> sequence{0};
        T item;
    };
    
    alignas(64) std::atomic<size_t> head_;
    alignas(64) std::atomic<size_t> tail_;
    alignas(64) std::array<slot_t, Size> slots_;
};

} // namespace ircamera