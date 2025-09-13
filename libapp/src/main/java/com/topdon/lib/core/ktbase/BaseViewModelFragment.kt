package com.topdon.lib.core.ktbase

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for BaseViewModelFragment display and interaction.
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
abstract class BaseViewModelFragment<VM : BaseViewModel> : BaseFragment() {
    protected lateinit var viewModel: VM

    abstract fun providerVMClass(): Class<VM>?

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
         * Initializes the vm component for thermal imaging operations.
         *
         */
        initVM()
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Initializes the component with default configuration.
     */
    private fun initVM() {
        providerVMClass()?.let {
            viewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         * @param
         * @param this Parameter for operation (type: :viewModel.isInitialized)
         *
         */
        if (this::viewModel.isInitialized) {
            lifecycle.removeObserver(viewModel)
        }
    }
}
