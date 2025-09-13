package com.topdon.tc001.gsr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityGsrSettingsBinding
import com.topdon.ble.util.BluetoothPermissionUtils
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.tc001.sensors.gsr.GSRSensorRecorder
import kotlinx.coroutines.launch

/**
 * Specialized thermal imaging component providing GSRSettingsActivity functionality for the IRCamera system.
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
class GSRSettingsActivity : BaseBindingActivity<ActivityGsrSettingsBinding>() {
    companion object {
        private const val TAG = "GSRSettingsActivity"

    /**
     * Executes startActivity functionality.
     */
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, GSRSettingsActivity::class.java))
        }
    }

    private lateinit var prefs: SharedPreferences
    private var gsrSensorRecorder: GSRSensorRecorder? = null
    private val availableDevices = mutableListOf<String>()
    private lateinit var deviceAdapter: ArrayAdapter<String>

    // Runtime permission handling
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var pendingOperation: (() -> Unit)? = null

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_gsr_settings

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        // Initialize permission handling first
        /**
         * Configures the uppermissionhandling with validation and thermal imaging optimization.
         *
         */
        setupPermissionHandling()

        // Initialize GSR sensor recorder for device management
        gsrSensorRecorder = GSRSensorRecorder(this)

        /**
         * Configures the upui with validation and thermal imaging optimization.
         *
         */
        setupUI()
        /**
         * Executes loadcurrentsettings operation with thermal imaging domain optimization.
         *
         */
        loadCurrentSettings()
        /**
         * Configures the uplisteners with validation and thermal imaging optimization.
         *
         */
        setupListeners()
        /**
         * Configures the updevicemanagement with validation and thermal imaging optimization.
         *
         */
        setupDeviceManagement()

        // Setup modern back handling
        onBackPressedDispatcher.addCallback(
            this,
            object : androidx.activity.OnBackPressedCallback(true) {
                /**
                 * Executes handleonbackpressed operation with thermal imaging domain optimization.
                 *
                 */
                override fun handleOnBackPressed() {
                    /**
                     * Executes savecurrentsettings operation with thermal imaging domain optimization.
                     *
                     */
                    saveCurrentSettings()
                    /**
                     * Executes finish operation with thermal imaging domain optimization.
                     *
                     */
                    finish()
                }
            },
        )
    }

    /**
     * Setup comprehensive runtime permission handling for Shimmer GSR integration
     */
    private fun setupPermissionHandling() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val allGranted = permissions.values.all { it }
                val deniedPermissions = permissions.filter { !it.value }.keys

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (allGranted) {
                    Log.i(TAG, "All Bluetooth permissions granted")
                    /**
                     * Executes updatepermissionstatus operation with thermal imaging domain optimization.
                     *
                     */
                    updatePermissionStatus("All Required Permissions Granted", android.graphics.Color.parseColor("#4caf50"))

                    // Execute pending operation if any
                    pendingOperation?.invoke()
                    pendingOperation = null

                    // Enable device management UI
                    /**
                     * Executes enabledevicemanagement operation with thermal imaging domain optimization.
                     *
                     */
                    enableDeviceManagement(true)
                } else {
                    Log.w(TAG, "Some Bluetooth permissions denied: $deniedPermissions")

                    // Check if any permissions were permanently denied
                    val permanentlyDenied =
                        deniedPermissions.any { permission ->
                            !ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                        }

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (permanentlyDenied) {
                        /**
                         * Executes showpermissionpermanentlydenieddialog operation with thermal imaging domain optimization.
                         *
                         */
                        showPermissionPermanentlyDeniedDialog(deniedPermissions.toList())
                    } else {
                        /**
                         * Executes showpermissiondenieddialog operation with thermal imaging domain optimization.
                         *
                         */
                        showPermissionDeniedDialog(deniedPermissions.toList())
                    }

                    /**
                     * Executes updatepermissionstatus operation with thermal imaging domain optimization.
                     *
                     */
                    updatePermissionStatus("Missing Required Permissions", android.graphics.Color.parseColor("#f44336"))
                    /**
                     * Executes enabledevicemanagement operation with thermal imaging domain optimization.
                     *
                     */
                    enableDeviceManagement(false)
                }
            }
    }

    /**
     * Show dialog explaining why permissions are needed and how to grant them
     */
    private fun showPermissionDialog(missingPermissions: List<String>) {
        val permissionDescriptions =
            missingPermissions.map { permission ->
                "• ${BluetoothPermissionUtils.getPermissionRationale(permission)}"
            }.joinToString("\n")

        AlertDialog.Builder(this)
            .setTitle("Permissions Required for Shimmer GSR")
            .setMessage(
                "The following permissions are required for Shimmer GSR device functionality:\n\n$permissionDescriptions\n\nGrant these permissions to enable device scanning and connection.",
            )
            .setPositiveButton("Grant Permissions") { _, _ ->
                /**
                 * Executes requestmissingpermissions operation with thermal imaging domain optimization.
                 *
                 */
                requestMissingPermissions()
            }
            .setNegativeButton("Skip") { _, _ ->
                /**
                 * Executes updatepermissionstatus operation with thermal imaging domain optimization.
                 *
                 */
                updatePermissionStatus("Permissions Denied - Limited Functionality", android.graphics.Color.parseColor("#ff9800"))
                /**
                 * Executes enabledevicemanagement operation with thermal imaging domain optimization.
                 *
                 */
                enableDeviceManagement(false)
            }
            .setCancelable(false)
            .show()
    }

    /**
     * Show dialog when permissions are denied but can still be requested
     */
    private fun showPermissionDeniedDialog(deniedPermissions: List<String>) {
        val permissionDescriptions =
            deniedPermissions.map { permission ->
                "• ${BluetoothPermissionUtils.getPermissionRationale(permission)}"
            }.joinToString("\n")

        AlertDialog.Builder(this)
            .setTitle("Permissions Denied")
            .setMessage(
                "The following permissions were denied:\n\n$permissionDescriptions\n\nWithout these permissions, Shimmer GSR device functionality will be limited.",
            )
            .setPositiveButton("Try Again") { _, _ ->
                /**
                 * Executes requestmissingpermissions operation with thermal imaging domain optimization.
                 *
                 */
                requestMissingPermissions()
            }
            .setNegativeButton("Continue Without") { _, _ ->
                /**
                 * Executes updatepermissionstatus operation with thermal imaging domain optimization.
                 *
                 */
                updatePermissionStatus("Limited Functionality - Permissions Denied", android.graphics.Color.parseColor("#ff9800"))
                /**
                 * Executes enabledevicemanagement operation with thermal imaging domain optimization.
                 *
                 */
                enableDeviceManagement(false)
            }
            .show()
    }

    /**
     * Show dialog when permissions are permanently denied
     */
    private fun showPermissionPermanentlyDeniedDialog(deniedPermissions: List<String>) {
        val permissionDescriptions =
            deniedPermissions.map { permission ->
                "• ${BluetoothPermissionUtils.getPermissionRationale(permission)}"
            }.joinToString("\n")

        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage(
                "The following permissions are required but were denied:\n\n$permissionDescriptions\n\nTo enable Shimmer GSR functionality, please grant these permissions in the app settings.",
            )
            .setPositiveButton("Open Settings") { _, _ ->
                /**
                 * Executes openappsettings operation with thermal imaging domain optimization.
                 *
                 */
                openAppSettings()
            }
            .setNegativeButton("Continue Without") { _, _ ->
                /**
                 * Executes updatepermissionstatus operation with thermal imaging domain optimization.
                 *
                 */
                updatePermissionStatus("GSR Disabled - Check App Settings", android.graphics.Color.parseColor("#f44336"))
                /**
                 * Executes enabledevicemanagement operation with thermal imaging domain optimization.
                 *
                 */
                enableDeviceManagement(false)
            }
            .show()
    }

    /**
     * Open app settings so user can manually grant permissions
     */
    private fun openAppSettings() {
        try {
            val intent =
                /**
                 * Executes intent operation with thermal imaging domain optimization.
                 *
                 */
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
            /**
             * Executes startactivity operation with thermal imaging domain optimization.
             *
             */
            startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open app settings", e)
            Toast.makeText(this, "Please grant permissions in app settings manually", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Request missing permissions from user
     */
    private fun requestMissingPermissions() {
        val missingPermissions = BluetoothPermissionUtils.getMissingPermissions(this)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (missingPermissions.isEmpty()) {
            Log.i(TAG, "All permissions already granted")
            /**
             * Executes updatepermissionstatus operation with thermal imaging domain optimization.
             *
             */
            updatePermissionStatus("All Permissions Granted", android.graphics.Color.parseColor("#4caf50"))
            /**
             * Executes enabledevicemanagement operation with thermal imaging domain optimization.
             *
             */
            enableDeviceManagement(true)
            return
        }

        Log.i(TAG, "Requesting missing permissions: $missingPermissions")
        permissionLauncher.launch(missingPermissions.toTypedArray())
    }

    /**
     * Check and request permissions with user guidance
     */
    private fun checkAndRequestPermissions(onPermissionsGranted: (() -> Unit)? = null) {
        val missingPermissions = BluetoothPermissionUtils.getMissingPermissions(this)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (missingPermissions.isEmpty()) {
            Log.i(TAG, "All required permissions already granted")
            /**
             * Executes updatepermissionstatus operation with thermal imaging domain optimization.
             *
             */
            updatePermissionStatus("All Permissions Granted", android.graphics.Color.parseColor("#4caf50"))
            /**
             * Executes enabledevicemanagement operation with thermal imaging domain optimization.
             *
             */
            enableDeviceManagement(true)
            onPermissionsGranted?.invoke()
            return
        }

        // Store the callback for after permissions are granted
        pendingOperation = onPermissionsGranted

        // Show explanation dialog first
        /**
         * Executes showpermissiondialog operation with thermal imaging domain optimization.
         *
         */
        showPermissionDialog(missingPermissions)
    }

    /**
     * Enable or disable device management UI based on permission status
     */
    private fun enableDeviceManagement(enabled: Boolean) {
        binding.scanDevicesButton.isEnabled = enabled
        binding.connectDeviceButton.isEnabled = enabled
        binding.shimmerDeviceSpinner.isEnabled = enabled

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!enabled) {
            binding.deviceInfoText.text = "Bluetooth permissions required for device management"
        }
    }

    /**
     * Update permission status display
     */
    private fun updatePermissionStatus(
        status: String,
        color: Int,
    ) {
        // Update permission status in device info text if no specific device info
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (binding.deviceInfoText.text.toString().contains("permission", ignoreCase = true) ||
            binding.deviceInfoText.text.toString().isEmpty()
        ) {
            binding.deviceInfoText.text = status
            binding.deviceInfoText.setTextColor(color)
        }
    }

    /**
     * Sets upui configuration.
     */
    /**
     * Configures the upui with validation and thermal imaging optimization.
     *
     */
    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "GSR Recording Settings"

        // All UI components are now accessible via binding
        // No findViewById calls needed - view binding provides type-safe access

        /**
         * Configures the upspinners with validation and thermal imaging optimization.
         *
         */
        setupSpinners()
        /**
         * Configures the updevicespinner with validation and thermal imaging optimization.
         *
         */
        setupDeviceSpinner()
    }

    /**
     * Sets updevicespinner configuration.
     */
    private fun setupDeviceSpinner() {
        // Initialize device list with empty state
        availableDevices.clear()
        availableDevices.add("No devices found")

        deviceAdapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(this, android.R.layout.simple_spinner_item, availableDevices).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        binding.shimmerDeviceSpinner.adapter = deviceAdapter

        // Update device status
        /**
         * Executes updatedevicestatus operation with thermal imaging domain optimization.
         *
         */
        updateDeviceStatus()
    }

    /**
     * Sets upspinners configuration.
     */
    private fun setupSpinners() {
        // GSR Sampling Rate options
        val samplingRates = arrayOf("32 Hz", "64 Hz", "128 Hz", "256 Hz", "512 Hz")
        binding.gsrSamplingRateSpinner.adapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(this, android.R.layout.simple_spinner_item, samplingRates).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        // GSR Range options
        val gsrRanges = arrayOf("Auto Range", "40 kΩ - 4 MΩ", "10 kΩ - 1 MΩ", "4 kΩ - 400 kΩ")
        binding.gsrRangeSpinner.adapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(this, android.R.layout.simple_spinner_item, gsrRanges).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        // Video Resolution options
        val videoResolutions = arrayOf("4K UHD (3840×2160)", "Full HD (1920×1080)", "HD (1280×720)", "SD (720×480)")
        binding.videoResolutionSpinner.adapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(this, android.R.layout.simple_spinner_item, videoResolutions).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        // Video Frame Rate options
        val videoFrameRates = arrayOf("30 fps", "60 fps", "24 fps", "15 fps")
        binding.videoFrameRateSpinner.adapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(this, android.R.layout.simple_spinner_item, videoFrameRates).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        // RAW Frame Rate options
        val rawFrameRates = arrayOf("30 fps", "15 fps", "10 fps", "5 fps", "1 fps")
        binding.rawFrameRateSpinner.adapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(this, android.R.layout.simple_spinner_item, rawFrameRates).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        // RAW Quality options
        val rawQualities = arrayOf("Maximum (Level 3)", "High (Level 2)", "Standard (Level 1)")
        binding.rawQualitySpinner.adapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(this, android.R.layout.simple_spinner_item, rawQualities).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        // Data Retention options
        val dataRetentions = arrayOf("Keep Forever", "30 Days", "7 Days", "24 Hours")
        binding.dataRetentionSpinner.adapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(this, android.R.layout.simple_spinner_item, dataRetentions).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        // Sync Tolerance options
        val syncTolerances = arrayOf("1 ms", "5 ms", "10 ms", "50 ms", "100 ms")
        binding.syncToleranceSpinner.adapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(this, android.R.layout.simple_spinner_item, syncTolerances).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
    }

    /**
     * Executes loadCurrentSettings functionality.
     */
    /**
     * Executes loadcurrentsettings operation with thermal imaging domain optimization.
     *
     */
    private fun loadCurrentSettings() {
        // Load GSR Settings
        binding.gsrSamplingRateSpinner.setSelection(prefs.getInt("gsr_sampling_rate", 2)) // Default: 128 Hz
        binding.gsrRangeSpinner.setSelection(prefs.getInt("gsr_range", 0)) // Default: Auto Range
        binding.gsrCalibrationSwitch.isChecked = prefs.getBoolean("gsr_calibration", true)

        // Load Video Settings
        binding.videoResolutionSpinner.setSelection(prefs.getInt("video_resolution", 0)) // Default: 4K UHD
        binding.videoFrameRateSpinner.setSelection(prefs.getInt("video_frame_rate", 0)) // Default: 30 fps
        binding.enableVideoSwitch.isChecked = prefs.getBoolean("enable_video", true)
        binding.enableStabilizationSwitch.isChecked = prefs.getBoolean("enable_stabilization", true)

        // Load RAW Capture Settings
        binding.enableRawCaptureSwitch.isChecked = prefs.getBoolean("enable_raw_capture", false)
        binding.rawFrameRateSpinner.setSelection(prefs.getInt("raw_frame_rate", 0)) // Default: 30 fps
        binding.rawQualitySpinner.setSelection(prefs.getInt("raw_quality", 0)) // Default: Maximum

        // Load Session Settings
        binding.autoExportSwitch.isChecked = prefs.getBoolean("auto_export", false)
        binding.dataRetentionSpinner.setSelection(prefs.getInt("data_retention", 0)) // Default: Keep Forever
        binding.sessionPrefixEdit.setText(prefs.getString("session_prefix", "GSR_Session"))

        // Load Sync Settings
        binding.enableTimeSyncSwitch.isChecked = prefs.getBoolean("enable_time_sync", true)
        binding.syncToleranceSpinner.setSelection(prefs.getInt("sync_tolerance", 1)) // Default: 5 ms
    }

    /**
     * Sets uplisteners configuration.
     */
    private fun setupListeners() {
        // Auto-save settings when changed
        val saveListener = { saveCurrentSettings() }

        binding.gsrSamplingRateSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                /**
                 * Executes onitemselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param parent Parameter for operation (type: AdapterView<*>?)
                 * @param view Parameter for operation (type: android.view.View?)
                 * @param position Parameter for operation (type: Int)
                 * @param id Parameter for operation (type: Long)
                 *
                 */
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long,
                ) {
                    /**
                     * Executes savelistener operation with thermal imaging domain optimization.
                     *
                     */
                    saveListener()
                }

                /**
                 * Executes onnothingselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param parent Parameter for operation (type: AdapterView<*>?)
                 *
                 */
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.enableVideoSwitch.setOnCheckedChangeListener { _, _ -> saveListener() }
        binding.enableRawCaptureSwitch.setOnCheckedChangeListener { _, _ -> saveListener() }
        binding.autoExportSwitch.setOnCheckedChangeListener { _, _ -> saveListener() }
        binding.enableTimeSyncSwitch.setOnCheckedChangeListener { _, _ -> saveListener() }
    }

    /**
     * Sets updevicemanagement configuration.
     */
    private fun setupDeviceManagement() {
        // Check permissions first before initializing GSR sensor recorder
        checkAndRequestPermissions {
            // This callback runs only when permissions are granted
            /**
             * Initializes the ializegsrsensorrecorder component for thermal imaging operations.
             *
             */
            initializeGSRSensorRecorder()
        }

        // Setup device management button listeners
        binding.scanDevicesButton.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BluetoothPermissionUtils.hasBleScanningPermissions(this)) {
                /**
                 * Executes scanfordevices operation with thermal imaging domain optimization.
                 *
                 */
                scanForDevices()
            } else {
                checkAndRequestPermissions {
                    /**
                     * Executes scanfordevices operation with thermal imaging domain optimization.
                     *
                     */
                    scanForDevices()
                }
            }
        }

        binding.connectDeviceButton.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BluetoothPermissionUtils.hasBluetoothPermissions(this)) {
                /**
                 * Executes connecttoselecteddevice operation with thermal imaging domain optimization.
                 *
                 */
                connectToSelectedDevice()
            } else {
                checkAndRequestPermissions {
                    /**
                     * Executes connecttoselecteddevice operation with thermal imaging domain optimization.
                     *
                     */
                    connectToSelectedDevice()
                }
            }
        }

        // Add permission check button for manual verification
        binding.checkPermissionsButton?.setOnClickListener {
            /**
             * Executes checkandrequestpermissions operation with thermal imaging domain optimization.
             *
             */
            checkAndRequestPermissions()
        }
    }

    /**
     * Initialize GSR sensor recorder only when permissions are available
     */
    private fun initializeGSRSensorRecorder() {
        lifecycleScope.launch {
            try {
                gsrSensorRecorder?.initialize()
                /**
                 * Executes updatedevicestatus operation with thermal imaging domain optimization.
                 *
                 */
                updateDeviceStatus()
                Log.i(TAG, "GSR sensor recorder initialized successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize GSR sensor recorder", e)
                /**
                 * Executes updatedevicestatus operation with thermal imaging domain optimization.
                 *
                 */
                updateDeviceStatus("Initialization Failed")
            }
        }
    }

    /**
     * Executes scanForDevices functionality.
     */
    /**
     * Executes scanfordevices operation with thermal imaging domain optimization.
     *
     */
    private fun scanForDevices() {
        // Double-check permissions before scanning
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!BluetoothPermissionUtils.hasBleScanningPermissions(this)) {
            Log.w(TAG, "Missing BLE scanning permissions")
            binding.deviceInfoText.text = "Bluetooth permissions required for device scanning"
            checkAndRequestPermissions {
                /**
                 * Executes scanfordevices operation with thermal imaging domain optimization.
                 *
                 */
                scanForDevices()
            }
            return
        }

        lifecycleScope.launch {
            try {
                binding.scanDevicesButton.isEnabled = false
                binding.scanDevicesButton.text = "Scanning..."
                binding.deviceInfoText.text = "Scanning for Shimmer devices..."

                Log.i(TAG, "Starting device scan with full permissions")
                val devices = gsrSensorRecorder?.getAvailableShimmerDevices() ?: emptyList()

                availableDevices.clear()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (devices.isNotEmpty()) {
                    availableDevices.addAll(devices)
                    binding.deviceInfoText.text = "Found ${devices.size} device(s). Select one and tap Connect."
                    Log.i(TAG, "Found ${devices.size} Shimmer devices: $devices")
                } else {
                    availableDevices.add("No devices found")
                    binding.deviceInfoText.text = "No devices found. Ensure Shimmer device is powered on and Bluetooth is enabled."
                    Log.w(TAG, "No Shimmer devices found during scan")
                }

                deviceAdapter.notifyDataSetChanged()
            } catch (e: SecurityException) {
                Log.e(TAG, "SecurityException during device scan - permissions may have been revoked", e)
                binding.deviceInfoText.text = "Permission error during scan. Please check app permissions."
                availableDevices.clear()
                availableDevices.add("Permission error")
                deviceAdapter.notifyDataSetChanged()

                // Re-check permissions
                /**
                 * Executes checkandrequestpermissions operation with thermal imaging domain optimization.
                 *
                 */
                checkAndRequestPermissions()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to scan for devices", e)
                binding.deviceInfoText.text = "Scan failed: ${e.message}"
                availableDevices.clear()
                availableDevices.add("Scan failed")
                deviceAdapter.notifyDataSetChanged()
            } finally {
                binding.scanDevicesButton.isEnabled = true
                binding.scanDevicesButton.text = "Scan Devices"
            }
        }
    }

    /**
     * Executes connectToSelectedDevice functionality.
     */
    /**
     * Executes connecttoselecteddevice operation with thermal imaging domain optimization.
     *
     */
    private fun connectToSelectedDevice() {
        val selectedDevice = binding.shimmerDeviceSpinner.selectedItem?.toString()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectedDevice.isNullOrEmpty() || selectedDevice == "No devices found" || selectedDevice == "Scan failed" || selectedDevice == "Permission error") {
            binding.deviceInfoText.text = "Please scan for devices first and select a valid device."
            return
        }

        // Double-check permissions before connecting
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!BluetoothPermissionUtils.hasBluetoothPermissions(this)) {
            Log.w(TAG, "Missing Bluetooth permissions for device connection")
            binding.deviceInfoText.text = "Bluetooth permissions required for device connection"
            checkAndRequestPermissions {
                /**
                 * Executes connecttoselecteddevice operation with thermal imaging domain optimization.
                 *
                 */
                connectToSelectedDevice()
            }
            return
        }

        lifecycleScope.launch {
            try {
                binding.connectDeviceButton.isEnabled = false
                binding.connectDeviceButton.text = "Connecting..."
                /**
                 * Executes updatedevicestatus operation with thermal imaging domain optimization.
                 *
                 */
                updateDeviceStatus("Connecting...")

                // Extract device address from the selected device string (assumed format: "DeviceName (MAC)")
                val deviceAddress =
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (selectedDevice.contains("(") && selectedDevice.contains(")")) {
                        selectedDevice.substringAfter("(").substringBefore(")")
                    } else {
                        selectedDevice // Use the full string as address if no parentheses
                    }

                Log.i(TAG, "Attempting to connect to device: $selectedDevice with address: $deviceAddress")
                val success = gsrSensorRecorder?.connectToShimmerDevice(deviceAddress) ?: false

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    /**
                     * Executes updatedevicestatus operation with thermal imaging domain optimization.
                     *
                     */
                    updateDeviceStatus("Connected")
                    binding.deviceInfoText.text = "Successfully connected to: $selectedDevice"
                    Log.i(TAG, "Successfully connected to Shimmer device: $selectedDevice")
                } else {
                    /**
                     * Executes updatedevicestatus operation with thermal imaging domain optimization.
                     *
                     */
                    updateDeviceStatus("Connection Failed")
                    binding.deviceInfoText.text = "Failed to connect to: $selectedDevice. Check device pairing and permissions."
                    Log.w(TAG, "Failed to connect to Shimmer device: $selectedDevice")
                }
            } catch (e: SecurityException) {
                Log.e(TAG, "SecurityException during device connection - permissions may have been revoked", e)
                /**
                 * Executes updatedevicestatus operation with thermal imaging domain optimization.
                 *
                 */
                updateDeviceStatus("Permission Error")
                binding.deviceInfoText.text = "Permission error during connection. Please check app permissions."

                // Re-check permissions
                /**
                 * Executes checkandrequestpermissions operation with thermal imaging domain optimization.
                 *
                 */
                checkAndRequestPermissions()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to connect to device", e)
                /**
                 * Executes updatedevicestatus operation with thermal imaging domain optimization.
                 *
                 */
                updateDeviceStatus("Connection Error")
                binding.deviceInfoText.text = "Connection error: ${e.message}"
            } finally {
                binding.connectDeviceButton.isEnabled = true
                binding.connectDeviceButton.text = "Connect"
            }
        }
    }

    /**
     * Executes updateDeviceStatus functionality.
     */
    /**
     * Executes updatedevicestatus operation with thermal imaging domain optimization.
     *
     * @param
     * @param statusOverride Parameter for operation (type: String? = null)
     *
     */
    private fun updateDeviceStatus(statusOverride: String? = null) {
        val status = statusOverride ?: gsrSensorRecorder?.getShimmerConnectionStatus() ?: "Unknown"

        binding.deviceStatusText.text = status

        // Update color based on status
        val color =
            when {
                status.contains("Connected", ignoreCase = true) -> android.graphics.Color.parseColor("#4caf50") // Green
                status.contains("Connecting", ignoreCase = true) -> android.graphics.Color.parseColor("#ff9800") // Orange
                status.contains("Failed", ignoreCase = true) || status.contains("Error", ignoreCase = true) ->
                    android.graphics.Color.parseColor(
                        "#f44336",
                    ) // Red
                else -> android.graphics.Color.parseColor("#ffcc00") // Yellow
            }
        binding.deviceStatusText.setTextColor(color)
    }

    /**
     * Executes saveCurrentSettings functionality.
     */
    /**
     * Executes savecurrentsettings operation with thermal imaging domain optimization.
     *
     */
    private fun saveCurrentSettings() {
        prefs.edit().apply {
            // GSR Settings
            /**
             * Executes putint operation with thermal imaging domain optimization.
             *
             */
            putInt("gsr_sampling_rate", binding.gsrSamplingRateSpinner.selectedItemPosition)
            /**
             * Executes putint operation with thermal imaging domain optimization.
             *
             */
            putInt("gsr_range", binding.gsrRangeSpinner.selectedItemPosition)
            /**
             * Executes putboolean operation with thermal imaging domain optimization.
             *
             */
            putBoolean("gsr_calibration", binding.gsrCalibrationSwitch.isChecked)

            // Video Settings
            /**
             * Executes putint operation with thermal imaging domain optimization.
             *
             */
            putInt("video_resolution", binding.videoResolutionSpinner.selectedItemPosition)
            /**
             * Executes putint operation with thermal imaging domain optimization.
             *
             */
            putInt("video_frame_rate", binding.videoFrameRateSpinner.selectedItemPosition)
            /**
             * Executes putboolean operation with thermal imaging domain optimization.
             *
             */
            putBoolean("enable_video", binding.enableVideoSwitch.isChecked)
            /**
             * Executes putboolean operation with thermal imaging domain optimization.
             *
             */
            putBoolean("enable_stabilization", binding.enableStabilizationSwitch.isChecked)

            // RAW Capture Settings
            /**
             * Executes putboolean operation with thermal imaging domain optimization.
             *
             */
            putBoolean("enable_raw_capture", binding.enableRawCaptureSwitch.isChecked)
            /**
             * Executes putint operation with thermal imaging domain optimization.
             *
             */
            putInt("raw_frame_rate", binding.rawFrameRateSpinner.selectedItemPosition)
            /**
             * Executes putint operation with thermal imaging domain optimization.
             *
             */
            putInt("raw_quality", binding.rawQualitySpinner.selectedItemPosition)

            // Session Settings
            /**
             * Executes putboolean operation with thermal imaging domain optimization.
             *
             */
            putBoolean("auto_export", binding.autoExportSwitch.isChecked)
            /**
             * Executes putint operation with thermal imaging domain optimization.
             *
             */
            putInt("data_retention", binding.dataRetentionSpinner.selectedItemPosition)
            /**
             * Executes putstring operation with thermal imaging domain optimization.
             *
             */
            putString("session_prefix", binding.sessionPrefixEdit.text.toString())

            // Sync Settings
            /**
             * Executes putboolean operation with thermal imaging domain optimization.
             *
             */
            putBoolean("enable_time_sync", binding.enableTimeSyncSwitch.isChecked)
            /**
             * Executes putint operation with thermal imaging domain optimization.
             *
             */
            putInt("sync_tolerance", binding.syncToleranceSpinner.selectedItemPosition)

            /**
             * Executes apply operation with thermal imaging domain optimization.
             *
             */
            apply()
        }
    }

    /**
     * Executes onsupportnavigateup operation with thermal imaging domain optimization.
     *
     */
    override fun onSupportNavigateUp(): Boolean {
        /**
         * Executes savecurrentsettings operation with thermal imaging domain optimization.
         *
         */
        saveCurrentSettings()
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    @Deprecated("Deprecated in Java")
    /**
     * Executes onbackpressed operation with thermal imaging domain optimization.
     *
     */
    override fun onBackPressed() {
        // Handle via OnBackPressedCallback instead
        onBackPressedDispatcher.onBackPressed()
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()

        // Cleanup GSR sensor recorder
        lifecycleScope.launch {
            try {
                gsrSensorRecorder?.cleanup()
            } catch (e: Exception) {
                Log.w(TAG, "Error during GSR sensor recorder cleanup", e)
            }
        }
    }
}
