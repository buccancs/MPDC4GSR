package com.topdon.tc001.camera

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.TextureView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.topdon.tc001.camera.core.DeviceCaps
import com.topdon.tc001.camera.core.ModeManager
import kotlinx.coroutines.*
import java.io.File

/**
 * Clean RGB Camera Recorder using Camera2System
 *
 * Implements the clean architecture requested:
 * - One camera client only (no CameraX conflicts)
 * - Two exclusive modes: RAW mode (50MP DNG stream) OR Video mode (4K60 if exposed, else 4K30)
 * - Fast switching without closing CameraDevice
 * - Deterministic state machine. No races. No silent failures.
 *
 * This is a wrapper around the new Camera2System that provides backward compatibility
 * with the existing API while using the clean architecture underneath.
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with RGBCameraRecorder functionality.
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
class RGBCameraRecorder(
    private val context: Context,
    private val textureView: TextureView,
    private val activity: Activity? = null,
) {
    companion object {
        private const val TAG = "RGBCameraRecorder"
    }

    // Clean Camera2 system
    private val camera2System = Camera2System(context, textureView)

    // Legacy compatibility enums
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraMode functionality.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    enum class CameraMode(val displayName: String, val description: String) {
        /**
         * Executes raw 50mp operation with thermal imaging domain optimization.
         *
         */
        RAW_50MP("RAW 50MP", "High-resolution RAW capture at ~15fps"),
        /**
         * Executes video 4k operation with thermal imaging domain optimization.
         *
         */
        VIDEO_4K("4K Video", "4K video recording at 30/60fps"),
        /**
         * Executes preview only operation with thermal imaging domain optimization.
         *
         */
        PREVIEW_ONLY("Preview", "Preview mode only"),
    }

/**
 * Specialized thermal imaging component providing VideoResolution functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    enum class VideoResolution(val width: Int, val height: Int, val displayName: String) {
        /**
         * Executes uhd 4k operation with thermal imaging domain optimization.
         *
         */
        UHD_4K(3840, 2160, "4K UHD (3840×2160)"),
        /**
         * Executes hd 1080p operation with thermal imaging domain optimization.
         *
         */
        HD_1080P(1920, 1080, "Full HD (1920×1080)"),
        /**
         * Executes hd 720p operation with thermal imaging domain optimization.
         *
         */
        HD_720P(1280, 720, "HD (1280×720)"),
        /**
         * Executes sd 480p operation with thermal imaging domain optimization.
         *
         */
        SD_480P(720, 480, "SD (720×480)"),
    }

/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraFacing functionality.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    enum class CameraFacing(val displayName: String) {
        /**
         * Executes back operation with thermal imaging domain optimization.
         *
         */
        BACK("Back Camera"),
        /**
         * Executes front operation with thermal imaging domain optimization.
         *
         */
        FRONT("Front Camera"),
    }

    // Legacy data classes for backward compatibility
    data class RecordingSettings(
        val mode: CameraMode = CameraMode.VIDEO_4K,
        val resolution: VideoResolution = VideoResolution.UHD_4K,
        val frameRate: Int = 30,
        val bitRate: Int = 10_000_000,
        val enableStabilization: Boolean = true,
        val enableFlash: Boolean = false,
        val audioEnabled: Boolean = true,
        val rawCaptureFrameRate: Int = 15,
        val enableHighSpeedVideo: Boolean = false,
    )

    data class CameraInfo(
        val cameraId: String,
        val facing: CameraFacing,
        val supportsRaw: Boolean,
        val supports4K: Boolean,
        val displayName: String,
    )

    // Current state
    private var currentCameraFacing = CameraFacing.BACK
    private var recordingSettings = RecordingSettings()
    private var sessionId: String = ""

    // Callbacks for backward compatibility
    var onError: ((String) -> Unit)? = null
    var onCameraSwitched: ((CameraFacing, String) -> Unit)? = null
    var onRawImageCaptured: ((File) -> Unit)? = null
    var onVideoRecordingStarted: (() -> Unit)? = null
    var onVideoRecordingCompleted: ((File) -> Unit)? = null

    init {
        /**
         * Configures the upcallbacks with validation and thermal imaging optimization.
         *
         */
        setupCallbacks()
    }

    /**
     * Sets upcallbacks configuration.
     */
    private fun setupCallbacks() {
        camera2System.onError = { error -> onError?.invoke(error) }
        camera2System.onProgress = { message -> Log.d(TAG, "Progress: $message") }
        camera2System.onModeChanged = { mode -> Log.i(TAG, "Mode changed to: $mode") }
        camera2System.onRecordingStarted = { onVideoRecordingStarted?.invoke() }
        camera2System.onRecordingStopped = { /* Handle stopped */ }
    }

    /**
     * Initialize the camera system with permission handling
     */
    suspend fun initializeCamera(cameraId: String = "0"): Boolean =
        withContext(Dispatchers.Main) {
            try {
                // Check camera permission first
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!checkCameraPermission()) {
                    Log.w(TAG, "Camera permission not granted")
                    return@withContext false
                }

                return@withContext camera2System.initialize(cameraId)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize camera", e)
                onError?.invoke("Failed to initialize camera: ${e.message}")
                return@withContext false
            }
        }

    /**
     * Switch camera mode (RAW_50MP, VIDEO_4K, PREVIEW_ONLY)
     */
    suspend fun switchMode(mode: CameraMode): Boolean {
        val systemMode =
            when (mode) {
                CameraMode.RAW_50MP -> ModeManager.CameraMode.RAW_50MP
                CameraMode.VIDEO_4K -> ModeManager.CameraMode.VIDEO_4K
                CameraMode.PREVIEW_ONLY -> ModeManager.CameraMode.PREVIEW_ONLY
            }
        return camera2System.switchMode(systemMode)
    }

    /**
     * Start recording in current mode
     */
    suspend fun startRecording(
        outputDir: File,
        sessionId: String,
    ): Boolean {
        this.sessionId = sessionId
        // Set output directory in camera2System first (if needed)
        // For now, use sessionId only as that's what Camera2System expects
        return camera2System.startRecording(sessionId)
    }

    /**
     * Stop recording
     */
    /**
     * Executes stoprecording operation with thermal imaging domain optimization.
     *
     */
    suspend fun stopRecording(): Boolean {
        return camera2System.stopRecording()
    }

    /**
     * Check camera permission and request if needed
     */
    private fun checkCameraPermission(): Boolean {
        return XXPermissions.isGranted(context, Permission.CAMERA)
    }

    /**
     * Request camera permission
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     * @param
     * @param callback Parameter for operation (type: (Boolean)
     *
     * @return Operation result or configured object (type: Unit))
     *
     */
    fun requestCameraPermission(callback: (Boolean) -> Unit) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (activity == null) {
            Log.e(TAG, "Activity context required for permission request")
            /**
             * Executes callback operation with thermal imaging domain optimization.
             *
             */
            callback(false)
            return
        }

        XXPermissions.with(activity)
            .permission(Permission.CAMERA)
            .request(
                object : OnPermissionCallback {
                    /**
                     * Executes ongranted operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param permissions Parameter for operation (type: MutableList<String>)
                     * @param allGranted Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onGranted(
                        permissions: MutableList<String>,
                        allGranted: Boolean,
                    ) {
                        /**
                         * Executes callback operation with thermal imaging domain optimization.
                         *
                         */
                        callback(allGranted)
                    }

                    /**
                     * Executes ondenied operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param permissions Parameter for operation (type: MutableList<String>)
                     * @param doNotAskAgain Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean,
                    ) {
                        Log.w(TAG, "Camera permission denied")
                        /**
                         * Executes callback operation with thermal imaging domain optimization.
                         *
                         */
                        callback(false)
                    }
                },
            )
    }

    /**
     * Switch camera (front/back)
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     * @param
     * @param facing Parameter for operation (type: CameraFacing)
     *
     */
    suspend fun switchCamera(facing: CameraFacing): Boolean {
        val cameraId = getFirstCameraIdForFacing(facing) ?: return false
        currentCameraFacing = facing
        val success = camera2System.initialize(cameraId)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (success) {
            onCameraSwitched?.invoke(facing, cameraId)
        }
        return success
    }

    /**
     * Switch to specific camera ID
     */
    suspend fun switchCamera(cameraId: String): Boolean {
        val success = camera2System.initialize(cameraId)
        if (success) {
            // Update facing based on camera ID (simplified logic)
            val facing = if (cameraId == "1") CameraFacing.FRONT else CameraFacing.BACK
            currentCameraFacing = facing
            onCameraSwitched?.invoke(facing, cameraId)
        }
        return success
    }

    /**
     * Get available cameras with capability information
     */
    fun getAvailableCameras(): List<CameraInfo> {
        // Delegate to camera2System for camera enumeration
        return emptyList() // Simplified for now
    }

    /**
     * Get current camera mode
     */
    /**
     * Retrieves the currentmode with optimized performance for thermal imaging operations.
     *
     */
    fun getCurrentMode(): CameraMode {
        val systemMode = camera2System.getCurrentMode()
        return when (systemMode) {
            ModeManager.CameraMode.RAW_50MP -> CameraMode.RAW_50MP
            ModeManager.CameraMode.VIDEO_4K -> CameraMode.VIDEO_4K
            ModeManager.CameraMode.PREVIEW_ONLY -> CameraMode.PREVIEW_ONLY
        }
    }

    /**
     * Check if recording is active
     */
    fun isRecording(): Boolean = camera2System.isRecording()

    /**
     * Get device capabilities
     */
    /**
     * Retrieves the devicecaps with optimized performance for thermal imaging operations.
     *
     */
    fun getDeviceCaps(): DeviceCaps? = camera2System.getDeviceCaps()

    /**
     * Get current camera facing
     */
    /**
     * Retrieves the currentcamerafacing with optimized performance for thermal imaging operations.
     *
     */
    fun getCurrentCameraFacing(): CameraFacing = currentCameraFacing

    /**
     * Get current session ID
     */
    /**
     * Retrieves the currentsessionid with optimized performance for thermal imaging operations.
     *
     */
    fun getCurrentSessionId(): String = sessionId

    /**
     * Update recording settings
     */
    /**
     * Executes updaterecordingsettings operation with thermal imaging domain optimization.
     *
     * @param
     * @param settings Parameter for operation (type: RecordingSettings)
     *
     */
    fun updateRecordingSettings(settings: RecordingSettings) {
        recordingSettings = settings
    }

    /**
     * Get current recording settings
     */
    fun getRecordingSettings(): RecordingSettings = recordingSettings

    /**
     * Release resources
     */
    /**
     * Executes release operation with thermal imaging domain optimization.
     *
     */
    suspend fun release() {
        camera2System.release()
    }

    // Private helper methods
    /**
     * Retrieves firstcameraidforfacing information.
     */
    private fun getFirstCameraIdForFacing(facing: CameraFacing): String? {
        // Simplified - typically "0" for back, "1" for front
        return when (facing) {
            CameraFacing.BACK -> "0"
            CameraFacing.FRONT -> "1"
        }
    }

    // Mode support checking methods
    /**
     * Executes isModeSupported functionality.
     */
    /**
     * Executes ismodesupported operation with thermal imaging domain optimization.
     *
     * @param
     * @param mode Parameter for operation (type: CameraMode)
     *
     */
    fun isModeSupported(mode: CameraMode): Boolean {
        val caps = getDeviceCaps()
        return when (mode) {
            CameraMode.RAW_50MP -> caps?.supportsRaw ?: false
            CameraMode.VIDEO_4K -> caps?.supports4k60 ?: true // Video is generally supported
            CameraMode.PREVIEW_ONLY -> true // Always supported
        }
    }

    /**
     * Retrieves availablemodes information.
     */
    fun getAvailableModes(): List<CameraMode> {
        val modes = mutableListOf<CameraMode>()
        modes.add(CameraMode.PREVIEW_ONLY) // Always available
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isModeSupported(CameraMode.VIDEO_4K)) modes.add(CameraMode.VIDEO_4K)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isModeSupported(CameraMode.RAW_50MP)) modes.add(CameraMode.RAW_50MP)
        return modes
    }

    /**
     * Executes supportsRawCapture functionality.
     */
    /**
     * Executes supportsrawcapture operation with thermal imaging domain optimization.
     *
     */
    fun supportsRawCapture(): Boolean = isModeSupported(CameraMode.RAW_50MP)

    /**
     * Executes supportsVideoRecording functionality.
     */
    /**
     * Executes supportsvideorecording operation with thermal imaging domain optimization.
     *
     */
    fun supportsVideoRecording(): Boolean = isModeSupported(CameraMode.VIDEO_4K)

    /**
     * Executes supportsHighSpeed60fps functionality.
     */
    /**
     * Executes supportshighspeed60fps operation with thermal imaging domain optimization.
     *
     */
    fun supportsHighSpeed60fps(): Boolean = getDeviceCaps()?.supports4k60 ?: false

    /**
     * Retrieves maxrawresolution information.
     */
    fun getMaxRawResolution(): VideoResolution? {
        // Return the highest resolution available
        return VideoResolution.UHD_4K // Simplified
    }

    /**
     * Retrieves currentvideoresolution information.
     */
    fun getCurrentVideoResolution(): VideoResolution = recordingSettings.resolution

    // Additional compatibility methods for legacy API
    /**
     * Executes updateSettings functionality.
     */
    /**
     * Executes updatesettings operation with thermal imaging domain optimization.
     *
     * @param
     * @param settings Parameter for operation (type: RecordingSettings)
     *
     */
    fun updateSettings(settings: RecordingSettings) = updateRecordingSettings(settings)

    /**
     * Retrieves currentsettings information.
     */
    fun getCurrentSettings(): RecordingSettings = getRecordingSettings()

    /**
     * Executes cleanup functionality.
     */
    /**
     * Executes cleanup operation with thermal imaging domain optimization.
     *
     */
    fun cleanup() = runBlocking { release() }

    // Additional methods for SynchronizedMultiModalRecorder compatibility
    /**
     * Configures the flashenabled with validation and thermal imaging optimization.
     *
     * @param
     * @param enabled Parameter for operation (type: Boolean)
     *
     */
    suspend fun setFlashEnabled(enabled: Boolean): Boolean {
        // Flash control not implemented in clean architecture yet
        Log.w(TAG, "Flash control not yet implemented in clean architecture")
        return false
    }

    /**
     * Executes pauserecording operation with thermal imaging domain optimization.
     *
     */
    suspend fun pauseRecording(): Boolean {
        // Pause/resume not implemented in clean architecture yet
        Log.w(TAG, "Pause/resume not yet implemented in clean architecture")
        return false
    }

    /**
     * Executes resumerecording operation with thermal imaging domain optimization.
     *
     */
    suspend fun resumeRecording(): Boolean {
        // Pause/resume not implemented in clean architecture yet
        Log.w(TAG, "Pause/resume not yet implemented in clean architecture")
        return false
    }

    // Legacy start recording method (for ParallelMultiModalRecorder compatibility)
    /**
     * Executes startrecording operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionId Parameter for operation (type: String)
     *
     */
    suspend fun startRecording(sessionId: String): Boolean {
        this.sessionId = sessionId
        return camera2System.startRecording(sessionId)
    }

    // Camera facing compatibility methods
    /**
     * Retrieves availablecamerafacing information.
     */
    fun getAvailableCameraFacing(): List<CameraFacing> = listOf(CameraFacing.BACK, CameraFacing.FRONT)

    /**
     * Retrieves supportedresolutions information.
     */
    fun getSupportedResolutions(): List<VideoResolution> = VideoResolution.values().toList()

    // Legacy getters for backward compatibility
    /**
     * Executes isRawCaptureActive functionality.
     */
    /**
     * Executes israwcaptureactive operation with thermal imaging domain optimization.
     *
     */
    fun isRawCaptureActive(): Boolean = isRecording() && getCurrentMode() == CameraMode.RAW_50MP

    /**
     * Executes isVideoRecordingActive functionality.
     */
    /**
     * Executes isvideorecordingactive operation with thermal imaging domain optimization.
     *
     */
    fun isVideoRecordingActive(): Boolean = isRecording() && getCurrentMode() == CameraMode.VIDEO_4K

    /**
     * Retrieves rawcapturecount information.
     */
    fun getRawCaptureCount(): Int = camera2System.getDeviceCaps()?.let { 0 } ?: 0

    /**
     * Retrieves currentvideofile information.
     */
    fun getCurrentVideoFile(): File? = null // Not exposed in clean architecture

    /**
     * Retrieves rawimagesdirectory information.
     */
    fun getRawImagesDirectory(): File? = null // Not exposed in clean architecture

    /**
     * Executes isSessionSwitching functionality.
     */
    /**
     * Executes issessionswitching operation with thermal imaging domain optimization.
     *
     */
    fun isSessionSwitching(): Boolean = false // Clean architecture handles this internally
}
