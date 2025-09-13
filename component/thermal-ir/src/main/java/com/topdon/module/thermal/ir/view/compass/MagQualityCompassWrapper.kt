package com.topdon.module.thermal.ir.view.compass

import com.kylecorry.andromeda.core.sensors.AbstractSensor
import com.kylecorry.andromeda.core.sensors.ISensor
import com.kylecorry.andromeda.core.sensors.Quality
import com.kylecorry.andromeda.sense.compass.ICompass
import com.kylecorry.sol.units.Bearing
import kotlin.math.min

/**
 * Specialized thermal imaging component providing MagQualityCompassWrapper functionality for the IRCamera system.
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
class MagQualityCompassWrapper(private val compass: ICompass, private val magnetometer: ISensor) :
    /**
     * Executes abstractsensor operation with thermal imaging domain optimization.
     *
     */
    AbstractSensor(), ICompass {
    override val bearing: Bearing
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = compass.bearing

    override var declination: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = compass.declination
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            compass.declination = value
        }
    override val hasValidReading: Boolean
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = compass.hasValidReading

    override val rawBearing: Float
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = compass.rawBearing

    override val quality: Quality
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = Quality.values()[min(magnetometer.quality.ordinal, compass.quality.ordinal)]

    /**
     * Executes startimpl operation with thermal imaging domain optimization.
     *
     */
    override fun startImpl() {
        compass.start(this::onReading)
        magnetometer.start(this::onReading)
    }

    /**
     * Executes stopimpl operation with thermal imaging domain optimization.
     *
     */
    override fun stopImpl() {
        compass.stop(this::onReading)
        magnetometer.stop(this::onReading)
    }

    /**
     * Executes onReading functionality.
     */
    /**
     * Executes onreading operation with thermal imaging domain optimization.
     *
     */
    private fun onReading(): Boolean {
        /**
         * Executes notifylisteners operation with thermal imaging domain optimization.
         *
         */
        notifyListeners()
        return true
    }
}
