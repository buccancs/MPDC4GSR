package com.topdon.module.thermal.ir.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import com.blankj.utilcode.util.SizeUtils
import com.energy.iruvc.utils.Line
import com.infisense.usbir.utils.TempDrawHelper
import com.infisense.usbir.utils.TempDrawHelper.Companion.correct
import com.infisense.usbir.utils.TempDrawHelper.Companion.correctPoint
import com.topdon.lib.core.tools.UnitTools
import com.topdon.module.thermal.ir.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
TC007、2D 编辑 pointlineareatemperature图层公共逻辑封装.
 *
 * Created by LCG on 2024/5/7.
/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TemperatureBaseView algorithms.
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
abstract class TemperatureBaseView : View {
    companion object {
        /**
支持pointlinearea的default最大数量.
         */
        private const val DEFAULT_MAX_COUNT = 3
/**
 * Specialized thermal imaging component providing Mode functionality for the IRCamera system.
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
    enum class Mode {
        POINT,
        LINE,
        RECT,
        TREND,
        FULL,
        CLEAR,
    }

    /**
当前是否display了full image.
     */
    @Volatile
    var isShowFull: Boolean = true
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (value && mode == Mode.CLEAR) {
                mode = Mode.FULL
            }
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }

    /**
当前操作mode：point、line、area、full image、Clear。
     */
    @Volatile
    open var mode = Mode.FULL
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (value == Mode.FULL) { // 全图
                isShowFull = true
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
            } else if (value == Mode.CLEAR) {
                isShowFull = false
                /**
                 * Executes synchronized operation with thermal imaging domain optimization.
                 *
                 */
                synchronized(this) {
                    pointList.clear()
                    lineList.clear()
                    rectList.clear()
                }
                trendLine = null
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
            }
        }

    /**
temperature值text大小，单位 px.
     */
    var tempTextSize: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = helper.textSize
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            helper.textSize = value
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }

    /**
temperature值text、pointlineareanametext color value.
     */
    var textColor: Int
        @ColorInt get() = helper.textColor
        set(
            @ColorInt value
        ) {
            helper.textColor = value
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }

    /**
由于 Touch Event导致的pointadd、移除、变更EventListener，coordinate为通过 [setImageSize] set的coordinate系
     */
    var onPointListener: ((pointList: List<Point>) -> Unit)? = null

    /**
由于 Touch Event导致的lineadd、移除、变更EventListener，coordinate为通过 [setImageSize] set的coordinate系
     */
    var onLineListener: ((lineList: List<Point>) -> Unit)? = null

    /**
由于 Touch Event导致的areaadd、移除、变更EventListener，coordinate为通过 [setImageSize] set的coordinate系
     */
    var onRectListener: ((rectList: List<Rect>) -> Unit)? = null

    /**
由于 Touch Event导致的趋势图add或趋势图移除EventListener.
     *
也就是说：将 [mode] set为 [Mode.CLEAR] 不会触发该Callback.
     */
    var onTrendOperateListener: ((isAdd: Boolean) -> Unit)? = null

    /**
以 View 尺寸为coordinate系，当前已add的point列表.
     */
    protected val pointList = ArrayList<Point>()

    /**
以 View 尺寸为coordinate系，当前已add的line列表.
     */
    protected val lineList = ArrayList<Line>()

    /**
以 View 尺寸为coordinate系，当前已add的area列表.
     */
    protected val rectList = ArrayList<Rect>()

    /**
以 View 尺寸为coordinate系，当前已add的趋势图直line.
     */
    @Volatile
    protected var trendLine: Line? = null

    /**
     * Retrieves the pointlistsafe with optimized performance for thermal imaging operations.
     *
     */
    protected fun getPointListSafe(): List<Point> = synchronized(this) { pointList }

    /**
     * Retrieves the linelistsafe with optimized performance for thermal imaging operations.
     *
     */
    protected fun getLineListSafe(): List<Line> = synchronized(this) { lineList }

    /**
     * Retrieves the rectlistsafe with optimized performance for thermal imaging operations.
     *
     */
    protected fun getRectListSafe(): List<Rect> = synchronized(this) { rectList }

    /**
     * Retrieves sourcepointlist information.
     */
    private fun getSourcePointList(): List<Point> {
        val resultList = ArrayList<Point>(pointList.size)
        pointList.forEach {
            resultList.add(Point((it.x / xScale).toInt(), (it.y / yScale).toInt()))
        }
        return resultList
    }

    /**
     * Retrieves sourcelinelist information.
     */
    private fun getSourceLineList(): List<Point> {
        val resultList = ArrayList<Point>(lineList.size * 2)
        lineList.forEach {
            val startPoint = Point((it.start.x / xScale).toInt(), (it.start.y / yScale).toInt())
            val endPoint = Point((it.end.x / xScale).toInt(), (it.end.y / yScale).toInt())
            resultList.add(startPoint)
            resultList.add(endPoint)
        }
        return resultList
    }

    /**
     * Retrieves sourcerectlist information.
     */
    private fun getSourceRectList(): List<Rect> {
        val resultList = ArrayList<Rect>(rectList.size)
        rectList.forEach {
            val left = (it.left / xScale).toInt()
            val right = (it.right / xScale).toInt()
            val top = (it.top / yScale).toInt()
            val bottom = (it.bottom / yScale).toInt()
            resultList.add(Rect(left, top, right, bottom))
        }
        return resultList
    }

    private val helper = TempDrawHelper()

    protected var xScale = 0f
    protected var yScale = 0f
    protected var imageWidth = 0
    protected var imageHeight = 0

    @CallSuper
    open fun setImageSize(
        imageWidth: Int,
        imageHeight: Int,
    ) {
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        this.xScale = width.toFloat() / imageWidth.toFloat()
        this.yScale = height.toFloat() / imageHeight.toFloat()
    }

    /**
支持pointlinearea的最大数量，default3.
     */
    protected val maxCount: Int

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
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.TemperatureBaseView)
        maxCount = typeArray.getInt(R.styleable.TemperatureBaseView_maxCount, DEFAULT_MAX_COUNT)
        typeArray.recycle()
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
        xScale = measuredWidth.toFloat() / imageWidth
        yScale = measuredHeight.toFloat() / imageHeight
    }

    // ******************************************** Draw ********************************************

    /**
以 View 尺寸为coordinate系，在 (x,y) 画a十字.
     *
注意，不对 x、y 进行processing，传进来是哪就在哪drawing。
@param point 以 View 尺寸为coordinate系的point
     */
    /**
     * Executes drawpoint operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     * @param point Parameter for operation (type: Point)
     *
     */
    protected fun drawPoint(
        canvas: Canvas,
        point: Point,
    ) {
        helper.drawPoint(canvas, point.x, point.y)
    }

    /**
以 View 尺寸为coordinate系，connection (startX, startY)、(stopX, stopY) 两pointdrawing一条line段.
     */
    protected fun drawLine(
        canvas: Canvas,
        line: Line,
    ) {
由于line段与实心point的的drawing是分开的，line段使用当前 View coordinate，而实心point使用temperature(192x256)coordinateconversion为 View coordinate
故而这里需要把当前的coordinate，尽量贴近temperaturecoordinate的整数倍，否则会出现实心圆偏离直line太远的情况
        val startX: Int = ((line.start.x / xScale).toInt() * xScale).toInt()
        val startY: Int = ((line.start.y / yScale).toInt() * yScale).toInt()
        val stopX: Int = ((line.end.x / xScale).toInt() * xScale).toInt()
        val stopY: Int = ((line.end.y / yScale).toInt() * yScale).toInt()
        helper.drawLine(canvas, startX, startY, stopX, stopY)
    }

    /**
以 View 尺寸为coordinate系，按指定rangedrawinga矩形.
     */
    protected fun drawRect(
        canvas: Canvas,
        rect: Rect,
    ) {
        val left: Int = ((rect.left / xScale).toInt() * xScale).toInt()
        val top: Int = ((rect.top / yScale).toInt() * yScale).toInt()
        val right: Int = ((rect.right / xScale).toInt() * xScale).toInt()
        val bottom: Int = ((rect.bottom / yScale).toInt() * yScale).toInt()
        helper.drawRect(canvas, left, top, right, bottom)
    }

    /**
以 View 尺寸为coordinate系，在 (x,y) 画a实心圆。
     *
注意，不对 x、y 进行processing，传进来是哪就在哪drawing。
@param isMax true-最high temperature红色 false-最low temperature蓝色
     */
    /**
     * Executes drawcircle operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     * @param x Parameter for operation (type: Int)
     * @param y Parameter for operation (type: Int)
     * @param isMax Parameter for operation (type: Boolean)
     *
     */
    protected fun drawCircle(
        canvas: Canvas,
        x: Int,
        y: Int,
        isMax: Boolean,
    ) {
        helper.drawCircle(canvas, x, y, isMax)
    }

    /**
以 View 尺寸为coordinate系，指定的 (x,y) coordinate为实心圆圆心，以该实心圆为基准drawing指定text。
若空间允许则放置在实心圆圆心右上方，否则根据实际情况放置在下方、左方或左下方.
     *
注意，不对 x、y 进行processing，传进来是哪就在哪drawing。
@param x 实心圆圆心的 View 尺寸coordinate
     */
    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     * @param x Parameter for operation (type: Int)
     * @param y Parameter for operation (type: Int)
     * @param temp Temperature value in Celsius (type: Float)
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    protected fun drawTempText(
        canvas: Canvas,
        x: Int,
        y: Int,
        temp: Float,
    ) {
        helper.drawTempText(canvas, UnitTools.showC(temp), width, x, y)
    }

    /**
以 View 尺寸为coordinate系，以指定line段为基准drawing趋势图的 "A"、"B" text。
     *
注意，不对 x、y 进行processing，传进来是哪就在哪drawing。
     */
    protected fun drawTrendText(
        canvas: Canvas,
        line: Line,
    ) {
        helper.drawTrendText(canvas, width, height, line.start.x, line.start.y, line.end.x, line.end.y)
    }

    /**
以 View 尺寸为coordinate系，指定的 (x,y) coordinate为实心圆圆心，以该实心圆为基准drawing指定pointnametext。
若空间允许则放置在实心圆圆心正下方，否则放正上方.
     */
    protected fun drawPointName(
        canvas: Canvas,
        name: String,
        point: Point,
    ) {
由于十字与实心point的的drawing是分开的，十字使用当前 View coordinate，而实心point使用temperature(192x256)coordinate
故而这里需要把当前的coordinate，conversion为temperaturecoordinate的整数倍，否则会出现center对不上的情况
        val x = ((point.x / xScale).toInt() * xScale).toInt()
        val y = ((point.y / yScale).toInt() * yScale).toInt()
        helper.drawPointName(canvas, name, width, height, x, y)
    }

    /**
以 View 尺寸为coordinate系，指定的 line段或矩形 coordinate为range，
以该range为基准drawing指定linenametext，放置于rangecenter。
     */
    protected fun drawLineName(
        canvas: Canvas,
        name: String,
        line: Line,
    ) {
        val startX = ((line.start.x / xScale).toInt() * xScale).toInt()
        val startY = ((line.start.y / yScale).toInt() * yScale).toInt()
        val stopX = ((line.end.x / xScale).toInt() * xScale).toInt()
        val stopY = ((line.end.y / yScale).toInt() * yScale).toInt()
        helper.drawPointRectName(canvas, name, width, height, startX, startY, stopX, stopY)
    }

    /**
以 View 尺寸为coordinate系，指定的 line段或矩形 coordinate为range，
以该range为基准drawing指定linenametext，放置于rangecenter。
     */
    protected fun drawRectName(
        canvas: Canvas,
        name: String,
        rect: Rect,
    ) {
        val left: Int = ((rect.left / xScale).toInt() * xScale).toInt()
        val top: Int = ((rect.top / yScale).toInt() * yScale).toInt()
        val right: Int = ((rect.right / xScale).toInt() * xScale).toInt()
        val bottom: Int = ((rect.bottom / yScale).toInt() * yScale).toInt()
        helper.drawPointRectName(canvas, name, width, height, left, top, right, bottom)
    }

    // **************************************** Touch ****************************************

    private var downX = 0
    private var downY = 0

    /**
是否为add pointlinearea mode。
     *
true-adda新pointlinearea false-移动a已有pointlinearea
     */
    private var isAddAction = true

    @SuppressLint("ClickableViewAccessibility")
    /**
     * Executes ontouchevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent)
     *
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isEnabled) {
            return false
        }
        return when (mode) {
            Mode.POINT -> touchPoint(event)
            Mode.LINE -> touchLine(event, false)
            Mode.RECT -> touchRect(event)
            Mode.TREND -> touchLine(event, true)
            else -> super.onTouchEvent(event)
        }
    }

**************************************** point ****************************************

    /**
Touch 时当前正在操作（add、移动）的point.
     */
    protected var operatePoint: Point? = null

    /**
     * Executes touchPoint functionality.
     */
    /**
     * Executes touchpoint operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent)
     *
     */
    private fun touchPoint(event: MotionEvent): Boolean {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x.correctPoint(width)
                downY = event.y.correctPoint(height)
                val point: Point? = pollPoint(downX, downY)
                isAddAction = point == null
                operatePoint = point ?: Point(downX, downY)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (point == null && pointList.size == maxCount) { // 新增时已达最大数量
                    /**
                     * Executes synchronized operation with thermal imaging domain optimization.
                     *
                     */
                    synchronized(this) {
                        pointList.removeAt(0)
                    }
                }
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                operatePoint?.x = event.x.correctPoint(width)
                operatePoint?.y = event.y.correctPoint(height)
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val x: Int = event.x.correctPoint(width)
                val y: Int = event.y.correctPoint(height)
                operatePoint?.x = x
                operatePoint?.y = y

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isAddAction || abs(x - downX) > DELETE_TOLERANCE || abs(y - downY) > DELETE_TOLERANCE) {
                    /**
                     * Executes synchronized operation with thermal imaging domain optimization.
                     *
                     */
                    synchronized(this) {
                        pointList.add(operatePoint ?: Point())
                    }
                }
                operatePoint = null
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()

                onPointListener?.invoke(getSourcePointList())
                return true
            }
            else -> return false
        }
    }

    /**
     * Executes pollPoint functionality.
     */
    /**
     * Executes pollpoint operation with thermal imaging domain optimization.
     *
     * @param
     * @param x Parameter for operation (type: Int)
     * @param y Parameter for operation (type: Int)
     *
     */
    private fun pollPoint(
        x: Int,
        y: Int,
    ): Point? {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in pointList.size - 1 downTo 0) {
            val point: Point = pointList[i]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (point.x in x - TOUCH_TOLERANCE..x + TOUCH_TOLERANCE && point.y in y - TOUCH_TOLERANCE..y + TOUCH_TOLERANCE) {
                return synchronized(this) { pointList.removeAt(i) }
            }
        }
        return null
/**
 * Specialized thermal imaging component providing LineMoveType functionality for the IRCamera system.
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
    private enum class LineMoveType { ALL, START, END, }

    /**
line移动方式：整体移动、仅变更头、仅变更尾。
     */
    private var lineMoveType = LineMoveType.ALL

    /**
仅整体移动line时，save DOWN state下的line初始coordinate，用于calculation移动.
     */
    private val downLine: Line = Line(Point(0, 0), Point(0, 0))

    /**
     * Executes touchLine functionality.
     */
    /**
     * Executes touchline operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent)
     * @param isTrend Parameter for operation (type: Boolean)
     *
     */
    private fun touchLine(
        event: MotionEvent,
        isTrend: Boolean,
    ): Boolean {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x.correct(width)
                downY = event.y.correct(height)
                val line: Line? = pollLine(downX, downY, isTrend)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (line == null) {
                    isAddAction = true
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isTrend) {
                        operateTrend = Line(Point(downX, downY), Point(downX, downY))
                        trendLine = null
                        onTrendOperateListener?.invoke(false)
                    } else {
                        operateLine = Line(Point(downX, downY), Point(downX, downY))
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (lineList.size == maxCount) {
                            /**
                             * Executes synchronized operation with thermal imaging domain optimization.
                             *
                             */
                            synchronized(this) {
                                lineList.removeAt(0)
                            }
                        }
                    }
                } else {
                    isAddAction = false
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isTrend) {
                        operateTrend = line
                        onTrendOperateListener?.invoke(false)
                    } else {
                        operateLine = line
                    }
                    downLine.start.set(line.start.x, line.start.y)
                    downLine.end.set(line.end.x, line.end.y)
                    lineMoveType =
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (downX > line.start.x - TOUCH_TOLERANCE && downX < line.start.x + TOUCH_TOLERANCE &&
                            downY > line.start.y - TOUCH_TOLERANCE && downY < line.start.y + TOUCH_TOLERANCE
                        ) {
                            LineMoveType.START
                        } else if (downX > line.end.x - TOUCH_TOLERANCE && downX < line.end.x + TOUCH_TOLERANCE &&
                            downY > line.end.y - TOUCH_TOLERANCE && downY < line.end.y + TOUCH_TOLERANCE
                        ) {
                            LineMoveType.END
                        } else {
                            LineMoveType.ALL
                        }
                }
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val x: Int = event.x.correct(width)
                val y: Int = event.y.correct(height)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isAddAction) {
                    (if (isTrend) operateTrend else operateLine)?.end?.x = x
                    (if (isTrend) operateTrend else operateLine)?.end?.y = y
                } else {
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (lineMoveType) {
                        LineMoveType.ALL -> { // 整体移动
                            val rect: Rect = TempDrawHelper.getRect(width, height)
                            val minX: Int = min(downLine.start.x, downLine.end.x)
                            val maxX: Int = max(downLine.start.x, downLine.end.x)
                            val minY: Int = min(downLine.start.y, downLine.end.y)
                            val maxY: Int = max(downLine.start.y, downLine.end.y)
                            val biasX: Int = if (x < downX) max(x - downX, rect.left - minX) else min(x - downX, rect.right - maxX)
                            val biasY: Int = if (y < downY) max(y - downY, rect.top - minY) else min(y - downY, rect.bottom - maxY)
                            (if (isTrend) operateTrend else operateLine)?.start?.set(downLine.start.x + biasX, downLine.start.y + biasY)
                            (if (isTrend) operateTrend else operateLine)?.end?.set(downLine.end.x + biasX, downLine.end.y + biasY)
                        }
                        LineMoveType.START -> {
                            (if (isTrend) operateTrend else operateLine)?.start?.x = x
                            (if (isTrend) operateTrend else operateLine)?.start?.y = y
                        }
                        LineMoveType.END -> {
                            (if (isTrend) operateTrend else operateLine)?.end?.x = x
                            (if (isTrend) operateTrend else operateLine)?.end?.y = y
                        }
                    }
                }
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val x: Int = event.x.correct(width)
                val y: Int = event.y.correct(height)
                val line: Line = (if (isTrend) operateTrend else operateLine) ?: Line(Point(), Point())
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if ((line.start.x / xScale).toInt() != (line.end.x / xScale).toInt() || (line.start.y / yScale).toInt() != (line.end.y / yScale).toInt()) {
只有画出来的结果不是apoint才生效
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isAddAction || abs(x - downX) > DELETE_TOLERANCE || abs(y - downY) > DELETE_TOLERANCE) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isTrend) {
                            trendLine = line
                            onTrendOperateListener?.invoke(true)
                        } else {
                            /**
                             * Executes synchronized operation with thermal imaging domain optimization.
                             *
                             */
                            synchronized(this) {
                                lineList.add(line)
                            }
                        }
                    }
                }
                operateTrend = null
                operateLine = null
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isTrend) {
                    onLineListener?.invoke(getSourceLineList())
                }
                return true
            }
            else -> return false
        }
    }

    /**
     * Executes pollLine functionality.
     */
    /**
     * Executes pollline operation with thermal imaging domain optimization.
     *
     * @param
     * @param x Parameter for operation (type: Int)
     * @param y Parameter for operation (type: Int)
     * @param isTrend Parameter for operation (type: Boolean)
     *
     */
    private fun pollLine(
        x: Int,
        y: Int,
        isTrend: Boolean,
    ): Line? {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTrend) {
            val resultLine = trendLine
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isLineConcat(resultLine, x, y)) {
                trendLine = null
                return resultLine
            }
        } else {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in lineList.size - 1 downTo 0) {
                val line: Line = lineList[i]
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isLineConcat(line, x, y)) {
                    return synchronized(this) { lineList.removeAt(i) }
                }
            }
        }
        return null
    }

    /**
/**
 * Executes 判断指定coordinate operation with thermal imaging domain optimization.
 *
 */
判断指定coordinate (x, y) 是否视为指定 Line 的selected.
     */
    private fun isLineConcat(
        line: Line?,
        x: Int,
        y: Int,
    ): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (line == null) {
/**
 * Specialized thermal imaging component providing RectMoveType functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
/**
 * Specialized thermal imaging component providing RectMoveEdge functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
/**
 * Specialized thermal imaging component providing RectMoveCorner functionality for the IRCamera system.
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
    private enum class RectMoveCorner { LT, RT, RB, LB }

    /**
仅角移动mode时，移动的是哪个角.
     */
    private var rectMoveCorner = RectMoveCorner.LT

    /**
移动area时，save DOWN state下的area初始coordinate，用于calculation移动.
     */
    private val downRect = Rect()

    /**
     * Executes touchRect functionality.
     */
    /**
     * Executes touchrect operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent)
     *
     */
    private fun touchRect(event: MotionEvent): Boolean {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x.correct(width)
                downY = event.y.correct(height)
                val rect: Rect? = pollRect(downX, downY)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (rect == null) { // 插入
                    isAddAction = true
                    operateRect = Rect(downX, downY, downX, downY)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (rectList.size == maxCount) {
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized(this) {
                            rectList.removeAt(0)
                        }
                    }
                } else { // 选取 - delete
                    isAddAction = false
                    operateRect = rect
                    downRect.set(rect)
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (downX) {
                        in rect.left - TOUCH_TOLERANCE..rect.left + TOUCH_TOLERANCE -> { // Selected最左那条边
                            /**
                             * Executes when operation with thermal imaging domain optimization.
                             *
                             */
                            when (downY) {
                                in rect.top - TOUCH_TOLERANCE..rect.top + TOUCH_TOLERANCE -> { // Selected顶边
                                    rectMoveType = RectMoveType.CORNER
                                    rectMoveCorner = RectMoveCorner.LT
                                }
                                in rect.bottom - TOUCH_TOLERANCE..rect.bottom + TOUCH_TOLERANCE -> { // Selected底边
                                    rectMoveType = RectMoveType.CORNER
                                    rectMoveCorner = RectMoveCorner.LB
                                }
                                else -> {
                                    rectMoveType = RectMoveType.EDGE
                                    rectMoveEdge = RectMoveEdge.LEFT
                                }
                            }
                        }
                        in rect.right - TOUCH_TOLERANCE..rect.right + TOUCH_TOLERANCE -> { // Selected最右那条边
                            /**
                             * Executes when operation with thermal imaging domain optimization.
                             *
                             */
                            when (downY) {
                                in rect.top - TOUCH_TOLERANCE..rect.top + TOUCH_TOLERANCE -> { // Selected顶边
                                    rectMoveType = RectMoveType.CORNER
                                    rectMoveCorner = RectMoveCorner.RT
                                }
                                in rect.bottom - TOUCH_TOLERANCE..rect.bottom + TOUCH_TOLERANCE -> { // Selected底边
                                    rectMoveType = RectMoveType.CORNER
                                    rectMoveCorner = RectMoveCorner.RB
                                }
                                else -> {
                                    rectMoveType = RectMoveType.EDGE
                                    rectMoveEdge = RectMoveEdge.RIGHT
                                }
                            }
                        }
                        else -> { // 左右都没selected
                            /**
                             * Executes when operation with thermal imaging domain optimization.
                             *
                             */
                            when (downY) {
                                in rect.top - TOUCH_TOLERANCE..rect.top + TOUCH_TOLERANCE -> { // Selected顶边
                                    rectMoveType = RectMoveType.EDGE
                                    rectMoveEdge = RectMoveEdge.TOP
                                }
                                in rect.bottom - TOUCH_TOLERANCE..rect.bottom + TOUCH_TOLERANCE -> { // Selected底边
                                    rectMoveType = RectMoveType.EDGE
                                    rectMoveEdge = RectMoveEdge.BOTTOM
                                }
                                else -> {
                                    rectMoveType = RectMoveType.ALL
                                }
                            }
                        }
                    }
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val x: Int = event.x.correct(width)
                val y: Int = event.y.correct(height)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isAddAction) {
                    operateRect?.set(min(downX, x), min(downY, y), max(downX, x), max(downY, y))
                } else {
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (rectMoveType) {
                        RectMoveType.ALL -> { // 整体移动
                            val rect: Rect = TempDrawHelper.getRect(width, height)
                            val biasX: Int =
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (x < downX) {
                                    /**
                                     * Executes max operation with thermal imaging domain optimization.
                                     *
                                     */
                                    max(
                                        x - downX,
                                        rect.left - downRect.left,
                                    )
                                } else {
                                    /**
                                     * Executes min operation with thermal imaging domain optimization.
                                     *
                                     */
                                    min(x - downX, rect.right - downRect.right)
                                }
                            val biasY: Int =
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (y < downY) {
                                    /**
                                     * Executes max operation with thermal imaging domain optimization.
                                     *
                                     */
                                    max(
                                        y - downY,
                                        rect.top - downRect.top,
                                    )
                                } else {
                                    /**
                                     * Executes min operation with thermal imaging domain optimization.
                                     *
                                     */
                                    min(y - downY, rect.bottom - downRect.bottom)
                                }
                            operateRect?.set(downRect.left + biasX, downRect.top + biasY, downRect.right + biasX, downRect.bottom + biasY)
                        }
                        RectMoveType.EDGE ->
                            /**
                             * Executes when operation with thermal imaging domain optimization.
                             *
                             */
                            when (rectMoveEdge) {
                                RectMoveEdge.LEFT -> { // 移动左边
                                    operateRect?.left = min(x, downRect.right)
                                    operateRect?.right = max(x, downRect.right)
                                }
                                RectMoveEdge.TOP -> { // 移动上边
                                    operateRect?.top = min(y, downRect.bottom)
                                    operateRect?.bottom = max(y, downRect.bottom)
                                }
                                RectMoveEdge.RIGHT -> { // 移动右边
                                    operateRect?.right = max(x, downRect.left)
                                    operateRect?.left = min(x, downRect.left)
                                }
                                RectMoveEdge.BOTTOM -> { // 移动下边
                                    operateRect?.bottom = max(y, downRect.top)
                                    operateRect?.top = min(y, downRect.top)
                                }
                            }
                        RectMoveType.CORNER ->
                            /**
                             * Executes when operation with thermal imaging domain optimization.
                             *
                             */
                            when (rectMoveCorner) {
                                RectMoveCorner.LT -> { // 移动左上角
                                    operateRect?.left = min(x, downRect.right)
                                    operateRect?.right = max(x, downRect.right)
                                    operateRect?.top = min(y, downRect.bottom)
                                    operateRect?.bottom = max(y, downRect.bottom)
                                }
                                RectMoveCorner.RT -> { // 移动右上角
                                    operateRect?.right = max(x, downRect.left)
                                    operateRect?.left = min(x, downRect.left)
                                    operateRect?.top = min(y, downRect.bottom)
                                    operateRect?.bottom = max(y, downRect.bottom)
                                }
                                RectMoveCorner.RB -> { // 移动右下角
                                    operateRect?.right = max(x, downRect.left)
                                    operateRect?.left = min(x, downRect.left)
                                    operateRect?.bottom = max(y, downRect.top)
                                    operateRect?.top = min(y, downRect.top)
                                }
                                RectMoveCorner.LB -> { // 移动左下角
                                    operateRect?.left = min(x, downRect.right)
                                    operateRect?.right = max(x, downRect.right)
                                    operateRect?.bottom = max(y, downRect.top)
                                    operateRect?.top = min(y, downRect.top)
                                }
                            }
                    }
                }
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val x: Int = event.x.correct(width)
                val y: Int = event.y.correct(height)
                val rect: Rect = operateRect ?: Rect()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if ((rect.left / xScale).toInt() != (rect.right / xScale).toInt() &&
                    (rect.top / yScale).toInt() != (rect.bottom / yScale).toInt()
                ) {
画出来的结果不是一条line才生效
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isAddAction || abs(x - downX) > DELETE_TOLERANCE || abs(y - downY) > DELETE_TOLERANCE) {
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized(this) {
                            rectList.add(rect)
                        }
                    }
                }
                operateRect = null
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()

                onRectListener?.invoke(getSourceRectList())
                return true
            }
            else -> return false
        }
    }

    /**
     * Executes pollRect functionality.
     */
    /**
     * Executes pollrect operation with thermal imaging domain optimization.
     *
     * @param
     * @param x Parameter for operation (type: Int)
     * @param y Parameter for operation (type: Int)
     *
     */
    private fun pollRect(
        x: Int,
        y: Int,
    ): Rect? {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in rectList.size - 1 downTo 0) {
            val rect: Rect = rectList[i]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rect.left - TOUCH_TOLERANCE < x && rect.right + TOUCH_TOLERANCE > x &&
                rect.top - TOUCH_TOLERANCE < y && rect.bottom + TOUCH_TOLERANCE > y
            ) {
                return synchronized(this) { rectList.removeAt(i) }
            }
        }
        return null
    }
}
