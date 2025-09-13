package com.infisense.usbir.inf

/**
 * 热稳定interface
 * @author: CaiSongL
 * @date: 2024/1/10 11:40
 */
/**
 * ILiteListener manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing ILiteListener functionality for the IRCamera system.
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
interface ILiteListener {
    /**
     * Retrieves deltanucandvtemp information.
     */
    fun getDeltaNucAndVTemp(): Float

    /**
     * Executes compensatetemp functionality.
     */
    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @param
     * @param temp Temperature value in Celsius (type: Float)
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    fun compensateTemp(temp: Float): Float
}
