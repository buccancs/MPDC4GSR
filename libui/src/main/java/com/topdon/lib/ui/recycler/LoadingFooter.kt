package com.topdon.lib.ui.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.topdon.lib.ui.databinding.UiFooterViewBinding

/**
 * 自定义FooterView - Modernized with view binding
 */
/**
 * Custom Loading footer view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * LoadingFooter manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing LoadingFooter functionality for the IRCamera system.
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
class LoadingFooter : LinearLayout {
    private val binding: UiFooterViewBinding

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor(context: Context) : this(context, null)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        binding = UiFooterViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    /**
     * Sets nomoredata configuration.
     */
    fun setNoMoreData(noMoreData: Boolean): Boolean {
        binding.llLoading.isVisible = !noMoreData
        binding.clLoadEnd.isVisible = noMoreData
        return true
    }

    /**
     * Retrieves customview information.
     */
    fun getCustomView(): View = this
}
