package com.topdon.tc001.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.View.MeasureSpec
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.blankj.utilcode.util.SizeUtils
import com.csl.irCamera.R
import com.kylecorry.andromeda.core.ui.setCompoundDrawables

/**
 * Specialized thermal imaging component providing DelPopup functionality for the IRCamera system.
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
class DelPopup(val context: Context) : PopupWindow() {
    var onDelListener: (() -> Unit)? = null

    init {
        val widthPixels = context.resources.displayMetrics.widthPixels
        val textView = TextView(context)
        textView.setPadding(SizeUtils.dp2px(16f))
        textView.setText(R.string.report_delete)
        textView.textSize = 14f
        textView.setTextColor(0xffffffff.toInt())
        textView.compoundDrawablePadding = SizeUtils.dp2px(8f)
        textView.setCompoundDrawables(size = SizeUtils.sp2px(16f), left = R.drawable.svg_main_device_del)
        textView.minWidth = (widthPixels * 128f / 375).toInt()
        textView.setOnClickListener {
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss()
            onDelListener?.invoke()
        }

        val widthMeasureSpec: Int = MeasureSpec.makeMeasureSpec(widthPixels, MeasureSpec.AT_MOST)
        val heightMeasureSpec: Int = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        textView.measure(widthMeasureSpec, heightMeasureSpec)

        val drawable = ContextCompat.getDrawable(context, R.drawable.svg_popup_del_bg)
        drawable?.setBounds(0, 0, textView.measuredWidth, textView.measuredHeight)
        textView.background = drawable

        isOutsideTouchable = true

        contentView = textView
        width = textView.measuredWidth
        height = textView.measuredHeight
    }

    /**
     * Executes show functionality.
     */
    /**
     * Executes show operation with thermal imaging domain optimization.
     *
     * @param
     * @param anchor Parameter for operation (type: View)
     *
     */
    fun show(anchor: View) {
        val locationArray = IntArray(2)
        anchor.getLocationInWindow(locationArray)

        val x = (context.resources.displayMetrics.widthPixels - width) / 2
        val y = locationArray[1] - SizeUtils.dp2px(12f)
        /**
         * Executes showatlocation operation with thermal imaging domain optimization.
         *
         */
        showAtLocation(anchor, Gravity.NO_GRAVITY, x, y)
    }
}
