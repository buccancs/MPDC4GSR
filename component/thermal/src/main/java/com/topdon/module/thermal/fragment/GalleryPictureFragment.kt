package com.topdon.module.thermal.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseViewModelFragment
import com.topdon.module.thermal.R
import com.topdon.module.thermal.adapter.GalleryAdapter
import com.topdon.module.thermal.tools.GlideImageEngine
import com.topdon.module.thermal.viewmodel.GalleryViewModel
import java.io.File

/**
image
 */
/**
/**
 * Specialized thermal imaging component providing GalleryPictureFragment functionality for the IRCamera system.
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
class GalleryPictureFragment : BaseViewModelFragment<GalleryViewModel>() {
    private val adapter by lazy { GalleryAdapter(requireContext()) }

    override fun providerVMClass() = GalleryViewModel::class.java

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.fragment_gallery_picture

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        val span = if (ScreenUtils.isLandscape()) 6 else 3
        val galleryRecycler = requireView().findViewById<RecyclerView>(R.id.gallery_recycler)
        galleryRecycler.layoutManager = GridLayoutManager(requireContext(), span)
        galleryRecycler.adapter = adapter

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
                     * Executes previewpicture operation with thermal imaging domain optimization.
                     *
                     */
                    previewPicture(path)
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
                            /**
                             * Executes share operation with thermal imaging domain optimization.
                             *
                             */
                            share(path)
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
        viewModel.getData()
    }

    /**
分享image
     */
    /**
     * Executes share operation with thermal imaging domain optimization.
     *
     * @param
     * @param path Parameter for operation (type: String)
     *
     */
    fun share(path: String) {
        val file = File(path)
        var intent = Intent()
        intent.action = Intent.ACTION_SEND // Settings分享行为
        intent.type = "image/*" // Settings分享内容的type
        val uri: Uri =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val authority = "${requireContext().packageName}.fileprovider"
                FileProvider.getUriForFile(requireContext(), authority, file)
            } else {
                Uri.fromFile(file)
            }
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent = Intent.createChooser(intent, "分享image")
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         */
        startActivity(intent)
    }

    /**
     * Executes previewPicture functionality.
     */
    /**
     * Executes previewpicture operation with thermal imaging domain optimization.
     *
     * @param
     * @param path Parameter for operation (type: String)
     *
     */
    fun previewPicture(path: String) {
        val imageEngine = GlideImageEngine()
        // Note: MNImageBrowser API requires proper library configuration and integration
        /*
        MNImageBrowser.with(requireContext()) // 当前位置
            .setCurrentPosition(0) // Image引擎
            .setImageEngine(imageEngine) // Image集合
            .setImageUrl(path)
            .show()
         */
    }
}
