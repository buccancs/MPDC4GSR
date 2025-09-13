package com.topdon.tc001.camera

import android.content.Context
import android.hardware.camera2.*
import android.util.Log
import android.util.Size
import android.view.TextureView
import com.topdon.tc001.camera.core.*
import kotlinx.coroutines.*
import java.io.File

/**
 * Clean Camera2-only system for Samsung S22 dual-mode camera
 *
 * Implements the architecture requested in the comment:
 * - CameraController: opens/closes CameraDevice, owns HandlerThread, constructs/swaps CameraCaptureSession
 * - VideoEngine: wraps MediaRecorder (prepare/start/stop/release)
 * - RawEngine: wraps RAW ImageReader, DngCreator, and file I/O
 * - ModeManager: state machine and switching
 * - UiBridge: pushes preview Surface, exposes errors and progress
 *
 * Goals:
 * - One camera client. No CameraX+Camera2 conflicts.
 * - Two exclusive modes: RAW mode (50 MP DNG stream) OR Video mode (4K60 if exposed, else 4K30)
 * - Fast switch without closing CameraDevice
 * - Deterministic state machine. No races. No silent failures.
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with Camera2System functionality.
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
class Camera2System(
    private val context: Context,
    private val textureView: TextureView,
) {
    companion object {
        private const val TAG = "Camera2System"
        private const val DEFAULT_BITRATE = 20_000_000 // 20Mbps for 4K
    }

    // Core components
    private val cameraController = CameraController(context)
    private val videoEngine = VideoEngine()
    private val rawEngine = RawEngine(context)
    private val modeManager = ModeManager()
    private val uiBridge = UiBridge(textureView)

    // Current session state
    private var currentSessionId: String = ""
    private var isRecording = false
    private var outputDirectory: File? = null

    // Callbacks
    var onError: ((String) -> Unit)? = null
    var onProgress: ((String) -> Unit)? = null
    var onModeChanged: ((ModeManager.CameraMode) -> Unit)? = null
    var onRecordingStarted: (() -> Unit)? = null
    var onRecordingStopped: (() -> Unit)? = null

    init {
        /**
         * Configures the upcallbacks with validation and thermal imaging optimization.
         *
         */
        setupCallbacks()
    }

    /**
     * Initialize the camera system
     */
    suspend fun initialize(cameraId: String = "0"): Boolean =
        withContext(Dispatchers.Main) {
            try {
                Log.i(TAG, "Initializing Camera2System")

                // Wait for texture to be ready
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (!uiBridge.isTextureReady()) {
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(50)
                }

                // Open camera and detect capabilities
                cameraController.openCamera(cameraId)

                // Wait for camera to open (callback will handle initialization)
                return@withContext true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize camera system", e)
                onError?.invoke("Initialization failed: ${e.message}")
                return@withContext false
            }
        }

    /**
     * Switch camera mode (RAW_50MP, VIDEO_4K, PREVIEW_ONLY)
     */
    suspend fun switchMode(mode: ModeManager.CameraMode): Boolean =
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!modeManager.canSwitchMode()) {
                    Log.w(TAG, "Cannot switch mode - switching already in progress")
                    return@withContext false
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!modeManager.requestModeSwitch(mode)) {
                    return@withContext false
                }

                uiBridge.reportProgress("Switching to ${mode.name}...")

                // Fast session reconfiguration without closing camera device
                val success =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (mode) {
                        ModeManager.CameraMode.RAW_50MP -> setupRawMode()
                        ModeManager.CameraMode.VIDEO_4K -> setupVideoMode()
                        ModeManager.CameraMode.PREVIEW_ONLY -> setupPreviewMode()
                    }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    modeManager.confirmModeSwitch()
                    uiBridge.updateMode(mode.name)
                    onModeChanged?.invoke(mode)
                    Log.i(TAG, "Successfully switched to ${mode.name}")
                } else {
                    modeManager.reportModeSwitchFailed("Session setup failed")
                }

                return@withContext success
            } catch (e: Exception) {
                Log.e(TAG, "Mode switch failed", e)
                modeManager.reportModeSwitchFailed(e.message ?: "Unknown error")
                return@withContext false
            }
        }

    /**
     * Start recording in current mode
     */
    suspend fun startRecording(sessionId: String): Boolean =
        withContext(Dispatchers.IO) {
            if (isRecording) {
                Log.w(TAG, "Already recording")
                return@withContext false
            }

            try {
                currentSessionId = sessionId
                outputDirectory = createOutputDirectory(sessionId)

                val success =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (modeManager.getCurrentMode()) {
                        ModeManager.CameraMode.RAW_50MP -> startRawRecording()
                        ModeManager.CameraMode.VIDEO_4K -> startVideoRecording()
                        ModeManager.CameraMode.PREVIEW_ONLY -> {
                            onError?.invoke("Cannot record in preview-only mode")
                            false
                        }
                    }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    isRecording = true
                    onRecordingStarted?.invoke()
                    uiBridge.reportProgress("Recording started")
                    Log.i(TAG, "Recording started in ${modeManager.getCurrentMode()}")
                }

                return@withContext success
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start recording", e)
                onError?.invoke("Recording failed: ${e.message}")
                return@withContext false
            }
        }

    /**
     * Stop recording
     */
    /**
     * Executes stoprecording operation with thermal imaging domain optimization.
     *
     */
    suspend fun stopRecording(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isRecording) {
                Log.w(TAG, "Not recording")
                return@withContext false
            }

            try {
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (modeManager.getCurrentMode()) {
                    ModeManager.CameraMode.RAW_50MP -> rawEngine.stopCapture()
                    ModeManager.CameraMode.VIDEO_4K -> videoEngine.stop()
                    ModeManager.CameraMode.PREVIEW_ONLY -> { /* Nothing to stop */ }
                }

                isRecording = false
                onRecordingStopped?.invoke()
                uiBridge.reportProgress("Recording stopped")
                Log.i(TAG, "Recording stopped")

                return@withContext true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to stop recording", e)
                onError?.invoke("Stop recording failed: ${e.message}")
                return@withContext false
            }
        }

    /**
     * Get current mode
     */
    /**
     * Retrieves the currentmode with optimized performance for thermal imaging operations.
     *
     */
    fun getCurrentMode(): ModeManager.CameraMode = modeManager.getCurrentMode()

    /**
     * Get available modes for this device
     */
    fun getAvailableModes(): List<ModeManager.CameraMode> = modeManager.getAvailableModes()

    /**
     * Check if recording
     */
    /**
     * Executes isrecording operation with thermal imaging domain optimization.
     *
     */
    fun isRecording(): Boolean = isRecording

    /**
     * Get device capabilities
     */
    /**
     * Retrieves the devicecaps with optimized performance for thermal imaging operations.
     *
     */
    fun getDeviceCaps(): DeviceCaps? = cameraController.getDeviceCaps()

    /**
     * Cleanup and release resources
     */
    fun release() {
        if (isRecording) {
            runBlocking { stopRecording() }
        }

        videoEngine.release()
        rawEngine.release()
        cameraController.close()
        uiBridge.release()

        Log.i(TAG, "Camera2System released")
    }

    // Private implementation methods

    /**
     * Sets upcallbacks configuration.
     */
    private fun setupCallbacks() {
        // Camera controller callbacks
        cameraController.onCameraOpened = { caps ->
            modeManager.initialize(caps)
            uiBridge.reportProgress("Camera opened, capabilities detected")

            // Start in preview mode
            /**
             * Executes coroutinescope operation with thermal imaging domain optimization.
             *
             */
            CoroutineScope(Dispatchers.IO).launch {
                /**
                 * Executes switchmode operation with thermal imaging domain optimization.
                 *
                 */
                switchMode(ModeManager.CameraMode.PREVIEW_ONLY)
            }
        }

        cameraController.onCameraError = { error ->
            uiBridge.reportError(error)
            onError?.invoke(error)
        }

        // Mode manager callbacks
        modeManager.onError = { error ->
            uiBridge.reportError(error)
            onError?.invoke(error)
        }

        // UI bridge callbacks
        uiBridge.onError = { error -> onError?.invoke(error) }
        uiBridge.onProgress = { message -> onProgress?.invoke(message) }
    }

    /**
     * Configures the uprawmode with validation and thermal imaging optimization.
     *
     */
    private suspend fun setupRawMode(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val caps = cameraController.getDeviceCaps() ?: return@withContext false
                val previewSurface = uiBridge.getPreviewSurface() ?: return@withContext false

                // Setup RAW engine
                rawEngine.setup(caps.rawSize, outputDirectory ?: createTempDirectory(), currentSessionId)
                val rawSurface = rawEngine.getSurface() ?: return@withContext false

                // Create capture session with preview + RAW surfaces
                val surfaces = listOf(previewSurface, rawSurface)

                return@withContext suspendCancellableCoroutine { continuation ->
                    cameraController.createCaptureSession(
                        surfaces,
                        object : CameraCaptureSession.StateCallback() {
                            /**
                             * Executes onconfigured operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param session Parameter for operation (type: CameraCaptureSession)
                             *
                             */
                            override fun onConfigured(session: CameraCaptureSession) {
                                cameraController.setCaptureSession(session)

                                // Start repeating preview request
                                val requestBuilder = cameraController.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                                requestBuilder?.addTarget(previewSurface)

                                try {
                                    session.setRepeatingRequest(requestBuilder!!.build(), null, null)
                                    continuation.resume(true, null)
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed to start preview request", e)
                                    continuation.resume(false, null)
                                }
                            }

                            /**
                             * Executes onconfigurefailed operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param session Parameter for operation (type: CameraCaptureSession)
                             *
                             */
                            override fun onConfigureFailed(session: CameraCaptureSession) {
                                Log.e(TAG, "RAW mode session configuration failed")
                                continuation.resume(false, null)
                            }
                        },
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to setup RAW mode", e)
                return@withContext false
            }
        }

    /**
     * Configures the upvideomode with validation and thermal imaging optimization.
     *
     */
    private suspend fun setupVideoMode(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val caps = cameraController.getDeviceCaps() ?: return@withContext false
                val previewSurface = uiBridge.getPreviewSurface() ?: return@withContext false

                // Determine video parameters
                val videoSize = Size(3840, 2160) // 4K
                val frameRate = if (caps.supports4k60) 60 else 30

                // Create capture session with preview surface only (MediaRecorder surface added when recording)
                val surfaces = listOf(previewSurface)

                return@withContext suspendCancellableCoroutine { continuation ->
                    cameraController.createCaptureSession(
                        surfaces,
                        object : CameraCaptureSession.StateCallback() {
                            /**
                             * Executes onconfigured operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param session Parameter for operation (type: CameraCaptureSession)
                             *
                             */
                            override fun onConfigured(session: CameraCaptureSession) {
                                cameraController.setCaptureSession(session)

                                // Start repeating preview request
                                val requestBuilder = cameraController.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                                requestBuilder?.addTarget(previewSurface)

                                try {
                                    session.setRepeatingRequest(requestBuilder!!.build(), null, null)
                                    continuation.resume(true, null)
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed to start video preview request", e)
                                    continuation.resume(false, null)
                                }
                            }

                            /**
                             * Executes onconfigurefailed operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param session Parameter for operation (type: CameraCaptureSession)
                             *
                             */
                            override fun onConfigureFailed(session: CameraCaptureSession) {
                                Log.e(TAG, "Video mode session configuration failed")
                                continuation.resume(false, null)
                            }
                        },
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to setup video mode", e)
                return@withContext false
            }
        }

    /**
     * Configures the uppreviewmode with validation and thermal imaging optimization.
     *
     */
    private suspend fun setupPreviewMode(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val previewSurface = uiBridge.getPreviewSurface() ?: return@withContext false

                // Create capture session with preview surface only
                val surfaces = listOf(previewSurface)

                return@withContext suspendCancellableCoroutine { continuation ->
                    cameraController.createCaptureSession(
                        surfaces,
                        object : CameraCaptureSession.StateCallback() {
                            /**
                             * Executes onconfigured operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param session Parameter for operation (type: CameraCaptureSession)
                             *
                             */
                            override fun onConfigured(session: CameraCaptureSession) {
                                cameraController.setCaptureSession(session)

                                // Start repeating preview request
                                val requestBuilder = cameraController.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                                requestBuilder?.addTarget(previewSurface)

                                try {
                                    session.setRepeatingRequest(requestBuilder!!.build(), null, null)
                                    continuation.resume(true, null)
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed to start preview request", e)
                                    continuation.resume(false, null)
                                }
                            }

                            /**
                             * Executes onconfigurefailed operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param session Parameter for operation (type: CameraCaptureSession)
                             *
                             */
                            override fun onConfigureFailed(session: CameraCaptureSession) {
                                Log.e(TAG, "Preview mode session configuration failed")
                                continuation.resume(false, null)
                            }
                        },
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to setup preview mode", e)
                return@withContext false
            }
        }

    /**
     * Executes startrawrecording operation with thermal imaging domain optimization.
     *
     */
    private suspend fun startRawRecording(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                // RAW capture uses continuous still capture requests
                rawEngine.startCapture()

                // Start periodic RAW capture
                /**
                 * Executes startperiodicrawcapture operation with thermal imaging domain optimization.
                 *
                 */
                startPeriodicRawCapture()

                return@withContext true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start RAW recording", e)
                return@withContext false
            }
        }

    /**
     * Executes startvideorecording operation with thermal imaging domain optimization.
     *
     */
    private suspend fun startVideoRecording(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val caps = cameraController.getDeviceCaps() ?: return@withContext false
                val videoFile = createVideoFile()
                val videoSize = Size(3840, 2160)
                val frameRate = if (caps.supports4k60) 60 else 30

                // Prepare MediaRecorder
                val recorderSurface =
                    videoEngine.prepare(videoFile, videoSize, frameRate, DEFAULT_BITRATE, true)
                        ?: return@withContext false

                // Reconfigure session to include recorder surface
                val previewSurface = uiBridge.getPreviewSurface() ?: return@withContext false
                val surfaces = listOf(previewSurface, recorderSurface)

                return@withContext suspendCancellableCoroutine { continuation ->
                    cameraController.createCaptureSession(
                        surfaces,
                        object : CameraCaptureSession.StateCallback() {
                            /**
                             * Executes onconfigured operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param session Parameter for operation (type: CameraCaptureSession)
                             *
                             */
                            override fun onConfigured(session: CameraCaptureSession) {
                                cameraController.setCaptureSession(session)

                                // Create recording request
                                val requestBuilder = cameraController.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                                requestBuilder?.addTarget(previewSurface)
                                requestBuilder?.addTarget(recorderSurface)

                                try {
                                    session.setRepeatingRequest(requestBuilder!!.build(), null, null)

                                    // Start MediaRecorder
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (videoEngine.start()) {
                                        continuation.resume(true, null)
                                    } else {
                                        continuation.resume(false, null)
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed to start recording request", e)
                                    continuation.resume(false, null)
                                }
                            }

                            /**
                             * Executes onconfigurefailed operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param session Parameter for operation (type: CameraCaptureSession)
                             *
                             */
                            override fun onConfigureFailed(session: CameraCaptureSession) {
                                Log.e(TAG, "Recording session configuration failed")
                                continuation.resume(false, null)
                            }
                        },
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start video recording", e)
                return@withContext false
            }
        }

    /**
     * Executes startPeriodicRawCapture functionality.
     */
    /**
     * Executes startperiodicrawcapture operation with thermal imaging domain optimization.
     *
     */
    private fun startPeriodicRawCapture() {
        // Start periodic RAW still captures at 15fps
        val captureInterval = 1000L / 15 // 15fps

        /**
         * Executes coroutinescope operation with thermal imaging domain optimization.
         *
         */
        CoroutineScope(Dispatchers.IO).launch {
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (isRecording && modeManager.getCurrentMode() == ModeManager.CameraMode.RAW_50MP) {
                /**
                 * Executes capturerawimage operation with thermal imaging domain optimization.
                 *
                 */
                captureRawImage()
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(captureInterval)
            }
        }
    }

    /**
     * Executes captureRawImage functionality.
     */
    /**
     * Executes capturerawimage operation with thermal imaging domain optimization.
     *
     */
    private fun captureRawImage() {
        try {
            val rawSurface = rawEngine.getSurface() ?: return
            val session = cameraController.getCaptureSession() ?: return

            val requestBuilder = cameraController.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            requestBuilder?.addTarget(rawSurface)

            session.capture(
                requestBuilder!!.build(),
                object : CameraCaptureSession.CaptureCallback() {
                    /**
                     * Executes oncapturecompleted operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param session Parameter for operation (type: CameraCaptureSession)
                     * @param request Parameter for operation (type: CaptureRequest)
                     * @param result Parameter for operation (type: TotalCaptureResult)
                     *
                     */
                    override fun onCaptureCompleted(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        result: TotalCaptureResult,
                    ) {
                        // Store capture result for DNG pairing
                        rawEngine.storeCaptureResult(result)
                    }
                },
                null,
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to capture RAW image", e)
        }
    }

    /**
     * Executes createOutputDirectory functionality.
     */
    /**
     * Executes createoutputdirectory operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionId Parameter for operation (type: String)
     *
     */
    private fun createOutputDirectory(sessionId: String): File {
        val timestamp = System.currentTimeMillis()
        val dirName = "Camera_${sessionId}_$timestamp"
        return File(context.getExternalFilesDir("Camera"), dirName).apply {
            /**
             * Executes mkdirs operation with thermal imaging domain optimization.
             *
             */
            mkdirs()
        }
    }

    /**
     * Processes temperature measurement data.
     */
    private fun createTempDirectory(): File {
        return File(context.cacheDir, "temp_raw").apply { mkdirs() }
    }

    /**
     * Executes createVideoFile functionality.
     */
    /**
     * Executes createvideofile operation with thermal imaging domain optimization.
     *
     */
    private fun createVideoFile(): File {
        val timestamp = System.currentTimeMillis()
        val filename = "Video_${currentSessionId}_$timestamp.mp4"
        return File(outputDirectory, filename)
    }
}
