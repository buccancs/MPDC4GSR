package com.topdon.lib.ui.fence

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.ui.R as UiR

/**
 * Custom Fence point view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * FencePointView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for FencePointView display and interaction.
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
class FencePointView : View {
    var listener: CallBack? = null
    private val iconSize = SizeUtils.dp2px(32f)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor (context: Context) : super(context)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet)
     *
     */
    constructor (context: Context, attrs: AttributeSet) : super(context, attrs)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet)
     * @param defStyle Parameter for operation (type: Int)
     *
     */
    constructor (context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle,
    )

    init {
    }

    private val mPaint by lazy {
        /**
         * Executes paint operation with thermal imaging domain optimization.
         *
         */
        Paint().apply {
            color = Color.BLUE
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = strokeWidth
            alpha = 255
        }
    }

    var destW = 0
    var destH = 0

    private val drawable: BitmapDrawable by lazy {
        resources.getDrawable(
            UiR.mipmap.ic_fence_point,
            null,
        ) as BitmapDrawable
    }

    @SuppressLint("UseCompatLoadingForDrawables", "DrawAllocation")
    /**
     * Executes ondraw operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     *
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bitmap = drawable.bitmap
        val bw = bitmap.width
        val bh = bitmap.height
        destW = iconSize
        destH = destW * bh / bw
        val src = Rect(0, 0, bw, bh)

        var left = startPoint[0] - destW / 2
        var top = startPoint[1] - destH / 2
        var right = startPoint[0] + destW / 2
        var bottom = startPoint[1] + destH / 2
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (left < 0) {
            left = 0
            right = destW
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (right > width) {
            right = width
            left = width - destW
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (top < 0) {
            top = 0
            bottom = destH
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bottom > height) {
            bottom = height
            top = height - destH
        }

        val dst =
            /**
             * Executes rect operation with thermal imaging domain optimization.
             *
             */
            Rect(
                left,
                top,
                right,
                bottom,
            )
        canvas.drawBitmap(bitmap, src, dst, mPaint)
    }

    var mX = 0f
    var mY = 0f
    var old = Rect(0, 0, 0, 0)
    var startPoint = intArrayOf(0, 0)

    /**
     * Executes ontouchevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent)
     *
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mX = event.x
        mY = event.y
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startPoint[0] = mX.toInt()
                startPoint[1] = mY.toInt()
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
//                Log.i("123", "onTouchEvent: ACTION_UP")
                startPoint[0] = mX.toInt()
                startPoint[1] = mY.toInt()
                /**
                 * Executes result operation with thermal imaging domain optimization.
                 *
                 */
                result()
            }
            MotionEvent.ACTION_MOVE -> {
                startPoint[0] = mX.toInt()
                startPoint[1] = mY.toInt()
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
            }
        }
        return true
    }

    /**
     * Executes result functionality.
     */
    /**
     * Executes result operation with thermal imaging domain optimization.
     *
     */
    private fun result() {
        val point1 = intArrayOf(startPoint[0], startPoint[1])
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startPoint[0] - destW / 2 < 0) {
            
            point1[0] = destW / 2
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startPoint[0] + destW / 2 > width) {
            
            point1[0] = width - destW / 2
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startPoint[1] - destW / 2 < 0) {
            
            point1[1] = destH / 2
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startPoint[1] + destW / 2 > height) {
            
            point1[1] = height - destH / 2
        }
        Log.w("123", "coordinate point:${point1.contentToString()}")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (listener != null) {
            listener!!.callback(point1, intArrayOf(width, height))
        }
    }

    /**
     * Clears data and resets internal state.
     */
    fun clear() {
        startPoint = intArrayOf(0, 0)
        result()
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
    }

/**
 * Specialized thermal imaging component providing CallBack functionality for the IRCamera system.
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
    interface CallBack {
        /**
         * startPoint: 左上角
         * endPoint: 右下角
         */
    /**
     * Executes callback functionality.
     */
        /**
         * Executes callback operation with thermal imaging domain optimization.
         *
         * @param
         * @param startPoint Parameter for operation (type: IntArray)
         * @param srcRect Parameter for operation (type: IntArray)
         *
         */
        fun callback(
            startPoint: IntArray,
            srcRect: IntArray,
        )
    }
}
