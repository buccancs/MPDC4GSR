package com.topdon.tc001.sensors.unified.model

/**
 * **Device Information Model**
 * 
 * Represents discovered Shimmer devices with connection and capability information.
 * 
 * Used for device discovery, prioritization, and selection in the unified GSR recording system.
 * Supports enhanced device filtering and GSR+ device prioritization.
 * 
 * @param address Bluetooth MAC address
 * @param name Device name (e.g., "Shimmer3-GSR+", "Shimmer Device")
 * @param deviceType Device type classification
 * @param rssi Signal strength in dBm
 * @param isGSRCapable Whether device supports GSR recording
 * @param priority Connection priority (1=highest, 2=medium, 3=lowest)
 * @param batteryLevel Battery level percentage (if available)
 * @param firmwareVersion Firmware version string (if available)
 * 
 * @author IRCamera Unified Sensor Integration
 */
data class DeviceInfo(
    val address: String,
    val name: String,
    val deviceType: String,
    val rssi: Int,
    val isGSRCapable: Boolean,
    val priority: Int = 2,
    val batteryLevel: Int? = null,
    val firmwareVersion: String? = null
) {
    
    /**
     * Check if this is a Shimmer3 GSR+ device
     */
    val isGSRPlusDevice: Boolean
        get() = name.contains("GSR", ignoreCase = true) || 
                deviceType.contains("GSR", ignoreCase = true)
    
    /**
     * Check if device has strong signal
     */
    val hasStrongSignal: Boolean
        get() = rssi >= -60
    
    /**
     * Check if device has weak signal
     */
    val hasWeakSignal: Boolean
        get() = rssi <= -80
    
    /**
     * Get signal strength classification
     */
    val signalStrength: SignalStrength
        get() = when {
            rssi >= -50 -> SignalStrength.EXCELLENT
            rssi >= -60 -> SignalStrength.GOOD
            rssi >= -70 -> SignalStrength.FAIR
            rssi >= -80 -> SignalStrength.POOR
            else -> SignalStrength.VERY_POOR
        }
    
    /**
     * Check if device is recommended for GSR recording
     */
    val isRecommended: Boolean
        get() = isGSRCapable && 
                hasStrongSignal && 
                (batteryLevel == null || batteryLevel > 20)
    
    /**
     * Get display name for UI
     */
    val displayName: String
        get() = when {
            isGSRPlusDevice -> "$name (GSR+)"
            isGSRCapable -> "$name (GSR)"
            else -> name
        }
    
    /**
     * Get status summary for UI
     */
    val statusSummary: String
        get() = buildString {
            append(signalStrength.displayName)
            batteryLevel?.let { append(" • $it% battery") }
            if (isRecommended) append(" • Recommended")
        }
    
    /**
     * Convert to map for JSON serialization
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "address" to address,
            "name" to name,
            "device_type" to deviceType,
            "rssi" to rssi,
            "is_gsr_capable" to isGSRCapable,
            "is_gsr_plus" to isGSRPlusDevice,
            "priority" to priority,
            "battery_level" to batteryLevel,
            "firmware_version" to firmwareVersion,
            "signal_strength" to signalStrength.name,
            "is_recommended" to isRecommended,
            "display_name" to displayName,
            "status_summary" to statusSummary
        )
    }
    
    /**
     * Signal strength classification
     */
    enum class SignalStrength(val displayName: String) {
        EXCELLENT("Excellent"),
        GOOD("Good"),
        FAIR("Fair"),
        POOR("Poor"),
        VERY_POOR("Very Poor")
    }
    
    companion object {
        
        /**
         * Shimmer MAC address prefixes for device identification
         */
        val SHIMMER_MAC_PREFIXES = listOf("00:06:66", "d0:39:72")
        
        /**
         * Check if MAC address belongs to a Shimmer device
         */
        fun isShimmerDevice(address: String): Boolean {
            return SHIMMER_MAC_PREFIXES.any { 
                address.startsWith(it, ignoreCase = true) 
            }
        }
        
        /**
         * Create DeviceInfo from Bluetooth device scan result
         */
        fun fromBluetoothDevice(
            address: String,
            name: String?,
            rssi: Int
        ): DeviceInfo? {
            
            if (!isShimmerDevice(address)) {
                return null  // Not a Shimmer device
            }
            
            val deviceName = name ?: "Shimmer Device"
            val isGSRDevice = deviceName.contains("GSR", ignoreCase = true)
            val deviceType = when {
                isGSRDevice -> "GSR+"
                deviceName.contains("Shimmer3", ignoreCase = true) -> "Shimmer3"
                deviceName.contains("Shimmer", ignoreCase = true) -> "Shimmer"
                else -> "Unknown"
            }
            
            // Prioritize GSR+ devices
            val priority = when {
                isGSRDevice -> 1  // Highest priority
                deviceType.startsWith("Shimmer3") -> 2  // Medium priority
                else -> 3  // Lower priority
            }
            
            return DeviceInfo(
                address = address,
                name = deviceName,
                deviceType = deviceType,
                rssi = rssi,
                isGSRCapable = true,  // All Shimmer devices can potentially do GSR
                priority = priority
            )
        }
        
        /**
         * Sort device list by connection priority
         */
        fun sortByPriority(devices: List<DeviceInfo>): List<DeviceInfo> {
            return devices.sortedWith(
                compareBy<DeviceInfo> { it.priority }
                    .thenByDescending { it.rssi }  // Better signal as tiebreaker
                    .thenBy { it.name }  // Name as final tiebreaker
            )
        }
        
        /**
         * Filter devices for GSR recording recommendations
         */
        fun getRecommendedDevices(devices: List<DeviceInfo>): List<DeviceInfo> {
            return devices
                .filter { it.isRecommended }
                .let { sortByPriority(it) }
        }
        
        /**
         * Create mock device for testing
         */
        fun createMockDevice(
            deviceId: String = "test_device",
            isGSR: Boolean = true,
            signalStrength: SignalStrength = SignalStrength.GOOD
        ): DeviceInfo {
            val rssi = when (signalStrength) {
                SignalStrength.EXCELLENT -> -45
                SignalStrength.GOOD -> -55
                SignalStrength.FAIR -> -65
                SignalStrength.POOR -> -75
                SignalStrength.VERY_POOR -> -85
            }
            
            return DeviceInfo(
                address = "00:06:66:${String.format("%02X:%02X:%02X", 
                    deviceId.hashCode() and 0xFF, 
                    (deviceId.hashCode() shr 8) and 0xFF,
                    (deviceId.hashCode() shr 16) and 0xFF
                )}",
                name = if (isGSR) "Shimmer3-GSR+ $deviceId" else "Shimmer3 $deviceId",
                deviceType = if (isGSR) "GSR+" else "Shimmer3",
                rssi = rssi,
                isGSRCapable = true,
                priority = if (isGSR) 1 else 2,
                batteryLevel = (50..90).random(),
                firmwareVersion = "BtStream 0.7.0"
            )
        }
    }
}