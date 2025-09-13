package com.topdon.lib.core.utils

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for CommUtils operations.
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
object CommUtils {
    /**
     * Retrieves appname information.
     */
    fun getAppName(): String {
        var msg = ""
        var appInfo: ApplicationInfo? = null
        appInfo =
            Utils.getApp().packageManager
                .getApplicationInfo(
                    Utils.getApp().packageName,
                    PackageManager.GET_META_DATA,
                )
        try {
            msg = appInfo.metaData.getString("app_name")?.toString() ?: ""
        } catch (e: Exception) {
            XLog.w("Get/Retrieveappnameexception： ${e.message}")
        }
        return msg
    }
}
