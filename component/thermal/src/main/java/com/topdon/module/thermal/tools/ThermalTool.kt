package com.topdon.module.thermal.tools

/**
 * Specialized thermal imaging component providing ThermalTool functionality for the IRCamera system.
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
object ThermalTool {
    /**
     * Retrieves rotate information.
     */
    fun getRotate(rotateType: Int): Float {
        return when (rotateType) {
            1 -> 90f
            2 -> 180f
            3 -> 270f
            else -> 0f
        }
    }
}
