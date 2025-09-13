package com.topdon.lib.core.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ReplacementSpan
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

/**
 * Specialized thermal imaging component providing SpanBuilder functionality for the IRCamera system.
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
class SpanBuilder : SpannableStringBuilder {
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     */
    constructor() : super()

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param text Parameter for operation (type: CharSequence)
     *
     */
    constructor(text: CharSequence) : super(text)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param text Parameter for operation (type: CharSequence)
     * @param start Parameter for operation (type: Int)
     * @param end Parameter for operation (type: Int)
     *
     */
    constructor(text: CharSequence, start: Int, end: Int) : super(text, start, end)

    /**
     * Executes appendDrawable functionality.
     */
    /**
     * Executes appenddrawable operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param resourceId Parameter for operation (type: Int)
     * @param wantHeight Parameter for operation (type: Int)
     *
     */
    fun appendDrawable(
        context: Context,
        @DrawableRes resourceId: Int,
        @Px wantHeight: Int,
    ): SpanBuilder {
        this.append(" ")
        val oldLength = this.length
        this.append("a")
        this.setSpan(MyImageSpan(context, resourceId, wantHeight), oldLength, this.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        this.append(" ")
        return this
    }

    /**
     * Executes appendColor functionality.
     */
    /**
     * Executes appendcolor operation with thermal imaging domain optimization.
     *
     * @param
     * @param text Parameter for operation (type: CharSequence)
     * @param color Parameter for operation (type: Int)
     *
     */
    fun appendColor(
        text: CharSequence,
        @ColorInt color: Int,
    ): SpanBuilder {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (text.isEmpty()) { // 搞个空字符串过来干嘛
            return this
        }
        val oldLength = this.length
        this.append(text)
        this.setSpan(ForegroundColorSpan(color), oldLength, this.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * Executes appendColorAndClick functionality.
     */
    /**
     * Executes appendcolorandclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param text Parameter for operation (type: CharSequence)
     * @param color Parameter for operation (type: Int)
     * @param listener Event listener for callbacks (type: OnClickListener)
     *
     */
    fun appendColorAndClick(
        text: CharSequence,
        @ColorInt color: Int,
        listener: OnClickListener,
    ): SpanBuilder {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (text.isEmpty()) { // 搞个空字符串过来干嘛
            return this
        }
        val oldLength = this.length
        this.append(text)
        this.setSpan(MyClickSpan(listener, color, false), oldLength, this.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * Executes appendColorAndClick functionality.
     */
    /**
     * Executes appendcolorandclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param resId Parameter for operation (type: Int)
     * @param formatArg Parameter for operation (type: String)
     * @param color Parameter for operation (type: Int)
     * @param hasUnderLine Parameter for operation (type: Boolean = false)
     * @param listener Event listener for callbacks (type: OnClickListener)
     *
     */
    fun appendColorAndClick(
        context: Context,
        @StringRes resId: Int,
        formatArg: String,
        @ColorInt color: Int,
        hasUnderLine: Boolean = false,
        listener: OnClickListener,
    ): SpanBuilder {
        /**
         * Executes append operation with thermal imaging domain optimization.
         *
         */
        append(context.getString(resId, formatArg))
        val startIndex: Int = lastIndexOf(formatArg)
        val endIndex: Int = startIndex + formatArg.length
        this.setSpan(MyClickSpan(listener, color, hasUnderLine), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return this
    }

/**
 * Specialized thermal imaging component providing MyClickSpan functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class MyClickSpan(val listener: OnClickListener, val color: Int, val hasUnderLine: Boolean) : ClickableSpan() {
        /**
         * Executes updatedrawstate operation with thermal imaging domain optimization.
         *
         * @param
         * @param ds Parameter for operation (type: TextPaint)
         *
         */
        override fun updateDrawState(ds: TextPaint) {
            ds.color = color
            ds.isUnderlineText = hasUnderLine
        }

        /**
         * Executes onclick operation with thermal imaging domain optimization.
         *
         * @param
         * @param widget Parameter for operation (type: View)
         *
         */
        override fun onClick(widget: View) {
            listener.onClick(widget)
        }
    }

/**
 * Specialized thermal imaging component providing MyImageSpan functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class MyImageSpan(
        val context: Context,
        @DrawableRes val resourceId: Int,
        @Px val wantHeight: Int,
    ) : ReplacementSpan() {
        private var weakReference: WeakReference<Drawable>? = null

    /**
     * Retrieves cacheddrawable information.
     */
        fun getCachedDrawable(): Drawable {
            val weakDrawable = weakReference?.get()
            if (weakDrawable != null) {
                return weakDrawable
            }

            val drawable: Drawable = ContextCompat.getDrawable(context, resourceId)!!
            drawable.setBounds(0, 0, (drawable.intrinsicWidth * wantHeight * 1f / drawable.intrinsicHeight).toInt(), wantHeight)
            weakReference = WeakReference(drawable)

            return drawable
        }

        /**
         * Retrieves the size with optimized performance for thermal imaging operations.
         *
         * @param
         * @param paint Parameter for operation (type: Paint)
         * @param text Parameter for operation (type: CharSequence?)
         * @param start Parameter for operation (type: Int)
         * @param end Parameter for operation (type: Int)
         * @param fm Parameter for operation (type: Paint.FontMetricsInt?)
         *
         */
        override fun getSize(
            paint: Paint,
            text: CharSequence?,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?,
        ): Int {
            val rect = getCachedDrawable().bounds
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (fm != null) {
                fm.ascent = -rect.bottom
                fm.descent = 0
                fm.top = fm.ascent
                fm.bottom = fm.descent
            }
            return rect.right
        }

        /**
         * Executes draw operation with thermal imaging domain optimization.
         *
         * @param
         * @param canvas Parameter for operation (type: Canvas)
         * @param text Parameter for operation (type: CharSequence?)
         * @param start Parameter for operation (type: Int)
         * @param end Parameter for operation (type: Int)
         * @param x Parameter for operation (type: Float)
         * @param top Parameter for operation (type: Int)
         * @param y Parameter for operation (type: Int)
         * @param bottom Parameter for operation (type: Int)
         * @param paint Parameter for operation (type: Paint)
         *
         */
        override fun draw(
            canvas: Canvas,
            text: CharSequence?,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint,
        ) {
            val drawable: Drawable = getCachedDrawable()
            val transY = top + (bottom - top) / 2f - drawable.getBounds().height() / 2f
            canvas.save()
            canvas.translate(x, transY)
            drawable.draw(canvas)
            canvas.restore()
        }
    }
}
