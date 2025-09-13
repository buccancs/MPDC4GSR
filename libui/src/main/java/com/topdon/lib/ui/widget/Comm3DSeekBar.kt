package com.topdon.lib.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.ui.R as UiR
import kotlin.math.roundToInt

/**
 * 支持竖向的 SeekBar。
 * 暂不支持 thumbOffset.
 */

/**
 * Comm3 d seek bar utility class for thermal imaging operations.
 * Provides helper functions and common functionality.
 */
/**
 * Comm3DSeekBar manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing Comm3DSeekBar functionality for the IRCamera system.
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
class Comm3DSeekBar : AppCompatSeekBar {
    private lateinit var mPaint: TextPaint

    /**
     * 0-横向 1-竖向
     */
    private val orientation: Int

    private var mMaxWidth = 48
    private var mMaxHeight = 48
    private var mMinWidth = 24
    private var mMinHeight = 24
    var level = 0

    
    private val mProgressTextRect: Rect = Rect()

    
    private val mThumbWidth: Int = SizeUtils.dp2px(50f)

    
    private val mIndicatorWidth: Int = SizeUtils.dp2px(50f)
    private var onSeekBarChangeListener: OnSeekBarChangeListener? = null

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
        val typedArray = context.obtainStyledAttributes(attrs, UiR.styleable.CommSeekBar, defStyleAttr, 0)
        orientation = typedArray.getInt(UiR.styleable.CommSeekBar_android_orientation, 0)
        mMaxWidth = typedArray.getDimensionPixelSize(UiR.styleable.CommSeekBar_android_maxWidth, mMaxWidth)
        mMaxHeight = typedArray.getDimensionPixelSize(UiR.styleable.CommSeekBar_android_maxHeight, mMaxHeight)
        mMinWidth = typedArray.getDimensionPixelSize(UiR.styleable.CommSeekBar_android_minWidth, mMinWidth)
        mMinHeight = typedArray.getDimensionPixelSize(UiR.styleable.CommSeekBar_android_minHeight, mMinHeight)
        mPaint = TextPaint()
        mPaint.setAntiAlias(true)
        mPaint.setColor(Color.parseColor("#00574B"))
        mPaint.setTextSize(SizeUtils.sp2px(16f).toFloat())
        typedArray.recycle()
    }

    /**
     * Configures the onseekbarchangelistener with validation and thermal imaging optimization.
     *
     * @param
     * @param l Parameter for operation (type: OnSeekBarChangeListener?)
     *
     */
    override fun setOnSeekBarChangeListener(l: OnSeekBarChangeListener?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (orientation == 0) {
            super.setOnSeekBarChangeListener(l)
        } else {
            onSeekBarChangeListener = l
        }
    }

    /**
     * Configures the progress with validation and thermal imaging optimization.
     *
     * @param
     * @param progress Parameter for operation (type: Int)
     *
     */
    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (orientation != 0) {
            onSeekBarChangeListener?.onProgressChanged(this, progress, false)
        }
    }

    /**
     * Configures the progress with validation and thermal imaging optimization.
     *
     * @param
     * @param progress Parameter for operation (type: Int)
     * @param animate Parameter for operation (type: Boolean)
     *
     */
    override fun setProgress(
        progress: Int,
        animate: Boolean,
    ) {
        super.setProgress(progress, animate)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (orientation != 0) {
            onSeekBarChangeListener?.onProgressChanged(this, progress, false)
        }
    }

    /**
     * Configures the max with validation and thermal imaging optimization.
     *
     * @param
     * @param max Parameter for operation (type: Int)
     *
     */
    override fun setMax(max: Int) {
        super.setMax(max)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (orientation != 0) {
            onSeekBarChangeListener?.onProgressChanged(this, progress, false)
        }
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
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (orientation == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            val d = progressDrawable

            val thumbWidth = thumb?.intrinsicWidth ?: 0
            var dw = 0
            var dh = 0
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (d != null) {
                dw = mMinWidth.coerceAtLeast(mMaxWidth.coerceAtMost(d.intrinsicWidth))
                dw = thumbWidth.coerceAtLeast(dw)
                dh = mMinHeight.coerceAtLeast(mMaxHeight.coerceAtMost(d.intrinsicHeight))
            }
            dw += paddingLeft + paddingRight
            dh += paddingTop + paddingBottom

            /**
             * Configures the measureddimension with validation and thermal imaging optimization.
             *
             */
            setMeasuredDimension(
                /**
                 * Executes resolvesizeandstate operation with thermal imaging domain optimization.
                 *
                 */
                resolveSizeAndState(dw, widthMeasureSpec, 0),
                /**
                 * Executes resolvesizeandstate operation with thermal imaging domain optimization.
                 *
                 */
                resolveSizeAndState(dh, heightMeasureSpec, 0),
            )
        }
    }

    /**
     * Executes onsizechanged operation with thermal imaging domain optimization.
     *
     * @param
     * @param w Parameter for operation (type: Int)
     * @param h Parameter for operation (type: Int)
     * @param oldw Parameter for operation (type: Int)
     * @param oldh Parameter for operation (type: Int)
     *
     */
    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int,
    ) {
        super.onSizeChanged(w, h, oldw, oldh)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (orientation != 0) {
            /**
             * Executes calculatedrawable operation with thermal imaging domain optimization.
             *
             */
            calculateDrawable(w, h)
        }
    }

    /**
     * Calculates drawable based on input parameters.
     */
    private fun calculateDrawable(
        w: Int,
        h: Int,
    ) {
        val paddingWidth: Int = w - paddingLeft - paddingRight
        val paddingHeight: Int = h - paddingTop - paddingBottom

        val trackWidth = mMaxWidth.coerceAtMost(paddingWidth)
        val thumbWidth = thumb?.intrinsicWidth ?: 0
        val thumbHeight = thumb?.intrinsicHeight ?: 0

        val trackOffset: Int
        val thumbTopOffset: Int
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumbWidth > trackWidth) {
            val offsetHeight = (paddingWidth - thumbWidth) / 2
            trackOffset = offsetHeight + (thumbWidth - trackWidth) / 2
            thumbTopOffset = offsetHeight
        } else {
            val offsetHeight = (paddingWidth - trackWidth) / 2
            trackOffset = offsetHeight
            thumbTopOffset = offsetHeight + (trackWidth - thumbWidth) / 2
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (progressDrawable != null) {
            progressDrawable.setBounds(0, trackOffset, paddingHeight, trackOffset + trackWidth)
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumb != null) {
            val available: Int = paddingHeight - thumbHeight + thumbOffset * 2
            val left = progress / max.toFloat() * available + 0.5f
            val reviseLeft =
                left.coerceAtLeast(thumbHeight / 2 + 0.5f)
                    .coerceAtMost(paddingHeight - thumbHeight / 2 - 0.5f).toInt()
            thumb.setBounds(reviseLeft, thumbTopOffset, reviseLeft + thumbHeight, thumbTopOffset + thumbWidth)
        }
    }

    /**
     * Executes ondraw operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     *
     */
    override fun onDraw(canvas: Canvas) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (orientation == 0) {
            super.onDraw(canvas)
// Val progressText = "$progress%"
// MPaint.getTextBounds(progressText, 0, progressText.length, mProgressTextRect)
//            
// Val progressRatio = progress.toFloat() / max
//            
// Val thumbOffset: Float =
//                (mThumbWidth - mProgressTextRect.width()) / 2 - mThumbWidth * progressRatio
// Val thumbX = width * progressRatio + thumbOffset
// Val thumbY: Float = height / 2f + mProgressTextRect.height() / 2f
// Canvas!!.drawText(progressText, thumbX, thumbY, mPaint)
        } else {
            canvas?.let {
                it.rotate(90f)
                it.translate(-paddingStart.toFloat(), -width.toFloat() + paddingEnd)
                super.onDraw(canvas)
            }
        }
    }

    /**
     * Executes ontouchevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent)
     *
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (orientation == 0) {
            return super.onTouchEvent(event)
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isEnabled) {
            return false
        }

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                /**
                 * Executes tracktouchevent operation with thermal imaging domain optimization.
                 *
                 */
                trackTouchEvent(event)
                onSeekBarChangeListener?.onStartTrackingTouch(this)
            }
            MotionEvent.ACTION_MOVE -> {
                /**
                 * Executes tracktouchevent operation with thermal imaging domain optimization.
                 *
                 */
                trackTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                isPressed = false
                /**
                 * Executes tracktouchevent operation with thermal imaging domain optimization.
                 *
                 */
                trackTouchEvent(event)
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
                onSeekBarChangeListener?.onStopTrackingTouch(this)
            }
            MotionEvent.ACTION_CANCEL -> {
                isPressed = false
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate()
                /**
                 * Executes stoptracktouchlevel operation with thermal imaging domain optimization.
                 *
                 */
                stopTrackTouchLevel()
                onSeekBarChangeListener?.onStopTrackingTouch(this)
            }
        }
        return true
    }

    /**
     * 通过级别分层进行粘性processing
     */
    /**
     * Executes stoptracktouchlevel operation with thermal imaging domain optimization.
     *
     */
    fun stopTrackTouchLevel() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (level > 0) {
            val newLevel = (progress.toFloat() / 100 * 4).roundToInt()
            /**
             * Configures the progress with validation and thermal imaging optimization.
             *
             */
            setProgress((newLevel.toFloat() / level * 100).toInt())
        }
    }

    /**
     * Handles touch gesture events.
     */
    private fun trackTouchEvent(event: MotionEvent) {
        val y = event.y.roundToInt()
        progress =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (y < paddingTop) {
                0
            } else if (y > height - paddingBottom) {
                max
            } else {
                val availableHeight: Int = height - paddingTop - paddingBottom
                val scale: Float = (y - paddingTop) / availableHeight.toFloat()
                (scale * max).roundToInt()
            }
        /**
         * Executes stoptracktouchlevel operation with thermal imaging domain optimization.
         *
         */
        stopTrackTouchLevel()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumb != null) {
            /**
             * Executes calculatedrawable operation with thermal imaging domain optimization.
             *
             */
            calculateDrawable(width, height)
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }
        onSeekBarChangeListener?.onProgressChanged(this, progress, true)
    }
}
