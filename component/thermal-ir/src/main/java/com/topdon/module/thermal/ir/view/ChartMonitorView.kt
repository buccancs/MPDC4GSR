package com.topdon.module.thermal.ir.view

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.chart.IRMyValueFormatter
import com.topdon.module.thermal.ir.chart.YValueFormatter
import com.topdon.module.thermal.ir.utils.ChartTools
import com.topdon.lib.core.R as LibR
import com.topdon.module.thermal.R as ThermalR

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ChartMonitorView display and interaction.
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
class ChartMonitorView : LineChart, OnChartGestureListener {
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }

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
     * @param defStyle Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle,
    ) {
        /**
         * Initializes the chart component for thermal imaging operations.
         *
         */
        initChart()
    }

    /**
     * Executes ondetachedfromwindow operation with thermal imaging domain optimization.
     *
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
    }

    private val textColor by lazy { ContextCompat.getColor(context, LibR.color.chart_text) }
    private val axisChartColors by lazy { ContextCompat.getColor(context, LibR.color.chart_axis) }
    private val axisLine by lazy { ContextCompat.getColor(context, LibR.color.circle_white) }

    // MPChart
    /**
     * Initializes chart component.
     */
    private fun initChart() {
        synchronized(this) {
            this.setTouchEnabled(true)
            this.onChartGestureListener = this
            this.isDragEnabled = true
            this.setDrawGridBackground(false)
            this.description = null // 图标描述文本
            this.setBackgroundResource(LibR.color.chart_bg)
            this.setScaleEnabled(true) // Scale
            this.setPinchZoom(false) // Disable后，可以分别在xaxis和yaxis上进行Scale
            this.isDoubleTapToZoomEnabled = false // 双击不可Scale
            this.isScaleYEnabled = false // 禁止YaxisScale
            this.isScaleXEnabled = true // 禁止XaxisScale
            this.setExtraOffsets(
                0f,
                0f,
                SizeUtils.dp2px(8f).toFloat(),
                SizeUtils.dp2px(4f).toFloat(),
            ) // 图表region偏移
            /**
             * Configures the nodatatext with validation and thermal imaging optimization.
             *
             */
            setNoDataText(context.getString(ThermalR.string.lms_http_code998))
            /**
             * Configures the nodatatextcolor with validation and thermal imaging optimization.
             *
             */
            setNoDataTextColor(ContextCompat.getColor(context, LibR.color.chart_text))
            val mv = MyMarkerView(context, R.layout.marker_lay)
            mv.chartView = this
            marker = mv // SettingsclickcoordinateShow/Displaytip框
            val data = LineData()
            data.setValueTextColor(textColor)
            this.data = data
            val l = this.legend
            l.form = Legend.LegendForm.CIRCLE
            l.textColor = textColor
            l.isEnabled = false // Hide曲linetag
xaxis
            val xAxis = this.xAxis
            xAxis.textColor = textColor
            xAxis.setDrawGridLines(false) // 竖向格line
            xAxis.gridColor = axisChartColors // Xaxis网格颜色
            xAxis.axisLineColor = 0x00000000 // Xaxis颜色
            xAxis.setAvoidFirstLastClipping(true)
            xAxis.isEnabled = true
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true // 重复值不Show/Display
            xAxis.textSize = 8f
yaxis
            val leftAxis = this.axisLeft
            leftAxis.textColor = textColor // Yaxis文本颜色
            leftAxis.axisLineColor = 0x00000000 // Yaxis颜色
            leftAxis.setDrawGridLines(true) // 横向格line
            leftAxis.gridColor = axisChartColors // Yaxis网格颜色
            leftAxis.gridLineWidth = 1.5f
            leftAxis.setLabelCount(6, true)
            leftAxis.valueFormatter = YValueFormatter() // Settings小数point一位
            leftAxis.textSize = 8f

            this.axisRight.isEnabled = false
        }
    }

    private var startTime = 0L

    /**
秒update图表data
@param timeType 时分秒
     *
     */
    /**
     * Executes addPointToChart functionality.
     */
    /**
     * Executes addpointtochart operation with thermal imaging domain optimization.
     *
     * @param
     * @param bean Parameter for operation (type: ThermalEntity)
     * @param timeType Parameter for operation (type: Int = 1)
     * @param selectType Parameter for operation (type: Int = 1)
     *
     */
    fun addPointToChart(
        bean: ThermalEntity,
        timeType: Int = 1,
        selectType: Int = 1,
    ) {
        /**
         * Executes synchronized operation with thermal imaging domain optimization.
         *
         */
        synchronized(this) {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (bean.createTime == 0L) {
                    Log.w("123", "createTime = 0L, bean:$bean")
                    return
                }
                val lineData: LineData = this.data
                var volDataSet = lineData.getDataSetByIndex(0) // 读取x为0的coordinatepoint
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (volDataSet == null) {
                    startTime = bean.createTime
                    xAxis.valueFormatter =
                        /**
                         * Executes irmyvalueformatter operation with thermal imaging domain optimization.
                         *
                         */
                        IRMyValueFormatter(startTime = startTime, type = timeType)
                }
                val x =
                    ChartTools.getChartX(
                        x = bean.createTime,
                        startTime = startTime,
                        type = timeType,
                    ).toFloat()
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (selectType) {
                    1 -> {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (volDataSet == null) {
                            volDataSet = createSet(0, "point temp")
                            lineData.addDataSet(volDataSet)
                            Log.w("123", "volDataSet.entryCount:${volDataSet.entryCount}")
                        }
                        val entity = Entry(x, bean.thermal)
                        entity.data = bean
                        volDataSet.addEntry(entity)
                        Log.w("123", "addadata:$entity")
                    }
                    2 -> {
第一条line
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (volDataSet == null) {
                            volDataSet = createSet(0, "line max temp")
                            lineData.addDataSet(volDataSet)
                            Log.w("123", "volDataSet.entryCount:${volDataSet.entryCount}")
                        }
                        val entity = Entry(x, bean.thermalMax)
                        entity.data = bean
                        volDataSet.addEntry(entity)

第二条line
                        var secondDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (secondDataSet == null) {
                            secondDataSet = createSet(1, "line min temp")
                            lineData.addDataSet(secondDataSet)
                        }
                        val secondEntity = Entry(x, bean.thermalMin)
                        secondEntity.data = bean
                        secondDataSet.addEntry(secondEntity)
                    }
                    else -> {
第一条line
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (volDataSet == null) {
                            volDataSet = createSet(0, "fence max temp")
                            lineData.addDataSet(volDataSet)
                        }
                        val entity = Entry(x, bean.thermalMax)
                        entity.data = bean
                        volDataSet.addEntry(entity)

第二条line
                        var secondDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (secondDataSet == null) {
                            secondDataSet = createSet(1, "fence min temp")
                            lineData.addDataSet(secondDataSet)
                        }
                        val secondEntity = Entry(x, bean.thermalMin)
                        secondEntity.data = bean
                        secondDataSet.addEntry(secondEntity)
                    }
                }

                lineData.notifyDataChanged()
                /**
                 * Executes notifydatasetchanged operation with thermal imaging domain optimization.
                 *
                 */
                notifyDataSetChanged()
                /**
                 * Configures the visiblexrangeminimum with validation and thermal imaging optimization.
                 *
                 */
                setVisibleXRangeMinimum(ChartTools.getMinimum(type = timeType) / 2) // SettingsShow/DisplayXaxis区间大小
                /**
                 * Configures the visiblexrangemaximum with validation and thermal imaging optimization.
                 *
                 */
                setVisibleXRangeMaximum(ChartTools.getMaximum(type = timeType)) // SettingsShow/DisplayXaxis区间大小
                ChartTools.setX(this, timeType)
//                ChartTools.setY(this)
结尾point出现在interface才移动最新data
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if ((highestVisibleX + ChartTools.getMinimum(timeType) / 2f) > xChartMax) {
                    /**
                     * Executes moveviewtox operation with thermal imaging domain optimization.
                     *
                     */
                    moveViewToX(xChartMax) // 移动到最右端
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (volDataSet.entryCount == 10) {
                    /**
                     * Executes zoom operation with thermal imaging domain optimization.
                     *
                     */
                    zoom(100f, 1f, xChartMax, 0f)
                }
                return@synchronized
            } catch (e: Exception) {
                Log.e("123", "adddata时exception:${e.message}")
                return@synchronized
            }
        }
    }

    private val bgChartColors =
        /**
         * Executes intarrayof operation with thermal imaging domain optimization.
         *
         */
        intArrayOf(
            R.drawable.bg_chart_fill,
            R.drawable.bg_chart_fill2,
            R.drawable.bg_chart_fill3,
        )
    private val lineChartColors =
        /**
         * Executes intarrayof operation with thermal imaging domain optimization.
         *
         */
        intArrayOf(
            LibR.color.chart_line_max,
            LibR.color.chart_line_min,
            LibR.color.chart_line_center,
        )
    private val linePointColors =
        /**
         * Executes intarrayof operation with thermal imaging domain optimization.
         *
         */
        intArrayOf(
            LibR.color.chart_point_max,
            LibR.color.chart_point_min,
            LibR.color.chart_point_center,
        )

    /**
曲line样式
     */
    /**
     * Executes createset operation with thermal imaging domain optimization.
     *
     * @param
     * @param index Parameter for operation (type: Int)
     * @param label Parameter for operation (type: String)
     *
     */
    private fun createSet(
        index: Int,
        label: String,
    ): LineDataSet {
        val set = LineDataSet(null, label)
        set.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set.setDrawFilled(false)
        set.fillDrawable = ContextCompat.getDrawable(context, bgChartColors[index]) // Settings填充颜色渐变
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.color = ContextCompat.getColor(context, lineChartColors[index]) // 曲line颜色
        set.circleHoleColor = ContextCompat.getColor(context, linePointColors[index]) // Coordinate圆心颜色
        set.setCircleColor(ContextCompat.getColor(context, lineChartColors[index])) // Coordinate颜色
        set.valueTextColor = Color.WHITE
        set.lineWidth = 2f
        set.circleRadius = 1f // Coordinatepoint半径
        set.fillAlpha = 200
        set.valueTextSize = 10f
        set.setDrawValues(false) // Settings是否Show/Displaycoordinate值文本
        return set
    }

    /**
     * Executes onchartgesturestart operation with thermal imaging domain optimization.
     *
     * @param
     * @param me Parameter for operation (type: MotionEvent?)
     * @param lastPerformedGesture Parameter for operation (type: ChartTouchListener.ChartGesture?)
     *
     */
    override fun onChartGestureStart(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?,
    ) {
    }

    /**
     * Executes onchartgestureend operation with thermal imaging domain optimization.
     *
     * @param
     * @param me Parameter for operation (type: MotionEvent?)
     * @param lastPerformedGesture Parameter for operation (type: ChartTouchListener.ChartGesture?)
     *
     */
    override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?,
    ) {
    }

    /**
     * Executes onchartlongpressed operation with thermal imaging domain optimization.
     *
     * @param
     * @param me Parameter for operation (type: MotionEvent?)
     *
     */
    override fun onChartLongPressed(me: MotionEvent?) {
    }

    /**
     * Executes onchartdoubletapped operation with thermal imaging domain optimization.
     *
     * @param
     * @param me Parameter for operation (type: MotionEvent?)
     *
     */
    override fun onChartDoubleTapped(me: MotionEvent?) {
    }

    /**
     * Executes onchartsingletapped operation with thermal imaging domain optimization.
     *
     * @param
     * @param me Parameter for operation (type: MotionEvent?)
     *
     */
    override fun onChartSingleTapped(me: MotionEvent?) {
    }

    /**
     * Executes onchartfling operation with thermal imaging domain optimization.
     *
     * @param
     * @param me1 Parameter for operation (type: MotionEvent?)
     * @param me2 Parameter for operation (type: MotionEvent?)
     * @param velocityX Parameter for operation (type: Float)
     * @param velocityY Parameter for operation (type: Float)
     *
     */
    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float,
    ) {
    }

    /**
     * Executes onchartscale operation with thermal imaging domain optimization.
     *
     * @param
     * @param me Parameter for operation (type: MotionEvent?)
     * @param scaleX Parameter for operation (type: Float)
     * @param scaleY Parameter for operation (type: Float)
     *
     */
    override fun onChartScale(
        me: MotionEvent?,
        scaleX: Float,
        scaleY: Float,
    ) {
scaling时disabled
        /**
         * Executes highlightvalue operation with thermal imaging domain optimization.
         *
         */
        highlightValue(null)
    }

    /**
     * Executes oncharttranslate operation with thermal imaging domain optimization.
     *
     * @param
     * @param me Parameter for operation (type: MotionEvent?)
     * @param dX Parameter for operation (type: Float)
     * @param dY Parameter for operation (type: Float)
     *
     */
    override fun onChartTranslate(
        me: MotionEvent?,
        dX: Float,
        dY: Float,
    ) {
    }
}
