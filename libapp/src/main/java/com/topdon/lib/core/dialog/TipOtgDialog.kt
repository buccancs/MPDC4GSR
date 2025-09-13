package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.StringRes
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.DialogTipOtgBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * tip窗
 * create by fylder on 2018/6/15
 **/
/**
 * TipOtgDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing TipOtgDialog functionality for the IRCamera system.
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
class TipOtgDialog : Dialog {
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor(context: Context) : super(context)

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
        var dialog: TipOtgDialog? = null
        private var context: Context? = null
        private var message: String? = null
        private var positiveStr: String? = null
        private var cancelStr: String? = null
        private var positiveEvent: ((check: Boolean) -> Unit)? = null
        private var cancelEvent: (() -> Unit)? = null
        private var canceled = false
        private var hasCheck = false

        private lateinit var messageText: TextView
        private lateinit var checkBox: CheckBox
        private lateinit var successBtn: Button
        private lateinit var cancelBtn: Button

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
        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

    /**
     * Sets message configuration.
     */
        fun setMessage(
            @StringRes message: Int,
        ): Builder {
            this.message = context!!.getString(message)
            return this
        }

    /**
     * Sets positivelistener configuration.
     */
        fun setPositiveListener(
            @StringRes strRes: Int,
            event: ((check: Boolean) -> Unit)? = null,
        ): Builder {
            return setPositiveListener(context!!.getString(strRes), event)
        }

    /**
     * Sets positivelistener configuration.
     */
        fun setPositiveListener(
            str: String,
            event: ((check: Boolean) -> Unit)? = null,
        ): Builder {
            this.positiveStr = str
            this.positiveEvent = event
            return this
        }

    /**
     * Sets cancellistener configuration.
     */
        fun setCancelListener(
            @StringRes strRes: Int,
            event: (() -> Unit)? = null,
        ): Builder {
            return setCancelListener(context!!.getString(strRes), event)
        }

    /**
     * Sets cancellistener configuration.
     */
        fun setCancelListener(
            str: String,
            event: (() -> Unit)? = null,
        ): Builder {
            this.cancelStr = str
            this.cancelEvent = event
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
     * Creates and configures a new  instance.
     */
        fun create(): TipOtgDialog {
            if (dialog == null) {
                dialog = TipOtgDialog(context!!, R.style.InfoDialog)
            }
            val inflater =
                context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val binding = DialogTipOtgBinding.inflate(LayoutInflater.from(context!!))
            messageText = binding.dialogTipMsgText
            checkBox = binding.dialogTipCheck
            successBtn = binding.dialogTipSuccessBtn
            cancelBtn = binding.dialogTipCancelBtn
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
                    
                    0.85
                } else {
                    
                    0.35
                }
            lp.width = (ScreenUtil.getScreenWidth(context!!) * wRatio).toInt() 
            dialog!!.window!!.attributes = lp

            dialog!!.setCanceledOnTouchOutside(canceled)
            checkBox.isChecked = false
            hasCheck = false
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                hasCheck = isChecked
            }
            successBtn.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                positiveEvent?.invoke(hasCheck)
            }
            cancelBtn.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                cancelEvent?.invoke()
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (positiveStr != null) {
                successBtn.text = positiveStr
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!TextUtils.isEmpty(cancelStr)) {
                cancelBtn.visibility = View.VISIBLE
                cancelBtn.text = cancelStr
            } else {
                cancelBtn.visibility = View.GONE
                cancelBtn.text = ""
            }
            
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (message != null) {
                messageText.visibility = View.VISIBLE
                messageText.setText(message, TextView.BufferType.NORMAL)
            } else {
                messageText.visibility = View.GONE
            }

            dialog!!.setContentView(binding.root)
            return dialog as TipOtgDialog
        }
    }
}
