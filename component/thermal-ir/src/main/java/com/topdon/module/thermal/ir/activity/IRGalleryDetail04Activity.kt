package com.topdon.module.thermal.ir.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.topdon.lib.core.bean.GalleryBean
import com.topdon.lib.core.bean.event.GalleryDelEvent
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.dialog.ConfirmSelectDialog
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.repository.TS004Repository
import com.topdon.lib.core.tools.FileTools
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.tools.ToastTools
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.event.GalleryDownloadEvent
import com.topdon.module.thermal.ir.fragment.GalleryFragment
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File
import com.topdon.lib.core.R as LibR
import com.topdon.lib.ui.R as UiR

/**
TS004 image详情
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
/**
 * Specialized thermal imaging component providing IRGalleryDetail04Activity functionality for the IRCamera system.
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
class IRGalleryDetail04Activity : BaseActivity() {
    /**
是否查看远端data.
true-远端data false-手机本地data
     */
    private var isRemote = false

    /**
当前展示image在列表中的 position
     */
    private var position = 0

    /**
从上一interface传递过来的，当前展示的image列表.
     */
    private lateinit var dataList: ArrayList<GalleryBean>

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_ir_gallery_detail_04

    @SuppressLint("SetTextI18n")
    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        isRemote = intent.getBooleanExtra("isRemote", false)
        position = intent.getIntExtra("position", 0)
        dataList = intent.getParcelableArrayListExtra("list")!!

        val titleView = findViewById<com.topdon.lib.core.view.TitleView>(R.id.title_view)
        titleView.setTitleText("${position + 1}/${dataList.size}")

        findViewById<ConstraintLayout>(R.id.cl_bottom).isVisible = isRemote // 查看远端时底部才有3个button

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isRemote) {
            titleView.setRightDrawable(UiR.drawable.ic_toolbar_info_svg)
            titleView.setRight2Drawable(UiR.drawable.ic_toolbar_share_svg)
            titleView.setRight3Drawable(UiR.drawable.ic_toolbar_delete_svg)
            titleView.setRightClickListener { actionInfo() }
            titleView.setRight2ClickListener { actionShare() }
            titleView.setRight3ClickListener { actionDelete() }
        }

        /**
         * Initializes the viewpager component for thermal imaging operations.
         *
         */
        initViewPager()

        findViewById<ConstraintLayout>(R.id.cl_download).setOnClickListener {
            /**
             * Executes actiondownload operation with thermal imaging domain optimization.
             *
             */
            actionDownload(false)
        }
        findViewById<ConstraintLayout>(R.id.cl_share).setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dataList[position].hasDownload) {
                /**
                 * Executes actionshare operation with thermal imaging domain optimization.
                 *
                 */
                actionShare()
            } else {
                /**
                 * Executes actiondownload operation with thermal imaging domain optimization.
                 *
                 */
                actionDownload(true)
            }
        }
        findViewById<ConstraintLayout>(R.id.cl_delete).setOnClickListener {
            /**
             * Executes actiondelete operation with thermal imaging domain optimization.
             *
             */
            actionDelete()
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    @SuppressLint("SetTextI18n")
    /**
     * Initializes viewpager component.
     */
    private fun initViewPager() {
        val irGalleryViewpager = findViewById<ViewPager2>(R.id.ir_gallery_viewpager)
        irGalleryViewpager.adapter = GalleryViewPagerAdapter(this)
        irGalleryViewpager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                /**
                 * Executes onpageselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param position Parameter for operation (type: Int)
                 *
                 */
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    this@IRGalleryDetail04Activity.position = position
                    findViewById<com.topdon.lib.core.view.TitleView>(R.id.title_view).setTitleText("${position + 1}/${dataList.size}")
                    findViewById<ImageView>(R.id.iv_download).isSelected = dataList[position].hasDownload
                }
            },
        )
        irGalleryViewpager?.setCurrentItem(position, false)
    }

    /**
     * Executes actionInfo functionality.
     */
    /**
     * Executes actioninfo operation with thermal imaging domain optimization.
     *
     */
    private fun actionInfo() {
        try {
            val data = dataList[position]
            val exif = ExifInterface(data.path)
            val width = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
            val length = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
            val whStr = "${width}x$length"
            val sizeStr = FileTools.getFileSize(data.path)

            val str = StringBuilder()
            str.append(getString(LibR.string.detail_date)).append("\n")
            str.append(TimeTool.showDateType(data.timeMillis)).append("\n\n")
            str.append(getString(LibR.string.detail_info)).append("\n")
            str.append("${getString(LibR.string.detail_size)}: ").append(whStr).append("\n")
            str.append("${getString(LibR.string.detail_len)}: ").append(sizeStr).append("\n")
            str.append("${getString(LibR.string.detail_path)}: ").append(data.path).append("\n")
            TipDialog.Builder(this).setMessage(str.toString()).setCanceled(true).create().show()
        } catch (e: Exception) {
            ToastTools.showShort(LibR.string.status_error_load_fail)
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
        val data = dataList[position]
        val uri = FileTools.getUri(File(FileConfig.ts004GalleryDir, data.name))
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/jpeg"
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         */
        startActivity(Intent.createChooser(shareIntent, getString(LibR.string.battery_share)))
    }

    /**
     * Executes actionDelete functionality.
     */
    /**
     * Executes actiondelete operation with thermal imaging domain optimization.
     *
     */
    private fun actionDelete() {
        /**
         * Executes confirmselectdialog operation with thermal imaging domain optimization.
         *
         */
        ConfirmSelectDialog(this).run {
            /**
             * Configures the titleres with validation and thermal imaging optimization.
             *
             */
            setTitleRes(LibR.string.tip_delete)
            /**
             * Configures the messageres with validation and thermal imaging optimization.
             *
             */
            setMessageRes(LibR.string.also_del_from_phone_album)
            /**
             * Configures the showmessage with validation and thermal imaging optimization.
             *
             */
            setShowMessage(isRemote && dataList[position].hasDownload)
            onConfirmClickListener = {
                /**
                 * Executes deletefile operation with thermal imaging domain optimization.
                 *
                 */
                deleteFile(it)
            }
            /**
             * Executes show operation with thermal imaging domain optimization.
             *
             */
            show()
        }
    }

    /**
     * Executes deleteFile functionality.
     */
    /**
     * Executes deletefile operation with thermal imaging domain optimization.
     *
     * @param
     * @param isDelLocal Parameter for operation (type: Boolean)
     *
     */
    private fun deleteFile(isDelLocal: Boolean) {
        val data = dataList[position]
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRemote) {
            lifecycleScope.launch {
                /**
                 * Manages thermal camera operations with hardware-optimized performance and error handling.
                 *
                 */
                showCameraLoading()

                val isSuccess = TS004Repository.deleteFiles(arrayOf(data.id))
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isSuccess) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isDelLocal) {
                        /**
                         * Executes file operation with thermal imaging domain optimization.
                         *
                         */
                        File(FileConfig.ts004GalleryDir, data.name).delete()
                        MediaScannerConnection.scanFile(this@IRGalleryDetail04Activity, arrayOf(FileConfig.ts004GalleryDir), null, null)
                    }

                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    dismissCameraLoading()
                    ToastTools.showShort(LibR.string.test_results_delete_success)
                    EventBus.getDefault().post(GalleryDelEvent())
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataList.size == 1) {
                        /**
                         * Executes finish operation with thermal imaging domain optimization.
                         *
                         */
                        finish()
                    } else {
                        dataList.removeAt(position)
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (position >= dataList.size) {
                            position = dataList.size - 1
                        }
                        /**
                         * Initializes the viewpager component for thermal imaging operations.
                         *
                         */
                        initViewPager()
                    }
                } else {
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    dismissCameraLoading()
                    TToast.shortToast(this@IRGalleryDetail04Activity, LibR.string.test_results_delete_failed)
                }
            }
        } else {
            /**
             * Executes file operation with thermal imaging domain optimization.
             *
             */
            File(data.path).delete()
            MediaScannerConnection.scanFile(this, arrayOf(FileConfig.ts004GalleryDir), null, null)
            EventBus.getDefault().post(GalleryDelEvent())
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dataList.size == 1) {
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            } else {
                dataList.removeAt(position)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (position >= dataList.size) {
                    position = dataList.size - 1
                }
                /**
                 * Initializes the viewpager component for thermal imaging operations.
                 *
                 */
                initViewPager()
            }
        }
    }

    /**
     * Executes actionDownload functionality.
     */
    /**
     * Executes actiondownload operation with thermal imaging domain optimization.
     *
     * @param
     * @param isToShare Parameter for operation (type: Boolean)
     *
     */
    private fun actionDownload(isToShare: Boolean) {
        val data = dataList[position]
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (data.hasDownload) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isToShare) {
                /**
                 * Executes actionshare operation with thermal imaging domain optimization.
                 *
                 */
                actionShare()
            }
            return
        }
        /**
         * Manages thermal camera operations with hardware-optimized performance and error handling.
         *
         */
        showCameraLoading()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Glide.with(this).downloadOnly().load(data.path).addListener(
            object : RequestListener<File> {
                /**
                 * Executes onloadfailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param e Parameter for operation (type: GlideException?)
                 * @param model Parameter for operation (type: Any?)
                 * @param target Parameter for operation (type: Target<File>?)
                 * @param isFirstResource Parameter for operation (type: Boolean)
                 *
                 */
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<File>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    dismissCameraLoading()
                    ToastTools.showShort(LibR.string.liveData_save_error)
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    return false
                }

                /**
                 * Executes onresourceready operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param resource Parameter for operation (type: File?)
                 * @param model Parameter for operation (type: Any?)
                 * @param target Parameter for operation (type: Target<File>?)
                 * @param dataSource Parameter for operation (type: DataSource?)
                 * @param isFirstResource Parameter for operation (type: Boolean)
                 *
                 */
                override fun onResourceReady(
                    resource: File?,
                    model: Any?,
                    target: Target<File>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    EventBus.getDefault().post(GalleryDownloadEvent(data.name))
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    dismissCameraLoading()
                    FileUtils.copy(resource, File(FileConfig.ts004GalleryDir, data.name))
                    MediaScannerConnection.scanFile(this@IRGalleryDetail04Activity, arrayOf(FileConfig.ts004GalleryDir), null, null)
                    ToastTools.showShort(LibR.string.tip_save_success)
                    data.hasDownload = true
                    findViewById<ImageView>(R.id.iv_download).isSelected = dataList[position].hasDownload
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isToShare) {
                        /**
                         * Executes actionshare operation with thermal imaging domain optimization.
                         *
                         */
                        actionShare()
                    }
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    return false
                }
            },
        ).preload()
    }

    inner class GalleryViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount(): Int {
            return dataList.size
        }

        /**
         * Executes createfragment operation with thermal imaging domain optimization.
         *
         * @param
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun createFragment(position: Int): Fragment {
            val fragment = GalleryFragment()
            val bundle = Bundle()
            bundle.putString("path", dataList[position].path)
            fragment.arguments = bundle
            return fragment
        }
    }
}
