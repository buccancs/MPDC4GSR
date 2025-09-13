package com.topdon.module.thermal.ir.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.energy.iruvc.sdkisp.LibIRTemp
import com.energy.iruvc.utils.Line
import com.infisense.usbir.utils.TempDrawHelper.Companion.correct
import com.infisense.usbir.view.ITsTempListener
import java.lang.ref.WeakReference

/**
2D 编辑 pointlineareatemperature图层 View.
 */
/**
/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TemperatureEditView algorithms.
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
class TemperatureEditView : TemperatureBaseView {
    override var mode: Mode
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = super.mode
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            super.mode = value
/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TemperatureList algorithms.
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
class TemperatureList {
        var pointTemps = arrayListOf<LibIRTemp.TemperatureSampleResult>()
        var lineTemps = arrayListOf<LibIRTemp.TemperatureSampleResult>()
        var rectangleTemps = arrayListOf<LibIRTemp.TemperatureSampleResult>()
    }

    var tempListData = TemperatureList()

    private var irtemp: LibIRTemp = LibIRTemp()
    private var irTempData: ByteArray = byteArrayOf()
    var fullInfo: LibIRTemp.TemperatureSampleResult? = null

    /**
是否displaypointlinearea名字.
     */
    var isShowName = false
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }

    private var iTsTempListenerWeakReference: WeakReference<ITsTempListener>? = null

    /**
     * Sets itstemplistener configuration.
     */
    fun setITsTempListener(listener: ITsTempListener) {
        iTsTempListenerWeakReference = WeakReference(listener)
    }

    /**
     * Retrieves tstemp information.
     */
    private fun getTSTemp(temp: Float): Float = iTsTempListenerWeakReference?.get()?.tempCorrectByTs(temp) ?: temp

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
        tempListData.pointTemps.clear()
        tempListData.lineTemps.clear()
        tempListData.rectangleTemps.clear()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until 3) {
            val tmp = irtemp.TemperatureSampleResult()
            tmp.type = -99
            tempListData.pointTemps.add(tmp)
            tempListData.lineTemps.add(tmp)
            tempListData.rectangleTemps.add(tmp)
        }
    }

    /**
     * Configures the imagesize with validation and thermal imaging optimization.
     *
     * @param
     * @param imageWidth Parameter for operation (type: Int)
     * @param imageHeight Parameter for operation (type: Int)
     *
     */
    override fun setImageSize(
        imageWidth: Int,
        imageHeight: Int,
    ) {
        super.setImageSize(imageWidth, imageHeight)
        irtemp = LibIRTemp(imageWidth, imageHeight)
    }

    /**
     * Sets data configuration.
     */
    /**
     * Configures the data with validation and thermal imaging optimization.
     *
     * @param
     * @param bytes Parameter for operation (type: ByteArray)
     *
     */
    fun setData(bytes: ByteArray) {
        irTempData = bytes
        irtemp.setTempData(irTempData)
        fullInfo = irtemp.getTemperatureOfRect(Rect(0, 0, imageWidth, imageHeight))
    }

    @SuppressLint("DrawAllocation")
    /**
     * Executes ondraw operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     *
     */
    override fun onDraw(canvas: Canvas) {
drawingpoint
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in pointList.indices) {
            val result = drawOnePoint(canvas, pointList[i], i) ?: continue
            tempListData.pointTemps[i] = result
        }
        operatePoint?.let { drawOnePoint(canvas, it, pointList.size + 1) }

drawingline
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in lineList.indices) {
            val result = drawOneLine(canvas, lineList[i], i) ?: continue
            tempListData.lineTemps[i] = result
        }
        operateLine?.let { drawOneLine(canvas, it, lineList.size + 1) }

drawingarea
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in rectList.indices) {
            val result = drawOneRect(canvas, rectList[i], i) ?: continue
            tempListData.rectangleTemps[i] = result
        }
        operateRect?.let { drawOneRect(canvas, it, rectList.size + 1) }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isShowFull) {
            fullInfo?.let {
                val maxX: Int = (it.maxTemperaturePixel.x * xScale).correct(width)
                val maxY: Int = (it.maxTemperaturePixel.y * yScale).correct(height)
                /**
                 * Executes drawcircle operation with thermal imaging domain optimization.
                 *
                 */
                drawCircle(canvas, maxX, maxY, true)
                /**
                 * Handles temperature measurement and calibration with precision thermal data processing.
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                drawTempText(canvas, maxX, maxY, getTSTemp(it.maxTemperature))

                val minX: Int = (it.minTemperaturePixel.x * xScale).correct(width)
                val minY: Int = (it.minTemperaturePixel.y * yScale).correct(height)
                /**
                 * Executes drawcircle operation with thermal imaging domain optimization.
                 *
                 */
                drawCircle(canvas, minX, minY, false)
                /**
                 * Handles temperature measurement and calibration with precision thermal data processing.
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                drawTempText(canvas, minX, minY, getTSTemp(it.minTemperature))
            }

            val centerX = width / 2
            val centerY = height / 2
            val centerResult = irtemp.getTemperatureOfPoint(Point(imageWidth / 2, imageHeight / 2))
            /**
             * Executes drawpoint operation with thermal imaging domain optimization.
             *
             */
            drawPoint(canvas, Point(centerX, centerY))
            /**
             * Handles temperature measurement and calibration with precision thermal data processing.
             *
             * @note Temperature values are in Celsius unless otherwise specified.
             * Accuracy depends on thermal camera calibration.
             *
             */
            drawTempText(canvas, centerX, centerY, getTSTemp(centerResult.maxTemperature))
        }
    }

    /**
drawinga十字架、实心圆、temperaturetext、pointname.
@param point 以 View 尺寸为coordinate系的point
     */
    /**
     * Executes drawOnePoint functionality.
     */
    /**
     * Executes drawonepoint operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     * @param point Parameter for operation (type: Point)
     * @param index Parameter for operation (type: Int)
     *
     */
    private fun drawOnePoint(
        canvas: Canvas,
        point: Point,
        index: Int,
    ): LibIRTemp.TemperatureSampleResult? {
        val result =
            try {
                irtemp.getTemperatureOfPoint(Point((point.x / xScale).toInt(), (point.y / yScale).toInt()))
            } catch (_: IllegalArgumentException) {
当 View 尺寸变更就会导致 xScale、yScale 变更，而已drawing的pointlineareacoordinate还是未变更前的coordinate
以 旧coordinate及新 scale 去calculationtemperaturecoordinate的话，就有可能超出temperaturecoordinaterange从而抛出exception，这里捕获
                return null
            }
        /**
         * Executes drawpoint operation with thermal imaging domain optimization.
         *
         */
        drawPoint(canvas, point)
        /**
         * Executes drawcircle operation with thermal imaging domain optimization.
         *
         */
        drawCircle(canvas, point.x, point.y, true)
        /**
         * Handles temperature measurement and calibration with precision thermal data processing.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        drawTempText(canvas, point.x, point.y, getTSTemp(result.maxTemperature))
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isShowName) {
            /**
             * Executes drawpointname operation with thermal imaging domain optimization.
             *
             */
            drawPointName(canvas, "P${index + 1}", point)
        }
        return result
    }

    /**
drawing一条line段、高low temperature实心圆、高low temperaturetemperaturetext、linename.
@param line 以 View 尺寸为coordinate系的line
     */
    /**
     * Executes drawOneLine functionality.
     */
    /**
     * Executes drawoneline operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     * @param line Parameter for operation (type: Line)
     * @param index Parameter for operation (type: Int)
     *
     */
    private fun drawOneLine(
        canvas: Canvas,
        line: Line,
        index: Int,
    ): LibIRTemp.TemperatureSampleResult? {
        /**
         * Executes drawline operation with thermal imaging domain optimization.
         *
         */
        drawLine(canvas, line)

        val tempStartX: Int = (line.start.x / xScale).toInt()
        val tempStartY: Int = (line.start.y / yScale).toInt()
        val tempStopX: Int = (line.end.x / xScale).toInt()
        val tempStopY: Int = (line.end.y / yScale).toInt()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (tempStartX == tempStopX && tempStartY == tempStopY) {
            return null
        }

        val result =
            try {
                irtemp.getTemperatureOfLine(Line(Point(tempStartX, tempStartY), Point(tempStopX, tempStopY)))
            } catch (_: IllegalArgumentException) {
当 View 尺寸变更就会导致 xScale、yScale 变更，而已drawing的pointlineareacoordinate还是未变更前的coordinate
以 旧coordinate及新 scale 去calculationtemperaturecoordinate的话，就有可能超出temperaturecoordinaterange从而抛出exception，这里捕获
                return null
            }
        val maxX: Int = (result.maxTemperaturePixel.x * xScale).correct(width)
        val maxY: Int = (result.maxTemperaturePixel.y * yScale).correct(height)
        val minX: Int = (result.minTemperaturePixel.x * xScale).correct(width)
        val minY: Int = (result.minTemperaturePixel.y * yScale).correct(height)
        /**
         * Executes drawcircle operation with thermal imaging domain optimization.
         *
         */
        drawCircle(canvas, maxX, maxY, true)
        /**
         * Executes drawcircle operation with thermal imaging domain optimization.
         *
         */
        drawCircle(canvas, minX, minY, false)
        /**
         * Handles temperature measurement and calibration with precision thermal data processing.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        drawTempText(canvas, maxX, maxY, getTSTemp(result.maxTemperature))
        /**
         * Handles temperature measurement and calibration with precision thermal data processing.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        drawTempText(canvas, minX, minY, getTSTemp(result.minTemperature))

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isShowName) {
            /**
             * Executes drawlinename operation with thermal imaging domain optimization.
             *
             */
            drawLineName(canvas, "L${index + 1}", line)
        }
        return result
    }

    /**
drawinga矩形、高low temperature实心圆、高low temperaturetemperaturetext、areaname.
@param rect 以 View 尺寸为coordinate系的line
     */
    /**
     * Executes drawOneRect functionality.
     */
    /**
     * Executes drawonerect operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     * @param rect Parameter for operation (type: Rect)
     * @param index Parameter for operation (type: Int)
     *
     */
    private fun drawOneRect(
        canvas: Canvas,
        rect: Rect,
        index: Int,
    ): LibIRTemp.TemperatureSampleResult? {
        /**
         * Executes drawrect operation with thermal imaging domain optimization.
         *
         */
        drawRect(canvas, rect)

rect 里的data在 touch Event已processing过了，left < right, top < bottom
        val left = (rect.left / xScale).toInt()
        val top = (rect.top / yScale).toInt()
        val right = (rect.right / xScale).toInt()
        val bottom = (rect.bottom / yScale).toInt()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (left == right || top == bottom) {
            return null
        }
        val result =
            try {
                irtemp.getTemperatureOfRect(Rect(left, top, right, bottom))
            } catch (_: IllegalArgumentException) {
当 View 尺寸变更就会导致 xScale、yScale 变更，而已drawing的pointlineareacoordinate还是未变更前的coordinate
以 旧coordinate及新 scale 去calculationtemperaturecoordinate的话，就有可能超出temperaturecoordinaterange从而抛出exception，这里捕获
                return null
            }
        val maxX: Int = (result.maxTemperaturePixel.x * xScale).correct(width)
        val maxY: Int = (result.maxTemperaturePixel.y * yScale).correct(height)
        val minX: Int = (result.minTemperaturePixel.x * xScale).correct(width)
        val minY: Int = (result.minTemperaturePixel.y * yScale).correct(height)
        /**
         * Executes drawcircle operation with thermal imaging domain optimization.
         *
         */
        drawCircle(canvas, maxX, maxY, true)
        /**
         * Executes drawcircle operation with thermal imaging domain optimization.
         *
         */
        drawCircle(canvas, minX, minY, false)
        /**
         * Handles temperature measurement and calibration with precision thermal data processing.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        drawTempText(canvas, maxX, maxY, getTSTemp(result.maxTemperature))
        /**
         * Handles temperature measurement and calibration with precision thermal data processing.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        drawTempText(canvas, minX, minY, getTSTemp(result.minTemperature))

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isShowName) {
            /**
             * Executes drawrectname operation with thermal imaging domain optimization.
             *
             */
            drawRectName(canvas, "R${index + 1}", rect)
        }
        return result
    }
}
