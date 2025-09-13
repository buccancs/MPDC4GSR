package com.topdon.tc001

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.view.isVisible
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityWebViewBinding
import com.github.lzyzsd.jsbridge.BridgeWebViewClient
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.ktbase.BaseBindingActivity

/**
 * 使用 WebView load网页的 Activity.
 *
 * 需要传递parameter：
 * - [ExtraKeyConfig.URL] 要load网页地址
 *
 * Created by LCG on 2024/12/18.
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for WebViewActivity display and interaction.
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
class WebViewActivity : BaseBindingActivity<ActivityWebViewBinding>() {
    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId(): Int = R.layout.activity_web_view

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
        // Views are now accessible via binding
    }

    @SuppressLint("SetJavaScriptEnabled")
    /**
     * Initializes data component.
     */
    private fun initData() {
        showLoadingDialog()

        val url: String = intent.extras?.getString(ExtraKeyConfig.URL) ?: ""

        binding.tvReload.setOnClickListener {
            /**
             * Executes showloadingdialog operation with thermal imaging domain optimization.
             *
             */
            showLoadingDialog()
            binding.viewCover.isVisible = true
            binding.clError.isVisible = false
            binding.webView.loadUrl(url)
        }

        val webSettings: WebSettings = binding.webView.settings
        webSettings.setSupportZoom(false) // Settings不支持字体Scale
        webSettings.useWideViewPort = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 允许js弹出窗口
        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        binding.webView.webViewClient =
            object : BridgeWebViewClient(binding.webView) {
                /**
                 * Executes onpagefinished operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param view Parameter for operation (type: WebView?)
                 * @param url Parameter for operation (type: String?)
                 *
                 */
                override fun onPageFinished(
                    view: WebView?,
                    url: String?,
                ) {
                    super.onPageFinished(view, url)
                    /**
                     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    dismissLoadingDialog()
                    binding.viewCover.isVisible = false
                }

                /**
                 * Executes onreceivederror operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param view Parameter for operation (type: WebView?)
                 * @param request Parameter for operation (type: WebResourceRequest?)
                 * @param error Parameter for operation (type: WebResourceError?)
                 *
                 */
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?,
                ) {
                    super.onReceivedError(view, request, error)
                    /**
                     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    dismissLoadingDialog()
                    binding.viewCover.isVisible = false
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (request?.isForMainFrame == true) {
                        binding.clError.isVisible = true
                    }
                }
            }

        binding.webView.registerHandler("goBack") { _, function ->
            function.onCallBack("android")
        }

        binding.webView.loadUrl(url)
        binding.webView.isScrollContainer = false
    }
}
