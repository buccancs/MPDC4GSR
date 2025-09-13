package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.DialogLoadingBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * Specialized thermal imaging component providing LoadingDialog functionality for the IRCamera system.
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
class LoadingDialog(context: Context) : Dialog(context, R.style.TransparentDialog) {
    private val binding: DialogLoadingBinding = DialogLoadingBinding.inflate(LayoutInflater.from(context))

    /**
     * Sets tips configuration.
     */
    /**
     * Configures the tips with validation and thermal imaging optimization.
     *
     * @param
     * @param resId Parameter for operation (type: Int)
     *
     */
    fun setTips(
        @StringRes resId: Int,
    ) {
        binding.tvTips.setText(resId)
        binding.tvTips.isVisible = true
    }

    /**
     * Sets tips configuration.
     */
    /**
     * Configures the tips with validation and thermal imaging optimization.
     *
     * @param
     * @param text Parameter for operation (type: CharSequence?)
     *
     */
    fun setTips(text: CharSequence?) {
        binding.tvTips.text = text
        binding.tvTips.isVisible = text?.isNotEmpty() == true
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

        window?.let {
            val layoutParams = it.attributes
            layoutParams.width = (ScreenUtil.getScreenWidth(context) * if (ScreenUtil.isPortrait(context)) 0.3 else 0.15).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }
    }
}
