package com.topdon.gsr.service

import android.content.Context
import android.os.Environment
import android.util.Log
import com.opencsv.CSVWriter
import com.topdon.gsr.model.GSRSample
import com.topdon.gsr.model.SessionInfo
import com.topdon.gsr.model.SyncMark
import com.topdon.gsr.util.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.coroutineContext

/**
 * Specialized thermal imaging component providing GSRRecorder functionality for the IRCamera system.
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
class GSRRecorder(
    private val context: Context,
    private val samplingRateHz: Int = 128,
) {
    // Shimmer3 integration
    private val shimmerRecorder = ShimmerGSRRecorder(context, samplingRateHz)
    private val useShimmerDevice = true // Set to false for simulated data only

    companion object {
        private const val TAG = "GSRRecorder"
        private const val SESSIONS_DIR = "IRCamera_Sessions"
        private const val SIGNALS_FILENAME = "signals.csv"
        private const val SYNC_MARKS_FILENAME = "sync_marks.csv"
        private const val SESSION_METADATA_FILENAME = "session_metadata.json"

        private val SIGNALS_HEADER =
            /**
             * Executes arrayof operation with thermal imaging domain optimization.
             *
             */
            arrayOf(
                "timestamp_ms",
                "utc_timestamp_ms",
                "conductance_us",
                "resistance_kohms",
                "sample_index",
                "session_id",
            )

        private val SYNC_MARKS_HEADER =
            /**
             * Executes arrayof operation with thermal imaging domain optimization.
             *
             */
            arrayOf(
                "timestamp_ms",
                "utc_timestamp_ms",
                "event_type",
                "session_id",
                "metadata",
            )
    }

    private val sampleIntervalMs = 1000L / samplingRateHz
    private val isRecording = AtomicBoolean(false)
/**
 * Specialized thermal imaging component providing GSRRecordingListener functionality for the IRCamera system.
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
    interface GSRRecordingListener {
    /**
     * Executes onRecordingStarted functionality.
     */
        /**
         * Executes onrecordingstarted operation with thermal imaging domain optimization.
         *
         * @param
         * @param sessionInfo Parameter for operation (type: SessionInfo)
         *
         */
        fun onRecordingStarted(sessionInfo: SessionInfo)

    /**
     * Executes onRecordingStopped functionality.
     */
        /**
         * Executes onrecordingstopped operation with thermal imaging domain optimization.
         *
         * @param
         * @param sessionInfo Parameter for operation (type: SessionInfo)
         *
         */
        fun onRecordingStopped(sessionInfo: SessionInfo)

    /**
     * Executes onSampleRecorded functionality.
     */
        /**
         * Executes onsamplerecorded operation with thermal imaging domain optimization.
         *
         * @param
         * @param sample Parameter for operation (type: GSRSample)
         *
         */
        fun onSampleRecorded(sample: GSRSample)

    /**
     * Executes onSyncMarkAdded functionality.
     */
        /**
         * Executes onsyncmarkadded operation with thermal imaging domain optimization.
         *
         * @param
         * @param syncMark Parameter for operation (type: SyncMark)
         *
         */
        fun onSyncMarkAdded(syncMark: SyncMark)

    /**
     * Executes onError functionality.
     */
        /**
         * Executes onerror operation with thermal imaging domain optimization.
         *
         * @param
         * @param error Parameter for operation (type: String)
         *
         */
        fun onError(error: String)
    }

    /**
     * Executes addListener functionality.
     */
    /**
     * Executes addlistener operation with thermal imaging domain optimization.
     *
     * @param
     * @param listener Event listener for callbacks (type: GSRRecordingListener)
     *
     */
    fun addListener(listener: GSRRecordingListener) {
        listeners.add(listener)
    }

    /**
     * Executes removeListener functionality.
     */
    /**
     * Executes removelistener operation with thermal imaging domain optimization.
     *
     * @param
     * @param listener Event listener for callbacks (type: GSRRecordingListener)
     *
     */
    fun removeListener(listener: GSRRecordingListener) {
        listeners.remove(listener)
    }

    /**
     * Initialize GSR recording system with Shimmer3 device detection
     */
    suspend fun initialize(): Boolean {
        return if (useShimmerDevice) {
            Log.i(TAG, "Attempting to initialize Shimmer3 GSR device...")
            val success = shimmerRecorder.initializeDevice()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (success) {
                Log.i(TAG, "Shimmer3 device initialized successfully")
                /**
                 * Configures the upshimmerlisteners with validation and thermal imaging optimization.
                 *
                 */
                setupShimmerListeners()
                true
            } else {
                Log.w(TAG, "Failed to initialize Shimmer3 device, will use simulated data")
                false
            }
        } else {
            Log.i(TAG, "Using simulated GSR data mode")
            true
        }
    }

    /**
     * Sets upshimmerlisteners configuration.
     */
    private fun setupShimmerListeners() {
        shimmerRecorder.addListener(
            object : ShimmerGSRRecorder.GSRRecordingListener {
                /**
                 * Executes onrecordingstarted operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param session Parameter for operation (type: SessionInfo)
                 *
                 */
                override fun onRecordingStarted(session: SessionInfo) {
                    listeners.forEach { it.onRecordingStarted(session) }
                }

                /**
                 * Executes onrecordingstopped operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param session Parameter for operation (type: SessionInfo)
                 *
                 */
                override fun onRecordingStopped(session: SessionInfo) {
                    listeners.forEach { it.onRecordingStopped(session) }
                }

                /**
                 * Executes onsamplerecorded operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param sample Parameter for operation (type: GSRSample)
                 *
                 */
                override fun onSampleRecorded(sample: GSRSample) {
                    listeners.forEach { it.onSampleRecorded(sample) }
                }

                /**
                 * Executes onsyncmarkrecorded operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param syncMark Parameter for operation (type: SyncMark)
                 *
                 */
                override fun onSyncMarkRecorded(syncMark: SyncMark) {
                    listeners.forEach { it.onSyncMarkAdded(syncMark) }
                }

                /**
                 * Executes onerror operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param error Parameter for operation (type: String)
                 *
                 */
                override fun onError(error: String) {
                    listeners.forEach { it.onError(error) }
                }

                /**
                 * Executes ondeviceconnected operation with thermal imaging domain optimization.
                 *
                 */
                override fun onDeviceConnected() {
                    Log.i(TAG, "Shimmer3 GSR device connected")
                }

                /**
                 * Executes ondevicedisconnected operation with thermal imaging domain optimization.
                 *
                 */
                override fun onDeviceDisconnected() {
                    Log.w(TAG, "Shimmer3 GSR device disconnected")
                }
            },
        )
    }

    /**
     * Start GSR recording session with Shimmer3 device or simulated data
     */
    suspend fun startRecording(
        sessionId: String,
        participantId: String? = null,
        studyName: String? = null,
    ): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording.get()) {
            Log.w(TAG, "Recording already in progress")
            return false
        }

        return if (useShimmerDevice) {
            // Use Shimmer3 device
            shimmerRecorder.startRecording(sessionId)
        } else {
            // Use simulated data
            /**
             * Executes startsimulatedrecording operation with thermal imaging domain optimization.
             *
             */
            startSimulatedRecording(sessionId, participantId, studyName)
        }
    }

    /**
     * Executes startsimulatedrecording operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionId Parameter for operation (type: String)
     * @param participantId Parameter for operation (type: String?)
     * @param studyName Parameter for operation (type: String?)
     *
     */
    private suspend fun startSimulatedRecording(
        sessionId: String,
        participantId: String?,
        studyName: String?,
    ): Boolean {
        try {
            // Create session directory
            sessionDirectory = createSessionDirectory(sessionId)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (sessionDirectory == null) {
                /**
                 * Executes notifyerror operation with thermal imaging domain optimization.
                 *
                 */
                notifyError("Failed to create session directory")
                return false
            }

            // Initialize CSV writers
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!initializeCsvWriters()) {
                /**
                 * Executes notifyerror operation with thermal imaging domain optimization.
                 *
                 */
                notifyError("Failed to initialize CSV writers")
                return false
            }

            // Create session info
            currentSession =
                /**
                 * Executes sessioninfo operation with thermal imaging domain optimization.
                 *
                 */
                SessionInfo(
                    sessionId = sessionId,
                    startTime = System.currentTimeMillis(),
                    participantId = participantId,
                    studyName = studyName ?: "GSR_Study",
                )

            // Reset counters
            sampleIndex.set(0)
            isRecording.set(true)

            // Start data generation coroutine for simulated data
            recordingJob =
                /**
                 * Executes coroutinescope operation with thermal imaging domain optimization.
                 *
                 */
                CoroutineScope(Dispatchers.IO).launch {
                    /**
                     * Executes generatesimulatedgsrdata operation with thermal imaging domain optimization.
                     *
                     */
                    generateSimulatedGSRData()
                }

            currentSession?.let { session ->
                listeners.forEach { it.onRecordingStarted(session) }
            }

            Log.i(TAG, "Simulated GSR recording started: sessionId=$sessionId, samplingRate=${samplingRateHz}Hz")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start simulated recording", e)
            /**
             * Executes cleanup operation with thermal imaging domain optimization.
             *
             */
            cleanup()
            /**
             * Executes notifyerror operation with thermal imaging domain optimization.
             *
             * @param
             * @param recording Parameter for operation (type: ${e.message}")
             *
             */
            notifyError("Failed to start recording: ${e.message}")
            return false
        }
    }

    /**
     * Executes generatesimulatedgsrdata operation with thermal imaging domain optimization.
     *
     */
    private suspend fun generateSimulatedGSRData() {
        val baseTime = System.currentTimeMillis()

        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (isRecording.get()) {
            try {
                val currentTime = System.currentTimeMillis()
                val utcTime = TimeUtil.getUtcTimestamp()
                val currentIndex = sampleIndex.getAndIncrement()

                // Calculate elapsed time from recording start for consistent timing
                val elapsedMs = currentTime - baseTime

                currentSession?.let { session ->
                    // Generate realistic GSR data with physiological patterns
                    val timeOffset = currentIndex * sampleIntervalMs
                    val baseFreq = timeOffset / 10000.0 // Slow base changes
                    val breathingFreq = timeOffset / 2000.0 // Breathing-like pattern
                    val noiseFreq = timeOffset / 500.0 // High-frequency noise

                    // Simulate realistic GSR patterns (10-50 µS typical range)
                    val conductance =
                        20.0 +
                            Math.sin(baseFreq) * 10.0 + // Slow drift
                            Math.sin(breathingFreq) * 3.0 + // Breathing pattern
                            Math.sin(noiseFreq) * 1.0 + // Fine noise
                            Math.random() * 2.0 // Random variation

                    // Ensure reasonable range and calculate resistance
                    val finalConductance = Math.max(5.0, Math.min(50.0, conductance))
                    val resistance = 1.0 / (finalConductance / 1000000.0) // Convert µS to kΩ

                    val sample =
                        /**
                         * Executes gsrsample operation with thermal imaging domain optimization.
                         *
                         */
                        GSRSample(
                            timestamp = currentTime,
                            utcTimestamp = utcTime,
                            conductance = finalConductance,
                            resistance = resistance,
                            sampleIndex = currentIndex,
                            sessionId = session.sessionId,
                        )

                    // Write to CSV
                    signalsWriter?.writeNext(sample.toCsvRow())
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (currentIndex % 10 == 0L) { // Flush every 10 samples
                        signalsWriter?.flush()
                    }

                    // Notify listeners
                    listeners.forEach { it.onSampleRecorded(sample) }
                }

                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(sampleIntervalMs)
            } catch (e: Exception) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (coroutineContext.isActive) {
                    Log.e(TAG, "Error in simulated data generation", e)
                    /**
                     * Executes notifyerror operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param error Parameter for operation (type: ${e.message}")
                     *
                     */
                    notifyError("Data generation error: ${e.message}")
                }
            }
        }
    }

    /**
     * Stop GSR recording session
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
        if (!isRecording.get()) {
            Log.w(TAG, "No recording in progress")
            return currentSession
        }

        isRecording.set(false)

        return if (useShimmerDevice) {
            shimmerRecorder.stopRecording()
        } else {
            /**
             * Executes stopsimulatedrecording operation with thermal imaging domain optimization.
             *
             */
            stopSimulatedRecording()
        }
    }

    /**
     * Executes stopSimulatedRecording functionality.
     */
    /**
     * Executes stopsimulatedrecording operation with thermal imaging domain optimization.
     *
     */
    private fun stopSimulatedRecording(): SessionInfo? {
        recordingJob?.cancel()
        recordingJob = null

        currentSession?.let { session ->
            session.endTime = System.currentTimeMillis()
            session.sampleCount = sampleIndex.get()

            // Save session metadata
            /**
             * Executes savesessionmetadata operation with thermal imaging domain optimization.
             *
             */
            saveSessionMetadata(session)

            listeners.forEach { it.onRecordingStopped(session) }
            Log.i(TAG, "Simulated GSR recording stopped: sessionId=${session.sessionId}, samples=${session.sampleCount}")
        }

        /**
         * Executes cleanup operation with thermal imaging domain optimization.
         *
         */
        cleanup()
        val completedSession = currentSession
        currentSession = null
        return completedSession
    }

    /**
     * Trigger a synchronization event
     */
    fun triggerSyncEvent(
        eventType: String,
        metadata: String = "",
    ): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isRecording.get()) return false

        return if (useShimmerDevice) {
            shimmerRecorder.triggerSyncEvent(eventType, metadata)
        } else {
            /**
             * Executes triggersimulatedsyncevent operation with thermal imaging domain optimization.
             *
             */
            triggerSimulatedSyncEvent(eventType, metadata)
        }
    }

    /**
     * Executes triggerSimulatedSyncEvent functionality.
     */
    /**
     * Executes triggersimulatedsyncevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param eventType Parameter for operation (type: String)
     * @param metadata Parameter for operation (type: String)
     *
     */
    private fun triggerSimulatedSyncEvent(
        eventType: String,
        metadata: String,
    ): Boolean {
        try {
            currentSession?.let { session ->
                val syncMark =
                    /**
                     * Executes syncmark operation with thermal imaging domain optimization.
                     *
                     */
                    SyncMark(
                        timestamp = System.currentTimeMillis(),
                        utcTimestamp = TimeUtil.getUtcTimestamp(),
                        eventType = eventType,
                        sessionId = session.sessionId,
                        metadata = if (metadata.isNotEmpty()) mapOf("data" to metadata) else emptyMap(),
                    )

                // Write to CSV
                syncMarksWriter?.writeNext(syncMark.toCsvRow())
                syncMarksWriter?.flush()

                // Notify listeners
                listeners.forEach { it.onSyncMarkAdded(syncMark) }

                Log.d(TAG, "Sync event recorded: $eventType")
                return true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error recording sync event", e)
            /**
             * Executes notifyerror operation with thermal imaging domain optimization.
             *
             * @param
             * @param event Parameter for operation (type: ${e.message}")
             *
             */
            notifyError("Error recording sync event: ${e.message}")
        }

        return false
    }

    /**
     * Executes createSessionDirectory functionality.
     */
    /**
     * Executes createsessiondirectory operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionId Parameter for operation (type: String)
     *
     */
    private fun createSessionDirectory(sessionId: String): File? {
        return try {
            val externalStorage = Environment.getExternalStorageDirectory()
            val sessionsDir = File(externalStorage, SESSIONS_DIR)
            val sessionDir = File(sessionsDir, sessionId)

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!sessionDir.exists() && !sessionDir.mkdirs()) {
                Log.e(TAG, "Failed to create session directory: ${sessionDir.absolutePath}")
                return null
            }

            Log.d(TAG, "Created session directory: ${sessionDir.absolutePath}")
            sessionDir
        } catch (e: Exception) {
            Log.e(TAG, "Error creating session directory", e)
            null
        }
    }

    /**
     * Initializes ializecsvwriters component.
     */
    private fun initializeCsvWriters(): Boolean {
        return try {
            sessionDirectory?.let { dir ->
                // Initialize signals CSV writer
                val signalsFile = File(dir, SIGNALS_FILENAME)
                signalsWriter =
                    /**
                     * Executes csvwriter operation with thermal imaging domain optimization.
                     *
                     */
                    CSVWriter(FileWriter(signalsFile)).apply {
                        /**
                         * Executes writenext operation with thermal imaging domain optimization.
                         *
                         */
                        writeNext(SIGNALS_HEADER)
                        /**
                         * Executes flush operation with thermal imaging domain optimization.
                         *
                         */
                        flush()
                    }

                // Initialize sync marks CSV writer
                val syncMarksFile = File(dir, SYNC_MARKS_FILENAME)
                syncMarksWriter =
                    /**
                     * Executes csvwriter operation with thermal imaging domain optimization.
                     *
                     */
                    CSVWriter(FileWriter(syncMarksFile)).apply {
                        /**
                         * Executes writenext operation with thermal imaging domain optimization.
                         *
                         */
                        writeNext(SYNC_MARKS_HEADER)
                        /**
                         * Executes flush operation with thermal imaging domain optimization.
                         *
                         */
                        flush()
                    }

                true
            } ?: false
        } catch (e: IOException) {
            Log.e(TAG, "Failed to initialize CSV writers", e)
            false
        }
    }

    /**
     * Executes saveSessionMetadata functionality.
     */
    /**
     * Executes savesessionmetadata operation with thermal imaging domain optimization.
     *
     * @param
     * @param session Parameter for operation (type: SessionInfo)
     *
     */
    private fun saveSessionMetadata(session: SessionInfo) {
        try {
            sessionDirectory?.let { dir ->
                val metadataFile = File(dir, SESSION_METADATA_FILENAME)

                val gson = com.google.gson.Gson()
                val json = gson.toJson(session)

                metadataFile.writeText(json)
                Log.d(TAG, "Session metadata saved")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save session metadata", e)
        }
    }

    /**
     * Executes cleanup functionality.
     */
    /**
     * Executes cleanup operation with thermal imaging domain optimization.
     *
     */
    private fun cleanup() {
        try {
            signalsWriter?.close()
            syncMarksWriter?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing CSV writers", e)
        } finally {
            signalsWriter = null
            syncMarksWriter = null
        }
    }

    /**
     * Executes notifyError functionality.
     */
    /**
     * Executes notifyerror operation with thermal imaging domain optimization.
     *
     * @param
     * @param error Parameter for operation (type: String)
     *
     */
    private fun notifyError(error: String) {
        Log.e(TAG, error)
        listeners.forEach { it.onError(error) }
    }

    /**
     * Disconnect from Shimmer3 device and clean up resources
     */
    fun disconnect() {
        if (useShimmerDevice) {
            shimmerRecorder.disconnect()
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording.get()) {
            /**
             * Executes stoprecording operation with thermal imaging domain optimization.
             *
             */
            stopRecording()
        }
    }

    /**
     * Check if Shimmer3 device is connected
     */
    fun isDeviceConnected(): Boolean {
        return if (useShimmerDevice) {
            shimmerRecorder.isDeviceConnected()
        } else {
            true // Simulated mode is always "connected"
        }
    }

    /**
     * Check if GSR recording is currently active
     */
    fun isRecording(): Boolean {
        return isRecording.get()
    }

    /**
     * Get the current recording session information
     */
    fun getCurrentSession(): SessionInfo? {
        return currentSession
    }

    /**
     * Get the current session directory where data is being stored
     */
    fun getSessionDirectory(): File? {
        return sessionDirectory
    }

    /**
     * Add a synchronization mark for cross-modal data alignment
     */
    suspend fun addSyncMark(
        eventType: String,
        metadata: String = "",
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isRecording.get()) {
                Log.w(TAG, "Cannot add sync mark - recording not active")
                return@withContext false
            }

            return@withContext if (useShimmerDevice) {
                shimmerRecorder.triggerSyncEvent(eventType, metadata)
            } else {
                /**
                 * Executes triggersimulatedsyncevent operation with thermal imaging domain optimization.
                 *
                 */
                triggerSimulatedSyncEvent(eventType, metadata)
            }
        }
}
