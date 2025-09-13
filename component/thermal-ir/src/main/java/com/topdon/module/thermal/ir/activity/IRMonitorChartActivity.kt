package com.topdon.module.thermal.ir.activity

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.yt.jni.Usbcontorl
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.elvishew.xlog.XLog
import com.energy.iruvc.ircmd.IRCMD
import com.energy.iruvc.ircmd.IRCMDType
import com.energy.iruvc.ircmd.IRUtils
import com.energy.iruvc.sdkisp.LibIRTemp
import com.energy.iruvc.utils.CommonParams
import com.energy.iruvc.utils.CommonUtils
import com.energy.iruvc.utils.Line
import com.energy.iruvc.utils.SynchronizedBitmap
import com.energy.iruvc.uvc.ConnectCallback
import com.energy.iruvc.uvc.UVCCamera
import com.infisense.usbir.camera.IRUVCTC
import com.infisense.usbir.config.MsgCode
import com.infisense.usbir.event.IRMsgEvent
import com.infisense.usbir.event.PreviewComplete
import com.infisense.usbir.thread.ImageThreadTC
import com.infisense.usbir.utils.USBMonitorCallback
import com.infisense.usbir.view.CameraView
import com.infisense.usbir.view.ITsTempListener
import com.infisense.usbir.view.TemperatureView
import com.infisense.usbir.view.TemperatureView.*
import com.topdon.lib.core.bean.event.device.DeviceCameraEvent
import com.topdon.lib.core.bean.tools.ThermalBean
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.DeviceConfig
import com.topdon.lib.core.db.AppDatabase
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.NumberTools
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.lib.core.view.TitleView
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.bean.SelectPositionBean
import com.topdon.module.thermal.ir.event.MonitorSaveEvent
import com.topdon.module.thermal.ir.repository.ConfigRepository
import com.topdon.module.thermal.ir.view.ChartMonitorView
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal
import java.math.RoundingMode
import com.topdon.lib.core.R as LibR

/**
temperature实时监控
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
/**
 * Specialized thermal imaging component providing IRMonitorChartActivity functionality for the IRCamera system.
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
class IRMonitorChartActivity : BaseActivity(), ITsTempListener {
defaultdata流mode：image+temperature复合data */
    protected var defaultDataFlowMode = CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT

    private var gainStatus = CommonParams.GainStatus.HIGH_GAIN
    private var isTS001 = false

    /**
从上一interface传递过来的，当前selected的 point/line/area info.
     */
    private var selectBean: SelectPositionBean = SelectPositionBean()

    // View properties - migrated from synthetic views
    private lateinit var temperatureView: TemperatureView
    private lateinit var llTime: View
    private lateinit var mpChartView: ChartMonitorView
    private lateinit var cameraView: CameraView
    private lateinit var tvTime: TextView
    private lateinit var thermalLay: View

    private var ircmd: IRCMD? = null
    private val bean = ThermalBean()
    private var ts_data_H: ByteArray? = null
    private var ts_data_L: ByteArray? = null

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_ir_monitor_chart

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        findViewById<TitleView>(R.id.title_view).setRightClickListener {
            recordJob?.cancel()
            lifecycleScope.launch {
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(200)
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }
        ts_data_H = CommonUtils.getTauData(this@IRMonitorChartActivity, "ts/TS001_H.bin")
        ts_data_L = CommonUtils.getTauData(this@IRMonitorChartActivity, "ts/TS001_L.bin")
        selectBean = intent.getParcelableExtra("select")!!

        findViewById<TextView>(R.id.monitor_current_vol).text = getString(if (selectBean.type == 1) LibR.string.chart_temperature else LibR.string.chart_temperature_high)
        findViewById<TextView>(R.id.monitor_real_vol).visibility = if (selectBean.type == 1) View.GONE else View.VISIBLE
        findViewById<ImageView>(R.id.monitor_real_img).visibility = if (selectBean.type == 1) View.GONE else View.VISIBLE

        // Initialize view properties
        temperatureView = findViewById(R.id.temperatureView)
        llTime = findViewById(R.id.ll_time)
        mpChartView = findViewById(R.id.mp_chart_view)
        cameraView = findViewById(R.id.cameraView)
        tvTime = findViewById(R.id.tv_time)
        thermalLay = findViewById(R.id.thermal_lay)

        temperatureView.isEnabled = false
        temperatureView.setTextSize(SaveSettingUtil.tempTextSize)

        /**
         * Initializes the datair component for thermal imaging operations.
         *
         */
        initDataIR()
    }

    /**
     * Executes finish operation with thermal imaging domain optimization.
     *
     */
    override fun finish() {
        super.finish()
        EventBus.getDefault().post(MonitorSaveEvent())
    }

    private var showTask: Job? = null

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (showTask != null && showTask!!.isActive) {
            showTask!!.cancel()
            showTask = null
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
                    val result: LibIRTemp.TemperatureSampleResult =
                        /**
                         * Executes when operation with thermal imaging domain optimization.
                         *
                         */
                        when (selectBean.type) {
                            1 -> temperatureView.getPointTemp(selectBean.startPosition)
                            2 -> temperatureView.getLineTemp(Line(selectBean.startPosition, selectBean.endPosition))
                            else -> temperatureView.getRectTemp(selectBean.getRect())
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
                                llTime.isVisible = true
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

    /**
     * Executes onstart operation with thermal imaging domain optimization.
     *
     */
    override fun onStart() {
        super.onStart()
        isStop = false
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isrun) {
            /**
             * Executes configparam operation with thermal imaging domain optimization.
             *
             */
            configParam()
            temperatureView.postDelayed({
初始configuration,pseudo-coloriron red
                try {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!isStop)
                        {
                            pseudoColorMode = 3
                            /**
                             * Executes startusb operation with thermal imaging domain optimization.
                             *
                             */
                            startUSB(false)
                            /**
                             * Executes startisp operation with thermal imaging domain optimization.
                             *
                             */
                            startISP()
                            temperatureView.start()
                            cameraView.start()
                            isrun = true
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (!isRecord)
                                {
                                    /**
                                     * Executes recordthermal operation with thermal imaging domain optimization.
                                     *
                                     */
                                    recordThermal() // StartRecord
                                }
                        }
                } catch (e: Exception) {
                    Log.e("Test", "// " + e.message)
                }
            }, 1500)
        }
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mpChartView.highlightValue(null) // Close高亮pointMarker
    }

    /**
     * Executes onpause operation with thermal imaging domain optimization.
     *
     */
    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private var isStop = false

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
        isStop = true
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (iruvc != null) {
            iruvc!!.stopPreview()
            iruvc!!.unregisterUSB()
        }
        imageThread?.interrupt()
        syncimage.valid = false
        temperatureView.stop()
        cameraView.stop()
        isrun = false
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        recordJob?.cancel()
        try {
            imageThread?.join()
        } catch (e: InterruptedException) {
            Log.e(TAG, "imageThread.join(): catch an interrupted exception")
        }
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
                    if (!isStop)
                        {
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
                                    mpChartView.addPointToChart(bean = entity, selectType = selectBean.type)
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
                                tvTime.text = TimeTool.showVideoLongTime(System.currentTimeMillis() - startTime)
                            }
                        }
                }
                XLog.w("stopRecord, data量:$time")
            }
    }

    private var imageThread: ImageThreadTC? = null
    private var bitmap: Bitmap? = null // 不需要Show/Displayimage，可去掉
    private var iruvc: IRUVCTC? = null
    private val cameraWidth = 256
    private val cameraHeight = 384
    private val tempHeight = 192
    private var imageWidth = cameraWidth
    private var imageHeight = cameraHeight - tempHeight
    private val imageBytes = ByteArray(imageWidth * imageHeight * 2)
    private val temperatureBytes = ByteArray(imageWidth * imageHeight * 2)
    private val syncimage = SynchronizedBitmap()
    private var isrun = false
    private var pseudoColorMode = 0

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes irEvent functionality.
     */
    /**
     * Executes irevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: IRMsgEvent)
     *
     */
    fun irEvent(event: IRMsgEvent) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (event.code == MsgCode.RESTART_USB) {
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            restartUsbCamera()
        }
    }

    private var rotateAngle = 270

    /**
初始data
     *
不做imageupdate
去掉cameraView
     * syncimage.valid = true
     */
    /**
     * Initializes datair component.
     */
    private fun initDataIR() {
        imageWidth = cameraHeight - tempHeight
        imageHeight = cameraWidth
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ScreenUtil.isPortrait(this)) {
            bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)
            temperatureView.setImageSize(imageWidth, imageHeight, this@IRMonitorChartActivity)
            rotateAngle = DeviceConfig.S_ROTATE_ANGLE
        } else {
            bitmap = Bitmap.createBitmap(imageHeight, imageWidth, Bitmap.Config.ARGB_8888)
            temperatureView.setImageSize(imageHeight, imageWidth, this@IRMonitorChartActivity)
            rotateAngle = DeviceConfig.ROTATE_ANGLE
        }
        cameraView.setSyncimage(syncimage)
        cameraView.bitmap = bitmap
        temperatureView.setSyncimage(syncimage)
        temperatureView.setTemperature(temperatureBytes)
        /**
         * Configures the viewlay with validation and thermal imaging optimization.
         *
         */
        setViewLay()
某些特定客户的特殊device需要使用该Commanddisabledsensor
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Usbcontorl.isload) {
            Usbcontorl.usb3803_mode_setting(1) // Open5V
            Log.w("123", "Open5V")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes iruvctc functionality.
     */
    /**
     * Executes iruvctc operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: PreviewComplete)
     *
     */
    fun iruvctc(event: PreviewComplete) {
        /**
         * Executes dealy16modepreviewcomplete operation with thermal imaging domain optimization.
         *
         */
        dealY16ModePreviewComplete()
    }

    /**
     * Executes dealY16ModePreviewComplete functionality.
     */
    /**
     * Executes dealy16modepreviewcomplete operation with thermal imaging domain optimization.
     *
     */
    private fun dealY16ModePreviewComplete() {
        /**
         * Executes dismissloadingdialog operation with thermal imaging domain optimization.
         *
         */
        dismissLoadingDialog()
        isConfigWait = false
        iruvc!!.setFrameReady(true)
        /**
         * Handles temperature measurement and calibration with precision thermal data processing.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        addTempLine()
    }

    /**
image信号processing
     */
    /**
     * Executes startisp operation with thermal imaging domain optimization.
     *
     */
    private fun startISP() {
        try {
            imageThread = ImageThreadTC(this@IRMonitorChartActivity, imageWidth, imageHeight)
            imageThread!!.setDataFlowMode(defaultDataFlowMode)
            imageThread!!.setSyncImage(syncimage)
            imageThread!!.setImageSrc(imageBytes)
            imageThread!!.setTemperatureSrc(temperatureBytes)
            imageThread!!.setBitmap(bitmap)
            imageThread!!.setRotate(rotateAngle)
            imageThread!!.start()
        } catch (e: Exception) {
            Log.e("imageline程重复启动", e.message.toString())
        }
    }

    /**
@param isRestart 是否是重启module
     */
    /**
     * Executes startusb operation with thermal imaging domain optimization.
     *
     * @param
     * @param isRestart Parameter for operation (type: Boolean)
     *
     */
    private fun startUSB(isRestart: Boolean) {
        iruvc =
            /**
             * Executes iruvctc operation with thermal imaging domain optimization.
             *
             * @param
             * @param object Parameter for operation (type: ConnectCallback {                     override fun onCameraOpened(uvcCamera: UVCCamera)
             * @param ircmd Parameter for operation (type: IRCMD)
             * @param object Parameter for operation (type: USBMonitorCallback {                     override fun onAttach()
             *
             * @return Calculated integer value or status code (type: onIRCMDCreate",                         )                         this@IRMonitorChartActivity.ircmd = ircmd 需要等IRCMDinitializecomplete之后才可以调用 // Ircmd.setPseudoColor( //                        CommonParams.PreviewPathChannel.PREVIEW_PATH0, //                        PseudocodeUtils.changePseudocodeModeByOld(pseudoColorMode))                         val fwBuildVersionInfoBytes = ByteArray(50)                         ircmd?.getDeviceInfo(                             CommonParams.DeviceInfoType.DEV_INFO_FW_BUILD_VERSION_INFO,                             fwBuildVersionInfoBytes,                         ) // Ok                         val value = IntArray(1)                         val arm = String(fwBuildVersionInfoBytes.copyOfRange(0, 8))                         isTS001 = arm.contains("Mini256", true)                         ircmd!!.getPropTPDParams(CommonParams.PropTPDParams.TPD_PROP_GAIN_SEL, value)                         Log.d(TAG, "TPD_PROP_GAIN_SEL=" + value[0])                         gainStatus =                             if (value[0] == 1))
             *
             */
            IRUVCTC(
                cameraWidth, cameraHeight, this@IRMonitorChartActivity, syncimage,
                defaultDataFlowMode,
                object : ConnectCallback {
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     * @param
                     * @param uvcCamera Camera configuration or reference (type: UVCCamera)
                     *
                     */
                    override fun onCameraOpened(uvcCamera: UVCCamera) {
                    }

                    /**
                     * Executes onircmdcreate operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param ircmd Parameter for operation (type: IRCMD)
                     *
                     */
                    override fun onIRCMDCreate(ircmd: IRCMD) {
                        Log.i(
                            TAG,
                            "ConnectCallback->onIRCMDCreate",
                        )
                        this@IRMonitorChartActivity.ircmd = ircmd
需要等IRCMDinitializecomplete之后才可以调用
// Ircmd.setPseudoColor(
//                        CommonParams.PreviewPathChannel.PREVIEW_PATH0,
//                        PseudocodeUtils.changePseudocodeModeByOld(pseudoColorMode))
                        val fwBuildVersionInfoBytes = ByteArray(50)
                        ircmd?.getDeviceInfo(
                            CommonParams.DeviceInfoType.DEV_INFO_FW_BUILD_VERSION_INFO,
                            fwBuildVersionInfoBytes,
                        ) // Ok
                        val value = IntArray(1)
                        val arm = String(fwBuildVersionInfoBytes.copyOfRange(0, 8))
                        isTS001 = arm.contains("Mini256", true)
                        ircmd!!.getPropTPDParams(CommonParams.PropTPDParams.TPD_PROP_GAIN_SEL, value)
                        Log.d(TAG, "TPD_PROP_GAIN_SEL=" + value[0])
                        gainStatus =
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (value[0] == 1) {
当前core为高gain
                                CommonParams.GainStatus.HIGH_GAIN
等效大气透过率表
                            } else {
当前core为低gain
                                CommonParams.GainStatus.LOW_GAIN
                            }
                    }
                },
                object : USBMonitorCallback {
                    /**
                     * Executes onattach operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onAttach() {}

                    /**
                     * Executes ongranted operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onGranted() {}

                    /**
                     * Executes onconnect operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onConnect() {}

                    /**
                     * Executes ondisconnect operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onDisconnect() {}

                    /**
                     * Executes ondettach operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onDettach() {
                        /**
                         * Executes finish operation with thermal imaging domain optimization.
                         *
                         */
                        finish()
                    }

                    /**
                     * Executes oncancel operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onCancel() {
                        /**
                         * Executes finish operation with thermal imaging domain optimization.
                         *
                         */
                        finish()
                    }
                },
            )
        iruvc!!.isRestart = isRestart
        iruvc!!.setImageSrc(imageBytes)
        iruvc!!.setTemperatureSrc(temperatureBytes)
        iruvc!!.setRotate(rotateAngle)
        iruvc!!.registerUSB()
    }

    
    /**
     * Executes restartUsbCamera functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    private fun restartUsbCamera() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (iruvc != null) {
            iruvc!!.stopPreview()
            iruvc!!.unregisterUSB()
        }
        /**
         * Executes startusb operation with thermal imaging domain optimization.
         *
         */
        startUSB(true)
    }

    private var isConfigWait = false

configuration
    /**
     * Executes configParam functionality.
     */
    /**
     * Executes configparam operation with thermal imaging domain optimization.
     *
     */
    private fun configParam() {
        lifecycleScope.launch {
            isConfigWait = true
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (isConfigWait) {
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(100)
            }
            val config = ConfigRepository.readConfig(false)
            val disChar = (config.distance * 128).toInt() // 距离(米)
            val emsChar = (config.radiation * 128).toInt() // 发射率
            XLog.w("settingsTPD_PROP DISTANCE:$disChar, EMS:$emsChar}")
            val timeMillis = 250L
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
emissivity
            ircmd!!.setPropTPDParams(
                CommonParams.PropTPDParams.TPD_PROP_EMS,
                CommonParams.PropTPDParamsValue.NumberType(emsChar.toString()),
            )
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
距离
            ircmd!!.setPropTPDParams(
                CommonParams.PropTPDParams.TPD_PROP_DISTANCE,
                CommonParams.PropTPDParamsValue.NumberType(disChar.toString()),
            )
自动快门
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
            ircmd?.zoomCenterDown(
                CommonParams.PreviewPathChannel.PREVIEW_PATH0,
                CommonParams.ZoomScaleStep.ZOOM_STEP2,
            )
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
            ircmd?.zoomCenterDown(
                CommonParams.PreviewPathChannel.PREVIEW_PATH0,
                CommonParams.ZoomScaleStep.ZOOM_STEP2,
            )
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
            ircmd?.zoomCenterDown(
                CommonParams.PreviewPathChannel.PREVIEW_PATH0,
                CommonParams.ZoomScaleStep.ZOOM_STEP2,
            )
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
            ircmd?.zoomCenterDown(
                CommonParams.PreviewPathChannel.PREVIEW_PATH0,
                CommonParams.ZoomScaleStep.ZOOM_STEP2,
            )
            iruvc?.let {
部分机型在disabled自动快门，初始会花屏
                /**
                 * Executes withcontext operation with thermal imaging domain optimization.
                 *
                 */
                withContext(Dispatchers.IO) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (SaveSettingUtil.isAutoShutter) {
                        ircmd!!.setPropAutoShutterParameter(
                            CommonParams.PropAutoShutterParameter.SHUTTER_PROP_SWITCH,
                            CommonParams.PropAutoShutterParameterValue.StatusSwith.ON,
                        )
                    } else {
                        ircmd!!.setPropAutoShutterParameter(
                            CommonParams.PropAutoShutterParameter.SHUTTER_PROP_SWITCH,
                            CommonParams.PropAutoShutterParameterValue.StatusSwith.OFF,
                        )
                    }
                }
            }
复位contrast、细节
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
            ircmd?.setPropImageParams(
                CommonParams.PropImageParams.IMAGE_PROP_LEVEL_CONTRAST,
                CommonParams.PropImageParamsValue.NumberType(128.toString()),
            )
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
            ircmd?.setPropImageParams(
                CommonParams.PropImageParams.IMAGE_PROP_LEVEL_DDE,
                CommonParams.PropImageParamsValue.DDEType.DDE_2,
            )
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
            ircmd?.setPropImageParams(
                CommonParams.PropImageParams.IMAGE_PROP_ONOFF_AGC,
                CommonParams.PropImageParamsValue.StatusSwith.ON,
            )
        }
    }

    /**
drawingpointlinearea
     */
    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    private fun addTempLine() {
        temperatureView.visibility = View.VISIBLE
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (selectBean.type) {
            1 -> {
point
                temperatureView.addScalePoint(selectBean.startPosition)
                temperatureView.temperatureRegionMode = REGION_MODE_POINT
            }
            2 -> {
line
                temperatureView.addScaleLine(
                    /**
                     * Executes line operation with thermal imaging domain optimization.
                     *
                     */
                    Line(
                        selectBean.startPosition,
                        selectBean.endPosition,
                    ),
                )
                temperatureView.temperatureRegionMode = REGION_MODE_LINE
            }
            3 -> {
area
                temperatureView.addScaleRectangle(
                    /**
                     * Executes rect operation with thermal imaging domain optimization.
                     *
                     */
                    Rect(
                        selectBean.startPosition!!.x,
                        selectBean.startPosition!!.y,
                        selectBean.endPosition!!.x,
                        selectBean.endPosition!!.y,
                    ),
                )
                temperatureView.temperatureRegionMode = REGION_MODE_RECTANGLE
            }
        }
        temperatureView.drawLine()
    }

    /**
     * Sets viewlay configuration.
     */
    private fun setViewLay() {
        thermalLay.post {
            val params = thermalLay.layoutParams
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ScreenUtil.isPortrait(this)) {
                params.height = thermalLay.height
                var w = params.height * imageWidth / imageHeight
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (w > ScreenUtil.getScreenWidth(this)) {
                    w = ScreenUtil.getScreenWidth(this)
                }
                params.width = w
            } else {
                params.width = thermalLay.width
                params.height = params.width * imageWidth / imageHeight
            }
            thermalLay.layoutParams = params
        }
    }

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
        var tmp = temp
        try {
            tmp = tempCorrect(temp!!, gainStatus, 0)
        } catch (e: Exception) {
            XLog.i("temperature校正failed: ${e.message}")
        }
        return tmp!!
    }

    /**
单point修正过程
     */
    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @param
     * @param temp Temperature value in Celsius (type: Float)
     * @param gainStatus Parameter for operation (type: CommonParams.GainStatus)
     * @param tempInfo Temperature value in Celsius (type: Long)
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    private fun tempCorrect(
        temp: Float,
        gainStatus: CommonParams.GainStatus,
        tempInfo: Long,
    ): Float {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTS001) {
不是ts001不需要修正
            return temp
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ts_data_H == null || ts_data_L == null) {
            return temp
        }
        val config = ConfigRepository.readConfig(false)
        config.radiation
        val paramsArray =
            /**
             * Executes floatarrayof operation with thermal imaging domain optimization.
             *
             */
            floatArrayOf(
                temp,
                config.radiation,
                config.environment,
                config.environment,
                config.distance,
                0.8f,
            )
        val newTemp =
            IRUtils.temperatureCorrection(
                IRCMDType.USB_IR_256_384,
                CommonParams.ProductType.WN256_ADVANCED,
                paramsArray[0],
                ts_data_H,
                ts_data_L,
                paramsArray[1],
                paramsArray[2],
                paramsArray[3],
                paramsArray[4],
                paramsArray[5],
                tempInfo,
                gainStatus,
            )
        Log.i(
            TAG,
            "temp correct, oldTemp = " + paramsArray[0] + " ems = " + paramsArray[1] + " ta = " + paramsArray[2] + " " +
                "distance = " + paramsArray[4] + " hum = " + paramsArray[5] + " productType = ${CommonParams.ProductType.WN256_ADVANCED}" + " " +
                "newtemp = " + newTemp,
        )
        return newTemp
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes cameraEvent functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     * @param
     * @param event Parameter for operation (type: DeviceCameraEvent)
     *
     */
    fun cameraEvent(event: DeviceCameraEvent) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            100 -> {
准备image
                /**
                 * Manages thermal camera operations with hardware-optimized performance and error handling.
                 *
                 */
                showCameraLoading()
            }
            101 -> {
displayimage
                /**
                 * Manages thermal camera operations with hardware-optimized performance and error handling.
                 *
                 */
                dismissCameraLoading()
                /**
                 * Handles temperature measurement and calibration with precision thermal data processing.
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                addTempLine()
            }
        }
    }
}
