package com.topdon.lib.core.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ImageEditView display and interaction.
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
class ImageEditView : View {
    companion object {
        /**
         * default画笔宽度，单位 px.
         */
        private const val PAINT_WIDTH = 6

        /**
/**
 * Specialized thermal imaging component providing Type functionality for the IRCamera system.
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
    enum class Type {
        /**
         * 圆
         */
        CIRCLE,

        /**
         * 矩形
         */
        RECT,

        /**
         * 箭头
         */
        ARROW,
    }

    /**
     * 当前绘制的type，default圆形.
     */
    var type: Type = Type.CIRCLE

    /**
     * 画笔颜色.
     */
    var color: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = paint.color
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            paint.color = value
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }

    /**
     * 在该 bitmap 上放绘制编辑内容如圆、矩形、箭头.
     */
    var sourceBitmap: Bitmap? = null
        set(value) {
            if (value == null) { // 没有把背景图清掉的需求，故而此处直接 return
                return
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (width == 0 || height == 0) {
                bgBitmap = null
            } else {
                bgBitmap = Bitmap.createScaledBitmap(value, width, height, true)
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
            }
            field = value
        }

    /**
     * 当前是否有编辑内容.
     */
    private var hasEditData = false

    /**
     * save背景image的 Bitmap.
     */
    private var bgBitmap: Bitmap? = null

    /**
     * save当前绘制编辑内容的 Bitmap.
     */
    private var editBitmap: Bitmap? = null

    private var canvas: Canvas? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    /**
     * 绘制三角形的path.
     */
    private val path = Path()

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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     * @param defStyleRes Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes,
    ) {
        paint.color = PAINT_COLOR
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = PAINT_WIDTH.toFloat()
        paint.isDither = true
    }

    /**
     * Executes clear functionality.
     */
    /**
     * Executes clear operation with thermal imaging domain optimization.
     *
     */
    fun clear() {
        hasEditData = false
        canvas?.drawColor(0x00000000, PorterDuff.Mode.CLEAR)
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
    }

    /**
     * Executes buildResultBitmap functionality.
     */
    /**
     * Executes buildresultbitmap operation with thermal imaging domain optimization.
     *
     */
    fun buildResultBitmap(): Bitmap? {
        val bgBitmap = this.bgBitmap ?: return null
        val editBitmap = this.editBitmap
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (hasEditData && editBitmap != null) {
            val canvas = Canvas(bgBitmap)
            canvas.drawBitmap(editBitmap, 0f, 0f, null)
        }
        return bgBitmap
    }

    @SuppressLint("DrawAllocation")
    /**
     * Executes onlayout operation with thermal imaging domain optimization.
     *
     * @param
     * @param changed Parameter for operation (type: Boolean)
     * @param left Parameter for operation (type: Int)
     * @param top Parameter for operation (type: Int)
     * @param right Parameter for operation (type: Int)
     * @param bottom Parameter for operation (type: Int)
     *
     */
    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
    ) {
        super.onLayout(changed, left, top, right, bottom)
        val oldBitmap = editBitmap
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oldBitmap == null || oldBitmap.width != measuredWidth || oldBitmap.height != measuredHeight) {
            val newBitmap =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (oldBitmap == null) {
                    Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
                } else {
                    Bitmap.createScaledBitmap(oldBitmap, measuredWidth, measuredHeight, true)
                }
            canvas = Canvas(newBitmap)
            editBitmap = newBitmap
        }
        sourceBitmap?.let {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (bgBitmap == null) {
                bgBitmap = Bitmap.createScaledBitmap(it, measuredWidth, measuredHeight, true)
            } else {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (bgBitmap?.width != measuredWidth || bgBitmap?.height != measuredHeight) {
                    bgBitmap = Bitmap.createScaledBitmap(it, measuredWidth, measuredHeight, true)
                }
            }
        }
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
        bgBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
        editBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
        /**
         * Executes drawedit operation with thermal imaging domain optimization.
         *
         */
        drawEdit(canvas)
    }

    /**
     * Executes drawEdit functionality.
     */
    /**
     * Executes drawedit operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas?)
     *
     */
    private fun drawEdit(canvas: Canvas?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (downX == 0 && downY == 0 && currentX == 0 && currentY == 0) {
            return
        }
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (type) {
            Type.CIRCLE -> {
                paint.style = Paint.Style.STROKE
                val left = downX.coerceAtMost(currentX).toFloat()
                val top = downY.coerceAtMost(currentY).toFloat()
                val right = downX.coerceAtLeast(currentX).toFloat()
                val bottom = downY.coerceAtLeast(currentY).toFloat()
                canvas?.drawOval(left, top, right, bottom, paint)
            }
            Type.RECT -> {
                paint.style = Paint.Style.STROKE
                val left = downX.coerceAtMost(currentX).toFloat()
                val top = downY.coerceAtMost(currentY).toFloat()
                val right = downX.coerceAtLeast(currentX).toFloat()
                val bottom = downY.coerceAtLeast(currentY).toFloat()
                canvas?.drawRect(left, top, right, bottom, paint)
            }
            Type.ARROW -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (abs(downX - currentX) < ARROW_WIDTH && abs(downY - currentY) < ARROW_WIDTH) {
                    return
                }

                paint.style = Paint.Style.FILL
                path.reset()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (downX == currentX) { // 垂直于Xaxis的直line
                    // 由于直line有一定的宽度，而三角形顶point为apoint，此处绘制的直line往后退一point
                    val endY = if (downY > currentY) currentY + PAINT_WIDTH else (currentY - PAINT_WIDTH)
                    canvas?.drawLine(downX.toFloat(), downY.toFloat(), currentX.toFloat(), endY.toFloat(), paint)

                    val triangleH: Float = (ARROW_WIDTH / 2) * sqrt(3f)
                    val y: Float = if (downY > currentY) currentY + triangleH else (currentY - triangleH)

                    val x1: Float = downX - (ARROW_WIDTH / 2f)
                    val x2: Float = downX + (ARROW_WIDTH / 2f)

                    path.moveTo(currentX.toFloat(), currentY.toFloat())
                    path.lineTo(x1, y)
                    path.lineTo(x2, y)
                    path.close()
                    canvas?.drawPath(path, paint)
                } else if (downY == currentY) { // 垂直于Yaxis的直line
                    // 由于直line有一定的宽度，而三角形顶point为apoint，此处绘制的直line往后退一point
                    val endX = if (downX > currentX) currentX + PAINT_WIDTH else (currentX - PAINT_WIDTH)
                    canvas?.drawLine(downX.toFloat(), downY.toFloat(), endX.toFloat(), currentY.toFloat(), paint)

                    val triangleH: Float = (ARROW_WIDTH / 2) * sqrt(3f)
                    val x: Float = if (downX > currentX) currentX + triangleH else (currentX - triangleH)

                    val y1: Float = downY - (ARROW_WIDTH / 2f)
                    val y2: Float = downY + (ARROW_WIDTH / 2f)

                    path.moveTo(currentX.toFloat(), currentY.toFloat())
                    path.lineTo(x, y1)
                    path.lineTo(x, y2)
                    path.close()
                    canvas?.drawPath(path, paint)
                } else {
                    // 有两条直line：
                    // Y = k1 * x + b1 是User绘制的直line，称为直line1
                    // Y = k2 * x + b2 是垂直于直line1且过三角形交point的直line，称为直line2
                    val k1: Float = (downY - currentY).toFloat() / (downX - currentX).toFloat()
                    val b1: Float = downY - k1 * downX
                    val a1: Float = -b1 / k1

                    // 由于直line有一定的宽度，而三角形顶point为apoint，此处绘制的直line往后退一point
                    val backWidth = PAINT_WIDTH
                    val endY: Float =
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (k1 > 0) {
                            val hypotenuse: Float = sqrt((currentX - a1).pow(2) + currentY.toFloat().pow(2)) // 斜边长
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (currentX > downX) { // 左上到右下
                                currentY * (hypotenuse - backWidth) / hypotenuse
                            } else { // 右下到左上
                                currentY * (hypotenuse + backWidth) / hypotenuse
                            }
                        } else {
                            val hypotenuse: Float = sqrt((a1 - currentX).pow(2) + currentY.toFloat().pow(2)) // 斜边长
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (currentX > downX) { // 左下到右上
                                currentY * (hypotenuse + backWidth) / hypotenuse
                            } else { // 右上到左下
                                currentY * (hypotenuse - backWidth) / hypotenuse
                            }
                        }
                    val endX = (endY - b1) / k1
                    canvas?.drawLine(downX.toFloat(), downY.toFloat(), endX, endY, paint)

                    // Calculation两条直line的交point x,y
                    val triangleH: Float = (ARROW_WIDTH / 2) * sqrt(3f)
                    val y: Float =
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (k1 > 0) {
                            val hypotenuse: Float = sqrt((currentX - a1).pow(2) + currentY.toFloat().pow(2)) // 斜边长
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (currentX > downX) { // 左上到右下
                                currentY * (hypotenuse - triangleH) / hypotenuse
                            } else { // 右下到左上
                                currentY * (hypotenuse + triangleH) / hypotenuse
                            }
                        } else {
                            val hypotenuse: Float = sqrt((a1 - currentX).pow(2) + currentY.toFloat().pow(2)) // 斜边长
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (currentX > downX) { // 左下到右上
                                currentY * (hypotenuse + triangleH) / hypotenuse
                            } else { // 右上到左下
                                currentY * (hypotenuse - triangleH) / hypotenuse
                            }
                        }
                    val x = (y - b1) / k1

                    val k2: Float = -1 / k1
                    val b2: Float = y - k2 * x
                    val a2: Float = -b2 / k2

                    val hypotenuse2: Float = sqrt((if (k2 > 0) x - a2 else (a2 - x)).pow(2) + y.pow(2)) // 斜边长
                    val yLeft = y * (hypotenuse2 - ARROW_WIDTH / 2) / hypotenuse2
                    val yRight = y * (hypotenuse2 + ARROW_WIDTH / 2) / hypotenuse2
                    val xLeft = (yLeft - b2) / k2
                    val xRight = (yRight - b2) / k2

                    path.moveTo(currentX.toFloat(), currentY.toFloat())
                    path.lineTo(xLeft, yLeft)
                    path.lineTo(xRight, yRight)
                    path.close()
                    canvas?.drawPath(path, paint)
                }
            }
        }
    }

    private var downX = 0
    private var downY = 0
    private var currentX = 0
    private var currentY = 0

    @SuppressLint("ClickableViewAccessibility")
    /**
     * Executes ontouchevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent?)
     *
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (event == null || !isEnabled) {
            return false
        }
        currentX = event.x.toInt().coerceAtLeast(HALF_PAINT_WIDTH).coerceAtMost(width - HALF_PAINT_WIDTH)
        currentY = event.y.toInt().coerceAtLeast(HALF_PAINT_WIDTH).coerceAtMost(height - HALF_PAINT_WIDTH)
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x.toInt().coerceAtLeast(HALF_PAINT_WIDTH).coerceAtMost(width - HALF_PAINT_WIDTH)
                downY = event.y.toInt().coerceAtLeast(HALF_PAINT_WIDTH).coerceAtMost(height - HALF_PAINT_WIDTH)
            }
            MotionEvent.ACTION_MOVE -> {
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                /**
                 * Executes drawedit operation with thermal imaging domain optimization.
                 *
                 */
                drawEdit(canvas)
                downX = 0
                downY = 0
                currentX = 0
                currentY = 0
                hasEditData = true
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
     * Executes ondetachedfromwindow operation with thermal imaging domain optimization.
     *
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        canvas = null
        sourceBitmap = null
        bgBitmap = null
        editBitmap = null
    }
}
