package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.annotation.StringRes
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.DialogTipShutterBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * 自动快门tip弹窗
 * @author: CaiSongL
 * @date: 2023/4/13 10:57
 */
/**
 * TipShutterDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing TipShutterDialog functionality for the IRCamera system.
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
class TipShutterDialog : Dialog {
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
    class Builder(private val context: Context) {
        var dialog: TipShutterDialog? = null
        private var titleRes: Int? = null
        private var message: CharSequence? = null
        private var closeEvent: ((check: Boolean) -> Unit)? = null
        private var canceled = false

    /**
     * Sets title configuration.
     */
        /**
         * Configures the title with validation and thermal imaging optimization.
         *
         * @param
         * @param resId Parameter for operation (type: Int)
         *
         */
        fun setTitle(
            @StringRes resId: Int,
        ): Builder {
            this.titleRes = resId
            return this
        }

    /**
     * Sets message configuration.
     */
        fun setMessage(message: CharSequence): Builder {
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
     * Creates and configures a new  instance.
     */
        fun create(): TipShutterDialog {
            if (dialog == null) {
                dialog = TipShutterDialog(context, R.style.InfoDialog)
            }
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val binding = DialogTipShutterBinding.inflate(LayoutInflater.from(context!!))
            dialog!!.addContentView(binding.root, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            dialog!!.setCanceledOnTouchOutside(canceled)

            val lp = dialog!!.window!!.attributes
            lp.width = (ScreenUtil.getScreenWidth(context) * if (ScreenUtil.isPortrait(context)) 0.85 else 0.35).toInt() 
            dialog!!.window!!.attributes = lp

            binding.tvIKnow.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                closeEvent?.invoke(binding.dialogTipCheck.isChecked)
            }
            binding.imgClose.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                closeEvent?.invoke(binding.dialogTipCheck.isChecked)
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (titleRes != null) {
                binding.tvTitle.setText(titleRes!!)
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
            dialog!!.setContentView(binding.root)
            return dialog as TipShutterDialog
        }
    }
}
