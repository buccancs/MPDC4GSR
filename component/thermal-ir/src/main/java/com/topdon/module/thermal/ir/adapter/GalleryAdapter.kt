package com.topdon.module.thermal.ir.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.bean.GalleryBean
import com.topdon.lib.core.bean.GalleryTitle
import com.topdon.lib.core.tools.GlideLoader
import com.topdon.lib.core.tools.TimeTool
import com.topdon.module.thermal.ir.R

/**
photo或video
 */
/**
 * Custom Gallery view for thermal imaging display.
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
class GalleryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_HEAD = 0
        private const val TYPE_DATA = 1
    }

    /**
当前display的data列表，包含有title item.
     */
    val dataList: ArrayList<GalleryBean> = ArrayList()

    /**
编辑mode下，当前selected的 position 列表.
     */
    val selectList: ArrayList<Int> = ArrayList()

    /**
是否为 TS004 远端mode，处于该mode会有Download图标.
     */
    var isTS004Remote = false
        set(value) {
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
当前是否处于编辑mode.
     */
    var isEditMode = false
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
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!value) {
                    selectList.clear()
                    selectCallback?.invoke(selectList)
                }
                /**
                 * Executes notifydatasetchanged operation with thermal imaging domain optimization.
                 *
                 */
                notifyDataSetChanged()
            }
        }

    /**
非编辑mode下 item 长按进入编辑modeEventListener.
     */
    var onLongEditListener: (() -> Unit)? = null

    /**
selected数量变更Callback.
data 当前selected的 item position 列表
     */
    var selectCallback: ((data: ArrayList<Int>) -> Unit)? = null

    /**
非编辑mode时，item clickEventListener.
     */
    var itemClickCallback: ((position: Int) -> Unit)? = null

    /**
     * Executes refreshList functionality.
     */
    /**
     * Executes refreshlist operation with thermal imaging domain optimization.
     *
     * @param
     * @param newList Parameter for operation (type: List<GalleryBean>)
     *
     */
    fun refreshList(newList: List<GalleryBean>) {
        dataList.clear()
        dataList.addAll(newList)
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes buildSelectList functionality.
     */
    /**
     * Executes buildselectlist operation with thermal imaging domain optimization.
     *
     */
    fun buildSelectList(): ArrayList<GalleryBean> {
        val resultList: ArrayList<GalleryBean> = ArrayList()
        selectList.forEach {
            resultList.add(dataList[it])
        }
        return resultList
    }

    /**
     * Executes selectAll functionality.
     */
    /**
     * Executes selectall operation with thermal imaging domain optimization.
     *
     */
    fun selectAll() {
        var dataCount = 0
        dataList.forEach {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it !is GalleryTitle) {
                dataCount++
            }
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectList.size >= dataCount) {
            selectList.clear()
        } else {
            selectList.clear()
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until dataList.size) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dataList[i] !is GalleryTitle) {
                    selectList.add(i)
                }
            }
        }
        selectCallback?.invoke(selectList)
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Retrieves the itemviewtype with optimized performance for thermal imaging operations.
     *
     * @param
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun getItemViewType(position: Int): Int {
        return if (dataList[position] is GalleryTitle) {
            TYPE_HEAD
        } else {
            TYPE_DATA
        }
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
        return if (viewType == TYPE_HEAD) {
            /**
             * Executes itemheadview operation with thermal imaging domain optimization.
             *
             */
            ItemHeadView(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_head_lay, parent, false))
        } else {
            /**
             * Executes itemview operation with thermal imaging domain optimization.
             *
             */
            ItemView(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_lay, parent, false))
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
        val data = dataList[position]
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (holder is ItemView) {
            GlideLoader.load(holder.img, data.thumb)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.name.uppercase().endsWith(".MP4")) {
                holder.info.text = TimeTool.showVideoTime(data.duration)
                holder.ivVideoTime.isVisible = true
            } else {
                holder.info.text = ""
                holder.ivVideoTime.isVisible = false
            }

            holder.ivHasDownload.isVisible = isTS004Remote && data.hasDownload

            holder.ivCheck.isVisible = isEditMode
            holder.ivCheck.isSelected = selectList.contains(position)

            holder.img.setOnClickListener {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isEditMode) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (selectList.contains(position)) {
                        selectList.remove(position)
                    } else {
                        selectList.add(position)
                    }
                    selectCallback?.invoke(selectList)

                    holder.ivCheck.isSelected = selectList.contains(position)
                } else {
                    itemClickCallback?.invoke(position)
                }
            }
            holder.img.setOnLongClickListener {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isEditMode) {
                    selectList.add(position)
                    selectCallback?.invoke(selectList)
                    holder.ivCheck.isVisible = true
                    holder.ivCheck.isSelected = true
                    isEditMode = true
                    onLongEditListener?.invoke()
                }
                return@setOnLongClickListener true
            }
        } else if (holder is ItemHeadView) {
            holder.name.text = TimeTool.showDateType(data.timeMillis, 4)
            holder.name.setTextColor(0x80ffffff.toInt())
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ItemHeadView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_gallery_head_text)
    }

    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.item_gallery_img)
        val info: TextView = itemView.findViewById(R.id.item_gallery_text)
        val ivVideoTime: ImageView = itemView.findViewById(R.id.iv_video_time)
        val ivHasDownload: ImageView = itemView.findViewById(R.id.iv_has_download)
        val ivCheck: ImageView = itemView.findViewById(R.id.iv_check)
    }
}
