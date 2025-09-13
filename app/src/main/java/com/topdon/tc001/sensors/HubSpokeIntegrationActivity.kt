package com.topdon.tc001.sensors

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.topdon.tc001.controller.RecordingController
import com.topdon.tc001.controller.RecordingState
import com.topdon.tc001.network.EnhancedNetworkClient
import com.topdon.tc001.network.NetworkServer
import com.topdon.tc001.service.RecordingService
import com.topdon.tc001.utils.TimeManager
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityHubSpokeIntegrationBinding
import com.topdon.lib.core.ktbase.BaseBindingActivity

// Enhanced BLE Module integration for systematic harmonization
import com.topdon.ble.EasyBLE
import com.topdon.ble.Device
import com.topdon.ble.ConnectionState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Hub-Spoke Integration Activity demonstrating the complete Multi-Modal Physiological Sensing Platform.
 * 
 * This activity provides a comprehensive interface for:
 * - PC Controller discovery and connection
 * - Multi-modal sensor recording coordination
 * - Real-time status monitoring and error handling
 * - Time synchronization visualization
 * - Background service management
 * 
 * Features:
 * - Complete Hub-Spoke system demonstration
 * - Real-time sensor status display
 * - Network connectivity and sync quality monitoring
 * - Coordinated recording session management
 * - Error recovery and status reporting
 * 
 * @author IRCamera Android Sensor Node (Spoke)
 */
/**
 * Specialized thermal imaging component providing HubSpokeIntegrationActivity functionality for the IRCamera system.
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
class HubSpokeIntegrationActivity : BaseBindingActivity<ActivityHubSpokeIntegrationBinding>() {

    companion object {
        private const val TAG = "HubSpokeIntegration"
        private const val DEFAULT_PC_CONTROLLER_PORT = 8080
    }

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId(): Int = R.layout.activity_hub_spoke_integration

    // Core components
    private lateinit var recordingController: RecordingController
    private lateinit var networkServer: NetworkServer
    private lateinit var timeManager: TimeManager
    
    // Enhanced BLE Module for systematic harmonization
    private lateinit var enhancedBLE: EasyBLE
    private lateinit var unifiedBleManager: com.topdon.ble.UnifiedBleManager
    private var connectedBLEDevices = mutableListOf<Device>()
    
    // Service connection
    private var recordingService: RecordingService? = null
    private var isServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        /**
         * Executes onserviceconnected operation with thermal imaging domain optimization.
         *
         * @param
         * @param name Parameter for operation (type: ComponentName?)
         * @param service Parameter for operation (type: IBinder?)
         *
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as RecordingService.RecordingServiceBinder
            recordingService = binder.getService()
            recordingController = binder.getService().getRecordingController()
            isServiceBound = true
            
            Log.i(TAG, "Connected to RecordingService")
            /**
             * Configures the uprecordingmonitoring with validation and thermal imaging optimization.
             *
             */
            setupRecordingMonitoring()
            /**
             * Configures the upnetworkmonitoring with validation and thermal imaging optimization.
             *
             */
            setupNetworkMonitoring()
            /**
             * Executes updateui operation with thermal imaging domain optimization.
             *
             */
            updateUI()
        }

        /**
         * Executes onservicedisconnected operation with thermal imaging domain optimization.
         *
         * @param
         * @param name Parameter for operation (type: ComponentName?)
         *
         */
        override fun onServiceDisconnected(name: ComponentName?) {
            recordingService = null
            isServiceBound = false
            Log.i(TAG, "Disconnected from RecordingService")
        }
    }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        /**
         * Initializes the ializeviews component for thermal imaging operations.
         *
         */
        initializeViews()
        /**
         * Initializes the ializecomponents component for thermal imaging operations.
         *
         */
        initializeComponents()
        /**
         * Configures the upclicklisteners with validation and thermal imaging optimization.
         *
         */
        setupClickListeners()
        /**
         * Executes bindtorecordingservice operation with thermal imaging domain optimization.
         *
         */
        bindToRecordingService()
        
        /**
         * Executes updateui operation with thermal imaging domain optimization.
         *
         */
        updateUI()
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        
        lifecycleScope.launch {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (::networkServer.isInitialized) {
                    networkServer.cleanup()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during cleanup", e)
            }
        }
        
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isServiceBound) {
            /**
             * Executes unbindservice operation with thermal imaging domain optimization.
             *
             */
            unbindService(serviceConnection)
        }
    }

    /**
     * Initializes ializeviews component.
     */
    private fun initializeViews() {
        // Set default session directory using binding
        val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
        val defaultSessionDir = "${getExternalFilesDir(null)}/hub_spoke_sessions/session_$timestamp"
        binding.sessionDirectoryEditText.setText(defaultSessionDir)
    }

    /**
     * Initializes ializecomponents component.
     */
    private fun initializeComponents() {
        timeManager = TimeManager.getInstance(this)
        
        // Initialize Enhanced BLE Module with Nordic backend for systematic harmonization
        enhancedBLE = EasyBLE.getBuilder()
            .setUseNordicBleBackend(true) // Enable Nordic BLE for enhanced reliability
            .build()
        
        Log.i(TAG, "Enhanced BLE Module initialized with Nordic BLE backend")
        
        // Initialize Enhanced BLE Manager for advanced multi-device coordination
        /**
         * Initializes the ializeadvancedblecoordination component for thermal imaging operations.
         *
         */
        initializeAdvancedBleCoordination()
        
        // Initialize network server (will be managed by service later)
        recordingController = RecordingController(this, this)
        networkServer = NetworkServer(this, 8080)
    }
    
    /**
     * Initialize advanced BLE coordination for systematic multi-device management
     */
    private fun initializeAdvancedBleCoordination() {
        lifecycleScope.launch {
            try {
                // Initialize Unified BLE Manager with multi-device coordination
                unifiedBleManager = com.topdon.ble.UnifiedBleManager.getInstance(this@HubSpokeIntegrationActivity)
                unifiedBleManager.initialize(this@HubSpokeIntegrationActivity, true)
                unifiedBleManager.enableMultiDeviceMode(true)
                
                Log.i(TAG, "Advanced BLE coordination initialized for hub-spoke system")
                
                // Setup BLE device monitoring for real-time status updates
                /**
                 * Configures the upbledevicemonitoring with validation and thermal imaging optimization.
                 *
                 */
                setupBleDeviceMonitoring()
                
                // Auto-discover and setup GSR sensors for physiological sensing
                /**
                 * Executes discovergsrsensorsforhubspoke operation with thermal imaging domain optimization.
                 *
                 */
                discoverGsrSensorsForHubSpoke()
                
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing advanced BLE coordination", e)
            }
        }
    }
    
    /**
     * Setup BLE device monitoring with system-wide status tracking
     */
    private fun setupBleDeviceMonitoring() {
        lifecycleScope.launch {
            try {
                // Monitor system BLE status and update UI
                launch {
                    /**
                     * Executes while operation with thermal imaging domain optimization.
                     *
                     */
                    while (isServiceBound || !isDestroyed) {
                        try {
                            val systemStatus = unifiedBleManager.getSystemStatus()
                            /**
                             * Executes updateblestatusui operation with thermal imaging domain optimization.
                             *
                             */
                            updateBleStatusUI(systemStatus)
                            
                            // Log system status for debugging
                            Log.d(TAG, "BLE System Status: $systemStatus")
                            
                            kotlinx.coroutines.delay(2000) // Update every 2 seconds
                        } catch (e: Exception) {
                            Log.e(TAG, "Error monitoring BLE status", e)
                            break
                        }
                    }
                }
                
                Log.i(TAG, "BLE device monitoring started")
                
            } catch (e: Exception) {
                Log.e(TAG, "Error setting up BLE device monitoring", e)
            }
        }
    }
    
    /**
     * Discover and setup GSR sensors for hub-spoke physiological sensing
     */
    private fun discoverGsrSensorsForHubSpoke() {
        lifecycleScope.launch {
            try {
                // Start BLE device discovery to find available GSR sensors
                enhancedBLE.addScanListener(object : com.topdon.ble.callback.ScanListener {
                    /**
                     * Executes onscanstart operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onScanStart() {
                        Log.d(TAG, "Hub-spoke GSR sensor discovery started")
                        runOnUiThread {
                            binding.statusTextView.text = "Scanning for GSR sensors..."
                        }
                    }
                    
                    /**
                     * Executes onscanstop operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onScanStop() {
                        Log.d(TAG, "Hub-spoke GSR sensor discovery stopped")
                    }
                    
                    /**
                     * Executes onscanresult operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param device Parameter for operation (type: Device)
                     * @param isConnectedBySys Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onScanResult(device: Device, isConnectedBySys: Boolean) {
                        // Check if device is a GSR sensor (Shimmer3 GSR+)
                        val deviceName = device.name?.uppercase() ?: ""
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (deviceName.contains("SHIMMER") || deviceName.contains("GSR")) {
                            Log.i(TAG, "GSR sensor detected for hub-spoke: ${device.name} (${device.address})")
                            
                            // Mark as GSR sensor for enhanced handling
                            unifiedBleManager.markAsGsrSensor(device.address)
                            
                            runOnUiThread {
                                binding.statusTextView.text = "GSR sensor found: ${device.name}"
                                /**
                                 * Executes updatediscovereddevicesui operation with thermal imaging domain optimization.
                                 *
                                 */
                                updateDiscoveredDevicesUI(device, device.getRssi())
                            }
                        }
                    }
                    
                    /**
                     * Executes onscanerror operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param errorCode Parameter for operation (type: Int)
                     * @param errorMsg Parameter for operation (type: String?)
                     *
                     */
                    override fun onScanError(errorCode: Int, errorMsg: String?) {
                        Log.e(TAG, "Hub-spoke GSR sensor discovery failed: $errorCode, message: $errorMsg")
                        runOnUiThread {
                            binding.statusTextView.text = "GSR sensor discovery failed"
                        }
                    }
                })
                
                // Start scanning for a limited time
                enhancedBLE.startScan()
                
                // Stop scanning after 30 seconds
                kotlinx.coroutines.delay(30000)
                enhancedBLE.stopScan()
                
            } catch (e: Exception) {
                Log.e(TAG, "Error discovering GSR sensors", e)
            }
        }
    }
    
    /**
     * Update BLE status in the UI with enhanced system information
     */
    private fun updateBleStatusUI(systemStatus: com.topdon.ble.UnifiedBleManager.SystemBleStatus?) {
        runOnUiThread {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (systemStatus != null) {
                    val statusText = "BLE: ${systemStatus.activeConnections} active, " +
                            "${systemStatus.totalDevicesConnected} total devices, " +
                            "Multi-device: ${if (systemStatus.multiDeviceMode) "ON" else "OFF"}"
                    
                    // Update BLE status display (assuming there's a BLE status TextView)
                    // Binding.bleStatusTextView.text = statusText
                    
                    // Update connection indicator based on active connections
                    val hasActiveDevices = systemStatus.activeConnections > 0
                    val networkConnected = recordingService?.isConnectedToPC() ?: false
                    binding.connectButton.isEnabled = !networkConnected
                    
                    Log.d(TAG, "BLE Status UI updated: $statusText")
                } else {
                    Log.w(TAG, "BLE system status is null")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating BLE status UI", e)
            }
        }
    }
    
    /**
     * Update discovered devices UI with real-time device information
     */
    private fun updateDiscoveredDevicesUI(device: Device, rssi: Int) {
        try {
            // Add device to the connected devices list
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!connectedBLEDevices.any { it.address == device.address }) {
                connectedBLEDevices.add(device)
                Log.i(TAG, "Added discovered BLE device: ${device.name} (${device.address})")
            }
            
            // Update device count display
            val deviceCountText = "Discovered BLE devices: ${connectedBLEDevices.size}"
            // Binding.deviceCountTextView.text = deviceCountText
            
            Log.d(TAG, deviceCountText)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error updating discovered devices UI", e)
        }
    }

    /**
     * Sets upclicklisteners configuration.
     */
    private fun setupClickListeners() {
        binding.connectButton.setOnClickListener {
            connectToPCController()
        }
        
        binding.disconnectButton.setOnClickListener {
            /**
             * Executes disconnectfrompccontroller operation with thermal imaging domain optimization.
             *
             */
            disconnectFromPCController()
        }
        
        binding.startRecordingButton.setOnClickListener {
            /**
             * Executes startcoordinatedrecording operation with thermal imaging domain optimization.
             *
             */
            startCoordinatedRecording()
        }
        
        binding.stopRecordingButton.setOnClickListener {
            /**
             * Executes stopcoordinatedrecording operation with thermal imaging domain optimization.
             *
             */
            stopCoordinatedRecording()
        }
        
        binding.addSyncMarkerButton.setOnClickListener {
            /**
             * Executes addsyncmarker operation with thermal imaging domain optimization.
             *
             */
            addSyncMarker()
        }
    }

    /**
     * Executes bindToRecordingService functionality.
     */
    /**
     * Executes bindtorecordingservice operation with thermal imaging domain optimization.
     *
     */
    private fun bindToRecordingService() {
        val intent = Intent(this, RecordingService::class.java)
        /**
         * Executes bindservice operation with thermal imaging domain optimization.
         *
         */
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    /**
     * Executes connectToPCController functionality.
     */
    /**
     * Executes connecttopccontroller operation with thermal imaging domain optimization.
     *
     */
    private fun connectToPCController() {
        // In the new server architecture, we don't connect TO the PC
        // Instead, we ensure our server is ready and display connection info
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.statusTextView.text = "Preparing to accept PC Controller connections..."
                
                // Use RecordingService to ensure the network server is ready
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isServiceBound) {
                    // The service automatically starts the network server
                    // Just ensure it's running and display connection info
                    RecordingService.connectToPC(this@HubSpokeIntegrationActivity, "0.0.0.0", 8080)
                    
                    // Give a moment for server to be ready
                    kotlinx.coroutines.delay(1000)
                    
                    // Display connection information
                    val deviceIP = getLocalIPAddress()
                    binding.statusTextView.text = "Network server ready!\nPC can connect to: $deviceIP:8080"
                    android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, 
                        "Ready for PC connections on $deviceIP:8080", android.widget.Toast.LENGTH_LONG).show()
                    Log.i(TAG, "Network server ready for PC Controller connections at $deviceIP:8080")
                    
                } else {
                    binding.statusTextView.text = "Recording service not available"
                    android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Recording service not ready", android.widget.Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Recording service not bound")
                }
                
            } catch (e: Exception) {
                binding.statusTextView.text = "Server setup error: ${e.message}"
                android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Server error: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error setting up network server", e)
            } finally {
                binding.progressBar.visibility = View.GONE
                /**
                 * Executes updateui operation with thermal imaging domain optimization.
                 *
                 */
                updateUI()
            }
        }
    }

    /**
     * Executes disconnectFromPCController functionality.
     */
    /**
     * Executes disconnectfrompccontroller operation with thermal imaging domain optimization.
     *
     */
    private fun disconnectFromPCController() {
        lifecycleScope.launch {
            try {
                binding.statusTextView.text = "Disconnecting from PC Controller..."
                
                // Use RecordingService to handle the disconnection
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isServiceBound) {
                    RecordingService.disconnectFromPC(this@HubSpokeIntegrationActivity)
                    
                    // Give some time for disconnection
                    kotlinx.coroutines.delay(1000)
                    
                    binding.statusTextView.text = "Disconnected from PC Controller"
                    android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Disconnected", android.widget.Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Disconnected from PC Controller via RecordingService")
                } else {
                    binding.statusTextView.text = "Recording service not available"
                    Log.e(TAG, "Recording service not bound")
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Disconnect error", e)
                binding.statusTextView.text = "Disconnect error: ${e.message}"
            } finally {
                /**
                 * Executes updateui operation with thermal imaging domain optimization.
                 *
                 */
                updateUI()
            }
        }
    }
    }

    /**
     * Executes startCoordinatedRecording functionality.
     */
    /**
     * Executes startcoordinatedrecording operation with thermal imaging domain optimization.
     *
     */
    private fun startCoordinatedRecording() {
        val sessionDirectory = binding.sessionDirectoryEditText.text.toString().trim()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (sessionDirectory.isEmpty()) {
            android.widget.Toast.makeText(this, "Please enter session directory", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.statusTextView.text = "Starting coordinated recording session..."
                
                // Create session directory
                val sessionDir = File(sessionDirectory)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!sessionDir.exists()) {
                    sessionDir.mkdirs()
                }
                
                val success = if (recordingService?.isConnectedToPC() == true) {
                    // Use recording service for coordinated session
                    RecordingService.startRecording(this@HubSpokeIntegrationActivity, sessionDirectory)
                    true // The service handles the coordination
                } else {
                    // Start local recording only
                    recordingController.startRecording(sessionDirectory)
                }
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    binding.statusTextView.text = "Coordinated recording session started"
                    android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Recording started", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    binding.statusTextView.text = "Failed to start recording session"
                    android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Recording failed to start", android.widget.Toast.LENGTH_SHORT).show()
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Recording start error", e)
                binding.statusTextView.text = "Recording start error: ${e.message}"
                android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Recording error", android.widget.Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                /**
                 * Executes updateui operation with thermal imaging domain optimization.
                 *
                 */
                updateUI()
            }
        }
    }

    /**
     * Executes stopCoordinatedRecording functionality.
     */
    /**
     * Executes stopcoordinatedrecording operation with thermal imaging domain optimization.
     *
     */
    private fun stopCoordinatedRecording() {
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.statusTextView.text = "Stopping coordinated recording session..."
                
                val success = if (recordingService?.isConnectedToPC() == true) {
                    // Use recording service for coordinated session
                    RecordingService.stopRecording(this@HubSpokeIntegrationActivity)
                    true // The service handles the coordination
                } else {
                    // Stop local recording only
                    recordingController.stopRecording()
                }
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    binding.statusTextView.text = "Coordinated recording session stopped"
                    android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Recording stopped", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    binding.statusTextView.text = "Failed to stop recording session"
                    android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Recording stop failed", android.widget.Toast.LENGTH_SHORT).show()
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Recording stop error", e)
                binding.statusTextView.text = "Recording stop error: ${e.message}"
            } finally {
                binding.progressBar.visibility = View.GONE
                /**
                 * Executes updateui operation with thermal imaging domain optimization.
                 *
                 */
                updateUI()
            }
        }
    }

    /**
     * Executes addSyncMarker functionality.
     */
    /**
     * Executes addsyncmarker operation with thermal imaging domain optimization.
     *
     */
    private fun addSyncMarker() {
        lifecycleScope.launch {
            try {
                val markerType = "manual_sync_${System.currentTimeMillis()}"
                val metadata = mapOf(
                    "source" to "HubSpokeIntegrationActivity",
                    "user_initiated" to "true"
                )
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (recordingService?.isConnectedToPC() == true) {
                    // Add sync marker through recording service (will distribute to PC)
                    RecordingService.addSyncMarker(this@HubSpokeIntegrationActivity, markerType, System.nanoTime())
                } else {
                    // Add local sync marker only
                    val timestampNs = timeManager.getCurrentTimestampNs()
                    recordingController.addSyncMarker(markerType, timestampNs, metadata)
                }
                
                android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Sync marker added", android.widget.Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Sync marker added: $markerType")
                
            } catch (e: Exception) {
                Log.e(TAG, "Sync marker error", e)
                android.widget.Toast.makeText(this@HubSpokeIntegrationActivity, "Sync marker failed", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Sets uprecordingmonitoring configuration.
     */
    private fun setupRecordingMonitoring() {
        if (!::recordingController.isInitialized) return
        
        // Monitor recording state
        recordingController.recordingStateFlow
            .onEach { state ->
                runOnUiThread {
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (state) {
                        RecordingState.STARTING -> binding.statusTextView.text = "Starting sensors..."
                        RecordingState.RECORDING -> binding.statusTextView.text = "Recording in progress"
                        RecordingState.STOPPING -> binding.statusTextView.text = "Stopping sensors..."
                        RecordingState.STOPPED -> binding.statusTextView.text = "Recording stopped"
                        RecordingState.ERROR -> binding.statusTextView.text = "Recording error"
                    }
                    /**
                     * Executes updateui operation with thermal imaging domain optimization.
                     *
                     */
                    updateUI()
                }
            }
            .launchIn(lifecycleScope)
        
        // Monitor sensor status
        recordingController.sensorStatusFlow
            .onEach { statusList ->
                runOnUiThread {
                    val statusText = buildString {
                        statusList.forEach { status ->
                            /**
                             * Executes append operation with thermal imaging domain optimization.
                             *
                             */
                            append("${status.sensorType}: ")
                            /**
                             * Executes append operation with thermal imaging domain optimization.
                             *
                             */
                            append(if (status.isRecording) "Recording" else "Stopped")
                            /**
                             * Executes append operation with thermal imaging domain optimization.
                             *
                             */
                            append(" (${status.samplesRecorded} samples, ")
                            /**
                             * Executes append operation with thermal imaging domain optimization.
                             *
                             */
                            append("${String.format("%.1f", status.storageUsedMB)}MB)\n")
                        }
                    }
                    binding.sensorStatusTextView.text = statusText.trim()
                }
            }
            .launchIn(lifecycleScope)
    }

    /**
     * Sets upnetworkmonitoring configuration.
     */
    private fun setupNetworkMonitoring() {
        // Monitor connection state through recording service
        lifecycleScope.launch {
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (!isDestroyed) {
                try {
                    val isConnected = recordingService?.isConnectedToPC() ?: false
                    runOnUiThread {
                        binding.connectionStatusTextView.text = "Connection: ${if (isConnected) "Connected" else "Waiting for PC"}"
                        /**
                         * Executes updateui operation with thermal imaging domain optimization.
                         *
                         */
                        updateUI()
                    }
                    
                    // Monitor time sync quality if connected
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isConnected) {
                        val syncQuality = timeManager.getSyncQuality()
                        runOnUiThread {
                            binding.syncQualityTextView.text = buildString {
                                /**
                                 * Executes append operation with thermal imaging domain optimization.
                                 *
                                 * @param
                                 * @param Sync Parameter for operation (type: ${syncQuality.level}")
                                 *
                                 */
                                append("Sync: ${syncQuality.level}")
                                syncQuality.qualityMs?.let { append(" (${it}ms)") }
                                syncQuality.timeSinceSyncMs?.let { append(" - ${it / 1000}s ago") }
                            }
                        }
                    } else {
                        runOnUiThread {
                            binding.syncQualityTextView.text = "Sync: Not Available"
                        }
                    }
                    
                    kotlinx.coroutines.delay(2000) // Update every 2 seconds
                } catch (e: Exception) {
                    Log.e(TAG, "Error in network monitoring", e)
                    break
                }
            }
        }
    }

    /**
     * Executes updateUI functionality.
     */
    /**
     * Executes updateui operation with thermal imaging domain optimization.
     *
     */
    private fun updateUI() {
        val isConnected = recordingService?.isConnectedToPC() ?: false
        val isRecording = ::recordingController.isInitialized && recordingController.isRecording
        
        binding.connectButton.isEnabled = !isConnected
        binding.disconnectButton.isEnabled = isConnected
        binding.startRecordingButton.isEnabled = !isRecording
        binding.stopRecordingButton.isEnabled = isRecording
        binding.addSyncMarkerButton.isEnabled = isRecording
        
        binding.pcAddressEditText.isEnabled = !isConnected
        binding.sessionDirectoryEditText.isEnabled = !isRecording
        
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isConnected) {
            binding.connectionStatusTextView.text = "Connection: Waiting for PC Controller"
            binding.syncQualityTextView.text = "Sync: Not Available"
        }
        
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isRecording) {
            binding.sensorStatusTextView.text = "Sensors: Idle"
        }
        
        // Update BLE device status
        /**
         * Executes updatebledevicestatus operation with thermal imaging domain optimization.
         *
         */
        updateBLEDeviceStatus()
    }
    
    /**
     * Update BLE device connection status in the UI
     * Part of systematic harmonization for enhanced BLE monitoring
     */
    /**
     * Executes updateBLEDeviceStatus functionality.
     */
    /**
     * Executes updatebledevicestatus operation with thermal imaging domain optimization.
     *
     */
    private fun updateBLEDeviceStatus() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (::enhancedBLE.isInitialized) {
            val bleDeviceCount = connectedBLEDevices.size
            val statusText = if (bleDeviceCount > 0) {
                "BLE Devices: $bleDeviceCount connected (Enhanced Nordic Backend)"
            } else {
                "BLE Devices: Scanning for devices..."
            }
            
            // Update the sensor status to include BLE device information
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             * @param
             * @param Sensors Parameter for operation (type: Idle")
             *
             */
            if (binding.sensorStatusTextView.text.toString().startsWith("Sensors: Idle")) {
                binding.sensorStatusTextView.text = "Sensors: Idle | $statusText"
            }
        }
    }
    
    /**
     * Get the local IP address of this device
     */
    private fun getLocalIPAddress(): String {
        try {
            val interfaces = java.net.NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!address.isLoopbackAddress && address is java.net.Inet4Address) {
                        return address.hostAddress ?: "Unknown"
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting local IP address", e)
        }
        return "Unknown IP"
    }
}