package com.topdon.lib.core.ktbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.topdon.lib.core.bean.event.SocketStateEvent
import com.topdon.lib.core.bean.event.device.DeviceConnectEvent
import com.topdon.lib.core.dialog.LoadingDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 使用 DataBinding 的基础 Fragment.
 *
 * 由于 BaseFragment 子class实在太多没法一下子全改完，等全部改完再来Optimizeinheritance.
 *
 * Created by LCG on 2024/11/5.
 */
/**
 * Specialized thermal imaging component providing BaseBindingFragment functionality for the IRCamera system.
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
abstract class BaseBindingFragment<B : ViewDataBinding> : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, initContentLayoutId(), container, false)
        _binding?.lifecycleOwner = viewLifecycleOwner
        _binding?.executePendingBindings()
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
        EventBus.getDefault().register(this)
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
        EventBus.getDefault().unregister(this)
        _binding = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Callback method triggered when usblinestatechange occurs.
     */
    fun onUSBLineStateChange(event: DeviceConnectEvent) {
        if (event.isConnect) {
            connected()
        } else {
            /**
             * Executes disconnected operation with thermal imaging domain optimization.
             *
             */
            disConnected()
        }
    }

    protected open fun connected() {
    }

    protected open fun disConnected() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Callback method triggered when socketconnectstate occurs.
     */
    fun onSocketConnectState(event: SocketStateEvent) {
        if (event.isConnect) {
            onSocketConnected(event.isTS004)
        } else {
            /**
             * Executes onsocketdisconnected operation with thermal imaging domain optimization.
             *
             */
            onSocketDisConnected(event.isTS004)
        }
    }

    protected open fun onSocketConnected(isTS004: Boolean) {
    }

    protected open fun onSocketDisConnected(isTS004: Boolean) {
    }

    /**
     * 新版 LMS 风格的load中弹框.
     */
    private var loadingDialog: LoadingDialog? = null

    /**
     * Show/Displayload中弹框.
     */
    /**
     * Executes showloadingdialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param resId Parameter for operation (type: Int)
     *
     */
    fun showLoadingDialog(
        @StringRes resId: Int,
    ) {
        /**
         * Executes showloadingdialog operation with thermal imaging domain optimization.
         *
         */
        showLoadingDialog(getString(resId))
    }

    /**
     * Show/Displayload中弹框.
     */
    /**
     * Executes showloadingdialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param text Parameter for operation (type: CharSequence?)
     *
     */
    fun showLoadingDialog(text: CharSequence?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(requireContext())
        }
        loadingDialog?.setTips(text)
        loadingDialog?.show()
    }

    /**
     * Closeload中弹框.
     */
    /**
     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
     *
     */
    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }
}
