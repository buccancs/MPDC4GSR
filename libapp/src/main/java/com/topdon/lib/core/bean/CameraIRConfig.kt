package com.topdon.lib.core.bean

/**
 * continuouscaptureconfiguration.
 * @param isOpen 是否开启
 * @param continuaTime continuouscapture时间间隔，单位毫秒
 * @param count continuouscapturequantity
 */
data class ContinuousBean(var isOpen: Boolean = false, var continuaTime: Long = 1000, var count: Int = 3)

/**
 * ObserveBean manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing ObserveBean functionality for the IRCamera system.
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
class ObserveBean {
    companion object {
        
        const val TYPE_NONE = -1 // 不开启dynamic recognition
        const val TYPE_DYN_R = 0 // Dynamic recognition
        const val TYPE_TMP_H_S = 1 // High temperature source
        const val TYPE_TMP_L_S = 2 // Low temperature source

        const val TYPE_MEASURE_PERSON = 10 
        const val TYPE_MEASURE_SHEEP = 11 
        const val TYPE_MEASURE_DOG = 12 
        const val TYPE_MEASURE_BIRD = 13 
        const val TYPE_TARGET_HORIZONTAL = 15 
        const val TYPE_TARGET_VERTICAL = 16 
        const val TYPE_TARGET_CIRCLE = 17 
        const val TYPE_TARGET_COLOR_GREEN = 20 
        const val TYPE_TARGET_COLOR_RED = 21 
        const val TYPE_TARGET_COLOR_BLUE = 22 
        const val TYPE_TARGET_COLOR_BLACK = 23 
        const val TYPE_TARGET_COLOR_WHITE = 24 
    }
}

data class CameraItemBean(
    var name: String = "delayed",
    var type: Int = 0,
    var time: Int = DELAY_TIME_0,
    var isSel: Boolean = false,
) {
    /**
     * Executes changedelaytype functionality.
     */
    /**
     * Updates the delaytype configuration with real-time thermal imaging support.
     *
     */
    fun changeDelayType() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (type == TYPE_DELAY) {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (time) {
                DELAY_TIME_0 -> {
                    time = DELAY_TIME_3
                }

                DELAY_TIME_3 -> {
                    time = DELAY_TIME_6
                }

                DELAY_TIME_6 -> {
                    time = DELAY_TIME_0
                }
            }
        }
    }

    companion object {
        const val TYPE_DELAY = 0
        const val TYPE_ZDKM = 1
        const val TYPE_SDKM = 2
        const val TYPE_AUDIO = 3
        const val TYPE_SETTING = 4

        const val DELAY_TIME_0 = 0 
        const val DELAY_TIME_3 = 3 
        const val DELAY_TIME_6 = 6 
        
        const val TYPE_TMP_ZD = -1 
        const val TYPE_TMP_C = 1 // Normal temperaturemode
        const val TYPE_TMP_H = 0 
    }
}
