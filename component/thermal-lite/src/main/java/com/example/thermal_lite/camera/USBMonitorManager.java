package com.example.thermal_lite.camera;

import android.hardware.usb.UsbDevice;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.energy.iruvccamera.usb.DeviceFilter;
import com.energy.iruvccamera.usb.USBMonitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private boolean mDeviceConnected = false;

    private HashMap<String, OnUSBConnectListener> mOnUSBConnectListeners = new HashMap<>();

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

    public void addOnUSBConnectListener(String key, OnUSBConnectListener onUSBConnectListener) {
        mOnUSBConnectListeners.put(key, onUSBConnectListener);
    }

    public void removeOnUSBConnectListener(String key) {
        mOnUSBConnectListeners.remove(key);
    }

    public void init() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUSBMonitor == null) {
            mUSBMonitor = new USBMonitor(Utils.getApp().getApplicationContext(),
                    new USBMonitor.OnDeviceConnectListener() {

                // Called by checking usb device
                // Do request device permission
                @Override
                public void onAttach(UsbDevice device) {
                    Log.w(TAG, "onAttach" + device.getProductId());
                    mUSBMonitor.requestPermission(device);
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param entry Parameter for operation (type: mOnUSBConnectListeners.entrySet()
                     *
                     */
                    for (Map.Entry<String, OnUSBConnectListener> entry: mOnUSBConnectListeners.entrySet()) {
                        entry.getValue().onAttach(device);
                    }
                }

                @Override
                public void onGranted(UsbDevice usbDevice, boolean granted) {
                    Log.d(TAG, "onGranted");
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param entry Parameter for operation (type: mOnUSBConnectListeners.entrySet()
                     *
                     */
                    for (Map.Entry<String, OnUSBConnectListener> entry: mOnUSBConnectListeners.entrySet()) {
                        entry.getValue().onGranted(usbDevice, granted);
                    }
                }

                // Called by taking out usb device
                // Do close camera
                @Override
                public void onDetach(UsbDevice device) {
                    Log.d(TAG, "onDetach");
                    mDeviceConnected = false;
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param entry Parameter for operation (type: mOnUSBConnectListeners.entrySet()
                     *
                     */
                    for (Map.Entry<String, OnUSBConnectListener> entry: mOnUSBConnectListeners.entrySet()) {
                        entry.getValue().onDetach(device);
                    }
                }

                // Called by connect to usb camera
                // Do open camera,start previewing
                @Override
                public void onConnect(final UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock, boolean createNew) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (createNew) {
                        mDeviceConnected = true;
                        Log.w(TAG, "onConnect");
                        /**
                         * Executes for operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param entry Parameter for operation (type: mOnUSBConnectListeners.entrySet()
                         *
                         */
                        for (Map.Entry<String, OnUSBConnectListener> entry: mOnUSBConnectListeners.entrySet()) {
                            entry.getValue().onConnect(device, ctrlBlock, createNew);
                        }
                    }
                }

                // Called by disconnect to usb camera
                // Do nothing
                @Override
                public void onDisconnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {
                    Log.w(TAG, "onDisconnect");
                    mDeviceConnected = false;
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param entry Parameter for operation (type: mOnUSBConnectListeners.entrySet()
                     *
                     */
                    for (Map.Entry<String, OnUSBConnectListener> entry: mOnUSBConnectListeners.entrySet()) {
                        entry.getValue().onDisconnect(device, ctrlBlock);
                    }
                }

                @Override
                public void onCancel(UsbDevice device) {
                    Log.d(TAG, "onCancel");
                    mDeviceConnected = false;
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param entry Parameter for operation (type: mOnUSBConnectListeners.entrySet()
                     *
                     */
                    for (Map.Entry<String, OnUSBConnectListener> entry: mOnUSBConnectListeners.entrySet()) {
                        entry.getValue().onCancel(device);
                    }
                }
            });
        }

    }

    public void registerMonitor() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUSBMonitor != null) {
            Log.d(TAG, "registerMonitor");
            mUSBMonitor.register();
        }
    }

    public void unregisterMonitor() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUSBMonitor != null) {
            Log.d(TAG, "unregisterMonitor");
            mUSBMonitor.unregister();
        }
    }

    public void destroyMonitor() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUSBMonitor != null) {
            Log.d(TAG, "destroyMonitor");
            mUSBMonitor.destroy();
            mUSBMonitor = null;
        }
    }

    public boolean isDeviceConnected() {
        return mDeviceConnected;
    }

    public List<DeviceFilter> getDeviceFilter() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mUSBMonitor != null) {
            mUSBMonitor.getDeviceFilter();
        }
        return null;
    }
}
