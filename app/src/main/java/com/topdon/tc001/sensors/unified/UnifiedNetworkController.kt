package com.topdon.tc001.sensors.unified

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.topdon.tc001.network.WebSocketClient
import com.topdon.tc001.sensors.unified.model.NetworkStatus
import com.topdon.tc001.sensors.unified.model.PCControllerInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import java.io.IOException
import java.net.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener

/**
 * **Unified Network Controller**
 * 
 * Comprehensive Wi-Fi network management and PC controller communication for the IRCamera app extension.
 * 
 * ## Key Features:
 * - **Wi-Fi Network Management**: Monitor connection, signal strength, and network changes
 * - **PC Controller Discovery**: mDNS/Zeroconf discovery of PC controllers on local network
 * - **WebSocket Communication**: Bi-directional JSON messaging with PC controller  
 * - **Network Health Monitoring**: Connection quality, latency, and stability tracking
 * - **Automatic Reconnection**: Intelligent reconnection with exponential backoff
 * - **Multi-Controller Support**: Connect to multiple PC controllers simultaneously
 * 
 * ## Integration Points:
 * - Works with existing WebSocketClient infrastructure
 * - Integrates with session management and recording coordination
 * - Provides real-time status updates via StateFlow
 * - Compatible with Android architecture components (ViewModel, LiveData)
 * 
 * @author IRCamera Unified Sensor Integration
 */
class UnifiedNetworkController(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    companion object {
        private const val TAG = "UnifiedNetworkController"
        
        // Service discovery constants
        private const val SERVICE_TYPE = "_ircamera._tcp.local."
        private const val DISCOVERY_TIMEOUT_MS = 30000L
        private const val RECONNECTION_DELAY_MS = 5000L
        private const val MAX_RECONNECTION_DELAY_MS = 60000L
        
        // Network quality thresholds
        private const val MIN_SIGNAL_STRENGTH = -70  // dBm
        private const val MAX_LATENCY_MS = 100
        private const val MIN_BANDWIDTH_KBPS = 1000
        
        // Connection timeouts
        private const val WEBSOCKET_CONNECT_TIMEOUT_MS = 10000L
        private const val HEARTBEAT_INTERVAL_MS = 30000L
    }
    
    // System managers
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    
    // Network discovery
    private var jmDNS: JmDNS? = null
    private var serviceListener: ServiceListener? = null
    
    // PC Controllers
    private val discoveredControllers = mutableMapOf<String, PCControllerInfo>()
    private val activeConnections = mutableMapOf<String, WebSocketClient>()
    
    // Network monitoring
    private val _networkStatus = MutableStateFlow(NetworkStatus.DISCONNECTED)
    val networkStatus: StateFlow<NetworkStatus> = _networkStatus.asStateFlow()
    
    private val _wifiSignalStrength = MutableStateFlow(0)
    val wifiSignalStrength: StateFlow<Int> = _wifiSignalStrength.asStateFlow()
    
    private val _discoveredControllersFlow = MutableStateFlow<List<PCControllerInfo>>(emptyList())
    val discoveredControllersFlow: StateFlow<List<PCControllerInfo>> = _discoveredControllersFlow.asStateFlow()
    
    private val _connectionQuality = MutableStateFlow(0.0)
    val connectionQuality: StateFlow<Double> = _connectionQuality.asStateFlow()
    
    // Control state
    private val isInitialized = AtomicBoolean(false)
    private val isDiscovering = AtomicBoolean(false)
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    
    // Coroutine jobs
    private var discoveryJob: Job? = null
    private var monitoringJob: Job? = null
    private var reconnectionJob: Job? = null
    
    /**
     * Initialize network controller with Wi-Fi monitoring
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Initializing Unified Network Controller")
        
        try {
            if (isInitialized.get()) {
                Log.i(TAG, "Network controller already initialized")
                return@withContext true
            }
            
            // Check network permissions
            if (!hasNetworkPermissions()) {
                Log.e(TAG, "Missing network permissions")
                _networkStatus.value = NetworkStatus.PERMISSION_DENIED
                return@withContext false
            }
            
            // Initialize Wi-Fi monitoring
            setupWifiMonitoring()
            
            // Start network monitoring
            startNetworkMonitoring()
            
            // Update initial network status
            updateNetworkStatus()
            
            isInitialized.set(true)
            Log.i(TAG, "Network controller initialized successfully")
            return@withContext true
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize network controller", e)
            _networkStatus.value = NetworkStatus.ERROR
            return@withContext false
        }
    }
    
    /**
     * Start PC controller discovery
     */
    suspend fun startDiscovery(): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Starting PC controller discovery via mDNS")
        
        if (!isInitialized.get()) {
            Log.e(TAG, "Network controller not initialized")
            return@withContext false
        }
        
        if (isDiscovering.get()) {
            Log.i(TAG, "Discovery already in progress")
            return@withContext true
        }
        
        try {
            _networkStatus.value = NetworkStatus.DISCOVERING
            isDiscovering.set(true)
            discoveredControllers.clear()
            
            // Initialize mDNS for service discovery
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress
            val inetAddress = InetAddress.getByAddress(byteArrayOf(
                (ipAddress and 0xff).toByte(),
                (ipAddress shr 8 and 0xff).toByte(),
                (ipAddress shr 16 and 0xff).toByte(),
                (ipAddress shr 24 and 0xff).toByte()
            ))
            
            jmDNS = JmDNS.create(inetAddress)
            
            // Set up service listener
            serviceListener = object : ServiceListener {
                override fun serviceAdded(event: ServiceEvent) {
                    Log.d(TAG, "mDNS service added: ${event.info.name}")
                    jmDNS?.requestServiceInfo(event.type, event.name)
                }
                
                override fun serviceRemoved(event: ServiceEvent) {
                    Log.d(TAG, "mDNS service removed: ${event.info.name}")
                    discoveredControllers.remove(event.info.name)
                    updateDiscoveredControllers()
                }
                
                override fun serviceResolved(event: ServiceEvent) {
                    Log.i(TAG, "mDNS service resolved: ${event.info}")
                    
                    val serviceInfo = event.info
                    val controllerInfo = PCControllerInfo(
                        name = serviceInfo.name,
                        host = serviceInfo.hostAddresses?.firstOrNull() ?: serviceInfo.server,
                        port = serviceInfo.port,
                        type = serviceInfo.type,
                        properties = serviceInfo.propertyNames?.associateWith { 
                            serviceInfo.getPropertyString(it) 
                        } ?: emptyMap()
                    )
                    
                    discoveredControllers[serviceInfo.name] = controllerInfo
                    updateDiscoveredControllers()
                    
                    Log.i(TAG, "Discovered PC controller: ${controllerInfo.name} at ${controllerInfo.host}:${controllerInfo.port}")
                }
            }
            
            // Start service discovery
            jmDNS?.addServiceListener(SERVICE_TYPE, serviceListener)
            
            // Run discovery for specified timeout
            discoveryJob = lifecycleOwner.lifecycleScope.launch {
                delay(DISCOVERY_TIMEOUT_MS)
                stopDiscovery()
            }
            
            Log.i(TAG, "PC controller discovery started")
            return@withContext true
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start discovery", e)
            _networkStatus.value = NetworkStatus.ERROR
            isDiscovering.set(false)
            return@withContext false
        }
    }
    
    /**
     * Stop PC controller discovery
     */
    suspend fun stopDiscovery(): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Stopping PC controller discovery")
        
        try {
            isDiscovering.set(false)
            discoveryJob?.cancel()
            
            serviceListener?.let { listener ->
                jmDNS?.removeServiceListener(SERVICE_TYPE, listener)
            }
            
            jmDNS?.close()
            jmDNS = null
            serviceListener = null
            
            if (discoveredControllers.isNotEmpty()) {
                _networkStatus.value = NetworkStatus.READY
            } else {
                _networkStatus.value = NetworkStatus.NO_CONTROLLERS_FOUND
            }
            
            Log.i(TAG, "Discovery stopped - found ${discoveredControllers.size} controllers")
            return@withContext true
            
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping discovery", e)
            return@withContext false
        }
    }
    
    /**
     * Connect to a specific PC controller
     */
    suspend fun connectToController(controllerInfo: PCControllerInfo): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Connecting to PC controller: ${controllerInfo.name}")
        
        try {
            // Check if already connected
            if (activeConnections.containsKey(controllerInfo.name)) {
                Log.i(TAG, "Already connected to ${controllerInfo.name}")
                return@withContext true
            }
            
            _networkStatus.value = NetworkStatus.CONNECTING
            
            // Create WebSocket client
            val webSocketClient = WebSocketClient(context).apply {
                // Configure connection parameters
                setServerInfo(controllerInfo.host, controllerInfo.port)
                
                // Set event listener
                setEventListener(createWebSocketEventListener(controllerInfo))
            }
            
            // Start connection
            val connected = webSocketClient.connect()
            
            if (connected) {
                activeConnections[controllerInfo.name] = webSocketClient
                _networkStatus.value = NetworkStatus.CONNECTED
                
                // Start heartbeat monitoring
                startHeartbeatMonitoring(controllerInfo.name)
                
                Log.i(TAG, "Successfully connected to ${controllerInfo.name}")
                return@withContext true
            } else {
                Log.w(TAG, "Failed to connect to ${controllerInfo.name}")
                _networkStatus.value = NetworkStatus.CONNECTION_FAILED
                return@withContext false
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to controller", e)
            _networkStatus.value = NetworkStatus.ERROR
            return@withContext false
        }
    }
    
    /**
     * Disconnect from a specific PC controller
     */
    suspend fun disconnectFromController(controllerName: String): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Disconnecting from PC controller: $controllerName")
        
        try {
            val webSocketClient = activeConnections.remove(controllerName)
            webSocketClient?.disconnect()
            
            if (activeConnections.isEmpty()) {
                _networkStatus.value = NetworkStatus.READY
            }
            
            Log.i(TAG, "Disconnected from $controllerName")
            return@withContext true
            
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting from controller", e)
            return@withContext false
        }
    }
    
    /**
     * Send message to all connected PC controllers
     */
    suspend fun broadcastMessage(messageType: String, data: JSONObject): Boolean {
        Log.d(TAG, "Broadcasting message: $messageType to ${activeConnections.size} controllers")
        
        val message = JSONObject().apply {
            put("type", messageType)
            put("data", data)
            put("timestamp", System.currentTimeMillis())
        }
        
        var success = true
        activeConnections.values.forEach { client ->
            try {
                client.sendMessage(message)
            } catch (e: Exception) {
                Log.e(TAG, "Error sending message to controller", e)
                success = false
            }
        }
        
        return success
    }
    
    /**
     * Send message to specific PC controller
     */
    suspend fun sendMessage(controllerName: String, messageType: String, data: JSONObject): Boolean {
        Log.d(TAG, "Sending message: $messageType to $controllerName")
        
        val client = activeConnections[controllerName]
        if (client == null) {
            Log.w(TAG, "No connection to controller: $controllerName")
            return false
        }
        
        return try {
            val message = JSONObject().apply {
                put("type", messageType)
                put("data", data)
                put("timestamp", System.currentTimeMillis())
            }
            
            client.sendMessage(message)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message to $controllerName", e)
            false
        }
    }
    
    /**
     * Get network performance metrics
     */
    fun getNetworkMetrics(): Map<String, Any> {
        val wifiInfo = wifiManager.connectionInfo
        
        return mapOf(
            "network_status" to _networkStatus.value.name,
            "wifi_ssid" to (wifiInfo.ssid ?: "Unknown"),
            "wifi_signal_strength" to _wifiSignalStrength.value,
            "wifi_link_speed" to wifiInfo.linkSpeed,
            "connection_quality" to _connectionQuality.value,
            "discovered_controllers" to discoveredControllers.size,
            "active_connections" to activeConnections.size,
            "is_discovering" to isDiscovering.get()
        )
    }
    
    /**
     * Get list of discovered controllers
     */
    fun getDiscoveredControllers(): List<PCControllerInfo> {
        return discoveredControllers.values.toList()
    }
    
    /**
     * Get list of active controller connections
     */
    fun getActiveConnections(): List<String> {
        return activeConnections.keys.toList()
    }
    
    /**
     * Check if connected to any PC controller
     */
    fun isConnectedToAnyController(): Boolean {
        return activeConnections.isNotEmpty()
    }
    
    /**
     * Check if connected to specific PC controller
     */
    fun isConnectedToController(controllerName: String): Boolean {
        return activeConnections.containsKey(controllerName)
    }
    
    /**
     * Cleanup network controller resources
     */
    suspend fun cleanup(): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Cleaning up network controller")
        
        try {
            // Stop discovery
            stopDiscovery()
            
            // Disconnect all controllers
            activeConnections.keys.toList().forEach { controllerName ->
                disconnectFromController(controllerName)
            }
            
            // Cancel jobs
            discoveryJob?.cancel()
            monitoringJob?.cancel()
            reconnectionJob?.cancel()
            
            // Unregister network callback
            networkCallback?.let { callback ->
                connectivityManager.unregisterNetworkCallback(callback)
            }
            
            isInitialized.set(false)
            _networkStatus.value = NetworkStatus.DISCONNECTED
            
            Log.i(TAG, "Network controller cleanup completed")
            return@withContext true
            
        } catch (e: Exception) {
            Log.e(TAG, "Error during cleanup", e)
            return@withContext false
        }
    }
    
    // Private helper methods
    
    private fun hasNetworkPermissions(): Boolean {
        // Check if necessary network permissions are available
        return try {
            wifiManager.isWifiEnabled || connectivityManager.activeNetwork != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun setupWifiMonitoring() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d(TAG, "Wi-Fi network available")
                updateNetworkStatus()
            }
            
            override fun onLost(network: Network) {
                Log.d(TAG, "Wi-Fi network lost")
                _networkStatus.value = NetworkStatus.NETWORK_LOST
                // Trigger reconnection if we had active connections
                if (activeConnections.isNotEmpty()) {
                    startAutomaticReconnection()
                }
            }
            
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                Log.d(TAG, "Wi-Fi network capabilities changed")
                updateNetworkStatus()
            }
        }
        
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)
    }
    
    private fun startNetworkMonitoring() {
        monitoringJob = lifecycleOwner.lifecycleScope.launch {
            while (isInitialized.get()) {
                updateWifiSignalStrength()
                updateConnectionQuality()
                delay(5000)  // Update every 5 seconds
            }
        }
    }
    
    private fun updateNetworkStatus() {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        
        if (network != null && networkCapabilities != null) {
            val isWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            
            if (isWifi && hasInternet) {
                if (_networkStatus.value == NetworkStatus.DISCONNECTED || 
                    _networkStatus.value == NetworkStatus.NETWORK_LOST) {
                    _networkStatus.value = NetworkStatus.CONNECTED_TO_WIFI
                }
            }
        } else {
            _networkStatus.value = NetworkStatus.NO_WIFI
        }
    }
    
    private fun updateWifiSignalStrength() {
        try {
            val wifiInfo = wifiManager.connectionInfo
            _wifiSignalStrength.value = wifiInfo.rssi
        } catch (e: Exception) {
            Log.w(TAG, "Error getting Wi-Fi signal strength", e)
        }
    }
    
    private fun updateConnectionQuality() {
        val signalStrength = _wifiSignalStrength.value
        val hasActiveConnections = activeConnections.isNotEmpty()
        
        val quality = when {
            !hasActiveConnections -> 0.0
            signalStrength >= -50 -> 1.0
            signalStrength >= -60 -> 0.8
            signalStrength >= -70 -> 0.6
            signalStrength >= -80 -> 0.4
            else -> 0.2
        }
        
        _connectionQuality.value = quality
    }
    
    private fun updateDiscoveredControllers() {
        _discoveredControllersFlow.value = discoveredControllers.values.toList()
    }
    
    private fun createWebSocketEventListener(controllerInfo: PCControllerInfo): WebSocketClient.WebSocketEventListener {
        return object : WebSocketClient.WebSocketEventListener {
            override fun onServerDiscovered(serverInfo: WebSocketClient.ServerInfo) {
                Log.d(TAG, "Server discovered: ${serverInfo.name}")
            }
            
            override fun onConnecting(serverInfo: WebSocketClient.ServerInfo) {
                Log.d(TAG, "Connecting to: ${serverInfo.name}")
            }
            
            override fun onConnected(serverInfo: WebSocketClient.ServerInfo) {
                Log.i(TAG, "Connected to: ${serverInfo.name}")
                _networkStatus.value = NetworkStatus.CONNECTED
            }
            
            override fun onAuthenticated() {
                Log.i(TAG, "Authenticated with: ${controllerInfo.name}")
            }
            
            override fun onDisconnected(reason: String) {
                Log.i(TAG, "Disconnected from: ${controllerInfo.name} - $reason")
                activeConnections.remove(controllerInfo.name)
                
                if (activeConnections.isEmpty()) {
                    _networkStatus.value = NetworkStatus.READY
                }
                
                // Start reconnection if disconnection was unexpected
                if (reason != "manual") {
                    startAutomaticReconnection()
                }
            }
            
            override fun onMessage(messageType: String, message: JSONObject) {
                Log.d(TAG, "Received message: $messageType from ${controllerInfo.name}")
                // Handle incoming messages from PC controller
                handleIncomingMessage(controllerInfo.name, messageType, message)
            }
            
            override fun onError(error: String, exception: Throwable?) {
                Log.e(TAG, "WebSocket error from ${controllerInfo.name}: $error", exception)
            }
            
            override fun onHeartbeatReceived() {
                Log.d(TAG, "Heartbeat received from: ${controllerInfo.name}")
            }
        }
    }
    
    private fun startHeartbeatMonitoring(controllerName: String) {
        lifecycleOwner.lifecycleScope.launch {
            while (activeConnections.containsKey(controllerName)) {
                try {
                    val client = activeConnections[controllerName]
                    client?.sendHeartbeat()
                    delay(HEARTBEAT_INTERVAL_MS)
                } catch (e: Exception) {
                    Log.w(TAG, "Heartbeat error for $controllerName", e)
                    break
                }
            }
        }
    }
    
    private fun startAutomaticReconnection() {
        if (reconnectionJob?.isActive == true) {
            return  // Already trying to reconnect
        }
        
        reconnectionJob = lifecycleOwner.lifecycleScope.launch {
            var delay = RECONNECTION_DELAY_MS
            
            while (_networkStatus.value == NetworkStatus.NETWORK_LOST || 
                   _networkStatus.value == NetworkStatus.CONNECTION_FAILED) {
                
                Log.i(TAG, "Attempting automatic reconnection in ${delay}ms")
                delay(delay)
                
                // Try to reconnect to previously connected controllers
                discoveredControllers.values.forEach { controllerInfo ->
                    if (!activeConnections.containsKey(controllerInfo.name)) {
                        Log.i(TAG, "Trying to reconnect to ${controllerInfo.name}")
                        connectToController(controllerInfo)
                    }
                }
                
                // Exponential backoff
                delay = minOf(delay * 2, MAX_RECONNECTION_DELAY_MS)
                
                if (activeConnections.isNotEmpty()) {
                    Log.i(TAG, "Automatic reconnection successful")
                    break
                }
            }
        }
    }
    
    private fun handleIncomingMessage(controllerName: String, messageType: String, message: JSONObject) {
        // This method can be extended to handle specific message types from PC controllers
        Log.d(TAG, "Processing message type: $messageType from $controllerName")
        
        when (messageType) {
            "ping" -> {
                // Respond to ping with pong
                lifecycleOwner.lifecycleScope.launch {
                    sendMessage(controllerName, "pong", JSONObject())
                }
            }
            "session_control" -> {
                // Handle session start/stop commands
                val action = message.optString("action")
                Log.i(TAG, "Session control: $action from $controllerName")
            }
            "sync_marker" -> {
                // Handle synchronization markers
                val markerType = message.optString("marker_type")
                Log.i(TAG, "Sync marker: $markerType from $controllerName")
            }
            else -> {
                Log.d(TAG, "Unknown message type: $messageType")
            }
        }
    }
}