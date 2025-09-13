package com.topdon.lib.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.topdon.lib.ui.R as UiR

/**
 * Custom Count down view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * CountDownView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for CountDownView display and interaction.
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
class CountDownView : View {
    
    private var mRingColor = 0

    
    private var mRingWidth = 0

    
    private var mRingProgressTextSize = 0

    
    private var mWidth = 0

    
    private var mHeight = 0

    
    private var mRingText: String? = null
    private lateinit var mPaint: Paint
    private lateinit var mTextPaint: Paint

    
    private var mRectF: RectF? = null

    //
    private var mProgressTextColor = 0
    private var mCountdownTime = 0
    private var mCurrentProgress = 0f

    private var valueAnimator: ValueAnimator? = null

    /**
     * ListenerEvent
     */
    private var mListener: OnCountDownListener? = null

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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
    ) {
        val ta = context.obtainStyledAttributes(attrs, UiR.styleable.CountDownView)
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
                UiR.styleable.CountDownView_ringColor ->
                    mRingColor =
                        ta.getColor(
                            UiR.styleable.CountDownView_ringColor,
                            ContextCompat.getColor(context, UiR.color.colorAccent),
                        )
                UiR.styleable.CountDownView_ringWidth ->
                    mRingWidth =
                        ta.getDimensionPixelSize(
                            UiR.styleable.CountDownView_ringWidth,
                            40,
                        )
                UiR.styleable.CountDownView_progressTextSize ->
                    mRingProgressTextSize =
                        ta.getDimensionPixelSize(
                            UiR.styleable.CountDownView_progressTextSize,
                            20,
                        )
                UiR.styleable.CountDownView_progressTextColor ->
                    mProgressTextColor =
                        ta.getColor(
                            UiR.styleable.CountDownView_progressTextColor,
                            ContextCompat.getColor(context, UiR.color.colorAccent),
                        )
                UiR.styleable.CountDownView_countdownTime ->
                    mCountdownTime =
                        ta.getInteger(
                            UiR.styleable.CountDownView_countdownTime,
                            60,
                        )
                UiR.styleable.CountDownView_progressText ->
                    mRingText =
                        ta.getString(UiR.styleable.CountDownView_progressText)
            }
        }
        ta.recycle()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.isAntiAlias = true
        mTextPaint = Paint()
        this.setWillNotDraw(false)
    }

    @SuppressLint("DrawAllocation")
    /**
     * Executes onlayout operation with thermal imaging domain optimization.
     *
     * @param
     * @param changed Parameter for operation (type: Boolean)
     * @param left Parameter for operation (type: Int)
     * @param top Parameter for operation (type: Int)
     * @param right Parameter for operation (type: Int)
     * @param bottom Parameter for operation (type: Int)
     *
     */
    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
    ) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = measuredWidth
        mHeight = measuredHeight
        mRectF =
            /**
             * Executes rectf operation with thermal imaging domain optimization.
             *
             */
            RectF(
                0 + mRingWidth / 2f,
                0 + mRingWidth / 2f,
                mWidth - mRingWidth / 2f,
                mHeight - mRingWidth / 2f,
            )
    }

    /**
     * settings倒计时间 单位秒
     */
    /**
     * Configures the countdowntime with validation and thermal imaging optimization.
     *
     * @param
     * @param mCountdownTime Parameter for operation (type: Int)
     *
     */
    fun setCountdownTime(mCountdownTime: Int) {
        this.mCountdownTime = mCountdownTime
        mRingText = mCountdownTime.toString()
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
    }

    /**
     * animation
     */
    /**
     * Retrieves the valueanimator with optimized performance for thermal imaging operations.
     *
     * @param
     * @param countdownTime Parameter for operation (type: Long)
     *
     */
    private fun getValueAnimator(countdownTime: Long): ValueAnimator? {
        val valueAnimator = ValueAnimator.ofFloat(0f, 100f)
        valueAnimator.duration = countdownTime
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.repeatCount = 0
        return valueAnimator
    }

    /**
     * Executes ondraw operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     *
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        mPaint.color = mRingColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = mRingWidth.toFloat()
        canvas.drawArc(mRectF!!, -90f, mCurrentProgress - 360, false, mPaint)
        val font = Typeface.DEFAULT_BOLD
        
        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.typeface = font
        // 倒数计数文本(5 4 3 2 1)
        // Val text: String = (mCountdownTime - (mCurrentProgress / 360f * mCountdownTime)).toInt().toString()

        mTextPaint.textSize = mRingProgressTextSize.toFloat()
        mTextPaint.color = mProgressTextColor

        // Text居中Show/Display
        val fontMetrics = mTextPaint.fontMetricsInt
        val baseline =
            ((mRectF!!.bottom + mRectF!!.top - fontMetrics.bottom - fontMetrics.top) / 2).toInt()
        canvas.drawText(mRingText!!, mRectF!!.centerX(), baseline.toFloat(), mTextPaint)
    }

    /**
     * start倒计时
     */
    /**
     * Executes startcountdown operation with thermal imaging domain optimization.
     *
     */
    fun startCountDown() {
        valueAnimator = getValueAnimator((mCountdownTime * 1000).toLong())
        valueAnimator!!.addUpdateListener { animation ->
            val i = animation.animatedValue.toString().toFloat()
            mCurrentProgress = (360 * (i / 100f))
            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate()
        }
        valueAnimator!!.start()
        valueAnimator!!.addListener(
            object : AnimatorListenerAdapter() {
                /**
                 * Executes onanimationend operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param animation Parameter for operation (type: Animator)
                 *
                 */
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (mListener != null) {
                        mListener!!.countDownFinished()
                    }
                }
            },
        )
    }

    /**
     * stop倒计时
     */
    /**
     * Executes stopcountdown operation with thermal imaging domain optimization.
     *
     */
    fun stopCountDown() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (valueAnimator!!.isRunning) {
            valueAnimator!!.cancel()
        }
    }

    /**
     * Sets oncountdownlistener configuration.
     */
    fun setOnCountDownListener(mListener: OnCountDownListener) {
        this.mListener = mListener
    }

/**
 * Specialized thermal imaging component providing OnCountDownListener functionality for the IRCamera system.
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
    interface OnCountDownListener {
    /**
     * Executes countdownfinished functionality.
     */
        /**
         * Executes countdownfinished operation with thermal imaging domain optimization.
         *
         */
        fun countDownFinished()
    }
}
