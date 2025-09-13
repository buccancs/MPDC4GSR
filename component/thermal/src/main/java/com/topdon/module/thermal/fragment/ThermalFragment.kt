package com.topdon.module.thermal.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import java.math.RoundingMode
import com.blankj.utilcode.util.ScreenUtils
import com.guide.zm04c.matrix.GuideInterface
import com.guide.zm04c.matrix.IrSurfaceView
// Import com.tbruyelle.rxpermissions2.RxPermissions // Temporarily disabled - dependency not available
// Import com.topdon.lib.core.bean.tools.ScreenBean // Temporarily disabled - utility class
import com.topdon.lib.core.tools.ToastTools
import com.topdon.lib.core.utils.ByteUtils.getIndex
// Import com.topdon.lib.core.utils.ScreenShotUtils // Temporarily disabled - utility class
// Import com.topdon.lib.ui.dialog.SeekDialog // Temporarily disabled - class not available
import com.topdon.lib.ui.dialog.ThermalInputDialog
import com.topdon.lib.ui.fence.FenceLineView
import com.topdon.lib.ui.fence.FencePointView
import com.topdon.lib.ui.fence.FenceView
import com.topdon.module.thermal.R
import com.topdon.module.thermal.base.BaseThermalFragment
import com.topdon.module.thermal.fragment.event.ThermalActionEvent
import com.topdon.module.thermal.tools.Fence
import com.topdon.module.thermal.tools.ThermalTool
import com.topdon.module.thermal.tools.medie.IYapVideoProvider
import com.topdon.module.thermal.tools.medie.YapVideoEncoder
import com.topdon.module.thermal.utils.ArrayUtils
import com.topdon.module.thermal.viewmodel.ThermalViewModel
import com.topdon.lib.ui.R as LibUiR
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.math.BigDecimal

/**
thermal imaging
 */
/**
/**
 * Specialized thermal imaging component providing ThermalFragment functionality for the IRCamera system.
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
class ThermalFragment : BaseThermalFragment(), IYapVideoProvider<Bitmap> {
    private val viewModel: ThermalViewModel by viewModels()

    protected var mIrSurfaceViewLayout: FrameLayout? = null
    protected var mIrSurfaceView: IrSurfaceView? = null

    private val msgLiveData by lazy { MutableLiveData<Int>() }

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.fragment_thermal

settemperature展示的位置
    /**
     * Sets viewposition configuration.
     */
    private fun setViewPosition(
        imageView: ImageView,
        index: Int,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rawWidth == 0 || rawHeight == 0) {
            return
        }
        val vg = imageView.parent as ViewGroup
        val pw = vg.width
        val ph = vg.height
        val y = index / rawWidth
        val x = index - y * rawWidth
        val x1 = x * pw / rawWidth
        val y1 = y * ph / rawHeight
        val maxX = x1 - imageView.width / 2
        val maxY = y1 - imageView.height / 2
Log.w("123", "真实位置 maxX:$maxX, maxY:$maxY")
        imageView.x = maxX.toFloat()
        imageView.y = maxY.toFloat()
    }

    private var mGuideInterface: GuideInterface? = null

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
        rotateType = 3 // Defaultrotation270度
        mCenterTextView = requireView().findViewById(R.id.temp_display)
        mMaxTextView = requireView().findViewById(R.id.max_temp_display)
        mMinTextView = requireView().findViewById(R.id.min_temp_display)
        maxImg = requireView().findViewById(R.id.max_img)
        minImg = requireView().findViewById(R.id.min_img)
        mDisplayFrameLayout = requireView().findViewById(R.id.temp_display_layout)
        mFenceLayout = requireView().findViewById(R.id.fence_lay)
        mCameraLayout = requireView().findViewById(R.id.temp_camera_layout)
        mDisplayFrameLayout!!.visibility = View.GONE
        mFenceLayout!!.visibility = View.GONE
        mIrSurfaceViewLayout = requireView().findViewById(R.id.final_ir_layout)
        mIrSurfaceView = IrSurfaceView(requireContext())
        val ifrSurfaceViewLayoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.CENTER,
            )
        mIrSurfaceView!!.layoutParams = ifrSurfaceViewLayoutParams
        mIrSurfaceView!!.setMatrix(ThermalTool.getRotate(rotateType), 256f, 192f)
        mIrSurfaceViewLayout!!.addView(mIrSurfaceView)
        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = screenWidth * 192 / 256
        width = screenWidth
        height = screenHeight
        highCrossWidth = resources.getDimension(R.dimen.high_cross_width).toInt()
        highCrossHeight = resources.getDimension(R.dimen.high_cross_height).toInt()
        mIrSurfaceViewLayout!!.viewTreeObserver.addOnGlobalLayoutListener {
            irSurfaceViewLayoutParams =
                mIrSurfaceViewLayout!!.layoutParams as ConstraintLayout.LayoutParams?
            displayViewLayoutParams = mDisplayFrameLayout!!.layoutParams as FrameLayout.LayoutParams
            fenceLayoutParams = mFenceLayout!!.layoutParams as FrameLayout.LayoutParams
            cameraLayoutParams = mCameraLayout!!.layoutParams as FrameLayout.LayoutParams
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (rotateType) {
                1, 3 -> {
                    irSurfaceViewWidth = height
                    irSurfaceViewHeight = width
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (irSurfaceViewWidth < width) {
                        irSurfaceViewWidth = width
                        irSurfaceViewHeight = screenWidth * 256 / 192
                    }
                }
                0, 2 -> {
                    irSurfaceViewWidth = width
                    irSurfaceViewHeight = height
                }
            }
            irSurfaceViewLayoutParams!!.width = irSurfaceViewWidth
            irSurfaceViewLayoutParams!!.height = irSurfaceViewHeight
            mIrSurfaceViewLayout!!.layoutParams = irSurfaceViewLayoutParams

            displayViewLayoutParams!!.width = irSurfaceViewWidth
            displayViewLayoutParams!!.height = irSurfaceViewHeight
            mDisplayFrameLayout!!.layoutParams = displayViewLayoutParams

            fenceLayoutParams!!.width = irSurfaceViewWidth
            fenceLayoutParams!!.height = irSurfaceViewHeight
            mFenceLayout!!.layoutParams = fenceLayoutParams

            cameraLayoutParams!!.width = irSurfaceViewWidth
            cameraLayoutParams!!.height = irSurfaceViewHeight
            mFenceLayout!!.layoutParams = cameraLayoutParams
        }
初始选取range
        /**
         * Initializes the fence component for thermal imaging operations.
         *
         */
        initFence()
初始image
        /**
         * Executes onirvideostart operation with thermal imaging domain optimization.
         *
         */
        onIrVideoStart()
        mIrSurfaceView!!.post {
            Log.w("123", "w:${mIrSurfaceView!!.width}, h:${mIrSurfaceView!!.height}")
        }

        msgLiveData.observe(this) { msg ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (msg == 0) {
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (selectType) {
                    0 -> {
                        mCenterTextView!!.visibility = View.VISIBLE
                        mMaxTextView!!.visibility = View.VISIBLE
                        mMinTextView!!.visibility = View.VISIBLE
                        mCenterTextView!!.text = "center温 $mCenter"
                        mMaxTextView!!.text = "maximum温 $mMaxTemp"
                        mMinTextView!!.text = "minimum温 $mMinTemp"
                        maxImg!!.visibility = View.GONE
                        minImg!!.visibility = View.GONE
                    }
                    1 -> {
                        mCenterTextView!!.visibility = View.VISIBLE
                        mMaxTextView!!.visibility = View.GONE
                        mMinTextView!!.visibility = View.GONE
                        mCenterTextView!!.text = "temperature $mMaxTemp"
                        maxImg!!.visibility = View.GONE
                        minImg!!.visibility = View.GONE
                    }
                    else -> {
                        mCenterTextView!!.visibility = View.VISIBLE
                        mMaxTextView!!.visibility = View.VISIBLE
                        mMinTextView!!.visibility = View.VISIBLE
                        mCenterTextView!!.text = "center温 $mCenter"
                        mMaxTextView!!.text = "maximum温 $mMaxTemp"
                        mMinTextView!!.text = "minimum温 $mMinTemp"
                        maxImg!!.visibility = View.VISIBLE
                        minImg!!.visibility = View.VISIBLE
                        maxImg?.let { setViewPosition(it, maxIndex) }
                        minImg?.let { setViewPosition(it, minIndex) }
                    }
                }
            }
        }
        /**
         * Handles temperature measurement and calibration with precision thermal data processing.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        onTempBtnClick()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Executes ondestroyview operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        /**
         * Executes onirvideostop operation with thermal imaging domain optimization.
         *
         */
        onIrVideoStop()
    }

    /**
enabledvideo流
     */
    /**
     * Executes onirvideostart operation with thermal imaging domain optimization.
     *
     */
    fun onIrVideoStart() {
        mIsIrVideoStart =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mIsIrVideoStart) {
                ToastTools.showShort("video流已开启")
                return
            } else {
                true
            }
        mGuideInterface = GuideInterface()
        val ret =
            mGuideInterface!!.init(
                /**
                 * Executes requirecontext operation with thermal imaging domain optimization.
                 *
                 */
                requireContext(),
                object : GuideInterface.IrDataCallback {
                    /**
                     * Executes processirdata operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param yuv Parameter for operation (type: ByteArray)
                     * @param temp Temperature value in Celsius (type: FloatArray)
                     *
                     */
                    override fun processIrData(
                        yuv: ByteArray,
                        temp: FloatArray,
                    ) {
refreshimage
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (mIrBitmap == null) {
                            mIrBitmap = Bitmap.createBitmap(256, 192, Bitmap.Config.ARGB_8888)
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (upValue > downValue) {
                            viewModel.yuvArea(yuv, temp, upValue, downValue)
                        }
                        mGuideInterface!!.yuv2Bitmap(mIrBitmap, yuv) // Video转码yuv
// MIrBitmap = mIrBitmap?.let { rotateBitmap(it, 90f) }
                        try {
                            mIrSurfaceView!!.doDraw(mIrBitmap, mGuideInterface!!.getImageStatus())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (rotateType == 1 || rotateType == 3) {
                            rawWidth = SRC_WIDTH
                            rawHeight = SRC_HEIGHT
                        } else {
                            rawWidth = SRC_HEIGHT
                            rawHeight = SRC_WIDTH
                        }
                        val centerIndex = rawWidth * (rawHeight / 2) + rawWidth / 2
                        try {
选取region
calculation选取指定point
                            val maxTempIndex = ArrayUtils.getMaxIndex(temp, rotateType, selectIndex)
                            val minTempIndex = ArrayUtils.getMinIndex(temp, rotateType, selectIndex)
                            maxIndex = maxTempIndex
                            minIndex = minTempIndex
rotation后的temperaturearray
                            val rotateData = ArrayUtils.matrixRotate(srcData = temp, rotateType)
calculation出temperature
                            val bigDecimal = BigDecimal.valueOf(rotateData[centerIndex].toDouble())
                            val maxBigDecimal = BigDecimal.valueOf(rotateData[maxTempIndex].toDouble())
                            val minBigDecimal = BigDecimal.valueOf(rotateData[minTempIndex].toDouble())
                            mCenter = bigDecimal.setScale(1, RoundingMode.HALF_UP).toFloat()
                            mMaxTemp = maxBigDecimal.setScale(1, RoundingMode.HALF_UP).toFloat()
                            mMinTemp = minBigDecimal.setScale(1, RoundingMode.HALF_UP).toFloat()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e(TAG, "提取temperatureexception:${e.message}")
                        }
                    }
                },
            )

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ret == 5) {
            Log.w("123", "video流开启complete")
        } else {
            ToastTools.showShort("video流开启failed")
        }
    }

    /**
     * Executes rotateBitmap functionality.
     */
    /**
     * Executes rotatebitmap operation with thermal imaging domain optimization.
     *
     * @param
     * @param origin Parameter for operation (type: Bitmap)
     * @param rotate Parameter for operation (type: Float)
     *
     */
    private fun rotateBitmap(
        origin: Bitmap,
        rotate: Float,
    ): Bitmap? {
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (origin == null) {
                return null
            }
            val width = origin.width
            val height = origin.height
            val matrix = Matrix()
            matrix.setRotate(rotate)
            val newBitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (newBitmap.equals(origin)) {
                return newBitmap
            }
            origin.recycle()
            return newBitmap
        } catch (e: Exception) {
            Log.e("123", "error:${e.message}")
            return origin
        }
    }

    /**
stopvideo流
     */
    /**
     * Executes onirvideostop operation with thermal imaging domain optimization.
     *
     */
    fun onIrVideoStop() {
        mIsIrVideoStart =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mIsIrVideoStart) {
                ToastTools.showShort("video流已stop")
                return
            } else {
                false
            }
        mGuideInterface!!.exit()
        mGuideInterface = null
        ToastTools.showShort("video流stopcomplete")
    }

    /**
     * Executes onLowRangeBtnClick functionality.
     */
    /**
     * Executes onlowrangebtnclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View?)
     *
     */
    fun onLowRangeBtnClick(view: View?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mGuideInterface == null) {
            ToastTools.showShort("请先开启video流")
            return
        }
        mGuideInterface!!.setRange(1)
        ToastTools.showShort("switch到常温档success")
    }

    /**
     * Executes onHighRangeBtnClick functionality.
     */
    /**
     * Executes onhighrangebtnclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View?)
     *
     */
    fun onHighRangeBtnClick(view: View?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mGuideInterface == null) {
            ToastTools.showShort("请先开启video流")
            return
        }
        mGuideInterface!!.setRange(2)
        ToastTools.showShort("switch到高温档success")
    }

    /**
temperaturedisplay
     */
    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    fun onTempBtnClick() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mGuideInterface == null) {
            ToastTools.showShort("请先开启video流")
            return
        }
        isDispLayTemp = !isDispLayTemp
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isDispLayTemp) {
            mDisplayFrameLayout!!.visibility = View.VISIBLE
            timerJob =
                lifecycleScope.launch {
                    /**
                     * Executes repeat operation with thermal imaging domain optimization.
                     *
                     */
                    repeat(Int.MAX_VALUE) {
                        msgLiveData.postValue(0)
                        /**
                         * Executes delay operation with thermal imaging domain optimization.
                         *
                         */
                        delay(1000)
                    }
                }
        } else {
            mDisplayFrameLayout!!.visibility = View.GONE
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (timerJob != null && timerJob!!.isActive) {
                timerJob!!.cancel()
                timerJob = null
            }
        }
    }

    private var upValue = 0f
    private var downValue = 0f

    /**
     * Executes addLimit functionality.
     */
    /**
     * Executes addlimit operation with thermal imaging domain optimization.
     *
     */
    private fun addLimit() {
        ThermalInputDialog.Builder(requireContext())
            .setMessage("请settingstemperature限值")
            .setPositiveListener(LibUiR.string.app_confirm) { up, down, _, _ ->
                ToastTools.showShort("settings上限:$up, 下限:$down")
                upValue = up
                downValue = down
            }
            .setCancelListener(LibUiR.string.app_cancel)
            .create().show()
    }

***************************************专家mode**********************************************

    /**
专家mode
     */
    /**
     * Executes onexpertmodeclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View?)
     *
     */
    fun onExpertModeClick(view: View?) {
        System.arraycopy(EXPERT_HITS, 1, EXPERT_HITS, 0, EXPERT_HITS.size - 1)
        EXPERT_HITS[EXPERT_HITS.size - 1] = System.currentTimeMillis()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (EXPERT_HITS[0] >= System.currentTimeMillis() - EXPERT_MODE_HIT_DURATION) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mExpertLayout!!.visibility == View.GONE) {
                mExpertLayout!!.visibility = View.VISIBLE
            } else {
                mExpertLayout!!.visibility = View.GONE
            }
            EXPERT_HITS = LongArray(EXPERT_MODE_HIT_COUNT)
        }
    }

    /**
     * Executes onNucShutterClick functionality.
     */
    /**
     * Executes onnucshutterclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View?)
     *
     */
    fun onNucShutterClick(view: View?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mGuideInterface == null) {
            ToastTools.showShort("请先开启video流")
            return
        }
        mGuideInterface!!.nuc()
    }

// Private fun showTipDialog(tip: String, type: Int) {
// Val tipDialog = TipDialog.Builder(requireContext())
//            .setIconType(type)
//            .setTipWord(tip)
//            .create()
// TipDialog.show()
// MHandler.postDelayed({
// Try {
// TipDialog.dismiss()
//            } catch (e: Exception) {
// E.printStackTrace()
//            }
//        }, 1500)
//    }

    /**
     * Executes onLut functionality.
     */
    /**
     * Executes onlut operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     *
     */
    fun onLut(view: View) {
        mIrSurfaceView!!.setOpenLut()
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
        Log.w("123", "event:${event.action}")
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            1001 -> {
拍照
                ToastTools.showShort("拍照")
                /**
                 * Executes picture operation with thermal imaging domain optimization.
                 *
                 */
                picture()
            }
            1002 -> {
recording
                ToastTools.showShort("recording")
                /**
                 * Executes video operation with thermal imaging domain optimization.
                 *
                 */
                video()
            }
            2001 -> {
addpoint
                /**
                 * Executes clearfenceui operation with thermal imaging domain optimization.
                 *
                 */
                clearFenceUI()
                /**
                 * Executes addpoint operation with thermal imaging domain optimization.
                 *
                 */
                addPoint()
            }
            2002 -> {
addline
                /**
                 * Executes clearfenceui operation with thermal imaging domain optimization.
                 *
                 */
                clearFenceUI()
                /**
                 * Executes addline operation with thermal imaging domain optimization.
                 *
                 */
                addLine()
            }
            2003 -> {
add围栏
                /**
                 * Executes clearfenceui operation with thermal imaging domain optimization.
                 *
                 */
                clearFenceUI()
                /**
                 * Executes addfence operation with thermal imaging domain optimization.
                 *
                 */
                addFence()
            }
            2004 -> {
addtemperature
// OnTempBtnClick()
                /**
                 * Executes addlimit operation with thermal imaging domain optimization.
                 *
                 */
                addLimit()
            }
            2006 -> {
Clear还原
                /**
                 * Executes clearfence operation with thermal imaging domain optimization.
                 *
                 */
                clearFence()
            }
            in 3000..3010 -> {
setpseudo-color
                /**
                 * Configures the color with validation and thermal imaging optimization.
                 *
                 */
                setColor(event.action)
            }
            4001 -> {
rotation
                /**
                 * Executes rotate operation with thermal imaging domain optimization.
                 *
                 */
                rotate()
                /**
                 * Executes clearfence operation with thermal imaging domain optimization.
                 *
                 */
                clearFence()
            }
            4002 -> {
imageEnhance
                /**
                 * Executes enhance operation with thermal imaging domain optimization.
                 *
                 */
                enhance()
            }
            4003 -> {
imageEnhance
                /**
                 * Manages thermal camera operations with hardware-optimized performance and error handling.
                 *
                 */
                camera()
            }
            in 5000..5010 -> {
全屏
                ToastTools.showShort("全屏")
            }
        }
    }

复位
    /**
     * Executes clearFence functionality.
     */
    /**
     * Executes clearfence operation with thermal imaging domain optimization.
     *
     */
    private fun clearFence() {
        /**
         * Executes clearfenceui operation with thermal imaging domain optimization.
         *
         */
        clearFenceUI()
temperature限值
        upValue = 0f
        downValue = 0f
        selectType = 0
    }

    /**
     * Executes clearFenceUI functionality.
     */
    /**
     * Executes clearfenceui operation with thermal imaging domain optimization.
     *
     */
    private fun clearFenceUI() {
        mFenceLayout!!.visibility = View.GONE
        fenceFlag = 0x000
        selectIndex.clear()
        /**
         * Executes requireview operation with thermal imaging domain optimization.
         *
         */
        requireView().findViewById<com.topdon.lib.ui.fence.FenceView>(R.id.fence_view).clear()
        /**
         * Executes requireview operation with thermal imaging domain optimization.
         *
         */
        requireView().findViewById<com.topdon.lib.ui.fence.FenceLineView>(R.id.fence_line_view).clear()
        /**
         * Executes requireview operation with thermal imaging domain optimization.
         *
         */
        requireView().findViewById<com.topdon.lib.ui.fence.FencePointView>(R.id.fence_point_view).clear()
    }

    /**
setpseudo-color
     */
    /**
     * Configures the color with validation and thermal imaging optimization.
     *
     * @param
     * @param action Parameter for operation (type: Int)
     *
     */
    private fun setColor(action: Int) {
        var type: Int = action % 3000 - 1
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (type < 0 || type > 10) {
            type = 0
        }
        /**
         * Executes updatepalette operation with thermal imaging domain optimization.
         *
         */
        updatePalette(type) // Default2
    }

    /**
色带
     */
    /**
     * Executes updatepalette operation with thermal imaging domain optimization.
     *
     * @param
     * @param index Parameter for operation (type: Int)
     *
     */
    private fun updatePalette(index: Int) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mGuideInterface == null) {
            ToastTools.showShort("请先开启video流")
            return
        }
        mGuideInterface!!.changePalette(index)
    }

    var fenceFlag = 0x000

    /**
     * Executes addPoint functionality.
     */
    /**
     * Executes addpoint operation with thermal imaging domain optimization.
     *
     */
    private fun addPoint() {
        /**
         * Executes showfence operation with thermal imaging domain optimization.
         *
         */
        showFence(1)
    }

    /**
     * Executes addLine functionality.
     */
    /**
     * Executes addline operation with thermal imaging domain optimization.
     *
     */
    private fun addLine() {
        /**
         * Executes showfence operation with thermal imaging domain optimization.
         *
         */
        showFence(2)
    }

    /**
     * Executes addFence functionality.
     */
    /**
     * Executes addfence operation with thermal imaging domain optimization.
     *
     */
    private fun addFence() {
        /**
         * Executes showfence operation with thermal imaging domain optimization.
         *
         */
        showFence(3)
    }

displaypointlinearea布局
    /**
     * Executes showFence functionality.
     */
    /**
     * Executes showfence operation with thermal imaging domain optimization.
     *
     * @param
     * @param index Parameter for operation (type: Int)
     *
     */
    private fun showFence(index: Int) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (fenceFlag.getIndex(index) == 0) {
            fenceFlag = 1.shl(4 * (index - 1)) // Settings001 or 010 or 100
            mFenceLayout!!.visibility = View.VISIBLE
            /**
             * Executes requireview operation with thermal imaging domain optimization.
             *
             */
            requireView().findViewById<com.topdon.lib.ui.fence.FencePointView>(R.id.fence_point_view).visibility = if (fenceFlag.getIndex(1) > 0) View.VISIBLE else View.GONE
            /**
             * Executes requireview operation with thermal imaging domain optimization.
             *
             */
            requireView().findViewById<com.topdon.lib.ui.fence.FenceLineView>(R.id.fence_line_view).visibility = if (fenceFlag.getIndex(2) > 0) View.VISIBLE else View.GONE
            /**
             * Executes requireview operation with thermal imaging domain optimization.
             *
             */
            requireView().findViewById<com.topdon.lib.ui.fence.FenceView>(R.id.fence_view).visibility = if (fenceFlag.getIndex(3) > 0) View.VISIBLE else View.GONE
        } else {
            fenceFlag = 0x000
            mFenceLayout!!.visibility = View.GONE
        }
    }

    private var selectType = 0
    private var selectIndex: ArrayList<Int> = arrayListOf()

    /**
     * Initializes fence component.
     */
    private fun initFence() {
        requireView().findViewById<com.topdon.lib.ui.fence.FencePointView>(R.id.fence_point_view).listener =
            object : FencePointView.CallBack {
                /**
                 * Executes callback operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param startPoint Parameter for operation (type: IntArray)
                 * @param srcRect Parameter for operation (type: IntArray)
                 *
                 */
                override fun callback(
                    startPoint: IntArray,
                    srcRect: IntArray,
                ) {
getpoint
                    selectType = 1
                    selectIndex =
                        /**
                         * Executes fence operation with thermal imaging domain optimization.
                         *
                         */
                        Fence(srcRect = srcRect, rotateType = rotateType).getPointIndex(startPoint)
                }
            }
        /**
         * Executes requireview operation with thermal imaging domain optimization.
         *
         */
        requireView().findViewById<com.topdon.lib.ui.fence.FenceLineView>(R.id.fence_line_view).listener =
            object : FenceLineView.CallBack {
                /**
                 * Executes callback operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param startPoint Parameter for operation (type: IntArray)
                 * @param endPoint Parameter for operation (type: IntArray)
                 * @param srcRect Parameter for operation (type: IntArray)
                 *
                 */
                override fun callback(
                    startPoint: IntArray,
                    endPoint: IntArray,
                    srcRect: IntArray,
                ) {
getline
                    selectType = 2
                    selectIndex =
                        /**
                         * Executes fence operation with thermal imaging domain optimization.
                         *
                         */
                        Fence(srcRect = srcRect, rotateType = rotateType)
                            .getLineIndex(startPoint, endPoint)
                }
            }
        /**
         * Executes requireview operation with thermal imaging domain optimization.
         *
         */
        requireView().findViewById<com.topdon.lib.ui.fence.FenceView>(R.id.fence_view).listener =
            object : FenceView.CallBack {
                /**
                 * Executes callback operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param startPoint Parameter for operation (type: IntArray)
                 * @param endPoint Parameter for operation (type: IntArray)
                 * @param srcRect Parameter for operation (type: IntArray)
                 *
                 */
                override fun callback(
                    startPoint: IntArray,
                    endPoint: IntArray,
                    srcRect: IntArray,
                ) {
getarea
                    selectType = 3
                    selectIndex =
                        /**
                         * Executes fence operation with thermal imaging domain optimization.
                         *
                         */
                        Fence(srcRect = srcRect, rotateType = rotateType)
                            .getAreaIndex(startPoint, endPoint)
                }
            }
    }

    /**
     * Executes picture functionality.
     */
    /**
     * Executes picture operation with thermal imaging domain optimization.
     *
     */
    private fun picture() {
//        ScreenShotUtils.shotScreen(requireContext(), temp_display_lay, 1, ScreenBean())
        // Note: ScreenShotUtils functionality requires integration with screenshot utility module
        // ScreenShotUtils.shotScreenBitmap(requireContext(), mIrBitmap, 1, ScreenBean())
    }

    var isVideoRunning = false

    /**
     * Executes video functionality.
     */
    /**
     * Executes video operation with thermal imaging domain optimization.
     *
     */
    private fun video() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isVideoRunning) {
            Log.w("123", "正在recording")
            return
        }
        // Note: FileConfig.galleryPath requires integration with file configuration module
        // Val latestResultPath = "${FileConfig.galleryPath}YapBitmapToMp4_${System.currentTimeMillis()}.mp4"
        val latestResultPath = "/tmp/YapBitmapToMp4_${System.currentTimeMillis()}.mp4" // Temporary fallback
        Log.w("123", "latestResultPath:$latestResultPath")
        /**
         * Executes yapvideoencoder operation with thermal imaging domain optimization.
         *
         */
        YapVideoEncoder(this, File(latestResultPath)).start()
    }

rotation
    /**
     * Executes rotate functionality.
     */
    /**
     * Executes rotate operation with thermal imaging domain optimization.
     *
     */
    private fun rotate() {
        rotateType = if (rotateType >= 3) 0 else rotateType + 1
        mIrSurfaceView!!.setMatrix(ThermalTool.getRotate(rotateType), 256f, 192f)
        ToastTools.showShort("rotation:${ThermalTool.getRotate(rotateType)}度")
    }

imageEnhance
    /**
     * Executes enhance functionality.
     */
    /**
     * Executes enhance operation with thermal imaging domain optimization.
     *
     */
    private fun enhance() {
        mIrSurfaceView!!.setOpenLut()
        val saturation = mIrSurfaceView?.getSaturationValue() ?: 0
        // Note: SeekDialog functionality requires integration with dialog utility module
        /*
        SeekDialog.Builder(requireContext())
            .setMessage(LibUiR.string.thermal_enhance)
            .setSaturation(saturation)
            .setPositiveListener(LibUiR.string.app_confirm) { value: Int ->
                mIrSurfaceView?.setSaturationValue(value)// Settingscontrast
            }
            .setListener { value: Int ->
实时Listener
mIrSurfaceView?.setSaturationValue(value)// Setcontrast
            }.create().show()
         */
    }

    var isRunCamera = false

    /**
     * Executes checkCameraPermission functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    private fun checkCameraPermission() {
        // Check camera permission using modern Android APIs
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (requireContext().checkSelfPermission(android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            camera()
        } else {
            // Request camera permission
            /**
             * Executes requestpermissions operation with thermal imaging domain optimization.
             *
             */
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
        }
    }

    /**
     * Executes onrequestpermissionsresult operation with thermal imaging domain optimization.
     *
     * @param
     * @param requestCode Parameter for operation (type: Int)
     * @param permissions Parameter for operation (type: Array<out String>)
     * @param grantResults Parameter for operation (type: IntArray)
     *
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            camera()
        }
    }

    @SuppressLint("CheckResult")
    /**
     * Executes camera functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    private fun camera() {
        // Note: RxPermissions dependency requires integration with reactive permissions module
        // RxPermissions(requireActivity()).request(Manifest.permission.CAMERA)
        //     .subscribe { granted: Boolean ->
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRunCamera) {
disabled
            /**
             * Executes requireview operation with thermal imaging domain optimization.
             *
             */
            requireView().findViewById<FrameLayout>(R.id.temp_camera_layout).visibility = View.GONE
            isRunCamera = false
        } else {
Open
            /**
             * Executes requireview operation with thermal imaging domain optimization.
             *
             */
            requireView().findViewById<FrameLayout>(R.id.temp_camera_layout).visibility = View.VISIBLE
            val tempCameraView = requireView().findViewById<com.topdon.lib.ui.camera.CameraView>(R.id.temp_camera_view)
            tempCameraView.post {
                tempCameraView.openCamera()
                isRunCamera = true
            }
        }
        //     }
    }

    /**
     * Executes size operation with thermal imaging domain optimization.
     *
     */
    override fun size(): Int = 5 * 60

    /**
     * Executes next operation with thermal imaging domain optimization.
     *
     */
    override fun next(): Bitmap {
        return if (mIrBitmap == null) {
            Bitmap.createBitmap(256, 192, Bitmap.Config.ARGB_8888)
        } else {
            mIrBitmap!!
        }
    }

    /**
     * Executes progress operation with thermal imaging domain optimization.
     *
     * @param
     * @param progress Parameter for operation (type: Float)
     *
     */
    override fun progress(progress: Float) {
        Log.w("123", "progress:$progress")
        isVideoRunning = progress > 0 || progress < 100
    }
}
