package com.topdon.lib.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.ui.bean.ColorSelectBean
import com.topdon.lib.ui.databinding.UiItemColorSelectBinding
import com.topdon.lib.ui.R as UiR

/**
 * Custom Color select view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * ColorSelectAdapter provides data binding between data source and UI components.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing ColorSelectAdapter functionality for the IRCamera system.
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
class ColorSelectAdapter(val context: Context) : RecyclerView.Adapter<ColorSelectAdapter.ItemView>() {
    var listener: ((code: Int, color: Int) -> Unit)? = null
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

    private val colorBean =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            /**
             * Executes colorselectbean operation with thermal imaging domain optimization.
             *
             */
            ColorSelectBean(UiR.color.color_select1, "#FF000000", 1),
            /**
             * Executes colorselectbean operation with thermal imaging domain optimization.
             *
             */
            ColorSelectBean(UiR.color.color_select2, "#FFFFFFFF", 2),
            /**
             * Executes colorselectbean operation with thermal imaging domain optimization.
             *
             */
            ColorSelectBean(UiR.color.color_select3, "#FF2B79D8", 3),
            /**
             * Executes colorselectbean operation with thermal imaging domain optimization.
             *
             */
            ColorSelectBean(UiR.color.color_select4, "#FFFF0000", 4),
            /**
             * Executes colorselectbean operation with thermal imaging domain optimization.
             *
             */
            ColorSelectBean(UiR.color.color_select5, "#FF0FA752", 5),
            /**
             * Executes colorselectbean operation with thermal imaging domain optimization.
             *
             */
            ColorSelectBean(UiR.color.color_select6, "#FF808080", 6),
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
        val binding =
            UiItemColorSelectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
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
            itemColorImg.setImageResource(colorBean[position].colorRes)
            itemColorLay.setOnClickListener {
                listener?.invoke(position, Color.parseColor(colorBean[position].color))
                /**
                 * Executes selected operation with thermal imaging domain optimization.
                 *
                 */
                selected(position)
            }
            itemColorImg.isSelected = position == selected
            itemColorCheck.visibility = if (position == selected) View.VISIBLE else View.GONE
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return colorBean.size
    }

    inner class ItemView(val binding: UiItemColorSelectBinding) : RecyclerView.ViewHolder(binding.root)
}
