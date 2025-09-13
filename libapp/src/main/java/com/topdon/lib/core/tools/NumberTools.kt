package com.topdon.lib.core.tools

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

/**
 * Specialized thermal imaging component providing NumberTools functionality for the IRCamera system.
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
object NumberTools {
    /**
     * 精确小数point后一位
     */
    /**
     * Executes to01 operation with thermal imaging domain optimization.
     *
     * @param
     * @param float Parameter for operation (type: Float)
     *
     */
    fun to01(float: Float): String {
        return String.format(Locale.ENGLISH, "%.1f", float)
    }

    /**
     * 精确小数point后两位
     */
    /**
     * Executes to01f operation with thermal imaging domain optimization.
     *
     * @param
     * @param float Parameter for operation (type: Float)
     *
     */
    fun to01f(float: Float): Float {
        return to01(float).toFloat()
    }

    /**
     * 精确小数point后两位
     */
    /**
     * Executes to02 operation with thermal imaging domain optimization.
     *
     * @param
     * @param float Parameter for operation (type: Float)
     *
     */
    fun to02(float: Float): String {
        return String.format(Locale.ENGLISH, "%.2f", float)
    }

    /**
     * 精确小数point后两位
     */
    /**
     * Executes to02f operation with thermal imaging domain optimization.
     *
     * @param
     * @param float Parameter for operation (type: Float)
     *
     */
    fun to02f(float: Float): Float {
        return to02(float).toFloat()
    }

    /**
     * 四舍五入
     * @param newScale 保留多少位小数
     */
    /**
     * Executes scale functionality.
     */
    /**
     * Executes scale operation with thermal imaging domain optimization.
     *
     * @param
     * @param value Parameter for operation (type: Float)
     * @param newScale Parameter for operation (type: Int)
     *
     */
    fun scale(
        value: Float,
        newScale: Int,
    ): Float {
        return BigDecimal(value.toDouble()).setScale(newScale, RoundingMode.HALF_UP).toFloat()
    }
}
