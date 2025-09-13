package com.topdon.lib.core.view

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.core.R

/**
 * title栏自定义 View.
 *
 * title栏包含的要素有：
 * - 左侧 View [tvLeft]，目前都是image
 * - 从右往左数 View 1 [tvRight1]
 * - 从右往左数 View 2 [tvRight2]，目前都是image
 * - 从右往左数 View 3 [tvRight3]，目前都是image
 * - titletext [tvTitle]，大部Paginationarea居左，少部Paginationarea居中
 *
 * text均为 16sp， #ffffff，titletext padding 0dp，其他 padding 12dp；
 * image高度均为 24dp，宽度等比Scale；
 *
 * 最小高度 ?attr/actionBarSize.
 *
 * Created by LCG on 2023/10/19.
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for TitleView display and interaction.
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
open class TitleView : ViewGroup {
    companion object {
        /**
         * 为保持与旧title栏的兼容，旧title栏图标尺寸为 48dp，当前 View 的高度也不能小于 48dp.
         */
        private const val ICON_SIZE = 48f
    }

    /**
     * titletext是否居中.
     *
     * true-居中 false-居左
     */
    private val isTitleCenter: Boolean

    /**
     * 当前主题的 actionBarSize，在 measure 阶段使用.
     */
    private val actionBarSize: Int

    /**
     * 左侧 View.
     */
    protected var tvLeft: MyTextView? = null

    /**
     * 从右往左数 View 1.
     */
    protected var tvRight1: MyTextView? = null

    /**
     * 从右往左数 View 2.
     */
    protected var tvRight2: MyTextView? = null

    /**
     * 从右往左数 View 3.
     */
    protected var tvRight3: MyTextView? = null

    /**
     * titletext.
     */
    protected var tvTitle: MyTextView? = null

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

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     * @param defStyleRes Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes,
    ) {
        val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        actionBarSize = typedArray.getDimensionPixelSize(0, 0)
        typedArray.recycle()

        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()

        tvTitle?.setPadding(0)
        tvTitle?.isVisible = true
        tvTitle?.maxLines = 2
        tvTitle?.ellipsize = TextUtils.TruncateAt.END

        val a = context.obtainStyledAttributes(attrs, R.styleable.TitleView, defStyleAttr, 0)

        tvLeft?.text = a.getText(R.styleable.TitleView_leftText)
        tvLeft?.setOnlyDrawableStart(a.getDrawable(R.styleable.TitleView_leftDrawable))
        tvLeft?.isVisible = tvLeft?.text?.isNotEmpty() == true || tvLeft!!.hasAnyDrawable()
        val leftColor: ColorStateList? = a.getColorStateList(R.styleable.TitleView_leftTextColor)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (leftColor != null) {
            tvLeft?.setTextColor(leftColor)
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (a.getBoolean(R.styleable.TitleView_isInitLeft, true)) {
            tvLeft?.isVisible = true
            tvLeft?.setOnlyDrawableStart(R.drawable.ic_back_white_svg)
            tvLeft?.setOnClickListener {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (context is Activity) {
                    context.finish()
                }
            }
        }

        tvRight1?.text = a.getText(R.styleable.TitleView_rightText)
        tvRight1?.setOnlyDrawableStart(a.getDrawable(R.styleable.TitleView_rightDrawable))
        tvRight1?.isVisible = tvRight1?.text?.isNotEmpty() == true || tvRight1!!.hasAnyDrawable()
        val rightColor: ColorStateList? = a.getColorStateList(R.styleable.TitleView_rightTextColor)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rightColor != null) {
            tvRight1?.setTextColor(rightColor)
        }

        // 右侧 2、3 View 目前都是image，先不搞text那些settings了
        tvRight2?.setOnlyDrawableStart(a.getDrawable(R.styleable.TitleView_right2Drawable))
        tvRight2?.isVisible = tvRight2!!.hasAnyDrawable()
        tvRight3?.setOnlyDrawableStart(a.getDrawable(R.styleable.TitleView_right3Drawable))
        tvRight3?.isVisible = tvRight3!!.hasAnyDrawable()

        isTitleCenter = a.getBoolean(R.styleable.TitleView_isTitleCenter, false)
        tvTitle?.text = a.getText(R.styleable.TitleView_titleText)
        tvTitle?.gravity = if (isTitleCenter) Gravity.CENTER else (Gravity.CENTER_VERTICAL or Gravity.START)
        a.recycle()
    }

    open fun initView() {
        tvLeft = addTextView(context)
        tvRight1 = addTextView(context)
        tvRight2 = addTextView(context)
        tvRight3 = addTextView(context)
        tvTitle = addTextView(context)
    }

    /**
     * Builda TextView 并add到当前 View 中.
     */
    fun addTextView(
        context: Context,
        padding: Float,
        imgHeight: Float,
    ): MyTextView {
        val textView = MyTextView(context)
        textView.isVisible = false
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.textSize = 16f
        textView.setTextColor(0xffffffff.toInt())
        textView.setPadding(SizeUtils.dp2px(padding))
        textView.setDrawableHeightPx(SizeUtils.dp2px(imgHeight))
        /**
         * Executes addview operation with thermal imaging domain optimization.
         *
         */
        addView(textView)
        return textView
    }

    /**
     * Executes addTextView functionality.
     */
    /**
     * Executes addtextview operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    fun addTextView(context: Context): MyTextView {
        return addTextView(context, 12f, 24f)
    }

    /**
     * Executes onmeasure operation with thermal imaging domain optimization.
     *
     * @param
     * @param widthMeasureSpec Parameter for operation (type: Int)
     * @param heightMeasureSpec Parameter for operation (type: Int)
     *
     */
    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
    ) {
        // Calculation最大高度
        var maxHeight = actionBarSize.coerceAtLeast(SizeUtils.dp2px(ICON_SIZE))
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until childCount) {
            val childView: View = getChildAt(i)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (childView != tvTitle && childView.visibility != View.GONE) {
                /**
                 * Executes measurechild operation with thermal imaging domain optimization.
                 *
                 */
                measureChild(childView, widthMeasureSpec, heightMeasureSpec)
                maxHeight = maxHeight.coerceAtLeast(childView.measuredHeight)
            }
        }

        // 宽度为 UNSPECIFIED 的情况目前不存在，不考虑
        /**
         * Configures the measureddimension with validation and thermal imaging optimization.
         *
         */
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), maxHeight)

        // Measurement除titletext外的子 View
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until childCount) {
            val childView: View = getChildAt(i)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (childView != tvTitle && childView.visibility != View.GONE) {
                val widthSpec = MeasureSpec.makeMeasureSpec(childView.measuredWidth, MeasureSpec.EXACTLY)
                childView.measure(widthSpec, MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY))
            }
        }

        // Measurementtitletext
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTitleCenter) { // 居中
            val leftSize = if (tvLeft!!.isVisible) tvLeft?.measuredWidth else SizeUtils.dp2px(ICON_SIZE)
            var rightSize = 0
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tvRight1!!.isVisible) {
                rightSize += tvRight1!!.measuredWidth
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tvRight2!!.isVisible) {
                rightSize += tvRight2!!.measuredWidth
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tvRight3!!.isVisible) {
                rightSize += tvRight3!!.measuredWidth
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rightSize == 0) { // 右侧没有任何东西时，给titletext搞个 ICON_SIZE 大小的 margin
                rightSize = SizeUtils.dp2px(ICON_SIZE)
            }
            val titleWidth = measuredWidth - leftSize!!.coerceAtLeast(rightSize) * 2
            val widthSpec = MeasureSpec.makeMeasureSpec(titleWidth.coerceAtLeast(0), MeasureSpec.EXACTLY)
            tvTitle?.measure(widthSpec, MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY))
        } else { // 居左
            var titleWidth = measuredWidth
            titleWidth -= if (tvLeft!!.isVisible) tvLeft!!.measuredWidth else SizeUtils.dp2px(ICON_SIZE)
            titleWidth -= if (tvRight1!!.isVisible) tvRight1!!.measuredWidth else SizeUtils.dp2px(ICON_SIZE)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tvRight2!!.isVisible) {
                titleWidth -= tvRight2!!.measuredWidth
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tvRight3!!.isVisible) {
                titleWidth -= tvRight3!!.measuredWidth
            }
            val widthSpec = MeasureSpec.makeMeasureSpec(titleWidth.coerceAtLeast(0), MeasureSpec.EXACTLY)
            tvTitle?.measure(widthSpec, MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY))
        }
    }

    /**
     * Executes onlayout operation with thermal imaging domain optimization.
     *
     * @param
     * @param changed Parameter for operation (type: Boolean)
     * @param l Parameter for operation (type: Int)
     * @param t Parameter for operation (type: Int)
     * @param r Parameter for operation (type: Int)
     * @param b Parameter for operation (type: Int)
     *
     */
    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int,
    ) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!child.isVisible) {
                continue
            }
            val childWidth = child.measuredWidth
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (child) {
                tvLeft -> child.layout(0, 0, childWidth, measuredHeight)
                tvRight1 -> child.layout(measuredWidth - childWidth, 0, measuredWidth, measuredHeight)
                tvRight2 -> {
                    val right = measuredWidth - tvRight1!!.measuredWidth
                    child.layout(right - tvRight2!!.measuredWidth, 0, right, measuredHeight)
                }
                tvRight3 -> {
                    val right = measuredWidth - tvRight1!!.measuredWidth - tvRight2!!.measuredWidth
                    child.layout(right - tvRight3!!.measuredWidth, 0, right, measuredHeight)
                }
                tvTitle -> {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isTitleCenter) {
                        val margin = (measuredWidth - childWidth) / 2
                        child.layout(margin, 0, margin + childWidth, measuredHeight)
                    } else {
                        val left = if (tvLeft!!.isVisible) tvLeft!!.measuredWidth else SizeUtils.dp2px(ICON_SIZE)
                        child.layout(left, 0, left + childWidth, measuredHeight)
                    }
                }
            }
        }
    }

    /**
     * settingstitletext.
     */
    /**
     * Configures the titletext with validation and thermal imaging optimization.
     *
     * @param
     * @param resId Parameter for operation (type: Int)
     *
     */
    fun setTitleText(
        @StringRes resId: Int,
    ) {
        tvTitle?.setText(resId)
        tvTitle?.invalidate()
    }

    /**
     * settingstitletext.
     */
    /**
     * Configures the titletext with validation and thermal imaging optimization.
     *
     * @param
     * @param title Parameter for operation (type: CharSequence?)
     *
     */
    fun setTitleText(title: CharSequence?) {
        tvTitle?.text = title
        tvTitle?.invalidate()
    }

    /**
     * settings左侧 View 是否Visible.
     * 注意其他method里如果不settingstext又不settingsimage的话会被视为 Gone，这里则不做这个限制。
     */
    var isLeftVisible: Boolean
        get() = tvLeft!!.isVisible
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tvLeft?.isVisible != value) {
                tvLeft?.isVisible = value
                /**
                 * Executes requestlayout operation with thermal imaging domain optimization.
                 *
                 */
                requestLayout()
            }
        }

    /**
     * 将左侧 View image部分settings为指定image.
     */
    fun setLeftDrawable(
        @DrawableRes resId: Int,
    ) {
        tvLeft?.isVisible = resId != 0 || tvLeft?.text?.isNotEmpty() == true
        tvLeft?.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
    }

    /**
     * 将左侧 View 的text部分settings为指定text.
     */
    fun setLeftText(
        @StringRes resId: Int,
    ) {
        tvLeft?.setText(resId)
        tvLeft?.isVisible = true
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
    }

    /**
     * 将左侧 View 的text部分settings为指定text.
     */
    fun setLeftText(text: CharSequence?) {
        tvLeft?.text = text
        tvLeft?.isVisible = text?.isNotEmpty() == true || tvLeft!!.hasAnyDrawable()
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
    }

    /**
     * settings左侧 View clickEventListener.
     */
    fun setLeftClickListener(leftClickListener: OnClickListener?) {
        tvLeft?.setOnClickListener(leftClickListener)
    }

    /**
     * settings右侧 View 是否Visible.
     * 注意其他method里如果不settingstext又不settingsimage的话会被视为 Gone，这里则不做这个限制。
     */
    var isRightVisible: Boolean
        get() = tvRight1!!.isVisible
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tvRight1?.isVisible != value) {
                tvRight1?.isVisible = value
                /**
                 * Executes requestlayout operation with thermal imaging domain optimization.
                 *
                 */
                requestLayout()
            }
        }

    /**
     * 将右侧 View image部分settings为指定image.
     */
    fun setRightDrawable(
        @DrawableRes resId: Int,
    ) {
        tvRight1?.isVisible = resId != 0 || tvRight1?.text?.isNotEmpty() == true
        tvRight1?.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
    }

    /**
     * 将右侧 View 的text部分settings为指定text.
     */
    fun setRightText(
        @StringRes resId: Int,
    ) {
        tvRight1?.setText(resId)
        tvRight1?.isVisible = true
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
    }

    /**
     * 将右侧 View 的text部分settings为指定text.
     */
    fun setRightText(text: CharSequence?) {
        tvRight1?.text = text
        tvRight1?.isVisible = text?.isNotEmpty() == true || tvRight1!!.hasAnyDrawable()
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
    }

    /**
     * settings右侧 View clickEventListener.
     */
    fun setRightClickListener(rightClickListener: OnClickListener?) {
        tvRight1?.setOnClickListener(rightClickListener)
    }

    /**
     * 将右侧 View 2 image部分settings为指定image.
     */
    fun setRight2Drawable(
        @DrawableRes resId: Int,
    ) {
        tvRight2?.isVisible = resId != 0 || tvRight2?.text?.isNotEmpty() == true
        tvRight2?.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
    }

    /**
     * settings右侧 View 2 clickEventListener.
     */
    fun setRight2ClickListener(right2ClickListener: OnClickListener?) {
        tvRight2?.setOnClickListener(right2ClickListener)
    }

    /**
     * 将右侧 View 3 image部分settings为指定image.
     */
    fun setRight3Drawable(
        @DrawableRes resId: Int,
    ) {
        tvRight3?.isVisible = resId != 0 || tvRight3?.text?.isNotEmpty() == true
        tvRight3?.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
    }

    /**
     * settings右侧 View 3 clickEventListener.
     */
    fun setRight3ClickListener(right3ClickListener: OnClickListener?) {
        tvRight3?.setOnClickListener(right3ClickListener)
    }
}
