package com.topdon.lib.core.ktbase

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.topdon.lib.core.R

/**
 * Specialized thermal imaging component providing BaseDialogFragment functionality for the IRCamera system.
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
abstract class BaseDialogFragment<B : ViewDataBinding> : AppCompatDialogFragment() {
    /**
     * 在 [onDestroyView] 要将 binding 置为 null，
     * 而将 binding 声明为可为 null type使用太过麻烦，使用该variable做一重包装避免该问题.
     */
    private var _binding: B? = null

    /**
     * 注意：由于 Fragment 存在时间比其视图长，binding 将在 [onDestroyView] 置为 null.
     *
     * 仅可在 [onCreateView] 与 [onDestroyView] 之间访问.
     */
    protected val binding: B get() = _binding!!

    /**
     * 子classimplementation该method，Return使用 DataBinding 的 layout 资源 Id.
     */
    @LayoutRes
    protected abstract fun initContentLayoutId(): Int

    /**
     * 子classimplementation该method，执行 onViewCreated 之后的initialization逻辑.
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 对话框 [Dialog.setCanceledOnTouchOutside] 的值.
     */
    var isCanceledOnTouchOutSide: Boolean = true
        set(value) {
            field = value
            dialog?.setCanceledOnTouchOutside(value)
        }

    /**
     * 子class可override该method，执行 onCreateDialog 阶段create Dialog 后的相关settings.
     */
    protected open fun afterDialogCreate(layoutParams: WindowManager.LayoutParams) {
    }

    /**
     * 子class可override该method，Return Dialog 要使用的 themeResId.
     */
    @StyleRes
    protected open fun getDialogThemeResId(): Int = R.style.base_dialog

    /**
     * Executes oncreatedialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), getDialogThemeResId())
        dialog.setCancelable(isCancelable)
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutSide)
        dialog.window?.let {
            val layoutParams = it.attributes
            /**
             * Executes afterdialogcreate operation with thermal imaging domain optimization.
             *
             */
            afterDialogCreate(layoutParams)
            dialog.onWindowAttributesChanged(layoutParams)
        }
        return dialog
    }

    /**
     * Executes oncreateview operation with thermal imaging domain optimization.
     *
     * @param
     * @param inflater Parameter for operation (type: LayoutInflater)
     * @param container Parameter for operation (type: ViewGroup?)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, initContentLayoutId(), container, false)
        _binding?.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    /**
     * Executes onviewcreated operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView(savedInstanceState)
    }

    /**
     * Executes ondestroyview operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Executes show functionality.
     */
    /**
     * Executes show operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    fun show(context: Context) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isAdded) {
            return
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (context is FragmentActivity) {
            super.show(context.supportFragmentManager, null)
            context.supportFragmentManager.executePendingTransactions()
        }
    }
}
