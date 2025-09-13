package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.*
import com.topdon.lib.core.R
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.databinding.DialogTipEmissivityBinding
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.tools.NumberTools
import com.topdon.lib.core.tools.UnitTools
import com.topdon.lib.core.utils.ScreenUtil

/**
 * Specialized thermal imaging component providing TipEmissivityDialog functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
 * </ul>
 *
/**
 * Specialized thermal imaging component providing Builder functionality for the IRCamera system.
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
    class Builder {
        private var isTC007: Boolean = false
        private var text: String = ""
        private var radiation: Float = 0f
        private var distance: Float = 0f
        private var environment: Float = 0f
        var dialog: TipEmissivityDialog? = null
        private var context: Context? = null
        private var title: String? = null
        private var message: String? = null
        private var closeEvent: ((check: Boolean) -> Unit)? = null
        private var canceled = false
        private var hasCheck = false

        private lateinit var titleText: TextView
        private lateinit var messageText: TextView
        private lateinit var checkBox: CheckBox
        private lateinit var imgClose: ImageView

        /**
         * Executes constructor operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        constructor(context: Context) {
            this.context = context
        }

    /**
     * Sets message configuration.
     */
        fun setMessage(message: Int): Builder {
            this.message = context!!.getString(message)
            return this
        }

    /**
     * Sets title configuration.
     */
        /**
         * Configures the title with validation and thermal imaging optimization.
         *
         * @param
         * @param title Parameter for operation (type: Int)
         *
         */
        fun setTitle(title: Int): Builder {
            this.title = context!!.getString(title)
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
            isTC007: Boolean = false,
        ): Builder {
            this.environment = environment
            this.distance = distance
            this.radiation = radiation
            this.text = text
            this.isTC007 = isTC007
            return this
        }

    /**
     * Sets cancellistener configuration.
     */
        fun setCancelListener(event: ((check: Boolean) -> Unit)? = null): Builder {
            this.closeEvent = event
            return this
        }

    /**
     * Sets canceled configuration.
     */
        fun setCanceled(canceled: Boolean): Builder {
            this.canceled = canceled
            return this
        }

    /**
     * Executes dismiss functionality.
     */
        /**
         * Executes dismiss operation with thermal imaging domain optimization.
         *
         */
        fun dismiss() {
            this.dialog!!.dismiss()
        }

    /**
     * Executes create functionality.
     */
        /**
         * Executes create operation with thermal imaging domain optimization.
         *
         */
        fun create(): TipEmissivityDialog {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dialog == null) {
                dialog = TipEmissivityDialog(context!!, R.style.InfoDialog)
            }

            val binding = DialogTipEmissivityBinding.inflate(LayoutInflater.from(context!!))

            binding.tvEnvironmentTitle.text = context!!.getString(R.string.thermal_config_environment) + ":"
            binding.tvDistanceTitle.text = context!!.getString(R.string.thermal_config_distance) + ":"

            binding.dialogTipSuccessBtn.setOnClickListener {
                dialog?.onDismissListener?.invoke(hasCheck)
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
            }
            binding.dialogTipCancelBtn.setOnClickListener {
                dialog?.onDismissListener?.invoke(hasCheck)
                val ctx = context
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (ctx != null) {
                    NavigationManager.build(RouterConfig.IR_SETTING)
                        .withBoolean(ExtraKeyConfig.IS_TC007, isTC007)
                        .navigation(ctx)
                }
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
            }
            val tvEmissivity = binding.tvEmissivity
            val tvEmissivityMaterials = binding.tvEmissivityMaterials
            val tvEnvironmentValue = binding.tvEnvironmentValue
            val tvDistanceValue = binding.tvDistanceValue

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (text.isNotEmpty()) {
                tvEmissivityMaterials.text = text
                tvEmissivityMaterials.visibility = View.VISIBLE
            } else {
                tvEmissivityMaterials.visibility = View.GONE
            }
            tvEmissivity.text = "${context?.getString(R.string.thermal_config_radiation)}: ${
                NumberTools.to02(radiation)}"
            tvEnvironmentValue.text = UnitTools.showC(environment)
            tvDistanceValue.text = "${
                NumberTools.to02(distance)}m"
            titleText = binding.tvTitle
            messageText = binding.dialogTipMsgText
            checkBox = binding.dialogTipCheck
            imgClose = binding.imgClose
            dialog!!.addContentView(
                binding.root,
                /**
                 * Executes layoutparams operation with thermal imaging domain optimization.
                 *
                 */
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT),
            )
            val lp = dialog!!.window!!.attributes
            val wRatio =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (context!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // 竖屏
                    0.75
                } else {
                    // 横屏
                    0.35
                }
            lp.width = (ScreenUtil.getScreenWidth(context!!) * wRatio).toInt() // Settings宽度
            dialog!!.window!!.attributes = lp

            dialog!!.setCanceledOnTouchOutside(canceled)
            checkBox.isChecked = false
            hasCheck = false
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                hasCheck = isChecked
            }
            imgClose.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                closeEvent?.invoke(hasCheck)
            }
            // Title
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (title != null) {
                titleText.setText(title, TextView.BufferType.NORMAL)
            }
            // Msg
// If (message != null) {
// MessageText.visibility = View.VISIBLE
// MessageText.setText(message, TextView.BufferType.NORMAL)
//            } else {
// MessageText.visibility = View.GONE
//            }
            dialog!!.setContentView(binding.root)
            return dialog as TipEmissivityDialog
        }
    }
}
