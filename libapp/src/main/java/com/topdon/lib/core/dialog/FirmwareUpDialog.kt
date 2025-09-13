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
 * firmware升级有新versiontip弹框.
 * Created by LCG on 2024/3/4.
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
        get() = binding.tvSize.text
        set(value) {
            binding.tvSize.text = value
        }

    /**
     * 升级内容，一般直接扔从interface拿到的东西
     */
    var contentStr: CharSequence?
        get() = binding.tvContent.text
        set(value) {
            binding.tvContent.text = value
        }

    /**
     * 是否显示底部device重启tip，目前仅firmware升级需要显示，默认隐藏(Gone).
     */
    var isShowRestartTips: Boolean
        get() = binding.tvRestartTips.isVisible
        set(value) {
            binding.tvRestartTips.isVisible = value
        }

    /**
     * 是否显示取消button，默认显示.
     */
    var isShowCancel: Boolean
        get() = binding.tvCancel.isVisible
        set(value) {
            binding.tvCancel.isVisible = value
        }

    /**
     * 取消click事件监听.
     */
    var onCancelClickListener: (() -> Unit)? = null

    /**
     * updateclick事件监听.
     */
    var onConfirmClickListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DialogFirmwareUpBinding.inflate(LayoutInflater.from(context))
        setCancelable(false)
        setCanceledOnTouchOutside(false)
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

    override fun onClick(v: View?) {
        when (v) {
            binding.tvCancel -> { // 取消
                dismiss()
                onCancelClickListener?.invoke()
            }
            binding.tvConfirm -> { // 确认
                dismiss()
                onConfirmClickListener?.invoke()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}
