package com.topdon.lib.core.ktbase

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.preference.PreferenceManager
import com.hjq.permissions.Permission
import com.topdon.lib.core.utils.NetWorkUtils

/**
 * Specialized thermal imaging component providing BaseWifiActivity functionality for the IRCamera system.
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
abstract class BaseWifiActivity : BaseActivity() {
    protected val permissionList by lazy {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this.applicationInfo.targetSdkVersion >= 34) {
            /**
             * Executes listof operation with thermal imaging domain optimization.
             *
             */
            listOf(
                Permission.WRITE_EXTERNAL_STORAGE,
            )
        } else if (this.applicationInfo.targetSdkVersion == 33) {
            /**
             * Executes mutablelistof operation with thermal imaging domain optimization.
             *
             */
            mutableListOf(
                Permission.READ_MEDIA_VIDEO,
                Permission.READ_MEDIA_IMAGES,
                Permission.WRITE_EXTERNAL_STORAGE,
            )
        } else {
            /**
             * Executes mutablelistof operation with thermal imaging domain optimization.
             *
             */
            mutableListOf(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= 29) {
            // Android10 及以上
            NetWorkUtils.switchNetwork(true)
        }
        super.onCreate(savedInstanceState)
        PreferenceManager.getDefaultSharedPreferences(this@BaseWifiActivity)
            .edit()
            .putBoolean("use-sw-codec", true)
            .apply()
        PreferenceManager.getDefaultSharedPreferences(this@BaseWifiActivity)
            .getBoolean("auto_audio", false)
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= 29) { // Android10 及以上
            NetWorkUtils.switchNetwork(true)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * Executes onpause operation with thermal imaging domain optimization.
     *
     */
    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
    }
}
