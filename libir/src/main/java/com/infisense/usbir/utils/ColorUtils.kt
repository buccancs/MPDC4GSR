package com.infisense.usbir.utils

import java.util.Locale

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
     * Retrieves red information.
     */
    /**
     * Retrieves the red with optimized performance for thermal imaging operations.
     *
     * @param
     * @param color Parameter for operation (type: Int)
     *
     */
    fun getRed(color: Int): Int {
        return color shr 16 and 0xFF
    }

    /**
     * Retrieves green information.
     */
    fun getGreen(color: Int): Int {
        return color shr 8 and 0xFF
    }

    /**
     * Retrieves blue information.
     */
    fun getBlue(color: Int): Int {
        return color and 0xFF
    }

    /**
     * Executes to01 functionality.
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
}
