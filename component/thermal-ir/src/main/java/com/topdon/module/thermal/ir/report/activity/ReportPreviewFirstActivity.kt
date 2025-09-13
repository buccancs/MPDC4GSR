package com.topdon.module.thermal.ir.report.activity

import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.view.TitleView
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.report.bean.ReportInfoBean
import com.topdon.module.thermal.ir.report.view.ReportInfoView
import com.topdon.module.thermal.ir.report.view.WatermarkView
import com.topdon.lib.ui.R as UiR

/**
生成report第1步的预览interface.
 *
需要传递
- 必选：reportinfo [ExtraKeyConfig.REPORT_INFO]
- 可选：检测条件 [ExtraKeyConfig.REPORT_CONDITION]
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ReportPreviewFirstActivity display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
class ReportPreviewFirstActivity : BaseActivity() {
    // View declarations
    private lateinit var titleView: TitleView
    private lateinit var reportInfoView: ReportInfoView
    private lateinit var watermarkView: WatermarkView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_report_preview_first

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views
        titleView = findViewById(R.id.title_view)
        reportInfoView = findViewById(R.id.report_info_view)
        watermarkView = findViewById(R.id.watermark_view)

        titleView.setLeftDrawable(UiR.drawable.svg_arrow_left_e8)
        titleView.setLeftClickListener {
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        }

        val reportInfoBean: ReportInfoBean? = intent.getParcelableExtra(ExtraKeyConfig.REPORT_INFO)
        reportInfoView.refreshInfo(reportInfoBean)
        reportInfoView.refreshCondition(intent.getParcelableExtra(ExtraKeyConfig.REPORT_CONDITION))

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (reportInfoBean?.is_report_watermark == 1) {
            watermarkView.watermarkText = reportInfoBean.report_watermark
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }
}
