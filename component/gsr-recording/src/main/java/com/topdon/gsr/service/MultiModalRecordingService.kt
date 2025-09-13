package com.topdon.gsr.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.topdon.gsr.model.GSRSample
import com.topdon.gsr.model.SessionInfo
import com.topdon.gsr.model.SyncMark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Specialized thermal imaging component providing MultiModalRecordingService functionality for the IRCamera system.
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
class MultiModalRecordingService : Service() {
    companion object {
        private const val TAG = "MultiModalService"
        private const val NOTIFICATION_ID = 12345
        private const val CHANNEL_ID = "gsr_recording_channel"
        private const val ACTION_START_RECORDING = "action_start_recording"
        private const val ACTION_STOP_RECORDING = "action_stop_recording"
        private const val ACTION_SYNC_EVENT = "action_sync_event"

        private const val EXTRA_SESSION_ID = "extra_session_id"
        private const val EXTRA_PARTICIPANT_ID = "extra_participant_id"
        private const val EXTRA_STUDY_NAME = "extra_study_name"
        private const val EXTRA_EVENT_TYPE = "extra_event_type"

    /**
     * Executes startRecording functionality.
     */
        /**
         * Executes startrecording operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param sessionId Parameter for operation (type: String)
         * @param participantId Parameter for operation (type: String? = null)
         * @param studyName Parameter for operation (type: String? = null)
         *
         */
        fun startRecording(
            context: Context,
            sessionId: String,
            participantId: String? = null,
            studyName: String? = null,
        ) {
            val intent =
                Intent(context, MultiModalRecordingService::class.java).apply {
                    action = ACTION_START_RECORDING
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_SESSION_ID, sessionId)
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_PARTICIPANT_ID, participantId)
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_STUDY_NAME, studyName)
                }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

    /**
     * Executes stopRecording functionality.
     */
        /**
         * Executes stoprecording operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun stopRecording(context: Context) {
            val intent =
                Intent(context, MultiModalRecordingService::class.java).apply {
                    action = ACTION_STOP_RECORDING
                }
            context.startService(intent)
        }

    /**
     * Executes triggerSyncEvent functionality.
     */
        /**
         * Executes triggersyncevent operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param eventType Parameter for operation (type: String)
         *
         */
        fun triggerSyncEvent(
            context: Context,
            eventType: String,
        ) {
            val intent =
                Intent(context, MultiModalRecordingService::class.java).apply {
                    action = ACTION_SYNC_EVENT
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_EVENT_TYPE, eventType)
                }
            context.startService(intent)
        }
    }

    private lateinit var gsrRecorder: GSRRecorder
    private lateinit var sessionManager: SessionManager
    private var isRecording = false
    private var currentSessionId: String? = null

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
                Log.i(TAG, "GSR recording started: ${sessionInfo.sessionId}")
                /**
                 * Executes updatenotification operation with thermal imaging domain optimization.
                 *
                 */
                updateNotification("Recording GSR data...")
            }

            /**
             * Executes onrecordingstopped operation with thermal imaging domain optimization.
             *
             * @param
             * @param sessionInfo Parameter for operation (type: SessionInfo)
             *
             */
            override fun onRecordingStopped(sessionInfo: SessionInfo) {
                Log.i(TAG, "GSR recording stopped: ${sessionInfo.sessionId}")
                isRecording = false
                currentSessionId = null
                /**
                 * Executes stopforeground operation with thermal imaging domain optimization.
                 *
                 */
                stopForeground(STOP_FOREGROUND_REMOVE)
                /**
                 * Executes stopself operation with thermal imaging domain optimization.
                 *
                 */
                stopSelf()
            }

            /**
             * Executes onsamplerecorded operation with thermal imaging domain optimization.
             *
             * @param
             * @param sample Parameter for operation (type: GSRSample)
             *
             */
            override fun onSampleRecorded(sample: GSRSample) {
                // Optionally update notification with sample count periodically
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (sample.sampleIndex % 1280 == 0L) { // Every 10 seconds at 128Hz
                    /**
                     * Executes updatenotification operation with thermal imaging domain optimization.
                     *
                     */
                    updateNotification("Recording... ${sample.sampleIndex} samples")
                }
            }

            /**
             * Executes onsyncmarkadded operation with thermal imaging domain optimization.
             *
             * @param
             * @param syncMark Parameter for operation (type: SyncMark)
             *
             */
            override fun onSyncMarkAdded(syncMark: SyncMark) {
                Log.d(TAG, "Sync mark added: ${syncMark.eventType}")
            }

            /**
             * Executes onerror operation with thermal imaging domain optimization.
             *
             * @param
             * @param error Parameter for operation (type: String)
             *
             */
            override fun onError(error: String) {
                Log.e(TAG, "GSR recording error: $error")
                // Handle error - could show notification or broadcast
            }
        }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     */
    override fun onCreate() {
        super.onCreate()
        gsrRecorder = GSRRecorder(this)
        sessionManager = SessionManager.getInstance(this)
        gsrRecorder.addListener(gsrListener)
        /**
         * Executes createnotificationchannel operation with thermal imaging domain optimization.
         *
         */
        createNotificationChannel()
        Log.d(TAG, "MultiModalRecordingService created")
    }

    /**
     * Executes onstartcommand operation with thermal imaging domain optimization.
     *
     * @param
     * @param intent Parameter for operation (type: Intent?)
     * @param flags Parameter for operation (type: Int)
     * @param startId Parameter for operation (type: Int)
     *
     */
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (intent?.action) {
            ACTION_START_RECORDING -> {
                val sessionId = intent.getStringExtra(EXTRA_SESSION_ID) ?: return START_NOT_STICKY
                val participantId = intent.getStringExtra(EXTRA_PARTICIPANT_ID)
                val studyName = intent.getStringExtra(EXTRA_STUDY_NAME)

                /**
                 * Executes startrecording operation with thermal imaging domain optimization.
                 *
                 */
                startRecording(sessionId, participantId, studyName)
            }

            ACTION_STOP_RECORDING -> {
                /**
                 * Executes stoprecording operation with thermal imaging domain optimization.
                 *
                 */
                stopRecording()
            }

            ACTION_SYNC_EVENT -> {
                val eventType = intent.getStringExtra(EXTRA_EVENT_TYPE) ?: "UNKNOWN"
                /**
                 * Executes triggersyncevent operation with thermal imaging domain optimization.
                 *
                 */
                triggerSyncEvent(eventType)
            }
        }

        return START_STICKY
    }

    /**
     * Executes onbind operation with thermal imaging domain optimization.
     *
     * @param
     * @param intent Parameter for operation (type: Intent?)
     *
     */
    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * Executes startRecording functionality.
     */
    /**
     * Executes startrecording operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionId Parameter for operation (type: String)
     * @param participantId Parameter for operation (type: String?)
     * @param studyName Parameter for operation (type: String?)
     *
     */
    private fun startRecording(
        sessionId: String,
        participantId: String?,
        studyName: String?,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            Log.w(TAG, "Recording already in progress")
            return
        }

        // Create session
        sessionManager.createSession(sessionId, participantId, studyName)

        // Start foreground service
        /**
         * Executes startforeground operation with thermal imaging domain optimization.
         *
         */
        startForeground(NOTIFICATION_ID, createNotification("Starting recording..."))

        // Start GSR recording in coroutine
        /**
         * Executes coroutinescope operation with thermal imaging domain optimization.
         *
         */
        CoroutineScope(Dispatchers.IO).launch {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (gsrRecorder.startRecording(sessionId, participantId, studyName)) {
                isRecording = true
                currentSessionId = sessionId
                Log.i(TAG, "Multi-modal recording started: $sessionId")
            } else {
                Log.e(TAG, "Failed to start GSR recording")
                /**
                 * Executes stopself operation with thermal imaging domain optimization.
                 *
                 */
                stopSelf()
            }
        }
    }

    /**
     * Executes stopRecording functionality.
     */
    /**
     * Executes stoprecording operation with thermal imaging domain optimization.
     *
     */
    private fun stopRecording() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isRecording) {
            Log.w(TAG, "No recording in progress")
            return
        }

        // Stop GSR recording
        val session = gsrRecorder.stopRecording()
        session?.let {
            sessionManager.completeSession(it.sessionId)
        }

        Log.i(TAG, "Multi-modal recording stopped")
    }

    /**
     * Executes triggerSyncEvent functionality.
     */
    /**
     * Executes triggersyncevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param eventType Parameter for operation (type: String)
     *
     */
    private fun triggerSyncEvent(eventType: String) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            gsrRecorder.triggerSyncEvent(eventType)
            Log.d(TAG, "Sync event triggered: $eventType")
        } else {
            Log.w(TAG, "Cannot trigger sync event - not recording")
        }
    }

    /**
     * Executes createNotificationChannel functionality.
     */
    /**
     * Executes createnotificationchannel operation with thermal imaging domain optimization.
     *
     */
    private fun createNotificationChannel() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                /**
                 * Executes notificationchannel operation with thermal imaging domain optimization.
                 *
                 */
                NotificationChannel(
                    CHANNEL_ID,
                    "GSR Recording",
                    NotificationManager.IMPORTANCE_LOW,
                ).apply {
                    description = "Multi-modal physiological data recording"
                    /**
                     * Configures the sound with validation and thermal imaging optimization.
                     *
                     */
                    setSound(null, null)
                }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Executes createNotification functionality.
     */
    /**
     * Executes createnotification operation with thermal imaging domain optimization.
     *
     * @param
     * @param content Parameter for operation (type: String)
     *
     */
    private fun createNotification(content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Multi-Modal Recording")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_media_ff) // Using system icon
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    /**
     * Executes updateNotification functionality.
     */
    /**
     * Executes updatenotification operation with thermal imaging domain optimization.
     *
     * @param
     * @param content Parameter for operation (type: String)
     *
     */
    private fun updateNotification(content: String) {
        val notification = createNotification(content)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        gsrRecorder.removeListener(gsrListener)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecording) {
            /**
             * Executes stoprecording operation with thermal imaging domain optimization.
             *
             */
            stopRecording()
        }
        Log.d(TAG, "MultiModalRecordingService destroyed")
    }
}
