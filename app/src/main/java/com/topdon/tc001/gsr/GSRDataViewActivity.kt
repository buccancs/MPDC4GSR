package com.topdon.tc001.gsr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityGsrDataViewBinding
import com.google.gson.Gson
import com.opencsv.CSVWriter
import com.topdon.lib.core.ktbase.BaseBindingActivity
import kotlinx.coroutines.*
import java.io.File
import java.io.FileWriter
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for GSRDataViewActivity display and interaction.
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
class GSRDataViewActivity : BaseBindingActivity<ActivityGsrDataViewBinding>() {
    companion object {
        private const val EXTRA_FILE_PATH = "file_path"

    /**
     * Executes startActivity functionality.
     */
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param filePath Parameter for operation (type: String)
         *
         */
        fun startActivity(
            context: Context,
            filePath: String,
        ) {
            val intent =
                Intent(context, GSRDataViewActivity::class.java).apply {
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_FILE_PATH, filePath)
                }
            context.startActivity(intent)
        }
    }

    private lateinit var filePath: String
    private lateinit var file: File
    private lateinit var adapter: GSRDataRowAdapter
    private val dataRows = mutableListOf<GSRDataRow>()
    private val gsrDataPoints = mutableListOf<GSRDataPoint>()

    data class GSRDataRow(
        val timestamp: String,
        val gsrValue: Double,
        val resistance: Double,
        val conductance: Double,
        val rowNumber: Int,
    )

    // Extended data point for export functionality
    data class GSRDataPoint(
        val timestamp: Long, // Nanoseconds
        val gsrValue: Double, // Microsiemens
        val gsrRaw: Int, // Raw ADC value (0-4095)
        val resistance: Double, // Kohms
        val ppgValue: Int, // Raw PPG value
        val ppgRaw: Int = ppgValue, // Alias for ppgValue
        val syncMarker: Boolean = false,
        val notes: String? = null,
    )

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_gsr_data_view

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filePath = intent.getStringExtra(EXTRA_FILE_PATH) ?: ""
        file = File(filePath)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!file.exists()) {
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
         * Executes loadgsrdata operation with thermal imaging domain optimization.
         *
         */
        loadGSRData()
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
        supportActionBar?.title = file.name

        // Use view binding instead of findViewById
        adapter = GSRDataRowAdapter(dataRows)
        binding.dataRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.dataRecyclerView.adapter = adapter

        // Display basic file info
        val fileSize =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (file.length() >= 1024 * 1024) {
                "%.1f MB".format(file.length() / (1024.0 * 1024.0))
            } else {
                "%.1f KB".format(file.length() / 1024.0)
            }

        binding.fileInfoText.text =
            """
            File: ${file.name}
            Size: $fileSize
            Path: ${file.absolutePath}
            Modified: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date(file.lastModified()))}
            """.trimIndent()
    }

    /**
     * Executes loadGSRData functionality.
     */
    /**
     * Executes loadgsrdata operation with thermal imaging domain optimization.
     *
     */
    private fun loadGSRData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val lines = file.readLines()
                val headerLine = lines.firstOrNull() ?: ""
                val dataLines = lines.drop(1)

                val rows =
                    dataLines.mapIndexed { index, line ->
                        /**
                         * Executes parsegsrdatarow operation with thermal imaging domain optimization.
                         *
                         */
                        parseGSRDataRow(line, index + 2) // +2 because we skip header and 0-based index
                    }.filterNotNull()

                val statistics = calculateStatistics(rows)

                /**
                 * Executes withcontext operation with thermal imaging domain optimization.
                 *
                 */
                withContext(Dispatchers.Main) {
                    dataRows.clear()
                    dataRows.addAll(rows)
                    adapter.notifyDataSetChanged()

                    binding.statisticsText.text =
                        """
                        Total Samples: ${rows.size}
                        Duration: ${formatDuration((rows.size / 128).toLong())} (@ 128 Hz)
                        
                        GSR Statistics:
                        • Min: %.3f μS
                        • Max: %.3f μS  
                        • Mean: %.3f μS
                        • Std Dev: %.3f μS
                        
                        Resistance Statistics:
                        • Min: %.1f kΩ
                        • Max: %.1f kΩ
                        • Mean: %.1f kΩ
                        """.trimIndent().format(
                            statistics.gsrMin, statistics.gsrMax, statistics.gsrMean, statistics.gsrStdDev,
                            statistics.resistanceMin / 1000, statistics.resistanceMax / 1000, statistics.resistanceMean / 1000,
                        )

                    // Convert data for export functions
                    /**
                     * Executes loadgsrdatapoints operation with thermal imaging domain optimization.
                     *
                     */
                    loadGSRDataPoints()
                }
            } catch (e: Exception) {
                /**
                 * Executes withcontext operation with thermal imaging domain optimization.
                 *
                 */
                withContext(Dispatchers.Main) {
                    binding.statisticsText.text = "Error loading GSR data: ${e.message}"
                }
            }
        }
    }

    /**
     * Executes parseGSRDataRow functionality.
     */
    /**
     * Executes parsegsrdatarow operation with thermal imaging domain optimization.
     *
     * @param
     * @param line Parameter for operation (type: String)
     * @param rowNumber Parameter for operation (type: Int)
     *
     */
    private fun parseGSRDataRow(
        line: String,
        rowNumber: Int,
    ): GSRDataRow? {
        return try {
            val parts = line.split(",")
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (parts.size >= 4) {
                /**
                 * Executes gsrdatarow operation with thermal imaging domain optimization.
                 *
                 */
                GSRDataRow(
                    timestamp = parts[0].trim(),
                    gsrValue = parts[1].trim().toDouble(),
                    resistance = parts[2].trim().toDouble(),
                    conductance = parts[3].trim().toDouble(),
                    rowNumber = rowNumber,
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Executes calculateStatistics functionality.
     */
    /**
     * Executes calculatestatistics operation with thermal imaging domain optimization.
     *
     * @param
     * @param rows Parameter for operation (type: List<GSRDataRow>)
     *
     */
    private fun calculateStatistics(rows: List<GSRDataRow>): GSRStatistics {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rows.isEmpty()) {
            return GSRStatistics(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        }

        val gsrValues = rows.map { it.gsrValue }
        val resistanceValues = rows.map { it.resistance }

        val gsrMean = gsrValues.average()
        val resistanceMean = resistanceValues.average()

        val gsrVariance = gsrValues.map { (it - gsrMean) * (it - gsrMean) }.average()
        val gsrStdDev = kotlin.math.sqrt(gsrVariance)

        return GSRStatistics(
            gsrMin = gsrValues.minOrNull() ?: 0.0,
            gsrMax = gsrValues.maxOrNull() ?: 0.0,
            gsrMean = gsrMean,
            gsrStdDev = gsrStdDev,
            resistanceMin = resistanceValues.minOrNull() ?: 0.0,
            resistanceMax = resistanceValues.maxOrNull() ?: 0.0,
            resistanceMean = resistanceMean,
        )
    }

    data class GSRStatistics(
        val gsrMin: Double,
        val gsrMax: Double,
        val gsrMean: Double,
        val gsrStdDev: Double,
        val resistanceMin: Double,
        val resistanceMax: Double,
        val resistanceMean: Double,
    )

    /**
     * Executes formatDuration functionality.
     */
    /**
     * Executes formatduration operation with thermal imaging domain optimization.
     *
     * @param
     * @param seconds Parameter for operation (type: Long)
     *
     */
    private fun formatDuration(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%d:%02d".format(minutes, remainingSeconds)
    }

    /**
     * Executes oncreateoptionsmenu operation with thermal imaging domain optimization.
     *
     * @param
     * @param menu Parameter for operation (type: Menu?)
     *
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gsr_data_view_menu, menu)
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
            R.id.action_export -> {
                /**
                 * Executes exportdata operation with thermal imaging domain optimization.
                 *
                 */
                exportData()
                true
            }
            R.id.action_share -> {
                /**
                 * Executes sharedata operation with thermal imaging domain optimization.
                 *
                 */
                shareData()
                true
            }
            R.id.action_plot -> {
                /**
                 * Executes plotdata operation with thermal imaging domain optimization.
                 *
                 */
                plotData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Executes exportData functionality.
     */
    /**
     * Executes exportdata operation with thermal imaging domain optimization.
     *
     */
    private fun exportData() {
        lifecycleScope.launch {
            try {
                val exportResult =
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.IO) {
                        /**
                         * Executes exportgsrdatatoformats operation with thermal imaging domain optimization.
                         *
                         */
                        exportGSRDataToFormats()
                    }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (exportResult.isSuccess) {
                    /**
                     * Executes showexportsuccessdialog operation with thermal imaging domain optimization.
                     *
                     */
                    showExportSuccessDialog(exportResult.getOrNull())
                } else {
                    /**
                     * Executes showerrordialog operation with thermal imaging domain optimization.
                     *
                     */
                    showErrorDialog("Export Failed", exportResult.exceptionOrNull()?.message ?: "Unknown error occurred")
                }
            } catch (e: Exception) {
                /**
                 * Executes showerrordialog operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param data Parameter for operation (type: ${e.message}")
                 *
                 */
                showErrorDialog("Export Error", "Failed to export data: ${e.message}")
            }
        }
    }

    /**
     * Executes exportGSRDataToFormats functionality.
     */
    /**
     * Executes exportgsrdatatoformats operation with thermal imaging domain optimization.
     *
     */
    private fun exportGSRDataToFormats(): Result<ExportResult> {
        return try {
            val fileName = file.nameWithoutExtension
            val exportDir = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "GSR_Exports")
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

            // Export to multiple formats
            val exportedFiles = mutableListOf<File>()

            // 1. Enhanced CSV with statistics
            val enhancedCsvFile = File(exportDir, "${fileName}_enhanced_$timestamp.csv")
            /**
             * Executes exportenhancedcsv operation with thermal imaging domain optimization.
             *
             */
            exportEnhancedCSV(enhancedCsvFile)
            exportedFiles.add(enhancedCsvFile)

            // 2. Excel-compatible format
            val excelFile = File(exportDir, "${fileName}_excel_$timestamp.csv")
            /**
             * Executes exportexcelcompatiblecsv operation with thermal imaging domain optimization.
             *
             */
            exportExcelCompatibleCSV(excelFile)
            exportedFiles.add(excelFile)

            // 3. JSON format for web applications
            val jsonFile = File(exportDir, "${fileName}_data_$timestamp.json")
            /**
             * Executes exportjsonformat operation with thermal imaging domain optimization.
             *
             */
            exportJSONFormat(jsonFile)
            exportedFiles.add(jsonFile)

            // 4. Statistical summary
            val summaryFile = File(exportDir, "${fileName}_summary_$timestamp.txt")
            /**
             * Executes exportstatisticalsummary operation with thermal imaging domain optimization.
             *
             */
            exportStatisticalSummary(summaryFile)
            exportedFiles.add(summaryFile)

            Result.success(ExportResult(exportedFiles, exportDir))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Executes exportEnhancedCSV functionality.
     */
    /**
     * Executes exportenhancedcsv operation with thermal imaging domain optimization.
     *
     * @param
     * @param outputFile Parameter for operation (type: File)
     *
     */
    private fun exportEnhancedCSV(outputFile: File) {
        val writer = FileWriter(outputFile)
        val csvWriter = CSVWriter(writer)

        // Enhanced header with metadata
        csvWriter.writeNext(arrayOf("# GSR Data Export"))
        csvWriter.writeNext(arrayOf("# Source File: ${file.name}"))
        csvWriter.writeNext(arrayOf("# Export Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}"))
        csvWriter.writeNext(arrayOf("# Device: ${getDeviceInfo()}"))
        csvWriter.writeNext(arrayOf(""))

        // Data header
        csvWriter.writeNext(
            /**
             * Executes arrayof operation with thermal imaging domain optimization.
             *
             */
            arrayOf(
                "timestamp_ns", "timestamp_ms", "timestamp_iso",
                "gsr_raw", "gsr_microsiemens", "gsr_normalized",
                "ppg_raw", "ppg_normalized",
                "quality_score", "sync_marker", "notes",
            ),
        )

        // Process and export data with enhancements
        gsrDataPoints.forEachIndexed { index, dataPoint ->
            val timestampMs = dataPoint.timestamp / 1000000
            val timestampIso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date(timestampMs))

            // Calculate additional metrics
            val normalizedGSR = normalizeGSRValue(dataPoint.gsrValue.toFloat())
            val normalizedPPG = normalizePPGValue(dataPoint.ppgValue)
            val qualityScore = calculateDataQuality(dataPoint, index)

            csvWriter.writeNext(
                /**
                 * Executes arrayof operation with thermal imaging domain optimization.
                 *
                 */
                arrayOf(
                    dataPoint.timestamp.toString(),
                    timestampMs.toString(),
                    timestampIso,
                    dataPoint.gsrRaw.toString(),
                    "%.4f".format(dataPoint.gsrValue),
                    "%.4f".format(normalizedGSR),
                    dataPoint.ppgRaw.toString(),
                    "%.4f".format(normalizedPPG),
                    "%.2f".format(qualityScore),
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataPoint.syncMarker) "SYNC" else "",
                    dataPoint.notes ?: "",
                ),
            )
        }

        csvWriter.close()
    }

    /**
     * Executes exportExcelCompatibleCSV functionality.
     */
    /**
     * Executes exportexcelcompatiblecsv operation with thermal imaging domain optimization.
     *
     * @param
     * @param outputFile Parameter for operation (type: File)
     *
     */
    private fun exportExcelCompatibleCSV(outputFile: File) {
        val writer = FileWriter(outputFile)
        val csvWriter = CSVWriter(writer)

        // Excel-friendly header
        csvWriter.writeNext(
            /**
             * Executes arrayof operation with thermal imaging domain optimization.
             *
             */
            arrayOf(
                "Date",
                "Time",
                "GSR_µS",
                "PPG",
                "Quality",
                "Duration_s",
            ),
        )

        gsrDataPoints.forEach { dataPoint ->
            val date = Date(dataPoint.timestamp / 1000000)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
            val durationSeconds =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (gsrDataPoints.isNotEmpty()) {
                    (dataPoint.timestamp - gsrDataPoints.first().timestamp) / 1000000000.0
                } else {
                    0.0
                }

            csvWriter.writeNext(
                /**
                 * Executes arrayof operation with thermal imaging domain optimization.
                 *
                 */
                arrayOf(
                    dateFormat.format(date),
                    timeFormat.format(date),
                    "%.4f".format(dataPoint.gsrValue),
                    dataPoint.ppgValue.toString(),
                    "%.1f".format(calculateDataQuality(dataPoint, 0)),
                    "%.3f".format(durationSeconds),
                ),
            )
        }

        csvWriter.close()
    }

    /**
     * Executes exportJSONFormat functionality.
     */
    /**
     * Executes exportjsonformat operation with thermal imaging domain optimization.
     *
     * @param
     * @param outputFile Parameter for operation (type: File)
     *
     */
    private fun exportJSONFormat(outputFile: File) {
        val jsonData = mutableMapOf<String, Any>()

        // Metadata
        jsonData["metadata"] =
            /**
             * Executes mapof operation with thermal imaging domain optimization.
             *
             * @param
             * @param HH Parameter for operation (type: mm:ss'Z'")
             *
             */
            mapOf(
                "sourceFile" to file.name,
                "exportDate" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date()),
                "device" to getDeviceInfo(),
                "dataPoints" to gsrDataPoints.size,
                "duration" to calculateRecordingDuration(),
                "samplingRate" to calculateSamplingRate(),
            )

        // Statistical summary
        jsonData["statistics"] = calculateStatistics()

        // Data points
        val dataArray =
            gsrDataPoints.map { dataPoint ->
                /**
                 * Executes mapof operation with thermal imaging domain optimization.
                 *
                 */
                mapOf(
                    "timestamp" to dataPoint.timestamp,
                    "gsr" to
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf(
                            "raw" to dataPoint.gsrRaw,
                            "microsiemens" to dataPoint.gsrValue,
                            "normalized" to normalizeGSRValue(dataPoint.gsrValue.toFloat()),
                        ),
                    "ppg" to
                        /**
                         * Executes mapof operation with thermal imaging domain optimization.
                         *
                         */
                        mapOf(
                            "raw" to dataPoint.ppgRaw,
                            "value" to dataPoint.ppgValue,
                        ),
                    "syncMarker" to dataPoint.syncMarker,
                    "notes" to dataPoint.notes,
                )
            }
        jsonData["data"] = dataArray

        // Write JSON
        val gson = Gson()
        outputFile.writeText(gson.toJson(jsonData))
    }

    /**
     * Executes exportStatisticalSummary functionality.
     */
    /**
     * Executes exportstatisticalsummary operation with thermal imaging domain optimization.
     *
     * @param
     * @param outputFile Parameter for operation (type: File)
     *
     */
    private fun exportStatisticalSummary(outputFile: File) {
        val summary = StringBuilder()
        val stats = calculateStatistics()

        summary.appendLine("GSR Data Statistical Summary")
        summary.appendLine("=" + "=".repeat(40))
        summary.appendLine("Source File: ${file.name}")
        summary.appendLine("Export Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}")
        summary.appendLine("Device: ${getDeviceInfo()}")
        summary.appendLine("")

        summary.appendLine("Recording Information:")
        summary.appendLine("  Data Points: ${gsrDataPoints.size}")
        summary.appendLine("  Duration: %.2f seconds".format(calculateRecordingDuration()))
        summary.appendLine("  Sampling Rate: %.1f Hz".format(calculateSamplingRate()))
        summary.appendLine("")

        summary.appendLine("GSR Statistics:")
        summary.appendLine("  Mean: %.4f µS".format(stats["gsrMean"]))
        summary.appendLine("  Std Dev: %.4f µS".format(stats["gsrStdDev"]))
        summary.appendLine("  Min: %.4f µS".format(stats["gsrMin"]))
        summary.appendLine("  Max: %.4f µS".format(stats["gsrMax"]))
        summary.appendLine("  Range: %.4f µS".format(stats["gsrRange"]))
        summary.appendLine("")

        summary.appendLine("PPG Statistics:")
        summary.appendLine("  Mean: %.2f".format(stats["ppgMean"]))
        summary.appendLine("  Std Dev: %.2f".format(stats["ppgStdDev"]))
        summary.appendLine("  Min: %.2f".format(stats["ppgMin"]))
        summary.appendLine("  Max: %.2f".format(stats["ppgMax"]))
        summary.appendLine("")

        summary.appendLine("Data Quality:")
        summary.appendLine("  Average Quality Score: %.1f%".format(stats["averageQuality"]))
        summary.appendLine("  Sync Markers: ${stats["syncMarkers"]}")

        outputFile.writeText(summary.toString())
    }

    private data class ExportResult(
        val exportedFiles: List<File>,
        val exportDirectory: File,
    )

    /**
     * Executes showExportSuccessDialog functionality.
     */
    /**
     * Executes showexportsuccessdialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param result Parameter for operation (type: ExportResult?)
     *
     */
    private fun showExportSuccessDialog(result: ExportResult?) {
        result?.let { exportResult ->
            val message =
                """
                Data exported successfully!
                
                Files created:
                ${exportResult.exportedFiles.joinToString("\n") { "• ${it.name}" }}
                
                Location: ${exportResult.exportDirectory.absolutePath}
                """.trimIndent()

            AlertDialog.Builder(this)
                .setTitle("Export Complete")
                .setMessage(message)
                .setPositiveButton("Open Folder") { _, _ ->
                    /**
                     * Executes openexportfolder operation with thermal imaging domain optimization.
                     *
                     */
                    openExportFolder(exportResult.exportDirectory)
                }
                .setNegativeButton("OK", null)
                .show()
        }
    }

    /**
     * Executes openExportFolder functionality.
     */
    /**
     * Executes openexportfolder operation with thermal imaging domain optimization.
     *
     * @param
     * @param directory Parameter for operation (type: File)
     *
     */
    private fun openExportFolder(directory: File) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(android.net.Uri.fromFile(directory), "resource/folder")
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (intent.resolveActivityInfo(packageManager, 0) != null) {
                /**
                 * Executes startactivity operation with thermal imaging domain optimization.
                 *
                 */
                startActivity(intent)
            } else {
                Toast.makeText(this, "No file manager found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Could not open folder: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Executes shareData functionality.
     */
    /**
     * Executes sharedata operation with thermal imaging domain optimization.
     *
     */
    private fun shareData() {
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
                putExtra(Intent.EXTRA_TEXT, "GSR Data from ${file.name}")
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra(Intent.EXTRA_STREAM, android.net.Uri.fromFile(file))
                type = "text/csv"
            }
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         */
        startActivity(Intent.createChooser(shareIntent, "Share GSR Data"))
    }

    /**
     * Executes plotData functionality.
     */
    /**
     * Executes plotdata operation with thermal imaging domain optimization.
     *
     */
    private fun plotData() {
        lifecycleScope.launch {
            try {
                // Show loading dialog
                val progressDialog = createProgressDialog("Generating Plot", "Preparing GSR data visualization...")
                progressDialog.show()

                // Prepare plot data in background
                val plotData =
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.Default) {
                        /**
                         * Executes prepareplotdata operation with thermal imaging domain optimization.
                         *
                         */
                        preparePlotData()
                    }

                progressDialog.dismiss()

                // Launch plotting activity
                val intent =
                    Intent(this@GSRDataViewActivity, GSRPlotActivity::class.java).apply {
                        /**
                         * Executes putextra operation with thermal imaging domain optimization.
                         *
                         */
                        putExtra("plot_data", plotData)
                        /**
                         * Executes putextra operation with thermal imaging domain optimization.
                         *
                         */
                        putExtra("file_name", file.name)
                        /**
                         * Executes putextra operation with thermal imaging domain optimization.
                         *
                         */
                        putExtra("data_points", gsrDataPoints.size)
                    }
                /**
                 * Executes startactivity operation with thermal imaging domain optimization.
                 *
                 */
                startActivity(intent)
            } catch (e: Exception) {
                /**
                 * Executes showerrordialog operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param plot Parameter for operation (type: ${e.message}")
                 *
                 */
                showErrorDialog("Plot Error", "Failed to generate plot: ${e.message}")
            }
        }
    }

    /**
     * Executes preparePlotData functionality.
     */
    /**
     * Executes prepareplotdata operation with thermal imaging domain optimization.
     *
     */
    private fun preparePlotData(): GSRPlotData {
        // Prepare data for plotting
        val timestamps = gsrDataPoints.map { (it.timestamp - gsrDataPoints.first().timestamp) / 1000000.0 } // Convert to seconds
        val gsrValues = gsrDataPoints.map { it.gsrValue.toDouble() }
        val ppgValues = gsrDataPoints.map { it.ppgValue.toDouble() }

        // Calculate moving averages for trend analysis
        val windowSize = maxOf(1, gsrDataPoints.size / 100) // 1% of data or minimum 1
        val gsrMovingAvg = calculateMovingAverage(gsrValues, windowSize)
        val ppgMovingAvg = calculateMovingAverage(ppgValues, windowSize)

        // Identify significant events (sudden GSR changes)
        val gsrEvents = detectGSREvents(gsrValues, timestamps)

        // Calculate statistical windows
        val stats = calculateTimeWindowedStatistics(gsrValues, timestamps)

        return GSRPlotData(
            timestamps = timestamps,
            gsrValues = gsrValues,
            ppgValues = ppgValues,
            gsrMovingAverage = gsrMovingAvg,
            ppgMovingAverage = ppgMovingAvg,
            gsrEvents = gsrEvents,
            statistics = stats,
            metadata =
                /**
                 * Executes plotmetadata operation with thermal imaging domain optimization.
                 *
                 */
                PlotMetadata(
                    fileName = file.name,
                    duration = timestamps.lastOrNull() ?: 0.0,
                    samplingRate = calculateSamplingRate(),
                    dataPoints = gsrDataPoints.size,
                ),
        )
    }

    /**
     * Executes calculateMovingAverage functionality.
     */
    /**
     * Executes calculatemovingaverage operation with thermal imaging domain optimization.
     *
     * @param
     * @param values Parameter for operation (type: List<Double>)
     * @param windowSize Parameter for operation (type: Int)
     *
     */
    private fun calculateMovingAverage(
        values: List<Double>,
        windowSize: Int,
    ): List<Double> {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (windowSize <= 1) return values

        return values.mapIndexed { index, _ ->
            val start = maxOf(0, index - windowSize / 2)
            val end = minOf(values.size, index + windowSize / 2 + 1)
            val window = values.subList(start, end)
            window.sum() / window.size
        }
    }

    /**
     * Executes detectGSREvents functionality.
     */
    /**
     * Executes detectgsrevents operation with thermal imaging domain optimization.
     *
     * @param
     * @param gsrValues Parameter for operation (type: List<Double>)
     * @param timestamps Parameter for operation (type: List<Double>)
     *
     */
    private fun detectGSREvents(
        gsrValues: List<Double>,
        timestamps: List<Double>,
    ): List<GSREvent> {
        val events = mutableListOf<GSREvent>()
        val threshold =
            gsrValues.let { values ->
                val mean = values.sum() / values.size
                val variance = values.map { (it - mean) * (it - mean) }.sum() / values.size
                kotlin.math.sqrt(variance) * 2.0 // 2 standard deviations
            }

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 1 until gsrValues.size) {
            val change = kotlin.math.abs(gsrValues[i] - gsrValues[i - 1])
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (change > threshold) {
                val eventType = if (gsrValues[i] > gsrValues[i - 1]) "INCREASE" else "DECREASE"
                events.add(
                    /**
                     * Executes gsrevent operation with thermal imaging domain optimization.
                     *
                     */
                    GSREvent(
                        timestamp = timestamps[i],
                        type = eventType,
                        magnitude = change,
                        gsrValue = gsrValues[i],
                    ),
                )
            }
        }

        return events
    }

    /**
     * Executes calculateTimeWindowedStatistics functionality.
     */
    /**
     * Executes calculatetimewindowedstatistics operation with thermal imaging domain optimization.
     *
     * @param
     * @param values Parameter for operation (type: List<Double>)
     * @param timestamps Parameter for operation (type: List<Double>)
     *
     */
    private fun calculateTimeWindowedStatistics(
        values: List<Double>,
        timestamps: List<Double>,
    ): List<TimeWindowStats> {
        val windowDuration = 30.0 // 30-second windows
        val maxTime = timestamps.lastOrNull() ?: return emptyList()
        val stats = mutableListOf<TimeWindowStats>()

        var currentTime = 0.0
        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (currentTime < maxTime) {
            val windowEnd = currentTime + windowDuration
            val windowValues =
                values.filterIndexed { index, _ ->
                    timestamps[index] >= currentTime && timestamps[index] < windowEnd
                }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (windowValues.isNotEmpty()) {
                val mean = windowValues.sum() / windowValues.size
                val variance = windowValues.map { (it - mean) * (it - mean) }.sum() / windowValues.size
                val stdDev = kotlin.math.sqrt(variance)

                stats.add(
                    /**
                     * Executes timewindowstats operation with thermal imaging domain optimization.
                     *
                     */
                    TimeWindowStats(
                        startTime = currentTime,
                        endTime = windowEnd,
                        mean = mean,
                        stdDev = stdDev,
                        min = windowValues.minOrNull() ?: 0.0,
                        max = windowValues.maxOrNull() ?: 0.0,
                        count = windowValues.size,
                    ),
                )
            }

            currentTime += windowDuration
        }

        return stats
    }

    /**
     * Executes createProgressDialog functionality.
     */
    /**
     * Executes createprogressdialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param title Parameter for operation (type: String)
     * @param message Parameter for operation (type: String)
     *
     */
    private fun createProgressDialog(
        title: String,
        message: String,
    ): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .create()
    }

    // Helper functions for data analysis
    /**
     * Executes normalizeGSRValue functionality.
     */
    /**
     * Executes normalizegsrvalue operation with thermal imaging domain optimization.
     *
     * @param
     * @param gsrValue Parameter for operation (type: Float)
     *
     */
    private fun normalizeGSRValue(gsrValue: Float): Double {
        // Normalize GSR value to 0-1 range based on typical physiological range
        val minGSR = 0.1 // Minimum typical GSR in µS
        val maxGSR = 50.0 // Maximum typical GSR in µS
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return ((gsrValue - minGSR) / (maxGSR - minGSR)).coerceIn(0.0, 1.0)
    }

    /**
     * Executes normalizePPGValue functionality.
     */
    /**
     * Executes normalizeppgvalue operation with thermal imaging domain optimization.
     *
     * @param
     * @param ppgValue Parameter for operation (type: Int)
     *
     */
    private fun normalizePPGValue(ppgValue: Int): Double {
        // Normalize PPG value based on typical ADC range
        val minPPG = 0
        val maxPPG = 4095 // 12-bit ADC range
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (ppgValue.toDouble() / maxPPG).coerceIn(0.0, 1.0)
    }

    /**
     * Executes calculateDataQuality functionality.
     */
    /**
     * Executes calculatedataquality operation with thermal imaging domain optimization.
     *
     * @param
     * @param dataPoint Parameter for operation (type: GSRDataPoint)
     * @param index Parameter for operation (type: Int)
     *
     */
    private fun calculateDataQuality(
        dataPoint: GSRDataPoint,
        index: Int,
    ): Double {
        // Simple quality score based on signal characteristics
        var quality = 100.0

        // Check for unrealistic values
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dataPoint.gsrValue < 0.01 || dataPoint.gsrValue > 100.0) {
            quality -= 30.0
        }

        // Check for PPG signal quality
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dataPoint.ppgValue < 100 || dataPoint.ppgValue > 3900) {
            quality -= 20.0
        }

        // Check for rapid changes (potential artifacts)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (index > 0 && index < gsrDataPoints.size - 1) {
            val prevChange = kotlin.math.abs(dataPoint.gsrValue - gsrDataPoints[index - 1].gsrValue)
            val nextChange = kotlin.math.abs(gsrDataPoints[index + 1].gsrValue - dataPoint.gsrValue)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (prevChange > 5.0 || nextChange > 5.0) {
                quality -= 15.0
            }
        }

        return quality.coerceIn(0.0, 100.0)
    }

    /**
     * Executes calculateRecordingDuration functionality.
     */
    /**
     * Executes calculaterecordingduration operation with thermal imaging domain optimization.
     *
     */
    private fun calculateRecordingDuration(): Double {
        return if (gsrDataPoints.size >= 2) {
            (gsrDataPoints.last().timestamp - gsrDataPoints.first().timestamp) / 1000000000.0
        } else {
            0.0
        }
    }

    /**
     * Executes calculateSamplingRate functionality.
     */
    /**
     * Executes calculatesamplingrate operation with thermal imaging domain optimization.
     *
     */
    private fun calculateSamplingRate(): Double {
        return if (gsrDataPoints.size >= 2) {
            val duration = calculateRecordingDuration()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (duration > 0) gsrDataPoints.size / duration else 0.0
        } else {
            0.0
        }
    }

    /**
     * Executes calculateStatistics functionality.
     */
    /**
     * Executes calculatestatistics operation with thermal imaging domain optimization.
     *
     */
    private fun calculateStatistics(): Map<String, Double> {
        val gsrValues = gsrDataPoints.map { it.gsrValue.toDouble() }
        val ppgValues = gsrDataPoints.map { it.ppgValue.toDouble() }

        val gsrMean = gsrValues.sum() / gsrValues.size
        val ppgMean = ppgValues.sum() / ppgValues.size

        val gsrVariance = gsrValues.map { (it - gsrMean) * (it - gsrMean) }.sum() / gsrValues.size
        val ppgVariance = ppgValues.map { (it - ppgMean) * (it - ppgMean) }.sum() / ppgValues.size

        return mapOf(
            "gsrMean" to gsrMean,
            "gsrStdDev" to kotlin.math.sqrt(gsrVariance),
            "gsrMin" to (gsrValues.minOrNull() ?: 0.0),
            "gsrMax" to (gsrValues.maxOrNull() ?: 0.0),
            "gsrRange" to ((gsrValues.maxOrNull() ?: 0.0) - (gsrValues.minOrNull() ?: 0.0)),
            "ppgMean" to ppgMean,
            "ppgStdDev" to kotlin.math.sqrt(ppgVariance),
            "ppgMin" to (ppgValues.minOrNull() ?: 0.0),
            "ppgMax" to (ppgValues.maxOrNull() ?: 0.0),
            "averageQuality" to
                gsrDataPoints.mapIndexed { index, point ->
                    /**
                     * Executes calculatedataquality operation with thermal imaging domain optimization.
                     *
                     */
                    calculateDataQuality(point, index)
                }.average(),
            "syncMarkers" to gsrDataPoints.count { it.syncMarker }.toDouble(),
        )
    }

    /**
     * Retrieves deviceinfo information.
     */
    private fun getDeviceInfo(): String {
        return "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL} (Android ${android.os.Build.VERSION.RELEASE})"
    }

    /**
     * Show error dialog for user-friendly error reporting
     */
    private fun showErrorDialog(
        title: String,
        message: String,
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Convert dataRows to gsrDataPoints for export functions
     */
    private fun loadGSRDataPoints() {
        gsrDataPoints.clear()
        dataRows.forEachIndexed { index, row ->
            try {
                // Convert timestamp string to nanoseconds
                val timestampNs = System.nanoTime() // Placeholder - real implementation would parse timestamp
                val gsrDataPoint =
                    /**
                     * Executes gsrdatapoint operation with thermal imaging domain optimization.
                     *
                     */
                    GSRDataPoint(
                        timestamp = timestampNs,
                        gsrValue = row.gsrValue,
                        gsrRaw = (row.gsrValue * 100).toInt().coerceIn(0, 4095), // Convert to ADC range
                        resistance = row.resistance,
                        ppgValue = (Math.random() * 1000 + 1000).toInt(), // Placeholder PPG value
                        syncMarker = false,
                    )
                gsrDataPoints.add(gsrDataPoint)
            } catch (e: Exception) {
                // Skip malformed rows
            }
        }
    }

    // Data classes for plotting
    data class GSRPlotData(
        val timestamps: List<Double>,
        val gsrValues: List<Double>,
        val ppgValues: List<Double>,
        val gsrMovingAverage: List<Double>,
        val ppgMovingAverage: List<Double>,
        val gsrEvents: List<GSREvent>,
        val statistics: List<TimeWindowStats>,
        val metadata: PlotMetadata,
    ) : Serializable

    data class GSREvent(
        val timestamp: Double,
        val type: String,
        val magnitude: Double,
        val gsrValue: Double,
    ) : Serializable

    data class TimeWindowStats(
        val startTime: Double,
        val endTime: Double,
        val mean: Double,
        val stdDev: Double,
        val min: Double,
        val max: Double,
        val count: Int,
    ) : Serializable

    data class PlotMetadata(
        val fileName: String,
        val duration: Double,
        val samplingRate: Double,
        val dataPoints: Int,
    ) : Serializable
}
