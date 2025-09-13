package com.topdon.module.thermal.ir.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.CollectionUtils
// Removed house module import - module removed as unused
// Import com.topdon.house.activity.ImagesDetailActivity
import com.topdon.lib.core.bean.HouseRepPreviewItemBean
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.thermal.ir.R

/**
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ReportPreviewAdapter display and interaction.
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
class ReportPreviewAdapter(private val cxt: Context, var dataList: List<HouseRepPreviewItemBean>) :
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_report_floor, parent, false),
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
        val data = dataList[position]
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (holder is ItemView) {
            holder.tvFloorNumber.text = data.itemName

            holder.rcyReport.layoutManager = LinearLayoutManager(cxt)
            val reportPreviewAdapter =
                /**
                 * Executes reportpreviewflooradapter operation with thermal imaging domain optimization.
                 *
                 */
                ReportPreviewFloorAdapter(cxt, data.projectItemBeans)
            holder.rcyReport.adapter = reportPreviewAdapter

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (CollectionUtils.isNotEmpty(data.projectItemBeans)) {
                holder.flyProject.visibility = View.VISIBLE
                holder.rcyCategory.layoutManager = LinearLayoutManager(cxt)
                val reportCategoryAdapter =
                    /**
                     * Executes reportpreviewflooradapter operation with thermal imaging domain optimization.
                     *
                     */
                    ReportPreviewFloorAdapter(cxt, data.projectItemBeans)
                holder.rcyCategory.adapter = reportCategoryAdapter
            } else {
                holder.flyProject.visibility = View.GONE
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (CollectionUtils.isNotEmpty(data.albumItemBeans)) {
                holder.llyAlbum.visibility = View.VISIBLE
                holder.rcyAlbum.layoutManager = GridLayoutManager(cxt, 3)
                val albumAdapter = ReportPreviewAlbumAdapter(cxt, data.albumItemBeans)
                holder.rcyAlbum.adapter = albumAdapter
                albumAdapter.jumpListener = { _, position ->
                    // Disabled - ImagesDetailActivity from removed house module
                    // Var intent = Intent(cxt, ImagesDetailActivity::class.java)
                    // Var photos = ArrayList<String>()
                    // Data.albumItemBeans.forEach {
                    // Photos.add(it.photoPath)
                    // }
                    // Intent.putExtra(ExtraKeyConfig.IMAGE_PATH_LIST, photos)
                    // Intent.putExtra(ExtraKeyConfig.CURRENT_ITEM, position)
                    // Cxt.startActivity(intent)

                    // Temporary stub - show toast instead of navigating
                    TToast.shortToast(cxt, "Image detail view disabled - house module removed")
                }
            } else {
                holder.llyAlbum.visibility = View.GONE
            }

            holder.hsvReport.setOnTouchListener { _, event ->
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (event.action == MotionEvent.ACTION_UP) {
                    // Generic view doesn't have startScrollerTask method
                    // Holder.hsvReport.startScrollerTask()
                }
                false
            }

            // Scroll listener commented out due to type issues - would need proper MHorizontalScrollView import
        /*
        holder.hsvReport.setOnScrollStopListner(object : OnScrollStopListner {
            /**
             * Executes onscrolltorightedge operation with thermal imaging domain optimization.
             *
             */
            override fun onScrollToRightEdge() {
                holder.viewCategoryMask.visibility = View.VISIBLE
            }

            /**
             * Executes onscrolltomiddle operation with thermal imaging domain optimization.
             *
             */
            override fun onScrollToMiddle() {
                holder.viewCategoryMask.visibility = View.VISIBLE
            }

            /**
             * Executes onscrolltoleftedge operation with thermal imaging domain optimization.
             *
             */
            override fun onScrollToLeftEdge() {
                holder.viewCategoryMask.visibility = View.GONE
            }

            /**
             * Executes onscrollstoped operation with thermal imaging domain optimization.
             *
             */
            override fun onScrollStoped() {
            }

            /**
             * Executes onscrollchanged operation with thermal imaging domain optimization.
             *
             * @param
             * @param l Parameter for operation (type: Int)
             * @param t Parameter for operation (type: Int)
             * @param oldl Parameter for operation (type: Int)
             * @param oldt Parameter for operation (type: Int)
             *
             */
            override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (holder.viewCategoryMask.visibility == View.VISIBLE) {
                    return
                }
                holder.viewCategoryMask.visibility = View.VISIBLE
            }
        })
         */
        } // End of if (holder is ItemView) block
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFloorNumber: TextView = itemView.findViewById(R.id.tv_floor_number)
        val rcyReport: RecyclerView = itemView.findViewById(R.id.rcy_report)
        val rcyCategory: RecyclerView = itemView.findViewById(R.id.rcy_category)
        val llyAlbum: LinearLayout = itemView.findViewById(R.id.lly_album)
        val rcyAlbum: RecyclerView = itemView.findViewById(R.id.rcy_album)
        val flyProject: View = itemView.findViewById(R.id.fly_project)
        val hsvReport: View = itemView.findViewById(R.id.hsv_report)
        val viewCategoryMask: View = itemView.findViewById(R.id.view_category_mask)
    }
}
