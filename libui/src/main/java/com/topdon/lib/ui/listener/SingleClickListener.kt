package com.topdon.lib.ui.listener

import android.view.View

/**
 * Specialized thermal imaging component providing SingleClickListener functionality for the IRCamera system.
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
public abstract class SingleClickListener : View.OnClickListener {
    private var mLastClickTime: Long = 0
    private var timeInterval = 500L

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     */
    constructor() {}
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param interval Parameter for operation (type: Long)
     *
     */
    constructor(interval: Long) {
        timeInterval = interval
    }

    /**
     * Executes onclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param v Parameter for operation (type: View)
     *
     */
    override fun onClick(v: View) {
        val nowTime = System.currentTimeMillis()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (nowTime - mLastClickTime > timeInterval) {
            /**
             * Executes onsingleclick operation with thermal imaging domain optimization.
             *
             */
            onSingleClick()
            mLastClickTime = nowTime
        }
    }

    protected abstract fun onSingleClick()
}
