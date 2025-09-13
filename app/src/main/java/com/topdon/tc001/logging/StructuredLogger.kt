package com.topdon.tc001.logging

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Specialized thermal imaging component providing StructuredLogger functionality for the IRCamera system.
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
class StructuredLogger private constructor(private val context: Context) {
    companion object {
        private const val TAG = "StructuredLogger"
        private const val LOG_DIRECTORY = "pc_to_phone_logs"
        private const val MAX_LOG_FILES = 10
        private const val MAX_LOG_SIZE_MB = 10
        private const val LOG_FLUSH_INTERVAL_MS = 5000L

        @Volatile
        private var instance: StructuredLogger? = null

    /**
     * Retrieves instance information.
     */
        fun getInstance(context: Context): StructuredLogger {
            return instance ?: synchronized(this) {
                instance ?: StructuredLogger(context.applicationContext).also { instance = it }
            }
        }

        // Convenience methods
    /**
     * Executes logInfo functionality.
     */
        /**
         * Executes loginfo operation with thermal imaging domain optimization.
         *
         * @param
         * @param component Parameter for operation (type: String)
         * @param event Parameter for operation (type: String)
         * @param details Parameter for operation (type: Map<String)
         *
         */
        fun logInfo(
            component: String,
            event: String,
            details: Map<String, Any> = emptyMap(),
        ) {
            instance?.log(LogLevel.INFO, component, event, details)
        }

    /**
     * Executes logWarning functionality.
     */
        /**
         * Executes logwarning operation with thermal imaging domain optimization.
         *
         * @param
         * @param component Parameter for operation (type: String)
         * @param event Parameter for operation (type: String)
         * @param details Parameter for operation (type: Map<String)
         *
         */
        fun logWarning(
            component: String,
            event: String,
            details: Map<String, Any> = emptyMap(),
        ) {
            instance?.log(LogLevel.WARNING, component, event, details)
        }

    /**
     * Executes logError functionality.
     */
        /**
         * Executes logerror operation with thermal imaging domain optimization.
         *
         * @param
         * @param component Parameter for operation (type: String)
         * @param event Parameter for operation (type: String)
         * @param details Parameter for operation (type: Map<String)
         *
         */
        fun logError(
            component: String,
            event: String,
            details: Map<String, Any> = emptyMap(),
        ) {
            instance?.log(LogLevel.ERROR, component, event, details)
        }

    /**
     * Executes logDebug functionality.
     */
        /**
         * Executes logdebug operation with thermal imaging domain optimization.
         *
         * @param
         * @param component Parameter for operation (type: String)
         * @param event Parameter for operation (type: String)
         * @param details Parameter for operation (type: Map<String)
         *
         */
        fun logDebug(
            component: String,
            event: String,
            details: Map<String, Any> = emptyMap(),
        ) {
            instance?.log(LogLevel.DEBUG, component, event, details)
        }
    }

/**
 * Specialized thermal imaging component providing LogLevel functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    enum class LogLevel(val value: String) {
        /**
         * Executes debug operation with thermal imaging domain optimization.
         *
         */
        DEBUG("DEBUG"),
        /**
         * Executes info operation with thermal imaging domain optimization.
         *
         */
        INFO("INFO"),
        /**
         * Executes warning operation with thermal imaging domain optimization.
         *
         */
        WARNING("WARNING"),
        /**
         * Executes error operation with thermal imaging domain optimization.
         *
         */
        ERROR("ERROR"),
    }

    private val deviceId =
        android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID,
        )

    private val logQueue = ConcurrentLinkedQueue<JSONObject>()
    private val logExecutor =
        Executors.newSingleThreadExecutor { r ->
            /**
             * Executes thread operation with thermal imaging domain optimization.
             *
             */
            Thread(r, "StructuredLogger").apply { isDaemon = true }
        }

    private var currentLogFile: File? = null
    private var currentLogWriter: BufferedWriter? = null
    private var currentLogSize = 0L
    private val dateFormatter =
        /**
         * Executes simpledateformat operation with thermal imaging domain optimization.
         *
         * @param
         * @param HH Parameter for operation (type: mm:ss.SSS'Z'")
         *
         */
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    // Message ID counter for tracking
    private var messageIdCounter = 0L

    // Coroutine scope for periodic flushing
    private val logScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        /**
         * Initializes the ializelogging component for thermal imaging operations.
         *
         */
        initializeLogging()
        /**
         * Executes startperiodicflush operation with thermal imaging domain optimization.
         *
         */
        startPeriodicFlush()
    }

    /**
     * Initializes ializelogging component.
     */
    private fun initializeLogging() {
        try {
            val logDir = File(context.getExternalFilesDir(null), LOG_DIRECTORY)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!logDir.exists()) {
                logDir.mkdirs()
            }

            // Clean up old log files
            /**
             * Executes cleanupoldlogs operation with thermal imaging domain optimization.
             *
             */
            cleanupOldLogs(logDir)

            // Create new log file
            /**
             * Executes createnewlogfile operation with thermal imaging domain optimization.
             *
             */
            createNewLogFile(logDir)

            // Log initialization
            /**
             * Executes log operation with thermal imaging domain optimization.
             *
             */
            log(
                LogLevel.INFO,
                "StructuredLogger",
                "logging_initialized",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "log_directory" to logDir.absolutePath,
                    "device_id" to deviceId,
                ),
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize structured logging", e)
        }
    }

    /**
     * Executes createNewLogFile functionality.
     */
    /**
     * Executes createnewlogfile operation with thermal imaging domain optimization.
     *
     * @param
     * @param logDir Parameter for operation (type: File)
     *
     */
    private fun createNewLogFile(logDir: File) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        currentLogFile = File(logDir, "pc_to_phone_$timestamp.jsonl")

        currentLogWriter?.close()
        currentLogWriter = BufferedWriter(FileWriter(currentLogFile, true))
        currentLogSize = currentLogFile?.length() ?: 0L

        Log.i(TAG, "Created new log file: ${currentLogFile?.name}")
    }

    /**
     * Executes cleanupOldLogs functionality.
     */
    /**
     * Executes cleanupoldlogs operation with thermal imaging domain optimization.
     *
     * @param
     * @param logDir Parameter for operation (type: File)
     *
     */
    private fun cleanupOldLogs(logDir: File) {
        try {
            val logFiles =
                logDir.listFiles { _, name ->
                    name.startsWith("pc_to_phone_") && name.endsWith(".jsonl")
                }?.sortedByDescending { it.lastModified() }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (logFiles != null && logFiles.size > MAX_LOG_FILES) {
                logFiles.drop(MAX_LOG_FILES).forEach { file ->
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (file.delete()) {
                        Log.i(TAG, "Deleted old log file: ${file.name}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error cleaning up old logs", e)
        }
    }

    /**
     * Log a structured message
     */
    /**
     * Executes log operation with thermal imaging domain optimization.
     *
     * @param
     * @param level Parameter for operation (type: LogLevel)
     * @param component Parameter for operation (type: String)
     * @param event Parameter for operation (type: String)
     * @param details Parameter for operation (type: Map<String)
     * @param connectionId Parameter for operation (type: String? = null)
     * @param messageId Parameter for operation (type: String? = null)
     *
     */
    fun log(
        level: LogLevel,
        component: String,
        event: String,
        details: Map<String, Any> = emptyMap(),
        connectionId: String? = null,
        messageId: String? = null,
    ) {
        try {
            val timestamp = dateFormatter.format(Date())
            val msgId = messageId ?: generateMessageId()

            val logEntry =
                /**
                 * Executes jsonobject operation with thermal imaging domain optimization.
                 *
                 */
                JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("ts", timestamp)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("level", level.value)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("comp", component)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("device_id", deviceId)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("conn_id", connectionId ?: "")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("msg_id", msgId)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("event", event)

                    // Add details
                    details.forEach { (key, value) ->
                        /**
                         * Executes put operation with thermal imaging domain optimization.
                         *
                         */
                        put(key, value)
                    }
                }

            // Add to queue for async processing
            logQueue.offer(logEntry)

            // Also log to Android logcat for immediate debugging
            val logMessage = "$component: $event ${if (details.isNotEmpty()) details else ""}"
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (level) {
                LogLevel.DEBUG -> Log.d(TAG, logMessage)
                LogLevel.INFO -> Log.i(TAG, logMessage)
                LogLevel.WARNING -> Log.w(TAG, logMessage)
                LogLevel.ERROR -> Log.e(TAG, logMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating log entry", e)
        }
    }

    /**
     * Log connection event with automatic connection ID tracking
     */
    fun logConnection(
        event: String,
        connectionId: String,
        details: Map<String, Any> = emptyMap(),
    ) {
        /**
         * Executes log operation with thermal imaging domain optimization.
         *
         */
        log(LogLevel.INFO, "NetworkConnection", event, details, connectionId)
    }

    /**
     * Log protocol message with automatic message ID
     */
    fun logProtocolMessage(
        event: String,
        messageId: String,
        connectionId: String? = null,
        details: Map<String, Any> = emptyMap(),
    ) {
        /**
         * Executes log operation with thermal imaging domain optimization.
         *
         */
        log(LogLevel.INFO, "ProtocolHandler", event, details, connectionId, messageId)
    }

    /**
     * Log server socket events
     */
    /**
     * Executes logserverevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: String)
     * @param details Parameter for operation (type: Map<String)
     *
     */
    fun logServerEvent(
        event: String,
        details: Map<String, Any> = emptyMap(),
    ) {
        /**
         * Executes log operation with thermal imaging domain optimization.
         *
         */
        log(LogLevel.INFO, "ServerSocket", event, details)
    }

    /**
     * Log sensor events
     */
    /**
     * Executes logsensorevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: String)
     * @param sensorType Parameter for operation (type: String)
     * @param details Parameter for operation (type: Map<String)
     *
     */
    fun logSensorEvent(
        event: String,
        sensorType: String,
        details: Map<String, Any> = emptyMap(),
    ) {
        val sensorDetails = details.toMutableMap()
        sensorDetails["sensor_type"] = sensorType
        /**
         * Executes log operation with thermal imaging domain optimization.
         *
         */
        log(LogLevel.INFO, "SensorRecorder", event, sensorDetails)
    }

    /**
     * Log recording session events
     */
    fun logSessionEvent(
        event: String,
        sessionId: String,
        details: Map<String, Any> = emptyMap(),
    ) {
        val sessionDetails = details.toMutableMap()
        sessionDetails["session_id"] = sessionId
        /**
         * Executes log operation with thermal imaging domain optimization.
         *
         */
        log(LogLevel.INFO, "RecordingSession", event, sessionDetails)
    }

    /**
     * Executes generateMessageId functionality.
     */
    /**
     * Executes generatemessageid operation with thermal imaging domain optimization.
     *
     */
    private fun generateMessageId(): String {
        return "${System.currentTimeMillis()}_${++messageIdCounter}"
    }

    /**
     * Executes startPeriodicFlush functionality.
     */
    /**
     * Executes startperiodicflush operation with thermal imaging domain optimization.
     *
     */
    private fun startPeriodicFlush() {
        logScope.launch {
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (true) {
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(LOG_FLUSH_INTERVAL_MS)
                /**
                 * Executes flushlogs operation with thermal imaging domain optimization.
                 *
                 */
                flushLogs()
            }
        }

        // Process logs in background thread
        logExecutor.execute {
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (true) {
                try {
                    /**
                     * Executes processlogqueue operation with thermal imaging domain optimization.
                     *
                     */
                    processLogQueue()
                    Thread.sleep(100) // Small delay to prevent busy waiting
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing log queue", e)
                }
            }
        }
    }

    /**
     * Executes processLogQueue functionality.
     */
    /**
     * Executes processlogqueue operation with thermal imaging domain optimization.
     *
     */
    private fun processLogQueue() {
        val writer = currentLogWriter ?: return

        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (true) {
            val logEntry = logQueue.poll() ?: break

            try {
                writer.write(logEntry.toString())
                writer.newLine()
                currentLogSize += logEntry.toString().length + 1

                // Check if we need to rotate log file
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (currentLogSize > MAX_LOG_SIZE_MB * 1024 * 1024) {
                    /**
                     * Executes rotatelogfile operation with thermal imaging domain optimization.
                     *
                     */
                    rotateLogFile()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error writing log entry", e)
            }
        }
    }

    /**
     * Executes flushLogs functionality.
     */
    /**
     * Executes flushlogs operation with thermal imaging domain optimization.
     *
     */
    private fun flushLogs() {
        try {
            currentLogWriter?.flush()
        } catch (e: Exception) {
            Log.e(TAG, "Error flushing logs", e)
        }
    }

    /**
     * Executes rotateLogFile functionality.
     */
    /**
     * Executes rotatelogfile operation with thermal imaging domain optimization.
     *
     */
    private fun rotateLogFile() {
        try {
            currentLogWriter?.close()

            val logDir = File(context.getExternalFilesDir(null), LOG_DIRECTORY)
            /**
             * Executes createnewlogfile operation with thermal imaging domain optimization.
             *
             */
            createNewLogFile(logDir)
            /**
             * Executes cleanupoldlogs operation with thermal imaging domain optimization.
             *
             */
            cleanupOldLogs(logDir)

            /**
             * Executes log operation with thermal imaging domain optimization.
             *
             */
            log(
                LogLevel.INFO,
                "StructuredLogger",
                "log_file_rotated",
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "new_file" to (currentLogFile?.name ?: "unknown"),
                    "previous_size_mb" to (currentLogSize / (1024 * 1024)),
                ),
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error rotating log file", e)
        }
    }

    /**
     * Get current log file path for debugging
     */
    fun getCurrentLogFile(): String? {
        return currentLogFile?.absolutePath
    }

    /**
     * Get log directory contents for debugging
     */
    fun getLogFiles(): List<String> {
        return try {
            val logDir = File(context.getExternalFilesDir(null), LOG_DIRECTORY)
            logDir.listFiles()?.map { it.name }?.sorted() ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting log files", e)
            /**
             * Executes emptylist operation with thermal imaging domain optimization.
             *
             */
            emptyList()
        }
    }

    /**
     * Export recent logs as string for debugging
     */
    fun exportRecentLogs(maxLines: Int = 100): String {
        return try {
            val logFile = currentLogFile ?: return "No log file available"

            val lines = mutableListOf<String>()
            /**
             * Executes bufferedreader operation with thermal imaging domain optimization.
             *
             */
            BufferedReader(FileReader(logFile)).use { reader ->
                var line: String?
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (reader.readLine().also { line = it } != null && lines.size < maxLines) {
                    line?.let { lines.add(it) }
                }
            }

            lines.takeLast(maxLines).joinToString("\n")
        } catch (e: Exception) {
            Log.e(TAG, "Error exporting logs", e)
            "Error exporting logs: ${e.message}"
        }
    }

    /**
     * Cleanup and shutdown logging
     */
    fun cleanup() {
        try {
            logScope.cancel()
            /**
             * Executes flushlogs operation with thermal imaging domain optimization.
             *
             */
            flushLogs()
            currentLogWriter?.close()
            logExecutor.shutdown()
            logExecutor.awaitTermination(5, TimeUnit.SECONDS)

            Log.i(TAG, "Structured logging cleanup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error during logging cleanup", e)
        }
    }
}
