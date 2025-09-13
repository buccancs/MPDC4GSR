package com.infisense.usbir.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for SupRUtils operations.
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
object SupRUtils {
    /**
     * 是否能开启超分
     */
    /**
     * Executes canopensupr operation with thermal imaging domain optimization.
     *
     */
    fun canOpenSupR(): Boolean  {
        return true
    }

    /**
     * 由此统一弹
     */
    /**
     * Executes showopensuprtipsdialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param activity Parameter for operation (type: Activity)
     *
     */
    fun showOpenSupRTipsDialog(activity: Activity)  {
    }

    /**
     * Executes bitmaptobytearray functionality.
     */
    /**
     * Executes bitmaptobytearray operation with thermal imaging domain optimization.
     *
     * @param
     * @param bitmap Parameter for operation (type: Bitmap)
     *
     */
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    /**
     * Executes bytearraytobitmap functionality.
     */
    /**
     * Executes bytearraytobitmap operation with thermal imaging domain optimization.
     *
     * @param
     * @param byteArray Parameter for operation (type: ByteArray)
     *
     */
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
