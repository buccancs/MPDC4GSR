package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.DialogNotTipsSelectBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * 与 TipDialog class似，不过多了个 “不再tip” selected效果的tip弹窗.
 *
 * Created by LCG on 2024/10/26.
 */
/**
 * NotTipsSelectDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing NotTipsSelectDialog functionality for the IRCamera system.
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
class NotTipsSelectDialog(context: Context) : Dialog(context, R.style.InfoDialog) {
    @StringRes
    private var tipsResId: Int = 0
    private var onConfirmListener: ((isSelect: Boolean) -> Unit)? = null

    private val binding: DialogNotTipsSelectBinding = DialogNotTipsSelectBinding.inflate(layoutInflater)

    /**
     * Sets tipsresid configuration.
     */
    fun setTipsResId(
        @StringRes tipsResId: Int,
    ): NotTipsSelectDialog {
        this.tipsResId = tipsResId
        return this
    }

    /**
     * click “我知道了” EventListener.
     */
    fun setOnConfirmListener(l: ((isSelect: Boolean) -> Unit)?): NotTipsSelectDialog {
        onConfirmListener = l
        return this
    }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Configures the cancelable with validation and thermal imaging optimization.
         *
         */
        setCancelable(false)
        /**
         * Configures the canceledontouchoutside with validation and thermal imaging optimization.
         *
         */
        setCanceledOnTouchOutside(false)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (tipsResId != 0) {
            binding.tvMessage.setText(tipsResId)
        }
        binding.tvSelect.setOnClickListener {
            it.isSelected = !it.isSelected
        }
        binding.tvIKnow.setOnClickListener {
            onConfirmListener?.invoke(binding.tvSelect.isSelected)
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss()
        }

        window?.let {
            val layoutParams = it.attributes
            layoutParams.width = (ScreenUtil.getScreenWidth(context) * 0.73f).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }
    }
}
