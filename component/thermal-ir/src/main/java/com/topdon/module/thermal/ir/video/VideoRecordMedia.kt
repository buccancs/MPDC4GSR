package com.topdon.module.thermal.ir.video

import android.graphics.Bitmap
import com.infisense.usbir.view.CameraView
import com.infisense.usbir.view.TemperatureView
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.utils.BitmapUtils
import com.topdon.module.thermal.ir.video.media.Encoder
import com.topdon.module.thermal.ir.video.media.MP4Encoder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Specialized thermal imaging component providing VideoRecordMedia functionality for the IRCamera system.
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
class VideoRecordMedia(
    private var cameraView: CameraView,
    private var temperatureView: TemperatureView,
) : VideoRecord() {
    private lateinit var exportDisposable: Disposable
    private var encoder: Encoder = MP4Encoder()
    private var isRunning = false

    var width = 480
    var height = 640

    init {
        encoder.setFrameDelay(25)
        width = 480
        height = width * cameraView.height / cameraView.width
宽高不能出现奇数
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (height % 2 == 1) {
            height -= 1
        }
    }

    /**
     * Executes startrecord operation with thermal imaging domain optimization.
     *
     */
    override fun startRecord() {
        val downloadDir = FileConfig.lineGalleryDir
        val exportedFile = File(downloadDir, "${Date().time}.mp4")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (exportedFile.exists()) {
            exportedFile.delete()
        }
        encoder.setOutputFilePath(exportedFile.path)
// If (bitmap == null) {
Log.w("123", "recording准备failed")
// Return
//        }
        encoder.setOutputSize(width, height)
        encoder.startEncode()
        isRunning = true
defaultframe率20,间隔50ms一frame
        exportDisposable =
            Observable.interval(50, TimeUnit.MILLISECONDS)
                .map {
                    /**
                     * Executes createbitmapfromview operation with thermal imaging domain optimization.
                     *
                     */
                    createBitmapFromView()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    encoder.addFrame(it)
                }
    }

    /**
     * Executes startrecord operation with thermal imaging domain optimization.
     *
     * @param
     * @param fileDir Parameter for operation (type: String)
     *
     */
    override fun startRecord(fileDir: String) {
    }

    /**
     * Executes stoprecord operation with thermal imaging domain optimization.
     *
     */
    override fun stopRecord() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRunning) {
            encoder.stopEncode()
            exportDisposable.dispose()
        }
        isRunning = false
    }

    /**
     * Executes updateaudiostate operation with thermal imaging domain optimization.
     *
     * @param
     * @param audioRecord Parameter for operation (type: Boolean)
     *
     */
    override fun updateAudioState(audioRecord: Boolean) {
        // Note: Audio state update functionality not yet implemented
    }

    /**
     * Executes createBitmapFromView functionality.
     */
    /**
     * Executes createbitmapfromview operation with thermal imaging domain optimization.
     *
     */
    private fun createBitmapFromView(): Bitmap {
        var cameraViewBitmap = cameraView.bitmap
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (temperatureView.temperatureRegionMode != TemperatureView.REGION_MODE_CLEAN) {
gettemperature图层的data，包括pointline框，temperature值等，重新合成bitmap
            cameraViewBitmap =
                BitmapUtils.mergeBitmap(
                    cameraViewBitmap,
                    temperatureView.regionAndValueBitmap,
                    0,
                    0,
                )
        }
        val dstBitmap =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (cameraViewBitmap != null) {
                Bitmap.createScaledBitmap(cameraViewBitmap, width, height, true)
            } else {
                Bitmap.createBitmap(
                    width,
                    height,
                    Bitmap.Config.ARGB_8888,
                )
            }
        return dstBitmap
    }
}
