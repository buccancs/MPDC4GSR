package com.shuyu.gsyvideoplayer.player

/**
 * Specialized thermal imaging component providing PlayerFactory functionality for the IRCamera system.
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
object PlayerFactory {
    const val SYSTEM_PLAYER = 0
    const val IJK_PLAYER = 1
    const val EXO_PLAYER = 2

    @JvmStatic
    /**
     * Sets playmanager configuration.
     */
    fun setPlayManager(playerType: Int) {
        // Stub implementation
    }

    @JvmStatic
    /**
     * Sets playmanager configuration.
     */
    fun setPlayManager(clazz: Class<*>) {
        // Stub implementation for Class parameter
    }
}
