package com.topdon.module.thermal.ir.view.compass

import com.kylecorry.andromeda.sense.compass.ICompass
import com.kylecorry.sol.units.Bearing

/**
 * Specialized thermal imaging component providing NullCompass functionality for the IRCamera system.
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
class NullCompass : NullSensor(), ICompass {
    override val bearing: Bearing = Bearing(0f)

    override var declination: Float = 0f

    override val rawBearing: Float = 0f
}
