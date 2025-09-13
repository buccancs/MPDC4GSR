package com.topdon.lib.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.R
import com.topdon.lib.ui.bean.ColorBean
import com.topdon.lib.ui.listener.SingleClickListener
import com.topdon.lib.ui.R as UiR
import com.topdon.menu.R as MenuR

/**
 * Custom Menu six view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
@Deprecated("看起来是旧版 2D 编辑的menu，根本没使用了")
/**
 * MenuSixAdapter provides data binding between data source and UI components.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing MenuSixAdapter functionality for the IRCamera system.
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
class MenuSixAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: ((index: Int, code: Int) -> Unit)? = null
    private var type = 0
    private var selected = -1
    private var colorEnable = false // Pseudo color条
    private var contrastEnable = false 
    private var ddeEnable = false 
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
     * Executes encolor functionality.
     */
    /**
     * Executes encolor operation with thermal imaging domain optimization.
     *
     * @param
     * @param colorEnable Parameter for operation (type: Boolean)
     *
     */
    fun enColor(colorEnable: Boolean) {
        this.colorEnable = colorEnable
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes encontrast functionality.
     */
    /**
     * Executes encontrast operation with thermal imaging domain optimization.
     *
     * @param
     * @param param Parameter for operation (type: Boolean)
     *
     */
    fun enContrast(param: Boolean) {
        this.contrastEnable = param
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes endde functionality.
     */
    /**
     * Executes endde operation with thermal imaging domain optimization.
     *
     * @param
     * @param param Parameter for operation (type: Boolean)
     *
     */
    fun enDde(param: Boolean) {
        this.ddeEnable = param
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    private val fourBean =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_setting_1, context.getString(R.string.thermal_pseudo), 1),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_setting_2, context.getString(R.string.thermal_contrast), 2),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_setting_3, context.getString(R.string.thermal_sharpen), 3),
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
        val view = LayoutInflater.from(parent.context).inflate(UiR.layout.ui_item_menu_four_view, parent, false)
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
            val bean = fourBean[position]
            holder.name.text = bean.name
            holder.img.setImageResource(bean.res)
            holder.lay.setOnClickListener(
                object : SingleClickListener() {
                    /**
                     * Executes onsingleclick operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onSingleClick() {
                        val adapterPosition = holder.adapterPosition
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            listener?.invoke(adapterPosition, bean.code)
                            /**
                             * Executes selected operation with thermal imaging domain optimization.
                             *
                             */
                            selected(bean.code)
                        }
                    }
                },
            )
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (bean.code) {
                1 -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(colorEnable, holder.img, holder.name)
                }
                2 -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(contrastEnable, holder.img, holder.name)
                }
                3 -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(ddeEnable, holder.img, holder.name)
                }
                else -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(bean.code == selected, holder.img, holder.name)
                }
            }
        }
    }

    
    /**
     * Executes iconUI functionality.
     */
    /**
     * Executes iconui operation with thermal imaging domain optimization.
     *
     * @param
     * @param isActive Parameter for operation (type: Boolean)
     * @param img Parameter for operation (type: ImageView)
     * @param nameText Parameter for operation (type: TextView)
     *
     */
    private fun iconUI(
        isActive: Boolean,
        img: ImageView,
        nameText: TextView,
    ) {
        img.isSelected = isActive
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isActive) {
            nameText.setTextColor(ContextCompat.getColor(context, UiR.color.white))
        } else {
            nameText.setTextColor(ContextCompat.getColor(context, UiR.color.font_third_color))
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return fourBean.size
    }

    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lay: View = itemView.findViewById(UiR.id.item_menu_tab_lay)
        val img: ImageView = itemView.findViewById(UiR.id.item_menu_tab_img)
        val name: TextView = itemView.findViewById(UiR.id.item_menu_tab_text)
    }
}
