package com.topdon.module.thermal.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.tools.GlideLoader
import com.topdon.module.thermal.R

/**
 * Specialized thermal imaging component providing GalleryAdapter functionality for the IRCamera system.
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
class GalleryAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: OnItemClickListener? = null

    var datas = arrayListOf<String>()
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
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
            GlideLoader.load(holder.img, datas[position])
            holder.lay.setOnClickListener {
                Log.w("123", "file: ${datas[position]}")
                listener?.onClick(position, datas[position])
            }
            holder.lay.setOnLongClickListener(
                View.OnLongClickListener {
                    Log.w("123", "file: ${datas[position]}")
                    listener?.onLongClick(position, datas[position])
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
         * @param path Parameter for operation (type: String)
         *
         */
        fun onClick(
            index: Int,
            path: String,
        )

    /**
     * Executes onLongClick functionality.
     */
        /**
         * Executes onlongclick operation with thermal imaging domain optimization.
         *
         * @param
         * @param index Parameter for operation (type: Int)
         * @param path Parameter for operation (type: String)
         *
         */
        fun onLongClick(
            index: Int,
            path: String,
        )
    }
}
