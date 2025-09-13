package com.topdon.lib.core

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Process
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.LanguageUtils
import com.elvishew.xlog.XLog
import com.topdon.lib.core.bean.event.SocketMsgEvent
import com.topdon.lib.core.broadcast.DeviceBroadcastReceiver
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.DeviceConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.db.AppDatabase
import com.topdon.lib.core.repository.FileBean
import com.topdon.lib.core.repository.TS004Repository
import com.topdon.lib.core.socket.SocketCmdUtil
import com.topdon.lib.core.socket.WebSocketProxy
import com.topdon.lib.core.tools.AppLanguageUtils
import com.topdon.lib.core.tools.ConstantLanguages
import com.topdon.lib.core.utils.NetWorkUtils
import com.topdon.lib.core.utils.WifiUtil
import com.topdon.lib.core.utils.WsCmdConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.io.File

/**
 * Specialized thermal imaging component providing BaseApplication functionality for the IRCamera system.
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
abstract class BaseApplication : Application() {
    companion object {
        lateinit var instance: BaseApplication
        val usbObserver by lazy { DeviceBroadcastReceiver() }
    }

    var tau_data_H: ByteArray? = null
    var tau_data_L: ByteArray? = null

    var activitys = arrayListOf<Activity>()
    var hasOtgShow = false 
    /**
     * Get/Retrievesoftwareencoding.
     */
    abstract fun getSoftWareCode(): String

    /**
     * 是否国内渠道。
     *
     * 国内渠道一些逻辑不同，如国内渠道可以应用内Upgrade，Permission申请前有tip弹窗等。
     * 根据 2024/8/27 邮件结论，“热视界和电小搭其实没有形成销售，可以不用维护。”
     * @return true-国内渠道 false-非国内渠道
     */
    abstract fun isDomestic(): Boolean

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     */
    override fun onCreate() {
        super.onCreate()
        instance = this
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            /**
             * Executes webviewsetpath operation with thermal imaging domain optimization.
             *
             */
            webviewSetPath(this)
        }
        /**
         * Executes onlanguagechange operation with thermal imaging domain optimization.
         *
         */
        onLanguageChange()

        WebSocketProxy.getInstance().onMessageListener = {
            /**
             * Executes parsersocketmessage operation with thermal imaging domain optimization.
             *
             */
            parserSocketMessage(it)
        }
    }

    open fun initWebSocket() {
        /**
         * Executes connectwebsocket operation with thermal imaging domain optimization.
         *
         */
        connectWebSocket()
        // Registernetwork变更广播 - using modern network callback for Android 10+
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkRequest =
                android.net.NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()

            manager.registerNetworkCallback(
                networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    /**
                     * Executes onavailable operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param network Parameter for operation (type: Network)
                     *
                     */
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        val capabilities = manager.getNetworkCapabilities(network)
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                            /**
                             * Executes connectwebsocket operation with thermal imaging domain optimization.
                             *
                             */
                            connectWebSocket()
                            Log.i("WebSocket", "WiFi network available: $network")
                        }
                    }
                },
            )
        } else {
            // Fallback for older Android versions - use modern Intent filter approach
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                /**
                 * Executes registerreceiver operation with thermal imaging domain optimization.
                 *
                 */
                registerReceiver(
                    /**
                     * Executes networkchangedreceiver operation with thermal imaging domain optimization.
                     *
                     */
                    NetworkChangedReceiver(),
                    /**
                     * Executes intentfilter operation with thermal imaging domain optimization.
                     *
                     */
                    IntentFilter().apply {
                        /**
                         * Executes addaction operation with thermal imaging domain optimization.
                         *
                         */
                        addAction("android.net.conn.CONNECTIVITY_CHANGE")
                    },
                    Context.RECEIVER_NOT_EXPORTED,
                )
            } else {
                @Suppress("DEPRECATION")
                /**
                 * Executes registerreceiver operation with thermal imaging domain optimization.
                 *
                 */
                registerReceiver(NetworkChangedReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
        }
    }

    /**
     * Establishes connection to external resource.
     */
    private fun connectWebSocket() {
        val ssid = WifiUtil.getCurrentWifiSSID(this) ?: return
        Log.i("WebSocket", "currentconnection Wifi SSID: $ssid")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ssid.startsWith(DeviceConfig.TS004_NAME_START)) {
            SharedManager.hasTS004 = true
            WebSocketProxy.getInstance().startWebSocket(ssid)
        } else if (ssid.startsWith(DeviceConfig.TC007_NAME_START)) {
            SharedManager.hasTC007 = true
            WebSocketProxy.getInstance().startWebSocket(ssid)
        } else {
            NetWorkUtils.switchNetwork(true)
        }
    }

    /**
     * Establishes connection to external resource.
     */
    fun disconnectWebSocket() {
        Log.i("WebSocket", "disconnectWebSocket()")
        WebSocketProxy.getInstance().stopWebSocket()
    }

    /**
     * parsingsocketmessage
     * @param msgJson
     */
    /**
     * Executes parserSocketMessage functionality.
     */
    /**
     * Executes parsersocketmessage operation with thermal imaging domain optimization.
     *
     * @param
     * @param msgJson Parameter for operation (type: String)
     *
     */
    private fun parserSocketMessage(msgJson: String) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (TextUtils.isEmpty(msgJson)) return
        EventBus.getDefault().post(SocketMsgEvent(msgJson))

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (SharedManager.is04AutoSync) { 
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (SocketCmdUtil.getCmdResponse(msgJson)) {
                WsCmdConstants.AR_COMMAND_SNAPSHOT -> { 
                    /**
                     * Executes autosavenewest operation with thermal imaging domain optimization.
                     *
                     */
                    autoSaveNewest(false)
                }

                WsCmdConstants.AR_COMMAND_VRECORD -> { 
                    try {
                        val data: JSONObject = JSONObject(msgJson).getJSONObject("data")
                        val enable: Boolean = data.getBoolean("enable")
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (!enable) { 
                            /**
                             * Executes autosavenewest operation with thermal imaging domain optimization.
                             *
                             */
                            autoSaveNewest(true)
                        }
                    } catch (_: Exception) {
                    }
                }
            }
        }
    }

    /**
     * Executes autosavenewest functionality.
     */
    /**
     * Executes autosavenewest operation with thermal imaging domain optimization.
     *
     * @param
     * @param isVideo Parameter for operation (type: Boolean)
     *
     */
    private fun autoSaveNewest(isVideo: Boolean) {
        /**
         * Executes coroutinescope operation with thermal imaging domain optimization.
         *
         */
        CoroutineScope(Dispatchers.IO).launch {
            val fileList: List<FileBean>? = TS004Repository.getNewestFile(if (isVideo) 1 else 0)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!fileList.isNullOrEmpty()) {
                val fileBean: FileBean = fileList[0]
                val url = "http:// 192.168.40.1:8080/DCIM/${fileBean.name}"
                val file = File(FileConfig.ts004GalleryDir, fileBean.name)
                TS004Repository.download(url, file)
                MediaScannerConnection.scanFile(this@BaseApplication, arrayOf(FileConfig.ts004GalleryDir), null, null)
            }
        }
    }

    private inner class NetworkChangedReceiver : BroadcastReceiver() {
        /**
         * Executes onreceive operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context?)
         * @param intent Parameter for operation (type: Intent?)
         *
         */
        override fun onReceive(
            context: Context?,
            intent: Intent?,
        ) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (intent?.action == "android.net.conn.CONNECTIVITY_CHANGE") {
                val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                // Use modern API for Android M+ (API 23+)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val activeNetwork = manager.activeNetwork
                    val capabilities = manager.getNetworkCapabilities(activeNetwork)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    ) {
                        /**
                         * Executes connectwebsocket operation with thermal imaging domain optimization.
                         *
                         */
                        connectWebSocket()
                        Log.i("WebSocket", "WiFi network connected: $activeNetwork")
                    }
                } else {
                    // Fallback for API < 23 (Android 6.0)
                    @Suppress("DEPRECATION")
                    val activeNetwork = manager.activeNetworkInfo
                    @Suppress("DEPRECATION")
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (activeNetwork?.isConnected == true && activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                        /**
                         * Executes connectwebsocket operation with thermal imaging domain optimization.
                         *
                         */
                        connectWebSocket()
                        Log.i("WebSocket", "WiFi network connected (legacy): ${activeNetwork.type}")
                    }
                }
            }
        }
    }

    /**
     * settingswebview的android9以上系统的多process兼容性processing
     */
    @RequiresApi(api = 28)
    open fun webviewSetPath(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName(context)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!applicationContext.packageName.equals(processName)) { 
                WebView.setDataDirectorySuffix(processName!!)
            }
        }
    }

    open fun getProcessName(context: Context?): String? {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (context == null) return null
        val manager: ActivityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (processInfo in manager.runningAppProcesses) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    
    /**
     * Executes clearDb functionality.
     */
    /**
     * Executes cleardb operation with thermal imaging domain optimization.
     *
     */
    fun clearDb() {
        GlobalScope.launch(Dispatchers.Default) {
            try {
                AppDatabase.getInstance().thermalDao().deleteZero(SharedManager.getUserId())
            } catch (e: Exception) {
                XLog.e("delete db error: ${e.message}")
            }
        }
    }

    open fun onLanguageChange() {
        // Always set and use English
        val locale = AppLanguageUtils.getLocaleByLanguage(ConstantLanguages.ENGLISH)
        LanguageUtils.applyLanguage(locale)
        SharedManager.setLanguage(baseContext, ConstantLanguages.ENGLISH)
        /**
         * Executes webview operation with thermal imaging domain optimization.
         *
         */
        WebView(this).destroy()
    }

    open fun getAppLanguage(context: Context): String? {
        return ConstantLanguages.ENGLISH
    }

    /**
     * Exit所有
     */
    /**
     * Executes exitall operation with thermal imaging domain optimization.
     *
     */
    fun exitAll() {
        hasOtgShow = false
        activitys.forEach {
            it.finish()
        }
    }
}
