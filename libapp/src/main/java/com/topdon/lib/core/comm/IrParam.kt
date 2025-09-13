package com.topdon.lib.core.comm

/**
 * des:
 * author: CaiSongL
 * date: 2024/4/30 10:16
 **/
/**
 * Specialized thermal imaging component providing IrParam functionality for the IRCamera system.
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
enum class IrParam {
    ParamLevel, 
    ParamAlarm, 
    ParamSharpness, 
    ParamTempFont, 
    ParamRotate, 
    ParamColor, // Pseudo color
    ParamMirror, 
    ParamCompass, 
    ParamPColor, // Pseudo color样式
    ParamTemperature, // Temperaturemode、高低gain
}

data class TempFont(val textSize: Int, val textColor: Int)
