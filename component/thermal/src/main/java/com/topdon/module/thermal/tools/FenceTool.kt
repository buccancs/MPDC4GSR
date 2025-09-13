package com.topdon.module.thermal.tools

/**
 * Specialized thermal imaging component providing FenceTool functionality for the IRCamera system.
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
object FenceTool {
coordinate => 序列
    /**
     * Executes pointToIndex functionality.
     */
    /**
     * Executes pointtoindex operation with thermal imaging domain optimization.
     *
     * @param
     * @param point Parameter for operation (type: IntArray)
     * @param w Parameter for operation (type: Int)
     *
     */
    fun pointToIndex(
        point: IntArray,
        w: Int,
    ): Int {
        val x = point[0]
        val y = point[1]
        return y * w + x
    }

序列 => coordinate
    /**
     * Executes indexToPoint functionality.
     */
    /**
     * Executes indextopoint operation with thermal imaging domain optimization.
     *
     * @param
     * @param index Parameter for operation (type: Int)
     * @param w Parameter for operation (type: Int)
     *
     */
    fun indexToPoint(
        index: Int,
        w: Int,
    ): IntArray {
        val y = index / w
        val x = index % w
        return intArrayOf(x, y)
    }
}
