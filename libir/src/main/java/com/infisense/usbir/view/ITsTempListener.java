package com.infisense.usbir.view;

/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with ITsTempListener algorithms.
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
public interface ITsTempListener {

    default float tempCorrectByTs(Float temp){
        return temp;
    }

}
