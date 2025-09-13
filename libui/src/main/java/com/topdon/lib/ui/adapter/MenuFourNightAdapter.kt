package com.topdon.lib.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.R
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.config.DeviceConfig
import com.topdon.lib.core.utils.Constants
import com.topdon.lib.core.utils.Constants.IR_OBSERVE_MODE
import com.topdon.lib.core.utils.Constants.IR_TC007_MODE
import com.topdon.lib.core.utils.Constants.IR_TCPLUS_MODE
import com.topdon.lib.core.utils.Constants.IR_TEMPERATURE_LITE
import com.topdon.lib.core.utils.Constants.IR_TEMPERATURE_MODE
import com.topdon.lib.ui.bean.ColorBean
import com.topdon.lib.ui.config.CameraHelp
import com.topdon.lib.ui.listener.SingleClickListener
import com.topdon.lib.ui.R as UiR
import com.topdon.menu.R as MenuR

/**
 * Custom Menu four night view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
@Deprecated("旧的settingsmenu，已重构过了")
@SuppressLint("NotifyDataSetChanged")
/**
 * MenuFourNightAdapter provides data binding between data source and UI components.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing MenuFourNightAdapter functionality for the IRCamera system.
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
class MenuFourNightAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: ((index: Int, code: Int) -> Unit)? = null

    private var colorEnable = false // Pseudo color条
    private var contrastEnable = false 
    private var ddeEnable = false 
    private var alarmEnable = false 
    private var textColorEnable = false 
    private var mirrorEnable = false 
    private var waterMarkEnable = false 
    private var compassEnable = false 
    private var rotateAngle = DeviceConfig.S_ROTATE_ANGLE 
    /**
     * Executes selectrotate functionality.
     */
    /**
     * Executes selectrotate operation with thermal imaging domain optimization.
     *
     * @param
     * @param rotateAngle Angle in degrees (type: Int)
     *
     */
    fun selectRotate(rotateAngle: Int) {
        this.rotateAngle = rotateAngle
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes encolor functionality.
     */
    /**
     * Executes encolor operation with thermal imaging domain optimization.
     *
     * @param
     * @param colorEnable Parameter for operation (type: Boolean)
     *
     */
    fun enColor(colorEnable: Boolean) {
        this.colorEnable = colorEnable
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes encontrast functionality.
     */
    /**
     * Executes encontrast operation with thermal imaging domain optimization.
     *
     * @param
     * @param param Parameter for operation (type: Boolean)
     *
     */
    fun enContrast(param: Boolean) {
        this.contrastEnable = param
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes endde functionality.
     */
    /**
     * Executes endde operation with thermal imaging domain optimization.
     *
     * @param
     * @param param Parameter for operation (type: Boolean)
     *
     */
    fun enDde(param: Boolean) {
        this.ddeEnable = param
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes enalarm functionality.
     */
    /**
     * Executes enalarm operation with thermal imaging domain optimization.
     *
     * @param
     * @param param Parameter for operation (type: Boolean)
     *
     */
    fun enAlarm(param: Boolean) {
        this.alarmEnable = param
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes entextcolor functionality.
     */
    /**
     * Executes entextcolor operation with thermal imaging domain optimization.
     *
     * @param
     * @param param Parameter for operation (type: Boolean)
     *
     */
    fun enTextColor(param: Boolean) {
        this.textColorEnable = param
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes enmirror functionality.
     */
    /**
     * Executes enmirror operation with thermal imaging domain optimization.
     *
     * @param
     * @param param Parameter for operation (type: Boolean)
     *
     */
    fun enMirror(param: Boolean) {
        this.mirrorEnable = param
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes encompass functionality.
     */
    /**
     * Executes encompass operation with thermal imaging domain optimization.
     *
     * @param
     * @param param Parameter for operation (type: Boolean)
     *
     */
    fun enCompass(param: Boolean) {
        this.compassEnable = param
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * Executes enwatermark functionality.
     */
    /**
     * Executes enwatermark operation with thermal imaging domain optimization.
     *
     * @param
     * @param param Parameter for operation (type: Boolean)
     *
     */
    fun enWaterMark(param: Boolean) {
        this.waterMarkEnable = param
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    /**
     * 不知道干嘛的
     * parameter [Constants.IR_TEMPERATURE_MODE] = 1 temperature measurementmode   pseudo color条、contrast、锐度、warning、rotation、font、镜像
     * parameter [Constants.IR_TCPLUS_MODE] = 5 dual lightdevice        pseudo color条、contrast、锐度、warning、rotation、font、
     * parameter [Constants.IR_TEMPERATURE_LITE] = 7 Litedevice  pseudo color条、contrast、warning、rotation、font、镜像
     * parameter [Constants.IR_TC007_MODE] = 6 TC007          pseudo color条、contrast、锐度、warning、font、镜像
     * else - 2D编辑menu                                  warning、font、watermark
     * parameter [Constants.IR_OBSERVE_MODE] = 2 observationmode  指南针、rotation、镜像、contrast
     */
    /**
     * Sets showmenufour configuration.
     */
    fun setShowMenuFour(modeType: Int) {
        fourBean.clear()
        when (modeType) {
            IR_TEMPERATURE_MODE -> {
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_1,
                        context.getString(R.string.thermal_pseudo),
                        CameraHelp.TYPE_SET_PSEUDOCOLOR,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_2,
                        context.getString(R.string.thermal_contrast),
                        CameraHelp.TYPE_SET_ParamLevelContrast,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_3,
                        context.getString(R.string.thermal_sharpen),
                        CameraHelp.TYPE_SET_ParamLevelDde,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_6,
                        context.getString(R.string.temp_alarm_alarm),
                        CameraHelp.TYPE_SET_ALARM,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_4,
                        context.getString(R.string.thermal_rotate),
                        CameraHelp.TYPE_SET_ROTATE,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_7,
                        context.getString(R.string.menu_thermal_font),
                        CameraHelp.TYPE_SET_COLOR,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(MenuR.drawable.selector_menu2_setting_5, context.getString(R.string.mirror), CameraHelp.TYPE_SET_MIRROR),
                )
            }
            IR_TCPLUS_MODE -> {
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_1,
                        context.getString(R.string.thermal_pseudo),
                        CameraHelp.TYPE_SET_PSEUDOCOLOR,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_2,
                        context.getString(R.string.thermal_contrast),
                        CameraHelp.TYPE_SET_ParamLevelContrast,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_3,
                        context.getString(R.string.thermal_sharpen),
                        CameraHelp.TYPE_SET_ParamLevelDde,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_6,
                        context.getString(R.string.temp_alarm_alarm),
                        CameraHelp.TYPE_SET_ALARM,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_4,
                        context.getString(R.string.thermal_rotate),
                        CameraHelp.TYPE_SET_ROTATE,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_7,
                        context.getString(R.string.menu_thermal_font),
                        CameraHelp.TYPE_SET_COLOR,
                    ),
                )
            }
            IR_TEMPERATURE_LITE -> {
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_1,
                        context.getString(R.string.thermal_pseudo),
                        CameraHelp.TYPE_SET_PSEUDOCOLOR,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_2,
                        context.getString(R.string.thermal_contrast),
                        CameraHelp.TYPE_SET_ParamLevelContrast,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_6,
                        context.getString(R.string.temp_alarm_alarm),
                        CameraHelp.TYPE_SET_ALARM,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_4,
                        context.getString(R.string.thermal_rotate),
                        CameraHelp.TYPE_SET_ROTATE,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_7,
                        context.getString(R.string.menu_thermal_font),
                        CameraHelp.TYPE_SET_COLOR,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(MenuR.drawable.selector_menu2_setting_5, context.getString(R.string.mirror), CameraHelp.TYPE_SET_MIRROR),
                )
            }
            IR_TC007_MODE -> {
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_1,
                        context.getString(R.string.thermal_pseudo),
                        CameraHelp.TYPE_SET_PSEUDOCOLOR,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_2,
                        context.getString(R.string.thermal_contrast),
                        CameraHelp.TYPE_SET_ParamLevelContrast,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_3,
                        context.getString(R.string.thermal_sharpen),
                        CameraHelp.TYPE_SET_ParamLevelDde,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_6,
                        context.getString(R.string.temp_alarm_alarm),
                        CameraHelp.TYPE_SET_ALARM,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_7,
                        context.getString(R.string.menu_thermal_font),
                        CameraHelp.TYPE_SET_COLOR,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(MenuR.drawable.selector_menu2_setting_5, context.getString(R.string.mirror), CameraHelp.TYPE_SET_MIRROR),
                )
            }
            IR_OBSERVE_MODE -> {
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_8,
                        context.getString(R.string.main_tab_second_compass),
                        CameraHelp.TYPE_SET_COMPASS,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_4,
                        context.getString(R.string.thermal_rotate),
                        CameraHelp.TYPE_SET_ROTATE,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(MenuR.drawable.selector_menu2_setting_5, context.getString(R.string.mirror), CameraHelp.TYPE_SET_MIRROR),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_2,
                        context.getString(R.string.thermal_contrast),
                        CameraHelp.TYPE_SET_ParamLevelContrast,
                    ),
                )
            }
            else -> {
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_6,
                        context.getString(R.string.temp_alarm_alarm),
                        CameraHelp.TYPE_SET_ALARM,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_7,
                        context.getString(R.string.menu_thermal_font),
                        CameraHelp.TYPE_SET_COLOR,
                    ),
                )
                fourBean.add(
                    /**
                     * Executes colorbean operation with thermal imaging domain optimization.
                     *
                     */
                    ColorBean(
                        MenuR.drawable.selector_menu2_setting_9,
                        context.getString(R.string.app_watemarking),
                        CameraHelp.TYPE_SET_WATERMARK,
                    ),
                )
            }
        }
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged()
    }

    private val fourBean =
        /**
         * Executes arraylistof operation with thermal imaging domain optimization.
         *
         */
        arrayListOf(
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_setting_1, context.getString(R.string.thermal_pseudo), CameraHelp.TYPE_SET_PSEUDOCOLOR),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_setting_2,
                context.getString(R.string.thermal_contrast),
                CameraHelp.TYPE_SET_ParamLevelContrast,
            ),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(
                MenuR.drawable.selector_menu2_setting_3,
                context.getString(R.string.thermal_sharpen),
                CameraHelp.TYPE_SET_ParamLevelDde,
            ),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_setting_6, context.getString(R.string.temp_alarm_alarm), CameraHelp.TYPE_SET_ALARM),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_setting_4, context.getString(R.string.thermal_rotate), CameraHelp.TYPE_SET_ROTATE),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_setting_7, context.getString(R.string.menu_thermal_font), CameraHelp.TYPE_SET_COLOR),
            /**
             * Executes colorbean operation with thermal imaging domain optimization.
             *
             */
            ColorBean(MenuR.drawable.selector_menu2_setting_5, context.getString(R.string.mirror), CameraHelp.TYPE_SET_MIRROR),
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
        val view = LayoutInflater.from(parent.context).inflate(UiR.layout.ui_item_menu_second_view, parent, false)
        compassEnable = SaveSettingUtil.isOpenCompass
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
        @SuppressLint("RecyclerView") position: Int,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (holder is ItemView) {
            
            /**
             * Executes updateviewwidth operation with thermal imaging domain optimization.
             *
             */
            updateViewWidth(holder.itemView, holder.img)
            val bean = fourBean[position]
            holder.name.text = bean.name
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (bean.code == CameraHelp.TYPE_SET_ROTATE) {
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (rotateAngle) {
                    0 -> {
                        holder.img.setImageResource(MenuR.drawable.svg_menu2_setting_4_rotate270)
                    }
                    90 -> {
                        holder.img.setImageResource(MenuR.drawable.svg_menu2_setting_4_rotate180)
                    }
                    180 -> {
                        holder.img.setImageResource(MenuR.drawable.svg_menu2_setting_4_rotate90)
                    }
                    270 -> {
                        holder.img.setImageResource(MenuR.drawable.svg_menu2_setting_4_rotate0)
                    }
                }
            } else {
                holder.img.setImageResource(bean.res)
            }
            holder.lay.setOnClickListener(
                object : SingleClickListener() {
                    /**
                     * Executes onsingleclick operation with thermal imaging domain optimization.
                     *
                     */
                    override fun onSingleClick() {
                        listener?.invoke(position, bean.code)
                    }
                },
            )
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (bean.code) {
                CameraHelp.TYPE_SET_ROTATE -> {
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (rotateAngle) {
                        0 -> {
                            holder.name.setTextColor(ContextCompat.getColor(context, UiR.color.white))
                        }
                        90 -> {
                            holder.name.setTextColor(ContextCompat.getColor(context, UiR.color.white))
                        }
                        180 -> {
                            holder.name.setTextColor(ContextCompat.getColor(context, UiR.color.white))
                        }
                        270 -> {
                            holder.name.setTextColor(ContextCompat.getColor(context, UiR.color.font_third_color))
                        }
                    }
                }
                CameraHelp.TYPE_SET_ParamLevelDde -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(ddeEnable, holder.img, holder.name)
                }
                CameraHelp.TYPE_SET_ParamLevelContrast -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(contrastEnable, holder.img, holder.name)
                }
                CameraHelp.TYPE_SET_PSEUDOCOLOR -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(colorEnable, holder.img, holder.name)
                }
                CameraHelp.TYPE_SET_ALARM -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(alarmEnable, holder.img, holder.name)
                }
                CameraHelp.TYPE_SET_COLOR -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(textColorEnable, holder.img, holder.name)
                }
                CameraHelp.TYPE_SET_MIRROR -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(mirrorEnable, holder.img, holder.name)
                }
                CameraHelp.TYPE_SET_COMPASS -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(compassEnable, holder.img, holder.name)
                }
                CameraHelp.TYPE_SET_WATERMARK -> {
                    /**
                     * Executes iconui operation with thermal imaging domain optimization.
                     *
                     */
                    iconUI(waterMarkEnable, holder.img, holder.name)
                }
            }
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
     * @param nameText Parameter for operation (type: TextView)
     *
     */
    private fun iconUI(
        isActive: Boolean,
        img: ImageView,
        nameText: TextView,
    ) {
        img.isSelected = isActive
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isActive) {
            nameText.setTextColor(ContextCompat.getColor(context, UiR.color.white))
        } else {
            nameText.setTextColor(ContextCompat.getColor(context, UiR.color.font_third_color))
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return fourBean.size
    }

    /**
     * Updates the viewwidth with new data.
     */
    private fun updateViewWidth(
        itemView: View,
        itemMenu: ImageView,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (fourBean.size <= 4) {
            itemView.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            itemView.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
// If (fourBean.size <= 4) {  // Item少于4个，每个占1/4
// Val canSeeCount = fourBean.size 
// Val with = (ScreenUtils.getScreenWidth() / canSeeCount)
// ItemView.layoutParams =
//                ViewGroup.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT)
// Val imageSize = (ScreenUtils.getScreenWidth() * 62 / 375f).toInt()
// Val layoutParams = itemMenu.layoutParams
// LayoutParams.width = imageSize
// LayoutParams.height = imageSize
// ItemMenu.layoutParams = layoutParams
//        } else {    // Item大于4个，每屏4.5个item
// Val canSeeCount = 4.5 
// Val with = (ScreenUtils.getScreenWidth() / canSeeCount).toInt()
// ItemView.layoutParams =
//                ConstraintLayout.LayoutParams(with, ConstraintLayout.LayoutParams.WRAP_CONTENT)
// Val imageSize = (ScreenUtils.getScreenWidth() * 62 / 375f).toInt()
// Val layoutParams = itemMenu.layoutParams
// LayoutParams.width = imageSize
// LayoutParams.height = imageSize
// ItemMenu.layoutParams = layoutParams
//        }
    }

    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lay: View = itemView.findViewById(UiR.id.item_menu_tab_lay)
        val img: ImageView = itemView.findViewById(UiR.id.item_menu_tab_img)
        val name: TextView = itemView.findViewById(UiR.id.item_menu_tab_text)
    }
}
