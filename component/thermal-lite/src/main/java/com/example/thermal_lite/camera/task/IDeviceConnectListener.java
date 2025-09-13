package com.example.thermal_lite.camera.task;

/**
 * Specialized thermal imaging component providing IDeviceConnectListener functionality for the IRCamera system.
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
public interface IDeviceConnectListener {
    void onPrepareConnect();
    void onConnected();
    void onDisconnected();
    void onPaused();
    void onResumed();
}
