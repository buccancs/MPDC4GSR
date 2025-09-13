package com.infisense.usbdual.camera;

import android.hardware.usb.UsbDevice;
import android.os.SystemClock;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.energy.iruvc.ircmd.ConcreteIRCMDBuilder;
import com.energy.iruvc.ircmd.IRCMD;
import com.energy.iruvc.ircmd.IRCMDType;
import com.energy.iruvc.usb.USBMonitor;
import com.energy.iruvc.utils.CommonParams;
import com.energy.iruvc.utils.IFrameCallback;
import com.energy.iruvc.uvc.ConcreateUVCBuilder;
import com.energy.iruvc.uvc.UVCCamera;
import com.energy.iruvc.uvc.UVCType;
import com.infisense.usbdual.Const;
import com.infisense.usbdual.inf.OnUSBConnectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialized thermal imaging component providing USBMonitorDualManager functionality for the IRCamera system.
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
public class USBMonitorDualManager {

    public static final String TAG = "USBMonitorDualManager";
    private USBMonitor mUSBMonitor;

    private IRCMD mIrcmd;

    private Object mSyncs = new Object();

    private List<OnUSBConnectListener> mOnUSBConnectListeners = new ArrayList<>();

    private UVCCamera mIrUvcCamera;

    private UVCCamera mVlUvcCamera;

    private boolean mIrOpened;
    private boolean mVlOpened;

    private IFrameCallback mVlIFrameCallback;

    /**
     * Executes usbmonitordualmanager operation with thermal imaging domain optimization.
     *
     */
    private USBMonitorDualManager() {
    }

    private static USBMonitorDualManager mInstance;

    public static synchronized USBMonitorDualManager getInstance() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mInstance == null) {
            mInstance = new USBMonitorDualManager();
        }
        return mInstance;
    }

    public void addOnUSBConnectListener(OnUSBConnectListener onUSBConnectListener) {
        mOnUSBConnectListeners.add(onUSBConnectListener);
    }

    public void removeOnUSBConnectListener(OnUSBConnectListener onUSBConnectListener) {
        mOnUSBConnectListeners.remove(onUSBConnectListener);
    }

    public void init(int irPid, int irFPS, int irWidth, int irHeight, float irBandWith,
                     int vlPid, int vlFPS, int vlWidth, int vlHeight, float vlBandWith, IFrameCallback frameCallback) {
        this.mVlIFrameCallback = frameCallback;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUSBMonitor == null) {
            mUSBMonitor = new USBMonitor(Utils.getApp(),
                    new USBMonitor.OnDeviceConnectListener() {
                        // Called by checking usb device
                        // Do request device permission
                        @Override
                        public void onAttach(UsbDevice device) {
                            Log.w(TAG, "USBMonitor-onAttach-getProductId = " + device.getProductId());

                            mUSBMonitor.requestPermission(device);
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param onUSBConnectListener Event listener for callbacks (type: mOnUSBConnectListeners)
                             *
                             */
                            for (OnUSBConnectListener onUSBConnectListener : mOnUSBConnectListeners) {
                                onUSBConnectListener.onAttach(device);
                            }
                        }

                        @Override
                        public void onGranted(UsbDevice usbDevice, boolean granted) {
                            Log.d(TAG, "USBMonitor-onGranted");
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param onUSBConnectListener Event listener for callbacks (type: mOnUSBConnectListeners)
                             *
                             */
                            for (OnUSBConnectListener onUSBConnectListener : mOnUSBConnectListeners) {
                                onUSBConnectListener.onGranted(usbDevice, granted);
                            }
                        }

                        // Called by taking out usb device
                        // Do close camera
                        @Override
                        public void onDettach(UsbDevice device) {
                            Log.d(TAG, "USBMonitor-onDettach");
                            Const.isDeviceConnected = false;
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param onUSBConnectListener Event listener for callbacks (type: mOnUSBConnectListeners)
                             *
                             */
                            for (OnUSBConnectListener onUSBConnectListener : mOnUSBConnectListeners) {
                                onUSBConnectListener.onDettach(device);
                            }

                        }

                        // Called by connect to usb camera
                        // Do open camera,start previewing
                        @Override
                        public void onConnect(final UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock,
                                              boolean createNew) {
                            Log.w(TAG, "USBMonitor-onConnect createNew " + createNew);
                            Log.w(TAG, "USBMonitor-onConnect Pid " + device.getProductId());
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (!mIrOpened) {
                                /**
                                 * Manages thermal camera operations with hardware-optimized performance and error handling.
                                 *
                                 */
                                openIrUVCCamera(irPid, irWidth, irHeight, irFPS, irBandWith, device, ctrlBlock);
                            }
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (!mVlOpened) {
                                /**
                                 * Manages thermal camera operations with hardware-optimized performance and error handling.
                                 *
                                 */
                                openVlUVCCamera(vlPid, vlWidth, vlHeight, vlFPS, vlBandWith, device, ctrlBlock);
                            }

                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (mIrOpened && mVlOpened) {
                                /**
                                 * Executes for operation with thermal imaging domain optimization.
                                 *
                                 * @param
                                 * @param onUSBConnectListener Event listener for callbacks (type: mOnUSBConnectListeners)
                                 *
                                 */
                                for (OnUSBConnectListener onUSBConnectListener : mOnUSBConnectListeners) {
                                    onUSBConnectListener.onConnect(device, ctrlBlock, createNew);
                                }
                            }
                        }

                        // Called by disconnect to usb camera
                        // Do nothing
                        @Override
                        public void onDisconnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {
                            Log.w(TAG, "USBMonitor-onDisconnect");
                            Const.isDeviceConnected = false;
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param onUSBConnectListener Event listener for callbacks (type: mOnUSBConnectListeners)
                             *
                             */
                            for (OnUSBConnectListener onUSBConnectListener : mOnUSBConnectListeners) {
                                onUSBConnectListener.onDisconnect(device, ctrlBlock);
                            }
                        }

                        @Override
                        public void onCancel(UsbDevice device) {
                            Log.d(TAG, "USBMonitor-onCancel");
                            Const.isDeviceConnected = false;
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param onUSBConnectListener Event listener for callbacks (type: mOnUSBConnectListeners)
                             *
                             */
                            for (OnUSBConnectListener onUSBConnectListener : mOnUSBConnectListeners) {
                                onUSBConnectListener.onCancel(device);
                            }
                        }
                    });
        }

    }

    public void registerUSB() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUSBMonitor != null) {
            mUSBMonitor.register();
        }
    }

    public void unregisterUSB() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUSBMonitor != null) {
            mUSBMonitor.unregister();
        }
    }

    private void openIrUVCCamera(int pid, int irWidth, int irHeight, int irFps, float irBandWidth, UsbDevice device, USBMonitor.UsbControlBlock controlBlock) {
        Log.w(TAG, "USBMonitor-openIrUVCCamera 1 " + device.getProductId());
        /**
         * Executes synchronized operation with thermal imaging domain optimization.
         *
         */
        synchronized (mSyncs) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (device.getProductId() != pid) {
                return;
            }
            Log.w(TAG, "USBMonitor-openIrUVCCamera " + device.getProductId());

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mIrUvcCamera == null) {
                ConcreateUVCBuilder concreateUVCBuilder = new ConcreateUVCBuilder();
                mIrUvcCamera = concreateUVCBuilder
                        .setUVCType(UVCType.USB_UVC)
                        .build();
                mIrUvcCamera.setDefaultBandwidth(irBandWidth);
                mIrUvcCamera.setDefaultPreviewMaxFps(irFps);
                // Uvc开启
                mIrUvcCamera.openUVCCamera(controlBlock);
                /**
                 * Adjust带宽
                 * 部分分辨率或在部分机型上，会出现无法出图，或出图一段时间后卡顿的问题，需要configuration对应的带宽
                 */

                initIRCMD();
                mIrUvcCamera.setUSBPreviewSize(irWidth, irHeight);
                mIrUvcCamera.onStartPreview();
                mIrOpened = true;
                Log.w(TAG, "USBMonitor-openIrUVCCamera complete" + device.getProductId());

            }
        }

    }

    public void initIRCMD() {

        // IRCMD init
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mIrUvcCamera != null) {
            ConcreteIRCMDBuilder concreteIRCMDBuilder = new ConcreteIRCMDBuilder();
            mIrcmd = concreteIRCMDBuilder
                    .setIrcmdType(IRCMDType.USB_IR_256_384)
                    .setIdCamera(mIrUvcCamera.getNativePtr())
                    .build();
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param onUSBConnectListener Event listener for callbacks (type: mOnUSBConnectListeners)
             *
             */
            for (OnUSBConnectListener onUSBConnectListener : mOnUSBConnectListeners) {
                onUSBConnectListener.onIRCMDInit(mIrcmd);
            }
        }
    }

    private void openVlUVCCamera(int pid, int vlWidth, int vlHeight, int vlFps, float vlBandWidth, UsbDevice device, USBMonitor.UsbControlBlock controlBlock) {
        /**
         * Executes synchronized operation with thermal imaging domain optimization.
         *
         */
        synchronized (mSyncs) {
            Log.w(TAG, "USBMonitor-openVlUVCCamera 1" + device.getProductId());

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (device.getProductId() != pid) {
                return;
            }
            Log.w(TAG, "USBMonitor-openVlUVCCamera " + device.getProductId());

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mVlUvcCamera == null) {
                ConcreateUVCBuilder concreateUVCBuilder = new ConcreateUVCBuilder();
                mVlUvcCamera = concreateUVCBuilder
                        .setUVCType(UVCType.USB_UVC)
                        .build();

                mVlUvcCamera.setDefaultPreviewMode(CommonParams.FRAMEFORMATType.FRAME_FORMAT_MJPEG);
                mVlUvcCamera.setDefaultBandwidth(vlBandWidth);
                mVlUvcCamera.setDefaultPreviewMaxFps(vlFps);
                // Uvc开启
                mVlUvcCamera.openUVCCamera(controlBlock);
                /**
                 * Adjust带宽
                 * 部分分辨率或在部分机型上，会出现无法出图，或出图一段时间后卡顿的问题，需要configuration对应的带宽
                 */

                mVlUvcCamera.setUSBPreviewSize(vlWidth, vlHeight);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mVlIFrameCallback != null) {
                    mVlUvcCamera.setFrameCallback(mVlIFrameCallback);
                }
                mVlUvcCamera.onStartPreview();
                mVlOpened = true;
                Log.w(TAG, "USBMonitor-openVlUVCCamera complete" + device.getProductId());
            }
        }
    }

    public void stopIrUVCCamera() {
        Log.i(TAG, "stopIrUVCCamera");
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mIrUvcCamera != null) {
            mIrUvcCamera.onStopPreview();
            SystemClock.sleep(200);
            mIrUvcCamera.onDestroyPreview();
            mIrUvcCamera = null;
            mIrOpened = false;
        }
    }

    public void stopVlUVCCamera() {
        Log.i(TAG, "stopVlUVCCamera");
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mVlUvcCamera != null) {
            mVlUvcCamera.onStopPreview();
            SystemClock.sleep(200);
            mVlUvcCamera.onDestroyPreview();
            mVlUvcCamera = null;
            mVlOpened = false;
        }
    }

    public IRCMD getIrcmd() {
        return mIrcmd;
    }

    public UVCCamera getIrUvcCamera() {
        return mIrUvcCamera;
    }

    public UVCCamera getVlUvcCamera() {
        return mVlUvcCamera;
    }

    public void onRelease() {
        mVlIFrameCallback = null;
        mUSBMonitor = null;
        mIrcmd = null;
        mIrUvcCamera = null;
        mVlUvcCamera = null;
        mVlOpened = false;
        mIrOpened = false;
    }
}
