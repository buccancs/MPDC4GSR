package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.R
import com.topdon.lib.core.adapter.TargetColorAdapter
import com.topdon.lib.core.bean.ObserveBean
import com.topdon.lib.core.databinding.DialogTipTargetColorBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * 观测-标靶颜色
 */
/**
 * TipTargetColorDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing TipTargetColorDialog functionality for the IRCamera system.
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
class TipTargetColorDialog : Dialog {
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
        var dialog: TipTargetColorDialog? = null
        private var context: Context? = null
        private var closeEvent: ((targetColor: Int) -> Unit)? = null
        private var canceled = false
        private var targetColor = ObserveBean.TYPE_TARGET_COLOR_GREEN

        private lateinit var titleText: TextView
        private lateinit var imgClose: ImageView
        private lateinit var recyclerView: RecyclerView

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
     * Sets cancellistener configuration.
     */
        fun setCancelListener(event: ((targetColor: Int) -> Unit)? = null): Builder {
            this.closeEvent = event
            return this
        }

    /**
     * Sets targetcolor configuration.
     */
        fun setTargetColor(color: Int): Builder {
            this.targetColor = color
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
        fun create(): TipTargetColorDialog {
            if (dialog == null) {
                dialog = TipTargetColorDialog(context!!, R.style.InfoDialog)
            }
            val binding = DialogTipTargetColorBinding.inflate(LayoutInflater.from(context!!))

            binding.tvIKnow.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                closeEvent?.invoke(targetColor)
            }

            titleText = binding.tvTitle
            imgClose = binding.imgClose
            recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
            val targetColorAdapter = TargetColorAdapter(context!!, targetColor)
            targetColorAdapter.listener = listener@{ _, item ->
                targetColor = item
                targetColorAdapter.selectedCode(item)
            }
            recyclerView?.adapter = targetColorAdapter
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
                    
                    0.90
                } else {
                    
                    0.35
                }
            lp.width = (ScreenUtil.getScreenWidth(context!!) * wRatio).toInt() 
            dialog!!.window!!.attributes = lp

            dialog!!.setCanceledOnTouchOutside(canceled)
            imgClose.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
// CloseEvent?.invoke(targetColor)
            }
            dialog!!.setContentView(binding.root)
            return dialog as TipTargetColorDialog
        }
    }
}
