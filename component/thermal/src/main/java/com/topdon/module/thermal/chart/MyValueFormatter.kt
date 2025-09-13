package com.topdon.module.thermal.chart

import android.annotation.SuppressLint
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Specialized thermal imaging component providing MyValueFormatter functionality for the IRCamera system.
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
class MyValueFormatter(private val startTime: Long, private val type: Int = 1) :
    /**
     * Executes indexaxisvalueformatter operation with thermal imaging domain optimization.
     *
     */
    IndexAxisValueFormatter() {
    companion object {
        const val TYPE_TIME_SECOND = 1
        const val TYPE_TIME_MINUTE = 2
        const val TYPE_TIME_HOUR = 3
        const val TYPE_TIME_DAY = 4
    }

    @Suppress("OVERRIDE_DEPRECATION")
    /**
     * Retrieves the formattedvalue with optimized performance for thermal imaging operations.
     *
     * @param
     * @param value Parameter for operation (type: Float)
     * @param axis Parameter for operation (type: AxisBase?)
     *
     */
    override fun getFormattedValue(
        value: Float,
        axis: AxisBase?,
    ): String {
        val time = startTime + value.toLong()
        return showDateSecond(time)
    }

    @SuppressLint("SimpleDateFormat")
    /**
     * Executes showDateSecond functionality.
     */
    /**
     * Executes showdatesecond operation with thermal imaging domain optimization.
     *
     * @param
     * @param time Parameter for operation (type: Long)
     *
     */
    fun showDateSecond(time: Long): String {
        val date = Date(time)
        // Yyyy-MM-dd HH:mm:ss
        val pattern =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (type) {
                TYPE_TIME_SECOND -> "HH:mm:ss"
                TYPE_TIME_MINUTE -> "HH:mm"
                TYPE_TIME_HOUR -> "HH:00"
                TYPE_TIME_DAY -> "yy-MM-dd"
                else -> "HH:mm:ss"
            }
        val dateFormat = SimpleDateFormat(pattern)
        val timeZone = TimeZone.getDefault()
        dateFormat.timeZone = timeZone
        return dateFormat.format(date)
    }
}
