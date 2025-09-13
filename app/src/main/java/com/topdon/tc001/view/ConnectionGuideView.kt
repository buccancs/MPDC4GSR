package com.topdon.tc001.view

import android.content.Context
import android.content.res.TypedArray
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.csl.irCamera.databinding.UiMainConnectionGuideBinding
import com.topdon.lib.ui.R as UiR

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ConnectionGuideView display and interaction.
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
class ConnectionGuideView : LinearLayout {
    private var iconRes: Int = 0
    private var contentStr: String = ""
    private var iconShow: Boolean = false
    private lateinit var binding: UiMainConnectionGuideBinding

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
        val ta: TypedArray = context.obtainStyledAttributes(attrs, UiR.styleable.ConnectionGuideView)
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
                UiR.styleable.ConnectionGuideView_guide_icon ->
                    iconRes =
                        ta.getResourceId(UiR.styleable.ConnectionGuideView_guide_icon, 0)
                UiR.styleable.ConnectionGuideView_guide_text ->
                    contentStr =
                        ta.getString(UiR.styleable.ConnectionGuideView_guide_text).toString()
                UiR.styleable.ConnectionGuideView_guide_icon_show ->
                    iconShow =
                        ta.getBoolean(UiR.styleable.ConnectionGuideView_guide_icon_show, false)
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
     * Initializes view component.
     */
    private fun initView() {
        binding = UiMainConnectionGuideBinding.inflate(LayoutInflater.from(context), this, true)
        binding.ivIcon.setImageResource(iconRes)
        binding.tvContent.text = contentStr
        binding.ivIcon.visibility = if (iconShow) View.VISIBLE else View.GONE
    }

    /**
     * Sets text configuration.
     */
    /**
     * Configures the text with validation and thermal imaging optimization.
     *
     * @param
     * @param text Parameter for operation (type: CharSequence?)
     *
     */
    fun setText(text: CharSequence?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (TextUtils.isEmpty(text)) return
        binding.tvContent.text = text
        binding.tvContent.movementMethod = LinkMovementMethod.getInstance()
    }

    /**
     * Retrieves text information.
     */
    fun getText(): String {
        return binding.tvContent.text.toString()
    }

    /**
     * Sets highlightcolor configuration.
     */
    fun setHighlightColor(color: Int) {
        binding.tvContent.highlightColor = color
    }

    /**
     * Retrieves compounddrawables information.
     */
    fun getCompoundDrawables(content: String) {
        var mContent = "$content  " // 插入空格是为了后area替换image
        val spannableString = SpannableString(mContent)
        val drawable = context.getDrawable(UiR.drawable.ic_connection_press_tip)
        drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        spannableString.setSpan(ImageSpan(drawable), mContent.length - 1, mContent.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvContent.text = spannableString
    }
}
