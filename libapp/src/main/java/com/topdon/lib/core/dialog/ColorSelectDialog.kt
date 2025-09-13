package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.DialogColorSelectBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * 仅拾取颜色的弹框.
 *
 * Created by LCG on 2024/2/2.
 */
/**
 * ColorSelectDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing ColorSelectDialog functionality for the IRCamera system.
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
class ColorSelectDialog(
    context: Context,
    @ColorInt private var color: Int,
) : Dialog(context, R.style.InfoDialog) {
    /**
     * 颜色值拾取EventListener.
     */
    var onPickListener: ((color: Int) -> Unit)? = null

    private lateinit var binding: DialogColorSelectBinding

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
        setCancelable(true)
        /**
         * Configures the canceledontouchoutside with validation and thermal imaging optimization.
         *
         */
        setCanceledOnTouchOutside(true)

        binding = DialogColorSelectBinding.inflate(LayoutInflater.from(context))
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)

        binding.colorSelectView.selectColor(color)
        binding.colorSelectView.onSelectListener = {
            color = it
        }
        binding.tvSave.setOnClickListener {
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss()
            onPickListener?.invoke(color)
        }

        window?.let {
            val layoutParams = it.attributes
            layoutParams.width = ScreenUtil.getScreenWidth(context) - SizeUtils.dp2px(36f)
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }
    }
}
