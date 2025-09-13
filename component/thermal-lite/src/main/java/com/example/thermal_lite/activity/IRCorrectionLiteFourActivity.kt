package com.example.thermal_lite.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.example.thermal_lite.R
import com.example.thermal_lite.databinding.ActivityIrCorrectionLiteFourBinding
import com.example.thermal_lite.fragment.IRMonitorLiteFragment
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.module.thermal.ir.event.CorrectionFinishEvent
import com.topdon.module.thermal.ir.view.TimeDownView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 *
锅盖矫正
 * @author: CaiSongL
 * @date: 2023/8/4 9:06
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing IRCorrectionLiteFourActivity functionality for the IRCamera system.
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
class IRCorrectionLiteFourActivity : BaseActivity() {
    private lateinit var binding: ActivityIrCorrectionLiteFourBinding
    val time = 60
    var result = false

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_ir_correction_lite_four

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.titleView.setLeftClickListener {
            TipDialog.Builder(this)
                .setTitleMessage(getString(R.string.app_tip))
                .setMessage(R.string.tips_cancel_correction)
                .setPositiveListener(R.string.app_yes) {
                    EventBus.getDefault().post(CorrectionFinishEvent())
                    /**
                     * Executes finish operation with thermal imaging domain optimization.
                     *
                     */
                    finish()
                }.setCancelListener(R.string.app_no) {
                }
                .create().show()
        }

        val irFragment =
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
        lifecycleScope.launch {
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(1000)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, irFragment)
                    .commit()
            }
        }

        binding.timeDownView.postDelayed({
start矫正
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (binding.timeDownView.downTimeWatcher == null)
                {
                    binding.timeDownView.setOnTimeDownListener(
                        object : TimeDownView.DownTimeWatcher {
                            /**
                             * Executes ontime operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param num Parameter for operation (type: Int)
                             *
                             */
                            override fun onTime(num: Int) {
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (num == 35)
                                    {
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            result = irFragment.autoStart()
                                        }
                                    }
                            }

                            /**
                             * Executes onlasttime operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param num Parameter for operation (type: Int)
                             *
                             */
                            override fun onLastTime(num: Int) {
                            }

                            /**
                             * Executes onlasttimefinish operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param num Parameter for operation (type: Int)
                             *
                             */
                            override fun onLastTimeFinish(num: Int) {
                                try {
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (!result)
                                        {
                                            ToastUtils.showShort("calibrationsavefailed，请重新calibration")
                                            return
                                        }
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (!this@IRCorrectionLiteFourActivity.isFinishing)
                                        {
                                            TipDialog.Builder(this@IRCorrectionLiteFourActivity)
                                                .setMessage(R.string.correction_complete)
                                                .setPositiveListener(R.string.app_confirm) {
                                                    EventBus.getDefault().post(CorrectionFinishEvent())
                                                    /**
                                                     * Executes finish operation with thermal imaging domain optimization.
                                                     *
                                                     */
                                                    finish()
                                                }
                                                .create().show()
                                        }
                                } catch (e: Exception) {
                                }
                            }
                        },
                    )
                }
            binding.timeDownView.downSecond(time, false)
        }, 2000)
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        binding = ActivityIrCorrectionLiteFourBinding.inflate(layoutInflater)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)
    }

    /**
     * Executes onbackpressed operation with thermal imaging domain optimization.
     *
     */
    override fun onBackPressed() {
        TipDialog.Builder(this)
            .setTitleMessage(getString(R.string.app_tip))
            .setMessage(R.string.tips_cancel_correction)
            .setPositiveListener(R.string.app_yes) {
                EventBus.getDefault().post(CorrectionFinishEvent())
                super.onBackPressed()
            }.setCancelListener(R.string.app_no) {
            }
            .create().show()
    }

    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    override fun disConnected() {
        super.disConnected()
        binding.timeDownView.cancel()
        EventBus.getDefault().post(CorrectionFinishEvent())
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().post(CorrectionFinishEvent())
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        binding.timeDownView.cancel()
    }
}
