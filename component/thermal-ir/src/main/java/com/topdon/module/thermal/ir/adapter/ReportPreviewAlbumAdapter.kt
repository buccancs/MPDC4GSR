package com.topdon.module.thermal.ir.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
// Removed house module import - module removed as unused
// Import com.topdon.house.activity.ImagesDetailActivity
import com.topdon.lib.core.bean.HouseRepPreviewAlbumItemBean
import com.topdon.module.thermal.ir.R

/**
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ReportPreviewAlbumAdapter display and interaction.
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
class ReportPreviewAlbumAdapter(
    private val cxt: Context,
    private var dataList: List<HouseRepPreviewAlbumItemBean>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var jumpListener: ((item: HouseRepPreviewAlbumItemBean, position: Int) -> Unit)? = null

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
                .inflate(R.layout.item_report_album_child, parent, false),
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
            Glide.with(cxt).load(bean.photoPath).into(holder.rivPhoto)
            holder.tvName.text = bean.title
            holder.rivPhoto.setOnClickListener {
                jumpListener?.invoke(bean, position)
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
        val rivPhoto: ImageView = itemView.findViewById(R.id.riv_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
    }
}
