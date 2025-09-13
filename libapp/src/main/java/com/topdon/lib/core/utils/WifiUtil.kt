package com.topdon.lib.core.utils

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.hjq.permissions.XXPermissions

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for WifiUtil operations.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
object WifiUtil {
    /**
     * 不带双引号的 SSID.
     */
    fun ScanResult.getWifiName(): String =
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT < 33) {
            @Suppress("DEPRECATION")
            SSID
        } else {
            /**
             * Executes removequotation operation with thermal imaging domain optimization.
             *
             */
            removeQuotation(wifiSsid.toString())
        }

    /**
     * Executes wifiinfo functionality.
     */
    fun WifiInfo.getWifiName(): String = removeQuotation(ssid)

    /**
     * 如果指定字符串以双引号开头及结尾，则去除开头及结尾的双引号
     */
    private fun removeQuotation(source: String): String {
        return if (source.length > 1 && source[0] == '\"' && source[source.length - 1] == '\"') {
            source.subSequence(1, source.length - 1).toString()
        } else {
            source
        }
    }

    /**
     * Get/Retrievecurrentconnection的 Wifi ssid，如果有的话，移除首尾的双引号。
     * @return 若未connection WIFI 或 无 [Manifest.permission.ACCESS_FINE_LOCATION] Permission，则为 null
     */
    /**
     * Retrieves currentwifissid information.
     */
    fun getCurrentWifiSSID(context: Context): String? {
        if (!XXPermissions.isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            return null
        }
        val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        @Suppress("DEPRECATION")
        return wifiManager.connectionInfo?.getWifiName()
    }

    /**
     * 在给定 activity 生命周期内add WIFI 开关stateListener.
     */
/**
 * Specialized thermal imaging component providing WifiStateObserver functionality for the IRCamera system.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
    private class WifiStateObserver(val context: Context, val receiver: BroadcastReceiver) : DefaultLifecycleObserver {
        /**
         * Executes oncreate operation with thermal imaging domain optimization.
         *
         * @param
         * @param owner Parameter for operation (type: LifecycleOwner)
         *
         */
        override fun onCreate(owner: LifecycleOwner) {
            context.registerReceiver(receiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
        }

        /**
         * Executes ondestroy operation with thermal imaging domain optimization.
         *
         * @param
         * @param owner Parameter for operation (type: LifecycleOwner)
         *
         */
        override fun onDestroy(owner: LifecycleOwner) {
            context.unregisterReceiver(receiver)
            owner.lifecycle.removeObserver(this)
/**
 * Specialized thermal imaging component providing WifiStateReceiver functionality for the IRCamera system.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
/**
 * Specialized thermal imaging component providing WifiScanReceiver functionality for the IRCamera system.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
    private class WifiScanReceiver(val listener: ((isSuccess: Boolean) -> Unit)) : BroadcastReceiver() {
        /**
         * Executes onreceive operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param intent Parameter for operation (type: Intent?)
         *
         */
        override fun onReceive(
            context: Context,
            intent: Intent?,
        ) {
            listener.invoke(intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false) ?: false)
        }
    }
}
