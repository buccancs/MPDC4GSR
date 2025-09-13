package com.topdon.module.thermal.ir.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.SizeUtils

/**
常用材料emissivity 页area所用，一行常用材料emissivity.
 *
 * Created by LCG on 2024/10/14.
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for EmissivityView display and interaction.
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
class EmissivityView : View {
    companion object {
        /**
defaultoutline尺寸，单位 dp.
         */
        private const val DEFAULT_STROKE_WIDTH: Float = 0.5f
    }

    /**
是否顶部对齐
     */
    var isAlignTop = false

    /**
是否需要drawing顶部横line
     */
    var drawTopLine = false

    /**
要display的text列表.
     */
    private val textList: ArrayList<CharSequence> = ArrayList(3)

    /**
执行drawing的 Layout 列表.
     */
    private val layoutList: ArrayList<StaticLayout> = ArrayList(3)

    private val strokeWidth = SizeUtils.dp2px(DEFAULT_STROKE_WIDTH).coerceAtLeast(1).toFloat()
    private val linePaint = Paint()
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

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
        linePaint.color = 0xff5b5961.toInt()
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = strokeWidth
    }

    /**
     * Executes refreshText functionality.
     */
    /**
     * Executes refreshtext operation with thermal imaging domain optimization.
     *
     * @param
     * @param newList Parameter for operation (type: List<String>)
     *
     */
    fun refreshText(newList: List<String>) {
        textList.clear()
        textList.addAll(newList)

        textPaint.color = if (textList.size == 1) 0xffffffff.toInt() else 0xccffffff.toInt()
        textPaint.textSize = SizeUtils.sp2px(if (textList.size == 1) 12f else 11f).toFloat()

        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
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
        val widthSize: Int = MeasureSpec.getSize(widthMeasureSpec) - paddingStart - paddingEnd
        val firstWidth: Int = (widthSize * 135 / 335f).toInt() // 3 列的比例为 135:100:100
        val elseWidth: Int = (widthSize - firstWidth) / 2
        val contentWidth: Int = firstWidth + elseWidth * 2

initialize layoutList
        layoutList.clear()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in textList.indices) {
            val textWidth: Int =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (textList.size == 1) {
                    contentWidth - SizeUtils.dp2px(24f) // 左右各 12dp padding
                } else {
                    (if (i == 0) firstWidth else elseWidth) - SizeUtils.dp2px(24f) // 左右各 12dp padding
                }
            layoutList.add(
                StaticLayout.Builder.obtain(textList[i], 0, textList[i].length, textPaint, textWidth)
                    .setAlignment(Layout.Alignment.ALIGN_CENTER)
                    .build(),
            )
        }

calculation最大高度
        var maxHeight = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (layout in layoutList) {
            maxHeight = maxHeight.coerceAtLeast(layout.height)
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (maxHeight == 0) { // 没有settings要Show/Display的字符时，给个占位的高度好了
            maxHeight = textPaint.fontMetricsInt.bottom - textPaint.fontMetricsInt.top
        }
        maxHeight += SizeUtils.dp2px(12f) // 上下各 6dp padding

宽度为 UNSPECIFIED 的情况目前不存在，不考虑
        /**
         * Configures the measureddimension with validation and thermal imaging optimization.
         *
         */
        setMeasuredDimension(contentWidth + paddingStart + paddingEnd, maxHeight)
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
        canvas.translate(paddingStart.toFloat(), 0f)

        val contentWidth = (width - paddingStart - paddingEnd).toFloat()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (drawTopLine) {
            canvas.drawLine(0f, strokeWidth / 2, contentWidth, strokeWidth / 2, linePaint)
        }
        canvas.drawLine(0f, height.toFloat() - strokeWidth / 2, contentWidth, height.toFloat() - strokeWidth / 2, linePaint)
        canvas.drawLine(strokeWidth / 2, 0f, strokeWidth / 2, height.toFloat(), linePaint)

        val padding = SizeUtils.dp2px(12f).toFloat()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (layout in layoutList) {
            canvas.save()
            canvas.translate(padding, if (isAlignTop) SizeUtils.dp2px(6f).toFloat() else (height - layout.height) / 2f)
            layout.draw(canvas)
            canvas.restore()

            val itemWidth = padding + layout.width.toFloat() + padding
            canvas.drawLine(itemWidth - strokeWidth / 2, 0f, itemWidth - strokeWidth / 2, height.toFloat(), linePaint)
            canvas.translate(itemWidth, 0f)
        }
    }
}
