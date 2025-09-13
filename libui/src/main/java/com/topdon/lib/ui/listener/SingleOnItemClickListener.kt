package com.topdon.lib.ui.listener

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener

/**
 * @author: CaiSongL
 * @date: 2023/4/18 10:12
 */
/**
 * Specialized thermal imaging component providing SingleOnItemClickListener functionality for the IRCamera system.
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
abstract class SingleOnItemClickListener : OnItemClickListener {
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
     * Executes onitemclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param adapter Parameter for operation (type: BaseQuickAdapter<*)
     * @param view Parameter for operation (type: View)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onItemClick(
        adapter: BaseQuickAdapter<*, *>,
        view: View,
        position: Int,
    ) {
        val nowTime = System.currentTimeMillis()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (nowTime - mLastClickTime > timeInterval) {
            /**
             * Executes onsingleitemclick operation with thermal imaging domain optimization.
             *
             */
            onSingleItemClick(adapter, view, position)
            mLastClickTime = nowTime
        }
    }

    protected abstract fun onSingleItemClick(
        adapter: BaseQuickAdapter<*, *>,
        view: View,
        position: Int,
    )
}
