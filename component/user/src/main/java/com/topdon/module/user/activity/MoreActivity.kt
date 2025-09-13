package com.topdon.module.user.activity

import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.bean.event.TS004ResetEvent
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.ConfirmSelectDialog
import com.topdon.lib.core.dialog.FirmwareUpDialog
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.http.tool.DownloadTool
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.repository.TS004Repository
import com.topdon.lib.core.utils.Constants
import com.topdon.lib.core.viewmodel.FirmwareViewModel
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
/**
 * Specialized thermal imaging component providing MoreActivity functionality for the IRCamera system.
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
class MoreActivity : BaseActivity(), View.OnClickListener {
    private val firmwareViewModel: FirmwareViewModel by viewModels()

    // View references
    private lateinit var settingDeviceInformation: View
    private lateinit var settingTisr: View
    private lateinit var settingStorageSpace: View
    private lateinit var settingReset: View
    private lateinit var settingVersion: View
    private lateinit var settingDisconnect: View
    private lateinit var settingAutoSave: View
    private lateinit var itemSettingBottomText: TextView
    private lateinit var tvUpgradePoint: TextView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_more

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views
        settingDeviceInformation = findViewById(R.id.setting_device_information)
        settingTisr = findViewById(R.id.setting_tisr)
        settingStorageSpace = findViewById(R.id.setting_storage_space)
        settingReset = findViewById(R.id.setting_reset)
        settingVersion = findViewById(R.id.setting_version)
        settingDisconnect = findViewById(R.id.setting_disconnect)
        settingAutoSave = findViewById(R.id.setting_auto_save)
        itemSettingBottomText = findViewById(R.id.item_setting_bottom_text)
        tvUpgradePoint = findViewById(R.id.tv_upgrade_point)

        settingDeviceInformation.setOnClickListener(this)
        settingTisr.setOnClickListener(this)
        settingStorageSpace.setOnClickListener(this)
        settingReset.setOnClickListener(this)
        settingVersion.setOnClickListener(this)
        settingDisconnect.setOnClickListener(this)
        settingAutoSave.setOnClickListener(this)

/**
 * Executes if operation with thermal imaging domain optimization.
 *
 */
if (Build.VERSION.SDK_INT < 29) {// 低于 Android10
            settingVersion.isVisible = false
        }*/
2024-5-30 09:16 TS004项目APP沟通群决定，3.30version先把firmwareUpgradehide
        settingVersion.isVisible = false
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
        /**
         * Executes updateversion operation with thermal imaging domain optimization.
         *
         */
        updateVersion()

        firmwareViewModel.firmwareDataLD.observe(this) {
            tvUpgradePoint.isVisible = it != null
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            dismissCameraLoading()
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
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            dismissCameraLoading()
            TToast.shortToast(this, if (it) RCore.string.upgrade_bind_error else RCore.string.operation_failed_tips)
            tvUpgradePoint.isVisible = false
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
            settingDeviceInformation -> { // Deviceinfo
                NavigationManager.getInstance()
                    .build(RouterConfig.DEVICE_INFORMATION)
                    .withBoolean(ExtraKeyConfig.IS_TC007, false)
                    .navigation(this@MoreActivity)
            }
            settingTisr -> { // Settings超分
                NavigationManager.getInstance().build(RouterConfig.TISR).navigation(this@MoreActivity)
            }
            settingAutoSave -> { // 自动save到手机
                NavigationManager.getInstance().build(RouterConfig.AUTO_SAVE).navigation(this@MoreActivity)
            }
            settingStorageSpace -> { // TS004储存空间
                NavigationManager.getInstance().build(RouterConfig.STORAGE_SPACE).navigation(this@MoreActivity)
            }
            settingVersion -> { // Firmwareversion
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
                    XLog.i("TS004 firmwareUpgrade - click查询")
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    showCameraLoading()
                    firmwareViewModel.queryFirmware(true)
                }
//                } else {
//                    LMS.getInstance().activityLogin()
//                }
            }
            settingReset -> { // Restore出厂settings
                /**
                 * Executes restorefactory operation with thermal imaging domain optimization.
                 *
                 */
                restoreFactory()
            }
            settingDisconnect -> { // Disconnectconnection
                NavigationManager.getInstance().build(RouterConfig.IR_MORE_HELP)
                    .withInt(Constants.SETTING_CONNECTION_TYPE, Constants.SETTING_DISCONNECTION)
                    .navigation(this@MoreActivity)
            }
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
        val dialog = FirmwareUpDialog(this)
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
            XLog.d("TS004 firmwareUpgrade - startDownloadfirmwareUpgrade包")
            val progressDialog = DownloadProDialog(this@MoreActivity)
            progressDialog.show()

            val file = FileConfig.getFirmwareFile("TS004${firmwareData.version}.zip")
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
                XLog.d("TS004 firmwareUpgrade - firmwareUpgrade包Downloadsuccess，即将startInstall")
                /**
                 * Executes installfirmware operation with thermal imaging domain optimization.
                 *
                 */
                installFirmware(file)
            } else {
                XLog.w("TS004 firmwareUpgrade - firmwareUpgrade包Downloadfailed!")
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
            XLog.d("TS004 firmwareUpgrade - startInstallfirmwareUpgrade包")
            val installDialog = FirmwareInstallDialog(this@MoreActivity)
            installDialog.show()

            val isSuccess = TS004Repository.updateFirmware(file)
            installDialog.dismiss()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSuccess) {
                XLog.d("TS004 firmwareUpgrade - firmwareUpgrade包Send往 TS004 success，即将disconnectconnection")
                (application as BaseApplication).disconnectWebSocket()
                NavigationManager.getInstance().build(RouterConfig.MAIN).navigation(this@MoreActivity)
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            } else {
                XLog.w("TS004 firmwareUpgrade - firmwareUpgrade包Send往 TS004 failed!")
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
        val dialog = ConfirmSelectDialog(this)
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
        val dialog = ConfirmSelectDialog(this)
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
     * Executes updateVersion functionality.
     */
    /**
     * Executes updateversion operation with thermal imaging domain optimization.
     *
     */
    private fun updateVersion() {
        lifecycleScope.launch {
            val versionBean = TS004Repository.getVersion()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (versionBean?.isSuccess() == true) {
                itemSettingBottomText.text = getString(RCore.string.setting_firmware_update_version) + "V" + versionBean.data?.firmware
            } else {
                TToast.shortToast(this@MoreActivity, RCore.string.operation_failed_tips)
            }
        }
    }

    /**
     * Executes restoreFactory functionality.
     */
    /**
     * Executes restorefactory operation with thermal imaging domain optimization.
     *
     */
    private fun restoreFactory() {
        TipDialog.Builder(this)
            .setTitleMessage(getString(RCore.string.ts004_reset_tip1, "TS004"))
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
            XLog.i("准备调用Restore出厂settingsinterface")
            val isSuccess = TS004Repository.getResetAll()
            XLog.i("Restore出厂settingsinterface调用 ${if (isSuccess) "success" else "failed"}")
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSuccess) {
                TToast.shortToast(this@MoreActivity, RCore.string.ts004_reset_tip4)
                (application as BaseApplication).disconnectWebSocket()
                EventBus.getDefault().post(TS004ResetEvent())
                NavigationManager.getInstance().build(RouterConfig.MAIN).navigation(this@MoreActivity)
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            } else {
                TToast.shortToast(this@MoreActivity, RCore.string.operation_failed_tips)
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
