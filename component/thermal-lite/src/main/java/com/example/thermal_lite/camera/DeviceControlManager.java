package com.example.thermal_lite.camera;

import android.util.Log;

import com.energy.iruvccamera.usb.USBMonitor;
import com.example.thermal_lite.camera.task.DeviceControlWorker;
import com.example.thermal_lite.camera.task.IDeviceConnectListener;
import com.example.thermal_lite.camera.task.PausePreviewTask;
import com.example.thermal_lite.camera.task.ResumePreviewTask;
import com.example.thermal_lite.camera.task.StartPreviewTask;
import com.example.thermal_lite.camera.task.StopPreviewTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Specialized thermal imaging component providing DeviceControlManager functionality for the IRCamera system.
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
public class DeviceControlManager implements IDeviceConnectListener {

    private static final String TAG = "DualDeviceControlManager";

    private DeviceControlWorker mDeviceControlWorker;

    private HashMap<String, IDeviceConnectListener> mIDeviceConnectListeners;

    /**
     * Executes devicecontrolmanager operation with thermal imaging domain optimization.
     *
     */
    private DeviceControlManager() {

    }

    private static DeviceControlManager mInstance;

    public static synchronized DeviceControlManager getInstance() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mInstance == null) {
            mInstance = new DeviceControlManager();
        }
        return mInstance;
    }

    /**
initialize
     */
    public void init() {
        mDeviceControlWorker = new DeviceControlWorker();
        mDeviceControlWorker.setDeviceControlCallback(this);
        mDeviceControlWorker.startWork();
        mIDeviceConnectListeners = new HashMap<>();
    }

    /**
RegisterdevicestateCallback，可在activity或fragment中Register，用于UI的改变
@param key 唯一标识
     * @param iDeviceConnectListener
     */
    public void addDeviceConnectListener(String key, IDeviceConnectListener iDeviceConnectListener) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mIDeviceConnectListeners != null) {
            mIDeviceConnectListeners.put(key, iDeviceConnectListener);
        }
    }

    /**
CancelRegisterdevicestateCallback
     * @param key
     */
    public void removeDeviceConnectListener(String key) {
        if (mIDeviceConnectListeners != null) {
            mIDeviceConnectListeners.remove(key);
        }
    }

    /**
recycle资源
     */
    public void release() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDeviceControlWorker != null) {
            mDeviceControlWorker.release();
            mDeviceControlWorker = null;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mIDeviceConnectListeners != null) {
            mIDeviceConnectListeners.clear();
            mIDeviceConnectListeners = null;
        }
    }

    /**
dual lightdata流出图
     * @param ctrlBlock
     */
    public void handleStartPreview(USBMonitor.UsbControlBlock ctrlBlock) {
        if (mDeviceControlWorker != null) {
            Log.d(TAG, "handleStartPreview");
            mDeviceControlWorker.addTask(new StartPreviewTask(ctrlBlock, mDeviceControlWorker.getDeviceState()));
        }
    }

    /**
dual lightdata流停图
     */
    public void handleStopPreview() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDeviceControlWorker != null) {
            Log.d(TAG, "handleStopPreview");
            mDeviceControlWorker.addTask(new StopPreviewTask(mDeviceControlWorker.getDeviceState()));
        }
    }

    /**
dual lightdata流pause
     */
    public void handlePauseDualPreview() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDeviceControlWorker != null) {
            Log.d(TAG, "handlePausePreview");
            mDeviceControlWorker.addTask(new PausePreviewTask(mDeviceControlWorker.getDeviceState()));
        }
    }

    /**
dual lightdata流Restore
     */
    public void handleResumeDualPreview() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDeviceControlWorker != null) {
            Log.d(TAG, "handleResumePreview");
            mDeviceControlWorker.addTask(new ResumePreviewTask(mDeviceControlWorker.getDeviceState()));
        }
    }

    @Override
    public void onPrepareConnect() {
StartPreview前Callback
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param entry Parameter for operation (type: mIDeviceConnectListeners.entrySet()
         *
         */
        for (Map.Entry<String, IDeviceConnectListener> entry: mIDeviceConnectListeners.entrySet()) {
            entry.getValue().onPrepareConnect();
        }
    }

    @Override
    public void onConnected() {
StartPreviewsuccessful前后Callback，注意是子line程
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param entry Parameter for operation (type: mIDeviceConnectListeners.entrySet()
         *
         */
        for (Map.Entry<String, IDeviceConnectListener> entry: mIDeviceConnectListeners.entrySet()) {
            entry.getValue().onConnected();
        }
    }

    @Override
    public void onDisconnected() {
StopPreviewsuccessful前后Callback，注意是子line程
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param entry Parameter for operation (type: mIDeviceConnectListeners.entrySet()
         *
         */
        for (Map.Entry<String, IDeviceConnectListener> entry: mIDeviceConnectListeners.entrySet()) {
            entry.getValue().onDisconnected();
        }
    }

    @Override
    public void onPaused() {
todo 自行定义Paused Task来implementation
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param entry Parameter for operation (type: mIDeviceConnectListeners.entrySet()
         *
         */
        for (Map.Entry<String, IDeviceConnectListener> entry: mIDeviceConnectListeners.entrySet()) {
            entry.getValue().onPaused();
        }
    }

    @Override
    public void onResumed() {
todo 自行定义Resumed Task来implementation
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param entry Parameter for operation (type: mIDeviceConnectListeners.entrySet()
         *
         */
        for (Map.Entry<String, IDeviceConnectListener> entry: mIDeviceConnectListeners.entrySet()) {
            entry.getValue().onResumed();
        }
    }
}
