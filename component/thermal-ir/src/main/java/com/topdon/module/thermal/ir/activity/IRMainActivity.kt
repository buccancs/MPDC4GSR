package com.topdon.module.thermal.ir.activity

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.AppUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.bean.event.PDFEvent
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.repository.GalleryRepository.DirType
import com.topdon.lib.core.repository.TC007Repository
import com.topdon.lib.core.socket.WebSocketProxy
import com.topdon.lib.core.tools.DeviceTools
import com.topdon.lib.core.utils.CommUtils
import com.topdon.lib.core.utils.NetWorkUtils
import com.topdon.lib.core.utils.PermissionUtils
import com.topdon.lms.sdk.LMS
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.databinding.ActivityIrMainBinding
import com.topdon.module.thermal.ir.dialog.HomeGuideDialog
import com.topdon.module.thermal.ir.fragment.AbilityFragment
import com.topdon.module.thermal.ir.fragment.IRGalleryTabFragment
import com.topdon.module.thermal.ir.fragment.IRThermalFragment
import com.topdon.module.thermal.ir.fragment.PDFListFragment
import com.topdon.module.user.fragment.MoreFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import com.topdon.lib.core.R as LibR

/**
插件式 或 TC007 首页.
 *
需要传递parameter：
- [ExtraKeyConfig.IS_TC007] - 当前device是否为 TC007
 *
 * Created by LCG on 2024/4/18.
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing IRMainActivity functionality for the IRCamera system.
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
class IRMainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityIrMainBinding

    /**
从上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIrMainBinding.inflate(layoutInflater)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
    }

    /**
     * Executes onnewintent operation with thermal imaging domain optimization.
     *
     * @param
     * @param intent Parameter for operation (type: Intent?)
     *
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
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
        isTC007 = intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false)

        binding.viewPage.offscreenPageLimit = 5
        binding.viewPage.isUserInputEnabled = false
        binding.viewPage.adapter = ViewPagerAdapter(this, isTC007)
        binding.viewPage.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                /**
                 * Executes onpageselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param position Parameter for operation (type: Int)
                 *
                 */
                override fun onPageSelected(position: Int) {
                    /**
                     * Executes refreshtabselect operation with thermal imaging domain optimization.
                     *
                     */
                    refreshTabSelect(position)
                }
            },
        )
        binding.viewPage.setCurrentItem(2, false)

        binding.clIconMonitor.setOnClickListener(this)
        binding.clIconGallery.setOnClickListener(this)
        // View_main_thermal.setOnClickListener(this) // Not found in view declarations, likely unused
        binding.clIconReport.setOnClickListener(this)
        binding.clIconMine.setOnClickListener(this)

        /**
         * Executes showguidedialog operation with thermal imaging domain optimization.
         *
         */
        showGuideDialog()
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
//        DeviceTools.isConnect(true)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (WebSocketProxy.getInstance().isTC007Connect()) {
                NetWorkUtils.switchNetwork(false)
                binding.ivMainBg.setImageResource(R.drawable.ic_ir_main_bg_connect)
                lifecycleScope.launch {
                    TC007Repository.syncTime()
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (SharedManager.isConnect07AutoOpen) {
                    NavigationManager.getInstance().build(RouterConfig.IR_THERMAL_07).navigation(this)
                }
            } else {
                binding.ivMainBg.setImageResource(R.drawable.ic_ir_main_bg_disconnect)
            }
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (DeviceTools.isConnect(isAutoRequest = false)) {
                binding.ivMainBg.setImageResource(R.drawable.ic_ir_main_bg_connect)
            } else {
                binding.ivMainBg.setImageResource(R.drawable.ic_ir_main_bg_disconnect)
            }
        }
    }

    /**
     * Initializes data component.
     */
    private fun initData() {
    }

    /**
     * Executes connected functionality.
     */
    /**
     * Executes connected operation with thermal imaging domain optimization.
     *
     */
    private fun connected() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTC007) {
            binding.ivMainBg.setImageResource(R.drawable.ic_ir_main_bg_connect)
        }
    }

    /**
     * Executes disConnected functionality.
     */
    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    private fun disConnected() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTC007) {
            binding.ivMainBg.setImageResource(R.drawable.ic_ir_main_bg_disconnect)
        }
    }

    /**
     * Executes onSocketConnected functionality.
     */
    /**
     * Executes onsocketconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTS004 Parameter for operation (type: Boolean)
     *
     */
    private fun onSocketConnected(isTS004: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTS004 && isTC007) {
            binding.ivMainBg.setImageResource(R.drawable.ic_ir_main_bg_connect)
        }
    }

    /**
     * Executes onSocketDisConnected functionality.
     */
    /**
     * Executes onsocketdisconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTS004 Parameter for operation (type: Boolean)
     *
     */
    private fun onSocketDisConnected(isTS004: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTS004 && isTC007) {
            binding.ivMainBg.setImageResource(R.drawable.ic_ir_main_bg_disconnect)
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
            binding.clIconMonitor -> { // 监控
                binding.viewPage.setCurrentItem(0, false)
            }
            binding.clIconGallery -> { // 图库
                /**
                 * Executes checkstoragepermission operation with thermal imaging domain optimization.
                 *
                 */
                checkStoragePermission()
            }
view_main_thermal -> {// 首页 - Commented out as not in view declarations
            // Binding.viewPage.setCurrentItem(2, false)
            // }
            binding.clIconReport -> { // Report
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (LMS.getInstance().isLogin) {
                    binding.viewPage.setCurrentItem(3, false)
                } else {
                    LMS.getInstance().activityLogin(null) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (it) {
                            binding.viewPage.setCurrentItem(3, false)
                            EventBus.getDefault().post(PDFEvent())
                        }
                    }
                }
            }
            binding.clIconMine -> { // 我的
                binding.viewPage.setCurrentItem(4, false)
            }
        }
    }

    /**
refresh 5 个 tab 的selectedstate
@param index 当前selected哪个 tab，`[0, 4]`
     */
    /**
     * Executes refreshTabSelect functionality.
     */
    /**
     * Executes refreshtabselect operation with thermal imaging domain optimization.
     *
     * @param
     * @param index Parameter for operation (type: Int)
     *
     */
    private fun refreshTabSelect(index: Int) {
        binding.ivIconMonitor.isSelected = false
        binding.tvIconMonitor.isSelected = false
        binding.ivIconGallery.isSelected = false
        binding.tvIconGallery.isSelected = false
        binding.ivIconReport.isSelected = false
        binding.tvIconReport.isSelected = false
        binding.ivIconMine.isSelected = false
        binding.tvIconMine.isSelected = false
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (index) {
            0 -> {
                binding.ivIconMonitor.isSelected = true
                binding.tvIconMonitor.isSelected = true
            }
            1 -> {
                binding.ivIconGallery.isSelected = true
                binding.tvIconGallery.isSelected = true
            }
            3 -> {
                binding.ivIconReport.isSelected = true
                binding.tvIconReport.isSelected = true
            }
            4 -> {
                binding.ivIconMine.isSelected = true
                binding.tvIconMine.isSelected = true
            }
        }
    }

    /**
display操作指引弹框.
     */
    /**
     * Executes showguidedialog operation with thermal imaging domain optimization.
     *
     */
    private fun showGuideDialog() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (SharedManager.homeGuideStep == 0) { // 已看过或不再tip
            return
        }

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (SharedManager.homeGuideStep) {
            1 -> binding.viewPage.setCurrentItem(0, false)
            2 -> binding.viewPage.setCurrentItem(4, false)
            3 -> binding.viewPage.setCurrentItem(2, false)
        }

        val guideDialog = HomeGuideDialog(this, SharedManager.homeGuideStep)
        guideDialog.onNextClickListener = {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (it) {
                1 -> {
                    binding.viewPage.setCurrentItem(4, false)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Build.VERSION.SDK_INT < 31) {
                        lifecycleScope.launch {
                            /**
                             * Executes delay operation with thermal imaging domain optimization.
                             *
                             */
                            delay(100)
                            guideDialog.blurBg(binding.clRoot)
                        }
                    }
                    SharedManager.homeGuideStep = 2
                }
                2 -> {
                    binding.viewPage.setCurrentItem(2, false)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Build.VERSION.SDK_INT < 31) {
                        lifecycleScope.launch {
                            /**
                             * Executes delay operation with thermal imaging domain optimization.
                             *
                             */
                            delay(100)
                            guideDialog.blurBg(binding.clRoot)
                        }
                    }
                    SharedManager.homeGuideStep = 3
                }
                3 -> {
                    SharedManager.homeGuideStep = 0
                }
            }
        }
        guideDialog.onSkinClickListener = {
            SharedManager.homeGuideStep = 0
        }
        guideDialog.setOnDismissListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= 31) {
                window?.decorView?.setRenderEffect(null)
            }
        }
        guideDialog.show()

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= 31) {
            window?.decorView?.setRenderEffect(RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.MIRROR))
        } else {
            lifecycleScope.launch {
interfaceswitch及temperature监控历史列表load均需要时间，所以需要等待1000毫秒再去refresh背景
而若等待1000毫秒太过久，interface会非模糊1000毫秒，所以先refresh一次背景占位
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(100)
                guideDialog.blurBg(binding.clRoot)
            }
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
            if (this.applicationInfo.targetSdkVersion >= 34)
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
                } else if (this.applicationInfo.targetSdkVersion >= 34)
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
                } else if (this.applicationInfo.targetSdkVersion == 33) {
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
        if (!XXPermissions.isGranted(this, permissionList)) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BaseApplication.instance.isDomestic()) {
                TipDialog.Builder(this)
                    .setMessage(getString(LibR.string.permission_request_storage_app, CommUtils.getAppName()))
                    .setCancelListener(LibR.string.app_cancel)
                    .setPositiveListener(LibR.string.app_confirm) {
                        /**
                         * Initializes the storagepermission component for thermal imaging operations.
                         *
                         */
                        initStoragePermission(permissionList)
                    }
                    .create().show()
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
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (PermissionUtils.isVisualUser())
            {
                binding.viewPage.setCurrentItem(1, false)
                return
            }
        XXPermissions.with(this)
            .permission(permissionList)
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
                            binding.viewPage.setCurrentItem(1, false)
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
                            TipDialog.Builder(this@IRMainActivity)
                                .setTitleMessage(getString(LibR.string.app_tip))
                                .setMessage(getString(LibR.string.app_album_content))
                                .setPositiveListener(LibR.string.app_open) {
                                    AppUtils.launchAppDetailsSettings()
                                }
                                .setCancelListener(LibR.string.app_cancel) {
                                }
                                .setCanceled(true)
                                .create().show()
                        }
                    }
                },
            )
    }

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ViewPagerAdapter display and interaction.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class ViewPagerAdapter(val activity: FragmentActivity, val isTC007: Boolean) : FragmentStateAdapter(activity) {
        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount() = 5

        /**
         * Executes createfragment operation with thermal imaging domain optimization.
         *
         * @param
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun createFragment(position: Int): Fragment {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (position == 1) { // 图库
                return IRGalleryTabFragment().apply {
                    arguments =
                        /**
                         * Executes bundle operation with thermal imaging domain optimization.
                         *
                         */
                        Bundle().also {
                            val dirType = if (isTC007) DirType.TC007.ordinal else DirType.LINE.ordinal
                            it.putBoolean(ExtraKeyConfig.CAN_SWITCH_DIR, false)
                            it.putBoolean(ExtraKeyConfig.HAS_BACK_ICON, false)
                            it.putInt(ExtraKeyConfig.DIR_TYPE, dirType)
                        }
                }
            } else {
                val fragment =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (position) {
                        0 -> AbilityFragment()
                        2 -> IRThermalFragment()
                        3 -> PDFListFragment()
                        else -> MoreFragment()
                    }
                fragment.arguments = Bundle().also { it.putBoolean(ExtraKeyConfig.IS_TC007, isTC007) }
                return fragment
            }
        }
    }
}
