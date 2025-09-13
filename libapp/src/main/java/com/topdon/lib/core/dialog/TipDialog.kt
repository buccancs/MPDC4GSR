package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.DialogTipBinding

/**
 * tip窗
 * create by fylder on 2018/6/15
 **/
/**
 * TipDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing TipDialog functionality for the IRCamera system.
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
class TipDialog : Dialog {
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor(context: Context) : super(context)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param themeResId Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    @Deprecated("This method is deprecated")
    /**
     * Executes onbackpressed operation with thermal imaging domain optimization.
     *
     */
    override fun onBackPressed() {
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
    class Builder(private val context: Context) {
        var dialog: TipDialog? = null

        private var message: String? = null
        private var titleMessage: String? = null
        private var positiveStr: String? = null
        private var cancelStr: String? = null
        private var positiveEvent: (() -> Unit)? = null
        private var cancelEvent: (() -> Unit)? = null
        private var canceled = false
        private var isShowRestartTips = false

    /**
     * Sets titlemessage configuration.
     */
        fun setTitleMessage(message: String): Builder {
            this.titleMessage = message
            return this
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
            this.message = context.getString(message)
            return this
        }

    /**
     * Sets positivelistener configuration.
     */
        fun setPositiveListener(
            @StringRes strRes: Int,
            event: (() -> Unit)? = null,
        ): Builder {
            return setPositiveListener(context.getString(strRes), event)
        }

    /**
     * Sets positivelistener configuration.
     */
        fun setPositiveListener(
            str: String,
            event: (() -> Unit)? = null,
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
            return setCancelListener(context.getString(strRes), event)
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
     * Sets showrestarttops configuration.
     */
        fun setShowRestartTops(isShowRestartTips: Boolean): Builder {
            this.isShowRestartTips = isShowRestartTips
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
        fun create(): TipDialog {
            if (dialog == null) {
                dialog = TipDialog(context, R.style.InfoDialog)
            }

            val binding = DialogTipBinding.inflate(LayoutInflater.from(context))
            dialog!!.addContentView(binding.root, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            val isPortrait = context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            val widthPixels = context.resources.displayMetrics.widthPixels
            val lp = dialog!!.window!!.attributes
            lp.width = (widthPixels * if (isPortrait) 0.85 else 0.35).toInt() 
            dialog!!.window!!.attributes = lp

            dialog!!.setCanceledOnTouchOutside(canceled)
            binding.dialogTipSuccessBtn.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                positiveEvent?.invoke()
            }
            binding.dialogTipCancelBtn.setOnClickListener {
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
                binding.dialogTipSuccessBtn.text = positiveStr
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!TextUtils.isEmpty(cancelStr)) {
                binding.spaceMargin.visibility = View.VISIBLE
                binding.dialogTipCancelBtn.visibility = View.VISIBLE
                binding.dialogTipCancelBtn.text = cancelStr
            } else {
                binding.spaceMargin.visibility = View.GONE
                binding.dialogTipCancelBtn.visibility = View.GONE
                binding.dialogTipCancelBtn.text = ""
            }
            
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (message != null) {
                binding.dialogTipMsgText.visibility = View.VISIBLE
                binding.dialogTipMsgText.setText(message, TextView.BufferType.NORMAL)
            } else {
                binding.dialogTipMsgText.visibility = View.GONE
            }

            
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (titleMessage != null) {
                binding.dialogTipTitleMsgText.visibility = View.VISIBLE
                binding.dialogTipTitleMsgText.setText(titleMessage, TextView.BufferType.NORMAL)
            } else {
                binding.dialogTipTitleMsgText.visibility = View.GONE
            }

            binding.tvRestartTips.isVisible = isShowRestartTips

            dialog!!.setContentView(binding.root)
            return dialog as TipDialog
        }
    }
}
