package com.topdon.module.thermal.activity

import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
// Import com.guide.zm04c.matrix.GuideInterface // Temporarily disabled - hardware specific
import com.topdon.lib.core.bean.tools.ThermalBean
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.db.AppDatabase
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.tools.NumberTools
import com.topdon.module.thermal.R
import com.topdon.module.thermal.adapter.SettingCheckAdapter
import com.topdon.module.thermal.adapter.SettingTimeAdapter
import com.topdon.module.thermal.chart.MyValueFormatter
import com.topdon.module.thermal.view.MyMarkerView
import com.topdon.module.thermal.viewmodel.LogViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
temperature监控
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
/**
 * Specialized thermal imaging component providing MonitorChartActivity functionality for the IRCamera system.
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
class MonitorChartActivity : BaseActivity(), View.OnClickListener, OnChartValueSelectedListener {
    private val viewModel: LogViewModel by viewModels()

    private val timeAdapter: SettingTimeAdapter by lazy { SettingTimeAdapter(this) } // 时分秒
    private val adapter: SettingCheckAdapter by lazy { SettingCheckAdapter(this) } // 时间间隔

    // Var MONITOR_ACTION = STATS_START
    private var selectDuration = 1
    private var selectType = 1 // 选取point 1:单point    2:line条    3:region
    private var selectIndex: ArrayList<Int> = arrayListOf() // 选取point
    private val bean = ThermalBean()
    private var selectTimeType = 1
    private var latestTime = 0L // Record当前图表最新时间戳,用于判断是否refresh(分, 时, 天)电压data
    private var startMonitor = false

    private lateinit var chart: LineChart

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_monitor_chart

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Set toolbar title
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(com.topdon.lib.core.R.id.toolbar_lay)
        toolbar?.title = getString(R.string.main_thermal_motion)

        selectType = intent.getIntExtra("type", 3)
        selectIndex = intent.getIntegerArrayListExtra("select")!!
        Log.w("123", "selectType:$selectType")
        Log.w("123", "selectIndex:${selectIndex.joinToString()}")
        // SharedManager.setSelectFenceType(selectType) // Temporarily disabled
        type =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (selectType) {
                1 -> "point"
                2 -> "line"
                else -> "fence"
            }
        chart = findViewById(R.id.mp_chart_view)
        /**
         * Initializes the chart component for thermal imaging operations.
         *
         */
        initChart()
        /**
         * Initializes the recycler component for thermal imaging operations.
         *
         */
        initRecycler()
        viewModel.resultLiveData.observe(this) {
查询到历史data
            Log.w("123", "查询到历史data:${it.dataList.size}")
            /**
             * Executes resultvol operation with thermal imaging domain optimization.
             *
             */
            resultVol(it)
        }
        lifecycleScope.launch {
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(300)
            /**
             * Executes onirvideostart operation with thermal imaging domain optimization.
             *
             */
            onIrVideoStart()
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
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
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        /**
         * Executes onirvideostop operation with thermal imaging domain optimization.
         *
         */
        onIrVideoStop()
    }

    /**
     * Executes onclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param v Parameter for operation (type: View?)
     *
     */
    override fun onClick(v: View?) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (v) {
        }
    }

    /**
     * Initializes recycler component.
     */
    private fun initRecycler() {
        val monitorChartTimeRecycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.monitor_chart_time_recycler)
        val monitorChartSettingRecycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.monitor_chart_setting_recycler)

        monitorChartTimeRecycler.layoutManager = GridLayoutManager(this, 4)
        monitorChartTimeRecycler.adapter = timeAdapter
        monitorChartSettingRecycler.layoutManager = GridLayoutManager(this, 3)
        monitorChartSettingRecycler.adapter = adapter
/**
 * Configures the 时间段type with validation and thermal imaging optimization.
 *
 */
set时间段type(秒 分 时 天)
        timeAdapter.listener =
            object : SettingTimeAdapter.OnItemClickListener {
                /**
                 * Executes onclick operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param index Parameter for operation (type: Int)
                 * @param timeType Parameter for operation (type: Int)
                 *
                 */
                override fun onClick(
                    index: Int,
                    timeType: Int,
                ) {
                    selectTimeType = timeType
                    chart.highlightValue(null) // Close高亮pointMarker
                    latestTime = 0L
                    /**
                     * Executes showloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    showLoadingDialog()
                    lifecycleScope.launch {
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(500)
                        /**
                         * Executes querylog operation with thermal imaging domain optimization.
                         *
                         */
                        queryLog(2)
                    }
                }
            }
时间间隔
        adapter.listener =
            object : SettingCheckAdapter.OnItemClickListener {
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
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (recordTask != null && recordTask!!.isActive) {
                        recordTask!!.cancel()
                        recordTask = null
                    }
// CanUpdate = false
                    Log.w("123", "select:$time")
                    adapter.setCheck(index)
                    timeMillis = time * 1000L
                    pointIndex = startIndex - defaultCount
                    /**
                     * Executes recordthermal operation with thermal imaging domain optimization.
                     *
                     */
                    recordThermal() // StartRecord
                }
            }
    }

    val defaultCount = 20 // DefaultShow/Display10个数
    val startIndex = 0f
    var pointIndex = startIndex - defaultCount

    // // /// /// /
    var mIsIrVideoStart = false

    // Private var mGuideInterface: GuideInterface? = null // Temporarily disabled - hardware specific
    var rotateType = 3

    /**
enabledvideo流
     */
    /**
     * Executes onirvideostart operation with thermal imaging domain optimization.
     *
     */
    private fun onIrVideoStart() {
        // Temporarily disabled - guide interface not available
        /*
        mIsIrVideoStart = if (mIsIrVideoStart) {
            ToastUtils.showShort("video流已开启")
            return
        } else {
            true
        }
        mGuideInterface = GuideInterface()
        val ret = mGuideInterface!!.init(this, object : GuideInterface.IrDataCallback {
            /**
             * Executes processirdata operation with thermal imaging domain optimization.
             *
             * @param
             * @param yuv Parameter for operation (type: ByteArray)
             * @param temp Temperature value in Celsius (type: FloatArray)
             *
             */
            override fun processIrData(yuv: ByteArray, temp: FloatArray) {
                try {
选取region
                    val centerTempIndex: Int = 256 * (192 / 2) + 256 / 2
                    val maxTempIndex = ArrayUtils.getMaxIndex(temp, rotateType, selectIndex)
                    val minTempIndex = ArrayUtils.getMinIndex(temp, rotateType, selectIndex)
                    val rotateData = ArrayUtils.matrixRotate(srcData = temp, rotateType)
                    val bigDecimal = BigDecimal.valueOf(rotateData[centerTempIndex].toDouble())
                    val maxBigDecimal = BigDecimal.valueOf(rotateData[maxTempIndex].toDouble())
                    val minBigDecimal = BigDecimal.valueOf(rotateData[minTempIndex].toDouble())
                    bean.centerTemp = bigDecimal.setScale(1, RoundingMode.HALF_UP).toFloat()
                    bean.maxTemp = maxBigDecimal.setScale(1, RoundingMode.HALF_UP).toFloat()
                    bean.minTemp = minBigDecimal.setScale(1, RoundingMode.HALF_UP).toFloat()
                    bean.createTime = System.currentTimeMillis()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "提取temperatureexception:${e.message}")
                }
            }

        })

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ret == 5) {
            Log.w("123", "video流开启complete")
            /**
             * Executes recordthermal operation with thermal imaging domain optimization.
             *
             */
            recordThermal()// StartRecord
        } else {
ToastUtils.showShort("video流enabledfailed")
            Log.w("123", "video流开启failed")
            mGuideInterface = null
            mIsIrVideoStart = false
        }
         */
    }

    /**
stopvideo流
     */
    /**
     * Executes onirvideostop operation with thermal imaging domain optimization.
     *
     */
    private fun onIrVideoStop() {
        // Temporarily disabled - guide interface not available
        /*
        mIsIrVideoStart = if (!mIsIrVideoStart) {
            Log.w("123", "video流已stop")
            return
        } else {
            false
        }
        mGuideInterface!!.exit()
        mGuideInterface = null
        Log.w("123", "video流stopcomplete")
         */
    }

    var isRecord = false
    var type = ""
    var timeMillis = 1000L // 间隔1s
    var canUpdate = false
    var recordTask: Job? = null
    var thermalId = TimeTool.showDateSecond()
    var startTime = 0L

    /**
循环Listener-datasave
     */
    /**
     * Executes recordthermal operation with thermal imaging domain optimization.
     *
     */
    private fun recordThermal() {
        recordTask =
            lifecycleScope.launch(Dispatchers.IO) {
                isRecord = true
                startTime = System.currentTimeMillis()
                var time = 0L
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (isRecord) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (canUpdate) {
                        val entity = ThermalEntity()
                        entity.userId = SharedManager.getUserId()
                        entity.thermalId = thermalId
                        entity.thermal = NumberTools.to02f(bean.centerTemp)
                        entity.thermalMax = NumberTools.to02f(bean.maxTemp)
                        entity.thermalMin = NumberTools.to02f(bean.minTemp)
                        entity.type = type
                        entity.startTime = startTime
                        entity.createTime = System.currentTimeMillis()
                        AppDatabase.getInstance().thermalDao().insert(entity)
                        time++
                        /**
                         * Executes launch operation with thermal imaging domain optimization.
                         *
                         */
                        launch(Dispatchers.Main) {
                            /**
                             * Executes updatechart operation with thermal imaging domain optimization.
                             *
                             */
                            updateChart()
                        }
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(timeMillis)
                    } else {
                        Log.w("123", "当前不可update")
                    }
                }
                Log.w("123", "stopRecord, data量:$time")
            }
    }

    // MPChart
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
xAxis.setLabelCount(6, true)// True保证有刻度数量不变
        xAxis.setLabelCount(6, false) // True保证有刻度数量不变
        val leftAxis = chart.axisLeft
        leftAxis.textSize = 9f
        leftAxis.textColor = textColor
        leftAxis.setDrawGridLines(true)
        leftAxis.setLabelCount(6, true)
        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
        chart.zoom(100f, 1f, chart.xChartMax, 0f)
        selectDuration = adapter.selectTime
        startTime = System.currentTimeMillis()
        canUpdate = true // 可以startupdateRecord
    }

    /**
分classprocessingupdate图表data
     */
    /**
     * Executes updatechart operation with thermal imaging domain optimization.
     *
     */
    private fun updateChart() {
        ++pointIndex
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (selectTimeType) {
            1 -> {
秒
                /**
                 * Executes addpointtochart operation with thermal imaging domain optimization.
                 *
                 */
                addPointToChart(bean)
            }
            2 -> {
分
                val addTime = 2 * 60 * 1000L
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (bean.createTime > TimeTool.timeToMinute(latestTime, 2) + addTime) {
                    /**
                     * Executes querylog operation with thermal imaging domain optimization.
                     *
                     */
                    queryLog(3)
                }
            }
            3 -> {
时
                val addTime = 2 * 60 * 60 * 1000L
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (bean.createTime > TimeTool.timeToMinute(latestTime, 3) + addTime) {
                    /**
                     * Executes querylog operation with thermal imaging domain optimization.
                     *
                     */
                    queryLog(3)
                }
            }
            4 -> {
/**
 * Executes 天 operation with thermal imaging domain optimization.
 *
 */
天(图表display最后a时间在昨天，要多加一天)
                val addTime = 2 * 24 * 60 * 60 * 1000L
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (bean.createTime > TimeTool.timeToMinute(latestTime, 4) + addTime) {
                    /**
                     * Executes querylog operation with thermal imaging domain optimization.
                     *
                     */
                    queryLog(3)
                }
            }
        }
    }

    /**
秒update图表data
     */
    /**
     * Executes addpointtochart operation with thermal imaging domain optimization.
     *
     * @param
     * @param bean Parameter for operation (type: ThermalBean)
     *
     */
    private fun addPointToChart(bean: ThermalBean) {
        /**
         * Executes synchronized operation with thermal imaging domain optimization.
         *
         */
        synchronized(chart) {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (bean.createTime == 0L) {
                    Log.w("123", "createTime = 0L, bean:$bean")
                    return
                }
                val data = ThermalEntity()
                data.thermalMax = bean.maxTemp
                data.thermalMin = bean.minTemp
                data.thermal = bean.centerTemp
                data.createTime = bean.createTime
                val lineData: LineData = chart.data
                var volDataSet = lineData.getDataSetByIndex(0) // 读取x为0的coordinatepoint
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (volDataSet == null) {
                    startTime = data.createTime
                    Log.w("123", "settings初始时间startTime:$startTime")
                    chart.xAxis.valueFormatter = MyValueFormatter(startTime = startTime)
                }
                val x = (data.createTime - startTime).toFloat()
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (type) {
                    "point" -> {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (volDataSet == null) {
                            volDataSet = createSet("green")
                            lineData.addDataSet(volDataSet)
                            Log.w("123", "volDataSet.entryCount:${volDataSet.entryCount}")
                        }
                        val entity = Entry(x, data.thermal)
                        entity.data = data
                        volDataSet.addEntry(entity)
                        Log.w("123", "addadata:$entity")
                    }
                    "line" -> {
第一条line
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (volDataSet == null) {
                            volDataSet = createSet("red")
                            lineData.addDataSet(volDataSet)
                            Log.w("123", "volDataSet.entryCount:${volDataSet.entryCount}")
                        }
                        val entity = Entry(x, data.thermalMax)
                        entity.data = data
                        volDataSet.addEntry(entity)

第二条line
                        var secondDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (secondDataSet == null) {
                            secondDataSet = createSet("blue")
                            lineData.addDataSet(secondDataSet)
                        }
                        val secondEntity = Entry(x, data.thermalMin)
                        secondEntity.data = data
                        secondDataSet.addEntry(secondEntity)
                    }
                    else -> {
第一条line
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (volDataSet == null) {
                            volDataSet = createSet("red")
                            lineData.addDataSet(volDataSet)
                        }
                        val entity = Entry(x, data.thermalMax)
                        entity.data = data
                        volDataSet.addEntry(entity)

第二条line
                        var secondDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (secondDataSet == null) {
                            secondDataSet = createSet("blue")
                            lineData.addDataSet(secondDataSet)
                        }
                        val secondEntity = Entry(x, data.thermalMin)
                        secondEntity.data = data
                        secondDataSet.addEntry(secondEntity)
                    }
                }

                lineData.notifyDataChanged()
                chart.notifyDataSetChanged()
                chart.setVisibleXRangeMinimum(getMinimum()) // SettingsShow/DisplayXaxis区间大小
                chart.setVisibleXRangeMaximum(getMaximum()) // SettingsShow/DisplayXaxis区间大小
                chart.xAxis.setLabelCount(getLabCount(volDataSet.entryCount), false) // True保证有刻度数量不变
                chart.moveViewToX(chart.xChartMax) // 移动到最右端
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (volDataSet.entryCount == 20) {
                    chart.zoom(100f, 1f, chart.xChartMax, 0f)
                }
                return@synchronized
            } catch (e: Exception) {
                Log.e("123", "adddata时exception:${e.message}")
                return@synchronized
            }
        }
    }

    private val fillColor by lazy { ContextCompat.getDrawable(this, R.drawable.bg_chart_fill2) }
    private val lineRed by lazy { ContextCompat.getColor(this, com.topdon.lib.core.R.color.chart_line_max) }
    private val lineBlue by lazy { ContextCompat.getColor(this, com.topdon.lib.core.R.color.chart_line_min) }
    private val lineGreen by lazy { ContextCompat.getColor(this, com.topdon.lib.core.R.color.chart_line_center) }
    private val whiteColors by lazy { ContextCompat.getColor(this, com.topdon.lib.core.R.color.circle_white) }
    private val textColor by lazy { ContextCompat.getColor(this, com.topdon.lib.core.R.color.chart_text) }

    /**
曲line样式
     */
    /**
     * Executes createset operation with thermal imaging domain optimization.
     *
     * @param
     * @param label Parameter for operation (type: String)
     *
     */
    private fun createSet(label: String): LineDataSet {
        val set = LineDataSet(null, label)
// Set.mode = LineDataSet.Mode.LINEAR
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawFilled(false)
set.fillDrawable = fillColor// Set填充颜色渐变
        set.axisDependency = YAxis.AxisDependency.LEFT

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (label) {
            "red" -> {
                set.color = lineRed // 曲line颜色
                set.circleHoleColor = lineRed // Coordinate内部颜色
            }
            "blue" -> {
                set.color = lineBlue // 曲line颜色
                set.circleHoleColor = lineBlue // Coordinate内部颜色
            }
            else -> {
                set.color = lineGreen // 曲line颜色
                set.circleHoleColor = lineGreen // Coordinate内部颜色
            }
        }

        set.setCircleColor(whiteColors) // Coordinate颜色
        set.circleHoleRadius = 4f // Coordinatepoint内部半径
        set.circleRadius = 5f // Coordinatepoint外部半径
        set.valueTextColor = Color.WHITE
        set.lineWidth = 2f
        set.fillAlpha = 200
        set.valueTextSize = 10f
        set.setDrawValues(false) // Settings是否Show/Displaycoordinate值文本
        set.isHighlightEnabled = true // 允许辅助line
        set.setDrawHorizontalHighlightIndicator(false) // 水平辅助lineClose
        set.enableDashedHighlightLine(8f, 8f, 0f) // 辅助虚line
        return set
    }

    /**
/**
 * Executes 查询历史电压data operation with thermal imaging domain optimization.
 *
 */
查询历史电压data(等待bluetooth传输历史Recordend后触发)
时间区间: 现在时间 => 倒退到startEvent
     *
     * @param action
0: 初始查询
1: refresh查询
2: switch查询
3: Listener查询
4: load历史data后查询
     */
    /**
     * Executes queryLog functionality.
     */
    /**
     * Executes querylog operation with thermal imaging domain optimization.
     *
     * @param
     * @param action Parameter for operation (type: Int)
     *
     */
    private fun queryLog(action: Int) {
        startMonitor = false
        lifecycleScope.launch(Dispatchers.IO) {
dataList.clear()// Cleardata
// DataList = arrayListOf()
            viewModel.queryLogThermals(selectTimeType = selectTimeType, action = action)
        }
    }

    /**
     * Executes resultVol functionality.
     */
    /**
     * Executes resultvol operation with thermal imaging domain optimization.
     *
     * @param
     * @param bean Parameter for operation (type: LogViewModel.ChartList)
     *
     */
    private fun resultVol(bean: LogViewModel.ChartList) {
        /**
         * Executes dismissloadingdialog operation with thermal imaging domain optimization.
         *
         */
        dismissLoadingDialog()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectTimeType != 1 && bean.dataList.size > 0) {
            val logTime = TimeTool.showDateType(bean.dataList.last().createTime, selectTimeType)
            val nowTime = TimeTool.showDateType(System.currentTimeMillis(), selectTimeType)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (TextUtils.equals(logTime, nowTime)) {
分时天,当前时间段没end，应当delete最新当前时间段data
                bean.dataList.removeLast()
            }
        }
// DataList = bean.dataList
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (latestTime == 0L) {
图表无data需要update
            /**
             * Executes addentity operation with thermal imaging domain optimization.
             *
             */
            addEntity(bean.dataList)
        } else if (bean.dataList.size > 0 && latestTime < bean.dataList.last().createTime) {
有新data再update
            /**
             * Executes addentity operation with thermal imaging domain optimization.
             *
             */
            addEntity(bean.dataList)
        }
    }

整体refresh
    /**
     * Executes addEntity functionality.
     */
    /**
     * Executes addentity operation with thermal imaging domain optimization.
     *
     * @param
     * @param data Parameter for operation (type: ArrayList<ThermalEntity>)
     *
     */
    private fun addEntity(data: ArrayList<ThermalEntity>) {
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
            return
        }
        latestTime = data[data.size - 1].createTime
        startTime = data[0].createTime
        chart.xAxis.valueFormatter = MyValueFormatter(startTime = startTime)
        val lineData: LineData = chart.data
        var volDataSet = lineData.getDataSetByIndex(0) // 读取x为0的coordinatepoint
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (volDataSet == null) {
            volDataSet = createSet("vol")
            lineData.addDataSet(volDataSet)
        }
        chart.xAxis.valueFormatter = MyValueFormatter(startTime = startTime, type = selectTimeType)
        val mv = MyMarkerView(this, R.layout.marker_lay)
        mv.chartView = chart
        chart.marker = mv // SettingsclickcoordinateShow/Displaytip框
        data.forEach {
            val x = (it.createTime - startTime).toFloat()
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (type) {
                "point" -> {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (volDataSet == null) {
                        volDataSet = createSet("green")
                        lineData.addDataSet(volDataSet)
                        Log.w("123", "volDataSet.entryCount:${volDataSet.entryCount}")
                    }
                    val entity = Entry(x, it.thermal)
                    entity.data = it
                    volDataSet.addEntry(entity)
                    Log.w("123", "addadata:$entity")
                }
                "line" -> {
第一条line
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (volDataSet == null) {
                        volDataSet = createSet("red")
                        lineData.addDataSet(volDataSet)
                        Log.w("123", "volDataSet.entryCount:${volDataSet.entryCount}")
                    }
                    val entity = Entry(x, it.thermalMax)
                    entity.data = it
                    volDataSet.addEntry(entity)

第二条line
                    var secondDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (secondDataSet == null) {
                        secondDataSet = createSet("blue")
                        lineData.addDataSet(secondDataSet)
                    }
                    val secondEntity = Entry(x, it.thermalMin)
                    secondEntity.data = it
                    secondDataSet.addEntry(secondEntity)
                }
                else -> {
第一条line
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (volDataSet == null) {
                        volDataSet = createSet("red")
                        lineData.addDataSet(volDataSet)
                    }
                    val entity = Entry(x, it.thermalMax)
                    entity.data = it
                    volDataSet.addEntry(entity)

第二条line
                    var secondDataSet = lineData.getDataSetByIndex(1) // 读取x为0的coordinatepoint
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (secondDataSet == null) {
                        secondDataSet = createSet("blue")
                        lineData.addDataSet(secondDataSet)
                    }
                    val secondEntity = Entry(x, it.thermalMax)
                    secondEntity.data = it
                    secondDataSet.addEntry(secondEntity)
                }
            }
        }
        Log.w("123", "曲linedata:${volDataSet.entryCount}个")
        lineData.notifyDataChanged()
        chart.notifyDataSetChanged()
        chart.setVisibleXRangeMinimum(getMinimum()) // SettingsShow/DisplayXaxis区间大小
        chart.setVisibleXRangeMaximum(getMaximum()) // SettingsShow/DisplayXaxis区间大小
        Log.i(
            "123",
            "list moveViewToX:${chart.xChartMax}, chart.highestVisibleX:${chart.highestVisibleX}",
        )
        chart.moveViewToX(chart.xChartMax) // 移动到最右端
        chart.xAxis.setLabelCount(getLabCount(volDataSet.entryCount), false) // True保证有刻度数量不变
        chart.zoom(100f, 1f, chart.xChartMax, 0f)
        startMonitor = true
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
            when (selectTimeType) {
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
}
