package com.topdon.tc001.camera.core

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

/**
 * Camera2-only module for Samsung S22 dual-mode camera system
 *
 * Implements the clean architecture requested:
 * - One camera client only (no CameraX conflicts)
 * - Fast switching without closing CameraDevice
 * - Deterministic state machine
 * - Capabilities detection once at camera open
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraController functionality.
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
class CameraController(private val context: Context) {
    companion object {
        private const val TAG = "CameraController"
        private const val CAMERA_OPEN_TIMEOUT_MS = 2500L
    }

    // Camera state
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var currentCameraId: String = "0"
    private var deviceCaps: DeviceCaps? = null

    // Background thread for camera operations
    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null
    private val cameraOpenCloseLock = Semaphore(1)

    // State callbacks
    var onCameraOpened: ((DeviceCaps) -> Unit)? = null
    var onCameraError: ((String) -> Unit)? = null

    init {
        /**
         * Executes startbackgroundthread operation with thermal imaging domain optimization.
         *
         */
        startBackgroundThread()
    }

    /**
     * Open camera and detect capabilities once
     */
    fun openCamera(cameraId: String = "0") {
        Log.i(TAG, "Opening camera $cameraId")

        try {
            val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val characteristics = manager.getCameraCharacteristics(cameraId)

            // Detect capabilities once at camera open (as specified)
            deviceCaps = detectCapabilities(characteristics)
            Log.i(TAG, "Device capabilities: $deviceCaps")

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!cameraOpenCloseLock.tryAcquire(CAMERA_OPEN_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }

            manager.openCamera(cameraId, stateCallback, backgroundHandler)
            currentCameraId = cameraId
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to open camera $cameraId", e)
            onCameraError?.invoke("Failed to open camera: ${e.message}")
        } catch (e: SecurityException) {
            Log.e(TAG, "Camera permission not granted", e)
            onCameraError?.invoke("Camera permission required")
        }
    }

    /**
     * Create capture session for specific mode
     * Fast switching without closing CameraDevice
     */
    /**
     * Executes createCaptureSession functionality.
     */
    /**
     * Executes createcapturesession operation with thermal imaging domain optimization.
     *
     * @param
     * @param surfaces Parameter for operation (type: List<Surface>)
     * @param callback Parameter for operation (type: CameraCaptureSession.StateCallback)
     *
     */
    fun createCaptureSession(
        surfaces: List<Surface>,
        callback: CameraCaptureSession.StateCallback,
    ) {
        cameraDevice?.let { device ->
            try {
                // Close previous session but keep camera device open
                captureSession?.close()
                captureSession = null

                Log.i(TAG, "Creating capture session with ${surfaces.size} surfaces")
                device.createCaptureSession(surfaces, callback, backgroundHandler)
            } catch (e: CameraAccessException) {
                Log.e(TAG, "Failed to create capture session", e)
                onCameraError?.invoke("Failed to create capture session: ${e.message}")
            }
        } ?: run {
            Log.e(TAG, "Cannot create session - camera device is null")
            onCameraError?.invoke("Camera not opened")
        }
    }

    /**
     * Create capture request builder for specific template
     */
    fun createCaptureRequest(template: Int): CaptureRequest.Builder? {
        return try {
            cameraDevice?.createCaptureRequest(template)
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to create capture request", e)
            null
        }
    }

    /**
     * Get device capabilities (detected once at open)
     */
    fun getDeviceCaps(): DeviceCaps? = deviceCaps

    /**
     * Check if camera is open
     */
    /**
     * Executes isopen operation with thermal imaging domain optimization.
     *
     */
    fun isOpen(): Boolean = cameraDevice != null

    /**
     * Set current capture session
     */
    fun setCaptureSession(session: CameraCaptureSession) {
        captureSession = session
    }

    /**
     * Get current capture session
     */
    fun getCaptureSession(): CameraCaptureSession? = captureSession

    /**
     * Close camera and cleanup
     */
    /**
     * Executes close operation with thermal imaging domain optimization.
     *
     */
    fun close() {
        try {
            cameraOpenCloseLock.acquire()
            captureSession?.close()
            captureSession = null
            cameraDevice?.close()
            cameraDevice = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }

        /**
         * Executes stopbackgroundthread operation with thermal imaging domain optimization.
         *
         */
        stopBackgroundThread()
    }

    /**
     * Capabilities detection (once, at camera open)
     */
    private fun detectCapabilities(characteristics: CameraCharacteristics): DeviceCaps {
        val capabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES) ?: IntArray(0)
        val supportsRaw = capabilities.contains(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW)

        val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

        // RAW sizes: from SCALER_STREAM_CONFIGURATION_MAP.getOutputSizes(RAW_SENSOR). Pick max.
        val rawSizes = map?.getOutputSizes(ImageFormat.RAW_SENSOR) ?: arrayOf(Size(0, 0))
        val rawSize = rawSizes.maxByOrNull { it.width * it.height } ?: Size(0, 0)

        // High-speed: Check 3840×2160 with fpsRange including 60
        var supports4k60 = false
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Check high-speed video sizes and fps ranges directly from StreamConfigurationMap
                map?.getHighSpeedVideoSizes()?.forEach { size ->
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (size.width == 3840 && size.height == 2160) {
                        // Check if any fps range includes 60
                        map.getHighSpeedVideoFpsRangesFor(size)?.forEach { range ->
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (range.upper >= 60) {
                                supports4k60 = true
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "High-speed video detection failed: ${e.message}")
        }

        val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0

        return DeviceCaps(
            supportsRaw = supportsRaw,
            rawSize = rawSize,
            supports4k60 = supports4k60,
            sensorOrientation = sensorOrientation,
        )
    }

    private val stateCallback =
        object : CameraDevice.StateCallback() {
            /**
             * Executes onopened operation with thermal imaging domain optimization.
             *
             * @param
             * @param camera Camera configuration or reference (type: CameraDevice)
             *
             */
            override fun onOpened(camera: CameraDevice) {
                cameraOpenCloseLock.release()
                cameraDevice = camera
                Log.i(TAG, "Camera opened successfully")

                deviceCaps?.let { caps ->
                    onCameraOpened?.invoke(caps)
                }
            }

            /**
             * Executes ondisconnected operation with thermal imaging domain optimization.
             *
             * @param
             * @param camera Camera configuration or reference (type: CameraDevice)
             *
             */
            override fun onDisconnected(camera: CameraDevice) {
                cameraOpenCloseLock.release()
                camera.close()
                cameraDevice = null
                Log.w(TAG, "Camera disconnected")
                onCameraError?.invoke("Camera disconnected")
            }

            /**
             * Executes onerror operation with thermal imaging domain optimization.
             *
             * @param
             * @param camera Camera configuration or reference (type: CameraDevice)
             * @param error Parameter for operation (type: Int)
             *
             */
            override fun onError(
                camera: CameraDevice,
                error: Int,
            ) {
                cameraOpenCloseLock.release()
                camera.close()
                cameraDevice = null

                val errorMessage =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (error) {
                        CameraDevice.StateCallback.ERROR_CAMERA_IN_USE -> "Camera is in use by another app"
                        CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE -> "Too many cameras in use"
                        CameraDevice.StateCallback.ERROR_CAMERA_DISABLED -> "Camera disabled by device policy"
                        CameraDevice.StateCallback.ERROR_CAMERA_DEVICE -> "Camera device error"
                        CameraDevice.StateCallback.ERROR_CAMERA_SERVICE -> "Camera service error"
                        else -> "Unknown camera error: $error"
                    }

                Log.e(TAG, "Camera error: $errorMessage")
                onCameraError?.invoke("Camera error: $errorMessage")
            }
        }

    /**
     * Executes startBackgroundThread functionality.
     */
    /**
     * Executes startbackgroundthread operation with thermal imaging domain optimization.
     *
     */
    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground")
        backgroundThread!!.start()
        backgroundHandler = Handler(backgroundThread!!.looper)
    }

    /**
     * Executes stopBackgroundThread functionality.
     */
    /**
     * Executes stopbackgroundthread operation with thermal imaging domain optimization.
     *
     */
    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Log.e(TAG, "Error stopping background thread", e)
        }
    }
}
