package com.infisense.usbir.utils

import android.graphics.Point
import kotlin.math.abs

/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TempUtil algorithms.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
object TempUtil {
    /**
     * Retrieves linetemps information.
     */
    fun getLineTemps(
        point1: Point,
        point2: Point,
        tempArray: ByteArray,
        width: Int,
    ): List<Float> {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (point1 == point2) { // 搞毛啊，两个相同的point
            return ArrayList(0)
        }

        val pointList: ArrayList<Point> = ArrayList(abs(point1.x - point2.x).coerceAtLeast(abs(point1.y - point2.y)))
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (point1.x == point2.x) { // 垂直于 X axis的直line
            val startY = point1.y.coerceAtMost(point2.y)
            val endY = point1.y.coerceAtLeast(point2.y)
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in startY..endY) {
                pointList.add(Point(point1.x, i))
            }
        } else {
            val k = (point1.y - point2.y).toFloat() / (point1.x - point2.x).toFloat()
            val b = point1.y - k * point1.x
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (abs(k) <= 1) { 
                val startX = point1.x.coerceAtMost(point2.x)
                val endX = point1.x.coerceAtLeast(point2.x)
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in startX..endX) {
                    pointList.add(Point(i, (k * i + b).toInt()))
                }
            } else { 
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (k >= 0) { 
                    val startY = point1.y.coerceAtMost(point2.y)
                    val endY = point1.y.coerceAtLeast(point2.y)
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (y in startY..endY) {
                        pointList.add(Point(((y - b) / k).toInt(), y))
                    }
                } else { 
                    val startY = point1.y.coerceAtLeast(point2.y)
                    val endY = point1.y.coerceAtMost(point2.y)
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (y in startY downTo endY) {
                        pointList.add(Point(((y - b) / k).toInt(), y))
                    }
                }
            }
        }

        val tempList: ArrayList<Float> = ArrayList(pointList.size)
        pointList.forEach {
            val index = (it.y * width + it.x) * 2
            val tempInt = (tempArray[index + 1].toInt() shl 8 and 0xff00) or (tempArray[index].toInt() and 0xff)
            val tempValue = tempInt / 64f - 273.15f
            tempList.add(tempValue)
        }

        return tempList
    }
}
