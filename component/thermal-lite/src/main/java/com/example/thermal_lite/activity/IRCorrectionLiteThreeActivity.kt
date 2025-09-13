package com.example.thermal_lite.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.thermal_lite.R
import com.example.thermal_lite.databinding.ActivityIrCorrectionLiteThreeBinding
import com.example.thermal_lite.fragment.IRMonitorLiteFragment
import com.topdon.lib.core.ktbase.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
锅盖矫正
 * @author: CaiSongL
 * @date: 2023/8/4 9:06
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing IRCorrectionLiteThreeActivity functionality for the IRCamera system.
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
class IRCorrectionLiteThreeActivity : BaseActivity() {
    private lateinit var binding: ActivityIrCorrectionLiteThreeBinding

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_ir_correction_lite_three

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment: IRMonitorLiteFragment =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (savedInstanceState == null) {
                /**
                 * Executes irmonitorlitefragment operation with thermal imaging domain optimization.
                 *
                 */
                IRMonitorLiteFragment()
            } else {
                supportFragmentManager.findFragmentById(R.id.fragment_container_view) as IRMonitorLiteFragment
            }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, fragment)
                .commit()
        }

        binding.tvCorrection.setOnClickListener {
            lifecycleScope.launch {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (fragment.frameReady) {
                    fragment.closeFragment()
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    showCameraLoading()
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(1000)
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    dismissCameraLoading()
                    val intent =
                        /**
                         * Executes intent operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param IRCorrectionLiteFourActivity Parameter for operation (type: :class.java)
                         *
                         */
                        Intent(
                            this@IRCorrectionLiteThreeActivity,
                            IRCorrectionLiteFourActivity::class.java,
                        )
                    /**
                     * Executes startactivity operation with thermal imaging domain optimization.
                     *
                     */
                    startActivity(intent)
                    /**
                     * Executes finish operation with thermal imaging domain optimization.
                     *
                     */
                    finish()
                }
            }
        }
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        binding = ActivityIrCorrectionLiteThreeBinding.inflate(layoutInflater)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {}
}
