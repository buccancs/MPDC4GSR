package com.topdon.tc001.gsr

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.MediaController
import androidx.core.content.FileProvider
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityGsrVideoPlayerBinding
import com.topdon.lib.core.ktbase.BaseBindingActivity
import java.io.File

/**
 * Specialized thermal imaging component providing GSRVideoPlayerActivity functionality for the IRCamera system.
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
class GSRVideoPlayerActivity : BaseBindingActivity<ActivityGsrVideoPlayerBinding>() {
    companion object {
        private const val TAG = "GSRVideoPlayerActivity"
        private const val EXTRA_VIDEO_PATH = "video_path"

    /**
     * Executes startActivity functionality.
     */
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param videoPath Parameter for operation (type: String)
         *
         */
        fun startActivity(
            context: Context,
            videoPath: String,
        ) {
            val intent =
                Intent(context, GSRVideoPlayerActivity::class.java).apply {
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_VIDEO_PATH, videoPath)
                }
            context.startActivity(intent)
        }
    }

    private lateinit var videoPath: String

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_gsr_video_player

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        videoPath = intent.getStringExtra(EXTRA_VIDEO_PATH) ?: ""
        val videoFile = File(videoPath)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!videoFile.exists()) {
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
            return
        }

        /**
         * Configures the upui with validation and thermal imaging optimization.
         *
         */
        setupUI(videoFile)
        /**
         * Executes playvideo operation with thermal imaging domain optimization.
         *
         */
        playVideo(videoFile)
    }

    /**
     * Sets upui configuration.
     */
    /**
     * Configures the upui with validation and thermal imaging optimization.
     *
     * @param
     * @param videoFile Parameter for operation (type: File)
     *
     */
    private fun setupUI(videoFile: File) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = videoFile.name
    }

    /**
     * Executes playVideo functionality.
     */
    /**
     * Executes playvideo operation with thermal imaging domain optimization.
     *
     * @param
     * @param videoFile Parameter for operation (type: File)
     *
     */
    private fun playVideo(videoFile: File) {
        Log.w(TAG, "Opening video file: ${videoFile.absolutePath}")

        val uri: Uri =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val authority = "$packageName.fileprovider"
                FileProvider.getUriForFile(this, authority, videoFile)
            } else {
                Uri.fromFile(videoFile)
            }

        Log.w(TAG, "Video URI: $uri")

        binding.videoView.setVideoURI(uri)
        binding.videoView.setMediaController(MediaController(this))
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            Log.i(TAG, "Video prepared, starting playback")
            // Video is ready to play
            mediaPlayer.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        }
        binding.videoView.setOnErrorListener { _, what: Int, extra: Int ->
            Log.e(TAG, "Video playback error: what=$what, extra=$extra")
            false
        }
        binding.videoView.start()
        binding.videoView.requestFocus()
    }

    /**
     * Executes oncreateoptionsmenu operation with thermal imaging domain optimization.
     *
     * @param
     * @param menu Parameter for operation (type: Menu?)
     *
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.video_player_menu, menu)
        return true
    }

    /**
     * Executes onoptionsitemselected operation with thermal imaging domain optimization.
     *
     * @param
     * @param item Parameter for operation (type: MenuItem)
     *
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                /**
                 * Executes onbackpressed operation with thermal imaging domain optimization.
                 *
                 */
                onBackPressed()
                true
            }
            R.id.action_share -> {
                /**
                 * Executes sharevideo operation with thermal imaging domain optimization.
                 *
                 */
                shareVideo()
                true
            }
            R.id.action_info -> {
                /**
                 * Executes showvideoinfo operation with thermal imaging domain optimization.
                 *
                 */
                showVideoInfo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Executes shareVideo functionality.
     */
    /**
     * Executes sharevideo operation with thermal imaging domain optimization.
     *
     */
    private fun shareVideo() {
        val videoFile = File(videoPath)
        val uri =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "$packageName.fileprovider", videoFile)
            } else {
                Uri.fromFile(videoFile)
            }

        val shareIntent =
            /**
             * Executes intent operation with thermal imaging domain optimization.
             *
             */
            Intent().apply {
                action = Intent.ACTION_SEND
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "video/*"
                /**
                 * Executes addflags operation with thermal imaging domain optimization.
                 *
                 */
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         */
        startActivity(Intent.createChooser(shareIntent, "Share Video"))
    }

    /**
     * Executes showVideoInfo functionality.
     */
    /**
     * Executes showvideoinfo operation with thermal imaging domain optimization.
     *
     */
    private fun showVideoInfo() {
        val videoFile = File(videoPath)
        val fileSize =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (videoFile.length() >= 1024 * 1024 * 1024) {
                "%.1f GB".format(videoFile.length() / (1024.0 * 1024.0 * 1024.0))
            } else if (videoFile.length() >= 1024 * 1024) {
                "%.1f MB".format(videoFile.length() / (1024.0 * 1024.0))
            } else {
                "%.1f KB".format(videoFile.length() / 1024.0)
            }

        val createdDate =
            java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault(),
            ).format(java.util.Date(videoFile.lastModified()))

        val info =
            """
            File: ${videoFile.name}
            Size: $fileSize
            Created: $createdDate
            Path: ${videoFile.absolutePath}
            """.trimIndent()

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Video Information")
            .setMessage(info)
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Executes onpause operation with thermal imaging domain optimization.
     *
     */
    override fun onPause() {
        super.onPause()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (binding.videoView.isPlaying) {
            binding.videoView.pause()
        }
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        // Video will auto-resume if it was playing
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.stopPlayback()
    }
}
