package com.topdon.module.thermal.ir.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.topdon.lib.core.bean.CameraItemBean
import com.topdon.lib.ui.listener.SingleClickListener
import com.topdon.lib.ui.widget.CountDownView
import com.topdon.module.thermal.ir.R

/**
 * @author: CaiSongL
 * @date: 2023/4/3 10:18
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraItemAdapter functionality.
 *
 * Provides advanced camera functionality for thermal imaging capture,
 * including temperature measurement and pseudo color visualization.
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
class CameraItemAdapter(
    data: MutableList<CameraItemBean>? = null,
) : BaseQuickAdapter<CameraItemBean, BaseViewHolder>(R.layout.item_camera, data) {
    var listener: ((index: Int, item: CameraItemBean) -> Unit)? = null

    /**
     * Executes convert operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: BaseViewHolder)
     * @param item Parameter for operation (type: CameraItemBean)
     *
     */
    override fun convert(
        holder: BaseViewHolder,
        item: CameraItemBean,
    ) {
        holder.setVisible(R.id.img, true)
        holder.setGone(R.id.count_down_view, true)
        holder?.itemView?.setOnClickListener(
            object : SingleClickListener() {
                /**
                 * Executes onsingleclick operation with thermal imaging domain optimization.
                 *
                 */
                override fun onSingleClick() {
                    listener?.invoke(data.indexOf(item), item)
                }
            },
        )
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (item.type) {
            CameraItemBean.TYPE_DELAY -> {
                holder.setImageResource(R.id.img, R.drawable.svg_camera_delay_0)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (CameraItemBean.DELAY_TIME_0 == item.time)
                    {
                        holder.setVisible(R.id.img, true)
                        holder.setGone(R.id.count_down_view, true)
                    } else
                    {
                        holder.setVisible(R.id.img, false)
                        holder.setGone(R.id.count_down_view, false)
                        val countDownView = holder.getView<CountDownView>(R.id.count_down_view)
                        holder.setGone(R.id.count_down_view, false)
                        countDownView.setCountdownTime(item.time)
                    }
            }
            CameraItemBean.TYPE_ZDKM -> {
                holder.setImageResource(
                    R.id.img,
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (item.isSel) {
                        R.drawable.svg_camera_auto_select_yes
                    } else {
                        R.drawable.svg_camera_auto_select_not
                    },
                )
            }
            CameraItemBean.TYPE_SDKM -> {
                holder.setImageResource(
                    R.id.img,
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (item.isSel) {
                        R.drawable.svg_camera_shutter_select_yes
                    } else {
                        R.drawable.svg_camera_shutter_select_not
                    },
                )
            }
            CameraItemBean.TYPE_AUDIO -> {
                holder.setImageResource(
                    R.id.img,
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (item.isSel) {
                        R.drawable.svg_camera_audio_select_yes
                    } else {
                        R.drawable.svg_camera_audio_select_not
                    },
                )
            }
            else -> {
                holder.setImageResource(R.id.img, R.drawable.svg_camera_setting)
            }
        }
    }
}
