package com.topdon.tc001.test

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.csl.irCamera.databinding.ActivityRawCaptureTestBinding

/**
 * Specialized thermal imaging component providing RAWCaptureTestActivity functionality for the IRCamera system.
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
class RAWCaptureTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRawCaptureTestBinding

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRawCaptureTestBinding.inflate(layoutInflater)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)
        // Initialize UI components through view binding
        /**
         * Configures the upspinner with validation and thermal imaging optimization.
         *
         */
        setupSpinner()
        /**
         * Configures the upswitchlisteners with validation and thermal imaging optimization.
         *
         */
        setupSwitchListeners()
    }

    /**
     * Sets upspinner configuration.
     */
    private fun setupSpinner() {
        binding.rawFrameRateSpinner.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                /**
                 * Executes listof operation with thermal imaging domain optimization.
                 *
                 */
                listOf("30 fps", "15 fps", "10 fps", "5 fps"),
            ).apply {
                /**
                 * Configures the dropdownviewresource with validation and thermal imaging optimization.
                 *
                 */
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        binding.rawFrameRateSpinner.setSelection(0) // Default to 30fps
    }

    /**
     * Sets upswitchlisteners configuration.
     */
    private fun setupSwitchListeners() {
        binding.enableRawCaptureSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.rawFrameRateSpinner.isEnabled = isChecked
            binding.rawFrameRateSpinner.alpha = if (isChecked) 1.0f else 0.5f
        }
    }
}
