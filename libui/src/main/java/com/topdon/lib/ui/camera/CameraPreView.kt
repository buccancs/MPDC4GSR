package com.topdon.lib.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintSet
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.topdon.lib.core.listener.BitmapViewListener
import com.topdon.lib.ui.databinding.CameraLayBinding
import java.util.Collections

/**
 * camera预览
 */
/**
 * Custom Camera pre view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * CameraPreView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraPreView functionality.
 *
 * Provides advanced camera functionality for thermal imaging capture,
 * including temperature measurement and pseudo color visualization.
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
class CameraPreView :
    LinearLayout,
    ScaleGestureDetector.OnScaleGestureListener,
    BitmapViewListener {
    private lateinit var binding: CameraLayBinding
    private var cameraCharacteristics: CameraCharacteristics? = null
    private var isReverse: Boolean = false
    private var cameraWidth = 0

    var isPreviewing = false

    var cameraPreViewCloseListener: (() -> Unit)? = null

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
        binding = CameraLayBinding.inflate(LayoutInflater.from(context), this, true)
        binding.cameraTexture.post { cameraWidth = binding.cameraTexture.width }
        lis = ScaleGestureDetector(context, this)
        /**
         * Executes onresumeview operation with thermal imaging domain optimization.
         *
         */
        onResumeView()
    }

    /**
     * Executes ondetachedfromwindow operation with thermal imaging domain optimization.
     *
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isPreviewing = false
        mCameraDevice?.close()
        mCameraHandler?.removeCallbacksAndMessages(null)
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

    private lateinit var lis: ScaleGestureDetector

    @SuppressLint("ClickableViewAccessibility")
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
        if (isScale && event.action != MotionEvent.ACTION_UP) {
            return lis.onTouchEvent(event)
        }
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scaleW = binding.cameraTexture.width * (scale - 1) / 2f
                scaleH = binding.cameraTexture.height * (scale - 1) / 2f
                startX = event.x - binding.cameraTexture.x
                startY = event.y - binding.cameraTexture.y
                val view: View = binding.cameraTexture.parent as View
                parentViewW = view.width.toFloat()
                parentViewH = view.height.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                
                moveX = event.x - startX
                moveY = event.y - startY
                // 根据移动情况，不Visible时候close
// If (moveX-scaleW < -mTextureView.width ||
// MoveX+scaleW > parentViewW ||
// MoveY - scaleH < -mTextureView.height ||
// MoveY + scaleH > parentViewH){
// CameraPreViewCloseListener?.invoke()
//                }

                
// If (moveX - scaleW < 0f) moveX = 0f + scaleW
// If (moveY - scaleH < 0f) moveY = 0f + scaleH
// If (moveX + scaleW > parentViewW - mTextureView.width) {
// MoveX = parentViewW - mTextureView.width - scaleW
//                }
// If (moveY + scaleH > parentViewH - mTextureView.height) {
// MoveY = parentViewH - mTextureView.height - scaleH
//                }
//                Log.e("Test---","/"+(moveX + scaleW)+"// /"+(parentViewW - mTextureView.width))
                binding.cameraTexture.x = moveX
                binding.cameraTexture.y = moveY
            }
            MotionEvent.ACTION_UP -> {
                isScale = false 
                val startX = viewX
                val startY = viewY
//                Log.e("Test","/"+(startX)+"// /"+startY+"// /"+(mTextureView.width)+"// "+mTextureView.width * scale)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if ((viewX < 0 && startX < -binding.cameraTexture.width * scale + SizeUtils.dp2px(10f)) ||
                    (startX > 0 && startX > parentViewW - SizeUtils.dp2px(10f)) ||
                    (startY < 0 && startY < -binding.cameraTexture.height * scale + SizeUtils.dp2px(10f)) ||
                    (startY > 0 && startY > parentViewH - SizeUtils.dp2px(10f))
                ) {
                    cameraPreViewCloseListener?.invoke()
                }
            }
        }
        return lis.onTouchEvent(event)
    }

    /**
     * savedimage
     */
    /**
     * Retrieves the bitmap with optimized performance for thermal imaging operations.
     *
     */
    public fun getBitmap(): Bitmap? {
        return binding.cameraTexture.bitmap
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
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (scaleFactor < 0) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (scale > 0.1) {
                    scale += scaleFactor
                    binding.cameraTexture.scaleX = scale
                    binding.cameraTexture.scaleY = scale
                }
            } else {
                scale += scaleFactor
                binding.cameraTexture.scaleX = scale
                binding.cameraTexture.scaleY = scale
            }
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

    /**
     * Callback method triggered when resume occurs.
     */
    fun onResume() {
        // Processingswitch后台，Open系统camera后，回到app导致预览不update画area的问题
        if (mCameraDevice != null) {
            mCameraDevice?.close()
            /**
             * Manages thermal camera operations with hardware-optimized performance and error handling.
             *
             */
            openCamera()
        }
    }

// // /// /// /// /// //

    /**cameraPermission请求标识 */
    private val REQUEST_CAMERA_CODE = 0x100

    /**image */
    private var mImageView: ImageView? = null

    /**照cameraID，标识前置后置 */
    private lateinit var mCameraId: String

    /**camera尺寸 */
    private var mCaptureSize: Size? = null

    /**image读取者 */
    private var mImageReader: ImageReader? = null

    /**image主line程Handler */
    private var mCameraHandler: Handler? = null

    /**cameradevice */
    private var mCameraDevice: CameraDevice? = null

    /**预览大小 */
    private var mPreviewSize: Size? = null

    /**camera请求 */
    private lateinit var mCaptureBuilder: CaptureRequest.Builder

    /**cameracapture捕获会话 */
    private var mCameraCaptureSession: CameraCaptureSession? = null

    /**camera管理者 */
    private var mCameraManager: CameraManager? = null

    /**cameradevicestateCallback */
    private val mStateCallback: CameraDevice.StateCallback =
        object : CameraDevice.StateCallback() {
            /**
             * Executes onopened operation with thermal imaging domain optimization.
             *
             * @param
             * @param camera Camera configuration or reference (type: CameraDevice)
             *
             */
            override fun onOpened(
                @NonNull camera: CameraDevice,
            ) {
                
                XLog.i("开启预览")
                mCameraDevice = camera
                /**
                 * Executes takepreview operation with thermal imaging domain optimization.
                 *
                 */
                takePreview()
            }

            /**
             * Executes ondisconnected operation with thermal imaging domain optimization.
             *
             * @param
             * @param camera Camera configuration or reference (type: CameraDevice)
             *
             */
            override fun onDisconnected(
                @NonNull camera: CameraDevice,
            ) {
                
                XLog.i("close预览")
                isPreviewing = false
// Camera.close()
// MCameraDevice = null
            }

            /**
             * Executes onerror operation with thermal imaging domain optimization.
             *
             * @param
             * @param camera Camera configuration or reference (type: CameraDevice)
             * @param error Parameter for operation (type: Int)
             *
             */
            override fun onError(
                @NonNull camera: CameraDevice,
                error: Int,
            ) {
                
                isPreviewing = false
                camera.close()
                mCameraDevice = null
                XLog.e("预览exception error: $error")
            }
        }

    /**
     * Sets rotation configuration.
     */
    fun setRotation(isReverse: Boolean) {
        this.isReverse = isReverse
        updateRotation()
    }

    /**
     * Updates the rotation with new data.
     */
    private fun updateRotation() {
        if (isReverse) {
            binding.cameraTexture.rotation = 180f
        } else {
            binding.cameraTexture.rotation = 0f
        }
    }

    /**
     * 预览
     * click开启camera后触发
     */
    /**
     * Executes takePreview functionality.
     */
    /**
     * Executes takepreview operation with thermal imaging domain optimization.
     *
     */
    private fun takePreview() {
// MTextureView.rotation = 270f
// MTextureView.rotation = 0f
        /**
         * Executes updaterotation operation with thermal imaging domain optimization.
         *
         */
        updateRotation()
// Val layoutParams = mTextureView.layoutParams
// LayoutParams.width = cameraWidth / 2
// MTextureView.layoutParams = layoutParams
        val surfaceTexture = binding.cameraTexture.surfaceTexture
        
        surfaceTexture?.setDefaultBufferSize(mPreviewSize!!.width, mPreviewSize!!.height)
        
        val previewSurface = Surface(surfaceTexture)
        try {
            
            mCaptureBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            
            mCaptureBuilder.addTarget(previewSurface)
            
            @Suppress("DEPRECATION")
            mCameraDevice!!.createCaptureSession(
                /**
                 * Executes listof operation with thermal imaging domain optimization.
                 *
                 */
                listOf(previewSurface),
                object : CameraCaptureSession.StateCallback() {
                    /**
                     * Executes onconfigured operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param session Parameter for operation (type: CameraCaptureSession)
                     *
                     */
                    override fun onConfigured(
                        @NonNull session: CameraCaptureSession,
                    ) {
                        try {
                            
                            val captureRequest = mCaptureBuilder.build()
                            
                            mCameraCaptureSession = session
                            
                            mCameraCaptureSession?.setRepeatingRequest(
                                captureRequest,
                                null,
                                mCameraHandler,
                            )
                        } catch (e: CameraAccessException) {
                            XLog.e("cameraexception：${e.printStackTrace()}")
                        }
                    }

                    /**
                     * Executes onconfigurefailed operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param session Parameter for operation (type: CameraCaptureSession)
                     *
                     */
                    override fun onConfigureFailed(
                        @NonNull session: CameraCaptureSession,
                    ) {
                        
                        XLog.e("configurationfailed")
                    }
                },
                mCameraHandler,
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * Callback method triggered when resumeview occurs.
     */
    private fun onResumeView() {
        binding.cameraTexture.surfaceTextureListener =
            object : TextureView.SurfaceTextureListener {
                /**
                 * Executes onsurfacetextureavailable operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param surface Parameter for operation (type: SurfaceTexture)
                 * @param width Parameter for operation (type: Int)
                 * @param height Parameter for operation (type: Int)
                 *
                 */
                override fun onSurfaceTextureAvailable(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int,
                ) {
                    
                    XLog.w("width:$width, height:$height")
                    /**
                     * Configures the upcamera with validation and thermal imaging optimization.
                     *
                     */
                    setUpCamera(width, height)
                }

                /**
                 * Executes onsurfacetexturesizechanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param surface Parameter for operation (type: SurfaceTexture)
                 * @param width Parameter for operation (type: Int)
                 * @param height Parameter for operation (type: Int)
                 *
                 */
                override fun onSurfaceTextureSizeChanged(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int,
                ) {
                    
                }

                /**
                 * Executes onsurfacetexturedestroyed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param surface Parameter for operation (type: SurfaceTexture)
                 *
                 */
                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    // SurfaceTexture destroy
                    return false
                }

                /**
                 * Executes onsurfacetextureupdated operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param surface Parameter for operation (type: SurfaceTexture)
                 *
                 */
                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                    // SurfaceTexture update
                }
            }
    }

    /**
     * Executes onattachedtowindow operation with thermal imaging domain optimization.
     *
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    /**
     * settingscameraparameter
     * @param width 宽度
     * @param height 高度
     */
    /**
     * Sets upcamera configuration.
     */
    private fun setUpCamera(
        width: Int,
        height: Int,
    ) {
        
        mCameraHandler = Handler(Looper.getMainLooper())
        // Get/Retrieve摄像头的管理者
        mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // 遍历所有摄像头,找到aCancel遍历
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (cameraId in mCameraManager!!.cameraIdList) {
                XLog.i("camera id: $cameraId")
                cameraCharacteristics = mCameraManager!!.getCameraCharacteristics(cameraId)
                // Get/Retrieve摄像头是前置还是后置
                val facing = cameraCharacteristics?.get(CameraCharacteristics.LENS_FACING)
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) continue
                // Get/RetrieveStreamConfigurationMap，管理摄像头支持的所有输出format和尺寸
                val map = cameraCharacteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                
                val mapList = map.getOutputSizes(SurfaceTexture::class.java)

                mPreviewSize = getOptimalSize(mapList, width, height)
                val constraintSet = ConstraintSet()
                constraintSet.clone(binding.cameraLayRoot)
                constraintSet.constrainHeight(binding.cameraTexture.id, width * mPreviewSize!!.width / mPreviewSize!!.height)
                constraintSet.applyTo(binding.cameraLayRoot)
                XLog.w("mPreviewSize:$mPreviewSize")
                // Get/Retrievecamera支持的最大capture尺寸
                val sizes = map.getOutputSizes(ImageFormat.JPEG)
                XLog.w("size:${sizes.toList()}")
                val w = 1000
                val h = w * sizes[0].height / sizes[0].width
// MCaptureSize = Size(w, h)
                XLog.w("选取比例 w:${sizes[0].width}, h:${sizes[0].height}")
                XLog.w("Adjust后 w: $w, h:$h")
                
// SetupImageReader()
                
                mCameraId = cameraId
                break
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Log.e("123", "settingscameraparameter:${e.message}")
        }
    }

    /**
     * selectionSizeMap中大于并且最接近width和height的size
     * @param sizeMap 可选的尺寸
     * @param width 宽
     * @param height 高
     * @return 最接近width和height的size
     */
    /**
     * Retrieves optimalsize information.
     */
    private fun getOptimalSize(
        sizeMap: Array<Size>,
        width: Int,
        height: Int,
    ): Size {
        
        val sizeList: MutableList<Size> = ArrayList()
        
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (option in sizeMap) {
            
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (width > height) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (option.width > width && option.height > height) {
                    sizeList.add(option)
                }
            } else {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (option.width > height && option.height > width) {
                    sizeList.add(option)
                }
            }
        }
        
        return if (sizeList.size > 0) {
            Collections.min(sizeList) { lhs, rhs ->
                java.lang.Long.signum((lhs.width * lhs.height - rhs.width * rhs.height).toLong())
            }
        } else {
            sizeMap[0]
        }
    }

    /**
     * Opencamera
     */
    @SuppressLint("MissingPermission")
    /**
     * Executes opencamera functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    fun openCamera() {
        isPreviewing = true
        try {
            mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            mCameraManager!!.openCamera(mCameraId, mStateCallback, mCameraHandler)
        } catch (e: Exception) {
            isPreviewing = false
            XLog.e("Opencamerafailed:${e.message}")
            ToastUtils.showShort("Opencamerafailed")
        }
    }

    /**
     * closecamera
     */
    @SuppressLint("MissingPermission")
    /**
     * Executes closecamera functionality.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    fun closeCamera() {
        isPreviewing = false
        try {
            mCameraDevice?.close()
            
            binding.cameraTexture.x = 0f
            binding.cameraTexture.y = 0f
            binding.cameraTexture.scaleX = 1f
            binding.cameraTexture.scaleY = 1f
            scale = 1f
// IsReverse = false
        } catch (e: Exception) {
            XLog.e("closecamerafailed:${e.message}")
            ToastUtils.showShort("closecamerafailed")
        }
    }

    override val viewX: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = binding.cameraTexture.x - (viewWidth - binding.cameraTexture.width) / 2
    override val viewY: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = binding.cameraTexture.y - (viewHeight - binding.cameraTexture.height) / 2
    override val viewAlpha: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = binding.cameraTexture.alpha
    override val viewWidth: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = binding.cameraTexture.width * scale
    override val viewHeight: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = binding.cameraTexture.height * scale
    override val viewScale: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = scale

    /**
     * Sets cameraalpha configuration.
     */
    fun setCameraAlpha(alpha: Float) {
        binding.cameraTexture.alpha = 1 - alpha
    }

    /**
     * Sets zoom configuration.
     */
    /**
     * Configures the zoom with validation and thermal imaging optimization.
     *
     * @param
     * @param zoomLeve Parameter for operation (type: Int)
     *
     */
    fun setZoom(zoomLeve: Int) {
        scale = zoomLeve * 0.5f
        binding.cameraTexture.scaleX = scale
        binding.cameraTexture.scaleY = scale
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
    }
}
