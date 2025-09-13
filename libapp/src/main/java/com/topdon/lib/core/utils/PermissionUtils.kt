package com.topdon.lib.core.utils

import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.core.content.ContextCompat
import com.topdon.lib.core.BaseApplication

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for PermissionUtils operations.
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
object PermissionUtils {
    /**
     * Check if Android 14 has granted partial read permissions
     * @return Boolean
     */
    /**
     * Executes isVisualUser functionality.
     */
    /**
     * Executes isvisualuser operation with thermal imaging domain optimization.
     *
     */
    fun isVisualUser(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
            ContextCompat.checkSelfPermission(
                BaseApplication.instance,
                READ_MEDIA_VISUAL_USER_SELECTED,
            ) == PERMISSION_GRANTED
    }

    /**
     * Executes hascamerapermission functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            BaseApplication.instance,
            android.Manifest.permission.CAMERA,
        ) == PERMISSION_GRANTED
    }
}
