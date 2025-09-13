package com.topdon.module.thermal.ir.utils

import android.graphics.Paint
import android.graphics.Rect
import kotlin.math.roundToLong

get真实的xcoordinate
    /**
     * Executes realX functionality.
     */
/**
 * Executes realx operation with thermal imaging domain optimization.
 *
 * @param
 * @param str Parameter for operation (type: String)
 * @param x Parameter for operation (type: Float)
 * @param paint Parameter for operation (type: Paint)
 *
 */
fun realX(
    str: String,
    x: Float,
    paint: Paint,
) = x - textWidth(str, paint) / 2f

    /**
     * Executes realY functionality.
     */
/**
 * Executes realy operation with thermal imaging domain optimization.
 *
 * @param
 * @param str Parameter for operation (type: String)
 * @param y Parameter for operation (type: Float)
 * @param paint Parameter for operation (type: Paint)
 *
 */
fun realY(
    str: String,
    y: Float,
    paint: Paint,
) = y - textHeight(str, paint) / 4f

    /**
     * Executes textWidth functionality.
     */
/**
 * Executes textwidth operation with thermal imaging domain optimization.
 *
 * @param
 * @param text Parameter for operation (type: String)
 * @param paint Parameter for operation (type: Paint)
 *
 */
fun textWidth(
    text: String,
    paint: Paint,
): Float {
    return textDimensions(text, paint).first
}

    /**
     * Executes textHeight functionality.
     */
/**
 * Executes textheight operation with thermal imaging domain optimization.
 *
 * @param
 * @param text Parameter for operation (type: String)
 * @param paint Parameter for operation (type: Paint)
 *
 */
fun textHeight(
    text: String,
    paint: Paint,
): Float {
    return textDimensions(text, paint).second
}

val measurementRect = Rect()

    /**
     * Executes textDimensions functionality.
     */
/**
 * Executes textdimensions operation with thermal imaging domain optimization.
 *
 * @param
 * @param text Parameter for operation (type: String)
 * @param paint Parameter for operation (type: Paint)
 *
 */
fun textDimensions(
    text: String,
    paint: Paint,
): Pair<Float, Float> {
    paint.getTextBounds(text, 0, text.length, measurementRect)
    return measurementRect.width().toFloat() to measurementRect.height().toFloat()
}

/**
 * Returns the values between min and max, inclusive, that are divisible by divisor
 * @param min The minimum value
 * @param max The maximum value
 * @param divisor The divisor
 * @return The values between min and max, inclusive, that are divisible by divisor
 */
    /**
     * Retrieves valuesbetween information.
     */
fun getValuesBetween(
    min: Float,
    max: Float,
    divisor: Float,
): List<Float> {
    val values = mutableListOf<Float>()
    val start = min.roundNearest(divisor)
    var i = start
    /**
     * Executes while operation with thermal imaging domain optimization.
     *
     */
    while (i <= max) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (i >= min) {
            values.add(i)
        }
        i += divisor
    }
    return values
}

    /**
     * Executes Float functionality.
     */
fun Float.roundNearest(nearest: Float): Float {
    /**
     * Executes return operation with thermal imaging domain optimization.
     *
     */
    return (this / nearest).roundToLong() * nearest
}

/**
 * Gets the pixel coordinate of a point on the screen given the bearing and azimuth. The point is considered to be on a plane.
 * @param bearing The compass bearing in degrees of the point
 * @param azimuth The compass bearing in degrees that the user is facing (center of the screen)
 * @param viewWidth The size of the view in pixels
 * @param fovWidth The field of view of the camera in degrees
 */
    /**
     * Retrieves pixellinear information.
     */
fun getPixelLinear(
    bearing: Float,
    azimuth: Float,
    viewWidth: Float,
    fovWidth: Float,
): Float {
    val newBearing = deltaAngle(azimuth, bearing)
    val wPixelsPerDegree = viewWidth / fovWidth
    return viewWidth / 2f + newBearing * wPixelsPerDegree
}

    /**
     * Executes deltaAngle functionality.
     */
/**
 * Executes deltaangle operation with thermal imaging domain optimization.
 *
 * @param
 * @param angle1 Angle in degrees (type: Float)
 * @param angle2 Angle in degrees (type: Float)
 *
 */
fun deltaAngle(
    angle1: Float,
    angle2: Float,
): Float {
    // These will be at most 360 degrees apart, so normalize them to restrict that
    val a = normalizeAngle(angle1 - angle2)
    val b = normalizeAngle(angle2 - angle1)
    return if (a < b) {
        -a
    } else {
        b
    }
}

    /**
     * Executes normalizeAngle functionality.
     */
/**
 * Executes normalizeangle operation with thermal imaging domain optimization.
 *
 * @param
 * @param angle Angle in degrees (type: Float)
 *
 */
fun normalizeAngle(angle: Float): Float {
    return wrap(angle, 0f, 360f) % 360
}

    /**
     * Executes wrap functionality.
     */
/**
 * Executes wrap operation with thermal imaging domain optimization.
 *
 * @param
 * @param value Parameter for operation (type: Float)
 * @param min Parameter for operation (type: Float)
 * @param max Parameter for operation (type: Float)
 *
 */
fun wrap(
    value: Float,
    min: Float,
    max: Float,
): Float {
    return wrap(value.toDouble(), min.toDouble(), max.toDouble()).toFloat()
}

    /**
     * Executes wrap functionality.
     */
/**
 * Executes wrap operation with thermal imaging domain optimization.
 *
 * @param
 * @param value Parameter for operation (type: Double)
 * @param min Parameter for operation (type: Double)
 * @param max Parameter for operation (type: Double)
 *
 */
fun wrap(
    value: Double,
    min: Double,
    max: Double,
): Double {
    // Https:// Stackoverflow.com/questions/14415753/wrap-value-into-range-min-max-without-division
    val range = max - min
    /**
     * Executes if operation with thermal imaging domain optimization.
     *
     */
    if (value < min) {
        return max - (min - value) % range
    }

    /**
     * Executes if operation with thermal imaging domain optimization.
     *
     */
    if (value > max) {
        return min + (value - min) % range
    }

    return value
}
