package com.topdon.module.thermal.ir.fragment

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.topdon.lib.core.bean.GalleryBean
import com.topdon.lib.core.bean.GalleryTitle
import com.topdon.lib.core.bean.event.GalleryDelEvent
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.config.FileConfig.getGalleryDirByType
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.ConfirmSelectDialog
import com.topdon.lib.core.ktbase.BaseFragment
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.repository.GalleryRepository.DirType
import com.topdon.lib.core.repository.TS004Repository
import com.topdon.lib.core.tools.FileTools.getUri
import com.topdon.lib.core.tools.ToastTools
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.adapter.GalleryAdapter
import com.topdon.module.thermal.ir.event.GalleryAddEvent
import com.topdon.module.thermal.ir.event.GalleryDirChangeEvent
import com.topdon.module.thermal.ir.event.GalleryDownloadEvent
import com.topdon.module.thermal.ir.viewmodel.IRGalleryTabViewModel
import com.topdon.module.thermal.ir.viewmodel.IRGalleryViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import com.topdon.lib.core.R as LibR

/**
图库
 */
/**
/**
 * Specialized thermal imaging component providing IRGalleryFragment functionality for the IRCamera system.
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
class IRGalleryFragment : BaseFragment() {
    /**
从上一interface传递过来的，进入图库时初始的目录type
     */
    private var currentDirType = DirType.LINE

    private val viewModel: IRGalleryViewModel by viewModels()

    private val tabViewModel: IRGalleryTabViewModel by activityViewModels()

    private val adapter = GalleryAdapter()

    // View references
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var clDownload: View
    private lateinit var clShare: View
    private lateinit var clDelete: View
    private lateinit var clBottom: View
    private lateinit var irGalleryRecycler: RecyclerView

    /**
从上一interface传递过来的，当前是查看photo还是查看video.
     */
    private var isVideo = false

    override fun initContentView() = R.layout.fragment_ir_gallery

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views with findViewById
        refreshLayout = requireView().findViewById(R.id.refresh_layout)
        clDownload = requireView().findViewById(R.id.cl_download)
        clShare = requireView().findViewById(R.id.cl_share)
        clDelete = requireView().findViewById(R.id.cl_delete)
        clBottom = requireView().findViewById(R.id.cl_bottom)
        irGalleryRecycler = requireView().findViewById(R.id.ir_gallery_recycler)

        currentDirType =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (arguments?.getInt(ExtraKeyConfig.DIR_TYPE, 0) ?: 0) {
                DirType.TS004_LOCALE.ordinal -> DirType.TS004_LOCALE
                DirType.TS004_REMOTE.ordinal -> DirType.TS004_REMOTE
                DirType.TC007.ordinal -> DirType.TC007
                else -> DirType.LINE
            }

        clDownload.isVisible = currentDirType == DirType.TS004_REMOTE

        /**
         * Initializes the recycler component for thermal imaging operations.
         *
         */
        initRecycler()

        clShare.setOnClickListener {
            val selectList = adapter.buildSelectList()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (selectList.size == 0) {
                ToastTools.showShort(getString(R.string.tip_least_select))
                return@setOnClickListener
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (selectList.size > 9) {
                ToastTools.showShort(getString(R.string.Limite_di_9carte))
                return@setOnClickListener
            }
            /**
             * Executes downloadlist operation with thermal imaging domain optimization.
             *
             */
            downloadList(selectList, true)
        }
        clDelete.setOnClickListener {
            /**
             * Executes showdeletedialog operation with thermal imaging domain optimization.
             *
             */
            showDeleteDialog()
        }
        clDownload.setOnClickListener {
            val selectList = adapter.buildSelectList()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (selectList.size == 0) {
                ToastTools.showShort(getString(R.string.tip_least_select))
                return@setOnClickListener
            }
            /**
             * Executes downloadlist operation with thermal imaging domain optimization.
             *
             */
            downloadList(selectList, false)
        }

        viewModel.pageListLD.observe(this) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it == null) {
                TToast.shortToast(requireContext(), LibR.string.operation_failed_tips)
            }
            refreshLayout.finishRefresh(it != null)
            refreshLayout.finishLoadMore(it != null)
            refreshLayout.setNoMoreData(it != null && it.size < IRGalleryViewModel.PAGE_COUNT)
        }
        viewModel.showListLD.observe(this) {
            adapter.refreshList(it)
        }
        viewModel.deleteResultLD.observe(this) {
            /**
             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
             *
             */
            dismissLoadingDialog()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it) {
                TToast.shortToast(requireContext(), R.string.test_results_delete_success)
                tabViewModel.isEditModeLD.value = false
                MediaScannerConnection.scanFile(
                    /**
                     * Executes requirecontext operation with thermal imaging domain optimization.
                     *
                     */
                    requireContext(),
                    /**
                     * Executes arrayof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayOf(FileConfig.lineGalleryDir, FileConfig.ts004GalleryDir),
                    null,
                    null,
                )
                EventBus.getDefault().post(GalleryDelEvent())
            } else {
                TToast.shortToast(requireContext(), LibR.string.test_results_delete_failed)
            }
        }
        tabViewModel.isEditModeLD.observe(this) {
            adapter.isEditMode = it
            clBottom.isVisible = it
        }
        tabViewModel.selectAllIndex.observe(this) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if ((isVideo && it == 1) || (!isVideo && it == 0)) {
                adapter.selectAll()
            }
        }

        isVideo = arguments?.getBoolean(ExtraKeyConfig.IS_VIDEO) ?: false
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes galleryDirChange functionality.
     */
    /**
     * Executes gallerydirchange operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: GalleryDirChangeEvent)
     *
     */
    fun galleryDirChange(event: GalleryDirChangeEvent) {
        currentDirType = event.dirType
        /**
         * Executes refresh operation with thermal imaging domain optimization.
         *
         */
        refresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes galleryDownload functionality.
     */
    /**
     * Executes gallerydownload operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: GalleryDownloadEvent)
     *
     */
    fun galleryDownload(event: GalleryDownloadEvent) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in adapter.dataList.indices) {
            val data = adapter.dataList[i]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.name == event.filename) {
                data.hasDownload = true
                adapter.notifyItemChanged(i)
                return
            }
        }
        /**
         * Executes refresh operation with thermal imaging domain optimization.
         *
         */
        refresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes galleryAdd functionality.
     */
    /**
     * Executes galleryadd operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: GalleryAddEvent)
     *
     */
    fun galleryAdd(event: GalleryAddEvent) {
        /**
         * Executes refresh operation with thermal imaging domain optimization.
         *
         */
        refresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes galleryDel functionality.
     */
    /**
     * Executes gallerydel operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: GalleryDelEvent)
     *
     */
    fun galleryDel(event: GalleryDelEvent) {
        /**
         * Executes refresh operation with thermal imaging domain optimization.
         *
         */
        refresh()
    }

    /**
     * Initializes recycler component.
     */
    private fun initRecycler() {
        val spanCount = 3
        val gridLayoutManager = GridLayoutManager(requireActivity(), spanCount)
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

        adapter.isTS004Remote = currentDirType == DirType.TS004_REMOTE
        adapter.onLongEditListener = {
            tabViewModel.isEditModeLD.value = true
            clBottom.isVisible = true
        }
        adapter.selectCallback = {
            tabViewModel.selectSizeLD.value = it.size
        }
        adapter.itemClickCallback = {
            val galleryBean: GalleryBean = adapter.dataList[it]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (galleryBean.name.uppercase().endsWith(".MP4")) {
                NavigationManager.getInstance().build(RouterConfig.IR_VIDEO_GSY)
                    .withBoolean("isRemote", currentDirType == DirType.TS004_REMOTE)
                    .withParcelable("data", adapter.dataList[it])
                    .navigation(requireActivity())
            } else {
                val sourceList: ArrayList<GalleryBean> = viewModel.sourceListLD.value ?: ArrayList()
                var position = if (it >= sourceList.size) sourceList.size - 1 else it
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in position downTo 0) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (sourceList[i].path == galleryBean.path) {
                        position = i
                        break
                    }
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (currentDirType == DirType.LINE || currentDirType == DirType.TC007) {
                    NavigationManager.getInstance().build(RouterConfig.IR_GALLERY_DETAIL_01)
                        .withBoolean(ExtraKeyConfig.IS_TC007, currentDirType == DirType.TC007)
                        .withInt("position", position)
                        .withParcelableArrayList("list", sourceList)
                        .navigation(requireActivity())
                } else {
                    NavigationManager.getInstance().build(RouterConfig.IR_GALLERY_DETAIL_04)
                        .withBoolean("isRemote", currentDirType == DirType.TS004_REMOTE)
                        .withInt("position", position)
                        .withParcelableArrayList("list", sourceList)
                        .navigation(requireActivity())
                }
            }
        }

        refreshLayout.setOnRefreshListener {
            /**
             * Executes refresh operation with thermal imaging domain optimization.
             *
             */
            refresh()
        }
        refreshLayout.setOnLoadMoreListener {
            viewModel.queryGalleryByPage(isVideo, currentDirType)
        }
        refreshLayout.setEnableScrollContentWhenLoaded(false)

        refreshLayout.autoRefresh()
    }

    /**
     * Executes refresh functionality.
     */
    /**
     * Executes refresh operation with thermal imaging domain optimization.
     *
     */
    private fun refresh() {
        refreshLayout.setEnableLoadMore(true)
        viewModel.hasLoadPage = 0
        viewModel.queryGalleryByPage(isVideo, currentDirType)
    }

    /**
     * Executes showDeleteDialog functionality.
     */
    /**
     * Executes showdeletedialog operation with thermal imaging domain optimization.
     *
     */
    private fun showDeleteDialog() {
        val deleteList = adapter.buildSelectList()

        var hasOneDownload = false
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (currentDirType == DirType.TS004_REMOTE) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (data in deleteList) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (data.hasDownload) {
                    hasOneDownload = true
                    break
                }
            }
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (deleteList.size > 0) {
            /**
             * Executes confirmselectdialog operation with thermal imaging domain optimization.
             *
             */
            ConfirmSelectDialog(requireContext()).run {
                /**
                 * Configures the titlestr with validation and thermal imaging optimization.
                 *
                 */
                setTitleStr(
                    /**
                     * Retrieves the string with optimized performance for thermal imaging operations.
                     *
                     */
                    getString(
                        R.string.tip_delete_chosen,
                        deleteList.size,
                    ),
                )
                /**
                 * Configures the messageres with validation and thermal imaging optimization.
                 *
                 */
                setMessageRes(R.string.also_del_from_phone_album)
                /**
                 * Configures the showmessage with validation and thermal imaging optimization.
                 *
                 */
                setShowMessage(currentDirType == DirType.TS004_REMOTE && hasOneDownload)
                onConfirmClickListener = {
                    /**
                     * Executes showloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    showLoadingDialog()
                    viewModel.delete(deleteList, currentDirType, it)
                }
                /**
                 * Executes show operation with thermal imaging domain optimization.
                 *
                 */
                show()
            }
        } else {
            ToastTools.showShort(getString(R.string.tip_least_select))
        }
    }

    /**
     * Executes downloadList functionality.
     */
    /**
     * Executes downloadlist operation with thermal imaging domain optimization.
     *
     * @param
     * @param downloadList Parameter for operation (type: List<GalleryBean>)
     * @param isShare Parameter for operation (type: Boolean)
     *
     */
    private fun downloadList(
        downloadList: List<GalleryBean>,
        isShare: Boolean,
    ) {
        val downloadMap = HashMap<String, File>()
        downloadList.forEach {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!it.hasDownload) {
                downloadMap[it.path] = File(FileConfig.ts004GalleryDir, it.name)
            }
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (downloadMap.isEmpty()) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isShare) {
                /**
                 * Executes shareimage operation with thermal imaging domain optimization.
                 *
                 */
                shareImage(downloadList)
            } else {
                ToastTools.showShort(R.string.ts004_download_complete)
            }
            tabViewModel.isEditModeLD.value = false
        } else {
            lifecycleScope.launch {
                (context as? Activity)?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                /**
                 * Executes showloadingdialog operation with thermal imaging domain optimization.
                 *
                 */
                showLoadingDialog()
                val successCount =
                    TS004Repository.downloadList(downloadMap) { path, isSuccess ->
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isSuccess) {
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             */
                            for (galleryBean in downloadList) {
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (galleryBean.path == path) {
                                    galleryBean.hasDownload = true
                                    adapter.notifyDataSetChanged()
                                    break
                                }
                            }
                        }
                    }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (successCount == downloadMap.size) { // 全都Downloadsuccess
                    /**
                     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    dismissLoadingDialog()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isShare) {
                        /**
                         * Executes shareimage operation with thermal imaging domain optimization.
                         *
                         */
                        shareImage(downloadList)
                    } else {
                        ToastTools.showShort(R.string.ts004_download_complete)
                    }
                    tabViewModel.isEditModeLD.value = false
                } else {
                    /**
                     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                     *
                     */
                    dismissLoadingDialog()
                    ToastTools.showShort(LibR.string.liveData_save_error)
                }
                MediaScannerConnection.scanFile(
                    /**
                     * Executes requirecontext operation with thermal imaging domain optimization.
                     *
                     */
                    requireContext(),
                    /**
                     * Executes arrayof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayOf(FileConfig.lineGalleryDir, FileConfig.ts004GalleryDir),
                    null,
                    null,
                )
                (context as? Activity)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    /**
     * Executes shareImage functionality.
     */
    /**
     * Executes shareimage operation with thermal imaging domain optimization.
     *
     * @param
     * @param shareList Parameter for operation (type: List<GalleryBean>)
     *
     */
    private fun shareImage(shareList: List<GalleryBean>) {
        val shareIntent = Intent()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (shareList.size == 1) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (shareList[0].name.uppercase().endsWith(".MP4")) {
                shareIntent.type = "video/*"
            } else {
                shareIntent.type = "image/*"
            }
            shareIntent.action = Intent.ACTION_SEND
            val uri = getUri(File(getGalleryDirByType(currentDirType), shareList[0].name))
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        } else {
            val imageUris = ArrayList<Uri>()
            shareIntent.type = "video/*"
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (bean in shareList) {
                imageUris.add(getUri(File(getGalleryDirByType(currentDirType), bean.name)))
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
