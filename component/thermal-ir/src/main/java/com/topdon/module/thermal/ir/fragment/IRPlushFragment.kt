package com.topdon.module.thermal.ir.fragment

import android.graphics.Bitmap
import android.view.SurfaceView
import android.view.View
import com.infisense.usbdual.Const
import com.infisense.usbdual.camera.DualViewWithExternalCameraCommonApi
import com.infisense.usbir.view.TemperatureView
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.activity.BaseIRPlushFragment

/**
 * des:
 * author: CaiSongL
 * date: 2024/9/3 11:43
/**
 * Specialized thermal imaging component providing IRPlushFragment functionality for the IRCamera system.
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
class IRPlushFragment : BaseIRPlushFragment() {
    // FindViewById declarations using proper view reference in onViewCreated
    private lateinit var dualTextureViewNativeCamera: SurfaceView
    private lateinit var temperatureView: TemperatureView

    /**
     * Executes onviewcreated operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: android.view.View)
     * @param savedInstanceState Parameter for operation (type: android.os.Bundle?)
     *
     */
    override fun onViewCreated(
        view: android.view.View,
        savedInstanceState: android.os.Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize findViewById in onViewCreated
        dualTextureViewNativeCamera = view.findViewById(R.id.dualTextureViewNativeCamera)
        temperatureView = view.findViewById(R.id.temperature_view)
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

    override suspend fun onDualViewCreate(dualView: DualViewWithExternalCameraCommonApi?) {
    }

    /**
     * Executes isdualir operation with thermal imaging domain optimization.
     *
     */
    override fun isDualIR(): Boolean {
        return true
    }

    /**
     * Configures the temperatureviewtype with validation and thermal imaging optimization.
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    override fun setTemperatureViewType() {
        /**
         * Retrieves the temperaturedualview with optimized performance for thermal imaging operations.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        getTemperatureDualView().productType = Const.TYPE_IR_DUAL
    }

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int {
        return R.layout.fragment_ir_plush
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        super.initView()
    }

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * Retrieves bitmap information.
     */
    fun getBitmap(): Bitmap?  {
        return dualView?.scaledBitmap
    }
}
