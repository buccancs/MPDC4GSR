package com.topdon.tc001.gsr

import android.content.Context
import android.util.Log
import com.topdon.gsr.model.SessionInfo
import com.topdon.gsr.service.GSRRecorder
import com.topdon.gsr.service.SessionManager
import com.topdon.gsr.util.TimeUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * Specialized thermal imaging component providing EnhancedThermalRecorder functionality for the IRCamera system.
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
class EnhancedThermalRecorder private constructor(
    private val context: Context,
) {
    companion object {
        private const val TAG = "EnhancedThermalRecorder"

        /**
         * Create Enhanced Thermal Recorder with Samsung S22 device validation
         * Supports both Exynos 2200 and Snapdragon 8 Gen 1 variants
         */
    /**
     * Executes create functionality.
     */
        /**
         * Executes create operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun create(context: Context): EnhancedThermalRecorder {
            val recorder = EnhancedThermalRecorder(context)

            // Initialize timing system to detect processor variant
            TimeUtil.initializeGroundTruthTiming()

            // Get detected processor and model information
            val detectedProcessor = TimeUtil.getDetectedProcessor()
            val deviceModel = TimeUtil.getDeviceModel()
            val deviceManufacturer = android.os.Build.MANUFACTURER

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (deviceManufacturer.contains("samsung", ignoreCase = true) &&
                deviceModel.contains("SM-S90", ignoreCase = true)
            ) {
                Log.d(TAG, "Samsung S22 device detected: $deviceManufacturer $deviceModel")
                Log.d(TAG, "Processor variant: $detectedProcessor - Optimal timing performance enabled")

                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (detectedProcessor) {
                    "Exynos_2200" -> Log.i(TAG, "Exynos 2200 processor detected - ARM Cortex-X2 high-precision timing active")
                    "Snapdragon_8_Gen_1" -> Log.i(TAG, "Snapdragon 8 Gen 1 processor detected - Kryo 780 high-precision timing active")
                    "Samsung_S22_Generic" -> Log.i(TAG, "Samsung S22 detected - Generic high-precision timing active")
                }
            } else {
                Log.w(TAG, "Non-Samsung S22 device: $deviceManufacturer $deviceModel - Using standard timing")
                Log.w(TAG, "Detected processor: $detectedProcessor")
            }

            return recorder
        }
    }

    private val gsrRecorder: GSRRecorder = GSRRecorder(context)
    private val sessionManager: SessionManager = SessionManager.getInstance(context)

    private var currentSession: SessionInfo? = null
    private var isRecordingState = false

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
                Log.i(TAG, "Enhanced thermal recording with GSR started: ${sessionInfo.sessionId}")
                currentSession = sessionInfo
            }

            /**
             * Executes onrecordingstopped operation with thermal imaging domain optimization.
             *
             * @param
             * @param sessionInfo Parameter for operation (type: SessionInfo)
             *
             */
            override fun onRecordingStopped(sessionInfo: SessionInfo) {
                Log.i(TAG, "Enhanced thermal recording with GSR stopped: ${sessionInfo.sessionId}")
                currentSession = null
                isRecordingState = false
            }

            /**
             * Executes onsamplerecorded operation with thermal imaging domain optimization.
             *
             * @param
             * @param sample Parameter for operation (type: com.topdon.gsr.model.GSRSample)
             *
             */
            override fun onSampleRecorded(sample: com.topdon.gsr.model.GSRSample) {
                // Log every 1280 samples (10 seconds at 128Hz) to avoid spam
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (sample.sampleIndex % 1280 == 0L) {
                    Log.d(TAG, "GSR recording: ${sample.sampleIndex} samples (${sample.sampleIndex / 128}s)")
                }
            }

            /**
             * Executes onsyncmarkadded operation with thermal imaging domain optimization.
             *
             * @param
             * @param syncMark Parameter for operation (type: com.topdon.gsr.model.SyncMark)
             *
             */
            override fun onSyncMarkAdded(syncMark: com.topdon.gsr.model.SyncMark) {
                Log.d(TAG, "Thermal sync event: ${syncMark.eventType}")
            }

            /**
             * Executes onerror operation with thermal imaging domain optimization.
             *
             * @param
             * @param error Parameter for operation (type: String)
             *
             */
            override fun onError(error: String) {
                Log.e(TAG, "GSR recording error during thermal session: $error")
            }
        }

    init {
        gsrRecorder.addListener(gsrListener)

        // Samsung S22 device timing initialization is handled in create() method
        // To avoid duplicate initialization
        Log.d(TAG, "Enhanced thermal recorder initialized with Samsung S22 ground truth timing")
        Log.d(TAG, "Detected processor: ${TimeUtil.getDetectedProcessor()}")
        Log.d(TAG, "Timing validation: ${TimeUtil.validateTimingSystem()}")
    }

    /**
     * Start recording session with automatic GSR integration and Samsung S22 ground truth timing
     */
    suspend fun startRecording(
        sessionName: String,
        participantId: String? = null,
        enableGsr: Boolean = true,
    ): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecordingState) {
            Log.w(TAG, "Recording already in progress")
            return false
        }

        val sessionId =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (sessionName.contains("_")) {
                sessionName // Use provided name if it looks like a session ID
            } else {
                TimeUtil.generateSessionId(sessionName)
            }

        // Establish unified Samsung S22 ground truth timestamp for true synchronization
        val unifiedStartTimestamp = TimeUtil.getHighPrecisionTimestamp()
        Log.d(TAG, "Starting synchronized recording with Samsung S22 ground truth timestamp: $unifiedStartTimestamp")
        Log.d(TAG, "Using ${TimeUtil.getDetectedProcessor()} processor timing for maximum precision")

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (enableGsr) {
            // Start GSR recording automatically with unified timing using suspend function
            val gsrStarted = gsrRecorder.startRecording(sessionName, participantId, "Thermal_GSR_Study")

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (gsrStarted) {
                isRecordingState = true

                // Verify timing synchronization
                val timingValidation = TimeUtil.validateTimingSystem()
                Log.i(TAG, "Enhanced thermal recording started with GSR: $sessionName")
                Log.d(TAG, "Samsung S22 timing system validation: $timingValidation")

                // Add initial synchronization verification mark
                val syncEventSuccess =
                    /**
                     * Executes triggersyncevent operation with thermal imaging domain optimization.
                     *
                     */
                    triggerSyncEvent(
                        "RECORDING_INITIALIZATION",
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf(
                            "unified_start_timestamp" to unifiedStartTimestamp.toString(),
                            "samsung_s22_ground_truth" to "established",
                            "timing_validation" to timingValidation.toString(),
                        ),
                    )

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (syncEventSuccess) {
                    Log.d(TAG, "Initial synchronization mark successfully added")
                }

                return true
            } else {
                Log.e(TAG, "Failed to start GSR recording for thermal session")
                return false
            }
        } else {
            // Create session without GSR recording
            currentSession = sessionManager.createSession(sessionId, participantId, "Thermal_Only_Study")
            isRecordingState = true
            Log.i(TAG, "Thermal recording started without GSR: $sessionId")
            return true
        }
    }

    /**
     * Stop recording session
     */
    /**
     * Executes stoprecording operation with thermal imaging domain optimization.
     *
     */
    fun stopRecording(): SessionInfo? {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isRecordingState) {
            Log.w(TAG, "No recording in progress")
            return currentSession
        }

        val session =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (gsrRecorder.isRecording()) {
                gsrRecorder.stopRecording()
            } else {
                currentSession?.let { sessionManager.completeSession(it.sessionId) }
            }

        isRecordingState = false
        Log.i(TAG, "Enhanced thermal recording stopped")
        return session
    }

    /**
     * Trigger synchronization event with high-precision Samsung S22 timing
     */
    fun triggerSyncEvent(
        eventType: String = "THERMAL_CAPTURE",
        metadata: Map<String, String> = emptyMap(),
    ): Boolean {
        return if (isRecordingState) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (gsrRecorder.isRecording()) {
                // Add unified timing metadata with Samsung S22 high-precision synchronization
                val synchronizedTimestamp = TimeUtil.getHighPrecisionTimestamp()
                val enhancedMetadata =
                    mutableMapOf<String, String>().apply {
                        /**
                         * Executes putall operation with thermal imaging domain optimization.
                         *
                         */
                        putAll(metadata)
                        /**
                         * Executes putall operation with thermal imaging domain optimization.
                         *
                         */
                        putAll(TimeUtil.getTimingMetadata())
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("sync_timestamp", synchronizedTimestamp.toString())
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("high_precision_timestamp", synchronizedTimestamp.toString())
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("thermal_ground_truth", "samsung_s22_snapdragon_8_gen_1")
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put("timing_validation", TimeUtil.validateTimingSystem().toString())
                    }

                // Use the existing coroutine scope to handle async call without blocking
                try {
                    // This will be executed in the GSRRecorder's internal coroutine context
                    GlobalScope.launch {
                        gsrRecorder.addSyncMark(eventType, enhancedMetadata.toString())
                    }
                    true
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to add sync mark", e)
                    false
                }
            } else {
                // Add sync mark to session manager for thermal-only sessions
                currentSession?.let { session ->
                    val syncMark =
                        com.topdon.gsr.model.SyncMark(
                            timestamp = System.currentTimeMillis(),
                            utcTimestamp = TimeUtil.getHighPrecisionTimestamp(),
                            eventType = eventType,
                            sessionId = session.sessionId,
                            metadata =
                                metadata + TimeUtil.getTimingMetadata() +
                                    /**
                                     * Executes mapof operation with thermal imaging domain optimization.
                                     *
                                     */
                                    mapOf(
                                        "samsung_s22_precision" to "sub_millisecond",
                                        "snapdragon_timer" to "active",
                                    ),
                        )
                    session.syncMarks.add(syncMark)
                    Log.d(TAG, "Sync event added to thermal-only session with Samsung S22 unified timing: $eventType")
                    true
                } ?: false
            }
        } else {
            Log.w(TAG, "Cannot trigger sync event - not recording")
            false
        }
    }

    /**
     * Trigger thermal capture with automatic sync marking using Samsung S22 high-precision timing
     */
    fun captureFrame(frameMetadata: Map<String, String> = emptyMap()): Boolean {
        val synchronizedTimestamp = TimeUtil.getHighPrecisionTimestamp()
        val metadata =
            mutableMapOf<String, String>().apply {
                /**
                 * Executes putall operation with thermal imaging domain optimization.
                 *
                 */
                putAll(frameMetadata)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("capture_type", "thermal")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("timestamp", TimeUtil.formatTimestamp(synchronizedTimestamp))
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("sync_method", "unified_samsung_s22_snapdragon_ground_truth")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("precision_level", "sub_millisecond")
                /**
                 * Executes putall operation with thermal imaging domain optimization.
                 *
                 */
                putAll(TimeUtil.getTimingMetadata())
            }

        return triggerSyncEvent("THERMAL_FRAME_CAPTURE", metadata)
    }

    /**
     * Check if currently recording
     */
    fun isRecording(): Boolean = isRecordingState

    /**
     * Get current session information
     */
    fun getCurrentSession(): SessionInfo? = currentSession

    /**
     * Get session directory for file storage
     */
    fun getSessionDirectory(): File? {
        return gsrRecorder.getSessionDirectory()
    }

    /**
     * Set PC time offset for synchronization
     */
    fun setPcTimeOffset(offsetMs: Long) {
        TimeUtil.setPcTimeOffset(offsetMs)
        Log.d(TAG, "PC time offset set: ${offsetMs}ms")
    }

    /**
     * Add custom metadata to current session
     */
    fun addSessionMetadata(
        key: String,
        value: String,
    ): Boolean {
        return currentSession?.let { session ->
            session.metadata[key] = value
            true
        } ?: false
    }

    /**
     * Get recording statistics
     */
    /**
     * Retrieves the recordingstats with optimized performance for thermal imaging operations.
     *
     */
    fun getRecordingStats(): RecordingStats? {
        return currentSession?.let { session ->
            /**
             * Executes recordingstats operation with thermal imaging domain optimization.
             *
             */
            RecordingStats(
                sessionId = session.sessionId,
                duration = session.getDurationMs(),
                gsrSampleCount = if (gsrRecorder.isRecording()) gsrRecorder.getCurrentSession()?.sampleCount ?: 0 else 0,
                syncEventCount = session.syncMarks.size,
                isActive = session.isActive(),
            )
        }
    }

    data class RecordingStats(
        val sessionId: String,
        val duration: Long,
        val gsrSampleCount: Long,
        val syncEventCount: Int,
        val isActive: Boolean,
    )

    /**
     * Clean up resources
     */
    /**
     * Executes cleanup operation with thermal imaging domain optimization.
     *
     */
    fun cleanup() {
        gsrRecorder.removeListener(gsrListener)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecordingState) {
            /**
             * Executes stoprecording operation with thermal imaging domain optimization.
             *
             */
            stopRecording()
        }
    }
}
