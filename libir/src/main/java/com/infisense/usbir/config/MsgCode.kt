package com.infisense.usbir.config

/**
 * Specialized thermal imaging component providing MsgCode functionality for the IRCamera system.
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
object MsgCode {
    const val RESTART_USB = 1000
    const val Y16_START_MSG = 1001
    const val YUV_STOP_MSG = 1002
    const val YUV_START_MSG = 1003
}
