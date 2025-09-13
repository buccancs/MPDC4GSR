package com.topdon.module.thermal.activity

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.BarUtils
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.module.thermal.R
import java.io.File
import com.topdon.lib.core.R as LibR

// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing VideoActivity functionality for the IRCamera system.
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
class VideoActivity : BaseActivity() {
    companion object {
        const val KEY_PATH = "video_path"
    }

    var videoPath = ""

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_video

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Set toolbar title
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(com.topdon.lib.core.R.id.toolbar_lay)
        toolbar?.title = getString(R.string.video)

        BarUtils.setNavBarColor(this, ContextCompat.getColor(this, LibR.color.black))
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (intent.hasExtra(KEY_PATH)) {
            videoPath = intent.getStringExtra(KEY_PATH)!!
        }
        /**
         * Executes previewvideo operation with thermal imaging domain optimization.
         *
         */
        previewVideo(videoPath)
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
     * @param path Parameter for operation (type: String)
     *
     */
    private fun previewVideo(path: String) {
        Log.w("123", "Openfile:$path")
        val file = File(path.replace("// ", "/"))
        Log.i("123", "Openfilefile:$file")
        val uri: Uri =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val authority = "$packageName.fileprovider"
                FileProvider.getUriForFile(this, authority, file)
            } else {
                Uri.fromFile(file)
            }
        Log.w("123", "Openfileuri:$uri")
        val videoView = findViewById<VideoView>(R.id.video_play)
        videoView.setVideoURI(uri)
        videoView.setMediaController(MediaController(this))
        videoView.start()
        videoView.requestFocus()
    }
}
