package com.topdon.gsr.network

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.topdon.gsr.model.SessionInfo
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.*
import java.net.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
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
    private var useTLS = true // Enable TLS by default
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

    // Authentication manager
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

        // Authentication events
    /**
     * Executes onPairingRequested functionality.
     */
        /**
         * Executes onpairingrequested operation with thermal imaging domain optimization.
         *
         * @param
         * @param controllerId Parameter for operation (type: String)
         * @param controllerName Parameter for operation (type: String)
         *
         */
        fun onPairingRequested(
            controllerId: String,
            controllerName: String,
        )

    /**
     * Executes onPairingCompleted functionality.
     */
        /**
         * Executes onpairingcompleted operation with thermal imaging domain optimization.
         *
         * @param
         * @param controllerId Parameter for operation (type: String)
         * @param success Parameter for operation (type: Boolean)
         *
         */
        fun onPairingCompleted(
            controllerId: String,
            success: Boolean,
        )

    /**
     * Executes onAuthenticationRequired functionality.
     */
        /**
         * Executes onauthenticationrequired operation with thermal imaging domain optimization.
         *
         * @param
         * @param controllerId Parameter for operation (type: String)
         *
         */
        fun onAuthenticationRequired(controllerId: String)
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
        /**
         * Configures the upauthenticationlistener with validation and thermal imaging optimization.
         *
         */
        setupAuthenticationListener()
    }

    /**
     * Sets eventlistener configuration.
     */
    fun setEventListener(listener: NetworkEventListener?) {
        eventListener = listener
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
     * Sets upauthenticationlistener configuration.
     */
    private fun setupAuthenticationListener() {
        authManager.setAuthEventListener(
            object : DeviceAuthenticationManager.AuthEventListener {
                /**
                 * Executes onpairingrequested operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param controllerId Parameter for operation (type: String)
                 * @param controllerName Parameter for operation (type: String)
                 *
                 */
                override fun onPairingRequested(
                    controllerId: String,
                    controllerName: String,
                ) {
                    eventListener?.onPairingRequested(controllerId, controllerName)
                }

                /**
                 * Executes onpairingcompleted operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param controllerId Parameter for operation (type: String)
                 * @param success Parameter for operation (type: Boolean)
                 *
                 */
                override fun onPairingCompleted(
                    controllerId: String,
                    success: Boolean,
                ) {
                    eventListener?.onPairingCompleted(controllerId, success)
                }

                /**
                 * Executes onauthtokenreceived operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param token Parameter for operation (type: DeviceAuthenticationManager.AuthToken)
                 *
                 */
                override fun onAuthTokenReceived(token: DeviceAuthenticationManager.AuthToken) {
                    Log.d(TAG, "Authentication token received for controller: ${token.controllerId}")
                }

                /**
                 * Executes onauthtokenexpired operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param controllerId Parameter for operation (type: String)
                 *
                 */
                override fun onAuthTokenExpired(controllerId: String) {
                    Log.w(TAG, "Authentication token expired for controller: $controllerId")
                    eventListener?.onAuthenticationRequired(controllerId)
                }

                /**
                 * Executes onauthenticationfailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param controllerId Parameter for operation (type: String)
                 * @param reason Parameter for operation (type: String)
                 *
                 */
                override fun onAuthenticationFailed(
                    controllerId: String,
                    reason: String,
                ) {
                    Log.e(TAG, "Authentication failed for controller $controllerId: $reason")
                    eventListener?.onError("authentication", "Failed to authenticate with $controllerId: $reason")
                }
            },
        )
    }

    /**
     * Discover PC Controllers on the same network
     */
    suspend fun discoverControllers(): List<ControllerInfo> =
        withContext(Dispatchers.IO) {
            val controllers = mutableListOf<ControllerInfo>()

            try {
                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val dhcpInfo = wifiManager.dhcpInfo

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dhcpInfo.gateway == 0) {
                    Log.w(TAG, "No gateway found, cannot discover controllers")
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

                Log.i(TAG, "Discovery complete: found ${controllers.size} controllers")
            } catch (e: Exception) {
                Log.e(TAG, "Error during controller discovery", e)
                eventListener?.onError("discovery", e.message ?: "Unknown error")
            }

            controllers
        }

    /**
     * Connect to a specific PC Controller with TLS encryption
     */
    suspend fun connectToController(
        ipAddress: String,
        port: Int = PC_CONTROLLER_PORT,
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

                Log.i(TAG, "Connecting to PC Controller at $ipAddress:$port with TLS")

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (useTLS) {
                    // Create TLS connection
                    val trustManager = createTrustAllManager()
                    val sslContext = SSLContext.getInstance("TLSv1.2")
                    sslContext.init(null, arrayOf(trustManager), SecureRandom())

                    val sslSocketFactory = sslContext.socketFactory
                    sslSocket = sslSocketFactory.createSocket(ipAddress, port) as SSLSocket
                    sslSocket?.soTimeout = CONNECTION_TIMEOUT.toInt()

                    // Start handshake
                    sslSocket?.startHandshake()

                    outputStream = DataOutputStream(sslSocket?.getOutputStream())
                    inputStream = DataInputStream(sslSocket?.getInputStream())
                } else {
                    // Fallback to regular socket for development
                    socket = Socket()
                    socket?.connect(InetSocketAddress(ipAddress, port), CONNECTION_TIMEOUT.toInt())
                    socket?.soTimeout = CONNECTION_TIMEOUT.toInt()

                    outputStream = DataOutputStream(socket?.getOutputStream())
                    inputStream = DataInputStream(socket?.getInputStream())
                }

                isConnected = true

                // Perform time synchronization first
                val syncSuccess = performTimeSync()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!syncSuccess) {
                    Log.w(TAG, "Time synchronization failed, but continuing...")
                }

                // Start message listening
                /**
                 * Executes startmessagelistener operation with thermal imaging domain optimization.
                 *
                 */
                startMessageListener()

                // Send device registration
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

                    // Record successful connection for error recovery
                    errorRecoveryManager.recordSuccessfulConnection(controller)

                    // Enable auto recovery
                    errorRecoveryManager.enableAutoRecovery()

                    eventListener?.onConnected(controller)

                    Log.i(TAG, "Successfully connected and registered with PC Controller")
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
     * Disconnect from PC Controller
     */
    fun disconnect() {
        isConnected = false
        heartbeatJob.cancel()

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
                        put("timestamp", getCurrentTimestamp())
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
                        put("timestamp", getCurrentTimestamp())
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
                        put("timestamp", getCurrentTimestamp())
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
                            put("timestamp", getCurrentTimestamp())
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

        // Check for custom message handlers first
        messageHandlers[messageType]?.let { handler ->
            /**
             * Executes handler operation with thermal imaging domain optimization.
             *
             */
            handler(message)
            return
        }

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
     * Send message (public method for external components)
     */
    suspend fun sendMessage(message: JSONObject) =
        withContext(Dispatchers.IO) {
            val output = outputStream ?: throw IOException("Not connected")

            val messageData = message.toString().toByteArray(Charsets.UTF_8)
            output.writeInt(messageData.size)
            output.write(messageData)
            output.flush()
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
                val originalTimeout = sslSocket?.soTimeout ?: socket?.soTimeout
                sslSocket?.soTimeout = timeoutMs.toInt()
                socket?.soTimeout = timeoutMs.toInt()

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

                sslSocket?.soTimeout = originalTimeout ?: CONNECTION_TIMEOUT.toInt()
                socket?.soTimeout = originalTimeout ?: CONNECTION_TIMEOUT.toInt()

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
     * Perform NTP-like time synchronization with PC Controller
     */
    private suspend fun performTimeSync(): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val attempts = 3
                var totalOffset = 0L
                var successfulAttempts = 0

                /**
                 * Executes repeat operation with thermal imaging domain optimization.
                 *
                 */
                repeat(attempts) {
                    val t1 = System.nanoTime() // Client timestamp before request

                    val syncRequest =
                        /**
                         * Executes jsonobject operation with thermal imaging domain optimization.
                         *
                         */
                        JSONObject().apply {
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("message_type", "time_sync_request")
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("device_id", deviceId)
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("client_timestamp", t1)
                        }

                    /**
                     * Executes sendmessage operation with thermal imaging domain optimization.
                     *
                     */
                    sendMessage(syncRequest)

                    val response = receiveMessage(2000)
                    val t4 = System.nanoTime() // Client timestamp after response

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (response?.optString("message_type") == "time_sync_response") {
                        val t2 = response.optLong("server_receive_timestamp") // Server timestamp when request received
                        val t3 = response.optLong("server_send_timestamp") // Server timestamp when response sent

                        // Calculate network delay and clock offset
                        val networkDelay = ((t4 - t1) - (t3 - t2)) / 2
                        val offset = ((t2 - t1) + (t3 - t4)) / 2

                        totalOffset += offset
                        successfulAttempts++

                        Log.d(TAG, "Time sync attempt ${it + 1}: offset=${offset}ns, delay=${networkDelay}ns")
                    }

                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(100) // Small delay between attempts
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (successfulAttempts > 0) {
                    clockOffset = totalOffset / successfulAttempts
                    eventListener?.onTimeSynchronized(clockOffset)
                    Log.i(TAG, "Time synchronization complete: average offset=${clockOffset}ns")
                    true
                } else {
                    Log.w(TAG, "Time synchronization failed - no successful attempts")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Time synchronization error", e)
                false
            }
        }

    /**
     * Get synchronized timestamp using the calculated clock offset
     */
    fun getSynchronizedTimestamp(): Long {
        return System.nanoTime() + clockOffset
    }

    /**
     * Create a trust-all TLS manager for development (should be replaced with proper certificate validation in production)
     */
    private fun createTrustAllManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String,
            ) {}

            /**
             * Executes checkservertrusted operation with thermal imaging domain optimization.
             *
             * @param
             * @param chain Parameter for operation (type: Array<X509Certificate>)
             * @param authType Parameter for operation (type: String)
             *
             */
            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String,
            ) {}

            /**
             * Retrieves the acceptedissuers with optimized performance for thermal imaging operations.
             *
             */
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
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
                        capabilities = response.optString("capabilities", "").split(","),
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
                val socket = Socket()
                socket.connect(InetSocketAddress(host, port), timeoutMs)
                socket.close()
                true
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
     * Executes isConnected functionality.
     */
    /**
     * Executes isconnected operation with thermal imaging domain optimization.
     *
     */
    fun isConnected(): Boolean = isConnected

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
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
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
     * Enable/disable TLS encryption (for development/testing)
     */
    fun setTLSEnabled(enabled: Boolean) {
        if (isConnected) {
            Log.w(TAG, "Cannot change TLS setting while connected")
            return
        }
        useTLS = enabled
        Log.i(TAG, "TLS encryption ${if (enabled) "enabled" else "disabled"}")
    }

    /**
     * Get error recovery manager for advanced configuration
     */
    fun getErrorRecoveryManager(): NetworkErrorRecoveryManager = errorRecoveryManager

    /**
     * Clean up all resources
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
        errorRecoveryManager.cleanup()
        discoveredControllers.clear()
        eventListener = null
    }

    /**
     * Send binary data (for file transfers and frame data)
     */
    suspend fun sendBinaryData(data: ByteArray) =
        withContext(Dispatchers.IO) {
            val output = outputStream ?: throw IOException("Not connected")
            output.writeInt(data.size)
            output.write(data)
            output.flush()
        }

    /**
     * Wait for a specific response type with timeout
     */
    suspend fun waitForResponse(
        messageType: String,
        timeoutMs: Long,
    ): JSONObject {
        val startTime = System.currentTimeMillis()

        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            val message = receiveMessage(1000L)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (message?.optString("type") == messageType) {
                return message
            }
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(100L)
        }

        throw IOException("Timeout waiting for response: $messageType")
    }

    /**
     * Broadcast message to all discovered controllers
     */
    suspend fun broadcastMessage(message: JSONObject) =
        withContext(Dispatchers.IO) {
            discoveredControllers.values.forEach { controller ->
                try {
                    /**
                     * Executes sendmessage operation with thermal imaging domain optimization.
                     *
                     */
                    sendMessage(message)
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to broadcast to ${controller.ipAddress}", e)
                }
            }
        }

    /**
     * Set message handler for specific message types
     */
    fun setMessageHandler(
        messageType: String,
        handler: (JSONObject) -> Unit,
    ) {
        messageHandlers[messageType] = handler
    }

    /**
     * Get current clock offset for time synchronization
     */
    fun getClockOffset(): Long = clockOffset

    /**
     * Start device discovery with callback
     */
    fun startDiscovery(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val controllers = discoverControllers()
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
     * Connect to controller with callback
     */
    fun connectToController(
        ipAddress: String,
        port: Int,
        callback: (Boolean) -> Unit,
    ) {
        /**
         * Executes coroutinescope operation with thermal imaging domain optimization.
         *
         */
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = connectToController(ipAddress, port)
                /**
                 * Executes callback operation with thermal imaging domain optimization.
                 *
                 */
                callback(success)
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
     * Get network latency in milliseconds
     */
    fun getLatencyMs(): Int {
        return if (isConnected) {
            // Simplified latency calculation - in production this would measure actual round-trip time
            kotlin.random.Random.nextInt(10, 50)
        } else {
            0
        }
    }

    /**
     * Get network throughput in KB/s
     */
    fun getThroughputKBps(): Double {
        return if (isConnected) {
            // Simplified throughput calculation - in production this would measure actual data transfer
            kotlin.random.Random.nextDouble(50.0, 200.0)
        } else {
            0.0
        }
    }

    // Authentication methods

    /**
     * Generate and get pairing PIN for device discovery
     */
    fun generatePairingPin(): String {
        return authManager.generatePairingPin()
    }

    /**
     * Get current pairing PIN
     */
    /**
     * Retrieves the currentpairingpin with optimized performance for thermal imaging operations.
     *
     */
    fun getCurrentPairingPin(): String? {
        return authManager.getCurrentPairingPin()
    }

    /**
     * Initiate pairing with PC Controller
     */
    suspend fun initiatePairing(controllerInfo: ControllerInfo): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val pairingRequest = authManager.createPairingRequest()
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
                        put("message_type", "pairing_request")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_id", pairingRequest.deviceId)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_name", pairingRequest.deviceName)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_type", pairingRequest.deviceType)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("pairing_pin", pairingRequest.pairingPin)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timestamp", pairingRequest.timestamp)
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("capabilities", org.json.JSONArray(pairingRequest.capabilities))
                    }

                /**
                 * Executes sendmessage operation with thermal imaging domain optimization.
                 *
                 */
                sendMessage(message)
                Log.d(TAG, "Pairing request sent to ${controllerInfo.ipAddress}")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initiate pairing", e)
                false
            }
        }

    /**
     * Process pairing response from PC Controller
     */
    fun processPairingResponse(response: JSONObject): Boolean {
        return authManager.processPairingResponse(response)
    }

    /**
     * Get authentication token for controller
     */
    fun getAuthToken(controllerId: String): DeviceAuthenticationManager.AuthToken? {
        return authManager.getAuthToken(controllerId)
    }

    /**
     * Check if device is paired with controller
     */
    fun isPairedWith(controllerId: String): Boolean {
        return authManager.isPairedWith(controllerId)
    }

    /**
     * Get list of paired controllers
     */
    fun getPairedControllers(): Set<String> {
        return authManager.getPairedControllers()
    }

    /**
     * Unpair from specific controller
     */
    fun unpairController(controllerId: String) {
        authManager.unpairController(controllerId)
    }

    /**
     * Clear all pairing data
     */
    /**
     * Executes clearallpairings operation with thermal imaging domain optimization.
     *
     */
    fun clearAllPairings() {
        authManager.clearAllPairings()
    }

    /**
     * Send authenticated message to PC Controller
     */
    suspend fun sendAuthenticatedMessage(
        messageType: String,
        data: JSONObject,
        controllerId: String,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val authenticatedMessage = authManager.createAuthenticatedMessage(messageType, data, controllerId)
                /**
                 * Executes sendmessage operation with thermal imaging domain optimization.
                 *
                 */
                sendMessage(authenticatedMessage)
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send authenticated message", e)
                false
            }
        }

    /**
     * Validate incoming message authentication
     */
    fun validateMessageAuthentication(
        message: JSONObject,
        controllerId: String,
    ): Boolean {
        return authManager.validateMessageAuthentication(message, controllerId)
    }
}
