package com.topdon.module.thermal.activity

import android.graphics.Color
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.elvishew.xlog.XLog
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.ToastTools
import com.topdon.module.thermal.R
import com.topdon.module.thermal.adapter.SettingTimeAdapter
import com.topdon.module.thermal.chart.MyValueFormatter
import com.topdon.module.thermal.view.MyMarkerView
import com.topdon.module.thermal.viewmodel.LogViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing LogMPChartActivity functionality for the IRCamera system.
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
class LogMPChartActivity : BaseActivity(), OnChartValueSelectedListener {
    private val viewModel: LogViewModel by viewModels()

    private val adapter: SettingTimeAdapter by lazy { SettingTimeAdapter(this) }

    // Private var dataList: ArrayList<ThermalEntity> = arrayListOf()
    private lateinit var chart: LineChart
    private var selectType = 1

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_log_mp_chart

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Set toolbar title
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(com.topdon.lib.core.R.id.toolbar_lay)
        toolbar?.title = getString(R.string.app_record)

        chart = findViewById(R.id.log_chart_time_chart)
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.log_chart_time_recycler)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = adapter
        adapter.listener =
            object : SettingTimeAdapter.OnItemClickListener {
                /**
                 * Executes onclick operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param index Parameter for operation (type: Int)
                 * @param time Parameter for operation (type: Int)
                 *
                 */
                override fun onClick(
                    index: Int,
                    time: Int,
                ) {
switchtype
                    chart.highlightValue(null) // Close高亮pointMarker
                    selectType = index + 1
                    /**
                     * Executes querylog operation with thermal imaging domain optimization.
                     *
                     */
                    queryLog()
                }
            }
        viewModel.resultLiveData.observe(this) {
            /**
             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
             *
             */
            dismissLoadingDialog()
            try {
                /**
                 * Initializes the entry component for thermal imaging operations.
                 *
                 */
                initEntry(it.dataList)
            } catch (e: Exception) {
                XLog.e("refresh图表exception:${e.message}")
                ToastTools.showShort("图表exception，请重新load")
            }
        }
        /**
         * Executes clearentity operation with thermal imaging domain optimization.
         *
         */
        clearEntity(true)
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
        /**
         * Executes querylog operation with thermal imaging domain optimization.
         *
         */
        queryLog()
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * Executes onpause operation with thermal imaging domain optimization.
     *
     */
    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * Executes queryLog functionality.
     */
    /**
     * Executes querylog operation with thermal imaging domain optimization.
     *
     */
    private fun queryLog() {
        /**
         * Executes showloadingdialog operation with thermal imaging domain optimization.
         *
         */
        showLoadingDialog()
// ViewModel.queryLogByType(selectType)
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.queryLogVolsByStartTime(
                type = 1, // Default fence type since getSelectFenceType() is not available
                selectTimeType = selectType,
            )
        }
    }

    /**
     * Initializes chart component.
     */
    private fun initChart() {
        chart.clear()
        chart.setOnChartValueSelectedListener(this)
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setDrawGridBackground(false)
        chart.description = null // 图标描述文本
        chart.setBackgroundResource(com.topdon.lib.core.R.color.chart_bg)
        chart.setScaleEnabled(true) // Scale
        chart.setPinchZoom(false) // Disable后，可以分别在xaxis和yaxis上进行Scale
        chart.isDoubleTapToZoomEnabled = false // 双击不可Scale
        chart.isScaleYEnabled = false // 禁止YaxisScale
        chart.setExtraOffsets(
            0f,
            0f,
            SizeUtils.dp2px(8f).toFloat(),
            SizeUtils.dp2px(4f).toFloat(),
        ) // 图表region偏移
        chart.setNoDataText(getString(R.string.lms_http_code998))
        chart.setNoDataTextColor(textColor)
        val mv = MyMarkerView(this, R.layout.marker_lay)
        mv.chartView = chart
        chart.marker = mv // SettingsclickcoordinateShow/Displaytip框
        val data = LineData()
        data.setValueTextColor(textColor)
        chart.data = data
        val l = chart.legend
        l.form = Legend.LegendForm.CIRCLE
        l.textColor = textColor
        l.isEnabled = false // Hide曲linetag
        val xAxis = chart.xAxis
        xAxis.textColor = textColor
        xAxis.setDrawGridLines(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.isEnabled = true
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = textColor
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true // 重复值不Show/Display
        xAxis.textSize = 9f
        val leftAxis = chart.axisLeft
        leftAxis.textSize = 9f
        leftAxis.textColor = textColor
        leftAxis.setDrawGridLines(true)
        leftAxis.setLabelCount(6, false) // 固定x刻度
        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
// Chart.zoom(10f, 1f, chart.xChartMax, 0f)
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
            com.topdon.lib.core.R.color.chart_line_max,
            com.topdon.lib.core.R.color.chart_line_min,
            com.topdon.lib.core.R.color.chart_line_center,
        )
    private val textColor by lazy { ContextCompat.getColor(this, com.topdon.lib.core.R.color.chart_text) }

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
// Set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.mode = LineDataSet.Mode.LINEAR
        set.setDrawFilled(false)
        set.fillDrawable = ContextCompat.getDrawable(this, bgChartColors[index]) // Settings填充颜色渐变
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.color = ContextCompat.getColor(this, lineChartColors[index]) // 曲line颜色
        set.setCircleColor(ContextCompat.getColor(this, com.topdon.lib.core.R.color.white)) // Coordinate颜色
// Set.fillColor = ContextCompat.getColor(this, R.color.purple_500)
// Set.highLightColor = ContextCompat.getColor(this, R.color.white)
        set.valueTextColor = Color.WHITE
        set.lineWidth = 2f
        set.circleRadius = 1f // 不Show/Displaycoordinatepoint
        set.setCircleColor(ContextCompat.getColor(this, lineChartColors[index])) // Coordinate颜色(Hideprocessing)
set.setCircleColor(ContextCompat.getColor(this, R.color.white))// Coordinate颜色(hideprocessing)
        set.fillAlpha = 200
        set.valueTextSize = 10f
        set.setDrawValues(false) // Settings是否Show/Displaycoordinate值文本
        return set
    }

    /**
     * Initializes entry component.
     */
    private fun initEntry(data: ArrayList<ThermalEntity>) {
        synchronized(chart) {
            lifecycleScope.launch(Dispatchers.IO) {
                /**
                 * Executes clearentity operation with thermal imaging domain optimization.
                 *
                 */
                clearEntity(data.size == 0)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (data.size == 0) {
                    return@launch
                }
                Log.i("chart", "update chart start")
                val lineData: LineData? = chart.data
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (lineData != null) {
                    Log.w(
                        "123",
                        "时间区间:${(data.last().createTime - data.first().createTime) / 1000}",
                    )
                    val startTime = data[0].createTime
                    Log.w("123", "settings初始时间startTime:$startTime")
                    chart.xAxis.valueFormatter =
                        /**
                         * Executes myvalueformatter operation with thermal imaging domain optimization.
                         *
                         */
                        MyValueFormatter(startTime = startTime, type = selectType)
                    XLog.w("chart init startTime:$startTime")
                    data[0].type = "default"
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
                                set = createSet(2, "temp")
                                lineData.addDataSet(set)
                            }
                            data.forEach {
                                val x = (it.createTime - startTime).toFloat()
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
                                maxDataSet = createSet(0, "line maxTemp")
                                lineData.addDataSet(maxDataSet)
                            }

                            var minDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (minDataSet == null) {
                                minDataSet = createSet(1, "line minTemp")
                                lineData.addDataSet(minDataSet)
                            }
                            Log.w("123", "两条曲line")
                            data.forEach {
                                val x = (it.createTime - startTime).toFloat()
                                // Max
                                val entity = Entry(x, it.thermalMax)
                                entity.data = it
                                maxDataSet.addEntry(entity)
                                // Min
                                val entityMin = Entry(x, it.thermalMin)
                                entityMin.data = it
                                minDataSet.addEntry(entityMin)
                            }
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
                                maxTempDataSet = createSet(0, "fence maxTemp")
                                lineData.addDataSet(maxTempDataSet)
                            }
                            // Center
                            var centerTempDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (centerTempDataSet == null) {
                                centerTempDataSet = createSet(1, "fence minTemp")
                                lineData.addDataSet(centerTempDataSet)
                            }
                            data.forEach {
                                val x = (it.createTime - startTime).toFloat()
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
                    chart.notifyDataSetChanged()
                    chart.setVisibleXRangeMinimum(getMinimum()) // SettingsShow/DisplayXaxis区间大小
                    chart.setVisibleXRangeMaximum(getMaximum()) // SettingsShow/DisplayXaxis区间大小
                    chart.xAxis.setLabelCount(5, false) // True保证有刻度数量不变
                    chart.moveViewToX(chart.xChartMax) // 移动到最右端
                    chart.zoom(1f, 1f, chart.xChartMax, 0f) // Default无Scale，全部Show/Display
                }
                Log.w("chart", "update chart finish")
            }
        }
    }

    /**
     * Executes onvalueselected operation with thermal imaging domain optimization.
     *
     * @param
     * @param e Parameter for operation (type: Entry?)
     * @param h Parameter for operation (type: Highlight?)
     *
     */
    override fun onValueSelected(
        e: Entry?,
        h: Highlight?,
    ) {
    }

    /**
     * Executes onnothingselected operation with thermal imaging domain optimization.
     *
     */
    override fun onNothingSelected() {
    }

    /**
xaxisdisplay多少个刻度
     */
    /**
     * Retrieves the labcount with optimized performance for thermal imaging operations.
     *
     * @param
     * @param count Parameter for operation (type: Int)
     *
     */
    private fun getLabCount(count: Int): Int {
        return when (count) {
            in 0..2 -> 1
            in 3..4 -> 2
            in 5..6 -> 3
            in 7..9 -> 4
            else -> 5
        }
    }

getdisplay最小区间
    /**
     * Retrieves minimum information.
     */
    private fun getMinimum(): Float {
        val min =
            when (selectType) {
                1 -> 1 * 10 * 1000f // 10s
                2 -> 10 * 60 * 1000f // 10min
                3 -> 10 * 60 * 60 * 1000f // 10hour
                4 -> 10 * 24 * 60 * 60 * 1000f // 10day
                else -> 1 * 10 * 1000f // 10s
            }
        return min
    }

getdisplay最大区间，以最小区间的50倍
    /**
     * Retrieves maximum information.
     */
    private fun getMaximum(): Float {
        return getMinimum() * 50f
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
            chart.clear()
        } else {
            chart.clearValues()
        }
    }
}
