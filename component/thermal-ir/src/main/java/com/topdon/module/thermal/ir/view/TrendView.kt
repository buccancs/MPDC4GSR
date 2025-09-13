package com.topdon.module.thermal.ir.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.blankj.utilcode.util.SizeUtils
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.databinding.ViewTrendBinding
import kotlin.math.min

/**
趋势图折line图及对应箭头等的封装.
 *
 * Created by LCG on 2024/12/31.
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for TrendView display and interaction.
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
class TrendView : FrameLayout {
    /**
展开趋势图
     */
    /**
     * Executes expand operation with thermal imaging domain optimization.
     *
     */
    fun expand() {
        binding.clOpen.isVisible = true
        binding.llClose.isVisible = false
    }

    /**
收起趋势图
     */
    /**
     * Executes close operation with thermal imaging domain optimization.
     *
     */
    fun close() {
        binding.clOpen.isVisible = false
        binding.llClose.isVisible = true
    }

    /**
根据指定的datarefresh折line图data
@param tempList temperature值列表，单位摄氏度
     */
    /**
     * Executes refreshChart functionality.
     */
    /**
     * Executes refreshchart operation with thermal imaging domain optimization.
     *
     * @param
     * @param tempList Temperature value in Celsius (type: List<Float>)
     *
     */
    fun refreshChart(tempList: List<Float>) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isVisible && binding.clOpen.isVisible) {
            binding.viewChartTrend.refresh(tempList)
        }
    }

    /**
将折line图clear
     */
    /**
     * Configures the toempty with validation and thermal imaging optimization.
     *
     */
    fun setToEmpty() {
        binding.viewChartTrend.setToEmpty()
    }

    private lateinit var binding: ViewTrendBinding

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
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     * @param defStyleRes Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isInEditMode) {
            LayoutInflater.from(context).inflate(R.layout.view_trend, this, true)
        } else {
            binding = ViewTrendBinding.inflate(LayoutInflater.from(context), this, true)

            binding.ivClose.setOnClickListener {
                binding.clOpen.isVisible = false
                binding.llClose.isVisible = true
            }
            binding.ivOpen.setOnClickListener {
                binding.clOpen.isVisible = true
                binding.llClose.isVisible = false
            }
        }
    }

    /**
     * Configures the visibility with validation and thermal imaging optimization.
     *
     * @param
     * @param visibility Parameter for operation (type: Int)
     *
     */
    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (visibility == View.GONE) {
            binding.viewChartTrend.setToEmpty()
        }
    }

    /**
     * Executes onmeasure operation with thermal imaging domain optimization.
     *
     * @param
     * @param widthMeasureSpec Parameter for operation (type: Int)
     * @param heightMeasureSpec Parameter for operation (type: Int)
     *
     */
    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
    ) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

宽度为 UNSPECIFIED 的情况目前不存在，不考虑
        val wantHeight: Int = SizeUtils.dp2px(34f) + (widthSize * 158 / 264f).toInt()
        val height =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (heightMode) {
                MeasureSpec.EXACTLY -> heightSize
                MeasureSpec.AT_MOST -> min(wantHeight, heightSize)
                else -> wantHeight
            }

        val newWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        val newHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(newWidthSpec, newHeightSpec)
    }
}
