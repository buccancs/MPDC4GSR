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
import com.topdon.lib.core.bean.CameraItemBean
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.tools.DeviceTools
import com.topdon.lib.ui.bean.TemperatureBean
import com.topdon.lib.ui.R as UiR
import com.topdon.menu.R as MenuR

/**
 * Custom Menu five night view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
@Deprecated("旧的temperature levelmenu，已重构过了")
/**
 * MenuFiveNightAdapter provides data binding between data source and UI components.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing MenuFiveNightAdapter functionality for the IRCamera system.
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
class MenuFiveNightAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onTempLevelListener: ((index: Int) -> Unit)? = null

    private var selectedCode = SaveSettingUtil.temperatureMode

    /**
     * Executes selected functionality.
     */
    /**
     * Executes selected operation with thermal imaging domain optimization.
     *
     * @param
     * @param code Parameter for operation (type: Int)
     *
     */
    fun selected(code: Int) {
        selectedCode = code
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    private val fiveBean =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            /**
             * Handles temperature measurement and calibration with precision thermal data processing.
             *
             * @note Temperature values are in Celsius unless otherwise specified.
             * Accuracy depends on thermal camera calibration.
             *
             */
            TemperatureBean(
                MenuR.drawable.selector_menu2_temp_level_1,
                context.getString(R.string.thermal_normal_temperature),
                /**
                 * Retrieves the tempstr with optimized performance for thermal imaging operations.
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                getTempStr(-20, 150),
                CameraItemBean.TYPE_TMP_C,
            ),
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (DeviceTools.isTC001LiteConnect()) {
                /**
                 * Handles temperature measurement and calibration with precision thermal data processing.
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                TemperatureBean(
                    MenuR.drawable.selector_menu2_temp_level_1,
                    context.getString(R.string.thermal_high_temperature),
                    /**
                     * Retrieves the tempstr with optimized performance for thermal imaging operations.
                     *
                     * @note Temperature values are in Celsius unless otherwise specified.
                     * Accuracy depends on thermal camera calibration.
                     *
                     */
                    getTempStr(150, 450),
                    CameraItemBean.TYPE_TMP_H,
                )
            } else {
                /**
                 * Handles temperature measurement and calibration with precision thermal data processing.
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                TemperatureBean(
                    MenuR.drawable.selector_menu2_temp_level_1,
                    context.getString(R.string.thermal_high_temperature),
                    /**
                     * Retrieves the tempstr with optimized performance for thermal imaging operations.
                     *
                     * @note Temperature values are in Celsius unless otherwise specified.
                     * Accuracy depends on thermal camera calibration.
                     *
                     */
                    getTempStr(150, 550),
                    CameraItemBean.TYPE_TMP_H,
                )
            },
            /**
             * Handles temperature measurement and calibration with precision thermal data processing.
             *
             * @note Temperature values are in Celsius unless otherwise specified.
             * Accuracy depends on thermal camera calibration.
             *
             */
            TemperatureBean(
                MenuR.drawable.selector_menu2_temp_level_2,
                context.getString(R.string.thermal_automatic),
                "",
                CameraItemBean.TYPE_TMP_ZD,
            ),
        )

    /**
     * Retrieves tempstr information.
     */
    private fun getTempStr(
        min: Int,
        max: Int,
    ): String =
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (SharedManager.getTemperature() == 1) {
            "${min}\n~\n$max°C"
        } else {
            "${(min * 1.8 + 32).toInt()}\n~\n${(max * 1.8 + 32).toInt()}°F"
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
        val view = LayoutInflater.from(parent.context).inflate(UiR.layout.ui_item_menu_five_view, parent, false)
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
            holder.img.setImageResource(fiveBean[position].res)
            holder.lay.setOnClickListener {
                onTempLevelListener?.invoke(fiveBean[position].code)
                /**
                 * Executes selected operation with thermal imaging domain optimization.
                 *
                 */
                selected(fiveBean[position].code)
            }
            holder.img.isSelected = fiveBean[position].code == selectedCode
            holder.name.text = fiveBean[position].name
            holder.info.text = fiveBean[position].info
            holder.name.isSelected = fiveBean[position].code == selectedCode
            holder.info.isSelected = fiveBean[position].code == selectedCode
            holder.name.setTextColor(
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (fiveBean[position].code == selectedCode) {
                    ContextCompat.getColor(context, UiR.color.white)
                } else {
                    ContextCompat.getColor(context, UiR.color.font_third_color)
                },
            )
            holder.info.setTextColor(
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (fiveBean[position].code == selectedCode) {
                    ContextCompat.getColor(context, UiR.color.color_FFBA42)
                } else {
                    ContextCompat.getColor(context, UiR.color.font_third_color)
                },
            )
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return fiveBean.size
    }

    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Init {
// Val canSeeCount = itemCount.toFloat() // 一屏Visible的 item quantity，目前都是全都Show/Display完
// Val with = (ScreenUtils.getScreenWidth() / canSeeCount).toInt()
// ItemView.layoutParams = ViewGroup.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT)
// Val imageSize = (ScreenUtils.getScreenWidth() * 62 / 375f).toInt()
// Val layoutParams = itemView.item_menu_tab_fl.layoutParams
// LayoutParams.width = imageSize
// LayoutParams.height = imageSize
// ItemView.item_menu_tab_fl.layoutParams = layoutParams
//        }
        val lay: View = itemView.findViewById(UiR.id.item_menu_tab_lay)
        val img: ImageView = itemView.findViewById(UiR.id.item_menu_tab_img)
        val name: TextView = itemView.findViewById(UiR.id.item_menu_tab_text)
        val info: TextView = itemView.findViewById(UiR.id.item_menu_tab_info_text)
    }
}
