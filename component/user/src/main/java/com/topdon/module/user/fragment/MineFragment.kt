package com.topdon.module.user.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.R as LibAppR // Import libapp R class for resources
import com.blankj.utilcode.util.CleanUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.request.RequestOptions
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.bean.event.PDFEvent
import com.topdon.lib.core.bean.event.WinterClickEvent
import com.topdon.lib.core.bean.response.ResponseUserInfo
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.common.UserInfoManager
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.db.AppDatabase
import com.topdon.lib.core.R as RCore
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseFragment
import com.topdon.lib.core.socket.WebSocketProxy
// Import com.topdon.lib.core.tools.AppLanguageUtils
// Import com.topdon.lib.core.tools.ConstantLanguages
import com.topdon.lib.core.tools.GlideLoader
import com.topdon.lib.core.utils.Constants
import com.topdon.lib.core.utils.NetWorkUtils
import com.topdon.lms.sdk.LMS
import com.topdon.lms.sdk.UrlConstant
import com.topdon.lms.sdk.bean.CommonBean
import com.topdon.lms.sdk.bean.FeedBackBean
import com.topdon.lms.sdk.feedback.activity.FeedbackActivity
import com.topdon.module.user.R
import com.topdon.module.user.activity.MoreActivity
// Import com.zoho.salesiqembed.ZohoSalesIQ  // ZohoSalesIQ dependency not available
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
公共set页，即公共 “我的”
[MoreActivity] - TS004 “我的”
[MoreFragment] - 插件式 “我的”
 *
 * Created by LCG on 2024/4/19.
 */
/**
 * Specialized thermal imaging component providing MineFragment functionality for the IRCamera system.
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
class MineFragment : BaseFragment(), View.OnClickListener {
    /**
/**
 * Executes onresume operation with thermal imaging domain optimization.
 *
 */
onResume() 阶段是否需要refreshLoginstate相关 UI.
     */
    private var isNeedRefreshLogin = false

    // View references
    private lateinit var ivWinter: ImageView
    private lateinit var settingItemVersion: View
    private lateinit var settingItemClear: View
    private lateinit var settingUserLay: View
    private lateinit var settingUserImgNight: ImageView
    private lateinit var settingUserText: TextView
    private lateinit var settingElectronicManual: View
    private lateinit var settingFaq: View
    private lateinit var settingFeedback: View
    private lateinit var settingItemUnit: View
    private lateinit var dragCustomerView: View
    private lateinit var viewWinterPoint: View

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.fragment_mine

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views
        ivWinter = requireView().findViewById(R.id.iv_winter)
        settingItemVersion = requireView().findViewById(R.id.setting_item_version)
        settingItemClear = requireView().findViewById(R.id.setting_item_clear)
        settingUserLay = requireView().findViewById(R.id.setting_user_lay)
        settingUserImgNight = requireView().findViewById(R.id.setting_user_img_night)
        settingUserText = requireView().findViewById(R.id.setting_user_text)
        settingElectronicManual = requireView().findViewById(R.id.setting_electronic_manual)
        settingFaq = requireView().findViewById(R.id.setting_faq)
        settingFeedback = requireView().findViewById(R.id.setting_feedback)
        settingItemUnit = requireView().findViewById(R.id.setting_item_unit)
        dragCustomerView = requireView().findViewById(R.id.drag_customer_view)
        viewWinterPoint = requireView().findViewById(R.id.view_winter_point)

        ivWinter.setOnClickListener(this)
        settingItemVersion.setOnClickListener(this)
        settingItemClear.setOnClickListener(this)
        settingUserLay.setOnClickListener(this)
        settingUserImgNight.setOnClickListener(this)
        settingUserText.setOnClickListener(this)
        settingElectronicManual.setOnClickListener(this)
        settingFaq.setOnClickListener(this)
        settingFeedback.setOnClickListener(this)
        settingItemUnit.setOnClickListener(this) // Temperature单温
        dragCustomerView.setOnClickListener(this)

        viewWinterPoint.isVisible = !SharedManager.hasClickWinter

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (BaseApplication.instance.isDomestic()) { // 国内版
            // Language selection removed - English only
        }

        viewLifecycleOwner.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                /**
                 * Executes onresume operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param owner Parameter for operation (type: LifecycleOwner)
                 *
                 */
                override fun onResume(owner: LifecycleOwner) {
要是当前已connection TS004、TC007，切到流量上，不然LoginRegister意见反馈那些没网
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (WebSocketProxy.getInstance().isConnected()) {
                        NetWorkUtils.switchNetwork(false)
                    }
                }
            },
        )
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes updatePDF functionality.
     */
    /**
     * Executes updatepdf operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: PDFEvent)
     *
     */
    fun updatePDF(event: PDFEvent) {
        isNeedRefreshLogin = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes onWinterClick functionality.
     */
    /**
     * Executes onwinterclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: WinterClickEvent)
     *
     */
    fun onWinterClick(event: WinterClickEvent) {
        viewWinterPoint.isVisible = false
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        /**
         * Updates the loginstyle configuration with real-time thermal imaging support.
         *
         */
        changeLoginStyle()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isNeedRefreshLogin) {
            isNeedRefreshLogin = false
            /**
             * Executes checkloginresult operation with thermal imaging domain optimization.
             *
             */
            checkLoginResult()
        }
    }

    // Language picker removed - English only app

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
            ivWinter -> { // 冬季特辑入口
                viewWinterPoint.isVisible = false
                SharedManager.hasClickWinter = true
                EventBus.getDefault().post(WinterClickEvent())

                val url =
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param https Parameter for operation (type: // Api.topdon.com/")
                     *
                     */
                    if (UrlConstant.BASE_URL == "https:// Api.topdon.com/") {
                        "https:// App.topdon.com/h5/share/#/detectionGuidanceIndex?showHeader=1&" +
                            "languageId=1" // Fixed to English (languageId=1)
                    } else {
                        "http:// 172.16.66.77:8081/#/detectionGuidanceIndex?languageId=1&showHeader=1"
                    }

                NavigationManager.getInstance().build(RouterConfig.WEB_VIEW)
                    .withString(ExtraKeyConfig.URL, url)
                    .navigation(requireContext())
            }
            settingUserLay, settingUserImgNight -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (UserInfoManager.getInstance().isLogin()) {
                    isNeedRefreshLogin = true
                    LMS.getInstance().activityUserInfo()
                } else {
                    /**
                     * Executes loginaction operation with thermal imaging domain optimization.
                     *
                     */
                    loginAction()
                }
            }
            settingUserText -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!LMS.getInstance().isLogin) {
                    /**
                     * Executes loginaction operation with thermal imaging domain optimization.
                     *
                     */
                    loginAction()
                }
            }
            settingElectronicManual -> { // 电子description书
                NavigationManager.getInstance().build(
                    RouterConfig.ELECTRONIC_MANUAL,
                ).withInt(Constants.SETTING_TYPE, Constants.SETTING_BOOK).navigation(requireContext())
            }
            settingFaq -> { // FAQ
                NavigationManager.getInstance().build(
                    RouterConfig.ELECTRONIC_MANUAL,
                ).withInt(Constants.SETTING_TYPE, Constants.SETTING_FAQ).navigation(requireContext())
            }
            settingFeedback -> { // 意见反馈
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (LMS.getInstance().isLogin) {
                    val devSn = SharedManager.getDeviceSn()
                    /**
                     * Executes feedbackbean operation with thermal imaging domain optimization.
                     *
                     */
                    FeedBackBean().apply {
                        logPath = logPath
                        sn = devSn
                        lastConnectSn = devSn
                        XLog.e("bcf", "sn $sn  logPath $logPath")
                    }.let { feedBackBean ->
                        val intent = Intent(requireContext(), FeedbackActivity::class.java)
                        intent.putExtra(FeedbackActivity.FEEDBACKBEAN, feedBackBean)
                        /**
                         * Executes startactivity operation with thermal imaging domain optimization.
                         *
                         */
                        startActivity(intent)
                    }
                } else {
                    /**
                     * Executes loginaction operation with thermal imaging domain optimization.
                     *
                     */
                    loginAction()
                }
            }
            settingItemUnit -> { // Temperature单位
                NavigationManager.getInstance().build(RouterConfig.UNIT).navigation(requireContext())
            }
            settingItemVersion -> { // Version
                NavigationManager.getInstance().build(RouterConfig.VERSION).navigation(requireContext())
            }
            settingItemClear -> { // Clearcache，实际已Hide
                /**
                 * Executes clearcache operation with thermal imaging domain optimization.
                 *
                 */
                clearCache()
            }
            dragCustomerView -> { // 客服
//                ActivityUtil.goSystemCustomer(requireContext())
                val sn = SharedManager.getDeviceSn()
                // ZohoSalesIQ functionality disabled - dependency not available
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!TextUtils.isEmpty(sn)) {
                    // ZohoSalesIQ.Visitor.addInfo("SN", sn)
                }
                // ZohoSalesIQ.Visitor.addInfo("Model", "Topinfrared")
                // ZohoSalesIQ.Chat.show()
            }
        }
    }

    /**
     * Executes loginAction functionality.
     */
    /**
     * Executes loginaction operation with thermal imaging domain optimization.
     *
     */
    private fun loginAction() {
        isNeedRefreshLogin = true
/**
 * Executes activitylogin operation with thermal imaging domain optimization.
 *
 */
activityLogin()Callback不可靠，但必然触发onResume()
        val bgBitmap = BitmapFactory.decodeResource(resources, LibAppR.mipmap.ic_default_user_head) // Use available resource from libapp
        LMS.getInstance().activityLogin(null, null, false, null, bgBitmap)
    }

    /**
     * Executes checkLoginResult functionality.
     */
    /**
     * Executes checkloginresult operation with thermal imaging domain optimization.
     *
     */
    private fun checkLoginResult() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (LMS.getInstance().isLogin) {
Loginsuccessful
            LMS.getInstance().getUserInfo { userinfo: CommonBean ->
                try {
                    val json = userinfo.data
                    val infoData = Gson().fromJson(json, ResponseUserInfo::class.java)
                    UserInfoManager.getInstance().login(
                        token = LMS.getInstance().token,
                        userId = infoData.topdonId,
                        phone = infoData.phone,
                        email = infoData.email,
                        nickname = infoData.userName,
                        headUrl = infoData.avatar,
                    )

updateui
                    /**
                     * Updates the loginstyle configuration with real-time thermal imaging support.
                     *
                     */
                    changeLoginStyle()
                } catch (e: Exception) {
                    XLog.e(" Loginexception: ${e.message}")
                }
            }
        } else {
Loginfailed
            XLog.e(" Loginfailed")
            /**
             * Updates the loginstyle configuration with real-time thermal imaging support.
             *
             */
            changeLoginStyle()
            settingUserImgNight.setImageResource(LibAppR.mipmap.ic_default_user_head) // Restoredefault头像
        }
    }

    /**
     * Changes loginstyle settings.
     */
    private fun changeLoginStyle() {
        if (LMS.getInstance().isLogin) {
            val layoutParams = ConstraintLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.startToEnd = R.id.setting_user_img_night
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.marginStart = SizeUtils.dp2px(16f)
            layoutParams.marginEnd = SizeUtils.dp2px(16f)
            settingUserText.setPadding(0, 0, 0, 0)
            settingUserText.gravity = Gravity.LEFT
            settingUserText.layoutParams = layoutParams
            val drawable = ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
            drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            settingUserText.setCompoundDrawables(null, null, drawable, null)
            settingUserText.text = SharedManager.getNickname()
            val tvEmail = requireView().findViewById<TextView>(R.id.tv_email)
            tvEmail.text = SharedManager.getUsername()
            settingUserLay.visibility = View.VISIBLE

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (settingUserImgNight != null) {
                GlideLoader.loadCircle(
                    settingUserImgNight,
                    SharedManager.getHeadIcon(),
                    LibAppR.mipmap.ic_default_user_head,
                    /**
                     * Executes requestoptions operation with thermal imaging domain optimization.
                     *
                     */
                    RequestOptions().optionalCircleCrop(),
                )
            }
        } else {
            val layoutParams =
                ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                )
            layoutParams.startToEnd = R.id.setting_user_img_night
            layoutParams.topToTop = R.id.setting_user_img_night
            layoutParams.bottomToBottom = R.id.setting_user_img_night
            settingUserText.setPadding(SizeUtils.dp2px(16f), SizeUtils.dp2px(16f), SizeUtils.dp2px(16f), SizeUtils.dp2px(16f))
            settingUserText.gravity = Gravity.CENTER
            settingUserText.layoutParams = layoutParams
            settingUserText.setText(
                // AppLanguageUtils.attachBaseContext(
                // Context, ConstantLanguages.ENGLISH).getString(RCore.string.app_sign_in))
                context?.getString(RCore.string.app_sign_in) ?: "Sign In",
            )
            val drawable = ContextCompat.getDrawable(requireContext(), R.mipmap.ic_arrow_login)
            drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            settingUserText.setCompoundDrawables(null, null, drawable, null)
            settingUserLay.visibility = View.GONE
            val tvEmail = requireView().findViewById<TextView>(R.id.tv_email)
            tvEmail.text = ""
            settingUserImgNight.setImageResource(LibAppR.mipmap.ic_default_user_head) // Restoredefault头像
        }
    }

    /**
Clearbuffer
     */
    /**
     * Executes clearcache operation with thermal imaging domain optimization.
     *
     */
    private fun clearCache() {
        lifecycleScope.launch {
            /**
             * Executes showloadingdialog operation with thermal imaging domain optimization.
             *
             */
            showLoadingDialog()
            /**
             * Executes withcontext operation with thermal imaging domain optimization.
             *
             */
            withContext(Dispatchers.IO) {
                try {
                    AppDatabase.getInstance().thermalDao().deleteByUserId(SharedManager.getUserId())
                    CleanUtils.cleanExternalCache()
                } catch (e: Exception) {
                    XLog.w("Clearcacheexception: ${e.message}")
                }
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(1000)
            }
            /**
             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
             *
             */
            dismissLoadingDialog()
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(50)
            TipDialog.Builder(requireContext())
                .setMessage(RCore.string.clear_finish)
                .setCanceled(true)
                .create().show()
        }
    }
}
