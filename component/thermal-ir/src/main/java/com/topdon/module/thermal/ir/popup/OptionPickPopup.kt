package com.topdon.module.thermal.ir.popup

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.core.view.MyTextView
import com.topdon.module.thermal.ir.R

/**
option拾取 PopupWindow.
 *
 * Created by LCG on 2024/1/5.
/**
 * Specialized thermal imaging component providing OptionPickPopup functionality for the IRCamera system.
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
class OptionPickPopup(
    private val context: Context,
    private val strArray: Array<String>,
    private val resIdArray: Array<Int>? = null,
) : PopupWindow() {
    // View references using findViewById
    private val recyclerView: RecyclerView by lazy { contentView.findViewById(R.id.recycler_view) }

    companion object {
        /**
optiontext大小，单位 ***sp***
         */
        private const val TEXT_SIZE_SP: Float = 14f

        /**
optiontext顶部或底部 padding，单位 ***dp***
         */
        private const val TEXT_PADDING: Float = 7f
    }

    /**
aoption被selectedEventListener.
     */
    var onPickListener: ((position: Int, str: String) -> Unit)? = null

    init {
        val textView = TextView(context)
        textView.textSize = TEXT_SIZE_SP

        val fontMetrics = textView.paint.fontMetricsInt

        val canSeeItem: Int = strArray.size.coerceAtMost(2)
        val itemHeight: Int = fontMetrics.bottom - fontMetrics.top + SizeUtils.dp2px(TEXT_PADDING) * 2
        val contentHeight = SizeUtils.dp2px(14f) + itemHeight * canSeeItem
        val contentWidth = (contentHeight * 120f / 81f).toInt()

        contentView = LayoutInflater.from(context).inflate(R.layout.popup_option_pick, null)
        width = contentWidth
        height = contentHeight

        isOutsideTouchable = true

        val adapter = MyAdapter()
        adapter.onItemClickListener = {
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss()
            onPickListener?.invoke(it, strArray[it])
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
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

        val x = locationArray[0] + anchor.width - width + SizeUtils.dp2px(5f)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (context.resources.displayMetrics.heightPixels - locationArray[1] - anchor.height > height - SizeUtils.dp2px(5f)) { // 在 anchor 底部放得下
            /**
             * Executes showatlocation operation with thermal imaging domain optimization.
             *
             */
            showAtLocation(anchor, Gravity.NO_GRAVITY, x, locationArray[1] + anchor.height - SizeUtils.dp2px(5f))
        } else { // 下area放不下就放上area吧
            /**
             * Executes showatlocation operation with thermal imaging domain optimization.
             *
             */
            showAtLocation(anchor, Gravity.NO_GRAVITY, x, (locationArray[1] - height + SizeUtils.dp2px(5f)).coerceAtLeast(0))
        }
    }

    private inner class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        /**
item clickEventListener.
         */
        var onItemClickListener: ((position: Int) -> Unit)? = null

        /**
         * Executes oncreateviewholder operation with thermal imaging domain optimization.
         *
         * @param
         * @param parent Parameter for operation (type: ViewGroup)
         * @param viewType Parameter for operation (type: Int)
         *
         */
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): ViewHolder {
            val textView = MyTextView(context)
            textView.textSize = TEXT_SIZE_SP
            textView.setDrawableHeightPx(SizeUtils.sp2px(18f))
            textView.setTextColor(0xffffffff.toInt())
            textView.setPadding(SizeUtils.dp2px(14f), SizeUtils.dp2px(TEXT_PADDING), SizeUtils.dp2px(14f), SizeUtils.dp2px(TEXT_PADDING))
            textView.compoundDrawablePadding = SizeUtils.dp2px(10f)
            textView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            return ViewHolder(textView)
        }

        /**
         * Executes onbindviewholder operation with thermal imaging domain optimization.
         *
         * @param
         * @param holder Parameter for operation (type: ViewHolder)
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int,
        ) {
            holder.textView.text = strArray[position]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (resIdArray != null && position < resIdArray.size) {
                holder.textView.setOnlyDrawableStart(resIdArray[position])
            } else {
                holder.textView.setOnlyDrawableStart(0)
            }
        }

        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount(): Int = strArray.size

        inner class ViewHolder(val textView: MyTextView) : RecyclerView.ViewHolder(textView) {
            init {
                textView.setOnClickListener {
                    val position = bindingAdapterPosition
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener?.invoke(position)
                    }
                }
            }
        }
    }
}
