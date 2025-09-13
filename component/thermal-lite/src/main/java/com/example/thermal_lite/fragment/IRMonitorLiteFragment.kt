package com.example.thermal_lite.fragment

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.hardware.usb.UsbDevice
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.elvishew.xlog.XLog
import com.energy.ac020library.bean.IrcmdError
import com.energy.commoncomponent.bean.RotateDegree
import com.energy.irutilslibrary.LibIRTempAC020
import com.energy.irutilslibrary.bean.GainStatus
import com.energy.iruvc.sdkisp.LibIRProcess
import com.energy.iruvc.utils.CommonParams
import com.energy.iruvc.utils.Line
import com.energy.iruvc.utils.SynchronizedBitmap
import com.energy.iruvccamera.usb.USBMonitor
import com.example.thermal_lite.R
import com.example.thermal_lite.activity.IRMonitorLiteActivity
import com.example.thermal_lite.camera.CameraPreviewManager
import com.example.thermal_lite.camera.DeviceControlManager
import com.example.thermal_lite.camera.DeviceIrcmdControlManager
import com.example.thermal_lite.camera.OnUSBConnectListener
import com.example.thermal_lite.camera.USBMonitorManager
import com.example.thermal_lite.ui.activity.IrDisplayActivity.HANDLE_INIT_FAIL
import com.example.thermal_lite.ui.activity.IrDisplayActivity.HANDLE_SHOW_FPS
import com.example.thermal_lite.ui.activity.IrDisplayActivity.HANDLE_SHOW_SUN_PROTECT_FLAG
import com.example.thermal_lite.ui.activity.IrDisplayActivity.HANDLE_SHOW_TOAST
import com.example.thermal_lite.ui.activity.IrDisplayActivity.HIDE_LOADING
import com.example.thermal_lite.ui.activity.IrDisplayActivity.PREVIEW_FAIL
import com.example.thermal_lite.ui.activity.IrDisplayActivity.SHOW_LOADING
import com.example.thermal_lite.util.IRTool
import com.infisense.usbir.view.ITsTempListener
import com.infisense.usbir.view.TemperatureView
import com.infisense.usbir.view.TemperatureView.REGION_MODE_LINE
import com.infisense.usbir.view.TemperatureView.REGION_MODE_POINT
import com.infisense.usbir.view.TemperatureView.REGION_MODE_RECTANGLE
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.ktbase.BaseFragment
import com.topdon.module.thermal.ir.bean.DataBean
import com.topdon.module.thermal.ir.bean.SelectPositionBean
import com.topdon.module.thermal.ir.event.ThermalActionEvent
import com.topdon.module.thermal.ir.repository.ConfigRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Specialized thermal imaging component providing IRMonitorLiteFragment functionality for the IRCamera system.
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
class IRMonitorLiteFragment : BaseFragment(), ITsTempListener {
    // View references (migrated from synthetic views)
    lateinit var temperatureView: com.infisense.usbir.view.TemperatureView
    protected lateinit var cameraView: com.topdon.lib.ui.widget.LiteSurfaceView

    private var configJob: Job? = null
    protected var isConfigWait = true
    protected var temperatureBytes = ByteArray(192 * 256 * 2) // Temperaturedata
    var rotateAngle = 270
    private val imageRes = LibIRProcess.ImageRes_t() // 原图尺寸
    val dstTempBytes = ByteArray(192 * 256 * 2)
    private var mProgressDialog: ProgressDialog? = null
    private var temperaturerun = false

    private var mPreviewWidth = 256
    private var mPreviewHeight = 192
    protected var ctrlBlock: USBMonitor.UsbControlBlock? = null
    private var mOnUSBConnectListener: OnUSBConnectListener? = null
    private val syncimage = SynchronizedBitmap()
    var frameReady = false
    private var shutterHandler: Handler? = null
    private var shutterRunnable: Runnable? = null
    private var shutterCount = 0
    protected var isPause = false
    protected var isPick = false

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
        fun newInstance(isPick: Boolean): IRMonitorLiteFragment {
            val fragment = IRMonitorLiteFragment()
            val bundle = Bundle()
            bundle.putBoolean("isPick", isPick)
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int {
        return R.layout.fragment_lite_ir_monitor
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
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize view references
        temperatureView = requireView().findViewById(R.id.temperatureView)
        cameraView = requireView().findViewById(R.id.cameraView)

        lifecycleScope.launch {
            /**
             * Executes showloadingdialog operation with thermal imaging domain optimization.
             *
             */
            showLoadingDialog()
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(1000)
            imageRes.width = 256.toChar()
            imageRes.height = 192.toChar()
            /**
             * Initializes the previewmanager component for thermal imaging operations.
             *
             */
            initPreviewManager()
            /**
             * Initializes the camerasize component for thermal imaging operations.
             *
             */
            initCameraSize()
            /**
             * Initializes the usbmonitormanager component for thermal imaging operations.
             *
             */
            initUSBMonitorManager()
            DeviceControlManager.getInstance().init()
            USBMonitorManager.getInstance().registerMonitor()

            configJob =
                lifecycleScope.launch {
                    /**
                     * Executes while operation with thermal imaging domain optimization.
                     *
                     */
                    while (isConfigWait && isActive) {
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(200)
                    }
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(500)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isPick)
                        {
                            CameraPreviewManager.getInstance().setPseudocolorMode(SaveSettingUtil.pseudoColorMode)
                        } else
                        {
                            CameraPreviewManager.getInstance().setPseudocolorMode(3)
                        }
                    CameraPreviewManager.getInstance().setColorList(null, null, false, 0f, 0f)
                    CameraPreviewManager.getInstance().alarmBean = null
自动快门
                    IRTool.setAutoShutter(true)
initializecontrast
                    IRTool.basicGlobalContrastLevelSet((50).toInt())
镜像
                    IRTool.basicMirrorAndFlipStatusSet(false)
initialize锐度
                    IRTool.basicImageDetailEnhanceLevelSet(50)
                    CameraPreviewManager.getInstance()?.setLimit(
                        Float.MAX_VALUE, Float.MIN_VALUE,
                        0, 0,
                    ) // 自定义颜色
                    shutterHandler = Handler(Looper.getMainLooper())

定义快门操作
    /**
     * Executes takePicture functionality.
     */
                    /**
                     * Executes takepicture operation with thermal imaging domain optimization.
                     *
                     */
                    fun takePicture() {
                        shutterCount++
                        try {
                            IRTool.setOneShutter()
                        } catch (e: RuntimeException) {
                        }
                    }
create Runnable，每5秒执行一次
                    shutterRunnable =
                        object : Runnable {
                            /**
                             * Executes run operation with thermal imaging domain optimization.
                             *
                             */
                            override fun run() {
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (shutterCount < 4) { // 确保只执行前40秒的操作（8次）
                                    shutterHandler?.postDelayed(this, 5000L) // 延迟5秒后再次执行
                                    /**
                                     * Executes takepicture operation with thermal imaging domain optimization.
                                     *
                                     */
                                    takePicture()
                                }
                            }
                        }
starttask
                    shutterHandler?.postDelayed(shutterRunnable!!, 300)
gainmodeinitialize
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(2000) // Sdk的高低gain需要延迟2秒后才能settingssuccess
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.IO) {
                        IRTool.basicGainSet(SaveSettingUtil.temperatureMode)
                    }
                }
        }
    }

    /**
start锅盖矫正流程
     */
    /**
     * Executes autostart operation with thermal imaging domain optimization.
     *
     */
    suspend fun autoStart(): Boolean  {
        return IRTool.autoStart()
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

    /**
     * Executes stopTask functionality.
     */
    /**
     * Executes stoptask operation with thermal imaging domain optimization.
     *
     */
    fun stopTask()  {
        showTask?.cancel()
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
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (requireActivity() is IRMonitorLiteActivity)
            {
                val activity = requireActivity() as IRMonitorLiteActivity
                activity.select(result)
            }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    val mLiteHandler: Handler =
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
                     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    dismissLoadingDialog()
                    frameReady = true
                    isConfigWait = false
                } else if (msg.what == HANDLE_INIT_FAIL) {
                    Log.d(TAG, "HANDLE_INIT_FAIL")
                    /**
                     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    dismissLoadingDialog()
                    Toast.makeText(requireActivity(), "handle init fail !", Toast.LENGTH_LONG).show()
                } else if (msg.what == HANDLE_SHOW_TOAST) {
                    val message = msg.obj as String
                    Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
                } else if (msg.what == PREVIEW_FAIL) {
                    /**
                     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    dismissLoadingDialog()
                    Toast.makeText(requireActivity(), "preview fail !", Toast.LENGTH_LONG).show()
                } else if (msg.what == HANDLE_SHOW_FPS) {
                    val fps = msg.obj as Double
                } else if (msg.what == HANDLE_SHOW_SUN_PROTECT_FLAG) {
                    Toast.makeText(requireActivity(), "Sun protected", Toast.LENGTH_LONG).show()
                }
            }
        }

    /**
initializeUSBconnection相关class
     */
    /**
     * Initializes the usbmonitormanager component for thermal imaging operations.
     *
     */
    private fun initUSBMonitorManager() {
        USBMonitorManager.getInstance().init()
        mOnUSBConnectListener =
            object : OnUSBConnectListener {
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
                 * Executes ondetach operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param device Parameter for operation (type: UsbDevice?)
                 *
                 */
                override fun onDetach(device: UsbDevice?) {
                    /**
                     * Executes requireactivity operation with thermal imaging domain optimization.
                     *
                     */
                    requireActivity().finish()
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
                    this@IRMonitorLiteFragment.ctrlBlock = ctrlBlock
USBconnectionsuccessful后
                    DeviceControlManager.getInstance().handleStartPreview(ctrlBlock)
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
//                DeviceControlManager.getInstance().handleStopPreview()
// Finish()
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
                 * Executes oncompleteinit operation with thermal imaging domain optimization.
                 *
                 */
                override fun onCompleteInit() {
                }
            }
        USBMonitorManager.getInstance()
            .addOnUSBConnectListener(IRMonitorLiteFragment::class.java.name, mOnUSBConnectListener)
    }

    /**
     * Initializes previewmanager component.
     */
    private fun initPreviewManager() {
initialize预览相关的class
        config = ConfigRepository.readConfig(false)
        CameraPreviewManager.getInstance().init(cameraView, mLiteHandler)
        CameraPreviewManager.getInstance().imageRotate = RotateDegree.DEGREE_270
        CameraPreviewManager.getInstance().setOnTempDataChangeCallback { data ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data != null) {
                System.arraycopy(data, 0, temperatureBytes, 0, temperatureBytes.size)
            }
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (rotateAngle) {
                270 -> {
                    LibIRProcess.rotateLeft90(temperatureBytes, imageRes, CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_Y14, dstTempBytes)
                }
                0 -> {
                    LibIRProcess.rotate180(temperatureBytes, imageRes, CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_Y14, dstTempBytes)
                }
                90 -> {
                    LibIRProcess.rotateRight90(temperatureBytes, imageRes, CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_Y14, dstTempBytes)
                }
                180 -> {
                    System.arraycopy(temperatureBytes, 0, dstTempBytes, 0, dstTempBytes.size)
                }
            }
            temperatureView.setTemperature(dstTempBytes)
        }
        temperatureView.setMonitor(true)
        temperatureView.start()
    }

    /**
     * Initializes camerasize component.
     */
    private fun initCameraSize() {
        temperatureView.setTextSize(SaveSettingUtil.tempTextSize)
        temperatureView.setSyncimage(syncimage)
calculation画area的宽高，避免被拉伸变形
        temperatureView.setTemperature(dstTempBytes)
        temperatureView.setUseIRISP(false)
初始全局temperature measurement
        temperatureView.post {
            lifecycleScope.launch {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!temperaturerun) {
                    temperaturerun = true
需等待renderingcomplete再display
                    temperatureView.visibility = View.VISIBLE
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(1000)
                    temperatureView.setImageSize(mPreviewHeight, mPreviewWidth, this@IRMonitorLiteFragment)
                    temperatureView.temperatureRegionMode = TemperatureView.REGION_MODE_CLEAN // 全屏temperature measurement
                }
            }
        }
    }

    /**
     * Processes temperature measurement data.
     */
    fun restTempView()  {
        temperatureView.restView()
        temperatureView.clear()
    }

    /**
drawingpointlinearea
     */
    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @param
     * @param selectBean Parameter for operation (type: SelectPositionBean)
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    fun addTempLine(selectBean: SelectPositionBean) {
        temperatureView.visibility = View.VISIBLE
        temperatureView.isEnabled = false
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
     * Executes onstart operation with thermal imaging domain optimization.
     *
     */
    override fun onStart() {
        super.onStart()
    }

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isPause)
            {
                DeviceControlManager.getInstance().handleResumeDualPreview()
                isPause = false
            }
    }

    /**
     * Executes onpause operation with thermal imaging domain optimization.
     *
     */
    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        isPause = true
        DeviceControlManager.getInstance().handlePauseDualPreview()
    }

    /**
     * Executes closeFragment functionality.
     */
    /**
     * Executes closefragment operation with thermal imaging domain optimization.
     *
     */
    fun closeFragment()  {
        try {
            DeviceControlManager.getInstance().handlePauseDualPreview()
            DeviceControlManager.getInstance().handleStopPreview()
            USBMonitorManager.getInstance().unregisterMonitor()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mOnUSBConnectListener != null) {
                USBMonitorManager.getInstance()
                    .removeOnUSBConnectListener(IRMonitorLiteFragment::class.java.name)
                mOnUSBConnectListener = null
            }
            USBMonitorManager.getInstance().destroyMonitor()
            DeviceControlManager.getInstance().release()
            CameraPreviewManager.getInstance().releaseSource()
        } catch (e: Exception) {
            XLog.e("$TAG:litedestroyexception--${e.message}")
        }
    }

    /**
     * Executes ondestroyview operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        temperatureView.stop()
        shutterRunnable?.let {
            shutterHandler?.removeCallbacks(it)
        }
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mOnUSBConnectListener != null) {
                DeviceControlManager.getInstance().handleStopPreview()
                USBMonitorManager.getInstance().unregisterMonitor()
                USBMonitorManager.getInstance()
                    .removeOnUSBConnectListener(IRMonitorLiteFragment::class.java.name)
                mOnUSBConnectListener = null
                USBMonitorManager.getInstance().destroyMonitor()
                DeviceControlManager.getInstance().release()
                CameraPreviewManager.getInstance().releaseSource()
            }
        } catch (e: Exception) {
            XLog.e("$TAG:litedestroyexception--${e.message}")
        }
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
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isConfigWait)
                {
                    return temp!!
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
                "temp correct, oldTemp = " + params_array[0] + " newtemp = " + tempNew +
                    " ems = " + params_array[1] + " ta = " + params_array[2] + " " +
                    "distance = " + params_array[4] + " hum = " + params_array[5] + " basicGain = " + basicGainGetValue[0],
            )
        } catch (e: Exception) {
            XLog.e("$TAG--temperature修正exception：${e.message}")
        } finally {
            return tempNew ?: 0f
        }
    }

    /**
     * Retrieves bitmap information.
     */
    fun getBitmap(): Bitmap  {
        return Bitmap.createScaledBitmap(
            CameraPreviewManager.getInstance().scaledBitmap(true),
            cameraView!!.width,
            cameraView!!.height,
            true,
        )
    }
}
