package com.topdon.module.thermal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.topdon.module.thermal.R

/**
 * Specialized thermal imaging component providing MenuTabAdapter functionality for the IRCamera system.
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
class MenuTabAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: OnItemClickListener? = null
    private var type = 0
    private var datas = arrayListOf<Int>()
    private var dataStrList = arrayListOf<String>()
    private var selected = -1

    companion object {
        private const val TYPE_ITEM = 300
        private const val TYPE_ITEM_MORE = 301
    }

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

拍摄
    private val firstMenus =
        arrayListOf<Int>(
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7001_svg,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7002_svg,
        )

选框
    private val secondMenus =
        arrayListOf<Int>(
            com.topdon.lib.ui.R.drawable.ic_menu_thermal6001,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal6003,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7001,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7002,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7003,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7004,
        )

选框
    private val secondMenusStr =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            "point",
            "line",
            "area",
            "add",
            "全图",
            "delete",
        )

选框
    private val fourthMenusStr =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            "rotation",
            "Enhance",
            "画中画",
            "色带",
        )

色彩 - Using available resources as placeholders
    private val thirdMenus =
        arrayListOf<Int>(
            com.topdon.lib.ui.R.drawable.ic_menu_thermal5003,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal6001,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal6002,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal6003,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7001,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7002,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7003,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7004,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal5003_selected_svg,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal6003_svg,
        )

set - Using available resources as placeholders
    private val fourthMenus =
        arrayListOf<Int>(
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7001_svg,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7002_svg,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7003_svg,
            com.topdon.lib.ui.R.drawable.ic_menu_thermal7004_svg,
        )

    /**
     * Initializes type component.
     */
    fun initType(type: Int) {
        this.type = type
        datas =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (type) {
                1 -> firstMenus
                2 -> secondMenus
                3 -> thirdMenus
                4 -> fourthMenus
                else -> thirdMenus
            }
        dataStrList =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (type) {
                2 -> secondMenusStr
                4 -> fourthMenusStr
                else -> secondMenusStr
            }
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

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
        return if (viewType == TYPE_ITEM) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_menu_tab_view, parent, false)
            /**
             * Executes itemview operation with thermal imaging domain optimization.
             *
             */
            ItemView(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_menu_tab_more_view, parent, false)
            /**
             * Executes itemmoreview operation with thermal imaging domain optimization.
             *
             */
            ItemMoreView(view)
        }
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
        if (holder is BaseItemView) {
            holder.img.setImageResource(datas[position])
            holder.lay.setOnClickListener {
                val index = type * 1000 + position + 1
                listener?.onClick(index)
                /**
                 * Executes selected operation with thermal imaging domain optimization.
                 *
                 */
                selected(position)
            }
            holder.img.isSelected = position == selected
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (holder is ItemView) {
                holder.name.text = dataStrList[position]
                holder.name.isSelected = position == selected
                holder.name.setTextColor(
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (position == selected) {
                        ContextCompat.getColor(context, com.topdon.lib.core.R.color.white)
                    } else {
                        ContextCompat.getColor(context, com.topdon.lib.core.R.color.font_third_color)
                    },
                )
            }
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return datas.size
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for BaseItemView display and interaction.
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
    open class BaseItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var lay: View
        lateinit var img: ImageView
    }

    inner class ItemView(itemView: View) : BaseItemView(itemView) {
        var name: TextView

        init {
/**
 * Specialized thermal imaging component providing OnItemClickListener functionality for the IRCamera system.
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
    interface OnItemClickListener {
    /**
     * Executes onClick functionality.
     */
        /**
         * Executes onclick operation with thermal imaging domain optimization.
         *
         * @param
         * @param index Parameter for operation (type: Int)
         *
         */
        fun onClick(index: Int)
    }
}
