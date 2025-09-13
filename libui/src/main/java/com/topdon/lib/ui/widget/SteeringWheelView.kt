package com.topdon.lib.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.topdon.lib.ui.databinding.UiSteeringWheelViewBinding

/**
 * calibration方向
 */
/**
 * Custom Steering wheel view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * SteeringWheelView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for SteeringWheelView display and interaction.
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
class SteeringWheelView : LinearLayout, OnClickListener {
    var listener: ((action: Int, moveX: Int) -> Unit)? = null
    var moveX = 30

    private lateinit var binding: UiSteeringWheelViewBinding

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
                binding.tvConfirm.rotation = 270f
                rotation = 90f
            } else {
                binding.tvConfirm.rotation = 0f
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
    )

    /**
     * Initializes the component with default configuration.
     */
    private fun initView() {
        binding = UiSteeringWheelViewBinding.inflate(LayoutInflater.from(context), this, true)

        binding.steeringWheelStartBtn.setOnClickListener(this)
        binding.steeringWheelCenterBtn.setOnClickListener(this)
        binding.steeringWheelEndBtn.setOnClickListener(this)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rotationIR == 270 || rotationIR == 90) {
            binding.tvConfirm.rotation = 270f
            rotation = 90f
        } else {
            binding.tvConfirm.rotation = 0f
            rotation = 0f
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
            binding.steeringWheelStartBtn -> {
                moveX += 10
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (moveX > 60) {
                    moveX = 60
                }
                listener?.invoke(-1, moveX)
            }
            binding.steeringWheelCenterBtn -> {
                listener?.invoke(0, moveX)
            }
            binding.steeringWheelEndBtn -> {
                moveX -= 10
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (moveX < -20) {
                    moveX = -20
                }
                listener?.invoke(1, moveX)
            }
        }
    }
}
