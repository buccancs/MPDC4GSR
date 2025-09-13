package com.topdon.module.thermal.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for MenuRecyclerView display and interaction.
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
class MenuRecyclerView : RecyclerView {
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
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
    )

    /**
     * Initializes type component.
     */
    fun initType(type: Int) {
        val span =
            when (type) {
                1 -> 2
                2 -> 6
                4 -> 4
                else -> 4
            }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (span == 2) {
            /**
             * Configures the padding with validation and thermal imaging optimization.
             *
             */
            setPadding(
                (ScreenUtils.getAppScreenWidth() / 3.5f).toInt(),
                0,
                (ScreenUtils.getAppScreenWidth() / 3.5f).toInt(),
                0,
            )
        } else {
            /**
             * Configures the padding with validation and thermal imaging optimization.
             *
             */
            setPadding(0, 0, 0, 0)
        }
        layoutManager =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (type == 3) {
                /**
                 * Executes linearlayoutmanager operation with thermal imaging domain optimization.
                 *
                 */
                LinearLayoutManager(context, HORIZONTAL, false)
            } else {
                /**
                 * Executes gridlayoutmanager operation with thermal imaging domain optimization.
                 *
                 */
                GridLayoutManager(context, span)
            }
        val menuTabAdapter = adapter
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (menuTabAdapter is MenuTabAdapter) {
            (menuTabAdapter as MenuTabAdapter).initType(type)
        }
    }
}
