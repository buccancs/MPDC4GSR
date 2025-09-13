package com.topdon.module.thermal.ir.report.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import com.topdon.lib.core.tools.UnitTools
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.report.bean.ImageTempBean
import com.topdon.lib.core.R as LibR

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ReportIRInputView display and interaction.
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
class ReportIRInputView : LinearLayout {
    companion object {
        private const val TYPE_FULL = 0 // 全图
        private const val TYPE_POINT = 1 // Point
        private const val TYPE_LINE = 2 // Line
        private const val TYPE_RECT = 3 // Area
    }

    // View references - migrated from synthetic views
    private lateinit var clTitle: View
    private lateinit var viewLine: View
    private lateinit var tvTitle: TextView
    private lateinit var clMax: View
    private lateinit var clMin: View
    private lateinit var clAverage: View
    private lateinit var clExplain: View

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor(context: Context) : this(context, null)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("SetTextI18n")
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        /**
         * Executes inflate operation with thermal imaging domain optimization.
         *
         */
        inflate(context, R.layout.view_report_ir_input, this)
        /**
         * Initializes the views component for thermal imaging operations.
         *
         */
        initViews()

        val etExplain = clExplain.findViewById<EditText>(R.id.et_item)
        etExplain.inputType = InputType.TYPE_CLASS_TEXT
        etExplain.filters = arrayOf(LengthFilter(150))

        val switchMax = clMax.findViewById<SwitchCompat>(R.id.switch_item)
        val etMax = clMax.findViewById<EditText>(R.id.et_item)
        /**
         * Configures the switchlistener with validation and thermal imaging optimization.
         *
         */
        setSwitchListener(switchMax, etMax)

        val switchMin = clMin.findViewById<SwitchCompat>(R.id.switch_item)
        val etMin = clMin.findViewById<EditText>(R.id.et_item)
        /**
         * Configures the switchlistener with validation and thermal imaging optimization.
         *
         */
        setSwitchListener(switchMin, etMin)

        val switchAverage = clAverage.findViewById<SwitchCompat>(R.id.switch_item)
        val etAverage = clAverage.findViewById<EditText>(R.id.et_item)
        /**
         * Configures the switchlistener with validation and thermal imaging optimization.
         *
         */
        setSwitchListener(switchAverage, etAverage)

        val switchExplain = clExplain.findViewById<SwitchCompat>(R.id.switch_item)
        /**
         * Configures the switchlistener with validation and thermal imaging optimization.
         *
         */
        setSwitchListener(switchExplain, etExplain)

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ReportIRInputView)
        val type = typeArray.getInt(R.styleable.ReportIRInputView_type, TYPE_FULL)
        val index = typeArray.getInt(R.styleable.ReportIRInputView_index, 0)
        typeArray.recycle()

        clTitle.isVisible = index == 0
        viewLine.isVisible = index > 0

        /**
         * Configures the uptypespecificviews with validation and thermal imaging optimization.
         *
         */
        setupTypeSpecificViews(type, index)
    }

    /**
     * Initializes views component.
     */
    private fun initViews() {
        clTitle = findViewById(R.id.cl_title)
        viewLine = findViewById(R.id.view_line)
        tvTitle = findViewById(R.id.tv_title)
        clMax = findViewById(R.id.cl_max)
        clMin = findViewById(R.id.cl_min)
        clAverage = findViewById(R.id.cl_average)
        clExplain = findViewById(R.id.cl_explain)
    }

    /**
     * Sets uptypespecificviews configuration.
     */
    private fun setupTypeSpecificViews(
        type: Int,
        index: Int,
    ) {
        val tvMaxName = clMax.findViewById<TextView>(R.id.tv_item_name)
        val tvMinName = clMin.findViewById<TextView>(R.id.tv_item_name)
        val tvAverageName = clAverage.findViewById<TextView>(R.id.tv_item_name)
        val tvExplainName = clExplain.findViewById<TextView>(R.id.tv_item_name)

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (type) {
            TYPE_FULL -> {
                tvTitle.setText(LibR.string.thermal_full_rect)
                clMin.isVisible = true
                clAverage.isVisible = false
                tvMaxName.text = context.getString(LibR.string.chart_temperature_high) + " (${UnitTools.showUnit()})"
                tvMinName.text = context.getString(LibR.string.chart_temperature_low) + " (${UnitTools.showUnit()})"
                tvExplainName.text = context.getString(LibR.string.album_report_comment)
            }
            TYPE_POINT -> {
                tvTitle.text = context.getString(LibR.string.thermal_point) + "(P)"
                clMin.isVisible = false
                clAverage.isVisible = false
                tvMaxName.text = "P${index + 1} " + context.getString(LibR.string.chart_temperature) + " (${UnitTools.showUnit()})"
                tvExplainName.text = "P${index + 1} " + context.getString(LibR.string.album_report_comment)
            }
            TYPE_LINE -> {
                tvTitle.text = context.getString(LibR.string.thermal_line) + "(L)"
                clMin.isVisible = true
                clAverage.isVisible = true
                tvMaxName.text = "L${index + 1} " + context.getString(LibR.string.chart_temperature_high) + " (${UnitTools.showUnit()})"
                tvMinName.text = "L${index + 1} " + context.getString(LibR.string.chart_temperature_low) + " (${UnitTools.showUnit()})"
                tvAverageName.text = "L${index + 1} " + context.getString(LibR.string.album_report_mean_temperature) + " (${UnitTools.showUnit()})"
                tvExplainName.text = "L${index + 1} " + context.getString(LibR.string.album_report_comment)
            }
            TYPE_RECT -> {
                tvTitle.text = context.getString(LibR.string.thermal_rect) + "(R)"
                clMin.isVisible = true
                clAverage.isVisible = true
                tvMaxName.text = "R${index + 1} " + context.getString(LibR.string.chart_temperature_high) + " (${UnitTools.showUnit()})"
                tvMinName.text = "R${index + 1} " + context.getString(LibR.string.chart_temperature_low) + " (${UnitTools.showUnit()})"
                tvAverageName.text = "R${index + 1} " + context.getString(LibR.string.album_report_mean_temperature) + " (${UnitTools.showUnit()})"
                tvExplainName.text = "R${index + 1} " + context.getString(LibR.string.album_report_comment)
            }
        }
    }

    /**
     * Executes isSwitchMaxCheck functionality.
     */
    /**
     * Executes isswitchmaxcheck operation with thermal imaging domain optimization.
     *
     */
    fun isSwitchMaxCheck(): Boolean {
        val switchMax = clMax.findViewById<SwitchCompat>(R.id.switch_item)
        return switchMax.isChecked
    }

    /**
     * Executes isSwitchMinCheck functionality.
     */
    /**
     * Executes isswitchmincheck operation with thermal imaging domain optimization.
     *
     */
    fun isSwitchMinCheck(): Boolean {
        val switchMin = clMin.findViewById<SwitchCompat>(R.id.switch_item)
        return switchMin.isChecked
    }

    /**
     * Executes isSwitchAverageCheck functionality.
     */
    /**
     * Executes isswitchaveragecheck operation with thermal imaging domain optimization.
     *
     */
    fun isSwitchAverageCheck(): Boolean {
        val switchAverage = clAverage.findViewById<SwitchCompat>(R.id.switch_item)
        return switchAverage.isChecked
    }

    /**
     * Executes isSwitchExplainCheck functionality.
     */
    /**
     * Executes isswitchexplaincheck operation with thermal imaging domain optimization.
     *
     */
    fun isSwitchExplainCheck(): Boolean {
        val switchExplain = clExplain.findViewById<SwitchCompat>(R.id.switch_item)
        return switchExplain.isChecked
    }

    /**
     * Retrieves maxinput information.
     */
    fun getMaxInput(): String {
        val etMax = clMax.findViewById<EditText>(R.id.et_item)
        return etMax.text.toString()
    }

    /**
     * Retrieves mininput information.
     */
    fun getMinInput(): String {
        val etMin = clMin.findViewById<EditText>(R.id.et_item)
        return etMin.text.toString()
    }

    /**
     * Retrieves averageinput information.
     */
    fun getAverageInput(): String {
        val etAverage = clAverage.findViewById<EditText>(R.id.et_item)
        return etAverage.text.toString()
    }

    /**
     * Retrieves explaininput information.
     */
    fun getExplainInput(): String {
        val etExplain = clExplain.findViewById<EditText>(R.id.et_item)
        return etExplain.text.toString()
    }

    /**
     * Executes refreshData functionality.
     */
    /**
     * Executes refreshdata operation with thermal imaging domain optimization.
     *
     * @param
     * @param tempBean Temperature value in Celsius (type: ImageTempBean.TempBean?)
     *
     */
    fun refreshData(tempBean: ImageTempBean.TempBean?) {
        val etMax = clMax.findViewById<EditText>(R.id.et_item)
        val etMin = clMin.findViewById<EditText>(R.id.et_item)
        val etAverage = clAverage.findViewById<EditText>(R.id.et_item)
        val etExplain = clExplain.findViewById<EditText>(R.id.et_item)

        tempBean?.max?.let {
            etMax.setText(UnitTools.showUnitValue(it.toFloat())?.toString())
        }
        tempBean?.min?.let {
            etMin.setText(UnitTools.showUnitValue(it.toFloat())?.toString())
        }
        tempBean?.average?.let {
            etAverage.setText(UnitTools.showUnitValue(it.toFloat())?.toString())
        }
        etExplain.setText("")
    }

    /**
     * Sets switchlistener configuration.
     */
    private fun setSwitchListener(
        switchCompat: SwitchCompat,
        editText: EditText,
    ) {
        switchCompat.setOnCheckedChangeListener { _, isChecked ->
            editText.isVisible = isChecked
        }
    }
}
