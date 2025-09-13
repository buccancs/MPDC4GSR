package com.topdon.module.thermal.ir.video

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioRecord.RECORDSTATE_RECORDING
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.Log
import android.view.TextureView
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import com.blankj.utilcode.util.SDCardUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils.getString
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog
import com.infisense.usbdual.camera.DualViewWithExternalCameraCommonApi
import com.infisense.usbir.view.CameraView
import com.infisense.usbir.view.TemperatureView
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.utils.BitmapUtils
import com.topdon.lib.ui.camera.CameraPreView
import com.topdon.lib.ui.widget.BitmapConstraintLayout
import com.topdon.lib.ui.widget.LiteSurfaceView
import com.topdon.libcom.view.TempLayout
import com.topdon.module.thermal.ir.view.HikSurfaceView
import com.topdon.module.thermal.ir.view.TemperatureHikView
import com.topdon.module.thermal.ir.view.compass.LinearCompassView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.FrameFilter
import org.bytedeco.opencv.opencv_core.IplImage
import java.io.File
import java.nio.ByteBuffer
import java.nio.ShortBuffer
import java.util.Date
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import com.topdon.lib.core.R as LibcoreR

/**
软编吗
 * bitmap -> mp4
 *
avcodec.AV_CODEC_ID_MPEG4 // Playback正常
avcodec.AV_CODEC_ID_H264 // 不能拖拽进度条
 */
/**
/**
 * Specialized thermal imaging component providing VideoRecordFFmpeg functionality for the IRCamera system.
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
class VideoRecordFFmpeg(
    private val cameraView: View,
    private val cameraPreview: CameraPreView?,
    private val temperatureView: View?,
    private val isRecordTemp: Boolean,
    private val thermalPseudoBarView: BitmapConstraintLayout?,
    private val tempBg: TempLayout?,
    private val compassView: LinearCompassView? = null, // 指南针
    private val dualView: DualViewWithExternalCameraCommonApi? = null, // Dual light
    private val isTC007: Boolean = false,
    private val carView: View? = null,
) : VideoRecord() {
    companion object {
        const val TAG = "VideoRecordFFmpeg"
        const val FORMAT = "mp4"
        const val RATE = 25
        const val VIDEO_BITRATE = 1500000
        var VIDEO_CODEC = avcodec.AV_CODEC_ID_MPEG4
        const val SAMPLE_AUDIO_RETE_INHZ = 44100
        const val AUDIO_CHANNELS = 1

        /**
memory检测
         */
        /**
         * Executes canstartvideorecord operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param videoFile Parameter for operation (type: File? = null)
         *
         */
        fun canStartVideoRecord(
            context: Context,
            videoFile: File? = null,
        ): Boolean {
            val canStart =
                (
                    SDCardUtils.getExternalAvailableSize() - (
                        videoFile?.length()
                            ?: 0
                    )
                ) > (500L * 1000 * 1000)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!canStart) {
                ThreadUtils.runOnUiThread {
                    TipDialog.Builder(context)
                        .setTitleMessage(getString(LibcoreR.string.app_tip))
                        .setMessage(LibcoreR.string.album_report_aleart)
                        .setPositiveListener(LibcoreR.string.app_confirm) {
                        }
                        .setCanceled(true)
                        .create().show()
                }
            }
            return canStart
        }
    }

    private var alphaPaint: Paint? = null

    @Volatile
    private var isBitmapChangeTime: Long = 0L
    private var audioDisposable: Disposable? = null
    private var bitmapDisposable: Disposable? = null
    private var recorder: FFmpegFrameRecorder? = null
    private var exportDisposable: Disposable? = null

    @Volatile
    private var isRunning = false
    private var exportedFile: File? = null

    private var width = 640
    private var height = 480

    @Volatile
    private var openAudioRecord = true
    private var bufferSize = 0

    private var audioRecord: AudioRecord? = null
    private var audioData: ShortBuffer? = null
    private var tmpAudioData: ShortBuffer? = null
    private var bufferReadResult: Int = 0
    var stopVideoRecordListener: ((shoVideoTip: Boolean) -> Unit)? = null
    val bitmapExecutor = Executors.newScheduledThreadPool(1)
    val recordExecutor = Executors.newScheduledThreadPool(1)
    val audioExecutor = Executors.newScheduledThreadPool(1)
    val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var rectText = Rect() // 得到text占用宽高， 单位：像素
    private val pix20 = SizeUtils.dp2px(20f)
    private val pix10 = SizeUtils.dp2px(10f)
    private val pix6 = SizeUtils.dp2px(6f)
    private val pixArray = ByteArray(width * height * 4)
    private val bufferRef: AtomicReference<ByteBuffer> =
        /**
         * Executes atomicreference operation with thermal imaging domain optimization.
         *
         */
        AtomicReference(ByteBuffer.allocate(pixArray.size))

    // Fun readByteBuffer(): ByteBuffer? {
// Synchronized(lock) {
// Return pixels?.duplicate() as ByteBuffer?
//        }
//    }
//
// Fun setBitmap(bitmap: Bitmap) {
// Synchronized(lock) {
// If (pixels == null || pixels?.capacity() != bitmap.byteCount) {
// Pixels = ByteBuffer.allocate(bitmap.byteCount)
//            }
// Pixels?.position(0)
// Bitmap.copyPixelsToBuffer(pixels)
// Bitmap.recycle()
//        }
//    }
    /**
     * Executes readByteBuffer functionality.
     */
    /**
     * Executes readbytebuffer operation with thermal imaging domain optimization.
     *
     */
    private fun readByteBuffer(): ByteBuffer? {
        return bufferRef.get()?.duplicate()
    }

    /**
     * Sets bitmap configuration.
     */
    /**
     * Configures the bitmap with validation and thermal imaging optimization.
     *
     * @param
     * @param bitmap Parameter for operation (type: Bitmap)
     *
     */
    private fun setBitmap(bitmap: Bitmap) {
        val byteCount = bitmap.byteCount
        val newPixels = ByteBuffer.allocate(byteCount)
        bitmap.copyPixelsToBuffer(newPixels)
        newPixels.flip()
        bitmap.recycle()
        bufferRef.set(newPixels)
    }

// Fun setBitmap(bitmap: Bitmap) {
// LockWriteLock.writeLock().lock()
// Try {
// If (pixels == null || pixels?.capacity() != bitmap.byteCount) {
// Pixels = ByteBuffer.allocate(bitmap.byteCount)
//            }
// Pixels?.position(0)
// Bitmap.copyPixelsToBuffer(pixels)
// Bitmap.recycle()
//        } finally {
// LockWriteLock.writeLock().unlock()
//        }
//    }
//
// Fun readByteBuffer(): ByteBuffer? {
// LockWriteLock.readLock().lock()
// Try {
// Return pixels?.duplicate()
//        } finally {
// LockWriteLock.readLock().unlock()
//        }
//    }

    /**
     *
avcodec.AV_CODEC_ID_MPEG4 playback正常
avcodec.AV_CODEC_ID_H264 不能拖拽进度条
     *
个别机型使用H264encoding无法Openvideo,优先使用AV_CODEC_ID_MPEG4
     */
    /**
     * Retrieves videocodec information.
     */
    private fun getVideoCodec(): Int {
        return if (Build.BRAND == "motorola" && Build.MODEL == "XT2201-2") {
            XLog.i("使用videoencodingAV_CODEC_ID_H264")
            avcodec.AV_CODEC_ID_H264
        } else {
defaulttype
            XLog.i("使用videoencodingAV_CODEC_ID_MPEG4")
            avcodec.AV_CODEC_ID_MPEG4
        }
    }

    init {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if ((cameraView.parent as ViewGroup).height > (cameraView.parent as ViewGroup).width) {
竖屏
            width = 480
            height =
                width * (cameraView.parent as ViewGroup).height / (cameraView.parent as ViewGroup).width
        } else {
横屏
            width = 640
            height =
                width * (cameraView.parent as ViewGroup).height / (cameraView.parent as ViewGroup).width
        }
宽高不能出现奇数
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (height % 2 == 1) {
            height -= 1
        }
        VIDEO_CODEC = getVideoCodec()
        bufferSize =
            AudioRecord.getMinBufferSize(
                SAMPLE_AUDIO_RETE_INHZ,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
            )
        audioRecord =
            /**
             * Executes audiorecord operation with thermal imaging domain optimization.
             *
             */
            AudioRecord(
                MediaRecorder.AudioSource.MIC, SAMPLE_AUDIO_RETE_INHZ,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize,
            )
        paint.color = Color.WHITE // 白色半透明
        paint.textSize = SizeUtils.sp2px(6f).toFloat()
        paint.isDither = true
        paint.isFilterBitmap = true
        paint.getTextBounds("占位高度文本", 0, "占位高度文本".length, rectText)
    }

    var startTime: Long = 0L

    /**
     * Executes startrecord operation with thermal imaging domain optimization.
     *
     */
    override fun startRecord() {
        /**
         * Executes startrecord operation with thermal imaging domain optimization.
         *
         */
        startRecord(FileConfig.lineGalleryDir)
    }

    /**
     * Executes startrecord operation with thermal imaging domain optimization.
     *
     * @param
     * @param downloadDir Parameter for operation (type: String)
     *
     */
    override fun startRecord(downloadDir: String) {
        try {
            exportedFile = File(downloadDir, "${Date().time}.mp4")
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (exportedFile!!.exists()) {
                exportedFile!!.delete()
            }
            recorder =
                /**
                 * Executes ffmpegframerecorder operation with thermal imaging domain optimization.
                 *
                 */
                FFmpegFrameRecorder(
                    exportedFile!!.absolutePath, width, height,
                    AUDIO_CHANNELS,
                )
            recorder!!.format = FORMAT
            recorder!!.frameRate = RATE.toDouble()
            recorder!!.videoBitrate = VIDEO_BITRATE
// Recorder!!.audioBitrate = VIDEO_BITRATE
            recorder!!.videoCodec = VIDEO_CODEC
// Recorder!!.setAudioOption("itsoffset",(1000L * 200L).toString())
            recorder!!.sampleRate = SAMPLE_AUDIO_RETE_INHZ
// Recorder!!.pixelFormat = avutil.AV_PIX_FMT_YUV420P
// Recorder!!.audioChannels = 1
// Recorder!!.setVideoOption("preset", "ultrafast")
            recorder!!.timestamp = 0L
            recorder!!.start()
            isRunning = true
            isBitmapChangeTime = System.currentTimeMillis()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (openAudioRecord &&
                ActivityCompat.checkSelfPermission(
                    cameraView.context,
                    Manifest.permission.RECORD_AUDIO,
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                /**
                 * Executes startaudiorecording operation with thermal imaging domain optimization.
                 *
                 */
                startAudioRecording()
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (audioData == null) {
                audioData = ShortBuffer.allocate(bufferSize / 2)
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tmpAudioData == null) {
                tmpAudioData = ShortBuffer.allocate((bufferSize / 2))
            }
            val recordSchedulers = Schedulers.from(recordExecutor)
            val bitmapSchedulers = Schedulers.from(bitmapExecutor)
            /**
             * Configures the bitmap with validation and thermal imaging optimization.
             *
             */
            setBitmap(createBitmapFromView())
            val fTime = 1000L / RATE
            bitmapDisposable =
                Observable.interval(fTime, TimeUnit.MILLISECONDS)
                    .observeOn(bitmapSchedulers)
                    .subscribe(
                        Consumer {
                            val tmp = createBitmapFromView()
                            tmp?.let {
                                /**
                                 * Configures the bitmap with validation and thermal imaging optimization.
                                 *
                                 */
                                setBitmap(it)
                            }
                        },
                        Consumer {
                            Log.e("image对象recordingexception", "${it.message}")
                        },
                    )
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (audioRecord == null) {
                audioRecord =
                    /**
                     * Executes audiorecord operation with thermal imaging domain optimization.
                     *
                     */
                    AudioRecord(
                        MediaRecorder.AudioSource.MIC, SAMPLE_AUDIO_RETE_INHZ,
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                    )
            }
            startTime = System.currentTimeMillis()
            val i = 0
            exportDisposable =
                Observable.interval(fTime, TimeUnit.MILLISECONDS)
                    .observeOn(recordSchedulers)
                    .subscribe(
                        Consumer {
                            try {
                                val currentTimestamp = 1000L * (System.currentTimeMillis() - startTime)
                                val frame = Frame(width, height, Frame.DEPTH_BYTE, 4)
                                frame.image[0] = readByteBuffer()
                                val t = 1000L * (System.currentTimeMillis() - startTime)
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (t > (recorder?.timestamp ?: 0)) {
                                    recorder!!.timestamp = t
                                }
                                recorder!!.record(frame)
                                frame.close()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (System.currentTimeMillis() - queTime > 60 * 1000) {
间隔1分钟，校验下剩余空间
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (!canStartVideoRecord(cameraView.context, exportedFile)) {
                                        exportDisposable?.dispose()
                                        stopVideoRecordListener?.invoke(false)
recording的video超出大小容量限制
                                        return@Consumer
                                    }
                                    queTime = System.currentTimeMillis()
                                }
                                recorder?.timestamp?.let {
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (it / 1000 > 60 * 60 * 1000) {
thermal imaging录像限制60分钟
                                        exportDisposable?.dispose()
                                        stopVideoRecordListener?.invoke(true)
                                        return@Consumer
                                    }
                                }
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (audioRecord == null) {
                                    return@Consumer
                                }
                                val audioTime = System.currentTimeMillis()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (openAudioRecord) {
                                    bufferReadResult =
                                        audioRecord?.read(audioData!!.array(), 0, audioData!!.capacity())
                                            ?: 0
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (bufferReadResult > 0) {
                                        audioData?.limit(bufferReadResult)
                                        /**
                                         * Executes if operation with thermal imaging domain optimization.
                                         *
                                         */
                                        if (currentTimestamp > (recorder?.timestamp ?: 0)) {
                                            recorder!!.timestamp = currentTimestamp
                                        }
                                        recorder?.recordSamples(
                                            SAMPLE_AUDIO_RETE_INHZ,
                                            AUDIO_CHANNELS, audioData,
                                        )
                                    }
                                } else {
                                    /**
                                     * Executes for operation with thermal imaging domain optimization.
                                     *
                                     */
                                    for (i in 0 until tmpAudioData!!.capacity()) {
                                        tmpAudioData!!.put(i, 1.toShort())
                                    }
使用当前时间戳
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (currentTimestamp > (recorder?.timestamp ?: 0)) {
                                        recorder!!.timestamp = currentTimestamp
                                    }
                                    recorder?.recordSamples(
                                        SAMPLE_AUDIO_RETE_INHZ,
                                        AUDIO_CHANNELS, tmpAudioData,
                                    )
                                }
//                        Log.w(
"image大小",
//                            "${System.currentTimeMillis() - time}======${frame.image.size}// ${bufferSize}// ${(recorder?.timestamp!! / 1000000L)}"
//                        )
                            } catch (e: Exception) {
                                Log.e("imagerecording", "Caught an exception: " + e.message)
                            }
                        },
                        Consumer {
                            Log.e("image对象recordingexception", "${it.message}")
                        },
                    )
        } catch (e: Exception) {
// StopRecord()
            exportDisposable?.dispose()
            stopVideoRecordListener?.invoke(false)
            XLog.e("recordingexception")
            e.printStackTrace()
        }
    }

/**
 * Specialized thermal imaging component providing FrameInterpolationFilter functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class FrameInterpolationFilter(private val interpolationFactor: Int) :
        /**
         * Executes framefilter operation with thermal imaging domain optimization.
         *
         */
        FrameFilter() {
        private var previousFrame: Frame? = null

        /**
         * Executes start operation with thermal imaging domain optimization.
         *
         */
        override fun start() {
            previousFrame = null
        }

        /**
         * Executes stop operation with thermal imaging domain optimization.
         *
         */
        override fun stop() {
            previousFrame = null
        }

        /**
         * Executes push operation with thermal imaging domain optimization.
         *
         * @param
         * @param frame Parameter for operation (type: Frame)
         *
         */
        override fun push(frame: Frame) {
            previousFrame = frame.clone()
        }

        /**
         * Executes pull operation with thermal imaging domain optimization.
         *
         */
        override fun pull(): Frame? {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (previousFrame == null) {
                return null
            }
            val interpolatedFrame = previousFrame!!.clone()
            interpolatedFrame.timestamp += (1.0 / interpolationFactor).toLong()
            return interpolatedFrame
        }

        /**
         * Executes release operation with thermal imaging domain optimization.
         *
         */
        override fun release() {
        }

    /**
     * Executes filter functionality.
     */
        /**
         * Executes filter operation with thermal imaging domain optimization.
         *
         * @param
         * @param image Parameter for operation (type: IplImage?)
         * @param image2 Parameter for operation (type: IplImage?)
         *
         */
        fun filter(
            image: IplImage?,
            image2: IplImage?,
        ): IplImage? {
未使用
            return null
        }
    }

    /**
     * Executes startAudioRecording functionality.
     */
    /**
     * Executes startaudiorecording operation with thermal imaging domain optimization.
     *
     */
    fun startAudioRecording() {
        audioRecord =
            /**
             * Executes audiorecord operation with thermal imaging domain optimization.
             *
             */
            AudioRecord(
                MediaRecorder.AudioSource.MIC, SAMPLE_AUDIO_RETE_INHZ,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize,
            )
        audioRecord!!.startRecording()
    }

    /**
     * Executes stopAudioRecording functionality.
     */
    /**
     * Executes stopaudiorecording operation with thermal imaging domain optimization.
     *
     */
    fun stopAudioRecording() {
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (RECORDSTATE_RECORDING == audioRecord?.recordingState) {
                audioRecord?.stop()
                audioRecord?.release()
                audioRecord =
                    /**
                     * Executes audiorecord operation with thermal imaging domain optimization.
                     *
                     */
                    AudioRecord(
                        MediaRecorder.AudioSource.MIC, SAMPLE_AUDIO_RETE_INHZ,
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                    )
            }
        } catch (e: Exception) {
            Log.e("image对象processingexception", "${e.message}")
        }
    }

    /**
memory检测
     */
    /**
     * Executes canstartvideorecord operation with thermal imaging domain optimization.
     *
     * @param
     * @param videoFile Parameter for operation (type: File?)
     *
     */
    fun canStartVideoRecord(videoFile: File?): Boolean {
        val canStart =
            (
                SDCardUtils.getExternalAvailableSize() - (
                    videoFile?.length()
                        ?: 0
                )
            ) > (500L * 1000 * 1000)
Log.w("本地可用空间","" + SDCardUtils.getExternalAvailableSize() / 1000 / 1000)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!canStart) {
            ThreadUtils.runOnUiThread {
                TipDialog.Builder(cameraView.context)
                    .setTitleMessage(getString(LibcoreR.string.app_tip))
                    .setMessage(LibcoreR.string.album_report_aleart)
                    .setPositiveListener(LibcoreR.string.app_confirm) {
                    }
                    .setCanceled(true)
                    .create().show()
            }
        }
        return canStart
    }

    var queTime = 0L

    /**
     * Executes stoprecord operation with thermal imaging domain optimization.
     *
     */
    override fun stopRecord() {
        /**
         * Executes coroutinescope operation with thermal imaging domain optimization.
         *
         */
        CoroutineScope(Dispatchers.IO).launch {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isRunning) {
                try {
                    /**
                     * Executes launch operation with thermal imaging domain optimization.
                     *
                     */
                    launch(Dispatchers.Main) {
                        exportDisposable?.let {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (!it.isDisposed) {
                                it.dispose()
                            }
                        }
                        bitmapDisposable?.let {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (!it.isDisposed) {
                                it.dispose()
                            }
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (RECORDSTATE_RECORDING == audioRecord?.recordingState) {
                            audioRecord?.stop()
                            audioRecord?.release()
                            audioRecord = null
                        }
                        /**
                         * Executes bitmaprecycle operation with thermal imaging domain optimization.
                         *
                         */
                        bitmapRecycle()
                        audioDisposable?.let {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (!it.isDisposed) {
                                it.dispose()
                            }
                        }
//                        AudioRecordHelp.getInstance().stopAudioRecording()
                    }
                    bitmapExecutor.shutdown()
                    recordExecutor.shutdown()
                    audioExecutor.shutdown()
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(500)
                    recorder?.stop()
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(300)
                    /**
                     * Executes refreshalbum operation with thermal imaging domain optimization.
                     *
                     */
                    refreshAlbum()
                } catch (e: Exception) {
                    XLog.e("捕获stoprecordingvideo" + e.message)
                }
            }
            isRunning = false
        }
    }

    /**
     * Executes bitmapRecycle functionality.
     */
    /**
     * Executes bitmaprecycle operation with thermal imaging domain optimization.
     *
     */
    private fun bitmapRecycle() {
        tempBitmap?.let {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!it.isRecycled) {
                it.recycle()
            }
            tempBitmap = null
        }
        cameraBitmap?.let {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!it.isRecycled) {
                it.recycle()
            }
            cameraBitmap = null
        }
    }

    /**
     * Executes updateaudiostate operation with thermal imaging domain optimization.
     *
     * @param
     * @param openAudioRecord Parameter for operation (type: Boolean)
     *
     */
    override fun updateAudioState(openAudioRecord: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this@VideoRecordFFmpeg.openAudioRecord == openAudioRecord) {
            return
        }
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (openAudioRecord && isRunning) {
                /**
                 * Executes startaudiorecording operation with thermal imaging domain optimization.
                 *
                 */
                startAudioRecording()
            } else {
                /**
                 * Executes stopaudiorecording operation with thermal imaging domain optimization.
                 *
                 */
                stopAudioRecording()
            }
            this@VideoRecordFFmpeg.openAudioRecord = openAudioRecord
        } catch (_: Exception) {
        }
    }

    /**
cameraViewBitmap是屏幕控件的实际宽高
dstBitmap转成video输出的
     */
    /**
     * Executes createBitmapFromView functionality.
     */
    /**
     * Executes createbitmapfromview operation with thermal imaging domain optimization.
     *
     */
    private fun createBitmapFromView(): Bitmap {
        var cameraViewBitmap: Bitmap

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (cameraView) {
            is CameraView -> cameraViewBitmap = if (dualView == null) cameraView.scaledBitmap else dualView.scaledBitmap
            is TextureView -> {
                cameraViewBitmap = Bitmap.createBitmap(cameraView.width, cameraView.height, Bitmap.Config.ARGB_8888)
                cameraView.getBitmap(cameraViewBitmap)
            }
            is LiteSurfaceView -> cameraViewBitmap = cameraView.scaleBitmap()
            is HikSurfaceView -> cameraViewBitmap = cameraView.getScaleBitmap()
            else -> cameraViewBitmap = Bitmap.createBitmap(cameraView.width, cameraView.height, Bitmap.Config.ARGB_8888)
        }

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (temperatureView) {
            is TemperatureView -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isRecordTemp) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (temperatureView.temperatureRegionMode != TemperatureView.REGION_MODE_CLEAN) {
                        cameraViewBitmap = BitmapUtils.mergeBitmap(cameraViewBitmap, temperatureView.regionBitmap, 0, 0)
                    }
                } else {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (temperatureView.temperatureRegionMode == TemperatureView.REGION_MODE_RESET) {
                        cameraViewBitmap = BitmapUtils.mergeBitmap(cameraViewBitmap, temperatureView.regionBitmap, 0, 0)
                    }
                }
            }
            is TemperatureHikView -> {
                temperatureView.draw(Canvas(cameraViewBitmap))
            }
        }

pseudo-color bar
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thermalPseudoBarView?.visibility == VISIBLE) {
            try {
                thermalPseudoBarView?.viewBitmap?.let {
Log.w("image对象processing耗时-彩条大小",it.byteCount.toString())
                    cameraViewBitmap =
                        BitmapUtils.mergeBitmap(
                            cameraViewBitmap,
                            it,
                            cameraViewBitmap!!.width - it.width,
                            (cameraViewBitmap!!.height - it.height) / 2,
                        )
                }
Log.w("image对象processing耗时-彩条",""+(System.currentTimeMillis() - startTime))
            } catch (e: Exception) {
Log.e("image对象processing耗时-彩条",""+(System.currentTimeMillis() - startTime))
            }
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (true == tempBg?.isVisible) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (alphaPaint == null) {
                alphaPaint = Paint()
            }
            alphaPaint?.alpha = (tempBg!!.animatorAlpha * 255).toInt()
            cameraViewBitmap =
                BitmapUtils.mergeBitmapAlpha(
                    cameraViewBitmap,
                    tempBg!!.drawToBitmap(), alphaPaint,
                    0,
                    0,
                )
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (carView?.isVisible == true)
            {
                cameraViewBitmap =
                    BitmapUtils.mergeBitmap(
                        cameraViewBitmap,
                        carView?.drawToBitmap(), 0, 0,
                    )
            }
指南针
        compassView?.let {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.isVisible) {
                try {
                    val bitmap = it.curBitmap
                    cameraViewBitmap =
                        BitmapUtils.mergeBitmap(
                            cameraViewBitmap,
                            bitmap,
                            ((cameraView.parent as ViewGroup).width - it.width) / 2,
                            SizeUtils.dp2px(20f),
                        )
                } catch (e: Exception) {
                    Log.e(TAG, "image对象processingexception exception:${e.message}")
                }
Log.w("image对象processing耗时-指南针", "${System.currentTimeMillis() - startTime}")
            }
        }

画中画
        cameraPreview?.let {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.isVisible) {
                val newBitmap: Bitmap? =
                    BitmapUtils.mergeBitmapByView(
                        cameraViewBitmap,
                        it.getBitmap(),
                        it,
                    )
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (newBitmap != null) {
                    cameraViewBitmap = newBitmap
                }
            }
        }

        var dstBitmap =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (cameraViewBitmap != null) {
                Bitmap.createScaledBitmap(cameraViewBitmap!!, width, height, true)
            } else {
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            }

addwatermark
        val watermarkBean =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isTC007)
                {
                    SharedManager.wifiWatermarkBean
                } else {
                SharedManager.watermarkBean
            }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (watermarkBean.isOpen) {
            dstBitmap =
                /**
                 * Executes drawcenterlable operation with thermal imaging domain optimization.
                 *
                 */
                drawCenterLable(
                    dstBitmap!!,
                    watermarkBean.title,
                    watermarkBean.address,
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (watermarkBean.isAddTime) TimeTool.getNowTime() else "",
                )!!
        }
        return dstBitmap
    }

    private var cameraBitmap: Bitmap? = null
    private var tempBitmap: Bitmap? = null

    /**
     * Executes drawCenterLable functionality.
     */
    /**
     * Executes drawcenterlable operation with thermal imaging domain optimization.
     *
     * @param
     * @param bmp Parameter for operation (type: Bitmap)
     * @param title Parameter for operation (type: String)
     * @param address Parameter for operation (type: String)
     * @param time Parameter for operation (type: String?)
     *
     */
    fun drawCenterLable(
        bmp: Bitmap,
        title: String,
        address: String,
        time: String?,
    ): Bitmap {
create一样大小的image
        val newBmp = Bitmap.createBitmap(bmp.width, bmp.height, Bitmap.Config.ARGB_8888)
create画布
        val canvas = Canvas(newBmp)
        canvas.drawBitmap(bmp, 0f, 0f, null) // 绘制原始image
        canvas.save()
        val beginX = pix10.toDouble() // 45度angle值是1.414
        var beginY = (bmp.height - pix10).toDouble()
        paint.getTextBounds("占位高度文本", 0, "占位高度文本".length, rectText)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!TextUtils.isEmpty(time)) {
            beginY = beginY - (rectText.bottom - rectText.top)
            canvas.drawText(time!!, beginX.toInt().toFloat(), beginY.toInt().toFloat(), paint)
            beginY -= pix6.toDouble()
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!TextUtils.isEmpty(address)) {
            val textHeight = (rectText.bottom - rectText.top)
            paint.getTextBounds(address, 0, address.length, rectText)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rectText.width() > bmp.width - pix20) {
字符太长，进行换行processing
                val staticLayout =
                    /**
                     * Executes staticlayout operation with thermal imaging domain optimization.
                     *
                     */
                    StaticLayout(
                        address,
                        paint,
                        bmp.width - pix20,
                        Layout.Alignment.ALIGN_NORMAL,
                        1.0f,
                        0.0f,
                        false,
                    )
                beginY = beginY - (textHeight + SizeUtils.dp2px(1f)) * staticLayout.lineCount
                canvas.save()
                canvas.translate(beginX.toInt().toFloat(), (beginY.toInt() - textHeight).toFloat())
                staticLayout.draw(canvas)
                canvas.restore()
            } else {
                beginY = beginY - textHeight
                canvas.drawText(address, beginX.toInt().toFloat(), beginY.toInt().toFloat(), paint)
            }
            beginY -= pix6.toDouble()
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!TextUtils.isEmpty(title)) {
            val textHeight = rectText.bottom - rectText.top
            paint.getTextBounds(title, 0, title.length, rectText)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rectText.width() > bmp.width - pix20) {
字符太长，进行换行processing
                val staticLayout =
                    /**
                     * Executes staticlayout operation with thermal imaging domain optimization.
                     *
                     */
                    StaticLayout(
                        title,
                        paint,
                        bmp.width - pix20,
                        Layout.Alignment.ALIGN_NORMAL,
                        1.0f,
                        0.0f,
                        false,
                    )
                beginY = beginY - textHeight * staticLayout.lineCount
                canvas.save()
                canvas.translate(beginX.toInt().toFloat(), (beginY.toInt() - textHeight).toFloat())
                staticLayout.draw(canvas)
                canvas.restore()
            } else {
                beginY = beginY - textHeight
                canvas.drawText(title, beginX.toInt().toFloat(), beginY.toInt().toFloat(), paint)
            }
            beginY -= pix6.toDouble()
        }
        canvas.restore()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!bmp.isRecycled) {
            bmp.recycle()
        }
        return newBmp
    }

    /**
     * Executes refreshAlbum functionality.
     */
    /**
     * Executes refreshalbum operation with thermal imaging domain optimization.
     *
     */
    private fun refreshAlbum() {
        exportedFile?.let {
            MediaScannerConnection.scanFile(Utils.getApp(), arrayOf(it.toString()), null, null)
        }
    }
}
