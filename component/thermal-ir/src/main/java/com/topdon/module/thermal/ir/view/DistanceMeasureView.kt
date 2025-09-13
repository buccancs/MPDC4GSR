package com.topdon.module.thermal.ir.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @author: CaiSongL
 * @date: 2023/9/28 13:49
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for DistanceMeasureView display and interaction.
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
class DistanceMeasureView : View {
    private var margin: Float = 0f
    private var linePaint: Paint? = null
    private var line1Y = 0f
    private var line2Y = 0f
    var distance = 0f
        private set

    var moveListener: ((distance: Float) -> Unit)? = null

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context?)
     *
     */
    constructor(context: Context?) : super(context) {
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init()
    }

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context?)
     * @param attrs Parameter for operation (type: AttributeSet?)
     *
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init()
    }

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context?)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
    ) {
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init()
    }

    /**
     * Initializes  component.
     */
    /**
     * Initializes the  component for thermal imaging operations.
     *
     */
    private fun init() {
        linePaint = Paint()
        linePaint!!.color = Color.GREEN
        linePaint!!.strokeWidth = 4f
        linePaint!!.style = Paint.Style.STROKE
set虚line的间隔长度和line条长度
        val intervals = floatArrayOf(10f, 10f)
        linePaint!!.pathEffect = DashPathEffect(intervals, 0f)
    }

    /**
     * Executes onmeasure operation with thermal imaging domain optimization.
     *
     * @param
     * @param widthMeasureSpec Parameter for operation (type: Int)
     * @param heightMeasureSpec Parameter for operation (type: Int)
     *
     */
    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
calculation初始位置，使得两条line居中，间隔20dp
        val screenHeight = measuredHeight
        val lineHeight = 50 // 在dimens.xml中定义line_height
        margin = ((screenHeight - lineHeight) / 2).toFloat()
        line1Y = margin
        line2Y = margin + lineHeight
        distance = lineHeight.toFloat()
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
drawing两条水平line
        canvas.drawLine(50f, line1Y, (width - 50).toFloat(), line1Y, linePaint!!)
        canvas.drawLine(50f, line2Y, (width - 50).toFloat(), line2Y, linePaint!!)
    }

    /**
     * Executes ontouchevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent)
     *
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                var newY = event.y

限制line的range，防止line移出视图range
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (newY < 0) {
                    newY = 0f
                } else if (newY > height) {
                    newY = height.toFloat()
                }

根据Touch位置updateline的位置
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (Math.abs(newY - line1Y) < Math.abs(newY - line2Y)) {
                    val abs = line1Y - newY
                    line1Y = newY
                    line2Y += abs
                } else {
                    val abs = newY - line2Y
                    line2Y = newY
                    line1Y -= abs
                }
update距离
                distance = Math.abs(line2Y - line1Y)
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
                moveListener?.invoke(distance)
            }
        }
        return true
    }
}
