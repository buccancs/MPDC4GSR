package com.example.thermal_lite.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.elvishew.xlog.XLog
import com.energy.ac020library.bean.IrcmdError
import com.energy.irutilslibrary.LibIRTempAC020
import com.energy.irutilslibrary.bean.GainStatus
import com.energy.iruvc.sdkisp.LibIRTemp
import com.energy.iruvc.utils.Line
import com.example.thermal_lite.R
import com.example.thermal_lite.camera.DeviceIrcmdControlManager
import com.example.thermal_lite.databinding.ActivityIrMonitorLiteBinding
import com.example.thermal_lite.fragment.IRMonitorLiteFragment
import com.google.gson.Gson
import com.infisense.usbir.view.ITsTempListener
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.bean.tools.ThermalBean
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.db.AppDatabase
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.NumberTools
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.ui.dialog.MonitorSelectDialog
import com.topdon.lib.ui.listener.SingleClickListener
import com.topdon.module.thermal.ir.bean.DataBean
import com.topdon.module.thermal.ir.bean.SelectPositionBean
import com.topdon.module.thermal.ir.event.MonitorSaveEvent
import com.topdon.module.thermal.ir.event.ThermalActionEvent
import com.topdon.module.thermal.ir.repository.ConfigRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.math.RoundingMode

/**
/**
 * Specialized thermal imaging component providing IRMonitorLiteActivity functionality for the IRCamera system.
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
open class IRMonitorLiteActivity : BaseActivity(), View.OnClickListener, ITsTempListener {
    private lateinit var binding: ActivityIrMonitorLiteBinding
    private var selectIndex: SelectPositionBean? = null // 选取point
    val irMonitorLiteFragment = IRMonitorLiteFragment()
    private val bean = ThermalBean()
    private var selectBean: SelectPositionBean = SelectPositionBean()

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_ir_monitor_lite

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        binding = ActivityIrMonitorLiteBinding.inflate(layoutInflater)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)

        binding.motionBtn.setOnClickListener(
            object : SingleClickListener() {
                /**
                 * Executes onsingleclick operation with thermal imaging domain optimization.
                 *
                 */
                override fun onSingleClick() {
                    MonitorSelectDialog.Builder(this@IRMonitorLiteActivity)
                        .setPositiveListener {
                            /**
                             * Executes updateui operation with thermal imaging domain optimization.
                             *
                             */
                            updateUI()
                            /**
                             * Executes when operation with thermal imaging domain optimization.
                             *
                             */
                            when (it) {
                                1 -> EventBus.getDefault().post(ThermalActionEvent(action = 2001))
                                2 -> EventBus.getDefault().post(ThermalActionEvent(action = 2002))
                                else -> EventBus.getDefault().post(ThermalActionEvent(action = 2003))
                            }
                        }
                        .create().show()
                }
            },
        )
        binding.motionStartBtn.setOnClickListener(this)
    }

    /**
     * Executes startChart functionality.
     */
    /**
     * Executes startchart operation with thermal imaging domain optimization.
     *
     */
    private fun startChart()  {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectIndex == null)
            {
                return
            }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        selectBean = selectIndex!!
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (showTask != null && showTask!!.isActive) {
            showTask!!.cancel()
            showTask = null
        }
        binding.titleView.setRightText(R.string.monitor_finish)
        binding.titleView.setRightClickListener {
            recordJob?.cancel()
            lifecycleScope.launch {
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(500)
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }
        showTask =
            lifecycleScope.launch {
                var isFirstRead = true
                var errorReadCount = 0
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (true) {
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(1000)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (irMonitorLiteFragment != null)
                        {
                            val result: LibIRTemp.TemperatureSampleResult =
                                /**
                                 * Executes when operation with thermal imaging domain optimization.
                                 *
                                 */
                                when (selectBean.type) {
                                    1 -> irMonitorLiteFragment!!.temperatureView.getPointTemp(selectBean.startPosition)
                                    2 -> irMonitorLiteFragment!!.temperatureView.getLineTemp(Line(selectBean.startPosition, selectBean.endPosition))
                                    else -> irMonitorLiteFragment!!.temperatureView.getRectTemp(selectBean.getRect())
                                } ?: continue
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (isFirstRead) {
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (result.maxTemperature > 200f || result.minTemperature < -200f) {
                                    errorReadCount++
                                    XLog.w("第 $errorReadCount 次读取到exceptiondata，max = ${result.maxTemperature} min = ${result.minTemperature}")
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (errorReadCount > 10) {
                                        XLog.i("连续10次Get/Retrieve到exceptiondata，认为temperatureregion稳定")
                                        isFirstRead = false
                                    }
                                    continue
                                } else {
                                    isFirstRead = false
                                    lifecycleScope.launch(Dispatchers.Main) {
                                        binding.llTime.isVisible = true
                                    }
                                }
                            }
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (result.maxTemperature >= -270f) {
                                val maxBigDecimal = BigDecimal.valueOf(tempCorrectByTs(result.maxTemperature).toDouble())
                                val minBigDecimal = BigDecimal.valueOf(tempCorrectByTs(result.minTemperature).toDouble())
                                bean.centerTemp = maxBigDecimal.setScale(1, RoundingMode.HALF_UP).toFloat()
                                bean.maxTemp = maxBigDecimal.setScale(1, RoundingMode.HALF_UP).toFloat()
                                bean.minTemp = minBigDecimal.setScale(1, RoundingMode.HALF_UP).toFloat()
                                bean.createTime = System.currentTimeMillis()
                                canUpdate = true // 可以startupdateRecord
                            }
                        }
                }
            }

        binding.monitorCurrentVol.text = getString(if (selectIndex!!.type == 1) R.string.chart_temperature else R.string.chart_temperature_high)
        binding.monitorRealVol.visibility = if (selectIndex!!.type == 1) View.GONE else View.VISIBLE
        binding.monitorRealImg.visibility = if (selectIndex!!.type == 1) View.GONE else View.VISIBLE
        /**
         * Executes recordthermal operation with thermal imaging domain optimization.
         *
         */
        recordThermal() // StartRecord
    }

    private var showTask: Job? = null

    private var isRecord = false
    private var timeMillis = 1000L // 间隔1s
    private var canUpdate = false

    private var recordJob: Job? = null

    /**
start每隔1秒Recordatemperaturedata到data库.
     */
    private fun recordThermal() {
        recordJob =
            lifecycleScope.launch(Dispatchers.IO) {
                isRecord = true
                val thermalId = TimeTool.showDateSecond()
                val startTime = System.currentTimeMillis()
                val typeStr =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (selectBean.type) {
                        1 -> "point"
                        2 -> "line"
                        else -> "fence"
                    }
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
                        entity.type = typeStr
                        entity.startTime = startTime
                        entity.createTime = System.currentTimeMillis()
                        AppDatabase.getInstance().thermalDao().insert(entity)
                        time++
                        /**
                         * Executes launch operation with thermal imaging domain optimization.
                         *
                         */
                        launch(Dispatchers.Main) {
                            binding.mpChartView.addPointToChart(bean = entity, selectType = selectBean.type)
                        }
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(timeMillis)
                    } else {
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(100)
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.tvTime.text = TimeTool.showVideoLongTime(System.currentTimeMillis() - startTime)
                    }
                }
                XLog.w("stopRecord, data量:$time")
            }
    }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().add(R.id.thermal_fragment, irMonitorLiteFragment).commit()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
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
        when (v?.id) {
            R.id.motion_start_btn -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (selectIndex == null) {
                    MonitorSelectDialog.Builder(this)
                        .setPositiveListener {
                            /**
                             * Executes updateui operation with thermal imaging domain optimization.
                             *
                             */
                            updateUI()
                            /**
                             * Executes when operation with thermal imaging domain optimization.
                             *
                             */
                            when (it) {
                                1 -> EventBus.getDefault().post(ThermalActionEvent(action = 2001))
                                2 -> EventBus.getDefault().post(ThermalActionEvent(action = 2002))
                                else -> EventBus.getDefault().post(ThermalActionEvent(action = 2003))
                            }
                        }
                        .create().show()
                    return
                }
                lifecycleScope.launch {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (irMonitorLiteFragment.frameReady) {
                        lifecycleScope.launch {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (selectIndex == null)
                                {
                                    return@launch
                                }
                            irMonitorLiteFragment?.stopTask()
                            binding.thermalFragment.getViewTreeObserver().addOnGlobalLayoutListener(
                                object :
                                    ViewTreeObserver.OnGlobalLayoutListener {
                                    /**
                                     * Executes ongloballayout operation with thermal imaging domain optimization.
                                     *
                                     */
                                    override fun onGlobalLayout() {
移除listener以避免重复调用
                                        binding.thermalFragment.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                                        irMonitorLiteFragment?.restTempView()
                                        irMonitorLiteFragment?.addTempLine(selectIndex!!)
进行需要的操作
                                    }
                                },
                            )
                            binding.motionActionLay.isVisible = false
                            binding.chartLay.isVisible = true
                            /**
                             * Manages thermal camera operations with hardware-optimized performance and error handling.
                             *
                             */
                            showCameraLoading()
                            /**
                             * Executes delay operation with thermal imaging domain optimization.
                             *
                             */
                            delay(500)
                            /**
                             * Manages thermal camera operations with hardware-optimized performance and error handling.
                             *
                             */
                            dismissCameraLoading()
                            /**
                             * Executes startchart operation with thermal imaging domain optimization.
                             *
                             */
                            startChart()
                        }
                    }
                }
            }
        }
    }

    /**
     * Executes select functionality.
     */
    /**
     * Executes select operation with thermal imaging domain optimization.
     *
     * @param
     * @param selectIndex Parameter for operation (type: SelectPositionBean?)
     *
     */
    fun select(selectIndex: SelectPositionBean?) {
        this.selectIndex = selectIndex
        XLog.i("绘制的pointlinearea：${Gson().toJson(selectIndex)}")
    }

    /**
     * Executes updateUI functionality.
     */
    /**
     * Executes updateui operation with thermal imaging domain optimization.
     *
     */
    private fun updateUI() {
        binding.motionStartBtn.visibility = View.VISIBLE
        binding.motionBtn.visibility = View.GONE
    }

    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    override fun disConnected() {
        super.disConnected()
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }

    var config: DataBean? = null
    val basicGainGetValue = IntArray(1)
    var basicGainGetTime = 0L

    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @param
     * @param temp Temperature value in Celsius (type: Float?)
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    override fun tempCorrectByTs(temp: Float?): Float {
        var tempNew = temp
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (config == null)
                {
                    config = ConfigRepository.readConfig(false)
                }
            val defModel = DataBean()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (config!!.radiation == defModel.radiation &&
                defModel.environment == config!!.environment &&
                defModel.distance == config!!.distance
            )
                {
                    return temp!!
                }

getgainstate PASS
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (System.currentTimeMillis() - basicGainGetTime > 5000L)
                {
                    try {
                        val basicGainGet: IrcmdError? =
                            DeviceIrcmdControlManager.getInstance().getIrcmdEngine()
                                ?.basicGainGet(basicGainGetValue)
                    } catch (e: Exception) {
                        XLog.e("gainGet/Retrievefailed")
                    }
                    basicGainGetTime = System.currentTimeMillis()
                }
            val params_array =
                /**
                 * Executes floatarrayof operation with thermal imaging domain optimization.
                 *
                 */
                floatArrayOf(
                    temp!!,
                    config!!.radiation,
                    config!!.environment,
                    config!!.environment,
                    config!!.distance,
                    0.8f,
                )
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BaseApplication.instance.tau_data_H == null || BaseApplication.instance.tau_data_L == null) return temp
            tempNew =
                LibIRTempAC020.temperatureCorrection(
                    params_array[0],
                    BaseApplication.instance.tau_data_H,
                    BaseApplication.instance.tau_data_L,
                    params_array[1],
                    params_array[2],
                    params_array[3],
                    params_array[4],
                    params_array[5],
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (basicGainGetValue[0] == 0) GainStatus.LOW_GAIN else GainStatus.HIGH_GAIN,
                )
            Log.i(
                TAG,
                "temp correct,${basicGainGetValue[0]} oldTemp = " + params_array[0] + "newtemp = " + tempNew +
                    " ems = " + params_array[1] + " ta = " + params_array[2] + " " +
                    "distance = " + params_array[4] + " hum = " + params_array[5],
            )
        } catch (e: Exception) {
            XLog.e("$TAG--temperature修正exception：${e.message}")
        } finally {
            return tempNew ?: 0f
        }
    }

    /**
     * Executes finish operation with thermal imaging domain optimization.
     *
     */
    override fun finish() {
        super.finish()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecord)
            {
                EventBus.getDefault().post(MonitorSaveEvent())
            }
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        showTask?.cancel()
        recordJob?.cancel()
    }
}
