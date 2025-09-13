package com.topdon.module.thermal.fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseViewModelFragment
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.module.thermal.R
import com.topdon.module.thermal.adapter.GalleryAdapter
import com.topdon.module.thermal.viewmodel.GalleryViewModel

/**
image
 */
/**
/**
 * Specialized thermal imaging component providing GalleryVideoFragment functionality for the IRCamera system.
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
class GalleryVideoFragment : BaseViewModelFragment<GalleryViewModel>() {
    private val adapter by lazy { GalleryAdapter(requireContext()) }

    override fun providerVMClass() = GalleryViewModel::class.java

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.fragment_gallery_video

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        val span = if (ScreenUtils.isLandscape()) 6 else 3
        val galleryVideoRecycler = requireView().findViewById<RecyclerView>(R.id.gallery_video_recycler)
        galleryVideoRecycler.layoutManager = GridLayoutManager(requireContext(), span)
        galleryVideoRecycler.adapter = adapter

        viewModel.galleryLiveData.observe(this) {
            adapter.datas = it
        }
        adapter.listener =
            object : GalleryAdapter.OnItemClickListener {
                /**
                 * Executes onclick operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param index Parameter for operation (type: Int)
                 * @param path Parameter for operation (type: String)
                 *
                 */
                override fun onClick(
                    index: Int,
                    path: String,
                ) {
                    /**
                     * Executes openvideo operation with thermal imaging domain optimization.
                     *
                     */
                    openVideo(path)
                }

                /**
                 * Executes onlongclick operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param index Parameter for operation (type: Int)
                 * @param path Parameter for operation (type: String)
                 *
                 */
                override fun onLongClick(
                    index: Int,
                    path: String,
                ) {
                    TipDialog.Builder(requireContext()).setMessage("exportimage")
                        .setPositiveListener("分享") {
// Share(path)
                        }
                        .create().show()
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
     * Executes onstart operation with thermal imaging domain optimization.
     *
     */
    override fun onStart() {
        super.onStart()
        viewModel.getVideoData()
    }

// Fun previewVideo(path: String) {
// Val imageEngine = GlideImageEngine()
//        MNImageBrowser.with(context)
//            .setCurrentPosition(0)
//            .setImageEngine(imageEngine)
//            .setImageUrl(path)
//            .show()
//    }

    /**
     * Executes openVideo functionality.
     */
    /**
     * Executes openvideo operation with thermal imaging domain optimization.
     *
     * @param
     * @param path Parameter for operation (type: String)
     *
     */
    fun openVideo(path: String) {
        NavigationManager.getInstance().build(RouterConfig.VIDEO).withString("video_path", path)
            .navigation(requireContext())
    }
}
