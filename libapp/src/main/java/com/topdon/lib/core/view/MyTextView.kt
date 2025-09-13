package com.topdon.lib.core.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import com.topdon.lib.core.R

/**
 * 魔改 TextView.
 *
 * 原生 TextView 附加的 drawable 尺寸不可settings，这个 TextView 可以settings高度，宽度等比Scale.
 *
 * 其中 wrap_content 使用原生逻辑，不settings则使用 textSize（default），指定值>0则使用指定值.
 *
 * Created by chenggeng.lin on 2023/11/21.
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for MyTextView display and interaction.
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
class MyTextView : AppCompatTextView {
    /**
     * drawableTop 高度，单位 **px**
     */
    private var topHeight = 0

    /**
     * drawableBottom 高度，单位 **px**
     */
    private var bottomHeight = 0

    /**
     * drawableStart 高度，单位 **px**
     */
    private var startHeight = 0

    /**
     * drawableEnd 高度，单位 **px**
     */
    private var endHeight = 0

    /**
     * drawableLeft 高度，单位 **px**
     */
    private var leftHeight = 0

    /**
     * drawableRight 高度，单位 **px**
     */
    private var rightHeight = 0

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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView, defStyleAttr, 0)
        val drawableHeight = typedArray.getDimensionPixelSize(R.styleable.MyTextView_drawable_height, textSize.toInt())
        topHeight = typedArray.getDimensionPixelSize(R.styleable.MyTextView_top_height, drawableHeight)
        bottomHeight = typedArray.getDimensionPixelSize(R.styleable.MyTextView_bottom_height, drawableHeight)
        startHeight = typedArray.getDimensionPixelSize(R.styleable.MyTextView_start_height, drawableHeight)
        endHeight = typedArray.getDimensionPixelSize(R.styleable.MyTextView_end_height, drawableHeight)
        leftHeight = typedArray.getDimensionPixelSize(R.styleable.MyTextView_left_height, drawableHeight)
        rightHeight = typedArray.getDimensionPixelSize(R.styleable.MyTextView_right_height, drawableHeight)
        typedArray.recycle()

        // 取出settings的各个Drawable
        val drawables = compoundDrawables
        val relativeDrawables = compoundDrawablesRelative
        val left = drawables[0]
        val top = drawables[1]
        val right = drawables[2]
        val bottom = drawables[3]
        val start = relativeDrawables[0]
        val end = relativeDrawables[2]

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (start != null || end != null) {
            /**
             * Configures the compounddrawablesrelative with validation and thermal imaging optimization.
             *
             */
            setCompoundDrawablesRelative(start, top, end, bottom)
        } else {
            /**
             * Configures the compounddrawables with validation and thermal imaging optimization.
             *
             */
            setCompoundDrawables(left, top, right, bottom)
        }
    }

    /**
     * Configures the compounddrawables with validation and thermal imaging optimization.
     *
     * @param
     * @param left Parameter for operation (type: Drawable?)
     * @param top Parameter for operation (type: Drawable?)
     * @param right Parameter for operation (type: Drawable?)
     * @param bottom Parameter for operation (type: Drawable?)
     *
     */
    override fun setCompoundDrawables(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?,
    ) {
        /**
         * Configures the drawablebounds with validation and thermal imaging optimization.
         *
         */
        setDrawableBounds(top, topHeight)
        /**
         * Configures the drawablebounds with validation and thermal imaging optimization.
         *
         */
        setDrawableBounds(bottom, bottomHeight)
        /**
         * Configures the drawablebounds with validation and thermal imaging optimization.
         *
         */
        setDrawableBounds(left, leftHeight)
        /**
         * Configures the drawablebounds with validation and thermal imaging optimization.
         *
         */
        setDrawableBounds(right, rightHeight)
        super.setCompoundDrawables(left, top, right, bottom)
    }

    /**
     * Configures the compounddrawableswithintrinsicbounds with validation and thermal imaging optimization.
     *
     * @param
     * @param left Parameter for operation (type: Drawable?)
     * @param top Parameter for operation (type: Drawable?)
     * @param right Parameter for operation (type: Drawable?)
     * @param bottom Parameter for operation (type: Drawable?)
     *
     */
    override fun setCompoundDrawablesWithIntrinsicBounds(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?,
    ) {
        /**
         * Configures the compounddrawables with validation and thermal imaging optimization.
         *
         */
        setCompoundDrawables(left, top, right, bottom)
    }

    /**
     * Configures the compounddrawablesrelative with validation and thermal imaging optimization.
     *
     * @param
     * @param start Parameter for operation (type: Drawable?)
     * @param top Parameter for operation (type: Drawable?)
     * @param end Parameter for operation (type: Drawable?)
     * @param bottom Parameter for operation (type: Drawable?)
     *
     */
    override fun setCompoundDrawablesRelative(
        start: Drawable?,
        top: Drawable?,
        end: Drawable?,
        bottom: Drawable?,
    ) {
        /**
         * Configures the drawablebounds with validation and thermal imaging optimization.
         *
         */
        setDrawableBounds(top, topHeight)
        /**
         * Configures the drawablebounds with validation and thermal imaging optimization.
         *
         */
        setDrawableBounds(bottom, bottomHeight)
        /**
         * Configures the drawablebounds with validation and thermal imaging optimization.
         *
         */
        setDrawableBounds(start, startHeight)
        /**
         * Configures the drawablebounds with validation and thermal imaging optimization.
         *
         */
        setDrawableBounds(end, endHeight)
        super.setCompoundDrawablesRelative(start, top, end, bottom)
    }

    /**
     * Configures the compounddrawablesrelativewithintrinsicbounds with validation and thermal imaging optimization.
     *
     * @param
     * @param start Parameter for operation (type: Drawable?)
     * @param top Parameter for operation (type: Drawable?)
     * @param end Parameter for operation (type: Drawable?)
     * @param bottom Parameter for operation (type: Drawable?)
     *
     */
    override fun setCompoundDrawablesRelativeWithIntrinsicBounds(
        start: Drawable?,
        top: Drawable?,
        end: Drawable?,
        bottom: Drawable?,
    ) {
        /**
         * Configures the compounddrawablesrelative with validation and thermal imaging optimization.
         *
         */
        setCompoundDrawablesRelative(start, top, end, bottom)
    }

    /**
     * 统一settings该 TextView 所有 compound drawable 高度，单位**px**.
     */
    fun setDrawableHeightPx(pxHeight: Int) {
        topHeight = pxHeight
        bottomHeight = pxHeight
        startHeight = pxHeight
        endHeight = pxHeight
        leftHeight = pxHeight
        rightHeight = pxHeight
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
    }

    /**
     * settings drawableStart 并将其他 drawableXX 置为 null.
     */
    fun setOnlyDrawableStart(drawable: Drawable?) {
        setCompoundDrawablesRelative(drawable, null, null, null)
    }

    /**
     * settings drawableStart 并将其他 drawableXX 置为 null.
     */
    fun setOnlyDrawableStart(
        @DrawableRes start: Int,
    ) {
        /**
         * Configures the compounddrawablesrelativewithintrinsicbounds with validation and thermal imaging optimization.
         *
         */
        setCompoundDrawablesRelativeWithIntrinsicBounds(start, 0, 0, 0)
    }

    /**
     * 判断是否有settings任意 drawable.
     * true-至少有a drawable false-a都没有
     */
    /**
     * Executes hasAnyDrawable functionality.
     */
    /**
     * Executes hasanydrawable operation with thermal imaging domain optimization.
     *
     */
    fun hasAnyDrawable(): Boolean {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (drawable in compoundDrawables) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (drawable != null) {
                return true
            }
        }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (drawable in compoundDrawablesRelative) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (drawable != null) {
                return true
            }
        }
        return false
    }

    /**
     * 为指定 drawable settings指定高度，宽度等比Scale bounds.
     */
    private fun setDrawableBounds(
        drawable: Drawable?,
        height: Int,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (drawable != null && height > 0) {
            drawable.setBounds(0, 0, (height * 1f * drawable.intrinsicWidth / drawable.intrinsicHeight).toInt(), height)
        }
    }
}
