package com.topdon.module.thermal.ir.activity

import android.graphics.ImageFormat
import android.hardware.usb.UsbDevice
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.view.SurfaceView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.energy.iruvc.ircmd.IRCMD
import com.energy.iruvc.sdkisp.LibIRProcess
import com.energy.iruvc.usb.USBMonitor
import com.energy.iruvc.utils.CommonParams
import com.energy.iruvc.utils.DualCameraParams
import com.energy.iruvc.utils.IFrameCallback
import com.energy.iruvc.utils.IIRFrameCallback
import com.energy.iruvc.utils.SynchronizedBitmap
import com.energy.iruvc.uvc.ConnectCallback
import com.energy.iruvc.uvc.UVCCamera
import com.example.suplib.wrapper.SupHelp
import com.infisense.usbdual.Const
// Import com.infisense.usbdual.camera.DualViewWithExternalCameraCommonApi // Temporarily disabled - hardware specific
// Import com.infisense.usbdual.camera.IRUVCDual // Temporarily disabled - hardware specific
// Import com.infisense.usbdual.camera.USBMonitorManager // Temporarily disabled - hardware specific
import com.infisense.usbdual.camera.DualViewWithExternalCameraCommonApi
import com.infisense.usbdual.camera.IRUVCDual
import com.infisense.usbdual.camera.USBMonitorManager
import com.infisense.usbdual.inf.OnUSBConnectListener
import com.infisense.usbir.utils.PseudocodeUtils
import com.infisense.usbir.view.TemperatureView
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.utils.DualParamsUtil
import com.topdon.module.thermal.ir.utils.IRCmdTool
import com.topdon.module.thermal.ir.utils.IRCmdTool.getSNStr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream

/**
dual light的initialize
dual light的
 */
/**
 * Specialized thermal imaging component providing BaseIRPlushActivity functionality for the IRCamera system.
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
abstract class BaseIRPlushActivity : IRThermalNightActivity(), OnUSBConnectListener, IIRFrameCallback {
thermal imagingdevicesn,可作为唯一id，此sn并非艾睿烧录的，是内部烧录的
    private var snStr = ""

    /**
使用 DualUVCCamera 进行画area预览、getCallbackdata的关键工具class.
     *
注意：这个命名有问题，虽然叫 View，但却不是 View!
     */
    protected var dualView: DualViewWithExternalCameraCommonApi? = null

    /**
     * ir camera
     * 22576 - 0x5830
     * 22592 - 0x5840
     */
    private var irPid = 0x5830

    private var imageWidth = 0 // 经过rotation后的image宽度
    private var imageHeight = 0 // 经过rotation后的image高度
    private var syncimage = SynchronizedBitmap()

    protected var mCurrentFusionType = DualParamsUtil.fusionTypeToParams(SaveSettingUtil.fusionType)

    /**
     * vl camera
     * 12341 - 0x3035  30 fps 640*480
     * 38704 - 0x9730  25 fps 1280*720
     * 8833
     */
    private var vlPid = 12337
    private var vlFps = 30 // 该分辨率支持的帧率

    protected var vlCameraWidth = 1280
    protected var vlCameraHeight = 720
    private var vlData = ByteArray(vlCameraWidth * vlCameraHeight * 3) // Storagevisible lightdata

    /**
     * dual camera
     */
    private var dualCameraWidth = 480
    private var dualCameraHeight = 640

是否使用IRISPalgorithm集成
    private val isUseIRISP = false

    private var psedocolor: Array<ByteArray>? = null

    protected var dualDisp = 30

    /**
camera camera相关
     */
    private var vlUVCCamera: IRUVCDual? = null

    /**
子classimplementation该method，Return用于rendering画area的 SurfaceView
     */
    abstract fun getSurfaceView(): SurfaceView

    /**
子classimplementation该method，Return用于displaytemperature图层的 TemperatureDualView
     */
    abstract fun getTemperatureDualView(): TemperatureView

    /**
是否是dual lightdevice
     */
    abstract fun isDualIR(): Boolean

    abstract fun setTemperatureViewType()

    open fun setDispViewData(dualDisp: Int)  {
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        super.initView()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isDualIR())
            {
defaultDataFlowMode 是 image+temperature，故而 SDK Return的sensor原始宽度为 256x384
那么一frameimage、一frametemperature的尺寸就是 256x(384/2) = 256x192
由于竖屏display需要rotation，那么最终出图尺寸就是 192x256
                imageWidth = 192
                imageHeight = 256
                USBMonitorManager.getInstance().init(irPid, isUseIRISP, defaultDataFlowMode)
                USBMonitorManager.getInstance().addOnUSBConnectListener(this)
            }
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        /**
         * Executes dualstart operation with thermal imaging domain optimization.
         *
         */
        dualStart()
    }

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
        /**
         * Executes dualstop operation with thermal imaging domain optimization.
         *
         */
        dualStop()
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        mIrHandler.removeCallbacksAndMessages(null)
        USBMonitorManager.getInstance().removeOnUSBConnectListener(this)
    }

    /**
     * Executes dualStart functionality.
     */
    /**
     * Executes dualstart operation with thermal imaging domain optimization.
     *
     */
    private fun dualStart() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isDualIR())
            {
                return
            }
        /**
Openinfraredmodule
需要Confirm好module的pid和分辨率
         */
        USBMonitorManager.getInstance().registerUSB()
在USBMonitorManager onConnectCallback中Openvisible lightmodule
        /**
         * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        getTemperatureDualView().setUseIRISP(isUseIRISP)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mCurrentFusionType == DualCameraParams.FusionType.IROnlyNoFusion) {
            /**
             * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
             *
             * @note Temperature values are in Celsius unless otherwise specified.
             * Accuracy depends on thermal camera calibration.
             *
             */
            getTemperatureDualView().setImageSize(Const.IR_HEIGHT, Const.IR_WIDTH, null)
        } else {
            /**
             * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
             *
             * @note Temperature values are in Celsius unless otherwise specified.
             * Accuracy depends on thermal camera calibration.
             *
             */
            getTemperatureDualView().setImageSize(dualCameraWidth, dualCameraHeight, null)
        }
        /**
         * Configures the temperatureviewtype with validation and thermal imaging optimization.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        setTemperatureViewType()
        /**
         * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        getTemperatureDualView().start()
    }

    private var mIrHandler: Handler =
        object : Handler(Looper.getMainLooper()) {
            /**
             * Executes handlemessage operation with thermal imaging domain optimization.
             *
             * @param
             * @param msg Parameter for operation (type: Message)
             *
             */
            override fun handleMessage(msg: Message) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isDualIR())
                    {
                        return
                    }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (msg.what == Const.RESTART_USB) {
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    restartDualCamera()
                } else if (msg.what == Const.HANDLE_CONNECT) {
避免冲突，需要延时
                    /**
开visible lightcamera
需要Confirm好module的pid和分辨率
                     */
                    lifecycleScope.launch(Dispatchers.Main) {
                        startVLCamera(vlPid, vlFps, vlCameraWidth, vlCameraHeight)
                        /**
                         * Initializes the dualcamera component for thermal imaging operations.
                         *
                         */
                        initDualCamera()
一体式
                        /**
                         * Initializes the defintegralargsdispvalue component for thermal imaging operations.
                         *
                         */
                        initDefIntegralArgsDISPValue(DualCameraParams.TypeLoadParameters.ROTATE_270)
                    }
                } else if (msg.what == Const.HANDLE_REGISTER) {
                    USBMonitorManager.getInstance().registerUSB()
                } else if (msg.what == Const.SHOW_LOADING) {
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    showCameraLoading()
                } else if (msg.what == Const.HIDE_LOADING) {
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    dismissCameraLoading()
                } else if (msg.what == Const.SHOW_RESTART_MESSAGE) {
                    Toast.makeText(
                        this@BaseIRPlushActivity,
                        "please restart app or reinsert device",
                        Toast.LENGTH_SHORT,
                    ).show()
                    /**
                     * Executes finish operation with thermal imaging domain optimization.
                     *
                     */
                    finish()
                }
            }
        }

    /**
     * Executes restartDualCamera functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    private fun restartDualCamera() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isrun) {
            USBMonitorManager.getInstance().isReStart = true
            /**
             * Executes dualstop operation with thermal imaging domain optimization.
             *
             */
            dualStop()
            SystemClock.sleep(200)
            /**
             * Executes dualstart operation with thermal imaging domain optimization.
             *
             */
            dualStart()
        }
    }

    /**
一体式
     */
    /**
     * Initializes the defintegralargsdispvalue component for thermal imaging operations.
     *
     * @param
     * @param typeLoadParameters Parameter for operation (type: DualCameraParams.TypeLoadParameters)
     *
     */
    private fun initDefIntegralArgsDISPValue(typeLoadParameters: DualCameraParams.TypeLoadParameters) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isDualIR())
            {
                return
            }
        lifecycleScope.launch {
            val parameters = IRCmdTool.getDualBytes(USBMonitorManager.getInstance().ircmd)
            val data = dualView?.dualUVCCamera?.loadParameters(parameters, typeLoadParameters)
            dualDisp = IRCmdTool.dispNumber
            /**
             * Configures the dispviewdata with validation and thermal imaging optimization.
             *
             */
            setDispViewData(dualDisp)
initializedefault值
            dualView?.dualUVCCamera?.setDisp(dualDisp)
            dualView?.startPreview()
        }
    }

    /**
     * Initializes dualcamera component.
     */
    private fun initDualCamera() {
        if (!isDualIR())
            {
                return
            }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dualView != null) {
            return
        }
        val dualRotate: Int = if (saveSetBean.rotateAngle == 270) 0 else (saveSetBean.rotateAngle + 90)
        dualView =
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            DualViewWithExternalCameraCommonApi(
                /**
                 * Retrieves the surfaceview with optimized performance for thermal imaging operations.
                 *
                 */
                getSurfaceView(),
                USBMonitorManager.getInstance().uvcCamera, defaultDataFlowMode,
                imageHeight, imageWidth,
                vlCameraWidth, vlCameraHeight,
                dualCameraWidth, dualCameraHeight,
                isUseIRISP, dualRotate, this,
            )
        dualView?.addFrameCallback(getTemperatureDualView())
        //
        /**
         * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        getTemperatureDualView().setDualUVCCamera(dualView!!.getDualUVCCamera())
        /**
         * Initializes the pseudocolor component for thermal imaging operations.
         *
         * @note This method is optimized for thermal imaging pseudo color processing.
         * Ensure proper thermal calibration before use.
         *
         */
        initPseudoColor()
        /**
         * Initializes the amplify component for thermal imaging operations.
         *
         */
        initAmplify(true)
这里可以setinitializefusionmode
// SetFusion(mCurrentFusionType)
// DualView!!.startPreview()
        dualView?.setHandler(mIrHandler)
        isrun = true
    }

    /**
     * Initializes pseudocolor component.
     */
    private fun initPseudoColor() {
        val am = assets
        var inputStream: InputStream? = null
        try {
loadpseudo-color，虽然用不上这个pseudo-color，但是sdk限制必须initializea才能正常出图
            psedocolor = Array(11) { ByteArray(0) }
            inputStream = am.open("pseudocolor/White_Hot.bin")
            val length = inputStream.available()
            psedocolor!![0] = ByteArray(length + 1)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (inputStream.read(psedocolor!![0]) != length) {
            }
            psedocolor!![0][length] = 0
            dualView!!.getDualUVCCamera().loadPseudocolor(
                CommonParams.PseudoColorUsbDualType.WHITE_HOT_MODE,
                psedocolor!![0],
            )
这里可以setinitializefusionmode
            /**
             * Configures the fusion with validation and thermal imaging optimization.
             *
             */
            setFusion(mCurrentFusionType)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Configures the fusion with validation and thermal imaging optimization.
     *
     * @param
     * @param fusion Parameter for operation (type: DualCameraParams.FusionType)
     *
     */
    protected fun setFusion(fusion: DualCameraParams.FusionType) {
        dualView?.setCurrentFusionType(fusion)
        /**
         * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        getTemperatureDualView().setCurrentFusionType(fusion)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (fusion == DualCameraParams.FusionType.IROnlyNoFusion) {
            /**
             * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
             *
             * @note Temperature values are in Celsius unless otherwise specified.
             * Accuracy depends on thermal camera calibration.
             *
             */
            getTemperatureDualView().setImageSize(Const.IR_HEIGHT, Const.IR_WIDTH, null)
        } else {
            /**
             * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
             *
             * @note Temperature values are in Celsius unless otherwise specified.
             * Accuracy depends on thermal camera calibration.
             *
             */
            getTemperatureDualView().setImageSize(dualCameraWidth, dualCameraHeight, null)
        }
    }

    /**
visible lightmodule
     *
@param pid          module的pid
@param cameraWidth  module的分辨率宽
@param cameraHeight module的分辨率高
     */
    /**
     * Executes startVLCamera functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     * @param
     * @param pid Parameter for operation (type: Int)
     * @param fps Parameter for operation (type: Int)
     * @param cameraWidth Camera configuration or reference (type: Int)
     * @param cameraHeight Camera configuration or reference (type: Int)
     *
     */
    private fun startVLCamera(
        pid: Int,
        fps: Int,
        cameraWidth: Int,
        cameraHeight: Int,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isDualIR())
            {
                return
            }
        vlUVCCamera =
            /**
             * Executes iruvcdual operation with thermal imaging domain optimization.
             *
             * @param
             * @param object Parameter for operation (type: ConnectCallback {                     override fun onCameraOpened(uvcCamera: UVCCamera)
             * @param ircmd Parameter for operation (type: IRCMD)
             *
             * @return Operation result or configured object (type: if (dualView != null && dualView?.getDualUVCCamera() != null &&                         Const.isDeviceConnected                     ))
             *
             */
            IRUVCDual(
                cameraWidth,
                cameraHeight,
                this,
                pid,
                fps,
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
                        /**
                         * Configures the uvccameraicmd with validation and thermal imaging optimization.
                         *
                         */
                        setUVCCameraICMD(ircmd)
                    }
                },
                IFrameCallback { frame ->
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dualView != null && dualView?.getDualUVCCamera() != null &&
                        Const.isDeviceConnected
                    ) {
                        System.arraycopy(frame, 0, vlData, 0, vlData.size)
                        dualView?.getDualUVCCamera()?.updateFrame(
                            ImageFormat.FLEX_RGB_888, vlData, vlCameraWidth,
                            vlCameraHeight,
                        )
                    }
                },
            )
        vlUVCCamera?.setHandler(mIrHandler)
        vlUVCCamera?.registerUSB()
        vlUVCCamera?.TAG = "mjpeg"
    }

    /**
     * Sets uvccameraicmd configuration.
     */
    private fun setUVCCameraICMD(ircmd: IRCMD) {
        this.ircmd = ircmd
        snStr = getSNStr(ircmd)
        isConfigWait = false
// GetTemperatureDualView().setIrcmd(ircmd)
// PopupCalibration.setIrcmd(ircmd)
// PopupImage.setIrcmd(ircmd)
// PopupOthers.setIrcmd(ircmd)
// GetTemperatureDualView().setIrcmd(ircmd)
// 画arearotationset
// PopupCalibration.setRotate(true)
// PopupImage.setRotate(true)
    }

    /**
     * Executes dualStop functionality.
     */
    /**
     * Executes dualstop operation with thermal imaging domain optimization.
     *
     */
    private fun dualStop() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isDualIR())
            {
                return
            }
        isrun = false
        syncimage.valid = false
        isConfigWait = true
        /**
         * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        getTemperatureDualView().stop()
        USBMonitorManager.getInstance().unregisterUSB()
        ircmd?.onDestroy()
        ircmd = null
        SystemClock.sleep(100)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dualView != null) {
            dualView?.removeFrameCallback(getTemperatureDualView())
            dualView?.dualUVCCamera?.onPausePreview()
            USBMonitorManager.getInstance().stopPreview()
            //
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (vlUVCCamera != null) {
                vlUVCCamera?.unregisterUSB()
                vlUVCCamera?.stopPreview()
                vlUVCCamera = null
            }
            //
            SystemClock.sleep(100)
            dualView?.stopPreview()
            dualView = null
        }
    }

    /**
     * Executes onattach operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice?)
     *
     */
    override fun onAttach(device: UsbDevice?) {
    }

    /**
     * Executes ongranted operation with thermal imaging domain optimization.
     *
     * @param
     * @param usbDevice Parameter for operation (type: UsbDevice?)
     * @param granted Parameter for operation (type: Boolean)
     *
     */
    override fun onGranted(
        usbDevice: UsbDevice?,
        granted: Boolean,
    ) {
    }

    /**
     * Executes ondettach operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice?)
     *
     */
    override fun onDettach(device: UsbDevice?) {
    }

    /**
     * Executes onconnect operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice?)
     * @param ctrlBlock Parameter for operation (type: USBMonitor.UsbControlBlock?)
     * @param createNew Parameter for operation (type: Boolean)
     *
     */
    override fun onConnect(
        device: UsbDevice?,
        ctrlBlock: USBMonitor.UsbControlBlock?,
        createNew: Boolean,
    ) {
        mIrHandler.sendEmptyMessage(Const.HANDLE_CONNECT)
    }

    /**
     * Executes ondisconnect operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice?)
     * @param ctrlBlock Parameter for operation (type: USBMonitor.UsbControlBlock?)
     *
     */
    override fun onDisconnect(
        device: UsbDevice?,
        ctrlBlock: USBMonitor.UsbControlBlock?,
    ) {
    }

    /**
     * Executes oncancel operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice?)
     *
     */
    override fun onCancel(device: UsbDevice?) {
    }

    /**
     * Executes onircmdinit operation with thermal imaging domain optimization.
     *
     * @param
     * @param ircmd Parameter for operation (type: IRCMD?)
     *
     */
    override fun onIRCMDInit(ircmd: IRCMD?) {
        /**
         * Configures the uvccameraicmd with validation and thermal imaging optimization.
         *
         */
        setUVCCameraICMD(ircmd!!)
    }

    /**
     * Executes oncompleteinit operation with thermal imaging domain optimization.
     *
     */
    override fun onCompleteInit() {
        mIrHandler.sendEmptyMessage(Const.HIDE_LOADING)
    }

    /**
     * Executes onsetpreviewsizefail operation with thermal imaging domain optimization.
     *
     */
    override fun onSetPreviewSizeFail() {
        mIrHandler.sendEmptyMessage(Const.SHOW_RESTART_MESSAGE)
    }

预processing后infraredARGBdata 192 * 256 * 4
    protected val preIrARGBData = ByteArray(256 * 192 * 4)
    protected val preIrData = ByteArray(256 * 192 * 2)
    protected val preTempData = ByteArray(256 * 192 * 2)

    /**
     * Executes onirframe operation with thermal imaging domain optimization.
     *
     * @param
     * @param irFrame Parameter for operation (type: ByteArray?)
     *
     */
    override fun onIrFrame(irFrame: ByteArray?): ByteArray {
        /**
@param irFrame 原始infraredYUV422data + temperaturedata 长度 irWidth * irHeight * 2 + irWidth * irHeight * 2
         * @return
         */
        System.arraycopy(irFrame, 0, preIrData, 0, preIrData.size)
        LibIRProcess.convertYuyvMapToARGBPseudocolor(
            preIrData,
            (Const.IR_WIDTH * Const.IR_HEIGHT).toLong(),
            PseudocodeUtils.changePseudocodeModeByOld(pseudoColorMode),
            preIrARGBData,
        )
        return preIrARGBData
    }

    /**
     * Executes switchamplify operation with thermal imaging domain optimization.
     *
     */
    override fun switchAmplify() {
        lifecycleScope.launch {
            /**
             * Executes withcontext operation with thermal imaging domain optimization.
             *
             */
            withContext(Dispatchers.IO) {
                try {
                    SupHelp.getInstance().initA4KCPP()
                } catch (e: UnsatisfiedLinkError) {
                    SupHelp.getInstance().loadOpenclSuccess = false
                    runOnUiThread {
                        TipDialog.Builder(this@BaseIRPlushActivity)
                            .setMessage(R.string.tips_tisr_fail)
                            .setPositiveListener(R.string.app_got_it) { }
                            .create().show()
                    }
                    XLog.e("超分initializationfailed")
                }
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!SupHelp.getInstance().loadOpenclSuccess)
                {
                    return@launch
                }
            isOpenAmplify = !isOpenAmplify
            dualView?.isOpenAmplify = isOpenAmplify

            val titleView = findViewById<com.topdon.lib.core.view.TitleView>(com.topdon.lib.core.R.id.title_view)
            titleView?.setRight2Drawable(if (isOpenAmplify) R.drawable.svg_tisr_on else R.drawable.svg_tisr_off)
            SaveSettingUtil.isOpenAmplify = isOpenAmplify
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isOpenAmplify)
                {
                    ToastUtils.showShort(R.string.tips_tisr_on)
                } else
                {
                    ToastUtils.showShort(R.string.tips_tisr_off)
                }
        }
    }

    /**
     * Initializes the amplify component for thermal imaging operations.
     *
     * @param
     * @param show Parameter for operation (type: Boolean)
     *
     */
    override fun initAmplify(show: Boolean) {
        lifecycleScope.launch {
            val titleView = findViewById<com.topdon.lib.core.view.TitleView>(com.topdon.lib.core.R.id.title_view)
            titleView?.setRight2Drawable(if (isOpenAmplify) R.drawable.svg_tisr_on else R.drawable.svg_tisr_off)
            /**
             * Executes withcontext operation with thermal imaging domain optimization.
             *
             */
            withContext(Dispatchers.IO) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isOpenAmplify)
                    {
                        SupHelp.getInstance().initA4KCPP()
                    }
            }
            dualView?.isOpenAmplify = isOpenAmplify
        }
    }
}
