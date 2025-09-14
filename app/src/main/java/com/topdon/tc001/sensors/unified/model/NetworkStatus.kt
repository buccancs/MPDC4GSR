package com.topdon.tc001.sensors.unified.model

/**
 * **Network Status Enumeration**
 * 
 * Represents the current state of network connectivity and PC controller discovery/connection.
 * 
 * Used by UnifiedNetworkController to communicate network state changes to UI components
 * and other system components that depend on network connectivity.
 * 
 * @author IRCamera Unified Sensor Integration
 */
enum class NetworkStatus(val displayName: String, val isConnected: Boolean, val canDiscover: Boolean) {
    
    /**
     * No network connection available
     */
    DISCONNECTED("Disconnected", false, false),
    
    /**
     * No Wi-Fi network connection
     */
    NO_WIFI("No Wi-Fi", false, false),
    
    /**
     * Connected to Wi-Fi but no internet or local network access
     */
    CONNECTED_TO_WIFI("Connected to Wi-Fi", true, true),
    
    /**
     * Missing network permissions
     */
    PERMISSION_DENIED("Permission Denied", false, false),
    
    /**
     * Currently discovering PC controllers on network
     */
    DISCOVERING("Discovering Controllers", true, true),
    
    /**
     * Discovery completed, ready to connect to controllers
     */
    READY("Ready", true, false),
    
    /**
     * No PC controllers found on network
     */
    NO_CONTROLLERS_FOUND("No Controllers Found", true, false),
    
    /**
     * Currently connecting to a PC controller
     */
    CONNECTING("Connecting", true, false),
    
    /**
     * Successfully connected to at least one PC controller
     */
    CONNECTED("Connected to PC", true, false),
    
    /**
     * Failed to connect to PC controller
     */
    CONNECTION_FAILED("Connection Failed", true, true),
    
    /**
     * Network connection lost (Wi-Fi disconnected)
     */
    NETWORK_LOST("Network Lost", false, false),
    
    /**
     * General network error occurred
     */
    ERROR("Network Error", false, false);
    
    /**
     * Check if network is available for operations
     */
    val isNetworkAvailable: Boolean
        get() = this != DISCONNECTED && this != NO_WIFI && this != NETWORK_LOST && this != PERMISSION_DENIED
    
    /**
     * Check if in an error state
     */
    val isError: Boolean
        get() = this == ERROR || this == CONNECTION_FAILED || this == PERMISSION_DENIED
    
    /**
     * Check if actively trying to connect
     */
    val isConnecting: Boolean
        get() = this == DISCOVERING || this == CONNECTING
    
    /**
     * Check if can attempt to connect to controllers
     */
    val canConnect: Boolean
        get() = this == READY || this == NO_CONTROLLERS_FOUND || this == CONNECTION_FAILED
    
    /**
     * Get status color for UI display
     */
    val statusColor: StatusColor
        get() = when (this) {
            CONNECTED -> StatusColor.GREEN
            CONNECTED_TO_WIFI, READY -> StatusColor.BLUE
            DISCOVERING, CONNECTING -> StatusColor.YELLOW
            CONNECTION_FAILED, NO_CONTROLLERS_FOUND -> StatusColor.ORANGE
            DISCONNECTED, NO_WIFI, NETWORK_LOST, ERROR, PERMISSION_DENIED -> StatusColor.RED
        }
    
    /**
     * Get detailed description for UI
     */
    val description: String
        get() = when (this) {
            DISCONNECTED -> "No network connection available"
            NO_WIFI -> "Wi-Fi connection required for PC communication"
            CONNECTED_TO_WIFI -> "Connected to Wi-Fi network"
            PERMISSION_DENIED -> "Network permissions required"
            DISCOVERING -> "Scanning for PC controllers on local network"
            READY -> "Ready to connect to PC controllers"
            NO_CONTROLLERS_FOUND -> "No PC controllers found on network"
            CONNECTING -> "Establishing connection to PC controller"
            CONNECTED -> "Connected and communicating with PC controller"
            CONNECTION_FAILED -> "Unable to connect to PC controller"
            NETWORK_LOST -> "Wi-Fi connection lost"
            ERROR -> "Network error occurred"
        }
    
    /**
     * Get recommended action for user
     */
    val recommendedAction: String?
        get() = when (this) {
            DISCONNECTED, NO_WIFI -> "Connect to Wi-Fi network"
            PERMISSION_DENIED -> "Grant network permissions in settings"
            NO_CONTROLLERS_FOUND -> "Ensure PC controller is running and on same network"
            CONNECTION_FAILED -> "Check PC controller address and try again"
            NETWORK_LOST -> "Reconnect to Wi-Fi network"
            ERROR -> "Check network settings and try again"
            else -> null
        }
    
    /**
     * Color classification for UI display
     */
    enum class StatusColor {
        GREEN,    // Success/Connected
        BLUE,     // Ready/Available
        YELLOW,   // In Progress
        ORANGE,   // Warning
        RED       // Error/Disconnected
    }
    
    companion object {
        
        /**
         * Get all states that indicate network connectivity
         */
        fun getConnectedStates(): List<NetworkStatus> {
            return values().filter { it.isConnected }
        }
        
        /**
         * Get all error states
         */
        fun getErrorStates(): List<NetworkStatus> {
            return values().filter { it.isError }
        }
        
        /**
         * Get all states that allow discovery
         */
        fun getDiscoveryStates(): List<NetworkStatus> {
            return values().filter { it.canDiscover }
        }
        
        /**
         * Determine status from network and connection state
         */
        fun fromConnectionState(
            hasWifi: Boolean,
            hasInternet: Boolean,
            isDiscovering: Boolean,
            connectedControllers: Int
        ): NetworkStatus {
            return when {
                !hasWifi -> NO_WIFI
                !hasInternet -> CONNECTED_TO_WIFI
                connectedControllers > 0 -> CONNECTED
                isDiscovering -> DISCOVERING
                else -> READY
            }
        }
    }
}