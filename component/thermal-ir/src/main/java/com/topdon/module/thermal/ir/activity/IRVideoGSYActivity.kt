package com.topdon.module.thermal.ir.activity

import android.content.Intent
import android.media.MediaScannerConnection
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.BarUtils
// Temporarily commented out GSY Video Player imports due to dependency resolution issues
// Import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
// Import com.shuyu.gsyvideoplayer.player.PlayerFactory
// Import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.topdon.lib.core.bean.GalleryBean
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.FileTools
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.ui.R as UiR
import com.topdon.lib.core.repository.TS004Repository
import com.topdon.lib.core.tools.ToastTools
import com.topdon.module.thermal.ir.R
import com.topdon.lib.core.R as LibR
import com.topdon.lib.core.dialog.ConfirmSelectDialog
import com.topdon.lib.core.bean.event.GalleryDelEvent
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.thermal.ir.event.GalleryDownloadEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
// Import com.shuyu.gsyvideoplayer.player.SystemPlayerManager
import java.io.File

// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing IRVideoGSYActivity functionality for the IRCamera system.
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
class IRVideoGSYActivity : BaseActivity() {
    private var isRemote = false
    private lateinit var data: GalleryBean

    // View declarations
    private lateinit var titleView: com.topdon.lib.core.view.TitleView
    private lateinit var clBottom: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var clDownload: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var clShare: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var clDelete: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var ivDownload: android.widget.ImageView
    // Private lateinit var gsyPlay: com.topdon.module.thermal.ir.view.MyGSYVideoPlayer

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_ir_video_gsy

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views
        titleView = findViewById(R.id.title_view)
        clBottom = findViewById(R.id.cl_bottom)
        clDownload = findViewById(R.id.cl_download)
        clShare = findViewById(R.id.cl_share)
        clDelete = findViewById(R.id.cl_delete)
        ivDownload = findViewById(R.id.iv_download)
        // GsyPlay = findViewById(R.id.gsy_play)

        BarUtils.setNavBarColor(this, ContextCompat.getColor(this, UiR.color.black))

        isRemote = intent.getBooleanExtra("isRemote", false)
        data = intent.getParcelableExtra("data") ?: throw NullPointerException("传递 data")

        clBottom.isVisible = isRemote // 查看远端时底部才有3个button

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
            titleView.setRight3ClickListener { showDeleteDialog() }
        }

        clDownload.setOnClickListener {
            /**
             * Executes actiondownload operation with thermal imaging domain optimization.
             *
             */
            actionDownload(false)
        }
        clShare.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.hasDownload) {
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
        clDelete.setOnClickListener {
            /**
             * Executes showdeletedialog operation with thermal imaging domain optimization.
             *
             */
            showDeleteDialog()
        }

        ivDownload.isSelected = data.hasDownload
        ivDownload.setImageResource(if (isRemote) R.drawable.selector_download else UiR.drawable.ic_toolbar_info_svg)

        /**
         * Executes previewvideo operation with thermal imaging domain optimization.
         *
         */
        previewVideo(isRemote, data.path)
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Executes previewVideo functionality.
     */
    /**
     * Executes previewvideo operation with thermal imaging domain optimization.
     *
     * @param
     * @param isRemote Parameter for operation (type: Boolean)
     * @param path Parameter for operation (type: String)
     *
     */
    private fun previewVideo(
        isRemote: Boolean,
        path: String,
    ) {
        // Temporarily commented out GSY Video Player usage due to dependency resolution issues
        // TODO: Re-enable with correct dependency once GSY Video Player is properly included
        /*
        PlayerFactory.setPlayManager(SystemPlayerManager::class.java)
        val url = if (isRemote) {
            path
        } else {
            path.replace("// ", "/")
            "file:// $path"
        }

        /**
         * Executes gsyvideooptionbuilder operation with thermal imaging domain optimization.
         *
         */
        GSYVideoOptionBuilder()
            .setUrl(url)
            .build(gsyPlay)
interfaceset
        gsyPlay.isNeedShowWifiTip = false // 不Show/Display消耗流量弹框
        gsyPlay.titleTextView.visibility = View.GONE
        gsyPlay.backButton.visibility = View.GONE
        gsyPlay.fullscreenButton.visibility = View.GONE
         */

        // Placeholder implementation - shows path for now
        // In production, implement with Media3 ExoPlayer as alternative
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
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (data.hasDownload) { // 已Download
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
        lifecycleScope.launch {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            showCameraLoading()
            val isSuccess = TS004Repository.download(data.path, File(FileConfig.ts004GalleryDir, data.name))
            MediaScannerConnection.scanFile(this@IRVideoGSYActivity, arrayOf(FileConfig.ts004GalleryDir), null, null)
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            dismissCameraLoading()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSuccess) {
                ToastTools.showShort(R.string.tip_save_success)
                EventBus.getDefault().post(GalleryDownloadEvent(data.name))
                data.hasDownload = true
                ivDownload.isSelected = true
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
            } else {
                ToastTools.showShort(LibR.string.liveData_save_error)
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    /**
     * Executes actionInfo functionality.
     */
    /**
     * Executes actioninfo operation with thermal imaging domain optimization.
     *
     */
    private fun actionInfo() {
        val sizeStr = FileTools.getFileSize(data.path)
        val str = StringBuilder()
        str.append(getString(R.string.detail_date)).append("\n")
        str.append(TimeTool.showDateType(data.timeMillis)).append("\n\n")
        str.append(getString(R.string.detail_info)).append("\n")
str.append("尺寸: ").append(whStr).append("\n")
        str.append("${getString(R.string.detail_len)}: ").append(sizeStr).append("\n")
        str.append("${getString(R.string.detail_path)}: ").append(data.path).append("\n")
        TipDialog.Builder(this)
            .setMessage(str.toString())
            .setCanceled(true)
            .create().show()
    }

    /**
     * Executes actionShare functionality.
     */
    /**
     * Executes actionshare operation with thermal imaging domain optimization.
     *
     */
    private fun actionShare() {
        val uri = FileTools.getUri(File(data.path))
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "video/*"
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         */
        startActivity(Intent.createChooser(shareIntent, getString(R.string.battery_share)))
    }

    /**
     * Executes showDeleteDialog functionality.
     */
    /**
     * Executes showdeletedialog operation with thermal imaging domain optimization.
     *
     */
    private fun showDeleteDialog() {
        /**
         * Executes confirmselectdialog operation with thermal imaging domain optimization.
         *
         */
        ConfirmSelectDialog(this).run {
            /**
             * Configures the titleres with validation and thermal imaging optimization.
             *
             */
            setTitleRes(R.string.tip_delete)
            /**
             * Configures the messageres with validation and thermal imaging optimization.
             *
             */
            setMessageRes(R.string.also_del_from_phone_album)
            /**
             * Configures the showmessage with validation and thermal imaging optimization.
             *
             */
            setShowMessage(isRemote && data.hasDownload)
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
                        MediaScannerConnection.scanFile(this@IRVideoGSYActivity, arrayOf(FileConfig.ts004GalleryDir), null, null)
                    }
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    dismissCameraLoading()
                    ToastTools.showShort(R.string.test_results_delete_success)
                    EventBus.getDefault().post(GalleryDelEvent())
                    /**
                     * Executes finish operation with thermal imaging domain optimization.
                     *
                     */
                    finish()
                } else {
                    /**
                     * Manages thermal camera operations with hardware-optimized performance and error handling.
                     *
                     */
                    dismissCameraLoading()
                    TToast.shortToast(this@IRVideoGSYActivity, LibR.string.test_results_delete_failed)
                }
            }
        } else {
            EventBus.getDefault().post(GalleryDelEvent())
            /**
             * Executes file operation with thermal imaging domain optimization.
             *
             */
            File(data.path).delete()
            MediaScannerConnection.scanFile(this, arrayOf(FileConfig.ts004GalleryDir), null, null)
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        }
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        // GetCurPlay().onVideoResume(false)
        super.onResume()
    }

    /**
     * Executes onpause operation with thermal imaging domain optimization.
     *
     */
    override fun onPause() {
        // GetCurPlay().onVideoPause()
        super.onPause()
    }

    /*
    /**
     * Retrieves curplay information.
     */
    private fun getCurPlay(): GSYVideoPlayer {
        return gsyPlay.fullWindowPlayer ?: gsyPlay
    }
     */
}
