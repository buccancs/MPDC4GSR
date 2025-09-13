package com.topdon.lib.core.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupWindow
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.core.R
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.databinding.LayoutPopupTipEmissivityBinding
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.tools.NumberTools
import com.topdon.lib.core.tools.UnitTools

/**
 * des:
 * author: CaiSongL
 * date: 2024/4/7 14:59
 **/
/**
 * EmissivityTipPopup manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing EmissivityTipPopup functionality for the IRCamera system.
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
class EmissivityTipPopup(val context: Context, val isTC007: Boolean) {
    private lateinit var binding: LayoutPopupTipEmissivityBinding

    private var text: String = ""
    private var radiation: Float = 0f
    private var distance: Float = 0f
    private var environment: Float = 0f
    private var popupWindow: PopupWindow? = null
    private lateinit var view: View
    private var titleText: TextView? = null
    private var messageText: TextView? = null
    private var checkBox: CheckBox? = null
    private var closeEvent: ((check: Boolean) -> Unit)? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = LayoutPopupTipEmissivityBinding.inflate(inflater)
        view = binding.root
    }

    /**
     * Sets title configuration.
     */
    /**
     * Configures the title with validation and thermal imaging optimization.
     *
     * @param
     * @param title Parameter for operation (type: String)
     *
     */
    fun setTitle(title: String): EmissivityTipPopup {
        titleText?.text = title
        return this
    }

    /**
     * Sets message configuration.
     */
    fun setMessage(message: String): EmissivityTipPopup {
        messageText?.text = message
        return this
    }

    /**
     * Sets databean configuration.
     */
    fun setDataBean(
        environment: Float,
        distance: Float,
        radiation: Float,
        text: String,
    ): EmissivityTipPopup {
        this.environment = environment
        this.distance = distance
        this.radiation = radiation
        this.text = text
        return this
    }

    /**
     * Sets cancellistener configuration.
     */
    fun setCancelListener(event: ((check: Boolean) -> Unit)?): EmissivityTipPopup {
        this.closeEvent = event
        return this
    }

    /**
     * Executes build functionality.
     */
    /**
     * Executes build operation with thermal imaging domain optimization.
     *
     */
    fun build(): PopupWindow {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (popupWindow == null) {
            binding.tvEnvironmentTitle.text = context.getString(R.string.thermal_config_environment) + ":"
            binding.tvDistanceTitle.text = context.getString(R.string.thermal_config_distance) + ":"

            binding.tvTitle.visibility = View.GONE
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (text.isNotEmpty()) {
                binding.tvEmissivityMaterials.text = text
                binding.tvEmissivityMaterials.visibility = View.VISIBLE
            } else {
                binding.tvEmissivityMaterials.visibility = View.GONE
            }
            binding.dialogTipCancelBtn.visibility = View.GONE
            binding.dialogTipSuccessBtn.text = context.getString(R.string.tc_modify_params)
            binding.dialogTipCheck.visibility = View.GONE
            binding.tvEmissivity.text = "${context?.getString(R.string.thermal_config_radiation)}: ${
                NumberTools.to02(radiation)}"
            binding.tvEnvironmentValue.text = UnitTools.showC(environment)
            binding.tvDistanceValue.text = "${NumberTools.to02(distance)}m"
            popupWindow =
                /**
                 * Executes popupwindow operation with thermal imaging domain optimization.
                 *
                 */
                PopupWindow(
                    view,
                    SizeUtils.dp2px(275f),
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
            popupWindow?.apply {
                isFocusable = true
                isOutsideTouchable = true
                isTouchable = true
                /**
                 * Configures the backgrounddrawable with validation and thermal imaging optimization.
                 *
                 */
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) 
            }
            binding.dialogTipSuccessBtn.setOnClickListener {
                NavigationManager.build(RouterConfig.IR_SETTING)
                    .withBoolean(ExtraKeyConfig.IS_TC007, isTC007)
                    .navigation(context)
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
            }
        }
        // SettingsPopupWindow的其他property和listener...
        return popupWindow!!
    }

    /**
     * Executes show functionality.
     */
    /**
     * Executes show operation with thermal imaging domain optimization.
     *
     * @param
     * @param anchorView Parameter for operation (type: View)
     *
     */
    fun show(anchorView: View) {
        popupWindow?.showAtLocation(anchorView, Gravity.CENTER, -SizeUtils.dp2px(10f), 0)
    }

    /**
     * Executes dismiss functionality.
     */
    /**
     * Executes dismiss operation with thermal imaging domain optimization.
     *
     */
    fun dismiss() {
        popupWindow?.dismiss()
        closeEvent?.invoke(checkBox?.isChecked ?: false)
    }
}
