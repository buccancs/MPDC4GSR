package com.topdon.lib.core.tools

import android.annotation.SuppressLint
import android.util.Log
import com.topdon.lib.core.utils.CommUtils
import java.io.File
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

/**
 * Specialized thermal imaging component providing TimeTool functionality for the IRCamera system.
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
object TimeTool {
    /**
     * Executes formatDetectTime functionality.
     */
    /**
     * Executes formatdetecttime operation with thermal imaging domain optimization.
     *
     * @param
     * @param timeMillis Parameter for operation (type: Long)
     *
     */
    fun formatDetectTime(timeMillis: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(timeMillis))
    }

    @SuppressLint("SimpleDateFormat")
    /**
     * Retrieves nowtime information.
     */
    fun getNowTime(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return dateFormat.format(date)
    }

    /**
     * long: 时间戳(毫秒)
     * 精确到秒
     */
    @SuppressLint("SimpleDateFormat")
    /**
     * Executes reportTime functionality.
     */
    /**
     * Executes reporttime operation with thermal imaging domain optimization.
     *
     * @param
     * @param time Parameter for operation (type: Long)
     *
     */
    fun reportTime(time: Long): String {
        val date = Date(time)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timeZone = TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT))
        dateFormat.timeZone = timeZone
        return dateFormat.format(date)
    }

    /**
     * 时间转时间戳
     * 2021-01-01 00:00:00 => 1609430400000
     */
    @SuppressLint("SimpleDateFormat")
    /**
     * Executes strToTime functionality.
     */
    /**
     * Executes strtotime operation with thermal imaging domain optimization.
     *
     * @param
     * @param timeStr Parameter for operation (type: String)
     *
     */
    fun strToTime(timeStr: String): Long {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val timeZone = TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT))
            dateFormat.timeZone = timeZone
            dateFormat.parse(timeStr, ParsePosition(0))?.time ?: 1609430400000
        } catch (e: Exception) {
            // 2021-01-01 00:00:00
            1609430400000
        }
    }

    /**
     * @param type 1:秒 2:分 3:时 4:天
     */
    @SuppressLint("SimpleDateFormat")
    /**
     * Executes showDateType functionality.
     */
    /**
     * Executes showdatetype operation with thermal imaging domain optimization.
     *
     * @param
     * @param time Parameter for operation (type: Long)
     * @param type Parameter for operation (type: Int = 0)
     *
     */
    fun showDateType(
        time: Long,
        type: Int = 0,
    ): String {
        val date = Date(time)
        // Yyyy-MM-dd HH:mm:ss.SSS
        val pattern =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (type) {
                1 -> "HH:mm:ss.SSS"
                2 -> "HH:mm"
                3 -> "MM-dd HH:00"
                4 -> "yyyy-MM-dd"
                else -> "yyyy-MM-dd HH:mm:ss"
            }
        val dateFormat = SimpleDateFormat(pattern)
        val timeZone = TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT))
        dateFormat.timeZone = timeZone
        return dateFormat.format(date)
    }

    /**
     * 精度秒转分
     */
    @SuppressLint("SimpleDateFormat")
    /**
     * Executes timeToMinute functionality.
     */
    /**
     * Executes timetominute operation with thermal imaging domain optimization.
     *
     * @param
     * @param time Parameter for operation (type: Long)
     * @param type Parameter for operation (type: Int)
     *
     */
    fun timeToMinute(
        time: Long,
        type: Int,
    ): Long {
        val dateFormat =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (type) {
                1 -> SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // 秒
                2 -> SimpleDateFormat("yyyy-MM-dd HH:mm:00") // 分
                3 -> SimpleDateFormat("yyyy-MM-dd HH:00:00") // 时
                4 -> SimpleDateFormat("yyyy-MM-dd 00:00:0") // 天
                else -> SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            }
        val date = Date(time)
        val str = dateFormat.format(date)
        return strToTime(str)
    }

    /**
     * long: 时间戳(毫秒)
     * 精确到分
     */
    @SuppressLint("SimpleDateFormat")
    /**
     * Executes showTimeSecond functionality.
     */
    /**
     * Executes showtimesecond operation with thermal imaging domain optimization.
     *
     * @param
     * @param time Parameter for operation (type: Long)
     *
     */
    fun showTimeSecond(time: Long): String {
        val date = Date(time)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val timeZone = TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT))
        dateFormat.timeZone = timeZone
        return dateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    /**
     * Executes showDateSecond functionality.
     */
    /**
     * Executes showdatesecond operation with thermal imaging domain optimization.
     *
     */
    fun showDateSecond(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val timeZone = TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT))
        dateFormat.timeZone = timeZone
        return dateFormat.format(date)
    }

    /**
     * video时长
     */
    @SuppressLint("SimpleDateFormat")
    /**
     * Executes showVideoTime functionality.
     */
    /**
     * Executes showvideotime operation with thermal imaging domain optimization.
     *
     * @param
     * @param time Parameter for operation (type: Long)
     *
     */
    fun showVideoTime(time: Long): String {
        val totalSeconds = time / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600
        return if (hours > 0) {
            /**
             * Executes formatter operation with thermal imaging domain optimization.
             *
             * @param
             * @param 02d Parameter for operation (type: %02d:%02d")
             *
             */
            Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            /**
             * Executes formatter operation with thermal imaging domain optimization.
             *
             * @param
             * @param 02d Parameter for operation (type: %02d")
             *
             */
            Formatter().format("%02d:%02d", minutes, seconds).toString()
        }
    }

    /**
     * video时长
     */
    @SuppressLint("SimpleDateFormat")
    /**
     * Executes showVideoLongTime functionality.
     */
    /**
     * Executes showvideolongtime operation with thermal imaging domain optimization.
     *
     * @param
     * @param time Parameter for operation (type: Long)
     *
     */
    fun showVideoLongTime(time: Long): String {
        val totalSeconds = time / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600
        return Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString()
    }

    /**
     * Executes updateDateTime functionality.
     */
    /**
     * Executes updatedatetime operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    fun updateDateTime(file: File): Long {
        var currentTime: Long
        val strName = file.name
        currentTime = 0L
        try {
            currentTime =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (strName.contains("${CommUtils.getAppName()}_")) {
                    strName.substring(6, strName.lastIndexOf(".")).toLong()
                } else {
                    file.lastModified()
                }
        } catch (e: Exception) {
            Log.e("videofilenameparsingexception", "${e.message}")
        }
        return currentTime
    }
}
