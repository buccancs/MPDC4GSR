package com.topdon.module.thermal.activity

import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.ui.dialog.MonitorSelectDialog
import com.topdon.module.thermal.R
import com.topdon.module.thermal.fragment.event.ThermalActionEvent
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Specialized thermal imaging component providing MonitorActivity functionality for the IRCamera system.
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
class MonitorActivity : BaseActivity(), View.OnClickListener {
    companion object {
        const val STATS_START = 101
        const val STATS_MONITOR = 102
        const val STATS_FINISH = 103
    }

    var MONITOR_ACTION = STATS_START

    private var selectType = 1 // 选取pointtype(point line area)
    private var selectIndex: ArrayList<Int> = arrayListOf() // 选取point

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_monitor

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Set toolbar title
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(com.topdon.lib.core.R.id.toolbar_lay)
        toolbar?.title = getString(R.string.main_thermal_motion)

        val blackColor = ContextCompat.getColor(this, com.topdon.lib.core.R.color.black)
        toolbar?.setBackgroundColor(blackColor)
        BarUtils.setStatusBarColor(this, blackColor)
        BarUtils.setNavBarColor(window, blackColor)
        findViewById<Button>(R.id.motion_log_btn).setOnClickListener(this)
        findViewById<Button>(R.id.motion_btn).setOnClickListener(this)
        findViewById<Button>(R.id.motion_start_btn).setOnClickListener(this)
// If (BaseApplication.instance.isConnected()) {
// MHandler.postDelayed({
//                EventBus.getDefault().post(ThermalActionEvent(action = 2001))
//            }, 300)
//        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Executes onclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param v Parameter for operation (type: View?)
     *
     */
    override fun onClick(v: View?) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (v) {
            findViewById<Button>(R.id.motion_log_btn) -> {
                NavigationManager.getInstance().build(RouterConfig.LOG_MP_CHART).navigation(this)
            }
            findViewById<Button>(R.id.motion_btn) -> {
                MonitorSelectDialog.Builder(this)
                    .setPositiveListener { select ->
                        /**
                         * Executes updateui operation with thermal imaging domain optimization.
                         *
                         */
                        updateUI()
                        /**
                         * Executes when operation with thermal imaging domain optimization.
                         *
                         */
                        when (select) {
                            1 -> EventBus.getDefault().post(ThermalActionEvent(action = 2001))
                            2 -> EventBus.getDefault().post(ThermalActionEvent(action = 2002))
                            else ->
                                EventBus.getDefault()
                                    .post(ThermalActionEvent(action = 2003))
                        }
                    }
                    .create().show()
            }
            findViewById<Button>(R.id.motion_start_btn) -> {
                NavigationManager.getInstance().build(RouterConfig.MONITOR_CHART)
                    .withInt("type", selectType)
                    .navigation(this)
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }
    }

    /**
     * Executes select functionality.
     */
    /**
     * Executes select operation with thermal imaging domain optimization.
     *
     * @param
     * @param selectType Parameter for operation (type: Int)
     * @param selectIndex Parameter for operation (type: ArrayList<Int>)
     *
     */
    fun select(
        selectType: Int,
        selectIndex: ArrayList<Int>,
    ) {
        findViewById<Button>(R.id.motion_start_btn).isEnabled = true
        this.selectType = selectType
        this.selectIndex = selectIndex
    }

    /**
     * Executes updateUI functionality.
     */
    /**
     * Executes updateui operation with thermal imaging domain optimization.
     *
     */
    private fun updateUI() {
        val motionStartBtn = findViewById<Button>(R.id.motion_start_btn)
        val motionLogBtn = findViewById<Button>(R.id.motion_log_btn)
        val motionBtn = findViewById<Button>(R.id.motion_btn)
        motionStartBtn.isEnabled = false
        motionStartBtn.visibility = View.VISIBLE
        motionLogBtn.visibility = View.GONE
        motionBtn.visibility = View.GONE
    }

秒
    /**
     * Executes updateTime functionality.
     */
    /**
     * Executes updatetime operation with thermal imaging domain optimization.
     *
     * @param
     * @param time Parameter for operation (type: Long)
     *
     */
    fun updateTime(time: Long) {
        val ss = time % 60
        val mm = time / 60 % 60
        val ssStr = String.format("%02d", ss)
        val mmStr = String.format("%02d", mm)
        findViewById<Button>(R.id.motion_start_btn).text = "$mmStr:$ssStr"
    }
}
