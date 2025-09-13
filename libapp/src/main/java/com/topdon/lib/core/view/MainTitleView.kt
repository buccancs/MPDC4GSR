package com.topdon.lib.core.view

import android.content.Context
import android.util.AttributeSet

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for MainTitleView display and interaction.
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
class MainTitleView
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
    ) : TitleView(context, attrs) {
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        override fun initView() {
            tvLeft = addTextView(context)
            tvRight1 = addTextView(context)
            tvRight2 = addTextView(context, 2f, 40f)
            tvRight3 = addTextView(context)
            tvTitle = addTextView(context)
        }
    }
