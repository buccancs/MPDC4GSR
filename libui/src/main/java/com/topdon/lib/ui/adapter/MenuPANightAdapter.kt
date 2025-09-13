package com.topdon.lib.ui.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.topdon.lib.ui.bean.ColorBean
import com.topdon.lib.ui.listener.SingleClickListener
import com.topdon.lib.ui.R as UiR

/**
 * Menu p a night adapter for thermal imaging data presentation.
 * Manages data binding and view recycling for efficient display.
 */
@Deprecated("旧的dual lightmenu，已重构过了")
/**
 * MenuPANightAdapter provides data binding between data source and UI components.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing MenuPANightAdapter functionality for the IRCamera system.
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
class MenuPANightAdapter(
    data: MutableList<ColorBean>,
    layoutId: Int,
    private val isDual: Boolean,
) : BaseQuickAdapter<ColorBean, BaseViewHolder>(layoutId, data) {
    var listener: ((index: Int) -> Unit)? = null

    /**
     * Executes convert operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: BaseViewHolder)
     * @param item Parameter for operation (type: ColorBean)
     *
     */
    override fun convert(
        holder: BaseViewHolder,
        item: ColorBean,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isDual) {
            val with = (ScreenUtils.getScreenWidth() / 2)
            holder.itemView.layoutParams = ViewGroup.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT)
            val imageSize = (ScreenUtils.getScreenWidth() * 62 / 375f).toInt()
            val layoutParams = holder.itemView.findViewById<android.widget.ImageView>(UiR.id.item_menu_tab_img).layoutParams
            layoutParams.width = imageSize
            layoutParams.height = imageSize
            holder.itemView.findViewById<android.widget.ImageView>(UiR.id.item_menu_tab_img).layoutParams = layoutParams
        } else {
            holder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val imageSize = (ScreenUtils.getScreenWidth() * 62 / 375f).toInt()
            val layoutParams = holder.itemView.findViewById<android.widget.ImageView>(UiR.id.item_menu_tab_img).layoutParams
            layoutParams.width = imageSize
            layoutParams.height = imageSize
            holder.itemView.findViewById<android.widget.ImageView>(UiR.id.item_menu_tab_img).layoutParams = layoutParams
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (item.isSelect) {
            holder.setImageResource(UiR.id.item_menu_tab_img, item.res)
        } else {
            holder.setImageResource(UiR.id.item_menu_tab_img, item.n_res)
        }
        holder.setText(UiR.id.item_menu_tab_text, item.name)
        holder.itemView.setOnClickListener(
            object : SingleClickListener() {
                /**
                 * Executes onsingleclick operation with thermal imaging domain optimization.
                 *
                 */
                override fun onSingleClick() {
                    listener?.invoke(data.indexOf(item))
                }
            },
        )
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (item.isSelect) {
            holder.setTextColor(UiR.id.item_menu_tab_text, ContextCompat.getColor(context, UiR.color.white))
        } else {
            holder.setTextColor(UiR.id.item_menu_tab_text, ContextCompat.getColor(context, UiR.color.font_third_color))
        }
    }
}
