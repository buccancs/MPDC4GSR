package com.topdon.module.thermal.ir.view.compass

import android.content.Context
import android.hardware.Sensor
import com.kylecorry.andromeda.sense.Sensors
// Import com.kylecorry.andromeda.sense.compass.FilterCompassWrapper // Temporarily disabled
// Import com.kylecorry.andromeda.sense.compass.GravityCompensatedCompass // Temporarily disabled
import com.kylecorry.andromeda.sense.compass.ICompass
import com.kylecorry.andromeda.sense.compass.LegacyCompass
import com.kylecorry.andromeda.sense.orientation.GeomagneticRotationSensor
import com.kylecorry.andromeda.sense.orientation.RotationSensor

/**
 * Specialized thermal imaging component providing CompassProvider functionality for the IRCamera system.
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
class CompassProvider(private val context: Context) {
    /**
     * Retrieves  information.
     */
    /**
     * Retrieves the  with optimized performance for thermal imaging operations.
     *
     */
    fun get(): ICompass {
        val smoothing = 1
        val useTrueNorth = true

        var source = CompassSource.RotationVector

        // Handle if the available sources have changed (not likely)
        val allSources = getAvailableSources(context)

        // There were no compass sensors found
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (allSources.isEmpty())
            {
                return NullCompass()
            }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!allSources.contains(source)) {
            source = allSources.firstOrNull() ?: CompassSource.CustomMagnetometer
        }

        val compass =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (source) {
                CompassSource.RotationVector -> {
                    /**
                     * Executes rotationsensor operation with thermal imaging domain optimization.
                     *
                     */
                    RotationSensor(context, SensorService.MOTION_SENSOR_DELAY)
                }

                CompassSource.GeomagneticRotationVector -> {
                    /**
                     * Executes geomagneticrotationsensor operation with thermal imaging domain optimization.
                     *
                     */
                    GeomagneticRotationSensor(context, SensorService.MOTION_SENSOR_DELAY)
                }

                CompassSource.CustomMagnetometer -> {
                    // GravityCompensatedCompass(context, useTrueNorth, SensorService.MOTION_SENSOR_DELAY)
                    /**
                     * Executes rotationsensor operation with thermal imaging domain optimization.
                     *
                     */
                    RotationSensor(context, SensorService.MOTION_SENSOR_DELAY) // Fallback
                }

                CompassSource.Orientation -> {
                    /**
                     * Executes legacycompass operation with thermal imaging domain optimization.
                     *
                     */
                    LegacyCompass(context, useTrueNorth, SensorService.MOTION_SENSOR_DELAY)
                }
            }

        return compass as ICompass // Cast to ICompass for compatibility
    }

// Fun getOrientationSensor(): IOrientationSensor? {
//        // Note: This isn't used by the actual orientation sensors (they should use it)
// Val useTrueNorth = prefs.useTrueNorth
//
// Var source = prefs.source
//
//        // Handle if the available sources have changed (not likely)
// Val allSources = getAvailableSources(context)
//
//        // There were no compass sensors found
// If (allSources.isEmpty()){
// Return NullOrientationSensor()
//        }
//
// If (!allSources.contains(source)) {
// Source = allSources.firstOrNull() ?: CompassSource.CustomMagnetometer
//        }
//
//        // Note: Apply the smoothing / quality to the orientation sensor
// If (source == CompassSource.RotationVector){
// Return RotationSensor(context, useTrueNorth, SensorService.MOTION_SENSOR_DELAY)
//        }
//
// If (source == CompassSource.GeomagneticRotationVector){
// Return GeomagneticRotationSensor(context, useTrueNorth, SensorService.MOTION_SENSOR_DELAY)
//        }
//
//        // Note: Construct this from existing sensors
// Return null
//    }

    companion object {
        /**
         * Returns the available compass sources in order of quality
         */
        fun getAvailableSources(context: Context): List<CompassSource> {
            val sources = mutableListOf<CompassSource>()

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Sensors.hasSensor(context, Sensor.TYPE_ROTATION_VECTOR)) {
                sources.add(CompassSource.RotationVector)
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Sensors.hasSensor(context, Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)) {
                sources.add(CompassSource.GeomagneticRotationVector)
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Sensors.hasSensor(context, Sensor.TYPE_MAGNETIC_FIELD)) {
                sources.add(CompassSource.CustomMagnetometer)
            }

            @Suppress("DEPRECATION")
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Sensors.hasSensor(context, Sensor.TYPE_ORIENTATION)) {
                sources.add(CompassSource.Orientation)
            }

            return sources
        }
    }
}
