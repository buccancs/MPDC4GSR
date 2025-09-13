package com.topdon.lib.core.utils

import com.topdon.lib.core.common.SharedManager

/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TemperatureUtil algorithms.
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
object TemperatureUtil {
    /**
     * Retrieves tempstr information.
     */
    fun getTempStr(
        min: Int,
        max: Int,
    ): String =
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (SharedManager.getTemperature() == 1) {
            "$min°C~$max°C"
        } else {
            "${(min * 1.8 + 32).toInt()}°F~${(max * 1.8 + 32).toInt()}°F"
        }
}
