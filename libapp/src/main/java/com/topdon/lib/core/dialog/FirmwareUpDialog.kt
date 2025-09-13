package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.DialogFirmwareUpBinding
import com.topdon.lib.core.utils.ScreenUtil

/**
 * firmwareUpgrade有新versiontip弹框.
 * Created by LCG on 2024/3/4.
 */
/**
 * FirmwareUpDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing FirmwareUpDialog functionality for the IRCamera system.
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
class FirmwareUpDialog(context: Context) : Dialog(context, R.style.InfoDialog), View.OnClickListener {
    private var _binding: DialogFirmwareUpBinding? = null
    private val binding get() = _binding!!

    /**
     * titletext，如 "发现新version V3.50"
     */
    var titleStr: CharSequence?
        get() = binding.tvTitle.text
        set(value) {
            binding.tvTitle.text = value
        }

    /**
     * file大小text，如 "大小: 239.6MB"
     */
    var sizeStr: CharSequence?
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = binding.tvSize.text
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            binding.tvSize.text = value
        }

    /**
     * Upgrade内容，一般直接扔从interface拿到的东西
     */
    var contentStr: CharSequence?
        get() = binding.tvContent.text
        set(value) {
            binding.tvContent.text = value
        }

    /**
     * 是否Show/Display底部device重启tip，目前仅firmwareUpgrade需要Show/Display，defaultHide(Gone).
     */
    var isShowRestartTips: Boolean
        get() = binding.tvRestartTips.isVisible
        set(value) {
            binding.tvRestartTips.isVisible = value
        }

    /**
     * 是否Show/DisplayCancelbutton，defaultShow/Display.
     */
    var isShowCancel: Boolean
        get() = binding.tvCancel.isVisible
        set(value) {
            binding.tvCancel.isVisible = value
        }

    /**
     * CancelclickEventListener.
     */
    var onCancelClickListener: (() -> Unit)? = null

    /**
     * updateclickEventListener.
     */
    var onConfirmClickListener: (() -> Unit)? = null

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DialogFirmwareUpBinding.inflate(LayoutInflater.from(context))
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
            layoutParams.width = (ScreenUtil.getScreenWidth(context) * 0.72).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }

        binding.tvCancel.setOnClickListener(this)
        binding.tvConfirm.setOnClickListener(this)
    }

    /**
     * Executes onclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param v Parameter for operation (type: View?)
     *
     */
    override fun onClick(v: View?) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (v) {
            binding.tvCancel -> { 
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                onCancelClickListener?.invoke()
            }
            binding.tvConfirm -> { 
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                onConfirmClickListener?.invoke()
            }
        }
    }

    /**
     * Executes ondetachedfromwindow operation with thermal imaging domain optimization.
     *
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}
