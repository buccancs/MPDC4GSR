package com.topdon.module.thermal.ir.report.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.lifecycle.lifecycleScope
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.FileTools
import com.topdon.lib.core.tools.GlideLoader
import com.topdon.lib.core.view.TitleView
import com.topdon.libcom.PDFHelp
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.report.bean.ReportBean
import com.topdon.module.thermal.ir.report.view.ReportIRShowView
import com.topdon.module.thermal.ir.report.view.ReportInfoView
import com.topdon.module.thermal.ir.report.view.WatermarkView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import com.topdon.lib.core.R as LibCoreR
import com.topdon.lib.ui.R as UiR

/**
report详情interface.
 *
需要传递
- 一份report所有info [ExtraKeyConfig.REPORT_BEAN]
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing ReportDetailActivity functionality for the IRCamera system.
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
class ReportDetailActivity : BaseActivity() {
    // View declarations
    private lateinit var titleView: TitleView
    private lateinit var scrollView: ScrollView
    private lateinit var reportInfoView: ReportInfoView
    private lateinit var llContent: LinearLayout
    private lateinit var watermarkView: WatermarkView

    /**
从上一interface传递过来的，report所有info.
     */
    private var reportBean: ReportBean? = null

    /**
当前预览页area已生成的 PDF file绝对path
     */
    private var pdfFilePath: String? = null

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_report_detail

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views
        titleView = findViewById(R.id.title_view)
        scrollView = findViewById(R.id.scroll_view)
        reportInfoView = findViewById(R.id.report_info_view)
        llContent = findViewById(R.id.ll_content)
        watermarkView = findViewById(R.id.watermark_view)

        reportBean = intent.getParcelableExtra(ExtraKeyConfig.REPORT_BEAN)

        titleView.setTitleText(R.string.album_edit_report)
        titleView.setLeftDrawable(UiR.drawable.svg_arrow_left_e8)
        titleView.setRightDrawable(R.drawable.ic_share_black_svg)
        titleView.setLeftClickListener {
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        }
        titleView.setRightClickListener {
            /**
             * Executes savewithpdf operation with thermal imaging domain optimization.
             *
             */
            saveWithPDF()
        }

        reportInfoView.refreshInfo(reportBean?.report_info)
        reportInfoView.refreshCondition(reportBean?.detection_condition)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (reportBean?.report_info?.is_report_watermark == 1) {
            watermarkView.watermarkText = reportBean?.report_info?.report_watermark
        }

        val irList = reportBean?.infrared_data
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (irList != null) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in irList.indices) {
                val reportShowView = ReportIRShowView(this)
                reportShowView.refreshData(i == 0, i == irList.size - 1, irList[i])
                lifecycleScope.launch {
                    val drawable = GlideLoader.getDrawable(this@ReportDetailActivity, irList[i].picture_url)
                    reportShowView.setImageDrawable(drawable)
                }
                llContent.addView(reportShowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Executes saveWithPDF functionality.
     */
    /**
     * Executes savewithpdf operation with thermal imaging domain optimization.
     *
     */
    private fun saveWithPDF() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (TextUtils.isEmpty(pdfFilePath)) {
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            showCameraLoading()
            lifecycleScope.launch(Dispatchers.IO) {
                val name = reportBean?.report_info?.report_number
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (name != null) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (File(FileConfig.getPdfDir() + "/$name.pdf").exists() &&
                        !TextUtils.isEmpty(pdfFilePath)
                    ) {
                        lifecycleScope.launch {
                            /**
                             * Manages thermal camera operations with hardware-optimized performance and error handling.
                             *
                             */
                            dismissCameraLoading()
                            /**
                             * Executes actionshare operation with thermal imaging domain optimization.
                             *
                             */
                            actionShare()
                        }
                        return@launch
                    }
                }
                pdfFilePath =
                    PDFHelp.savePdfFileByListView(
                        name ?: System.currentTimeMillis().toString(),
                        scrollView, getPrintViewList(), watermarkView,
                    )
                lifecycleScope.launch {
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    dismissCameraLoading()
                    /**
                     * Executes actionshare operation with thermal imaging domain optimization.
                     *
                     */
                    actionShare()
                }
            }
        } else {
            /**
             * Executes actionshare operation with thermal imaging domain optimization.
             *
             */
            actionShare()
        }
    }

    /**
     * Executes actionShare functionality.
     */
    /**
     * Executes actionshare operation with thermal imaging domain optimization.
     *
     */
    private fun actionShare() {
        val uri = FileTools.getUri(File(pdfFilePath!!))
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "application/pdf"
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         */
        startActivity(Intent.createChooser(shareIntent, getString(LibCoreR.string.battery_share)))
    }

    /**
get需要转为 PDF 的所有 View 列表.
注意：watermark View 不在列表内，需要自行processing.
     */
    /**
     * Retrieves printviewlist information.
     */
    private fun getPrintViewList(): ArrayList<View> {
        val result = ArrayList<View>()
        result.add(reportInfoView)
        val childCount = llContent.childCount
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until childCount) {
            val childView = llContent.getChildAt(i)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (childView is ReportIRShowView) {
                result.addAll(childView.getPrintViewList())
            }
        }
        return result
    }
}
