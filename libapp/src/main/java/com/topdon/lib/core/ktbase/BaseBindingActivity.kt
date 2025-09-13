package com.topdon.lib.core.ktbase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.topdon.lib.core.R
import com.topdon.lib.core.bean.event.SocketStateEvent
import com.topdon.lib.core.bean.event.device.DeviceConnectEvent
import com.topdon.lib.core.dialog.LoadingDialog
import com.topdon.lib.core.tools.AppLanguageUtils
import com.topdon.lib.core.tools.ConstantLanguages
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * 使用 DataBinding 的基础 Activity.
 *
 * 由于 BaseActivity 子class实在太多没法一下子全改完，
 * 所以 BaseActivity 里的逻辑改a搬a吧，等全部改完再来Optimizeinheritance.
 *
 * Created by LCG on 2024/10/14.
 */
/**
 * Specialized thermal imaging component providing BaseBindingActivity functionality for the IRCamera system.
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
abstract class BaseBindingActivity<B : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding: B

    /**
     * 子classimplementation该method，Return使用 DataBinding 的 layout 资源 Id.
     */
    @LayoutRes
    protected abstract fun initContentLayoutId(): Int

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, initContentLayoutId())
        binding.lifecycleOwner = this
        binding.executePendingBindings()

        EventBus.getDefault().register(this)
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    /**
     * Executes attachbasecontext operation with thermal imaging domain optimization.
     *
     * @param
     * @param newBase Parameter for operation (type: Context?)
     *
     */
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(AppLanguageUtils.attachBaseContext(newBase, ConstantLanguages.ENGLISH))
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
     * @param resId Parameter for operation (type: Int = R.string.tip_loading)
     *
     */
    fun showLoadingDialog(
        @StringRes resId: Int = R.string.tip_loading,
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
            loadingDialog = LoadingDialog(this)
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

    protected class TakePhotoResult : ActivityResultContract<File, File?>() {
        private lateinit var file: File

        /**
         * Executes createintent operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param input Parameter for operation (type: File)
         *
         */
        override fun createIntent(
            context: Context,
            input: File,
        ): Intent {
            file = input
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        /**
         * Executes parseresult operation with thermal imaging domain optimization.
         *
         * @param
         * @param resultCode Parameter for operation (type: Int)
         * @param intent Parameter for operation (type: Intent?)
         *
         */
        override fun parseResult(
            resultCode: Int,
            intent: Intent?,
        ): File? = if (resultCode == RESULT_OK) file else null
    }
}
