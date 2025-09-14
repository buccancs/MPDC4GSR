package com.topdon.tc001

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csl.irCamera.R
import com.topdon.tc001.controller.RecordingController
import com.topdon.tc001.sensors.unified.*
import com.topdon.tc001.sensors.unified.model.*
import com.topdon.tc001.sensors.unified.adapters.DeviceAdapter
import com.topdon.tc001.sensors.unified.adapters.PCControllerAdapter
import kotlinx.coroutines.launch

/**
 * **Unified Sensor Activity**
 * 
 * Main activity that demonstrates the complete IRCamera extension architecture following the integration plan:
 * 
 * ## Implementation Features:
 * - **GSRRecorder Component**: Complete Shimmer3 GSR+ integration with BLE management
 * - **NetworkController**: Wi-Fi control and PC controller communication
 * - **Unified Session Management**: Comprehensive session logging and coordination
 * - **Android Architecture**: ViewModel-compatible with LiveData integration
 * - **Multi-Modal Coordination**: GSR, thermal, RGB sensors with synchronized recording
 * 
 * ## Integration Points:
 * - Works seamlessly with existing IRCamera thermal functionality
 * - Extends MainActivity with long-press access (developer feature)
 * - Integrates with existing RecordingController and sensor infrastructure
 * - Provides unified logging and session management across all sensors
 * 
 * ## User Interface:
 * - Device discovery and connection management
 * - Real-time session monitoring and quality indicators
 * - PC controller discovery and communication
 * - Session controls (start/stop/sync markers)
 * - Data quality visualization and export options
 * 
 * @author IRCamera Unified Sensor Integration
 */
class UnifiedSensorActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "UnifiedSensorActivity"
        
        // UI update intervals
        private const val STATUS_UPDATE_INTERVAL_MS = 1000L
        private const val QUALITY_UPDATE_INTERVAL_MS = 5000L
    }
    
    // Core components
    private lateinit var gsrRecorder: UnifiedGSRRecorder
    private lateinit var networkController: UnifiedNetworkController
    private lateinit var sessionManager: UnifiedSessionManager
    private lateinit var recordingController: RecordingController
    
    // UI Components
    private lateinit var statusText: TextView
    private lateinit var qualityIndicator: ProgressBar
    private lateinit var gsrStatusText: TextView
    private lateinit var networkStatusText: TextView
    private lateinit var sessionStatusText: TextView
    
    // Device Management UI
    private lateinit var deviceRecyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var discoverButton: Button
    private lateinit var connectButton: Button
    
    // PC Controller UI
    private lateinit var pcRecyclerView: RecyclerView
    private lateinit var pcAdapter: PCControllerAdapter
    private lateinit var discoverPCButton: Button
    private lateinit var connectPCButton: Button
    
    // Session Control UI
    private lateinit var sessionNameEdit: EditText
    private lateinit var participantIdEdit: EditText
    private lateinit var startSessionButton: Button
    private lateinit var stopSessionButton: Button
    private lateinit var addMarkerButton: Button
    
    // Permission handling
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            initializeComponents()
        } else {
            showPermissionError()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unified_sensor)
        
        Log.i(TAG, "Starting Unified Sensor Activity - IRCamera Extension")
        
        initializeUI()
        checkPermissionsAndInitialize()
    }
    
    /**
     * Initialize UI components
     */
    private fun initializeUI() {
        Log.d(TAG, "Initializing UI components")
        
        // Status indicators
        statusText = findViewById(R.id.statusText)
        qualityIndicator = findViewById(R.id.qualityIndicator)
        gsrStatusText = findViewById(R.id.gsrStatusText)
        networkStatusText = findViewById(R.id.networkStatusText)
        sessionStatusText = findViewById(R.id.sessionStatusText)
        
        // Device management
        deviceRecyclerView = findViewById(R.id.deviceRecyclerView)
        deviceAdapter = DeviceAdapter { device -> connectToDevice(device) }
        deviceRecyclerView.adapter = deviceAdapter
        deviceRecyclerView.layoutManager = LinearLayoutManager(this)
        
        discoverButton = findViewById(R.id.discoverButton)
        connectButton = findViewById(R.id.connectButton)
        
        discoverButton.setOnClickListener { startDeviceDiscovery() }
        connectButton.setOnClickListener { connectToSelectedDevice() }
        
        // PC Controller management
        pcRecyclerView = findViewById(R.id.pcRecyclerView)
        pcAdapter = PCControllerAdapter { controller -> connectToPC(controller) }
        pcRecyclerView.adapter = pcAdapter
        pcRecyclerView.layoutManager = LinearLayoutManager(this)
        
        discoverPCButton = findViewById(R.id.discoverPCButton)
        connectPCButton = findViewById(R.id.connectPCButton)
        
        discoverPCButton.setOnClickListener { startPCDiscovery() }
        connectPCButton.setOnClickListener { connectToSelectedPC() }
        
        // Session control
        sessionNameEdit = findViewById(R.id.sessionNameEdit)
        participantIdEdit = findViewById(R.id.participantIdEdit)
        startSessionButton = findViewById(R.id.startSessionButton)
        stopSessionButton = findViewById(R.id.stopSessionButton)
        addMarkerButton = findViewById(R.id.addMarkerButton)
        
        startSessionButton.setOnClickListener { startSession() }
        stopSessionButton.setOnClickListener { stopSession() }
        addMarkerButton.setOnClickListener { addSyncMarker() }
        
        // Initial UI state
        updateUIState(false, false, false)
    }
    
    /**
     * Check permissions and initialize components
     */
    private fun checkPermissionsAndInitialize() {
        val requiredPermissions = UnifiedGSRRecorder.getRequiredPermissions()
        val missingPermissions = requiredPermissions.filter { permission ->
            ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        }
        
        if (missingPermissions.isNotEmpty()) {
            Log.i(TAG, "Requesting missing permissions: ${missingPermissions.joinToString()}")
            requestPermissionLauncher.launch(missingPermissions.toTypedArray())
        } else {
            initializeComponents()
        }
    }
    
    /**
     * Initialize core components after permissions are granted
     */
    private fun initializeComponents() {
        Log.i(TAG, "Initializing core components")
        
        try {
            // Initialize RecordingController
            recordingController = RecordingController(this, this)
            
            // Initialize GSR Recorder
            gsrRecorder = UnifiedGSRRecorder(this, this)
            
            // Initialize Network Controller  
            networkController = UnifiedNetworkController(this, this)
            
            // Initialize Session Manager
            sessionManager = UnifiedSessionManager(
                context = this,
                lifecycleOwner = this,
                recordingController = recordingController,
                networkController = networkController,
                gsrRecorder = gsrRecorder
            )
            
            // Initialize components
            lifecycleScope.launch {
                val gsrInitialized = gsrRecorder.initialize()
                val networkInitialized = networkController.initialize()
                
                if (gsrInitialized && networkInitialized) {
                    statusText.text = "Components initialized successfully"
                    updateUIState(true, false, false)
                    observeComponentStates()
                } else {
                    statusText.text = "Component initialization failed"
                    showInitializationError(gsrInitialized, networkInitialized)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize components", e)
            statusText.text = "Initialization error: ${e.message}"
        }
    }
    
    /**
     * Start observing component state changes
     */
    private fun observeComponentStates() {
        Log.d(TAG, "Setting up component state observers")
        
        // Observe GSR device status
        lifecycleScope.launch {
            gsrRecorder.deviceStatus.collect { status ->
                gsrStatusText.text = "GSR: $status"
            }
        }
        
        // Observe network status
        lifecycleScope.launch {
            networkController.networkStatus.collect { status ->
                networkStatusText.text = "Network: ${status.displayName}"
            }
        }
        
        // Observe session status
        lifecycleScope.launch {
            sessionManager.sessionStatus.collect { status ->
                sessionStatusText.text = "Session: ${status.displayName}"
                updateSessionUI(status)
            }
        }
        
        // Observe session quality
        lifecycleScope.launch {
            sessionManager.sessionQuality.collect { quality ->
                qualityIndicator.progress = (quality.overallQuality * 100).toInt()
                updateQualityIndicator(quality)
            }
        }
        
        // Observe discovered devices
        lifecycleScope.launch {
            networkController.discoveredControllersFlow.collect { controllers ->
                pcAdapter.updateControllers(controllers)
            }
        }
    }
    
    /**
     * Start Shimmer device discovery
     */
    private fun startDeviceDiscovery() {
        Log.i(TAG, "Starting Shimmer device discovery")
        
        discoverButton.isEnabled = false
        discoverButton.text = "Discovering..."
        
        lifecycleScope.launch {
            val success = gsrRecorder.startDeviceDiscovery()
            
            if (success) {
                val devices = gsrRecorder.getDiscoveredDevices()
                deviceAdapter.updateDevices(devices)
                
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Found ${devices.size} Shimmer devices", 
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Device discovery failed", 
                    Toast.LENGTH_SHORT).show()
            }
            
            discoverButton.isEnabled = true
            discoverButton.text = "Discover Devices"
        }
    }
    
    /**
     * Start PC controller discovery
     */
    private fun startPCDiscovery() {
        Log.i(TAG, "Starting PC controller discovery")
        
        discoverPCButton.isEnabled = false
        discoverPCButton.text = "Discovering..."
        
        lifecycleScope.launch {
            val success = networkController.startDiscovery()
            
            if (success) {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "PC discovery started", 
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "PC discovery failed", 
                    Toast.LENGTH_SHORT).show()
            }
            
            discoverPCButton.isEnabled = true
            discoverPCButton.text = "Discover PCs"
        }
    }
    
    /**
     * Connect to selected Shimmer device
     */
    private fun connectToDevice(device: DeviceInfo) {
        Log.i(TAG, "Connecting to Shimmer device: ${device.name}")
        
        lifecycleScope.launch {
            val connected = gsrRecorder.connectToDevice(device)
            
            if (connected) {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Connected to ${device.name}", 
                    Toast.LENGTH_SHORT).show()
                updateUIState(true, true, false)
            } else {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Failed to connect to ${device.name}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    
    /**
     * Connect to selected PC controller
     */
    private fun connectToPC(controller: PCControllerInfo) {
        Log.i(TAG, "Connecting to PC controller: ${controller.name}")
        
        lifecycleScope.launch {
            val connected = networkController.connectToController(controller)
            
            if (connected) {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Connected to ${controller.displayName}", 
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Failed to connect to ${controller.displayName}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    
    /**
     * Start recording session
     */
    private fun startSession() {
        Log.i(TAG, "Starting recording session")
        
        val sessionName = sessionNameEdit.text.toString().trim()
        val participantId = participantIdEdit.text.toString().trim()
        
        if (sessionName.isEmpty() || participantId.isEmpty()) {
            Toast.makeText(this, "Please enter session name and participant ID", Toast.LENGTH_SHORT).show()
            return
        }
        
        val sessionConfig = SessionConfig(
            sessionName = sessionName,
            studyName = "IRCamera Unified Recording",
            participantId = participantId,
            enabledSensors = listOf("gsr", "thermal", "rgb"),
            sessionType = SessionType.HYBRID,
            metadata = mapOf(
                "device_type" to "Android",
                "app_version" to "2.0.0",
                "unified_extension" to true
            )
        )
        
        lifecycleScope.launch {
            val session = sessionManager.createSession(sessionConfig)
            if (session != null) {
                val started = sessionManager.startSession()
                if (started) {
                    Toast.makeText(this@UnifiedSensorActivity, 
                        "Session started: ${session.sessionId}", 
                        Toast.LENGTH_SHORT).show()
                    updateUIState(true, true, true)
                } else {
                    Toast.makeText(this@UnifiedSensorActivity, 
                        "Failed to start session", 
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    /**
     * Stop recording session
     */
    private fun stopSession() {
        Log.i(TAG, "Stopping recording session")
        
        lifecycleScope.launch {
            val stopped = sessionManager.stopSession()
            if (stopped) {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Session stopped successfully", 
                    Toast.LENGTH_SHORT).show()
                updateUIState(true, true, false)
            } else {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Failed to stop session", 
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    /**
     * Add synchronization marker
     */
    private fun addSyncMarker() {
        Log.i(TAG, "Adding sync marker")
        
        lifecycleScope.launch {
            val added = sessionManager.addSyncMarker(
                markerType = "manual_marker",
                markerData = mapOf(
                    "timestamp" to System.currentTimeMillis(),
                    "source" to "user_button"
                )
            )
            
            if (added) {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Sync marker added", 
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@UnifiedSensorActivity, 
                    "Failed to add sync marker", 
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    /**
     * Connect to selected device (if any)
     */
    private fun connectToSelectedDevice() {
        val selectedDevice = deviceAdapter.getSelectedDevice()
        if (selectedDevice != null) {
            connectToDevice(selectedDevice)
        } else {
            Toast.makeText(this, "Please select a device first", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Connect to selected PC controller (if any)
     */
    private fun connectToSelectedPC() {
        val selectedPC = pcAdapter.getSelectedController()
        if (selectedPC != null) {
            connectToPC(selectedPC)
        } else {
            Toast.makeText(this, "Please select a PC controller first", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Update UI state based on system status
     */
    private fun updateUIState(initialized: Boolean, deviceConnected: Boolean, sessionActive: Boolean) {
        // Device controls
        discoverButton.isEnabled = initialized && !sessionActive
        connectButton.isEnabled = initialized && !sessionActive
        
        // PC controls  
        discoverPCButton.isEnabled = initialized && !sessionActive
        connectPCButton.isEnabled = initialized && !sessionActive
        
        // Session controls
        startSessionButton.isEnabled = initialized && deviceConnected && !sessionActive
        stopSessionButton.isEnabled = sessionActive
        addMarkerButton.isEnabled = sessionActive
        
        // Session input
        sessionNameEdit.isEnabled = !sessionActive
        participantIdEdit.isEnabled = !sessionActive
    }
    
    /**
     * Update session-related UI elements
     */
    private fun updateSessionUI(status: SessionStatus) {
        when (status) {
            SessionStatus.RECORDING -> {
                statusText.text = "Recording session active"
                qualityIndicator.visibility = ProgressBar.VISIBLE
            }
            SessionStatus.COMPLETED -> {
                statusText.text = "Session completed successfully"
                qualityIndicator.visibility = ProgressBar.INVISIBLE
            }
            SessionStatus.ERROR -> {
                statusText.text = "Session error occurred"
                qualityIndicator.visibility = ProgressBar.INVISIBLE
            }
            else -> {
                statusText.text = "Ready for recording"
                qualityIndicator.visibility = ProgressBar.INVISIBLE
            }
        }
    }
    
    /**
     * Update quality indicator colors and text
     */
    private fun updateQualityIndicator(quality: SessionQuality) {
        val color = when (quality.qualityLevel) {
            SessionQuality.QualityLevel.EXCELLENT -> android.graphics.Color.GREEN
            SessionQuality.QualityLevel.GOOD -> android.graphics.Color.BLUE
            SessionQuality.QualityLevel.FAIR -> android.graphics.Color.YELLOW
            SessionQuality.QualityLevel.POOR -> android.graphics.Color.ORANGE
            SessionQuality.QualityLevel.CRITICAL -> android.graphics.Color.RED
        }
        
        qualityIndicator.progressTintList = android.content.res.ColorStateList.valueOf(color)
    }
    
    /**
     * Show permission error dialog
     */
    private fun showPermissionError() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("This app requires Bluetooth and location permissions to connect to Shimmer devices and discover PC controllers.")
            .setPositiveButton("Settings") { _, _ ->
                // Could open app settings here
                finish()
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .show()
    }
    
    /**
     * Show initialization error details
     */
    private fun showInitializationError(gsrInitialized: Boolean, networkInitialized: Boolean) {
        val message = buildString {
            append("Component initialization failed:\n\n")
            if (!gsrInitialized) append("• GSR Recorder: Failed\n")
            if (!networkInitialized) append("• Network Controller: Failed\n")
            append("\nPlease check Bluetooth and Wi-Fi settings.")
        }
        
        AlertDialog.Builder(this)
            .setTitle("Initialization Error")
            .setMessage(message)
            .setPositiveButton("Retry") { _, _ ->
                initializeComponents()
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .show()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "Cleaning up Unified Sensor Activity")
        
        // Cleanup components
        lifecycleScope.launch {
            try {
                sessionManager.cleanup()
                gsrRecorder.cleanup()
                networkController.cleanup()
            } catch (e: Exception) {
                Log.e(TAG, "Error during cleanup", e)
            }
        }
    }
}