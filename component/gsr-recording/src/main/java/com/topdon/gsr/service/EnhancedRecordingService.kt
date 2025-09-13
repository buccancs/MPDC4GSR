package com.topdon.gsr.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.topdon.gsr.model.GSRSample
import com.topdon.gsr.model.SessionInfo
import com.topdon.gsr.model.SyncMark
import com.topdon.gsr.network.DataStreamingService
import com.topdon.gsr.network.NetworkClient
import com.topdon.gsr.network.ZeroconfDiscoveryService
import kotlinx.coroutines.*

/**
 * Specialized thermal imaging component providing EnhancedRecordingService functionality for the IRCamera system.
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
class EnhancedRecordingService : Service() {
    companion object {
        private const val TAG = "EnhancedRecordingService"
        private const val NOTIFICATION_ID = 12346
        private const val CHANNEL_ID = "enhanced_recording_channel"
        private const val WAKE_LOCK_TAG = "IRCamera:EnhancedRecording"

        // Service actions
        private const val ACTION_START_RECORDING = "action_start_recording"
        private const val ACTION_STOP_RECORDING = "action_stop_recording"
        private const val ACTION_CONNECT_PC = "action_connect_pc"
        private const val ACTION_DISCONNECT_PC = "action_disconnect_pc"
        private const val ACTION_START_DISCOVERY = "action_start_discovery"
        private const val ACTION_STOP_DISCOVERY = "action_stop_discovery"

        // Intent extras
        private const val EXTRA_SESSION_ID = "extra_session_id"
        private const val EXTRA_PARTICIPANT_ID = "extra_participant_id"
        private const val EXTRA_STUDY_NAME = "extra_study_name"
        private const val EXTRA_PC_IP = "extra_pc_ip"
        private const val EXTRA_PC_PORT = "extra_pc_port"

        // Service control methods
    /**
     * Executes startRecording functionality.
     */
        /**
         * Executes startrecording operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param sessionId Parameter for operation (type: String)
         * @param participantId Parameter for operation (type: String? = null)
         * @param studyName Parameter for operation (type: String? = null)
         *
         */
        fun startRecording(
            context: Context,
            sessionId: String,
            participantId: String? = null,
            studyName: String? = null,
        ) {
            val intent =
                Intent(context, EnhancedRecordingService::class.java).apply {
                    action = ACTION_START_RECORDING
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_SESSION_ID, sessionId)
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_PARTICIPANT_ID, participantId)
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_STUDY_NAME, studyName)
                }
            /**
             * Executes startforegroundservice operation with thermal imaging domain optimization.
             *
             */
            startForegroundService(context, intent)
        }

    /**
     * Executes stopRecording functionality.
     */
        /**
         * Executes stoprecording operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun stopRecording(context: Context) {
            val intent =
                Intent(context, EnhancedRecordingService::class.java).apply {
                    action = ACTION_STOP_RECORDING
                }
            context.startService(intent)
        }

    /**
     * Executes connectToPC functionality.
     */
        /**
         * Executes connecttopc operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param ipAddress Parameter for operation (type: String)
         * @param port Parameter for operation (type: Int = 8080)
         *
         */
        fun connectToPC(
            context: Context,
            ipAddress: String,
            port: Int = 8080,
        ) {
            val intent =
                Intent(context, EnhancedRecordingService::class.java).apply {
                    action = ACTION_CONNECT_PC
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_PC_IP, ipAddress)
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_PC_PORT, port)
                }
            context.startService(intent)
        }

    /**
     * Executes disconnectFromPC functionality.
     */
        /**
         * Executes disconnectfrompc operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun disconnectFromPC(context: Context) {
            val intent =
                Intent(context, EnhancedRecordingService::class.java).apply {
                    action = ACTION_DISCONNECT_PC
                }
            context.startService(intent)
        }

    /**
     * Executes startDiscovery functionality.
     */
        /**
         * Executes startdiscovery operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun startDiscovery(context: Context) {
            val intent =
                Intent(context, EnhancedRecordingService::class.java).apply {
                    action = ACTION_START_DISCOVERY
                }
            context.startService(intent)
        }

    /**
     * Executes startForegroundService functionality.
     */
        /**
         * Executes startforegroundservice operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param intent Parameter for operation (type: Intent)
         *
         */
        private fun startForegroundService(
            context: Context,
            intent: Intent,
        ) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    // Service components
    private lateinit var gsrRecorder: GSRRecorder
    private lateinit var sessionManager: SessionManager
    private lateinit var networkClient: NetworkClient
    private lateinit var dataStreamingService: DataStreamingService
    private lateinit var discoveryService: ZeroconfDiscoveryService

    // Service state
    private var isRecording = false
    private var isConnectedToPC = false
    private var isStreamingData = false
    private var currentSessionId: String? = null
    private var wakeLock: PowerManager.WakeLock? = null

    // Coroutine scope for service operations
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

/**
 * Specialized thermal imaging component providing ServiceEventListener functionality for the IRCamera system.
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
    interface ServiceEventListener {
    /**
     * Executes onRecordingStateChanged functionality.
     */
        /**
         * Executes onrecordingstatechanged operation with thermal imaging domain optimization.
         *
         * @param
         * @param isRecording Parameter for operation (type: Boolean)
         * @param sessionId Parameter for operation (type: String?)
         *
         */
        fun onRecordingStateChanged(
            isRecording: Boolean,
            sessionId: String?,
        )

    /**
     * Executes onNetworkStateChanged functionality.
     */
        /**
         * Executes onnetworkstatechanged operation with thermal imaging domain optimization.
         *
         * @param
         * @param isConnected Parameter for operation (type: Boolean)
         * @param controllerInfo Parameter for operation (type: NetworkClient.ControllerInfo?)
         *
         */
        fun onNetworkStateChanged(
            isConnected: Boolean,
            controllerInfo: NetworkClient.ControllerInfo?,
        )

    /**
     * Executes onDataStreamingStateChanged functionality.
     */
        /**
         * Executes ondatastreamingstatechanged operation with thermal imaging domain optimization.
         *
         * @param
         * @param isStreaming Parameter for operation (type: Boolean)
         *
         */
        fun onDataStreamingStateChanged(isStreaming: Boolean)

    /**
     * Executes onServiceError functionality.
     */
        /**
         * Executes onserviceerror operation with thermal imaging domain optimization.
         *
         * @param
         * @param operation Parameter for operation (type: String)
         * @param error Parameter for operation (type: String)
         *
         */
        fun onServiceError(
            operation: String,
            error: String,
        )

    /**
     * Executes onDiscoveryResult functionality.
     */
        /**
         * Executes ondiscoveryresult operation with thermal imaging domain optimization.
         *
         * @param
         * @param controllers Parameter for operation (type: List<NetworkClient.ControllerInfo>)
         *
         */
        fun onDiscoveryResult(controllers: List<NetworkClient.ControllerInfo>)
    }

    private var eventListener: ServiceEventListener? = null

    /**
     * Sets eventlistener configuration.
     */
    fun setEventListener(listener: ServiceEventListener?) {
        eventListener = listener
    }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     */
    override fun onCreate() {
        super.onCreate()
        /**
         * Initializes the ializecomponents component for thermal imaging operations.
         *
         */
        initializeComponents()
        /**
         * Configures the upnetworklisteners with validation and thermal imaging optimization.
         *
         */
        setupNetworkListeners()
        /**
         * Executes createnotificationchannel operation with thermal imaging domain optimization.
         *
         */
        createNotificationChannel()
        /**
         * Executes acquirewakelock operation with thermal imaging domain optimization.
         *
         */
        acquireWakeLock()
        Log.i(TAG, "Enhanced recording service created")
    }

    /**
     * Executes onstartcommand operation with thermal imaging domain optimization.
     *
     * @param
     * @param intent Parameter for operation (type: Intent?)
     * @param flags Parameter for operation (type: Int)
     * @param startId Parameter for operation (type: Int)
     *
     */
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (intent?.action) {
            ACTION_START_RECORDING -> {
                val sessionId = intent.getStringExtra(EXTRA_SESSION_ID) ?: return START_NOT_STICKY
                val participantId = intent.getStringExtra(EXTRA_PARTICIPANT_ID)
                val studyName = intent.getStringExtra(EXTRA_STUDY_NAME)
                /**
                 * Executes startrecording operation with thermal imaging domain optimization.
                 *
                 */
                startRecording(sessionId, participantId, studyName)
            }
            ACTION_STOP_RECORDING -> stopRecording()
            ACTION_CONNECT_PC -> {
                val ipAddress = intent.getStringExtra(EXTRA_PC_IP) ?: return START_NOT_STICKY
                val port = intent.getIntExtra(EXTRA_PC_PORT, 8080)
                /**
                 * Executes connecttopc operation with thermal imaging domain optimization.
                 *
                 */
                connectToPC(ipAddress, port)
            }
            ACTION_DISCONNECT_PC -> disconnectFromPC()
            ACTION_START_DISCOVERY -> startPCDiscovery()
            ACTION_STOP_DISCOVERY -> stopPCDiscovery()
        }

        return START_STICKY // Restart service if killed
    }

    /**
     * Executes onbind operation with thermal imaging domain optimization.
     *
     * @param
     * @param intent Parameter for operation (type: Intent?)
     *
     */
    override fun onBind(intent: Intent?): IBinder = binder

    /**
     * Initializes ializecomponents component.
     */
    private fun initializeComponents() {
        gsrRecorder = GSRRecorder(this)
        sessionManager = SessionManager.getInstance(this)
        networkClient = NetworkClient(this)
        dataStreamingService = DataStreamingService(this, networkClient)
        discoveryService = ZeroconfDiscoveryService(this)
    }

    /**
     * Sets upnetworklisteners configuration.
     */
    private fun setupNetworkListeners() {
        // GSR recorder listener for data capture
        gsrRecorder.addListener(
            object : GSRRecorder.GSRRecordingListener {
                /**
                 * Executes onrecordingstarted operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param sessionInfo Parameter for operation (type: SessionInfo)
                 *
                 */
                override fun onRecordingStarted(sessionInfo: SessionInfo) {
                    Log.i(TAG, "GSR recording started: ${sessionInfo.sessionId}")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Recording started - ${sessionInfo.sessionId}")
                    eventListener?.onRecordingStateChanged(true, sessionInfo.sessionId)

                    // Start data streaming if connected to PC
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isConnectedToPC) {
                        serviceScope.launch {
                            val streamingStarted = dataStreamingService.startStreaming(sessionInfo.sessionId)
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (streamingStarted) {
                                isStreamingData = true
                                eventListener?.onDataStreamingStateChanged(true)
                            }
                        }
                    }
                }

                /**
                 * Executes onrecordingstopped operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param sessionInfo Parameter for operation (type: SessionInfo)
                 *
                 */
                override fun onRecordingStopped(sessionInfo: SessionInfo) {
                    Log.i(TAG, "GSR recording stopped: ${sessionInfo.sessionId}")
                    isRecording = false
                    currentSessionId = null

                    // Stop data streaming
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isStreamingData) {
                        serviceScope.launch {
                            dataStreamingService.stopStreaming()
                            isStreamingData = false
                            eventListener?.onDataStreamingStateChanged(false)
                        }
                    }

                    eventListener?.onRecordingStateChanged(false, null)
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Recording stopped")
                }

                /**
                 * Executes onsamplerecorded operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param sample Parameter for operation (type: GSRSample)
                 *
                 */
                override fun onSampleRecorded(sample: GSRSample) {
                    // Stream sample to PC if connected
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isStreamingData) {
                        dataStreamingService.queueGSRSample(sample)
                    }

                    // Update notification periodically
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (sample.sampleIndex % 1280 == 0L) { // Every 10 seconds at 128Hz
                        /**
                         * Executes updatenotification operation with thermal imaging domain optimization.
                         *
                         */
                        updateNotification("Recording... ${sample.sampleIndex} samples")
                    }
                }

                /**
                 * Executes onsyncmarkadded operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param syncMark Parameter for operation (type: SyncMark)
                 *
                 */
                override fun onSyncMarkAdded(syncMark: SyncMark) {
                    Log.d(TAG, "Sync mark added: ${syncMark.eventType}")
                }

                /**
                 * Executes onerror operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param error Parameter for operation (type: String)
                 *
                 */
                override fun onError(error: String) {
                    Log.e(TAG, "GSR recording error: $error")
                    eventListener?.onServiceError("gsr_recording", error)
                }
            },
        )

        // Network client listener for PC communication
        networkClient.setEventListener(
            object : NetworkClient.NetworkEventListener {
                /**
                 * Executes oncontrollerdiscovered operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param controller Parameter for operation (type: NetworkClient.ControllerInfo)
                 *
                 */
                override fun onControllerDiscovered(controller: NetworkClient.ControllerInfo) {
                    Log.i(TAG, "PC Controller discovered: ${controller.deviceName} at ${controller.ipAddress}")
                }

                /**
                 * Executes onconnected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param controller Parameter for operation (type: NetworkClient.ControllerInfo)
                 *
                 */
                override fun onConnected(controller: NetworkClient.ControllerInfo) {
                    Log.i(TAG, "Connected to PC Controller: ${controller.deviceName}")
                    isConnectedToPC = true
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Connected to ${controller.deviceName}")
                    eventListener?.onNetworkStateChanged(true, controller)
                }

                /**
                 * Executes ondisconnected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param reason Parameter for operation (type: String)
                 *
                 */
                override fun onDisconnected(reason: String) {
                    Log.i(TAG, "Disconnected from PC Controller: $reason")
                    isConnectedToPC = false

                    // Stop data streaming
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isStreamingData) {
                        serviceScope.launch {
                            dataStreamingService.stopStreaming()
                            isStreamingData = false
                            eventListener?.onDataStreamingStateChanged(false)
                        }
                    }

                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Disconnected from PC")
                    eventListener?.onNetworkStateChanged(false, null)
                }

                /**
                 * Executes onremotemeasurementrequest operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param sessionInfo Parameter for operation (type: SessionInfo)
                 *
                 */
                override fun onRemoteMeasurementRequest(sessionInfo: SessionInfo) {
                    Log.i(TAG, "Remote measurement request: ${sessionInfo.sessionId}")
                    // Auto-start recording for remote requests
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!isRecording) {
                        /**
                         * Executes startrecording operation with thermal imaging domain optimization.
                         *
                         */
                        startRecording(sessionInfo.sessionId, sessionInfo.participantId, sessionInfo.studyName)
                    }
                }

                /**
                 * Executes onsyncflash operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param durationMs Duration in milliseconds (type: Int)
                 *
                 */
                override fun onSyncFlash(durationMs: Int) {
                    Log.i(TAG, "Sync flash requested: ${durationMs}ms")
                    // Trigger visual sync flash on device
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isRecording) {
                        gsrRecorder.triggerSyncEvent("SYNC_FLASH_${durationMs}ms")
                    }
                }

                /**
                 * Executes ontimesynchronized operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param offsetNanoseconds Parameter for operation (type: Long)
                 *
                 */
                override fun onTimeSynchronized(offsetNanoseconds: Long) {
                    Log.i(TAG, "Time synchronized: offset=${offsetNanoseconds}ns")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param offset Parameter for operation (type: ${offsetNanoseconds / 1000000}ms)
                     *
                     */
                    updateNotification("Time synchronized (offset: ${offsetNanoseconds / 1000000}ms)")
                }

                /**
                 * Executes ondatastreamingstarted operation with thermal imaging domain optimization.
                 *
                 */
                override fun onDataStreamingStarted() {
                    Log.i(TAG, "Data streaming to PC started")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Streaming data to PC")
                }

                /**
                 * Executes ondatastreamingstopped operation with thermal imaging domain optimization.
                 *
                 */
                override fun onDataStreamingStopped() {
                    Log.i(TAG, "Data streaming to PC stopped")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Data streaming stopped")
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
                    Log.e(TAG, "Network error in $operation: $error")
                    eventListener?.onServiceError("network_$operation", error)
                }

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
                    Log.i(TAG, "Pairing requested by controller: $controllerName ($controllerId)")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Pairing requested by $controllerName")
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
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (success) {
                        Log.i(TAG, "Pairing completed successfully with controller: $controllerId")
                        /**
                         * Executes updatenotification operation with thermal imaging domain optimization.
                         *
                         */
                        updateNotification("Paired with controller")
                    } else {
                        Log.w(TAG, "Pairing failed with controller: $controllerId")
                        /**
                         * Executes updatenotification operation with thermal imaging domain optimization.
                         *
                         */
                        updateNotification("Pairing failed")
                    }
                }

                /**
                 * Executes onauthenticationrequired operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param controllerId Parameter for operation (type: String)
                 *
                 */
                override fun onAuthenticationRequired(controllerId: String) {
                    Log.w(TAG, "Authentication required for controller: $controllerId")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Authentication required")
                }
            },
        )

        // Data streaming service listener
        dataStreamingService.setEventListener(
            object : DataStreamingService.StreamingEventListener {
                /**
                 * Executes onstreamingstarted operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param sessionId Parameter for operation (type: String)
                 *
                 */
                override fun onStreamingStarted(sessionId: String) {
                    Log.i(TAG, "Data streaming started for session: $sessionId")
                    isStreamingData = true
                    eventListener?.onDataStreamingStateChanged(true)
                }

                /**
                 * Executes onstreamingstopped operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param sessionId Parameter for operation (type: String)
                 *
                 */
                override fun onStreamingStopped(sessionId: String) {
                    Log.i(TAG, "Data streaming stopped for session: $sessionId")
                    isStreamingData = false
                    eventListener?.onDataStreamingStateChanged(false)
                }

                /**
                 * Executes onbatchsent operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param batchSize Parameter for operation (type: Int)
                 * @param dataType Parameter for operation (type: String)
                 *
                 */
                override fun onBatchSent(
                    batchSize: Int,
                    dataType: String,
                ) {
                    Log.d(TAG, "Sent $dataType batch: $batchSize samples")
                }

                /**
                 * Executes onstreamingerror operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param error Parameter for operation (type: String)
                 *
                 */
                override fun onStreamingError(error: String) {
                    Log.e(TAG, "Data streaming error: $error")
                    eventListener?.onServiceError("data_streaming", error)
                }

                /**
                 * Executes onqueuefull operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param dataType Parameter for operation (type: String)
                 * @param droppedSamples Parameter for operation (type: Int)
                 *
                 */
                override fun onQueueFull(
                    dataType: String,
                    droppedSamples: Int,
                ) {
                    Log.w(TAG, "Queue full for $dataType: dropped $droppedSamples samples")
                }
            },
        )

        // Discovery service listener
        discoveryService.setServiceListener(
            object : ZeroconfDiscoveryService.ServiceDiscoveryListener {
                /**
                 * Executes onservicediscovered operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NetworkClient.ControllerInfo)
                 *
                 */
                override fun onServiceDiscovered(serviceInfo: NetworkClient.ControllerInfo) {
                    Log.i(TAG, "mDNS service discovered: ${serviceInfo.deviceName}")
                }

                /**
                 * Executes onservicelost operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceName Parameter for operation (type: String)
                 *
                 */
                override fun onServiceLost(serviceName: String) {
                    Log.i(TAG, "mDNS service lost: $serviceName")
                }

                /**
                 * Executes onserviceregistered operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceName Parameter for operation (type: String)
                 *
                 */
                override fun onServiceRegistered(serviceName: String) {
                    Log.i(TAG, "mDNS service registered: $serviceName")
                }

                /**
                 * Executes ondiscoveryerror operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param errorCode Parameter for operation (type: Int)
                 * @param message Parameter for operation (type: String)
                 *
                 */
                override fun onDiscoveryError(
                    errorCode: Int,
                    message: String,
                ) {
                    Log.e(TAG, "mDNS discovery error: $message (code: $errorCode)")
                    eventListener?.onServiceError("mdns_discovery", message)
                }
            },
        )
    }

    /**
     * Executes startRecording functionality.
     */
    /**
     * Executes startrecording operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionId Parameter for operation (type: String)
     * @param participantId Parameter for operation (type: String?)
     * @param studyName Parameter for operation (type: String?)
     *
     */
    private fun startRecording(
        sessionId: String,
        participantId: String?,
        studyName: String?,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            Log.w(TAG, "Recording already in progress")
            return
        }

        serviceScope.launch {
            try {
                // Create session
                sessionManager.createSession(sessionId, participantId, studyName)

                // Start foreground notification
                /**
                 * Executes startforeground operation with thermal imaging domain optimization.
                 *
                 */
                startForeground(NOTIFICATION_ID, createNotification("Starting recording..."))

                // Start GSR recording
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (gsrRecorder.startRecording(sessionId, participantId, studyName)) {
                    isRecording = true
                    currentSessionId = sessionId
                    Log.i(TAG, "Enhanced recording started: $sessionId")
                } else {
                    Log.e(TAG, "Failed to start GSR recording")
                    eventListener?.onServiceError("start_recording", "Failed to start GSR recording")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting recording", e)
                eventListener?.onServiceError("start_recording", e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Executes stopRecording functionality.
     */
    /**
     * Executes stoprecording operation with thermal imaging domain optimization.
     *
     */
    private fun stopRecording() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isRecording) {
            Log.w(TAG, "No recording in progress")
            return
        }

        serviceScope.launch {
            try {
                // Stop GSR recording
                val session = gsrRecorder.stopRecording()
                session?.let {
                    sessionManager.completeSession(it.sessionId)
                }

                Log.i(TAG, "Enhanced recording stopped")
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping recording", e)
                eventListener?.onServiceError("stop_recording", e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Executes connectToPC functionality.
     */
    /**
     * Executes connecttopc operation with thermal imaging domain optimization.
     *
     * @param
     * @param ipAddress Parameter for operation (type: String)
     * @param port Parameter for operation (type: Int)
     *
     */
    private fun connectToPC(
        ipAddress: String,
        port: Int,
    ) {
        serviceScope.launch {
            try {
                val success = networkClient.connectToController(ipAddress, port)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!success) {
                    eventListener?.onServiceError("connect_pc", "Failed to connect to $ipAddress:$port")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error connecting to PC", e)
                eventListener?.onServiceError("connect_pc", e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Executes disconnectFromPC functionality.
     */
    /**
     * Executes disconnectfrompc operation with thermal imaging domain optimization.
     *
     */
    private fun disconnectFromPC() {
        networkClient.disconnect()
    }

    /**
     * Executes startPCDiscovery functionality.
     */
    /**
     * Executes startpcdiscovery operation with thermal imaging domain optimization.
     *
     */
    private fun startPCDiscovery() {
        serviceScope.launch {
            try {
                val success = discoveryService.startDiscovery()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Discovering PC Controllers...")
                    // Register this device for PC discovery
                    discoveryService.registerService(
                        deviceId =
                            android.provider.Settings.Secure.getString(
                                contentResolver,
                                android.provider.Settings.Secure.ANDROID_ID,
                            ),
                        port = 0, // Client doesn't listen on a port
                    )
                } else {
                    eventListener?.onServiceError("start_discovery", "Failed to start discovery")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting discovery", e)
                eventListener?.onServiceError("start_discovery", e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Executes stopPCDiscovery functionality.
     */
    /**
     * Executes stoppcdiscovery operation with thermal imaging domain optimization.
     *
     */
    private fun stopPCDiscovery() {
        discoveryService.stopDiscovery()
        /**
         * Executes updatenotification operation with thermal imaging domain optimization.
         *
         */
        updateNotification("Discovery stopped")
    }

    /**
     * Executes acquireWakeLock functionality.
     */
    /**
     * Executes acquirewakelock operation with thermal imaging domain optimization.
     *
     */
    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock =
            powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                WAKE_LOCK_TAG,
            ).apply {
                /**
                 * Executes acquire operation with thermal imaging domain optimization.
                 *
                 */
                acquire(10 * 60 * 1000L /* 10 minutes */)
            }
    }

    /**
     * Executes releaseWakeLock functionality.
     */
    /**
     * Executes releasewakelock operation with thermal imaging domain optimization.
     *
     */
    private fun releaseWakeLock() {
        wakeLock?.takeIf { it.isHeld }?.release()
        wakeLock = null
    }

    /**
     * Executes createNotificationChannel functionality.
     */
    /**
     * Executes createnotificationchannel operation with thermal imaging domain optimization.
     *
     */
    private fun createNotificationChannel() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                /**
                 * Executes notificationchannel operation with thermal imaging domain optimization.
                 *
                 */
                NotificationChannel(
                    CHANNEL_ID,
                    "Enhanced Recording Service",
                    NotificationManager.IMPORTANCE_LOW,
                ).apply {
                    description = "Multi-modal physiological data recording with PC communication"
                    /**
                     * Configures the sound with validation and thermal imaging optimization.
                     *
                     */
                    setSound(null, null)
                }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Executes createNotification functionality.
     */
    /**
     * Executes createnotification operation with thermal imaging domain optimization.
     *
     * @param
     * @param content Parameter for operation (type: String)
     *
     */
    private fun createNotification(content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Enhanced Recording Service")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_media_ff)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    /**
     * Executes updateNotification functionality.
     */
    /**
     * Executes updatenotification operation with thermal imaging domain optimization.
     *
     * @param
     * @param content Parameter for operation (type: String)
     *
     */
    private fun updateNotification(content: String) {
        val notification = createNotification(content)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    // Public API methods for bound clients
    /**
     * Retrieves connectionstatus information.
     */
    fun getConnectionStatus(): Boolean = isConnectedToPC

    /**
     * Retrieves recordingstatus information.
     */
    fun getRecordingStatus(): Boolean = isRecording

    /**
     * Retrieves streamingstatus information.
     */
    fun getStreamingStatus(): Boolean = isStreamingData

    /**
     * Retrieves currentsessionid information.
     */
    fun getCurrentSessionId(): String? = currentSessionId

    /**
     * Retrieves discoveredcontrollers information.
     */
    fun getDiscoveredControllers(): List<NetworkClient.ControllerInfo> = discoveryService.getDiscoveredControllers()

    /**
     * Retrieves queuesizes information.
     */
    fun getQueueSizes(): Map<String, Int> = dataStreamingService.getQueueSizes()

    override fun onDestroy() {
        super.onDestroy()

        // Clean up all components
        serviceScope.launch {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isRecording) {
                /**
                 * Executes stoprecording operation with thermal imaging domain optimization.
                 *
                 */
                stopRecording()
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isConnectedToPC) {
                /**
                 * Executes disconnectfrompc operation with thermal imaging domain optimization.
                 *
                 */
                disconnectFromPC()
            }

            dataStreamingService.cleanup()
            discoveryService.cleanup()
        }

        // Cancel coroutines
        serviceJob.cancel()

        // Release wake lock
        /**
         * Executes releasewakelock operation with thermal imaging domain optimization.
         *
         */
        releaseWakeLock()

        Log.i(TAG, "Enhanced recording service destroyed")
    }
}
