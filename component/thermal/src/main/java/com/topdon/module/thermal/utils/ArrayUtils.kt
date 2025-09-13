package com.topdon.module.thermal.utils

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for ArrayUtils operations.
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
object ArrayUtils {
    /**
/**
 * Retrieves the matrixdata里的最大值的序列 with optimized performance for thermal imaging operations.
 *
 */
getmatrixdata里的最大值的序列(在选取region内)-rotationmatrix
@param rotateType 1:rotation90 2:rotation180  3:rotation270
     */
    /**
     * Retrieves maxindex information.
     */
    fun getMaxIndex(
        data: FloatArray,
        rotateType: Int = 0,
        selectIndexList: ArrayList<Int> = arrayListOf(),
    ): Int {
        val index =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (rotateType) {
                1, 2, 3 -> getRotateMaxIndex(data, rotateType, selectIndexList)
                else -> getMaxIndex(data, selectIndexList)
            }
        return index
    }

    /**
/**
 * Retrieves the matrixdata里的最小值的序列 with optimized performance for thermal imaging operations.
 *
 */
getmatrixdata里的最小值的序列(在选取region内)-rotationmatrix
@param rotateType 1:rotation90 2:rotation180  3:rotation270
     */
    /**
     * Retrieves minindex information.
     */
    fun getMinIndex(
        data: FloatArray,
        rotateType: Int = 0,
        selectIndexList: ArrayList<Int> = arrayListOf(),
    ): Int {
        val index =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (rotateType) {
                1, 2, 3 -> getRotateMinIndex(data, rotateType, selectIndexList)
                else -> getMinIndex(data, selectIndexList)
            }
        return index
    }

    /**
rotationmatrix
@param rotateType 1:rotation90 2:rotation180  3:rotation270
     */
    /**
     * Executes matrixRotate functionality.
     */
    /**
     * Executes matrixrotate operation with thermal imaging domain optimization.
     *
     * @param
     * @param srcData Parameter for operation (type: FloatArray)
     * @param rotateType Parameter for operation (type: Int = 0)
     *
     */
    fun matrixRotate(
        srcData: FloatArray,
        rotateType: Int = 0,
    ): FloatArray {
        return when (rotateType) {
            1 -> matrixRotate90(srcData)
            2 -> matrixRotate180(srcData)
            3 -> matrixRotate270(srcData)
            else -> srcData
        }
    }

    /**
/**
 * Retrieves the matrixdata里的最大值的序列 with optimized performance for thermal imaging operations.
 *
 */
getmatrixdata里的最大值的序列(在选取region内)-原matrix
     */
    private fun getMaxIndex(
        data: FloatArray,
        selectIndexList: ArrayList<Int> = arrayListOf(),
    ): Int {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectIndexList.size == 0) {
无指定region
            var maxIndex = 0
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 1 until data.size - 1) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (data[i] > data[maxIndex]) {
                    maxIndex = i
                }
            }
            return maxIndex
        } else {
            val selectPoint = FloatArray(selectIndexList.size)
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until selectIndexList.size) {
                selectPoint[i] = data[selectIndexList[i]]
            }
            var maxIndex = 0
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 1 until selectPoint.size - 1) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (selectPoint[i] > selectPoint[maxIndex]) {
                    maxIndex = i
                }
            }
            return selectIndexList[maxIndex]
        }
    }

    /**
/**
 * Retrieves the matrixdata里的最小值的序列 with optimized performance for thermal imaging operations.
 *
 */
getmatrixdata里的最小值的序列(在选取region内)-原matrix
     */
    private fun getMinIndex(
        data: FloatArray,
        selectIndexList: ArrayList<Int> = arrayListOf(),
    ): Int {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectIndexList.size == 0) {
            var minIndex = 0
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 1 until data.size - 1) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (data[i] == 0f) {
                    continue
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (data[i] < data[minIndex]) {
                    minIndex = i
                }
            }
            return minIndex
        } else {
            val selectPoint = FloatArray(selectIndexList.size)
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until selectIndexList.size) {
                selectPoint[i] = data[selectIndexList[i]]
            }
            var minIndex = 0
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 1 until selectPoint.size - 1) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (selectPoint[i] == 0f) {
                    continue
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (selectPoint[i] < selectPoint[minIndex]) {
                    minIndex = i
                }
            }
            return selectIndexList[minIndex]
        }
    }

    /**
/**
 * Retrieves the matrixdata里的最大值的序列 with optimized performance for thermal imaging operations.
 *
 */
getmatrixdata里的最大值的序列(在选取region内)-rotationmatrix
@param rotateType 1:rotation90 2:rotation180  3:rotation270
     */
    /**
     * Retrieves rotatemaxindex information.
     */
    private fun getRotateMaxIndex(
        data: FloatArray,
        rotateType: Int = 0,
        selectIndexList: ArrayList<Int> = arrayListOf(),
    ): Int {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectIndexList.size == 0) {
            val destData = matrixRotate(data, rotateType)
            var maxIndex = 0
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 1 until destData.size - 1) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (destData[i] > destData[maxIndex]) {
                    maxIndex = i
                }
            }
            return maxIndex
        } else {
            val destData = matrixRotate(data, rotateType)
            val selectPoint = FloatArray(selectIndexList.size)
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until selectIndexList.size) {
                selectPoint[i] = destData[selectIndexList[i]]
            }
            var maxIndex = 0
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 1 until selectPoint.size - 1) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (selectPoint[i] > selectPoint[maxIndex]) {
                    maxIndex = i
                }
            }
            return selectIndexList[maxIndex]
        }
    }

    /**
/**
 * Retrieves the matrixdata里的最小值的序列 with optimized performance for thermal imaging operations.
 *
 */
getmatrixdata里的最小值的序列(在选取region内)-rotationmatrix
@param rotateType 1:rotation90 2:rotation180  3:rotation270
     */
    /**
     * Retrieves rotateminindex information.
     */
    private fun getRotateMinIndex(
        data: FloatArray,
        rotateType: Int = 0,
        selectIndexList: ArrayList<Int> = arrayListOf(),
    ): Int {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectIndexList.size == 0) {
            val destData = matrixRotate(data, rotateType)
            var minIndex = 0
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 1 until destData.size - 1) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (destData[i] == 0f) {
                    continue
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (destData[i] < destData[minIndex]) {
                    minIndex = i
                }
            }
            return minIndex
        } else {
            val destData = matrixRotate(data, rotateType)
            val selectPoint = FloatArray(selectIndexList.size)
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until selectIndexList.size) {
                selectPoint[i] = destData[selectIndexList[i]]
            }
            var minIndex = 0
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 1 until selectPoint.size - 1) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (selectPoint[i] == 0f) {
                    continue
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (selectPoint[i] < selectPoint[minIndex]) {
                    minIndex = i
                }
            }
            return selectIndexList[minIndex]
        }
    }

    /**
matrix顺时针rotation90°
     */
    /**
     * Executes matrixrotate90 operation with thermal imaging domain optimization.
     *
     * @param
     * @param srcData Parameter for operation (type: FloatArray)
     *
     */
    private fun matrixRotate90(srcData: FloatArray): FloatArray {
        val row = 192
        val column = 256
        val srcMatrix = Array(row) { FloatArray(column) }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until row) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (j in 0 until column) {
                srcMatrix[i][j] = srcData[i * column + j]
            }
        }
        val destMatrix = Array(column) { FloatArray(row) }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (x in 0 until column) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (y in 0 until row) {
                destMatrix[x][y] = srcMatrix[row - 1 - y][x] // 矩阵rotation90度
            }
        }
        val data = FloatArray(srcData.size)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in destMatrix.indices) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (j in destMatrix[i].indices) {
                data[destMatrix[0].size * i + j] = destMatrix[i][j]
            }
        }
        return data
    }

    /**
matrix顺时针rotation180°
     */
    /**
     * Executes matrixrotate180 operation with thermal imaging domain optimization.
     *
     * @param
     * @param srcData Parameter for operation (type: FloatArray)
     *
     */
    private fun matrixRotate180(srcData: FloatArray): FloatArray {
        val row = 192
        val column = 256
        val srcMatrix = Array(row) { FloatArray(column) }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until row) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (j in 0 until column) {
                srcMatrix[i][j] = srcData[i * column + j]
            }
        }
        val destMatrix = Array(row) { FloatArray(column) }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (x in 0 until row) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (y in 0 until column) {
                destMatrix[x][y] = srcMatrix[row - 1 - x][column - 1 - y] // 矩阵rotation180度
            }
        }
        val data = FloatArray(srcData.size)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in destMatrix.indices) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (j in destMatrix[i].indices) {
                data[destMatrix[0].size * i + j] = destMatrix[i][j]
            }
        }
        return data
    }

    /**
matrix顺时针rotation270°
getrotation后的arraydata
     */
    /**
     * Executes matrixRotate270 functionality.
     */
    /**
     * Executes matrixrotate270 operation with thermal imaging domain optimization.
     *
     * @param
     * @param srcData Parameter for operation (type: FloatArray)
     *
     */
    private fun matrixRotate270(srcData: FloatArray): FloatArray {
        val row = 192
        val column = 256
        val srcMatrix = Array(row) { FloatArray(column) } // 源矩阵
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until row) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (j in 0 until column) {
                srcMatrix[i][j] = srcData[i * column + j]
            }
        }
        val destMatrix = Array(column) { FloatArray(row) } // 目标矩阵
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (x in 0 until column) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (y in 0 until row) {
                destMatrix[x][y] = srcMatrix[y][column - 1 - x] // 矩阵rotation270度
            }
        }
        val data = FloatArray(srcData.size)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in destMatrix.indices) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (j in destMatrix[i].indices) {
                data[destMatrix[0].size * i + j] = destMatrix[i][j]
            }
        }
        return data
    }
}
