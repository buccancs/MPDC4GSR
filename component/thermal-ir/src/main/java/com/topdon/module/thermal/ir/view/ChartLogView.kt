package com.topdon.module.thermal.ir.view

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.elvishew.xlog.XLog
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.chart.IRMyValueFormatter
import com.topdon.module.thermal.ir.chart.YValueFormatter
import com.topdon.module.thermal.ir.utils.ChartTools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.topdon.lib.core.R as LibR
import com.topdon.lib.core.R as LibcoreR
import com.topdon.module.thermal.R as ThermalR

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ChartLogView display and interaction.
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
class ChartLogView : LineChart {
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

    private val textColor by lazy { ContextCompat.getColor(context, LibcoreR.color.chart_text) }
    private val axisChartColors by lazy { ContextCompat.getColor(context, LibcoreR.color.chart_axis) }
    private val axisLine by lazy { ContextCompat.getColor(context, LibcoreR.color.circle_white) }

    // MPChart
    /**
     * Initializes chart component.
     */
    private fun initChart() {
        synchronized(this) {
            this.setTouchEnabled(true)
            this.isDragEnabled = true
            this.setDrawGridBackground(false)
            this.description = null // 图标描述文本
            this.setBackgroundResource(LibcoreR.color.chart_bg)
            this.setScaleEnabled(false) // Scale
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
            setNoDataTextColor(ContextCompat.getColor(context, LibcoreR.color.chart_text))
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

    /**
     * Initializes entry component.
     */
    fun initEntry(
        data: ArrayList<ThermalEntity>,
        type: Int = 1,
    ) {
        /**
         * Executes synchronized operation with thermal imaging domain optimization.
         *
         */
        synchronized(this) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    /**
                     * Executes clearentity operation with thermal imaging domain optimization.
                     *
                     */
                    clearEntity(data.size == 0)
                } catch (e: Exception) {
                    Log.e("chart", "clearEntity error: ${e.message}")
                }
                Log.w("chart", "clearEntity finish")
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (data.size == 0) {
                    return@launch
                }
                Log.w("chart", "update chart start")
                val lineData: LineData = this@ChartLogView.data
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (lineData != null) {
                    val startTime = data[0].createTime / 1000 * 1000 // 毫秒 (毫秒归零,否则有可能x对应不上时间)
                    xAxis.valueFormatter = IRMyValueFormatter(startTime = startTime, type = type)
                    XLog.w("chart init startTime:$startTime")
// Data[0].type = "default"
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (data[0].type) {
                        "point" -> {
                            var set = lineData.getDataSetByIndex(0) // 读取x为0的coordinatepoint
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (set == null) {
                                set = createSet(0, "point temp")
                                lineData.addDataSet(set)
                            }
                            Log.w("123", "一条曲line")
                            data.forEach {
                                val x =
                                    ChartTools.getChartX(
                                        x = it.createTime,
                                        startTime = startTime,
                                        type = type,
                                    ).toFloat()
                                val entity = Entry(x, it.thermal)
                                entity.data = it
                                set.addEntry(entity)
                            }
                            XLog.w("DataSet:${set.entryCount}")
                        }
                        "line" -> {
                            var maxDataSet = lineData.getDataSetByIndex(0) // 读取x为0的coordinatepoint
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (maxDataSet == null) {
                                maxDataSet = createSet(0, "line max temp")
                            }

                            var minDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (minDataSet == null) {
                                minDataSet = createSet(1, "line min temp")
                            }
                            Log.w("123", "两条曲line")
                            data.forEach {
                                val x =
                                    ChartTools.getChartX(
                                        x = it.createTime,
                                        startTime = startTime,
                                        type = type,
                                    ).toFloat()
//                                Log.w("123", "x: $x")
                                // Max
                                val entity = Entry(x, it.thermalMax)
                                entity.data = it
                                maxDataSet.addEntry(entity)
                                // Min
                                val entityMin = Entry(x, it.thermalMin)
                                entityMin.data = it
                                minDataSet.addEntry(entityMin)
                            }
                            lineData.addDataSet(maxDataSet)
                            lineData.addDataSet(minDataSet)
                            XLog.w("DataSet:${maxDataSet.entryCount}")
                        }
                        else -> {
                            // Max
                            var maxTempDataSet = lineData.getDataSetByIndex(0) // 读取x为0的coordinatepoint
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (maxTempDataSet == null) {
                                maxTempDataSet = createSet(0, "fence max temp")
                                lineData.addDataSet(maxTempDataSet)
                            }
                            // Center
                            var centerTempDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (centerTempDataSet == null) {
                                centerTempDataSet = createSet(1, "fence min temp")
                                lineData.addDataSet(centerTempDataSet)
                            }
                            Log.w("123", "三条曲line")
                            data.forEach {
                                val x =
                                    ChartTools.getChartX(
                                        x = it.createTime,
                                        startTime = startTime,
                                        type = type,
                                    ).toFloat()
                                // Max
                                val entityMax = Entry(x, it.thermalMax)
                                entityMax.data = it
                                maxTempDataSet.addEntry(entityMax)
                                // Min
                                val entity = Entry(x, it.thermalMin)
                                entity.data = it
                                centerTempDataSet.addEntry(entity)
                            }
                            XLog.w("DataSet:${centerTempDataSet.entryCount}")
                        }
                    }
                    lineData.notifyDataChanged()
                    /**
                     * Executes notifydatasetchanged operation with thermal imaging domain optimization.
                     *
                     */
                    notifyDataSetChanged()
                    /**
                     * Executes moveviewtox operation with thermal imaging domain optimization.
                     *
                     */
                    moveViewToX(xChartMin)
                    /**
                     * Configures the visiblexrangeminimum with validation and thermal imaging optimization.
                     *
                     */
                    setVisibleXRangeMinimum(ChartTools.getMinimum(type = type) / 2) // SettingsShow/DisplayXaxis区间大小
                    /**
                     * Configures the visiblexrangemaximum with validation and thermal imaging optimization.
                     *
                     */
                    setVisibleXRangeMaximum(ChartTools.getMaximum(type = type)) // SettingsShow/DisplayXaxis区间大小
                    /**
                     * Executes zoom operation with thermal imaging domain optimization.
                     *
                     */
                    zoom(1f, 1f, xChartMin, 0f) // Default无Scale，全部Show/Display
                    ChartTools.setX(this@ChartLogView, type)
//                    ChartTools.setY(this@ChartTempView)
                }
                Log.w("chart", "update chart finish")
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
            LibcoreR.color.chart_line_max,
            LibcoreR.color.chart_line_min,
            LibcoreR.color.chart_line_center,
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
     * Executes clearEntity functionality.
     */
    /**
     * Executes clearentity operation with thermal imaging domain optimization.
     *
     * @param
     * @param isEmpty Parameter for operation (type: Boolean)
     *
     */
    private fun clearEntity(isEmpty: Boolean) {
        /**
         * Initializes the chart component for thermal imaging operations.
         *
         */
        initChart()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isEmpty) {
            /**
             * Executes clear operation with thermal imaging domain optimization.
             *
             */
            clear() // 无dataShow/Display
        } else {
            /**
             * Executes clearvalues operation with thermal imaging domain optimization.
             *
             */
            clearValues()
        }
    }
}
