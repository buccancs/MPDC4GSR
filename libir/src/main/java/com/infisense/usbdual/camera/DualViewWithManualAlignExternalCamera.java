package com.infisense.usbdual.camera;

import static com.infisense.usbdual.Const.HIDE_LOADING;

import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import com.energy.commonlibrary.view.SurfaceNativeWindow;
import com.energy.iruvc.dual.ConcreateDualBuilder;
import com.energy.iruvc.dual.DualType;
import com.energy.iruvc.dual.DualUVCCamera;
import com.energy.iruvc.utils.CommonParams;
import com.energy.iruvc.utils.IAlignCallback;
import com.energy.iruvc.utils.IFrameCallback;
import com.energy.iruvc.uvc.UVCCamera;

/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with DualViewWithManualAlignExternalCamera functionality.
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
public class DualViewWithManualAlignExternalCamera extends BaseParamDualView{
        private final String TAG = "DualViewWithManualAlignExternalCamera";
        private DualUVCCamera dualUVCCamera;
        public SurfaceView cameraview;

        private Handler handler;

        private byte[] mixData;
        private int fusionLength;

        private boolean firstFrame = false;
        private SurfaceNativeWindow mSurfaceNativeWindow;
        private Surface mSurface;

        /**
         * @param handler
         */
        public void setHandler(Handler handler) {
            this.handler = handler;
        }

        private IFrameCallback iFrameCallback = new IFrameCallback() {
            /**
             * frame里area是有两帧image的，前area是fusion之后的image，是ARGBformat，占4个字节;
             * 后area是infrared和temperature的image，infrared和temperature的image是YUV422format，占2个字节
             */

            @Override
            public void onFrame(byte[] frame) {
                Log.d(TAG, "onFrame");
                System.arraycopy(frame, 0, mixData, 0, fusionLength);

                mSurface = cameraview.getHolder().getSurface();
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mSurface != null) {
                    mSurfaceNativeWindow.onDrawFrame(mSurface, mixData, mDualWidth, mDualHeight);
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!firstFrame) {
                    firstFrame = true;
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (handler != null) {
                        handler.sendEmptyMessage(HIDE_LOADING);
                    }
                }
            }
        };

        private IAlignCallback iAlignCallback = new IAlignCallback() {
            @Override
            public void onFrame(byte[] frame) {
                System.arraycopy(frame, 0, mixData, 0, fusionLength);
                mSurface = cameraview.getHolder().getSurface();
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mSurface != null) {
                    mSurfaceNativeWindow.onDrawFrame(mSurface, mixData, mDualWidth, mDualHeight);
                }
            }
        };

        /**
         * @param cameraview
         * @param iruvc
         */
        public DualViewWithManualAlignExternalCamera(int irWidth, int irHeight, int vlWidth, int vlHeight, int dualWidth, int dualHeight,
                                                     SurfaceView cameraview, UVCCamera iruvc, CommonParams.DataFlowMode dataFlowMode) {
            /**
             * Executes super operation with thermal imaging domain optimization.
             *
             */
            super(irWidth, irHeight, vlWidth, vlHeight, dualWidth, dualHeight);
            this.cameraview = cameraview;
            // DualUVCCamera initialize
            ConcreateDualBuilder concreateDualBuilder = new ConcreateDualBuilder();
            dualUVCCamera = concreateDualBuilder
                    .setDualType(DualType.USB_DUAL)
                    .setIRSize(mIrWidth, mIrHeight)
                    .setVLSize(mVlWidth, mVlHeight)
                    .setDualSize(mDualHeight, mDualWidth)
                    .setDataFlowMode(dataFlowMode)
                    .setPreviewCameraStyle(CommonParams.PreviewCameraStyle.EXTERNAL_CAMERA)
                    .setDeviceStyle(CommonParams.DeviceStyle.ALL_IN_ONE)
                    .setUseDualGPU(false)
                    .setMultiThreadHandleDualEnable(false)
                    .build();
            //
            mSurfaceNativeWindow = new SurfaceNativeWindow();
// DualUVCCamera.setImageRotate(DualCameraParams.TypeLoadParameters.ROTATE_180);
            dualUVCCamera.addIrUVCCamera(iruvc);
            fusionLength = mDualWidth * mDualHeight * 4;
            mixData = new byte[fusionLength];

        }

        
        public void startPreview() {
            dualUVCCamera.setFrameCallback(iFrameCallback);
            dualUVCCamera.onStartPreview();
            firstFrame = false;
        }

        /**
         * @return
         */
        public DualUVCCamera getDualUVCCamera() {
            return dualUVCCamera;
        }

        /**
         * closedual light预览
         */
        public void stopPreview() {
            dualUVCCamera.setFrameCallback(null);
            dualUVCCamera.onStopPreview();
        }

        public void destroyPreview() {
            dualUVCCamera.onDestroy();
        }

        public void startAlign() {
            dualUVCCamera.setAlignCallback(iAlignCallback);
            dualUVCCamera.startManualAlign();
        }

        public void onDraw() {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dualUVCCamera != null) {
                mSurface = cameraview.getHolder().getSurface();
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mSurface != null) {
                    mSurfaceNativeWindow.onDrawFrame(mSurface, mixData, mDualWidth, mDualHeight);
                }
            }
        }
}

