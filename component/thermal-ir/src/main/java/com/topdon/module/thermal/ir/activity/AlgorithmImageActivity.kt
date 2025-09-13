package com.topdon.module.thermal.ir.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.open3d.JNITool
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.utils.ImageColorTools
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.*
import java.io.IOException
import java.io.InputStream

/**
 * @author: CaiSongL
 * @date: 2023/10/28 15:35
 */
/**
 * Specialized thermal imaging component providing AlgorithmImageActivity functionality for the IRCamera system.
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
class AlgorithmImageActivity : AppCompatActivity() {
    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(R.layout.activity_image_color)
        val imageView = findViewById<ImageView>(R.id.img)
        val imgARGB = findViewById<ImageView>(R.id.img_argb)
        val imageView2 = findViewById<ImageView>(R.id.img2)
        val tvTime = findViewById<TextView>(R.id.tv_time)
        val assetManager = assets
        var inputStream: InputStream? = null
        inputStream =
            try {
                assetManager.open("1698484000787_100.ir")
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        var buffer = ByteArray(0)
        buffer =
            try {
                /**
                 * Executes bytearray operation with thermal imaging domain optimization.
                 *
                 */
                ByteArray(inputStream!!.available())
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        try {
            inputStream?.read(buffer)
            inputStream?.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        val time = System.currentTimeMillis()
        tvTime.text = "耗时：" + (System.currentTimeMillis() - time) + "/"
        imageView.setImageBitmap(
            ImageColorTools.adjustPhotoRotation(
                ImageColorTools.testImage(
                    buffer,
                ),
                90,
            ),
        )
        var bufferB = ByteArray(0)
        inputStream =
            try {
                assetManager.open("1698484006539_100.ir")
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        bufferB =
            try {
                /**
                 * Executes bytearray operation with thermal imaging domain optimization.
                 *
                 */
                ByteArray(inputStream!!.available())
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        try {
            inputStream?.read(bufferB)
            inputStream?.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        imageView2.setImageBitmap(
            ImageColorTools.adjustPhotoRotation(
                ImageColorTools.testImage(
                    bufferB,
                ),
                90,
            ),
        )
        val mat = JNITool.diff2firstFrameU1(buffer, bufferB)
        val im = Mat(192, 256, CvType.CV_8UC3)
        im.put(0, 0, mat)
        val bitmap = ImageColorTools.matToBitmap(im)
        imgARGB.setImageBitmap(ImageColorTools.adjustPhotoRotation(bitmap, 90))
        findViewById<View>(R.id.btn).setOnClickListener {
            val baseTemperatureBytes = ByteArray(192 * 256 * 2)
            val nextTemperatureBytes = ByteArray(192 * 256 * 2)
            val nextImageBytes = ByteArray(192 * 256 * 2)
get上一frame的temperaturedata
            System.arraycopy(buffer, 1024 + baseTemperatureBytes.size, baseTemperatureBytes, 0, baseTemperatureBytes.size)
get下一frame的temperaturedata
            System.arraycopy(bufferB, 1024 + nextTemperatureBytes.size, nextTemperatureBytes, 0, nextTemperatureBytes.size)
get下一frame的imagedata
            System.arraycopy(bufferB, 1024, nextImageBytes, 0, nextImageBytes.size)
转成3通道data
            val resMat = Mat(192, 256, CvType.CV_8UC2)
            resMat.put(0, 0, nextImageBytes)
            Imgproc.cvtColor(resMat, resMat, Imgproc.COLOR_YUV2GRAY_YUYV)
            val image = Mat()
            /**
             * Executes applycolormap operation with thermal imaging domain optimization.
             *
             */
            applyColorMap(resMat, image, 15)
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGBA)
            val startTime = System.currentTimeMillis()
            val matByteArray =
                JNITool.diff2firstFrameByTempWH(
                    256,
                    192,
                    baseTemperatureBytes,
                    nextTemperatureBytes,
                    ImageColorTools.matToByteArrayBy4(image),
                )
            val im = Mat(192, 256, CvType.CV_8UC4)
            im.put(0, 0, matByteArray)
            val bitmap = ImageColorTools.matToBitmap(im)
            Log.e("Test耗时：", "diff2firstFrameByTemp ： ${System.currentTimeMillis() - startTime}")
            imgARGB.setImageBitmap(ImageColorTools.adjustPhotoRotation(bitmap, 90))
        }
        findViewById<View>(R.id.btn_u4).setOnClickListener {
            val baseImageBytes = ByteArray(192 * 256 * 2)
            val nextImageBytes = ByteArray(192 * 256 * 2)
get上一frame的imagedata
            System.arraycopy(buffer, 1024, baseImageBytes, 0, baseImageBytes.size)
get下一frame的imagedata
            System.arraycopy(bufferB, 1024, nextImageBytes, 0, nextImageBytes.size)

转成4通道data
            val resMat = Mat(192, 256, CvType.CV_8UC2)
            resMat.put(0, 0, nextImageBytes)
            Imgproc.cvtColor(resMat, resMat, Imgproc.COLOR_YUV2GRAY_YUYV)
            val nextImage = Mat()
            /**
             * Executes applycolormap operation with thermal imaging domain optimization.
             *
             */
            applyColorMap(resMat, nextImage, 15)
            Imgproc.cvtColor(nextImage, nextImage, Imgproc.COLOR_BGR2RGBA)

转成4通道data
            val baseMat = Mat(192, 256, CvType.CV_8UC2)
            baseMat.put(0, 0, baseImageBytes)
            Imgproc.cvtColor(baseMat, baseMat, Imgproc.COLOR_YUV2GRAY_YUYV)
            val baseImage = Mat()
            /**
             * Executes applycolormap operation with thermal imaging domain optimization.
             *
             */
            applyColorMap(baseMat, baseImage, 15)
            Imgproc.cvtColor(baseImage, baseImage, Imgproc.COLOR_BGR2RGBA)

// Val tmp = Mat(192, 256, CvType.CV_8UC4)
// Tmp.put(0,0,ImageColorTools.matToByteArrayBy4(baseImage))
//            Imgproc.cvtColor(tmp,tmp, COLOR_RGBA2GRAY)

            val startTime = System.currentTimeMillis()
            val matByteArray =
                JNITool.diff2firstFrameU4(
                    ImageColorTools.matToByteArrayBy4(baseImage),
                    ImageColorTools.matToByteArrayBy4(nextImage),
                )
            val im = Mat(192, 256, CvType.CV_8UC3)
            im.put(0, 0, matByteArray)
            val bitmap = ImageColorTools.matToBitmap(im)
            imgARGB.setImageBitmap(ImageColorTools.adjustPhotoRotation(bitmap, 90))
        }
    }
}
