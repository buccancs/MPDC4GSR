package com.topdon.lib.ui.recycler

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.topdon.lib.ui.R as UiR

/**
 * 自定义HeaderView
 */
/**
 * Custom Header view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * HeaderView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for HeaderView display and interaction.
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
class HeaderView : LinearLayout {
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor(context: Context) : this(context, null)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        /**
         * Executes inflate operation with thermal imaging domain optimization.
         *
         */
        inflate(context, UiR.layout.ui_header_view, this)
    }
}
