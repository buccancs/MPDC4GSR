package com.topdon.module.thermal.ir.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.topdon.module.thermal.ir.R
import com.topdon.menu.R as MenuR

/**
/**
 * Specialized thermal imaging component providing GalleryEditMenuAdapter functionality for the IRCamera system.
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
class GalleryEditMenuAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: ((code: Int) -> Unit)? = null

    private var pointColor = false // Point
    private var pseudoColor = false // Pseudo color
    private var pseudoColorBar = false // Pseudo color条
    private var settingColorBar = false // Settings

    private val bean =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            /**
             * Executes iconbean operation with thermal imaging domain optimization.
             *
             */
            IconBean(
                name = context.getString(R.string.menu_3d_calibrate),
                icon = MenuR.drawable.selector_menu_first_2_5,
                code = 1000,
            ), // Calibration
            /**
             * Executes iconbean operation with thermal imaging domain optimization.
             *
             */
            IconBean(
                name = context.getString(R.string.thermal_false_color),
                icon = MenuR.drawable.selector_menu_first_4_3,
                code = 2000,
            ), // Pseudo color
            /**
             * Executes iconbean operation with thermal imaging domain optimization.
             *
             */
            IconBean(name = context.getString(R.string.app_setting), icon = MenuR.drawable.selector_menu_first_5_6, code = 4000), // Settings
            /**
             * Executes iconbean operation with thermal imaging domain optimization.
             *
             */
            IconBean(
                name = context.getString(R.string.func_temper_ruler),
                icon = MenuR.drawable.selector_menu_first_edit_4,
                code = 3000,
            ), // 等温尺
        )

    /**
     * Executes enPointColor functionality.
     */
    /**
     * Executes enpointcolor operation with thermal imaging domain optimization.
     *
     * @param
     * @param pointColor Parameter for operation (type: Boolean)
     *
     */
    fun enPointColor(pointColor: Boolean) {
        this.pointColor = pointColor
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Handles pseudo color configuration for thermal imaging.
     */
    fun enPseudoColor(pseudoColor: Boolean) {
        this.pseudoColor = pseudoColor
        notifyDataSetChanged()
    }

    /**
     * Handles pseudo color configuration for thermal imaging.
     */
    fun enPseudoColorBar(pseudoColorBar: Boolean) {
        this.pseudoColorBar = pseudoColorBar
        notifyDataSetChanged()
    }

    /**
     * Executes enSettingColorBar functionality.
     */
    /**
     * Executes ensettingcolorbar operation with thermal imaging domain optimization.
     *
     * @param
     * @param settingColorBar Parameter for operation (type: Boolean)
     *
     */
    fun enSettingColorBar(settingColorBar: Boolean) {
        this.settingColorBar = settingColorBar
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
        return ItemView(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_edit_menu, parent, false))
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return bean.size
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
            val data = bean[position]
            holder.name.text = data.name
            holder.img.setImageResource(data.icon)
            holder.lay.setOnClickListener {
                listener?.invoke(data.code)
            }
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (data.code) {
                1000 -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(pointColor, holder.img, holder.name)
                }
                2000 -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(pseudoColor, holder.img, holder.name)
                }
                3000 -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(pseudoColorBar, holder.img, holder.name)
                }
                4000 -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(settingColorBar, holder.img, holder.name)
                }
            }
        }
    }

state变化
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
            nameText.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            nameText.setTextColor(ContextCompat.getColor(context, R.color.font_third_color))
        }
    }

    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lay: View
        var img: ImageView
        var name: TextView

        init {
            lay = itemView.findViewById(R.id.item_edit_menu_tab_lay)
            img = itemView.findViewById(R.id.item_edit_menu_tab_img)
            name = itemView.findViewById(R.id.item_edit_menu_tab_text)
        }
    }

/**
 * Custom Icon view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
data class IconBean(
        val name: String,
        @DrawableRes val icon: Int,
        val code: Int,
    )
}
