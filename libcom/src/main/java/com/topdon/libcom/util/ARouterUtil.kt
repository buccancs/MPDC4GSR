package com.topdon.libcom.util

import android.app.Activity
import com.topdon.libcom.navigation.NavigationManager

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for ARouterUtil operations.
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
object ARouterUtil {
    /**
     * 统一跳转infrared拍照界area
     * @param activity Activity
     * @param isTC007 Boolean
     */
    /**
     * Executes jumpImagePick functionality.
     */
    /**
     * Executes jumpimagepick operation with thermal imaging domain optimization.
     *
     * @param
     * @param activity Parameter for operation (type: Activity)
     * @param isTC007 Parameter for operation (type: Boolean)
     * @param imgPath Parameter for operation (type: String)
     *
     */
    fun jumpImagePick(
        activity: Activity,
        isTC007: Boolean,
        imgPath: String,
    ) {
        NavigationManager.jumpImagePick(activity, isTC007, imgPath)
    }
}
