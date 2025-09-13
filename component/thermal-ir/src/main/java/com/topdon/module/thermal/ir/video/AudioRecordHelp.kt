package com.topdon.module.thermal.ir.video

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.elvishew.xlog.XLog
import org.bytedeco.javacv.FFmpegFrameRecorder
import java.lang.ref.WeakReference
import java.nio.ShortBuffer

/**
音频采集并且与videoMerge一起
 * @author: CaiSongL
 * @date: 2023/3/28
/**
 * Specialized thermal imaging component providing AudioRecordHelp functionality for the IRCamera system.
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
class AudioRecordHelp private constructor() {
    private var audioRecord: AudioRecord? = null
    private var audioRecordRunnable: AudioRecordRunnable? = null
    private var audioThread: Thread? = null

    @Volatile
    private var recordingAudio = false
    private var startTime: Long = 0

    @Volatile
    var runAudioThread = true
    var audioData: ShortBuffer? = null
    var bufferReadResult: Int = 0
/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for AudioUtilHolder operations.
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
object AudioUtilHolder {
        val INSTANCE = AudioRecordHelp()
    }

    @SuppressLint("MissingPermission")
    /**
     * Executes startRecording functionality.
     */
    /**
     * Executes startrecording operation with thermal imaging domain optimization.
     *
     * @param
     * @param recorder Parameter for operation (type: FFmpegFrameRecorder)
     * @param startRecordTime Parameter for operation (type: Long)
     *
     */
    fun startRecording(
        recorder: FFmpegFrameRecorder,
        startRecordTime: Long,
    ) {
        this.startRecordTime = startRecordTime
        type = 1
        /**
         * Initializes the recorder component for thermal imaging operations.
         *
         */
        initRecorder(recorder)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (audioRecord == null)
            {
                audioRecord =
                    /**
                     * Executes audiorecord operation with thermal imaging domain optimization.
                     *
                     */
                    AudioRecord(
                        MediaRecorder.AudioSource.MIC, VideoRecordFFmpeg.SAMPLE_AUDIO_RETE_INHZ,
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                    )
            }
        try {
            startTime = System.currentTimeMillis()
            audioThread!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Initializes recorder component.
     */
    private fun initRecorder(recorder: FFmpegFrameRecorder) {
        audioRecordRunnable = AudioRecordRunnable(recorder)
        audioThread = Thread(audioRecordRunnable)
// AudioThread?.priority = THREAD_PRIORITY_URGENT_AUDIO
        runAudioThread = true
    }

    internal inner class AudioRecordRunnable(recorder: FFmpegFrameRecorder) : Runnable {
        private val recorder: WeakReference<FFmpegFrameRecorder> = WeakReference(recorder)

        @SuppressLint("MissingPermission")
        /**
         * Executes run operation with thermal imaging domain optimization.
         *
         */
        override fun run() {
//            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (audioRecord == null)
                {
                    return
                }
            // Audio
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (audioData == null)
                {
                    audioData = ShortBuffer.allocate(bufferSize)
                }
            audioRecord!!.startRecording()
            /**
音频进行循环encoding
             */
            try {
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (runAudioThread) {
                    bufferReadResult = audioRecord!!.read(audioData!!.array(), 0, audioData!!.capacity())
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (recordingAudio) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (bufferReadResult > 0) {
                            audioData?.limit(bufferReadResult)
                            Log.w("音频采集", bufferReadResult.toString() + "// " + bufferReadResult)
                            recorder?.get()?.recordSamples(
                                VideoRecordFFmpeg.SAMPLE_AUDIO_RETE_INHZ,
                                VideoRecordFFmpeg.AUDIO_CHANNELS,
                                audioData,
                            )
Log.w("音频采集中2",""+recorder?.get()?.frameNumber)
                        }
                    } else
                        {
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             */
                            for (i in 0 until bufferSize) {
                                audioData!!.put(i, 0)
                            }
                            recorder?.get()?.recordSamples(
                                VideoRecordFFmpeg.SAMPLE_AUDIO_RETE_INHZ,
                                VideoRecordFFmpeg.AUDIO_CHANNELS,
                                audioData,
                            )
                            Thread.sleep(1000L / VideoRecordFFmpeg.RATE)
                        }
                }
Log.w("stop采集",""+recorder?.get()?.frameNumber)
            } catch (e: Exception) {
                XLog.e("采集容器exception")
            }
        }
    }

    /**
     * Executes updateAudioRecordingState functionality.
     */
    /**
     * Executes updateaudiorecordingstate operation with thermal imaging domain optimization.
     *
     * @param
     * @param boolean Parameter for operation (type: Boolean)
     *
     */
    public fun updateAudioRecordingState(boolean: Boolean)  {
        recordingAudio = boolean
    }

    /**
     * Executes stopAudioRecording functionality.
     */
    /**
     * Executes stopaudiorecording operation with thermal imaging domain optimization.
     *
     */
    fun stopAudioRecording() {
        type = 2
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!runAudioThread)
            {
                return
            }
        runAudioThread = false
        try {
            audioThread?.interrupt()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
        audioRecordRunnable = null
        audioThread = null
        recordingAudio = false
    }

    /**
     * Executes stopRecording functionality.
     */
    /**
     * Executes stoprecording operation with thermal imaging domain optimization.
     *
     */
    fun stopRecording()  {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!runAudioThread)
            {
                return
            }
    }

    companion object {
        private val LOG_TAG = AudioRecordHelp::class.java.name

    /**
     * Retrieves instance information.
     */
        fun getInstance(): AudioRecordHelp {
            return AudioUtilHolder.INSTANCE
        }
    }
}
