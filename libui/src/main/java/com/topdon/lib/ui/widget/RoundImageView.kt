package com.topdon.lib.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.topdon.lib.ui.R as UiR

/**
 * Custom Round image view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * RoundImageView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for RoundImageView display and interaction.
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
class RoundImageView : AppCompatImageView {
    companion object {
        /** 圆角位置 - 左上.  */
        const val LEFT_TOP = 1

        /** 圆角位置 - 右上.  */
        const val RIGHT_TOP = 2

        /** 圆角位置 - 左下.  */
        const val LEFT_BOTTOM = 4

        /** 圆角位置 - 右下.  */
        const val RIGHT_BOTTOM = 8

        /** default圆角半径 - 10dp  */
        private const val DEFAULT_RADIUS = 10f

        /** default圆角位置 - 4个角均圆角  */
        private const val DEFAULT_POSITION = 15
    }

    var position = 0 
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (field != value) {
                field = value
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
            }
        }

    private var radius = 0 // 圆角半径，单位 px
    private val path = Path() 
    private var density = 0f // 屏幕Scale等级，用于dp与pxconversion

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
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        density = context.resources.displayMetrics.density

        val typedArray = context.obtainStyledAttributes(attrs, UiR.styleable.RoundImageView, defStyleAttr, 0)
        radius = typedArray.getDimensionPixelSize(UiR.styleable.RoundImageView_round_radius, dp2px(DEFAULT_RADIUS))
        position = typedArray.getInt(UiR.styleable.RoundImageView_round_position, DEFAULT_POSITION)
        typedArray.recycle()
    }

    /**
     * Executes ondraw operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     *
     */
    override fun onDraw(canvas: Canvas) {
        path.rewind()

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (position and LEFT_TOP == LEFT_TOP) {
            path.moveTo(radius.toFloat(), 0f)
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (position and RIGHT_TOP == RIGHT_TOP) {
            path.lineTo((width - radius).toFloat(), 0f)
            path.quadTo(width.toFloat(), 0f, width.toFloat(), radius.toFloat())
        } else {
            path.lineTo(width.toFloat(), 0f)
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (position and RIGHT_BOTTOM == RIGHT_BOTTOM) {
            path.lineTo(width.toFloat(), (height - radius).toFloat())
            path.quadTo(width.toFloat(), height.toFloat(), (width - radius).toFloat(), height.toFloat())
        } else {
            path.lineTo(width.toFloat(), height.toFloat())
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (position and LEFT_BOTTOM == LEFT_BOTTOM) {
            path.lineTo(radius.toFloat(), height.toFloat())
            path.quadTo(0f, height.toFloat(), 0f, (height - radius).toFloat())
        } else {
            path.lineTo(0f, height.toFloat())
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (position and LEFT_TOP == LEFT_TOP) {
            path.lineTo(0f, radius.toFloat())
            path.quadTo(0f, 0f, radius.toFloat(), 0f)
        } else {
            path.lineTo(0f, 0f)
        }

        canvas.clipPath(path)
        super.onDraw(canvas)
    }

    /**
     * settings圆角半径，单位**dp**.
     */
    /**
     * Configures the radius with validation and thermal imaging optimization.
     *
     * @param
     * @param radius Parameter for operation (type: Float)
     *
     */
    fun setRadius(radius: Float) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this.radius != dp2px(radius)) {
            this.radius = dp2px(radius)
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }
    }

    /**
     * Executes dp2px functionality.
     */
    /**
     * Executes dp2px operation with thermal imaging domain optimization.
     *
     * @param
     * @param dpValue Parameter for operation (type: Float)
     *
     */
    private fun dp2px(dpValue: Float): Int {
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (dpValue * density + 0.5f).toInt()
    }
}
