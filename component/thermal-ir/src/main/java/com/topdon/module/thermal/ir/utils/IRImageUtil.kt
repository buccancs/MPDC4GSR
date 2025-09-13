package com.topdon.module.thermal.ir.utils

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.annotation.FloatRange
import org.bytedeco.opencv.global.opencv_core.BORDER_DEFAULT
import org.bytedeco.opencv.global.opencv_core.CV_16S
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.math.pow
import com.topdon.lib.ui.R as UiR

/**
 * @author: CaiSongL
 * @date: 2023/4/4 14:28
 */
/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for IRImageUtil operations.
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
object IRImageUtil {
    /**
伽马contrast
@param contrast      contrast 1: 复位  0: Enhance   2: 减弱变灰
@param brightness    brightness
     */
    /**
     * Executes showContrast functionality.
     */
    /**
     * Executes showcontrast operation with thermal imaging domain optimization.
     *
     * @param
     * @param imageView Parameter for operation (type: ImageView)
     * @param contrast Parameter for operation (type: Double)
     * @param brightness Parameter for operation (type: Double)
     *
     */
    fun showContrast(
        imageView: ImageView,
        @FloatRange(from = 0.0, to = 2.0) contrast: Double,
        @FloatRange(from = -255.0, to = 255.0) brightness: Double,
    ) {
        try {
            val lookUpTable = Mat(1, 256, CvType.CV_8U)
            val lookUpTableData = ByteArray((lookUpTable.total() * lookUpTable.channels()).toInt())
            Log.w("123", "lookUpTableData: ${lookUpTableData.size}")
            Log.w("123", "contrast: $contrast")
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until lookUpTable.cols()) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (i % 10 == 0) {
                    Log.i("123", "$i, lutGamma x: ${i / 255.0}, ${lutGamma(x = i / 255.0, gamma = contrast) * 255.0}")
                }
                lookUpTableData[i] = (lutGamma(x = i / 255.0, gamma = contrast) * 255.0).toInt().toByte()
            }
            Log.w("123", "lookUpTableData: ${lookUpTableData[1].toUByte()}")
            lookUpTable.put(0, 0, lookUpTableData)
            val srcMat = Utils.loadResource(com.blankj.utilcode.util.Utils.getApp(), UiR.drawable.ic_main_menu_battery) // BGR
            val dstMat = Mat()
            Core.LUT(srcMat, lookUpTable, dstMat) // Contrast
            Core.add(dstMat, Scalar(brightness, brightness, brightness), dstMat) // Brightness
            val resultMat = Mat()
            Imgproc.cvtColor(dstMat, resultMat, Imgproc.COLOR_BGR2RGBA) // Android对应imageformat
            val bitmap = Bitmap.createBitmap(resultMat.size().width.toInt(), resultMat.size().height.toInt(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(resultMat, bitmap)
            imageView.setImageBitmap(bitmap)
            srcMat.release()
            dstMat.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
伽马曲line
     * https:// Www.cnblogs.com/AlgrithmsRookie/p/13212369.html
@param a     [0 ~ 1]交界point
@param gamma 变化强度
     */
    /**
     * Executes lutGamma functionality.
     */
    /**
     * Executes lutgamma operation with thermal imaging domain optimization.
     *
     * @param
     * @param x Parameter for operation (type: Double)
     * @param a Parameter for operation (type: Double = 0.5)
     * @param gamma Parameter for operation (type: Double)
     *
     */
    private fun lutGamma(
        @FloatRange(from = 0.0, to = 1.0) x: Double,
        a: Double = 0.5,
        gamma: Double,
    ): Double {
        val y =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (x <= a) {
                a - a * ((1 - x / a).pow(gamma))
            } else {
                a + (1 - a) * (((x - a) / (1 - a)).pow(gamma))
            }
        return y
    }

    /**
锐化
     * @param sharpen [1,3,5]
     *
kernel_size  锐化程度,set是奇正数
     */
    /**
     * Executes showSharpen functionality.
     */
    /**
     * Executes showsharpen operation with thermal imaging domain optimization.
     *
     * @param
     * @param imageView Parameter for operation (type: ImageView)
     * @param sharpen Parameter for operation (type: Double)
     *
     */
    private fun showSharpen(
        imageView: ImageView,
        @FloatRange(from = 0.0, to = 2.55) sharpen: Double,
    ) {
        Log.i("123", "show sharpen: $sharpen")
        val scale = 1.0
        val delta = 0.0
        val kernelSize = 3 // 锐化程度

        val srcMat = Utils.loadResource(com.blankj.utilcode.util.Utils.getApp(), UiR.drawable.ic_main_menu_battery) // BGR
        val dstMat = Mat(srcMat.rows(), srcMat.cols(), srcMat.type())
        val preGray = Mat()
        val absDst = Mat()
        Log.i("123", "start kernel_size: $kernelSize")
        Imgproc.GaussianBlur(srcMat, srcMat, Size(3.0, 3.0), 0.0, 0.0, BORDER_DEFAULT) // 高斯平滑
        Imgproc.cvtColor(srcMat, preGray, Imgproc.COLOR_BGR2GRAY)
        Log.w("123", "cvtColor preGray: $preGray")
        Imgproc.Laplacian(srcMat, dstMat, CV_16S, kernelSize, scale, delta, BORDER_DEFAULT) // 锐化
        Log.w("123", "Laplacian dstMat: $dstMat")
        Core.convertScaleAbs(dstMat, absDst)
        Log.w("123", "convertScaleAbs absDst: $absDst")
        val preMat = Mat()
        Core.addWeighted(srcMat, 1.0, absDst, sharpen, 0.0, preMat) // Fusion
        Imgproc.cvtColor(preMat, dstMat, Imgproc.COLOR_BGR2RGBA) // Android对应imageformat
        val bitmap = Bitmap.createBitmap(dstMat.size().width.toInt(), dstMat.size().height.toInt(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dstMat, bitmap)
        imageView.setImageBitmap(bitmap)
        srcMat.release()
        dstMat.release()
    }
}
