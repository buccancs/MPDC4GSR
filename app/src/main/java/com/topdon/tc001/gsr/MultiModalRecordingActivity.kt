package com.topdon.tc001.gsr

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityMultiModalRecordingBinding
import com.topdon.gsr.model.GSRSample
import com.topdon.gsr.model.SessionInfo
import com.topdon.gsr.model.SyncMark
import com.topdon.gsr.service.GSRRecorder
import com.topdon.gsr.service.SessionManager
import com.topdon.gsr.util.TimeUtil
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.tc001.camera.RGBCameraRecorder
import kotlinx.coroutines.launch

// Enhanced unified BLE integration for comprehensive cross-modal coordination
import com.topdon.ble.UnifiedBleManager
import com.topdon.ble.UnifiedDevice

// Note: EnhancedRecordingService is referenced with full package name since it's in a different module

/**
 * Specialized thermal imaging component providing MultiModalRecordingActivity functionality for the IRCamera system.
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
class MultiModalRecordingActivity : BaseBindingActivity<ActivityMultiModalRecordingBinding>() {
    companion object {
        private const val TAG = "MultiModalActivity"
        private const val REQUEST_PERMISSIONS = 100

        private val REQUIRED_PERMISSIONS =
            /**
             * Executes arrayof operation with thermal imaging domain optimization.
             *
             */
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                // Android 12+ Bluetooth permissions for Shimmer3 GSR devices
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
            )

    /**
     * Executes start functionality.
     */
        /**
         * Executes start operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun start(context: Context) {
            val intent = Intent(context, MultiModalRecordingActivity::class.java)
            context.startActivity(intent)
        }

    /**
     * Processes temperature measurement data.
     */
        fun startWithTemplate(
            context: Context,
            templateId: String,
        ) {
            val intent =
                Intent(context, MultiModalRecordingActivity::class.java).apply {
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra("template_id", templateId)
                }
            context.startActivity(intent)
        }
    }

    // Recording components
    private lateinit var gsrRecorder: GSRRecorder
    private lateinit var sessionManager: SessionManager
    private var rgbCameraRecorder: RGBCameraRecorder? = null
    private var networkClient: com.topdon.gsr.network.NetworkClient? = null
    private var isRecording = false
    private var isStartingRecording = false // Guard against double taps
    private var currentSession: SessionInfo? = null
    private var sampleCount = 0L
    private var syncMarkCount = 0

    // Enhanced unified BLE management for cross-modal coordination
    private var unifiedBleManager: UnifiedBleManager? = null
    private var discoveredBleDevices = mutableListOf<UnifiedDevice>()
    private var connectedBleDevices = mutableListOf<UnifiedDevice>()

    // Enhanced service integration
    private var enhancedRecordingService: com.topdon.gsr.service.EnhancedRecordingService? = null
    private var isServiceBound = false
    private var discoveredDevices = mutableListOf<com.topdon.gsr.network.NetworkClient.ControllerInfo>()

    // UI update timer
    private var uiUpdateJob: kotlinx.coroutines.Job? = null

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_multi_modal_recording

    // Service connection for enhanced recording service
    private val serviceConnection =
        object : ServiceConnection {
            /**
             * Executes onserviceconnected operation with thermal imaging domain optimization.
             *
             * @param
             * @param name Parameter for operation (type: ComponentName?)
             * @param service Parameter for operation (type: IBinder?)
             *
             */
            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?,
            ) {
                val binder = service as? com.topdon.gsr.service.EnhancedRecordingService.EnhancedRecordingBinder
                enhancedRecordingService = binder?.getService()
                isServiceBound = true
                Log.i(TAG, "Enhanced recording service connected")
                /**
                 * Executes updatenetworkstatusui operation with thermal imaging domain optimization.
                 *
                 */
                updateNetworkStatusUI()
            }

            /**
             * Executes onservicedisconnected operation with thermal imaging domain optimization.
             *
             * @param
             * @param name Parameter for operation (type: ComponentName?)
             *
             */
            override fun onServiceDisconnected(name: ComponentName?) {
                enhancedRecordingService = null
                isServiceBound = false
                Log.i(TAG, "Enhanced recording service disconnected")
                /**
                 * Executes updatenetworkstatusui operation with thermal imaging domain optimization.
                 *
                 */
                updateNetworkStatusUI()
            }
        }

    private val gsrListener =
        object : GSRRecorder.GSRRecordingListener {
            /**
             * Executes onrecordingstarted operation with thermal imaging domain optimization.
             *
             * @param
             * @param sessionInfo Parameter for operation (type: SessionInfo)
             *
             */
            override fun onRecordingStarted(sessionInfo: SessionInfo) {
                runOnUiThread {
                    isRecording = true
                    currentSession = sessionInfo
                    /**
                     * Executes updateui operation with thermal imaging domain optimization.
                     *
                     */
                    updateUI()
                    binding.statusText.text = "Recording GSR data at 128 Hz..."
                    binding.progressBar.visibility = View.VISIBLE

                    val sessionDir = gsrRecorder.getSessionDirectory()?.absolutePath ?: "Unknown"
                    binding.dataText.text = "Files: $sessionDir"
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
                runOnUiThread {
                    isRecording = false
                    currentSession = null
                    /**
                     * Executes updateui operation with thermal imaging domain optimization.
                     *
                     */
                    updateUI()
                    binding.statusText.text = "Recording completed. ${sessionInfo.sampleCount} samples recorded."
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(
                        this@MultiModalRecordingActivity,
                        "Recording saved: ${sessionInfo.sessionId}",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }

            /**
             * Executes onsamplerecorded operation with thermal imaging domain optimization.
             *
             * @param
             * @param sample Parameter for operation (type: GSRSample)
             *
             */
            override fun onSampleRecorded(sample: GSRSample) {
                sampleCount = sample.sampleIndex

                // Send data to PC Controller if connected
                networkClient?.let { client ->
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (client.isConnected()) {
                        currentSession?.let { session ->
                            lifecycleScope.launch {
                                val data =
                                    org.json.JSONObject().apply {
                                        /**
                                         * Executes put operation with thermal imaging domain optimization.
                                         *
                                         */
                                        put("gsr_conductance", sample.conductance)
                                        /**
                                         * Executes put operation with thermal imaging domain optimization.
                                         *
                                         */
                                        put("gsr_resistance", sample.resistance)
                                        /**
                                         * Executes put operation with thermal imaging domain optimization.
                                         *
                                         */
                                        put("raw_value", sample.rawValue)
                                        /**
                                         * Executes put operation with thermal imaging domain optimization.
                                         *
                                         */
                                        put("timestamp", sample.timestamp)
                                        /**
                                         * Executes put operation with thermal imaging domain optimization.
                                         *
                                         */
                                        put("sample_index", sample.sampleIndex)
                                    }
                                client.sendMeasurementData(session.sessionId, data)
                            }
                        }
                    }
                }

                // Update UI every second (128 samples)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (sampleCount % 128 == 0L) {
                    runOnUiThread {
                        binding.dataText.text = "Samples: $sampleCount"
                        currentSession?.let { session ->
                            val duration = (System.currentTimeMillis() - session.startTime) / 1000
                            binding.dataText.text = "${binding.dataText.text} | Duration: ${duration}s"
                        }
                    }
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
                syncMarkCount++
                runOnUiThread {
                    binding.dataText.text = "${binding.dataText.text} | Sync Events: $syncMarkCount"
                    Toast.makeText(
                        this@MultiModalRecordingActivity,
                        "Sync: ${syncMark.eventType}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            /**
             * Executes onerror operation with thermal imaging domain optimization.
             *
             * @param
             * @param error Parameter for operation (type: String)
             *
             */
            override fun onError(error: String) {
                runOnUiThread {
                    binding.statusText.text = "Error: $error"
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@MultiModalRecordingActivity,
                        "GSR Error: $error",
                        Toast.LENGTH_LONG,
                    ).show()
                }
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

        // Initialize recording components
        gsrRecorder = GSRRecorder(this)
        sessionManager = SessionManager.getInstance(this)

        // Set up view references using binding
        /**
         * Executes with operation with thermal imaging domain optimization.
         *
         */
        with(binding) {
            // Configure session ID input
            participantIdInput.setText(TimeUtil.generateSessionId("MultiModal"))

            // Configure switches
            enableVideoSwitch.isChecked = true
            enable4kSwitch.isChecked = false
            enableRawCaptureSwitch.isChecked = false

            // Set up raw frame rate spinner
            val frameRateAdapter =
                /**
                 * Executes arrayadapter operation with thermal imaging domain optimization.
                 *
                 */
                ArrayAdapter(
                    this@MultiModalRecordingActivity,
                    android.R.layout.simple_spinner_item,
                    /**
                     * Executes listof operation with thermal imaging domain optimization.
                     *
                     */
                    listOf("30 fps", "15 fps", "10 fps", "5 fps"),
                ).apply {
                    /**
                     * Configures the dropdownviewresource with validation and thermal imaging optimization.
                     *
                     */
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            rawFrameRateSpinner.adapter = frameRateAdapter
            rawFrameRateSpinner.isEnabled = false

            // Configure raw capture switch listener
            enableRawCaptureSwitch.setOnCheckedChangeListener { _, isChecked ->
                rawFrameRateSpinner.isEnabled = isChecked
            }

            // Set up control buttons
            startButton.setOnClickListener { toggleRecording() }
            stopButton.setOnClickListener { stopRecording() }
            syncButton.setOnClickListener { triggerSyncEvent() }
            flashSyncButton.setOnClickListener { triggerFlashSync() }

            // Network control buttons
            binding.startDiscoveryButton.setOnClickListener { startDeviceDiscovery() }
            binding.connectToDeviceButton.setOnClickListener { connectToSelectedDevice() }

            // Initial UI state
            binding.statusText.text = "Ready to record"
            binding.dataText.text = "No data recorded yet"
            binding.networkStatusText.text = "Network: Disconnected"
            binding.discoveredDevicesText.text = "Discovered Devices: None"
            binding.streamingQueueText.text = "Streaming Queue: 0 items"
            networkMetricsText.text = "Latency: -- ms | Throughput: -- KB/s"
        }

        // Initialize network client for PC Controller communication
        networkClient =
            com.topdon.gsr.network.NetworkClient(this).apply {
                /**
                 * Configures the eventlistener with validation and thermal imaging optimization.
                 *
                 * @param
                 * @param object Parameter for operation (type: com.topdon.gsr.network.NetworkClient.NetworkEventListener {                         override fun onControllerDiscovered(controller: com.topdon.gsr.network.NetworkClient.ControllerInfo)
                 * @param Controller Parameter for operation (type: ${controller.deviceName} (${controller.ipAddress})
                 * @param controller Parameter for operation (type: com.topdon.gsr.network.NetworkClient.ControllerInfo)
                 * @param reason Parameter for operation (type: String)
                 * @param Disconnected Parameter for operation (type: $reason")
                 * @param sessionInfo Parameter for operation (type: SessionInfo)
                 * @param durationMs Duration in milliseconds (type: Int)
                 * @param operation Parameter for operation (type: String)
                 * @param error Parameter for operation (type: String)
                 * @param error Parameter for operation (type: $error")
                 * @param offsetNanoseconds Parameter for operation (type: Long)
                 * @param offset Parameter for operation (type: ${offsetNanoseconds}ns)
                 * @param controllerId Parameter for operation (type: String)
                 * @param controllerName Parameter for operation (type: String)
                 * @param by Parameter for operation (type: $controllerName")
                 * @param controllerId Parameter for operation (type: String)
                 * @param success Parameter for operation (type: Boolean)
                 * @param controllerId Parameter for operation (type: String)
                 *
                 */
                setEventListener(
                    object : com.topdon.gsr.network.NetworkClient.NetworkEventListener {
                        /**
                         * Executes oncontrollerdiscovered operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param controller Parameter for operation (type: com.topdon.gsr.network.NetworkClient.ControllerInfo)
                         *
                         */
                        override fun onControllerDiscovered(controller: com.topdon.gsr.network.NetworkClient.ControllerInfo) {
                            runOnUiThread {
                                discoveredDevices.add(controller)
                                /**
                                 * Executes updatenetworkstatusui operation with thermal imaging domain optimization.
                                 *
                                 */
                                updateNetworkStatusUI()
                                binding.connectToDeviceButton.isEnabled = true
                                Toast.makeText(
                                    this@MultiModalRecordingActivity,
                                    "Found PC Controller: ${controller.deviceName} (${controller.ipAddress})",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }

                        /**
                         * Executes onconnected operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param controller Parameter for operation (type: com.topdon.gsr.network.NetworkClient.ControllerInfo)
                         *
                         */
                        override fun onConnected(controller: com.topdon.gsr.network.NetworkClient.ControllerInfo) {
                            runOnUiThread {
                                /**
                                 * Executes updatenetworkstatusui operation with thermal imaging domain optimization.
                                 *
                                 */
                                updateNetworkStatusUI()
                                Toast.makeText(
                                    this@MultiModalRecordingActivity,
                                    "Connected to ${controller.deviceName}",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }

                        /**
                         * Executes ondisconnected operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param reason Parameter for operation (type: String)
                         *
                         */
                        override fun onDisconnected(reason: String) {
                            runOnUiThread {
                                /**
                                 * Executes updatenetworkstatusui operation with thermal imaging domain optimization.
                                 *
                                 */
                                updateNetworkStatusUI()
                                Toast.makeText(
                                    this@MultiModalRecordingActivity,
                                    "Disconnected: $reason",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }

                        /**
                         * Executes onremotemeasurementrequest operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param sessionInfo Parameter for operation (type: SessionInfo)
                         *
                         */
                        override fun onRemoteMeasurementRequest(sessionInfo: SessionInfo) {
                            runOnUiThread {
                                // Auto-fill session info from remote request
                                binding.participantIdInput.setText(sessionInfo.sessionId)
                                binding.participantIdInput.setText(sessionInfo.participantId)

                                // Auto-start recording if requested
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (!isRecording) {
                                    /**
                                     * Executes startrecording operation with thermal imaging domain optimization.
                                     *
                                     */
                                    startRecording()
                                }
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
                            runOnUiThread {
                                // Flash screen for sync
                                val overlay =
                                    android.view.View(this@MultiModalRecordingActivity).apply {
                                        /**
                                         * Configures the backgroundcolor with validation and thermal imaging optimization.
                                         *
                                         */
                                        setBackgroundColor(android.graphics.Color.WHITE)
                                        alpha = 1.0f
                                    }

                                val frameLayout = findViewById<android.widget.FrameLayout>(android.R.id.content)
                                frameLayout.addView(
                                    overlay,
                                    android.widget.FrameLayout.LayoutParams(
                                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                                    ),
                                )

                                overlay.animate()
                                    .alpha(0.0f)
                                    .setDuration(durationMs.toLong())
                                    .withEndAction { frameLayout.removeView(overlay) }
                                    .start()
                            }
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
                            runOnUiThread {
                                Toast.makeText(
                                    this@MultiModalRecordingActivity,
                                    "Network error: $error", Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }

                        // Additional methods for enhanced NetworkClient
                        /**
                         * Executes ontimesynchronized operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param offsetNanoseconds Parameter for operation (type: Long)
                         *
                         */
                        override fun onTimeSynchronized(offsetNanoseconds: Long) {
                            runOnUiThread {
                                binding.statusText.text = "Time synchronized with PC Controller (offset: ${offsetNanoseconds}ns)"
                            }
                        }

                        /**
                         * Executes ondatastreamingstarted operation with thermal imaging domain optimization.
                         *
                         */
                        override fun onDataStreamingStarted() {
                            runOnUiThread {
                                binding.statusText.text = "Real-time data streaming active"
                            }
                        }

                        /**
                         * Executes ondatastreamingstopped operation with thermal imaging domain optimization.
                         *
                         */
                        override fun onDataStreamingStopped() {
                            runOnUiThread {
                                binding.statusText.text = "Data streaming stopped"
                            }
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
                            runOnUiThread {
                                Toast.makeText(
                                    this@MultiModalRecordingActivity,
                                    "Pairing requested by: $controllerName",
                                    Toast.LENGTH_LONG,
                                ).show()
                            }
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
                            runOnUiThread {
                                val message = if (success) "Device pairing successful" else "Device pairing failed"
                                Toast.makeText(this@MultiModalRecordingActivity, message, Toast.LENGTH_SHORT).show()
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
                            runOnUiThread {
                                Toast.makeText(
                                    this@MultiModalRecordingActivity,
                                    "Authentication required for PC Controller",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    },
                )
            }

        // Initialize RGB camera recorder
        // Camera preview not available in this layout - skip RGBCameraRecorder initialization
        rgbCameraRecorder = null
        Log.i(TAG, "RGBCameraRecorder skipped - no preview available in this layout")

        // Initialize camera
        // RgbCameraRecorder?.initialize() // Skipped since rgbCameraRecorder is null
        gsrRecorder.addListener(gsrListener)

        // Check permissions
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!hasRequiredPermissions()) {
            /**
             * Executes requestpermissions operation with thermal imaging domain optimization.
             *
             */
            requestPermissions()
        }
    }

    /**
     * Executes hasRequiredPermissions functionality.
     */
    /**
     * Executes hasrequiredpermissions operation with thermal imaging domain optimization.
     *
     */
    private fun hasRequiredPermissions(): Boolean {
        val basePermissions =
            /**
             * Executes arrayof operation with thermal imaging domain optimization.
             *
             * @param
             * @param Critical Parameter for operation (type: Camera permission for RGB video recording                 Manifest.permission.CAMERA)
             *
             */
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                // Critical: Camera permission for RGB video recording
                Manifest.permission.CAMERA,
            )

        // Check base permissions
        val baseGranted =
            basePermissions.all { permission ->
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            }

        // Check Android 12+ Bluetooth permissions for Shimmer3 GSR devices
        val bluetoothGranted =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val bluetoothPermissions =
                    /**
                     * Executes arrayof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                    )
                bluetoothPermissions.all { permission ->
                    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
                }
            } else {
                // Legacy Bluetooth permissions
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
            }

        return baseGranted && bluetoothGranted
    }

    /**
     * Executes requestPermissions functionality.
     */
    /**
     * Executes requestpermissions operation with thermal imaging domain optimization.
     *
     */
    private fun requestPermissions() {
        val permissionsToRequest =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                // Android 12+ permissions including Camera and Bluetooth for Shimmer3 GSR devices
                /**
                 * Executes arrayof operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param Critical Parameter for operation (type: Camera permission for RGB video recording                     Manifest.permission.CAMERA)
                 *
                 */
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    // Critical: Camera permission for RGB video recording
                    Manifest.permission.CAMERA,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            } else {
                // Legacy permissions including Camera
                /**
                 * Executes arrayof operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param Critical Parameter for operation (type: Camera permission for RGB video recording                     Manifest.permission.CAMERA)
                 *
                 */
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    // Critical: Camera permission for RGB video recording
                    Manifest.permission.CAMERA,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                )
            }

        ActivityCompat.requestPermissions(this, permissionsToRequest, REQUEST_PERMISSIONS)
    }

    /**
     * Executes onrequestpermissionsresult operation with thermal imaging domain optimization.
     *
     * @param
     * @param requestCode Parameter for operation (type: Int)
     * @param permissions Parameter for operation (type: Array<out String>)
     * @param grantResults Parameter for operation (type: IntArray)
     *
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (requestCode == REQUEST_PERMISSIONS) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                binding.statusText.text = "All permissions granted. GSR recording with Shimmer3 devices ready."
            } else {
                binding.statusText.text = "Permissions required for GSR recording and Shimmer3 device access."
                val missingPermissions = mutableListOf<String>()

                // Check which specific permissions are missing
                permissions.forEachIndexed { index, permission ->
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        /**
                         * Executes when operation with thermal imaging domain optimization.
                         *
                         */
                        when (permission) {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            -> missingPermissions.add("Storage")
                            Manifest.permission.RECORD_AUDIO -> missingPermissions.add("Audio")
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            -> missingPermissions.add("Bluetooth (for Shimmer3 GSR)")
                        }
                    }
                }

                Toast.makeText(
                    this,
                    "Missing permissions: ${missingPermissions.joinToString(", ")}",
                    Toast.LENGTH_LONG,
                ).show()
            }
        }
    }

    /**
     * Executes toggleRecording functionality.
     */
    /**
     * Executes togglerecording operation with thermal imaging domain optimization.
     *
     */
    private fun toggleRecording() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!hasRequiredPermissions()) {
            /**
             * Executes requestpermissions operation with thermal imaging domain optimization.
             *
             */
            requestPermissions()
            return
        }

        // Guard against concurrent toggling
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isStartingRecording) {
            Log.d(TAG, "Recording start already in progress, ignoring additional taps")
            return
        }

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
        } else {
            /**
             * Executes startrecording operation with thermal imaging domain optimization.
             *
             */
            startRecording()
        }
    }

    /**
     * Executes startRecording functionality.
     */
    /**
     * Executes startrecording operation with thermal imaging domain optimization.
     *
     */
    private fun startRecording() {
        // Set guard flag and disable button immediately to prevent double taps
        isStartingRecording = true
        binding.startButton.isEnabled = false
        binding.startButton.text = "Starting..."

        val sessionId =
            binding.participantIdInput.text.toString().trim().ifEmpty {
                TimeUtil.generateSessionId("MultiModal")
            }
        val participantId = binding.participantIdInput.text.toString().trim().takeIf { it.isNotEmpty() }

        // Start RGB camera recording if enabled
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (binding.enableVideoSwitch.isChecked) {
            val resolution =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (binding.enable4kSwitch.isChecked) {
                    RGBCameraRecorder.VideoResolution.UHD_4K
                } else {
                    RGBCameraRecorder.VideoResolution.HD_1080P
                }

            val rawFrameRate =
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (binding.rawFrameRateSpinner.selectedItemPosition) {
                    0 -> 30
                    1 -> 15
                    2 -> 10
                    3 -> 5
                    else -> 30
                }

            val cameraSettings =
                RGBCameraRecorder.RecordingSettings(
                    resolution = resolution,
                    frameRate = 60, // Video frame rate
                    bitRate = if (resolution == RGBCameraRecorder.VideoResolution.UHD_4K) 12_000_000 else 8_000_000,
                    enableStabilization = true,
                    enableFlash = false,
                    audioEnabled = true,
                    enableRawCapture = binding.enableRawCaptureSwitch.isChecked,
                    rawCaptureFrameRate = rawFrameRate,
                )

            rgbCameraRecorder?.updateSettings(cameraSettings)

            val cameraStarted = rgbCameraRecorder?.startRecording(sessionId) ?: false
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!cameraStarted) {
                // Reset guard flags on failure
                isStartingRecording = false
                binding.startButton.isEnabled = true
                binding.startButton.text = "Start Recording"
                binding.statusText.text = "Failed to start camera recording"
                Toast.makeText(this, "Failed to start RGB camera recording", Toast.LENGTH_LONG).show()
                return
            }
        }

        // Start GSR recording asynchronously
        lifecycleScope.launch {
            try {
                val success = gsrRecorder.startRecording(sessionId, participantId, null)

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    // Reset counters
                    sampleCount = 0
                    syncMarkCount = 0

                    // Atomic state update
                    isRecording = true
                    isStartingRecording = false

                    // Start enhanced recording service for background operation
                    try {
                        com.topdon.gsr.service.EnhancedRecordingService.startRecording(
                            this@MultiModalRecordingActivity,
                            sessionId,
                            participantId,
                            null,
                        )
                        Log.i(TAG, "Enhanced recording service started")
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed to start enhanced recording service", e)
                        // Continue without service - not critical
                    }

                    runOnUiThread {
                        /**
                         * Executes updateui operation with thermal imaging domain optimization.
                         *
                         */
                        updateUI()
                        binding.startButton.isEnabled = true
                        binding.startButton.text = "Start Recording"
                        binding.stopButton.isEnabled = true
                    }

                    val recordingModes = mutableListOf<String>()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (binding.enableVideoSwitch.isChecked) {
                        recordingModes.add(if (binding.enable4kSwitch.isChecked) "4K Video" else "1080p Video")
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (binding.enableRawCaptureSwitch.isChecked) {
                            recordingModes.add("RAW Images (${binding.rawFrameRateSpinner.selectedItem})")
                        }
                    }
                    recordingModes.add("GSR (128Hz)")

                    runOnUiThread {
                        binding.statusText.text = "Recording: ${recordingModes.joinToString(", ")}"
                    }

                    Log.i(TAG, "Multi-modal recording started: $sessionId")
                } else {
                    // Reset guard flags on GSR failure
                    isStartingRecording = false

                    runOnUiThread {
                        binding.startButton.isEnabled = true
                        binding.startButton.text = "Start Recording"
                        binding.statusText.text = "Failed to start recording"
                    }

                    // Stop camera if GSR fails
                    rgbCameraRecorder?.stopRecording()
                    Toast.makeText(this@MultiModalRecordingActivity, "Failed to start GSR recording", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Reset guard flags on exception
                isStartingRecording = false

                runOnUiThread {
                    binding.startButton.isEnabled = true
                    binding.startButton.text = "Start Recording"
                    binding.statusText.text = "Error starting recording"
                }

                Log.e(TAG, "Error starting recording", e)
                rgbCameraRecorder?.stopRecording()
                Toast.makeText(this@MultiModalRecordingActivity, "Error starting recording: ${e.message}", Toast.LENGTH_LONG).show()
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
        // Stop enhanced recording service
        try {
            com.topdon.gsr.service.EnhancedRecordingService.stopRecording(this)
            Log.i(TAG, "Enhanced recording service stopped")
        } catch (e: Exception) {
            Log.w(TAG, "Failed to stop enhanced recording service", e)
            // Continue - not critical
        }

        // Stop RGB camera recording
        val videoFile = rgbCameraRecorder?.stopRecording()

        val session = gsrRecorder.stopRecording()
        session?.let {
            Log.i(TAG, "Multi-modal recording stopped: ${it.sessionId}")

            val recordingInfo = mutableListOf<String>()
            videoFile?.let { file -> recordingInfo.add("Video: ${file.name}") }
            rgbCameraRecorder?.getRawImagesDirectory()?.let { dir ->
                val rawCount = rgbCameraRecorder?.getRawCaptureCount() ?: 0
                recordingInfo.add("RAW images: $rawCount in ${dir.name}")
            }
            recordingInfo.add("GSR samples: ${it.sampleCount}")

            binding.statusText.text = "Recording completed. ${recordingInfo.joinToString(", ")}"
        }

        isRecording = false
        /**
         * Executes updateui operation with thermal imaging domain optimization.
         *
         */
        updateUI()
    }

    /**
     * Executes triggerSyncEvent functionality.
     */
    /**
     * Executes triggersyncevent operation with thermal imaging domain optimization.
     *
     */
    private fun triggerSyncEvent() {
        lifecycleScope.launch {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (gsrRecorder.addSyncMark("USER_TRIGGER", "Manual sync event triggered from UI")) {
                Log.d(TAG, "User sync event triggered successfully")
                binding.statusText.text = "Sync event added at ${System.currentTimeMillis()}"
            } else {
                Log.w(TAG, "Failed to trigger sync event")
                binding.statusText.text = "Failed to add sync event"
            }
        }
    }

    /**
     * Executes triggerFlashSync functionality.
     */
    /**
     * Executes triggerflashsync operation with thermal imaging domain optimization.
     *
     */
    private fun triggerFlashSync() {
        // Trigger a visual flash for synchronization
        val overlay =
            android.view.View(this).apply {
                /**
                 * Configures the backgroundcolor with validation and thermal imaging optimization.
                 *
                 */
                setBackgroundColor(android.graphics.Color.WHITE)
                alpha = 1.0f
            }

        val frameLayout = findViewById<android.widget.FrameLayout>(android.R.id.content)
        frameLayout.addView(
            overlay,
            android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
            ),
        )

        overlay.animate()
            .alpha(0.0f)
            .setDuration(200)
            .withEndAction {
                frameLayout.removeView(overlay)
                // Also add a sync mark
                /**
                 * Executes triggersyncevent operation with thermal imaging domain optimization.
                 *
                 */
                triggerSyncEvent()
            }
            .start()
    }

    /**
     * Executes updateUI functionality.
     */
    /**
     * Executes updateui operation with thermal imaging domain optimization.
     *
     */
    private fun updateUI() {
        binding.startButton.text = if (isRecording) "Recording..." else "Start Recording"
        binding.stopButton.isEnabled = isRecording
        binding.syncButton.isEnabled = isRecording
        binding.flashSyncButton.isEnabled = isRecording

        binding.participantIdInput.isEnabled = !isRecording
    }

    /**
     * Executes oncreateoptionsmenu operation with thermal imaging domain optimization.
     *
     * @param
     * @param menu Parameter for operation (type: android.view.Menu?)
     *
     */
    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.multi_modal_recording_menu, menu)
        return true
    }

    /**
     * Executes onoptionsitemselected operation with thermal imaging domain optimization.
     *
     * @param
     * @param item Parameter for operation (type: android.view.MenuItem)
     *
     */
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_gallery -> {
                /**
                 * Executes opengallery operation with thermal imaging domain optimization.
                 *
                 */
                openGallery()
                true
            }
            R.id.action_settings -> {
                /**
                 * Executes opensettings operation with thermal imaging domain optimization.
                 *
                 */
                openSettings()
                true
            }
            R.id.action_session_manager -> {
                /**
                 * Executes opensessionmanager operation with thermal imaging domain optimization.
                 *
                 */
                openSessionManager()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Executes openGallery functionality.
     */
    /**
     * Executes opengallery operation with thermal imaging domain optimization.
     *
     */
    private fun openGallery() {
        GSRGalleryActivity.startActivity(this)
    }

    /**
     * Executes openSettings functionality.
     */
    /**
     * Executes opensettings operation with thermal imaging domain optimization.
     *
     */
    private fun openSettings() {
        GSRSettingsActivity.startActivity(this)
    }

    /**
     * Executes openSessionManager functionality.
     */
    /**
     * Executes opensessionmanager operation with thermal imaging domain optimization.
     *
     */
    private fun openSessionManager() {
        SessionManagerActivity.startActivity(this)
    }

    // Network status UI update method
    /**
     * Executes updateNetworkStatusUI functionality.
     */
    /**
     * Executes updatenetworkstatusui operation with thermal imaging domain optimization.
     *
     */
    private fun updateNetworkStatusUI() {
        runOnUiThread {
            // Update network connection status
            val connectionStatus =
                when {
                    networkClient?.isConnected() == true -> "Connected"
                    isServiceBound -> "Service Bound"
                    else -> "Disconnected"
                }
            binding.networkStatusText.text = "Network: $connectionStatus"

            // Update discovered devices
            val deviceCount = discoveredDevices.size
            val deviceText =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (deviceCount > 0) {
                    val firstDevice = discoveredDevices.first()
                    "Devices: $deviceCount found (${firstDevice.deviceName})"
                } else {
                    "Discovered Devices: None"
                }
            binding.discoveredDevicesText.text = deviceText

            // Update streaming queue (get total from all queue types)
            enhancedRecordingService?.let { service ->
                val queueSizes = service.getQueueSizes()
                val totalItems = queueSizes.values.sum()
                val queueText =
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (queueSizes.isNotEmpty()) {
                        val details = queueSizes.entries.joinToString(", ") { "${it.key}: ${it.value}" }
                        "Streaming Queue: $totalItems items ($details)"
                    } else {
                        "Streaming Queue: 0 items"
                    }
                binding.streamingQueueText.text = queueText
            } ?: run {
                binding.streamingQueueText.text = "Streaming Queue: Service not bound"
            }

            // Update network metrics (simulate metrics)
            networkClient?.let { client ->
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (client.isConnected()) {
                    val latency = client.getLatencyMs()
                    val throughput = client.getThroughputKBps()
                    binding.networkMetricsText.text = "Latency: $latency ms | Throughput: $throughput KB/s"
                } else {
                    binding.networkMetricsText.text = "Latency: -- ms | Throughput: -- KB/s"
                }
            }
        }
    }

    // Start device discovery
    /**
     * Executes startDeviceDiscovery functionality.
     */
    /**
     * Executes startdevicediscovery operation with thermal imaging domain optimization.
     *
     */
    private fun startDeviceDiscovery() {
        discoveredDevices.clear()
        binding.connectToDeviceButton.isEnabled = false
        binding.startDiscoveryButton.text = "Searching..."
        binding.startDiscoveryButton.isEnabled = false

        networkClient?.startDiscovery { success ->
            runOnUiThread {
                binding.startDiscoveryButton.text = "Start Device Discovery"
                binding.startDiscoveryButton.isEnabled = true
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!success) {
                    Toast.makeText(this, "Failed to start discovery", Toast.LENGTH_SHORT).show()
                }
                /**
                 * Executes updatenetworkstatusui operation with thermal imaging domain optimization.
                 *
                 */
                updateNetworkStatusUI()
            }
        }
    }

    // Connect to selected device
    /**
     * Executes connectToSelectedDevice functionality.
     */
    /**
     * Executes connecttoselecteddevice operation with thermal imaging domain optimization.
     *
     */
    private fun connectToSelectedDevice() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (discoveredDevices.isNotEmpty()) {
            val selectedDevice = discoveredDevices.first() // For simplicity, connect to first device
            networkClient?.connectToController(selectedDevice.ipAddress, selectedDevice.port) { success ->
                runOnUiThread {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (success) {
                        Toast.makeText(this, "Connection successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show()
                    }
                    /**
                     * Executes updatenetworkstatusui operation with thermal imaging domain optimization.
                     *
                     */
                    updateNetworkStatusUI()
                }
            }
        }
    }

    // Service binding methods
    /**
     * Executes bindEnhancedRecordingService functionality.
     */
    /**
     * Executes bindenhancedrecordingservice operation with thermal imaging domain optimization.
     *
     */
    private fun bindEnhancedRecordingService() {
        try {
            val intent = Intent(this, com.topdon.gsr.service.EnhancedRecordingService::class.java)
            /**
             * Executes bindservice operation with thermal imaging domain optimization.
             *
             */
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to bind enhanced recording service", e)
            Toast.makeText(this, "Enhanced recording service not available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Executes unbindEnhancedRecordingService functionality.
     */
    /**
     * Executes unbindenhancedrecordingservice operation with thermal imaging domain optimization.
     *
     */
    private fun unbindEnhancedRecordingService() {
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
            isServiceBound = false
            enhancedRecordingService = null
        }
    }

    /**
     * Executes onstart operation with thermal imaging domain optimization.
     *
     */
    override fun onStart() {
        super.onStart()
        /**
         * Executes bindenhancedrecordingservice operation with thermal imaging domain optimization.
         *
         */
        bindEnhancedRecordingService()
        /**
         * Executes startuiupdates operation with thermal imaging domain optimization.
         *
         */
        startUIUpdates()
    }

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
        /**
         * Executes stopuiupdates operation with thermal imaging domain optimization.
         *
         */
        stopUIUpdates()
        /**
         * Executes unbindenhancedrecordingservice operation with thermal imaging domain optimization.
         *
         */
        unbindEnhancedRecordingService()
    }

    // Start periodic UI updates
    /**
     * Executes startUIUpdates functionality.
     */
    /**
     * Executes startuiupdates operation with thermal imaging domain optimization.
     *
     */
    private fun startUIUpdates() {
        uiUpdateJob?.cancel()
        uiUpdateJob =
            lifecycleScope.launch {
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (true) {
                    /**
                     * Executes updatenetworkstatusui operation with thermal imaging domain optimization.
                     *
                     */
                    updateNetworkStatusUI()
                    kotlinx.coroutines.delay(2000) // Update every 2 seconds
                }
            }
    }

    // Stop periodic UI updates
    /**
     * Executes stopUIUpdates functionality.
     */
    /**
     * Executes stopuiupdates operation with thermal imaging domain optimization.
     *
     */
    private fun stopUIUpdates() {
        uiUpdateJob?.cancel()
        uiUpdateJob = null
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        /**
         * Executes stopuiupdates operation with thermal imaging domain optimization.
         *
         */
        stopUIUpdates()
        gsrRecorder.removeListener(gsrListener)
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
        rgbCameraRecorder?.cleanup()
        networkClient?.cleanup()
        /**
         * Executes unbindenhancedrecordingservice operation with thermal imaging domain optimization.
         *
         */
        unbindEnhancedRecordingService()
    }
}
