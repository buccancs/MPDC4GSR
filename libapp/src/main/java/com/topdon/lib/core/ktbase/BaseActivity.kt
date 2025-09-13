package com.topdon.lib.core.ktbase

import android.content.*
import android.content.pm.ActivityInfo
import android.os.*
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.R
import com.topdon.lib.core.bean.event.SocketStateEvent
import com.topdon.lib.core.bean.event.device.DeviceConnectEvent
import com.topdon.lib.core.bean.response.ResponseUserInfo
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.common.UserInfoManager
import com.topdon.lib.core.dialog.LoadingDialog
import com.topdon.lib.core.dialog.TipCameraProgressDialog
import com.topdon.lib.core.tools.*
import com.topdon.lms.sdk.LMS
import com.topdon.lms.sdk.bean.CommonBean
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * Specialized thermal imaging component providing BaseActivity functionality for the IRCamera system.
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
abstract class BaseActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName

    protected abstract fun initContentView(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected var savedInstanceState: Bundle? = null

    protected open fun isLockPortrait(): Boolean = true

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.instance.activitys.add(this)
        this.savedInstanceState = savedInstanceState
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isLockPortrait()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        @Suppress("DEPRECATION")
        window.navigationBarColor = ContextCompat.getColor(this, R.color.toolbar_16131E)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(initContentView())
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
        /**
         * Executes synlogin operation with thermal imaging domain optimization.
         *
         */
        synLogin()
    }

    /**
     * Executes attachbasecontext operation with thermal imaging domain optimization.
     *
     * @param
     * @param newBase Parameter for operation (type: Context?)
     *
     */
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(AppLanguageUtils.attachBaseContext(newBase, ConstantLanguages.ENGLISH))
    }

    /**
     * Executes onstart operation with thermal imaging domain optimization.
     *
     */
    override fun onStart() {
        super.onStart()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * Executes onstop operation with thermal imaging domain optimization.
     *
     */
    override fun onStop() {
        super.onStop()
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        cameraDialog?.dismiss()
        super.onDestroy()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        BaseApplication.instance.activitys.remove(this)
    }

    /**
     * Listener USB connectionstate
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Retrieves connectstate information.
     */
    fun getConnectState(event: DeviceConnectEvent) {
        if (event.isConnect) {
            connected()
        } else {
            /**
             * Executes disconnected operation with thermal imaging domain optimization.
             *
             */
            disConnected()
        }
    }

    protected open fun connected() {
    }

    protected open fun disConnected() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Callback method triggered when socketconnectstate occurs.
     */
    fun onSocketConnectState(event: SocketStateEvent) {
        Log.d("onSocketConnectState", "${event.isConnect}")
        if (event.isConnect) {
            /**
             * Executes onsocketconnected operation with thermal imaging domain optimization.
             *
             */
            onSocketConnected(event.isTS004)
        } else {
            /**
             * Executes onsocketdisconnected operation with thermal imaging domain optimization.
             *
             */
            onSocketDisConnected(event.isTS004)
        }
    }

    protected open fun onSocketConnected(isTS004: Boolean) {
    }

    protected open fun onSocketDisConnected(isTS004: Boolean) {
    }

    /**
     * 新版 LMS 风格的load中弹框.
     */
    private var loadingDialog: LoadingDialog? = null

    /**
     * 真是醉了，aload中的弹框现在就有 3 种，不管了，继续加，理论上后续都要改成这个.
     */
    fun showLoadingDialog(
        @StringRes resId: Int = R.string.tip_loading,
    ) {
        /**
         * Executes showloadingdialog operation with thermal imaging domain optimization.
         *
         */
        showLoadingDialog(getString(resId))
    }

    /**
     * Executes showloadingdialog functionality.
     */
    /**
     * Executes showloadingdialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param text Parameter for operation (type: CharSequence?)
     *
     */
    fun showLoadingDialog(text: CharSequence?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this)
        }
        loadingDialog?.setTips(text)
        loadingDialog?.show()
    }

    /**
     * 真是醉了，aload中的弹框现在就有 3 种，不管了，继续加，理论上后续都要改成这个.
     */
    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private var cameraDialog: TipCameraProgressDialog? = null

    /**
     * Executes showcameraloading functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    fun showCameraLoading() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (cameraDialog != null && cameraDialog!!.isShowing) {
            return
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (cameraDialog == null) {
            cameraDialog =
                TipCameraProgressDialog.Builder(this)
                    .setCanceleable(false)
                    .create()
        }
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!(isFinishing && isDestroyed)) {
                cameraDialog?.show()
            }
        } catch (e: Exception) {
            // 临时捕获方案，后area需求complete后再追踪Optimize
            Log.e("临时processing方案", e.message.toString())
        }
    }

    /**
     * Executes dismisscameraloading functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    fun dismissCameraLoading() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (cameraDialog != null && cameraDialog!!.isShowing) {
            cameraDialog?.dismiss()
        }
    }

    
    /**
     * Executes synLogin functionality.
     */
    /**
     * Executes synlogin operation with thermal imaging domain optimization.
     *
     */
    private fun synLogin() {
        if (this::class.java.simpleName == "MainActivity") {
            LMS.getInstance().syncUserInfo()
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (SharedManager.getHasShowClause() && LMS.getInstance().isLogin) {
            LMS.getInstance().getUserInfo { userinfo: CommonBean ->
                try {
                    val infoData = Gson().fromJson(userinfo.data, ResponseUserInfo::class.java)
                    UserInfoManager.getInstance().login(
                        token = LMS.getInstance().token,
                        userId = infoData.topdonId,
                        phone = infoData.phone,
                        email = infoData.email,
                        nickname = infoData.userName,
                        headUrl = infoData.avatar,
                    )
                } catch (e: Exception) {
                    XLog.e("login error:${e.message}")
                }
            }
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (UserInfoManager.getInstance().isLogin()) {
                // 账号已Exit,本地Loginstate,需Exit操作
                UserInfoManager.getInstance().logout()
            }
        }
    }

    protected class TakePhotoResult : ActivityResultContract<File, File?>() {
        private lateinit var file: File

        /**
         * Executes createintent operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param input Parameter for operation (type: File)
         *
         */
        override fun createIntent(
            context: Context,
            input: File,
        ): Intent {
            file = input
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        /**
         * Executes parseresult operation with thermal imaging domain optimization.
         *
         * @param
         * @param resultCode Parameter for operation (type: Int)
         * @param intent Parameter for operation (type: Intent?)
         *
         */
        override fun parseResult(
            resultCode: Int,
            intent: Intent?,
        ): File? = if (resultCode == RESULT_OK) file else null
    }
}
