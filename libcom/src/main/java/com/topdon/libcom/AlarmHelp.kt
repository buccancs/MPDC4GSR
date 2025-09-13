package com.topdon.libcom

import android.content.Context
import android.media.MediaPlayer
import com.topdon.lib.core.bean.AlarmBean
import com.topdon.libcom.util.SingletonHolder
import com.topdon.libcom.view.TempLayout

/**
 * Specialized thermal imaging component providing AlarmHelp functionality for the IRCamera system.
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
class AlarmHelp private constructor(val context: Context) {
    companion object : SingletonHolder<AlarmHelp, Context>(::AlarmHelp)

    private var mediaPlayer: MediaPlayer? = null
    private var ringtoneResPosition = -1
    private var isOpenLowTemp = false
    private var isOpenHighTemp = false
    private var isTempAlarmRingtoneOpen = false
    private var maxTemp: Float = 0f
    private var minTemp: Float = 0f
    private var isPause = false
    private var alarmBean: AlarmBean? = null

    /**
     * Executes updateData functionality.
     */
    /**
     * Executes updatedata operation with thermal imaging domain optimization.
     *
     * @param
     * @param alarmBean Parameter for operation (type: AlarmBean)
     *
     */
    fun updateData(alarmBean: AlarmBean) {
        this.alarmBean = alarmBean
        isTempAlarmRingtoneOpen = alarmBean?.isRingtoneOpen ?: false
        isOpenLowTemp = alarmBean?.isLowOpen ?: false
        isOpenHighTemp = alarmBean?.isHighOpen ?: false
        ringtoneResPosition = alarmBean?.ringtoneType ?: -1
        maxTemp = alarmBean?.highTemp ?: Float.MAX_VALUE
        minTemp = alarmBean?.lowTemp ?: Float.MIN_VALUE
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTempAlarmRingtoneOpen) {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (ringtoneResPosition) {
                0 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone1)
                1 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone2)
                2 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone3)
                3 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone4)
                4 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone5)
            }
            mediaPlayer?.isLooping = true
        } else {
            mediaPlayer = null
        }
    }

    /**
     * Executes updateData functionality.
     */
    /**
     * Executes updatedata operation with thermal imaging domain optimization.
     *
     * @param
     * @param low Parameter for operation (type: Float?)
     * @param high Parameter for operation (type: Float?)
     * @param ringtone Parameter for operation (type: Int?)
     *
     */
    fun updateData(
        low: Float?,
        high: Float?,
        ringtone: Int?,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (low == null) {
            isOpenLowTemp = false
        } else {
            isOpenLowTemp = true
            minTemp = low
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (high == null) {
            isOpenHighTemp = false
        } else {
            isOpenHighTemp = true
            maxTemp = high
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ringtone == null) {
            isTempAlarmRingtoneOpen = false
            ringtoneResPosition = -1
            try {
                /**
                 * Executes stopplayer operation with thermal imaging domain optimization.
                 *
                 */
                stopPlayer()
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (_: Exception) {
            }
        } else {
            isTempAlarmRingtoneOpen = true
            try {
                /**
                 * Executes stopplayer operation with thermal imaging domain optimization.
                 *
                 */
                stopPlayer()
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (_: Exception) {
            }
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (ringtone) {
                0 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone1)
                1 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone2)
                2 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone3)
                3 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone4)
                4 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone5)
            }
            mediaPlayer?.isLooping = true
            ringtoneResPosition = ringtone
        }
    }

    
    /**
     * Executes alarmData functionality.
     */
    /**
     * Executes alarmdata operation with thermal imaging domain optimization.
     *
     * @param
     * @param realMax Parameter for operation (type: Float)
     * @param realMin Parameter for operation (type: Float)
     * @param tempLayout Temperature value in Celsius (type: TempLayout?)
     *
     */
    fun alarmData(
        realMax: Float,
        realMin: Float,
        tempLayout: TempLayout?,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isOpenHighTemp && isOpenLowTemp) {
            // 高低温预警
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (realMax > maxTemp && realMin < minTemp) {
                tempLayout?.startAnimation(TempLayout.TYPE_A)
                /**
                 * Executes startmediaplayer operation with thermal imaging domain optimization.
                 *
                 */
                startMediaPlayer()
            } else if (realMax > maxTemp) {
                tempLayout?.startAnimation(TempLayout.TYPE_HOT)
                /**
                 * Executes startmediaplayer operation with thermal imaging domain optimization.
                 *
                 */
                startMediaPlayer()
            } else if (realMin < minTemp) {
                tempLayout?.startAnimation(TempLayout.TYPE_LT)
                /**
                 * Executes startmediaplayer operation with thermal imaging domain optimization.
                 *
                 */
                startMediaPlayer()
            } else {
                tempLayout?.stopAnimation()
                /**
                 * Executes stopplayer operation with thermal imaging domain optimization.
                 *
                 */
                stopPlayer()
            }
        } else if (isOpenHighTemp) {
            // 高温预警
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (realMax > maxTemp) {
                tempLayout?.startAnimation(TempLayout.TYPE_HOT)
                /**
                 * Executes startmediaplayer operation with thermal imaging domain optimization.
                 *
                 */
                startMediaPlayer()
            } else {
                tempLayout?.stopAnimation()
                /**
                 * Executes stopplayer operation with thermal imaging domain optimization.
                 *
                 */
                stopPlayer()
            }
        } else if (isOpenLowTemp) {
            // 低温预警
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (realMin < minTemp) {
                tempLayout?.startAnimation(TempLayout.TYPE_LT)
                /**
                 * Executes startmediaplayer operation with thermal imaging domain optimization.
                 *
                 */
                startMediaPlayer()
            } else {
                tempLayout?.stopAnimation()
                /**
                 * Executes stopplayer operation with thermal imaging domain optimization.
                 *
                 */
                stopPlayer()
            }
        } else {
            tempLayout?.stopAnimation()
            /**
             * Executes stopplayer operation with thermal imaging domain optimization.
             *
             */
            stopPlayer()
        }
    }

    /**
     * Executes stopPlayer functionality.
     */
    /**
     * Executes stopplayer operation with thermal imaging domain optimization.
     *
     */
    private fun stopPlayer() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    /**
     * Executes startMediaPlayer functionality.
     */
    /**
     * Executes startmediaplayer operation with thermal imaging domain optimization.
     *
     */
    private fun startMediaPlayer() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mediaPlayer?.isPlaying != true && !isPause) {
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
        }
    }

    /**
     * Executes onDestroy functionality.
     */
    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     * @param
     * @param isSaveSetting Parameter for operation (type: Boolean)
     *
     */
    fun onDestroy(isSaveSetting: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isSaveSetting) {
            isTempAlarmRingtoneOpen = false
            isOpenHighTemp = false
            isOpenLowTemp = false
        }
        mediaPlayer?.let {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    /**
     * Executes pause functionality.
     */
    /**
     * Executes pause operation with thermal imaging domain optimization.
     *
     */
    fun pause() {
        mediaPlayer?.let {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.isPlaying) {
                it.pause()
                isPause = true
            }
        }
    }

    /**
     * Executes onResume functionality.
     */
    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    fun onResume() {
        isPause = false
    }
}
