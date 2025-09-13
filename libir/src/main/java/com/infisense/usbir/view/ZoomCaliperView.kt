package com.infisense.usbir.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.hardware.camera2.*
import android.os.Build
import android.util.AttributeSet
import android.util.Size
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Magnifier
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.infisense.usbir.R
import com.infisense.usbir.utils.TargetUtils
import com.topdon.lib.core.bean.ObserveBean

/**
 * Scaleview基class - Optimized findViewById usage
 */
/**
 * ZoomCaliperView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ZoomCaliperView display and interaction.
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
class ZoomCaliperView : LinearLayout, ScaleGestureDetector.OnScaleGestureListener {
    private var centerX: Float = Float.MAX_VALUE
    private var centerY: Float = Float.MAX_VALUE
    private var cameraCharacteristics: CameraCharacteristics? = null
    private var isReverse: Boolean = false
    private lateinit var mTextureView: View
    private var canScale = false
    private var def_caliper = 180f 
    var magnifier: Magnifier? = null
    var textureMagnifier: Magnifier? = null
    var m: Float = 0.0f

    var zoomViewCloseListener: (() -> Unit)? = null

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
        inflate(context, R.layout.zoom_bb, this)
        // Cache view reference instead of repeated findViewById calls
        mTextureView = findViewById(R.id.camera_texture)
        lis = ScaleGestureDetector(context, this)
        originalBitmap = (androidx.core.content.ContextCompat.getDrawable(context, R.drawable.svg_ic_target_horizontal_person_green) as? BitmapDrawable)?.bitmap
            ?: return // Early return if bitmap is null
// PxBitmapHeight = originalBitmap.height.toFloat()
        originalBitmapWidth = originalBitmap.width.toFloat()
        originalBitmapHeight = originalBitmap.height.toFloat()
// SetCaliperM(50f)
        /**
         * Executes onresumeview operation with thermal imaging domain optimization.
         *
         */
        onResumeView()
    }

    /**
     * Sets imagesize configuration.
     */
    fun setImageSize(
        imageHeight: Int,
        imageWidth: Int,
        parentViewWidth: Int,
        parentViewHeight: Int,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this.imageHeight == imageHeight && this.imageWidth == imageWidth)
            {
                return
            }
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
        showBitmapHeightWidth = pxBitmapHeight * originalBitmapWidth / originalBitmapHeight * xscale
        val layoutParams = mTextureView.layoutParams
        layoutParams.width = showBitmapHeightWidth.toInt()
        layoutParams.height = showBitmapHeight.toInt()
//        Log.e("Test","rotation后的宽高：target"+showBitmapHeight+"// /"+imageHeight+"---")
        mTextureView.layoutParams = layoutParams
        (mTextureView as ImageView).setImageBitmap(originalBitmap)
    }

    /**
     * Executes ondetachedfromwindow operation with thermal imaging domain optimization.
     *
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    private var startX = 0f 
    private var startY = 0f
    private var moveX = 0f
    private var moveY = 0f
    private var parentViewW = 0f
    private var parentViewH = 0f
    private var isScale = false
    private var scale = 1f
    private var scaleW = 0f 
    private var scaleH = 0f

    
    private lateinit var originalBitmap: Bitmap
    private var imageWidth = 0
    private var imageHeight = 0
    private var parentViewWidth = 0f
    private var parentViewHeight = 0f
    private var xscale = 0f
    private var yscale = 0f
    private var originalBitmapWidth = 0f
    private var originalBitmapHeight = 0f

    private var pxBitmapHeight = 200f

    private var showBitmapHeightWidth = 0f
    private var showBitmapHeight = 0f

    private lateinit var lis: ScaleGestureDetector
    var isCheckChildView = false
    var contentWith = 0
    var contentHeight = 0

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
        if (canScale && isScale && event.action != MotionEvent.ACTION_UP) {
            return lis.onTouchEvent(event)
        }
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scaleW = mTextureView.width * (scale - 1) / 2f
                scaleH = mTextureView.height * (scale - 1) / 2f
                startX = event.x - mTextureView.x
                startY = event.y - mTextureView.y
                val view: View = mTextureView.parent as View
                parentViewW = view.measuredWidth.toFloat()
                parentViewH = view.measuredHeight.toFloat()
                isCheckChildView = isTouchPointInView(mTextureView, event.rawX.toInt(), event.rawY.toInt())
            }
            MotionEvent.ACTION_MOVE -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isCheckChildView)
                    {
                        
                        moveX = event.x - startX
                        moveY = event.y - startY
                        
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (m < 100f && m >= 50f)
                            {
                                contentWith = (mTextureView.measuredWidth / 2).toInt()
                                contentHeight = (mTextureView.measuredHeight / 2).toInt()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveX < (-contentWith / 2)) moveX = (-contentWith / 2).toFloat()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveY < (-contentHeight / 2)) moveY = (-contentHeight / 2).toFloat()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveX > parentViewW - contentWith * 4 / 3) {
                                    moveX = parentViewW - contentWith * 4 / 3
                                }
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (parentViewH > parentViewW)
                                    {
                                        /**
                                         * Executes if operation with thermal imaging domain optimization.
                                         *
                                         */
                                        if (moveY > parentViewH - contentHeight * 4 / 3) {
                                            moveY = parentViewH - contentHeight * 4 / 3
                                        }
                                    } else
                                    {
                                        /**
                                         * Executes if operation with thermal imaging domain optimization.
                                         *
                                         */
                                        if (moveY > parentViewH - contentHeight * 4 / 3) {
                                            moveY = parentViewH - contentHeight * 4 / 3
                                        }
                                    }
                            } else if (m <= 20f)
                            {
                                contentWith = (mTextureView.measuredWidth / 2f).toInt()
                                contentHeight = (mTextureView.measuredHeight / 2f).toInt()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveX < (-contentWith / 2)) moveX = (-contentWith / 2).toFloat()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveY < (-contentHeight / 2)) moveY = (-contentHeight / 2).toFloat()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveX > parentViewW - contentWith) {
                                    moveX = parentViewW - contentWith
                                }
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (parentViewH > parentViewW)
                                    {
                                        /**
                                         * Executes if operation with thermal imaging domain optimization.
                                         *
                                         */
                                        if (moveY > parentViewH - contentHeight) {
                                            moveY = parentViewH - contentHeight
                                        }
                                    } else
                                    {
                                        /**
                                         * Executes if operation with thermal imaging domain optimization.
                                         *
                                         */
                                        if (moveY > parentViewH - contentHeight) {
                                            moveY = parentViewH - contentHeight
                                        }
                                    }
                            } else
                            {
                                contentWith = mTextureView.width
                                contentHeight = mTextureView.height
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveX < (-contentWith / 2)) moveX = (-contentWith / 2).toFloat()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveY < (-contentHeight / 2)) moveY = (-contentHeight / 2).toFloat()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveX > parentViewW - mTextureView.width / 2) {
                                    moveX = parentViewW - mTextureView.width / 2
                                }
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (moveY > parentViewH - mTextureView.height / 2) {
                                    moveY = parentViewH - mTextureView.height / 2
                                }
                            }
                        mTextureView.x = moveX
                        mTextureView.y = moveY
                        centerX = mTextureView.x + mTextureView.measuredWidth / 2
                        centerY = mTextureView.y + mTextureView.measuredHeight / 2
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && m < 100f) {
                            magnifier?.show(centerX, centerY)
                        }
                    }
            }
            MotionEvent.ACTION_UP -> {
                isCheckChildView = false
                isScale = false
                val startX = viewX
                val startY = viewY
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if ((viewX < 0 && startX < -mTextureView.width * scale + SizeUtils.dp2px(10f)) ||
                    (startX > 0 && startX > parentViewW - SizeUtils.dp2px(10f)) ||
                    (startY < 0 && startY < -mTextureView.height * scale + SizeUtils.dp2px(10f)) ||
                    (startY > 0 && startY > parentViewH - SizeUtils.dp2px(10f))
                )
                    {
                        zoomViewCloseListener?.invoke()
                    }
            }
        }
        var canTouch = isCheckChildView
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (canScale)
            {
                canTouch = lis.onTouchEvent(event)
            }
// If (!isCheckChildView){
// ParentView.requestFocus()
//        }
        return canTouch
    }

    /**
     * Executes onattachedtowindow operation with thermal imaging domain optimization.
     *
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    /**
     * Handles touch gesture events.
     */
    private fun isTouchPointInView(
        targetView: View?,
        xAxis: Int,
        yAxis: Int,
    ): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (targetView == null) {
            return false
        }
        val location = IntArray(2)
        targetView.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + targetView.measuredWidth
        val bottom = top + targetView.measuredHeight
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (yAxis >= top) && (yAxis <= bottom) && (xAxis >= left) && (xAxis <= right)
    }

    /**
     * Executes onscale operation with thermal imaging domain optimization.
     *
     * @param
     * @param detector Parameter for operation (type: ScaleGestureDetector)
     *
     */
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        
        isScale = true
        detector?.let {
            val scaleFactor = it.scaleFactor - 1
            scale += scaleFactor
            mTextureView.scaleX = scale
            mTextureView.scaleY = scale
        }
        return true
    }

    /**
     * Executes onscalebegin operation with thermal imaging domain optimization.
     *
     * @param
     * @param detector Parameter for operation (type: ScaleGestureDetector)
     *
     */
    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        isScale = true
        return true
    }

    /**
     * Executes onscaleend operation with thermal imaging domain optimization.
     *
     * @param
     * @param detector Parameter for operation (type: ScaleGestureDetector)
     *
     */
    override fun onScaleEnd(detector: ScaleGestureDetector) {
    }

    /**预览大小 */
    private var mPreviewSize: Size? = null

    /**
     * Configures the rotation with validation and thermal imaging optimization.
     *
     * @param
     * @param isReverse Parameter for operation (type: Boolean)
     *
     */
    fun setRotation(isReverse: Boolean)  {
        this.isReverse = isReverse
        /**
         * Executes updaterotation operation with thermal imaging domain optimization.
         *
         */
        updateRotation()
    }

    /**
     * Updates the rotation with new data.
     */
    private fun updateRotation()  {
        if (isReverse)
            {
                mTextureView.rotation = 180f
            } else
            {
                mTextureView.rotation = 0f
            }
    }

    /**
     * Callback method triggered when resumeview occurs.
     */
    private fun onResumeView() {
    }

    val viewX: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = mTextureView.x - (viewWidth - mTextureView.width) / 2
    val viewY: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = mTextureView.y - (viewHeight - mTextureView.height) / 2
    val viewAlpha: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = mTextureView.alpha
    val viewWidth: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = mTextureView.width * scale
    val viewHeight: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = mTextureView.height * scale
    val viewScale: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = scale

    /**
     * Sets cameraalpha configuration.
     */
    fun setCameraAlpha(alpha: Float)  {
        mTextureView?.alpha = 1 - alpha
    }

    /**
     * Sets caliperm configuration.
     */
    fun setCaliperM(m: Float)  {
        scale = m / def_caliper
        mTextureView.scaleX = scale
        mTextureView.scaleY = scale
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
    }

    private var curChooseMeasureMode: Int = ObserveBean.TYPE_MEASURE_PERSON
    private var curChooseTargetMode: Int = ObserveBean.TYPE_TARGET_HORIZONTAL

    /**
     * Updates the selectbitmap with new data.
     */
    fun updateSelectBitmap(
        targetMeasureMode: Int,
        targetType: Int,
        targetColorType: Int,
        parentCameraView: View?,
    )  {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (curChooseTargetMode == targetType && curChooseMeasureMode == targetMeasureMode)
            {
                return
            }
        curChooseMeasureMode = targetMeasureMode
        curChooseTargetMode = targetType
        /**
         * Executes updatetargetbitmap operation with thermal imaging domain optimization.
         *
         */
        updateTargetBitmap(targetMeasureMode, targetType, targetColorType, parentCameraView)
    }

    /**
     * Updates the targetbitmap with new data.
     */
    fun updateTargetBitmap(
        targetMeasureMode: Int,
        targetType: Int,
        targetColorType: Int,
        parentCameraView: View?,
    )  {
        this.visibility = View.VISIBLE
        m = TargetUtils.getMeasureSize(targetMeasureMode)
        val targetIcon = TargetUtils.getSelectTargetDraw(targetMeasureMode, targetType, targetColorType)
        originalBitmap = (androidx.core.content.ContextCompat.getDrawable(context, targetIcon) as? BitmapDrawable)?.bitmap ?: return
        (mTextureView as ImageView).setImageBitmap(originalBitmap)
//        Log.e("Test","rotation后的宽高updateSelectBitmap"+parentCameraView!!.width+"---"+parentCameraView!!.height)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            magnifier?.dismiss()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (m >= 100f)
                {
                    /**
                     * Configures the caliperm with validation and thermal imaging optimization.
                     *
                     */
                    setCaliperM(def_caliper)
                    mTextureView.visibility = View.VISIBLE
                    textureMagnifier?.dismiss()
                    magnifier?.dismiss()
                    /**
                     * Executes invalidate operation with thermal imaging domain optimization.
                     *
                     */
                    invalidate()
                    return
                }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (parentCameraView != null)
                {
                    val builder = Magnifier.Builder(parentCameraView)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (m < 50f)
                        {
                            /**
                             * Configures the caliperm with validation and thermal imaging optimization.
                             *
                             */
                            setCaliperM(def_caliper / 2)
                            mTextureView.visibility = View.INVISIBLE
                            builder.setInitialZoom(4f)
                            builder.setCornerRadius(SizeUtils.dp2px(282f).toFloat())
                            builder.setClippingEnabled(false)
                            builder.setOverlay(ContextCompat.getDrawable(context, targetIcon))
                            builder.setSize(
                                SizeUtils.dp2px(282f),
                                SizeUtils.dp2px(282f),
                            )
                            magnifier = builder.build()
                        } else if (m >= 50f && m < 100f)
                        {
                            /**
                             * Configures the caliperm with validation and thermal imaging optimization.
                             *
                             */
                            setCaliperM(def_caliper / 2)
                            mTextureView.visibility = View.VISIBLE
// Builder.setInitialZoom(1.15f)
                            builder.setInitialZoom(2f)
// Builder.setOverlay(ContextCompat.getDrawable(context,targetIcon))
                            builder.setCornerRadius(SizeUtils.dp2px(282f).toFloat())
                            builder.setClippingEnabled(false)
                            builder.setSize(
                                SizeUtils.dp2px(282f),
                                SizeUtils.dp2px(282f),
                            )
                            magnifier = builder.build()
                        }
                }
            /**
             * Executes requestlayout operation with thermal imaging domain optimization.
             *
             */
            requestLayout()
            mTextureView.postDelayed(
                Runnable {
// If (centerX == Float.MAX_VALUE && centerY == Float.MAX_VALUE){
                    centerX = parentCameraView!!.measuredWidth.toFloat() / 2
                    centerY = parentCameraView!!.measuredHeight.toFloat() / 2
                    mTextureView.x = centerX - mTextureView.measuredWidth / 2
                    mTextureView.y = centerY - mTextureView.measuredHeight / 2
//                }
                    magnifier?.show(centerX, centerY)
                },
                200,
            )
        }
    }

    /**
     * Executes hideview functionality.
     */
    /**
     * Executes hideview operation with thermal imaging domain optimization.
     *
     */
    fun hideView()  {
        this.visibility = GONE
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            magnifier?.dismiss()
        }
    }

    /**
     * Executes showview functionality.
     */
    /**
     * Executes showview operation with thermal imaging domain optimization.
     *
     */
    fun showView()  {
        this.visibility = VISIBLE
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            magnifier?.show(centerX, centerY)
        }
    }

    /**
     * Updates the magnifier with new data.
     */
    fun updateMagnifier()  {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            magnifier?.update()
        }
    }

    /**
     * 还原
     */
    /**
     * Executes del operation with thermal imaging domain optimization.
     *
     * @param
     * @param reductionXY Parameter for operation (type: Boolean)
     *
     */
    fun del(reductionXY: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            magnifier?.dismiss()
        }
        curChooseMeasureMode = ObserveBean.TYPE_MEASURE_PERSON
        curChooseTargetMode = ObserveBean.TYPE_TARGET_HORIZONTAL
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this.visibility == View.VISIBLE)
            {
                this.visibility = GONE
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (reductionXY)
                    {
                        centerX = Float.MAX_VALUE
                        centerY = Float.MAX_VALUE
                    } else
                    {
                        val parent = parent as ViewGroup
                        centerX = parent.measuredWidth.toFloat() / 2
                        centerY = parent.measuredHeight.toFloat() / 2
                        mTextureView.x = centerX - mTextureView.width / 2
                        mTextureView.y = centerY - mTextureView.height / 2
                    }
            }
    }

    /**
     * Updates the center with new data.
     */
    fun updateCenter()  {
        val parent = parent as ViewGroup
        centerX = parent.measuredWidth.toFloat() / 2
        centerY = parent.measuredHeight.toFloat() / 2
        mTextureView.x = centerX - mTextureView.width / 2
        mTextureView.y = centerY - mTextureView.height / 2
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            magnifier?.show(centerX, centerY)
        }
    }
}
