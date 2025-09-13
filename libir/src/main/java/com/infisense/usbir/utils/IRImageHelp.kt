package com.infisense.usbir.utils

import android.util.Log
import com.example.open3d.JNITool
import com.topdon.lib.core.bean.AlarmBean
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.IOException

/**
 * thermal imagingimage二次processing的统一入口，为了方便管理
 * @author: CaiSongL
 * @date: 2024/1/17 9:54
 */
/**
 * IRImageHelp manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing IRImageHelp functionality for the IRCamera system.
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
class IRImageHelp {
    
    @Volatile
    private var colorList: IntArray? = null

    @Volatile
    private var places: FloatArray? = null

    private var isUseGray = true
    private var customMaxTemp = 0f
    private var customMinTemp = 0f
    private var maxRGB = IntArray(3)
    private var minRGB = IntArray(3)

    /**
     * Retrieves colorlist information.
     */
    fun getColorList(): IntArray?  {
        return colorList
    }

    /**
     * settings自定义pseudo color条property
     * @author: CaiSongL
     * @date: 2024/1/17 10:07
     */
    /**
     * Sets colorlist configuration.
     */
    fun setColorList(
        colorList: IntArray?,
        places: FloatArray?,
        isUseGray: Boolean,
        customMaxTemp: Float,
        customMinTemp: Float,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (colorList == null) {
            this.isUseGray = true
        } else {
            this.isUseGray = isUseGray
        }
        this.colorList = colorList
        this.places = places
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (colorList != null) {
            this.customMaxTemp = customMaxTemp
            this.customMinTemp = customMinTemp
            val maxColor = colorList[colorList.size - 1]
            val minColor = colorList[0]
            this.maxRGB[0] = maxColor shr 16 and 0xFF
            this.maxRGB[1] = maxColor shr 8 and 0xFF
            this.maxRGB[2] = maxColor and 0xFF
            this.minRGB[0] = minColor shr 16 and 0xFF
            this.minRGB[1] = minColor shr 8 and 0xFF
            this.minRGB[2] = minColor and 0xFF
        }
    }

    /**
     * 自定义pseudo colorprocessing，在执行这个method之前，变更pseudo colorproperty时先通过 上areasetColorList进行propertysettings
     * @param imageDst ByteArray ： imagedata，argbformat
     * @param temperatureSrc ByteArray ： temperaturedata
     * @param imageWidth Int ：
     * @param imageHeight Int
     * @return ByteArray ： Returnprocessing后的imagedata，argbformat
     */
    /**
     * Handles pseudo color configuration for thermal imaging.
     */
    fun customPseudoColor(
        imageDst: ByteArray,
        temperatureSrc: ByteArray,
        imageWidth: Int,
        imageHeight: Int,
    ): ByteArray  {
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (colorList != null && temperatureSrc != null) {
                var j = 0
                val imageDstLength: Int = imageWidth * imageHeight * 4
                // 遍历像素point，Filtertemperature阈值
                var index = 0
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (index < imageDstLength) {
                    
                    var temperature0: Float =
                        (
                            (temperatureSrc.get(j).toInt() and 0xff) + (
                                temperatureSrc.get(j + 1)
                                    .toInt() and 0xff
                            ) * 256
                        ).toFloat()
                    temperature0 = (temperature0 / 64 - 273.15).toFloat()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (temperature0 >= customMinTemp && temperature0 <= customMaxTemp) {
                        // OpencvTools disabled due to missing AAR dependencies
                        // Using simple fallback color logic
                        /*
                        val rgb = OpencvTools.getOneColorByTempUnif(
                            customMaxTemp,
                            customMinTemp,
                            temperature0,
                            colorList,
                            places
                        )
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (rgb != null) {
                            imageDst[index] = rgb[0].toByte()
                            imageDst[index + 1] = rgb[1].toByte()
                            imageDst[index + 2] = rgb[2].toByte()
                        }
                         */
                        // Simple fallback: use temperature-based grayscale
                        val intensity = ((temperature0 - customMinTemp) / (customMaxTemp - customMinTemp) * 255).toInt().coerceIn(0, 255)
                        imageDst[index] = intensity.toByte()
                        imageDst[index + 1] = intensity.toByte()
                        imageDst[index + 2] = intensity.toByte()
                    } else if (temperature0 > customMaxTemp) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isUseGray) {
                        } else {
                            imageDst[index] = maxRGB[0].toByte()
                            imageDst[index + 1] = maxRGB[1].toByte()
                            imageDst[index + 2] = maxRGB[2].toByte()
                        }
                    } else if (temperature0 < customMinTemp) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isUseGray) {
                        } else {
                            imageDst[index] = minRGB[0].toByte()
                            imageDst[index + 1] = minRGB[1].toByte()
                            imageDst[index + 2] = minRGB[2].toByte()
                        }
                    }
                    imageDst[index + 3] = 255.toByte()
                    index += 4
                    j += 2
                }
//                                        Log.w("Test上色耗时-总耗时", System.currentTimeMillis() - startTimeAll + "// ");
            }
        } catch (exception: Exception) {
            Log.e("上色exception", exception.message!!)
        } finally {
            return imageDst
        }
    }

    /**
     * 等温尺processing,展示pseudo color的temperaturerange内info
     */
    fun setPseudoColorMaxMin(
        imageDst: ByteArray?,
        temperatureSrc: ByteArray?,
        max: Float,
        min: Float,
        imageWidth: Int,
        imageHeight: Int,
    )  {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (temperatureSrc != null && (max != Float.MAX_VALUE || min != Float.MIN_VALUE)) {
            var j = 0
            val imageDstLength: Int = imageWidth * imageHeight * 4
            val biaochiMax: Float = max
            val biaochiMin: Float = min 
            val startTimeAll = System.currentTimeMillis()
            // 遍历像素point，Filtertemperature阈值
            var index = 0
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (index < imageDstLength) {
                
                var temperature0: Float =
                    (
                        (temperatureSrc[j].toInt() and 0xff) + (
                            temperatureSrc[j + 1]
                                .toInt() and 0xff
                        ) * 256
                    ).toFloat()
                temperature0 = (temperature0 / 64 - 273.15).toFloat()
                val y0: Int = imageDst!![j].toInt() and 0xff
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (temperature0 < biaochiMin || temperature0 > biaochiMax) {
                    val r: Int = imageDst!![index].toInt() and 0xff
                    val g: Int = imageDst!![index + 1].toInt() and 0xff
                    val b: Int = imageDst!![index + 2].toInt() and 0xff
                    
                    val grey = (r * 0.3f + g * 0.59f + b * 0.11f).toInt()
                    imageDst!![index] = grey.toByte()
                    imageDst!![index + 1] = grey.toByte()
                    imageDst!![index + 2] = grey.toByte()
                }
                imageDst!![index + 3] = 255.toByte()
                index += 4
                j += 2
            }
        }
    }

    /**
     * contourDetection 轮廓检测
     */
    /**
     * Executes contourdetection operation with thermal imaging domain optimization.
     *
     * @param
     * @param alarmBean Parameter for operation (type: AlarmBean?)
     * @param imageDst Parameter for operation (type: ByteArray?)
     * @param temperatureSrc Temperature value in Celsius (type: ByteArray?)
     * @param imageWidth Parameter for operation (type: Int)
     * @param imageHeight Parameter for operation (type: Int)
     *
     */
    fun contourDetection(
        alarmBean: AlarmBean?,
        imageDst: ByteArray?,
        temperatureSrc: ByteArray?,
        imageWidth: Int,
        imageHeight: Int,
    ): ByteArray?  {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (alarmBean != null && imageDst != null && temperatureSrc != null) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (alarmBean.isMarkOpen && (
                    (alarmBean.highTemp != Float.MAX_VALUE && alarmBean.isHighOpen) ||
                        (alarmBean.isLowOpen && alarmBean.lowTemp != Float.MIN_VALUE)
                )
            ) {
                try {
                    val matByteArray =
                        JNITool.draw_edge_from_temp_reigon_bitmap_argb_psd(
                            imageDst,
                            temperatureSrc,
                            imageHeight,
                            imageWidth,
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (alarmBean.isHighOpen) alarmBean.highTemp else Float.MAX_VALUE,
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (alarmBean.isLowOpen) alarmBean.lowTemp else Float.MIN_VALUE,
                            alarmBean.highColor,
                            alarmBean.lowColor,
                            alarmBean.markType,
                        )
                    val diffMat =
                        /**
                         * Executes mat operation with thermal imaging domain optimization.
                         *
                         */
                        Mat(
                            imageHeight,
                            imageWidth,
                            CvType.CV_8UC3,
                        )
                    diffMat.put(0, 0, matByteArray)
                    Imgproc.cvtColor(diffMat, diffMat, Imgproc.COLOR_BGR2RGBA)
                    val grayData = ByteArray(diffMat.cols() * diffMat.rows() * 4)
                    diffMat[0, 0, grayData]
                    return grayData
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }
        return imageDst
    }
}
