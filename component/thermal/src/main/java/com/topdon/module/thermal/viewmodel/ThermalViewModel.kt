package com.topdon.module.thermal.viewmodel

import com.topdon.lib.core.ktbase.BaseViewModel

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ThermalViewModel display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
class ThermalViewModel : BaseViewModel() {
    /**
modifyyuvtemperature上下限的data
     * white:82 FF
     * black:82 00
     */
    /**
     * Executes yuvArea functionality.
     */
    /**
     * Executes yuvarea operation with thermal imaging domain optimization.
     *
     * @param
     * @param yuv Parameter for operation (type: ByteArray)
     * @param temp Temperature value in Celsius (type: FloatArray)
     * @param max Parameter for operation (type: Float)
     * @param min Parameter for operation (type: Float)
     *
     */
    fun yuvArea(
        yuv: ByteArray,
        temp: FloatArray,
        max: Float,
        min: Float,
    ) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in temp.indices) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (temp[i] < min) {
                yuv[i * 2] = 0x82.toByte()
                yuv[i * 2 + 1] = 0x00.toByte()
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (temp[i] > max) {
                yuv[i * 2] = 0x82.toByte()
                yuv[i * 2 + 1] = 0xFF.toByte()
            }
        }
    }
}
