package com.topdon.lib.core.tools

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog
import com.topdon.lib.core.bean.event.device.DeviceConnectEvent
import com.topdon.lib.core.bean.event.device.DevicePermissionEvent
import com.topdon.lib.core.broadcast.DeviceBroadcastReceiver
import com.topdon.lib.core.config.DeviceConfig.isHik256
import com.topdon.lib.core.config.DeviceConfig.isTcLiteDevice
import com.topdon.lib.core.config.DeviceConfig.isTcTsDevice
import com.topdon.lib.core.utils.ByteUtils.toBytes
import com.topdon.lib.core.utils.ByteUtils.toHexString
import org.greenrobot.eventbus.EventBus

/**
 * Specialized thermal imaging component providing DeviceTools functionality for the IRCamera system.
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
object DeviceTools {
    /**
     * 判断当前是否已connection 插件式device 且有Permission.
     * 若已connection且有Permissiondefault不Send已connectionEvent.
     * 若已connection但无Permissiondefault触发Permission申请.
     */
    /**
     * Executes isConnect functionality.
     */
    /**
     * Executes isconnect operation with thermal imaging domain optimization.
     *
     * @param
     * @param isSendConnectEvent Parameter for operation (type: Boolean = false)
     * @param isAutoRequest Parameter for operation (type: Boolean = true)
     *
     */
    fun isConnect(
        isSendConnectEvent: Boolean = false,
        isAutoRequest: Boolean = true,
    ): Boolean {
        val usbManager = Utils.getApp().getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList: HashMap<String, UsbDevice> = usbManager.deviceList
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (usbDevice in deviceList.values) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (usbDevice.isTcTsDevice()) {
                return if (usbManager.hasPermission(usbDevice)) {
                    XLog.i("device已connection且有Permission")
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isSendConnectEvent) {
                        EventBus.getDefault().post(DeviceConnectEvent(true, usbDevice))
                    }
                    true
                } else {
                    XLog.w("device已connection但无Permission")
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isAutoRequest) {
                        EventBus.getDefault().post(DevicePermissionEvent(usbDevice))
                    }
                    false
                }
            }
        }
        return false
    }

    /**
     * Executes findUsbDevice functionality.
     */
    /**
     * Executes findusbdevice operation with thermal imaging domain optimization.
     *
     */
    fun findUsbDevice(): UsbDevice? {
        val usbManager = Utils.getApp().getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList: HashMap<String, UsbDevice> = usbManager.deviceList
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (usbDevice in deviceList.values) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (usbDevice.isTcTsDevice()) {
                val productID = usbDevice.productId.toBytes(2).toHexString()
                val vendorID = usbDevice.vendorId.toBytes(2).toHexString()
                XLog.i("找到ausbdevice productId:$productID, vendorId:$vendorID, deviceName:${usbDevice.deviceName}")
                return usbDevice
            }
        }
        XLog.i("检索到${deviceList.size}个device, 没有符合Customizeusbdevice")
        return null
    }

    /**
     * 判断当前是否已connection TC001 Plus 且有Permission.
     */
    fun isTC001PlusConnect(): Boolean {
        val usbManager = Utils.getApp().getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList: HashMap<String, UsbDevice> = usbManager.deviceList
        var usbCameraNumber = 0
        var isTcTsDev = false
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (usbDevice in deviceList.values) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if ("USB Camera" == usbDevice.productName) {
                usbCameraNumber++
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isTcTsDev) {
                isTcTsDev = usbDevice.isTcTsDevice() && usbManager.hasPermission(usbDevice)
            }
        }
        return isTcTsDev && usbCameraNumber > 1
    }

    /**
     * 判断是否connection了TC001 Lite 且有Permission
     */
    fun isTC001LiteConnect(): Boolean {
        val usbManager = Utils.getApp().getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList: HashMap<String, UsbDevice> = usbManager.deviceList
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (usbDevice in deviceList.values) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (usbDevice.isTcLiteDevice()) {
                return true
            }
        }
        return false
    }

    /**
     * 判断海康 256 是否已connection
     */
    /**
     * Executes ishikconnect operation with thermal imaging domain optimization.
     *
     */
    fun isHikConnect(): Boolean {
        val usbManager: UsbManager = Utils.getApp().getSystemService(Context.USB_SERVICE) as UsbManager
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (usbDevice in usbManager.deviceList.values) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (usbDevice.isHik256()) {
                return true
            }
        }
        return false
    }

    /**
     * Get/RetrieveusbPermission
     *
     * UsbManager.requestPermission
     * 在android 10无法弹出Authorization框
     * targetSdk 27
     */
    /**
     * Executes requestUsb functionality.
     */
    /**
     * Executes requestusb operation with thermal imaging domain optimization.
     *
     * @param
     * @param activity Parameter for operation (type: Activity)
     * @param requestCode Parameter for operation (type: Int)
     * @param device Parameter for operation (type: UsbDevice)
     *
     */
    fun requestUsb(
        activity: Activity,
        requestCode: Int,
        device: UsbDevice,
    ) {
        val usbManager = Utils.getApp().getSystemService(Context.USB_SERVICE) as UsbManager
        val intent = Intent(DeviceBroadcastReceiver.ACTION_USB_PERMISSION)
        val flag = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getBroadcast(activity, requestCode, intent, flag)
        usbManager.requestPermission(device, pendingIntent)
        XLog.i("申请usbPermission")
    }
}
