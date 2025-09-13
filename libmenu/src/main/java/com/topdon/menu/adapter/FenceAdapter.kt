package com.topdon.menu.adapter

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.topdon.lib.core.R
import com.topdon.menu.R as MenuR
import com.topdon.menu.constant.FenceType
import com.topdon.menu.constant.MenuType

/**
 * point、line、area、全图、趋势图(可选)、delete menu Adapter.
 *
 * - 单光：   point、line、area、全图、趋势图、delete
 * - dual light：   point、line、area、全图、趋势图、delete
 * - Lite：  point、line、area、全图、趋势图、delete
 * - TC007： point、line、area、全图、趋势图、delete
 * - 2D 编辑：point、line、area、全图、delete
 *
 * point、line、area、趋势图、全图 与 delete互斥
 *
 * point、line、area、趋势图 互斥，全图独立可选
 *
 * Created by LCG on 2024/11/18.
 */
@SuppressLint("NotifyDataSetChanged")
internal class FenceAdapter(menuType: MenuType) : BaseMenuAdapter() {
    /**
     * currentselected的menutype，if null indicates所有都未selected.
     */
    var selectType: FenceType? = null
        set(value) {
            when (value) {
                FenceType.FULL -> isFullSelect = true
                FenceType.DEL -> isFullSelect = false
                else -> { // point、line、area、趋势图，不会影响全图state
                }
            }
            field = value
            notifyDataSetChanged()
        }

    /**
     * 全图是否已selected.
     */
    private var isFullSelect: Boolean = false

    /**
     * menuclickevent listener，目前都是single selection，等后续有空重构了，再搞成 IOS 那样“全图”可以多选。
     */
    var onFenceListener: ((fenceType: FenceType, isSelected: Boolean) -> Unit)? = null

    private val dataList: ArrayList<Data> = ArrayList(6)

    init {
        dataList.add(Data(R.string.thermal_point, MenuR.drawable.selector_menu2_fence_point, FenceType.POINT))
        dataList.add(Data(R.string.thermal_line, MenuR.drawable.selector_menu2_fence_line, FenceType.LINE))
        dataList.add(Data(R.string.thermal_rect, MenuR.drawable.selector_menu2_fence_rect, FenceType.RECT))
        dataList.add(Data(R.string.thermal_full_rect, MenuR.drawable.selector_menu2_fence_full, FenceType.FULL))
        if (menuType != MenuType.GALLERY_EDIT) { 
            dataList.add(Data(R.string.thermal_trend, MenuR.drawable.selector_menu2_fence_trend, FenceType.TREND))
        }
        dataList.add(Data(R.string.thermal_delete, MenuR.drawable.selector_menu2_del, FenceType.DEL))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val data: Data = dataList[position]
        holder.binding.ivIcon.setImageResource(data.drawableId)
        holder.binding.tvText.setText(data.stringId)
        holder.binding.ivIcon.isSelected = if (data.fenceType == FenceType.FULL) isFullSelect else data.fenceType == selectType
        holder.binding.tvText.isSelected = if (data.fenceType == FenceType.FULL) isFullSelect else data.fenceType == selectType
        holder.binding.clRoot.setOnClickListener {
            if (data.fenceType == FenceType.FULL) {
                isFullSelect = !isFullSelect
                onFenceListener?.invoke(data.fenceType, isFullSelect)
                if (selectType == FenceType.DEL) {
                    selectType = FenceType.FULL
                    notifyDataSetChanged()
                } else {
                    holder.binding.ivIcon.isSelected = isFullSelect
                    holder.binding.tvText.isSelected = isFullSelect
                }
            } else {
                if (data.fenceType != selectType) {
                    selectType = data.fenceType
                    onFenceListener?.invoke(data.fenceType, true)
                }
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

/**
 * Custom Data view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
    data class Data(
        @StringRes val stringId: Int,
        @DrawableRes val drawableId: Int,
        val fenceType: FenceType,
    )
}
