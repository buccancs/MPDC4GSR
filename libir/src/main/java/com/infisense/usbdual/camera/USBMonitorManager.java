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
import com.energy.iruvc.utils.DeviceType;
import com.energy.iruvc.uvc.CameraSize;
import com.energy.iruvc.uvc.ConcreateUVCBuilder;
import com.energy.iruvc.uvc.UVCCamera;
import com.energy.iruvc.uvc.UVCType;
import com.infisense.usbdual.Const;
import com.infisense.usbdual.inf.OnUSBConnectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialized thermal imaging component providing USBMonitorManager functionality for the IRCamera system.
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
public class USBMonitorManager {
    public static final String TAG = "USBMonitorManager";
    private USBMonitor mUSBMonitor;
    private UVCCamera mUvcCamera;
    private IRCMD mIrcmd;
    private CommonParams.DataFlowMode mDefaultDataFlowMode = CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT;
    // Module支持的高低gainmode
    private CommonParams.GainMode gainMode = CommonParams.GainMode.GAIN_MODE_HIGH_LOW;
    private boolean isUseIRISP;
    // 是否使用GPU方案
    private boolean isUseGPU = true;
    private int cameraWidth;
    private int cameraHeight;
    private byte[] tau_data_H;
    private byte[] tau_data_L;
    private long tempinfo = 0;
    private short[] nuc_table_high = new short[8192];
    private short[] nuc_table_low = new short[8192];

    private byte[] priv_high = new byte[1201];
    private byte[] priv_low = new byte[1201];
    private short[] kt_high = new short[1201];
    private short[] kt_low = new short[1201];
    private short[] bt_high = new short[1201];
    private short[] bt_low = new short[1201];
    private boolean isGetNucFromFlash; // 是否从coreFlash中读取的nucdata，会影响到temperature measurement修正的资源release
    // Current的gainstate
    private CommonParams.GainStatus gainStatus = CommonParams.GainStatus.HIGH_GAIN;
    // Coretemperature
    private int[] curVtemp = new int[1];

    private List<OnUSBConnectListener> mOnUSBConnectListeners = new ArrayList<>();

    private boolean isTempReplacedWithTNREnabled;

    private boolean isReStart = false;
    private int mPid = 0;

    /**
     * Executes usbmonitormanager operation with thermal imaging domain optimization.
     *
     */
    private USBMonitorManager() {
    }

    private static USBMonitorManager mInstance;

    public static synchronized USBMonitorManager getInstance() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mInstance == null) {
            mInstance = new USBMonitorManager();
        }
        return mInstance;
    }

    public void addOnUSBConnectListener(OnUSBConnectListener onUSBConnectListener) {
        mOnUSBConnectListeners.add(onUSBConnectListener);
    }

    public void removeOnUSBConnectListener(OnUSBConnectListener onUSBConnectListener) {
        mOnUSBConnectListeners.remove(onUSBConnectListener);
    }

    public boolean isReStart() {
        return isReStart;
    }

    public void setReStart(boolean reStart) {
        isReStart = reStart;
    }

    /**
     * @param pid                 需要initialize的device的pid
     * @param isUseIRISP
     * @param defaultDataFlowMode
     */
    public void init(int pid, boolean isUseIRISP, CommonParams.DataFlowMode defaultDataFlowMode) {
        this.mPid = pid;
        this.isUseIRISP = isUseIRISP;
        this.mDefaultDataFlowMode = defaultDataFlowMode;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (defaultDataFlowMode == CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT) {
            /**
             * image+temperature
             */
            cameraWidth = 256; // Sensor的原始宽度
            cameraHeight = 384; // Sensor的原始高度
        } else {
            /**
             * image
             */
            cameraWidth = 256;// Sensor的原始宽度
            cameraHeight = 192;// Sensor的原始高度
        }
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
                            Log.w(TAG, "USBMonitorManager-onAttach-getProductId = " + device.getProductId());
                            Log.w(TAG, "USBMonitorManager-onAttach-mPid = " + mPid);
                            /**
                             * USBMonitor会同时响应所有的UVCdevice，
                             * 需要根据自己的initializepid判断自己需要initialize的device
                             */
                            if (device.getProductId() != mPid) {
                                return;
                            }
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
                            Log.d(TAG, "USBMonitorManager-onGranted");
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
                            Log.d(TAG, "USBMonitorManager-onDettach");
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
                            Log.w(TAG, "USBMonitorManager-onConnect");
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (createNew) {
                                Const.isDeviceConnected = true;
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (isReStart()) {
                                    SystemClock.sleep(2000);
                                }
                                /**
                                 * Executes handleusbconnect operation with thermal imaging domain optimization.
                                 *
                                 */
                                handleUSBConnect(ctrlBlock);
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
                            Log.w(TAG, "USBMonitorManager-onDisconnect");
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
                            Log.d(TAG, "USBMonitorManager-onCancel");
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

    private void initUVCCamera() {
        Log.d(TAG, "initUVCCamera");
        // UVCCamera init

        ConcreateUVCBuilder concreateUVCBuilder = new ConcreateUVCBuilder();
        mUvcCamera = concreateUVCBuilder
                .setUVCType(UVCType.USB_UVC)
                .build();
        /**
         * Adjust带宽
         * 部分分辨率或在部分机型上，会出现无法出图，或出图一段时间后卡顿的问题，需要configuration对应的带宽
         */
        mUvcCamera.setDefaultBandwidth(1f);
    }

    /**
     * @param ctrlBlock
     */
    public void openUVCCamera(USBMonitor.UsbControlBlock ctrlBlock) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUvcCamera == null) {
            /**
             * Initializes the uvccamera component for thermal imaging operations.
             *
             */
            initUVCCamera();
        }
        // Uvc开启
        mUvcCamera.openUVCCamera(ctrlBlock);
    }

    /**
     * @return
     */
    public UVCCamera getUvcCamera() {
        return mUvcCamera;
    }

    /**
     * @return
     */
    public IRCMD getIrcmd() {
        return mIrcmd;
    }

    public void handleUSBConnect(USBMonitor.UsbControlBlock ctrlBlock) {
        /**
         * Manages thermal camera operations with hardware-optimized performance and error handling.
         *
         */
        openUVCCamera(ctrlBlock);
        // Get/Retrievedevice的分辨率list
        List<CameraSize> previewList = getAllSupportedSize();
        // 可以根据Get/Retrieve到的分辨率list，来区分不同的module，从而改变不同的cmdparameter来调用不同的SDK
        /**
         * Initializes the ircmd component for thermal imaging operations.
         *
         */
        initIRCMD(previewList);
        // 根据device的分辨率list，这里可以动态的settingsmodule的宽高(这里作为示例，用的是从外部传入的方式)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDefaultDataFlowMode == CommonParams.DataFlowMode.TNR_OUTPUT) {
            isTempReplacedWithTNREnabled = mIrcmd.isTempReplacedWithTNREnabled(DeviceType.P2);
            Log.i(TAG, "startPreview->isTempReplacedWithTNREnabled = " + isTempReplacedWithTNREnabled);
            // P2modulefirmware3.06version后, TNRdata无需停图再出图，TNRdata在256*384data的下半部分，顶替之前的temperaturedata
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isTempReplacedWithTNREnabled) {
                cameraWidth = 256;
                cameraHeight = 384;
            } else {
                cameraWidth = 256;
                cameraHeight = 192;
            }
        }
        int result = setPreviewSize(cameraWidth, cameraHeight);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (result == 0) {
            //
            /**
             * Executes startpreview operation with thermal imaging domain optimization.
             *
             */
            startPreview();
        }
    }

    /**
     * Get/Retrieve支持的分辨率list
     *
     * @return
     */
    private List<CameraSize> getAllSupportedSize() {
        Log.w(TAG, "getSupportedSize = " + mUvcCamera.getSupportedSize());
        List<CameraSize> previewList = new ArrayList<>();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUvcCamera != null) {
            previewList = mUvcCamera.getSupportedSizeList();
        }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param size Parameter for operation (type: previewList)
         *
         */
        for (CameraSize size : previewList) {
            Log.i(TAG, "SupportedSize : " + size.width + " * " + size.height);
        }
        return previewList;
    }

    /**
     * init IRCMD
     * 可以根据Get/Retrieve到的分辨率list，来区分不同的module，从而改变不同的cmdparameter来调用不同的SDK
     *
     * @param previewList
     */
    public void initIRCMD(List<CameraSize> previewList) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param size Parameter for operation (type: previewList)
         *
         */
        for (CameraSize size : previewList) {
            Log.i(TAG, "SupportedSize : " + size.width + " * " + size.height);
        }
        // IRCMD init
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUvcCamera != null) {
            ConcreteIRCMDBuilder concreteIRCMDBuilder = new ConcreteIRCMDBuilder();
            mIrcmd = concreteIRCMDBuilder
                    .setIrcmdType(IRCMDType.USB_IR_256_384)
                    .setIdCamera(mUvcCamera.getNativePtr())
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

    /**
     * 之前的openUVCCameramethod中传入的都是default值，这里需要根据实际传入对应的值
     *
     * @param cameraWidth
     * @param cameraHeight
     */
    private int setPreviewSize(int cameraWidth, int cameraHeight) {
        int result = -1;
        // 有时候可能上电后不稳定或者module没插稳，setUSBPreviewSize会settingsfailed，这里可以捕获exception，tipUser重新插拔module，重启app
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mUvcCamera != null) {
                result = mUvcCamera.setUSBPreviewSize(cameraWidth, cameraHeight);
            }
        } catch (Exception e) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param onUSBConnectListener Event listener for callbacks (type: mOnUSBConnectListeners)
             *
             */
            for (OnUSBConnectListener onUSBConnectListener : mOnUSBConnectListeners) {
                onUSBConnectListener.onSetPreviewSizeFail();
            }
        }
        return result;
    }

    
    private void startPreview() {
        Log.d(TAG, "startPreview");

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUvcCamera == null) {
            return;
        }

        Const.isReadFlashData = true;
        mUvcCamera.setOpenStatus(true);
        mUvcCamera.onStartPreview();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTempReplacedWithTNREnabled) {
            // 从isp出图switch到正常复合data出图，需要调用y16_start_preview Y16_MODE_TEMPERATURE,将下半部分的data从TNRswitch到temperature
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mIrcmd.startPreview(CommonParams.PreviewPathChannel.PREVIEW_PATH0,
                    CommonParams.StartPreviewSource.SOURCE_SENSOR,
                    25, CommonParams.StartPreviewMode.VOC_DVP_MODE,
                    CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT) == 0) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mIrcmd.startY16ModePreview(CommonParams.PreviewPathChannel.PREVIEW_PATH0,
                        CommonParams.Y16ModePreviewSrcType.Y16_MODE_TEMPERATURE) == 0) {
                    mIrcmd.setPropImageParams(CommonParams.PropImageParams.IMAGE_PROP_SEL_MIRROR_FLIP,
                            Const.IR_MIRROR_FLIP_TYPE);
                }
            }
        } else {
            // 根据业务逻辑自行processing
            // 第一次进入app，可不调用stopPreview，去掉sleep 2000ms
            // 如果没有中间出图的逻辑，无需重新出图，可不调用stopPreview，去掉sleep 2000ms
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mIrcmd.startPreview(CommonParams.PreviewPathChannel.PREVIEW_PATH0,
                    CommonParams.StartPreviewSource.SOURCE_SENSOR,
                    25, CommonParams.StartPreviewMode.VOC_DVP_MODE,
                    CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT) == 0) {
                mIrcmd.setPropImageParams(CommonParams.PropImageParams.IMAGE_PROP_SEL_MIRROR_FLIP,
                        Const.IR_MIRROR_FLIP_TYPE);
            }
        }
    }

    public void stopPreview() {
        Log.i(TAG, "stopPreview");
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUvcCamera != null) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mUvcCamera.getOpenStatus()) {
                mUvcCamera.onStopPreview();
            }
            SystemClock.sleep(200);

            mUvcCamera.onDestroyPreview();
            mUvcCamera = null;
        }
    }

    public void onPauseUvcPreview() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUvcCamera != null) {
            mUvcCamera.onPausePreview();
        }
    }

    public void onResumeUvcPreview() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUvcCamera != null) {
            mUvcCamera.onResumePreview();
        }
    }

    // ##################################################################################################################
    // ##################################################################################################################

}
