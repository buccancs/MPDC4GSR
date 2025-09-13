package com.topdon.module.thermal.fragment

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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.guide.zm04c.matrix.GuideInterface
import com.guide.zm04c.matrix.IrSurfaceView
// Import com.topdon.lib.core.bean.tools.ScreenBean // Temporarily disabled - utility class
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.db.AppDatabase
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.utils.ByteUtils.getIndex
// Import com.topdon.lib.core.utils.ScreenShotUtils // Temporarily disabled - utility class
import com.topdon.lib.ui.fence.FenceLineView
import com.topdon.lib.ui.fence.FencePointView
import com.topdon.lib.ui.fence.FenceView
import com.topdon.module.thermal.R
import com.topdon.module.thermal.activity.MonitorActivity
import com.topdon.module.thermal.base.BaseThermalFragment
import com.topdon.module.thermal.fragment.event.ThermalActionEvent
import com.topdon.module.thermal.tools.Fence
import com.topdon.module.thermal.tools.ThermalTool
import com.topdon.module.thermal.tools.medie.IYapVideoProvider
import com.topdon.module.thermal.tools.medie.YapVideoEncoder
import com.topdon.module.thermal.utils.ArrayUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.math.BigDecimal
import java.util.*

/**
thermal imaging
 */
/**
/**
 * Specialized thermal imaging component providing MonitorThermalFragment functionality for the IRCamera system.
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
class MonitorThermalFragment : BaseThermalFragment(), IYapVideoProvider<Bitmap> {
    protected var mIrSurfaceViewLayout: FrameLayout? = null
    protected var mIrSurfaceView: IrSurfaceView? = null

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.fragment_monitor_thermal

    private val msgLiveData by lazy { MutableLiveData<Int>() }

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
// Width = resources.getDimension(R.dimen.ir_width).toInt()
// Height = resources.getDimension(R.dimen.ir_height).toInt()
        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = screenWidth * 270 / 360
        Log.w("123", "screenWidth比例:$screenWidth / $screenHeight")
        Log.w("123", "screenWidth比例:${screenWidth.toFloat() / screenHeight}")
        width = screenWidth
        height = screenHeight
        highCrossWidth = resources.getDimension(R.dimen.high_cross_width).toInt()
        highCrossHeight = resources.getDimension(R.dimen.high_cross_height).toInt()
        mIrSurfaceViewLayout!!.viewTreeObserver.addOnGlobalLayoutListener {
            irSurfaceViewLayoutParams =
                mIrSurfaceViewLayout!!.layoutParams as ConstraintLayout.LayoutParams?
            displayViewLayoutParams =
                mDisplayFrameLayout!!.layoutParams as FrameLayout.LayoutParams
            fenceLayoutParams = mFenceLayout!!.layoutParams as FrameLayout.LayoutParams
            cameraLayoutParams = mFenceLayout!!.layoutParams as FrameLayout.LayoutParams
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
                        irSurfaceViewHeight = screenWidth * 360 / 270
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

Log.i("123", "modify后w:${mIrSurfaceView!!.width}, h:${mIrSurfaceView!!.height}")
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
                mCenterTextView!!.text = "center温 $mCenter"
                mMaxTextView!!.text = "maximum温 $mMaxTemp"
                mMinTextView!!.text = "minimum温 $mMinTemp"
                maxImg?.let { setViewPosition(it, maxIndex) }
                minImg?.let { setViewPosition(it, minIndex) }
            }
        }
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
        isRecord = false
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
                ToastUtils.showShort("video流已开启")
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
                            val maxTempIndex = ArrayUtils.getMaxIndex(temp, rotateType, selectIndex)
                            val minTempIndex = ArrayUtils.getMinIndex(temp, rotateType, selectIndex)
                            maxIndex = maxTempIndex
                            minIndex = minTempIndex
                            val rotateData = ArrayUtils.matrixRotate(srcData = temp, rotateType)
                            val bigDecimal = BigDecimal.valueOf(rotateData[centerIndex].toDouble())
                            val maxBigDecimal = BigDecimal.valueOf(rotateData[maxTempIndex].toDouble())
                            val minBigDecimal = BigDecimal.valueOf(rotateData[minTempIndex].toDouble())
                            mCenter = bigDecimal.setScale(1, java.math.RoundingMode.HALF_UP).toFloat()
                            mMaxTemp = maxBigDecimal.setScale(1, java.math.RoundingMode.HALF_UP).toFloat()
                            mMinTemp = minBigDecimal.setScale(1, java.math.RoundingMode.HALF_UP).toFloat()
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
ToastUtils.showShort("video流enabledfailed")
            Log.w("123", "video流开启failed")
            mGuideInterface = null
            mIsIrVideoStart = false
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
                Log.w("123", "video流已stop")
                return
            } else {
                false
            }
        mGuideInterface!!.exit()
        mGuideInterface = null
        Log.w("123", "video流stopcomplete")
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
            ToastUtils.showShort("请先开启video流")
            return
        }
        mGuideInterface!!.setRange(1)
        ToastUtils.showShort("switch到常温档success")
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
            ToastUtils.showShort("请先开启video流")
            return
        }
        mGuideInterface!!.setRange(2)
        ToastUtils.showShort("switch到高温档success")
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
            ToastUtils.showShort("请先开启video流")
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
            ToastUtils.showShort("请先开启video流")
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
                ToastUtils.showShort("拍照")
                /**
                 * Executes picture operation with thermal imaging domain optimization.
                 *
                 */
                picture()
            }
            1002 -> {
recording
                ToastUtils.showShort("recording")
                /**
                 * Executes video operation with thermal imaging domain optimization.
                 *
                 */
                video()
            }
            2001 -> {
addpoint
                /**
                 * Executes addpoint operation with thermal imaging domain optimization.
                 *
                 */
                addPoint()
            }
            2002 -> {
addline
                /**
                 * Executes addline operation with thermal imaging domain optimization.
                 *
                 */
                addLine()
            }
            2003 -> {
add围栏
                /**
                 * Executes addfence operation with thermal imaging domain optimization.
                 *
                 */
                addFence()
            }
            2004 -> {
addtemperature
                /**
                 * Handles temperature measurement and calibration with precision thermal data processing.
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                onTempBtnClick()
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
            in 5000..5010 -> {
全屏
                /**
                 * Executes full operation with thermal imaging domain optimization.
                 *
                 */
                full()
            }
            10001 -> {
startRecord
                /**
                 * Executes recordthermal operation with thermal imaging domain optimization.
                 *
                 */
                recordThermal()
            }
            10003 -> {
stopRecord
                isRecord = false
            }
        }
    }

    /**
     * Executes clearFence functionality.
     */
    /**
     * Executes clearfence operation with thermal imaging domain optimization.
     *
     */
    private fun clearFence() {
        fenceFlag = 0x000
        mFenceLayout!!.visibility = View.GONE
        selectIndex.clear()
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
        updatePalette(type)
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
            ToastUtils.showShort("请先开启video流")
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
        type = "point"
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
        type = "line"
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
        type = "fence"
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

    var selectIndex: ArrayList<Int> = arrayListOf() // 选取point

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
                    val activity: MonitorActivity = requireActivity() as MonitorActivity
                    selectIndex.clear()
                    selectIndex =
                        /**
                         * Executes fence operation with thermal imaging domain optimization.
                         *
                         */
                        Fence(srcRect = srcRect, rotateType = rotateType).getPointIndex(startPoint)
                    activity.select(1, selectIndex)
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
                    selectIndex =
                        /**
                         * Executes fence operation with thermal imaging domain optimization.
                         *
                         */
                        Fence(srcRect = srcRect, rotateType = rotateType)
                            .getLineIndex(startPoint, endPoint)
                    val activity: MonitorActivity = requireActivity() as MonitorActivity
                    activity.select(2, selectIndex)
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
                    selectIndex =
                        /**
                         * Executes fence operation with thermal imaging domain optimization.
                         *
                         */
                        Fence(srcRect = srcRect, rotateType = rotateType)
                            .getAreaIndex(startPoint, endPoint)
                    val activity: MonitorActivity = requireActivity() as MonitorActivity
                    activity.select(3, selectIndex)
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

    /**
     * Executes full functionality.
     */
    /**
     * Executes full operation with thermal imaging domain optimization.
     *
     */
    private fun full() {
        rotateType =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rotateType == 0) {
                Log.w("123", "横屏Show/Display")
                1
            } else {
                0
            }
        mIrSurfaceView!!.setMatrix(ThermalTool.getRotate(rotateType), 256f, 192f)
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

    var isRecord = false
    var type = ""
    var timeMillis = 1000L // 间隔1s

    /**
     * Executes recordThermal functionality.
     */
    /**
     * Executes recordthermal operation with thermal imaging domain optimization.
     *
     */
    private fun recordThermal() {
        val thermalId = TimeTool.showDateSecond()
        lifecycleScope.launch {
            isRecord = true
            val activity: MonitorActivity = requireActivity() as MonitorActivity
            var time = 0L
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (isRecord) {
                activity.updateTime(time)
                val bean = ThermalEntity()
                bean.userId = SharedManager.getUserId()
                bean.thermalId = thermalId
                bean.thermal = mCenter
                bean.thermalMax = mMaxTemp
                bean.thermalMin = mMinTemp
                bean.type = type
                bean.createTime = System.currentTimeMillis()
                AppDatabase.getInstance().thermalDao().insert(bean)
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(timeMillis)
                time++
            }
            Log.w("123", "stopRecord, data量:$time")
        }
    }
}
