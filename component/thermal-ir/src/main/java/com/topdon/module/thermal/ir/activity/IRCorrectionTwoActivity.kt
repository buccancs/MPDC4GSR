package com.topdon.module.thermal.ir.activity

import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.socket.WebSocketProxy
import com.topdon.lib.core.tools.DeviceTools
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
 * Specialized thermal imaging component providing IRCorrectionTwoActivity functionality for the IRCamera system.
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
class IRCorrectionTwoActivity : BaseActivity() {
    /**
From上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    // Modern findViewById references
    private lateinit var tvCorrection: TextView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_ir_correction_two

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        isTC007 = intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false)

        val ivSketchMap = findViewById<ImageView>(R.id.iv_sketch_map)
        tvCorrection = findViewById(R.id.tv_correction)

        ivSketchMap.setImageResource(if (isTC007) R.drawable.ic_corrected_tc007 else R.drawable.ic_corrected_line)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (if (isTC007) WebSocketProxy.getInstance().isTC007Connect() else DeviceTools.isConnect()) {
            tvCorrection.setBackgroundResource(com.topdon.lib.core.R.drawable.bg_corners05_solid_theme)
        } else {
            tvCorrection.setBackgroundResource(com.topdon.lib.core.R.drawable.bg_corners05_solid_50_theme)
        }

        tvCorrection.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (if (isTC007) WebSocketProxy.getInstance().isTC007Connect() else DeviceTools.isConnect()) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isTC007) {
                    NavigationManager.getInstance().build(RouterConfig.IR_CORRECTION_07).navigation(this)
                } else {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (DeviceTools.isTC001LiteConnect())
                        {
                            NavigationManager.getInstance().build(RouterConfig.IR_CORRECTION_THREE_LITE).navigation(this)
                        } else if (DeviceTools.isHikConnect()) {
                        NavigationManager.getInstance().build(RouterConfig.IR_HIK_CORRECT_THREE).navigation(this)
                    } else
                        {
                            startActivity(Intent(this, IRCorrectionThreeActivity::class.java))
                        }
                }
            }
        }
    }

    /**
     * Executes connected operation with thermal imaging domain optimization.
     *
     */
    override fun connected() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTC007) {
            tvCorrection.setBackgroundResource(com.topdon.lib.core.R.drawable.bg_corners05_solid_theme)
        }
    }

    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    override fun disConnected() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTC007) {
            tvCorrection.setBackgroundResource(com.topdon.lib.core.R.drawable.bg_corners05_solid_50_theme)
        }
    }

    /**
     * Executes onsocketconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTS004 Parameter for operation (type: Boolean)
     *
     */
    override fun onSocketConnected(isTS004: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007 && !isTS004) {
            tvCorrection.setBackgroundResource(com.topdon.lib.core.R.drawable.bg_corners05_solid_theme)
        }
    }

    /**
     * Executes onsocketdisconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTS004 Parameter for operation (type: Boolean)
     *
     */
    override fun onSocketDisConnected(isTS004: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007 && !isTS004) {
            tvCorrection.setBackgroundResource(com.topdon.lib.core.R.drawable.bg_corners05_solid_50_theme)
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
