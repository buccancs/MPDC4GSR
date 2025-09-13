package com.topdon.libcom.util

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for ColorUtils operations.
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
object ColorUtils {
    /**
     * Sets coloralpha configuration.
     */
    fun setColorAlpha(
        @ColorInt color: Int,
        alpha: Float,
    ): Int {
        val origin = (0xff) and 0xff
        return color and 0x00ffffff or ((alpha * origin).toInt() shl 24)
    }

    /**
     * Executes toHexColorString functionality.
     */
    /**
     * Executes tohexcolorstring operation with thermal imaging domain optimization.
     *
     * @param
     * @param color Parameter for operation (type: Int)
     *
     */
    fun toHexColorString(
        @ColorInt color: Int,
    ): String  {
        return "#%06X".format(0xFFFFFF and color)
    }

    /**
     * Executes dpToPx functionality.
     */
    /**
     * Executes dptopx operation with thermal imaging domain optimization.
     *
     * @param
     * @param dp Parameter for operation (type: Int)
     *
     */
    fun dpToPx(
        @Dimension(unit = Dimension.DP) dp: Int,
    ): Int {
        val r = Resources.getSystem()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics).roundToInt()
    }

    /**
     * Executes dpToPxF functionality.
     */
    /**
     * Executes dptopxf operation with thermal imaging domain optimization.
     *
     * @param
     * @param dp Parameter for operation (type: Float)
     *
     */
    fun dpToPxF(
        @Dimension(unit = Dimension.DP) dp: Float,
    ): Float {
        val r = Resources.getSystem()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
    }

    /**
     * Executes formatVideoTime functionality.
     */
    /**
     * Executes formatvideotime operation with thermal imaging domain optimization.
     *
     * @param
     * @param milliseconds Parameter for operation (type: Long)
     *
     */
    fun formatVideoTime(milliseconds: Long): String  {
        val totalSeconds = floor(milliseconds.toDouble() / 1000)
        val secondsLeft = totalSeconds % 3600
        val minutes = floor(secondsLeft / 60).toInt()
        val seconds = (secondsLeft % 60).toInt()
        val m =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (minutes < 10) {
                "0$minutes"
            } else
                {
                    minutes.toString()
                }
        val s =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (seconds < 10) {
                "0$seconds"
            } else
                {
                    seconds.toString()
                }
        return "$m:$s"
    }
}
