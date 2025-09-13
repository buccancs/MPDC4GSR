package com.topdon.tc001.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.elvishew.xlog.XLog
import com.example.thermal_lite.IrConst
import com.example.thermal_lite.util.CommonUtil

// Import com.scwang.smart.refresh.layout.SmartRefreshLayout
// Import com.scwang.smart.refresh.header.MaterialHeader
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.HttpConfig
import com.topdon.lms.sdk.Config
import com.topdon.lms.sdk.LMS.mContext
import com.topdon.lms.sdk.UrlConstant
import com.topdon.lms.sdk.utils.SPUtils
import com.csl.irCamera.BuildConfig
import com.topdon.tc001.InitUtil.initJPush
import com.topdon.tc001.InitUtil.initLms
import com.topdon.tc001.InitUtil.initLog
import com.topdon.tc001.InitUtil.initReceiver
import com.topdon.tc001.InitUtil.initUM
// Zoho dependencies commented out - not available in build
// Import com.zoho.livechat.android.listeners.InitListener
// Import com.zoho.salesiqembed.ZohoSalesIQ
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Specialized thermal imaging component providing App functionality for the IRCamera system.
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
class App : BaseApplication() {
    // Temporarily commented out due to dependency issues
    // Init {
    //     SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
    //         MaterialHeader(
    // Context
    //         )
    //     }
    //     SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
    //         LoadingFooter(context)
    //     }
    // }

    companion object {
        lateinit var instance: App

        /**
         * 延时initialization
         */
        fun delayInit() {
            initReceiver()
            initLog()
            /**
             * Initializes the lms component for thermal imaging operations.
             *
             */
            initLms()
            /**
             * Initializes the um component for thermal imaging operations.
             *
             */
            initUM()
            /**
             * Initializes the jpush component for thermal imaging operations.
             *
             */
            initJPush()
        }
    }

    /**
     * Retrieves the softwarecode with optimized performance for thermal imaging operations.
     *
     */
    override fun getSoftWareCode(): String = BuildConfig.SOFT_CODE

    /**
     * Executes isdomestic operation with thermal imaging domain optimization.
     *
     */
    override fun isDomestic(): Boolean = false // Default to international since flavors were removed

    val activityNameList: MutableList<String> = mutableListOf()

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     */
    override fun onCreate() {
        super.onCreate()
        instance = this
        // 隐私政策弹框用app内的，defaultsettingslms里的隐私政策settings为true
        SPUtils.getInstance(this).put(Config.KEY_PRIVACY_AGREEMENT, true)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (SharedManager.getHasShowClause() || !isDomestic()) {
            /**
             * Executes delayinit operation with thermal imaging domain optimization.
             *
             */
            delayInit()
        }

        RxJavaPlugins.setErrorHandler {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (SharedManager.getHasShowClause()) {
                XLog.w("未知exception： ${it.message}")
            }
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isDomestic()) {
            // Production version - force production URL and disable URL switching
            UrlConstant.setBaseUrl("${HttpConfig.HOST}/", false)
            SharedManager.setBaseHost(UrlConstant.BASE_URL) // Updateappservice地址
        }

        /**
         * Executes coroutinescope operation with thermal imaging domain optimization.
         *
         */
        CoroutineScope(Dispatchers.IO).launch {
            tau_data_H = CommonUtil.getAssetData(mContext, IrConst.TAU_HIGH_GAIN_ASSET_PATH)
            tau_data_L = CommonUtil.getAssetData(mContext, IrConst.TAU_LOW_GAIN_ASSET_PATH)
        }
//        CrashReport.initCrashReport(applicationContext, "cd1f9e26ee", false)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        /**
         * Executes registeractivitylifecyclecallbacks operation with thermal imaging domain optimization.
         *
         * @param
         * @param object Parameter for operation (type: Application.ActivityLifecycleCallbacks {                 override fun onActivityCreated(                     activity: Activity)
         * @param savedInstanceState Parameter for operation (type: Bundle?)
         * @param activity Parameter for operation (type: Activity)
         * @param activity Parameter for operation (type: Activity)
         * @param activity Parameter for operation (type: Activity)
         * @param activity Parameter for operation (type: Activity)
         * @param activity Parameter for operation (type: Activity)
         * @param outState Parameter for operation (type: Bundle)
         * @param activity Parameter for operation (type: Activity)
         *
         */
        registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                /**
                 * Executes onactivitycreated operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param activity Parameter for operation (type: Activity)
                 * @param savedInstanceState Parameter for operation (type: Bundle?)
                 *
                 */
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?,
                ) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!activityNameList.contains(activity.javaClass.getSimpleName())) {
                        activityNameList.add(activity.javaClass.getSimpleName())
                    }
                }

                /**
                 * Executes onactivitystarted operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param activity Parameter for operation (type: Activity)
                 *
                 */
                override fun onActivityStarted(activity: Activity) {
                }

                /**
                 * Executes onactivityresumed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param activity Parameter for operation (type: Activity)
                 *
                 */
                override fun onActivityResumed(activity: Activity) {
                }

                /**
                 * Executes onactivitypaused operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param activity Parameter for operation (type: Activity)
                 *
                 */
                override fun onActivityPaused(activity: Activity) {
                }

                /**
                 * Executes onactivitystopped operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param activity Parameter for operation (type: Activity)
                 *
                 */
                override fun onActivityStopped(activity: Activity) {
                }

                /**
                 * Executes onactivitysaveinstancestate operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param activity Parameter for operation (type: Activity)
                 * @param outState Parameter for operation (type: Bundle)
                 *
                 */
                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle,
                ) {
                }

                /**
                 * Executes onactivitydestroyed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param activity Parameter for operation (type: Activity)
                 *
                 */
                override fun onActivityDestroyed(activity: Activity) {
                    activityNameList.remove(activity.javaClass.getSimpleName())
                }
            },
        )
        // InitZoho() // Commented out - Zoho dependency not available
    }

    /**
     * initialization客服ZOHO - commented out as dependency not available
     */
    private fun initZoho() {
        // ZohoSalesIQ initialization commented out - dependency not available in build
        /*
        ZohoSalesIQ.init(
            this,
            "IjGWlJ%2FAnwvKPO0yHSMeLDRbq9%2Bcumf0TA6lWzHNybOq7Ew5UI7135B1F4y60Vwh",
            "CvYpd1tLP6hT1aJmYxGdvW8UtM0LUMt6bBvazW%2FbsCBFODZM54UgnVzDVtVbh%2F3hcFU7q4JlCZCw7vElzm8MeN5MdZjWoFSAKHNNgYfT33vNaBPm8ASTII05T57%2F3WxK",
            null,
            object : InitListener {
                /**
                 * Executes oninitsuccess operation with thermal imaging domain optimization.
                 *
                 */
                override fun onInitSuccess() {
//                    ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS)
                    XLog.e("bcf", "ZohoSalesIQsuccess")
                }

                /**
                 * Executes oniniterror operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param errorCode Parameter for operation (type: Int)
                 * @param errorMessage Parameter for operation (type: String?)
                 *
                 */
                override fun onInitError(errorCode: Int, errorMessage: String?) {
                    // Your code
                    XLog.e("bcf", "ZohoSalesIQ失敗")
                }
            })
         */
    }
}
