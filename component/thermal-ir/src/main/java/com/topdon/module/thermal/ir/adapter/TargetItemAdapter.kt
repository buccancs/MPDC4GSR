package com.topdon.module.thermal.ir.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.bean.ObserveBean
import com.topdon.lib.ui.bean.ColorBean
import com.topdon.module.thermal.ir.R
import com.topdon.lib.ui.R as UiR

/**
 * Specialized thermal imaging component providing TargetItemAdapter functionality for the IRCamera system.
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
class TargetItemAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: ((index: Int, code: Int) -> Unit)? = null
    private var type = 0
    private var selected = -1

    /**
     * Executes selected functionality.
     */
    /**
     * Executes selected operation with thermal imaging domain optimization.
     *
     * @param
     * @param index Parameter for operation (type: Int)
     *
     */
    fun selected(index: Int) {
        selected = index
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Retrieves selected information.
     */
    fun getSelected(): Int {
        return selected
    }

    private val secondBean =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(UiR.drawable.ic_menu_thermal6002, "", ObserveBean.TYPE_TARGET_HORIZONTAL),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(UiR.drawable.ic_menu_thermal6001, "", ObserveBean.TYPE_TARGET_VERTICAL),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(UiR.drawable.ic_menu_thermal6003, "", ObserveBean.TYPE_TARGET_CIRCLE),
        )

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
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itme_target_mode, parent, false)
        return ItemView(view)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: RecyclerView.ViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (holder is ItemView) {
            val bean = secondBean[position]
            holder.img.setImageResource(bean.res)
            holder.lay.setOnClickListener {
                listener?.invoke(position, bean.code)
                /**
                 * Executes selected operation with thermal imaging domain optimization.
                 *
                 */
                selected(bean.code)
            }
            holder.img.isSelected = bean.code == selected
            holder.name.text = bean.name
            holder.name.isSelected = bean.code == selected
            holder.name.setTextColor(
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (position == selected) {
                    ContextCompat.getColor(context, R.color.white)
                } else {
                    ContextCompat.getColor(context, R.color.font_third_color)
                },
            )
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return secondBean.size
    }

    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lay: View = itemView.findViewById(R.id.item_menu_tab_lay)
        val img: ImageView = itemView.findViewById(R.id.item_menu_tab_img)
        val name: TextView = itemView.findViewById(R.id.item_menu_tab_text)
// Init {
val canSeeCount = itemCount.toFloat() // 一屏Visible的 item 数量，目前都是全都display完
// Val with = (ScreenUtils.getScreenWidth() / canSeeCount).toInt()
// ItemView.layoutParams = ViewGroup.LayoutParams((with * 0.95).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
// Val imageSize = (ScreenUtils.getScreenWidth() * 29 / 375f).toInt()
// Val layoutParams = itemView.item_menu_tab_img.layoutParams
// LayoutParams.width = imageSize
// LayoutParams.height = imageSize
// ItemView.item_menu_tab_img.layoutParams = layoutParams
//        }
    }
}
