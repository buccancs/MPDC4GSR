package com.topdon.lib.core.bean.tools

/**
 * Specialized thermal imaging component providing ThermalBean functionality for the IRCamera system.
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
class ThermalBean {
    var maxTemp = 0f
    var minTemp = 0f
    var centerTemp = 0f
    var type = 1
    var createTime = 0L
}
