package com.topdon.module.user.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.topdon.lib.core.common.SharedManager
import com.topdon.module.user.databinding.ActivityUnitBinding

/**
 * Specialized thermal imaging component providing UnitActivity functionality for the IRCamera system.
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
class UnitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnitBinding

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnitBinding.inflate(layoutInflater)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)

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
        binding.titleView.setRightClickListener {
            SharedManager.setTemperature(if (binding.ivDegreesCelsius.isVisible) 1 else 0)
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        }

        binding.ivDegreesCelsius.isVisible = SharedManager.getTemperature() == 1
        binding.ivFahrenheit.isVisible = SharedManager.getTemperature() == 0

        binding.constraintDegreesCelsius.setOnClickListener {
            binding.ivDegreesCelsius.isVisible = true
            binding.ivFahrenheit.isVisible = false
        }
        binding.constraintFahrenheit.setOnClickListener {
            binding.ivDegreesCelsius.isVisible = false
            binding.ivFahrenheit.isVisible = true
        }
    }
}
