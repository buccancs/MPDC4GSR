package com.topdon.tc001.gsr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
// Removed ARouter import - using NavigationManager instead
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityGsrDemoBinding
import com.topdon.gsr.model.GSRSample
import com.topdon.gsr.model.SessionInfo
import com.topdon.gsr.model.SyncMark
import com.topdon.gsr.service.GSRRecorder
import com.topdon.gsr.util.TimeUtil
import com.topdon.lib.core.ktbase.BaseBindingActivity

/**
 * Specialized thermal imaging component providing GSRDemoActivity functionality for the IRCamera system.
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
class GSRDemoActivity : BaseBindingActivity<ActivityGsrDemoBinding>() {
    companion object {
        private const val TAG = "GSRDemoActivity"

    /**
     * Executes start functionality.
     */
        /**
         * Executes start operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun start(context: Context) {
            val intent = Intent(context, GSRDemoActivity::class.java)
            context.startActivity(intent)
        }
    }

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_gsr_demo

    private lateinit var gsrRecorder: GSRRecorder

    private var isRecording = false
    private var lastSample: GSRSample? = null

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
                runOnUiThread {
                    isRecording = true
                    /**
                     * Executes updatebuttonstates operation with thermal imaging domain optimization.
                     *
                     */
                    updateButtonStates()
                    binding.statusText.text = "Recording started: ${sessionInfo.sessionId}"
                }
            }

            /**
             * Executes onrecordingstopped operation with thermal imaging domain optimization.
             *
             * @param
             * @param sessionInfo Parameter for operation (type: SessionInfo)
             *
             */
            override fun onRecordingStopped(sessionInfo: SessionInfo) {
                runOnUiThread {
                    isRecording = false
                    /**
                     * Executes updatebuttonstates operation with thermal imaging domain optimization.
                     *
                     */
                    updateButtonStates()
                    binding.statusText.text = "Recording stopped. ${sessionInfo.sampleCount} samples recorded."

                    val sessionDir = gsrRecorder.getSessionDirectory()?.absolutePath
                    binding.dataText.text = "Session saved to:\n$sessionDir\n\n" +
                        "Files created:\n" +
                        "- signals.csv (GSR data)\n" +
                        "- sync_marks.csv (sync events)\n" +
                        "- session_metadata.json (metadata)"
                }
            }

            /**
             * Executes onsamplerecorded operation with thermal imaging domain optimization.
             *
             * @param
             * @param sample Parameter for operation (type: GSRSample)
             *
             */
            override fun onSampleRecorded(sample: GSRSample) {
                lastSample = sample

                // Update display every 32 samples (4 times per second at 128Hz)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (sample.sampleIndex % 32 == 0L) {
                    runOnUiThread {
                        binding.dataText.text =
                            buildString {
                                /**
                                 * Executes append operation with thermal imaging domain optimization.
                                 *
                                 */
                                append("Latest Sample #${sample.sampleIndex}:\n")
                                /**
                                 * Executes append operation with thermal imaging domain optimization.
                                 *
                                 * @param
                                 * @param Conductance Parameter for operation (type: ${"%.3f".format(sample.conductance)
                                 *
                                 */
                                append("Conductance: ${"%.3f".format(sample.conductance)} µS\n")
                                /**
                                 * Executes append operation with thermal imaging domain optimization.
                                 *
                                 * @param
                                 * @param Resistance Parameter for operation (type: ${"%.3f".format(sample.resistance)
                                 *
                                 */
                                append("Resistance: ${"%.3f".format(sample.resistance)} kΩ\n")
                                /**
                                 * Executes append operation with thermal imaging domain optimization.
                                 *
                                 * @param
                                 * @param Timestamp Parameter for operation (type: ${TimeUtil.formatTimestamp(sample.timestamp)
                                 *
                                 */
                                append("Timestamp: ${TimeUtil.formatTimestamp(sample.timestamp)}\n")
                                /**
                                 * Executes append operation with thermal imaging domain optimization.
                                 *
                                 * @param
                                 * @param Rate Parameter for operation (type: 128 Hz\n\n")
                                 *
                                 */
                                append("Rate: 128 Hz\n\n")

                                val duration =
                                    (
                                        System.currentTimeMillis() -
                                            (gsrRecorder.getCurrentSession()?.startTime ?: System.currentTimeMillis())
                                    ) / 1000
                                /**
                                 * Executes append operation with thermal imaging domain optimization.
                                 *
                                 * @param
                                 * @param Duration Duration in milliseconds (type: ${duration}s")
                                 *
                                 */
                                append("Recording Duration: ${duration}s")
                            }
                    }
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
                runOnUiThread {
                    Toast.makeText(
                        this@GSRDemoActivity,
                        "Sync Event: ${syncMark.eventType}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            /**
             * Executes onerror operation with thermal imaging domain optimization.
             *
             * @param
             * @param error Parameter for operation (type: String)
             *
             */
            override fun onError(error: String) {
                runOnUiThread {
                    binding.statusText.text = "Error: $error"
                    Toast.makeText(this@GSRDemoActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        // Setup click listeners
        binding.startButton.setOnClickListener { startRecording() }
        binding.stopButton.setOnClickListener { stopRecording() }
        binding.syncButton.setOnClickListener { triggerSyncEvent() }

        gsrRecorder = GSRRecorder(this)
        gsrRecorder.addListener(gsrListener)
        /**
         * Executes updatebuttonstates operation with thermal imaging domain optimization.
         *
         */
        updateButtonStates()
    }

    /**
     * Executes startRecording functionality.
     */
    /**
     * Executes startrecording operation with thermal imaging domain optimization.
     *
     */
    private fun startRecording() {
        val sessionId = TimeUtil.generateSessionId("GSRDemo")

        lifecycleScope.launch {
            val success = gsrRecorder.startRecording(sessionId, "demo_participant", "GSR_Demo_Study")

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (success) {
                Toast.makeText(this@GSRDemoActivity, "GSR recording started", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@GSRDemoActivity, "Failed to start recording", Toast.LENGTH_LONG).show()
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
        gsrRecorder.stopRecording()
    }

    /**
     * Executes triggerSyncEvent functionality.
     */
    /**
     * Executes triggersyncevent operation with thermal imaging domain optimization.
     *
     */
    private fun triggerSyncEvent() {
        val metadata =
            /**
             * Executes mapof operation with thermal imaging domain optimization.
             *
             */
            mapOf(
                "trigger" to "manual",
                "timestamp" to TimeUtil.formatTimestamp(System.currentTimeMillis()),
            )

        lifecycleScope.launch {
            val metadataJson = com.google.gson.Gson().toJson(metadata)
            val success = gsrRecorder.addSyncMark("DEMO_SYNC_EVENT", metadataJson)

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (success) {
                // Success feedback is handled in the listener
                Log.d(TAG, "Demo sync event triggered successfully")
            } else {
                Log.w(TAG, "Failed to trigger demo sync event")
            }
        }
    }

    /**
     * Executes updateButtonStates functionality.
     */
    /**
     * Executes updatebuttonstates operation with thermal imaging domain optimization.
     *
     */
    private fun updateButtonStates() {
        binding.startButton.isEnabled = !isRecording
        binding.stopButton.isEnabled = isRecording
        binding.syncButton.isEnabled = isRecording
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
            gsrRecorder.stopRecording()
        }
    }
}
