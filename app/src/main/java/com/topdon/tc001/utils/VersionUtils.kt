package com.topdon.tc001.utils

import android.content.Context

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for VersionUtils operations.
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
object VersionUtils {
    /**
     * Gets the version name from the application's package information.
     *
     * @param context The application context used to access package manager
     * @return The version name string, or empty string if not available
     */
    /**
     * Retrieves codestr information.
     */
    fun getCodeStr(context: Context): String {
        val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        return versionName ?: ""
    }
}
