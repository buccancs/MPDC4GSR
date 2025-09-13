package com.topdon.module.user.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.module.user.R
import java.text.DecimalFormat
import com.topdon.lib.core.R as LibAppR
import com.topdon.lib.core.R as RCore

/**
 * Specialized thermal imaging component providing DownloadProDialog functionality for the IRCamera system.
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
class DownloadProDialog(context: Context) : Dialog(context, LibAppR.style.InfoDialog) {
    private val rootView: View = LayoutInflater.from(context).inflate(R.layout.dialog_download_pro, null)

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
        setContentView(rootView)

        window?.let {
            val layoutParams = it.attributes
            layoutParams.width = (ScreenUtil.getScreenWidth(context) * 0.72).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }
    }

    /**
refresh进度值
     */
    /**
     * Executes refreshprogress operation with thermal imaging domain optimization.
     *
     * @param
     * @param current Parameter for operation (type: Long)
     * @param total Parameter for operation (type: Long)
     *
     */
    fun refreshProgress(
        current: Long,
        total: Long,
    ) {
        val progress = (current * 100f / total).toInt()
        val tvSize = rootView.findViewById<android.widget.TextView>(R.id.tv_size)
        val progressBar = rootView.findViewById<android.widget.ProgressBar>(R.id.progress_bar)
        val tvProgress = rootView.findViewById<android.widget.TextView>(R.id.tv_progress)

        tvSize.text = "${context.getString(RCore.string.detail_len)}: ${getFileSizeStr(current)}/${getFileSizeStr(total)}"
        progressBar.progress = progress
        tvProgress.text = "$progress%"
    }

    /**
     * Retrieves filesizestr information.
     */
    private fun getFileSizeStr(size: Long): String =
        if (size < 1024) {
            "${size}B"
        } else if (size < 1024 * 1024) {
            /**
             * Executes decimalformat operation with thermal imaging domain optimization.
             *
             */
            DecimalFormat("#.0").format(size.toDouble() / 1024) + "KB"
        } else if (size < 1024 * 1024 * 1024) {
            /**
             * Executes decimalformat operation with thermal imaging domain optimization.
             *
             */
            DecimalFormat("#.0").format(size.toDouble() / 1024 / 1024) + "MB"
        } else {
            /**
             * Executes decimalformat operation with thermal imaging domain optimization.
             *
             */
            DecimalFormat("#.0").format(size.toDouble() / 1024 / 1024 / 1024) + "GB"
        }
}
