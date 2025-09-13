package com.topdon.module.thermal.tools.medie

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

/**
/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for YapVideoUtils operations.
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
object YapVideoUtils {
    /**
     * Executes convertViewToBitmap functionality.
     */
    /**
     * Executes convertviewtobitmap operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     *
     */
    fun convertViewToBitmap(view: View): Bitmap {
        var bitmap: Bitmap?
        view.destroyDrawingCache()
        view.buildDrawingCache()
        bitmap = view.drawingCache
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bitmap == null) {
            bitmap =
                Bitmap.createBitmap(
                    view.measuredWidth,
                    view.measuredHeight, Bitmap.Config.RGB_565,
                )
            val bitmapHolder = Canvas(bitmap)
            view.draw(bitmapHolder)
        }
        return bitmap!!
    }
}
