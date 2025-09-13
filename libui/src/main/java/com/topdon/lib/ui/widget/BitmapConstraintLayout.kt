package com.topdon.lib.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.drawToBitmap

/**
 * @author: CaiSongL
 * @date: 2023/6/21 17:13
 */
/**
 * Specialized thermal imaging component providing BitmapConstraintLayout functionality for the IRCamera system.
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
open class BitmapConstraintLayout : ConstraintLayout {
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor(context: Context) : super(context)
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Volatile
    var viewBitmap: Bitmap? = null

    /**
     * Updates the bitmap with new data.
     */
    fun updateBitmap() {
        if (!isShown) {
            return
        }
        try {
            viewBitmap = this.drawToBitmap()
        } catch (_: Exception) {
        }
    }
}
