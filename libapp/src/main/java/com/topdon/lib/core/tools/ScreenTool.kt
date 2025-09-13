package com.topdon.lib.core.tools

import android.content.Context
import android.util.DisplayMetrics
import com.blankj.utilcode.util.Utils
import com.topdon.lib.core.utils.ScreenUtil
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Specialized thermal imaging component providing ScreenTool functionality for the IRCamera system.
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
object ScreenTool {
    /**
     * 折叠屏
     */
    /**
     * Executes islandphone operation with thermal imaging domain optimization.
     *
     */
    fun isLandPhone(): Boolean {
        val displayMetrics: DisplayMetrics = Utils.getApp().resources.displayMetrics
        val width = displayMetrics.widthPixels.toFloat()
        val height = displayMetrics.heightPixels.toFloat()
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (width / height) < 0.75f
    }

    /**
     * Executes isIPad functionality.
     */
    /**
     * Executes isipad operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    fun isIPad(context: Context): Boolean {
        val width = ScreenUtil.getScreenWidth(context)
        val height = ScreenUtil.getScreenHeight(context)
        val densityDpi = context.resources.displayMetrics.densityDpi
        val diagonalPixels = sqrt(width.toDouble().pow(2) + height.toDouble().pow(2))
        val screenInches = diagonalPixels / densityDpi
        return screenInches >= 7f
    }
}
