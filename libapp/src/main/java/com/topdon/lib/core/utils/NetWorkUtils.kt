package com.topdon.lib.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import com.elvishew.xlog.XLog
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.repository.TS004Repository

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for NetWorkUtils operations.
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
object NetWorkUtils {
    private var mNetworkCallback: ConnectivityManager.NetworkCallback? = null
    private var netWorkListener: ((network: Network?) -> Unit)? = null
    val connectivityManager by lazy {
        BaseApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val wifiManager by lazy {
        BaseApplication.instance.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    /**
     * Executes iswifinamevalid functionality.
     */
    /**
     * Executes iswifinamevalid operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param prefixes Parameter for operation (type: List<String>)
     *
     */
    fun isWifiNameValid(
        context: Context,
        prefixes: List<String>,
    ): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        @Suppress("DEPRECATION")
        val wifiInfo = wifiManager.connectionInfo
        val ssid = wifiInfo.ssid.replace("\"", "") 
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (prefix in prefixes) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ssid.startsWith(prefix)) {
                return true
            }
        }
        return false
    }

    /**
     * Establishes connection to external resource.
     */
    fun connectWifi(
        ssid: String,
        password: String,
        listener: ((network: Network?) -> Unit)? = null,
    ) {
        netWorkListener = listener
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT < 29) { // 低于 Android10
            val request =
                NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) // 不需要能访问 internet
                    .build()
            val callback =
                object : ConnectivityManager.NetworkCallback() {
                    /**
                     * Executes onavailable operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param network Parameter for operation (type: Network)
                     *
                     */
                    override fun onAvailable(network: Network) {
                        XLog.e("Test", "onAvailable")
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (WifiUtil.getCurrentWifiSSID(BaseApplication.instance) == ssid) {
                            connectivityManager.unregisterNetworkCallback(this)
                            listener?.invoke(network)
                        }
                    }

                    /**
                     * Executes onunavailable operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onUnavailable() {
                        XLog.e("Test", "onUnavailable")
                        connectivityManager.unregisterNetworkCallback(this)
                        listener?.invoke(null)
                    }

                    /**
                     * Executes oncapabilitieschanged operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param network Parameter for operation (type: Network)
                     * @param networkCapabilities Parameter for operation (type: NetworkCapabilities)
                     *
                     */
                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities,
                    ) {
                        XLog.e("Test", "onCapabilitiesChanged")
                        super.onCapabilitiesChanged(network, networkCapabilities)
                    }

                    /**
                     * Executes onblockedstatuschanged operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param network Parameter for operation (type: Network)
                     * @param blocked Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onBlockedStatusChanged(
                        network: Network,
                        blocked: Boolean,
                    ) {
                        super.onBlockedStatusChanged(network, blocked)
                        XLog.e("Test", "onBlockedStatusChanged")
                    }

                    /**
                     * Executes onlinkpropertieschanged operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param network Parameter for operation (type: Network)
                     * @param linkProperties Parameter for operation (type: LinkProperties)
                     *
                     */
                    override fun onLinkPropertiesChanged(
                        network: Network,
                        linkProperties: LinkProperties,
                    ) {
                        super.onLinkPropertiesChanged(network, linkProperties)
                        XLog.e("Test", "onLinkPropertiesChanged")
                    }

                    /**
                     * Executes onlosing operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param network Parameter for operation (type: Network)
                     * @param maxMsToLive Parameter for operation (type: Int)
                     *
                     */
                    override fun onLosing(
                        network: Network,
                        maxMsToLive: Int,
                    ) {
                        super.onLosing(network, maxMsToLive)
                        XLog.e("Test", "onLosing")
                    }
                }
            connectivityManager.registerNetworkCallback(request, callback)

            @Suppress("DEPRECATION")
            val configuration = WifiConfiguration()
            @Suppress("DEPRECATION")
            configuration.SSID = "\"$ssid\""
            @Suppress("DEPRECATION")
            configuration.preSharedKey = "\"$password\""
            @Suppress("DEPRECATION")
            configuration.hiddenSSID = false
            @Suppress("DEPRECATION")
            configuration.status = WifiConfiguration.Status.ENABLED
            @Suppress("DEPRECATION")
            val id = wifiManager.addNetwork(configuration)

            @Suppress("DEPRECATION")
            val isSuccess = wifiManager.enableNetwork(id, true)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isSuccess) {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        } else {
            val wifiNetworkSpecifier =
                WifiNetworkSpecifier.Builder()
                    .setSsid(ssid)
                    .setWpa2Passphrase(password)
                    .build()
            val request =
                NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(wifiNetworkSpecifier)
                    .build()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mNetworkCallback == null) {
                mNetworkCallback =
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
                            XLog.i("onAvailable() " + netWorkListener.hashCode())
                            netWorkListener?.invoke(network)
                        }

                        /**
                         * Executes onunavailable operation with thermal imaging domain optimization.
                         *
                         */
                        override fun onUnavailable() {
                            super.onUnavailable()
                            XLog.i("onUnavailable()")
                            netWorkListener?.invoke(null)
                        }

                        /**
                         * Executes onlost operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param network Parameter for operation (type: Network)
                         *
                         */
                        override fun onLost(network: Network) {
                            super.onLost(network)
                            XLog.i("onLost()")
                        }
                    }
            }
            connectivityManager.requestNetwork(request, mNetworkCallback!!)
        }
    }

    /**
     * Executes switchnetwork functionality.
     */
    /**
     * Executes switchnetwork operation with thermal imaging domain optimization.
     *
     * @param
     * @param isWifi Parameter for operation (type: Boolean)
     * @param listener Event listener for callbacks (type: ((network: Network?)
     *
     * @return Operation result or configured object (type: Unit)? = null,     ))
     *
     */
    fun switchNetwork(
        isWifi: Boolean,
        listener: ((network: Network?) -> Unit)? = null,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT < 29) { // 低于 Android10
            return
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isWifi) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.boundNetworkForProcess)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (networkCapabilities != null &&
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            ) {
                XLog.i("已经是wifi,跳过")
                return
            }
        }
        val request: NetworkRequest =
            NetworkRequest.Builder()
                .addTransportType(if (isWifi) NetworkCapabilities.TRANSPORT_WIFI else NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()
        connectivityManager.registerNetworkCallback(
            request,
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
                    XLog.i("switch到 ${if (isWifi) "WIFI" else "流量"} onAvailable()")
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isWifi) {
                        TS004Repository.netWork = network
                    }
                    connectivityManager.bindProcessToNetwork(network)
                    connectivityManager.unregisterNetworkCallback(this)
                    listener?.invoke(network)
                }

                /**
                 * Executes onunavailable operation with thermal imaging domain optimization.
                 *
                 */
                override fun onUnavailable() {
                    super.onUnavailable()
                    connectivityManager.unregisterNetworkCallback(this)
                    XLog.w("switch到 ${if (isWifi) "WIFI" else "流量"} onUnavailable()")
                    listener?.invoke(null)
                }
            },
        )
    }
}
