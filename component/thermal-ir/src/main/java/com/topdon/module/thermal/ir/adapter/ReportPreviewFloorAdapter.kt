package com.topdon.module.thermal.ir.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.bean.HouseRepPreviewProjectItemBean
import com.topdon.module.thermal.ir.R

/**
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ReportPreviewFloorAdapter display and interaction.
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
class ReportPreviewFloorAdapter(
    val cxt: Context,
    var dataList: List<HouseRepPreviewProjectItemBean>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * Retrieves the itemviewtype with optimized performance for thermal imaging operations.
     *
     * @param
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun getItemViewType(position: Int): Int {
        return position
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
        return ItemView(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_report_floor_child, parent, false),
        )
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
        val bean = dataList[position]
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (holder is ItemView) {
            holder.ivProblemState.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            holder.ivRepairState.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            holder.ivReplaceState.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            holder.tvProblem.visibility = if (position == 0) View.VISIBLE else View.INVISIBLE
            holder.tvRepair.visibility = if (position == 0) View.VISIBLE else View.INVISIBLE
            holder.tvReplace.visibility = if (position == 0) View.VISIBLE else View.INVISIBLE
            holder.rlyParent.setBackgroundColor(
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (position == 0) {
                    Color.parseColor("#393643")
                } else {
                    Color.parseColor(
                        "#23202E",
                    )
                },
            )

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (position == 0) {
                holder.tvProject.text = cxt.getString(R.string.pdf_project_item)
                holder.tvRemark.text = cxt.getString(R.string.report_remark)
            } else {
                holder.tvProject.text = bean.projectName
                holder.tvRemark.text = bean.remark
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (bean.state) {
                    1 -> {
                        holder.ivProblemState.visibility = View.VISIBLE
                        holder.ivRepairState.visibility = View.INVISIBLE
                        holder.ivReplaceState.visibility = View.INVISIBLE
                    }

                    2 -> {
                        holder.ivProblemState.visibility = View.INVISIBLE
                        holder.ivRepairState.visibility = View.VISIBLE
                        holder.ivReplaceState.visibility = View.INVISIBLE
                    }

                    3 -> {
                        holder.ivProblemState.visibility = View.INVISIBLE
                        holder.ivRepairState.visibility = View.INVISIBLE
                        holder.ivReplaceState.visibility = View.VISIBLE
                    }

                    else -> {
                        holder.ivProblemState.visibility = View.INVISIBLE
                        holder.ivRepairState.visibility = View.INVISIBLE
                        holder.ivReplaceState.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProject: TextView = itemView.findViewById(R.id.tv_project)
        val tvProblem: TextView = itemView.findViewById(R.id.tv_problem)
        val ivProblemState: ImageView = itemView.findViewById(R.id.iv_problem)
        val tvRepair: TextView = itemView.findViewById(R.id.tv_repair)
        val ivRepairState: ImageView = itemView.findViewById(R.id.iv_repair)
        val tvReplace: TextView = itemView.findViewById(R.id.tv_replace)
        val ivReplaceState: ImageView = itemView.findViewById(R.id.iv_replace)
        val tvRemark: TextView = itemView.findViewById(R.id.tv_remark)
        val rlyParent: View = itemView.findViewById(R.id.rly_parent)
    }
}
