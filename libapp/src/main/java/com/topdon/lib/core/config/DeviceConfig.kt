package com.topdon.lib.core.config

import android.hardware.usb.UsbDevice

/**
 * Configuration management system for thermal imaging parameters. Handles settings and calibration for DeviceConfig operations.
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
object DeviceConfig {
    const val TS004_NAME_START = "TS004_"
    const val TS004_PASSWORD = "TS004001"

    const val TC007_NAME_START = "TC007_"
    const val TC007_PASSWORD = "12345678"

    
    // Vid:3034, pid:22592
    const val IR_VENDOR_ID = 0x0BDA
    const val IR_PRODUCT_ID = 0x5840

    
    const val TOPDON_VENDOR_ID = 0x0BDA
    const val TOPDON_PRODUCT_ID = 0x5830

    const val TCLITE_VENDOR_ID = 13428
    const val TCLITE_PRODUCT_ID = 17185

    const val HIK_VENDOR_ID = 11231
    const val HIK_PRODUCT_ID = 258

    /**
     * 判断该 UsbDevice 是否为TC、TS插件式device.
     */
    fun UsbDevice.isTcTsDevice(): Boolean {
        return (productId == TOPDON_PRODUCT_ID && vendorId == TOPDON_VENDOR_ID) ||
            (productId == IR_PRODUCT_ID && vendorId == IR_VENDOR_ID) ||
            (productId == TCLITE_PRODUCT_ID && vendorId == TCLITE_VENDOR_ID) ||
            (productId == HIK_PRODUCT_ID && vendorId == HIK_VENDOR_ID)
    }

    /**
     * Executes usbdevice functionality.
     */
    fun UsbDevice.isTcLiteDevice(): Boolean {
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (productId == TCLITE_PRODUCT_ID && vendorId == TCLITE_VENDOR_ID)
    }

    /**
     * Executes usbdevice functionality.
     */
    fun UsbDevice.isHik256(): Boolean = productId == HIK_PRODUCT_ID && vendorId == HIK_VENDOR_ID

    const val SKU = "TDTC001A11"
    const val SN = "TC001A11000001"

//    
// Const val SKU = "TDBT006A11"
// Const val SN = "BT006AAG100001"

    // 横屏 TC003校对defaultangle0 default竖屏false initializesettingsinitDataIR()
    const val ROTATE_ANGLE = 0
    const val IS_PORTRAIT = false

    
    const val S_ROTATE_ANGLE = 270
    const val S_IS_PORTRAIT = true
}
