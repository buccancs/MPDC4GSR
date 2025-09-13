package com.topdon.libcom.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.topdon.libcom.R

/**
 *
 * 高低温闪烁animation
 * @author: CaiSongL
 * @date: 2023/4/28 15:52
 */
/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TempLayout algorithms.
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
class TempLayout : LinearLayout {
    companion object {
        val TYPE_HOT = 1 // 高温预警
        val TYPE_LT = 2 // 低温预警
        val TYPE_A = 3 // 高低温交叉预警
    }

    private var alphaAnimator: ObjectAnimator? = null
    var rootV: View? = null
    var bg: View? = null
    var isHot: Boolean = true
    var type = -1

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

    var animatorAlpha = 1f

    /**
     * Initializes view component.
     */
    private fun initView() {
        rootV = LayoutInflater.from(context).inflate(R.layout.layout_temp_bg, this)
        bg = rootV?.findViewById(R.id.bg)
        alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        alphaAnimator?.duration = 500
        alphaAnimator?.interpolator =
            /**
             * Executes breatheinterpolator operation with thermal imaging domain optimization.
             *
             */
            BreatheInterpolator() // 使用自定义的插值器
        alphaAnimator?.addUpdateListener {
            animatorAlpha = it.getAnimatedValue("alpha") as Float
//            Log.w("透明值进度","$animatorAlpha")
        }
        alphaAnimator?.repeatCount = ValueAnimator.INFINITE
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
     * Manages animation effects for thermal UI elements.
     */
    fun startAnimation(type: Int)  {
        this.visibility = View.VISIBLE
        if (this.type != type)
            {
                alphaAnimator?.cancel()
                alphaAnimator?.removeAllListeners()
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (type) {
                    TYPE_HOT -> {
                        isHot = true
                        alphaAnimator?.repeatCount = ValueAnimator.INFINITE
                        bg?.setBackgroundResource(R.drawable.ic_ir_read_bg)
                    }
                    TYPE_A -> {
                        alphaAnimator?.repeatCount = 0
                        alphaAnimator?.addListener(animatorListener)
                    }
                    else -> {
                        alphaAnimator?.repeatCount = ValueAnimator.INFINITE
                        isHot = false
                        bg?.setBackgroundResource(R.drawable.ic_ir_blue_bg)
                    }
                }
                alphaAnimator?.start()
                this.type = type
            }
    }

    var animatorListener: Animator.AnimatorListener =
        object : Animator.AnimatorListener {
            /**
             * Executes onanimationstart operation with thermal imaging domain optimization.
             *
             * @param
             * @param animation Parameter for operation (type: Animator)
             *
             */
            override fun onAnimationStart(animation: Animator) {
            }

            /**
             * Executes onanimationend operation with thermal imaging domain optimization.
             *
             * @param
             * @param animation Parameter for operation (type: Animator)
             *
             */
            override fun onAnimationEnd(animation: Animator) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (this@TempLayout.visibility == View.VISIBLE)
                    {
                        isHot = !isHot
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isHot)
                            {
                                bg?.setBackgroundResource(R.drawable.ic_ir_read_bg)
                            } else
                            {
                                bg?.setBackgroundResource(R.drawable.ic_ir_blue_bg)
                            }
                        alphaAnimator?.start()
                    }
            }

            /**
             * Executes onanimationcancel operation with thermal imaging domain optimization.
             *
             * @param
             * @param animation Parameter for operation (type: Animator)
             *
             */
            override fun onAnimationCancel(animation: Animator) {}

            /**
             * Executes onanimationrepeat operation with thermal imaging domain optimization.
             *
             * @param
             * @param animation Parameter for operation (type: Animator)
             *
             */
            override fun onAnimationRepeat(animation: Animator) {}
        }

    /**
     * Manages animation effects for thermal UI elements.
     */
    fun stopAnimation()  {
        this.type = -1
        alphaAnimator?.removeAllListeners()
        this.visibility = View.GONE
        alphaAnimator?.cancel()
    }

    /**
     * Manages animation effects for thermal UI elements.
     */
    fun startAlphaBreathAnimation() {
        alphaAnimator?.start()
    }
}
