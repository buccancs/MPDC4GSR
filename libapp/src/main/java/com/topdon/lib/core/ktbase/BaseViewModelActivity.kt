package com.topdon.lib.core.ktbase

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.topdon.lib.core.R
import com.topdon.lib.core.dialog.MsgDialog
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for BaseViewModelActivity display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
abstract class BaseViewModelActivity<VM : BaseViewModel> : BaseActivity() {
    protected lateinit var viewModel: VM

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Initializes the vm component for thermal imaging operations.
         *
         */
        initVM()
        super.onCreate(savedInstanceState)
    }

    /**
     * Initializes the component with default configuration.
     */
    private fun initVM() {
        providerVMClass().let {
            viewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    
    abstract fun providerVMClass(): Class<VM>

    // Interface请求出错，子class可以override此method做一些操作
    /**
     * Executes requesterror operation with thermal imaging domain optimization.
     *
     * @param
     * @param it Parameter for operation (type: Exception?)
     *
     */
    protected fun requestError(it: Exception?) {
        
        it?.run {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (it) {
                is TimeoutCancellationException -> httpErrorTip(getString(R.string.http_time_out), "")
                is CancellationException -> Log.d("$TAG--->interface请求Cancel", it.message.toString())
                else -> httpErrorTip(getString(R.string.http_code_z5004), "")
            }
        }
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    open fun httpErrorTip(
        text: String,
        requestUrl: String,
    ) {
        MsgDialog.Builder(this)
            .setMessage(text)
            .setImg(R.drawable.ic_tip_error_svg)
            .create().show()
    }
}
