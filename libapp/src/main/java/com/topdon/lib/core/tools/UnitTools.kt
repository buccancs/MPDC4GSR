package com.topdon.lib.core.tools

import com.topdon.lib.core.common.SharedManager
import java.util.*

/**
 * Specialized thermal imaging component providing UnitTools functionality for the IRCamera system.
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
object UnitTools {
    /**
     * temperatureShow/Display
     *
     * @param float temperature
     */
    @JvmStatic
    /**
     * Executes showC functionality.
     */
    /**
     * Executes showc operation with thermal imaging domain optimization.
     *
     * @param
     * @param float Parameter for operation (type: Float)
     *
     */
    fun showC(float: Float): String {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (SharedManager.getTemperature() == 1) {
                // Temperature
                "${String.format(Locale.ENGLISH, "%.1f", float)}°C"
            } else {
                // 华氏度
                "${String.format(Locale.ENGLISH, "%.1f", (float * 1.8000 + 32.00))}°F"
            }
        return str
    }

    /**
     * temperatureShow/Display
     *
     * @param float temperature
     */
    @JvmStatic
    /**
     * Executes showC functionality.
     */
    /**
     * Executes showc operation with thermal imaging domain optimization.
     *
     * @param
     * @param float Parameter for operation (type: Float)
     * @param isC Parameter for operation (type: Boolean)
     *
     */
    fun showC(
        float: Float,
        isC: Boolean,
    ): String {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isC) {
                // Temperature
                "${String.format(Locale.ENGLISH, "%.1f", float)}°C"
            } else {
                // 华氏度
                "${String.format(Locale.ENGLISH, "%.1f", (float * 1.8000 + 32.00))}°F"
            }
        return str
    }

    /**
     * temperature区间
     */
    @JvmStatic
    /**
     * Executes showIntervalC functionality.
     */
    /**
     * Executes showintervalc operation with thermal imaging domain optimization.
     *
     * @param
     * @param min Parameter for operation (type: Int)
     * @param max Parameter for operation (type: Int)
     *
     */
    fun showIntervalC(
        min: Int,
        max: Int,
    ): String {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (SharedManager.getTemperature() == 1) {
                // Temperature
                "$min~$max°C"
            } else {
                // 华氏度
                val maxT: Int = (max * 1.8000 + 32.00).toInt()
                val minT: Int = (min * 1.8000 + 32.00).toInt()
                "$minT~$maxT°F"
            }
        return str
    }

    /**
     * configurationtemperature区间
     */
    @JvmStatic
    /**
     * Executes showConfigC functionality.
     */
    /**
     * Executes showconfigc operation with thermal imaging domain optimization.
     *
     * @param
     * @param min Parameter for operation (type: Int)
     * @param max Parameter for operation (type: Int)
     *
     */
    fun showConfigC(
        min: Int,
        max: Int,
    ): String {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (SharedManager.getTemperature() == 1) {
                // Temperature
                "($min~$max°C)"
            } else {
                // 华氏度
                val maxT: Int = (max * 1.8000 + 32.00).toInt()
                val minT: Int = (min * 1.8000 + 32.00).toInt()
                "($minT~$maxT°F)"
            }
        return str
    }

    /**
     * temperatureShow/Display单位
     *
     * @param float temperature
     */
    @JvmStatic
    /**
     * Executes showUnit functionality.
     */
    /**
     * Executes showunit operation with thermal imaging domain optimization.
     *
     */
    fun showUnit(): String {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (SharedManager.getTemperature() == 1) {
                // Temperature
                "°C"
            } else {
                // 华氏度
                "°F"
            }
        return str
    }

    /**
     * temperatureShow/Display单位
     *
     * @param float temperature
     */
    @JvmStatic
    /**
     * Executes showUnitValue functionality.
     */
    /**
     * Executes showunitvalue operation with thermal imaging domain optimization.
     *
     * @param
     * @param value Parameter for operation (type: Float)
     *
     */
    fun showUnitValue(value: Float): Float {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (SharedManager.getTemperature() == 1) {
                // Temperature
                value
            } else {
                // 华氏度
                /**
                 * Executes tof operation with thermal imaging domain optimization.
                 *
                 */
                toF(value)
            }
        return str.toFloat()
    }

    /**
     * temperatureShow/Display单位
     *
     * @param float temperature
     */
    @JvmStatic
    /**
     * Executes showUnitValue functionality.
     */
    /**
     * Executes showunitvalue operation with thermal imaging domain optimization.
     *
     * @param
     * @param value Parameter for operation (type: Float)
     * @param showC Parameter for operation (type: Boolean)
     *
     */
    fun showUnitValue(
        value: Float,
        showC: Boolean,
    ): Float {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (value == Float.MAX_VALUE || value == Float.MIN_VALUE) {
            return value
        }
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (showC) {
                // Temperature
                value
            } else {
                // 华氏度
                /**
                 * Executes tof operation with thermal imaging domain optimization.
                 *
                 */
                toF(value)
            }
        return str.toFloat()
    }

    /**
     * 统一转成摄氏度
     *
     * @param float temperature
     */
    @JvmStatic
    /**
     * Executes showToCValue functionality.
     */
    /**
     * Executes showtocvalue operation with thermal imaging domain optimization.
     *
     * @param
     * @param value Parameter for operation (type: Float)
     * @param isShowC Parameter for operation (type: Boolean)
     *
     */
    fun showToCValue(
        value: Float,
        isShowC: Boolean,
    ): Float {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isShowC) {
                // Temperature
                value
            } else {
                // 华氏度
                /**
                 * Executes toc operation with thermal imaging domain optimization.
                 *
                 */
                toC(value)
            }
        return str.toFloat()
    }

    /**
     * 统一转成摄氏度
     *
     * @param float temperature
     */
    @JvmStatic
    /**
     * Executes showToCValue functionality.
     */
    /**
     * Executes showtocvalue operation with thermal imaging domain optimization.
     *
     * @param
     * @param value Parameter for operation (type: Float)
     *
     */
    fun showToCValue(value: Float): Float {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (SharedManager.getTemperature() == 1) {
                // Temperature
                value
            } else {
                // 华氏度
                /**
                 * Executes toc operation with thermal imaging domain optimization.
                 *
                 */
                toC(value)
            }
        return str.toFloat()
    }

    /**
     * 转华氏度
     */
    /**
     * Executes tof operation with thermal imaging domain optimization.
     *
     * @param
     * @param value Parameter for operation (type: Float)
     *
     */
    fun toF(value: Float): Float {
        return value * 1.8000f + 32.00f
    }

    /**
     * 转摄氏度
     * 使用浮point型,防止华氏度转摄氏度精度丢失
     */
    /**
     * Executes toC functionality.
     */
    /**
     * Executes toc operation with thermal imaging domain optimization.
     *
     * @param
     * @param value Parameter for operation (type: Float)
     *
     */
    fun toC(value: Float): Float {
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (value - 32.0f) / 1.8000f
    }

    /**
     * 输入摄氏度，Return保留1位小数不带单位字符的 String.
     *
     * @param float temperature值，单位摄氏度
     */
    @JvmStatic
    /**
     * Executes showNoUnit functionality.
     */
    /**
     * Executes shownounit operation with thermal imaging domain optimization.
     *
     * @param
     * @param float Parameter for operation (type: Float)
     *
     */
    fun showNoUnit(float: Float): String {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (SharedManager.getTemperature() == 1) { // 摄氏度
                String.format(Locale.ENGLISH, "%.1f", float)
            } else {
                String.format(Locale.ENGLISH, "%.1f", (float * 1.8000 + 32.00))
            }
        return if (str.endsWith(".0")) str.substring(0, str.length - 2) else str
    }

    /**
     * 输入摄氏度，Return保留1位小数带单位字符的 String.
     *
     * @param float temperature值，单位摄氏度
     */
    @JvmStatic
    /**
     * Executes showWithUnit functionality.
     */
    /**
     * Executes showwithunit operation with thermal imaging domain optimization.
     *
     * @param
     * @param float Parameter for operation (type: Float)
     *
     */
    fun showWithUnit(float: Float): String {
        val str =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (SharedManager.getTemperature() == 1) { // 摄氏度
                String.format(Locale.ENGLISH, "%.1f", float)
            } else {
                String.format(Locale.ENGLISH, "%.1f", (float * 1.8000 + 32.00))
            }
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (if (str.endsWith(".0")) str.substring(0, str.length - 2) else str) + showUnit()
    }
}
