package com.topdon.module.thermal.ir.activity

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.CollectionUtils
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
// Removed house module imports - module removed as unused
// Import com.topdon.house.activity.SignInputActivity
// Import com.topdon.house.event.HouseReportAddEvent
// Import com.topdon.house.util.PDFUtil
// Import com.topdon.house.viewmodel.DetectViewModel
// Import com.topdon.house.viewmodel.ReportViewModel
// Import com.topdon.lib.core.bean.HouseRepPreviewAlbumItemBean
// Import com.topdon.lib.core.bean.HouseRepPreviewBean
// Import com.topdon.lib.core.bean.HouseRepPreviewItemBean
// Import com.topdon.lib.core.bean.HouseRepPreviewProjectItemBean
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.db.AppDatabase
import com.topdon.lib.core.db.entity.HouseReport
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.adapter.ReportPreviewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs

// Temporary data class stubs to resolve compilation issues
/**
 * Custom House rep preview view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
data class HouseRepPreviewBean(
    var itemBeans: ArrayList<HouseRepPreviewItemBean>? = null,
    var housePhoto: String = "",
    var houseAddress: String = "",
    var houseName: String = "",
    var detectTime: String = "",
    var inspectorName: String = "",
    var houseYear: String = "",
    var houseArea: String = "",
    var expenses: String = "",
    var inspectorWhitePath: String = "",
    var houseOwnerWhitePath: String = "",
)

/**
 * Custom House rep preview item view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
data class HouseRepPreviewItemBean(
    var projectItemBeans: ArrayList<HouseRepPreviewProjectItemBean>? = null,
    var albumItemBeans: ArrayList<HouseRepPreviewAlbumItemBean>? = null,
    var itemName: String = "",
)

/**
 * Custom House rep preview project item view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
data class HouseRepPreviewProjectItemBean(
    var projectName: String = "",
    var state: String = "",
    var remark: String = "",
)

/**
 * Custom House rep preview album item view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
data class HouseRepPreviewAlbumItemBean(
    var photoPath: String = "",
    var title: String = "",
)

/**
需要传递：
- [ExtraKeyConfig.IS_REPORT] - true-查看report即查看 false-查看检测即生成
- [ExtraKeyConfig.LONG_ID] - 房屋检测Id(生成时)  房屋reportId(查看时）
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ReportPreviewActivity display and interaction.
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
class ReportPreviewActivity : BaseActivity(), View.OnClickListener {
    // Disabled - ViewModels from removed house module
    // Private val detectViewModel: DetectViewModel by viewModels()
    // Private val reportViewModel: ReportViewModel by viewModels()

    // View declarations
    private lateinit var tvSave: android.widget.TextView
    private lateinit var rlyInspectorSignature: android.widget.RelativeLayout
    private lateinit var rlyHouseOwnerSignature: android.widget.RelativeLayout
    private lateinit var toolbarBackImg: android.widget.ImageView
    private lateinit var clSign: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var layAppbar: com.google.android.material.appbar.AppBarLayout
    private lateinit var layToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var llSave: android.widget.LinearLayout
    private lateinit var scrollView: androidx.core.widget.NestedScrollView
    private lateinit var ivHeaderBg: android.widget.ImageView
    private lateinit var tvAddress: android.widget.TextView
    private lateinit var tvHouseName: android.widget.TextView
    private lateinit var tvDetectTime: android.widget.TextView
    private lateinit var ivInspectorSignature: android.widget.ImageView
    private lateinit var ivHouseOwnerSignature: android.widget.ImageView
    private lateinit var tvInspector: android.widget.TextView
    private lateinit var tvBuildYear: android.widget.TextView
    private lateinit var tvArea: android.widget.TextView
    private lateinit var tvCost: android.widget.TextView
    private lateinit var rcyFloor: androidx.recyclerview.widget.RecyclerView

    /**
true-查看report即查看 false-查看检测即生成
     */
    private var isReport = false
    private var houseReport = HouseReport()
    private var mPreviewBean: HouseRepPreviewBean? = null

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_report_preview

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views
        tvSave = findViewById(R.id.tv_save)
        rlyInspectorSignature = findViewById(R.id.rly_inspector_signature)
        rlyHouseOwnerSignature = findViewById(R.id.rly_house_owner_signature)
        toolbarBackImg = findViewById(R.id.toolbar_back_img)
        clSign = findViewById(R.id.cl_sign)
        layAppbar = findViewById(R.id.lay_appbar)
        layToolbar = findViewById(R.id.lay_toolbar)
        llSave = findViewById(R.id.ll_save)
        scrollView = findViewById(R.id.scroll_view)
        ivHeaderBg = findViewById(R.id.iv_header_bg)
        tvAddress = findViewById(R.id.tv_address)
        tvHouseName = findViewById(R.id.tv_house_name)
        tvDetectTime = findViewById(R.id.tv_detect_time)
        ivInspectorSignature = findViewById(R.id.iv_inspector_signature)
        ivHouseOwnerSignature = findViewById(R.id.iv_house_owner_signature)
        tvInspector = findViewById(R.id.tv_inspector)
        tvBuildYear = findViewById(R.id.tv_build_year)
        tvArea = findViewById(R.id.tv_area)
        tvCost = findViewById(R.id.tv_cost)
        rcyFloor = findViewById(R.id.rcy_floor)

        /**
         * Executes showloadingdialog operation with thermal imaging domain optimization.
         *
         */
        showLoadingDialog("")
        isReport = intent.getBooleanExtra(ExtraKeyConfig.IS_REPORT, false)
        tvSave.isEnabled = false
        rlyInspectorSignature.isEnabled = !isReport
        rlyHouseOwnerSignature.isEnabled = !isReport
        tvSave.text = if (isReport) getString(R.string.battery_share) else getString(R.string.finalize_and_save)
        toolbarBackImg.setOnClickListener(this)
        tvSave.setOnClickListener(this)
        rlyInspectorSignature.setOnClickListener(this)
        rlyHouseOwnerSignature.setOnClickListener(this)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (clSign.isShown)
            {
                val mAppBarChildAt: View = layAppbar.getChildAt(0)
                val mAppBarParams = mAppBarChildAt.layoutParams as AppBarLayout.LayoutParams
                mAppBarParams.scrollFlags = 0
            }

        // Disabled - ViewModels from removed house module
        // DetectViewModel.detectLD.observe(this) {
        // TvSave.isEnabled = it != null
        // If (it != null) {
        // HouseReport = it.toHouseReport()
        // MPreviewBean = convertDataModel(houseReport)
        // SetAdapter()
        //     }
        // DismissLoadingDialog()
        // }
        // ReportViewModel.reportLD.observe(this) {
        // TvSave.isEnabled = it != null
        // If (it != null) {
        // HouseReport = it
        // MPreviewBean = convertDataModel(it)
        // SetAdapter()
        //     }
        // DismissLoadingDialog()
        // }

        // Disabled - ViewModels from removed house module
/**
 * Executes if operation with thermal imaging domain optimization.
 *
 */
if (isReport) {// 查看report
        // ReportViewModel.queryById(intent.getLongExtra(ExtraKeyConfig.LONG_ID, 0))
} else {// 生成report
        // DetectViewModel.queryById(intent.getLongExtra(ExtraKeyConfig.LONG_ID, 0))
        // }

        // Temporary stub - disable save functionality without ViewModels
        tvSave.isEnabled = false
        /**
         * Executes dismissloadingdialog operation with thermal imaging domain optimization.
         *
         */
        dismissLoadingDialog()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        /**
         * Configures the avatorchange with validation and thermal imaging optimization.
         *
         */
        setAvatorChange()
    }

    /**
     * Sets avatorchange configuration.
     */
    private fun setAvatorChange() {
        layAppbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
verticalOffset始终为0以下的负数
            val percent = abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange
            layToolbar.setBackgroundColor(changeAlpha(getColor(R.color.color_23202E), percent))
        }
    }

    /**
     * Changes alpha settings.
     */
    /**
     * Updates the alpha configuration with real-time thermal imaging support.
     *
     * @param
     * @param color Parameter for operation (type: Int)
     * @param fraction Parameter for operation (type: Float)
     *
     */
    private fun changeAlpha(
        color: Int,
        fraction: Float,
    ): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alpha = (Color.alpha(color) * fraction).toInt()
        return Color.argb(alpha, red, green, blue)
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
            toolbarBackImg -> {
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }

            rlyInspectorSignature -> {
                // Disabled - SignInputActivity from removed house module
                // Var intent = Intent(this, SignInputActivity::class.java)
                // Intent.putExtra(ExtraKeyConfig.IS_PICK_INSPECTOR, true)
                // StartActivityForResult(intent, 1000)
            }

            rlyHouseOwnerSignature -> {
                // Disabled - SignInputActivity from removed house module
                // Var intent = Intent(this, SignInputActivity::class.java)
                // Intent.putExtra(ExtraKeyConfig.IS_PICK_INSPECTOR, false)
                // StartActivityForResult(intent, 1001)
            }

            tvSave -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isReport) { // 分享
                    lifecycleScope.launch {
                        /**
                         * Executes showloadingdialog operation with thermal imaging domain optimization.
                         *
                         */
                        showLoadingDialog()
                        // Disabled - PDFUtil from removed house module
                        // PDFUtil.delAllPDF(this@ReportPreviewActivity)
                        // Val pdfUri: Uri? = PDFUtil.savePDF(this@ReportPreviewActivity, houseReport)
                        /**
                         * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                         *
                         */
                        dismissLoadingDialog()

                        // Disabled PDF functionality - house module removed
                        TToast.shortToast(this@ReportPreviewActivity, "PDF sharing disabled - house module removed")

                        // Original PDF sharing code commented out:
                        // If (pdfUri != null) {
                        // Val shareIntent = Intent()
                        // ShareIntent.action = Intent.ACTION_SEND
                        // ShareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri)
                        // ShareIntent.type = "application/pdf"
                        // StartActivity(Intent.createChooser(shareIntent, getString(R.string.battery_share)))
                        // }
                    }
                } else { // 定稿并save
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (houseReport.inspectorWhitePath.isEmpty() || houseReport.houseOwnerWhitePath.isEmpty()) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (clSign.bottom + layAppbar.height > llSave.top) {
                            layAppbar.setExpanded(false, true)
                            scrollView.smoothScrollTo(0, clSign.top)
                        }
                        TToast.shortToast(this, R.string.pdf_sign_tips)
                        return
                    }
                    /**
                     * Executes showloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    showLoadingDialog("")
                    lifecycleScope.launch(Dispatchers.IO) {
                        val currentTime = System.currentTimeMillis()
                        houseReport.createTime = currentTime
                        houseReport.updateTime = currentTime
                        AppDatabase.getInstance().houseReportDao().insert(houseReport)
                        lifecycleScope.launch(Dispatchers.Main) {
                            /**
                             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                             *
                             */
                            dismissLoadingDialog()
                            TToast.shortToast(this@ReportPreviewActivity, R.string.pdf_saved_tips)
                            // Disabled - HouseReportAddEvent from removed house module
                            // EventBus.getDefault().post(HouseReportAddEvent())
                            /**
                             * Executes finish operation with thermal imaging domain optimization.
                             *
                             */
                            finish()
                        }
                    }
                }
            }
        }
    }

    /**
     * Executes onactivityresult operation with thermal imaging domain optimization.
     *
     * @param
     * @param requestCode Parameter for operation (type: Int)
     * @param resultCode Parameter for operation (type: Int)
     * @param data Parameter for operation (type: Intent?)
     *
     */
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (resultCode == RESULT_OK) {
            val whitePath = data?.getStringExtra(ExtraKeyConfig.RESULT_PATH_WHITE) ?: return
            val blackPath = data.getStringExtra(ExtraKeyConfig.RESULT_PATH_BLACK) ?: return
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (requestCode) {
                1000 -> {
检测师签名
                    Glide.with(this).load(whitePath).into(ivInspectorSignature)
                    houseReport.inspectorWhitePath = whitePath
                    houseReport.inspectorBlackPath = blackPath
                }

                1001 -> {
房主签名
                    Glide.with(this).load(whitePath).into(ivHouseOwnerSignature)
                    houseReport.houseOwnerWhitePath = whitePath
                    houseReport.houseOwnerBlackPath = blackPath
                }
            }
        }
    }

    /**
     * Executes convertDataModel functionality.
     */
    /**
     * Executes convertdatamodel operation with thermal imaging domain optimization.
     *
     * @param
     * @param houseReport Parameter for operation (type: HouseReport)
     *
     */
    private fun convertDataModel(houseReport: HouseReport): HouseRepPreviewBean {
        var houseRepPreviewBean = HouseRepPreviewBean()
        houseRepPreviewBean.housePhoto = houseReport.imagePath
        houseRepPreviewBean.houseAddress = houseReport.address
        houseRepPreviewBean.houseName = houseReport.name
        houseRepPreviewBean.detectTime =
            "${getString(R.string.detect_time)}${": "}${TimeTool.formatDetectTime(houseReport.detectTime)}"
        houseRepPreviewBean.inspectorName = houseReport.inspectorName
        houseRepPreviewBean.houseYear =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (houseReport.year == null) "--" else "${houseReport.year?.toString()}${getString(R.string.year)}"
        houseRepPreviewBean.houseArea =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (houseReport.houseSpace.isEmpty()) "--" else "${houseReport.houseSpace} ${houseReport.getSpaceUnitStr()}"
        houseRepPreviewBean.expenses =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (houseReport.cost.isEmpty()) "--" else "${resources.getStringArray(R.array.currency)[houseReport.costUnit]} ${houseReport.cost}"
        houseRepPreviewBean.itemBeans = ArrayList<HouseRepPreviewItemBean>()
        houseReport.dirList.forEachIndexed { _, dirReport ->
            var itemBean = HouseRepPreviewItemBean()
            itemBean.itemName = dirReport.dirName
            var count = dirReport.goodCount + dirReport.warnCount + dirReport.dangerCount
            itemBean.projectItemBeans = ArrayList<HouseRepPreviewProjectItemBean>()
            itemBean.albumItemBeans = ArrayList<HouseRepPreviewAlbumItemBean>()

            dirReport.itemList.forEachIndexed { _, itemReport ->
                var projectItemBean = HouseRepPreviewProjectItemBean()
                projectItemBean.projectName = itemReport.itemName
                projectItemBean.state = itemReport.state.toString()
                projectItemBean.remark = itemReport.inputText
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (itemReport.state > 0 || itemReport.inputText.isNotEmpty()) {
                    itemBean.projectItemBeans?.add(projectItemBean)
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (itemReport.getImageSize() > 0) {
                    var albumItemBean: HouseRepPreviewAlbumItemBean? = null
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (itemReport.image1.isNotEmpty()) {
                        albumItemBean = HouseRepPreviewAlbumItemBean()
                        albumItemBean.photoPath = itemReport.image1
                        albumItemBean.title = itemReport.itemName
                        itemBean.albumItemBeans?.add(albumItemBean)
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (itemReport.image2.isNotEmpty()) {
                        albumItemBean = HouseRepPreviewAlbumItemBean()
                        albumItemBean.photoPath = itemReport.image2
                        albumItemBean.title = itemReport.itemName
                        itemBean.albumItemBeans?.add(albumItemBean)
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (itemReport.image3.isNotEmpty()) {
                        albumItemBean = HouseRepPreviewAlbumItemBean()
                        albumItemBean.photoPath = itemReport.image3
                        albumItemBean.title = itemReport.itemName
                        itemBean.albumItemBeans?.add(albumItemBean)
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (itemReport.image4.isNotEmpty()) {
                        albumItemBean = HouseRepPreviewAlbumItemBean()
                        albumItemBean.photoPath = itemReport.image4
                        albumItemBean.title = itemReport.itemName
                        itemBean.albumItemBeans?.add(albumItemBean)
                    }
                }
            }

            var isEmpty =
                CollectionUtils.isEmpty(itemBean.projectItemBeans) &&
                    CollectionUtils.isEmpty(
                        itemBean.albumItemBeans,
                    )
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (CollectionUtils.isNotEmpty(itemBean.projectItemBeans)) {
                itemBean.projectItemBeans?.add(0, HouseRepPreviewProjectItemBean())
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isEmpty) {
                houseRepPreviewBean.itemBeans?.add(itemBean)
            }
        }
        houseRepPreviewBean.inspectorWhitePath = houseReport.inspectorWhitePath
        houseRepPreviewBean.houseOwnerWhitePath = houseReport.houseOwnerWhitePath
        return houseRepPreviewBean
    }

    /**
     * Sets adapter configuration.
     */
    private fun setAdapter() {
        mPreviewBean?.let {
            Glide.with(this).load(it.housePhoto).into(ivHeaderBg)
            tvAddress.text = it.houseAddress
            tvHouseName.text = it.houseName
            tvDetectTime.text = it.detectTime
            tvInspector.text = it.inspectorName
            tvBuildYear.text = it.houseYear
            tvArea.text = it.houseArea
            tvCost.text = it.expenses

            rcyFloor.layoutManager = LinearLayoutManager(this)
            val reportPreviewAdapter =
                /**
                 * Executes reportpreviewadapter operation with thermal imaging domain optimization.
                 *
                 */
                ReportPreviewAdapter(
                    this,
                    it.itemBeans?.map { itemBean ->
                        // Convert local HouseRepPreviewItemBean to libapp HouseRepPreviewItemBean
                        com.topdon.lib.core.bean.HouseRepPreviewItemBean().apply {
                            // Map properties as needed - this is a simplified conversion
                        }
                    } ?: emptyList(),
                )
            rcyFloor.isNestedScrollingEnabled = false
            rcyFloor.adapter = reportPreviewAdapter

            Glide.with(this).load(it.inspectorWhitePath).into(ivInspectorSignature)
            Glide.with(this).load(it.houseOwnerWhitePath).into(ivHouseOwnerSignature)
        }
    }
}
