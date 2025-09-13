package com.topdon.tc001

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.lifecycle.lifecycleScope
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityClauseBinding
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.dialog.TipProgressDialog
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.utils.CommUtils
import com.topdon.lms.sdk.utils.NetworkUtil
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.tc001.app.App
import com.topdon.tc001.utils.VersionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import com.topdon.lib.core.R as LibCoreR

/**
/**
 * Specialized thermal imaging component providing ClauseActivity functionality for the IRCamera system.
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
class ClauseActivity : BaseBindingActivity<ActivityClauseBinding>() {
    private lateinit var dialog: TipProgressDialog

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_clause

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
    }

    /**
     * Initializes view component.
     */
    private fun initView() {
        dialog =
            TipProgressDialog.Builder(this)
                .setMessage(LibCoreR.string.tip_loading)
                .setCanceleable(false)
                .create()

        val year = Calendar.getInstance().get(Calendar.YEAR)
        binding.clauseYearTxt.text = getString(R.string.version_year, "2023-$year")

        binding.clauseAgreeBtn.setOnClickListener {
            /**
             * Executes confirminitapp operation with thermal imaging domain optimization.
             *
             */
            confirmInitApp()
        }
        binding.clauseDisagreeBtn.setOnClickListener {
            // 再次弹框Confirm是否Exit
            TipDialog.Builder(this)
                .setMessage(getString(R.string.privacy_tips))
                .setPositiveListener(R.string.privacy_confirm) {
                    /**
                     * Executes confirminitapp operation with thermal imaging domain optimization.
                     *
                     */
                    confirmInitApp()
                }
                .setCancelListener(R.string.privacy_cancel) {
                    this.finish()
                }
                .setCanceled(true)
                .create().show()
        }
        val keyUseType = if (BaseApplication.instance.isDomestic()) 1 else 0
        binding.clauseItem.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!NetworkUtil.isConnected(this)) {
                TToast.shortToast(this, R.string.lms_setting_http_error)
            } else {
                // Service条款
                NavigationManager.getInstance()
                    .build(RouterConfig.POLICY)
                    .withInt(PolicyActivity.KEY_THEME_TYPE, 1)
                    .withInt(PolicyActivity.KEY_USE_TYPE, keyUseType)
                    .navigation(this)
            }
        }
        binding.clauseItem2.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!NetworkUtil.isConnected(this)) {
                TToast.shortToast(this, R.string.lms_setting_http_error)
            } else {
                // 隐私条款
                NavigationManager.getInstance()
                    .build(RouterConfig.POLICY)
                    .withInt(PolicyActivity.KEY_THEME_TYPE, 2)
                    .withInt(PolicyActivity.KEY_USE_TYPE, keyUseType)
                    .navigation(this)
            }
        }
        binding.clauseItem3.setOnClickListener {
            // 第三方
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!NetworkUtil.isConnected(this)) {
                TToast.shortToast(this, R.string.lms_setting_http_error)
            } else {
                NavigationManager.getInstance()
                    .build(RouterConfig.POLICY)
                    .withInt(PolicyActivity.KEY_THEME_TYPE, 3)
                    .withInt(PolicyActivity.KEY_USE_TYPE, keyUseType)
                    .navigation(this)
            }
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (BaseApplication.instance.isDomestic()) {
            binding.tvPrivacy.text = "    ${getString(R.string.privacy_agreement_tips_new, CommUtils.getAppName())}"
            binding.tvPrivacy.visibility = android.view.View.VISIBLE
            binding.tvPrivacy.movementMethod = ScrollingMovementMethod.getInstance()
        }
        binding.tvWelcome.text = getString(R.string.welcome_use_app, CommUtils.getAppName())
        binding.tvVersion.text = "${getString(R.string.set_version)}V${VersionUtils.getCodeStr(this)}"
        binding.clauseName.text = CommUtils.getAppName()
    }

    /**
     * Executes confirmInitApp functionality.
     */
    /**
     * Executes confirminitapp operation with thermal imaging domain optimization.
     *
     */
    private fun confirmInitApp() {
        lifecycleScope.launch {
            /**
             * Executes showloading operation with thermal imaging domain optimization.
             *
             */
            showLoading()
            // Initialization
            App.delayInit()
            /**
             * Executes async operation with thermal imaging domain optimization.
             *
             */
            async(Dispatchers.IO) {
                // 等待1000ms initializationend
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(1000)
                return@async
            }.await().let {
                NavigationManager.build(RouterConfig.MAIN).navigation(this@ClauseActivity)
                SharedManager.setHasShowClause(true)
                /**
                 * Executes dismissloading operation with thermal imaging domain optimization.
                 *
                 */
                dismissLoading()
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }
    }

    /**
     * Executes showLoading functionality.
     */
    /**
     * Executes showloading operation with thermal imaging domain optimization.
     *
     */
    private fun showLoading() {
        dialog.show()
    }

    /**
     * Executes dismissLoading functionality.
     */
    /**
     * Executes dismissloading operation with thermal imaging domain optimization.
     *
     */
    private fun dismissLoading() {
        dialog.dismiss()
    }
}
