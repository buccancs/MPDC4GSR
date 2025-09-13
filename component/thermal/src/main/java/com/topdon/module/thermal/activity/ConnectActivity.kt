package com.topdon.module.thermal.activity

import android.widget.TextView
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.DeviceTools
import com.topdon.module.thermal.R

connectiondevice
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing ConnectActivity functionality for the IRCamera system.
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
class ConnectActivity : BaseActivity() {
    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_connect

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Set toolbar title
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(com.topdon.lib.core.R.id.toolbar_lay)
        toolbar?.title = getString(R.string.app_name)

        val bluetoothBtn = findViewById<TextView>(R.id.bluetooth_btn)
        val isDeviceConnected = DeviceTools.isConnect()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isDeviceConnected) {
未connection
            bluetoothBtn.text = getString(R.string.app_no_connect)
        } else {
已connection
            bluetoothBtn.text = getString(R.string.app_connect)
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }
}
