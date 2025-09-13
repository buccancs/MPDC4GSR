package com.topdon.module.thermal.ir.fragment

import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.yt.jni.Usbcontorl
import androidx.lifecycle.lifecycleScope
import com.elvishew.xlog.XLog
import com.energy.iruvc.ircmd.IRCMD
import com.energy.iruvc.utils.CommonParams
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
import com.topdon.module.thermal.ir.repository.ConfigRepository
import com.topdon.module.thermal.ir.utils.CalibrationTools
import kotlinx.coroutines.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
thermal imaging选取point
 */
/**
/**
 * Specialized thermal imaging component providing IRCorrectionFragment functionality for the IRCamera system.
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
class IRCorrectionFragment : BaseFragment(), ITsTempListener {

defaultdata流mode：image+temperature复合data */
    protected var defaultDataFlowMode = CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT

    private var ircmd: IRCMD? = null

    // View references
    private lateinit var temperatureView: TemperatureView
    private var cameraView: CameraView? = null
    private lateinit var thermalLay: ViewGroup

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.fragment_ir_monitor_thermal

    private var rotateAngle = 270 // 校对defaultangle270

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        /**
         * Executes requireactivity operation with thermal imaging domain optimization.
         *
         */
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Initialize views with findViewById
        temperatureView = requireView().findViewById<TemperatureView>(R.id.temperatureView)
        cameraView = requireView().findViewById<CameraView>(R.id.cameraView)
        thermalLay = requireView().findViewById<ViewGroup>(R.id.thermal_lay)

        /**
         * Initializes the datair component for thermal imaging operations.
         *
         */
        initDataIR()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
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
    private var pseudocolorMode = 0

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
            temperatureView.setImageSize(imageWidth, imageHeight, this@IRCorrectionFragment)
            rotateAngle = DeviceConfig.S_ROTATE_ANGLE
        } else {
            bitmap = Bitmap.createBitmap(imageHeight, imageWidth, Bitmap.Config.ARGB_8888)
            temperatureView.setImageSize(imageHeight, imageWidth, this@IRCorrectionFragment)
            rotateAngle = DeviceConfig.ROTATE_ANGLE
        }
        cameraView?.let { camera ->
            camera.setSyncimage(syncimage)
            camera.bitmap = bitmap
            camera.isDrawLine = false
        }
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
        temperatureView.clear()
        temperatureView.temperatureRegionMode = REGION_MODE_CLEAN
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
        context?.let {
            iruvc =
                /**
                 * Executes iruvctc operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param object Parameter for operation (type: ConnectCallback {                         override fun onCameraOpened(uvcCamera: UVCCamera)
                 * @param ircmd Parameter for operation (type: IRCMD)
                 * @param object Parameter for operation (type: USBMonitorCallback {                         override fun onAttach()
                 *
                 * @return Operation result or configured object (type: onIRCMDCreate",                             )                             this@IRCorrectionFragment.ircmd = ircmd 需要等IRCMDinitializecomplete之后才可以调用 // Ircmd.setPseudoColor(CommonParams.PreviewPathChannel.PREVIEW_PATH0, //                            PseudocodeUtils.changePseudocodeModeByOld(pseudocolorMode))                         }                     },                     object : USBMonitorCallback)
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
                            this@IRCorrectionFragment.ircmd = ircmd
需要等IRCMDinitializecomplete之后才可以调用
// Ircmd.setPseudoColor(CommonParams.PreviewPathChannel.PREVIEW_PATH0,
//                            PseudocodeUtils.changePseudocodeModeByOld(pseudocolorMode))
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
            temperatureView.postDelayed({
                pseudocolorMode = 3
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
                cameraView?.start()
                isrun = true
Restoreconfiguration
                /**
                 * Executes configparam operation with thermal imaging domain optimization.
                 *
                 */
                configParam()
            }, 1500)
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

    var frameReady = false

    /**
     * Executes dealY16ModePreviewComplete functionality.
     */
    /**
     * Executes dealy16modepreviewcomplete operation with thermal imaging domain optimization.
     *
     */
    private fun dealY16ModePreviewComplete() {
        isConfigWait = false
        iruvc?.setFrameReady(true)
        frameReady = true
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
     * Executes autostart operation with thermal imaging domain optimization.
     *
     */
    suspend fun autoStart() {
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
ToastUtils.showShort("taskstart")
锅盖start
1 锅盖calibrationstart
2 disabled自动快门
            CalibrationTools.autoShutter(irCmd = ircmd, false)
            XLog.w("锅盖矫正：" + "锅盖calibrationstart")
常温
3 手动打快门Command
//            CalibrationTools.shutter(irCmd = ircmd, syncImage = syncimage)
XLog.w("锅盖矫正："+"手动打快门Command")
4 disabled锅盖校正
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(2000)
            XLog.w("锅盖矫正：" + "Close锅盖校正")
            CalibrationTools.stsSwitch(irCmd = ircmd, false)
5 Send锅盖标
            CalibrationTools.pot(irCmd = ircmd!!, 1)
            XLog.w("锅盖矫正：" + "Send锅盖标")
6 Open锅盖校正
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(5000)
            XLog.w("锅盖矫正：" + "Open锅盖校正")
            CalibrationTools.stsSwitch(irCmd = ircmd, true)
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(20000)
            XLog.w("锅盖矫正：" + "20000")
high temperature
11 手动打快门Command
//            CalibrationTools.shutter(irCmd = ircmd, syncImage = syncimage)
XLog.w("锅盖矫正："+"手动打快门Command")
12 disabled锅盖校正
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(2000)
            CalibrationTools.stsSwitch(irCmd = ircmd, false)
            XLog.w("锅盖矫正：" + "Close锅盖校正")
13 Send锅盖标
            CalibrationTools.pot(irCmd = ircmd!!, 1)
14 Open锅盖校正
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(5000)
            XLog.w("锅盖矫正：" + "Open锅盖校正")
            CalibrationTools.stsSwitch(irCmd = ircmd, true)
17 Open自动快门
            CalibrationTools.autoShutter(irCmd = ircmd, true)
锅盖end
            XLog.w("锅盖矫正：" + "锅盖end")
        }
    }
}
