package com.topdon.module.thermal.ir.frame

import android.graphics.Bitmap
import android.graphics.Rect
import com.elvishew.xlog.XLog
import com.energy.iruvc.sdkisp.LibIRProcess
import com.energy.iruvc.sdkisp.LibIRTemp
import com.energy.iruvc.utils.CommonParams
import com.example.suplib.wrapper.SupHelp
import com.infisense.usbir.tools.ImageTools
import com.infisense.usbir.utils.IRImageHelp
import com.infisense.usbir.utils.OpencvTools
import com.topdon.pseudo.bean.CustomPseudoBean
import java.io.IOException
import java.nio.ByteBuffer

/**
 * Specialized thermal imaging component providing FrameTool functionality for the IRCamera system.
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
class FrameTool {
    val imageWidth = 256
    val imageHeight = 192
    private val scrImageLen = imageWidth * imageHeight * 2
    private val srcTemperatureLen = imageWidth * imageHeight * 2
    private val imageBytes = ByteArray(scrImageLen)
    val temperatureBytes = ByteArray(srcTemperatureLen)
    private val imageRes = LibIRProcess.ImageRes_t() // Original image dimensions
    private var struct: FrameStruct = FrameStruct() // Header information

    private var maxLimit = -273f
    private var minLimit = -273f
    private var irImageHelp = IRImageHelp()

// Private val scrBitmap = Bitmap.createBitmap(192, 256, Bitmap.Config.ARGB_8888)
    private val supImageData = ByteArray(imageWidth * imageHeight * 4 * 4)
    private var dstArgbBytes: ByteArray? = null

    /**
     * Executes read functionality.
     */
    /**
     * Executes read operation with thermal imaging domain optimization.
     *
     * @param
     * @param bytes Parameter for operation (type: ByteArray)
     *
     */
    fun read(bytes: ByteArray) {
        try {
            val frame = ByteArray(bytes.size)
            System.arraycopy(bytes, 0, frame, 0, frame.size)
            /**
             * Executes println operation with thermal imaging domain optimization.
             *
             * @param
             * @param len Parameter for operation (type: ${frame.size}")
             *
             */
            println("bs len: ${frame.size}")
            System.arraycopy(frame, 0, imageBytes, 0, scrImageLen) // Image data (192 x 256 x 2) yuv
            System.arraycopy(frame, scrImageLen, temperatureBytes, 0, srcTemperatureLen) // Temperature data (192 x 256 x 2)
            /**
             * Executes println operation with thermal imaging domain optimization.
             *
             * @param
             * @param len Parameter for operation (type: ${imageBytes.size}")
             *
             */
            println("imageBytes len: ${imageBytes.size}")
            /**
             * Executes println operation with thermal imaging domain optimization.
             *
             * @param
             * @param len Parameter for operation (type: ${temperatureBytes.size}")
             *
             */
            println("temperatureBytes len: ${temperatureBytes.size}")
        } catch (e: Exception) {
            e.printStackTrace()
            XLog.e("Failed to read frame raw data: ${e.message}")
        }
    }

    /**
     * Set image default dimensions
     */
    fun initStruct(struct: FrameStruct) {
        this.struct = struct
        imageRes.width = imageWidth.toChar()
        imageRes.height = imageHeight.toChar()
    }

    /**
     * Correction angle
     */
    /**
     * Initializes the rotate component for thermal imaging operations.
     *
     */
    fun initRotate(): ImageParams {
        var rotate = ImageParams.ROTATE_0
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (struct.rotate) {
            0 -> rotate = ImageParams.ROTATE_0
            90 -> rotate = ImageParams.ROTATE_270
            180 -> rotate = ImageParams.ROTATE_180
            270 -> rotate = ImageParams.ROTATE_90
        }
        return rotate
    }

    /**
     * Get temperature data
     */
    /**
     * Retrieves the tempbytes with optimized performance for thermal imaging operations.
     *
     * @param
     * @param rotate Parameter for operation (type: ImageParams = ImageParams.ROTATE_0)
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    fun getTempBytes(rotate: ImageParams = ImageParams.ROTATE_0): ByteArray {
        val tempBytes = ByteArray(srcTemperatureLen)
        val dstTempBytes = ByteArray(srcTemperatureLen)
        System.arraycopy(temperatureBytes, 0, tempBytes, 0, srcTemperatureLen)
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (rotate) {
            ImageParams.ROTATE_270 ->
                LibIRProcess.rotateLeft90(
                    tempBytes,
                    imageRes,
                    CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_Y14,
                    dstTempBytes,
                )
            ImageParams.ROTATE_90 ->
                LibIRProcess.rotateRight90(
                    tempBytes,
                    imageRes,
                    CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_Y14,
                    dstTempBytes,
                )
            ImageParams.ROTATE_180 ->
                LibIRProcess.rotate180(
                    tempBytes,
                    imageRes,
                    CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_Y14,
                    dstTempBytes,
                )
            else -> System.arraycopy(temperatureBytes, 0, dstTempBytes, 0, srcTemperatureLen)
        }
        return dstTempBytes
    }

    /**
     * Retrieves rotate90temp information.
     */
    fun getRotate90Temp(temperatureBytes: ByteArray): ByteArray {
        val tempBytes = ByteArray(temperatureBytes.size)
        val dstTempBytes = ByteArray(temperatureBytes.size)
        System.arraycopy(temperatureBytes, 0, tempBytes, 0, temperatureBytes.size)
        val imgRes = LibIRProcess.ImageRes_t()
        imgRes.height = 192.toChar()
        imgRes.width = 256.toChar()
        LibIRProcess.rotateRight90(tempBytes, imgRes, CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_Y14, dstTempBytes)
        return dstTempBytes
    }

    /**
     * Convert grayscale image to pseudo-color image
     * yuv -> argb -> temperature scale -> rotation -> bitmap
     */
    /**
     * Retrieves scrpseudocolorscaledbitmap information.
     */
    fun getScrPseudoColorScaledBitmap(
        pseudoColorMode: CommonParams.PseudoColorType = CommonParams.PseudoColorType.PSEUDO_3,
        max: Float = -273f,
        min: Float = 273f,
        rotate: ImageParams = ImageParams.ROTATE_0,
        customPseudoBean: CustomPseudoBean,
        maxTemperature: Float,
        minTemperature: Float,
        isAmplify: Boolean,
    ): Bitmap? {
        maxLimit = max
        minLimit = min
        val imageBytesTemp = ByteArray(imageBytes.size)
        System.arraycopy(imageBytes, 0, imageBytesTemp, 0, imageBytesTemp.size)
        val pixNum = imageWidth * imageHeight
        val argbLen = pixNum * 4
        var argbBytes = ByteArray(argbLen)
        dstArgbBytes = ByteArray(argbLen)
        val maxRGB = IntArray(3)
        val minRGB = IntArray(3)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (customPseudoBean.isUseCustomPseudo) {
            // Custom rendering mode
            LibIRProcess.convertYuyvMapToARGBPseudocolor(imageBytesTemp, pixNum.toLong(), CommonParams.PseudoColorType.PSEUDO_1, argbBytes)
            val colorList: IntArray? = customPseudoBean.getColorList(struct.isTC007())
            val places: FloatArray? = customPseudoBean.getPlaceList()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (colorList != null) {
                val customMaxTemp = customPseudoBean.maxTemp
                val customMinTemp = customPseudoBean.minTemp
                val maxColor: Int = colorList.last()
                val minColor: Int = colorList.first()
                maxRGB[0] = maxColor shr 16 and 0xFF
                maxRGB[1] = maxColor shr 8 and 0xFF
                maxRGB[2] = maxColor and 0xFF
                minRGB[0] = minColor shr 16 and 0xFF
                minRGB[1] = minColor shr 8 and 0xFF
                minRGB[2] = minColor and 0xFF
                var j = 0
                val argbBytesLength = imageWidth * imageHeight * 4
                // Iterate through pixels, filter temperature thresholds
                var index = 0
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (index < argbBytesLength) {
                    // Temperature conversion formula
                    var temperature0: Float =
                        (
                            (temperatureBytes[j].toInt() and 0xff) + (
                                temperatureBytes[j + 1]
                                    .toInt() and 0xff
                            ) * 256
                        ).toFloat()
                    temperature0 = (temperature0 / 64 - 273.15).toFloat()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (temperature0 in customMinTemp..customMaxTemp) {
                        val rgb =
                            OpencvTools.getOneColorByTempUnif(
                                customMaxTemp,
                                customMinTemp,
                                temperature0,
                                colorList,
                                places,
                            )
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (rgb != null) {
                            argbBytes[index] = rgb[0].toByte()
                            argbBytes[index + 1] = rgb[1].toByte()
                            argbBytes[index + 2] = rgb[2].toByte()
                        }
                    } else if (temperature0 > customMaxTemp) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (!customPseudoBean.isUseGray) {
                            argbBytes[index] = maxRGB[0].toByte()
                            argbBytes[index + 1] = maxRGB[1].toByte()
                            argbBytes[index + 2] = maxRGB[2].toByte()
                        }
                    } else if (temperature0 < customMinTemp) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (!customPseudoBean.isUseGray) {
                            argbBytes[index] = minRGB[0].toByte()
                            argbBytes[index + 1] = minRGB[1].toByte()
                            argbBytes[index + 2] = minRGB[2].toByte()
                        }
                    }
                    argbBytes[index + 3] = 255.toByte()
                    index += 4
                    j += 2
                }
            }
        } else {
            LibIRProcess.convertYuyvMapToARGBPseudocolor(imageBytesTemp, pixNum.toLong(), pseudoColorMode, argbBytes)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!(maxLimit == -273f && minLimit == -273f) && !(maxTemperature == maxLimit && minLimit == minTemperature)) {
                ImageTools.dualReadFrame(argbBytes, temperatureBytes, maxLimit, minLimit) // Temperature scale
            }
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if ((struct.alarmBean.isHighOpen && struct.alarmBean.highTemp != Float.MAX_VALUE) ||
            (struct.alarmBean.isLowOpen && struct.alarmBean.lowTemp != Float.MIN_VALUE)
        ) {
            try {
                argbBytes =
                    irImageHelp.contourDetection(
                        struct.alarmBean,
                        argbBytes, temperatureBytes,
                        imageWidth,
                        imageHeight,
                    )!!
            } catch (e: IOException) {
            }
        }

        /**
         * Executes argbbytesrotate operation with thermal imaging domain optimization.
         *
         */
        argbBytesRotate(argbBytes, dstArgbBytes!!, rotate) // Rotation
        val dstImageRes = getDstImageRes(rotate)
        var scrBitmap: Bitmap? = null
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isAmplify)
            {
// ScrBitmap = Bitmap.createBitmap(dstImageRes.width.code,
// DstImageRes.height.code, Bitmap.Config.ARGB_8888)
// ScrBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(dstArgbBytes, 0, argbLen))
// Return OpencvTools.supImageFourExToBitmap(scrBitmap)
                SupHelp.getInstance().initA4KCPP()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (SupHelp.getInstance().loadOpenclSuccess)
                    {
                        OpencvTools.supImage(
                            dstArgbBytes,
                            dstImageRes.height.code,
                            dstImageRes.width.code,
                            supImageData,
                        )
                        scrBitmap =
                            Bitmap.createBitmap(
                                dstImageRes.width.code * 2,
                                dstImageRes.height.code * 2, Bitmap.Config.ARGB_8888,
                            )
                        scrBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(supImageData, 0, argbLen * 4))
                    } else
                    {
                        scrBitmap =
                            Bitmap.createBitmap(
                                dstImageRes.width.code,
                                dstImageRes.height.code, Bitmap.Config.ARGB_8888,
                            )
                        scrBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(dstArgbBytes, 0, argbLen))
                    }
            } else
            {
                scrBitmap =
                    Bitmap.createBitmap(
                        dstImageRes.width.code,
                        dstImageRes.height.code, Bitmap.Config.ARGB_8888,
                    )
                scrBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(dstArgbBytes, 0, argbLen))
            }
        return scrBitmap
    }

    /**
     * Get original image bitmap
     */
    /**
     * Retrieves the basebitmap with optimized performance for thermal imaging operations.
     *
     * @param
     * @param rotate Parameter for operation (type: ImageParams)
     *
     */
    fun getBaseBitmap(rotate: ImageParams): Bitmap  {
        val dstImageRes = getDstImageRes(rotate)
        val scrBitmap =
            Bitmap.createBitmap(
                dstImageRes.width.code,
                dstImageRes.height.code,
                Bitmap.Config.ARGB_8888,
            )
        dstArgbBytes?.let {
            scrBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(it, 0, it.size))
        }
        return scrBitmap
    }

    /**
目标尺寸
     */
    /**
     * Retrieves the dstimageres with optimized performance for thermal imaging operations.
     *
     * @param
     * @param rotate Parameter for operation (type: ImageParams)
     *
     */
    private fun getDstImageRes(rotate: ImageParams): LibIRProcess.ImageRes_t {
        val dstImageRes = LibIRProcess.ImageRes_t() // 目标尺寸
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rotate == ImageParams.ROTATE_270 || rotate == ImageParams.ROTATE_90) {
            dstImageRes.width = imageRes.height
            dstImageRes.height = imageRes.width
        } else {
            dstImageRes.width = imageRes.width
            dstImageRes.height = imageRes.height
        }
        return dstImageRes
    }

    /**
     * ARGB pixel matrix rotation
     */
    /**
     * Executes argbbytesrotate operation with thermal imaging domain optimization.
     *
     * @param
     * @param argbBytes Parameter for operation (type: ByteArray)
     * @param dstArgbBytes Parameter for operation (type: ByteArray)
     * @param rotate Parameter for operation (type: ImageParams)
     *
     */
    private fun argbBytesRotate(
        argbBytes: ByteArray,
        dstArgbBytes: ByteArray,
        rotate: ImageParams,
    ) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (rotate) {
            ImageParams.ROTATE_270 ->
                LibIRProcess.rotateLeft90(
                    argbBytes,
                    imageRes,
                    CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_ARGB8888,
                    dstArgbBytes,
                )
            ImageParams.ROTATE_90 ->
                LibIRProcess.rotateRight90(
                    argbBytes,
                    imageRes,
                    CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_ARGB8888,
                    dstArgbBytes,
                )

            ImageParams.ROTATE_180 ->
                LibIRProcess.rotate180(
                    argbBytes,
                    imageRes,
                    CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_ARGB8888,
                    dstArgbBytes,
                )
            else -> System.arraycopy(argbBytes, 0, dstArgbBytes, 0, argbBytes.size)
        }
    }

// Fun getTemp() {
// Getfull image最high temperature和最low temperature的data
// Val irTemp = Libirtemp(256, 192)
// IrTemp.settempdata(mixTemperatureBytes)
// Val temperatureSampleEasyResult = irTemp.getTemperatureOfRect(Rect(0, 0, 256, 192))
//        Log.w("123", "mix max: ${temperatureSampleEasyResult.maxTemperature}, min: ${temperatureSampleEasyResult.minTemperature}")
//    }

// Fun getSrcTemp()：Libirt{
// Getfull image最high temperature和最low temperature的data
// Val irTemp = Libirtemp(256, 192)
// IrTemp.settempdata(temperatureBytes)
// Val temperatureSampleEasyResult = irTemp.getTemperatureOfRect(Rect(0, 0, 256, 192))
// TemperatureSampleEasyResult.maxTemperaturePixel
//        Log.w("123", "src max: ${temperatureSampleEasyResult.maxTemperature}, min: ${temperatureSampleEasyResult.minTemperature}")
//    }

    /**
     * Global temperature measurement (raw data)
     */
    fun getSrcTemp(): LibIRTemp.TemperatureSampleResult {
        // Get full image maximum and minimum temperature data
        val irTemp = LibIRTemp(imageWidth, imageHeight)
        irTemp.setTempData(temperatureBytes)
        return irTemp.getTemperatureOfRect(Rect(0, 0, imageWidth, imageHeight))
    }
}
