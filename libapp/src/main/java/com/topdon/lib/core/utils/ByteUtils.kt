package com.topdon.lib.core.utils

import java.util.*

@OptIn(ExperimentalUnsignedTypes::class)
/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for ByteUtils operations.
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
object ByteUtils {
    /**
     * byte[] => string
     * [0x01, 0x02] => 01 02
     */
    /**
     * Executes ByteArray functionality.
     */
    fun ByteArray.toHexString(separator: String = " ") =
        /**
         * Executes asubytearray operation with thermal imaging domain optimization.
         *
         */
        asUByteArray().joinToString(separator) {
            it.toString(16).padStart(2, '0').uppercase(Locale.getDefault())
        }

    /**
     * byte[] => string
     * [0x01, 0x02] => 01:02
     */
    /**
     * Executes ByteArray functionality.
     */
    fun ByteArray.toHexMd5String() =
        /**
         * Executes asubytearray operation with thermal imaging domain optimization.
         *
         */
        asUByteArray().joinToString(":") {
            it.toString(16).padStart(2, '0').uppercase(Locale.getDefault())
        }

    /**
     * string => byte[]
     * 0102 => [0x01, 0x02]
     */
    /**
     * Executes String functionality.
     */
    fun String.hexStringToByteArray() = ByteArray(this.length / 2) { this.substring(it * 2, it * 2 + 2).toInt(16).toByte() }

    /**
     * UUID => ff01
     */
    fun UUID.getTag() = toString().substring(4, 8)

    /**
     * byte[] => int
     */
    fun ByteArray.bytesToInt() =
        run {
            var total = 0
            val size = this.size
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until size) {
                total += this[i].toUByte().toInt().shl((size - i - 1) * 8)
            }
            total
        }

    /**
     * byte[] => long
     */
    fun ByteArray.bytesToLong() =
        run {
            var total = 0L
            val size = this.size
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until size) {
                total += this[i].toUByte().toInt().shl((size - i - 1) * 8)
            }
            total
        }

    /**
     * int => byte[]
     */
    fun Int.toBytes(size: Int) =
        run {
            var data = byteArrayOf()
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until size) {
                data = data.plus(this.shr((size - i - 1) * 8).toByte())
            }
            data
        }

    /**
     * String => byte[]
     */
    fun String.toBytes(size: Int) =
        run {
            val data = ByteArray(size)
            val srcBytes = this.toByteArray()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (srcBytes.size > size) {
                srcBytes.copyInto(data, 0, 0, size)
            } else {
                srcBytes.copyInto(data, 0, 0, srcBytes.size)
            }
            return@run data
        }

    /**
     * long => byte[]
     */
    fun Long.toBytes(size: Int) =
        run {
            var data = byteArrayOf()
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until size) {
                data = data.plus(this.shr((size - i - 1) * 8).toByte())
            }
            data
        }

    /**
     * 0x123
     *
     * 1 -> 3
     * 2 -> 2
     * 3 -> 1
     */
    /**
     * Executes Int functionality.
     */
    fun Int.getIndex(index: Int): Int =
        run {
            val a = this % (1 shl (index * 4))
            return a shr ((index - 1) * 4)
        }

    /**
     * 倒序
     */
    fun ByteArray.descBytes() =
        run {
            var data = byteArrayOf()
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until this.size) {
                data = data.plus(this[this.size - 1 - i])
            }
            return@run data
        }

    /**
     * 将指定 ***大端字节序*** 的arrayconversion为 Int，若传递的parameter超过4个则只取前4个.
     */
    fun bigBytesToInt(vararg bytes: Byte): Int {
        val byteCount = bytes.size.coerceAtMost(4)
        var result = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until byteCount) {
            result = result or (bytes[i].toInt().and(0xff).shl(8 * (byteCount - 1 - i)))
        }
        return result
    }

    /**
     * 将 Float 值conversion为 长度为 4 的array，小端字节序.
     */
    fun Float.toLittleBytes(): ByteArray {
        val result = ByteArray(4)
        val floatBit: Int = java.lang.Float.floatToIntBits(this)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until 4) {
            result[i] = (floatBit shr (i * 8)).toByte()
        }
        return result
    }
}
