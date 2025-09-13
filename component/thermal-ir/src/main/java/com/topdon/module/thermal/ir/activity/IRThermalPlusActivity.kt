package com.topdon.module.thermal.ir.activity

import android.graphics.Bitmap
import android.view.SurfaceView
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.energy.iruvc.sdkisp.LibIRProcess
import com.energy.iruvc.utils.CommonParams
import com.energy.iruvc.utils.DualCameraParams
import com.infisense.usbdual.Const
import com.infisense.usbir.utils.IRImageHelp
import com.infisense.usbir.utils.PseudocodeUtils
import com.infisense.usbir.view.TemperatureView
import com.topdon.lib.core.bean.CameraItemBean
import com.topdon.lib.core.common.ProductType.PRODUCT_NAME_TCP
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.tools.ToastTools
import com.topdon.menu.constant.TwoLightType
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.event.GalleryAddEvent
import com.topdon.module.thermal.ir.video.VideoRecordFFmpeg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
dual lightdevice的interface
 * @author: CaiSongL
 * @date: 2024/1/17 17:47
 */
/**
 * Specialized thermal imaging component providing IRThermalPlusActivity functionality for the IRCamera system.
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
class IRThermalPlusActivity : BaseIRPlushActivity() {
    private val irImageHelp by lazy {
        /**
         * Executes irimagehelp operation with thermal imaging domain optimization.
         *
         */
        IRImageHelp()
    }

    // Synthetic view properties - migrated from kotlin-android-extensions
    private val dualTextureViewNativeCamera by lazy { findViewById<SurfaceView>(R.id.dualTextureViewNativeCamera) }
    // // Private val thermalSteeringView by lazy { findViewById<com.topdon.lib.ui.widget.SteeringWheelView>(R.id.thermalSteeringView) }  // ID doesn't exist
    // ThermalRecyclerNight inherited from parent class

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_ir_thermal_double

    /**
     * Executes isdualir operation with thermal imaging domain optimization.
     *
     */
    override fun isDualIR(): Boolean {
        return true
    }

    /**
     * Retrieves the surfaceview with optimized performance for thermal imaging operations.
     *
     */
    override fun getSurfaceView(): SurfaceView {
        return dualTextureViewNativeCamera
    }

    /**
     * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    override fun getTemperatureDualView(): TemperatureView {
        return temperatureView
    }

    /**
     * Retrieves the productname with optimized performance for thermal imaging operations.
     *
     */
    override fun getProductName(): String {
        return PRODUCT_NAME_TCP
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        super.initView()
findViewById<TextView>(R.id.toolbar_title)?.text = "dual lightdevice"
        cameraView.visibility = View.GONE
        dualTextureViewNativeCamera?.visibility = View.VISIBLE
        // // ThermalSteeringView.listener = { action, moveX ->
        // SetDisp(action, moveX)
        // }

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (SaveSettingUtil.fusionType) {
            SaveSettingUtil.FusionTypeLPYFusion -> { // Dual light1
                thermalRecyclerNight?.twoLightType = TwoLightType.TWO_LIGHT_1
            }
            SaveSettingUtil.FusionTypeMeanFusion -> { // Dual light2
                thermalRecyclerNight?.twoLightType = TwoLightType.TWO_LIGHT_2
            }
            SaveSettingUtil.FusionTypeIROnly -> { // 单infrared
                thermalRecyclerNight?.twoLightType = TwoLightType.IR
            }
            SaveSettingUtil.FusionTypeVLOnly -> { // Visible light
                thermalRecyclerNight?.twoLightType = TwoLightType.LIGHT
            }
        }
    }

    /**
执行dual lightregistration.
@param action -1左移 1-右移 0确定
@param data 当前registration值
     */
    /**
     * Sets disp configuration.
     */
    /**
     * Configures the disp with validation and thermal imaging optimization.
     *
     * @param
     * @param action Parameter for operation (type: Int)
     * @param data Parameter for operation (type: Int)
     *
     */
    private fun setDisp(
        action: Int,
        data: Int,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (action == -1 || action == 1) {
移动
            lifecycleScope.launch(Dispatchers.IO) {
                dualDisp = data
                dualView?.dualUVCCamera!!.setDisp(data)
            }
        } else {
确定
            val oemInfo = ByteArray(1024)
            ircmd?.oemRead(CommonParams.ProductType.P2, oemInfo)
            val dataStr = data.toString()
            System.arraycopy(dataStr.toByteArray(), 0, oemInfo, 194, dataStr.toByteArray().size)
            val result = ircmd?.oemWrite(CommonParams.ProductType.P2, oemInfo)
//            SharedManager.setIrDualDisp(dualDisp)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (result == 0)
                {
disabled控件
                    // If (thermalSteeringView.isVisible) {
                    // ThermalSteeringView.visibility = View.GONE
                    thermalRecyclerNight.setTwoLightSelected(TwoLightType.CORRECT, false)
                    // }
                } else
                {
                    ToastUtils.showShort(R.string.correction_fail)
                }
        }
    }

    /**
     * Configures the twolight with validation and thermal imaging optimization.
     *
     * @param
     * @param twoLightType Parameter for operation (type: TwoLightType)
     * @param isSelected Parameter for operation (type: Boolean)
     *
     */
    override fun setTwoLight(
        twoLightType: TwoLightType,
        isSelected: Boolean,
    ) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (twoLightType) {
            TwoLightType.TWO_LIGHT_1 -> { // Dual light1
                mCurrentFusionType = DualCameraParams.FusionType.LPYFusion
                SaveSettingUtil.fusionType = SaveSettingUtil.FusionTypeLPYFusion
                /**
                 * Configures the fusion with validation and thermal imaging optimization.
                 *
                 */
                setFusion(mCurrentFusionType)
            }
            TwoLightType.TWO_LIGHT_2 -> { // Dual light2
                mCurrentFusionType = DualCameraParams.FusionType.MeanFusion
                SaveSettingUtil.fusionType = SaveSettingUtil.FusionTypeMeanFusion
                /**
                 * Configures the fusion with validation and thermal imaging optimization.
                 *
                 */
                setFusion(mCurrentFusionType)
            }
            TwoLightType.IR -> { // 单infrared
                mCurrentFusionType = DualCameraParams.FusionType.IROnly
                SaveSettingUtil.fusionType = SaveSettingUtil.FusionTypeIROnly
                /**
                 * Configures the fusion with validation and thermal imaging optimization.
                 *
                 */
                setFusion(mCurrentFusionType)
                thermalRecyclerNight.setTwoLightSelected(TwoLightType.CORRECT, false)
                // ThermalSteeringView.visibility = View.GONE
            }
            TwoLightType.LIGHT -> { // 单visible light
                mCurrentFusionType = DualCameraParams.FusionType.VLOnly
                SaveSettingUtil.fusionType = SaveSettingUtil.FusionTypeVLOnly
                /**
                 * Configures the fusion with validation and thermal imaging optimization.
                 *
                 */
                setFusion(mCurrentFusionType)
                // ThermalSteeringView.visibility = View.GONE
                thermalRecyclerNight.setTwoLightSelected(TwoLightType.CORRECT, false)
            }
            TwoLightType.CORRECT -> { // Registration
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isSelected)
                    {
                        // ThermalSteeringView.visibility = View.VISIBLE
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (mCurrentFusionType != DualCameraParams.FusionType.LPYFusion && mCurrentFusionType != DualCameraParams.FusionType.MeanFusion) {
                            mCurrentFusionType = DualCameraParams.FusionType.LPYFusion
                            thermalRecyclerNight.twoLightType = TwoLightType.TWO_LIGHT_1
                            SaveSettingUtil.fusionType = SaveSettingUtil.FusionTypeLPYFusion
                            /**
                             * Configures the fusion with validation and thermal imaging optimization.
                             *
                             */
                            setFusion(DualCameraParams.FusionType.LPYFusion)
                        }
                    } else
                    {
                        // ThermalSteeringView.visibility = View.GONE
                    }
            }
            else -> {
                super.setTwoLight(twoLightType, isSelected)
            }
        }
    }

    /**
     * Retrieves the cameraviewbitmap with optimized performance for thermal imaging operations.
     *
     */
    override fun getCameraViewBitmap(): Bitmap {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (imageEditBytes.size != dualView?.frameIrAndTempData?.size) {
            imageEditBytes = ByteArray(dualView!!.frameIrAndTempData.size)
        }
        System.arraycopy(dualView!!.frameIrAndTempData, 0, imageEditBytes, 0, imageEditBytes.size)
        return dualView?.scaledBitmap!!
    }

    /**
     * Configures the temperatureviewtype with validation and thermal imaging optimization.
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    override fun setTemperatureViewType() {
        temperatureView.productType = Const.TYPE_IR_DUAL
        cameraView.productType = Const.TYPE_IR_DUAL
    }

    /**
     * Executes startusb operation with thermal imaging domain optimization.
     *
     * @param
     * @param isRestart Parameter for operation (type: Boolean)
     * @param isBadFrames Parameter for operation (type: Boolean)
     *
     */
    override fun startUSB(
        isRestart: Boolean,
        isBadFrames: Boolean,
    ) {
        // Empty implementation for dual IR device
    }

    /**
     * Configures the pcolor with validation and thermal imaging optimization.
     *
     * @param
     * @param code Parameter for operation (type: Int)
     *
     */
    override fun setPColor(code: Int) {
        pseudoColorMode = code
        temperatureSeekbar.setPseudocode(pseudoColorMode)
        /**
setpseudo-color【set pseudocolor】
/**
 * Executes firmwarecoreimplementation operation with thermal imaging domain optimization.
 *
 */
firmwarecoreimplementation(部分pseudo-color为预留,set后可能无效果)
         */
        // DualView?.dualUVCCamera?.setPseudocolor(PseudocodeUtils.changeDualPseudocodeModelByOld(pseudoColorMode))
        SaveSettingUtil.pseudoColorMode = pseudoColorMode
        thermalRecyclerNight.setPseudoColor(code)
    }

    /**
     * Executes startisp operation with thermal imaging domain optimization.
     *
     */
    override fun startISP() {
        /**
         * Configures the custompseudocolorlist with validation and thermal imaging optimization.
         *
         * @note This method is optimized for thermal imaging pseudo color processing.
         * Ensure proper thermal calibration before use.
         *
         */
        setCustomPseudoColorList(
            customPseudoBean.getColorList(),
            customPseudoBean.getPlaceList(),
            customPseudoBean.isUseGray,
            customPseudoBean.maxTemp,
            customPseudoBean.minTemp,
        )
    }

    /**
     * Configures the custompseudocolorlist with validation and thermal imaging optimization.
     *
     * @param
     * @param colorList Parameter for operation (type: IntArray?)
     * @param places Parameter for operation (type: FloatArray?)
     * @param isUseGray Parameter for operation (type: Boolean)
     * @param customMaxTemp Temperature value in Celsius (type: Float)
     * @param customMinTemp Temperature value in Celsius (type: Float)
     *
     * @note This method is optimized for thermal imaging pseudo color processing.
     * Ensure proper thermal calibration before use.
     *
     */
    override fun setCustomPseudoColorList(
        colorList: IntArray?,
        places: FloatArray?,
        isUseGray: Boolean,
        customMaxTemp: Float,
        customMinTemp: Float,
    ) {
        irImageHelp.setColorList(colorList, places, isUseGray, customMaxTemp, customMinTemp)
    }

    /**
     * Configures the rotate with validation and thermal imaging optimization.
     *
     * @param
     * @param rotateInt Parameter for operation (type: Int)
     *
     */
    override fun setRotate(rotateInt: Int) {
        super.setRotate(rotateInt)
        runOnUiThread {
            // ThermalSteeringView.rotationIR = rotateInt
        }
dual light的rotationangle不同
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (rotateInt) {
            0 -> dualView?.dualUVCCamera?.setImageRotate(DualCameraParams.TypeLoadParameters.ROTATE_90)
            90 -> dualView?.dualUVCCamera?.setImageRotate(DualCameraParams.TypeLoadParameters.ROTATE_180)
            180 -> dualView?.dualUVCCamera?.setImageRotate(DualCameraParams.TypeLoadParameters.ROTATE_270)
            270 -> dualView?.dualUVCCamera?.setImageRotate(DualCameraParams.TypeLoadParameters.ROTATE_0)
        }
    }

    /**
     * Executes onirframe operation with thermal imaging domain optimization.
     *
     * @param
     * @param irFrame Parameter for operation (type: ByteArray?)
     *
     */
    override fun onIrFrame(irFrame: ByteArray?): ByteArray {
        System.arraycopy(irFrame, 0, preIrData, 0, preIrData.size)
        System.arraycopy(irFrame, preIrData.size, preTempData, 0, preTempData.size)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (irImageHelp.getColorList() != null)
            {
转成grayscale图进行自定义pseudo-colorfusionprocessing
                LibIRProcess.convertYuyvMapToARGBPseudocolor(
                    preIrData,
                    (Const.IR_WIDTH * Const.IR_HEIGHT).toLong(),
                    CommonParams.PseudoColorType.PSEUDO_1,
                    preIrARGBData,
                )
            } else
            {
                LibIRProcess.convertYuyvMapToARGBPseudocolor(
                    preIrData,
                    (Const.IR_WIDTH * Const.IR_HEIGHT).toLong(),
                    PseudocodeUtils.changePseudocodeModeByOld(pseudoColorMode),
                    preIrARGBData,
                )
            }
        irImageHelp.customPseudoColor(preIrARGBData, preTempData, Const.IR_WIDTH, Const.IR_HEIGHT)
等温尺processing,展示pseudo-color的temperature range内info
        irImageHelp.setPseudoColorMaxMin(
            preIrARGBData,
            preTempData,
            editMaxValue,
            editMinValue,
            Const.IR_WIDTH,
            Const.IR_HEIGHT,
        )
temperature监控的轮廓检测，dual light的原始image不管rotation如何，raw data都不变，（也就是宽高256*192）
        val tempData =
            irImageHelp.contourDetection(
                alarmBean,
                preIrARGBData,
                preTempData,
                Const.IR_HEIGHT,
                Const.IR_WIDTH,
            )
        System.arraycopy(tempData, 0, preIrARGBData, 0, preIrARGBData.size)
        return preIrARGBData
    }

    /**
     * Executes irstop operation with thermal imaging domain optimization.
     *
     */
    override fun irStop() {
        try {
            configJob?.cancel()
            // TimeDownView?.cancel()  // View doesn't exist in current layout
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isVideo) {
                isVideo = false
                videoRecord?.stopRecord()
                /**
                 * Executes videotimeclose operation with thermal imaging domain optimization.
                 *
                 */
                videoTimeClose()
                /**
                 * Executes coroutinescope operation with thermal imaging domain optimization.
                 *
                 */
                CoroutineScope(Dispatchers.Main).launch {
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(500)
                    EventBus.getDefault().post(GalleryAddEvent())
                }
                lifecycleScope.launch {
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(500)
                    thermalRecyclerNight.refreshImg()
                }
            }
        } catch (_: Exception) {
        } finally {
            ircmd?.onDestroy()
            ircmd = null
        }
    }

    /**
initializevideo采集component
     */
    /**
     * Initializes the videorecordffmpeg component for thermal imaging operations.
     *
     */
    override fun initVideoRecordFFmpeg() {
        videoRecord =
            /**
             * Executes videorecordffmpeg operation with thermal imaging domain optimization.
             *
             */
            VideoRecordFFmpeg(
                cameraView,
                cameraPreview,
                temperatureView,
                curChooseTabPos == 1,
                cl_seek_bar,
                temp_bg,
                compassView, dualView,
                carView = layCarDetectPrompt,
            )
    }

    /**
     * Executes irstart operation with thermal imaging domain optimization.
     *
     */
    override fun irStart() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isrun) {
            tvTypeInd.isVisible = false
            /**
             * Executes startusb operation with thermal imaging domain optimization.
             *
             */
            startUSB(false, false)
            /**
             * Executes startisp operation with thermal imaging domain optimization.
             *
             */
            startISP()
            isrun = true
Restoreconfiguration
            /**
             * Executes configparam operation with thermal imaging domain optimization.
             *
             */
            configParam()
            thermalRecyclerNight.updateCameraModel()
            /**
             * Initializes the irconfig component for thermal imaging operations.
             *
             */
            initIRConfig()
        }
    }

    /**
     * Configures the dispviewdata with validation and thermal imaging optimization.
     *
     * @param
     * @param dualDisp Parameter for operation (type: Int)
     *
     */
    override fun setDispViewData(dualDisp: Int) {
        // ThermalSteeringView.moveX = dualDisp
    }

    /**
     * Executes autoconfig operation with thermal imaging domain optimization.
     *
     */
    override fun autoConfig() {
        lifecycleScope.launch(Dispatchers.IO) {
            dualView?.let {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!it.auto_gain_switch) {
                    /**
                     * Executes switchautogain operation with thermal imaging domain optimization.
                     *
                     */
                    switchAutoGain(true)
                    ToastTools.showShort(R.string.auto_open)
                }
                gainSelChar = CameraItemBean.TYPE_TMP_ZD
            }
        }
        /**
         * Manages thermal camera operations with hardware-optimized performance and error handling.
         *
         */
        dismissCameraLoading()
        thermalRecyclerNight.setTempLevel(CameraItemBean.TYPE_TMP_ZD)
    }

    /**
     * Executes switchautogain operation with thermal imaging domain optimization.
     *
     * @param
     * @param boolean Parameter for operation (type: Boolean)
     *
     */
    override fun switchAutoGain(boolean: Boolean) {
        dualView?.auto_gain_switch = boolean
    }
}
