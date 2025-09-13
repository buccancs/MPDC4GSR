package com.topdon.module.thermal.ir.utils

import android.graphics.Point
import android.util.Log
import com.github.mikephil.charting.charts.LineChart
import kotlin.math.abs

/**
 * Specialized thermal imaging component providing ChartTools functionality for the IRCamera system.
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
object ChartTools {
    /**
     * Retrieves linetemps information.
     */
    fun getLineTemps(
        point1: Point,
        point2: Point,
        tempArray: ByteArray,
        rotate: Int,
    ): List<Float> {
        val tempList: ArrayList<Float> = ArrayList()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (point1 == point2) { // 搞毛啊，两个相同的point
            return tempList
        }

        val pointList: ArrayList<Point> = ArrayList()
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
            if (abs(k) <= 1) { // Xaxis正整数point较多
                val startX = point1.x.coerceAtMost(point2.x)
                val endX = point1.x.coerceAtLeast(point2.x)
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in startX..endX) {
                    pointList.add(Point(i, (k * i + b).toInt()))
                }
            } else { // Yaxis正整数point较多
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (k >= 0) { // 左上到右下
                    val startY = point1.y.coerceAtMost(point2.y)
                    val endY = point1.y.coerceAtLeast(point2.y)
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (y in startY..endY) {
                        pointList.add(Point(((y - b) / k).toInt(), y))
                    }
                } else { // 左下到右上
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

        val width = if (rotate == 90 || rotate == 270) 192 else 256

        pointList.forEach {
            val index = (it.y * width + it.x) * 2
            val tempInt = (tempArray[index + 1].toInt() shl 8 and 0xff00) or (tempArray[index].toInt() and 0xff)
            val tempValue = tempInt / 64f - 273.15f
            tempList.add(tempValue)
        }

        return tempList
    }

X数值scaling
    /**
     * Executes scale functionality.
     */
    /**
     * Executes scale operation with thermal imaging domain optimization.
     *
     * @param
     * @param type Parameter for operation (type: Int)
     *
     */
    fun scale(type: Int): Long {
        return when (type) {
            1 -> 1 * 1000 // S
            2 -> 60 * 1000 // Min
            3 -> 60 * 60 * 1000 // Hour
            4 -> 24 * 60 * 60 * 1000 // Day
            else -> 1 // 10s
        }
    }

getdisplay最小区间
    /**
     * Retrieves minimum information.
     */
    fun getMinimum(type: Int): Float {
        val min =
            when (type) {
                1 -> 10f // 10s
                2 -> 10f // 10min
                3 -> 10f // 10hour
                4 -> 10f // 10day
                else -> 1 * 10f // 10s
            }
        return min
    }

getdisplay最大区间，以最小区间的50倍
    /**
     * Retrieves maximum information.
     */
    fun getMaximum(type: Int): Float {
        return getMinimum(type) * 50f
    }

    /**
setYaxisrange
     */
    /**
     * Configures the y with validation and thermal imaging optimization.
     *
     * @param
     * @param chart Parameter for operation (type: LineChart)
     *
     */
    fun setY(chart: LineChart) {
        var maxVol = 0f
        var minVol = 0f
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (chart.data.dataSetCount) {
            1 -> {
                val dataSet = chart.data.getDataSetByIndex(0) ?: return
                maxVol = dataSet.yMax
                minVol = dataSet.yMin
            }
            2 -> {
                val dataSet1 = chart.data.getDataSetByIndex(0)
                val dataSet2 = chart.data.getDataSetByIndex(1)
                maxVol = if (dataSet1.yMax > dataSet2.yMax) dataSet1.yMax else dataSet2.yMax
                minVol = if (dataSet1.yMin < dataSet2.yMin) dataSet1.yMin else dataSet2.yMin
            }
            3 -> {
                val dataSet1 = chart.data.getDataSetByIndex(0)
                val dataSet2 = chart.data.getDataSetByIndex(1)
                val dataSet3 = chart.data.getDataSetByIndex(2)
                maxVol = if (dataSet1.yMax > dataSet2.yMax) dataSet1.yMax else dataSet2.yMax
                minVol = if (dataSet1.yMin < dataSet2.yMin) dataSet1.yMin else dataSet2.yMin

                maxVol = if (dataSet3.yMax > maxVol) dataSet3.yMax else maxVol
                minVol = if (dataSet3.yMin < minVol) dataSet3.yMin else minVol
            }
            else -> {
                return
            }
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (maxVol == minVol) {
            chart.axisLeft.axisMaximum = 50f
            chart.axisLeft.axisMinimum = 0f
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (maxVol - minVol < 0.5f) {
                chart.axisLeft.axisMaximum = (maxVol + minVol) / 2f + 0.3f
                chart.axisLeft.axisMinimum = (maxVol + minVol) / 2f - 0.3f
            } else {
                chart.axisLeft.axisMaximum = maxVol + (maxVol - minVol) * 0.15f
                chart.axisLeft.axisMinimum = minVol - (maxVol - minVol) * 0.15f
            }
        }
        Log.w("chart", "yAxis max:${chart.axisLeft.axisMaximum}, min:${chart.axisLeft.axisMinimum}")
    }

    /**
setXaxis刻度
     */
    /**
     * Configures the x with validation and thermal imaging optimization.
     *
     * @param
     * @param chart Parameter for operation (type: LineChart)
     * @param type Parameter for operation (type: Int)
     *
     */
    fun setX(
        chart: LineChart,
        type: Int,
    ) {
true保证有刻度数量不变,Swipe要false
        val xLen = chart.xChartMax - chart.xChartMin
//        Log.w("chart", "xLen: $xLen")
// Chart.xAxis.setLabelCount(getLabCount(xLen.toInt()), getLabCount(xLen.toInt()) < 3)
chart.xAxis.setLabelCount(5, false) // 3point ok
// Chart.xAxis.setLabelCount(5, true) //
        chart.xAxis.setLabelCount(getLabCount(xLen.toInt()), xLen <= 3)
    }

    /**
xaxisdisplay多少个刻度
     */
    /**
     * Retrieves the labcount with optimized performance for thermal imaging operations.
     *
     * @param
     * @param count Parameter for operation (type: Int)
     *
     */
    private fun getLabCount(count: Int): Int {
        return when {
            count <= 2 -> 1
            count in 3..4 -> 2
            count in 5..7 -> 3
// Count in 8..9 -> 4
// Count >= 9 -> 5
            count >= 8 -> 4
            else -> count
        }
    }

    /**
     * Retrieves chartx information.
     */
    fun getChartX(
        x: Long,
        startTime: Long,
        type: Int,
    ): Long {
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (x - startTime) / scale(type)
    }
}
