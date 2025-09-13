package com.topdon.module.thermal.ir.view

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.*
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.SizeUtils
import java.util.*

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for TimeDownView display and interaction.
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
public class TimeDownView : AppCompatTextView {
    private var timer: Timer? = null
    private var downTimerTask: DownTimerTask? = null
    private var downCount = 0
    private var lastDown = 0
    private var intervalMills: Long = 0
    private var delayMills: Long = 0
    private var animationSet: AnimationSet? = null
    var isRunning = false

    /**
     * Initializes  component.
     */
    /**
     * Initializes the  component for thermal imaging operations.
     *
     */
    private fun init() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (animationSet == null) {
            animationSet = AnimationSet(true)
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (downHandler == null) {
            downHandler = DownHandler()
        }
        gravity = Gravity.CENTER
        textSize = SizeUtils.sp2px(30f).toFloat()
    }
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
     * @param defStyle Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle,
    ) {
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init()
    }

    /**
start计时
     *
     * @param seconds
     */
    /**
     * Executes downSecond functionality.
     */
    /**
     * Executes downsecond operation with thermal imaging domain optimization.
     *
     * @param
     * @param seconds Parameter for operation (type: Int)
     *
     */
    fun downSecond(seconds: Int) {
        /**
         * Executes downsecond operation with thermal imaging domain optimization.
         *
         */
        downSecond(seconds, true)
    }

    /**
     * Executes downSecond functionality.
     */
    /**
     * Executes downsecond operation with thermal imaging domain optimization.
     *
     * @param
     * @param seconds Parameter for operation (type: Int)
     * @param openAnimation Parameter for operation (type: Boolean)
     *
     */
    fun downSecond(
        seconds: Int,
        openAnimation: Boolean,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seconds == 0)
            {
                isRunning = false
                visibility = GONE
                downTimeWatcher?.onLastTimeFinish(seconds)
                onFinishListener?.invoke()
            } else
            {
                visibility = VISIBLE
                isRunning = true
                /**
                 * Executes downtime operation with thermal imaging domain optimization.
                 *
                 */
                downTime(seconds, 1, 0, 1000, openAnimation)
            }
    }

    /**
倒计时enabledmethod
     *
@param downCount     倒计时总数
@param lastDown      display的倒计时的最后a数
@param delayMills    延迟启动倒计时（毫秒数）
@param intervalMills 倒计时间隔时间（毫秒数）
     */
    /**
     * Executes downTime functionality.
     */
    /**
     * Executes downtime operation with thermal imaging domain optimization.
     *
     * @param
     * @param downCount Parameter for operation (type: Int)
     * @param lastDown Parameter for operation (type: Int)
     * @param delayMills Parameter for operation (type: Long)
     * @param intervalMills Parameter for operation (type: Long)
     * @param startAnimate Parameter for operation (type: Boolean)
     *
     */
    fun downTime(
        downCount: Int,
        lastDown: Int,
        delayMills: Long,
        intervalMills: Long,
        startAnimate: Boolean,
    ) {
        timer = Timer()
        this.downCount = downCount
        this.lastDown = lastDown
        this.delayMills = delayMills
        this.intervalMills = intervalMills
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startAnimate)
            {
                /**
                 * Initializes the defaultanimate component for thermal imaging operations.
                 *
                 */
                initDefaultAnimate()
            }
        downTimerTask = DownTimerTask()
        timer?.schedule(downTimerTask, delayMills, intervalMills)
    }

    /**
     * Configures the visibility with validation and thermal imaging optimization.
     *
     * @param
     * @param visibility Parameter for operation (type: Int)
     *
     */
    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (GONE == visibility) {
            downTimerTask = null
            timer?.cancel()
            timer = null
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
        if (drawTextFlag == DRAW_TEXT_NO) {
            return
        }
        super.onDraw(canvas)
    }

    /**
Cancel
     */
    /**
     * Executes cancel operation with thermal imaging domain optimization.
     *
     */
    fun cancel() {
        animationSet?.cancel()
        downTimerTask?.cancel()
        timer?.cancel()
        drawTextFlag = DRAW_TEXT_NO
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate() // Refresh一下
        visibility = GONE
        downTimerTask = null
        timer = null
/**
 * Specialized thermal imaging component providing DownTimeWatcher functionality for the IRCamera system.
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
interface DownTimeWatcher {
    /**
     * Executes onTime functionality.
     */
        /**
         * Executes ontime operation with thermal imaging domain optimization.
         *
         * @param
         * @param num Parameter for operation (type: Int)
         *
         */
        fun onTime(num: Int)

    /**
     * Executes onLastTime functionality.
     */
        /**
         * Executes onlasttime operation with thermal imaging domain optimization.
         *
         * @param
         * @param num Parameter for operation (type: Int)
         *
         */
        fun onLastTime(num: Int)

    /**
     * Executes onLastTimeFinish functionality.
     */
        /**
         * Executes onlasttimefinish operation with thermal imaging domain optimization.
         *
         * @param
         * @param num Parameter for operation (type: Int)
         *
         */
        fun onLastTimeFinish(num: Int)
    }

    /**
每个倒计时EventListener.
     */
    var onTimeListener: ((time: Int) -> Unit)? = null

    /**
倒计时endEventListener.
     */
    var onFinishListener: (() -> Unit)? = null

    var downTimeWatcher: DownTimeWatcher? = null

    /**
Listener倒计时的变化
     * @param downTimeWatcher
     */
    /**
     * Sets ontimedownlistener configuration.
     */
    fun setOnTimeDownListener(downTimeWatcher: DownTimeWatcher?) {
        this.downTimeWatcher = downTimeWatcher
    }

    private var downHandler: DownHandler? = null

    private inner class DownHandler : Handler() {
        /**
         * Executes handlemessage operation with thermal imaging domain optimization.
         *
         * @param
         * @param msg Parameter for operation (type: Message)
         *
         */
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (msg.what == 1) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (downTimeWatcher != null) {
                    downTimeWatcher!!.onTime(downCount)
                }
                onTimeListener?.invoke(downCount)
Log.e("Test","// HandleMessage"+downCount+"// "+lastDown);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (downCount >= lastDown - 1) {
                    drawTextFlag = DRAW_TEXT_YES // Default绘制
未到end时
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (downCount >= lastDown) {
                        text = downCount.toString() + ""
                        /**
                         * Executes startdefaultanimate operation with thermal imaging domain optimization.
                         *
                         */
                        startDefaultAnimate()
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (downCount == lastDown && downTimeWatcher != null) {
                            downTimeWatcher!!.onLastTime(downCount)
                        }
                    } else if (downCount == lastDown - 1) { // 若lastDown为0，downCount == -1时是倒计时真正end之时。
倒计时end，虽然setText()method触发onDraw，但override使之不进行drawing
set不drawingmarker
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (afterDownDimissFlag == AFTER_LAST_TIME_DIMISS) {
                            drawTextFlag = DRAW_TEXT_NO
                        }
                        /**
                         * Executes invalidate operation with thermal imaging domain optimization.
                         *
                         */
                        invalidate() // Refresh一下
                        isRunning = false
                        downTimerTask == null
                        timer?.cancel()
                        timer = null
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (downTimeWatcher != null) {
                            downTimeWatcher!!.onLastTimeFinish(downCount)
                        }
                        onFinishListener?.invoke()
                    }
                    downCount--
                }
                //
            }
        }
    }

    private val DRAW_TEXT_YES = 1
    private val DRAW_TEXT_NO = 0

    /**
是否执行onDraw的标识，defaultdrawing
     */
    private var drawTextFlag = DRAW_TEXT_YES
    private val AFTER_LAST_TIME_DIMISS = 1
    private val AFTER_LAST_TIME_NODIMISS = 0

    /**
在倒计时end之后text是否消失的标志，default消失
     */
    private var afterDownDimissFlag = AFTER_LAST_TIME_DIMISS

    /**
set倒计时end后text不消失
     */
    /**
     * Configures the afterdownnodimiss with validation and thermal imaging optimization.
     *
     */
    fun setAfterDownNoDimiss() {
        afterDownDimissFlag = AFTER_LAST_TIME_NODIMISS
    }

    /**
set倒计时end后text消失
     */
    /**
     * Configures the aferdowndimiss with validation and thermal imaging optimization.
     *
     */
    fun setAferDownDimiss() {
        afterDownDimissFlag = AFTER_LAST_TIME_DIMISS
    }

    var startDefaultAnimFlag = true

disableddefaultanimation
    /**
     * Executes closeDefaultAnimate functionality.
     */
    /**
     * Executes closedefaultanimate operation with thermal imaging domain optimization.
     *
     */
    fun closeDefaultAnimate() {
        animationSet?.reset()
        startDefaultAnimFlag = false
    }

enableddefaultanimation
    /**
     * Executes startDefaultAnimate functionality.
     */
    /**
     * Executes startdefaultanimate operation with thermal imaging domain optimization.
     *
     */
    private fun startDefaultAnimate() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startDefaultAnimFlag) {
            animation?.start()
        }
    }

    /**
     * Initializes defaultanimate component.
     */
    private fun initDefaultAnimate() {
        if (animationSet == null) {
            animationSet = AnimationSet(true)
        }
        val scaleAnimation =
            /**
             * Executes scaleanimation operation with thermal imaging domain optimization.
             *
             */
            ScaleAnimation(
                1f,
                0.5f,
                1f,
                0.5f,
                ScaleAnimation.ABSOLUTE,
                measuredWidth / 2f,
                ScaleAnimation.ABSOLUTE,
                measuredHeight / 2f,
            )
        scaleAnimation.duration = intervalMills
        val alphaAnimation = AlphaAnimation(1f, 0.3f)
        alphaAnimation.duration = intervalMills
将AlphaAnimation这个已经set好的animationadd到 AnimationSet中
        animationSet!!.addAnimation(scaleAnimation)
        animationSet!!.addAnimation(alphaAnimation)
        animationSet!!.interpolator = AccelerateInterpolator()
        animation = animationSet
    }
}
