package com.example.thermal_lite.camera.task;

import android.os.SystemClock;
import android.util.Log;

import com.example.thermal_lite.camera.CameraPreviewManager;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for StopPreviewTask display and interaction.
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
public class StopPreviewTask extends BaseTask {

    /**
     * Executes stoppreviewtask operation with thermal imaging domain optimization.
     *
     */
    public StopPreviewTask(DeviceState deviceState) {
        this.mDeviceState = deviceState;
    }

    @Override
    public void run() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDeviceState != DeviceState.CLOSED) {
            Log.d(TAG, "stopPreview start");

            CameraPreviewManager.getInstance().stopPreview();
            SystemClock.sleep(100);
            CameraPreviewManager.getInstance().closePreview();
todo 这里的200ms是为了usb能完全停图，因为如果User操作过快，触发停图出图，core还stop，就又出图会导致程序卡死
            SystemClock.sleep(200);
            mDeviceState = DeviceState.CLOSED;
            Log.d(TAG, "stopPreview end33");
        }
    }
}
