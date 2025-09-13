package com.topdon.lib.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.topdon.lib.ui.R as UiR
import com.topdon.lib.ui.databinding.UiSettingViewNightBinding

/**
 * Custom Setting night view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * SettingNightView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for SettingNightView display and interaction.
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
class SettingNightView : LinearLayout {
    private lateinit var binding: UiSettingViewNightBinding

    var isRightArrowVisible: Boolean
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = binding.itemSettingEndImage.isVisible
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            binding.itemSettingEndImage.isVisible = value
        }

    /**
     * Sets righttextid configuration.
     */
    fun setRightTextId(
        @StringRes resId: Int,
    ) {
        binding.tvEnd.isVisible = resId != 0
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (resId != 0) {
            binding.tvEnd.setText(resId)
        }
    }

    private var iconRes: Int = 0
    private var contentStr: String = ""
    private var moreShow: Boolean = true
    private var lineShow: Boolean = false
    private var iconShow: Boolean = false

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
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        binding = UiSettingViewNightBinding.inflate(LayoutInflater.from(context), this, true)

        val ta: TypedArray = context.obtainStyledAttributes(attrs, UiR.styleable.SettingNightView)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until ta.indexCount) {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (ta.getIndex(i)) {
                UiR.styleable.SettingNightView_setting_icon_night ->
                    iconRes =
                        ta.getResourceId(
                            UiR.styleable.SettingNightView_setting_icon_night,
                            UiR.drawable.ic_setting_default_svg,
                        )
                UiR.styleable.SettingNightView_setting_text_night ->
                    contentStr =
                        ta.getString(UiR.styleable.SettingNightView_setting_text_night).toString()
                UiR.styleable.SettingNightView_setting_more_night ->
                    moreShow =
                        ta.getBoolean(UiR.styleable.SettingNightView_setting_more_night, true)
                UiR.styleable.SettingNightView_setting_line_night ->
                    lineShow =
                        ta.getBoolean(UiR.styleable.SettingNightView_setting_line_night, false)
                UiR.styleable.SettingNightView_setting_icon_show_night ->
                    iconShow =
                        ta.getBoolean(UiR.styleable.SettingNightView_setting_icon_show_night, false)
            }
        }
        ta.recycle()
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
    }

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
    )

    /**
     * Initializes the component with default configuration.
     */
    private fun initView() {
        // Views are already inflated in constructor via binding
        binding.itemSettingImage.setImageResource(iconRes)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (iconShow) {
            binding.itemSettingImage.visibility = View.VISIBLE
        } else {
            binding.itemSettingImage.visibility = View.GONE
        }
        binding.itemSettingText.text = contentStr
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (moreShow) {
            binding.itemSettingEndImage.visibility = View.VISIBLE
        } else {
            binding.itemSettingEndImage.visibility = View.GONE
        }
        binding.itemSettingLine.visibility = if (lineShow) View.VISIBLE else View.GONE
    }
}
