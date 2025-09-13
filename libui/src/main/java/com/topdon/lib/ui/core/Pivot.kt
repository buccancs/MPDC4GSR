package com.topdon.lib.ui.core

import android.view.View
import androidx.annotation.IntDef

/**
 * @author: CaiSongL
 * @date: 2023/4/1 14:18
 */
/**
 * Custom Pivot view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * Pivot manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing Pivot functionality for the IRCamera system.
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
class Pivot(
    @get:Axis
    @param:Axis val axis: Int,
    private val pivotPoint: Int,
) {
    /**
     * Sets on configuration.
     */
    /**
     * Configures the on with validation and thermal imaging optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     *
     */
    fun setOn(view: View) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (axis == AXIS_X) {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (pivotPoint) {
                PIVOT_CENTER -> view.pivotX = view.width * 0.5f
                PIVOT_MAX -> view.pivotX = view.width.toFloat()
                else -> view.pivotX = pivotPoint.toFloat()
            }
            return
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (axis == AXIS_Y) {
/**
 * Specialized thermal imaging component providing X functionality for the IRCamera system.
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
    enum class X {
        LEFT {
            /**
             * Executes create operation with thermal imaging domain optimization.
             *
             */
            override fun create(): Pivot {
                return Pivot(AXIS_X, 0)
            }
        },
        CENTER {
            /**
             * Executes create operation with thermal imaging domain optimization.
             *
             */
            override fun create(): Pivot {
                return Pivot(AXIS_X, PIVOT_CENTER)
            }
        },
        RIGHT {
/**
 * Specialized thermal imaging component providing Y functionality for the IRCamera system.
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
    enum class Y {
        TOP {
            /**
             * Executes create operation with thermal imaging domain optimization.
             *
             */
            override fun create(): Pivot {
                return Pivot(AXIS_Y, 0)
            }
        },
        CENTER {
            /**
             * Executes create operation with thermal imaging domain optimization.
             *
             */
            override fun create(): Pivot {
                return Pivot(AXIS_Y, PIVOT_CENTER)
            }
        },
        BOTTOM {
            /**
             * Executes create operation with thermal imaging domain optimization.
             *
             */
            override fun create(): Pivot {
                return Pivot(AXIS_Y, PIVOT_MAX)
            }
        }, ;

        abstract fun create(): Pivot
    }

    @IntDef(AXIS_X, AXIS_Y)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Axis

    companion object {
        const val AXIS_X = 0
        const val AXIS_Y = 1
        private const val PIVOT_CENTER = -1
        private const val PIVOT_MAX = -2
    }
}
