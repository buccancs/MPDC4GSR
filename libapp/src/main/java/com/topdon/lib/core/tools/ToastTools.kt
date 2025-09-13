package com.topdon.lib.core.tools

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.Utils
import com.topdon.lib.core.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Specialized thermal imaging component providing ToastTools functionality for the IRCamera system.
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
object ToastTools {
    var mPublicToast: Toast? = null

    /**
     * Executes showShort functionality.
     */
    /**
     * Executes showshort operation with thermal imaging domain optimization.
     *
     * @param
     * @param textStr Parameter for operation (type: Int)
     *
     */
    fun showShort(
        @StringRes textStr: Int,
    ) {
        /**
         * Executes showshort operation with thermal imaging domain optimization.
         *
         */
        showShort(Utils.getApp().getString(textStr))
    }

    /**
     * Executes showShort functionality.
     */
    /**
     * Executes showshort operation with thermal imaging domain optimization.
     *
     * @param
     * @param textStr Parameter for operation (type: String)
     *
     */
    fun showShort(textStr: String) {
        /**
         * Executes showshort operation with thermal imaging domain optimization.
         *
         */
        showShort(textStr, Toast.LENGTH_SHORT)
    }

    /**
     * Executes showShort functionality.
     */
    /**
     * Executes showshort operation with thermal imaging domain optimization.
     *
     * @param
     * @param textStr Parameter for operation (type: String)
     * @param duration Duration in milliseconds (type: Int)
     *
     */
    fun showShort(
        textStr: String,
        duration: Int,
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val inflater =
                Utils.getApp().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.toast_tip, null)
            val text = view.findViewById(R.id.toast_tip_text) as TextView
            text.text = textStr
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Use simple text toast for Android 11+ (API 30+) since custom views are deprecated
                mPublicToast = Toast.makeText(Utils.getApp(), textStr, duration)
                mPublicToast?.setGravity(Gravity.BOTTOM, 0, ScreenUtils.getScreenHeight() / 8)
            } else {
                // Use custom view for older Android versions
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mPublicToast == null) {
                    mPublicToast = Toast(Utils.getApp())
                }
                mPublicToast?.duration = duration
                mPublicToast?.setGravity(Gravity.BOTTOM, 0, ScreenUtils.getScreenHeight() / 8)
                @Suppress("DEPRECATION")
                mPublicToast?.view = view
            }
            mPublicToast?.show()
        }
    }
}
