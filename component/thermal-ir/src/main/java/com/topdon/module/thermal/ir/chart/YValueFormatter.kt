package com.topdon.module.thermal.ir.chart

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.topdon.lib.core.tools.UnitTools

/**
Yaxis文本format
 */
/**
/**
 * Specialized thermal imaging component providing YValueFormatter functionality for the IRCamera system.
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
class YValueFormatter : IndexAxisValueFormatter() {
    /**
     * Retrieves the formattedvalue with optimized performance for thermal imaging operations.
     *
     * @param
     * @param value Parameter for operation (type: Float)
     *
     */
    override fun getFormattedValue(value: Float): String {
        return try {
            String.format("%.1f", value) // 检测value是不是数字
            UnitTools.showC(value)
        } catch (e: Exception) {
            UnitTools.showC(value)
        }
    }
}
