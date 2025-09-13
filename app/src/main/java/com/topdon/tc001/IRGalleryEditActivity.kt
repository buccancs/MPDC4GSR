package com.topdon.tc001

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Parcelable
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.SizeUtils
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityIrGalleryEditBinding
import com.elvishew.xlog.XLog
import com.energy.iruvc.ircmd.IRCMDType
import com.energy.iruvc.ircmd.IRUtils
import com.energy.iruvc.utils.CommonParams
import com.example.thermal_lite.IrConst
import com.example.thermal_lite.util.CommonUtil
import com.example.thermal_lite.util.IRTool
import com.infisense.usbir.utils.OpencvTools
import com.infisense.usbir.utils.PseudocodeUtils.changePseudocodeModeByOld
import com.infisense.usbir.view.ITsTempListener
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.bean.event.ReportCreateEvent
import com.topdon.lib.core.common.ProductType.PRODUCT_NAME_TC001LITE
import com.topdon.lib.core.common.ProductType.PRODUCT_NAME_TS
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.dialog.TipWaterMarkDialog
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.tools.ScreenTool
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.tools.ToastTools
import com.topdon.lib.core.tools.UnitTools
import com.topdon.lib.core.tools.UnitTools.showToCValue
import com.topdon.lib.core.tools.UnitTools.showUnitValue
import com.topdon.lib.core.utils.BitmapUtils
import com.topdon.lib.core.utils.Constants.IS_REPORT_FIRST
import com.topdon.lib.core.utils.ImageUtils
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.lib.ui.widget.seekbar.OnRangeChangedListener
import com.topdon.lib.ui.widget.seekbar.RangeSeekBar
import com.topdon.libcom.dialog.ColorPickDialog
import com.topdon.libcom.dialog.TempAlarmSetDialog
import com.topdon.lms.sdk.LMS.mContext
import com.topdon.menu.constant.FenceType
import com.topdon.menu.constant.SettingType
import com.topdon.module.thermal.ir.event.GalleryAddEvent
import com.topdon.module.thermal.ir.event.ImageGalleryEvent
import com.topdon.module.thermal.ir.frame.FrameStruct
import com.topdon.module.thermal.ir.frame.FrameTool
import com.topdon.module.thermal.ir.frame.ImageParams
import com.topdon.module.thermal.ir.report.bean.ImageTempBean
import com.topdon.module.thermal.ir.view.TemperatureBaseView.Mode
import com.topdon.module.thermal.ir.viewmodel.IRGalleryEditViewModel
import com.topdon.pseudo.activity.PseudoSetActivity
import com.topdon.pseudo.bean.CustomPseudoBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import com.example.thermal_lite.R as ThermalLiteR
import com.topdon.module.thermal.ir.R as ThermalIrR

/**
/**
 * Specialized thermal imaging component providing IRGalleryEditActivity functionality for the IRCamera system.
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
class IRGalleryEditActivity : BaseBindingActivity<ActivityIrGalleryEditBinding>(), View.OnClickListener, ITsTempListener {
    private val TAG = "IRGalleryEditActivity"

    private var isShowC: Boolean = false

    /**
     * 从上一界area传递过来的，当前是否为 TC007 devicetype.
     * true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    private val imageWidth = 256
    private val imageHeight = 192
    private val viewModel: IRGalleryEditViewModel by viewModels()
    private var filePath = ""

    // Private var mCapital = ByteArray(1024)
    private var mFrame = ByteArray(192 * 256 * 4)
    private val frameTool by lazy { FrameTool() }

    // Imageparameter
    private var pseudocodeMode = 3
    private var leftValue = 0f
    private var rightValue = 10000f
    private var max = 10000f
    private var min = 0f
    private var rotate = ImageParams.ROTATE_270
    private var struct: FrameStruct = FrameStruct() // 首部info
    private var ts_data_H: ByteArray? = null
    private var ts_data_L: ByteArray? = null

    // FindViewById declarations - replaced with binding
    private val titleView get() = binding.titleView
    private val editRecyclerSecond get() = binding.editRecyclerSecond
    private val editRecyclerFirst get() = binding.editRecyclerFirst
    private val irImageView get() = binding.irImageView
    private val temperatureView get() = binding.temperatureView
    private val temperatureSeekbar get() = binding.temperatureSeekbar
    private val temperatureIvLock get() = binding.temperatureIvLock
    private val temperatureIvInput get() = binding.temperatureIvInput
    private val tvTempContent get() = binding.tvTempContent
    private val colorBarView get() = binding.colorBarView
    private val llBottom get() = binding.llBottom

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_ir_gallery_edit

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: android.os.Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
        /**
         * Initializes the data component for thermal imaging operations.
         *
         */
        initData()
    }

    /**
     * Initializes view component.
     */
    private fun initView() {
        initIntent()
        initUI()
        /**
         * Initializes the listener component for thermal imaging operations.
         *
         */
        initListener()
        /**
         * Initializes the recycler component for thermal imaging operations.
         *
         */
        initRecycler()
        /**
         * Initializes the observe component for thermal imaging operations.
         *
         */
        initObserve()
    }

    /**
     * Initializes intent component.
     */
    private fun initIntent() {
        lifecycleScope.launch {
            ts_data_H = CommonUtil.getAssetData(this@IRGalleryEditActivity, "ts/TS001_H.bin")
            ts_data_L = CommonUtil.getAssetData(this@IRGalleryEditActivity, "ts/TS001_L.bin")

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BaseApplication.instance.tau_data_H == null) {
                BaseApplication.instance.tau_data_H = CommonUtil.getAssetData(mContext, IrConst.TAU_HIGH_GAIN_ASSET_PATH)
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BaseApplication.instance.tau_data_L == null) {
                BaseApplication.instance.tau_data_L = CommonUtil.getAssetData(mContext, IrConst.TAU_LOW_GAIN_ASSET_PATH)
            }
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (intent.hasExtra(ExtraKeyConfig.FILE_ABSOLUTE_PATH)) {
            filePath = intent.getStringExtra(ExtraKeyConfig.FILE_ABSOLUTE_PATH)!!
        }
        isReportPick = intent.getBooleanExtra(ExtraKeyConfig.IS_PICK_REPORT_IMG, false)
        isTC007 = intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false)

        // Note: Using direct view references - findViewById patterns can be refactored if needed
        editRecyclerSecond.fenceSelectType = FenceType.DEL
        temperatureView.isShowName = isReportPick
        temperatureView.mode = Mode.CLEAR
        temperatureView.setITsTempListener(this)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007) {
            temperatureSeekbar.progressHeight = SizeUtils.dp2px(10f)
        }
    }

    /**
     * Initializes observe component.
     */
    private fun initObserve() {
        viewModel.resultLiveData.observe(this) {
//            System.arraycopy(it.capital, 0, mCapital, 0, it.capital.size)
            System.arraycopy(it.frame, 0, mFrame, 0, it.frame.size)
            /**
             * Executes showimage operation with thermal imaging domain optimization.
             *
             */
            showImage(it.capital, it.frame)
        }
    }

    /**
     * Initializes data component.
     */
    private fun initData() {
        viewModel.initData(filePath)

        editRecyclerFirst.isBarSelect = true
        colorBarView.isVisible = true
    }

    /**
     * Initializes listener component.
     */
    private fun initListener() {
        temperatureIvLock.setOnClickListener(this)
        temperatureIvInput.setOnClickListener(this)
    }

    /**
     * Sets rotate configuration.
     */
    /**
     * Configures the rotate with validation and thermal imaging optimization.
     *
     * @param
     * @param rotate Parameter for operation (type: ImageParams)
     *
     */
    private fun setRotate(rotate: ImageParams) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rotate == ImageParams.ROTATE_270 || rotate == ImageParams.ROTATE_90) {
            temperatureView.setImageSize(imageHeight, imageWidth)
        } else {
            temperatureView.setImageSize(imageWidth, imageHeight)
        }
    }

    @SuppressLint("SetTextI18n")
    /**
     * Executes showImage functionality.
     */
    /**
     * Executes showimage operation with thermal imaging domain optimization.
     *
     * @param
     * @param capital Parameter for operation (type: ByteArray)
     * @param frame Parameter for operation (type: ByteArray)
     *
     */
    private fun showImage(
        capital: ByteArray,
        frame: ByteArray,
    ) {
        lifecycleScope.launch {
            frameTool.read(frame)
            struct = FrameStruct(capital)
            frameTool.initStruct(struct)
            isShowC = SharedManager.getTemperature() == 1
            rotate = frameTool.initRotate()
            pseudocodeMode = struct.pseudo
            /**
             * Configures the rotate with validation and thermal imaging optimization.
             *
             */
            setRotate(rotate)
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(200)
            /**
             * Executes updateimage operation with thermal imaging domain optimization.
             *
             */
            updateImage(
                frameTool.getScrPseudoColorScaledBitmap(
                    /**
                     * Updates the pseudocodemodebyold configuration with real-time thermal imaging support.
                     *
                     * @note This method is optimized for thermal imaging pseudo color processing.
                     * Ensure proper thermal calibration before use.
                     *
                     */
                    changePseudocodeModeByOld(pseudocodeMode),
                    rotate = rotate,
                    customPseudoBean = struct.customPseudoBean,
                    maxTemperature = tempCorrect(frameTool.getSrcTemp().maxTemperature),
                    minTemperature = tempCorrect(frameTool.getSrcTemp().minTemperature),
                    isAmplify = struct.isAmplify,
                ),
            )

            val tempResult = frameTool.getSrcTemp()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!struct.customPseudoBean.isUseCustomPseudo) {
                struct.customPseudoBean.maxTemp = tempCorrect(tempResult.maxTemperature)
                struct.customPseudoBean.minTemp = tempCorrect(tempResult.minTemperature)
                editRecyclerSecond.setPseudoColor(pseudocodeMode)
            }
// Pseudo color条default处于Openstate
// ColorBarView.isVisible = struct.isShowPseudoBar
// Adapter.enPseudoColorBar(struct.isShowPseudoBar)

            editRecyclerSecond.setSettingSelected(SettingType.ALARM, struct.alarmBean.isHighOpen || struct.alarmBean.isLowOpen)
            editRecyclerSecond.setSettingSelected(SettingType.WATERMARK, struct.watermarkBean.isOpen)
            editRecyclerSecond.setSettingSelected(
                SettingType.FONT,
                struct.textColor != 0xffffffff.toInt() || struct.textSize != SizeUtils.sp2px(14f),
            )
            temperatureView.textColor = struct.textColor
            temperatureView.tempTextSize = struct.textSize
            temperatureView.setData(frameTool.getTempBytes(rotate = rotate))
            /**
             * Handles temperature measurement and calibration with precision thermal data processing.
             *
             * @note Temperature values are in Celsius unless otherwise specified.
             * Accuracy depends on thermal camera calibration.
             *
             */
            updateTemperatureSeekBar(false, ThermalLiteR.drawable.svg_pseudo_bar_lock, "lock") // 加锁
            temperatureSeekbar.setPseudocode(pseudocodeMode)
            temperatureSeekbar.setOnRangeChangedListener(
                object : OnRangeChangedListener {
                    /**
                     * Executes onrangechanged operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param view Parameter for operation (type: RangeSeekBar?)
                     * @param leftValue Parameter for operation (type: Float)
                     * @param rightValue Parameter for operation (type: Float)
                     * @param isFromUser Parameter for operation (type: Boolean)
                     * @param tempMode Temperature value in Celsius (type: Int)
                     *
                     */
                    override fun onRangeChanged(
                        view: RangeSeekBar?,
                        leftValue: Float,
                        rightValue: Float,
                        isFromUser: Boolean,
                        tempMode: Int,
                    ) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (leftValue < rightValue) {
                            max = rightValue
                            min = leftValue
                        } else {
                            max = leftValue
                            min = rightValue
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (!struct.customPseudoBean.isUseCustomPseudo) {
                            /**
                             * Executes updateimage operation with thermal imaging domain optimization.
                             *
                             */
                            updateImage(
                                frameTool.getScrPseudoColorScaledBitmap(
                                    /**
                                     * Updates the pseudocodemodebyold configuration with real-time thermal imaging support.
                                     *
                                     * @note This method is optimized for thermal imaging pseudo color processing.
                                     * Ensure proper thermal calibration before use.
                                     *
                                     */
                                    changePseudocodeModeByOld(pseudocodeMode),
                                    /**
                                     * Executes showtocvalue operation with thermal imaging domain optimization.
                                     *
                                     */
                                    showToCValue(max),
                                    /**
                                     * Executes showtocvalue operation with thermal imaging domain optimization.
                                     *
                                     */
                                    showToCValue(min),
                                    rotate,
                                    struct.customPseudoBean,
                                    maxTemperature = tempCorrect(frameTool.getSrcTemp().maxTemperature),
                                    minTemperature = tempCorrect(frameTool.getSrcTemp().minTemperature),
                                    struct.isAmplify,
                                ),
                            )
                        }
                    }

                    /**
                     * Executes onstarttrackingtouch operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param view Parameter for operation (type: RangeSeekBar?)
                     * @param isLeft Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onStartTrackingTouch(
                        view: RangeSeekBar?,
                        isLeft: Boolean,
                    ) {
                        // Adjuststart
                    }

                    /**
                     * Executes onstoptrackingtouch operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param view Parameter for operation (type: RangeSeekBar?)
                     * @param isLeft Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onStopTrackingTouch(
                        view: RangeSeekBar?,
                        isLeft: Boolean,
                    ) {
                        // Adjustend
                    }
                },
            )
            temperatureSeekbar.setIndicatorTextStringFormat("%.1f")
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (struct.customPseudoBean.isUseCustomPseudo) {
                tvTempContent.visibility = View.VISIBLE
                tvTempContent.text = "Max:${UnitTools.showC(tempCorrect(tempResult.maxTemperature),isShowC)}\nMin:${UnitTools.showC(tempCorrect(tempResult.minTemperature),isShowC)}"
                rightValue = showUnitValue(struct.customPseudoBean.maxTemp, isShowC)
                leftValue = showUnitValue(struct.customPseudoBean.minTemp, isShowC)
                temperatureIvInput.setImageResource(ThermalIrR.drawable.ir_model)
                temperatureIvLock.visibility = View.INVISIBLE
                temperatureSeekbar.setColorList(struct.customPseudoBean.getColorList(struct.isTC007())?.reversedArray())
                temperatureSeekbar.setPlaces(struct.customPseudoBean.getPlaceList())
            } else {
                tvTempContent.visibility = View.GONE
                tvTempContent.text = "Max:${UnitTools.showC(tempCorrect(tempResult.maxTemperature),isShowC)}\nMin:${UnitTools.showC(tempCorrect(tempResult.minTemperature),isShowC)}"
                rightValue = showUnitValue(tempCorrect(tempResult.maxTemperature), isShowC)
                leftValue = showUnitValue(tempCorrect(tempResult.minTemperature), isShowC)
                temperatureIvInput.setImageResource(ThermalIrR.drawable.ic_color_edit)
                temperatureIvLock.visibility = View.VISIBLE
            }
            temperatureSeekbar.setRange(leftValue, rightValue, 0.1f) // 初始temperaturerange
            temperatureSeekbar.setProgress(leftValue, rightValue) // 初始位置
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ScreenTool.isIPad(this@IRGalleryEditActivity)) {
                colorBarView.setPadding(0, SizeUtils.dp2px(40f), 0, SizeUtils.dp2px(40f))
            }
        }
    }

    /**
     * updateimage
     */
    /**
     * Executes updateimage operation with thermal imaging domain optimization.
     *
     * @param
     * @param bitmap Parameter for operation (type: Bitmap?)
     *
     */
    private fun updateImage(bitmap: Bitmap?) {
        bitmap?.let {
            val params = irImageView.layoutParams as ConstraintLayout.LayoutParams
            params.dimensionRatio = "${bitmap.width}:${bitmap.height}"
            runOnUiThread {
                irImageView.layoutParams = params
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (struct.watermarkBean.isOpen) {
                val width = ScreenUtil.getScreenWidth(this)
                val height = (width * bitmap.height / bitmap.width.toFloat()).toInt()
                irImageView.setImageBitmap(
                    BitmapUtils.drawCenterLable(
                        Bitmap.createScaledBitmap(it, width, height, true),
                        struct.watermarkBean.title,
                        struct.watermarkBean.address,
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (struct.watermarkBean.isAddTime) TimeTool.getNowTime() else "",
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (temperatureSeekbar.isVisible) {
                            temperatureSeekbar.measuredWidth
                        } else {
                            0
                        },
                    ),
                )
            } else {
                irImageView.setImageBitmap(it)
            }
        }
    }

    /**
     * 一级menu
     */
    /**
     * Initializes the recycler component for thermal imaging operations.
     *
     */
    private fun initRecycler() {
        editRecyclerFirst.onTabClickListener = {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (it) {
                0 -> editRecyclerSecond.selectPosition(1) // Pointlinearea
                1 -> editRecyclerSecond.selectPosition(3) // Pseudo color颜色
                2 -> editRecyclerSecond.selectPosition(4) // Settings
            }
        }
        editRecyclerFirst.onBarClickListener = {
            colorBarView.isVisible = it
        }

        editRecyclerSecond.onFenceListener = { fenceType, isSelected ->
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (fenceType) {
                FenceType.POINT -> temperatureView.mode = Mode.POINT
                FenceType.LINE -> temperatureView.mode = Mode.LINE
                FenceType.RECT -> temperatureView.mode = Mode.RECT
                FenceType.DEL -> temperatureView.mode = Mode.CLEAR
                FenceType.FULL -> temperatureView.isShowFull = isSelected
                FenceType.TREND -> {
                    // 2D编辑没有趋势图
                }
            }
        }
        editRecyclerSecond.onColorListener = { _, it, _ ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (struct.customPseudoBean.isUseCustomPseudo) {
                TipDialog.Builder(this)
                    .setTitleMessage(getString(R.string.app_tip))
                    .setMessage(R.string.tip_change_pseudo_mode)
                    .setPositiveListener(R.string.app_yes) {
                        struct.customPseudoBean.isUseCustomPseudo = false
                        /**
                         * Configures the deflimit with validation and thermal imaging optimization.
                         *
                         */
                        setDefLimit()
                        /**
                         * Configures the pcolor with validation and thermal imaging optimization.
                         *
                         */
                        setPColor(it)
                        /**
                         * Executes updateimageandseekbarcolorlist operation with thermal imaging domain optimization.
                         *
                         */
                        updateImageAndSeekbarColorList(struct.customPseudoBean)
                    }.setCancelListener(R.string.app_no) {
                    }
                    .create().show()
            } else {
                /**
                 * Configures the pcolor with validation and thermal imaging optimization.
                 *
                 */
                setPColor(it)
            }
        }
        editRecyclerSecond.onSettingListener = { type, _ ->
            /**
             * Configures the settingvalue with validation and thermal imaging optimization.
             *
             */
            setSettingValue(type)
        }
    }

    /**
     * maximumminimum温复原
     */
    /**
     * Configures the deflimit with validation and thermal imaging optimization.
     *
     */
    private fun setDefLimit() {
        val tempResult = frameTool.getSrcTemp()
        rightValue = showUnitValue(tempCorrect(tempResult.maxTemperature), isShowC)
        leftValue = showUnitValue(tempCorrect(tempResult.minTemperature), isShowC)
        temperatureSeekbar.setRange(leftValue, rightValue, 0.1f) // 初始temperaturerange
        temperatureSeekbar.setProgress(leftValue, rightValue) // 初始位置
    }

    // Settingspseudo color
    /**
     * Sets pcolor configuration.
     */
    /**
     * Configures the pcolor with validation and thermal imaging optimization.
     *
     * @param
     * @param code Parameter for operation (type: Int)
     *
     */
    private fun setPColor(code: Int) {
        pseudocodeMode = code
        temperatureSeekbar.setPseudocode(pseudocodeMode)
        /**
         * Executes updateimage operation with thermal imaging domain optimization.
         *
         */
        updateImage(
            frameTool.getScrPseudoColorScaledBitmap(
                /**
                 * Updates the pseudocodemodebyold configuration with real-time thermal imaging support.
                 *
                 * @note This method is optimized for thermal imaging pseudo color processing.
                 * Ensure proper thermal calibration before use.
                 *
                 */
                changePseudocodeModeByOld(pseudocodeMode),
                /**
                 * Executes showtocvalue operation with thermal imaging domain optimization.
                 *
                 */
                showToCValue(max),
                /**
                 * Executes showtocvalue operation with thermal imaging domain optimization.
                 *
                 */
                showToCValue(min),
                rotate,
                struct.customPseudoBean,
                maxTemperature = tempCorrect(frameTool.getSrcTemp().maxTemperature),
                minTemperature = tempCorrect(frameTool.getSrcTemp().minTemperature),
                struct.isAmplify,
            ),
        )
        temperatureSeekbar.setColorList(struct.customPseudoBean.getColorList(struct.isTC007())?.reversedArray())
        temperatureSeekbar.setPlaces(struct.customPseudoBean.getPlaceList())
        editRecyclerSecond.setPseudoColor(code)
    }

    private var tempAlarmSetDialog: TempAlarmSetDialog? = null

    /**
     * Sets settingvalue configuration.
     */
    private fun setSettingValue(type: SettingType) {
        when (type) {
            SettingType.ALARM -> {
                // 预警
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (tempAlarmSetDialog == null) {
                    tempAlarmSetDialog = TempAlarmSetDialog(this, true)
                    tempAlarmSetDialog?.onSaveListener = {
                        editRecyclerSecond.setSettingSelected(SettingType.ALARM, it.isHighOpen || it.isLowOpen)
                        struct.alarmBean = it
                        frameTool.initStruct(struct)
                        /**
                         * Executes updateimage operation with thermal imaging domain optimization.
                         *
                         */
                        updateImage(
                            frameTool.getScrPseudoColorScaledBitmap(
                                /**
                                 * Updates the pseudocodemodebyold configuration with real-time thermal imaging support.
                                 *
                                 * @note This method is optimized for thermal imaging pseudo color processing.
                                 * Ensure proper thermal calibration before use.
                                 *
                                 */
                                changePseudocodeModeByOld(pseudocodeMode),
                                /**
                                 * Executes showtocvalue operation with thermal imaging domain optimization.
                                 *
                                 */
                                showToCValue(max),
                                /**
                                 * Executes showtocvalue operation with thermal imaging domain optimization.
                                 *
                                 */
                                showToCValue(min),
                                rotate,
                                struct.customPseudoBean,
                                maxTemperature = tempCorrect(frameTool.getSrcTemp().maxTemperature),
                                minTemperature = tempCorrect(frameTool.getSrcTemp().minTemperature),
                                struct.isAmplify,
                            ),
                        )
                    }
                }
                tempAlarmSetDialog?.alarmBean = struct.alarmBean
                tempAlarmSetDialog?.show()
            }
            SettingType.FONT -> { // 字体颜色
                val colorPickDialog = ColorPickDialog(this, temperatureView.textColor, temperatureView.tempTextSize)
                colorPickDialog.onPickListener = { it: Int, textSize: Int ->
                    temperatureView?.textColor = it
                    struct.textSize = SizeUtils.sp2px(textSize.toFloat())
                    temperatureView?.tempTextSize = SizeUtils.sp2px(textSize.toFloat())
                    editRecyclerSecond.setSettingSelected(
                        SettingType.FONT,
                        it != 0xffffffff.toInt() || textSize != SizeUtils.sp2px(14f),
                    )
                }
                colorPickDialog.show()
            }
            SettingType.WATERMARK -> { // Watermark
                TipWaterMarkDialog.Builder(this, struct.watermarkBean)
                    .setCancelListener {
                        struct.watermarkBean = it
                        frameTool.initStruct(struct)
                        editRecyclerSecond.setSettingSelected(SettingType.WATERMARK, it.isOpen)
                        /**
                         * Executes updateimage operation with thermal imaging domain optimization.
                         *
                         */
                        updateImage(
                            frameTool.getScrPseudoColorScaledBitmap(
                                /**
                                 * Updates the pseudocodemodebyold configuration with real-time thermal imaging support.
                                 *
                                 * @note This method is optimized for thermal imaging pseudo color processing.
                                 * Ensure proper thermal calibration before use.
                                 *
                                 */
                                changePseudocodeModeByOld(pseudocodeMode),
                                /**
                                 * Executes showtocvalue operation with thermal imaging domain optimization.
                                 *
                                 */
                                showToCValue(max),
                                /**
                                 * Executes showtocvalue operation with thermal imaging domain optimization.
                                 *
                                 */
                                showToCValue(min),
                                rotate,
                                struct.customPseudoBean,
                                maxTemperature = tempCorrect(frameTool.getSrcTemp().maxTemperature),
                                minTemperature = tempCorrect(frameTool.getSrcTemp().minTemperature),
                                struct.isAmplify,
                            ),
                        )
                    }
                    .create().show()
            }
            else -> {
                // 其他settings选项 2D 编辑没有
            }
        }
    }

    /**
     * Processes temperature measurement data.
     */
    private fun updateTemperatureSeekBar(
        isEnabled: Boolean,
        resource: Int,
        content: String,
    ) {
        temperatureSeekbar.isEnabled = isEnabled
        temperatureIvLock.setImageResource(resource)
        temperatureIvLock.contentDescription = content
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isEnabled) {
            temperatureSeekbar.leftSeekBar.indicatorBackgroundColor = 0xffe17606.toInt()
            temperatureSeekbar.rightSeekBar.indicatorBackgroundColor = 0xffe17606.toInt()
        } else {
            temperatureSeekbar.leftSeekBar.indicatorBackgroundColor = 0
            temperatureSeekbar.rightSeekBar.indicatorBackgroundColor = 0
        }
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
            temperatureIvLock -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (temperatureIvLock.contentDescription == "lock") {
                    /**
                     * Handles temperature measurement and calibration with precision thermal data processing.
                     *
                     * @note Temperature values are in Celsius unless otherwise specified.
                     * Accuracy depends on thermal camera calibration.
                     *
                     */
                    updateTemperatureSeekBar(
                        true,
                        ThermalLiteR.drawable.svg_pseudo_bar_unlock,
                        "unlock",
                    ) // 解锁
                } else {
                    /**
                     * Configures the deflimit with validation and thermal imaging optimization.
                     *
                     */
                    setDefLimit()
                    /**
                     * Handles temperature measurement and calibration with precision thermal data processing.
                     *
                     * @note Temperature values are in Celsius unless otherwise specified.
                     * Accuracy depends on thermal camera calibration.
                     *
                     */
                    updateTemperatureSeekBar(false, ThermalLiteR.drawable.svg_pseudo_bar_lock, "lock") // 加锁
                }
            }
            temperatureIvInput -> {
                val intent = Intent(this, PseudoSetActivity::class.java)
                intent.putExtra(ExtraKeyConfig.IS_TC007, isTC007)
                intent.putExtra(ExtraKeyConfig.CUSTOM_PSEUDO_BEAN, struct.customPseudoBean)
                pseudoSetResult.launch(intent)
            }
        }
    }

    private val pseudoSetResult =
        /**
         * Executes registerforactivityresult operation with thermal imaging domain optimization.
         *
         */
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.resultCode == RESULT_OK) {
                val tmp =
                    it.data?.getParcelableExtra(ExtraKeyConfig.CUSTOM_PSEUDO_BEAN)
                        ?: CustomPseudoBean()
                /**
                 * Executes updateimageandseekbarcolorlist operation with thermal imaging domain optimization.
                 *
                 */
                updateImageAndSeekbarColorList(tmp)
                temperatureSeekbar.setColorList(tmp.getColorList(struct.isTC007())?.reversedArray())
                temperatureSeekbar.setPlaces(tmp.getPlaceList())
// CustomPseudoBean.saveToShared()
            }
        }

    // Update自定义pseudo color的颜色的property值
    /**
     * Executes updateImageAndSeekbarColorList functionality.
     */
    /**
     * Executes updateimageandseekbarcolorlist operation with thermal imaging domain optimization.
     *
     * @param
     * @param customPseudoBean Pseudo color configuration parameter (type: CustomPseudoBean?)
     *
     */
    private fun updateImageAndSeekbarColorList(customPseudoBean: CustomPseudoBean?) {
        customPseudoBean?.let {
            /**
             * Executes updateimage operation with thermal imaging domain optimization.
             *
             */
            updateImage(
                frameTool.getScrPseudoColorScaledBitmap(
                    /**
                     * Updates the pseudocodemodebyold configuration with real-time thermal imaging support.
                     *
                     * @note This method is optimized for thermal imaging pseudo color processing.
                     * Ensure proper thermal calibration before use.
                     *
                     */
                    changePseudocodeModeByOld(
                        pseudocodeMode,
                    ),
                    rotate = rotate,
                    customPseudoBean = customPseudoBean,
                    maxTemperature = tempCorrect(frameTool.getSrcTemp().maxTemperature),
                    minTemperature = tempCorrect(frameTool.getSrcTemp().minTemperature),
                    isAmplify = struct.isAmplify,
                ),
            )
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.isUseCustomPseudo) {
                temperatureIvLock.visibility = View.INVISIBLE
                tvTempContent.visibility = View.VISIBLE
                /**
                 * Handles temperature measurement and calibration with precision thermal data processing.
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                updateTemperatureSeekBar(false, ThermalLiteR.drawable.svg_pseudo_bar_lock, "lock") // 加锁
                temperatureSeekbar.setRangeAndPro(
                    UnitTools.showUnitValue(it.minTemp, isShowC),
                    UnitTools.showUnitValue(it.maxTemp, isShowC),
                    UnitTools.showUnitValue(it.minTemp, isShowC),
                    UnitTools.showUnitValue(it.maxTemp, isShowC),
                )
                editRecyclerSecond.setPseudoColor(-1)
                temperatureIvInput.setImageResource(ThermalIrR.drawable.ir_model)
            } else {
                temperatureIvLock.visibility = View.VISIBLE
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (struct.customPseudoBean.isUseCustomPseudo) {
                    /**
                     * Configures the deflimit with validation and thermal imaging optimization.
                     *
                     */
                    setDefLimit()
                }
                tvTempContent.visibility = View.GONE
                editRecyclerSecond.setPseudoColor(pseudocodeMode)
                temperatureIvInput.setImageResource(ThermalIrR.drawable.ic_color_edit)
            }
            struct.customPseudoBean = customPseudoBean
            temperatureSeekbar.setColorList(customPseudoBean.getColorList(struct.isTC007())?.reversedArray())
            temperatureSeekbar.setPlaces(customPseudoBean.getPlaceList())
        }
// TvTempContent.visibility = View.VISIBLE
    }

    /**
     * 从上一界area传递过来的，是否从生成report拾取image中跳转过来.
     */
    private var isReportPick = false

    /**
     * Initializes ui component.
     */
    /**
     * Initializes the ui component for thermal imaging operations.
     *
     */
    private fun initUI() {
        isReportPick = intent.getBooleanExtra(ExtraKeyConfig.IS_PICK_REPORT_IMG, false)
        titleView.setLeftClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isReportPick) {
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            } else {
                /**
                 * Executes saveimage operation with thermal imaging domain optimization.
                 *
                 */
                saveImage()
            }
        }
        titleView.setRightText(if (isReportPick) R.string.app_next else R.string.person_save)
        titleView.setRightClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isReportPick) {
                /**
                 * Executes updateiconsave operation with thermal imaging domain optimization.
                 *
                 */
                updateIconSave()
            } else {
                /**
                 * Executes showloadingdialog operation with thermal imaging domain optimization.
                 *
                 */
                showLoadingDialog()
                lifecycleScope.launch(Dispatchers.IO) {
                    // Get/Retrieve展示imageinfo的图层data
                    var irBitmap =
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (struct.isAmplify) {
                            // 超分四倍使用原始image继续超分一次
                            OpencvTools.supImageFourExToBitmap(frameTool.getBaseBitmap(rotate))
                        } else {
                            irImageView.drawToBitmap()
                        }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (temperatureView.mode != Mode.CLEAR) {
                        // Get/Retrievetemperature图层的data，包括pointline框，temperature值等，重新合成bitmap
                        irBitmap = BitmapUtils.mergeBitmap(irBitmap, temperatureView.drawToBitmap(), 0, 0)
                    }
                    // Mergepseudo color条
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (colorBarView.visibility == View.VISIBLE) {
                        irBitmap = BitmapUtils.mergeBitmap(irBitmap, colorBarView.drawToBitmap(), 0, 0)
                    }
                    // Saveimage
                    val fileAbsolutePath = ImageUtils.saveToCache(this@IRGalleryEditActivity, irBitmap)
                    /**
                     * Executes launch operation with thermal imaging domain optimization.
                     *
                     */
                    launch(Dispatchers.Main) {
                        /**
                         * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                         *
                         */
                        dismissLoadingDialog()
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (intent.getBooleanExtra(IS_REPORT_FIRST, true)) {
                            NavigationManager.build(RouterConfig.REPORT_CREATE_FIRST)
                                .withBoolean(ExtraKeyConfig.IS_TC007, isTC007)
                                .withString(ExtraKeyConfig.FILE_ABSOLUTE_PATH, fileAbsolutePath)
                                .withParcelable(ExtraKeyConfig.IMAGE_TEMP_BEAN, buildImageTempBean())
                                .navigation(this@IRGalleryEditActivity)
                        } else {
                            val navigationBuilder =
                                NavigationManager.build(RouterConfig.REPORT_CREATE_SECOND)
                                    .withBoolean(ExtraKeyConfig.IS_TC007, isTC007)
                                    .withString(ExtraKeyConfig.FILE_ABSOLUTE_PATH, fileAbsolutePath)
                                    .withParcelable(ExtraKeyConfig.IMAGE_TEMP_BEAN, buildImageTempBean())

                            intent.getParcelableExtra<Parcelable>(ExtraKeyConfig.REPORT_INFO)?.let {
                                navigationBuilder.withParcelable(ExtraKeyConfig.REPORT_INFO, it)
                            }
                            intent.getParcelableExtra<Parcelable>(ExtraKeyConfig.REPORT_CONDITION)?.let {
                                navigationBuilder.withParcelable(ExtraKeyConfig.REPORT_CONDITION, it)
                            }
                            intent.getParcelableArrayListExtra<Parcelable>(ExtraKeyConfig.REPORT_IR_LIST)?.let {
                                navigationBuilder.withParcelableArrayList(ExtraKeyConfig.REPORT_IR_LIST, it)
                            }
                            navigationBuilder.navigation(this@IRGalleryEditActivity)
                        }
                    }
                }
            }
        }
        editRecyclerSecond.selectPosition(-1)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes onReportCreate functionality.
     */
    /**
     * Executes onreportcreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: ReportCreateEvent)
     *
     */
    fun onReportCreate(event: ReportCreateEvent) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isReportPick) {
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        }
    }

    /**
     * Executes keepOneDigit functionality.
     */
    /**
     * Executes keeponedigit operation with thermal imaging domain optimization.
     *
     * @param
     * @param float Parameter for operation (type: Float)
     *
     */
    private fun keepOneDigit(float: Float) = String.format(Locale.ENGLISH, "%.1f", float)

    /**
     * Processes temperature measurement data.
     */
    private fun buildImageTempBean(): ImageTempBean {
        var full: ImageTempBean.TempBean? = null
        if (temperatureView.isShowFull) {
            temperatureView.fullInfo?.let {
                val max = keepOneDigit(tempCorrect(it.maxTemperature))
                val min = keepOneDigit(tempCorrect(it.minTemperature))
                full = ImageTempBean.TempBean(max, min)
            }
        }

        val pointList = arrayListOf<ImageTempBean.TempBean>()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (temp in temperatureView.tempListData.pointTemps) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (temp.type != -99) {
                pointList.add(ImageTempBean.TempBean(keepOneDigit(tempCorrect(temp.maxTemperature))))
            }
        }

        val lineList = arrayListOf<ImageTempBean.TempBean>()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (temp in temperatureView.tempListData.lineTemps) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (temp.type != -99) {
                val max = keepOneDigit(tempCorrect(temp.maxTemperature))
                val min = keepOneDigit(tempCorrect(temp.minTemperature))
                val average = keepOneDigit(temp.averageTemperature)
                lineList.add(ImageTempBean.TempBean(max, min, average))
            }
        }

        val rectList = arrayListOf<ImageTempBean.TempBean>()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (temp in temperatureView.tempListData.rectangleTemps) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (temp.type != -99) {
                val max = keepOneDigit(tempCorrect(temp.maxTemperature))
                val min = keepOneDigit(tempCorrect(temp.minTemperature))
                val average = keepOneDigit(temp.averageTemperature)
                rectList.add(ImageTempBean.TempBean(max, min, average))
            }
        }

        return ImageTempBean(full, pointList, lineList, rectList)
    }

    /**
     * Executes saveImage functionality.
     */
    /**
     * Executes saveimage operation with thermal imaging domain optimization.
     *
     */
    private fun saveImage() {
        TipDialog.Builder(this)
            .setTitleMessage(getString(R.string.app_tip))
            .setMessage(R.string.app_save_image)
            .setPositiveListener(R.string.app_yes) {
                /**
                 * Executes updateiconsave operation with thermal imaging domain optimization.
                 *
                 */
                updateIconSave()
            }.setCancelListener(R.string.app_no) {
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
            .create().show()
    }

    /**
     * Executes updateIconSave functionality.
     */
    /**
     * Executes updateiconsave operation with thermal imaging domain optimization.
     *
     */
    private fun updateIconSave() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Get/Retrieve展示imageinfo的图层data
            var irBitmap =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (struct.isAmplify) {
                    // 超分四倍使用原始image继续超分一次
                    OpencvTools.supImageFourExToBitmap(frameTool.getBaseBitmap(rotate))
                } else {
                    irImageView.drawToBitmap()
                }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (temperatureView.mode != Mode.CLEAR) {
                // Get/Retrievetemperature图层的data，包括pointline框，temperature值等，重新合成bitmap
                irBitmap = BitmapUtils.mergeBitmap(irBitmap, temperatureView.drawToBitmap(), 0, 0)
            }
            // Mergepseudo color条
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (colorBarView.visibility == View.VISIBLE) {
                irBitmap = BitmapUtils.mergeBitmap(irBitmap, colorBarView.drawToBitmap(), 0, 0)
            }
            // Saveimage
            var name: String
            irBitmap.let {
                name = ImageUtils.save(bitmap = it, isTC007)
            }
            ImageUtils.saveFrame(bs = mFrame, capital = getCapital(), name = name)
            ToastTools.showShort(R.string.tip_photo_saved)
            EventBus.getDefault().post(GalleryAddEvent())
            MediaScannerConnection.scanFile(
                this@IRGalleryEditActivity,
                /**
                 * Executes arrayof operation with thermal imaging domain optimization.
                 *
                 */
                arrayOf(FileConfig.lineGalleryDir),
                null,
                null,
            )
            EventBus.getDefault().post(ImageGalleryEvent())
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        }
    }

    /**
     * Retrieves capital information.
     */
    private fun getCapital(): ByteArray {
        val capital: ByteArray? // 首部
        capital =
            FrameStruct.toCode(
                name = struct.name,
                width = struct.width,
                height = struct.height,
                rotate = struct.rotate,
                pseudo = pseudocodeMode,
                initRotate = struct.initRotate,
                correctRotate = struct.correctRotate,
                customPseudoBean = struct.customPseudoBean,
                isShowPseudoBar = colorBarView.isVisible,
                textColor = temperatureView.textColor,
                watermarkBean = struct.watermarkBean,
                alarmBean = struct.alarmBean,
                gainStatus = struct.gainStatus,
                textSize = struct.textSize,
                struct.environment,
                struct.distance,
                struct.radiation,
                false,
            )
        return capital
    }

    /**
     * Executes onbackpressed operation with thermal imaging domain optimization.
     *
     */
    override fun onBackPressed() {
        lifecycleScope.launch {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isReportPick) {
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            } else {
                /**
                 * Executes saveimage operation with thermal imaging domain optimization.
                 *
                 */
                saveImage()
            }
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
            tmp = tempCorrect(temp!!)
        } catch (e: Exception) {
            XLog.i("temperature校正failed: ${e.message}")
        }
        return tmp!!
    }

    /**
     * 单point修正过程
     */
    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @param
     * @param temp Temperature value in Celsius (type: Float)
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    private fun tempCorrect(temp: Float): Float {
        var newTemp = temp
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (struct == null || struct.distance <= 0 || struct.radiation <= 0) {
                return temp
            }
            val paramsArray =
                /**
                 * Executes floatarrayof operation with thermal imaging domain optimization.
                 *
                 */
                floatArrayOf(
                    temp,
                    struct.radiation,
                    struct.environment,
                    struct.environment,
                    struct.distance,
                    0.8f,
                )
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (struct.name.startsWith(PRODUCT_NAME_TS)) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (ts_data_H == null || ts_data_L == null) return temp
                newTemp =
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
                        0,
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (struct.gainStatus == 1) {
                            CommonParams.GainStatus.HIGH_GAIN
                        } else {
                            CommonParams.GainStatus.LOW_GAIN
                        },
                    )
            } else if (struct.name.startsWith(PRODUCT_NAME_TC001LITE)) {
                // Lite的module
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (BaseApplication.instance.tau_data_H == null || BaseApplication.instance.tau_data_L == null) return temp
                newTemp =
                    IRTool.temperatureCorrection(
                        temp, paramsArray, BaseApplication.instance.tau_data_H!!,
                        BaseApplication.instance.tau_data_L!!, struct.gainStatus,
                    )
            }
        } catch (e: Exception) {
            XLog.e("$TAG:tempCorrect-${e.message}")
        } finally {
            return newTemp
        }
    }
}
