package com.topdon.module.thermal.ir.frame

/**
 * Specialized thermal imaging component providing ImageParams functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
enum class ImageParams(val value: Int) {
    /**
     * Executes rotate 0 operation with thermal imaging domain optimization.
     *
     */
    ROTATE_0(0),
    /**
     * Executes rotate 90 operation with thermal imaging domain optimization.
     *
     */
    ROTATE_90(1),
    /**
     * Executes rotate 180 operation with thermal imaging domain optimization.
     *
     */
    ROTATE_180(2),
    /**
     * Executes rotate 270 operation with thermal imaging domain optimization.
     *
     */
    ROTATE_270(3),
}
