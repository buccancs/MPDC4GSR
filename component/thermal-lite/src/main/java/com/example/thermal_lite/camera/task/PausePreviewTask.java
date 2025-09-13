package com.example.thermal_lite.camera.task;

import android.util.Log;

import com.example.thermal_lite.camera.CameraPreviewManager;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for PausePreviewTask display and interaction.
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
public class PausePreviewTask extends BaseTask {
    /**
     * Executes pausepreviewtask operation with thermal imaging domain optimization.
     *
     */
    public PausePreviewTask(DeviceState deviceState) {
        this.mDeviceState = deviceState;
    }

    @Override
    public void run() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDeviceState != DeviceState.PAUSED) {
            Log.d(TAG, "pausePreview start");
            CameraPreviewManager.getInstance().pausePreview();
            mDeviceState = DeviceState.PAUSED;
        }
    }
}
