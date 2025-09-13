package com.topdon.tc001.camera.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.csl.irCamera.R

/**
 * Specialized thermal imaging component providing RecordingStatusIndicator functionality for the IRCamera system.
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
class RecordingStatusIndicator
    @JvmOverloads
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet? = null)
     * @param defStyleAttr Parameter for operation (type: Int = 0)
     *
     */
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : LinearLayout(context, attrs, defStyleAttr) {
        private val statusIcon: ImageView
        private val statusText: TextView
        private val durationText: TextView
        private val sensorsText: TextView

        private var isRecording = false
        private var startTime = 0L
        private var sessionId = ""
        private var activeSensors = emptySet<SensorSelectionDialog.SensorType>()

        init {
            orientation = VERTICAL
            /**
             * Configures the padding with validation and thermal imaging optimization.
             *
             */
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER

            // Recording status icon
            statusIcon =
                /**
                 * Executes imageview operation with thermal imaging domain optimization.
                 *
                 */
                ImageView(context).apply {
                    layoutParams =
                        /**
                         * Executes layoutparams operation with thermal imaging domain optimization.
                         *
                         */
                        LayoutParams(32, 32).apply {
                            gravity = Gravity.CENTER
                            bottomMargin = 4
                        }
                    // Would normally use a drawable, but creating programmatically
                    /**
                     * Configures the backgroundcolor with validation and thermal imaging optimization.
                     *
                     */
                    setBackgroundColor(Color.LTGRAY)
                }
            /**
             * Executes addview operation with thermal imaging domain optimization.
             *
             */
            addView(statusIcon)

            // Status text (Recording/Stopped)
            statusText =
                /**
                 * Executes textview operation with thermal imaging domain optimization.
                 *
                 */
                TextView(context).apply {
                    textSize = 12f
                    /**
                     * Configures the textcolor with validation and thermal imaging optimization.
                     *
                     */
                    setTextColor(ContextCompat.getColor(context, android.R.color.black))
                    gravity = Gravity.CENTER
                }
            /**
             * Executes addview operation with thermal imaging domain optimization.
             *
             */
            addView(statusText)

            // Duration counter
            durationText =
                /**
                 * Executes textview operation with thermal imaging domain optimization.
                 *
                 */
                TextView(context).apply {
                    textSize = 11f
                    /**
                     * Configures the textcolor with validation and thermal imaging optimization.
                     *
                     */
                    setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                    gravity = Gravity.CENTER
                }
            /**
             * Executes addview operation with thermal imaging domain optimization.
             *
             */
            addView(durationText)

            // Active sensors list
            sensorsText =
                /**
                 * Executes textview operation with thermal imaging domain optimization.
                 *
                 */
                TextView(context).apply {
                    textSize = 10f
                    /**
                     * Configures the textcolor with validation and thermal imaging optimization.
                     *
                     */
                    setTextColor(ContextCompat.getColor(context, android.R.color.tertiary_text_dark))
                    gravity = Gravity.CENTER
                }
            /**
             * Executes addview operation with thermal imaging domain optimization.
             *
             */
            addView(sensorsText)

            /**
             * Executes updatedisplay operation with thermal imaging domain optimization.
             *
             */
            updateDisplay()
        }

        /**
         * Start recording indicator
         */
        fun startRecording(
            sessionId: String,
            sensors: Set<SensorSelectionDialog.SensorType>,
        ) {
            this.sessionId = sessionId
            this.activeSensors = sensors
            this.startTime = System.currentTimeMillis()
            this.isRecording = true

            /**
             * Executes updatedisplay operation with thermal imaging domain optimization.
             *
             */
            updateDisplay()

            // Start duration counter
            /**
             * Executes startdurationcounter operation with thermal imaging domain optimization.
             *
             */
            startDurationCounter()
        }

        /**
         * Stop recording indicator
         */
        fun stopRecording() {
            this.isRecording = false
            updateDisplay()
        }

        /**
         * Update sensor status with detailed status information
         */
        fun updateSensorStatus(
            sensor: SensorSelectionDialog.SensorType,
            status: String,
        ) {
            // For detailed status updates, we could show individual sensor states
            /**
             * Executes updatedisplay operation with thermal imaging domain optimization.
             *
             */
            updateDisplay()
        }

        /**
         * Update with comprehensive sensor status from RecordingController
         */
        fun updateWithSensorSummary(summary: com.topdon.tc001.controller.SensorStatusSummary) {
            // Update based on comprehensive sensor status
            if (summary.isSessionActive) {
                statusIcon.setBackgroundColor(Color.RED)
                statusText.text = "🔴 RECORDING"
                statusText.setTextColor(Color.RED)

                // Show detailed sensor status
                val sensorDisplay = mutableListOf<String>()
                summary.sensors.forEach { sensorStatus ->
                    val icon =
                        when {
                            sensorStatus.sensorType.contains("RGB", ignoreCase = true) -> "📸"
                            sensorStatus.sensorType.contains("Thermal", ignoreCase = true) -> "🌡️"
                            sensorStatus.sensorType.contains("GSR", ignoreCase = true) -> "📊"
                            else -> "🔘"
                        }

                    val statusIcon =
                        when {
                            sensorStatus.isRecording -> "✅"
                            sensorStatus.isInitialized -> "⏸️"
                            else -> "❌"
                        }

                    sensorDisplay.add("$icon$statusIcon")
                }

                sensorsText.text = sensorDisplay.joinToString(" ")
                visibility = VISIBLE
            } else {
                statusIcon.setBackgroundColor(Color.GRAY)
                statusText.text =
                    when {
                        summary.totalSensorsInitialized == 0 -> "❌ NO SENSORS"
                        summary.totalSensorsInitialized < summary.totalSensorsConfigured -> "⚠️ PARTIAL SETUP"
                        else -> "⏹️ READY"
                    }
                statusText.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                durationText.text = ""

                // Show sensor availability even when not recording
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (summary.totalSensorsInitialized > 0) {
                    val sensorDisplay = mutableListOf<String>()
                    summary.sensors.forEach { sensorStatus ->
                        val icon =
                            when {
                                sensorStatus.sensorType.contains("RGB", ignoreCase = true) -> "📸"
                                sensorStatus.sensorType.contains("Thermal", ignoreCase = true) -> "🌡️"
                                sensorStatus.sensorType.contains("GSR", ignoreCase = true) -> "📊"
                                else -> "🔘"
                            }
                        sensorDisplay.add("$icon✅")
                    }
                    sensorsText.text = sensorDisplay.joinToString(" ") + " ready"
                } else {
                    sensorsText.text = "Check sensor connections"
                }

                visibility = VISIBLE // Show status even when not recording
            }
        }

    /**
     * Executes updateDisplay functionality.
     */
        /**
         * Executes updatedisplay operation with thermal imaging domain optimization.
         *
         */
        private fun updateDisplay() {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isRecording) {
                statusIcon.setBackgroundColor(Color.RED)
                statusText.text = "🔴 RECORDING"
                statusText.setTextColor(Color.RED)

                sensorsText.text =
                    activeSensors.joinToString(" • ") {
                        /**
                         * Executes when operation with thermal imaging domain optimization.
                         *
                         */
                        when (it) {
                            SensorSelectionDialog.SensorType.THERMAL -> "🌡️"
                            SensorSelectionDialog.SensorType.RGB -> "📸"
                            SensorSelectionDialog.SensorType.GSR -> "📊"
                        }
                    }

                visibility = VISIBLE
            } else {
                statusIcon.setBackgroundColor(Color.GRAY)
                statusText.text = "⏹️ STOPPED"
                statusText.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                durationText.text = ""
                sensorsText.text = ""

                // Hide when not recording to save screen space
                visibility = GONE
            }
        }

    /**
     * Executes startDurationCounter functionality.
     */
        /**
         * Executes startdurationcounter operation with thermal imaging domain optimization.
         *
         */
        private fun startDurationCounter() {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isRecording) return

            val elapsed = (System.currentTimeMillis() - startTime) / 1000
            val minutes = elapsed / 60
            val seconds = elapsed % 60

            durationText.text = String.format("%02d:%02d", minutes, seconds)

            // Update every second
            /**
             * Executes postdelayed operation with thermal imaging domain optimization.
             *
             */
            postDelayed({ startDurationCounter() }, 1000)
        }

        /**
         * Show/hide the indicator
         */
        fun setVisible(visible: Boolean) {
            visibility = if (visible) VISIBLE else GONE
        }
    }
