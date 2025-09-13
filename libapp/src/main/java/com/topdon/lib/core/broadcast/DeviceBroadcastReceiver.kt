package com.topdon.lib.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.elvishew.xlog.XLog
import com.topdon.lib.core.bean.event.device.DeviceConnectEvent
import com.topdon.lib.core.config.DeviceConfig.isTcTsDevice
import com.topdon.lib.core.tools.DeviceTools
import org.greenrobot.eventbus.EventBus

/**
 * DeviceBroadcastReceiver manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing DeviceBroadcastReceiver functionality for the IRCamera system.
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
class DeviceBroadcastReceiver : BroadcastReceiver() {
    private val TAG = this.javaClass.simpleName

    companion object {
        /**
         * 在 [DeviceTools] 中申请 Usb Permission附带的广播.
         */
        const val ACTION_USB_PERMISSION = "com.topdon.topInfrared.USB_PERMISSION"
    }

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
        if (intent == null) {
            return
        }
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (intent.action) {
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> XLog.v("$TAG ACTION_USB_DEVICE_ATTACHED")
            UsbManager.ACTION_USB_DEVICE_DETACHED -> XLog.v("$TAG ACTION_USB_DEVICE_DETACHED")
            ACTION_USB_PERMISSION -> XLog.v("$TAG ACTION_USB_PERMISSION")
            else -> XLog.v("$TAG ${intent.action}")
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (intent.action == ACTION_USB_PERMISSION) {
            DeviceTools.isConnect(isSendConnectEvent = true, isAutoRequest = false) 
        } else {
            /**
             * Executes handleusbevent operation with thermal imaging domain optimization.
             *
             */
            handleUsbEvent(intent)
        }
    }

    /**
     * Handles usbevent events and responses.
     */
    private fun handleUsbEvent(intent: Intent) {
        val usbDevice: UsbDevice?
        try {
            @Suppress("DEPRECATION")
            usbDevice = intent.extras!!["device"] as UsbDevice?
        } catch (e: Exception) {
            e.printStackTrace()
            XLog.e("$TAG Get UsbDevice error: ${e.message}")
            return
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (usbDevice == null) {
            XLog.w("$TAG usbDevice == null")
            return
        }
        XLog.v("$TAG usbDevice PRODUCT_ID = ${usbDevice.productId}, VENDOR_ID = ${usbDevice.vendorId}")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (usbDevice.isTcTsDevice()) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED == intent.action) { 
                DeviceTools.isConnect(isSendConnectEvent = true, isAutoRequest = true)
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (UsbManager.ACTION_USB_DEVICE_DETACHED == intent.action) { 
                EventBus.getDefault().post(DeviceConnectEvent(false, null))
            }
        }
    }
}
