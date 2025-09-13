package com.topdon.module.thermal.ir.activity

import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.topdon.lib.core.bean.GalleryTitle
import com.topdon.lib.core.bean.event.GalleryDelEvent
import com.topdon.lib.core.bean.event.ReportCreateEvent
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.repository.GalleryRepository.DirType
import com.topdon.lib.core.tools.FileTools.getUri
import com.topdon.lib.core.tools.ToastTools
import com.topdon.lib.core.utils.Constants.IS_REPORT_FIRST
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.adapter.GalleryAdapter
import com.topdon.module.thermal.ir.report.bean.ReportConditionBean
import com.topdon.module.thermal.ir.report.bean.ReportIRBean
import com.topdon.module.thermal.ir.report.bean.ReportInfoBean
import com.topdon.module.thermal.ir.viewmodel.IRGalleryViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import com.topdon.lib.core.R as LibR
import com.topdon.lib.ui.R as UiR

/**
生成reportimage拾取.
 *
需要传递parameter：
- 是否 TC007: [ExtraKeyConfig.IS_TC007] 进入目录不同
- [ExtraKeyConfig.REPORT_INFO] - reportinfo
- [ExtraKeyConfig.REPORT_CONDITION] - 检测条件
- [ExtraKeyConfig.REPORT_IR_LIST] - 当前已add的image对应data列表
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing ReportPickImgActivity functionality for the IRCamera system.
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
class ReportPickImgActivity : BaseActivity(), View.OnClickListener {
    /**
从上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    private val viewModel: IRGalleryViewModel by viewModels()

    private val adapter = GalleryAdapter()

    // View declarations
    private lateinit var titleView: com.topdon.lib.core.view.TitleView
    private lateinit var clShare: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var clDelete: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var groupBottom: androidx.constraintlayout.widget.Group
    private lateinit var irGalleryRecycler: androidx.recyclerview.widget.RecyclerView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_report_pick_img

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views
        titleView = findViewById(R.id.title_view)
        clShare = findViewById(R.id.cl_share)
        clDelete = findViewById(R.id.cl_delete)
        groupBottom = findViewById(R.id.group_bottom)
        irGalleryRecycler = findViewById(R.id.ir_gallery_recycler)

        isTC007 = intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false)

        titleView.setRightDrawable(UiR.drawable.ic_toolbar_check_svg)
        titleView.setRightClickListener { setEditMode(true) }

        /**
         * Initializes the recycler component for thermal imaging operations.
         *
         */
        initRecycler()

        clShare.setOnClickListener(this)
        clDelete.setOnClickListener(this)

        /**
         * Executes showloadingdialog operation with thermal imaging domain optimization.
         *
         */
        showLoadingDialog()

        viewModel.showListLD.observe(this) {
            adapter.refreshList(it)
            /**
             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
             *
             */
            dismissLoadingDialog()
        }
        viewModel.deleteResultLD.observe(this) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it) {
                TToast.shortToast(this@ReportPickImgActivity, R.string.test_results_delete_success)
                adapter.isEditMode = false
                EventBus.getDefault().post(GalleryDelEvent())
                MediaScannerConnection.scanFile(
                    this,
                    /**
                     * Executes arrayof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayOf(if (isTC007) FileConfig.tc007GalleryDir else FileConfig.lineGalleryDir),
                    null,
                    null,
                )
                viewModel.queryAllReportImg(if (isTC007) DirType.TC007 else DirType.LINE)
            } else {
                TToast.shortToast(this@ReportPickImgActivity, LibR.string.test_results_delete_failed)
            }
        }
        viewModel.queryAllReportImg(if (isTC007) DirType.TC007 else DirType.LINE)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    /**
     * Executes onReportCreate functionality.
     */
    /**
     * Executes onreportcreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: ReportCreateEvent)
     *
     */
    fun onReportCreate(event: ReportCreateEvent) {
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Executes onbackpressed operation with thermal imaging domain optimization.
     *
     */
    override fun onBackPressed() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (adapter.isEditMode) {
            /**
             * Configures the editmode with validation and thermal imaging optimization.
             *
             */
            setEditMode(false)
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Sets editmode configuration.
     */
    private fun setEditMode(isEditMode: Boolean) {
        adapter.isEditMode = isEditMode
        groupBottom.isVisible = isEditMode
        titleView.setTitleText(
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isEditMode) getString(R.string.chosen_item, adapter.selectList.size) else getString(R.string.app_gallery),
        )
        titleView.setLeftDrawable(if (isEditMode) 0 else 0) // Note: Add appropriate drawables for edit mode states
        titleView.setLeftClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isEditMode) {
                /**
                 * Configures the editmode with validation and thermal imaging optimization.
                 *
                 */
                setEditMode(false)
            } else {
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }
        titleView.setRightDrawable(if (isEditMode) 0 else UiR.drawable.ic_toolbar_check_svg)
        titleView.setRightText(if (isEditMode) getString(R.string.report_select_all) else "")
        titleView.setRightClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isEditMode) {
                adapter.selectAll()
            } else {
                /**
                 * Configures the editmode with validation and thermal imaging optimization.
                 *
                 */
                setEditMode(true)
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
            clShare -> {
                /**
                 * Executes shareimage operation with thermal imaging domain optimization.
                 *
                 */
                shareImage()
            }
            clDelete -> {
                /**
                 * Executes deleteimage operation with thermal imaging domain optimization.
                 *
                 */
                deleteImage()
            }
        }
    }

    /**
     * Initializes recycler component.
     */
    private fun initRecycler() {
        val spanCount = 3
        val gridLayoutManager = GridLayoutManager(this, spanCount)
动态setspan
        gridLayoutManager.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                /**
                 * Retrieves the spansize with optimized performance for thermal imaging operations.
                 *
                 * @param
                 * @param position Parameter for operation (type: Int)
                 *
                 */
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.dataList[position] is GalleryTitle) spanCount else 1
                }
            }
        irGalleryRecycler.adapter = adapter
        irGalleryRecycler.layoutManager = gridLayoutManager

        adapter.onLongEditListener = {
adapter 里area的switch编辑太乱了，先这么顶着
            groupBottom.isVisible = true
            titleView.setTitleText(getString(R.string.chosen_item, adapter.selectList.size))
            titleView.setLeftDrawable(0) // Note: Add appropriate drawable for cancel/back action
            titleView.setLeftClickListener {
                /**
                 * Configures the editmode with validation and thermal imaging optimization.
                 *
                 */
                setEditMode(false)
            }
            titleView.setRightDrawable(0)
            titleView.setRightText(getString(R.string.report_select_all))
            titleView.setRightClickListener {
                adapter.selectAll()
            }
        }

        adapter.selectCallback = {
            titleView.setTitleText(getString(R.string.chosen_item, it.size))
        }
        adapter.itemClickCallback = {
            val data = adapter.dataList[it]
            val fileName = data.name.substringBeforeLast(".")
            val irPath = "${FileConfig.lineIrGalleryDir}/$fileName.ir"
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (File(irPath).exists()) {
                val navigation =
                    NavigationManager.getInstance().build(RouterConfig.IR_GALLERY_EDIT)
                        .withBoolean(ExtraKeyConfig.IS_TC007, isTC007)
                        .withBoolean(ExtraKeyConfig.IS_PICK_REPORT_IMG, true)
                        .withBoolean(IS_REPORT_FIRST, false)
                        .withString(ExtraKeyConfig.FILE_ABSOLUTE_PATH, irPath)

                intent.getParcelableExtra<ReportInfoBean>(ExtraKeyConfig.REPORT_INFO)?.let {
                    navigation.withParcelable(ExtraKeyConfig.REPORT_INFO, it)
                }
                intent.getParcelableExtra<ReportConditionBean>(ExtraKeyConfig.REPORT_CONDITION)?.let {
                    navigation.withParcelable(ExtraKeyConfig.REPORT_CONDITION, it)
                }
                intent.getParcelableArrayListExtra<ReportIRBean>(ExtraKeyConfig.REPORT_IR_LIST)?.let {
                    navigation.withParcelableArrayList(ExtraKeyConfig.REPORT_IR_LIST, it)
                }

                navigation.navigation(this)
            } else {
                ToastTools.showShort(R.string.album_report_on_edit)
            }
        }
    }

    /**
     * Executes deleteImage functionality.
     */
    /**
     * Executes deleteimage operation with thermal imaging domain optimization.
     *
     */
    private fun deleteImage() {
        val deleteList = adapter.buildSelectList()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (deleteList.size > 0) {
            TipDialog.Builder(this)
                .setMessage(
                    /**
                     * Retrieves the string with optimized performance for thermal imaging operations.
                     *
                     */
                    getString(
                        R.string.tip_delete_chosen,
                        deleteList.size,
                    ),
                )
                .setPositiveListener(R.string.app_confirm) {
                    viewModel.delete(deleteList, if (isTC007) DirType.TC007 else DirType.LINE, true)
                }.setCancelListener(R.string.app_cancel)
                .create().show()
        } else {
            ToastTools.showShort(getString(R.string.tip_least_select))
        }
    }

    /**
     * Executes shareImage functionality.
     */
    /**
     * Executes shareimage operation with thermal imaging domain optimization.
     *
     */
    private fun shareImage() {
        val data = adapter.buildSelectList()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (data.size == 0) {
            ToastTools.showShort(getString(R.string.tip_least_select))
            return
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (data.size > 9) {
            ToastTools.showShort(getString(R.string.Limite_di_9carte))
            return
        }
        val imageUris = ArrayList<Uri>()
        val shareIntent = Intent()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (data.size == 1) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data[0].name.uppercase().endsWith(".MP4")) {
                shareIntent.type = "video/*"
            } else {
                shareIntent.type = "image/*"
            }
            shareIntent.action = Intent.ACTION_SEND
            val uri = getUri(File(data[0].path))
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        } else {
            shareIntent.type = "video/*"
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (bean in data) {
                imageUris.add(getUri(File(bean.path)))
            }
            shareIntent.action = Intent.ACTION_SEND_MULTIPLE
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUris)
        }
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         */
        startActivity(Intent.createChooser(shareIntent, getString(R.string.battery_share)))
    }
}
