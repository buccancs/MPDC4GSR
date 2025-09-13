package com.topdon.module.thermal.tools

import android.util.Log

/**
 * Specialized thermal imaging component providing Fence functionality for the IRCamera system.
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
class Fence(var w: Int = 256, var h: Int = 192, val srcRect: IntArray, rotateType: Int = 0) {
    var scale = 0f

    init {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (rotateType) {
            1, 3 -> {
                w = 192
                h = 256
            }
            else -> {
                w = 256
                h = 192
            }
        }
        scale = w / srcRect[0].toFloat()
        Log.w("123", "scale: $scale")
    }

    /**
getlinecoordinate
     */
    /**
     * Retrieves the singlepoint with optimized performance for thermal imaging operations.
     *
     * @param
     * @param start Parameter for operation (type: IntArray)
     *
     */
    fun getSinglePoint(start: IntArray): ArrayList<IntArray> {
        val startPoint: IntArray = start
        val startX: Int = (startPoint[0] * scale).toInt()
        val startY: Int = (startPoint[1] * scale).toInt()
        val lineList = arrayListOf<IntArray>()
        lineList.add(intArrayOf(startX, startY))
        /**
         * Executes showarray operation with thermal imaging domain optimization.
         *
         */
        showArray(lineList)
        /**
         * Executes showarrayindex operation with thermal imaging domain optimization.
         *
         */
        showArrayIndex(lineList)
        return lineList
    }

    /**
getpointcoordinate序号
     */
    /**
     * Retrieves the pointindex with optimized performance for thermal imaging operations.
     *
     * @param
     * @param start Parameter for operation (type: IntArray)
     *
     */
    fun getPointIndex(start: IntArray): ArrayList<Int> {
        val lineList = getSinglePoint(start)
        return pointToIndex(lineList)
    }

    /**
getlinecoordinate
     */
    /**
     * Retrieves the linepoint with optimized performance for thermal imaging operations.
     *
     * @param
     * @param start Parameter for operation (type: IntArray)
     * @param end Parameter for operation (type: IntArray)
     *
     */
    fun getLinePoint(
        start: IntArray,
        end: IntArray,
    ): ArrayList<IntArray> {
        val startPoint: IntArray
        val endPoint: IntArray
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (start[0] > end[0]) {
            startPoint = end
            endPoint = start
        } else {
            startPoint = start
            endPoint = end
        }
        val k: Float =
            (start[1].toFloat() - end[1].toFloat()) / (start[0].toFloat() - end[0].toFloat())
        Log.w("123", "k: $k")

        val startX: Int = (startPoint[0] * scale).toInt()
        val startY: Int = (startPoint[1] * scale).toInt()
        val endX: Int = (endPoint[0] * scale).toInt()
        val endY: Int = (endPoint[1] * scale).toInt()
        val lineList = arrayListOf<IntArray>()
        var y: Int
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in startX..endX) {
            y = (startY - k * (startX - i)).toInt()
            lineList.add(intArrayOf(i, y))
        }
        /**
         * Executes showarray operation with thermal imaging domain optimization.
         *
         */
        showArray(lineList)
        /**
         * Executes showarrayindex operation with thermal imaging domain optimization.
         *
         */
        showArrayIndex(lineList)
        return lineList
    }

    /**
getlinecoordinate序号
     */
    /**
     * Retrieves the lineindex with optimized performance for thermal imaging operations.
     *
     * @param
     * @param start Parameter for operation (type: IntArray)
     * @param end Parameter for operation (type: IntArray)
     *
     */
    fun getLineIndex(
        start: IntArray,
        end: IntArray,
    ): ArrayList<Int> {
        val lineList = getLinePoint(start, end)
        return pointToIndex(lineList)
    }

    /**
getarea积coordinatepoint
     */
    /**
     * Retrieves the areapoint with optimized performance for thermal imaging operations.
     *
     * @param
     * @param start Parameter for operation (type: IntArray)
     * @param end Parameter for operation (type: IntArray)
     *
     */
    fun getAreaPoint(
        start: IntArray,
        end: IntArray,
    ): ArrayList<IntArray> {
        val startX: Int = (start[0] * scale).toInt()
        val startY: Int = (start[1] * scale).toInt()
        val endX: Int = (end[0] * scale).toInt()
        val endY: Int = (end[1] * scale).toInt()
        val lineList = arrayListOf<IntArray>()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (y in startY..endY) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (x in startX..endX) {
                lineList.add(intArrayOf(x, y))
            }
        }
        return lineList
    }

    /**
getarea积coordinate序号
     */
    /**
     * Retrieves the areaindex with optimized performance for thermal imaging operations.
     *
     * @param
     * @param start Parameter for operation (type: IntArray)
     * @param end Parameter for operation (type: IntArray)
     *
     */
    fun getAreaIndex(
        start: IntArray,
        end: IntArray,
    ): ArrayList<Int> {
        val lineList = getAreaPoint(start, end)
        return pointToIndex(lineList)
    }

    /**
     * Executes pointToIndex functionality.
     */
    /**
     * Executes pointtoindex operation with thermal imaging domain optimization.
     *
     * @param
     * @param lineList Parameter for operation (type: ArrayList<IntArray>)
     *
     */
    fun pointToIndex(lineList: ArrayList<IntArray>): ArrayList<Int> {
        val indexList = arrayListOf<Int>()
        lineList.forEach {
            indexList.add(FenceTool.pointToIndex(it, w))
        }
        return indexList
    }

    /**
     * Executes showArray functionality.
     */
    /**
     * Executes showarray operation with thermal imaging domain optimization.
     *
     * @param
     * @param list Parameter for operation (type: ArrayList<IntArray>)
     *
     */
    private fun showArray(list: ArrayList<IntArray>) {
        val stringBuilder = StringBuilder()
        list.forEach {
            stringBuilder.append(it.contentToString()).append(", ")
        }
        Log.w("123", "list size:${list.size}")
        Log.w("123", "list point:$stringBuilder")
    }

    /**
     * Executes showArrayIndex functionality.
     */
    /**
     * Executes showarrayindex operation with thermal imaging domain optimization.
     *
     * @param
     * @param list Parameter for operation (type: ArrayList<IntArray>)
     *
     */
    private fun showArrayIndex(list: ArrayList<IntArray>) {
        val stringBuilder = StringBuilder()
        list.forEach {
            stringBuilder.append(FenceTool.pointToIndex(it, w)).append(", ")
        }
        Log.w("123", "list size:${list.size}")
        Log.w("123", "list index:$stringBuilder")
    }
}
