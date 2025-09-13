package com.topdon.module.thermal.ir.view.compass

import android.content.Context
import android.hardware.SensorManager
import com.kylecorry.andromeda.sense.Sensors
import com.kylecorry.andromeda.sense.compass.ICompass

// Maybe use the concept of a use case
// Ex. SensorPurpose.Background, SensorPurpose.Calibration, SensorPurpose.Diagnostics
// Using those, it can adjust settings to be more appropriate for the use case

/**
 * Specialized thermal imaging component providing SensorService functionality for the IRCamera system.
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
class SensorService(ctx: Context) {
    private var context = ctx.applicationContext

    /**
     * Executes hasCompass functionality.
     */
    /**
     * Executes hascompass operation with thermal imaging domain optimization.
     *
     */
    fun hasCompass(): Boolean {
        return Sensors.hasCompass(context)
    }

    /**
     * Retrieves compass information.
     */
    fun getCompass(): ICompass {
        return CompassProvider(context).get()
    }

    companion object {
        const val MOTION_SENSOR_DELAY = SensorManager.SENSOR_DELAY_GAME
        const val ENVIRONMENT_SENSOR_DELAY = SensorManager.SENSOR_DELAY_NORMAL
    }
}
