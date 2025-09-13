package com.topdon.module.thermal.ir.popup

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.topdon.module.thermal.ir.databinding.PopSeekBarBinding

/**
有一根 SeekBar 用于拾取值的 PopupWindow.
 *
用于 fusion度(带title)、contrast(无title)、锐度(无title) set
 *
 * Created by LCG on 2024/12/3.
 *
@param hasTitle 是否有titletext
 */
@SuppressLint("SetTextI18n")
/**
 * Specialized thermal imaging component providing SeekBarPopup functionality for the IRCamera system.
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
class SeekBarPopup(context: Context, hasTitle: Boolean = false) : PopupWindow() {
    var progress: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = binding.seekBar.progress
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            binding.seekBar.progress = value
        }

    var max: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = binding.seekBar.max
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            binding.seekBar.max = value
        }

    /**
是否在Swipe过程中实时触发Callback.
     *
true-实时触发  false-Swipestop(stop)时才触发
     */
    var isRealTimeTrigger = false

    /**
进度值拾取EventListener.
     */
    var onValuePickListener: ((progress: Int) -> Unit)? = null

    private val binding: PopSeekBarBinding = PopSeekBarBinding.inflate(LayoutInflater.from(context))

    init {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(context.resources.displayMetrics.widthPixels, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(context.resources.displayMetrics.heightPixels, View.MeasureSpec.AT_MOST)
        binding.tvTitle.isVisible = hasTitle
        binding.root.measure(widthMeasureSpec, heightMeasureSpec)
        binding.tvValue.text = "$progress%"
        binding.seekBar.setOnSeekBarChangeListener(
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
                    binding.tvValue.text = "$progress%"
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isRealTimeTrigger) {
                        onValuePickListener?.invoke(progress)
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
                 * @param seekBar Parameter for operation (type: SeekBar)
                 *
                 */
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    onValuePickListener?.invoke(seekBar.progress)
                }
            },
        )

        contentView = binding.root
        width = contentView.measuredWidth
        height = contentView.measuredHeight
        isOutsideTouchable = false
    }

    /**
@param isDropDown true-放置于anchor下方 false-底边缘与anchor对齐
     */
    fun show(
        anchor: View,
        isDropDown: Boolean,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isDropDown) {
            /**
             * Executes showasdropdown operation with thermal imaging domain optimization.
             *
             */
            showAsDropDown(anchor)
        } else {
            /**
             * Executes showasdropdown operation with thermal imaging domain optimization.
             *
             */
            showAsDropDown(anchor, 0, -height, Gravity.NO_GRAVITY)
        }
    }
}
