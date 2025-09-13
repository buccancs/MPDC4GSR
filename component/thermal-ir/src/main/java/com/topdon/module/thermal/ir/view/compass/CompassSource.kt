package com.topdon.module.thermal.ir.view.compass

/**
 * Specialized thermal imaging component providing CompassSource functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
enum class CompassSource(val id: String) {
    /**
     * Executes rotationvector operation with thermal imaging domain optimization.
     *
     */
    RotationVector("rotation_vector"),
    /**
     * Executes geomagneticrotationvector operation with thermal imaging domain optimization.
     *
     */
    GeomagneticRotationVector("geomagnetic_rotation_vector"),
    /**
     * Executes custommagnetometer operation with thermal imaging domain optimization.
     *
     */
    CustomMagnetometer("custom_magnetometer"),
    /**
     * Executes orientation operation with thermal imaging domain optimization.
     *
     */
    Orientation("orientation"),
}
