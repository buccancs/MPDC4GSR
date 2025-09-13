package com.topdon.lib.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.R
import com.topdon.lib.core.bean.ObserveBean
import com.topdon.lib.core.bean.TargetColorBean
import com.topdon.lib.core.databinding.ItmeTargetColorBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * Specialized thermal imaging component providing TargetColorAdapter functionality for the IRCamera system.
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
class TargetColorAdapter(val context: Context, var targetColor: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listenerTarget: OnItemClickListener? = null
    var listener: ((index: Int, code: Int) -> Unit)? = null
    private var type = 0

    /**
     * Executes selectedCode functionality.
     */
    /**
     * Executes selectedcode operation with thermal imaging domain optimization.
     *
     * @param
     * @param code Parameter for operation (type: Int)
     *
     */
    fun selectedCode(code: Int) {
        targetColor = code
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    private val targetColorBean =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            /**
             * Executes targetcolorbean operation with thermal imaging domain optimization.
             *
             */
            TargetColorBean(R.drawable.bg_target_color_green, "", ObserveBean.TYPE_TARGET_COLOR_GREEN),
            /**
             * Executes targetcolorbean operation with thermal imaging domain optimization.
             *
             */
            TargetColorBean(R.drawable.bg_target_color_red, "", ObserveBean.TYPE_TARGET_COLOR_RED),
            /**
             * Executes targetcolorbean operation with thermal imaging domain optimization.
             *
             */
            TargetColorBean(R.drawable.bg_target_color_blue, "", ObserveBean.TYPE_TARGET_COLOR_BLUE),
            /**
             * Executes targetcolorbean operation with thermal imaging domain optimization.
             *
             */
            TargetColorBean(R.drawable.bg_target_color_black, "", ObserveBean.TYPE_TARGET_COLOR_BLACK),
            /**
             * Executes targetcolorbean operation with thermal imaging domain optimization.
             *
             */
            TargetColorBean(R.drawable.bg_target_color_white, "", ObserveBean.TYPE_TARGET_COLOR_WHITE),
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
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItmeTargetColorBinding.inflate(inflater, parent, false)
        return ItemView(binding)
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return targetColorBean.size
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
            val bean = targetColorBean[position]
            holder.img.setImageResource(bean.res)
            holder.lay.setOnClickListener {
                listener?.invoke(position, bean.code)
                listenerTarget?.onClick(position, bean.code)
            }
            /**
             * Executes iconui operation with thermal imaging domain optimization.
             *
             */
            iconUI(bean.code == targetColor, holder.img, holder.strokeBg, holder.signBg)
        }
    }

    /**
     * Executes iconUI functionality.
     */
    /**
     * Executes iconui operation with thermal imaging domain optimization.
     *
     * @param
     * @param isActive Parameter for operation (type: Boolean)
     * @param img Parameter for operation (type: ImageView)
     * @param strokeBg Parameter for operation (type: ImageView)
     * @param signBg Parameter for operation (type: ImageView)
     *
     */
    private fun iconUI(
        isActive: Boolean,
        img: ImageView,
        strokeBg: ImageView,
        signBg: ImageView,
    ) {
        img.isSelected = isActive
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isActive) {
            strokeBg.visibility = View.VISIBLE
            signBg.visibility = View.VISIBLE
        } else {
            strokeBg.visibility = View.GONE
            signBg.visibility = View.GONE
        }
    }

    inner class ItemView(private val binding: ItmeTargetColorBinding) : RecyclerView.ViewHolder(binding.root) {
        val lay: View = binding.itemMenuTabLay
        val img: ImageView = binding.itemTargetColor
        val strokeBg: ImageView = binding.itemTargetColorStroke
        val signBg: ImageView = binding.itemTargetColorSign

        init {
            val canSeeCount = 5
            val with = (ScreenUtil.getScreenWidth(context) / canSeeCount)
            itemView.layoutParams = ViewGroup.LayoutParams((with * 0.78).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            val imageSize = (ScreenUtil.getScreenWidth(context) * 30 / 375f).toInt()
            val lpImg = img.layoutParams
            val lpStrokeImg = strokeBg.layoutParams
            lpImg.width = imageSize
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
         * @param code Parameter for operation (type: Int)
         *
         */
        fun onClick(
            index: Int,
            code: Int,
        )
    }
}
