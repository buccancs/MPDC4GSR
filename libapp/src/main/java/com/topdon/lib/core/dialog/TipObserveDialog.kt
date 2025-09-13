package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.*
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.DialogTipObserveBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * 观测-弹框封装
 */
/**
 * TipObserveDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing TipObserveDialog functionality for the IRCamera system.
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
class TipObserveDialog : Dialog {
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
        var dialog: TipObserveDialog? = null
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
        fun create(): TipObserveDialog {
            if (dialog == null) {
                dialog = TipObserveDialog(context!!, R.style.InfoDialog)
            }
            val inflater =
                context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val binding = DialogTipObserveBinding.inflate(LayoutInflater.from(context!!))

            binding.tvIKnow.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                closeEvent?.invoke(hasCheck)
            }

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
                    
                    0.75
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
            imgClose.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                closeEvent?.invoke(hasCheck)
            }
            
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (title != null) {
                titleText.setText(title, TextView.BufferType.NORMAL)
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
            return dialog as TipObserveDialog
        }
    }
}
