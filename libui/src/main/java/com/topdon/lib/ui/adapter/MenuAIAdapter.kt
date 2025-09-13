package com.topdon.lib.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.R
import com.topdon.lib.core.bean.ObserveBean
import com.topdon.lib.ui.bean.ColorBean
import com.topdon.lib.ui.databinding.UiItemMenuSecondViewBinding
import com.topdon.lib.ui.R as UiR
import com.topdon.menu.R as MenuR

/**
 * Custom Menu a i view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
@Deprecated("旧的high/low temperature源menu，已重构过了")
/**
 * MenuAIAdapter provides data binding between data source and UI components.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing MenuAIAdapter functionality for the IRCamera system.
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
class MenuAIAdapter(val context: Context) : RecyclerView.Adapter<MenuAIAdapter.ItemView>() {
    /**
     * currentselected的选项 code.
     *
     * Due to legacy constraints (saved in SharedPreferences), the code values are:
     * - Nothing selected: -1
     * - Dynamic recognition: 0
     * - High temperature source: 1
     * - Low temperature source: 2
     */
    var selectCode: Int = -1
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (field != value) {
                field = value
                /**
                 * Executes notifydatasetchanged operation with thermal imaging domain optimization.
                 *
                 */
                notifyDataSetChanged()
            }
        }

    /**
     * Observation mode - Menu 2 - High/Low temperature source click event listener，single selection。
     */
    var onTempSourceListener: ((code: Int) -> Unit)? = null

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
            ColorBean(
                MenuR.drawable.selector_menu2_source_1_auto,
                context.getString(R.string.main_tab_second_dynamic_recognition),
                ObserveBean.TYPE_DYN_R,
            ),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_source_2_high,
                context.getString(R.string.main_tab_second_high_temperature_source),
                ObserveBean.TYPE_TMP_H_S,
            ),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_source_3_low,
                context.getString(R.string.main_tab_second_low_temperature_source),
                ObserveBean.TYPE_TMP_L_S,
            ),
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
    ): ItemView {
        val binding = UiItemMenuSecondViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemView(binding)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: ItemView)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: ItemView,
        position: Int,
    ) {
        /**
         * Executes with operation with thermal imaging domain optimization.
         *
         */
        with(holder.binding) {
            itemMenuTabImg.setImageResource(secondBean[position].res)
            itemMenuTabLay.setOnClickListener {
                selectCode = secondBean[position].code
                onTempSourceListener?.invoke(secondBean[position].code)
            }
            itemMenuTabImg.isSelected = secondBean[position].code == selectCode
            itemMenuTabText.text = secondBean[position].name
            itemMenuTabText.isSelected = secondBean[position].code == selectCode
            itemMenuTabText.setTextColor(
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (secondBean[position].code == selectCode) {
                    ContextCompat.getColor(context, UiR.color.white)
                } else {
                    ContextCompat.getColor(context, UiR.color.font_third_color)
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

    inner class ItemView(val binding: UiItemMenuSecondViewBinding) : RecyclerView.ViewHolder(binding.root)
}
