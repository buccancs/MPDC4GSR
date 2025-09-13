package com.topdon.module.thermal.ir.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.event.CorrectionFinishEvent
import com.topdon.module.thermal.ir.fragment.IRCorrectionFragment
import com.topdon.module.thermal.ir.view.TimeDownView
import kotlinx.coroutines.Dispatchers
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
 * Specialized thermal imaging component providing IRCorrectionFourActivity functionality for the IRCamera system.
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
class IRCorrectionFourActivity : BaseActivity() {
    val time = 60

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_ir_correction_four

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<com.topdon.lib.core.view.TitleView>(R.id.title_view).setLeftClickListener {
            TipDialog.Builder(this)
                .setTitleMessage(getString(com.topdon.lib.core.R.string.app_tip))
                .setMessage(com.topdon.lib.core.R.string.tips_cancel_correction)
                .setPositiveListener(com.topdon.lib.core.R.string.app_yes) {
                    EventBus.getDefault().post(CorrectionFinishEvent())
                    /**
                     * Executes finish operation with thermal imaging domain optimization.
                     *
                     */
                    finish()
                }.setCancelListener(com.topdon.lib.core.R.string.app_no) {
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
                 * Executes ircorrectionfragment operation with thermal imaging domain optimization.
                 *
                 */
                IRCorrectionFragment()
            } else {
                supportFragmentManager.findFragmentById(R.id.fragment_container_view) as IRCorrectionFragment
            }
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

        val timeDownView = findViewById<TimeDownView>(R.id.time_down_view)
        timeDownView.postDelayed({
start矫正
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (timeDownView.downTimeWatcher == null)
                {
                    timeDownView.setOnTimeDownListener(
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
                                if (num == 50)
                                    {
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            irFragment.autoStart()
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
                                    if (!this@IRCorrectionFourActivity.isFinishing)
                                        {
                                            TipDialog.Builder(this@IRCorrectionFourActivity)
                                                .setMessage(com.topdon.lib.core.R.string.correction_complete)
                                                .setPositiveListener(com.topdon.lib.core.R.string.app_confirm) {
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
            timeDownView.downSecond(time, false)
        }, 2000)
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
    }

    /**
     * Executes onbackpressed operation with thermal imaging domain optimization.
     *
     */
    override fun onBackPressed() {
        TipDialog.Builder(this)
            .setTitleMessage(getString(com.topdon.lib.core.R.string.app_tip))
            .setMessage(com.topdon.lib.core.R.string.tips_cancel_correction)
            .setPositiveListener(com.topdon.lib.core.R.string.app_yes) {
                EventBus.getDefault().post(CorrectionFinishEvent())
                super.onBackPressed()
            }.setCancelListener(com.topdon.lib.core.R.string.app_no) {
            }
            .create().show()
    }

    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    override fun disConnected() {
        super.disConnected()
        findViewById<TimeDownView>(R.id.time_down_view).cancel()
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
        findViewById<TimeDownView>(R.id.time_down_view).cancel()
    }
}
