package com.topdon.tc001.camera

import android.content.Context
import android.util.Log
import android.view.TextureView
import com.topdon.gsr.util.TimeUtil
import com.topdon.tc001.gsr.EnhancedThermalRecorder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * Specialized thermal imaging component providing SynchronizedMultiModalRecorder functionality for the IRCamera system.
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
class SynchronizedMultiModalRecorder(
    private val context: Context,
    private val thermalRecorder: EnhancedThermalRecorder,
    private val rgbTextureView: TextureView,
) {
    companion object {
        private const val TAG = "SynchronizedRecorder"
    }

    // Recording components
    private var rgbCameraRecorder: RGBCameraRecorder? = null
    private var currentSessionId: String? = null
    private var isRecording = false

    // Synchronized file outputs
    data class RecordingSession(
        val sessionId: String,
        val startTimestamp: Long,
        val endTimestamp: Long? = null,
        val thermalVideoFile: File? = null,
        val rgbVideoFile: File? = null,
        val gsrDataFile: File? = null,
        val syncMarksFile: File? = null,
        val sessionMetadata: File? = null,
    )

    // Callbacks for recording events
    var onRecordingStarted: ((RecordingSession) -> Unit)? = null
    var onRecordingStopped: ((RecordingSession) -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    /**
     * Initialize all recording components
     */
    fun initialize() {
        rgbCameraRecorder =
            RGBCameraRecorder(context, rgbTextureView).apply {
                /**
                 * Initializes the ialize component for thermal imaging operations.
                 *
                 */
                initialize()

                onRecordingStarted = {
                    Log.d(TAG, "RGB recording started")
                }

                onRecordingStopped = { videoFile ->
                    Log.d(TAG, "RGB recording stopped: ${videoFile?.toString()}")
                }

                onError = { error ->
                    Log.e(TAG, "RGB camera error: $error")
                    this@SynchronizedMultiModalRecorder.onError?.invoke("RGB Camera: $error")
                }
            }
    }

    /**
     * Start synchronized multi-modal recording
     */
    fun startSynchronizedRecording(
        sessionId: String? = null,
        rgbSettings: RGBCameraRecorder.RecordingSettings = RGBCameraRecorder.RecordingSettings(),
    ): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            Log.w(TAG, "Already recording")
            return false
        }

        try {
            // Generate unified session ID with Samsung S22 ground truth timing
            val unifiedSessionId = sessionId ?: TimeUtil.generateSessionId("MultiModal")
            val synchronizedTimestamp = TimeUtil.getSynchronizedTimestamp()
            currentSessionId = unifiedSessionId

            Log.i(TAG, "Starting synchronized multi-modal recording with unified timestamp: $synchronizedTimestamp")

            // 1. Start GSR recording first (fastest to initialize) - using coroutine for async call
            var gsrStarted = false
            GlobalScope.launch {
                gsrStarted = thermalRecorder.startRecording(unifiedSessionId, null, true)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!gsrStarted) {
                    Log.w(TAG, "GSR recording failed to start, continuing with thermal+RGB only")
                }
            }

            // 2. Start RGB camera recording with same session ID
            rgbCameraRecorder?.updateSettings(rgbSettings)
            val rgbStarted = runBlocking { rgbCameraRecorder?.startRecording(unifiedSessionId) } ?: false
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!rgbStarted) {
                Log.w(TAG, "RGB recording failed to start")
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (gsrStarted) {
                    thermalRecorder.stopRecording()
                }
                return false
            }

            // 3. Add synchronized start marker with exact timestamp coordination
            thermalRecorder.triggerSyncEvent(
                "MULTIMODAL_START",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "sync_timestamp" to synchronizedTimestamp.toString(),
                    "unified_time_base" to "samsung_s22_ground_truth",
                    "session_id" to unifiedSessionId,
                    "thermal_recording" to "active",
                    "rgb_recording" to "active",
                    "gsr_recording" to if (gsrStarted) "active" else "unavailable",
                    "recording_mode" to "synchronized_trimodal",
                ),
            )

            isRecording = true

            // Create session data
            val session =
                /**
                 * Executes recordingsession operation with thermal imaging domain optimization.
                 *
                 */
                RecordingSession(
                    sessionId = unifiedSessionId,
                    startTimestamp = synchronizedTimestamp,
                    rgbVideoFile = rgbCameraRecorder?.getCurrentVideoFile(),
                )

            onRecordingStarted?.invoke(session)

            Log.i(TAG, "Synchronized multi-modal recording started successfully: $unifiedSessionId")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start synchronized recording", e)
            onError?.invoke("Failed to start synchronized recording: ${e.message}")

            // Cleanup on failure
            /**
             * Executes cleanup operation with thermal imaging domain optimization.
             *
             */
            cleanup()
            return false
        }
    }

    /**
     * Stop synchronized multi-modal recording
     */
    fun stopSynchronizedRecording(): RecordingSession? {
        if (!isRecording || currentSessionId == null) {
            Log.w(TAG, "Not currently recording")
            return null
        }

        try {
            val stopTimestamp = TimeUtil.getSynchronizedTimestamp()
            val sessionId = currentSessionId!!

            Log.i(TAG, "Stopping synchronized multi-modal recording at timestamp: $stopTimestamp")

            // Add synchronized stop marker
            thermalRecorder.triggerSyncEvent(
                "MULTIMODAL_STOP",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "sync_timestamp" to stopTimestamp.toString(),
                    "session_id" to sessionId,
                    "unified_time_base" to "samsung_s22_ground_truth",
                    "stop_reason" to "user_initiated",
                ),
            )

            // Stop all recording components simultaneously
            val gsrSession = thermalRecorder.stopRecording()
            val rgbVideoFile = runBlocking { rgbCameraRecorder?.stopRecording() }

            isRecording = false

            // Create final session with all output files
            val finalSession =
                /**
                 * Executes recordingsession operation with thermal imaging domain optimization.
                 *
                 */
                RecordingSession(
                    sessionId = sessionId,
                    startTimestamp = gsrSession?.startTime ?: System.currentTimeMillis(),
                    endTimestamp = stopTimestamp,
                    rgbVideoFile = null, // Boolean return type doesn't match File expected
                    gsrDataFile = gsrSession?.let { File(thermalRecorder.getSessionDirectory(), "signals.csv") },
                    syncMarksFile = gsrSession?.let { File(thermalRecorder.getSessionDirectory(), "sync_marks.csv") },
                    sessionMetadata = gsrSession?.let { File(thermalRecorder.getSessionDirectory(), "session_metadata.json") },
                )

            currentSessionId = null
            onRecordingStopped?.invoke(finalSession)

            Log.i(TAG, "Synchronized multi-modal recording completed: $sessionId")
            Log.i(TAG, "Session files: RGB=${if (rgbVideoFile == true) "completed" else "failed"}, GSR=${gsrSession?.sampleCount} samples")

            return finalSession
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop synchronized recording", e)
            onError?.invoke("Failed to stop synchronized recording: ${e.message}")
            /**
             * Executes cleanup operation with thermal imaging domain optimization.
             *
             */
            cleanup()
            return null
        }
    }

    /**
     * Add synchronized event marker across all recording streams
     */
    fun addSyncEvent(
        eventName: String,
        metadata: Map<String, String> = emptyMap(),
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isRecording) return

        val timestamp = TimeUtil.getSynchronizedTimestamp()
        val eventData =
            metadata.toMutableMap().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("sync_timestamp", timestamp.toString())
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("event_name", eventName)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("session_id", currentSessionId ?: "unknown")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("timing_source", "samsung_s22_ground_truth")
            }

        thermalRecorder.triggerSyncEvent("CROSS_MODAL_EVENT_$eventName", eventData)

        Log.d(TAG, "Added synchronized event: $eventName at timestamp $timestamp")
    }

    /**
     * Switch RGB camera (front/back)
     */
    fun switchRGBCamera(): RGBCameraRecorder.CameraFacing? {
        val currentFacing = rgbCameraRecorder?.getCurrentCameraFacing()
        val newFacing =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (currentFacing == RGBCameraRecorder.CameraFacing.BACK) {
                RGBCameraRecorder.CameraFacing.FRONT
            } else {
                RGBCameraRecorder.CameraFacing.BACK
            }

        // Switch to the new facing
        val success = runBlocking { rgbCameraRecorder?.switchCamera(newFacing) ?: false }
        return if (success) newFacing else currentFacing
    }

    /**
     * Update RGB recording settings
     */
    fun updateRGBSettings(settings: RGBCameraRecorder.RecordingSettings) {
        rgbCameraRecorder?.updateSettings(settings)

        // Add sync event to mark settings change
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            /**
             * Executes addsyncevent operation with thermal imaging domain optimization.
             *
             */
            addSyncEvent(
                "RGB_SETTINGS_CHANGED",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "resolution" to settings.resolution.displayName,
                    "frame_rate" to settings.frameRate.toString(),
                    "stabilization" to settings.enableStabilization.toString(),
                ),
            )
        }
    }

    /**
     * Enable/disable RGB flash
     */
    /**
     * Configures the rgbflash with validation and thermal imaging optimization.
     *
     * @param
     * @param enabled Parameter for operation (type: Boolean)
     *
     */
    fun setRGBFlash(enabled: Boolean) {
        runBlocking { rgbCameraRecorder?.setFlashEnabled(enabled) }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            /**
             * Executes addsyncevent operation with thermal imaging domain optimization.
             *
             */
            addSyncEvent(
                "RGB_FLASH_TOGGLE",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "flash_enabled" to enabled.toString(),
                ),
            )
        }
    }

    /**
     * Pause/resume RGB recording (Android N+)
     */
    fun pauseRGBRecording() {
        runBlocking { rgbCameraRecorder?.pauseRecording() }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            /**
             * Executes addsyncevent operation with thermal imaging domain optimization.
             *
             */
            addSyncEvent("RGB_RECORDING_PAUSED")
        }
    }

    /**
     * Executes resumeRGBRecording functionality.
     */
    /**
     * Executes resumergbrecording operation with thermal imaging domain optimization.
     *
     */
    fun resumeRGBRecording() {
        runBlocking { rgbCameraRecorder?.resumeRecording() }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            /**
             * Executes addsyncevent operation with thermal imaging domain optimization.
             *
             */
            addSyncEvent("RGB_RECORDING_RESUMED")
        }
    }

    /**
     * Get current recording state
     */
    fun isRecording() = isRecording

    /**
     * Retrieves currentsessionid information.
     */
    fun getCurrentSessionId() = currentSessionId

    /**
     * Retrieves currentrgbsettings information.
     */
    fun getCurrentRGBSettings() = rgbCameraRecorder?.getCurrentSettings()

    /**
     * Retrieves rgbcamerafacing information.
     */
    fun getRGBCameraFacing() = rgbCameraRecorder?.getCurrentCameraFacing()

    /**
     * Retrieves availablergbcameras information.
     */
    fun getAvailableRGBCameras() = rgbCameraRecorder?.getAvailableCameraFacing() ?: emptyList()

    /**
     * Retrieves supportedrgbresolutions information.
     */
    fun getSupportedRGBResolutions() = rgbCameraRecorder?.getSupportedResolutions() ?: emptyList()

    /**
     * Get session directory with all synchronized files
     */
    fun getSessionDirectory(): File? {
        return thermalRecorder.getSessionDirectory()
    }

    /**
     * Cleanup all resources
     */
    /**
     * Executes cleanup operation with thermal imaging domain optimization.
     *
     */
    fun cleanup() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            /**
             * Executes stopsynchronizedrecording operation with thermal imaging domain optimization.
             *
             */
            stopSynchronizedRecording()
        }

        rgbCameraRecorder?.cleanup()
        thermalRecorder.cleanup()

        currentSessionId = null
        isRecording = false
    }

    /**
     * Create a new session combining thermal video recording with RGB+GSR
     * This integrates with the existing thermal recording workflow
     */
    /**
     * Executes createThermalRGBSession functionality.
     */
    /**
     * Executes createthermalrgbsession operation with thermal imaging domain optimization.
     *
     * @param
     * @param thermalVideoFile Parameter for operation (type: File)
     *
     */
    fun createThermalRGBSession(thermalVideoFile: File): RecordingSession? {
        val sessionId = currentSessionId ?: return null
        val sessionDir = getSessionDirectory() ?: return null

        return RecordingSession(
            sessionId = sessionId,
            startTimestamp = System.currentTimeMillis(),
            thermalVideoFile = thermalVideoFile,
            rgbVideoFile = rgbCameraRecorder?.getCurrentVideoFile(),
            gsrDataFile = File(sessionDir, "signals.csv"),
            syncMarksFile = File(sessionDir, "sync_marks.csv"),
            sessionMetadata = File(sessionDir, "session_metadata.json"),
        )
    }

    /**
     * Generate comprehensive session metadata including Samsung S22 processor information
     */
    fun generateSessionMetadata(): Map<String, Any> {
        return mapOf(
            "session_id" to (currentSessionId ?: "unknown"),
            "device_model" to android.os.Build.MODEL,
            "device_processor" to detectSamsungS22Processor(),
            "timing_precision" to "sub_millisecond",
            "unified_time_base" to "samsung_s22_ground_truth",
            "recording_components" to
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "thermal" to "thermal_camera_video",
                    "rgb" to
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf(
                            "resolution" to (rgbCameraRecorder?.getCurrentSettings()?.resolution?.displayName ?: "unknown"),
                            "frame_rate" to (rgbCameraRecorder?.getCurrentSettings()?.frameRate ?: 0),
                            "camera_facing" to (rgbCameraRecorder?.getCurrentCameraFacing()?.displayName ?: "unknown"),
                        ),
                    "gsr" to
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf(
                            "sampling_rate" to "128Hz",
                            "device_type" to "shimmer3_gsr",
                            "data_format" to "conductance_resistance_csv",
                        ),
                ),
            "synchronization_accuracy" to "samsung_s22_hardware_timer",
            "android_version" to android.os.Build.VERSION.RELEASE,
            "api_level" to android.os.Build.VERSION.SDK_INT,
        )
    }

    /**
     * Executes detectSamsungS22Processor functionality.
     */
    /**
     * Executes detectsamsungs22processor operation with thermal imaging domain optimization.
     *
     */
    private fun detectSamsungS22Processor(): String {
        val deviceModel = android.os.Build.MODEL
        return when {
            deviceModel.contains("SM-S901E") -> "Exynos_2200" // International
            deviceModel.contains("SM-S901U") -> "Snapdragon_8_Gen_1" // US
            deviceModel.contains("SM-S901W") -> "Snapdragon_8_Gen_1" // Canada
            deviceModel.contains("SM-S901N") -> "Snapdragon_8_Gen_1" // Korea
            deviceModel.contains("SM-S901") -> "Samsung_S22_Generic" // Generic S22
            else -> "Unknown_Device"
        }
    }
}
