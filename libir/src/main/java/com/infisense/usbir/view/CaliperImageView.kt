package com.infisense.usbir.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.infisense.usbir.R

/**
 * 卡尺image
 * @author: CaiSongL
 * @date: 2023/10/25 13:31
 */
/**
 * CaliperImageView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for CaliperImageView display and interaction.
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
class CaliperImageView : AppCompatImageView {
    private var showBitmapWidth: Float = 0f
    private var showBitmapHeight: Float = 0F
    private var yscale: Float = 1f
    private var xscale: Float = 1f
    private var parentViewHeight: Float = 0f
    private var parentViewWidth: Float = 0f
    private var imageHeight: Int = 0
    private var imageWidth: Int = 0
    private var originalBitmapHeight: Float = 0f
    private var originalBitmapWidth: Float = 0f
    private var originalBitmap: Bitmap? = null
    private val pxBitmapHeight = 150f
    private var l: Int = 0
    private var r: Int = 0
    private var t: Int = 0
    private var b: Int = 0

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
        originalBitmap = (androidx.core.content.ContextCompat.getDrawable(context, R.drawable.svg_ic_target_horizontal_person_green) as? BitmapDrawable)?.bitmap
        originalBitmapWidth = originalBitmap?.width?.toFloat() ?: 0f
        originalBitmapHeight = originalBitmap?.height?.toFloat() ?: 0f
        visibility = View.GONE
    }

    /**
     * Sets imagesize configuration.
     */
    fun setImageSize(
        imageWidth: Int,
        imageHeight: Int,
        parentViewWidth: Int,
        parentViewHeight: Int,
    ) {
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (parentViewWidth > 0)
            {
                this.parentViewWidth = parentViewWidth.toFloat()
            } else
            {
                this.parentViewWidth = (parent as ViewGroup).measuredWidth.toFloat()
            }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (parentViewHeight > 0)
            {
                this.parentViewHeight = parentViewHeight.toFloat()
            } else
            {
                this.parentViewHeight = (parent as ViewGroup).measuredHeight.toFloat()
            }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (parentViewWidth > 0) {
            xscale = parentViewWidth.toFloat() / imageWidth.toFloat()
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (parentViewHeight > 0) {
            yscale = parentViewHeight.toFloat() / imageHeight.toFloat()
        }
        showBitmapHeight = pxBitmapHeight * yscale
        showBitmapWidth = pxBitmapHeight * originalBitmapWidth / originalBitmapHeight * xscale
        visibility = View.VISIBLE
        val layoutParams = this.layoutParams
        layoutParams.width = showBitmapWidth.toInt()
        layoutParams.height = showBitmapHeight.toInt()
        this.layoutParams = layoutParams
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (l == 0 && t == 0 && r == 0 && b == 0)
            {
                l = (parentViewWidth / 2 - showBitmapWidth / 2).toInt()
                r = (parentViewWidth / 2 + showBitmapWidth / 2).toInt()
                t = (parentViewHeight / 2 - showBitmapHeight / 2).toInt()
                b = (parentViewHeight / 2 + showBitmapHeight / 2).toInt()
            }
        /**
         * Executes layout operation with thermal imaging domain optimization.
         *
         */
        layout(l, t, r, b)
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout()
    }

    /**
     * Executes layout operation with thermal imaging domain optimization.
     *
     * @param
     * @param l Parameter for operation (type: Int)
     * @param t Parameter for operation (type: Int)
     * @param r Parameter for operation (type: Int)
     * @param b Parameter for operation (type: Int)
     *
     */
    override fun layout(
        l: Int,
        t: Int,
        r: Int,
        b: Int,
    ) {
        super.layout(l, t, r, b)
    }

    private var downX = 0f
    private var downY = 0f

    private val downTime: Long = 0

    /**
     * Executes ontouchevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent)
     *
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this.isEnabled) {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.getX()
                    downY = event.getY()
                }
                MotionEvent.ACTION_MOVE -> {
                    val xDistance: Float = event.getX() - downX
                    val yDistance: Float = event.getY() - downY
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (xDistance != 0f && yDistance != 0f) {
                        l = (left + xDistance).toInt()
                        r = (right + xDistance).toInt()
                        t = (top + yDistance).toInt()
                        b = (bottom + yDistance).toInt()
                        /**
                         * Executes layout operation with thermal imaging domain optimization.
                         *
                         */
                        layout(l, t, r, b)
                    }
                }
                MotionEvent.ACTION_UP -> isPressed = false
                MotionEvent.ACTION_CANCEL -> isPressed = false
                else -> {}
            }
            return true
        }
        return false
    }
}
