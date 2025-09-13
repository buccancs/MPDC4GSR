package com.topdon.lib.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.R
import com.topdon.lib.core.bean.ObserveBean
import com.topdon.lib.ui.bean.ColorBean
import com.topdon.lib.ui.config.CameraHelp
import com.topdon.menu.constant.TargetType
import com.topdon.lib.ui.R as UiR
import com.topdon.menu.R as MenuR

/**
 * Custom Menu target view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
@Deprecated("旧的targetmenu，已重构过了")
/**
 * MenuTargetAdapter provides data binding between data source and UI components.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing MenuTargetAdapter functionality for the IRCamera system.
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
class MenuTargetAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: ((code: Int) -> Unit)? = null

    /**
     * settingsspecified option的selectedstate
     */
    fun setSelected(
        targetType: TargetType,
        isSelected: Boolean,
    ) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (targetType) {
            TargetType.MODE -> secondBean[0].isSelect = isSelected
            TargetType.STYLE -> secondBean[1].isSelect = isSelected
            TargetType.COLOR -> secondBean[2].isSelect = isSelected
            TargetType.DELETE -> secondBean[3].isSelect = isSelected
            TargetType.HELP -> secondBean[4].isSelect = isSelected
        }
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    private val secondBean =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_target_1_person,
                context.getString(R.string.main_tab_second_measure_mode),
                CameraHelp.TYPE_SET_MEASURE_MODE,
            ),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_target_2_style,
                context.getString(R.string.main_tab_first_target),
                CameraHelp.TYPE_SET_TARGET_MODE,
            ),
//      ColorBean(UiR.drawable.ic_menu_second_zoom, context.getString(R.string.main_tab_second_zoom), CameraHelp.TYPE_SET_TARGET_ZOOM),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_target_3_color,
                context.getString(R.string.main_tab_second_target_color),
                CameraHelp.TYPE_SET_TARGET_COLOR,
            ),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_del, context.getString(R.string.thermal_delete), CameraHelp.TYPE_SET_TARGET_DELETE),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_target_4_help,
                context.getString(R.string.main_tab_second_target_help),
                CameraHelp.TYPE_SET_TARGET_HELP,
            ),
        )

    /**
     * refreshmeasurement mode图标
     */
    /**
     * Executes upcurrentmeasuremode operation with thermal imaging domain optimization.
     *
     * @param
     * @param measureMode Parameter for operation (type: Int)
     *
     */
    fun upCurrentMeasureMode(measureMode: Int) {
        secondBean.clear()
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (measureMode) {
            ObserveBean.TYPE_MEASURE_PERSON -> {
                secondBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_target_1_person,
                        context.getString(R.string.main_tab_second_measure_mode),
                        CameraHelp.TYPE_SET_MEASURE_MODE,
                    ),
                )
            }
            ObserveBean.TYPE_MEASURE_SHEEP -> {
                secondBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_target_1_sheep,
                        context.getString(R.string.main_tab_second_measure_mode),
                        CameraHelp.TYPE_SET_MEASURE_MODE,
                    ),
                )
            }
            ObserveBean.TYPE_MEASURE_DOG -> {
                secondBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_target_1_dog,
                        context.getString(R.string.main_tab_second_measure_mode),
                        CameraHelp.TYPE_SET_MEASURE_MODE,
                    ),
                )
            }
            ObserveBean.TYPE_MEASURE_BIRD -> {
                secondBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_target_1_bird,
                        context.getString(R.string.main_tab_second_measure_mode),
                        CameraHelp.TYPE_SET_MEASURE_MODE,
                    ),
                )
            }
        }
        secondBean.add(
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_target_2_style,
                context.getString(R.string.main_tab_first_target),
                CameraHelp.TYPE_SET_TARGET_MODE,
            ),
        )
        secondBean.add(
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_target_3_color,
                context.getString(R.string.main_tab_second_target_color),
                CameraHelp.TYPE_SET_TARGET_COLOR,
            ),
        )
        secondBean.add(
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_del, context.getString(R.string.thermal_delete), CameraHelp.TYPE_SET_TARGET_DELETE),
        )
        secondBean.add(
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_target_4_help,
                context.getString(R.string.main_tab_second_target_help),
                CameraHelp.TYPE_SET_TARGET_HELP,
            ),
        )
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
        val view = LayoutInflater.from(parent.context).inflate(UiR.layout.ui_item_menu_second_view, parent, false)
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
            val bean = secondBean[position]
            holder.name.text = bean.name
            holder.img.setImageResource(bean.res)

            holder.img.isSelected = bean.isSelect
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (bean.isSelect) {
                holder.name.setTextColor(ContextCompat.getColor(context, UiR.color.white))
            } else {
                holder.name.setTextColor(ContextCompat.getColor(context, UiR.color.font_third_color))
            }

            holder.lay.setOnClickListener {
                listener?.invoke(bean.code)
                /**
                 * Executes notifydatasetchanged operation with thermal imaging domain optimization.
                 *
                 */
                notifyDataSetChanged()
            }
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return secondBean.size
    }

    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lay: View = itemView.findViewById(UiR.id.item_menu_tab_lay)
        val img: ImageView = itemView.findViewById(UiR.id.item_menu_tab_img)
        val name: TextView = itemView.findViewById(UiR.id.item_menu_tab_text)

        init {
// Val canSeeCount = 4.5 
// Val with = (ScreenUtils.getScreenWidth() / canSeeCount).toInt()
            itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
// Val imageSize = (ScreenUtils.getScreenWidth() * 62 / 375f).toInt()
// Val layoutParams = itemView.item_menu_tab_img.layoutParams
// LayoutParams.width = imageSize
// LayoutParams.height = imageSize
// ItemView.item_menu_tab_img.layoutParams = layoutParams
        }
    }
}
