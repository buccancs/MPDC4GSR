package com.topdon.tc001.sensors.unified.model

/**
 * **PC Controller Information Model**
 * 
 * Represents discovered PC controllers on the local network with connection details and capabilities.
 * 
 * Used by UnifiedNetworkController for service discovery, connection management, and capability negotiation.
 * Supports multiple PC controllers with different configurations and feature sets.
 * 
 * @param name Controller service name (e.g., "IRCamera-PC-001")
 * @param host IP address or hostname of the PC controller
 * @param port Network port for communication (typically 8888 for WebSocket)
 * @param type mDNS service type (e.g., "_ircamera._tcp.local.")
 * @param properties Additional service properties from mDNS discovery
 * @param capabilities List of supported features/capabilities
 * @param protocolVersion Communication protocol version
 * @param lastSeen Timestamp when last seen on network
 * 
 * @author IRCamera Unified Sensor Integration
 */
data class PCControllerInfo(
    val name: String,
    val host: String,
    val port: Int,
    val type: String,
    val properties: Map<String, String> = emptyMap(),
    val capabilities: List<String> = emptyList(),
    val protocolVersion: String = "1.0",
    val lastSeen: Long = System.currentTimeMillis()
) {
    
    /**
     * Full connection address
     */
    val address: String
        get() = "$host:$port"
    
    /**
     * Check if controller supports GSR recording
     */
    val supportsGSR: Boolean
        get() = capabilities.contains("gsr") || 
                properties["supports_gsr"] == "true" ||
                properties.containsKey("shimmer_support")
    
    /**
     * Check if controller supports thermal camera
     */
    val supportsThermal: Boolean
        get() = capabilities.contains("thermal") || 
                properties["supports_thermal"] == "true"
    
    /**
     * Check if controller supports RGB camera
     */
    val supportsRGB: Boolean
        get() = capabilities.contains("rgb") || 
                properties["supports_rgb"] == "true"
    
    /**
     * Check if controller supports secure connections (TLS/SSL)
     */
    val supportsSecure: Boolean
        get() = capabilities.contains("tls") || 
                properties["secure"] == "true" ||
                properties["tls"] == "true"
    
    /**
     * Check if controller is recently active (within last 60 seconds)
     */
    val isRecentlyActive: Boolean
        get() = System.currentTimeMillis() - lastSeen < 60000
    
    /**
     * Get controller software version if available
     */
    val softwareVersion: String?
        get() = properties["version"] ?: properties["software_version"]
    
    /**
     * Get controller platform information
     */
    val platform: String?
        get() = properties["platform"] ?: properties["os"]
    
    /**
     * Get display name for UI
     */
    val displayName: String
        get() = properties["display_name"] ?: name
    
    /**
     * Get status summary for UI
     */
    val statusSummary: String
        get() = buildString {
            append(address)
            softwareVersion?.let { append(" • v$it") }
            platform?.let { append(" • $it") }
            
            val supportedFeatures = mutableListOf<String>()
            if (supportsGSR) supportedFeatures.add("GSR")
            if (supportsThermal) supportedFeatures.add("Thermal")
            if (supportsRGB) supportedFeatures.add("RGB")
            
            if (supportedFeatures.isNotEmpty()) {
                append(" • ${supportedFeatures.joinToString("/")}")
            }
        }
    
    /**
     * Get connection priority based on capabilities and recency
     */
    val connectionPriority: Int
        get() {
            var priority = 0
            
            // Recent activity gets higher priority
            if (isRecentlyActive) priority += 100
            
            // More capabilities = higher priority
            if (supportsGSR) priority += 20
            if (supportsThermal) priority += 15
            if (supportsRGB) priority += 10
            if (supportsSecure) priority += 5
            
            return priority
        }
    
    /**
     * Convert to map for JSON serialization
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "host" to host,
            "port" to port,
            "type" to type,
            "address" to address,
            "capabilities" to capabilities,
            "properties" to properties,
            "protocol_version" to protocolVersion,
            "last_seen" to lastSeen,
            "supports_gsr" to supportsGSR,
            "supports_thermal" to supportsThermal,
            "supports_rgb" to supportsRGB,
            "supports_secure" to supportsSecure,
            "is_recently_active" to isRecentlyActive,
            "software_version" to softwareVersion,
            "platform" to platform,
            "display_name" to displayName,
            "status_summary" to statusSummary,
            "connection_priority" to connectionPriority
        )
    }
    
    /**
     * Create WebSocket URL for connection
     */
    fun getWebSocketUrl(secure: Boolean = false): String {
        val protocol = if (secure && supportsSecure) "wss" else "ws"
        return "$protocol://$host:$port"
    }
    
    /**
     * Check compatibility with required features
     */
    fun isCompatibleWith(requiredFeatures: List<String>): Boolean {
        return requiredFeatures.all { feature ->
            when (feature.lowercase()) {
                "gsr" -> supportsGSR
                "thermal" -> supportsThermal
                "rgb" -> supportsRGB
                "secure", "tls" -> supportsSecure
                else -> capabilities.contains(feature) || properties.containsKey(feature)
            }
        }
    }
    
    companion object {
        
        /**
         * Create PC controller info from mDNS service info
         */
        fun fromServiceInfo(
            serviceName: String,
            hostAddress: String,
            port: Int,
            serviceType: String,
            txtRecord: Map<String, String>
        ): PCControllerInfo {
            
            // Parse capabilities from TXT record
            val capabilities = txtRecord["capabilities"]?.split(",")?.map { it.trim() } ?: emptyList()
            
            // Parse protocol version
            val protocolVersion = txtRecord["protocol_version"] ?: txtRecord["version"] ?: "1.0"
            
            return PCControllerInfo(
                name = serviceName,
                host = hostAddress,
                port = port,
                type = serviceType,
                properties = txtRecord,
                capabilities = capabilities,
                protocolVersion = protocolVersion
            )
        }
        
        /**
         * Create mock PC controller for testing
         */
        fun createMockController(
            controllerId: String = "test_pc",
            includeGSR: Boolean = true,
            includeThermal: Boolean = true,
            includeRGB: Boolean = true
        ): PCControllerInfo {
            
            val capabilities = mutableListOf<String>()
            if (includeGSR) capabilities.add("gsr")
            if (includeThermal) capabilities.add("thermal")
            if (includeRGB) capabilities.add("rgb")
            capabilities.add("tls")
            
            val properties = mapOf(
                "version" to "2.1.0",
                "platform" to "Windows 11",
                "display_name" to "IRCamera PC Controller $controllerId",
                "supports_gsr" to includeGSR.toString(),
                "supports_thermal" to includeThermal.toString(),
                "supports_rgb" to includeRGB.toString(),
                "secure" to "true",
                "shimmer_support" to "true"
            )
            
            return PCControllerInfo(
                name = "ircamera-pc-$controllerId",
                host = "192.168.1.${100 + controllerId.hashCode() % 50}",
                port = 8888,
                type = "_ircamera._tcp.local.",
                properties = properties,
                capabilities = capabilities,
                protocolVersion = "2.0"
            )
        }
        
        /**
         * Sort controllers by connection priority
         */
        fun sortByPriority(controllers: List<PCControllerInfo>): List<PCControllerInfo> {
            return controllers.sortedByDescending { it.connectionPriority }
        }
        
        /**
         * Filter controllers by required features
         */
        fun filterByFeatures(
            controllers: List<PCControllerInfo>,
            requiredFeatures: List<String>
        ): List<PCControllerInfo> {
            return controllers.filter { it.isCompatibleWith(requiredFeatures) }
        }
        
        /**
         * Get controllers that support GSR recording
         */
        fun getGSRCapableControllers(controllers: List<PCControllerInfo>): List<PCControllerInfo> {
            return controllers.filter { it.supportsGSR }
        }
        
        /**
         * Get recently active controllers
         */
        fun getActiveControllers(controllers: List<PCControllerInfo>): List<PCControllerInfo> {
            return controllers.filter { it.isRecentlyActive }
        }
    }
}