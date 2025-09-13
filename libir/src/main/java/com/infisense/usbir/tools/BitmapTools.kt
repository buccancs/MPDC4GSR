package com.infisense.usbir.tools

import androidx.annotation.ColorInt
import com.elvishew.xlog.XLog
import com.topdon.lib.core.utils.ByteUtils.bytesToInt
import com.topdon.lib.core.utils.ByteUtils.descBytes

/**
 * Specialized thermal imaging component providing BitmapTools functionality for the IRCamera system.
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
object BitmapTools {
    /**
     * Executes readtempvalue functionality.
     */
    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @param
     * @param bytes Parameter for operation (type: ByteArray)
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    private fun readTempValue(bytes: ByteArray): Float {
        val data: ByteArray = bytes.descBytes()
        val scale = 16
        val tempInt = data.bytesToInt() / 4
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (tempInt.toDouble() / scale.toDouble() - 273.15).toFloat()
    }

    /**
     * Executes replacebitmapcolor functionality.
     */
    /**
     * Executes replacebitmapcolor operation with thermal imaging domain optimization.
     *
     * @param
     * @param imageBytes Parameter for operation (type: ByteArray)
     * @param tempBytes Temperature value in Celsius (type: ByteArray)
     * @param max Parameter for operation (type: Float = 40f)
     * @param min Parameter for operation (type: Float = 20f)
     * @param maxColor Parameter for operation (type: Int)
     * @param minColor Parameter for operation (type: Int)
     *
     */
    fun replaceBitmapColor(
        imageBytes: ByteArray,
        tempBytes: ByteArray,
        max: Float = 40f,
        min: Float = 20f,
        @ColorInt maxColor: Int,
        @ColorInt minColor: Int,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (max < min) {
            return
        }
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (maxColor == 0 && minColor == 0) {
                var data: ByteArray
                val len = imageBytes.size / 4
                var value: Float
                var r: Int
                var g: Int
                var b: Int
                var grey: Int
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in 0 until len) {
                    data = tempBytes.copyOfRange(i * 2, i * 2 + 2)
                    value = readTempValue(data)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (value > max || value < min) {
                        // Max color
                        r = imageBytes[i * 4].toInt() and 0xff
                        g = imageBytes[i * 4 + 1].toInt() and 0xff
                        b = imageBytes[i * 4 + 2].toInt() and 0xff
                        
                        grey = (r * 0.3f).toInt() + (g * 0.59f).toInt() + (b * 0.11f).toInt()
                        imageBytes[i * 4] = grey.toByte()
                        imageBytes[i * 4 + 1] = grey.toByte()
                        imageBytes[i * 4 + 2] = grey.toByte()
//                        Log.e("Test","grayscale化"+value)
                    }
                }
            } else {
                var data: ByteArray
                val len = imageBytes.size / 4
                val maxA = ((maxColor shr 24) and 0xff).toByte()
                val maxR = ((maxColor shr 16) and 0xff).toByte()
                val maxG = ((maxColor shr 8) and 0xff).toByte()
                val maxB = ((maxColor shr 0) and 0xff).toByte()
                val minA = ((minColor shr 24) and 0xff).toByte()
                val minR = ((minColor shr 16) and 0xff).toByte()
                val minG = ((minColor shr 8) and 0xff).toByte()
                val minB = ((minColor shr 0) and 0xff).toByte()
                var value: Float
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in 0 until len) {
                    data = tempBytes.copyOfRange(i * 2, i * 2 + 2)
                    value = readTempValue(data)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (value > max) {
                        // Max color
                        imageBytes[i * 4] = maxR 
                        imageBytes[i * 4 + 1] = maxG 
                        imageBytes[i * 4 + 2] = maxB 
                        imageBytes[i * 4 + 3] = maxA 
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (value < min) {
                        // Min color
                        imageBytes[i * 4] = minR
                        imageBytes[i * 4 + 1] = minG
                        imageBytes[i * 4 + 2] = minB
                        imageBytes[i * 4 + 3] = minA
                    }
                }
            }
        } catch (e: Exception) {
            XLog.w("color替换failed: ${e.message}")
        }
    }
}
