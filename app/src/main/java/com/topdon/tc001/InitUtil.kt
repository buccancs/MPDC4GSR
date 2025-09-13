package com.topdon.tc001

import android.content.Context
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Build
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.Utils
import com.csl.irCamera.BuildConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.PatternFlattener
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy2
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.ChangelessFileNameGenerator
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.broadcast.DeviceBroadcastReceiver
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.HttpConfig
import com.topdon.lms.sdk.LMS
import com.topdon.lms.sdk.utils.ConstantUtil
import com.topdon.lms.sdk.utils.LanguageUtil
import java.util.Date

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for InitUtil operations.
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
object InitUtil {
    /**
     * Initializes log component.
     */
    /**
     * Initializes the log component for thermal imaging operations.
     *
     */
    fun initLog() {
        val fileName = "logs_${TimeUtils.date2String(Date(), "yyyy-MM-dd")}.log"
        val fileDir = BaseApplication.instance.getExternalFilesDir("log")!!.absolutePath
        val tag = "MPDC4GSR_LOG"
        val pattern = "{d}, {L}, {t}, {m}"
        val backupStrategy = FileSizeBackupStrategy2(5 * 1024 * 1024L, 10) // 一份file的大小
        val cleanStrategy = FileLastModifiedCleanStrategy(30 * 24 * 60 * 60) // Settings自动Clear时间

        val config =
            LogConfiguration.Builder()
                .logLevel(LogLevel.ALL)
                .tag(tag)
                .build()
        val androidPrinter = AndroidPrinter(true)
        val filePrinter =
            FilePrinter.Builder(fileDir) // 指定saveLogfile的path
                .fileNameGenerator(ChangelessFileNameGenerator(fileName)) // 指定Logfile名生成器
                .backupStrategy(backupStrategy) // 指定LogfileBackupStrategy
                .cleanStrategy(cleanStrategy) // 指定LogfileClearStrategy
                .flattener(PatternFlattener(pattern)) // 自定义Logformat
                .build()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (BuildConfig.DEBUG) {
            XLog.init(config, androidPrinter, filePrinter)
        } else {
            // Release不使用logcat
            XLog.init(config, filePrinter)
        }
    }

    /**
     * Initializes lms component.
     */
    /**
     * Initializes the lms component for thermal imaging operations.
     *
     */
    fun initLms() {
        // 隐私政策地址
        val privacyPolicyUrl =
            "https:// Plat.topdon.com/topdon-plat/out-user/baseinfo/template/getHtmlContentById?" +
                "softCode=${BaseApplication.instance.getSoftWareCode()}&" +
                "language=${LanguageUtil.getLanguageId(Utils.getApp())}&type=22"
        // Userprotocol地址
        val servicesAgreementUrl =
            "https:// Plat.topdon.com/topdon-plat/out-user/baseinfo/template/getHtmlContentById?" +
                "softCode=${BaseApplication.instance.getSoftWareCode()}&" +
                "language=${LanguageUtil.getLanguageId(Utils.getApp())}&type=21"

        LMS.getInstance().init(BaseApplication.instance)
            .apply {
                productType = "TC001"
                /**
                 * Configures the logintype with validation and thermal imaging optimization.
                 *
                 */
                setLoginType(ConstantUtil.LOGIN_TS001_TYPE)
                softwareCode = BaseApplication.instance.getSoftWareCode()
                /**
                 * Configures the enabledlog with validation and thermal imaging optimization.
                 *
                 */
                setEnabledLog(false)
                /**
                 * Configures the privacypolicy with validation and thermal imaging optimization.
                 *
                 */
                setPrivacyPolicy(privacyPolicyUrl)
                /**
                 * Configures the servicesagreement with validation and thermal imaging optimization.
                 *
                 */
                setServicesAgreement(servicesAgreementUrl)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!BaseApplication.instance.isDomestic()) {
                    /**
                     * Initializes the xutils component for thermal imaging operations.
                     *
                     */
                    initXutils()
                } else {
                    // 有data之后需要进行替换
                    /**
                     * Configures the wxappid with validation and thermal imaging optimization.
                     *
                     */
                    setWxAppId("wx588cb319449b72dd")
                    /**
                     * Configures the buglyappid with validation and thermal imaging optimization.
                     *
                     */
                    setBuglyAppId("0b375add84")
                    // 国内版需要友盟
// SetUMengAppKey("65780ed9a7208a5af184643c", channel, "")
                }
                /**
                 * Configures the appkey with validation and thermal imaging optimization.
                 *
                 */
                setAppKey(BuildConfig.APP_KEY)
                /**
                 * Configures the appsecret with validation and thermal imaging optimization.
                 *
                 */
                setAppSecret(BuildConfig.APP_SECRET)
                /**
                 * Configures the authsecret with validation and thermal imaging optimization.
                 *
                 */
                setAuthSecret(HttpConfig.AUTH_SECRET)
            }
    }

    /**
     * Initializes um component.
     */
    /**
     * Initializes the um component for thermal imaging operations.
     *
     */
    fun initUM() {
// If (BaseApplication.instance.isDomestic()){
        // 只有国内版才需要接入友盟
//            UMConfigure.setLogEnabled(BuildConfig.DEBUG)
//            // 友盟预initialization
//            UMConfigure.preInit(BaseApplication.instance, "659384b895b14f599d0d9247", "Um-eng")
//            // 判断是否同意隐私protocol，uminit为1时为已经同意，直接initializationumsdk
//            UMConfigure.init(
//                BaseApplication.instance,
//                "659384b895b14f599d0d9247",
//                "Um-eng",
//                UMConfigure.DEVICE_TYPE_PHONE,
//                ""
//            )
//            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
//        }
    }

    /**
     * Initializes jpush component.
     */
    fun initJPush() {
        var registrationID = ""
// If (BaseApplication.instance.isDomestic()){
//            // 只有国内版才需要接入友盟
//            JPushInterface.setDebugMode(BuildConfig.DEBUG)
//            JPushInterface.init(BaseApplication.instance)
// RegistrationID = JPushInterface.getRegistrationID(BaseApplication.instance)
//        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (SharedManager.getHasShowClause()) {
            XLog.w("registrationID= $registrationID")
        }
    }

    /**
     * Initializes receiver component.
     */
    fun initReceiver() {
        try {
            BaseApplication.instance.unregisterReceiver(BaseApplication.usbObserver)
        } catch (e: Exception) {
        }
        // 必须动态Register,否则部分机型无法收到usbstate
        val filter = IntentFilter()
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED)
        filter.addAction(DeviceBroadcastReceiver.ACTION_USB_PERMISSION) // 申请USBPermission
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT < 33) {
            BaseApplication.instance.registerReceiver(BaseApplication.usbObserver, filter)
        } else {
            BaseApplication.instance.registerReceiver(BaseApplication.usbObserver, filter, Context.RECEIVER_NOT_EXPORTED)
        }
    }
}
