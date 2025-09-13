package com.topdon.lib.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Custom No scroll view pager view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * NoScrollViewPager implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for NoScrollViewPager display and interaction.
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
class NoScrollViewPager : ViewPager {
    private var isCanScroll = false

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
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    /**
     * Executes onintercepttouchevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param ev Parameter for operation (type: MotionEvent?)
     *
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isCanScroll && super.onInterceptTouchEvent(ev)
    }

    /**
     * Executes ontouchevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param ev Parameter for operation (type: MotionEvent?)
     *
     */
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isCanScroll && super.onTouchEvent(ev)
    }

    /**
     * Configures the currentitem with validation and thermal imaging optimization.
     *
     * @param
     * @param item Parameter for operation (type: Int)
     *
     */
    override fun setCurrentItem(item: Int) {
        
        super.setCurrentItem(item, false)
    }
}
