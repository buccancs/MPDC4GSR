package com.topdon.module.thermal.ir.fragment

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.yt.jni.Usbcontorl
import androidx.lifecycle.lifecycleScope
import com.elvishew.xlog.XLog
import com.energy.iruvc.ircmd.IRCMD
import com.energy.iruvc.utils.CommonParams
import com.energy.iruvc.utils.CommonUtils
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
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.config.DeviceConfig
import com.topdon.lib.core.ktbase.BaseFragment
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.activity.IRMonitorActivity
import com.topdon.module.thermal.ir.bean.SelectPositionBean
import com.topdon.module.thermal.ir.event.ThermalActionEvent
import com.topdon.module.thermal.ir.repository.ConfigRepository
import kotlinx.coroutines.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
thermal imaging选取point
 */
/**
/**
 * Specialized thermal imaging component providing IRMonitorThermalFragment functionality for the IRCamera system.
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
class IRMonitorThermalFragment : BaseFragment(), ITsTempListener {
defaultdata流mode：image+temperature复合data */
    protected var defaultDataFlowMode = CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT

    private var ircmd: IRCMD? = null
    private var gainStatus = CommonParams.GainStatus.HIGH_GAIN

    // FindViewById declarations
    private lateinit var temperatureView: TemperatureView
    private lateinit var thermalLay: FrameLayout
    private lateinit var cameraView: CameraView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.fragment_ir_monitor_thermal

    private var rotateAngle = 270 // 校对defaultangle270
    private var ts_data_H: ByteArray? = null
    private var ts_data_L: ByteArray? = null
    private var isPick = false

    companion object {
    /**
     * Executes newInstance functionality.
     */
        /**
         * Executes newinstance operation with thermal imaging domain optimization.
         *
         * @param
         * @param isPick Parameter for operation (type: Boolean)
         *
         */
        fun newInstance(isPick: Boolean): IRMonitorThermalFragment {
            val fragment = IRMonitorThermalFragment()
            val bundle = Bundle()
            bundle.putBoolean("isPick", isPick)
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize findViewById using view
        temperatureView = view!!.findViewById(R.id.temperatureView)
        thermalLay = view!!.findViewById(R.id.thermal_lay)
        cameraView = view!!.findViewById(R.id.cameraView)

        ts_data_H = CommonUtils.getTauData(context, "ts/TS001_H.bin")
        ts_data_L = CommonUtils.getTauData(context, "ts/TS001_L.bin")
        /**
         * Executes requireactivity operation with thermal imaging domain optimization.
         *
         */
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        /**
         * Initializes the datair component for thermal imaging operations.
         *
         */
        initDataIR()
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
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (arguments?.containsKey("isPick") == true)
            {
                isPick = requireArguments().getBoolean("isPick")
            }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes action functionality.
     */
    /**
     * Executes action operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: ThermalActionEvent)
     *
     */
    fun action(event: ThermalActionEvent) {
        temperatureView.isEnabled = true
        Log.w("123", "event:${event.action}")
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            2001 -> {
point
                temperatureView.visibility = View.VISIBLE
                temperatureView.temperatureRegionMode = REGION_MODE_POINT
                /**
                 * Executes readposition operation with thermal imaging domain optimization.
                 *
                 */
                readPosition(1)
            }
            2002 -> {
line
                temperatureView.visibility = View.VISIBLE
                temperatureView.temperatureRegionMode = REGION_MODE_LINE
                /**
                 * Executes readposition operation with thermal imaging domain optimization.
                 *
                 */
                readPosition(2)
            }
            2003 -> {
area
                temperatureView.visibility = View.VISIBLE
                temperatureView.temperatureRegionMode = REGION_MODE_RECTANGLE
                /**
                 * Executes readposition operation with thermal imaging domain optimization.
                 *
                 */
                readPosition(3)
            }
        }
    }

    private var imageThread: ImageThreadTC? = null
    private var bitmap: Bitmap? = null
    private var iruvc: IRUVCTC? = null
    private val cameraWidth = 256
    private val cameraHeight = 384
    private val tempHeight = 192
    private var imageWidth = cameraWidth
    private var imageHeight = cameraHeight - tempHeight
    private val image = ByteArray(imageWidth * imageHeight * 2)
    private val temperature = ByteArray(imageWidth * imageHeight * 2)
    private val syncimage = SynchronizedBitmap()
    private var isrun = false
    private var pseudocolorMode = 3
    private var temperaturerun = false
    private var isTS001 = false

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
            restartusbcamera()
        }
    }

    /**
初始data
     */
    /**
     * Initializes the datair component for thermal imaging operations.
     *
     */
    private fun initDataIR() {
        imageWidth = cameraHeight - tempHeight
        imageHeight = cameraWidth
        temperatureView.setTextSize(SaveSettingUtil.tempTextSize)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ScreenUtil.isPortrait(requireContext())) {
            bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)
            temperatureView.setImageSize(imageWidth, imageHeight, this@IRMonitorThermalFragment)
            rotateAngle = DeviceConfig.S_ROTATE_ANGLE
        } else {
            bitmap = Bitmap.createBitmap(imageHeight, imageWidth, Bitmap.Config.ARGB_8888)
            temperatureView.setImageSize(imageHeight, imageWidth, this@IRMonitorThermalFragment)
            rotateAngle = DeviceConfig.ROTATE_ANGLE
        }
        cameraView!!.setSyncimage(syncimage)
        cameraView!!.bitmap = bitmap
        temperatureView.setSyncimage(syncimage)
        temperatureView.setTemperature(temperature)
        temperatureView.isEnabled = false
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
初始全局temperature measurement
        temperatureView.post {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!temperaturerun) {
                temperaturerun = true
需等待renderingcomplete再display
                temperatureView.visibility = View.VISIBLE
            }
        }
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
            imageThread = ImageThreadTC(context, imageWidth, imageHeight)
            imageThread!!.setDataFlowMode(defaultDataFlowMode)
            imageThread!!.setSyncImage(syncimage)
            imageThread!!.setImageSrc(image)
            imageThread!!.setTemperatureSrc(temperature)
            imageThread!!.setBitmap(bitmap)
            imageThread?.setRotate(rotateAngle)
            imageThread!!.setRotate(true)
            imageThread!!.start()
        } catch (e: Exception) {
            Log.e("imageline程重复启动", e.message.toString())
        }
    }

    
    /**
     * Executes startUSB functionality.
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
             * @return Calculated integer value or status code (type: onIRCMDCreate",                         )                         this@IRMonitorThermalFragment.ircmd = ircmd reset镜像为非镜像                         ircmd.setPropImageParams(                             CommonParams.PropImageParams.IMAGE_PROP_SEL_MIRROR_FLIP,                             CommonParams.PropImageParamsValue.MirrorFlipType.NO_MIRROR_FLIP,                         ) 需要等IRCMDinitializecomplete之后才可以调用 // Ircmd?.setPseudoColor(CommonParams.PreviewPathChannel.PREVIEW_PATH0, CommonParams.PseudoColorType.PSEUDO_1)                         val fwBuildVersionInfoBytes = ByteArray(50)                         ircmd?.getDeviceInfo(                             CommonParams.DeviceInfoType.DEV_INFO_FW_BUILD_VERSION_INFO,                             fwBuildVersionInfoBytes,                         ) // Ok                         val value = IntArray(1)                         val arm = String(fwBuildVersionInfoBytes.copyOfRange(0, 8))                         isTS001 = arm.contains("Mini256", true)                         ircmd!!.getPropTPDParams(CommonParams.PropTPDParams.TPD_PROP_GAIN_SEL, value)                         Log.d(TAG, "TPD_PROP_GAIN_SEL=" + value[0])                         gainStatus =                             if (value[0] == 1))
             *
             */
            IRUVCTC(
                cameraWidth, cameraHeight, context, syncimage,
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
                        this@IRMonitorThermalFragment.ircmd = ircmd
reset镜像为非镜像
                        ircmd.setPropImageParams(
                            CommonParams.PropImageParams.IMAGE_PROP_SEL_MIRROR_FLIP,
                            CommonParams.PropImageParamsValue.MirrorFlipType.NO_MIRROR_FLIP,
                        )
需要等IRCMDinitializecomplete之后才可以调用
// Ircmd?.setPseudoColor(CommonParams.PreviewPathChannel.PREVIEW_PATH0, CommonParams.PseudoColorType.PSEUDO_1)
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
                        activity?.finish()
                    }

                    /**
                     * Executes oncancel operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onCancel() {
                        activity?.finish()
                    }
                },
            )
        iruvc!!.isRestart = isRestart
        iruvc!!.setImageSrc(image)
        iruvc!!.setTemperatureSrc(temperature)
        iruvc!!.setRotate(rotateAngle)
        iruvc!!.registerUSB()
    }

    
    /**
     * Executes restartusbcamera functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    private fun restartusbcamera() {
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

    /**
     * Executes onstart operation with thermal imaging domain optimization.
     *
     */
    override fun onStart() {
        super.onStart()
        Log.w(TAG, "onStart")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isrun) {
初始configuration,pseudo-coloriron red
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isPick)
                {
                    pseudocolorMode = SaveSettingUtil.pseudoColorMode
                } else
                {
                    pseudocolorMode = 3
                }
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
            cameraView!!.start()
            isrun = true
Restoreconfiguration
            /**
             * Executes configparam operation with thermal imaging domain optimization.
             *
             */
            configParam()
        }
    }

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
        Log.w(TAG, "onStop")
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
        cameraView?.stop()
        isrun = false
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.w(TAG, "onDestroy")
        try {
            imageThread?.join()
        } catch (e: InterruptedException) {
            Log.e(TAG, "imageThread.join(): catch an interrupted exception")
        }
某些特定客户的特殊device需要使用该Commanddisabledsensor
// If (Usbcontorl.isload) {
Usbcontorl.usb3803_mode_setting(0) // Disabled5V
//        }
// If (tempinfo != 0L) {
//            Libircmd.temp_correction_release(tempinfo)
//        }
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
        isConfigWait = false
        iruvc!!.setFrameReady(true)
    }

    private var showTask: Job? = null

    /**
     * Executes readPosition functionality.
     */
    /**
     * Executes readposition operation with thermal imaging domain optimization.
     *
     * @param
     * @param type Parameter for operation (type: Int)
     *
     */
    private fun readPosition(type: Int) {
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
                     * Handles temperature measurement and calibration with precision thermal data processing.
                     *
                     * @note Temperature values are in Celsius unless otherwise specified.
                     * Accuracy depends on thermal camera calibration.
                     *
                     */
                    updateTemp(type)
                }
            }
    }

get选取point
    /**
     * Processes temperature measurement data.
     */
    private fun updateTemp(type: Int) {
        var result: SelectPositionBean? = null
        val contentRectF = RectF(0f, 0f, 192f, 256f)
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (type) {
            1 -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (temperatureView.point != null &&
                    contentRectF.contains(
                        temperatureView.point.x.toFloat(),
                        temperatureView.point.y.toFloat(),
                    )
                ) {
                    result = SelectPositionBean(1, temperatureView.point)
                }
            }
            2 -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (temperatureView.line != null) {
                    result =
                        /**
                         * Executes selectpositionbean operation with thermal imaging domain optimization.
                         *
                         */
                        SelectPositionBean(
                            2,
                            temperatureView.line.start,
                            temperatureView.line.end,
                        )
                }
            }
            3 -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (temperatureView.rectangle != null &&
                    contentRectF.contains(
                        /**
                         * Executes rectf operation with thermal imaging domain optimization.
                         *
                         */
                        RectF(
                            temperatureView.rectangle.left.toFloat(),
                            temperatureView.rectangle.top.toFloat(),
                            temperatureView.rectangle.right.toFloat(),
                            temperatureView.rectangle.bottom.toFloat(),
                        ),
                    )
                ) {
                    result =
                        /**
                         * Executes selectpositionbean operation with thermal imaging domain optimization.
                         *
                         */
                        SelectPositionBean(
                            3,
                            /**
                             * Executes point operation with thermal imaging domain optimization.
                             *
                             */
                            Point(
                                temperatureView.rectangle.left,
                                temperatureView.rectangle.top,
                            ),
                            /**
                             * Executes point operation with thermal imaging domain optimization.
                             *
                             */
                            Point(
                                temperatureView.rectangle.right,
                                temperatureView.rectangle.bottom,
                            ),
                        )
                }
            }
        }
        val activity = requireActivity() as IRMonitorActivity
        activity.select(result)
    }

    /**
     * Sets viewlay configuration.
     */
    private fun setViewLay() {
        thermalLay.post {
            if (ScreenUtil.isPortrait(requireContext())) {
                val params = thermalLay.layoutParams
                params.width = ScreenUtil.getScreenWidth(requireContext())
                params.height = params.width * imageHeight / imageWidth
                thermalLay.layoutParams = params
            } else {
横屏
                val params = thermalLay.layoutParams
                params.height = thermalLay.height
                params.width = params.height * imageHeight / imageWidth
                thermalLay.layoutParams = params
            }
        }
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
                 * Executes showloadingdialog operation with thermal imaging domain optimization.
                 *
                 */
                showLoadingDialog()
            }
            101 -> {
displayimage
                lifecycleScope.launch {
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(500)
                    isConfigWait = false
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(1000)
                    /**
                     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    dismissLoadingDialog()
                }
            }
        }
    }

    private var isConfigWait = true

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
            imageThread?.pseudocolorMode = pseudocolorMode // Settingspseudo color
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
val tuChar = (config.environment * 10).toInt().toChar() // Ambient temperature
            XLog.w("settingsTPD_PROP DISTANCE:${disChar.toInt()}, EMS:${emsChar.toInt()}}")
            val timeMillis = 250L
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
emissivity
            ircmd?.setPropTPDParams(
                CommonParams.PropTPDParams.TPD_PROP_EMS,
                CommonParams.PropTPDParamsValue.NumberType(emsChar.toString()),
            )
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
距离
            ircmd?.setPropTPDParams(
                CommonParams.PropTPDParams.TPD_PROP_DISTANCE,
                CommonParams.PropTPDParamsValue.NumberType(disChar.toString()),
            )
自动快门
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(timeMillis)
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
                        ircmd?.setPropAutoShutterParameter(
                            CommonParams.PropAutoShutterParameter.SHUTTER_PROP_SWITCH,
                            CommonParams.PropAutoShutterParameterValue.StatusSwith.ON,
                        )
                    } else
                        {
                            ircmd?.setPropAutoShutterParameter(
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
     * Retrieves bitmap information.
     */
    fun getBitmap(): Bitmap  {
        return cameraView.scaledBitmap
    }

    /**
     * Executes startCoverStsSwitchReady functionality.
     */
    /**
     * Executes startcoverstsswitchready operation with thermal imaging domain optimization.
     *
     */
    fun startCoverStsSwitchReady(): Int  {
锅盖calibration-准备
        return ircmd?.rmCoverStsSwitch(CommonParams.RMCoverStsSwitchStatus.RMCOVER_DIS) ?: 1
    }

    /**
     * Executes startCoverStsSwitch functionality.
     */
    /**
     * Executes startcoverstsswitch operation with thermal imaging domain optimization.
     *
     */
    fun startCoverStsSwitch(): Int  {
锅盖calibration-准备
        ircmd?.rmCoverAutoCalc(CommonParams.RMCoverAutoCalcType.GAIN_1)
        return ircmd?.rmCoverStsSwitch(CommonParams.RMCoverStsSwitchStatus.RMCOVER_DIS) ?: 1
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
        return temp
    }
}
