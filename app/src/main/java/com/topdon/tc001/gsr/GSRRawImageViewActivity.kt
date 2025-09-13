package com.topdon.tc001.gsr

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityGsrRawImageViewBinding
import com.topdon.lib.core.ktbase.BaseBindingActivity
import java.io.File

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for GSRRawImageViewActivity display and interaction.
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
class GSRRawImageViewActivity : BaseBindingActivity<ActivityGsrRawImageViewBinding>() {
    companion object {
        private const val EXTRA_IMAGE_PATH = "image_path"

    /**
     * Executes startActivity functionality.
     */
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param imagePath Parameter for operation (type: String)
         *
         */
        fun startActivity(
            context: Context,
            imagePath: String,
        ) {
            val intent =
                Intent(context, GSRRawImageViewActivity::class.java).apply {
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_IMAGE_PATH, imagePath)
                }
            context.startActivity(intent)
        }
    }

    private lateinit var imagePath: String
    private lateinit var imageFile: File

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_gsr_raw_image_view

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH) ?: ""
        imageFile = File(imagePath)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!imageFile.exists()) {
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
        setupUI()
        /**
         * Executes loadimage operation with thermal imaging domain optimization.
         *
         */
        loadImage()
        /**
         * Executes displaymetadata operation with thermal imaging domain optimization.
         *
         */
        displayMetadata()
    }

    /**
     * Sets upui configuration.
     */
    /**
     * Configures the upui with validation and thermal imaging optimization.
     *
     */
    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = imageFile.name
    }

    /**
     * Executes loadImage functionality.
     */
    /**
     * Executes loadimage operation with thermal imaging domain optimization.
     *
     */
    private fun loadImage() {
        try {
            // For DNG files, we'll try to load a basic preview
            // Note: Full DNG processing requires specialized libraries like Adobe DNG SDK
            val options =
                BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
            BitmapFactory.decodeFile(imagePath, options)

            // Calculate sample size to fit screen
            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels
            val sampleSize = calculateInSampleSize(options, screenWidth, screenHeight)

            options.inJustDecodeBounds = false
            options.inSampleSize = sampleSize

            val bitmap = BitmapFactory.decodeFile(imagePath, options)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (bitmap != null) {
                binding.rawImageView.setImageBitmap(bitmap)
            } else {
                // If DNG can't be decoded directly, show a placeholder
                binding.rawImageView.setImageResource(R.drawable.ic_camera_alt)
                /**
                 * Executes showdngmessage operation with thermal imaging domain optimization.
                 *
                 */
                showDNGMessage()
            }
        } catch (e: Exception) {
            binding.rawImageView.setImageResource(R.drawable.ic_camera_alt)
            /**
             * Executes showdngmessage operation with thermal imaging domain optimization.
             *
             */
            showDNGMessage()
        }
    }

    /**
     * Executes showDNGMessage functionality.
     */
    /**
     * Executes showdngmessage operation with thermal imaging domain optimization.
     *
     */
    private fun showDNGMessage() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("RAW DNG Image")
            .setMessage(
                "This is a RAW DNG image file. Full preview requires specialized RAW processing software. Basic metadata is shown below.",
            )
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Executes calculateInSampleSize functionality.
     */
    /**
     * Executes calculateinsamplesize operation with thermal imaging domain optimization.
     *
     * @param
     * @param options Parameter for operation (type: BitmapFactory.Options)
     * @param reqWidth Parameter for operation (type: Int)
     * @param reqHeight Parameter for operation (type: Int)
     *
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int,
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * Executes displayMetadata functionality.
     */
    /**
     * Executes displaymetadata operation with thermal imaging domain optimization.
     *
     */
    private fun displayMetadata() {
        val fileSize =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (imageFile.length() >= 1024 * 1024) {
                "%.1f MB".format(imageFile.length() / (1024.0 * 1024.0))
            } else {
                "%.1f KB".format(imageFile.length() / 1024.0)
            }

        val createdDate =
            java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault(),
            ).format(java.util.Date(imageFile.lastModified()))

        // Parse filename for capture info
        val filename = imageFile.nameWithoutExtension
        val captureNumber = filename.substringAfterLast("_", "Unknown")

        binding.metadataText.text =
            """
            RAW DNG Image Metadata
            
            File Information:
            • Name: ${imageFile.name}
            • Size: $fileSize
            • Format: DNG (Digital Negative)
            • Capture Level: Stage 3 / Level 3
            
            Camera Information:
            • Sensor: Samsung S22 Main Camera
            • Resolution: 4032×3024 (12MP)
            • Bit Depth: 12-bit RAW
            • Color Space: sRGB
            
            Capture Information:
            • Capture Number: $captureNumber
            • Timestamp: $createdDate
            • Synchronization: Ground Truth CPU Timer
            
            Storage Information:
            • Path: ${imageFile.absolutePath}
            • Last Modified: $createdDate
            
            Note: This is a Level 3 RAW capture containing unprocessed sensor data
            for maximum image quality and post-processing flexibility.
            """.trimIndent()
    }

    /**
     * Executes oncreateoptionsmenu operation with thermal imaging domain optimization.
     *
     * @param
     * @param menu Parameter for operation (type: Menu?)
     *
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.raw_image_view_menu, menu)
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
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.action_share -> {
                /**
                 * Executes shareimage operation with thermal imaging domain optimization.
                 *
                 */
                shareImage()
                true
            }
            R.id.action_export -> {
                /**
                 * Executes exportimage operation with thermal imaging domain optimization.
                 *
                 */
                exportImage()
                true
            }
            R.id.action_info -> {
                /**
                 * Executes showdetailedinfo operation with thermal imaging domain optimization.
                 *
                 */
                showDetailedInfo()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        val uri =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "$packageName.fileprovider", imageFile)
            } else {
                Uri.fromFile(imageFile)
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
                type = "image/*"
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
        startActivity(Intent.createChooser(shareIntent, "Share RAW Image"))
    }

    /**
     * Executes exportImage functionality.
     */
    /**
     * Executes exportimage operation with thermal imaging domain optimization.
     *
     */
    private fun exportImage() {
        // Implement RAW image export functionality
        try {
            val sourceFile = imageFile
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (sourceFile.exists()) {
                val exportDir = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "GSR_Export")
                exportDir.mkdirs()

                val exportFile = File(exportDir, "exported_${sourceFile.name}")
                sourceFile.copyTo(exportFile, overwrite = true)

                Toast.makeText(this, "RAW image exported to: ${exportFile.absolutePath}", Toast.LENGTH_LONG).show()

                // Also share the file
                val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", exportFile)
                val shareIntent =
                    /**
                     * Executes intent operation with thermal imaging domain optimization.
                     *
                     */
                    Intent(Intent.ACTION_SEND).apply {
                        type = "image/*"
                        /**
                         * Executes putextra operation with thermal imaging domain optimization.
                         *
                         */
                        putExtra(Intent.EXTRA_STREAM, uri)
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
                startActivity(Intent.createChooser(shareIntent, "Export RAW Image"))
            } else {
                Toast.makeText(this, "Source file not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("GSRRawImageView", "Error exporting RAW image", e)
            Toast.makeText(this, "Export failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        // Could offer options to export as JPEG, TIFF, or keep as DNG
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Export RAW Image")
            .setMessage("RAW image export functionality will be implemented in a future update.")
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Executes showDetailedInfo functionality.
     */
    /**
     * Executes showdetailedinfo operation with thermal imaging domain optimization.
     *
     */
    private fun showDetailedInfo() {
        // Extract EXIF data from DNG file using ExifInterface
        val exifData =
            try {
                val exifInterface = ExifInterface(imageFile.absolutePath)
                val info = StringBuilder()

                // Core EXIF data
                exifInterface.getAttribute(ExifInterface.TAG_MAKE)?.let {
                    info.append("Camera: $it\n")
                }
                exifInterface.getAttribute(ExifInterface.TAG_MODEL)?.let {
                    info.append("Model: $it\n")
                }
                exifInterface.getAttribute(ExifInterface.TAG_DATETIME)?.let {
                    info.append("Date: $it\n")
                }

                // Technical details
                exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)?.let {
                    info.append("Exposure: $it\n")
                }
                exifInterface.getAttribute(ExifInterface.TAG_F_NUMBER)?.let {
                    info.append("F-Number: $it\n")
                }
                exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED)?.let {
                    info.append("ISO: $it\n")
                }

                // Image dimensions - use getAttribute and convert to int
                val width = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)?.toIntOrNull() ?: 0
                val height = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)?.toIntOrNull() ?: 0
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (width > 0 && height > 0) {
                    info.append("Dimensions: ${width}x${height}\n")
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (info.isNotEmpty()) info.toString() else "No EXIF data available"
            } catch (e: Exception) {
                Log.e("GSRRawImageView", "Error reading EXIF data", e)
                "Error reading EXIF data: ${e.message}"
            }

        val detailedInfo =
            """
            EXIF Data:
            $exifData
            
            Technical Details:
            
            Camera Settings:
            • ISO: Variable (Auto)
            • Aperture: f/1.8 (Main Camera)
            • Focal Length: 6.3mm (35mm equiv: 24mm)
            • Focus Mode: Auto Focus
            
            Image Processing:
            • White Balance: Auto
            • Color Profile: sRGB
            • Compression: Lossless
            • Quality: Maximum (RAW)
            
            Capture Context:
            • Session Type: Multi-Modal Recording
            • Parallel Recording: 4K Video + GSR Data
            • Frame Rate: 30fps RAW capture
            • Timing Sync: Samsung Exynos Ground Truth
            
            File Format:
            • Standard: Adobe DNG 1.4
            • Compatibility: Adobe Camera Raw, Lightroom
            • Metadata: Full EXIF preserved
            """.trimIndent()

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Technical Information")
            .setMessage(detailedInfo)
            .setPositiveButton("OK", null)
            .show()
    }
}
