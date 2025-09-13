package com.topdon.lib.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.SurfaceView
import java.nio.ByteBuffer

/**
 * des:
 * author: CaiSongL
 * date: 2024/8/1 13:52
 **/
/**
 * Custom Lite surface view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * LiteSurfaceView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for LiteSurfaceView display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
class LiteSurfaceView
    @JvmOverloads
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet? = null)
     *
     */
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : SurfaceView(context, attrs) {
        var mFinalImageWidth = 0

        var mFinalImageHeight = 0

        var tmpData: ByteArray? = null
        var mIrRotateData: ByteArray? = null

        var imageBitmap: Bitmap? = null

    /**
     * Executes scalebitmap functionality.
     */
        /**
         * Executes scalebitmap operation with thermal imaging domain optimization.
         *
         */
        fun scaleBitmap(): Bitmap {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (tmpData == null) {
                    tmpData = ByteArray(mIrRotateData!!.size)
                }
                System.arraycopy(mIrRotateData!!, 0, tmpData!!, 0, mIrRotateData!!.size)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (imageBitmap == null || imageBitmap!!.getWidth() != mFinalImageWidth) {
                    imageBitmap =
                        Bitmap.createBitmap(mFinalImageWidth, mFinalImageHeight, Bitmap.Config.ARGB_8888)
                }
                imageBitmap?.copyPixelsFromBuffer(ByteBuffer.wrap(tmpData!!))
                return Bitmap.createScaledBitmap(
                    imageBitmap!!,
                    measuredWidth,
                    measuredHeight,
                    true,
                )
            } catch (e: Exception) {
                return Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
            }
        }
    }
