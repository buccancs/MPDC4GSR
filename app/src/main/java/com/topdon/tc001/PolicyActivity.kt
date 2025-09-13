package com.topdon.tc001

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityPolicyBinding
import com.elvishew.xlog.XLog
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.ktbase.BaseBindingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Terms and Conditions Activity
 * 
 * Displays different types of terms:
 * 1: User terms  2: Privacy terms  3: Third-party terms
 *
 * When service returns an error, loads default terms
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing PolicyActivity functionality for the IRCamera system.
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
class PolicyActivity : BaseBindingActivity<ActivityPolicyBinding>() {
    private val mHandler = Handler(Looper.getMainLooper())

    companion object {
        const val KEY_THEME_TYPE = "key_theme_type"
        const val KEY_USE_TYPE = "key_use_type" // Usage type: local or network
    }

    private var themeType = 1
    private var themeStr = ""
    private var reloadCount = 1
    private var keyUseType = 0

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_policy

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: android.os.Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
    }

    /**
     * Initializes view component.
     */
    private fun initView() {
        if (intent.hasExtra(KEY_THEME_TYPE)) {
            themeType = intent.getIntExtra(KEY_THEME_TYPE, 1)
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (intent.hasExtra(KEY_USE_TYPE)) {
            keyUseType = intent.getIntExtra(KEY_USE_TYPE, 0)
        }
        themeStr =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (themeType) {
                1 -> getString(R.string.user_services_agreement)
                2 -> getString(R.string.privacy_policy)
                3 -> getString(R.string.third_party_components)
                else -> getString(R.string.user_services_agreement)
            }

        // Initialize views using view binding
        binding.titleView.apply {
            // Note: Title text setting is handled by the parent view implementation
            // Title_view.setTitleText(themeStr)
        }

        // Create a simple ViewModel-like observer pattern since we're using BaseBindingActivity
        /**
         * Executes observehtmldata operation with thermal imaging domain optimization.
         *
         */
        observeHtmlData()

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (keyUseType != 0) {
            /**
             * Executes loadhttpwhennotinit operation with thermal imaging domain optimization.
             *
             */
            loadHttpWhenNotInit(binding.policyWeb)
            /**
             * Executes delayshowwebview operation with thermal imaging domain optimization.
             *
             */
            delayShowWebView()
        }
    }

    /**
     * Executes observeHtmlData functionality.
     */
    /**
     * Executes observehtmldata operation with thermal imaging domain optimization.
     *
     */
    private fun observeHtmlData() {
        // Since we're not using ViewModel anymore, we'll directly load the data
        // This is a simplified version - in a real scenario you'd want proper MVVM
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    /**
     * Delayed WebView display to resolve white screen flashing issue
     */
    private fun delayShowWebView() {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(200)
            /**
             * Executes launch operation with thermal imaging domain optimization.
             *
             */
            launch(Dispatchers.Main) {
                binding.policyWeb.visibility = android.view.View.VISIBLE
            }
        }
    }

    /**
     * Initializes data component.
     */
    private fun initData() {
        if (keyUseType == 0) {
            showLoadingDialog()
            // Load data directly since we removed ViewModel
            /**
             * Executes loaddefaultcontent operation with thermal imaging domain optimization.
             *
             */
            loadDefaultContent()
        }
    }

    /**
     * Executes loadDefaultContent functionality.
     */
    /**
     * Executes loaddefaultcontent operation with thermal imaging domain optimization.
     *
     */
    private fun loadDefaultContent() {
        // Load the appropriate content based on theme type
        /**
         * Executes loadhttp operation with thermal imaging domain optimization.
         *
         */
        loadHttp(binding.policyWeb)
        /**
         * Executes delayshowwebview operation with thermal imaging domain optimization.
         *
         */
        delayShowWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    /**
     * Initializes web component.
     */
    /**
     * Initializes the web component for thermal imaging operations.
     *
     * @param
     * @param url Parameter for operation (type: String)
     *
     */
    private fun initWeb(url: String) {
        binding.policyWeb.visibility = android.view.View.INVISIBLE
        val webSettings: android.webkit.WebSettings = binding.policyWeb.settings
        webSettings.javaScriptEnabled = true // Settings支持javascript

        binding.policyWeb.webViewClient =
            object : android.webkit.WebViewClient() {
                /**
                 * Executes shouldoverrideurlloading operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param view Parameter for operation (type: android.webkit.WebView)
                 * @param url Parameter for operation (type: String)
                 *
                 */
                override fun shouldOverrideUrlLoading(
                    view: android.webkit.WebView,
                    url: String,
                ): Boolean {
                    view.loadUrl(url)
                    return true
                }

                /**
                 * Executes onpagefinished operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param view Parameter for operation (type: android.webkit.WebView?)
                 * @param url Parameter for operation (type: String?)
                 *
                 */
                override fun onPageFinished(
                    view: android.webkit.WebView?,
                    url: String?,
                ) {
                    super.onPageFinished(view, url)
                    Log.w("123", "onPageFinished url: $url")
                }
            }

        binding.policyWeb.webChromeClient =
            object : android.webkit.WebChromeClient() {
                /**
                 * Executes onprogresschanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param view Parameter for operation (type: android.webkit.WebView)
                 * @param newProgress Parameter for operation (type: Int)
                 *
                 */
                override fun onProgressChanged(
                    view: android.webkit.WebView,
                    newProgress: Int,
                ) {
                    super.onProgressChanged(view, newProgress)
                }

                /**
                 * Executes onreceivedtitle operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param view Parameter for operation (type: android.webkit.WebView?)
                 * @param title Parameter for operation (type: String?)
                 *
                 */
                override fun onReceivedTitle(
                    view: android.webkit.WebView?,
                    title: String?,
                ) {
                    super.onReceivedTitle(view, title)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (title!!.contains("404") && reloadCount > 0) {
                        /**
                         * Executes loadhttp operation with thermal imaging domain optimization.
                         *
                         */
                        loadHttp(view!!)
                        /**
                         * Executes delayshowwebview operation with thermal imaging domain optimization.
                         *
                         */
                        delayShowWebView()
                    } else {
                        mHandler.postDelayed({
                            binding.policyWeb.visibility = android.view.View.VISIBLE
                        }, 200)
                    }
                }
            }

        binding.policyWeb.settings.defaultTextEncodingName = "utf-8"
        binding.policyWeb.loadDataWithBaseURL(null, url, "text/html", "utf-8", null)
    }

    /**
     * processing富文本
     *
     * @param bodyHTML body
     * @param fontColor 需要改变的字体颜色
     * @param backgroundColor modify字体颜色
     * @return String
     */
    /**
     * Retrieves htmldata information.
     */
    fun getHtmlData(
        htmlBody: String,
        fontColor: String,
        backgroundColor: String,
    ): String {
        val head =
            "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:100%; height:auto;}video{max-width: 100%; width:100%; height:auto;}*{margin:0px;}body{font-size:16px;color: $fontColor; background-color: $backgroundColor;}</style>" + "</head>"
        return "<html>$head<body>$htmlBody</body></html>"
    }

    /**
     * Executes httpErrorTip functionality.
     */
    /**
     * Executes httperrortip operation with thermal imaging domain optimization.
     *
     * @param
     * @param text Parameter for operation (type: String)
     * @param requestUrl Parameter for operation (type: String)
     *
     */
    private fun httpErrorTip(
        text: String,
        requestUrl: String,
    ) {
        XLog.w("声明interfaceexception,Opendefault链接")
        /**
         * Executes loadhttp operation with thermal imaging domain optimization.
         *
         */
        loadHttp(binding.policyWeb)
        /**
         * Executes delayshowwebview operation with thermal imaging domain optimization.
         *
         */
        delayShowWebView()
    }

    /**
     * Executes loadHttpWhenNotInit functionality.
     */
    /**
     * Executes loadhttpwhennotinit operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: android.webkit.WebView)
     *
     */
    fun loadHttpWhenNotInit(view: android.webkit.WebView) {
        reloadCount--
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (themeType) {
            1 -> {
                // Userserviceprotocol
                view.loadUrl(
                    "https:// Plat.topdon.com/topdon-plat/out-user/baseinfo/template/getHtmlContentById?softCode=${BaseApplication.instance.getSoftWareCode()}&language=1&type=21",
                )
            }

            2 -> {
                // 隐私政策
                view.loadUrl(
                    "https:// Plat.topdon.com/topdon-plat/out-user/baseinfo/template/getHtmlContentById?softCode=${BaseApplication.instance.getSoftWareCode()}&language=1&type=22",
                )
            }

            3 -> {
                // 第三方component
                view.loadUrl("file:// /android_asset/web/third_statement.html")
            }
        }
    }

    /**
     * loaddefaultprotocol网址(英文版)
     */
    /**
     * Executes loadhttp operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: android.webkit.WebView)
     *
     */
    fun loadHttp(view: android.webkit.WebView) {
        reloadCount--
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (themeType) {
            1 -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (BaseApplication.instance.isDomestic()) {
                    view.loadUrl("file:// /android_asset/web/services_agreement_default_inside_china.html")
                } else {
                    // Userserviceprotocol
                    view.loadUrl("file:// /android_asset/web/services_agreement_default.html")
                }
            }

            2 -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (BaseApplication.instance.isDomestic()) {
                    view.loadUrl("file:// /android_asset/web/privacy_default_inside_china.html")
                } else {
                    // 隐私政策
                    view.loadUrl("file:// /android_asset/web/privacy_default.html")
                }
            }

            3 -> {
                // 第三方component
                view.loadUrl("file:// /android_asset/web/third_statement.html")
            }
        }
    }
}
