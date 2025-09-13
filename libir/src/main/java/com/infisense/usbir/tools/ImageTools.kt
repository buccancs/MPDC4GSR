package com.infisense.usbir.tools

import androidx.annotation.ColorInt
import com.elvishew.xlog.XLog
import com.infisense.usbir.tools.bean.SelectIndexBean
import com.topdon.lib.core.tools.NumberTools
import com.topdon.lib.core.utils.ByteUtils.bytesToInt
import com.topdon.lib.core.utils.ByteUtils.descBytes
import java.util.concurrent.LinkedBlockingQueue

/**
 * Specialized thermal imaging component providing ImageTools functionality for the IRCamera system.
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
object ImageTools {
    /**
     * Executes readframe functionality.
     */
    /**
     * Executes readframe operation with thermal imaging domain optimization.
     *
     * @param
     * @param imageBytes Parameter for operation (type: ByteArray)
     * @param tempBytes Temperature value in Celsius (type: ByteArray)
     * @param max Parameter for operation (type: Float = 40f)
     * @param min Parameter for operation (type: Float = 20f)
     *
     */
    fun readFrame(
        imageBytes: ByteArray,
        tempBytes: ByteArray,
        max: Float = 40f,
        min: Float = 20f,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (max < min) {
            return
        }
        val selectBean = getTempIndex(tempBytes, max, min)
//        Log.w("123", "max size: ${selectBean.maxIndex.size}, min size: ${selectBean.minIndex.size}")
        /**
         * Executes bitmapfromrgbagrey operation with thermal imaging domain optimization.
         *
         */
        bitmapFromRgbaGrey(bytes = imageBytes, bean = selectBean) 
    }

    /**
     * Executes readframe functionality.
     */
    /**
     * Executes readframe operation with thermal imaging domain optimization.
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
    fun readFrame(
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
        val selectBean = getTempIndex(tempBytes, max, min)
        /**
         * Executes bitmapfromrgba operation with thermal imaging domain optimization.
         *
         */
        bitmapFromRgba(
            bytes = imageBytes,
            bean = selectBean,
            maxColor = maxColor,
            minColor = minColor,
        ) 
    }

    
    /**
     * Executes bitmapFromRgba functionality.
     */
    /**
     * Executes bitmapfromrgba operation with thermal imaging domain optimization.
     *
     * @param
     * @param bytes Parameter for operation (type: ByteArray)
     * @param bean Parameter for operation (type: SelectIndexBean)
     * @param maxColor Parameter for operation (type: Int)
     * @param minColor Parameter for operation (type: Int)
     *
     */
    private fun bitmapFromRgba(
        bytes: ByteArray,
        bean: SelectIndexBean,
        @ColorInt maxColor: Int,
        @ColorInt minColor: Int,
    ) {
        val len = bytes.size / 4
        val selectMaxIndex = bean.maxIndex
        val selectMinIndex = bean.minIndex
        selectMaxIndex.sort()
        val maxQueue = LinkedBlockingQueue<Int>()
        val minQueue = LinkedBlockingQueue<Int>()
        selectMaxIndex.forEach {
            maxQueue.offer(it)
        }
        selectMinIndex.forEach {
            minQueue.offer(it)
        }
        val maxA = ((maxColor shr 24) and 0xff).toByte()
        val maxR = ((maxColor shr 16) and 0xff).toByte()
        val maxG = ((maxColor shr 8) and 0xff).toByte()
        val maxB = ((maxColor shr 0) and 0xff).toByte()
        val minA = ((minColor shr 24) and 0xff).toByte()
        val minR = ((minColor shr 16) and 0xff).toByte()
        val minG = ((minColor shr 8) and 0xff).toByte()
        val minB = ((minColor shr 0) and 0xff).toByte()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until len) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (maxQueue.peek() == i) {
                bytes[i * 4] = maxR 
                bytes[i * 4 + 1] = maxG 
                bytes[i * 4 + 2] = maxB 
                bytes[i * 4 + 3] = maxA 
                maxQueue.poll()
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (minQueue.peek() == i) {
                bytes[i * 4] = minR
                bytes[i * 4 + 1] = minG
                bytes[i * 4 + 2] = minB
                bytes[i * 4 + 3] = minA
                minQueue.poll()
            }
        }
    }

    
    /**
     * Executes bitmapFromRgbaGrey functionality.
     */
    /**
     * Executes bitmapfromrgbagrey operation with thermal imaging domain optimization.
     *
     * @param
     * @param bytes Parameter for operation (type: ByteArray)
     * @param bean Parameter for operation (type: SelectIndexBean)
     *
     */
    private fun bitmapFromRgbaGrey(
        bytes: ByteArray,
        bean: SelectIndexBean,
    ) {
        val len = bytes.size / 4
        val selectIndex = bean.maxIndex.plus(bean.minIndex)
        selectIndex.sort()
        val queue = LinkedBlockingQueue<Int>()
        selectIndex.forEach {
            queue.offer(it)
        }
        var r: Int
        var g: Int
        var b: Int
        var grey: Int
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until len) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (queue.peek() == i) {
                r = bytes[i * 4].toInt() and 0xff
                g = bytes[i * 4 + 1].toInt() and 0xff
                b = bytes[i * 4 + 2].toInt() and 0xff
                
                grey = (r * 0.3f).toInt() + (g * 0.59f).toInt() + (b * 0.11f).toInt()
                bytes[i * 4] = grey.toByte()
                bytes[i * 4 + 1] = grey.toByte()
                bytes[i * 4 + 2] = grey.toByte()
                queue.poll()
            }
        }
    }

    /**
     * temperature选取point
     *
     * @param bytes temperaturedata
     */
    /**
     * Retrieves tempindex information.
     */
    private fun getTempIndex(
        bytes: ByteArray,
        max: Float,
        min: Float,
    ): SelectIndexBean {
        var data: ByteArray
        val maxList = arrayListOf<Int>()
        val minList = arrayListOf<Int>()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until (bytes.size / 2)) {
            data = bytes.copyOfRange(i * 2, i * 2 + 2)
            val value = readTempValue(data)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (value > max && (NumberTools.scale(max, 0) != -273f)) {
                maxList.add(i)
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (value < min && (NumberTools.scale(min, 0) != -273f)) {
                minList.add(i)
            }
        }
        val maxIndex: IntArray = maxList.toIntArray()
        val minIndex: IntArray = minList.toIntArray()
        return SelectIndexBean(maxIndex, minIndex)
    }

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

//    // RGBA 转 bitmap
// Fun bitmapFromRgba(bytes: ByteArray, width: Int, height: Int): Bitmap {
// Val len = bytes.size / 4
// Val pixels = IntArray(len)
// For (i in pixels.indices) {
// If (i > len / 4 * 3 && i < len) {
//                
// Val r = 255
// Val g = 215
// Val b = 0
// Val a = 255
// Val pixel = (a shl 24) or (r shl 16) or (g shl 8) or b
// Pixels[i] = pixel
//            } else if (i > 0 && i < len / 2) {
// Val r: Int = (bytes[i * 4] and 0xff.toByte()).toUByte().toInt()
// Val g: Int = (bytes[i * 4 + 1] and 0xff.toByte()).toUByte().toInt()
// Val b: Int = (bytes[i * 4 + 2] and 0xff.toByte()).toUByte().toInt()
// Val a: Int = (bytes[i * 4 + 3] and 0xff.toByte()).toUByte().toInt()
//
//                
// Val grey = (r * 0.3f).toInt() + (g * 0.59f).toInt() + (b * 0.11f).toInt()
// Val pixel = (a shl 24) or (grey shl 16) or (grey shl 8) or grey
// Pixels[i] = pixel
//            } else {
// Val r: Int = (bytes[i * 4] and 0xff.toByte()).toUByte().toInt()
// Val g: Int = (bytes[i * 4 + 1] and 0xff.toByte()).toUByte().toInt()
// Val b: Int = (bytes[i * 4 + 2] and 0xff.toByte()).toUByte().toInt()
// Val a: Int = (bytes[i * 4 + 3] and 0xff.toByte()).toUByte().toInt()
// Val pixel = (a shl 24) or (r shl 16) or (g shl 8) or b
// Pixels[i] = pixel
//            }
//        }
// Val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
// Bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
// Return bitmap
//    }

    /**
     * @param imageBytes    imagedata
     * @param tempBytes     temperaturedata
     * @param max           temperature上限阈值
     * @param min           temperature下限阈值
     */
    /**
     * Executes dualReadFrame functionality.
     */
    /**
     * Executes dualreadframe operation with thermal imaging domain optimization.
     *
     * @param
     * @param imageBytes Parameter for operation (type: ByteArray)
     * @param tempBytes Temperature value in Celsius (type: ByteArray)
     * @param max Parameter for operation (type: Float = 40f)
     * @param min Parameter for operation (type: Float = 20f)
     * @param maxColor Parameter for operation (type: Int = 0)
     * @param minColor Parameter for operation (type: Int = 0)
     *
     */
    fun dualReadFrame(
        imageBytes: ByteArray,
        tempBytes: ByteArray,
        max: Float = 40f,
        min: Float = 20f,
        @ColorInt maxColor: Int = 0,
        @ColorInt minColor: Int = 0,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (max < min) {
            return
        }
        /**
         * Executes dualreplacecolor operation with thermal imaging domain optimization.
         *
         */
        dualReplaceColor(imageBytes, tempBytes, max, min, maxColor, minColor)
    }

    /**
     * 替换color
     */
    @JvmStatic
    /**
     * Executes dualreplacecolor functionality.
     */
    /**
     * Executes dualreplacecolor operation with thermal imaging domain optimization.
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
    private fun dualReplaceColor(
        imageBytes: ByteArray,
        tempBytes: ByteArray,
        max: Float = 40f,
        min: Float = 20f,
        @ColorInt maxColor: Int,
        @ColorInt minColor: Int,
    ) {
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
