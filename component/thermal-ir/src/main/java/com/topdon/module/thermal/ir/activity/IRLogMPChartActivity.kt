package com.topdon.module.thermal.ir.activity

import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.FileTools
import com.topdon.lib.core.tools.ToastTools
import com.topdon.libcom.ExcelUtil
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.viewmodel.IRMonitorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.collections.ArrayList
import com.topdon.lib.core.R as LibR

// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing IRLogMPChartActivity functionality for the IRCamera system.
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
class IRLogMPChartActivity : BaseActivity() {
    private val viewModel: IRMonitorViewModel by viewModels()

    /**
从上一interface传递过来的，当前查看的监控Recordstart时间戳.
     */
    private var startTime = 0L

    private val permissionList by lazy {
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
                    Permission.WRITE_EXTERNAL_STORAGE,
                )
            } else if (this.applicationInfo.targetSdkVersion == 33) {
            /**
             * Executes mutablelistof operation with thermal imaging domain optimization.
             *
             */
            mutableListOf(
                Permission.WRITE_EXTERNAL_STORAGE,
            )
        } else {
            /**
             * Executes mutablelistof operation with thermal imaging domain optimization.
             *
             */
            mutableListOf(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_ir_log_mp_chart

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        startTime = intent.getLongExtra(ExtraKeyConfig.TIME_MILLIS, 0)
        viewModel.detailListLD.observe(this) {
            /**
             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
             *
             */
            dismissLoadingDialog()

            val isPoint = it?.isNotEmpty() == true && it.first().type == "point"
            findViewById<TextView>(R.id.monitor_current_vol).text = getString(if (isPoint) LibR.string.chart_temperature else LibR.string.chart_temperature_high)
            findViewById<TextView>(R.id.monitor_real_vol).visibility = if (isPoint) View.GONE else View.VISIBLE
            findViewById<ImageView>(R.id.monitor_real_img).visibility = if (isPoint) View.GONE else View.VISIBLE

            try {
                val chartView = findViewById<com.topdon.module.thermal.ir.view.ChartLogView>(R.id.log_chart_time_chart)
                chartView.initEntry(it as ArrayList<ThermalEntity>)
            } catch (e: Exception) {
                XLog.e("refresh图表exception:${e.message}")
            }
        }

        findViewById<View>(R.id.btn_ex)?.setOnClickListener {
            TipDialog.Builder(this)
                .setMessage(LibR.string.tip_album_temp_exportfile)
                .setPositiveListener(LibR.string.app_confirm) {
                    val tempData = viewModel.detailListLD.value
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (tempData?.isEmpty() == true) {
                        ToastTools.showShort("No data available")
                    } else {
                        XXPermissions.with(this)
                            .permission(
                                permissionList,
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
                                            lifecycleScope.launch {
                                                /**
                                                 * Executes showloadingdialog operation with thermal imaging domain optimization.
                                                 *
                                                 */
                                                showLoadingDialog()
                                                var filePath: String? = null
                                                /**
                                                 * Executes withcontext operation with thermal imaging domain optimization.
                                                 *
                                                 */
                                                withContext(Dispatchers.IO) {
                                                    tempData?.get(0)?.let {
                                                        filePath = ExcelUtil.exportExcel(tempData as java.util.ArrayList<ThermalEntity>?, "point" == it.type)
                                                    }
                                                }
                                                /**
                                                 * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                                                 *
                                                 */
                                                dismissLoadingDialog()
                                                /**
                                                 * Executes if operation with thermal imaging domain optimization.
                                                 *
                                                 */
                                                if (filePath.isNullOrEmpty()) {
                                                    ToastTools.showShort(LibR.string.liveData_save_error)
                                                } else {
                                                    val uri = FileTools.getUri(File(filePath))
                                                    val shareIntent = Intent()
                                                    shareIntent.action = Intent.ACTION_SEND
                                                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                                                    shareIntent.type = "application/xlsx"
                                                    /**
                                                     * Executes startactivity operation with thermal imaging domain optimization.
                                                     *
                                                     */
                                                    startActivity(Intent.createChooser(shareIntent, getString(LibR.string.battery_share)))
                                                }
                                            }
                                        } else {
                                            ToastTools.showShort(LibR.string.scan_ble_tip_authorize)
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
                                            /**
                                             * Executes if operation with thermal imaging domain optimization.
                                             *
                                             */
                                            if (BaseApplication.instance.isDomestic())
                                                {
                                                    ToastUtils.showShort(getString(LibR.string.app_storage_content))
                                                    return
                                                }
                                            TipDialog.Builder(this@IRLogMPChartActivity)
                                                .setTitleMessage(getString(LibR.string.app_tip))
                                                .setMessage(getString(LibR.string.app_storage_content))
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
                }.setCancelListener(LibR.string.app_cancel) {
                }
                .setCanceled(true)
                .create().show()
        }
        findViewById<TextView>(R.id.tv_save_path)?.text = getString(LibR.string.temp_export_path) + ": " + FileConfig.excelDir
        viewModel.queryDetail(startTime)
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
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * Executes onpause operation with thermal imaging domain optimization.
     *
     */
    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
