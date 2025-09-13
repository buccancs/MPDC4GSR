package com.topdon.lib.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.Image
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
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.NonNull
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.blankj.utilcode.util.ToastUtils
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.lib.ui.databinding.CameraLayBinding
import java.nio.ByteBuffer
import java.util.Collections
import kotlin.concurrent.thread

/**
 * Custom Camera view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
/**
 * CameraView implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraView functionality.
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
class CameraView : LinearLayout, ScaleGestureDetector.OnScaleGestureListener {
    /**预览 */
    lateinit var mTextureView: TextureView
    private lateinit var binding: CameraLayBinding

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
        mTextureView = binding.cameraTexture
        mTextureView.alpha = 0.4f
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
        mCameraDevice?.close()
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
        if (isScale && event.action != MotionEvent.ACTION_UP) {
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
                parentViewW = view.width.toFloat()
                parentViewH = view.height.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                
                moveX = event.x - startX
                moveY = event.y - startY
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (moveX - scaleW < 0f) moveX = 0f + scaleW
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (moveY - scaleH < 0f) moveY = 0f + scaleH
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (moveX + scaleW > parentViewW - mTextureView.width) {
                    moveX = parentViewW - mTextureView.width - scaleW
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (moveY + scaleH > parentViewH - mTextureView.height) {
                    moveY = parentViewH - mTextureView.height - scaleH
                }
                mTextureView.x = moveX
                mTextureView.y = moveY
            }
            MotionEvent.ACTION_UP -> {
                isScale = false 
            }
        }
        return lis.onTouchEvent(event)
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

// // /// /// /// /// //
    /**cameraPermission请求标识 */
    private val REQUEST_CAMERA_CODE = 0x100

    /**capturebutton */
    private var mBtnTake: Button? = null

    /**image */
    private var mImageView: ImageView? = null

    /**照cameraID，标识前置后置 */
    private lateinit var mCameraId: String

    /**camera尺寸 */
    private var mCaptureSize: Size? = null

    /**image读取者 */
    private lateinit var mImageReader: ImageReader

    /**image主line程Handler */
    private lateinit var mCameraHandler: Handler

    /**cameradevice */
    private var mCameraDevice: CameraDevice? = null

    /**预览大小 */
    private var mPreviewSize: Size? = null

    /**camera请求 */
    private lateinit var mCameraCaptureBuilder: CaptureRequest.Builder

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
                
                camera.close()
                mCameraDevice = null
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
                
                camera.close()
                mCameraDevice = null
            }
        }

    /**
     * 预览
     */
    /**
     * Executes takepreview operation with thermal imaging domain optimization.
     *
     */
    private fun takePreview() {
// MTextureView.rotation = 270f
        mTextureView.rotation = 0f
        // Get/RetrieveSurfaceTexture
        val surfaceTexture = mTextureView.surfaceTexture
        
        surfaceTexture!!.setDefaultBufferSize(mPreviewSize!!.width, mPreviewSize!!.height)
        
        val previewSurface = Surface(surfaceTexture)
        try {
            
            mCameraCaptureBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            
            mCameraCaptureBuilder.addTarget(previewSurface)
            
            @Suppress("DEPRECATION")
            mCameraDevice!!.createCaptureSession(
                /**
                 * Executes listof operation with thermal imaging domain optimization.
                 *
                 */
                listOf(
                    previewSurface,
                    mImageReader.surface,
                ),
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
                            
                            val captureRequest = mCameraCaptureBuilder.build()
                            
                            mCameraCaptureSession = session
                            
                            mCameraCaptureSession!!.setRepeatingRequest(
                                captureRequest,
                                null,
                                mCameraHandler,
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
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
        mTextureView.surfaceTextureListener =
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
                    
                    
                    Log.w("123", "width:$width, height:$height")
                    // W:h = 1 / 1.33
                    /**
                     * Configures the upcamera with validation and thermal imaging optimization.
                     *
                     */
                    setUpCamera(width, height)
// OpenCamera()
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
        // Get/Retrieve照camera管理者
        try {
            mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
            mCameraManager!!.openCamera(mCameraId, mStateCallback, mCameraHandler)
        } catch (e: Exception) {
            Log.e("123", "Opencamerafailed:${e.message}")
            ToastUtils.showShort("Opencamerafailed")
        }
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
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (cameraId in cameraManager.cameraIdList) {
                
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
                // Get/Retrieve摄像头是前置还是后置
                val facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
                
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (null != facing && CameraCharacteristics.LENS_FACING_FRONT == facing) continue
                // Get/RetrieveStreamConfigurationMap，管理摄像头支持的所有输出format和尺寸
                val map =
                    cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                
                mPreviewSize =
                    /**
                     * Retrieves the optimalsize with optimized performance for thermal imaging operations.
                     *
                     * @param
                     * @param SurfaceTexture Parameter for operation (type: :class.java)
                     *
                     */
                    getOptimalSize(
                        map.getOutputSizes(SurfaceTexture::class.java),
                        width,
                        height,
                    )
                // Get/Retrievecamera支持的最大capture尺寸
                val sizes = map.getOutputSizes(ImageFormat.JPEG)
                val w = 1000
                val h = w * sizes[0].height / sizes[0].width
                mCaptureSize = Size(w, h)
                Log.w("123", "w:${sizes[0].width}, h:${sizes[0].height}")
                Log.w("123", "Adjust后w:$w, h:$h")
// MCaptureSize = Size(1000, 1000)
// MCaptureSize =
//                    Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG))) { lhs, rhs ->
// Java.lang.Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth())
//                    }
                
                /**
                 * Configures the upimagereader with validation and thermal imaging optimization.
                 *
                 */
                setupImageReader()
                
                mCameraId = cameraId
                break
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
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
            Collections.min(
                sizeList,
            ) { lhs, rhs -> java.lang.Long.signum((lhs.width * lhs.height - rhs.width * rhs.height).toLong()) }
        } else {
            sizeMap[0]
        }
    }

    private var flag = 0

    /**
     * settingsImageReader
     */
    /**
     * Configures the upimagereader with validation and thermal imaging optimization.
     *
     */
    private fun setupImageReader() {
        // 2代表ImageReader中最多可以Get/Retrieve两帧image流
        mImageReader =
            ImageReader.newInstance(
                mCaptureSize!!.width,
                mCaptureSize!!.height,
                ImageFormat.JPEG,
                1,
            )
        
        mImageReader.setOnImageAvailableListener({ reader ->
            flag = 1
            // Get/Retrieveimage
            val image: Image = reader.acquireLatestImage()
            // 提交task，savedimage
            mCameraHandler.post(ImageSaver(image))
            
            runOnUiThread { // Get/Retrieve字节buffer区
                val buffer: ByteBuffer = image.planes[0].buffer
                // Createarray之前调用此method，restoredefaultsettings
                buffer.rewind()
                
                val bytes = ByteArray(buffer.remaining())
                // 从buffer区存入字节array,读取complete之后position在末尾
                buffer[bytes]
                // Get/RetrieveBitmapimage
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                // Show/Display
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (null != bitmap) {
                    val h = bitmap.height
                    val w = bitmap.width
                    mImageView?.let {
                        val sw = ScreenUtil.getScreenWidth(context)
                        it.layoutParams.height = sw / 2 * w / h
                        it.layoutParams.width = sw / 2
                        it.setImageBitmap(bitmap)
                    }
                }
                flag++
                thread {
                    /**
                     * Executes while operation with thermal imaging domain optimization.
                     *
                     */
                    while (flag < 3) {
// Delay(100)
                        Thread.sleep(100)
                    }
                    flag = 0
                    image.close()
                }
            }
        }, mCameraHandler)
    }

    /**
     * savedimagetask
     */
    private inner class ImageSaver(image: Image) : Runnable {
        /**image */
        private val mImage: Image = image

        /**
         * Executes run operation with thermal imaging domain optimization.
         *
         */
        override fun run() {
//            ImageSaverTool().save(mImage)
            flag++
        }
    }
}
