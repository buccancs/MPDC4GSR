package com.topdon.tc001.sensors.rgb
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.util.Log
import android.view.TextureView
import android.view.WindowManager
import com.opencsv.CSVWriter
import com.topdon.tc001.camera.Camera2System
import com.topdon.tc001.camera.core.DeviceCaps
import com.topdon.tc001.camera.core.ModeManager
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.topdon.tc001.sensors.*
import com.topdon.tc001.sensors.RecordingStats
import com.topdon.tc001.sensors.ErrorType
import com.topdon.tc001.util.CSVBufferedWriter
import com.topdon.tc001.util.SessionDirectoryManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileWriter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
class RgbCameraRecorder(
    private val context: Context,
    private val textureView: TextureView
) : SensorRecorder {
    companion object {
        private const val TAG = "RgbCameraRecorder"
        private const val VIDEO_WIDTH = 3840  
        private const val VIDEO_HEIGHT = 2160 
        private const val VIDEO_FPS = 60      
        private const val VIDEO_BITRATE = 50_000_000  
        private const val AUDIO_BITRATE = 256_000     
    }
    override val sensorId: String = "rgb_camera_${System.currentTimeMillis()}"
    override val sensorType: String = "RGB_Camera_Dual_Output"
    override val samplingRate: Double = VIDEO_FPS.toDouble()
    private val _isRecording = AtomicBoolean(false)
    override val isRecording: Boolean get() = _isRecording.get()
    private val camera2System = Camera2System(context, textureView)
    private var selectedCameraId: String = "0"
    private var deviceCaps: DeviceCaps? = null
    private val statusFlow = MutableStateFlow(createInitialStatus())
    private val errorFlow = MutableSharedFlow<SensorError>()
    private var sessionDirectory: String = ""
    private var csvWriter: CSVWriter? = null
    private var videoFile: File? = null
    private var csvBufferedWriter: CSVBufferedWriter? = null
    private var csvFile: File? = null
    private val samplesRecorded = AtomicLong(0)
    private val sessionStartTime = AtomicLong(0)
    private val lastFrameTime = AtomicLong(0)
    private var droppedFrames = AtomicLong(0)
    private val syncMarkersRecorded = AtomicLong(0)
    private val rawCapturesRecorded = AtomicLong(0)
    private val recordingScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var currentRecordingMode: RecordingMode = RecordingMode.DUAL_OUTPUT
    enum class RecordingMode {
        DUAL_OUTPUT,  
        VIDEO_ONLY,   
        RAW_ONLY      
    }
    override suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Initializing RGB Camera2 recorder with dual output capability")
            selectedCameraId = selectCameraWithRawSupport()
            Log.i(TAG, "Selected camera ID: $selectedCameraId")
            setupCamera2Callbacks()
            val success = camera2System.initialize(selectedCameraId)
            if (!success) {
                emitError(ErrorType.INITIALIZATION_FAILED, "Camera2System initialization failed")
                return@withContext false
            }
            var retryCount = 0
            while (deviceCaps == null && retryCount < 10) {
                delay(100)
                deviceCaps = camera2System.getDeviceCaps()
                retryCount++
            }
            deviceCaps?.let { caps ->
                Log.i(TAG, "Device capabilities: RAW=${caps.supportsRaw}, 4K60=${caps.supports4k60}")
                if (!caps.supportsRaw) {
                    Log.w(TAG, "Device does not support RAW capture - falling back to video-only mode")
                    currentRecordingMode = RecordingMode.VIDEO_ONLY
                }
            } ?: run {
                Log.w(TAG, "Could not detect device capabilities - using video-only mode")
                currentRecordingMode = RecordingMode.VIDEO_ONLY
            }
            Log.i(TAG, "RGB Camera2 recorder initialized successfully with mode: $currentRecordingMode")
            updateStatus(isInitialized = true)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize RGB Camera2 recorder", e)
            emitError(ErrorType.INITIALIZATION_FAILED, "Camera2 initialization failed: ${e.message}")
            false
        }
    }
    private fun selectCameraWithRawSupport(): String {
        try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            for (cameraId in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing != CameraCharacteristics.LENS_FACING_BACK) {
                    continue
                }
                val capabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
                val supportsRaw = capabilities?.contains(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW) == true
                Log.d(TAG, "Camera $cameraId: facing=$facing, supportsRaw=$supportsRaw")
                if (supportsRaw) {
                    Log.i(TAG, "Found camera with RAW support: $cameraId")
                    return cameraId
                }
            }
            Log.w(TAG, "No camera with RAW support found, using default back camera")
            return "0"
        } catch (e: Exception) {
            Log.e(TAG, "Error selecting camera with RAW support", e)
            return "0"
        }
    }
    private fun setupCamera2Callbacks() {
        camera2System.onError = { error ->
            recordingScope.launch {
                emitError(ErrorType.HARDWARE_DISCONNECTED, error)
            }
        }
        camera2System.onProgress = { message ->
            Log.d(TAG, "Camera2System progress: $message")
        }
        camera2System.onModeChanged = { mode ->
            Log.i(TAG, "Camera mode changed to: $mode")
        }
        camera2System.onRecordingStarted = {
            Log.i(TAG, "Camera2System recording started")
            recordingScope.launch {
                samplesRecorded.incrementAndGet()
                updateStatus(isRecording = true)
            }
        }
        camera2System.onRecordingStopped = {
            Log.i(TAG, "Camera2System recording stopped") 
            recordingScope.launch {
                updateStatus(isRecording = false)
            }
        }
    }
    override suspend fun startRecording(sessionDirectory: String): Boolean {
        return try {
            if (_isRecording.get()) {
                Log.w(TAG, "Recording already in progress")
                return false
            }
            this.sessionDirectory = sessionDirectory
            val sessionDir = File(sessionDirectory)
            if (!sessionDir.exists()) {
                sessionDir.mkdirs()
            }
            setupOutputFiles()
            startVideoRecording()
            recordingScope.launch {
                initializeCsvWriter()
            }
            val success = when (currentRecordingMode) {
                RecordingMode.DUAL_OUTPUT -> startDualOutputRecording()
                RecordingMode.VIDEO_ONLY -> startVideoOnlyRecording()
                RecordingMode.RAW_ONLY -> startRawOnlyRecording()
            }
            if (success) {
                _isRecording.set(true)
                sessionStartTime.set(System.nanoTime())
                samplesRecorded.set(0)
                droppedFrames.set(0)
                rawCapturesRecorded.set(0)
                Log.i(TAG, "RGB Camera2 recording started in: $sessionDirectory with mode: $currentRecordingMode")
                updateStatus(isRecording = true)
            }
            success
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start RGB Camera2 recording", e)
            emitError(ErrorType.RECORDING_FAILED, "Failed to start recording: ${e.message}")
            false
        }
    }
    private fun setupOutputFiles() {
        val rgbDir = File(sessionDirectory, "RGB")
        if (!rgbDir.exists()) {
            rgbDir.mkdirs()
        }
        videoFile = File(rgbDir, SessionDirectoryManager.RGB_VIDEO_FILE)
        csvFile = File(rgbDir, "rgb_timestamps.csv")
    }
    private suspend fun startDualOutputRecording(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.i(TAG, "Starting dual output recording (4K60fps video + RAW capture)")
                val success = camera2System.switchMode(ModeManager.CameraMode.VIDEO_4K60)
                if (!success) {
                    Log.w(TAG, "4K60fps not supported, falling back to VIDEO_4K")
                    val fallbackSuccess = camera2System.switchMode(ModeManager.CameraMode.VIDEO_4K)
                    if (!fallbackSuccess) {
                        Log.e(TAG, "Failed to switch to any 4K video mode for dual output")
                        return@withContext false
                    }
                }
                val sessionId = "session_${System.currentTimeMillis()}"
                val recordingSuccess = camera2System.startRecording(sessionId)
                if (!recordingSuccess) {
                    Log.e(TAG, "Failed to start Camera2System recording")
                    return@withContext false
                }
                startPeriodicRawCapture()
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start dual output recording", e)
                false
            }
        }
    }
    private suspend fun startVideoOnlyRecording(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.i(TAG, "Starting 4K60fps video-only recording")
                var success = camera2System.switchMode(ModeManager.CameraMode.VIDEO_4K60)
                if (!success) {
                    Log.w(TAG, "4K60fps not supported, falling back to 4K30fps")
                    success = camera2System.switchMode(ModeManager.CameraMode.VIDEO_4K)
                    if (!success) {
                        Log.e(TAG, "Failed to switch to any 4K video mode")
                        return@withContext false
                    }
                }
                val sessionId = "session_${System.currentTimeMillis()}"
                camera2System.startRecording(sessionId)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start video-only recording", e)
                false
            }
        }
    }
    private suspend fun startRawOnlyRecording(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.i(TAG, "Starting RAW-only recording (50MP)")
                val success = camera2System.switchMode(ModeManager.CameraMode.RAW_50MP)
                if (!success) {
                    Log.e(TAG, "Failed to switch to RAW 50MP mode")
                    return@withContext false
                }
                val sessionId = "session_${System.currentTimeMillis()}"
                camera2System.startRecording(sessionId)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start RAW-only recording", e)
                false
            }
        }
    }
    private fun startPeriodicRawCapture() {
        recordingScope.launch {
            Log.i(TAG, "Starting periodic RAW capture for dual output")
            while (_isRecording.get() && currentRecordingMode == RecordingMode.DUAL_OUTPUT) {
                try {
                    if (camera2System.switchMode(ModeManager.CameraMode.RAW_50MP)) {
                        delay(100) 
                        val timestamp = System.nanoTime()
                        rawCapturesRecorded.incrementAndGet()
                        logRawCapture(timestamp)
                        val videoMode = if (deviceCaps?.supports4k60 == true) {
                            ModeManager.CameraMode.VIDEO_4K60
                        } else {
                            ModeManager.CameraMode.VIDEO_4K
                        }
                        camera2System.switchMode(videoMode)
                        delay(100) 
                    }
                    delay(2000)
                } catch (e: Exception) {
                    Log.w(TAG, "Failed during periodic RAW capture", e)
                    delay(2000) 
                }
            }
            Log.i(TAG, "Periodic RAW capture stopped")
        }
    }
    private suspend fun initializeCsvWriter() {
        try {
            csvFile?.let { file ->
                csvWriter = CSVWriter(FileWriter(file)).apply {
                    writeNext(
                        arrayOf(
                            "timestamp_ns",
                            "sample_number", 
                            "session_time_ms",
                            "event_type",      
                            "metadata"         
                        )
                    )
                    flush()
                }
                val headers = listOf(
                    "timestamp_ns",
                    "frame_number", 
                    "session_time_ms",
                    "sync_marker",
                    "metadata"
                )
                csvBufferedWriter = CSVBufferedWriter(
                    outputFile = file,
                    headers = headers,
                    bufferSize = 4096,
                    flushIntervalMs = 1000L  
                )
                csvBufferedWriter?.startWithHeaders()
            }
            Log.d(TAG, "Buffered CSV writer initialized for frame timestamps")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize CSV writer", e)
            throw e
        }
    }
    private fun logRawCapture(timestampNs: Long) {
        try {
            csvBufferedWriter?.let { writer ->
                val sessionTimeMs = (timestampNs - sessionStartTime.get()) / 1_000_000
                val filename = "raw_${timestampNs}.dng"
                writer.writeNext(
                    arrayOf(
                        timestampNs.toString(),
                        rawCapturesRecorded.get().toString(),
                        sessionTimeMs.toString(),
                        "raw_capture",
                        "filename=$filename,mode=dual_output"
                    )
                )
            }
            Log.d(TAG, "RAW capture logged: ${rawCapturesRecorded.get()}")
        } catch (e: Exception) {
            Log.w(TAG, "Failed to log RAW capture", e)
        }
    }
    override suspend fun stopRecording(): Boolean {
        return try {
            if (!_isRecording.get()) {
                Log.w(TAG, "No recording in progress to stop")
                return false
            }
            _isRecording.set(false)
            val success = camera2System.stopRecording()
            csvWriter?.close()
            csvWriter = null
            activeRecording?.stop()
            activeRecording = null
            csvBufferedWriter?.stop()
            csvBufferedWriter = null
            if (success) {
                Log.i(TAG, "RGB Camera2 recording stopped successfully")
                Log.i(TAG, "Session stats - Video frames: ${samplesRecorded.get()}, RAW captures: ${rawCapturesRecorded.get()}")
            } else {
                Log.w(TAG, "Camera2System stop returned false, but continuing cleanup")
            }
            updateStatus(isRecording = false)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop RGB Camera2 recording", e)
            emitError(ErrorType.RECORDING_FAILED, "Failed to stop recording: ${e.message}")
            false
        }
    }
    override suspend fun addSyncMarker(
        markerType: String,
        timestampNs: Long,
        metadata: Map<String, String>
    ) {
        try {
            csvBufferedWriter?.let { writer ->
                val sessionTimeMs = (timestampNs - sessionStartTime.get()) / 1_000_000
                val metadataStr = metadata.entries.joinToString(",") { "${it.key}=${it.value}" }
                val row = listOf(
                    timestampNs,
                    samplesRecorded.get(),
                    sessionTimeMs,
                    markerType,
                    metadataStr
                )
                writer.writeRow(row)
            }
            Log.d(TAG, "Sync marker added: $markerType at $timestampNs ns")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add sync marker", e)
            emitError(ErrorType.SYNC_FAILED, "Failed to add sync marker: ${e.message}")
        }
    }
    override suspend fun cleanup() {
        try {
            if (_isRecording.get()) {
                stopRecording()
            }
            camera2System.release()
            recordingScope.cancel()
            Log.i(TAG, "RGB Camera2 recorder cleanup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error during Camera2 cleanup", e)
        }
    }
    override fun getStatusFlow(): Flow<RecordingStatus> = statusFlow.asStateFlow()
    override fun getErrorFlow(): Flow<SensorError> = errorFlow.asSharedFlow()
    override fun getRecordingStats(): RecordingStats {
        val currentTime = System.nanoTime()
        val sessionDuration = if (sessionStartTime.get() > 0) {
            (currentTime - sessionStartTime.get()) / 1_000_000
        } else 0L
        val totalSamples = samplesRecorded.get() + rawCapturesRecorded.get()
        return RecordingStats(
            sensorId = sensorId,
            sensorType = "$sensorType (${currentRecordingMode.name})",
            sessionDurationMs = sessionDuration,
            totalSamplesRecorded = totalSamples,
            averageDataRate = if (sessionDuration > 0) {
                (totalSamples * 1000.0) / sessionDuration
            } else 0.0,
            droppedSamples = droppedFrames.get(),
            storageUsedMB = calculateStorageUsed(),
            syncMarkersCount = syncMarkersRecorded.get().toInt(),
            lastSampleTimestampNs = currentTime
        )
    }
    private fun calculateStorageUsed(): Double {
        var totalBytes = 0L
        if (sessionDirectory.isNotEmpty()) {
            val sessionDir = File(sessionDirectory)
            if (sessionDir.exists()) {
                totalBytes += sessionDir.walkTopDown()
                    .filter { it.isFile }
                    .map { it.length() }
                    .sum()
            }
        }
        csvFile?.let { file ->
            if (file.exists()) {
                totalBytes += file.length()
            }
        }
        return totalBytes / (1024.0 * 1024.0) 
    }
    private fun createInitialStatus() = RecordingStatus(
        sensorId = sensorId,
        sensorType = "$sensorType (Camera2_Dual_Output)",
        isRecording = false,
        samplesRecorded = 0,
        currentDataRate = 0.0,
        storageUsedMB = 0.0,
        timestampNs = System.nanoTime()
    )
    private suspend fun updateStatus(
        isRecording: Boolean = this.isRecording,
        isInitialized: Boolean = false
    ) {
        val stats = getRecordingStats()
        val status = RecordingStatus(
            sensorId = sensorId,
            sensorType = sensorType,
            isRecording = isRecording,
            samplesRecorded = stats.totalSamplesRecorded,
            currentDataRate = stats.averageDataRate,
            storageUsedMB = stats.storageUsedMB,
            timestampNs = System.nanoTime()
        )
        statusFlow.emit(status)
    }
    suspend fun captureRawImage(): Boolean {
        if (!isRecording || deviceCaps?.supportsRaw != true) {
            Log.w(TAG, "Cannot capture RAW - not recording or device doesn't support RAW")
            return false
        }
        return try {
            val success = camera2System.switchMode(ModeManager.CameraMode.RAW_50MP)
            if (success) {
                delay(200) 
                val timestamp = System.nanoTime()
                rawCapturesRecorded.incrementAndGet()
                logRawCapture(timestamp)
                if (currentRecordingMode == RecordingMode.DUAL_OUTPUT) {
                    camera2System.switchMode(ModeManager.CameraMode.VIDEO_4K)
                }
                Log.d(TAG, "Manual RAW capture completed")
                true
            } else {
                Log.e(TAG, "Failed to switch to RAW mode for manual capture")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during manual RAW capture", e)
            false
        }
    }
    suspend fun recordSyncMarker(markerType: String = "SYNC") {
        if (!isRecording) return
        val timestamp = System.nanoTime()
        recordingScope.launch {
            csvBufferedWriter?.let { writer ->
                try {
                    val row = listOf(
                        timestamp,
                        "SYNC_MARKER",
                        markerType,
                        System.currentTimeMillis(),
                        0, 
                        0  
                    )
                    writer.writeRow(row)
                    syncMarkersRecorded.incrementAndGet()
                    Log.d(TAG, "Sync marker recorded: $markerType at $timestamp")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to record sync marker", e)
                }
            }
        }
    }
    private fun getDeviceRotation(): Int {
        return try {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
            when (windowManager.defaultDisplay.rotation) {
                android.view.Surface.ROTATION_0 -> 0
                android.view.Surface.ROTATION_90 -> 90
                android.view.Surface.ROTATION_180 -> 180
                android.view.Surface.ROTATION_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get device rotation", e)
            0
        }
    }
    private fun calculateOrientationHint(): Int {
        val deviceRotation = getDeviceRotation()
        val sensorOrientation = deviceCaps?.sensorOrientation ?: 90
        val orientationHint = (sensorOrientation - deviceRotation + 360) % 360
        Log.d(TAG, "Orientation calculation: device=$deviceRotation, sensor=$sensorOrientation, hint=$orientationHint")
        return orientationHint
    }
    private suspend fun emitError(errorType: ErrorType, message: String) {
        val error = SensorError(
            sensorId = sensorId,
            sensorType = sensorType,
            errorType = errorType,
            errorMessage = message,
            timestampNs = System.nanoTime(),
            isRecoverable = errorType != ErrorType.HARDWARE_DISCONNECTED
        )
        errorFlow.emit(error)
    }
}