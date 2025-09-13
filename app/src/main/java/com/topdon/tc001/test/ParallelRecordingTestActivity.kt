package com.topdon.tc001.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.topdon.tc001.camera.ui.RecordingStatusIndicator
import com.topdon.tc001.controller.RecordingController
import kotlinx.coroutines.launch
import java.io.File

/**
 * Test activity to demonstrate the robust parallel recording functionality.
 *
 * This activity allows testing of the enhanced RecordingController that can handle
 * individual sensor failures gracefully without aborting the entire session.
 *
 * Features tested:
 * - Sensor initialization with partial failures
 * - Recording start with some sensors failing
 * - Real-time status monitoring
 * - Graceful session continuation with available sensors
 */
/**
 * Specialized thermal imaging component providing ParallelRecordingTestActivity functionality for the IRCamera system.
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
class ParallelRecordingTestActivity : ComponentActivity() {
    companion object {
        private const val TAG = "ParallelRecordingTest"
    }

    private lateinit var recordingController: RecordingController
    private lateinit var statusIndicator: RecordingStatusIndicator
    private lateinit var statusText: TextView
    private lateinit var initializeButton: Button
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var testButton: Button

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create simple test UI programmatically (since we don't have layout files in this PR)
        /**
         * Executes createtestui operation with thermal imaging domain optimization.
         *
         */
        createTestUI()

        // Initialize recording controller
        recordingController = RecordingController(this, this)

        /**
         * Configures the upeventhandlers with validation and thermal imaging optimization.
         *
         */
        setupEventHandlers()
        /**
         * Executes updateui operation with thermal imaging domain optimization.
         *
         */
        updateUI()
    }

    /**
     * Executes createTestUI functionality.
     */
    /**
     * Executes createtestui operation with thermal imaging domain optimization.
     *
     */
    private fun createTestUI() {
        // Create simple vertical layout programmatically
        val layout =
            android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                /**
                 * Configures the padding with validation and thermal imaging optimization.
                 *
                 */
                setPadding(32, 32, 32, 32)
            }

        // Title
        val titleText =
            /**
             * Executes textview operation with thermal imaging domain optimization.
             *
             */
            TextView(this).apply {
                text = "Parallel Recording Test"
                textSize = 20f
                /**
                 * Configures the padding with validation and thermal imaging optimization.
                 *
                 */
                setPadding(0, 0, 0, 24)
            }
        layout.addView(titleText)

        // Status indicator
        statusIndicator = RecordingStatusIndicator(this)
        layout.addView(statusIndicator)

        // Status text
        statusText =
            /**
             * Executes textview operation with thermal imaging domain optimization.
             *
             */
            TextView(this).apply {
                text = "Press 'Initialize Sensors' to begin"
                /**
                 * Configures the padding with validation and thermal imaging optimization.
                 *
                 */
                setPadding(0, 16, 0, 16)
            }
        layout.addView(statusText)

        // Buttons
        initializeButton =
            /**
             * Executes button operation with thermal imaging domain optimization.
             *
             */
            Button(this).apply {
                text = "Initialize Sensors"
            }
        layout.addView(initializeButton)

        startButton =
            /**
             * Executes button operation with thermal imaging domain optimization.
             *
             */
            Button(this).apply {
                text = "Start Recording"
                isEnabled = false
            }
        layout.addView(startButton)

        stopButton =
            /**
             * Executes button operation with thermal imaging domain optimization.
             *
             */
            Button(this).apply {
                text = "Stop Recording"
                isEnabled = false
            }
        layout.addView(stopButton)

        testButton =
            /**
             * Executes button operation with thermal imaging domain optimization.
             *
             */
            Button(this).apply {
                text = "Test Sensor Connections"
                isEnabled = false
            }
        layout.addView(testButton)

        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(layout)
    }

    /**
     * Sets upeventhandlers configuration.
     */
    private fun setupEventHandlers() {
        initializeButton.setOnClickListener {
            initializeSensors()
        }

        startButton.setOnClickListener {
            /**
             * Executes startrecording operation with thermal imaging domain optimization.
             *
             */
            startRecording()
        }

        stopButton.setOnClickListener {
            /**
             * Executes stoprecording operation with thermal imaging domain optimization.
             *
             */
            stopRecording()
        }

        testButton.setOnClickListener {
            /**
             * Executes testsensorconnections operation with thermal imaging domain optimization.
             *
             */
            testSensorConnections()
        }
    }

    /**
     * Initializes ializesensors component.
     */
    private fun initializeSensors() {
        lifecycleScope.launch {
            try {
                statusText.text = "Initializing sensors..."
                Log.i(TAG, "Starting sensor initialization")

                val success = recordingController.initializeSensors()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    val summary = recordingController.getSensorStatusSummary()
                    statusText.text = "Initialization complete: ${summary.totalSensorsInitialized}/3 sensors available\n${summary.statusMessage}"

                    statusIndicator.updateWithSensorSummary(summary)

                    // Enable controls
                    startButton.isEnabled = true
                    testButton.isEnabled = true
                    initializeButton.isEnabled = false

                    Log.i(TAG, "Sensor initialization successful: ${summary.totalSensorsInitialized} sensors")
                } else {
                    statusText.text = "Sensor initialization failed - no sensors available"
                    Log.e(TAG, "All sensor initialization failed")
                }
            } catch (e: Exception) {
                statusText.text = "Initialization error: ${e.message}"
                Log.e(TAG, "Sensor initialization error", e)
            }
        }
    }

    /**
     * Executes startRecording functionality.
     */
    /**
     * Executes startrecording operation with thermal imaging domain optimization.
     *
     */
    private fun startRecording() {
        lifecycleScope.launch {
            try {
                statusText.text = "Starting recording..."

                // Create session directory
                val sessionDir = File(getExternalFilesDir(null), "test_session_${System.currentTimeMillis()}")

                val success = recordingController.startRecording(sessionDir.absolutePath)

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    val summary = recordingController.getSensorStatusSummary()
                    statusText.text = "Recording started!\n${summary.statusMessage}"

                    statusIndicator.updateWithSensorSummary(summary)

                    // Update button states
                    startButton.isEnabled = false
                    stopButton.isEnabled = true
                    testButton.isEnabled = false

                    Log.i(TAG, "Recording started with ${summary.totalSensorsRecording} sensors")
                } else {
                    statusText.text = "Failed to start recording - no sensors available"
                    Log.e(TAG, "Recording start failed")
                }
            } catch (e: Exception) {
                statusText.text = "Recording start error: ${e.message}"
                Log.e(TAG, "Recording start error", e)
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
        lifecycleScope.launch {
            try {
                statusText.text = "Stopping recording..."

                val success = recordingController.stopRecording()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    val summary = recordingController.getSensorStatusSummary()
                    statusText.text = "Recording stopped.\n${summary.statusMessage}"

                    statusIndicator.updateWithSensorSummary(summary)

                    // Update button states
                    startButton.isEnabled = true
                    stopButton.isEnabled = false
                    testButton.isEnabled = true

                    Log.i(TAG, "Recording stopped successfully")
                } else {
                    statusText.text = "Warning: Some sensors may not have stopped cleanly"
                    Log.w(TAG, "Recording stop had issues")
                }
            } catch (e: Exception) {
                statusText.text = "Recording stop error: ${e.message}"
                Log.e(TAG, "Recording stop error", e)
            }
        }
    }

    /**
     * Executes testSensorConnections functionality.
     */
    /**
     * Executes testsensorconnections operation with thermal imaging domain optimization.
     *
     */
    private fun testSensorConnections() {
        lifecycleScope.launch {
            try {
                statusText.text = "Testing sensor connections..."

                val testResults = recordingController.testSensorConnections()

                val resultText =
                    buildString {
                        /**
                         * Executes appendline operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param Results Parameter for operation (type: ")
                         *
                         */
                        appendLine("Sensor Connection Test Results:")
                        testResults.forEach { (sensorId, success) ->
                            val status = if (success) "✅ OK" else "❌ FAILED"
                            /**
                             * Executes appendline operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param sensorId Parameter for operation (type: $status")
                             *
                             */
                            appendLine("$sensorId: $status")
                        }
                    }

                statusText.text = resultText
                Log.i(TAG, "Sensor test complete: $testResults")
            } catch (e: Exception) {
                statusText.text = "Sensor test error: ${e.message}"
                Log.e(TAG, "Sensor test error", e)
            }
        }
    }

    /**
     * Executes updateUI functionality.
     */
    /**
     * Executes updateui operation with thermal imaging domain optimization.
     *
     */
    private fun updateUI() {
        // Initial UI state
        statusIndicator.visibility = android.view.View.VISIBLE
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()

        // Clean up recording controller
        lifecycleScope.launch {
            try {
                recordingController.cleanup()
            } catch (e: Exception) {
                Log.e(TAG, "Cleanup error", e)
            }
        }
    }
}
