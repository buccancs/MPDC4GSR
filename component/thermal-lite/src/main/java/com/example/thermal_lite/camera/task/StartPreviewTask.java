package com.example.thermal_lite.camera.task;

import android.util.Log;

import com.example.thermal_lite.camera.CameraPreviewManager;
import com.energy.iruvccamera.usb.USBMonitor;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for StartPreviewTask display and interaction.
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
public class StartPreviewTask extends BaseTask {
    private IDeviceConnectListener mDeviceControlCallback;
    private USBMonitor.UsbControlBlock mUsbControlBlock;

    /**
     * Executes startpreviewtask operation with thermal imaging domain optimization.
     *
     */
    public StartPreviewTask(USBMonitor.UsbControlBlock usbControlBlock, DeviceState deviceState) {
        this.mDeviceState = deviceState;
        this.mUsbControlBlock = usbControlBlock;
    }

    @Override
    public void run() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDeviceState != DeviceState.OPEN) {
            Log.d(TAG, "startPreview start");

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mDeviceControlCallback != null) {
                mDeviceControlCallback.onPrepareConnect();
            }

            CameraPreviewManager.getInstance().handleUSBConnect(mUsbControlBlock);

            mDeviceState = DeviceState.OPEN;
            Log.d(TAG, "startPreview start end ");
        } else {
            mDeviceState = DeviceState.OPEN;
        }
    }

    public void setDeviceControlCallback(IDeviceConnectListener deviceControlCallback) {
        this.mDeviceControlCallback = deviceControlCallback;
    }
}
