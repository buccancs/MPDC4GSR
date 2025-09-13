package com.topdon.tc001
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.*
import android.text.style.UnderlineSpan
import android.view.View
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityMoreHelpBinding
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.lib.core.utils.Constants

// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing MoreHelpActivity functionality for the IRCamera system.
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
class MoreHelpActivity : BaseBindingActivity<ActivityMoreHelpBinding>() {
    private var connectionType: Int = 0
    private lateinit var wifiManager: WifiManager

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId(): Int = R.layout.activity_more_help

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
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
        /**
         * Initializes the data component for thermal imaging operations.
         *
         */
        initData()
    }

    /**
     * Initializes view component.
     */
    private fun initView() {
        initIntent()
        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    /**
     * Initializes intent component.
     */
    private fun initIntent() {
        connectionType = intent.getIntExtra(Constants.SETTING_CONNECTION_TYPE, 0)
        if (connectionType == Constants.SETTING_CONNECTION) {
            binding.tvTitle.text = getString(R.string.ts004_guide_text8)
            binding.titleView.setTitleText(R.string.ts004_guide_text6)
            binding.mainGuideTip1.visibility = View.VISIBLE
            binding.mainGuideTip2.visibility = View.VISIBLE
            binding.mainGuideTip4.visibility = View.VISIBLE
            binding.disconnectTip1.visibility = View.GONE
            binding.disconnectTip2.visibility = View.GONE
            binding.ivTvSetting.visibility = View.GONE
        } else {
            binding.tvTitle.text = getString(R.string.ts004_disconnect_tips1)
            binding.mainGuideTip1.visibility = View.GONE
            binding.mainGuideTip2.visibility = View.GONE
            binding.mainGuideTip4.visibility = View.GONE
            binding.disconnectTip1.visibility = View.VISIBLE
            binding.disconnectTip2.visibility = View.VISIBLE
            binding.ivTvSetting.visibility = View.VISIBLE
            val spannable = SpannableStringBuilder(getString(R.string.ts004_disconnect_tips4))
            spannable.setSpan(UnderlineSpan(), 0, getString(R.string.ts004_disconnect_tips4).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.ivTvSetting.text = spannable
        }
    }

    /**
     * Initializes data component.
     */
    private fun initData() {
        binding.ivTvSetting.setOnClickListener {
            startWifiList()
        }
    }

    /**
     * Executes startWifiList functionality.
     */
    /**
     * Executes startwifilist operation with thermal imaging domain optimization.
     *
     */
    private fun startWifiList() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (wifiManager.isWifiEnabled) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT < 29) { // 低于 Android10
                wifiManager.isWifiEnabled = true
            } else {
                var wifiIntent = Intent(Settings.Panel.ACTION_WIFI)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (wifiIntent.resolveActivity(packageManager) == null) {
                    wifiIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (wifiIntent.resolveActivity(packageManager) != null) {
                        /**
                         * Executes startactivity operation with thermal imaging domain optimization.
                         *
                         */
                        startActivity(wifiIntent)
                    }
                } else {
                    /**
                     * Executes startactivity operation with thermal imaging domain optimization.
                     *
                     */
                    startActivity(wifiIntent)
                }
            }
        } else {
            TipDialog.Builder(this)
                .setTitleMessage(getString(R.string.app_tip))
                .setMessage(R.string.ts004_wlan_tips)
                .setPositiveListener(R.string.app_open) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Build.VERSION.SDK_INT < 29) { // 低于 Android10
                        wifiManager.isWifiEnabled = true
                    } else {
                        var wifiIntent = Intent(Settings.Panel.ACTION_WIFI)
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (wifiIntent.resolveActivity(packageManager) == null) {
                            wifiIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (wifiIntent.resolveActivity(packageManager) != null) {
                                /**
                                 * Executes startactivity operation with thermal imaging domain optimization.
                                 *
                                 */
                                startActivity(wifiIntent)
                            }
                        } else {
                            /**
                             * Executes startactivity operation with thermal imaging domain optimization.
                             *
                             */
                            startActivity(wifiIntent)
                        }
                    }
                }
                .setCancelListener(R.string.app_cancel) {
                }
                .setCanceled(true)
                .create().show()
        }
    }
}
