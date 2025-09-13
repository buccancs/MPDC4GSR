package com.topdon.tc001.gsr

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityGsrPlotBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.topdon.lib.core.ktbase.BaseBindingActivity

/**
 * Specialized thermal imaging component providing GSRPlotActivity functionality for the IRCamera system.
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
class GSRPlotActivity : BaseBindingActivity<ActivityGsrPlotBinding>() {
    private lateinit var plotData: GSRDataViewActivity.GSRPlotData

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_gsr_plot

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup toolbar
        supportActionBar?.apply {
            /**
             * Configures the displayhomeasupenabled with validation and thermal imaging optimization.
             *
             */
            setDisplayHomeAsUpEnabled(true)
            title = "GSR Data Analysis"
        }

        /**
         * Executes loadplotdata operation with thermal imaging domain optimization.
         *
         */
        loadPlotData()
        /**
         * Configures the upcharts with validation and thermal imaging optimization.
         *
         */
        setupCharts()
        /**
         * Executes displaystatistics operation with thermal imaging domain optimization.
         *
         */
        displayStatistics()
    }

    /**
     * Executes loadPlotData functionality.
     */
    /**
     * Executes loadplotdata operation with thermal imaging domain optimization.
     *
     */
    private fun loadPlotData() {
        plotData = intent.getSerializableExtra("plot_data") as GSRDataViewActivity.GSRPlotData
    }

    /**
     * Sets upcharts configuration.
     */
    private fun setupCharts() {
        setupGSRChart()
        setupPPGChart()
    }

    /**
     * Sets upgsrchart configuration.
     */
    private fun setupGSRChart() {
        // Configure GSR chart
        binding.gsrChart.apply {
            description =
                /**
                 * Executes description operation with thermal imaging domain optimization.
                 *
                 */
                Description().apply {
                    text = "GSR (µS) over Time"
                    textSize = 12f
                }

            // Configure X-axis
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = TimeFormatter()
                granularity = 1f
                labelCount = 6
            }

            // Configure Y-axis
            axisLeft.apply {
                /**
                 * Configures the drawgridlines with validation and thermal imaging optimization.
                 *
                 */
                setDrawGridLines(true)
                gridColor = Color.LTGRAY
            }
            axisRight.isEnabled = false

            // Enable zoom and pan
            /**
             * Configures the touchenabled with validation and thermal imaging optimization.
             *
             */
            setTouchEnabled(true)
            isDragEnabled = true
            /**
             * Configures the scaleenabled with validation and thermal imaging optimization.
             *
             */
            setScaleEnabled(true)
            /**
             * Configures the pinchzoom with validation and thermal imaging optimization.
             *
             */
            setPinchZoom(true)
        }

        // Create GSR data sets
        val gsrEntries =
            plotData.timestamps.mapIndexed { index, timestamp ->
                /**
                 * Executes entry operation with thermal imaging domain optimization.
                 *
                 */
                Entry(timestamp.toFloat(), plotData.gsrValues[index].toFloat())
            }

        val gsrMovingAvgEntries =
            plotData.timestamps.mapIndexed { index, timestamp ->
                /**
                 * Executes entry operation with thermal imaging domain optimization.
                 *
                 */
                Entry(timestamp.toFloat(), plotData.gsrMovingAverage[index].toFloat())
            }

        val gsrDataSet =
            /**
             * Executes linedataset operation with thermal imaging domain optimization.
             *
             */
            LineDataSet(gsrEntries, "GSR Raw").apply {
                color = Color.BLUE
                /**
                 * Configures the drawcircles with validation and thermal imaging optimization.
                 *
                 */
                setDrawCircles(false)
                lineWidth = 1.5f
                /**
                 * Configures the drawvalues with validation and thermal imaging optimization.
                 *
                 */
                setDrawValues(false)
            }

        val gsrAvgDataSet =
            /**
             * Executes linedataset operation with thermal imaging domain optimization.
             *
             */
            LineDataSet(gsrMovingAvgEntries, "GSR Moving Average").apply {
                color = Color.RED
                /**
                 * Configures the drawcircles with validation and thermal imaging optimization.
                 *
                 */
                setDrawCircles(false)
                lineWidth = 2f
                /**
                 * Configures the drawvalues with validation and thermal imaging optimization.
                 *
                 */
                setDrawValues(false)
            }

        // Set data to chart
        binding.gsrChart.data = LineData(gsrDataSet, gsrAvgDataSet)
        binding.gsrChart.invalidate()
    }

    /**
     * Sets upppgchart configuration.
     */
    private fun setupPPGChart() {
        // Configure PPG chart
        binding.ppgChart.apply {
            description =
                /**
                 * Executes description operation with thermal imaging domain optimization.
                 *
                 */
                Description().apply {
                    text = "PPG Signal over Time"
                    textSize = 12f
                }

            // Configure X-axis
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = TimeFormatter()
                granularity = 1f
                labelCount = 6
            }

            // Configure Y-axis
            axisLeft.apply {
                /**
                 * Configures the drawgridlines with validation and thermal imaging optimization.
                 *
                 */
                setDrawGridLines(true)
                gridColor = Color.LTGRAY
            }
            axisRight.isEnabled = false

            // Enable zoom and pan
            /**
             * Configures the touchenabled with validation and thermal imaging optimization.
             *
             */
            setTouchEnabled(true)
            isDragEnabled = true
            /**
             * Configures the scaleenabled with validation and thermal imaging optimization.
             *
             */
            setScaleEnabled(true)
            /**
             * Configures the pinchzoom with validation and thermal imaging optimization.
             *
             */
            setPinchZoom(true)
        }

        // Create PPG data sets
        val ppgEntries =
            plotData.timestamps.mapIndexed { index, timestamp ->
                /**
                 * Executes entry operation with thermal imaging domain optimization.
                 *
                 */
                Entry(timestamp.toFloat(), plotData.ppgValues[index].toFloat())
            }

        val ppgMovingAvgEntries =
            plotData.timestamps.mapIndexed { index, timestamp ->
                /**
                 * Executes entry operation with thermal imaging domain optimization.
                 *
                 */
                Entry(timestamp.toFloat(), plotData.ppgMovingAverage[index].toFloat())
            }

        val ppgDataSet =
            /**
             * Executes linedataset operation with thermal imaging domain optimization.
             *
             */
            LineDataSet(ppgEntries, "PPG Raw").apply {
                color = Color.GREEN
                /**
                 * Configures the drawcircles with validation and thermal imaging optimization.
                 *
                 */
                setDrawCircles(false)
                lineWidth = 1.5f
                /**
                 * Configures the drawvalues with validation and thermal imaging optimization.
                 *
                 */
                setDrawValues(false)
            }

        val ppgAvgDataSet =
            /**
             * Executes linedataset operation with thermal imaging domain optimization.
             *
             */
            LineDataSet(ppgMovingAvgEntries, "PPG Moving Average").apply {
                color = Color.MAGENTA
                /**
                 * Configures the drawcircles with validation and thermal imaging optimization.
                 *
                 */
                setDrawCircles(false)
                lineWidth = 2f
                /**
                 * Configures the drawvalues with validation and thermal imaging optimization.
                 *
                 */
                setDrawValues(false)
            }

        // Set data to chart
        binding.ppgChart.data = LineData(ppgDataSet, ppgAvgDataSet)
        binding.ppgChart.invalidate()
    }

    /**
     * Executes displayStatistics functionality.
     */
    /**
     * Executes displaystatistics operation with thermal imaging domain optimization.
     *
     */
    private fun displayStatistics() {
        val metadata = plotData.metadata
        val stats = StringBuilder()

        stats.appendLine("📊 Recording Statistics")
        stats.appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        stats.appendLine("📁 File: ${metadata.fileName}")
        stats.appendLine("⏱️ Duration: ${formatDuration(metadata.duration)}")
        stats.appendLine("📈 Data Points: ${metadata.dataPoints}")
        stats.appendLine("🔄 Sampling Rate: ${"%.1f".format(metadata.samplingRate)} Hz")
        stats.appendLine("")

        // GSR Statistics
        val gsrMean = plotData.gsrValues.average()
        val gsrStdDev = calculateStandardDeviation(plotData.gsrValues)
        val gsrMin = plotData.gsrValues.minOrNull() ?: 0.0
        val gsrMax = plotData.gsrValues.maxOrNull() ?: 0.0

        stats.appendLine("🧬 GSR Analysis")
        stats.appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        stats.appendLine("Mean: ${"%.4f".format(gsrMean)} µS")
        stats.appendLine("Std Dev: ${"%.4f".format(gsrStdDev)} µS")
        stats.appendLine("Range: ${"%.4f".format(gsrMin)} - ${"%.4f".format(gsrMax)} µS")
        stats.appendLine("Variation: ${"%.2f".format((gsrStdDev / gsrMean) * 100)}%")
        stats.appendLine("")

        // PPG Statistics
        val ppgMean = plotData.ppgValues.average()
        val ppgStdDev = calculateStandardDeviation(plotData.ppgValues)
        val ppgMin = plotData.ppgValues.minOrNull() ?: 0.0
        val ppgMax = plotData.ppgValues.maxOrNull() ?: 0.0

        stats.appendLine("❤️ PPG Analysis")
        stats.appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        stats.appendLine("Mean: ${"%.1f".format(ppgMean)}")
        stats.appendLine("Std Dev: ${"%.1f".format(ppgStdDev)}")
        stats.appendLine("Range: ${"%.0f".format(ppgMin)} - ${"%.0f".format(ppgMax)}")
        stats.appendLine("")

        // Events Analysis
        stats.appendLine("🎯 Events Detected")
        stats.appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        val increases = plotData.gsrEvents.count { it.type == "INCREASE" }
        val decreases = plotData.gsrEvents.count { it.type == "DECREASE" }
        stats.appendLine("GSR Increases: $increases")
        stats.appendLine("GSR Decreases: $decreases")
        stats.appendLine("Total Events: ${plotData.gsrEvents.size}")

        binding.statsTextView.text = stats.toString()
    }

    /**
     * Executes calculateStandardDeviation functionality.
     */
    /**
     * Executes calculatestandarddeviation operation with thermal imaging domain optimization.
     *
     * @param
     * @param values Parameter for operation (type: List<Double>)
     *
     */
    private fun calculateStandardDeviation(values: List<Double>): Double {
        val mean = values.average()
        val variance = values.map { (it - mean) * (it - mean) }.average()
        return kotlin.math.sqrt(variance)
    }

    /**
     * Executes formatDuration functionality.
     */
    /**
     * Executes formatduration operation with thermal imaging domain optimization.
     *
     * @param
     * @param seconds Parameter for operation (type: Double)
     *
     */
    private fun formatDuration(seconds: Double): String {
        val minutes = (seconds / 60).toInt()
        val remainingSeconds = (seconds % 60).toInt()
        return "$minutes:${"%02d".format(remainingSeconds)}"
    }

/**
 * Specialized thermal imaging component providing TimeFormatter functionality for the IRCamera system.
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
    private class TimeFormatter : ValueFormatter() {
        /**
         * Retrieves the formattedvalue with optimized performance for thermal imaging operations.
         *
         * @param
         * @param value Parameter for operation (type: Float)
         *
         */
        override fun getFormattedValue(value: Float): String {
            val seconds = value.toInt()
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return "$minutes:${"%02d".format(remainingSeconds)}"
        }
    }
}
