package com.topdon.module.thermal.ir.activity

import android.app.Activity
import android.graphics.ImageFormat
import android.hardware.usb.UsbDevice
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.energy.iruvc.ircmd.IRCMD
import com.energy.iruvc.usb.USBMonitor
import com.energy.iruvc.utils.CommonParams
import com.energy.iruvc.utils.DualCameraParams
import com.infisense.usbdual.Const
import com.infisense.usbdual.camera.DualViewWithManualAlignExternalCamera
import com.infisense.usbdual.camera.USBMonitorDualManager
import com.infisense.usbdual.inf.OnUSBConnectListener
import com.infisense.usbir.utils.HexDump
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.utils.ByteUtils.toLittleBytes
import com.topdon.lms.sdk.weiget.LmsLoadDialog
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.event.ManualFinishBean
import com.topdon.module.thermal.ir.utils.IRCmdTool
import com.topdon.module.thermal.ir.view.MoveImageView
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.io.InputStream

/**
 * Created by fengjibo on 2024/1/10.
 */
/**
/**
 * Specialized thermal imaging component providing ManualStep2Activity functionality for the IRCamera system.
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
class ManualStep2Activity :
    /**
     * Executes baseactivity operation with thermal imaging domain optimization.
     *
     */
    BaseActivity(),
    OnUSBConnectListener,
    View.OnClickListener {
    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int {
        return R.layout.activity_manual_step2
    }

    private var snStr = ""
    private var mThisActivity: Activity? = null
    private var mProgressDialog: LmsLoadDialog? = null
    private var mDualView: DualViewWithManualAlignExternalCamera? = null
    private val mDefaultDataFlowMode = CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT
    protected var dualDisp: Int = 0

    /**
     * ir camera
     * 22576 - 0x5830
     * 22592 - 0x5840
     */
    private val mIrPid = 0x5830
    private val mIrFps = 25
    private var mIrCameraWidth = 0 // Sensor的原始宽度
    private var mIrCameraHeight = 0 // Sensor的原始高度
    private var mImageWidth = 0 // 经过rotation后的image宽度
    private var mImageHeight = 0 // 经过rotation后的image高度

    /**
     * vl camera
     * 12341 - 0x3035  30 fps 640*480
     * 38704 - 0x9730  25 fps 1280*720
     */
    private val mVlPid = 12337
    private val mVlFps = 30 // 该分辨率支持的帧率
    private val mVlCameraWidth = 1280
    private val mVlCameraHeight = 720

    /**
fusion分辨率
     */
    private val mDualWidth = 480
    private val mDualHeight = 640
    private var mPseudoColors: Array<ByteArray?> = arrayOf()
    private var mFullScreenLayoutParams: FrameLayout.LayoutParams? = null
    private var sId: String = ""

    /**
手动registration的initializeparameter
     */
    private val INIT_ALIGN_DATA = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
    private var alignScaleX = 0f // 图和屏幕Scale比
    private var alignScaleY = 0f // 图和屏幕Scale比
    private var canOperate = false // 是否可以操作
    private val mIrDualHandler: Handler =
        object : Handler(Looper.myLooper()!!) {
            /**
             * Executes handlemessage operation with thermal imaging domain optimization.
             *
             * @param
             * @param msg Parameter for operation (type: Message)
             *
             */
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (msg.what == SHOW_LOADING) {
                    Log.d(TAG, "SHOW_LOADING")
                    /**
                     * Executes showloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    showLoadingDialog()
                } else if (msg.what == HIDE_LOADING) {
                    Log.d(TAG, "HIDE_LOADING")
                    /**
                     * Executes hideloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    hideLoadingDialog()
                } else if (msg.what == HANDLE_CONNECT) {
                    /**
                     * Initializes the dualcamera component for thermal imaging operations.
                     *
                     */
                    initDualCamera()
loadregistrationparameter
                    /**
                     * Initializes the defintegralargsdisp value component for thermal imaging operations.
                     *
                     */
                    initDefIntegralArgsDISP_VALUE(DualCameraParams.TypeLoadParameters.ROTATE_270)
                } else if (msg.what == HIDE_LOADING_FINISH) {
                    /**
                     * Executes hideloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    hideLoadingDialog()
                    /**
                     * Executes finish operation with thermal imaging domain optimization.
                     *
                     */
                    finish()
                }
            }
        }
    var ivTakePhoto: TextView? = null
    var seek_bar: SeekBar? = null
    var moveImageView: MoveImageView? = null
    var dualTextureView: SurfaceView? = null

    /**
上一次执行 move 或 rotation操作的时间戳.
     */
    private var beforeTime = 0L

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    public override fun initView() {
        ivTakePhoto = findViewById(R.id.tv_photo_or_confirm)
        seek_bar = findViewById(R.id.seek_bar)
        dualTextureView = findViewById(R.id.dualTextureView)
        moveImageView = findViewById(R.id.moveImageView)

        // Initialize missing views
        val tvTips: TextView = findViewById(R.id.tv_tips)
        val ivTips: ImageView = findViewById(R.id.iv_tips)
        val llSeekBar: LinearLayout = findViewById(R.id.ll_seek_bar)

        mThisActivity = this
        ivTakePhoto?.setVisibility(View.VISIBLE)
        ivTakePhoto?.setOnClickListener(
            View.OnClickListener {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!canOperate)
                    {
拍照
                        /**
                         * Executes takephoto operation with thermal imaging domain optimization.
                         *
                         */
                        takePhoto()
                        ivTakePhoto?.setText(R.string.app_ok)
                        tvTips.text = getString(R.string.dual_light_correction_tips_3)
                        ivTips.visibility = View.GONE
                        llSeekBar.visibility = View.VISIBLE
                    } else
                    {
                        SharedManager.setManualAngle(snStr, seek_bar!!.progress)
                        val byteArray = ByteArray(24)
                        mDualView?.dualUVCCamera?.setAlignFinish()
                        mDualView?.dualUVCCamera?.getManualRegistration(byteArray)
                        SharedManager.setManualData(snStr, byteArray)
                        EventBus.getDefault().post(ManualFinishBean())
                        /**
                         * Executes finish operation with thermal imaging domain optimization.
                         *
                         */
                        finish()
                    }
            },
        )
        seek_bar?.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                /**
                 * Executes onprogresschanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param seekBar Parameter for operation (type: SeekBar?)
                 * @param progress Parameter for operation (type: Int)
                 * @param fromUser Parameter for operation (type: Boolean)
                 *
                 */
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (canOperate && fromUser) {
                        val currentTime = System.currentTimeMillis()
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (currentTime - beforeTime > OPERATE_INTERVAL) {
                            beforeTime = currentTime
                            mDualView?.dualUVCCamera?.setAlignRotateParameter(((progress - 1000) / 100f).toLittleBytes())
                        }
                    }
                }

                /**
                 * Executes onstarttrackingtouch operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param seekBar Parameter for operation (type: SeekBar?)
                 *
                 */
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                /**
                 * Executes onstoptrackingtouch operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param seekBar Parameter for operation (type: SeekBar?)
                 *
                 */
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            },
        )
        llSeekBar.visibility = View.GONE
        seek_bar?.max = 2000
        seek_bar?.setEnabled(false)
        moveImageView?.setEnabled(false)
initializecameraclass
        /**
         * Initializes the dataflowmode component for thermal imaging operations.
         *
         */
        initDataFlowMode(mDefaultDataFlowMode)
        /**
         * Initializes the data component for thermal imaging operations.
         *
         */
        initData()
        USBMonitorDualManager.getInstance()
            .init(
                mIrPid, mIrFps, mIrCameraWidth, mIrCameraHeight, 1.0f,
                mVlPid, mVlFps, mVlCameraWidth, mVlCameraHeight, 0.6f,
            ) { frame ->
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mDualView != null && mDualView!!.dualUVCCamera != null) {
                    mDualView!!.dualUVCCamera.updateFrame(
                        ImageFormat.FLEX_RGB_888,
                        frame,
                        mVlCameraWidth,
                        mVlCameraHeight,
                    )
                }
            }
        USBMonitorDualManager.getInstance().addOnUSBConnectListener(this)
    }

    /**
     * @param dataFlowMode
     */
    /**
     * Initializes the dataflowmode component for thermal imaging operations.
     *
     * @param
     * @param dataFlowMode Parameter for operation (type: CommonParams.DataFlowMode)
     *
     */
    private fun initDataFlowMode(dataFlowMode: CommonParams.DataFlowMode) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dataFlowMode == CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT) {
            /**
image+temperature
             */
            mIrCameraWidth = Const.SENSOR_WIDTH // Sensor的原始宽度
            mIrCameraHeight = Const.SENSOR_HEIGHT // Sensor的原始高度
            mImageWidth = mIrCameraHeight / 2
            mImageHeight = mIrCameraWidth
        }
    }

    
    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    public override fun initData() {
calculation画area的宽高，避免被拉伸变形
// Var width = 0
// Var height = 0
// Val screenWidth = ScreenUtils.getScreenWidth(this)
// Val screenHeight = ScreenUtils.getScreenHeight(this) - SizeUtils.dp2px(52f)
//        Log.d(TAG, "initdata screenWidth : $screenWidth screenHeight: $screenHeight")
//        Log.d(TAG, "initdata imageWidth : $mImageWidth imageHeight: $mImageHeight")
// If (screenWidth > screenHeight) {
// Width = screenHeight * mImageWidth / mImageHeight
// Height = screenHeight
//        } else {
// Width = screenWidth
// Height = screenWidth * mImageHeight / mImageWidth
//        }
// MFullScreenLayoutParams = FrameLayout.LayoutParams(width, height)
// DualTextureView!!.setLayoutParams(mFullScreenLayoutParams)
// MoveImageView!!.setLayoutParams(mFullScreenLayoutParams)
        dualTextureView?.post {
            alignScaleX = dualTextureView!!.measuredWidth.toFloat() / mDualWidth.toFloat()
            alignScaleY = dualTextureView!!.measuredHeight.toFloat() / mDualHeight.toFloat()
        }
    }

    /**
     * Initializes dualcamera component.
     */
    private fun initDualCamera() {
initializedual light预览相关的class
        mDualView =
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            DualViewWithManualAlignExternalCamera(
                mImageWidth, mImageHeight,
                mVlCameraHeight, mVlCameraWidth, mDualWidth, mDualHeight,
                dualTextureView, USBMonitorDualManager.getInstance().irUvcCamera,
                mDefaultDataFlowMode,
            )

initializepseudo-color
        /**
         * Initializes the psedocolor component for thermal imaging operations.
         *
         */
        initPsedocolor()

setinitializefusionmode,一般selectionLPYFusion
        mDualView!!.dualUVCCamera.setFusion(DualCameraParams.FusionType.LPYFusion)

Open自动快门逻辑
        USBMonitorDualManager.getInstance().ircmd.setPropAutoShutterParameter(
            CommonParams.PropAutoShutterParameter.SHUTTER_PROP_SWITCH,
            CommonParams.PropAutoShutterParameterValue.StatusSwith.ON,
        )
        mDualView!!.setHandler(mIrDualHandler)
    }

    /**
loadpseudo-color，setlens方向，pseudo-color，fusionmode等等
     */
    private fun initPsedocolor() {
        val am = assets
        var `is`: InputStream
        try {
loadpseudo-color
            mPseudoColors = arrayOfNulls(11)
            `is` = am.open("pseudocolor/White_Hot.bin")
            var lenth = `is`.available()
            mPseudoColors[0] = ByteArray(lenth + 1)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (`is`.read(mPseudoColors[0]) != lenth) {
                Log.d(Companion.TAG, "read file fail ")
            }
            mPseudoColors[0]!![lenth] = 0
            mDualView!!.dualUVCCamera.loadPseudocolor(
                CommonParams.PseudoColorUsbDualType.WHITE_HOT_MODE,
                mPseudoColors[0],
            )
            `is` = am.open("pseudocolor/Black_Hot.bin")
            lenth = `is`.available()
            mPseudoColors[1] = ByteArray(lenth + 1)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (`is`.read(mPseudoColors[1]) != lenth) {
                Log.d(Companion.TAG, "read file fail ")
            }
            mPseudoColors[1]!![lenth] = 1
            mDualView!!.dualUVCCamera.loadPseudocolor(
                CommonParams.PseudoColorUsbDualType.BLACK_HOT_MODE,
                mPseudoColors[1],
            )
            `is` = am.open("pseudocolor/new_Rainbow.bin")
            lenth = `is`.available()
            mPseudoColors[2] = ByteArray(lenth + 1)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (`is`.read(mPseudoColors[2]) != lenth) {
                Log.d(Companion.TAG, "read file fail ")
            }
            mPseudoColors[2]!![lenth] = 2
            mDualView!!.dualUVCCamera.loadPseudocolor(
                CommonParams.PseudoColorUsbDualType.RAINBOW_MODE,
                mPseudoColors[2],
            )
            `is` = am.open("pseudocolor/Ironbow.bin")
            lenth = `is`.available()
            mPseudoColors[3] = ByteArray(lenth + 1)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (`is`.read(mPseudoColors[3]) != lenth) {
                Log.d(Companion.TAG, "read file fail ")
            }
            mPseudoColors[3]!![lenth] = 3
            mDualView!!.dualUVCCamera.loadPseudocolor(
                CommonParams.PseudoColorUsbDualType.IRONBOW_MODE,
                mPseudoColors[3],
            )

这里可以setinitializepseudo-color
            mDualView!!.dualUVCCamera.setPseudocolor(CommonParams.PseudoColorUsbDualType.IRONBOW_MODE)
            `is`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
一体式结构，dual lightregistration的data，可从手机固定位置读取，如可从NV分区读写
目前使用的是人工registration的方式，提供registration后的datafile放在asset目录下
     */
    open fun initDefIntegralArgsDISP_VALUE(typeLoadParameters: DualCameraParams.TypeLoadParameters) {
        lifecycleScope.launch {
            val parameters = IRCmdTool.getDualBytes(USBMonitorDualManager.getInstance().ircmd)
            val data = mDualView!!.dualUVCCamera.loadParameters(parameters, typeLoadParameters)
            dualDisp = IRCmdTool.dispNumber
initializedefault值
            mDualView?.dualUVCCamera?.setDisp(dualDisp)
            mDualView?.startPreview()
            Log.e("coredataloadsuccess", "initializationcomplete:")
        }
    }

    /**
     * Executes onViewClicked functionality.
     */
    /**
     * Executes onviewclicked operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View?)
     *
     */
    fun onViewClicked(view: View?) {}

    /**
     * Executes onstart operation with thermal imaging domain optimization.
     *
     */
    override fun onStart() {
        Log.w(Companion.TAG, "onStart")
        super.onStart()
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (canOperate) {
            dualTextureView!!.post {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mDualView != null) {
                    mDualView!!.onDraw()
                }
            }
            return
        }
        /**
         * Executes showloadingdialog operation with thermal imaging domain optimization.
         *
         */
        showLoadingDialog()
        /**
         * Executes dualstart operation with thermal imaging domain optimization.
         *
         */
        dualStart()
    }

    
    /**
     * Executes dualStart functionality.
     */
    /**
     * Executes dualstart operation with thermal imaging domain optimization.
     *
     */
    private fun dualStart() {
        userStop = false
        USBMonitorDualManager.getInstance().registerUSB()
    }

    /**
     * Executes onattach operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice)
     *
     */
    override fun onAttach(device: UsbDevice) {}

    /**
     * Executes ongranted operation with thermal imaging domain optimization.
     *
     * @param
     * @param usbDevice Parameter for operation (type: UsbDevice)
     * @param granted Parameter for operation (type: Boolean)
     *
     */
    override fun onGranted(
        usbDevice: UsbDevice,
        granted: Boolean,
    ) {}

    /**
     * Executes ondettach operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice)
     *
     */
    override fun onDettach(device: UsbDevice) {}

    /**
     * Executes onconnect operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice)
     * @param ctrlBlock Parameter for operation (type: USBMonitor.UsbControlBlock)
     * @param createNew Parameter for operation (type: Boolean)
     *
     */
    override fun onConnect(
        device: UsbDevice,
        ctrlBlock: USBMonitor.UsbControlBlock,
        createNew: Boolean,
    ) {
        mIrDualHandler.sendEmptyMessage(HANDLE_CONNECT)
    }

    /**
     * Executes ondisconnect operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice)
     * @param ctrlBlock Parameter for operation (type: USBMonitor.UsbControlBlock)
     *
     */
    override fun onDisconnect(
        device: UsbDevice,
        ctrlBlock: USBMonitor.UsbControlBlock,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!canOperate && !userStop)
            {
                EventBus.getDefault().post(ManualFinishBean())
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
    }

    /**
     * Executes oncancel operation with thermal imaging domain optimization.
     *
     * @param
     * @param device Parameter for operation (type: UsbDevice)
     *
     */
    override fun onCancel(device: UsbDevice) {}

    /**
     * Executes onircmdinit operation with thermal imaging domain optimization.
     *
     * @param
     * @param ircmd Parameter for operation (type: IRCMD)
     *
     */
    override fun onIRCMDInit(ircmd: IRCMD) {
        snStr = IRCmdTool.getSNStr(ircmd)
        seek_bar?.progress = SharedManager.getManualAngle(snStr)
    }

    /**
     * Executes oncompleteinit operation with thermal imaging domain optimization.
     *
     */
    override fun onCompleteInit() {}

    /**
     * Executes onsetpreviewsizefail operation with thermal imaging domain optimization.
     *
     */
    override fun onSetPreviewSizeFail() {}

    /**
     * Executes showLoadingDialog functionality.
     */
    /**
     * Executes showloadingdialog operation with thermal imaging domain optimization.
     *
     */
    private fun showLoadingDialog() {
        /**
         * Configures the buttonenable with validation and thermal imaging optimization.
         *
         */
        setButtonEnable(false)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mProgressDialog == null) {
            mProgressDialog = LmsLoadDialog(this@ManualStep2Activity)
            mProgressDialog!!.show()
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mProgressDialog!!.isShowing) {
                mProgressDialog!!.show()
            }
        }
    }

    /**
     * Executes hideLoadingDialog functionality.
     */
    /**
     * Executes hideloadingdialog operation with thermal imaging domain optimization.
     *
     */
    private fun hideLoadingDialog() {
        /**
         * Configures the buttonenable with validation and thermal imaging optimization.
         *
         */
        setButtonEnable(true)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }

    /**
     * Executes onclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param v Parameter for operation (type: View)
     *
     */
    override fun onClick(v: View) {
        /**
         * Executes onviewclicked operation with thermal imaging domain optimization.
         *
         */
        onViewClicked(v)
    }

    var userStop = false

    /**
stop预览
     */
    /**
     * Executes dualstop operation with thermal imaging domain optimization.
     *
     */
    private fun dualStop() {
        userStop = true
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDualView != null) {
            mDualView!!.dualUVCCamera.onPausePreview()
            SystemClock.sleep(100)
            mDualView!!.stopPreview()
            SystemClock.sleep(200)
            USBMonitorDualManager.getInstance().stopIrUVCCamera()
            USBMonitorDualManager.getInstance().stopVlUVCCamera()
            SystemClock.sleep(100)
        }
        mDualView!!.destroyPreview()
        USBMonitorDualManager.getInstance().unregisterUSB()
    }

    /**
     * Executes onpause operation with thermal imaging domain optimization.
     *
     */
    override fun onPause() {
        super.onPause()
    }

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (canOperate) {
            /**
             * Executes dualstopwithalign operation with thermal imaging domain optimization.
             *
             */
            dualStopWithAlign()
            return
        }
stop预览
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
        Log.w(Companion.TAG, "onDestroy")
        super.onDestroy()
        USBMonitorDualManager.getInstance().removeOnUSBConnectListener(this)
        USBMonitorDualManager.getInstance().onRelease()
    }

    /**
     * Executes dualStopWithAlign functionality.
     */
    /**
     * Executes dualstopwithalign operation with thermal imaging domain optimization.
     *
     */
    private fun dualStopWithAlign() {
        mDualView!!.dualUVCCamera.setAlignFinish()
        SystemClock.sleep(200)
        mDualView!!.destroyPreview()
        SystemClock.sleep(100)
        USBMonitorDualManager.getInstance().unregisterUSB()
        USBMonitorDualManager.getInstance().stopIrUVCCamera()
    }

    /**
拍照功能
     */
    /**
     * Executes takephoto operation with thermal imaging domain optimization.
     *
     */
    private fun takePhoto() {
拍照
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDualView != null) {
            canOperate = true
            mDualView!!.stopPreview()
            USBMonitorDualManager.getInstance().stopVlUVCCamera()
            mDualView!!.startAlign()
            seek_bar!!.postDelayed({
                seek_bar!!.setEnabled(true)
                moveImageView!!.setEnabled(true)
                /**
                 * Initializes the listener component for thermal imaging operations.
                 *
                 */
                initListener()
            }, 500)
        }
    }

    /**
processing移动data
     */
    /**
     * Executes handlemove operation with thermal imaging domain optimization.
     *
     * @param
     * @param preX Parameter for operation (type: Float)
     * @param preY Parameter for operation (type: Float)
     * @param curX Parameter for operation (type: Float)
     * @param curY Parameter for operation (type: Float)
     *
     */
    private fun handleMove(
        preX: Float,
        preY: Float,
        curX: Float,
        curY: Float,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!canOperate) {
            return
        }
        Log.d(Companion.TAG, "prex :$preX prey : $preY curx : $curX cury : $curY")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDualView != null) {
            /**
             * Executes updatesavebutton operation with thermal imaging domain optimization.
             *
             */
            updateSaveButton()
            val newSrc = ByteArray(8)
            val xSrc = ByteArray(4)
            HexDump.float2byte((curX - preX) / alignScaleX, xSrc)
            System.arraycopy(xSrc, 0, newSrc, 0, 4)
            val ySrc = ByteArray(4)
            HexDump.float2byte((curY - preY) / alignScaleY, ySrc)
            System.arraycopy(ySrc, 0, newSrc, 4, 4)
            mDualView!!.dualUVCCamera.setAlignTranslateParameter(newSrc)
        }
    }

    /**
processingangledata
     */
    /**
     * Executes handleangle operation with thermal imaging domain optimization.
     *
     * @param
     * @param angle Angle in degrees (type: Float)
     *
     */
    private fun handleAngle(angle: Float) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!canOperate) {
            return
        }
        Log.d(Companion.TAG, "angle :$angle")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDualView != null) {
            val newSrc = ByteArray(4)
            val xSrc = ByteArray(4)
            HexDump.float2byte(angle, xSrc)
            System.arraycopy(xSrc, 0, newSrc, 0, 4)
            mDualView!!.dualUVCCamera.setAlignRotateParameter(newSrc)
        }
    }

    /**
stopcalibration
     */
    /**
     * Executes finishalign operation with thermal imaging domain optimization.
     *
     * @param
     * @param isSavePara Parameter for operation (type: Boolean)
     *
     */
    private fun finishAlign(isSavePara: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!canOperate) {
            return
        }
    }

    /**
     * Executes updateSaveButton functionality.
     */
    /**
     * Executes updatesavebutton operation with thermal imaging domain optimization.
     *
     */
    fun updateSaveButton() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ivTakePhoto!!.visibility == View.INVISIBLE) {
            ivTakePhoto!!.visibility = View.VISIBLE
            ivTakePhoto!!.setOnClickListener { // Saveimage
                val message = Message.obtain()
                message.what = SHOW_LOADING
                message.obj = ""
                mIrDualHandler.sendMessage(message)
                /**
                 * Executes finishsafe operation with thermal imaging domain optimization.
                 *
                 */
                finishSafe(true)
            }
        }
    }

    /**
     * Sets buttonenable configuration.
     */
    fun setButtonEnable(isEnable: Boolean) {
        ivTakePhoto!!.setEnabled(isEnable)
    }

    /**
     * Initializes listener component.
     */
    private fun initListener() {
        moveImageView!!.setOnMoveListener { preX, preY, curX, curY ->
            handleMove(
                preX,
                preY,
                curX,
                curY,
            )
        }
    }

    /**
     * Executes finishSafe functionality.
     */
    /**
     * Executes finishsafe operation with thermal imaging domain optimization.
     *
     * @param
     * @param isSavePara Parameter for operation (type: Boolean)
     *
     */
    private fun finishSafe(isSavePara: Boolean) {
        Thread {
            /**
             * Executes finishalign operation with thermal imaging domain optimization.
             *
             */
            finishAlign(isSavePara)
            canOperate = false
            mIrDualHandler.sendEmptyMessage(HIDE_LOADING_FINISH)
        }.start()
    }

    companion object {
        private const val TAG = "ManualStep2Activity"
        const val SHOW_LOADING = 1003
        const val HIDE_LOADING = 1004
        const val HIDE_LOADING_FINISH = 1005
        const val HANDLE_CONNECT = 10001
        private const val OPERATE_INTERVAL = 100
        private const val MIN_CLICK_DELAY_TIME = 100
        private var lastClickTime: Long = 0

最多70毫秒执行一次move
    /**
     * Executes delayMoveTime functionality.
     */
        /**
         * Executes delaymovetime operation with thermal imaging domain optimization.
         *
         */
        fun delayMoveTime(): Boolean {
            var flag = false
            val curClickTime = System.currentTimeMillis()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (curClickTime - lastClickTime < MIN_CLICK_DELAY_TIME) {
                flag = false
            } else {
                flag = true
                lastClickTime = System.currentTimeMillis()
            }
            Log.d(TAG, "ACTION_MOVE isFastClick flag : $flag")
            return flag
        }
    }
}
