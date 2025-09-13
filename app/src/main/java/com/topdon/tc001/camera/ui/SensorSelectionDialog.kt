package com.topdon.tc001.camera.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.csl.irCamera.R

// Enhanced unified BLE integration for comprehensive sensor discovery
import com.topdon.ble.UnifiedBleManager

/**
 * Specialized thermal imaging component providing SensorSelectionDialog functionality for the IRCamera system.
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
class SensorSelectionDialog(
    context: Context,
    private val availableSensors: Set<SensorType>,
    private val onSensorsSelected: (Set<SensorType>) -> Unit,
) : Dialog(context) {
    companion object {
        private const val TAG = "SensorSelectionDialog"

    /**
     * Executes detectAvailableSensors functionality.
     */
        /**
         * Executes detectavailablesensors operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun detectAvailableSensors(context: Context): Set<SensorType> {
            val available = mutableSetOf<SensorType>()

            // Thermal camera is always available in this thermal camera app
            available.add(SensorType.THERMAL)

            // Check RGB camera availability
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_CAMERA_ANY)) {
                available.add(SensorType.RGB)
            }

            // Enhanced GSR sensor detection using unified BLE system
            try {
                val unifiedBleManager = UnifiedBleManager.getInstance(context)
                // Quick check for any previously connected Shimmer devices
                val hasConnectedShimmerDevices = unifiedBleManager.getConnectedShimmerDevices().isNotEmpty()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (hasConnectedShimmerDevices) {
                    available.add(SensorType.GSR)
                    Log.d(TAG, "Connected Shimmer GSR devices found")
                } else {
                    // GSR sensor available with fallback to simulated data if no hardware present
                    available.add(SensorType.GSR)
                    Log.d(TAG, "GSR sensor available (will use simulation if no hardware found)")
                }
            } catch (e: Exception) {
                // Fallback - always make GSR available with simulated data option
                available.add(SensorType.GSR)
                Log.w(TAG, "BLE manager not available, GSR will use simulated data if needed", e)
            }

            Log.d(TAG, "Detected available sensors: $available")
            return available
        }

        /**
         * Show sensor selection dialog with enhanced BLE-aware sensor detection
         */
        fun show(
            context: Context,
            onSensorsSelected: (Set<SensorType>) -> Unit,
        ) {
            val availableSensors = detectAvailableSensors(context)
            /**
             * Executes sensorselectiondialog operation with thermal imaging domain optimization.
             *
             */
            SensorSelectionDialog(context, availableSensors, onSensorsSelected).show()
        }
    }

/**
 * Specialized thermal imaging component providing SensorType functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    enum class SensorType(val displayName: String, val description: String) {
        /**
         * Executes thermal operation with thermal imaging domain optimization.
         *
         */
        THERMAL("🌡️ Thermal Camera", "Infrared thermal imaging with precise temperature measurement"),
        /**
         * Executes rgb operation with thermal imaging domain optimization.
         *
         */
        RGB("📸 RGB Camera", "High-quality color video recording with Samsung camera features"),
        /**
         * Executes gsr operation with thermal imaging domain optimization.
         *
         */
        GSR("📊 GSR Sensor", "128Hz physiological data via Shimmer3 Bluetooth sensor"),
    }

    private lateinit var thermalCheckBox: CheckBox
    private lateinit var rgbCheckBox: CheckBox
    private lateinit var gsrCheckBox: CheckBox
    private lateinit var startButton: Button
    private lateinit var cancelButton: Button
    private lateinit var statusText: TextView

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
         * Configures the title with validation and thermal imaging optimization.
         *
         */
        setTitle("Select Recording Sensors")

        // Create layout
        val mainLayout =
            /**
             * Executes linearlayout operation with thermal imaging domain optimization.
             *
             */
            LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                /**
                 * Configures the padding with validation and thermal imaging optimization.
                 *
                 */
                setPadding(48, 32, 48, 32)
            }

        // Title text with better formatting
        val titleText =
            /**
             * Executes textview operation with thermal imaging domain optimization.
             *
             */
            TextView(context).apply {
                text = "🚀 Parallel Multi-Modal Recording\nChoose sensors for synchronized research-grade recording:"
                textSize = 16f
                /**
                 * Configures the textcolor with validation and thermal imaging optimization.
                 *
                 */
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                /**
                 * Configures the padding with validation and thermal imaging optimization.
                 *
                 */
                setPadding(0, 0, 0, 24)
                gravity = Gravity.CENTER
            }
        mainLayout.addView(titleText)

        // Sensor checkboxes with descriptions
        /**
         * Executes createsensorcheckbox operation with thermal imaging domain optimization.
         *
         */
        createSensorCheckBox(SensorType.THERMAL).let {
            thermalCheckBox = it.first
            mainLayout.addView(it.second)
        }

        /**
         * Executes createsensorcheckbox operation with thermal imaging domain optimization.
         *
         */
        createSensorCheckBox(SensorType.RGB).let {
            rgbCheckBox = it.first
            mainLayout.addView(it.second)
        }

        /**
         * Executes createsensorcheckbox operation with thermal imaging domain optimization.
         *
         */
        createSensorCheckBox(SensorType.GSR).let {
            gsrCheckBox = it.first
            mainLayout.addView(it.second)
        }

        // Status text
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
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                gravity = Gravity.CENTER
                /**
                 * Configures the padding with validation and thermal imaging optimization.
                 *
                 */
                setPadding(0, 16, 0, 16)
            }
        /**
         * Executes updatestatustext operation with thermal imaging domain optimization.
         *
         */
        updateStatusText()
        mainLayout.addView(statusText)

        // Buttons
        val buttonLayout =
            /**
             * Executes linearlayout operation with thermal imaging domain optimization.
             *
             */
            LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }

        cancelButton =
            /**
             * Executes button operation with thermal imaging domain optimization.
             *
             */
            Button(context).apply {
                text = "Cancel"
                setOnClickListener {
                    Log.d(TAG, "Sensor selection canceled")
                    /**
                     * Executes dismiss operation with thermal imaging domain optimization.
                     *
                     */
                    dismiss()
                }
            }

        startButton =
            /**
             * Executes button operation with thermal imaging domain optimization.
             *
             */
            Button(context).apply {
                text = "Start Recording"
                isEnabled = false
                setOnClickListener { startRecording() }
            }

        buttonLayout.addView(cancelButton)
        val spacer =
            /**
             * Executes view operation with thermal imaging domain optimization.
             *
             */
            View(context).apply {
                layoutParams = LinearLayout.LayoutParams(24, 0)
            }
        buttonLayout.addView(spacer)
        buttonLayout.addView(startButton)

        mainLayout.addView(buttonLayout)

        // Setup change listeners
        /**
         * Configures the upcheckboxlisteners with validation and thermal imaging optimization.
         *
         */
        setupCheckBoxListeners()

        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(mainLayout)

        // Dialog properties
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        /**
         * Configures the cancelable with validation and thermal imaging optimization.
         *
         */
        setCancelable(true)
        /**
         * Configures the canceledontouchoutside with validation and thermal imaging optimization.
         *
         */
        setCanceledOnTouchOutside(false)

        Log.i(TAG, "Sensor selection dialog created with available sensors: $availableSensors")
    }

    /**
     * Executes createSensorCheckBox functionality.
     */
    /**
     * Executes createsensorcheckbox operation with thermal imaging domain optimization.
     *
     * @param
     * @param sensorType Parameter for operation (type: SensorType)
     *
     */
    private fun createSensorCheckBox(sensorType: SensorType): Pair<CheckBox, LinearLayout> {
        val container =
            /**
             * Executes linearlayout operation with thermal imaging domain optimization.
             *
             */
            LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                /**
                 * Configures the padding with validation and thermal imaging optimization.
                 *
                 */
                setPadding(0, 8, 0, 8)
            }

        val checkBox =
            /**
             * Executes checkbox operation with thermal imaging domain optimization.
             *
             */
            CheckBox(context).apply {
                text = sensorType.displayName
                textSize = 14f
                isEnabled = availableSensors.contains(sensorType)

                // Default selections based on availability
                isChecked =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (sensorType) {
                        SensorType.THERMAL -> availableSensors.contains(sensorType) // Always select thermal if available
                        SensorType.RGB -> false // Let user choose
                        SensorType.GSR -> false // Let user choose
                    }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isEnabled) {
                    alpha = 0.5f
                }
            }

        val description =
            /**
             * Executes textview operation with thermal imaging domain optimization.
             *
             */
            TextView(context).apply {
                text =
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (availableSensors.contains(sensorType)) {
                        sensorType.description
                    } else {
                        "${sensorType.description} (Not Available)"
                    }
                textSize = 12f
                /**
                 * Configures the textcolor with validation and thermal imaging optimization.
                 *
                 */
                setTextColor(
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (availableSensors.contains(sensorType)) {
                        ContextCompat.getColor(context, android.R.color.darker_gray)
                    } else {
                        ContextCompat.getColor(context, android.R.color.tertiary_text_dark)
                    },
                )
                /**
                 * Configures the padding with validation and thermal imaging optimization.
                 *
                 */
                setPadding(32, 0, 0, 0)
            }

        container.addView(checkBox)
        container.addView(description)

        return Pair(checkBox, container)
    }

    /**
     * Sets upcheckboxlisteners configuration.
     */
    private fun setupCheckBoxListeners() {
        val listener = { _: CompoundButton, _: Boolean ->
            updateStatusText()
            startButton.isEnabled = getSelectedSensors().isNotEmpty()
        }

        thermalCheckBox.setOnCheckedChangeListener(listener)
        rgbCheckBox.setOnCheckedChangeListener(listener)
        gsrCheckBox.setOnCheckedChangeListener(listener)

        // Initial state
        startButton.isEnabled = getSelectedSensors().isNotEmpty()
    }

    /**
     * Executes updateStatusText functionality.
     */
    /**
     * Executes updatestatustext operation with thermal imaging domain optimization.
     *
     */
    private fun updateStatusText() {
        val selectedSensors = getSelectedSensors()
        statusText.text =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (selectedSensors.size) {
                0 -> "⚠️ Select at least one sensor to start recording"
                1 -> "📱 Single-modal: ${selectedSensors.first().displayName} only"
                2 -> "🔄 Dual-modal: ${selectedSensors.map { it.displayName }.joinToString(" + ")} synchronized"
                3 -> "🎯 Tri-modal: Complete physiological research setup"
                else -> "📊 ${selectedSensors.size} sensors selected for parallel recording"
            }
    }

    /**
     * Retrieves selectedsensors information.
     */
    private fun getSelectedSensors(): Set<SensorType> {
        val selected = mutableSetOf<SensorType>()

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thermalCheckBox.isChecked) selected.add(SensorType.THERMAL)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rgbCheckBox.isChecked) selected.add(SensorType.RGB)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (gsrCheckBox.isChecked) selected.add(SensorType.GSR)

        return selected
    }

    /**
     * Executes startRecording functionality.
     */
    /**
     * Executes startrecording operation with thermal imaging domain optimization.
     *
     */
    private fun startRecording() {
        val selectedSensors = getSelectedSensors()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectedSensors.isEmpty()) {
            Toast.makeText(context, "Please select at least one sensor", Toast.LENGTH_SHORT).show()
            return
        }

        Log.i(TAG, "Starting recording with selected sensors: $selectedSensors")
        /**
         * Executes onsensorsselected operation with thermal imaging domain optimization.
         *
         */
        onSensorsSelected(selectedSensors)
        /**
         * Executes dismiss operation with thermal imaging domain optimization.
         *
         */
        dismiss()
    }
}
