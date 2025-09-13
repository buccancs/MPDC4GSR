package com.infisense.usbir.utils;

/**
 * @ProjectName: ANDROID_IRUVC_SDK
 * @Package: com.infisense.usbirmini640.utils
 * @ClassName: USBMonitorCallback
 * @Description:
 * @Author: brilliantzhao
 * @CreateDate: 3/16/2023 1:20 PM
 * @UpdateUser:
 * @UpdateDate: 3/16/2023 1:20 PM
 * @UpdateRemark:
 * @Version: 1.0.0
 */
/**
 * Specialized thermal imaging component providing USBMonitorCallback functionality for the IRCamera system.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
public interface USBMonitorCallback {

    void onAttach();

    void onGranted();

    void onConnect();

    void onDisconnect();

    void onDettach();

    void onCancel();

}
