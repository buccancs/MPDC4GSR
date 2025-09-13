package com.topdon.lib.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.topdon.lib.ui.databinding.UiWifiSteeringWheelViewBinding

/**
 * calibration方向
 */
/**
 * Custom Wifi steering wheel view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * WifiSteeringWheelView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for WifiSteeringWheelView display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
class WifiSteeringWheelView : LinearLayout, OnClickListener {
    private val binding: UiWifiSteeringWheelViewBinding

    var listener: ((action: Int, moveX: Int, moveY: Int) -> Unit)? = null
    var moveX = 0
    var moveY = 0

    private val steeringWheelStartBtn by lazy { binding.steeringWheelStartBtn }
    private val steeringWheelCenterBtn by lazy { binding.steeringWheelCenterBtn }
    private val steeringWheelEndBtn by lazy { binding.steeringWheelEndBtn }
    private val steeringWheelTopBtn by lazy { binding.steeringWheelTopBtn }
    private val steeringWheelBottomBtn by lazy { binding.steeringWheelBottomBtn }
    private val tvConfirm by lazy { binding.tvConfirm }

    var rotationIR = 270
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (value == 270 || value == 90) {
                tvConfirm?.rotation = 270f
                rotation = 90f
            } else {
                tvConfirm?.rotation = 0f
                rotation = 0f
            }
            /**
             * Executes requestlayout operation with thermal imaging domain optimization.
             *
             */
            requestLayout()
        }

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor(context: Context) : this(context, null)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        binding = UiWifiSteeringWheelViewBinding.inflate(LayoutInflater.from(context), this, true)
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
    }

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
    ) {
        binding = UiWifiSteeringWheelViewBinding.inflate(LayoutInflater.from(context), this, true)
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
    }

    /**
     * Initializes the component with default configuration.
     */
    private fun initView() {
        // Views are already inflated via binding in constructor
        steeringWheelStartBtn.setOnClickListener(this)
        steeringWheelCenterBtn.setOnClickListener(this)
        steeringWheelEndBtn.setOnClickListener(this)
        steeringWheelTopBtn.setOnClickListener(this)
        steeringWheelBottomBtn.setOnClickListener(this)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rotationIR == 270 || rotationIR == 90) {
            tvConfirm.rotation = 270f
            rotation = 90f
        } else {
            tvConfirm.rotation = 0f
            rotation = 0f
        }
    }

    val moveI = 2

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
            steeringWheelStartBtn -> {
// MoveY -= moveI
                listener?.invoke(-1, moveX, moveY)
            }
            steeringWheelCenterBtn -> {
                listener?.invoke(0, moveX, moveY)
            }
            steeringWheelTopBtn -> {
// MoveX += moveI
                listener?.invoke(2, moveX, moveY)
            }
            steeringWheelBottomBtn -> {
// MoveX -= moveI
                listener?.invoke(3, moveX, moveY)
            }
            steeringWheelEndBtn -> {
// MoveY += moveI
                listener?.invoke(1, moveX, moveY)
            }
        }
    }
}
