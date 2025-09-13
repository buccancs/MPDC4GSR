package com.example.thermal_lite.camera.task;

import android.util.Log;

import com.example.thermal_lite.camera.CameraPreviewManager;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ResumePreviewTask display and interaction.
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
public class ResumePreviewTask extends BaseTask {
    /**
     * Executes resumepreviewtask operation with thermal imaging domain optimization.
     *
     */
    public ResumePreviewTask(DeviceState deviceState) {
        this.mDeviceState = deviceState;
    }

    @Override
    public void run() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDeviceState != DeviceState.RESUMED) {
            Log.d(TAG, "resumePreview start");
            CameraPreviewManager.getInstance().resumePreview();
            mDeviceState = DeviceState.RESUMED;
        }
    }
}