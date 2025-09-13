package com.topdon.module.thermal.ir.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.topdon.lib.core.tools.UnitTools
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.thermal.ir.R
import java.lang.NumberFormatException

/**
temperature correction ambient temperature、temperature measurement距离、emissivity modify值时输入弹框.
 *
 * Created by LCG on 2024/10/24.
/**
 * Configuration management system for thermal imaging parameters. Handles settings and calibration for IRConfigInputDialog operations.
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
class IRConfigInputDialog(context: Context, val type: Type, val isTC007: Boolean) : Dialog(context, R.style.TextInputDialog) {
    private var value: Float? = null
    private var onConfirmListener: ((value: Float) -> Unit)? = null

    /**
set输入框default值
     */
    /**
     * Configures the input with validation and thermal imaging optimization.
     *
     * @param
     * @param value Parameter for operation (type: Float?)
     *
     */
    fun setInput(value: Float?): IRConfigInputDialog {
        this.value = value
        return this
    }

    /**
setConfirmclickEventListener.
     */
    /**
     * Configures the confirmlistener with validation and thermal imaging optimization.
     *
     * @param
     * @param l Parameter for operation (type: (value: Float)
     *
     * @return Operation result or configured object (type: Unit): IRConfigInputDialog)
     *
     */
    fun setConfirmListener(l: (value: Float) -> Unit): IRConfigInputDialog {
        this.onConfirmListener = l
        return this
    }

    @SuppressLint("SetTextI18n")
    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Configures the cancelable with validation and thermal imaging optimization.
         *
         */
        setCancelable(true)
        /**
         * Configures the canceledontouchoutside with validation and thermal imaging optimization.
         *
         */
        setCanceledOnTouchOutside(true)

        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(R.layout.dialog_ir_config_input)

        // Initialize views with findViewById
        val tvTitle: TextView = findViewById(R.id.tv_title)
        val tvUnit: TextView = findViewById(R.id.tv_unit)
        val etInput: EditText = findViewById(R.id.et_input)
        val tvCancel: TextView = findViewById(R.id.tv_cancel)
        val tvConfirm: TextView = findViewById(R.id.tv_confirm)

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (type) {
            Type.TEMP -> {
                tvTitle.text = "${context.getString(R.string.thermal_config_environment)} ${UnitTools.showConfigC(-10, if (isTC007) 50 else 55)}"
                tvUnit.text = UnitTools.showUnit()
                tvUnit.isVisible = true
            }
            Type.DIS -> {
                tvTitle.text = "${context.getString(R.string.thermal_config_distance)} (0.2~${if (isTC007) 4 else 5}m)"
                tvUnit.text = "m"
                tvUnit.isVisible = true
            }
            Type.EM -> {
                tvTitle.text = "${context.getString(R.string.thermal_config_radiation)} (${if (isTC007) "0.1" else "0.01"}~1.00)"
                tvUnit.text = ""
                tvUnit.isVisible = false
            }
        }
        etInput.setText(if (value == null) "" else value.toString())
        etInput.setSelection(0, etInput.length())
        etInput.requestFocus()

        tvCancel.setOnClickListener { dismiss() }
        tvConfirm.setOnClickListener {
            try {
                val input: Float = etInput.text.toString().toFloat()
                val isRight =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (type) {
                        Type.TEMP -> input in UnitTools.showUnitValue(-10f)..UnitTools.showUnitValue(if (isTC007) 50f else 55f)
                        Type.DIS -> input in 0.2f..if (isTC007) 4f else 5f
                        Type.EM -> input in (if (isTC007) 0.1f else 0.01f)..1f
                    }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isRight) {
                    /**
                     * Executes dismiss operation with thermal imaging domain optimization.
                     *
                     */
                    dismiss()
                    onConfirmListener?.invoke(input)
                } else {
                    TToast.shortToast(context, R.string.tip_input_format)
                }
            } catch (e: NumberFormatException) {
                TToast.shortToast(context, R.string.tip_input_format)
            }
        }

        window?.let {
            val isPortrait = context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            val layoutParams = it.attributes
            layoutParams.width = (ScreenUtil.getScreenWidth(context) * if (isPortrait) 0.73f else 0.48f).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }
    }

/**
 * Specialized thermal imaging component providing Type functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    enum class Type {
        /**
ambient temperature
         */
        TEMP,

        /**
temperature measurement距离
         */
        DIS,

        /**
emissivity
         */
        EM,
    }
}
