package com.topdon.tc001.camera.integration

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.TextureView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.csl.irCamera.R
import com.topdon.tc001.camera.RGBCameraRecorder
import com.topdon.tc001.camera.ui.CameraModeSelector
import kotlinx.coroutines.launch

/**
 * Demo Activity for Dual-Mode Camera System Integration
 *
 * Demonstrates the enhanced RGBCameraRecorder with:
 * - RAW 50MP capture mode
 * - 4K video recording mode
 * - Fast session switching
 * - Samsung S22 optimizations
 * - CameraModeSelector UI integration
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with DualModeCameraActivity functionality.
 *
 * Provides advanced camera functionality for thermal imaging capture,
 * including temperature measurement and pseudo color visualization.
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
class DualModeCameraActivity : AppCompatActivity() {
    private lateinit var textureView: TextureView
    private lateinit var cameraModeSelector: CameraModeSelector
    private var rgbCameraRecorder: RGBCameraRecorder? = null

    // Camera permission launcher
    private val requestPermissionLauncher =
        /**
         * Executes registerforactivityresult operation with thermal imaging domain optimization.
         *
         * @param
         * @param isGranted Parameter for operation (type: Boolean ->)
         *
         */
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isGranted) {
                /**
                 * Initializes the ializecamera component for thermal imaging operations.
                 *
                 */
                initializeCamera()
            } else {
                Toast.makeText(this, "Camera permission required for dual-mode system", Toast.LENGTH_LONG).show()
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
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
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(R.layout.activity_dual_mode_camera)

        // Initialize views
        textureView = findViewById(R.id.texture_view)
        cameraModeSelector = findViewById(R.id.camera_mode_selector)

        // Get initial mode from intent
        val initialMode = intent.getStringExtra("INITIAL_MODE") ?: "VIDEO_4K"
        val enableSamsungOptimizations = intent.getBooleanExtra("ENABLE_SAMSUNG_OPTIMIZATIONS", true)

        // Set up mode selector
        /**
         * Configures the upmodeselector with validation and thermal imaging optimization.
         *
         */
        setupModeSelector(initialMode)

        // Check permissions and initialize
        /**
         * Manages thermal camera operations with hardware-optimized performance and error handling.
         *
         */
        checkCameraPermission()
    }

    /**
     * Executes checkCameraPermission functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED -> {
                /**
                 * Initializes the ializecamera component for thermal imaging operations.
                 *
                 */
                initializeCamera()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    /**
     * Initializes ializecamera component.
     */
    private fun initializeCamera() {
        try {
            // Initialize enhanced RGBCameraRecorder with dual-mode support
            rgbCameraRecorder = RGBCameraRecorder(this, textureView)

            // Configure initial settings
            val settings =
                RGBCameraRecorder.RecordingSettings(
                    mode = RGBCameraRecorder.CameraMode.VIDEO_4K,
                    resolution = RGBCameraRecorder.VideoResolution.UHD_4K,
                    frameRate = 30,
                    bitRate = 10_000_000,
                    enableStabilization = true,
                    enableHighSpeedVideo = false, // Start conservative for Samsung compatibility
                )

            rgbCameraRecorder?.updateRecordingSettings(settings)

            Toast.makeText(this, "Dual-mode camera system initialized", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to initialize camera: ${e.message}", Toast.LENGTH_LONG).show()
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        }
    }

    /**
     * Sets upmodeselector configuration.
     */
    private fun setupModeSelector(initialMode: String) {
        // Set initial mode
        val mode =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (initialMode) {
                "RAW_50MP" -> RGBCameraRecorder.CameraMode.RAW_50MP
                "VIDEO_4K" -> RGBCameraRecorder.CameraMode.VIDEO_4K
                else -> RGBCameraRecorder.CameraMode.PREVIEW_ONLY
            }

        // Configure mode selector callback
        cameraModeSelector.setOnModeChangeListener { newMode ->
            lifecycleScope.launch {
                /**
                 * Manages thermal camera operations with hardware-optimized performance and error handling.
                 *
                 */
                switchCameraMode(newMode)
            }
        }

        // Set initial mode in selector
        cameraModeSelector.setMode(mode)
    }

    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     * @param
     * @param newMode Parameter for operation (type: RGBCameraRecorder.CameraMode)
     *
     */
    private suspend fun switchCameraMode(newMode: RGBCameraRecorder.CameraMode) {
        try {
            val success = rgbCameraRecorder?.switchMode(newMode) ?: false
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (success) {
                Toast.makeText(this, "Switched to ${newMode.displayName}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to switch to ${newMode.displayName}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Mode switch error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        rgbCameraRecorder?.cleanup()
    }
}
