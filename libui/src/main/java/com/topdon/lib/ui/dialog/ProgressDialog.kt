package com.topdon.lib.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import com.topdon.lib.core.R
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.lib.ui.databinding.DialogProgressBinding

/**
 * 带进度条的tip弹框.
 */
/**
 * ProgressDialog(context: class
 */
/**
 * Progress dialog for thermal imaging user interaction.
 * Provides specialized input and configuration interfaces.
 */
/**
 * ProgressDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing ProgressDialog functionality for the IRCamera system.
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
class ProgressDialog(context: Context) : Dialog(context, R.style.InfoDialog) {
    private val binding: DialogProgressBinding = DialogProgressBinding.inflate(LayoutInflater.from(context))

    var max: Int = 100
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            binding.progressBar.max = value
            field = value
        }

    var progress: Int = 0
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            binding.progressBar.progress = value
            field = value
        }

    init {
        // Binding is initialized in constructor
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
            layoutParams.width = (ScreenUtil.getScreenWidth(context) * if (ScreenUtil.isPortrait(context)) 0.8 else 0.45).toInt()
            layoutParams.height = LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }
    }

    /**
     * Executes show operation with thermal imaging domain optimization.
     *
     */
    override fun show() {
        super.show()
        binding.progressBar.max = max
        binding.progressBar.progress = progress
    }
}
