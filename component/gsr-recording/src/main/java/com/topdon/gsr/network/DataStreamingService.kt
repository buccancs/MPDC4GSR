package com.topdon.gsr.network

import android.content.Context
import android.util.Log
import com.topdon.gsr.model.GSRSample
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Specialized thermal imaging component providing DataStreamingService functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
 * </ul>
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
class DataStreamingService(
    private val context: Context,
    private val networkClient: NetworkClient,
) {
    companion object {
        private const val TAG = "DataStreamingService"
        private const val BATCH_SIZE = 50 // Number of samples per batch
        private const val BATCH_TIMEOUT_MS = 100L // Maximum time to wait for batch completion
        private const val MAX_QUEUE_SIZE = 5000 // Maximum queue size to prevent memory issues
        private const val RETRY_ATTEMPTS = 3
        private const val RETRY_DELAY_MS = 500L
    }

    private val streamingJob = SupervisorJob()
    private val streamingScope = CoroutineScope(Dispatchers.IO + streamingJob)

    private val gsrQueue = ConcurrentLinkedQueue<GSRSample>()
    private val thermalQueue = ConcurrentLinkedQueue<ThermalSample>()
    private val videoMetadataQueue = ConcurrentLinkedQueue<VideoMetadata>()

    private val isStreaming = AtomicBoolean(false)
    private val isConnected = AtomicBoolean(false)

    private var batchingJob: Job? = null
    private var currentSessionId: String? = null

    data class ThermalSample(
        val timestamp: Long,
        val frameIndex: Long,
        val temperature: Float,
        val x: Int,
        val y: Int,
        val sessionId: String,
/**
 * Specialized thermal imaging component providing StreamingEventListener functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
 * </ul>
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    interface StreamingEventListener {
    /**
     * Executes onStreamingStarted functionality.
     */
        /**
         * Executes onstreamingstarted operation with thermal imaging domain optimization.
         *
         * @param
         * @param sessionId Parameter for operation (type: String)
         *
         */
        fun onStreamingStarted(sessionId: String)

    /**
     * Executes onStreamingStopped functionality.
     */
        /**
         * Executes onstreamingstopped operation with thermal imaging domain optimization.
         *
         * @param
         * @param sessionId Parameter for operation (type: String)
         *
         */
        fun onStreamingStopped(sessionId: String)

    /**
     * Executes onBatchSent functionality.
     */
        /**
         * Executes onbatchsent operation with thermal imaging domain optimization.
         *
         * @param
         * @param batchSize Parameter for operation (type: Int)
         * @param dataType Parameter for operation (type: String)
         *
         */
        fun onBatchSent(
            batchSize: Int,
            dataType: String,
        )

    /**
     * Executes onStreamingError functionality.
     */
        /**
         * Executes onstreamingerror operation with thermal imaging domain optimization.
         *
         * @param
         * @param error Parameter for operation (type: String)
         *
         */
        fun onStreamingError(error: String)

    /**
     * Executes onQueueFull functionality.
     */
        /**
         * Executes onqueuefull operation with thermal imaging domain optimization.
         *
         * @param
         * @param dataType Parameter for operation (type: String)
         * @param droppedSamples Parameter for operation (type: Int)
         *
         */
        fun onQueueFull(
            dataType: String,
            droppedSamples: Int,
        )
    }

    private var eventListener: StreamingEventListener? = null

    /**
     * Sets eventlistener configuration.
     */
    fun setEventListener(listener: StreamingEventListener?) {
        eventListener = listener
    }

    /**
     * Start real-time data streaming for a session
     */
    suspend fun startStreaming(sessionId: String): Boolean =
        withContext(Dispatchers.IO) {
            if (isStreaming.get()) {
                Log.w(TAG, "Data streaming already active")
                return@withContext false
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!networkClient.isConnected()) {
                Log.w(TAG, "Cannot start streaming - not connected to PC Controller")
                return@withContext false
            }

            try {
                currentSessionId = sessionId
                isStreaming.set(true)
                isConnected.set(true)

                // Clear any existing queued data
                /**
                 * Executes clearqueues operation with thermal imaging domain optimization.
                 *
                 */
                clearQueues()

                // Start the batching and sending process
                /**
                 * Executes startbatchingprocess operation with thermal imaging domain optimization.
                 *
                 */
                startBatchingProcess()

                // Notify PC Controller that streaming started
                val success = networkClient.startDataStreaming()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    eventListener?.onStreamingStarted(sessionId)
                    Log.i(TAG, "Data streaming started for session: $sessionId")
                    true
                } else {
                    /**
                     * Executes stopstreaming operation with thermal imaging domain optimization.
                     *
                     */
                    stopStreaming()
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start data streaming", e)
                eventListener?.onStreamingError("Failed to start: ${e.message}")
                false
            }
        }

    /**
     * Stop real-time data streaming
     */
    suspend fun stopStreaming(): Boolean =
        withContext(Dispatchers.IO) {
            if (!isStreaming.get()) {
                Log.w(TAG, "Data streaming not active")
                return@withContext false
            }

            try {
                isStreaming.set(false)

                // Stop batching process
                batchingJob?.cancel()
                batchingJob = null

                // Send any remaining batched data
                /**
                 * Executes sendremainingdata operation with thermal imaging domain optimization.
                 *
                 */
                sendRemainingData()

                // Notify PC Controller that streaming stopped
                val success = networkClient.stopDataStreaming()

                val sessionId = currentSessionId
                currentSessionId = null

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (sessionId != null) {
                    eventListener?.onStreamingStopped(sessionId)
                }

                Log.i(TAG, "Data streaming stopped")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping data streaming", e)
                false
            }
        }

    /**
     * Queue GSR sample for streaming
     */
    fun queueGSRSample(sample: GSRSample) {
        if (!isStreaming.get()) return

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (gsrQueue.size >= MAX_QUEUE_SIZE) {
            // Drop oldest samples to prevent memory overflow
            val dropped = minOf(BATCH_SIZE, gsrQueue.size / 2)
            /**
             * Executes repeat operation with thermal imaging domain optimization.
             *
             */
            repeat(dropped) { gsrQueue.poll() }
            eventListener?.onQueueFull("GSR", dropped)
            Log.w(TAG, "GSR queue full, dropped $dropped samples")
        }

        gsrQueue.offer(sample)
    }

    /**
     * Queue thermal sample for streaming
     */
    fun queueThermalSample(sample: ThermalSample) {
        if (!isStreaming.get()) return

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thermalQueue.size >= MAX_QUEUE_SIZE) {
            val dropped = minOf(BATCH_SIZE, thermalQueue.size / 2)
            /**
             * Executes repeat operation with thermal imaging domain optimization.
             *
             */
            repeat(dropped) { thermalQueue.poll() }
            eventListener?.onQueueFull("Thermal", dropped)
            Log.w(TAG, "Thermal queue full, dropped $dropped samples")
        }

        thermalQueue.offer(sample)
    }

    /**
     * Queue video metadata for streaming
     */
    fun queueVideoMetadata(metadata: VideoMetadata) {
        if (!isStreaming.get()) return

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (videoMetadataQueue.size >= MAX_QUEUE_SIZE) {
            val dropped = minOf(BATCH_SIZE, videoMetadataQueue.size / 2)
            /**
             * Executes repeat operation with thermal imaging domain optimization.
             *
             */
            repeat(dropped) { videoMetadataQueue.poll() }
            eventListener?.onQueueFull("VideoMetadata", dropped)
            Log.w(TAG, "Video metadata queue full, dropped $dropped samples")
        }

        videoMetadataQueue.offer(metadata)
    }

    /**
     * Executes startBatchingProcess functionality.
     */
    /**
     * Executes startbatchingprocess operation with thermal imaging domain optimization.
     *
     */
    private fun startBatchingProcess() {
        batchingJob =
            streamingScope.launch {
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (isStreaming.get() && isActive) {
                    try {
                        // Process GSR batches
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (gsrQueue.size >= BATCH_SIZE) {
                            /**
                             * Executes sendgsrbatch operation with thermal imaging domain optimization.
                             *
                             */
                            sendGSRBatch()
                        }

                        // Process thermal batches
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (thermalQueue.size >= BATCH_SIZE) {
                            /**
                             * Executes sendthermalbatch operation with thermal imaging domain optimization.
                             *
                             */
                            sendThermalBatch()
                        }

                        // Process video metadata batches
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (videoMetadataQueue.size >= BATCH_SIZE) {
                            /**
                             * Executes sendvideometadatabatch operation with thermal imaging domain optimization.
                             *
                             */
                            sendVideoMetadataBatch()
                        }

                        // Timeout-based batching for partial batches
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(BATCH_TIMEOUT_MS)
                    } catch (e: Exception) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isActive) {
                            Log.e(TAG, "Error in batching process", e)
                            eventListener?.onStreamingError("Batching error: ${e.message}")
                            /**
                             * Executes delay operation with thermal imaging domain optimization.
                             *
                             */
                            delay(1000) // Wait before retrying
                        }
                    }
                }
            }
    }

    /**
     * Executes sendgsrbatch operation with thermal imaging domain optimization.
     *
     */
    private suspend fun sendGSRBatch() {
        val batch = mutableListOf<GSRSample>()
        /**
         * Executes repeat operation with thermal imaging domain optimization.
         *
         */
        repeat(minOf(BATCH_SIZE, gsrQueue.size)) {
            gsrQueue.poll()?.let { batch.add(it) }
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (batch.isNotEmpty()) {
            val batchData = createGSRBatchJson(batch)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (sendBatchWithRetry(batchData, "gsr")) {
                eventListener?.onBatchSent(batch.size, "GSR")
            }
        }
    }

    /**
     * Executes sendthermalbatch operation with thermal imaging domain optimization.
     *
     */
    private suspend fun sendThermalBatch() {
        val batch = mutableListOf<ThermalSample>()
        /**
         * Executes repeat operation with thermal imaging domain optimization.
         *
         */
        repeat(minOf(BATCH_SIZE, thermalQueue.size)) {
            thermalQueue.poll()?.let { batch.add(it) }
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (batch.isNotEmpty()) {
            val batchData = createThermalBatchJson(batch)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (sendBatchWithRetry(batchData, "thermal")) {
                eventListener?.onBatchSent(batch.size, "Thermal")
            }
        }
    }

    /**
     * Executes sendvideometadatabatch operation with thermal imaging domain optimization.
     *
     */
    private suspend fun sendVideoMetadataBatch() {
        val batch = mutableListOf<VideoMetadata>()
        /**
         * Executes repeat operation with thermal imaging domain optimization.
         *
         */
        repeat(minOf(BATCH_SIZE, videoMetadataQueue.size)) {
            videoMetadataQueue.poll()?.let { batch.add(it) }
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (batch.isNotEmpty()) {
            val batchData = createVideoMetadataBatchJson(batch)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (sendBatchWithRetry(batchData, "video_metadata")) {
                eventListener?.onBatchSent(batch.size, "VideoMetadata")
            }
        }
    }

    /**
     * Executes sendbatchwithretry operation with thermal imaging domain optimization.
     *
     * @param
     * @param batchData Parameter for operation (type: JSONObject)
     * @param dataType Parameter for operation (type: String)
     *
     */
    private suspend fun sendBatchWithRetry(
        batchData: JSONObject,
        dataType: String,
    ): Boolean {
        /**
         * Executes repeat operation with thermal imaging domain optimization.
         *
         */
        repeat(RETRY_ATTEMPTS) { attempt ->
            try {
                val success =
                    networkClient.sendMeasurementData(
                        currentSessionId ?: "unknown",
                        batchData,
                    )
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    return true
                }
            } catch (e: Exception) {
                Log.w(TAG, "Batch send attempt ${attempt + 1} failed for $dataType", e)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (attempt < RETRY_ATTEMPTS - 1) {
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(RETRY_DELAY_MS)
                }
            }
        }

        Log.e(TAG, "Failed to send $dataType batch after $RETRY_ATTEMPTS attempts")
        eventListener?.onStreamingError("Failed to send $dataType batch")
        return false
    }

    /**
     * Executes createGSRBatchJson functionality.
     */
    /**
     * Executes creategsrbatchjson operation with thermal imaging domain optimization.
     *
     * @param
     * @param samples Parameter for operation (type: List<GSRSample>)
     *
     */
    private fun createGSRBatchJson(samples: List<GSRSample>): JSONObject {
        val samplesArray = JSONArray()
        samples.forEach { sample ->
            val sampleJson =
                /**
                 * Executes jsonobject operation with thermal imaging domain optimization.
                 *
                 */
                JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("timestamp", sample.timestamp)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("utc_timestamp", sample.utcTimestamp)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("sample_index", sample.sampleIndex)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("conductance", sample.conductance)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("resistance", sample.resistance)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("raw_value", sample.rawValue)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("session_id", sample.sessionId)
                }
            samplesArray.put(sampleJson)
        }

        return JSONObject().apply {
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("data_type", "gsr_batch")
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("batch_size", samples.size)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("samples", samplesArray)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("synchronized_timestamp", networkClient.getSynchronizedTimestamp())
        }
    }

    /**
     * Executes createThermalBatchJson functionality.
     */
    /**
     * Executes createthermalbatchjson operation with thermal imaging domain optimization.
     *
     * @param
     * @param samples Parameter for operation (type: List<ThermalSample>)
     *
     */
    private fun createThermalBatchJson(samples: List<ThermalSample>): JSONObject {
        val samplesArray = JSONArray()
        samples.forEach { sample ->
            val sampleJson =
                /**
                 * Executes jsonobject operation with thermal imaging domain optimization.
                 *
                 */
                JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("timestamp", sample.timestamp)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("frame_index", sample.frameIndex)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("temperature", sample.temperature)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("x", sample.x)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("y", sample.y)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("session_id", sample.sessionId)
                }
            samplesArray.put(sampleJson)
        }

        return JSONObject().apply {
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("data_type", "thermal_batch")
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("batch_size", samples.size)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("samples", samplesArray)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("synchronized_timestamp", networkClient.getSynchronizedTimestamp())
        }
    }

    /**
     * Executes createVideoMetadataBatchJson functionality.
     */
    /**
     * Executes createvideometadatabatchjson operation with thermal imaging domain optimization.
     *
     * @param
     * @param samples Parameter for operation (type: List<VideoMetadata>)
     *
     */
    private fun createVideoMetadataBatchJson(samples: List<VideoMetadata>): JSONObject {
        val samplesArray = JSONArray()
        samples.forEach { sample ->
            val sampleJson =
                /**
                 * Executes jsonobject operation with thermal imaging domain optimization.
                 *
                 */
                JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("timestamp", sample.timestamp)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("frame_index", sample.frameIndex)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("frame_size", sample.frameSize)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("session_id", sample.sessionId)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("camera_type", sample.cameraType)
                }
            samplesArray.put(sampleJson)
        }

        return JSONObject().apply {
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("data_type", "video_metadata_batch")
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("batch_size", samples.size)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("samples", samplesArray)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("synchronized_timestamp", networkClient.getSynchronizedTimestamp())
        }
    }

    /**
     * Executes sendremainingdata operation with thermal imaging domain optimization.
     *
     */
    private suspend fun sendRemainingData() {
        // Send any remaining GSR data
        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (gsrQueue.isNotEmpty()) {
            /**
             * Executes sendgsrbatch operation with thermal imaging domain optimization.
             *
             */
            sendGSRBatch()
        }

        // Send any remaining thermal data
        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (thermalQueue.isNotEmpty()) {
            /**
             * Executes sendthermalbatch operation with thermal imaging domain optimization.
             *
             */
            sendThermalBatch()
        }

        // Send any remaining video metadata
        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (videoMetadataQueue.isNotEmpty()) {
            /**
             * Executes sendvideometadatabatch operation with thermal imaging domain optimization.
             *
             */
            sendVideoMetadataBatch()
        }
    }

    /**
     * Executes clearQueues functionality.
     */
    /**
     * Executes clearqueues operation with thermal imaging domain optimization.
     *
     */
    private fun clearQueues() {
        gsrQueue.clear()
        thermalQueue.clear()
        videoMetadataQueue.clear()
    }

    /**
     * Get current queue sizes for monitoring
     */
    fun getQueueSizes(): Map<String, Int> {
        return mapOf(
            "gsr" to gsrQueue.size,
            "thermal" to thermalQueue.size,
            "video_metadata" to videoMetadataQueue.size,
        )
    }

    /**
     * Check if streaming is active
     */
    fun isStreamingActive(): Boolean = isStreaming.get()

    /**
     * Clean up resources
     */
    /**
     * Executes cleanup operation with thermal imaging domain optimization.
     *
     */
    suspend fun cleanup() {
        // Stop streaming before cancelling jobs to ensure proper data flush
        /**
         * Executes stopstreaming operation with thermal imaging domain optimization.
         *
         */
        stopStreaming()

        // Cancel all jobs after streaming is stopped
        streamingJob.cancel()
        /**
         * Executes clearqueues operation with thermal imaging domain optimization.
         *
         */
        clearQueues()
        eventListener = null
    }
}
