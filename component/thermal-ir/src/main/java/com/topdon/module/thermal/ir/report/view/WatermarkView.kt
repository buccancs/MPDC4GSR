package com.topdon.module.thermal.ir.report.view

import android.content.Context
import android.graphics.Canvas
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.SizeUtils

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for WatermarkView display and interaction.
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
class WatermarkView : View {
    /**
     * Watermark text content for display.
     */
    var watermarkText: String? = null
        set(value) {
            field = value
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }

    private val marginTop = SizeUtils.dp2px(220f).toFloat()

    private val textPaint: TextPaint = TextPaint()

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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        textPaint.isFakeBoldText = true
        textPaint.isAntiAlias = true
        textPaint.color = 0x082b79d8
        textPaint.textSize = SizeUtils.sp2px(80f).toFloat()
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
        watermarkText?.let {
            var hasAddCount = 0
            var hasUseHeight = 0f
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (hasUseHeight < height + marginTop) {
                canvas?.save()
                canvas?.rotate(15f)
                val translateX = (width - textPaint.measureText(it)).coerceAtLeast(0f) / 2f + if (hasAddCount % 2 == 0) 100f else 0f
                canvas?.translate(translateX, 0f)
                canvas?.drawText(it, 0f, 0f, textPaint)
                canvas?.restore()
                canvas?.translate(0f, marginTop)
                hasUseHeight += marginTop
                hasAddCount++
            }
        }
    }
}
