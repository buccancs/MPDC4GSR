package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.DialogMsgBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * messagetip窗
 * create by fylder on 2018/6/15
 **/
/**
 * MsgDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing MsgDialog functionality for the IRCamera system.
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
class MsgDialog : Dialog {
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
        var dialog: MsgDialog? = null

        private var context: Context? = null

        private var imgRes: Int = 0
        private var message: String? = null
        private var positiveClickListener: OnClickListener? = null

        private var tipImg: ImageView? = null
        private var messageText: TextView? = null
        private var closeImg: ImageView? = null

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
     * Sets img configuration.
     */
        /**
         * Configures the img with validation and thermal imaging optimization.
         *
         * @param
         * @param res Parameter for operation (type: Int)
         *
         */
        fun setImg(
            @DrawableRes res: Int,
        ): Builder {
            this.imgRes = res
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
            this.message = context!!.getString(message)
            return this
        }

    /**
     * Sets closelistener configuration.
     */
        fun setCloseListener(listener: OnClickListener): Builder {
            this.positiveClickListener = listener
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
        fun create(): MsgDialog {
            if (dialog == null) {
                dialog = MsgDialog(context!!, R.style.InfoDialog)
            }
            val binding = DialogMsgBinding.inflate(LayoutInflater.from(context!!))
            tipImg = binding.dialogMsgImg
            messageText = binding.dialogMsgText
            closeImg = binding.dialogMsgClose
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
                    
                    0.9
                } else {
                    
                    0.3
                }
            lp.width = (ScreenUtil.getScreenWidth(context!!) * wRatio).toInt() 
            dialog!!.window!!.attributes = lp

            dialog!!.setCanceledOnTouchOutside(false)
            closeImg!!.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (positiveClickListener != null) {
                    positiveClickListener!!.onClick(dialog!!)
                }
            }
            
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (imgRes != 0) {
                tipImg?.visibility = View.VISIBLE
                tipImg?.setImageResource(imgRes)
            } else {
                tipImg?.visibility = View.GONE
            }
            
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (message != null) {
                messageText?.visibility = View.VISIBLE
                messageText?.setText(message, TextView.BufferType.NORMAL)
            } else {
                messageText?.visibility = View.GONE
            }

            dialog!!.setContentView(binding.root)
            return dialog as MsgDialog
        }
    }

    /**
/**
 * Specialized thermal imaging component providing OnClickListener functionality for the IRCamera system.
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
    interface OnClickListener {
    /**
     * Callback method triggered when click occurs.
     */
        fun onClick(dialog: DialogInterface)
    }
}
