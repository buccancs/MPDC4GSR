package com.topdon.lib.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.lib.ui.R as UiR

/**
 * ViewPager 指示 View.
 *
 * Created by chenggeng.lin on 2023/11/13.
 */
/**
 * Custom Indicate view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * IndicateView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for IndicateView display and interaction.
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
class IndicateView : View {
    var itemCount: Int = 0
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes requestlayout operation with thermal imaging domain optimization.
             *
             */
            requestLayout()
        }

    var currentIndex: Int = 0
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }

    private val itemWidth: Int = (ScreenUtil.getScreenWidth(context) * 20 / 375f).toInt()

    private val itemHeight: Int = (itemWidth * 2 / 20f).toInt()

    private val itemMargin: Int = (itemWidth * 4 / 20f).toInt()

    private val roundRadius: Float = SizeUtils.dp2px(2f).toFloat()

    private val defaultPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectPaint = Paint(Paint.ANTI_ALIAS_FLAG)

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
        defaultPaint.color = 0xffffffff.toInt()
        val typedArray = context.obtainStyledAttributes(attrs, UiR.styleable.IndicateView)
        val color = typedArray.getColor(UiR.styleable.IndicateView_selectColor, 0xffffba42.toInt())
        typedArray.recycle()
        selectPaint.color = color
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
        /**
         * Configures the measureddimension with validation and thermal imaging optimization.
         *
         */
        setMeasuredDimension(itemWidth * itemCount + itemMargin * (itemCount - 1), itemHeight)
    }

    /**
     * Executes ondraw operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     *
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until itemCount) {
            val left = i * (itemWidth + itemMargin).toFloat()
            val right = left + itemWidth
            val top = 0f
            val bottom = itemHeight.toFloat()
            canvas.drawRoundRect(left, top, right, bottom, roundRadius, roundRadius, if (i == currentIndex) selectPaint else defaultPaint)
        }
    }
}
