package com.topdon.module.thermal.ir.activity

import android.content.Intent
import android.widget.TextView
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.event.CorrectionFinishEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
锅盖矫正
 * @author: CaiSongL
 * @date: 2023/8/4 9:06
 *
需要传递parameter：
- [ExtraKeyConfig.IS_TC007] - 当前device是否为 TC007
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing IRCorrectionActivity functionality for the IRCamera system.
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
class IRCorrectionActivity : BaseActivity() {
    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_ir_correction

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        findViewById<TextView>(R.id.tv_correction).setOnClickListener {
            val jumpIntent = Intent(this, IRCorrectionTwoActivity::class.java)
            jumpIntent.putExtra(ExtraKeyConfig.IS_TC007, intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false))
            /**
             * Executes startactivity operation with thermal imaging domain optimization.
             *
             */
            startActivity(jumpIntent)
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {}

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes finishCorrection functionality.
     */
    /**
     * Executes finishcorrection operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: CorrectionFinishEvent)
     *
     */
    fun finishCorrection(event: CorrectionFinishEvent) {
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }
}
