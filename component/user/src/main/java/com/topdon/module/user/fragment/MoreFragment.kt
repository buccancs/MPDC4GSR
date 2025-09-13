package com.topdon.module.user.fragment

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.bean.event.TS004ResetEvent
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.common.WifiSaveSettingUtil
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.ConfirmSelectDialog
import com.topdon.lib.core.dialog.FirmwareUpDialog
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.http.tool.DownloadTool
import com.topdon.lib.core.ktbase.BaseFragment
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.repository.ProductBean
import com.topdon.lib.core.repository.TC007Repository
import com.topdon.lib.core.socket.WebSocketProxy
import com.topdon.lib.core.tools.DeviceTools
import com.topdon.lib.core.viewmodel.FirmwareViewModel
import com.topdon.lib.ui.SettingNightView
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.user.R
import com.topdon.module.user.dialog.DownloadProDialog
import com.topdon.module.user.dialog.FirmwareInstallDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.text.DecimalFormat
import com.topdon.lib.core.R as RCore

/**
插件式 “更多” 页area
 *
需要传递parameter：
- [ExtraKeyConfig.IS_TC007] - 当前device是否为 TC007
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing MoreFragment functionality for the IRCamera system.
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
class MoreFragment : BaseFragment(), View.OnClickListener {
    /**
从上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    /**
TC007 firmwareUpgrade ViewModel.
     */
    private val firmwareViewModel: FirmwareViewModel by viewModels()

    // View references
    private lateinit var settingItemModel: View
    private lateinit var settingItemCorrection: View
    private lateinit var settingItemDual: View
    private lateinit var settingItemUnit: View
    private lateinit var settingVersion: View
    private lateinit var settingDeviceInformation: SettingNightView
    private lateinit var settingReset: SettingNightView
    private lateinit var settingItemConfigSelect: androidx.appcompat.widget.SwitchCompat
    private lateinit var tvUpgradePoint: TextView
    private lateinit var itemSettingBottomText: TextView
    private lateinit var tvRightText: TextView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.fragment_more

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        isTC007 = arguments?.getBoolean(ExtraKeyConfig.IS_TC007, false) ?: false

        // Initialize views
        settingItemModel = requireView().findViewById(R.id.setting_item_model)
        settingItemCorrection = requireView().findViewById(R.id.setting_item_correction)
        settingItemDual = requireView().findViewById(R.id.setting_item_dual)
        settingItemUnit = requireView().findViewById(R.id.setting_item_unit)
        settingVersion = requireView().findViewById(R.id.setting_version)
        settingDeviceInformation = requireView().findViewById(R.id.setting_device_information)
        settingReset = requireView().findViewById(R.id.setting_reset)
        settingItemConfigSelect = requireView().findViewById(R.id.setting_item_config_select)
        tvUpgradePoint = requireView().findViewById(R.id.tv_upgrade_point)
        itemSettingBottomText = requireView().findViewById(R.id.item_setting_bottom_text)
        tvRightText = requireView().findViewById(R.id.tv_right_text)

        settingItemModel.setOnClickListener(this) // Temperature修正
        settingItemCorrection.setOnClickListener(this) // Image校正
        settingItemDual.setOnClickListener(this) // Dual light校正
        settingItemUnit.setOnClickListener(this) // Temperature单温
        settingVersion.setOnClickListener(this) // TC007firmwareUpgrade
        settingDeviceInformation.setOnClickListener(this) // TC007deviceinfo
        settingReset.setOnClickListener(this) // TC007Restore出厂settings

根据 2024/5/23 评审会结论，TC007没有多少需要Restore出厂的configuration，产品决定砍掉
        settingReset.isVisible = false

        settingVersion.isVisible = isTC007 && Build.VERSION.SDK_INT >= 29
        settingDeviceInformation.isVisible = isTC007
        settingItemDual.isVisible = !isTC007 && DeviceTools.isTC001PlusConnect()

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007) {
            /**
             * Executes refresh07connect operation with thermal imaging domain optimization.
             *
             */
            refresh07Connect(WebSocketProxy.getInstance().isTC007Connect())
        }

        val settingItemAutoShow = requireView().findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.setting_item_auto_show)
        settingItemAutoShow.isChecked = if (isTC007) SharedManager.isConnect07AutoOpen else SharedManager.isConnectAutoOpen
        settingItemAutoShow.setOnCheckedChangeListener { _, isChecked ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isTC007) {
                SharedManager.isConnect07AutoOpen = isChecked
            } else {
                SharedManager.isConnectAutoOpen = isChecked
            }
        }

        settingItemConfigSelect.isChecked = if (isTC007) WifiSaveSettingUtil.isSaveSetting else SaveSettingUtil.isSaveSetting
        settingItemConfigSelect.setOnCheckedChangeListener { _, isChecked ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isChecked) {
                TipDialog.Builder(requireContext())
                    .setMessage(RCore.string.save_setting_tips)
                    .setPositiveListener(RCore.string.app_ok) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isTC007)
                            {
                                WifiSaveSettingUtil.isSaveSetting = true
                            } else
                            {
                                SaveSettingUtil.isSaveSetting = true
                            }
                    }
                    .setCancelListener(RCore.string.app_cancel) {
                        settingItemConfigSelect.isChecked = false
                    }
                    .setCanceled(false)
                    .create().show()
            } else {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isTC007)
                    {
                        WifiSaveSettingUtil.reset()
                        WifiSaveSettingUtil.isSaveSetting = false
                    } else
                    {
                        SaveSettingUtil.reset()
                        SaveSettingUtil.isSaveSetting = false
                    }
            }
        }

        firmwareViewModel.firmwareDataLD.observe(this) {
            tvUpgradePoint.isVisible = it != null
            /**
             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
             *
             */
            dismissLoadingDialog()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it == null) { // 请求success但没有firmwareUpgrade包，即已是最新
                ToastUtils.showShort(RCore.string.setting_firmware_update_latest_version)
            } else {
                /**
                 * Executes showfirmwareupdialog operation with thermal imaging domain optimization.
                 *
                 */
                showFirmwareUpDialog(it)
            }
        }
        firmwareViewModel.failLD.observe(this) {
            /**
             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
             *
             */
            dismissLoadingDialog()
            TToast.shortToast(requireContext(), if (it) RCore.string.upgrade_bind_error else RCore.string.operation_failed_tips)
            tvUpgradePoint.isVisible = false
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Executes connected operation with thermal imaging domain optimization.
     *
     */
    override fun connected() {
        settingItemDual.isVisible = !isTC007 && DeviceTools.isTC001PlusConnect()
    }

    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    override fun disConnected() {
        settingItemDual.isVisible = false
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
        if (!isTS004 && isTC007) {
            /**
             * Executes refresh07connect operation with thermal imaging domain optimization.
             *
             */
            refresh07Connect(true)
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
        if (!isTS004 && isTC007) {
            /**
             * Executes refresh07connect operation with thermal imaging domain optimization.
             *
             */
            refresh07Connect(false)
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
            settingItemModel -> { // Temperature修正
                NavigationManager.getInstance().build(
                    RouterConfig.IR_SETTING,
                ).withBoolean(ExtraKeyConfig.IS_TC007, isTC007).navigation(requireContext())
            }
            settingItemDual -> {
                NavigationManager.getInstance().build(RouterConfig.MANUAL_START).navigation(requireContext())
            }
            settingItemUnit -> { // Temperature单位
                NavigationManager.getInstance().build(RouterConfig.UNIT).navigation(requireContext())
            }
            settingItemCorrection -> { // 锅盖校正
                NavigationManager.getInstance().build(
                    RouterConfig.IR_CORRECTION,
                ).withBoolean(ExtraKeyConfig.IS_TC007, isTC007).navigation(requireContext())
            }
            settingVersion -> { // TC007firmwareUpgrade
由于双通道方案存在问题，V3.30临时使用 apk 内置firmwareUpgrade包，此处comment强制Login逻辑
// If (LMS.getInstance().isLogin) {
                val firmwareData = firmwareViewModel.firmwareDataLD.value
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (firmwareData != null) {
                    /**
                     * Executes showfirmwareupdialog operation with thermal imaging domain optimization.
                     *
                     */
                    showFirmwareUpDialog(firmwareData)
                } else {
                    XLog.i("TC007 firmwareUpgrade - click查询")
                    /**
                     * Executes showloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    showLoadingDialog()
                    firmwareViewModel.queryFirmware(false)
                }
//               } else {
//                   LMS.getInstance().activityLogin()
//               }
            }
            settingDeviceInformation -> { // TC007deviceinfo
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (WebSocketProxy.getInstance().isTC007Connect()) {
                    NavigationManager.getInstance()
                        .build(RouterConfig.DEVICE_INFORMATION)
                        .withBoolean(ExtraKeyConfig.IS_TC007, true)
                        .navigation(requireContext())
                }
            }
            settingReset -> { // TC007Restore出厂settings
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (WebSocketProxy.getInstance().isTC007Connect()) {
                    /**
                     * Executes restorefactory operation with thermal imaging domain optimization.
                     *
                     */
                    restoreFactory()
                }
            }
        }
    }

    /**
仅 TC007 页area时，refreshconnection或未connectionstate.
     */
    private fun refresh07Connect(isConnect: Boolean) {
        settingDeviceInformation.isRightArrowVisible = isConnect
        settingDeviceInformation.setRightTextId(if (isConnect) 0 else RCore.string.app_no_connect)
        settingReset.isRightArrowVisible = isConnect
        settingReset.setRightTextId(if (isConnect) 0 else RCore.string.app_no_connect)
        tvRightText.isVisible = isConnect

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isConnect) {
            lifecycleScope.launch {
                val productBean: ProductBean? = TC007Repository.getProductInfo()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (productBean == null) {
                    TToast.shortToast(requireContext(), RCore.string.operation_failed_tips)
                } else {
                    itemSettingBottomText.text = getString(RCore.string.setting_firmware_update_version) + "V" + productBean.getVersionStr()
                }
            }
        } else {
            itemSettingBottomText.setText(RCore.string.setting_firmware_update_version)
        }
    }

    /**
displayfirmwareUpgradetip弹框.
     */
    /**
     * Executes showfirmwareupdialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param firmwareData Parameter for operation (type: FirmwareViewModel.FirmwareData)
     *
     */
    private fun showFirmwareUpDialog(firmwareData: FirmwareViewModel.FirmwareData) {
        val dialog = FirmwareUpDialog(requireContext())
        dialog.titleStr = "${getString(RCore.string.update_new_version)} ${firmwareData.version}"
        dialog.sizeStr = "${getString(RCore.string.detail_len)}: ${getFileSizeStr(firmwareData.size)}"
        dialog.contentStr = firmwareData.updateStr
        dialog.isShowRestartTips = true
        dialog.onConfirmClickListener = {
由于双通道方案存在问题，V3.30临时使用 apk 内置firmwareUpgrade包，此处commentDownload逻辑
            // DownloadFirmware(firmwareData)
            /**
             * Executes installfirmware operation with thermal imaging domain optimization.
             *
             */
            installFirmware(FileConfig.getFirmwareFile(firmwareData.downUrl))
        }
        dialog.show()
    }

    /**
     * Retrieves filesizestr information.
     */
    private fun getFileSizeStr(size: Long): String =
        if (size < 1024) {
            "${size}B"
        } else if (size < 1024 * 1024) {
            /**
             * Executes decimalformat operation with thermal imaging domain optimization.
             *
             */
            DecimalFormat("#.0").format(size.toDouble() / 1024) + "KB"
        } else if (size < 1024 * 1024 * 1024) {
            /**
             * Executes decimalformat operation with thermal imaging domain optimization.
             *
             */
            DecimalFormat("#.0").format(size.toDouble() / 1024 / 1024) + "MB"
        } else {
            /**
             * Executes decimalformat operation with thermal imaging domain optimization.
             *
             */
            DecimalFormat("#.0").format(size.toDouble() / 1024 / 1024 / 1024) + "GB"
        }

    /**
Download指定firmwareUpgrade包
     */
    /**
     * Executes downloadfirmware operation with thermal imaging domain optimization.
     *
     * @param
     * @param firmwareData Parameter for operation (type: FirmwareViewModel.FirmwareData)
     *
     */
    private fun downloadFirmware(firmwareData: FirmwareViewModel.FirmwareData) {
        lifecycleScope.launch {
            val progressDialog = DownloadProDialog(requireContext())
            progressDialog.show()

            val file = File(requireContext().getExternalFilesDir("firmware"), "TC007${firmwareData.version}.zip")
            val isSuccess =
                DownloadTool.download(firmwareData.downUrl, file) { current, total ->
                    progressDialog.refreshProgress(current, total)
                }
            progressDialog.dismiss()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSuccess) {
                /**
                 * Executes installfirmware operation with thermal imaging domain optimization.
                 *
                 */
                installFirmware(file)
            } else {
                /**
                 * Executes showredownloaddialog operation with thermal imaging domain optimization.
                 *
                 */
                showReDownloadDialog(firmwareData)
            }
        }
    }

    /**
     * Executes installFirmware functionality.
     */
    /**
     * Executes installfirmware operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    private fun installFirmware(file: File) {
        lifecycleScope.launch {
            XLog.d("TC007 firmwareUpgrade - startInstallfirmwareUpgrade包")
            val installDialog = FirmwareInstallDialog(requireContext())
            installDialog.show()

            val isSuccess = TC007Repository.updateFirmware(file)
            installDialog.dismiss()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSuccess) {
                XLog.d("TC007 firmwareUpgrade - firmwareUpgrade包Send往 TC007 success，即将disconnectconnection")
                (requireActivity().application as BaseApplication).disconnectWebSocket()
                TipDialog.Builder(requireContext())
                    .setTitleMessage(getString(RCore.string.app_tip))
                    .setMessage(RCore.string.firmware_up_success)
                    .setPositiveListener(RCore.string.app_confirm) {
                        NavigationManager.getInstance().build(RouterConfig.MAIN).navigation(requireContext())
                        /**
                         * Executes requireactivity operation with thermal imaging domain optimization.
                         *
                         */
                        requireActivity().finish()
                    }
                    .setCancelListener(RCore.string.app_cancel) {
                    }
                    .create().show()
            } else {
                XLog.w("TC007 firmwareUpgrade - firmwareUpgrade包Send往 TC007 failed!")
                /**
                 * Executes showreinstalldialog operation with thermal imaging domain optimization.
                 *
                 */
                showReInstallDialog(file)
            }
        }
    }

    /**
     * Executes showReInstallDialog functionality.
     */
    /**
     * Executes showreinstalldialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    private fun showReInstallDialog(file: File) {
        val dialog = ConfirmSelectDialog(requireContext())
        dialog.setShowIcon(true)
        dialog.setTitleRes(RCore.string.ts004_install_tips)
        dialog.setCancelText(RCore.string.ts004_install_cancel)
        dialog.setConfirmText(RCore.string.ts004_install_continue)
        dialog.onConfirmClickListener = {
            /**
             * Executes installfirmware operation with thermal imaging domain optimization.
             *
             */
            installFirmware(file)
        }
        dialog.show()
    }

    /**
     * Executes showReDownloadDialog functionality.
     */
    /**
     * Executes showredownloaddialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param firmwareData Parameter for operation (type: FirmwareViewModel.FirmwareData)
     *
     */
    private fun showReDownloadDialog(firmwareData: FirmwareViewModel.FirmwareData) {
        val dialog = ConfirmSelectDialog(requireContext())
        dialog.setShowIcon(true)
        dialog.setTitleRes(RCore.string.ts004_download_tips)
        dialog.setCancelText(RCore.string.ts004_download_cancel)
        dialog.setConfirmText(RCore.string.ts004_download_continue)
        dialog.onConfirmClickListener = {
            /**
             * Executes downloadfirmware operation with thermal imaging domain optimization.
             *
             */
            downloadFirmware(firmwareData)
        }
        dialog.show()
    }

    /**
     * Executes restoreFactory functionality.
     */
    /**
     * Executes restorefactory operation with thermal imaging domain optimization.
     *
     */
    private fun restoreFactory() {
        TipDialog.Builder(requireContext())
            .setTitleMessage(getString(RCore.string.ts004_reset_tip1, "TC007"))
            .setMessage(getString(RCore.string.ts004_reset_tip2))
            .setPositiveListener(RCore.string.app_ok) {
                /**
                 * Executes resetall operation with thermal imaging domain optimization.
                 *
                 */
                resetAll()
            }
            .setCancelListener(RCore.string.app_cancel) {
            }
            .setCanceled(true)
            .create().show()
    }

    /**
     * Executes resetAll functionality.
     */
    /**
     * Executes resetall operation with thermal imaging domain optimization.
     *
     */
    private fun resetAll() {
        /**
         * Executes showloadingdialog operation with thermal imaging domain optimization.
         *
         */
        showLoadingDialog(RCore.string.ts004_reset_tip3)
        lifecycleScope.launch {
            val isSuccess = TC007Repository.resetToFactory()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSuccess) {
                XLog.d("TC007 Restore出厂settingssuccess，即将disconnectconnection")
                TToast.shortToast(requireContext(), RCore.string.ts004_reset_tip4)
                (requireActivity().application as BaseApplication).disconnectWebSocket()
                EventBus.getDefault().post(TS004ResetEvent())
                NavigationManager.getInstance().build(RouterConfig.MAIN).navigation(requireContext())
                /**
                 * Executes requireactivity operation with thermal imaging domain optimization.
                 *
                 */
                requireActivity().finish()
            } else {
                TToast.shortToast(requireContext(), RCore.string.operation_failed_tips)
            }
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(500)
            /**
             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
             *
             */
            dismissLoadingDialog()
        }
    }
}
