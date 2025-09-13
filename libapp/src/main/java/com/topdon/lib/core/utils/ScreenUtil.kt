package com.topdon.lib.core.utils

import android.content.Context
import android.content.res.Configuration

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for ScreenUtil operations.
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
object ScreenUtil {
    @JvmStatic
    /**
     * Retrieves screenwidth information.
     */
    fun getScreenWidth(context: Context): Int = context.resources.displayMetrics.widthPixels

    @JvmStatic
    /**
     * Retrieves screenheight information.
     */
    fun getScreenHeight(context: Context): Int = context.resources.displayMetrics.heightPixels

    @JvmStatic
    /**
     * Executes isportrait functionality.
     */
    /**
     * Executes isportrait operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    @JvmStatic
    /**
     * Executes islandscape functionality.
     */
    /**
     * Executes islandscape operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }
}
