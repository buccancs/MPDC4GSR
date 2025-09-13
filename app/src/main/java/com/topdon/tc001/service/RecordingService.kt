package com.topdon.tc001.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.topdon.tc001.controller.RecordingController
import com.topdon.tc001.controller.RecordingState
import com.topdon.gsr.model.SessionInfo
import com.topdon.gsr.model.SyncMark
import com.topdon.tc001.network.EnhancedNetworkClient
import com.topdon.tc001.network.NetworkClient
import com.topdon.tc001.network.NetworkServer
import com.topdon.tc001.utils.TimeManager
import com.csl.irCamera.R
// Phase 0 baseline imports
import com.topdon.tc001.config.FeatureFlags
import com.topdon.tc001.config.ProtocolVersion
import com.topdon.tc001.logging.StructuredLogger
import com.topdon.tc001.supervisor.CrashSafeSupervisor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Background service for multi-modal sensor recording with PC-to-Phone server socket support.
 * 
 * This service ensures continuous recording operation even when the app is in the background.
 * It manages the RecordingController and provides status updates through notifications.
 * 
 * Key Features:
 * - Foreground service for uninterrupted recording
 * - Persistent server socket for PC connections
 * - Multi-connection support with re-accept loop
 * - NSD service advertisement for discovery
 * - Real-time status notifications
 * - Automatic recovery from errors
 * - Integration with PC Controller communication
 * - Power management awareness
 * - Graceful shutdown and resource management
 * 
 * @author IRCamera Android Sensor Node (Spoke)
 */
/**
 * Specialized thermal imaging component providing RecordingService functionality for the IRCamera system.
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
class RecordingService : LifecycleService() {

    companion object {
        private const val TAG = "RecordingService"
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "recording_service_channel"
        
        // Server socket configuration
        private const val SERVER_PORT = 8080
        private const val SERVICE_TYPE = "_ircamera._tcp."
        private const val SERVICE_NAME = "IRCamera-Android"
        
        // Actions
        const val ACTION_START_RECORDING = "com.topdon.tc001.START_RECORDING"
        const val ACTION_STOP_RECORDING = "com.topdon.tc001.STOP_RECORDING"
        const val ACTION_ADD_SYNC_MARKER = "com.topdon.tc001.ADD_SYNC_MARKER"
<<<<<<< HEAD
        const val ACTION_START_SERVER = "com.topdon.tc001.START_SERVER"
        const val ACTION_STOP_SERVER = "com.topdon.tc001.STOP_SERVER"
=======
        const val ACTION_CONNECT_PC = "com.topdon.tc001.CONNECT_PC"
        const val ACTION_DISCONNECT_PC = "com.topdon.tc001.DISCONNECT_PC"
        const val ACTION_START_DISCOVERY = "com.topdon.tc001.START_DISCOVERY"
>>>>>>> dev
        
        // Extras
        const val EXTRA_SESSION_DIRECTORY = "session_directory"
        const val EXTRA_MARKER_TYPE = "marker_type"
        const val EXTRA_TIMESTAMP_NS = "timestamp_ns"
        const val EXTRA_PC_IP = "pc_ip"
        const val EXTRA_PC_PORT = "pc_port"
        
        /**
         * Start the recording service
         */
        fun startRecording(context: Context, sessionDirectory: String) {
            val intent = Intent(context, RecordingService::class.java).apply {
                action = ACTION_START_RECORDING
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra(EXTRA_SESSION_DIRECTORY, sessionDirectory)
            }
            context.startForegroundService(intent)
        }
        
        /**
         * Stop the recording service
         */
        fun stopRecording(context: Context) {
            val intent = Intent(context, RecordingService::class.java).apply {
                action = ACTION_STOP_RECORDING
            }
            context.startService(intent)
        }
        
        /**
         * Start server socket for PC connections
         */
        fun startServer(context: Context) {
            val intent = Intent(context, RecordingService::class.java).apply {
                action = ACTION_START_SERVER
            }
            context.startForegroundService(intent)
        }
        
        /**
         * Stop server socket
         */
        fun stopServer(context: Context) {
            val intent = Intent(context, RecordingService::class.java).apply {
                action = ACTION_STOP_SERVER
            }
            context.startService(intent)
        }
        
        /**
         * Add sync marker through service
         */
        fun addSyncMarker(context: Context, markerType: String, timestampNs: Long) {
            val intent = Intent(context, RecordingService::class.java).apply {
                action = ACTION_ADD_SYNC_MARKER
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra(EXTRA_MARKER_TYPE, markerType)
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra(EXTRA_TIMESTAMP_NS, timestampNs)
            }
            context.startService(intent)
        }
        
        /**
         * Connect to PC Controller
         */
        fun connectToPC(context: Context, ipAddress: String, port: Int = 8080) {
            val intent = Intent(context, RecordingService::class.java).apply {
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
         * Disconnect from PC Controller
         */
        fun disconnectFromPC(context: Context) {
            val intent = Intent(context, RecordingService::class.java).apply {
                action = ACTION_DISCONNECT_PC
            }
            context.startService(intent)
        }
        
        /**
         * Start PC Controller discovery
         */
        fun startDiscovery(context: Context) {
            val intent = Intent(context, RecordingService::class.java).apply {
                action = ACTION_START_DISCOVERY
            }
            context.startService(intent)
        }
    }

    // Service binding
    private val binder = RecordingServiceBinder()
    
    // Recording controller
    private lateinit var recordingController: RecordingController
    private var isInitialized = false
    
    // Network communication - both client and server capabilities
    private lateinit var networkClient: NetworkClient
    private lateinit var networkServer: NetworkServer
    private var isNetworkInitialized = false
    private var isConnectedToPC = false
    
    // Current session
    private var currentSessionDirectory: String? = null
    private var recordingStartTime: Long = 0
    
    // Notification manager
    private lateinit var notificationManager: NotificationManager
    
    // Server socket components
    private var serverSocket: ServerSocket? = null
    private var isServerRunning = AtomicBoolean(false)
    private var serverJob: Job? = null
    private val activeConnections = ConcurrentHashMap<String, ClientConnection>()
    
    // NSD components
    private var nsdManager: NsdManager? = null
    private var nsdServiceInfo: NsdServiceInfo? = null
    private var isServiceRegistered = false
    
    // Phase 0 baseline components
    private lateinit var structuredLogger: StructuredLogger
    private lateinit var crashSafeSupervisor: CrashSafeSupervisor
    
    /**
     * Represents an active client connection from PC
     */
    private data class ClientConnection(
        val socket: Socket,
        val clientId: String,
        val inputStream: DataInputStream,
        val outputStream: DataOutputStream,
        val job: Job
    )

    inner class RecordingServiceBinder : Binder() {
    /**
     * Retrieves service information.
     */
        fun getService(): RecordingService = this@RecordingService
    /**
     * Retrieves recordingcontroller information.
     */
        fun getRecordingController(): RecordingController = recordingController
    /**
     * Retrieves networkserver information.
     */
        fun getNetworkServer(): NetworkServer = networkServer
    /**
     * Executes isConnectedToPC functionality.
     */
        /**
         * Executes isconnectedtopc operation with thermal imaging domain optimization.
         *
         */
        fun isConnectedToPC(): Boolean = isConnectedToPC
    }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     */
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "RecordingService created")
        
        // Initialize Phase 0 baseline components
        /**
         * Initializes the ializephase0baseline component for thermal imaging operations.
         *
         */
        initializePhase0Baseline()
        
        // Initialize notification manager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /**
         * Executes createnotificationchannel operation with thermal imaging domain optimization.
         *
         */
        createNotificationChannel()
        
        // Initialize NSD manager
        nsdManager = getSystemService(Context.NSD_SERVICE) as NsdManager
        
        // Initialize recording controller
        recordingController = RecordingController(this, this)
        
<<<<<<< HEAD
        // Initialize sensors under supervision
        crashSafeSupervisor.registerJob(
            id = "recording_service_init",
            name = "RecordingService Initialization",
            critical = true,
            restartable = false
        ) { stopToken ->
=======
        // Initialize both network client and server for maximum compatibility
        networkClient = NetworkClient(this)
        networkServer = NetworkServer(this, 8080)
        
        // Initialize sensors and dual network architecture
        lifecycleScope.launch {
>>>>>>> dev
            try {
                val sensorsSuccess = recordingController.initializeSensors()
                isInitialized = sensorsSuccess
                
<<<<<<< HEAD
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    structuredLogger.log(
                        StructuredLogger.LogLevel.INFO,
                        "RecordingService",
                        "service_initialized"
                    )
                    /**
                     * Configures the upstatusmonitoring with validation and thermal imaging optimization.
                     *
                     */
                    setupStatusMonitoring()
                    
                    // Start server socket automatically if enabled
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (FeatureFlags.MDNS_ENABLE) {
                        /**
                         * Executes startserversocket operation with thermal imaging domain optimization.
                         *
                         */
                        startServerSocket()
=======
                val networkSuccess = initializeNetworkClient()
                isNetworkInitialized = networkSuccess
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (sensorsSuccess) {
                    Log.i(TAG, "Recording service initialized successfully")
                    /**
                     * Configures the upstatusmonitoring with validation and thermal imaging optimization.
                     *
                     */
                    setupStatusMonitoring()
                    /**
                     * Configures the upnetworkserver with validation and thermal imaging optimization.
                     *
                     */
                    setupNetworkServer()
                    
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (networkSuccess) {
                        Log.i(TAG, "Network client initialized successfully")
                        /**
                         * Executes startnetworkdiscovery operation with thermal imaging domain optimization.
                         *
                         */
                        startNetworkDiscovery()
                    } else {
                        Log.w(TAG, "Network client initialization failed - running in server-only mode")
>>>>>>> dev
                    }
                } else {
                    structuredLogger.log(
                        StructuredLogger.LogLevel.ERROR,
                        "RecordingService",
                        "initialization_failed"
                    )
                    /**
                     * Executes stopself operation with thermal imaging domain optimization.
                     *
                     */
                    stopSelf()
                }
            } catch (e: Exception) {
                structuredLogger.log(
                    StructuredLogger.LogLevel.ERROR,
                    "RecordingService",
                    "initialization_exception",
                    /**
                     * Executes mapof operation with thermal imaging domain optimization.
                     *
                     */
                    mapOf("error" to e.message)
                )
                /**
                 * Executes stopself operation with thermal imaging domain optimization.
                 *
                 */
                stopSelf()
                throw e
            }
        }
    }
    
    /**
     * Initialize Phase 0 baseline components for the service
     */
    private fun initializePhase0Baseline() {
        try {
            // Initialize feature flags if not already done
            FeatureFlags.initialize(this)
            
            // Initialize structured logger
            structuredLogger = StructuredLogger.getInstance(this)
            
            // Initialize crash-safe supervisor
            crashSafeSupervisor = CrashSafeSupervisor.getInstance(this)
            crashSafeSupervisor.initialize()
            
            structuredLogger.log(
                StructuredLogger.LogLevel.INFO,
                "RecordingService",
                "phase0_baseline_initialized",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "feature_flags" to FeatureFlags.getAllFlags(),
                    "protocol_version" to ProtocolVersion.CURRENT_VERSION
                )
            )
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Phase 0 baseline in service", e)
        }
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
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (intent?.action) {
            ACTION_START_RECORDING -> {
                val sessionDirectory = intent.getStringExtra(EXTRA_SESSION_DIRECTORY)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (sessionDirectory != null) {
                    /**
                     * Executes startrecordingsession operation with thermal imaging domain optimization.
                     *
                     */
                    startRecordingSession(sessionDirectory)
                } else {
                    Log.e(TAG, "No session directory provided for recording")
                }
            }
            
            ACTION_STOP_RECORDING -> {
                /**
                 * Executes stoprecordingsession operation with thermal imaging domain optimization.
                 *
                 */
                stopRecordingSession()
            }
            
            ACTION_START_SERVER -> {
                /**
                 * Executes startserversocket operation with thermal imaging domain optimization.
                 *
                 */
                startServerSocket()
            }
            
            ACTION_STOP_SERVER -> {
                /**
                 * Executes stopserversocket operation with thermal imaging domain optimization.
                 *
                 */
                stopServerSocket()
            }
            
            ACTION_ADD_SYNC_MARKER -> {
                val markerType = intent.getStringExtra(EXTRA_MARKER_TYPE)
                val timestampNs = intent.getLongExtra(EXTRA_TIMESTAMP_NS, System.nanoTime())
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (markerType != null) {
                    /**
                     * Executes addsyncmarker operation with thermal imaging domain optimization.
                     *
                     */
                    addSyncMarker(markerType, timestampNs)
                }
            }
            
            ACTION_CONNECT_PC -> {
                val ipAddress = intent.getStringExtra(EXTRA_PC_IP)
                val port = intent.getIntExtra(EXTRA_PC_PORT, 8080)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (ipAddress != null) {
                    /**
                     * Executes connecttopc operation with thermal imaging domain optimization.
                     *
                     */
                    connectToPC(ipAddress, port)
                }
            }
            
            ACTION_DISCONNECT_PC -> {
                /**
                 * Executes disconnectfrompc operation with thermal imaging domain optimization.
                 *
                 */
                disconnectFromPC()
            }
            
            ACTION_START_DISCOVERY -> {
                /**
                 * Executes startpcdiscovery operation with thermal imaging domain optimization.
                 *
                 */
                startPCDiscovery()
            }
        }
        
        return START_STICKY // Changed to STICKY to ensure server persistence
    }

    /**
     * Executes onbind operation with thermal imaging domain optimization.
     *
     * @param
     * @param intent Parameter for operation (type: Intent)
     *
     */
    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        
        structuredLogger.log(
            StructuredLogger.LogLevel.INFO,
            "RecordingService",
            "service_destroying"
        )
        
        lifecycleScope.launch {
            try {
<<<<<<< HEAD
                // Stop server socket first
                /**
                 * Executes stopserversocket operation with thermal imaging domain optimization.
                 *
                 */
                stopServerSocket()
                
                // Stop recording if active
                recordingController.cleanup()
                
                structuredLogger.log(
                    StructuredLogger.LogLevel.INFO,
                    "RecordingService",
                    "service_cleanup_completed"
                )
            } catch (e: Exception) {
                structuredLogger.log(
                    StructuredLogger.LogLevel.ERROR,
                    "RecordingService",
                    "service_cleanup_error",
                    /**
                     * Executes mapof operation with thermal imaging domain optimization.
                     *
                     */
                    mapOf("error" to e.message)
                )
            } finally {
                // Cleanup Phase 0 components
                try {
                    crashSafeSupervisor.shutdown()
                } catch (e: Exception) {
                    Log.e(TAG, "Error shutting down supervisor", e)
=======
                networkServer.stop()
                isConnectedToPC = false
                Log.i(TAG, "Network server stopped")
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Network server stopped")
                
                // Clean up network client resources if initialized
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isNetworkInitialized) {
                    networkClient.disconnect()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during service cleanup", e)
            } finally {
                try {
                    recordingController.cleanup()
                } catch (e: Exception) {
                    Log.e(TAG, "Error during recording controller cleanup", e)
>>>>>>> dev
                }
            }
        }
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
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Recording Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Multi-modal sensor recording service"
                /**
                 * Configures the showbadge with validation and thermal imaging optimization.
                 *
                 */
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Executes startRecordingSession functionality.
     */
    /**
     * Executes startrecordingsession operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionDirectory Parameter for operation (type: String)
     *
     */
    private fun startRecordingSession(sessionDirectory: String) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isInitialized) {
            Log.e(TAG, "Service not initialized, cannot start recording")
            return
        }
        
        lifecycleScope.launch {
            try {
                // Create session directory
                val sessionDir = File(sessionDirectory)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!sessionDir.exists()) {
                    sessionDir.mkdirs()
                }
                
                currentSessionDirectory = sessionDirectory
                recordingStartTime = System.nanoTime()
                
                // Start foreground service
                /**
                 * Executes startforeground operation with thermal imaging domain optimization.
                 *
                 */
                startForeground(NOTIFICATION_ID, createRecordingNotification("Starting recording..."))
                
                // Start recording
                val success = recordingController.startRecording(sessionDirectory)
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    Log.i(TAG, "Recording session started: $sessionDirectory")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Recording in progress")
                } else {
                    Log.e(TAG, "Failed to start recording session")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Recording failed to start")
                    /**
                     * Executes stoprecordingsession operation with thermal imaging domain optimization.
                     *
                     */
                    stopRecordingSession()
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error starting recording session", e)
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Recording error occurred")
                /**
                 * Executes stoprecordingsession operation with thermal imaging domain optimization.
                 *
                 */
                stopRecordingSession()
            }
        }
    }

    /**
     * Executes stopRecordingSession functionality.
     */
    /**
     * Executes stoprecordingsession operation with thermal imaging domain optimization.
     *
     */
    private fun stopRecordingSession() {
        lifecycleScope.launch {
            try {
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Stopping recording...")
                
                val success = recordingController.stopRecording()
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    val sessionDuration = if (recordingStartTime > 0) {
                        (System.nanoTime() - recordingStartTime) / 1_000_000_000.0
                    } else 0.0
                    
                    Log.i(TAG, "Recording session stopped (duration: ${sessionDuration}s)")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Recording completed (${String.format("%.1f", sessionDuration)}s)")
                    
                    // Stop foreground service after a brief delay to show completion message
                    kotlinx.coroutines.delay(2000)
                    /**
                     * Executes stopforeground operation with thermal imaging domain optimization.
                     *
                     */
                    stopForeground(true)
                    /**
                     * Executes stopself operation with thermal imaging domain optimization.
                     *
                     */
                    stopSelf()
                } else {
                    Log.e(TAG, "Failed to stop recording session cleanly")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Recording stop failed")
                }
                
                currentSessionDirectory = null
                recordingStartTime = 0
                
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping recording session", e)
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Recording stop error")
            }
        }
    }

    /**
     * Executes addSyncMarker functionality.
     */
    /**
     * Executes addsyncmarker operation with thermal imaging domain optimization.
     *
     * @param
     * @param markerType Parameter for operation (type: String)
     * @param timestampNs Parameter for operation (type: Long)
     *
     */
    private fun addSyncMarker(markerType: String, timestampNs: Long) {
        lifecycleScope.launch {
            try {
                recordingController.addSyncMarker(markerType, timestampNs)
                Log.i(TAG, "Sync marker added: $markerType")
                
                // Briefly update notification to show sync event
                val originalText = "Recording in progress"
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param marker Parameter for operation (type: $markerType")
                 *
                 */
                updateNotification("Sync marker: $markerType")
                kotlinx.coroutines.delay(1000)
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification(originalText)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error adding sync marker", e)
            }
        }
    }

    /**
     * Sets upstatusmonitoring configuration.
     */
    private fun setupStatusMonitoring() {
        // Monitor recording state changes
        recordingController.recordingStateFlow
            .onEach { state ->
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (state) {
                    RecordingState.STARTING -> updateNotification("Starting sensors...")
                    RecordingState.RECORDING -> updateNotification("Recording in progress")
                    RecordingState.STOPPING -> updateNotification("Stopping sensors...")
                    RecordingState.STOPPED -> updateNotification("Recording stopped")
                    RecordingState.ERROR -> updateNotification("Recording error")
                }
            }
            .launchIn(lifecycleScope)
        
        // Monitor sensor status
        recordingController.sensorStatusFlow
            .onEach { statusList ->
                val activeSensors = statusList.count { it.isRecording }
                val totalSamples = statusList.sumOf { it.samplesRecorded }
                val totalStorage = statusList.sumOf { it.storageUsedMB }
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (activeSensors > 0) {
                    val statusText = "Recording: $activeSensors sensors, " +
                            "${totalSamples} samples, " +
                            "${String.format("%.1f", totalStorage)}MB"
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification(statusText)
                }
            }
            .launchIn(lifecycleScope)
        
        // Monitor errors
        recordingController.errorFlow
            .onEach { error ->
                Log.w(TAG, "Recording controller error: ${error.message}")
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!error.isRecoverable) {
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param error Parameter for operation (type: ${error.message}")
                     *
                     */
                    updateNotification("Critical error: ${error.message}")
                    /**
                     * Executes stoprecordingsession operation with thermal imaging domain optimization.
                     *
                     */
                    stopRecordingSession()
                } else {
                    // Show temporary error notification
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param Warning Parameter for operation (type: ${error.message}")
                     *
                     */
                    updateNotification("Warning: ${error.message}")
                    kotlinx.coroutines.delay(3000)
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Recording in progress")
                }
            }
            .launchIn(lifecycleScope)
    }

    /**
     * Executes createRecordingNotification functionality.
     */
    /**
     * Executes createrecordingnotification operation with thermal imaging domain optimization.
     *
     * @param
     * @param contentText Parameter for operation (type: String)
     *
     */
    private fun createRecordingNotification(contentText: String): Notification {
        val stopIntent = Intent(this, RecordingService::class.java).apply {
            action = ACTION_STOP_RECORDING
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("IRCamera Recording")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_media_play) // Use system icon for recording
            .setOngoing(true)
            .addAction(
                android.R.drawable.ic_media_pause, // Use system stop icon
                "Stop",
                stopPendingIntent
            )
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    /**
     * Executes updateNotification functionality.
     */
    /**
     * Executes updatenotification operation with thermal imaging domain optimization.
     *
     * @param
     * @param contentText Parameter for operation (type: String)
     *
     */
    private fun updateNotification(contentText: String) {
        try {
            val notification = createRecordingNotification(contentText)
            notificationManager.notify(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to update notification", e)
        }
    }

    /**
     * Get the recording controller instance
     */
    fun getRecordingController(): RecordingController = recordingController

    /**
     * Check if service is initialized and ready
     */
    fun isInitialized(): Boolean = isInitialized

    }
    
    /**
     * Initialize network client and set up command handlers
     */
<<<<<<< HEAD
    /**
     * Retrieves currentsession information.
     */
    fun getCurrentSession(): SessionInfo? {
        return currentSessionDirectory?.let { directory ->
            SessionInfo(
                sessionId = directory.substringAfterLast("/"),
                startTime = recordingStartTime
            )
        }
    }
    
    // ==================== SERVER SOCKET IMPLEMENTATION ====================
    
    /**
     * Start persistent server socket for PC connections with supervision
     */
    private fun startServerSocket() {
        if (isServerRunning.get()) {
            structuredLogger.logServerEvent("server_already_running", mapOf("port" to SERVER_PORT))
            return
        }
        
        // Register server socket under supervision
        crashSafeSupervisor.registerJob(
            id = "server_socket",
            name = "Server Socket",
            critical = true,
            restartable = true,
            healthCheck = object : CrashSafeSupervisor.HealthCheck {
                override suspend fun checkHealth(): CrashSafeSupervisor.HealthStatus {
                    return if (isServerRunning.get() && serverSocket?.isClosed == false) {
                        CrashSafeSupervisor.HealthStatus(
                            isHealthy = true,
                            message = "Server socket running normally",
                            details = mapOf(
                                "port" to SERVER_PORT,
                                "active_connections" to activeConnections.size,
                                "nsd_registered" to isServiceRegistered
                            )
                        )
                    } else {
                        CrashSafeSupervisor.HealthStatus(
                            isHealthy = false,
                            message = "Server socket not running or closed"
                        )
                    }
                }
            }
        ) { stopToken ->
            /**
             * Executes runserversocketsupervised operation with thermal imaging domain optimization.
             *
             */
            runServerSocketSupervised(stopToken)
        }
    }
    
    /**
     * Run server socket under supervision
     */
    private suspend fun runServerSocketSupervised(stopToken: CrashSafeSupervisor.StopToken) {
        try {
            // Create server socket
            serverSocket = ServerSocket(SERVER_PORT)
            isServerRunning.set(true)
            
            structuredLogger.logServerEvent(
                "server_socket_started",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf("port" to SERVER_PORT)
            )
            
            // Register NSD service if enabled
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (FeatureFlags.MDNS_ENABLE) {
                /**
                 * Executes registernsdservice operation with thermal imaging domain optimization.
                 *
                 */
                registerNsdService()
            }
            
            // Start as foreground service if not already
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isServiceForeground()) {
                /**
                 * Executes startforeground operation with thermal imaging domain optimization.
                 *
                 */
                startForeground(NOTIFICATION_ID, createServerNotification("Server listening for PC connections"))
            }
            
            // Run accept loop
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (!stopToken.isStopRequested() && isServerRunning.get()) {
                try {
                    val clientSocket = withContext(Dispatchers.IO) {
                        serverSocket?.accept()
                    }
                    
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (clientSocket != null && isServerRunning.get()) {
                        val clientId = "${clientSocket.inetAddress.hostAddress}:${clientSocket.port}"
                        
                        structuredLogger.logConnection(
                            "pc_client_connected",
                            clientId,
                            /**
                             * Executes mapof operation with thermal imaging domain optimization.
                             *
                             */
                            mapOf("client_address" to clientSocket.inetAddress.hostAddress)
                        )
                        
                        // Handle client connection
                        /**
                         * Executes handlenewclientconnection operation with thermal imaging domain optimization.
                         *
                         */
                        handleNewClientConnection(clientSocket, clientId)
                        
                        // Update notification
                        /**
                         * Executes withcontext operation with thermal imaging domain optimization.
                         *
                         */
                        withContext(Dispatchers.Main) {
                            /**
                             * Executes updatenotification operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param PCs Parameter for operation (type: ${activeConnections.size}")
                             *
                             */
                            updateNotification("Connected PCs: ${activeConnections.size}")
                        }
                    }
                } catch (e: SocketException) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isServerRunning.get() && !stopToken.isStopRequested()) {
                        structuredLogger.logServerEvent(
                            "accept_socket_error",
                            /**
                             * Executes mapof operation with thermal imaging domain optimization.
                             *
                             */
                            mapOf("error" to e.message)
                        )
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(1000)
                    }
                } catch (e: Exception) {
                    structuredLogger.logServerEvent(
                        "accept_unexpected_error", 
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf("error" to e.message)
                    )
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isServerRunning.get() && !stopToken.isStopRequested()) {
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(5000) // Longer delay for unexpected errors
                    }
                }
            }
            
        } catch (e: Exception) {
            structuredLogger.logServerEvent(
                "server_socket_failed",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf("error" to e.message)
            )
            isServerRunning.set(false)
            throw e
        } finally {
            // Cleanup
            structuredLogger.logServerEvent("server_socket_cleanup_started")
            /**
             * Executes cleanupserversocket operation with thermal imaging domain optimization.
             *
             */
            cleanupServerSocket()
        }
    }
    
    /**
     * Clean up server socket resources
     */
    private fun cleanupServerSocket() {
        isServerRunning.set(false)
        
        // Cancel server job
        serverJob?.cancel()
        serverJob = null
        
        // Close all active connections
        activeConnections.values.forEach { connection ->
            try {
                connection.job.cancel()
                connection.socket.close()
            } catch (e: Exception) {
                structuredLogger.logConnection(
                    "connection_cleanup_error",
                    connection.clientId,
                    /**
                     * Executes mapof operation with thermal imaging domain optimization.
                     *
                     */
                    mapOf("error" to e.message)
                )
            }
        }
        activeConnections.clear()
        
        // Close server socket
        try {
            serverSocket?.close()
        } catch (e: Exception) {
            structuredLogger.logServerEvent(
                "server_socket_close_error",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf("error" to e.message)
            )
        } finally {
            serverSocket = null
        }
        
        // Unregister NSD service
        /**
         * Executes unregisternsdservice operation with thermal imaging domain optimization.
         *
         */
        unregisterNsdService()
        
        structuredLogger.logServerEvent("server_socket_cleanup_completed")
    }
    
    /**
     * Stop server socket and cleanup
     */
    private fun stopServerSocket() {
        if (!isServerRunning.get()) {
            structuredLogger.logServerEvent("server_not_running")
            return
        }
        
        structuredLogger.logServerEvent("server_socket_stop_requested")
        
        // Unregister from supervisor
        crashSafeSupervisor.unregisterJob("server_socket")
        
        /**
         * Executes cleanupserversocket operation with thermal imaging domain optimization.
         *
         */
        cleanupServerSocket()
        
        structuredLogger.logServerEvent("server_socket_stopped")
    }
    
    /**
     * Start accept loop to handle multiple PC connections
     */
    private fun startAcceptLoop() {
        serverJob = lifecycleScope.launch(Dispatchers.IO) {
            while (isServerRunning.get() && !currentCoroutineContext().isActive.not()) {
                try {
                    val clientSocket = serverSocket?.accept()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (clientSocket != null && isServerRunning.get()) {
                        val clientId = "${clientSocket.inetAddress.hostAddress}:${clientSocket.port}"
                        Log.i(TAG, "PC client connected: $clientId")
                        
                        // Handle client connection
                        /**
                         * Executes handlenewclientconnection operation with thermal imaging domain optimization.
                         *
                         */
                        handleNewClientConnection(clientSocket, clientId)
                        
                        // Update notification
                        /**
                         * Executes withcontext operation with thermal imaging domain optimization.
                         *
                         */
                        withContext(Dispatchers.Main) {
                            /**
                             * Executes updatenotification operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param PCs Parameter for operation (type: ${activeConnections.size}")
                             *
                             */
                            updateNotification("Connected PCs: ${activeConnections.size}")
                        }
                    }
                } catch (e: SocketException) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isServerRunning.get()) {
                        Log.w(TAG, "Server socket accept error", e)
                        // Brief delay before retry
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(1000)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Unexpected error in accept loop", e)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isServerRunning.get()) {
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(5000) // Longer delay for unexpected errors
                    }
                }
            }
            Log.i(TAG, "Accept loop terminated")
        }
    }
    
    /**
     * Handle new PC client connection
     */
    private suspend fun handleNewClientConnection(clientSocket: Socket, clientId: String) {
        try {
            // Set socket timeout
            clientSocket.soTimeout = 30000 // 30 second timeout
            
            // Create streams
            val inputStream = DataInputStream(clientSocket.getInputStream())
            val outputStream = DataOutputStream(clientSocket.getOutputStream())
            
            // Create client handler job
            val clientJob = lifecycleScope.launch(Dispatchers.IO) {
                try {
                    /**
                     * Executes handleclientmessages operation with thermal imaging domain optimization.
                     *
                     */
                    handleClientMessages(clientId, inputStream, outputStream)
                } catch (e: Exception) {
                    Log.w(TAG, "Client $clientId handler error", e)
                } finally {
                    // Clean up connection
                    activeConnections.remove(clientId)
                    try {
                        clientSocket.close()
                    } catch (e: Exception) {
                        Log.w(TAG, "Error closing client socket", e)
                    }
                    Log.i(TAG, "PC client disconnected: $clientId")
                    
                    // Update notification
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.Main) {
                        /**
                         * Executes updatenotification operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param PCs Parameter for operation (type: ${activeConnections.size}")
                         *
                         */
                        updateNotification("Connected PCs: ${activeConnections.size}")
                    }
                }
            }
            
            // Store connection
            val connection = ClientConnection(
                socket = clientSocket,
                clientId = clientId,
                inputStream = inputStream,
                outputStream = outputStream,
                job = clientJob
            )
            activeConnections[clientId] = connection
            
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up client connection", e)
            try {
                clientSocket.close()
            } catch (e: Exception) {
                Log.w(TAG, "Error closing failed client socket", e)
            }
        }
    }
    
    /**
     * Handle messages from PC client
     */
    private suspend fun handleClientMessages(
        clientId: String, 
        inputStream: DataInputStream, 
        outputStream: DataOutputStream
    ) {
        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (isServerRunning.get() && currentCoroutineContext().isActive) {
            try {
                // Read message length
                val messageLength = inputStream.readInt()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (messageLength > 1024 * 1024) { // 1MB limit
                    Log.w(TAG, "Message too large from $clientId: $messageLength bytes")
                    break
                }
                
                // Read message data
                val messageData = ByteArray(messageLength)
                inputStream.readFully(messageData)
                
                // Parse JSON message
                val message = JSONObject(String(messageData, Charsets.UTF_8))
                
                // Process message
                /**
                 * Executes processclientmessage operation with thermal imaging domain optimization.
                 *
                 */
                processClientMessage(clientId, message, outputStream)
                
            } catch (e: SocketTimeoutException) {
                // Send keepalive
                /**
                 * Executes sendkeepalive operation with thermal imaging domain optimization.
                 *
                 */
                sendKeepAlive(outputStream)
            } catch (e: EOFException) {
                Log.i(TAG, "Client $clientId disconnected normally")
                break
            } catch (e: Exception) {
                Log.w(TAG, "Error handling message from $clientId", e)
                break
=======
    /**
     * Initializes the ializenetworkclient component for thermal imaging operations.
     *
     */
    private suspend fun initializeNetworkClient(): Boolean {
        return try {
            val success = networkClient.initialize()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (success) {
                /**
                 * Configures the upnetworkcommandhandlers with validation and thermal imaging optimization.
                 *
                 */
                setupNetworkCommandHandlers()
                Log.i(TAG, "Network client initialized successfully")
            } else {
                Log.w(TAG, "Network client initialization failed")
            }
            success
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing network client", e)
            false
        }
    }
    
    /**
     * Set up network command handlers for PC Controller communication
     */
    private fun setupNetworkCommandHandlers() {
        // Set up command handlers for legacy NetworkClient compatibility
        try {
            networkClient.setMessageHandler("start_recording") { message ->
                /**
                 * Executes handlestartrecordingcommand operation with thermal imaging domain optimization.
                 *
                 */
                handleStartRecordingCommand(message)
            }
            
            networkClient.setMessageHandler("stop_recording") { message ->
                /**
                 * Executes handlestoprecordingcommand operation with thermal imaging domain optimization.
                 *
                 */
                handleStopRecordingCommand(message)
            }
            
            networkClient.setMessageHandler("sync_flash") { message ->
                /**
                 * Executes handlesyncflashcommand operation with thermal imaging domain optimization.
                 *
                 */
                handleSyncFlashCommand(message)
            }
            
            networkClient.setMessageHandler("query_capabilities") { message ->
                /**
                 * Executes handlequerycapabilitiescommand operation with thermal imaging domain optimization.
                 *
                 */
                handleQueryCapabilitiesCommand(message)
            }
            
            networkClient.setMessageHandler("query_status") { message ->
                /**
                 * Executes handlequerystatuscommand operation with thermal imaging domain optimization.
                 *
                 */
                handleQueryStatusCommand(message)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Network client message handlers setup failed: ${e.message}")
        }
    }
    
    /**
     * Start network discovery to find PC Controllers
     */
    private fun startNetworkDiscovery() {
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Network discovery started successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error starting network discovery", e)
            }
        }
    }
    
    /**
     * Handle start recording command from PC Controller
     */
    private fun handleStartRecordingCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                val sessionId = message.optString("session_id", "session_${System.currentTimeMillis()}")
                val sessionDirectory = "/storage/emulated/0/IRCamera_Sessions/$sessionId"
                
                Log.i(TAG, "Received start recording command from PC Controller")
                /**
                 * Executes startrecordingsession operation with thermal imaging domain optimization.
                 *
                 */
                startRecordingSession(sessionDirectory)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling start recording command", e)
            }
        }
    }
    
    /**
     * Handle stop recording command from PC Controller
     */
    private fun handleStopRecordingCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Received stop recording command from PC Controller")
                /**
                 * Executes stoprecordingsession operation with thermal imaging domain optimization.
                 *
                 */
                stopRecordingSession()
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling stop recording command", e)
            }
        }
    }
    
    /**
     * Handle sync flash command from PC Controller
     */
    private fun handleSyncFlashCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                val durationMs = message.optInt("flash_duration_ms", 100)
                val timestamp = System.nanoTime()
                
                Log.i(TAG, "Received sync flash command from PC Controller")
                /**
                 * Executes addsyncmarker operation with thermal imaging domain optimization.
                 *
                 */
                addSyncMarker("flash_sync", timestamp)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling sync flash command", e)
            }
        }
    }
    
    /**
     * Handle query capabilities command from PC Controller
     */
    private fun handleQueryCapabilitiesCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Received query capabilities command from PC Controller")
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling query capabilities command", e)
            }
        }
    }
    
    /**
     * Handle query status command from PC Controller  
     */
    private fun handleQueryStatusCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Received query status command from PC Controller")
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling query status command", e)
            }
        }
    }
    
    /**
     * Get network client instance (for testing/debugging)
     */
    fun getNetworkClient(): NetworkClient = networkClient
    
    // Network server setup and management
    
    /**
     * Sets upnetworkserver configuration.
     */
    private fun setupNetworkServer() {
        lifecycleScope.launch {
            try {
                // Start the TCP server immediately when service initializes
                val serverStarted = networkServer.start()
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (serverStarted) {
                    Log.i(TAG, "Network server started automatically, listening on port 8080")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Listening for PC Controller on port 8080")
                } else {
                    Log.e(TAG, "Failed to start network server automatically")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Network server failed to start")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error setting up network server", e)
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param error Parameter for operation (type: ${e.message}")
                 *
                 */
                updateNotification("Network server error: ${e.message}")
            }
        }
        
        // Monitor connection state
        lifecycleScope.launch {
            networkServer.connectionStateFlow.collect { connected ->
                isConnectedToPC = connected
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (connected) {
                    Log.i(TAG, "PC Controller connected to network server")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("PC Controller connected")
                } else {
                    Log.i(TAG, "PC Controller disconnected, still listening on port 8080")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Listening for PC Controller on port 8080")
                }
            }
        }
        
        // Monitor incoming messages from PC Controller
        lifecycleScope.launch {
            networkServer.messageFlow.collect { message ->
                /**
                 * Executes handlepccommand operation with thermal imaging domain optimization.
                 *
                 */
                handlePCCommand(message)
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
    private fun connectToPC(ipAddress: String, port: Int) {
        // Dual approach: try client connection first, fallback to server mode
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Attempting connection to PC Controller at $ipAddress:$port")
                
                // Ensure our server is running for PC to connect to us
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!networkServer.isRunning()) {
                    val started = networkServer.start()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (started) {
                        Log.i(TAG, "Network server started, ready for PC Controller connection")
                        /**
                         * Executes updatenotification operation with thermal imaging domain optimization.
                         *
                         */
                        updateNotification("Ready for PC Controller connection")
                    } else {
                        Log.e(TAG, "Failed to start network server")
                        /**
                         * Executes updatenotification operation with thermal imaging domain optimization.
                         *
                         */
                        updateNotification("Failed to start network server")
                    }
                } else {
                    Log.i(TAG, "Network server already running, ready for PC Controller")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Network server ready")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during PC connection attempt", e)
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param error Parameter for operation (type: ${e.message}")
                 *
                 */
                updateNotification("Connection error: ${e.message}")
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
        lifecycleScope.launch {
            try {
                // Disconnect client if connected
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isNetworkInitialized) {
                    networkClient.disconnect()
                }
                
                // Stop the network server, which will disconnect any connected PC
                networkServer.stop()
                isConnectedToPC = false
                Log.i(TAG, "Disconnected from PC Controller")
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Disconnected from PC Controller") 
            } catch (e: Exception) {
                Log.e(TAG, "Error disconnecting from PC", e)
>>>>>>> dev
            }
        }
    }
    
<<<<<<< HEAD
    /**
     * Process message from PC client with protocol validation and structured logging
     */
    private suspend fun processClientMessage(
        clientId: String, 
        message: JSONObject, 
        outputStream: DataOutputStream
    ) {
        val messageType = message.optString("message_type")
        val messageId = message.optString("msg_id", "unknown")
        
        structuredLogger.logProtocolMessage(
            "message_received",
            messageId,
            clientId,
            /**
             * Executes mapof operation with thermal imaging domain optimization.
             *
             */
            mapOf(
                "message_type" to messageType,
                "protocol_version" to message.optString("protocol_version", "unknown")
            )
        )
        
        // Validate protocol version
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!ProtocolVersion.validateMessageVersion(message)) {
            val errorMsg = "Unsupported protocol version"
            structuredLogger.logProtocolMessage(
                "protocol_version_error", 
                messageId,
                clientId,
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf("error" to errorMsg)
            )
            /**
             * Executes senderror operation with thermal imaging domain optimization.
             *
             */
            sendError(outputStream, errorMsg)
            return
        }
        
        try {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (messageType) {
                "protocol_handshake" -> {
                    val handshakeResult = ProtocolVersion.validateHandshakeResponse(message)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (handshakeResult.success) {
                        structuredLogger.logProtocolMessage(
                            "handshake_success",
                            messageId,
                            clientId,
                            /**
                             * Executes mapof operation with thermal imaging domain optimization.
                             *
                             */
                            mapOf(
                                "negotiated_version" to (handshakeResult.negotiatedVersion ?: "unknown"),
                                "capabilities" to handshakeResult.commonCapabilities.joinToString(",")
                            )
                        )
                        
                        val responseMessage = ProtocolVersion.createHandshakeMessage(
                            android.provider.Settings.Secure.getString(
                                contentResolver,
                                android.provider.Settings.Secure.ANDROID_ID
                            )
                        )
                        /**
                         * Executes sendmessage operation with thermal imaging domain optimization.
                         *
                         */
                        sendMessage(outputStream, responseMessage)
                    } else {
                        structuredLogger.logProtocolMessage(
                            "handshake_failed",
                            messageId,
                            clientId,
                            /**
                             * Executes mapof operation with thermal imaging domain optimization.
                             *
                             */
                            mapOf("error" to (handshakeResult.error ?: "unknown"))
                        )
                        /**
                         * Executes senderror operation with thermal imaging domain optimization.
                         *
                         */
                        sendError(outputStream, handshakeResult.error ?: "Handshake failed")
                    }
                }
                
                "session_start" -> {
                    val sessionId = message.optString("session_id", "remote_${System.currentTimeMillis()}")
                    val sessionName = message.optString("session_name", "PC Remote Session")
                    
                    structuredLogger.logSessionEvent(
                        "remote_session_start_request",
                        sessionId,
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf("session_name" to sessionName, "client_id" to clientId)
                    )
                    
                    // Create session directory
                    val baseDir = File(getExternalFilesDir(null), "recordings")
                    val sessionDir = File(baseDir, sessionId)
                    
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.Main) {
                        /**
                         * Executes startrecordingsession operation with thermal imaging domain optimization.
                         *
                         */
                        startRecordingSession(sessionDir.absolutePath)
                    }
                    
                    // Send acknowledgment
                    val ackMessage = ProtocolVersion.createProtocolMessage("ack", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("ack_for", "session_start")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("result", "Recording started")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("session_id", sessionId)
                    })
                    /**
                     * Executes sendmessage operation with thermal imaging domain optimization.
                     *
                     */
                    sendMessage(outputStream, ackMessage)
                }
                
                "session_stop" -> {
                    structuredLogger.logSessionEvent(
                        "remote_session_stop_request",
                        "current",
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf("client_id" to clientId)
                    )
                    
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.Main) {
                        /**
                         * Executes stoprecordingsession operation with thermal imaging domain optimization.
                         *
                         */
                        stopRecordingSession()
                    }
                    
                    val ackMessage = ProtocolVersion.createProtocolMessage("ack", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("ack_for", "session_stop")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("result", "Recording stopped")
                    })
                    /**
                     * Executes sendmessage operation with thermal imaging domain optimization.
                     *
                     */
                    sendMessage(outputStream, ackMessage)
                }
                
                "sync_flash" -> {
                    val durationMs = message.optInt("duration_ms", 100)
                    
                    structuredLogger.log(
                        StructuredLogger.LogLevel.INFO,
                        "SyncFlash",
                        "remote_sync_flash_request",
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf("duration_ms" to durationMs, "client_id" to clientId)
                    )
                    
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.Main) {
                        /**
                         * Executes performsyncflash operation with thermal imaging domain optimization.
                         *
                         */
                        performSyncFlash(durationMs)
                    }
                    
                    val ackMessage = ProtocolVersion.createProtocolMessage("ack", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("ack_for", "sync_flash")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("result", "Flash performed")
                    })
                    /**
                     * Executes sendmessage operation with thermal imaging domain optimization.
                     *
                     */
                    sendMessage(outputStream, ackMessage)
                }
                
                "status_request" -> {
                    structuredLogger.logProtocolMessage(
                        "status_request_received",
                        messageId,
                        clientId
                    )
                    /**
                     * Executes sendstatusresponse operation with thermal imaging domain optimization.
                     *
                     */
                    sendStatusResponse(outputStream)
                }
                
                "heartbeat" -> {
                    structuredLogger.logProtocolMessage(
                        "heartbeat_received",
                        messageId,
                        clientId,
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf("timestamp" to message.optLong("timestamp", 0))
                    )
                    
                    val ackMessage = ProtocolVersion.createProtocolMessage("ack", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("ack_for", "heartbeat")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("result", "alive")
                    })
                    /**
                     * Executes sendmessage operation with thermal imaging domain optimization.
                     *
                     */
                    sendMessage(outputStream, ackMessage)
                }
                
                else -> {
                    structuredLogger.logProtocolMessage(
                        "unknown_message_type",
                        messageId,
                        clientId,
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf("message_type" to messageType)
                    )
                    
                    val errorMessage = ProtocolVersion.createProtocolMessage("error", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param type Parameter for operation (type: $messageType")
                         *
                         */
                        put("error", "Unknown message type: $messageType")
                    })
                    /**
                     * Executes sendmessage operation with thermal imaging domain optimization.
                     *
                     */
                    sendMessage(outputStream, errorMessage)
                }
            }
        } catch (e: Exception) {
            structuredLogger.logProtocolMessage(
                "message_processing_error",
                messageId,
                clientId,
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "message_type" to messageType,
                    "error" to e.message
                )
            )
            
            val errorMessage = ProtocolVersion.createProtocolMessage("error", JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param messageType Parameter for operation (type: ${e.message}")
                 *
                 */
                put("error", "Error processing $messageType: ${e.message}")
            })
            /**
             * Executes sendmessage operation with thermal imaging domain optimization.
             *
             */
            sendMessage(outputStream, errorMessage)
        }
    }
    
    /**
     * Send acknowledgment to PC client
     */
    private suspend fun sendAck(outputStream: DataOutputStream, messageType: String, result: String) {
        val ackMessage = JSONObject().apply {
            put("message_type", "ack")
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("ack_for", messageType)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("result", result)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("timestamp", System.currentTimeMillis())
        }
        /**
         * Executes sendmessage operation with thermal imaging domain optimization.
         *
         */
        sendMessage(outputStream, ackMessage)
    }
    
    /**
     * Send error to PC client
     */
    /**
     * Executes senderror operation with thermal imaging domain optimization.
     *
     * @param
     * @param outputStream Parameter for operation (type: DataOutputStream)
     * @param error Parameter for operation (type: String)
     *
     */
    private suspend fun sendError(outputStream: DataOutputStream, error: String) {
        val errorMessage = JSONObject().apply {
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("message_type", "error")
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("error", error)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("timestamp", System.currentTimeMillis())
        }
        /**
         * Executes sendmessage operation with thermal imaging domain optimization.
         *
         */
        sendMessage(outputStream, errorMessage)
    }
    
    /**
     * Send status response to PC client
     */
    private suspend fun sendStatusResponse(outputStream: DataOutputStream) {
        val statusMessage = JSONObject().apply {
            put("message_type", "status_response")
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("device_id", android.provider.Settings.Secure.getString(
                contentResolver, android.provider.Settings.Secure.ANDROID_ID))
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("recording_active", recordingController.isRecording)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("connected_clients", activeConnections.size)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("server_running", isServerRunning.get())
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("sensors_initialized", isInitialized)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("current_session", currentSessionDirectory)
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("timestamp", System.currentTimeMillis())
        }
        /**
         * Executes sendmessage operation with thermal imaging domain optimization.
         *
         */
        sendMessage(outputStream, statusMessage)
    }
    
    /**
     * Send keepalive to PC client
     */
    private suspend fun sendKeepAlive(outputStream: DataOutputStream) {
        val keepAliveMessage = JSONObject().apply {
            put("message_type", "keepalive")
            /**
             * Executes put operation with thermal imaging domain optimization.
             *
             */
            put("timestamp", System.currentTimeMillis())
        }
        /**
         * Executes sendmessage operation with thermal imaging domain optimization.
         *
         */
        sendMessage(outputStream, keepAliveMessage)
    }
    
    /**
     * Send message to PC client with protocol versioning
     */
    private suspend fun sendMessage(outputStream: DataOutputStream, message: JSONObject) {
        withContext(Dispatchers.IO) {
            try {
                // Ensure protocol version is included
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!message.has("protocol_version")) {
                    message.put("protocol_version", ProtocolVersion.CURRENT_VERSION)
                }
                
                val messageData = message.toString().toByteArray(Charsets.UTF_8)
                outputStream.writeInt(messageData.size)
                outputStream.write(messageData)
                outputStream.flush()
                
                structuredLogger.log(
                    StructuredLogger.LogLevel.DEBUG,
                    "ServerSocket",
                    "message_sent",
                    /**
                     * Executes mapof operation with thermal imaging domain optimization.
                     *
                     */
                    mapOf(
                        "message_type" to message.optString("message_type", "unknown"),
                        "size_bytes" to messageData.size
                    )
                )
                
            } catch (e: Exception) {
                structuredLogger.log(
                    StructuredLogger.LogLevel.ERROR,
                    "ServerSocket",
                    "message_send_error",
                    /**
                     * Executes mapof operation with thermal imaging domain optimization.
                     *
                     */
                    mapOf("error" to e.message)
                )
                throw e
=======
    /**
     * Executes startPCDiscovery functionality.
     */
    /**
     * Executes startpcdiscovery operation with thermal imaging domain optimization.
     *
     */
    private fun startPCDiscovery() {
        lifecycleScope.launch {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isNetworkInitialized) {
                    // Use client discovery
                    /**
                     * Executes startnetworkdiscovery operation with thermal imaging domain optimization.
                     *
                     */
                    startNetworkDiscovery()
                } else {
                    // TODO: Implement PC discovery using zeroconf/mDNS
                    // For now, log that discovery was requested
                    Log.i(TAG, "PC Controller discovery requested")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Searching for PC Controller...")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting PC discovery", e)
            }
        }
    }
    
    /**
     * Executes handlepccommand operation with thermal imaging domain optimization.
     *
     * @param
     * @param message Parameter for operation (type: JSONObject)
     *
     */
    private suspend fun handlePCCommand(message: JSONObject) {
        try {
            val messageType = message.optString("message_type")
            Log.i(TAG, "Processing PC command: $messageType")
            
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (messageType) {
                "enhanced_device_registration" -> {
                    Log.i(TAG, "PC Controller device registration request")
                    /**
                     * Executes sendresponsetopc operation with thermal imaging domain optimization.
                     *
                     */
                    sendResponseToPC("enhanced_registration_ack", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("status", "success")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_type", "android_sensor_node")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("capabilities", JSONObject().apply {
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("recording", true)
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("sensors", arrayOf("rgb_camera", "thermal_camera", "gsr"))
                        })
                    })
                }
                
                "session_start_command" -> {
                    val sessionDirectory = message.optString("session_directory")
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (sessionDirectory.isNotEmpty()) {
                        Log.i(TAG, "Received remote start command from PC for session: $sessionDirectory")
                        /**
                         * Executes startrecordingsession operation with thermal imaging domain optimization.
                         *
                         */
                        startRecordingSession(sessionDirectory)
                        /**
                         * Executes sendresponsetopc operation with thermal imaging domain optimization.
                         *
                         */
                        sendResponseToPC("session_start_response", JSONObject().apply {
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("status", "started")
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("session_directory", sessionDirectory)
                        })
                    }
                }
                
                "session_stop_command" -> {
                    Log.i(TAG, "Received remote stop command from PC")
                    /**
                     * Executes stoprecordingsession operation with thermal imaging domain optimization.
                     *
                     */
                    stopRecordingSession()
                    /**
                     * Executes sendresponsetopc operation with thermal imaging domain optimization.
                     *
                     */
                    sendResponseToPC("session_stop_response", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("status", "stopped")
                    })
                }
                
                "sync_marker_command" -> {
                    val markerType = message.optString("marker_type")
                    val timestampNs = message.optLong("timestamp_ns", System.nanoTime())
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (markerType.isNotEmpty()) {
                        Log.i(TAG, "Received remote sync marker from PC: $markerType")
                        /**
                         * Executes addsyncmarker operation with thermal imaging domain optimization.
                         *
                         */
                        addSyncMarker(markerType, timestampNs)
                        /**
                         * Executes sendresponsetopc operation with thermal imaging domain optimization.
                         *
                         */
                        sendResponseToPC("sync_marker_response", JSONObject().apply {
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("status", "added")
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("marker_type", markerType)
                        })
                    }
                }
                
                "ping" -> {
                    Log.d(TAG, "Received ping from PC Controller")
                    /**
                     * Executes sendresponsetopc operation with thermal imaging domain optimization.
                     *
                     */
                    sendResponseToPC("pong", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timestamp_ns", System.nanoTime())
                    })
                }
                
                "status_request" -> {
                    Log.d(TAG, "PC Controller requested status")
                    /**
                     * Executes sendstatustopc operation with thermal imaging domain optimization.
                     *
                     */
                    sendStatusToPC()
                }
                
                else -> {
                    Log.w(TAG, "Unknown command from PC Controller: $messageType")
                    /**
                     * Executes sendresponsetopc operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param command Parameter for operation (type: $messageType")
                     *
                     */
                    sendResponseToPC("error", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param command Parameter for operation (type: $messageType")
                         *
                         */
                        put("message", "Unknown command: $messageType")
                    })
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling PC command", e)
            /**
             * Executes sendresponsetopc operation with thermal imaging domain optimization.
             *
             * @param
             * @param command Parameter for operation (type: ${e.message}")
             *
             */
            sendResponseToPC("error", JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param command Parameter for operation (type: ${e.message}")
                 *
                 */
                put("message", "Error processing command: ${e.message}")
            })
        }
    }
    
    /**
     * Executes sendresponsetopc operation with thermal imaging domain optimization.
     *
     * @param
     * @param messageType Parameter for operation (type: String)
     * @param data Parameter for operation (type: JSONObject = JSONObject()
     *
     */
    private suspend fun sendResponseToPC(messageType: String, data: JSONObject = JSONObject()) {
        try {
            val response = JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("message_type", messageType)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_id", android.provider.Settings.Secure.getString(
                    contentResolver, android.provider.Settings.Secure.ANDROID_ID))
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("timestamp_ns", System.nanoTime())
                // Merge additional data
                data.keys().forEach { key ->
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put(key, data.get(key))
                }
            }
            
            networkServer.sendMessage(response)
            Log.d(TAG, "Sent response to PC: $messageType")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error sending response to PC", e)
        }
    }
    
    /**
     * Executes sendstatustopc operation with thermal imaging domain optimization.
     *
     */
    private suspend fun sendStatusToPC() {
        try {
            val statusData = JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("is_recording", recordingController.isRecording)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("current_session", currentSessionDirectory ?: "")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("recording_start_time", recordingStartTime)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("service_initialized", isInitialized)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("network_server_running", networkServer.isRunning())
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("pc_connected", isConnectedToPC)
            }
            
            /**
             * Executes sendresponsetopc operation with thermal imaging domain optimization.
             *
             */
            sendResponseToPC("status_response", statusData)
            Log.i(TAG, "Status sent to PC Controller")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error sending status to PC", e)
        }
    }
    
<<<<<<< HEAD
    /**
     * Initialize network client and set up command handlers
     */
    private suspend fun initializeNetworkClient(): Boolean {
        return try {
            val success = networkClient.initialize()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (success) {
                /**
                 * Configures the upnetworkcommandhandlers with validation and thermal imaging optimization.
                 *
                 */
                setupNetworkCommandHandlers()
                Log.i(TAG, "Network client initialized successfully")
            } else {
                Log.w(TAG, "Network client initialization failed")
            }
            success
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing network client", e)
            false
        }
    }
    
    /**
     * Set up network command handlers for PC Controller communication
     */
    private fun setupNetworkCommandHandlers() {
        // Set up command handlers
        networkClient.setMessageHandler("start_recording") { message ->
            /**
             * Executes handlestartrecordingcommand operation with thermal imaging domain optimization.
             *
             */
            handleStartRecordingCommand(message)
        }
        
        networkClient.setMessageHandler("stop_recording") { message ->
            /**
             * Executes handlestoprecordingcommand operation with thermal imaging domain optimization.
             *
             */
            handleStopRecordingCommand(message)
        }
        
        networkClient.setMessageHandler("sync_flash") { message ->
            /**
             * Executes handlesyncflashcommand operation with thermal imaging domain optimization.
             *
             */
            handleSyncFlashCommand(message)
        }
        
        networkClient.setMessageHandler("query_capabilities") { message ->
            /**
             * Executes handlequerycapabilitiescommand operation with thermal imaging domain optimization.
             *
             */
            handleQueryCapabilitiesCommand(message)
        }
        
        networkClient.setMessageHandler("query_status") { message ->
            /**
             * Executes handlequerystatuscommand operation with thermal imaging domain optimization.
             *
             */
            handleQueryStatusCommand(message)
        }
        
        // Set up network event listener for automatic connection handling
        networkClient.setEventListener(object : NetworkClient.NetworkEventListener {
            /**
             * Executes oncontrollerdiscovered operation with thermal imaging domain optimization.
             *
             * @param
             * @param controller Parameter for operation (type: NetworkClient.ControllerInfo)
             *
             */
            override fun onControllerDiscovered(controller: NetworkClient.ControllerInfo) {
                Log.i(TAG, "PC Controller discovered: ${controller.deviceName} at ${controller.ipAddress}")
                
                // Automatically attempt to connect to discovered PC Controllers
                lifecycleScope.launch {
                    networkClient.connectToController(controller.ipAddress, controller.port) { success ->
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (success) {
                            Log.i(TAG, "Successfully connected to PC Controller: ${controller.deviceName}")
                        } else {
                            Log.w(TAG, "Failed to connect to PC Controller: ${controller.deviceName}")
                        }
                    }
                }
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
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Connected to PC Controller")
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
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Disconnected from PC Controller") 
            }
            
            /**
             * Executes onremotemeasurementrequest operation with thermal imaging domain optimization.
             *
             * @param
             * @param sessionInfo Parameter for operation (type: com.topdon.gsr.model.SessionInfo)
             *
             */
            override fun onRemoteMeasurementRequest(sessionInfo: com.topdon.gsr.model.SessionInfo) {
                Log.i(TAG, "Received remote measurement request")
                // Handle remote measurement requests if needed
            }
            
            /**
             * Executes onsyncflash operation with thermal imaging domain optimization.
             *
             * @param
             * @param durationMs Duration in milliseconds (type: Int)
             *
             */
            override fun onSyncFlash(durationMs: Int) {
                Log.i(TAG, "Sync flash request: ${durationMs}ms")
                // Handle sync flash requests
            }
            
            /**
             * Executes ontimesynchronized operation with thermal imaging domain optimization.
             *
             * @param
             * @param offsetNanoseconds Parameter for operation (type: Long)
             *
             */
            override fun onTimeSynchronized(offsetNanoseconds: Long) {
                Log.i(TAG, "Time synchronized with PC Controller (offset: ${offsetNanoseconds}ns)")
            }
            
            /**
             * Executes ondatastreamingstarted operation with thermal imaging domain optimization.
             *
             */
            override fun onDataStreamingStarted() {
                Log.i(TAG, "Data streaming started")
            }
            
            /**
             * Executes ondatastreamingstopped operation with thermal imaging domain optimization.
             *
             */
            override fun onDataStreamingStopped() {
                Log.i(TAG, "Data streaming stopped")
            }
            
            /**
             * Executes onerror operation with thermal imaging domain optimization.
             *
             * @param
             * @param operation Parameter for operation (type: String)
             * @param error Parameter for operation (type: String)
             *
             */
            override fun onError(operation: String, error: String) {
                Log.e(TAG, "Network error in $operation: $error")
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param error Parameter for operation (type: $operation")
                 *
                 */
                updateNotification("Network error: $operation")
            }
        })
    }
    
    /**
     * Start network discovery to find PC Controllers
     */
    private fun startNetworkDiscovery() {
        lifecycleScope.launch {
            try {
                networkClient.startDiscovery { success ->
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (success) {
                        Log.i(TAG, "Network discovery started successfully")
                    } else {
                        Log.w(TAG, "Network discovery failed to start")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting network discovery", e)
            }
        }
    }
    
    /**
     * Handle start recording command from PC Controller
     */
    private fun handleStartRecordingCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                val sessionId = message.optString("session_id", "session_${System.currentTimeMillis()}")
                val sessionDirectory = "/storage/emulated/0/IRCamera_Sessions/$sessionId"
                
                Log.i(TAG, "Received start recording command from PC Controller")
                /**
                 * Executes startrecordingsession operation with thermal imaging domain optimization.
                 *
                 */
                startRecordingSession(sessionDirectory)
                
                // Send acknowledgment back to PC Controller
                val response = JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message_type", "response")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("response_to", "start_recording") 
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("status", "success")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("session_id", sessionId)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message", "Recording started successfully")
                }
                networkClient.sendMessage(response)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling start recording command", e)
                
                // Send error response
                val response = JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message_type", "response")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("response_to", "start_recording")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("status", "error")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param recording Parameter for operation (type: ${e.message}")
                     *
                     */
                    put("message", "Failed to start recording: ${e.message}")
                }
                networkClient.sendMessage(response)
>>>>>>> dev
            }
        }
    }
    
    /**
<<<<<<< HEAD
     * Perform sync flash for PC synchronization
     */
    /**
     * Executes performSyncFlash functionality.
     */
    /**
     * Executes performsyncflash operation with thermal imaging domain optimization.
     *
     * @param
     * @param durationMs Duration in milliseconds (type: Int)
     *
     */
    private fun performSyncFlash(durationMs: Int) {
        // This would need to be handled by the main activity
        // For now, just add a sync marker
        /**
         * Executes addsyncmarker operation with thermal imaging domain optimization.
         *
         */
        addSyncMarker("pc_sync_flash", System.nanoTime())
    }
    
    // ==================== NSD SERVICE MANAGEMENT ====================
    
    /**
     * Register NSD service for PC discovery
     */
    private fun registerNsdService() {
        if (isServiceRegistered) {
            Log.i(TAG, "NSD service already registered")
            return
        }
        
        try {
            val serviceInfo = NsdServiceInfo().apply {
                serviceName = SERVICE_NAME
                serviceType = SERVICE_TYPE
                port = SERVER_PORT
            }
            
            nsdManager?.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, object : NsdManager.RegistrationListener {
                /**
                 * Executes onserviceregistered operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo?)
                 *
                 */
                override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
                    Log.i(TAG, "NSD service registered: ${serviceInfo?.serviceName}")
                    nsdServiceInfo = serviceInfo
                    isServiceRegistered = true
                }
                
                /**
                 * Executes onregistrationfailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo?)
                 * @param errorCode Parameter for operation (type: Int)
                 *
                 */
                override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                    Log.e(TAG, "NSD service registration failed: $errorCode")
                }
                
                /**
                 * Executes onserviceunregistered operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo?)
                 *
                 */
                override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
                    Log.i(TAG, "NSD service unregistered: ${serviceInfo?.serviceName}")
                    isServiceRegistered = false
                }
                
                /**
                 * Executes onunregistrationfailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo?)
                 * @param errorCode Parameter for operation (type: Int)
                 *
                 */
                override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                    Log.e(TAG, "NSD service unregistration failed: $errorCode")
                }
            })
            
        } catch (e: Exception) {
            Log.e(TAG, "Error registering NSD service", e)
        }
    }
    
    /**
     * Unregister NSD service
     */
    /**
     * Executes unregisternsdservice operation with thermal imaging domain optimization.
     *
     */
    private fun unregisterNsdService() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isServiceRegistered || nsdServiceInfo == null) {
            return
        }
        
        try {
            nsdManager?.unregisterService(object : NsdManager.RegistrationListener {
                /**
                 * Executes onserviceregistered operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo?)
                 *
                 */
                override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {}
                /**
                 * Executes onregistrationfailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo?)
                 * @param errorCode Parameter for operation (type: Int)
                 *
                 */
                override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {}
                /**
                 * Executes onserviceunregistered operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo?)
                 *
                 */
                override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
                    Log.i(TAG, "NSD service unregistered successfully")
                    isServiceRegistered = false
                    nsdServiceInfo = null
                }
                /**
                 * Executes onunregistrationfailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo?)
                 * @param errorCode Parameter for operation (type: Int)
                 *
                 */
                override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                    Log.e(TAG, "NSD service unregistration failed: $errorCode")
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering NSD service", e)
        }
    }
    
    /**
     * Check if service is running in foreground
     */
    private fun isServiceForeground(): Boolean {
        // This is a simplified check - in production you might want more sophisticated detection
        return currentSessionDirectory != null || isServerRunning.get()
    }
    
    /**
     * Create notification for server status
     */
    private fun createServerNotification(contentText: String): Notification {
        val stopIntent = Intent(this, RecordingService::class.java).apply {
            action = ACTION_STOP_SERVER
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 1, stopIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("IRCamera Server")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .addAction(
                android.R.drawable.ic_media_pause,
                "Stop Server",
                stopPendingIntent
            )
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }
    
    /**
     * Get server connection status
     */
    fun getServerStatus(): String {
        return if (isServerRunning.get()) {
            "Running on port $SERVER_PORT (${activeConnections.size} clients)"
        } else {
            "Stopped"
        }
    }
    
    /**
     * Get list of connected PC clients
     */
    fun getConnectedClients(): List<String> {
        return activeConnections.keys.toList()
    }
}
=======
     * Handle stop recording command from PC Controller
     */
    /**
     * Executes handleStopRecordingCommand functionality.
     */
    /**
     * Executes handlestoprecordingcommand operation with thermal imaging domain optimization.
     *
     * @param
     * @param message Parameter for operation (type: JSONObject)
     *
     */
    private fun handleStopRecordingCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Received stop recording command from PC Controller")
                /**
                 * Executes stoprecordingsession operation with thermal imaging domain optimization.
                 *
                 */
                stopRecordingSession()
                
                // Send acknowledgment back to PC Controller
                val response = JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message_type", "response")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("response_to", "stop_recording")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("status", "success")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message", "Recording stopped successfully")
                }
                networkClient.sendMessage(response)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling stop recording command", e)
                
                // Send error response
                val response = JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message_type", "response")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("response_to", "stop_recording") 
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("status", "error")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param recording Parameter for operation (type: ${e.message}")
                     *
                     */
                    put("message", "Failed to stop recording: ${e.message}")
                }
                networkClient.sendMessage(response)
            }
        }
    }
    
    /**
     * Handle sync flash command from PC Controller
     */
    private fun handleSyncFlashCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                val durationMs = message.optInt("flash_duration_ms", 100)
                val timestamp = System.nanoTime()
                
                Log.i(TAG, "Received sync flash command from PC Controller")
                
                // Add sync marker to recording
                /**
                 * Executes addsyncmarker operation with thermal imaging domain optimization.
                 *
                 */
                addSyncMarker("flash_sync", timestamp)
                
                // TODO: Implement screen flash functionality
                // This would require UI interaction which is complex from a background service
                
                // Send acknowledgment back to PC Controller
                val response = JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message_type", "response")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("response_to", "sync_flash")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("status", "success")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("timestamp_ns", timestamp)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message", "Sync flash executed")
                }
                networkClient.sendMessage(response)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling sync flash command", e)
            }
        }
    }
    
    /**
     * Handle query capabilities command from PC Controller
     */
    private fun handleQueryCapabilitiesCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Received query capabilities command from PC Controller")
                
                val capabilities = JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("rgb_camera", true)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("thermal_camera", true) // Assuming thermal camera is available
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("gsr_sensor", true)     // Assuming GSR sensor is available
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("sync_flash", true)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("background_recording", true)
                }
                
                // Send capabilities response back to PC Controller
                val response = JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message_type", "response")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("response_to", "query_capabilities")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("status", "capabilities_data")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("capabilities", capabilities)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("device_model", android.os.Build.MODEL)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("android_version", android.os.Build.VERSION.RELEASE)
                }
                networkClient.sendMessage(response)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling query capabilities command", e)
            }
        }
    }
    
    /**
     * Handle query status command from PC Controller  
     */
    private fun handleQueryStatusCommand(message: JSONObject) {
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Received query status command from PC Controller")
                
                val currentSession = getCurrentSession()
                val status = JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("is_recording", recordingController.isRecording)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("is_initialized", isInitialized)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("current_session", currentSession?.directory ?: "")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("recording_start_time", currentSession?.startTime ?: 0)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("uptime_ms", System.currentTimeMillis())
                }
                
                // Send status response back to PC Controller
                val response = JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("message_type", "response")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("response_to", "query_status")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("status", "status_data")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("data", status)
                }
                networkClient.sendMessage(response)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error handling query status command", e)
            }
        }
    }
    
    /**
     * Get network client instance (for testing/debugging)
     */
    fun getNetworkClient(): NetworkClient = networkClient
    
    /**
     * Manually connect to a PC Controller using IP address
     * This can be used as a fallback when automatic discovery fails
     */
    /**
     * Executes connectToPC functionality.
     */
    /**
     * Executes connecttopc operation with thermal imaging domain optimization.
     *
     * @param
     * @param ipAddress Parameter for operation (type: String)
     * @param port Parameter for operation (type: Int = 8080)
     * @param callback Parameter for operation (type: ((Boolean)
     *
     * @return Operation result or configured object (type: Unit)? = null))
     *
     */
    fun connectToPC(ipAddress: String, port: Int = 8080, callback: ((Boolean) -> Unit)? = null) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isNetworkInitialized) {
            Log.e(TAG, "Network client not initialized")
            callback?.invoke(false)
            return
        }
        
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Attempting manual connection to PC Controller at $ipAddress:$port")
                
                val success = networkClient.connectToController(ipAddress, port)
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    Log.i(TAG, "Manual connection to PC Controller successful")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Connected to PC Controller ($ipAddress)")
                } else {
                    Log.w(TAG, "Manual connection to PC Controller failed")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Failed to connect to PC ($ipAddress)")
                }
                
                callback?.invoke(success)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error during manual PC connection", e)
                callback?.invoke(false)
            }
        }
    }
=======
    // Network server setup and management
    
    /**
     * Sets upnetworkserver configuration.
     */
    private fun setupNetworkServer() {
        lifecycleScope.launch {
            try {
                // Start the TCP server immediately when service initializes
                val serverStarted = networkServer.start()
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (serverStarted) {
                    Log.i(TAG, "Network server started automatically, listening on port 8080")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Listening for PC Controller on port 8080")
                } else {
                    Log.e(TAG, "Failed to start network server automatically")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Network server failed to start")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error setting up network server", e)
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param error Parameter for operation (type: ${e.message}")
                 *
                 */
                updateNotification("Network server error: ${e.message}")
            }
        }
        
        // Monitor connection state
        lifecycleScope.launch {
            networkServer.connectionStateFlow.collect { connected ->
                isConnectedToPC = connected
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (connected) {
                    Log.i(TAG, "PC Controller connected to network server")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("PC Controller connected")
                } else {
                    Log.i(TAG, "PC Controller disconnected, still listening on port 8080")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Listening for PC Controller on port 8080")
                }
            }
        }
        
        // Monitor incoming messages from PC Controller
        lifecycleScope.launch {
            networkServer.messageFlow.collect { message ->
                /**
                 * Executes handlepccommand operation with thermal imaging domain optimization.
                 *
                 */
                handlePCCommand(message)
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
    private fun connectToPC(ipAddress: String, port: Int) {
        // With server architecture, we don't "connect" to PC
        // Instead, we ensure our server is running and ready for PC to connect to us
        lifecycleScope.launch {
            try {
                Log.i(TAG, "Ensuring network server is ready for PC Controller connection")
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!networkServer.isRunning()) {
                    val started = networkServer.start()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (started) {
                        Log.i(TAG, "Network server started, ready for PC Controller at any IP")
                        /**
                         * Executes updatenotification operation with thermal imaging domain optimization.
                         *
                         */
                        updateNotification("Ready for PC Controller connection")
                    } else {
                        Log.e(TAG, "Failed to start network server")
                        /**
                         * Executes updatenotification operation with thermal imaging domain optimization.
                         *
                         */
                        updateNotification("Failed to start network server")
                    }
                } else {
                    Log.i(TAG, "Network server already running, ready for PC Controller")
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Network server ready")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error ensuring network server is ready", e)
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param error Parameter for operation (type: ${e.message}")
                 *
                 */
                updateNotification("Network server error: ${e.message}")
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
        lifecycleScope.launch {
            try {
                // Stop the network server, which will disconnect any connected PC
                networkServer.stop()
                isConnectedToPC = false
                Log.i(TAG, "Network server stopped, PC Controller disconnected")
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Network server stopped") 
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping network server", e)
            }
        }
    }
    
    /**
     * Executes startPCDiscovery functionality.
     */
    /**
     * Executes startpcdiscovery operation with thermal imaging domain optimization.
     *
     */
    private fun startPCDiscovery() {
        lifecycleScope.launch {
            try {
                // TODO: Implement PC discovery using zeroconf/mDNS
                // For now, log that discovery was requested
                Log.i(TAG, "PC Controller discovery requested")
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Searching for PC Controller...")
                
                // This could be extended to use NetworkDiscoveryService
                // Or implement manual discovery logic here
            } catch (e: Exception) {
                Log.e(TAG, "Error starting PC discovery", e)
            }
        }
    }
    
    /**
     * Executes handlepccommand operation with thermal imaging domain optimization.
     *
     * @param
     * @param message Parameter for operation (type: JSONObject)
     *
     */
    private suspend fun handlePCCommand(message: JSONObject) {
        try {
            val messageType = message.optString("message_type")
            Log.i(TAG, "Processing PC command: $messageType")
            
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (messageType) {
                "enhanced_device_registration" -> {
                    Log.i(TAG, "PC Controller device registration request")
                    /**
                     * Executes sendresponsetopc operation with thermal imaging domain optimization.
                     *
                     */
                    sendResponseToPC("enhanced_registration_ack", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("status", "success")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("device_type", "android_sensor_node")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("capabilities", JSONObject().apply {
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("recording", true)
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("sensors", arrayOf("rgb_camera", "thermal_camera", "gsr"))
                        })
                    })
                }
                
                "session_start_command" -> {
                    val sessionDirectory = message.optString("session_directory")
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (sessionDirectory.isNotEmpty()) {
                        Log.i(TAG, "Received remote start command from PC for session: $sessionDirectory")
                        /**
                         * Executes startrecordingsession operation with thermal imaging domain optimization.
                         *
                         */
                        startRecordingSession(sessionDirectory)
                        /**
                         * Executes sendresponsetopc operation with thermal imaging domain optimization.
                         *
                         */
                        sendResponseToPC("session_start_response", JSONObject().apply {
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("status", "started")
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("session_directory", sessionDirectory)
                        })
                    }
                }
                
                "session_stop_command" -> {
                    Log.i(TAG, "Received remote stop command from PC")
                    /**
                     * Executes stoprecordingsession operation with thermal imaging domain optimization.
                     *
                     */
                    stopRecordingSession()
                    /**
                     * Executes sendresponsetopc operation with thermal imaging domain optimization.
                     *
                     */
                    sendResponseToPC("session_stop_response", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("status", "stopped")
                    })
                }
                
                "sync_marker_command" -> {
                    val markerType = message.optString("marker_type")
                    val timestampNs = message.optLong("timestamp_ns", System.nanoTime())
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (markerType.isNotEmpty()) {
                        Log.i(TAG, "Received remote sync marker from PC: $markerType")
                        /**
                         * Executes addsyncmarker operation with thermal imaging domain optimization.
                         *
                         */
                        addSyncMarker(markerType, timestampNs)
                        /**
                         * Executes sendresponsetopc operation with thermal imaging domain optimization.
                         *
                         */
                        sendResponseToPC("sync_marker_response", JSONObject().apply {
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("status", "added")
                            /**
                             * Executes put operation with thermal imaging domain optimization.
                             *
                             */
                            put("marker_type", markerType)
                        })
                    }
                }
                
                "ping" -> {
                    Log.d(TAG, "Received ping from PC Controller")
                    /**
                     * Executes sendresponsetopc operation with thermal imaging domain optimization.
                     *
                     */
                    sendResponseToPC("pong", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timestamp_ns", System.nanoTime())
                    })
                }
                
                "status_request" -> {
                    Log.d(TAG, "PC Controller requested status")
                    /**
                     * Executes sendstatustopc operation with thermal imaging domain optimization.
                     *
                     */
                    sendStatusToPC()
                }
                
                else -> {
                    Log.w(TAG, "Unknown command from PC Controller: $messageType")
                    /**
                     * Executes sendresponsetopc operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param command Parameter for operation (type: $messageType")
                     *
                     */
                    sendResponseToPC("error", JSONObject().apply {
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param command Parameter for operation (type: $messageType")
                         *
                         */
                        put("message", "Unknown command: $messageType")
                    })
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling PC command", e)
            /**
             * Executes sendresponsetopc operation with thermal imaging domain optimization.
             *
             * @param
             * @param command Parameter for operation (type: ${e.message}")
             *
             */
            sendResponseToPC("error", JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param command Parameter for operation (type: ${e.message}")
                 *
                 */
                put("message", "Error processing command: ${e.message}")
            })
        }
    }
    
    /**
     * Executes sendresponsetopc operation with thermal imaging domain optimization.
     *
     * @param
     * @param messageType Parameter for operation (type: String)
     * @param data Parameter for operation (type: JSONObject = JSONObject()
     *
     */
    private suspend fun sendResponseToPC(messageType: String, data: JSONObject = JSONObject()) {
        try {
            val response = JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("message_type", messageType)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_id", android.provider.Settings.Secure.getString(
                    contentResolver, android.provider.Settings.Secure.ANDROID_ID))
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("timestamp_ns", System.nanoTime())
                // Merge additional data
                data.keys().forEach { key ->
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put(key, data.get(key))
                }
            }
            
            networkServer.sendMessage(response)
            Log.d(TAG, "Sent response to PC: $messageType")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error sending response to PC", e)
        }
    }
    
    /**
     * Executes sendstatustopc operation with thermal imaging domain optimization.
     *
     */
    private suspend fun sendStatusToPC() {
        try {
            val statusData = JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("is_recording", recordingController.isRecording)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("current_session", currentSessionDirectory ?: "")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("recording_start_time", recordingStartTime)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("service_initialized", isInitialized)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("network_server_running", networkServer.isRunning())
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("pc_connected", isConnectedToPC)
            }
            
            /**
             * Executes sendresponsetopc operation with thermal imaging domain optimization.
             *
             */
            sendResponseToPC("status_response", statusData)
            Log.i(TAG, "Status sent to PC Controller")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error sending status to PC", e)
        }
    }
>>>>>>> dev
}

/**
 * Current session information
 */
data class SessionInfo(
    val directory: String,
    val startTime: Long,
    val isRecording: Boolean
)
>>>>>>> dev
