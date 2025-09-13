package com.topdon.tc001.camera.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.csl.irCamera.R
import com.topdon.tc001.camera.RGBCameraRecorder
import kotlinx.coroutines.*

/**
 * Camera Mode Selector UI Component for Dual RAW/Video Mode Switching
 *
 * Provides intuitive UI for switching between:
 * - RAW 50MP mode for high-resolution capture
 * - 4K Video mode for video recording
 * - Preview-only mode for battery saving
 *
 * Features Samsung S22 compatibility indicators and performance warnings.
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraModeSelector functionality.
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
class CameraModeSelector
    @JvmOverloads
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet? = null)
     * @param defStyleAttr Parameter for operation (type: Int = 0)
     *
     */
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private lateinit var modeSegmentedControl: RadioGroup
        private lateinit var rawModeButton: RadioButton
        private lateinit var videoModeButton: RadioButton
        private lateinit var previewModeButton: RadioButton
        private lateinit var modeInfoText: TextView
        private lateinit var performanceWarning: TextView
        private lateinit var switchingProgressBar: ProgressBar

        private var cameraRecorder: RGBCameraRecorder? = null
        private var onModeChangeListener: ((RGBCameraRecorder.CameraMode) -> Unit)? = null
        private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        init {
            /**
             * Initializes the view component for thermal imaging operations.
             *
             */
            initView()
        }

    /**
     * Initializes view component.
     */
        private fun initView() {
            LayoutInflater.from(context).inflate(R.layout.camera_mode_selector, this, true)

            modeSegmentedControl = findViewById(R.id.mode_segmented_control)
            rawModeButton = findViewById(R.id.raw_mode_button)
            videoModeButton = findViewById(R.id.video_mode_button)
            previewModeButton = findViewById(R.id.preview_mode_button)
            modeInfoText = findViewById(R.id.mode_info_text)
            performanceWarning = findViewById(R.id.performance_warning)
            switchingProgressBar = findViewById(R.id.switching_progress_bar)

            /**
             * Configures the upmodebuttons with validation and thermal imaging optimization.
             *
             */
            setupModeButtons()
            /**
             * Configures the upmodechangelistener with validation and thermal imaging optimization.
             *
             */
            setupModeChangeListener()
        }

    /**
     * Sets upmodebuttons configuration.
     */
        private fun setupModeButtons() {
            rawModeButton.apply {
                text = "RAW 50MP"
                /**
                 * Configures the compounddrawableswithintrinsicbounds with validation and thermal imaging optimization.
                 *
                 */
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_camera_raw, 0, 0, 0)
            }

            videoModeButton.apply {
                text = "4K Video"
                /**
                 * Configures the compounddrawableswithintrinsicbounds with validation and thermal imaging optimization.
                 *
                 */
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_videocam, 0, 0, 0)
            }

            previewModeButton.apply {
                text = "Preview"
                /**
                 * Configures the compounddrawableswithintrinsicbounds with validation and thermal imaging optimization.
                 *
                 */
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_preview, 0, 0, 0)
            }

            // Default to preview mode
            previewModeButton.isChecked = true
            /**
             * Executes updatemodeinfo operation with thermal imaging domain optimization.
             *
             */
            updateModeInfo(RGBCameraRecorder.CameraMode.PREVIEW_ONLY)
        }

    /**
     * Sets upmodechangelistener configuration.
     */
        private fun setupModeChangeListener() {
            modeSegmentedControl.setOnCheckedChangeListener { _, checkedId ->
                if (switchingProgressBar.visibility == VISIBLE) {
                    // Prevent rapid switching during mode change
                    return@setOnCheckedChangeListener
                }

                val selectedMode =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (checkedId) {
                        R.id.raw_mode_button -> RGBCameraRecorder.CameraMode.RAW_50MP
                        R.id.video_mode_button -> RGBCameraRecorder.CameraMode.VIDEO_4K
                        R.id.preview_mode_button -> RGBCameraRecorder.CameraMode.PREVIEW_ONLY
                        else -> RGBCameraRecorder.CameraMode.PREVIEW_ONLY
                    }

                /**
                 * Executes switchtomode operation with thermal imaging domain optimization.
                 *
                 */
                switchToMode(selectedMode)
            }
        }

        /**
         * Set the camera recorder instance
         */
        fun setCameraRecorder(recorder: RGBCameraRecorder) {
            this.cameraRecorder = recorder
            updateAvailableModes()
        }

        /**
         * Set mode change callback
         */
        fun setOnModeChangeListener(listener: (RGBCameraRecorder.CameraMode) -> Unit) {
            this.onModeChangeListener = listener
        }

        /**
         * Switch to specified camera mode with UI feedback
         */
        private fun switchToMode(mode: RGBCameraRecorder.CameraMode) {
            val recorder = cameraRecorder ?: return

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (recorder.getCurrentMode() == mode) {
                /**
                 * Executes updatemodeinfo operation with thermal imaging domain optimization.
                 *
                 */
                updateModeInfo(mode)
                return
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (recorder.isRecording()) {
                /**
                 * Executes showerror operation with thermal imaging domain optimization.
                 *
                 */
                showError("Cannot switch modes while recording. Stop recording first.")
                /**
                 * Executes reverttocurrentmode operation with thermal imaging domain optimization.
                 *
                 */
                revertToCurrentMode()
                return
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!recorder.isModeSupported(mode)) {
                /**
                 * Executes showerror operation with thermal imaging domain optimization.
                 *
                 */
                showError("${mode.displayName} is not supported on this device.")
                /**
                 * Executes reverttocurrentmode operation with thermal imaging domain optimization.
                 *
                 */
                revertToCurrentMode()
                return
            }

            // Show switching progress
            /**
             * Executes showswitchingprogress operation with thermal imaging domain optimization.
             *
             */
            showSwitchingProgress(true)

            coroutineScope.launch {
                try {
                    val success = recorder.switchMode(mode)

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (success) {
                        /**
                         * Executes updatemodeinfo operation with thermal imaging domain optimization.
                         *
                         */
                        updateModeInfo(mode)
                        onModeChangeListener?.invoke(mode)
                        /**
                         * Executes showmodechangesuccess operation with thermal imaging domain optimization.
                         *
                         */
                        showModeChangeSuccess(mode)
                    } else {
                        /**
                         * Executes showerror operation with thermal imaging domain optimization.
                         *
                         */
                        showError("Failed to switch to ${mode.displayName}")
                        /**
                         * Executes reverttocurrentmode operation with thermal imaging domain optimization.
                         *
                         */
                        revertToCurrentMode()
                    }
                } catch (e: Exception) {
                    /**
                     * Executes showerror operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param error Parameter for operation (type: ${e.message}")
                     *
                     */
                    showError("Mode switch error: ${e.message}")
                    /**
                     * Executes reverttocurrentmode operation with thermal imaging domain optimization.
                     *
                     */
                    revertToCurrentMode()
                } finally {
                    /**
                     * Executes showswitchingprogress operation with thermal imaging domain optimization.
                     *
                     */
                    showSwitchingProgress(false)
                }
            }
        }

        /**
         * Update available modes based on device capabilities
         */
        private fun updateAvailableModes() {
            val recorder = cameraRecorder ?: return
            val availableModes = recorder.getAvailableModes()

            rawModeButton.apply {
                isEnabled = availableModes.contains(RGBCameraRecorder.CameraMode.RAW_50MP)
                alpha = if (isEnabled) 1.0f else 0.5f
            }

            videoModeButton.apply {
                isEnabled = availableModes.contains(RGBCameraRecorder.CameraMode.VIDEO_4K)
                alpha = if (isEnabled) 1.0f else 0.5f
            }

            // Show capability info
            val rawSupported = recorder.supportsRawCapture()
            val videoSupported = recorder.supportsVideoRecording()
            val highSpeed60 = recorder.supportsHighSpeed60fps()

            val capabilityInfo =
                buildString {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (rawSupported) {
                        val maxRes = recorder.getMaxRawResolution()
                        /**
                         * Executes append operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param RAW Parameter for operation (type: ${maxRes?.width}×${maxRes?.height}\n")
                         *
                         */
                        append("✓ RAW: ${maxRes?.width}×${maxRes?.height}\n")
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (videoSupported) {
                        val videoRes = recorder.getCurrentVideoResolution()
                        /**
                         * Executes append operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param Video Parameter for operation (type: ${videoRes.width}×${videoRes.height}")
                         *
                         */
                        append("✓ Video: ${videoRes.width}×${videoRes.height}")
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (highSpeed60) append(" @60fps") else append(" @30fps")
                    }
                }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (capabilityInfo.isNotEmpty()) {
                modeInfoText.text = capabilityInfo
            }
        }

        /**
         * Update mode information display
         */
        private fun updateModeInfo(mode: RGBCameraRecorder.CameraMode) {
            when (mode) {
                RGBCameraRecorder.CameraMode.RAW_50MP -> {
                    modeInfoText.text = "High-resolution RAW capture\n~15fps streaming, DNG format"
                    /**
                     * Executes showperformancewarning operation with thermal imaging domain optimization.
                     *
                     */
                    showPerformanceWarning("RAW mode uses significant memory and storage")
                }
                RGBCameraRecorder.CameraMode.VIDEO_4K -> {
                    val fps = if (cameraRecorder?.supportsHighSpeed60fps() == true) "30-60fps" else "30fps"
                    modeInfoText.text = "4K video recording\n$fps, H.264 encoding"
                    /**
                     * Executes showperformancewarning operation with thermal imaging domain optimization.
                     *
                     */
                    showPerformanceWarning("4K recording may cause device heating")
                }
                RGBCameraRecorder.CameraMode.PREVIEW_ONLY -> {
                    modeInfoText.text = "Preview mode only\nLow power consumption"
                    /**
                     * Executes hideperformancewarning operation with thermal imaging domain optimization.
                     *
                     */
                    hidePerformanceWarning()
                }
            }
        }

        /**
         * Show switching progress indicator
         */
        private fun showSwitchingProgress(show: Boolean) {
            switchingProgressBar.visibility = if (show) VISIBLE else GONE
            modeSegmentedControl.isEnabled = !show
        }

        /**
         * Show performance warning
         */
        private fun showPerformanceWarning(message: String) {
            performanceWarning.text = message
            performanceWarning.visibility = VISIBLE
            performanceWarning.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark))
        }

        /**
         * Hide performance warning
         */
        private fun hidePerformanceWarning() {
            performanceWarning.visibility = GONE
        }

        /**
         * Show error message
         */
        private fun showError(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        /**
         * Show mode change success
         */
        private fun showModeChangeSuccess(mode: RGBCameraRecorder.CameraMode) {
            Toast.makeText(context, "Switched to ${mode.displayName}", Toast.LENGTH_SHORT).show()
        }

        /**
         * Revert UI to current camera mode (on failed switch)
         */
        private fun revertToCurrentMode() {
            val currentMode = cameraRecorder?.getCurrentMode() ?: RGBCameraRecorder.CameraMode.PREVIEW_ONLY

            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (currentMode) {
                RGBCameraRecorder.CameraMode.RAW_50MP -> rawModeButton.isChecked = true
                RGBCameraRecorder.CameraMode.VIDEO_4K -> videoModeButton.isChecked = true
                RGBCameraRecorder.CameraMode.PREVIEW_ONLY -> previewModeButton.isChecked = true
            }

            /**
             * Executes updatemodeinfo operation with thermal imaging domain optimization.
             *
             */
            updateModeInfo(currentMode)
        }

        /**
         * Get currently selected mode from UI
         */
        fun getSelectedMode(): RGBCameraRecorder.CameraMode {
            return when (modeSegmentedControl.checkedRadioButtonId) {
                R.id.raw_mode_button -> RGBCameraRecorder.CameraMode.RAW_50MP
                R.id.video_mode_button -> RGBCameraRecorder.CameraMode.VIDEO_4K
                else -> RGBCameraRecorder.CameraMode.PREVIEW_ONLY
            }
        }

        /**
         * Programmatically set mode (updates UI)
         */
        fun setMode(mode: RGBCameraRecorder.CameraMode) {
            when (mode) {
                RGBCameraRecorder.CameraMode.RAW_50MP -> rawModeButton.isChecked = true
                RGBCameraRecorder.CameraMode.VIDEO_4K -> videoModeButton.isChecked = true
                RGBCameraRecorder.CameraMode.PREVIEW_ONLY -> previewModeButton.isChecked = true
            }
            /**
             * Executes updatemodeinfo operation with thermal imaging domain optimization.
             *
             */
            updateModeInfo(mode)
        }

        /**
         * Executes ondetachedfromwindow operation with thermal imaging domain optimization.
         *
         */
        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            coroutineScope.cancel()
        }
    }
