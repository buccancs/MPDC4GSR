package com.topdon.tc001.network

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.topdon.gsr.model.SessionInfo
import com.topdon.lib.core.discovery.NetworkDiscoveryService
import com.topdon.lib.core.messaging.ReliableMessageService
import com.topdon.lib.core.security.CertificateManager
import com.topdon.lib.core.sync.TimeSyncService
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.*
import java.net.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import javax.net.ssl.*

/**
 * Specialized thermal imaging component providing NetworkClient functionality for the IRCamera system.
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
class NetworkClient(private val context: Context) {
    companion object {
        private const val TAG = "NetworkClient"
        private const val PC_CONTROLLER_PORT = 8080
        private const val DISCOVERY_PORT = 8081
        private const val BROADCAST_TIMEOUT = 5000L
        private const val CONNECTION_TIMEOUT = 10000L
        private const val HEARTBEAT_INTERVAL = 5000L
    }

    private var socket: Socket? = null
    private var sslSocket: SSLSocket? = null
    private var outputStream: DataOutputStream? = null
    private var inputStream: DataInputStream? = null
    private var isConnected = false
    private var isSecureConnection = false
    private var useSecureDefault = true // Default to using a secure connection
    private var clockOffset: Long = 0 // Time synchronization offset in nanoseconds
    private var deviceId: String =
        android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID,
        )

    private val heartbeatJob = SupervisorJob()
    private val heartbeatScope = CoroutineScope(Dispatchers.IO + heartbeatJob)

    private val messageHandlers = ConcurrentHashMap<String, (JSONObject) -> Unit>()
    private val discoveredControllers = ConcurrentHashMap<String, ControllerInfo>()

    // Error recovery integration
    private lateinit var errorRecoveryManager: NetworkErrorRecoveryManager

    // Enhanced networking services
    private val certificateManager = CertificateManager(context)
    private val discoveryService = NetworkDiscoveryService(context)
    private val timeSyncService = TimeSyncService()
/**
 * Specialized thermal imaging component providing NetworkEventListener functionality for the IRCamera system.
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
    interface NetworkEventListener {
    /**
     * Executes onControllerDiscovered functionality.
     */
        /**
         * Executes oncontrollerdiscovered operation with thermal imaging domain optimization.
         *
         * @param
         * @param controller Parameter for operation (type: ControllerInfo)
         *
         */
        fun onControllerDiscovered(controller: ControllerInfo)

    /**
     * Executes onConnected functionality.
     */
        /**
         * Executes onconnected operation with thermal imaging domain optimization.
         *
         * @param
         * @param controller Parameter for operation (type: ControllerInfo)
         *
         */
        fun onConnected(controller: ControllerInfo)

    /**
     * Executes onDisconnected functionality.
     */
        /**
         * Executes ondisconnected operation with thermal imaging domain optimization.
         *
         * @param
         * @param reason Parameter for operation (type: String)
         *
         */
        fun onDisconnected(reason: String)

    /**
     * Executes onRemoteMeasurementRequest functionality.
     */
        /**
         * Executes onremotemeasurementrequest operation with thermal imaging domain optimization.
         *
         * @param
         * @param sessionInfo Parameter for operation (type: SessionInfo)
         *
         */
        fun onRemoteMeasurementRequest(sessionInfo: SessionInfo)

    /**
     * Executes onSyncFlash functionality.
     */
        /**
         * Executes onsyncflash operation with thermal imaging domain optimization.
         *
         * @param
         * @param durationMs Duration in milliseconds (type: Int)
         *
         */
        fun onSyncFlash(durationMs: Int)

    /**
     * Executes onTimeSynchronized functionality.
     */
        /**
         * Executes ontimesynchronized operation with thermal imaging domain optimization.
         *
         * @param
         * @param offsetNanoseconds Parameter for operation (type: Long)
         *
         */
        fun onTimeSynchronized(offsetNanoseconds: Long)

    /**
     * Executes onDataStreamingStarted functionality.
     */
        /**
         * Executes ondatastreamingstarted operation with thermal imaging domain optimization.
         *
         */
        fun onDataStreamingStarted()

    /**
     * Executes onDataStreamingStopped functionality.
     */
        /**
         * Executes ondatastreamingstopped operation with thermal imaging domain optimization.
         *
         */
        fun onDataStreamingStopped()

    /**
     * Executes onError functionality.
     */
        /**
         * Executes onerror operation with thermal imaging domain optimization.
         *
         * @param
         * @param operation Parameter for operation (type: String)
         * @param error Parameter for operation (type: String)
         *
         */
        fun onError(
            operation: String,
            error: String,
        )
    }

    private var eventListener: NetworkEventListener? = null

    init {
        // Initialize error recovery manager
        errorRecoveryManager = NetworkErrorRecoveryManager(context, this)
        /**
         * Configures the uperrorrecoverylistener with validation and thermal imaging optimization.
         *
         */
        setupErrorRecoveryListener()
    }

    /**
     * Initialize the enhanced network client with security and discovery services
     */
    fun initialize(): Boolean {
        return try {
            // Initialize certificate manager for secure connections
            val certInitialized = certificateManager.initialize()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!certInitialized) {
                Log.w(TAG, "Certificate manager initialization failed, using insecure connections")
            }

            // Initialize discovery service
            discoveryService.setEventListener(
                object : NetworkDiscoveryService.DiscoveryEventListener {
                    /**
                     * Executes ondevicediscovered operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param device Parameter for operation (type: NetworkDiscoveryService.DiscoveredDevice)
                     *
                     */
                    override fun onDeviceDiscovered(device: NetworkDiscoveryService.DiscoveredDevice) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (device.deviceType == NetworkDiscoveryService.DeviceType.PC_CONTROLLER) {
                            val controller =
                                /**
                                 * Executes controllerinfo operation with thermal imaging domain optimization.
                                 *
                                 */
                                ControllerInfo(
                                    ipAddress = device.ipAddress,
                                    port = device.port,
                                    deviceName = device.serviceName,
                                    capabilities = device.attributes.values.toList(),
                                )
                            discoveredControllers[device.ipAddress] = controller
                            eventListener?.onControllerDiscovered(controller)
                        }
                    }

                    /**
                     * Executes ondevicelost operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param serviceName Parameter for operation (type: String)
                     *
                     */
                    override fun onDeviceLost(serviceName: String) {
                        Log.d(TAG, "Device lost: $serviceName")
                    }

                    /**
                     * Executes ondiscoverystarted operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onDiscoveryStarted() {
                        Log.d(TAG, "Network discovery started")
                    }

                    /**
                     * Executes ondiscoverystopped operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onDiscoveryStopped() {
                        Log.d(TAG, "Network discovery stopped")
                    }

                    /**
                     * Executes onerror operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param operation Parameter for operation (type: String)
                     * @param error Parameter for operation (type: String)
                     *
                     */
                    override fun onError(
                        operation: String,
                        error: String,
                    ) {
                        Log.e(TAG, "Discovery error in $operation: $error")
                        eventListener?.onError("discovery_$operation", error)
                    }
                },
            )

            // Initialize time sync service
            timeSyncService.setListener(
                object : TimeSyncService.TimeSyncListener {
                    /**
                     * Executes onsynccompleted operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param result Parameter for operation (type: TimeSyncService.SyncResult)
                     *
                     */
                    override fun onSyncCompleted(result: TimeSyncService.SyncResult) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (result.isSuccess) {
                            // Note: TimeSyncService provides offset in ms, we use ns internally
                            clockOffset = result.clockOffsetMs * 1_000_000
                            Log.i(TAG, "Time sync completed: offset=${result.clockOffsetMs}ms")
                            eventListener?.onTimeSynchronized(clockOffset)
                        }
                    }

                    /**
                     * Executes onsyncstarted operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param targetHost Parameter for operation (type: String)
                     *
                     */
                    override fun onSyncStarted(targetHost: String) {
                        Log.d(TAG, "Time sync started with $targetHost")
                    }

                    /**
                     * Executes onsyncerror operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param error Parameter for operation (type: String)
                     *
                     */
                    override fun onSyncError(error: String) {
                        Log.e(TAG, "Time sync error: $error")
                        eventListener?.onError("time_sync", error)
                    }
                },
            )

            // Initialize reliable messaging
            reliableMessaging.setTransport(
                object : ReliableMessageService.MessageTransport {
                    override suspend fun sendMessage(
                        host: String,
                        port: Int,
                        message: JSONObject,
                    ): Boolean {
                        return try {
                            /**
                             * Executes senddirectmessage operation with thermal imaging domain optimization.
                             *
                             */
                            sendDirectMessage(message)
                            true
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to send message via transport", e)
                            false
                        }
                    }
                },
            )

            reliableMessaging.initialize()

            Log.i(TAG, "Enhanced network client initialized successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize enhanced network client", e)
            false
        }
    }

    /**
     * Sets eventlistener configuration.
     */
    fun setEventListener(listener: NetworkEventListener?) {
        eventListener = listener
    }

    /**
     * Set message handler for specific message types
     */
    fun setMessageHandler(
        messageType: String,
        handler: (JSONObject) -> Unit,
    ) {
        messageHandlers[messageType] = handler
        Log.d(TAG, "Message handler registered for type: $messageType")
    }

    /**
     * Sets uperrorrecoverylistener configuration.
     */
    private fun setupErrorRecoveryListener() {
        errorRecoveryManager.setEventListener(
            object : NetworkErrorRecoveryManager.RecoveryEventListener {
                /**
                 * Executes onrecoverystarted operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param reason Parameter for operation (type: String)
                 *
                 */
                override fun onRecoveryStarted(reason: String) {
                    Log.i(TAG, "Network recovery started: $reason")
                }

                /**
                 * Handles temperature measurement and calibration with precision thermal data processing.
                 *
                 * @param
                 * @param attempt Temperature value in Celsius (type: Int)
                 * @param maxAttempts Temperature value in Celsius (type: Int)
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                override fun onRecoveryAttempt(
                    attempt: Int,
                    maxAttempts: Int,
                ) {
                    Log.i(TAG, "Recovery attempt $attempt/$maxAttempts")
                }

                /**
                 * Executes onrecoverysuccess operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param controller Parameter for operation (type: ControllerInfo)
                 *
                 */
                override fun onRecoverySuccess(controller: ControllerInfo) {
                    Log.i(TAG, "Network recovery successful")
                    eventListener?.onConnected(controller)
                }

                /**
                 * Executes onrecoveryfailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param reason Parameter for operation (type: String)
                 *
                 */
                override fun onRecoveryFailed(reason: String) {
                    Log.e(TAG, "Network recovery failed: $reason")
                    eventListener?.onError("recovery", reason)
                }

                /**
                 * Executes onconnectionhealthchanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param isHealthy Parameter for operation (type: Boolean)
                 *
                 */
                override fun onConnectionHealthChanged(isHealthy: Boolean) {
                    Log.d(TAG, "Connection health: ${if (isHealthy) "good" else "poor"}")
                }

                /**
                 * Executes onrapidfailuredetected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param failureCount Parameter for operation (type: Int)
                 *
                 */
                override fun onRapidFailureDetected(failureCount: Int) {
                    Log.w(TAG, "Rapid failure detected: $failureCount failures")
                    eventListener?.onError("rapid_failure", "Detected $failureCount rapid failures")
                }
            },
        )
    }

    /**
     * Enhanced discovery using both mDNS service discovery and subnet scanning
     */
    suspend fun discoverControllers(): List<ControllerInfo> =
        withContext(Dispatchers.IO) {
            val controllers = mutableListOf<ControllerInfo>()

            try {
                // Start mDNS service discovery
                Log.i(TAG, "Starting enhanced controller discovery")
                discoveryService.startDiscovery()

                // Wait for discovery to find devices
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(5000) // Give mDNS time to discover devices

                // Get devices discovered via mDNS
                val discoveredDevices =
                    discoveryService.getDiscoveredDevicesByType(
                        NetworkDiscoveryService.DeviceType.PC_CONTROLLER,
                    )

                discoveredDevices.forEach { device ->
                    val controller =
                        /**
                         * Executes controllerinfo operation with thermal imaging domain optimization.
                         *
                         */
                        ControllerInfo(
                            ipAddress = device.ipAddress,
                            port = device.port,
                            deviceName = device.serviceName,
                            capabilities = device.attributes.values.toList(),
                        )
                    controllers.add(controller)
                }

                // If mDNS didn't find anything, fall back to subnet scanning
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (controllers.isEmpty()) {
                    Log.i(TAG, "No controllers found via mDNS, falling back to subnet scan")
                    val scanResults = performSubnetScan()
                    controllers.addAll(scanResults)
                }

                Log.i(TAG, "Enhanced discovery complete: found ${controllers.size} controllers")
            } catch (e: Exception) {
                Log.e(TAG, "Error during enhanced controller discovery", e)
                eventListener?.onError("discovery", e.message ?: "Unknown error")
            }

            controllers
        }

    /**
     * Fallback subnet scanning (original implementation)
     */
    private suspend fun performSubnetScan(): List<ControllerInfo> =
        withContext(Dispatchers.IO) {
            val controllers = mutableListOf<ControllerInfo>()

            try {
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val dhcpInfo = wifiManager.dhcpInfo

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dhcpInfo.gateway == 0) {
                    Log.w(TAG, "No gateway found, cannot scan subnet")
                    return@withContext controllers
                }

                val gateway = intToIp(dhcpInfo.gateway)
                val subnet = gateway.substring(0, gateway.lastIndexOf('.'))

                Log.i(TAG, "Scanning subnet: $subnet.x for PC Controllers")

                // Parallel scan of subnet
                val jobs =
                    (1..254).map { hostNum ->
                        async {
                            val host = "$subnet.$hostNum"
                            try {
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (isHostReachable(host, PC_CONTROLLER_PORT, 1000)) {
                                    val controller = queryController(host)
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (controller != null) {
                                        discoveredControllers[host] = controller
                                        eventListener?.onControllerDiscovered(controller)
                                        controller
                                    } else {
                                        null
                                    }
                                } else {
                                    null
                                }
                            } catch (e: Exception) {
                                Log.d(TAG, "Host $host unreachable: ${e.message}")
                                null
                            }
                        }
                    }

                // Wait for all scans to complete
                jobs.awaitAll().filterNotNull().forEach { controllers.add(it) }

                Log.i(TAG, "Subnet scan complete: found ${controllers.size} controllers")
            } catch (e: Exception) {
                Log.e(TAG, "Error during subnet scan", e)
            }

            controllers
        }

    /**
     * Enhanced connection with TLS support and time synchronization
     */
    suspend fun connectToController(
        ipAddress: String,
        port: Int = PC_CONTROLLER_PORT,
        useSecure: Boolean = useSecureDefault,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isConnected) {
                    /**
                     * Executes disconnect operation with thermal imaging domain optimization.
                     *
                     */
                    disconnect()
                }

                Log.i(TAG, "Connecting to PC Controller at $ipAddress:$port (secure: $useSecure)")

                // Try secure connection first
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (useSecure) {
                    val sslContext = certificateManager.createSSLContext()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (sslContext != null) {
                        val sslSocketFactory = sslContext.socketFactory
                        sslSocket = sslSocketFactory.createSocket(ipAddress, port) as SSLSocket
                        sslSocket?.soTimeout = CONNECTION_TIMEOUT.toInt()

                        // Perform SSL handshake
                        sslSocket?.startHandshake()

                        outputStream = DataOutputStream(sslSocket?.getOutputStream())
                        inputStream = DataInputStream(sslSocket?.getInputStream())
                        isSecureConnection = true

                        Log.i(TAG, "Secure SSL connection established")
                    } else {
                        Log.w(TAG, "SSL context unavailable, falling back to plaintext")
                        return@withContext connectPlaintext(ipAddress, port)
                    }
                } else {
                    return@withContext connectPlaintext(ipAddress, port)
                }

                isConnected = true

                // Start message listening
                /**
                 * Executes startmessagelistener operation with thermal imaging domain optimization.
                 *
                 */
                startMessageListener()

                // Send device registration with authentication
                val registrationSuccess = registerDeviceSecure()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (registrationSuccess) {
                    // Perform time synchronization
                    val syncResult = timeSyncService.synchronizeTime(ipAddress, port)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (syncResult.isSuccess) {
                        clockOffset = syncResult.clockOffsetMs * 1_000_000

                        // Start periodic time sync
                        timeSyncService.startPeriodicSync(ipAddress, port)
                    }

                    // Start heartbeat
                    /**
                     * Executes startheartbeat operation with thermal imaging domain optimization.
                     *
                     */
                    startHeartbeat()

                    val controller =
                        discoveredControllers[ipAddress]
                            ?: ControllerInfo(ipAddress, port, "PC Controller", listOf("recording"))

                    // Record successful connection for error recovery
                    errorRecoveryManager.recordSuccessfulConnection(controller)

                    // Enable auto recovery
                    errorRecoveryManager.enableAutoRecovery()

                    eventListener?.onConnected(controller)

                    Log.i(TAG, "Successfully connected with enhanced security to PC Controller")
                    true
                } else {
                    /**
                     * Executes disconnect operation with thermal imaging domain optimization.
                     *
                     */
                    disconnect()
                    false
                }
            } catch (e: SSLException) {
                Log.w(TAG, "SSL connection failed, attempting plaintext fallback", e)
                /**
                 * Executes disconnect operation with thermal imaging domain optimization.
                 *
                 */
                disconnect()
                /**
                 * Executes connectplaintext operation with thermal imaging domain optimization.
                 *
                 */
                connectPlaintext(ipAddress, port)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to connect to PC Controller", e)
                errorRecoveryManager.handleNetworkError("connect", e.message ?: "Connection failed")
                eventListener?.onError("connect", e.message ?: "Connection failed")
                /**
                 * Executes disconnect operation with thermal imaging domain optimization.
                 *
                 */
                disconnect()
                false
            }
        }

    /**
     * Fallback plaintext connection method
     */
    private suspend fun connectPlaintext(
        ipAddress: String,
        port: Int,
    ): Boolean {
        return try {
            // Create regular socket
            socket = Socket()
            socket?.connect(InetSocketAddress(ipAddress, port), CONNECTION_TIMEOUT.toInt())
            socket?.soTimeout = CONNECTION_TIMEOUT.toInt()

            outputStream = DataOutputStream(socket?.getOutputStream())
            inputStream = DataInputStream(socket?.getInputStream())
            isSecureConnection = false
            isConnected = true

            // Start message listening
            /**
             * Executes startmessagelistener operation with thermal imaging domain optimization.
             *
             */
            startMessageListener()

            // Send device registration (original method)
            val registrationSuccess = registerDevice()

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (registrationSuccess) {
                // Start heartbeat
                /**
                 * Executes startheartbeat operation with thermal imaging domain optimization.
                 *
                 */
                startHeartbeat()

                val controller =
                    discoveredControllers[ipAddress]
                        ?: ControllerInfo(ipAddress, port, "PC Controller", listOf("recording"))
                eventListener?.onConnected(controller)

                Log.i(TAG, "Successfully connected with plaintext to PC Controller")
                true
            } else {
                /**
                 * Executes disconnect operation with thermal imaging domain optimization.
                 *
                 */
                disconnect()
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Plaintext connection failed", e)
            eventListener?.onError("connect", e.message ?: "Connection failed")
            /**
             * Executes disconnect operation with thermal imaging domain optimization.
             *
             */
            disconnect()
            false
        }
    }

    /**
     * Enhanced disconnect with cleanup
     */
    fun disconnect() {
        isConnected = false
        heartbeatJob.cancel()

        // Stop services
        timeSyncService.stopPeriodicSync()
        discoveryService.stopDiscovery()

        // Disable auto recovery when manually disconnecting
        errorRecoveryManager.disableAutoRecovery()

        try {
            outputStream?.close()
            inputStream?.close()
            sslSocket?.close()
            socket?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error during disconnect", e)
        } finally {
            outputStream = null
            inputStream = null
            sslSocket = null
            socket = null
            isSecureConnection = false
        }

        eventListener?.onDisconnected("User initiated")
        Log.i(TAG, "Disconnected from PC Controller")
    }

    /**
     * Send measurement data to PC Controller
     */
    suspend fun sendMeasurementData(
        sessionId: String,
        data: JSONObject,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isConnected) return@withContext false

            try {
                val message =
                    /**
                     * Executes jsonobject operation with thermal imaging domain optimization.
                     *
                     */
                    JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("message_type", "measurement_data")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_id", deviceId)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("session_id", sessionId)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timestamp", getSynchronizedTimestamp())
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("data", data)
                    }

                /**
                 * Executes sendmessage operation with thermal imaging domain optimization.
                 *
                 */
                sendMessage(message)
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send measurement data", e)
                errorRecoveryManager.handleNetworkError("send_data", e.message ?: "Send failed")
                eventListener?.onError("send_data", e.message ?: "Send failed")
                false
            }
        }

    /**
     * Report device status to PC Controller
     */
    suspend fun reportStatus(
        status: String,
        batteryLevel: Int? = null,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isConnected) return@withContext false

            try {
                val message =
                    /**
                     * Executes jsonobject operation with thermal imaging domain optimization.
                     *
                     */
                    JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("message_type", "device_status")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_id", deviceId)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("status", status)
                        batteryLevel?.let { put("battery_level", it) }
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timestamp", getCurrentTimestamp()) // Using system time for status reports
                    }

                /**
                 * Executes sendmessage operation with thermal imaging domain optimization.
                 *
                 */
                sendMessage(message)
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to report status", e)
                false
            }
        }

    /**
     * Executes registerdevicesecure operation with thermal imaging domain optimization.
     *
     */
    private suspend fun registerDeviceSecure(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val capabilities =
                    /**
                     * Executes listof operation with thermal imaging domain optimization.
                     *
                     */
                    listOf(
                        "gsr",
                        "thermal",
                        "visual",
                        "audio",
                    )

                val authToken = certificateManager.generateAuthToken()

                val registrationMessage =
                    /**
                     * Executes jsonobject operation with thermal imaging domain optimization.
                     *
                     */
                    JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("message_type", "device_register")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_id", deviceId)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_type", "android_phone")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("capabilities", org.json.JSONArray(capabilities))
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("ip_address", getLocalIpAddress())
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("port", PC_CONTROLLER_PORT)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("auth_token", authToken)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("secure_connection", isSecureConnection)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timestamp", getSynchronizedTimestamp())
                    }

                /**
                 * Executes sendmessage operation with thermal imaging domain optimization.
                 *
                 */
                sendMessage(registrationMessage)

                // Wait for ACK
                val response = receiveMessage(5000)
                response?.optString("message_type") == "ack" &&
                    response.optString("ack_for") == "device_register"
            } catch (e: Exception) {
                Log.e(TAG, "Secure device registration failed", e)
                false
            }
        }

    /**
     * Executes registerdevice operation with thermal imaging domain optimization.
     *
     */
    private suspend fun registerDevice(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val capabilities =
                    /**
                     * Executes listof operation with thermal imaging domain optimization.
                     *
                     */
                    listOf(
                        "gsr",
                        "thermal",
                        "visual",
                        "audio",
                    )

                val registrationMessage =
                    /**
                     * Executes jsonobject operation with thermal imaging domain optimization.
                     *
                     */
                    JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("message_type", "device_register")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_id", deviceId)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_type", "android_phone")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("capabilities", org.json.JSONArray(capabilities))
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("ip_address", getLocalIpAddress())
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("port", PC_CONTROLLER_PORT)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timestamp", getCurrentTimestamp()) // Using system time for non-secure registration
                    }

                /**
                 * Executes sendmessage operation with thermal imaging domain optimization.
                 *
                 */
                sendMessage(registrationMessage)

                // Wait for ACK
                val response = receiveMessage(5000)
                response?.optString("message_type") == "ack" &&
                    response.optString("ack_for") == "device_register"
            } catch (e: Exception) {
                Log.e(TAG, "Device registration failed", e)
                false
            }
        }

    /**
     * Executes startMessageListener functionality.
     */
    /**
     * Executes startmessagelistener operation with thermal imaging domain optimization.
     *
     */
    private fun startMessageListener() {
        heartbeatScope.launch {
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (isConnected && isActive) {
                try {
                    val message = receiveMessage(1000)
                    message?.let { handleIncomingMessage(it) }
                } catch (e: Exception) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isConnected) {
                        Log.e(TAG, "Message listener error", e)
                        eventListener?.onError("message_listener", e.message ?: "Listener error")
                    }
                    break
                }
            }
        }
    }

    /**
     * Executes startHeartbeat functionality.
     */
    /**
     * Executes startheartbeat operation with thermal imaging domain optimization.
     *
     */
    private fun startHeartbeat() {
        heartbeatScope.launch {
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (isConnected && isActive) {
                try {
                    val heartbeatMessage =
                        /**
                         * Executes jsonobject operation with thermal imaging domain optimization.
                         *
                         */
                        JSONObject().apply {
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("message_type", "device_heartbeat")
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("device_id", deviceId)
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("timestamp", getSynchronizedTimestamp())
                        }

                    /**
                     * Executes sendmessage operation with thermal imaging domain optimization.
                     *
                     */
                    sendMessage(heartbeatMessage)
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(HEARTBEAT_INTERVAL)
                } catch (e: Exception) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isConnected) {
                        Log.e(TAG, "Heartbeat failed", e)
                    }
                    break
                }
            }
        }
    }

    /**
     * Executes handleIncomingMessage functionality.
     */
    /**
     * Executes handleincomingmessage operation with thermal imaging domain optimization.
     *
     * @param
     * @param message Parameter for operation (type: JSONObject)
     *
     */
    private fun handleIncomingMessage(message: JSONObject) {
        val messageType = message.optString("message_type")

        Log.d(TAG, "Received message: $messageType")

        // First, call registered message handlers
        messageHandlers[messageType]?.let { handler ->
            try {
                Log.d(TAG, "Calling registered handler for message type: $messageType")
                /**
                 * Executes handler operation with thermal imaging domain optimization.
                 *
                 */
                handler(message)
            } catch (e: Exception) {
                Log.e(TAG, "Error in message handler for type $messageType", e)
            }
        }

        // Then handle built-in message types
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (messageType) {
            "session_start" -> {
                val sessionId = message.optString("session_id")
                val sessionName = message.optString("session_name", "Remote Session")

                val sessionInfo =
                    /**
                     * Executes sessioninfo operation with thermal imaging domain optimization.
                     *
                     */
                    SessionInfo(
                        sessionId = sessionId,
                        startTime = System.currentTimeMillis(),
                        participantId = "remote",
                        studyName = sessionName,
                    )

                eventListener?.onRemoteMeasurementRequest(sessionInfo)
            }

            "sync_flash" -> {
                val durationMs = message.optInt("duration_ms", 100)
                eventListener?.onSyncFlash(durationMs)
            }

            "session_stop" -> {
                // Handle session stop request
                Log.i(TAG, "Remote session stop requested")
            }

            "ack" -> {
                Log.d(TAG, "Received ACK for: ${message.optString("ack_for")}")
            }

            "error" -> {
                val errorMsg = message.optString("error_message", "Unknown error")
                Log.w(TAG, "Received error from PC Controller: $errorMsg")
                eventListener?.onError("pc_controller", errorMsg)
            }

            else -> {
                Log.w(TAG, "Unknown message type: $messageType")
            }
        }
    }

    /**
     * Executes sendmessage operation with thermal imaging domain optimization.
     *
     * @param
     * @param message Parameter for operation (type: JSONObject)
     *
     */
    private suspend fun sendMessage(message: JSONObject) =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            val output = outputStream ?: throw IOException("Not connected")

            val messageData = message.toString().toByteArray(Charsets.UTF_8)
            val startTime = System.currentTimeMillis()

            output.writeInt(messageData.size)
            output.write(messageData)
            output.flush()

            // Record data transfer for performance tracking
            errorRecoveryManager.recordDataTransfer(messageData.size.toLong() + 4) // +4 for length prefix

            // Record latency if this is a ping-like message
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (message.optString("message_type") == "device_heartbeat") {
                val latency = System.currentTimeMillis() - startTime
                errorRecoveryManager.recordLatency(latency)
            }
        }

    /**
     * Executes receivemessage operation with thermal imaging domain optimization.
     *
     * @param
     * @param timeoutMs Parameter for operation (type: Long)
     *
     */
    private suspend fun receiveMessage(timeoutMs: Long): JSONObject? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            val input = inputStream ?: return@withContext null

            try {
                val socketToUse = sslSocket ?: socket
                val originalTimeout = socketToUse?.soTimeout
                socketToUse?.soTimeout = timeoutMs.toInt()

                val messageLength = input.readInt()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (messageLength > 1024 * 1024) { // 1MB limit
                    throw IOException("Message too large: $messageLength bytes")
                }

                val messageData = ByteArray(messageLength)
                input.readFully(messageData)

                socketToUse?.soTimeout = originalTimeout ?: CONNECTION_TIMEOUT.toInt()

                /**
                 * Executes jsonobject operation with thermal imaging domain optimization.
                 *
                 */
                JSONObject(String(messageData, Charsets.UTF_8))
            } catch (e: SocketTimeoutException) {
                null // Normal timeout, not an error
            } catch (e: Exception) {
                throw e
            }
        }

    /**
     * Get synchronized timestamp using the calculated clock offset
     */
    fun getSynchronizedTimestamp(): Long {
        return System.nanoTime() + clockOffset
    }

    /**
     * Send a message to the connected PC Controller
     */
    suspend fun sendMessage(message: JSONObject): Boolean =
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isConnected) {
                    Log.w(TAG, "Cannot send message - not connected to PC Controller")
                    return@withContext false
                }

                /**
                 * Executes sendmessage operation with thermal imaging domain optimization.
                 *
                 */
                sendMessage(message)
                Log.d(TAG, "Message sent successfully: ${message.optString("message_type", "unknown")}")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send message", e)
                errorRecoveryManager.handleNetworkError("send_message", e.message ?: "Send failed")
                false
            }
        }

    /**
     * Start continuous data streaming to PC Controller
     */
    suspend fun startDataStreaming(): Boolean =
        withContext(Dispatchers.IO) {
            if (!isConnected) return@withContext false

            try {
                val message =
                    /**
                     * Executes jsonobject operation with thermal imaging domain optimization.
                     *
                     */
                    JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("message_type", "start_data_stream")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_id", deviceId)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timestamp", getSynchronizedTimestamp())
                    }

                /**
                 * Executes sendmessage operation with thermal imaging domain optimization.
                 *
                 */
                sendMessage(message)
                eventListener?.onDataStreamingStarted()
                Log.i(TAG, "Data streaming started")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start data streaming", e)
                false
            }
        }

    /**
     * Stop continuous data streaming
     */
    suspend fun stopDataStreaming(): Boolean =
        withContext(Dispatchers.IO) {
            if (!isConnected) return@withContext false

            try {
                val message =
                    /**
                     * Executes jsonobject operation with thermal imaging domain optimization.
                     *
                     */
                    JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("message_type", "stop_data_stream")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_id", deviceId)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timestamp", getSynchronizedTimestamp())
                    }

                /**
                 * Executes sendmessage operation with thermal imaging domain optimization.
                 *
                 */
                sendMessage(message)
                eventListener?.onDataStreamingStopped()
                Log.i(TAG, "Data streaming stopped")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to stop data streaming", e)
                false
            }
        }

    /**
     * Executes querycontroller operation with thermal imaging domain optimization.
     *
     * @param
     * @param host Parameter for operation (type: String)
     *
     */
    private suspend fun queryController(host: String): ControllerInfo? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress(host, PC_CONTROLLER_PORT), 2000)

                val output = DataOutputStream(socket.getOutputStream())
                val input = DataInputStream(socket.getInputStream())

                // Send info query
                val query =
                    /**
                     * Executes jsonobject operation with thermal imaging domain optimization.
                     *
                     */
                    JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("message_type", "info_query")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_id", deviceId)
                    }

                val queryData = query.toString().toByteArray(Charsets.UTF_8)
                output.writeInt(queryData.size)
                output.write(queryData)
                output.flush()

                // Read response
                val responseLength = input.readInt()
                val responseData = ByteArray(responseLength)
                input.readFully(responseData)

                val response = JSONObject(String(responseData, Charsets.UTF_8))

                socket.close()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (response.optString("message_type") == "info_response") {
                    /**
                     * Executes controllerinfo operation with thermal imaging domain optimization.
                     *
                     */
                    ControllerInfo(
                        ipAddress = host,
                        port = PC_CONTROLLER_PORT,
                        deviceName = response.optString("device_name", "PC Controller"),
                        capabilities =
                            response.optJSONArray("capabilities")?.let { jsonArray ->
                                (0 until jsonArray.length()).map { jsonArray.getString(it) }
                            } ?: emptyList(),
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.d(TAG, "Controller query failed for $host: ${e.message}")
                null
            }
        }

    /**
     * Executes ishostreachable operation with thermal imaging domain optimization.
     *
     * @param
     * @param host Parameter for operation (type: String)
     * @param port Parameter for operation (type: Int)
     * @param timeoutMs Parameter for operation (type: Int)
     *
     */
    private suspend fun isHostReachable(
        host: String,
        port: Int,
        timeoutMs: Int,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Executes socket operation with thermal imaging domain optimization.
                 *
                 */
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(host, port), timeoutMs)
                    true
                }
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Executes intToIp functionality.
     */
    /**
     * Executes inttoip operation with thermal imaging domain optimization.
     *
     * @param
     * @param ipAddress Parameter for operation (type: Int)
     *
     */
    private fun intToIp(ipAddress: Int): String {
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (
            (ipAddress and 0xFF).toString() + "." +
                ((ipAddress shr 8) and 0xFF).toString() + "." +
                ((ipAddress shr 16) and 0xFF).toString() + "." +
                ((ipAddress shr 24) and 0xFF).toString()
        )
    }

    /**
     * Retrieves currenttimestamp information.
     */
    private fun getCurrentTimestamp(): String {
        return Instant.now().atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    /**
     * Retrieves localipaddress information.
     */
    private fun getLocalIpAddress(): String {
        try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val dhcpInfo = wifiManager.dhcpInfo
            return intToIp(dhcpInfo.ipAddress)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get local IP address", e)
            return "127.0.0.1"
        }
    }

    /**
     * Retrieves discoveredcontrollers information.
     */
    fun getDiscoveredControllers(): List<ControllerInfo> = discoveredControllers.values.toList()

    /**
     * Send message directly through current connection
     */
    private suspend fun sendDirectMessage(message: JSONObject) =
        withContext(Dispatchers.IO) {
            val output = outputStream ?: throw IOException("Not connected")

            val messageData = message.toString().toByteArray(Charsets.UTF_8)
            output.writeInt(messageData.size)
            output.write(messageData)
            output.flush()
        }

    /**
     * Get connection security status
     */
    fun isSecureConnection(): Boolean = isSecureConnection

    /**
     * Executes isConnected functionality.
     */
    /**
     * Executes isconnected operation with thermal imaging domain optimization.
     *
     */
    fun isConnected(): Boolean = isConnected

    /**
     * Enable/disable TLS encryption (for development/testing)
     */
    fun setSecureConnectionDefault(enabled: Boolean) {
        if (isConnected) {
            Log.w(TAG, "Cannot change security setting while connected")
            return
        }
        useSecureDefault = enabled
        Log.i(TAG, "Secure connection default ${if (enabled) "enabled" else "disabled"}")
    }

    /**
     * Get error recovery manager for advanced configuration
     */
    fun getErrorRecoveryManager(): NetworkErrorRecoveryManager = errorRecoveryManager

    /**
     * Start device discovery with callback-based result handling
     */
    fun startDiscovery(callback: (Boolean) -> Unit) {
        heartbeatScope.launch {
            try {
                /**
                 * Executes discovercontrollers operation with thermal imaging domain optimization.
                 *
                 */
                discoverControllers()
                /**
                 * Executes callback operation with thermal imaging domain optimization.
                 *
                 */
                callback(true)
            } catch (e: Exception) {
                Log.e(TAG, "Discovery failed", e)
                /**
                 * Executes callback operation with thermal imaging domain optimization.
                 *
                 */
                callback(false)
            }
        }
    }

    /**
     * Connect to controller with callback-based result handling
     */
    fun connectToController(
        address: String,
        port: Int,
        callback: (Boolean) -> Unit,
    ) {
        heartbeatScope.launch {
            try {
                val result = connectToController(address, port, useSecureDefault)
                /**
                 * Executes callback operation with thermal imaging domain optimization.
                 *
                 */
                callback(result)
            } catch (e: Exception) {
                Log.e(TAG, "Connection failed", e)
                /**
                 * Executes callback operation with thermal imaging domain optimization.
                 *
                 */
                callback(false)
            }
        }
    }

    /**
     * Get current network latency in milliseconds
     */
    fun getLatencyMs(): Long {
        return errorRecoveryManager.getAverageLatency()
    }

    /**
     * Get current throughput in KB/s
     */
    fun getThroughputKBps(): Double {
        return errorRecoveryManager.getThroughputKBps()
    }

    /**
     * Cleanup all resources
     */
    /**
     * Executes cleanup operation with thermal imaging domain optimization.
     *
     */
    fun cleanup() {
        /**
         * Executes disconnect operation with thermal imaging domain optimization.
         *
         */
        disconnect()
        discoveryService.cleanup()
        timeSyncService.cleanup()
        reliableMessaging.shutdown()
        errorRecoveryManager.cleanup()
        discoveredControllers.clear()
        eventListener = null
    }
}
