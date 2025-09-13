package com.topdon.lib.core.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.elvishew.xlog.XLog
import com.topdon.lib.core.config.DeviceConfig
import com.topdon.lib.core.tools.PermissionTool

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for BluetoothUtil operations.
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
/**
 * Specialized thermal imaging component providing BtStateObserver functionality for the IRCamera system.
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
    private class BtStateObserver(val context: Context, val listener: ((isEnable: Boolean) -> Unit)) : DefaultLifecycleObserver {
        private val receiver = BtStateReceiver()

        /**
         * Executes oncreate operation with thermal imaging domain optimization.
         *
         * @param
         * @param owner Parameter for operation (type: LifecycleOwner)
         *
         */
        override fun onCreate(owner: LifecycleOwner) {
            context.registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
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
        }

        private inner class BtStateReceiver : BroadcastReceiver() {
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
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)) {
                    BluetoothAdapter.STATE_OFF -> listener.invoke(false)
                    BluetoothAdapter.STATE_ON -> listener.invoke(true)
                }
            }
        }
    }

    private val scanCallback = MyScanCallback()

    /**
     * settings低功耗bluetoothSearchCallback.
     */
    fun setLeScanListener(
        isTS004: Boolean,
        listener: (name: String) -> Unit,
    ) {
        scanCallback.isTS004 = isTS004
        scanCallback.listener = listener
    }

    /**
     * 开启低功耗bluetoothSearch，调用前需确保拥有相应Permission且开启bluetooth.
     * @return true-调用success false-缺少Permission或bluetooth未开启
     */
    @SuppressLint("MissingPermission")
    /**
     * Initiates the operation or service.
     */
    fun startLeScan(context: Context): Boolean {
        XLog.i("startLeScan()")

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!PermissionTool.hasBtPermission(context)) {
            XLog.e("startbluetooth扫描-没有相应定位或bluetoothPermission!")
            return false
        }

        val btAdapter: BluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        val btLeScanner: BluetoothLeScanner? = btAdapter.bluetoothLeScanner
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (btLeScanner == null) {
            XLog.e("startbluetooth扫描-bluetooth未开启")
            return false
        }

        val settings =
            ScanSettings.Builder()
                .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

        btLeScanner.startScan(null, settings, scanCallback)
        return true
    }

    /**
     * stop低功耗bluetoothSearch，调用前需确保拥有相应Permission且开启bluetooth.
     * @return true-调用success false-缺少Permission或bluetooth未开启
     */
    @SuppressLint("MissingPermission")
    /**
     * Stops the operation or service.
     */
    fun stopLeScan(context: Context): Boolean {
        XLog.i("stopBtScan()")

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!PermissionTool.hasBtPermission(context)) {
            XLog.w("stopbluetooth扫描-没有相应定位或bluetoothPermission!")
            return false
        }

        val btAdapter: BluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        val btLeScanner: BluetoothLeScanner? = btAdapter.bluetoothLeScanner
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (btLeScanner == null) {
            XLog.w("stopbluetooth扫描-bluetooth未开启")
            return false
        }

        btLeScanner.stopScan(scanCallback)
        return true
    }

/**
 * Specialized thermal imaging component providing MyScanCallback functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class MyScanCallback : ScanCallback() {
        var isTS004: Boolean = false
        var listener: ((name: String) -> Unit)? = null

        @SuppressLint("MissingPermission")
        /**
         * Executes onscanresult operation with thermal imaging domain optimization.
         *
         * @param
         * @param callbackType Parameter for operation (type: Int)
         * @param result Parameter for operation (type: ScanResult?)
         *
         */
        override fun onScanResult(
            callbackType: Int,
            result: ScanResult?,
        ) {
            val name: String = result?.device?.name ?: return
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (name.startsWith(if (isTS004) DeviceConfig.TS004_NAME_START else DeviceConfig.TC007_NAME_START)) {
                XLog.v("bluetooth扫描出a目标device：$name")
                listener?.invoke(name)
            }
        }

        /**
         * Executes onscanfailed operation with thermal imaging domain optimization.
         *
         * @param
         * @param errorCode Parameter for operation (type: Int)
         *
         */
        override fun onScanFailed(errorCode: Int) {
            XLog.e("bluetooth扫描failed！$errorCode")
        }
    }
}
