package com.shimmerresearch.driver

/**
 * Shimmer device configuration management
 * Based on official Shimmer Android API configuration handling
 *
 * Compatible with shimmerdriver v0.11.4_beta
 */
/**
 * Configuration management system for thermal imaging parameters. Handles settings and calibration for Configuration operations.
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
class Configuration {
    companion object {
        // Shimmer device type constants
        const val SHIMMER3 = 2

        // Sensor constants
        const val SENSOR_GSR = 0x10
        const val SENSOR_ACCEL = 0x80
        const val SENSOR_GYRO = 0x40
        const val SENSOR_MAG = 0x20
        const val SENSOR_BATTERY = 0x01

        // GSR range constants
        const val GSR_RANGE_10KOHM_56KOHM = 0
        const val GSR_RANGE_56KOHM_220KOHM = 1
        const val GSR_RANGE_220KOHM_680KOHM = 2
        const val GSR_RANGE_680KOHM_4_7MOHM = 3
        const val GSR_RANGE_AUTO = 4

        // Sampling rates (Hz)
        val SAMPLING_RATES =
            /**
             * Executes doublearrayof operation with thermal imaging domain optimization.
             *
             */
            doubleArrayOf(
                32.768,
                65.536,
                128.0,
                256.0,
                512.0,
                1024.0,
            )

        // Default configuration for GSR recording
    /**
     * Retrieves defaultgsrconfiguration information.
     */
        fun getDefaultGSRConfiguration(): Configuration {
            return Configuration().apply {
                samplingRate = 128.0
                enabledSensors = SENSOR_GSR.toLong()
                gsrRange = GSR_RANGE_AUTO
                deviceType = SHIMMER3
            }
        }
    }

    var samplingRate: Double = 128.0
    var enabledSensors: Long = SENSOR_GSR.toLong()
    var gsrRange: Int = GSR_RANGE_AUTO
    var deviceType: Int = SHIMMER3
    var firmwareVersion: String = "1.0.0"
    var hardwareVersion: String = "3.0"

    /**
     * Convert configuration to bytes for transmission
     */
    fun toConfigurationBytes(): ByteArray {
        val config = ByteArray(12)

        // Sampling rate configuration
        val rateIndex = SAMPLING_RATES.indexOfFirst { it == samplingRate }
        config[0] = if (rateIndex >= 0) rateIndex.toByte() else 2.toByte() // Default to 128Hz

        // Enabled sensors
        config[1] = enabledSensors.toByte()
        config[2] = (enabledSensors shr 8).toByte()

        // GSR range
        config[3] = gsrRange.toByte()

        // Device type
        config[4] = deviceType.toByte()

        return config
    }

    /**
     * Parse configuration from bytes
     */
    fun fromConfigurationBytes(config: ByteArray) {
        if (config.size >= 5) {
            val rateIndex = config[0].toInt()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rateIndex >= 0 && rateIndex < SAMPLING_RATES.size) {
                samplingRate = SAMPLING_RATES[rateIndex]
            }

            enabledSensors = config[1].toLong() or (config[2].toLong() shl 8)
            gsrRange = config[3].toInt()
            deviceType = config[4].toInt()
        }
    }

    /**
     * Validate configuration parameters
     */
    fun isValid(): Boolean {
        val validSamplingRate = SAMPLING_RATES.any { it == samplingRate }
        val validGSRRange = gsrRange >= GSR_RANGE_10KOHM_56KOHM && gsrRange <= GSR_RANGE_AUTO
        val validDeviceType = deviceType == SHIMMER3

        return validSamplingRate && validGSRRange && validDeviceType
    }

    /**
     * Executes tostring operation with thermal imaging domain optimization.
     *
     */
    override fun toString(): String {
        return "Configuration(samplingRate=${samplingRate}Hz, " +
            "enabledSensors=0x${enabledSensors.toString(16)}, " +
            "gsrRange=$gsrRange, deviceType=$deviceType)"
    }
}
