package com.topdon.module.thermal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.lib.core.tools.TimeTool
import com.topdon.module.thermal.R

/**
 * Specialized thermal imaging component providing MonitorLogAdapter functionality for the IRCamera system.
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
class MonitorLogAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: OnItemClickListener? = null

    var datas = arrayListOf<ThermalEntity>()
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
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
            val data = datas[position]
            holder.indexText.text = "${position + 1}"
            holder.timeText.text = TimeTool.showTimeSecond(data.createTime)
            holder.lay.setOnClickListener {
                listener?.onClick(position, data.thermalId)
            }
            holder.lay.setOnLongClickListener(
                View.OnLongClickListener {
                    listener?.onLongClick(position, data.thermalId)
                    return@OnLongClickListener true
                },
            )
        }
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
         * @param thermalId Parameter for operation (type: String)
         *
         */
        fun onClick(
            index: Int,
            thermalId: String,
        )

    /**
     * Executes onLongClick functionality.
     */
        /**
         * Executes onlongclick operation with thermal imaging domain optimization.
         *
         * @param
         * @param index Parameter for operation (type: Int)
         * @param thermalId Parameter for operation (type: String)
         *
         */
        fun onLongClick(
            index: Int,
            thermalId: String,
        )
    }
}
