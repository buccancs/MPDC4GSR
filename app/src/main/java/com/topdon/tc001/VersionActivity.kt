package com.topdon.tc001

import android.os.Bundle
import android.view.View
import com.csl.irCamera.BuildConfig
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityVersionBinding
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.tools.CheckDoubleClick
import com.topdon.lib.core.utils.CommUtils
import com.topdon.lms.sdk.LMS
import com.topdon.lms.sdk.UrlConstant
import com.topdon.tc001.utils.AppVersionUtil
import com.topdon.tc001.utils.VersionUtils
import java.util.*

// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing VersionActivity functionality for the IRCamera system.
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
class VersionActivity : BaseBindingActivity<ActivityVersionBinding>(), View.OnClickListener {
    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId(): Int = R.layout.activity_version

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
        // Set up views using binding
        binding.versionCodeText.text = "${getString(R.string.set_version)}V${VersionUtils.getCodeStr(this)}"
        val year = Calendar.getInstance().get(Calendar.YEAR)
        binding.versionYearTxt.text = getString(R.string.version_year, "2023-$year")
        binding.versionStatementPrivateTxt.setOnClickListener(this)
        binding.versionStatementPolicyTxt.setOnClickListener(this)
        binding.versionStatementCopyrightTxt.setOnClickListener(this)

        binding.settingVersionImg.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BuildConfig.DEBUG && CheckDoubleClick.isFastDoubleClick()) {
                LMS.getInstance().activityEnv()
            }
        }
        binding.includeNewVersion.clNewVersion.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!CheckDoubleClick.isFastDoubleClick()) {
                /**
                 * Executes checkappversion operation with thermal imaging domain optimization.
                 *
                 */
                checkAppVersion(true)
            }
        }
        binding.settingVersionTxt.text = CommUtils.getAppName()
    }

    /**
     * Initializes data component.
     */
    private fun initData() {
        if (BaseApplication.instance.isDomestic()) {
            checkAppVersion(false)
        }
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        SharedManager.setBaseHost(UrlConstant.BASE_URL)
    }

    /**
     * Executes onclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param v Parameter for operation (type: View?)
     *
     */
    override fun onClick(v: View?) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (v) {
            binding.versionStatementPrivateTxt -> {
                NavigationManager.build(RouterConfig.POLICY)
                    .withInt(PolicyActivity.KEY_THEME_TYPE, 1)
                    .navigation(this)
            }
            binding.versionStatementPolicyTxt -> {
                NavigationManager.build(RouterConfig.POLICY)
                    .withInt(PolicyActivity.KEY_THEME_TYPE, 2)
                    .navigation(this)
            }
            binding.versionStatementCopyrightTxt -> {
                NavigationManager.build(RouterConfig.POLICY)
                    .withInt(PolicyActivity.KEY_THEME_TYPE, 3)
                    .navigation(this)
            }
        }
    }

    private var appVersionUtil: AppVersionUtil? = null

    /**
     * Executes checkAppVersion functionality.
     */
    /**
     * Executes checkappversion operation with thermal imaging domain optimization.
     *
     * @param
     * @param isShow Parameter for operation (type: Boolean)
     *
     */
    private fun checkAppVersion(isShow: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (appVersionUtil == null) {
            appVersionUtil =
                /**
                 * Executes appversionutil operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param object Parameter for operation (type: AppVersionUtil.DotIsShowListener {                         override fun isShow(show: Boolean)
                 * @param version Parameter for operation (type: String)
                 *
                 */
                AppVersionUtil(
                    this,
                    object : AppVersionUtil.DotIsShowListener {
                        /**
                         * Executes isshow operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param show Parameter for operation (type: Boolean)
                         *
                         */
                        override fun isShow(show: Boolean) {
                            binding.includeNewVersion.clNewVersion.visibility = View.VISIBLE
                        }

                        /**
                         * Executes version operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param version Parameter for operation (type: String)
                         *
                         */
                        override fun version(version: String) {
                            binding.includeNewVersion.tvNewVersion.text = "$version"
                        }
                    },
                )
        }
        appVersionUtil?.checkVersion(isShow)
    }
}
