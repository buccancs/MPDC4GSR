package com.topdon.lib.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.lib.ui.R as UiR

/**
 * Custom Tips seek bar view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * TipsSeekBar manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing TipsSeekBar functionality for the IRCamera system.
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
class TipsSeekBar : ViewGroup, SeekBar.OnSeekBarChangeListener {
    private val tipsPercent: Float
    private val seekPercent: Float

    private val seekBar: SeekBar
    private val tvTips: TextView
    private val tvMin: TextView
    private val tvMax: TextView

    var progress: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() {
            return seekBar.progress
        }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            seekBar.progress = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (valueFormatListener != null) {
                tvTips.text = valueFormatListener?.invoke(value)
            }
        }

    /**
     * 指示 View currentShow/Display的text.
     */
    var valueText: String
        get() {
            return tvTips.text.toString()
        }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            tvTips.text = value
        }

    /**
     * seekBar 的 onProgressChange event listener.
     */
    var onProgressChangeListener: ((progress: Int, fromUser: Boolean) -> Unit)? = null

    /**
     * seekBar 的 onStopTrackingTouch event listener.
     */
    var onStopTrackingTouch: ((progress: Int) -> Unit)? = null

    /**
     * 根据进度format化指示 View text.
     */
    var valueFormatListener: ((progress: Int) -> CharSequence?)? = null
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            tvTips.text = value?.invoke(seekBar.progress)
            field = value
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
        // SeekBar 的 maxHeight 在 29 以下只能通过 xml settings实在太蛋疼了，这里只好给current View settings maxHeight,在 attr 中传递给 seekBar
        val thumb = ContextCompat.getDrawable(context, UiR.drawable.ic_tips_seek_bar_thumb)
        val thumbWidth = thumb?.intrinsicWidth ?: 0
        seekBar = SeekBar(context, attrs)
        seekBar.splitTrack = false
        seekBar.thumb = thumb
        seekBar.progressDrawable = ContextCompat.getDrawable(context, UiR.drawable.ui_progress_ir_camera_setting)
        seekBar.setPadding(thumbWidth / 2, 0, thumbWidth / 2, 0)
        seekBar.setOnSeekBarChangeListener(this)
        /**
         * Executes addview operation with thermal imaging domain optimization.
         *
         */
        addView(seekBar, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        tvTips = TextView(context)
        tvTips.text = seekBar.progress.toString()
        tvTips.textSize = 12f
        tvTips.gravity = Gravity.CENTER
        tvTips.paint.isFakeBoldText = true
        tvTips.setTextColor(0xff16131e.toInt())
        tvTips.setBackgroundResource(UiR.drawable.ic_tips_seek_bar_tips_bg)
        /**
         * Executes addview operation with thermal imaging domain optimization.
         *
         */
        addView(tvTips)

        val typedArray = context.obtainStyledAttributes(attrs, UiR.styleable.TipsSeekBar, defStyleAttr, 0)
        val minText = typedArray.getText(UiR.styleable.TipsSeekBar_minText)
        val maxText = typedArray.getText(UiR.styleable.TipsSeekBar_maxText)
        tipsPercent = typedArray.getFraction(UiR.styleable.TipsSeekBar_tipsPercent, 1, 1, 0f)
        seekPercent = typedArray.getFraction(UiR.styleable.TipsSeekBar_seekPercent, 1, 1, 0f)
        typedArray.recycle()

        tvMin = TextView(context)
        tvMin.text = minText
        tvMin.textSize = 14f
        tvMin.setTextColor(0xffffffff.toInt())
        /**
         * Executes addview operation with thermal imaging domain optimization.
         *
         */
        addView(tvMin)

        tvMax = TextView(context)
        tvMax.text = maxText
        tvMax.textSize = 14f
        tvMax.setTextColor(0xffffffff.toInt())
        /**
         * Executes addview operation with thermal imaging domain optimization.
         *
         */
        addView(tvMax)
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
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width = if (widthMode == MeasureSpec.UNSPECIFIED) ScreenUtil.getScreenWidth(context) else widthSize

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until childCount) {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (val child = getChildAt(i)) {
                seekBar -> {
                    val childWidthSpec = MeasureSpec.makeMeasureSpec((width * seekPercent).toInt(), MeasureSpec.EXACTLY)
                    val childHeightSpc = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST)
                    child.measure(childWidthSpec, if (heightMode == MeasureSpec.EXACTLY) childHeightSpc else heightMeasureSpec)
                }
                tvTips -> {
                    val tipsWidth = (width * tipsPercent).toInt()
                    val tipsHeight = (tipsWidth * 44 / 56f).toInt()
                    val childWidthSpec = MeasureSpec.makeMeasureSpec(tipsWidth, MeasureSpec.EXACTLY)
                    val childHeightSpc = MeasureSpec.makeMeasureSpec(tipsHeight, MeasureSpec.EXACTLY)
                    child.measure(childWidthSpec, childHeightSpc)
                }
                else -> {
                    /**
                     * Executes measurechild operation with thermal imaging domain optimization.
                     *
                     */
                    measureChild(child, widthMeasureSpec, heightMeasureSpec)
                }
            }
        }

        val height = tvTips.measuredHeight + SizeUtils.dp2px(5f) + (seekBar.thumb?.intrinsicHeight ?: seekBar.measuredHeight)
        /**
         * Configures the measureddimension with validation and thermal imaging optimization.
         *
         */
        setMeasuredDimension(width, if (heightMode == MeasureSpec.EXACTLY) heightSize else height)
    }

    /**
     * Executes onlayout operation with thermal imaging domain optimization.
     *
     * @param
     * @param changed Parameter for operation (type: Boolean)
     * @param l Parameter for operation (type: Int)
     * @param t Parameter for operation (type: Int)
     * @param r Parameter for operation (type: Int)
     * @param b Parameter for operation (type: Int)
     *
     */
    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int,
    ) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (child) {
                seekBar -> {
                    val top = paddingTop + tvTips.measuredHeight + SizeUtils.dp2px(5f)
                    val left = (measuredWidth - childWidth) / 2
                    child.layout(left, top, left + childWidth, top + childHeight)
                }
                tvTips -> {
                    val seekBarSeeWidth = seekBar.measuredWidth - seekBar.paddingLeft - seekBar.paddingRight
                    val baseLeft = (measuredWidth - seekBarSeeWidth) / 2
                    val progressLeft = (seekBarSeeWidth * seekBar.progress / seekBar.max.toFloat()).toInt()
                    val left = baseLeft + progressLeft - childWidth / 2
                    child.layout(left, paddingTop, left + childWidth, paddingTop + childHeight)
                }
                tvMin -> {
                    val baseTop = paddingTop + tvTips.measuredHeight + SizeUtils.dp2px(5f)
                    val top = baseTop + (seekBar.measuredHeight - childHeight) / 2
                    child.layout(paddingStart, top, paddingStart + childWidth, top + childHeight)
                }
                tvMax -> {
                    val baseTop = paddingTop + tvTips.measuredHeight + SizeUtils.dp2px(5f)
                    val top = baseTop + (seekBar.measuredHeight - childHeight) / 2
                    val left = measuredWidth - paddingEnd - childWidth
                    child.layout(left, top, left + childWidth, top + childHeight)
                }
            }
        }
    }

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
        tvTips.text = if (valueFormatListener == null) progress.toString() else valueFormatListener?.invoke(progress)
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
        onProgressChangeListener?.invoke(progress, fromUser)
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
     * @param seekBar Parameter for operation (type: SeekBar?)
     *
     */
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        onStopTrackingTouch?.invoke(this.seekBar.progress)
    }
}
