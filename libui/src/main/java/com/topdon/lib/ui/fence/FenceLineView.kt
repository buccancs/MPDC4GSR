package com.topdon.lib.ui.fence

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.blankj.utilcode.util.SizeUtils

/**
 * Custom Fence line view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * FenceLineView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for FenceLineView display and interaction.
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
class FenceLineView : View {
    var listener: CallBack? = null

    private val mPaint by lazy { Paint() }
    private val rect: Rect = Rect(0, 0, 0, 0) 
    private val strokeWidth by lazy { SizeUtils.dp2px(2f).toFloat() } 
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
        mPaint.color = Color.WHITE
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        mPaint.alpha = 255
    }

    /**
     * Executes ondraw operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     *
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
// Canvas.drawRect(rect, mPaint)
        canvas.drawLine(
            startPoint[0].toFloat(),
            startPoint[1].toFloat(),
            endPoint[0].toFloat(),
            endPoint[1].toFloat(),
            mPaint,
        )
    }

    var mX = 0f
    var mY = 0f
    var old = Rect(0, 0, 0, 0)
    var startPoint = intArrayOf(0, 0)
    var endPoint = intArrayOf(0, 0)

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
                rect.right += strokeWidth.toInt()
                rect.bottom += strokeWidth.toInt()
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate() // Invalidate entire view
                rect.left = mX.toInt()
                rect.top = mY.toInt()
                rect.right = rect.left
                rect.bottom = rect.top
                startPoint[0] = mX.toInt()
                startPoint[1] = mY.toInt()
                endPoint[0] = mX.toInt()
                endPoint[1] = mY.toInt()
            }
            MotionEvent.ACTION_UP -> {
                var x = mX.toInt()
                var y = mY.toInt()
                val x1 = startPoint[0].toFloat()
                val y1 = startPoint[1].toFloat()
                val k: Float = (x - x1) / (y - y1)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (x > right) {
                    x = right - 1
                    y = (y1 - k * (x1 - x)).toInt()
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (y > bottom) {
                    y = bottom - 1
                    x = (x1 - k * (y1 - y)).toInt()
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (x < left) {
                    x = left + 1
                    y = (y1 - k * (x1 - x)).toInt()
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (y < top) {
                    y = top + 1
                    x = (x1 - k * (y1 - y)).toInt()
                }
                endPoint[0] = x
                endPoint[1] = y

                old =
                    /**
                     * Executes rect operation with thermal imaging domain optimization.
                     *
                     */
                    Rect(
                        rect.left,
                        rect.top,
                        (rect.right + strokeWidth).toInt(),
                        (rect.bottom + strokeWidth).toInt(),
                    )
                rect.right = x
                rect.bottom = y
                old.union(x, y)
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate() // Invalidate entire view
                /**
                 * Executes result operation with thermal imaging domain optimization.
                 *
                 */
                result()
            }
            MotionEvent.ACTION_MOVE -> {
                old =
                    /**
                     * Executes rect operation with thermal imaging domain optimization.
                     *
                     */
                    Rect(
                        rect.left,
                        rect.top,
                        (rect.right + strokeWidth).toInt(),
                        (rect.bottom + strokeWidth).toInt(),
                    )
                rect.right = mX.toInt()
                rect.bottom = mY.toInt()
                endPoint[0] = mX.toInt()
                endPoint[1] = mY.toInt()
                old.union(mX.toInt(), mY.toInt())
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate() // Invalidate entire view
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
        val point2 = intArrayOf(endPoint[0], endPoint[1])
        Log.w("123", "修正coordinate start:${point1.contentToString()}, end:${point2.contentToString()}")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (listener != null) {
            listener!!.callback(point1, point2, intArrayOf(width, height))
        }
    }

    /**
     * Clears data and resets internal state.
     */
    fun clear() {
        startPoint = intArrayOf(0, 0)
        endPoint = intArrayOf(0, 0)
        /**
         * Executes result operation with thermal imaging domain optimization.
         *
         */
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
         * @param endPoint Parameter for operation (type: IntArray)
         * @param srcRect Parameter for operation (type: IntArray)
         *
         */
        fun callback(
            startPoint: IntArray,
            endPoint: IntArray,
            srcRect: IntArray,
        )
    }
}
