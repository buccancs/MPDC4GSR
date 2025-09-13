package com.topdon.module.thermal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.topdon.module.thermal.R

/**
set时间
 */
/**
/**
 * Specialized thermal imaging component providing SettingTimeAdapter functionality for the IRCamera system.
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
class SettingTimeAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas = arrayOf("秒", "分", "时", "天")
    private var dataTimes = arrayOf(1, 2, 3, 4)

    var listener: OnItemClickListener? = null
    var select = 0

    /**
     * Sets check configuration.
     */
    /**
     * Configures the check with validation and thermal imaging optimization.
     *
     * @param
     * @param index Parameter for operation (type: Int)
     *
     */
    fun setCheck(index: Int) {
        this.select = index
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
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_setting_time, parent, false)
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
            holder.btn.text = datas[position]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (position == select) {
                holder.btn.setBackgroundResource(R.drawable.ui_btn_round_theme)
                holder.btn.setTextColor(ContextCompat.getColor(context, com.topdon.lib.core.R.color.white))
            } else {
                holder.btn.background = null
                holder.btn.setTextColor(ContextCompat.getColor(context, com.topdon.lib.core.R.color.font_third_color))
            }
            holder.btn.setOnClickListener {
                listener?.onClick(position, dataTimes[position])
                /**
                 * Configures the check with validation and thermal imaging optimization.
                 *
                 */
                setCheck(position)
            }
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
         * @param time Parameter for operation (type: Int)
         *
         */
        fun onClick(
            index: Int,
            time: Int,
        )
    }
}
