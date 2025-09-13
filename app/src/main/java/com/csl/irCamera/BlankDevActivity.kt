package com.csl.irCamera

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.csl.irCamera.databinding.ActivityBlankDevBinding
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.module.thermal.ir.activity.IRMainActivity
import com.topdon.tc001.app.App

/**
 * Specialized thermal imaging component providing BlankDevActivity functionality for the IRCamera system.
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
class BlankDevActivity : BaseBindingActivity<ActivityBlankDevBinding>() {
    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_blank_dev

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
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (SharedManager.getHasShowClause()) {
            if (!App.instance.activityNameList.contains(IRMainActivity::class.simpleName)) {
                NavigationManager.build(RouterConfig.MAIN).navigation(this)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!SharedManager.isConnectAutoOpen) {
                    NavigationManager.build(RouterConfig.IR_MAIN).navigation(this)
                }
            }
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        } else {
            startActivity(Intent(this, com.topdon.tc001.ClauseActivity::class.java))
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        }
    }

    /**
     * Executes isActivityExists functionality.
     */
    /**
     * Executes isactivityexists operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param activityClassName Parameter for operation (type: String)
     *
     */
    fun isActivityExists(
        context: Context,
        activityClassName: String,
    ): Boolean {
        val activityManager =
            context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
                ?: return false
        val tasks = activityManager.getRunningTasks(Int.MAX_VALUE)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (task in tasks) {
            if (task.topActivity != null && task.topActivity!!.className == activityClassName) {
                return true
            }
            if (task.baseActivity != null && task.baseActivity!!.className == activityClassName) {
                return true
            }
        }
        return false
    }
}
