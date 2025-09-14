package com.topdon.tc001.sensors.shimmer.model

/**
 * **Shimmer Device Information Model**
 * 
 * Comprehensive device information for Shimmer3 GSR+ sensor selection and management.
 * Implements the device discovery and selection requirements from the integration plan.
 * 
 * ## Enhanced Features:
 * - **MAC Address Filtering**: Validation against Shimmer MAC prefixes (00:06:66, d0:39:72)
 * - **Device Prioritization**: Priority scoring for optimal device selection  
 * - **Connection Quality**: Real-time RSSI and connection status monitoring
 * - **Pairing Status**: Bluetooth pairing and availability tracking
 * - **Device Ranking**: Automatic ranking based on signal strength and device type
 * 
 * @property macAddress Bluetooth MAC address (used for connection and filtering)
 * @property name Device name as advertised over BLE
 * @property rssi Signal strength in dBm (for connection quality assessment)
 * @property isPaired Whether device is already paired in Bluetooth settings
 * @property priority Calculated priority score for device selection (higher = better)
 * @property connectionState Current connection status
 * @property deviceType Shimmer device model identification
 * @property batteryLevel Battery percentage if available
 * @property firmwareVersion Device firmware version if available
 * 
 * @author IRCamera Shimmer Integration Team
 */
data class ShimmerDeviceInfo(
    val macAddress: String,
    val name: String,
    val rssi: Int,
    val isPaired: Boolean,
    val priority: Int,
    val connectionState: String,
    val deviceType: String = detectDeviceType(name),
    val batteryLevel: Int? = null,
    val firmwareVersion: String? = null
) {
    
    companion object {
        
        // Shimmer MAC address prefixes for validation
        private val SHIMMER_MAC_PREFIXES = listOf("00:06:66", "d0:39:72", "00:80:98")
        
        /**
         * Detect Shimmer device type from name
         */
        fun detectDeviceType(deviceName: String): String = when {
            deviceName.contains("Shimmer3-GSR", ignoreCase = true) -> "Shimmer3 GSR+"
            deviceName.contains("GSR", ignoreCase = true) -> "Shimmer GSR"
            deviceName.contains("Shimmer3", ignoreCase = true) -> "Shimmer3"
            deviceName.contains("Shimmer", ignoreCase = true) -> "Shimmer (Generic)"
            else -> "Unknown"
        }
        
        /**
         * Validate if MAC address belongs to a Shimmer device
         */
        fun isValidShimmerMAC(macAddress: String): Boolean {
            return SHIMMER_MAC_PREFIXES.any { prefix ->
                macAddress.startsWith(prefix, ignoreCase = true)
            }
        }
        
        /**
         * Calculate device priority based on multiple factors
         */
        fun calculatePriority(
            macAddress: String,
            deviceName: String, 
            rssi: Int,
            isPaired: Boolean
        ): Int {
            var priority = 0
            
            // MAC address priority (official Shimmer prefixes)
            when {
                macAddress.startsWith("00:06:66", true) -> priority += 100  // Primary 
                macAddress.startsWith("d0:39:72", true) -> priority += 90   // Secondary
                macAddress.startsWith("00:80:98", true) -> priority += 80   // Alternative
            }
            
            // Device name priority
            when {
                deviceName.contains("Shimmer3-GSR", true) -> priority += 50
                deviceName.contains("GSR", true) -> priority += 30
                deviceName.contains("Shimmer3", true) -> priority += 25
                deviceName.contains("Shimmer", true) -> priority += 20
            }
            
            // Pairing status bonus
            if (isPaired) priority += 25
            
            // Signal strength bonus (convert dBm to priority points)
            priority += maxOf(0, (rssi + 100) / 2)
            
            return priority
        }
    }
    
    /**
     * Check if this device is a valid Shimmer GSR sensor
     */
    fun isValidGSRDevice(): Boolean {
        return isValidShimmerMAC(macAddress) && 
               (name.contains("GSR", ignoreCase = true) || 
                name.contains("Shimmer", ignoreCase = true))
    }
    
    /**
     * Get signal quality level based on RSSI
     */
    fun getSignalQuality(): String = when {
        rssi >= -50 -> "Excellent"
        rssi >= -60 -> "Good"
        rssi >= -70 -> "Fair" 
        rssi >= -80 -> "Poor"
        else -> "Weak"
    }
    
    /**
     * Get connection state with quality indicator
     */
    fun getDetailedStatus(): String {
        val quality = getSignalQuality()
        return "$connectionState ($quality, ${rssi}dBm)"
    }
    
    /**
     * Check if device is ready for connection
     */
    fun isReadyForConnection(): Boolean {
        return connectionState in listOf("Available", "Discovered", "Ready") &&
               rssi >= -85  // Minimum signal strength threshold
    }
    
    /**
     * Get device description for UI display
     */
    fun getDisplayName(): String {
        return "$name\n${macAddress} • ${getSignalQuality()}"
    }
    
    /**
     * Create updated copy with new connection state
     */
    fun withConnectionState(newState: String): ShimmerDeviceInfo {
        return copy(connectionState = newState)
    }
    
    /**
     * Create updated copy with new RSSI value
     */
    fun withRSSI(newRssi: Int): ShimmerDeviceInfo {
        return copy(
            rssi = newRssi,
            priority = calculatePriority(macAddress, name, newRssi, isPaired)
        )
    }
    
    /**
     * Convert to summary string for logging
     */
    override fun toString(): String {
        return "ShimmerDevice(name='$name', mac='$macAddress', rssi=${rssi}dBm, " +
               "type='$deviceType', priority=$priority, paired=$isPaired, state='$connectionState')"
    }
}