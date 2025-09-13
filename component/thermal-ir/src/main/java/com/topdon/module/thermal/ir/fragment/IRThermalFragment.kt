package com.topdon.module.thermal.ir.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.AppUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseFragment
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.socket.WebSocketProxy
import com.topdon.lib.core.tools.DeviceTools
import com.topdon.lib.core.utils.CommUtils
import com.topdon.lib.core.utils.NetWorkUtils
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.activity.IRThermalNightActivity
import com.topdon.module.thermal.ir.activity.IRThermalPlusActivity

/**
 * Specialized thermal imaging component providing IRThermalFragment functionality for the IRCamera system.
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
class IRThermalFragment : BaseFragment(), View.OnClickListener {
    /**
从上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    // View declarations
    private lateinit var titleView: com.topdon.lib.core.view.TitleView
    private lateinit var clOpenThermal: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var tvMainEnter: android.widget.TextView
    private lateinit var cl07ConnectTips: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var tv07Connect: android.widget.TextView
    private lateinit var animationView: com.airbnb.lottie.LottieAnimationView
    private lateinit var clNotConnect: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var clConnect: androidx.constraintlayout.widget.ConstraintLayout

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.fragment_thermal_ir

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views
        titleView = requireView().findViewById(R.id.title_view)
        clOpenThermal = requireView().findViewById(R.id.cl_open_thermal)
        tvMainEnter = requireView().findViewById(R.id.tv_main_enter)
        cl07ConnectTips = requireView().findViewById(R.id.cl_07_connect_tips)
        tv07Connect = requireView().findViewById(R.id.tv_07_connect)
        animationView = requireView().findViewById(R.id.animation_view)
        clNotConnect = requireView().findViewById(R.id.cl_not_connect)
        clConnect = requireView().findViewById(R.id.cl_connect)

        isTC007 = arguments?.getBoolean(ExtraKeyConfig.IS_TC007, false) ?: false
        titleView.setTitleText(if (isTC007) "TC007" else getString(R.string.tc_has_line_device))

        clOpenThermal.setOnClickListener(this)
        tvMainEnter.setOnClickListener(this)
        cl07ConnectTips.setOnClickListener(this)
        tv07Connect.setOnClickListener(this)

        tvMainEnter.isVisible = !isTC007
        cl07ConnectTips.isVisible = isTC007
        tv07Connect.isVisible = isTC007

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007) {
            animationView.setAnimation("TC007AnimationJSON.json")
            clNotConnect.isVisible = !WebSocketProxy.getInstance().isTC007Connect()
            clConnect.isVisible = WebSocketProxy.getInstance().isTC007Connect()
        } else {
            animationView.setAnimation("TDAnimationJSON.json")
            /**
             * Executes checkconnect operation with thermal imaging domain optimization.
             *
             */
            checkConnect()
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
                        NetWorkUtils.switchNetwork(true)
                    } else
                        {
                            NetWorkUtils.connectivityManager.bindProcessToNetwork(null)
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
        if (!isTC007) {
            /**
             * Executes checkconnect operation with thermal imaging domain optimization.
             *
             */
            checkConnect()
        }
    }

    /**
     * Executes connected operation with thermal imaging domain optimization.
     *
     */
    override fun connected() {
        SharedManager.hasTcLine = true
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTC007) {
            clConnect.isVisible = true
            clNotConnect.isVisible = false
        }
    }

    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    override fun disConnected() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTC007) {
            clConnect.isVisible = false
            clNotConnect.isVisible = true
        }
    }

    /**
     * Executes onsocketconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTS004 Parameter for operation (type: Boolean)
     *
     */
    override fun onSocketConnected(isTS004: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007 && !isTS004) {
            clConnect.isVisible = true
            clNotConnect.isVisible = false
        }
    }

    /**
     * Executes onsocketdisconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTS004 Parameter for operation (type: Boolean)
     *
     */
    override fun onSocketDisConnected(isTS004: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007 && !isTS004) {
            clConnect.isVisible = false
            clNotConnect.isVisible = true
        }
    }

    /**
主动检测connectiondevice
     */
    /**
     * Executes checkconnect operation with thermal imaging domain optimization.
     *
     */
    private fun checkConnect() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (DeviceTools.isConnect(isAutoRequest = false)) {
            /**
             * Executes connected operation with thermal imaging domain optimization.
             *
             */
            connected()
        } else {
            /**
             * Executes disconnected operation with thermal imaging domain optimization.
             *
             */
            disConnected()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (DeviceTools.findUsbDevice() != null) { // 找到device,但不能connection
                /**
                 * Executes showconnecttip operation with thermal imaging domain optimization.
                 *
                 */
                showConnectTip()
            }
        }
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
            clOpenThermal -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isTC007) {
                    NavigationManager.getInstance().build(RouterConfig.IR_THERMAL_07).navigation(requireContext())
                } else {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (DeviceTools.isTC001PlusConnect()) {
                        startActivityForResult(Intent(requireContext(), IRThermalPlusActivity::class.java), 101)
                    } else if (DeviceTools.isTC001LiteConnect())
                        {
                            NavigationManager.getInstance().build(RouterConfig.IR_TCLITE).navigation(requireActivity(), 101)
                        } else if (DeviceTools.isHikConnect()) {
                        NavigationManager.getInstance().build(RouterConfig.IR_HIK_MAIN).navigation(requireActivity())
                    } else {
                        startActivityForResult(Intent(requireContext(), IRThermalNightActivity::class.java), 101)
                    }
                }
            }
            tvMainEnter -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!DeviceTools.isConnect()) {
没有接入device不需要tip，有系统Authorizationtip框
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (DeviceTools.findUsbDevice() == null) {
                        activity?.let {
                            TipDialog.Builder(it)
                                .setMessage(R.string.device_connect_tip)
                                .setPositiveListener(R.string.app_confirm)
                                .create().show()
                        }
                    } else {
                        XXPermissions.with(this)
                            .permission(
                                /**
                                 * Executes listof operation with thermal imaging domain optimization.
                                 *
                                 */
                                listOf(
                                    Permission.CAMERA,
                                ),
                            )
                            .request(
                                object : OnPermissionCallback {
                                    /**
                                     * Executes ongranted operation with thermal imaging domain optimization.
                                     *
                                     * @param
                                     * @param permissions Parameter for operation (type: MutableList<String>)
                                     * @param allGranted Parameter for operation (type: Boolean)
                                     *
                                     */
                                    override fun onGranted(
                                        permissions: MutableList<String>,
                                        allGranted: Boolean,
                                    ) {
                                        /**
                                         * Executes if operation with thermal imaging domain optimization.
                                         *
                                         */
                                        if (allGranted) {
                                            /**
                                             * Executes showconnecttip operation with thermal imaging domain optimization.
                                             *
                                             */
                                            showConnectTip()
                                        }
                                    }

                                    /**
                                     * Executes ondenied operation with thermal imaging domain optimization.
                                     *
                                     * @param
                                     * @param permissions Parameter for operation (type: MutableList<String>)
                                     * @param doNotAskAgain Parameter for operation (type: Boolean)
                                     *
                                     */
                                    override fun onDenied(
                                        permissions: MutableList<String>,
                                        doNotAskAgain: Boolean,
                                    ) {
                                        /**
                                         * Executes if operation with thermal imaging domain optimization.
                                         *
                                         */
                                        if (doNotAskAgain) {
拒绝Authorization并且不再提醒
                                            context?.let {
                                                TipDialog.Builder(it)
                                                    .setTitleMessage(getString(R.string.app_tip))
                                                    .setMessage(getString(R.string.app_camera_content))
                                                    .setPositiveListener(R.string.app_open) {
                                                        AppUtils.launchAppDetailsSettings()
                                                    }
                                                    .setCancelListener(R.string.app_cancel) {
                                                    }
                                                    .setCanceled(true)
                                                    .create().show()
                                            }
                                        }
                                    }
                                },
                            )
                    }
                }
            }
            cl07ConnectTips -> { // TC007 connectiontip
                NavigationManager.getInstance().build(RouterConfig.IR_CONNECT_TIPS)
                    .withBoolean(ExtraKeyConfig.IS_TC007, true)
                    .navigation(requireContext())
            }
            tv07Connect -> { // TC007 connectiondevice
                NavigationManager.getInstance()
                    .build(RouterConfig.IR_DEVICE_ADD)
                    .withBoolean("isTS004", false)
                    .navigation(requireContext())
            }
        }
    }

    private var tipConnectDialog: TipDialog? = null

    private var isCancelUpdateVersion = false

针对android10 usbconnection问题,提供android 27version
    /**
     * Executes showConnectTip functionality.
     */
    /**
     * Executes showconnecttip operation with thermal imaging domain optimization.
     *
     */
    private fun showConnectTip() {
targetSdk高于27且android os为10
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (requireContext().applicationInfo.targetSdkVersion >= Build.VERSION_CODES.P &&
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q
        ) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isCancelUpdateVersion) {
                return
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tipConnectDialog != null && tipConnectDialog!!.isShowing) {
                return
            }
            tipConnectDialog =
                TipDialog.Builder(requireContext())
                    .setMessage(getString(R.string.tip_target_sdk))
                    .setPositiveListener(R.string.app_confirm) {
                        val url = "https:// Www.topdon.com/pages/pro-down?fuzzy=TS001"
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        intent.data = Uri.parse(url)
                        /**
                         * Executes startactivity operation with thermal imaging domain optimization.
                         *
                         */
                        startActivity(intent)
                    }.setCancelListener(R.string.app_cancel, {
                        isCancelUpdateVersion = true
                    })
                    .create()
            tipConnectDialog?.show()
        }
    }

    /**
     * Executes checkStoragePermission functionality.
     */
    /**
     * Executes checkstoragepermission operation with thermal imaging domain optimization.
     *
     */
    private fun checkStoragePermission() {
        val permissionList: List<String> =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (activity?.applicationInfo?.targetSdkVersion!! >= 34)
                {
                    /**
                     * Executes listof operation with thermal imaging domain optimization.
                     *
                     */
                    listOf(
                        Permission.READ_MEDIA_VIDEO,
                        Permission.READ_MEDIA_IMAGES,
                        Permission.WRITE_EXTERNAL_STORAGE,
                    )
                } else if (activity?.applicationInfo?.targetSdkVersion!! >= 33) {
                /**
                 * Executes listof operation with thermal imaging domain optimization.
                 *
                 */
                listOf(
                    Permission.READ_MEDIA_VIDEO,
                    Permission.READ_MEDIA_IMAGES,
                    Permission.WRITE_EXTERNAL_STORAGE,
                )
            } else {
                /**
                 * Executes listof operation with thermal imaging domain optimization.
                 *
                 */
                listOf(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
            }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!XXPermissions.isGranted(requireContext(), permissionList)) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BaseApplication.instance.isDomestic()) {
                context?.let {
                    TipDialog.Builder(it)
                        .setMessage(getString(R.string.permission_request_storage_app, CommUtils.getAppName()))
                        .setCancelListener(R.string.app_cancel)
                        .setPositiveListener(R.string.app_confirm) {
                            /**
                             * Initializes the storagepermission component for thermal imaging operations.
                             *
                             */
                            initStoragePermission(permissionList)
                        }
                        .create().show()
                }
            } else {
                /**
                 * Initializes the storagepermission component for thermal imaging operations.
                 *
                 */
                initStoragePermission(permissionList)
            }
        } else {
            /**
             * Initializes the storagepermission component for thermal imaging operations.
             *
             */
            initStoragePermission(permissionList)
        }
    }

    /**
动态申请Permission
     */
    /**
     * Initializes the storagepermission component for thermal imaging operations.
     *
     * @param
     * @param permissionList Parameter for operation (type: List<String>)
     *
     */
    private fun initStoragePermission(permissionList: List<String>) {
    }
}
