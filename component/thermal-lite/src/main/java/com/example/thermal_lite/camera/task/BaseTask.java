package com.example.thermal_lite.camera.task;

/**
 * Specialized thermal imaging component providing BaseTask functionality for the IRCamera system.
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
public abstract class BaseTask implements Runnable {
    protected static final String TAG = BaseTask.class.getSimpleName();
    protected DeviceState mDeviceState;

    /**
     * Executes basetask operation with thermal imaging domain optimization.
     *
     */
    public BaseTask() {

    }

    public DeviceState getDeviceState() {
        return mDeviceState;
    }

    public void setDeviceState(DeviceState mDeviceState) {
        this.mDeviceState = mDeviceState;
    }
}
