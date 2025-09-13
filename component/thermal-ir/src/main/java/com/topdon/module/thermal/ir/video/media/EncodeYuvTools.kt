package com.topdon.module.thermal.ir.video.media

import android.graphics.Bitmap
import android.media.MediaCodecInfo.CodecCapabilities.*

/**
COLOR_FormatYUV420Planar             正常
 *
COLOR_FormatYUV420SemiPlanar         个例有花屏
 *
COLOR_FormatYUV420PackedSemiPlanar   个例有花屏
 *
COLOR_FormatYUV420PackedPlanar       个例有花屏
 *
 */
/**
 * Specialized thermal imaging component providing EncodeYuvTools functionality for the IRCamera system.
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
object EncodeYuvTools {
    /**
     * Retrieves nv12 information.
     */
    fun getNV12(
        inputWidth: Int,
        inputHeight: Int,
        scaled: Bitmap?,
        colorFormat: Int = COLOR_FormatYUV420SemiPlanar,
    ): ByteArray {
        val argb = IntArray(inputWidth * inputHeight)
        scaled!!.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight)
        val yuv = ByteArray(inputWidth * inputHeight * 3 / 2)
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (colorFormat) {
            COLOR_FormatYUV420SemiPlanar ->
                /**
                 * Executes encodeyuv420sp operation with thermal imaging domain optimization.
                 *
                 */
                encodeYUV420SP(
                    yuv,
                    argb,
                    inputWidth,
                    inputHeight,
                )
            COLOR_FormatYUV420Planar ->
                /**
                 * Executes encodeyuv420p operation with thermal imaging domain optimization.
                 *
                 */
                encodeYUV420P(
                    yuv,
                    argb,
                    inputWidth,
                    inputHeight,
                )
            COLOR_FormatYUV420PackedSemiPlanar ->
                /**
                 * Executes encodeyuv420psp operation with thermal imaging domain optimization.
                 *
                 */
                encodeYUV420PSP(
                    yuv,
                    argb,
                    inputWidth,
                    inputHeight,
                )
            COLOR_FormatYUV420PackedPlanar ->
                /**
                 * Executes encodeyuv420pp operation with thermal imaging domain optimization.
                 *
                 */
                encodeYUV420PP(
                    yuv,
                    argb,
                    inputWidth,
                    inputHeight,
                )
            else ->
                /**
                 * Executes encodeyuv420sp operation with thermal imaging domain optimization.
                 *
                 */
                encodeYUV420SP(
                    yuv,
                    argb,
                    inputWidth,
                    inputHeight,
                )
        }
        return yuv
    }

    /**
     * Executes encodeYUV420SP functionality.
     */
    /**
     * Executes encodeyuv420sp operation with thermal imaging domain optimization.
     *
     * @param
     * @param yuv420sp Parameter for operation (type: ByteArray)
     * @param argb Parameter for operation (type: IntArray)
     * @param width Parameter for operation (type: Int)
     * @param height Parameter for operation (type: Int)
     *
     */
    private fun encodeYUV420SP(
        yuv420sp: ByteArray,
        argb: IntArray,
        width: Int,
        height: Int,
    ) {
        val frameSize = width * height
        var yIndex = 0
        var uvIndex = frameSize
        var index = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (j in 0 until height) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until width) {
                // Val a = argb[index] and -0x1000000 shr 24
                val r = argb[index] and 0xff0000 shr 16
                val g = argb[index] and 0xff00 shr 8
                val b = argb[index] and 0xff shr 0
                val y = (66 * r + 129 * g + 25 * b + 128 shr 8) + 16
                val u = (112 * r - 94 * g - 18 * b + 128 shr 8) + 128
                val v = (-38 * r - 74 * g + 112 * b + 128 shr 8) + 128
                yuv420sp[yIndex++] =
                    (
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (y < 0) {
                            0
                        } else if (y > 255) {
                            255
                        } else {
                            y
                        }
                    ).toByte()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[uvIndex++] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (v < 0) {
                                0
                            } else if (v > 255) {
                                255
                            } else {
                                v
                            }
                        ).toByte()
                    yuv420sp[uvIndex++] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (u < 0) {
                                0
                            } else if (u > 255) {
                                255
                            } else {
                                u
                            }
                        ).toByte()
                }
                index++
            }
        }
    }

    /**
     * Executes encodeYUV420P functionality.
     */
    /**
     * Executes encodeyuv420p operation with thermal imaging domain optimization.
     *
     * @param
     * @param yuv420sp Parameter for operation (type: ByteArray)
     * @param argb Parameter for operation (type: IntArray)
     * @param width Parameter for operation (type: Int)
     * @param height Parameter for operation (type: Int)
     *
     */
    private fun encodeYUV420P(
        yuv420sp: ByteArray,
        argb: IntArray,
        width: Int,
        height: Int,
    ) {
        val frameSize = width * height
        var yIndex = 0
        var uIndex = frameSize
        var vIndex = frameSize + width * height / 4
        var index = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (j in 0 until height) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until width) {
                // Val a = argb[index] and -0x1000000 shr 24
                val r = argb[index] and 0xff0000 shr 16
                val g = argb[index] and 0xff00 shr 8
                val b = argb[index] and 0xff shr 0
                val y = (66 * r + 129 * g + 25 * b + 128 shr 8) + 16
                val u = (112 * r - 94 * g - 18 * b + 128 shr 8) + 128
                val v = (-38 * r - 74 * g + 112 * b + 128 shr 8) + 128
                yuv420sp[yIndex++] =
                    (
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (y < 0) {
                            0
                        } else if (y > 255) {
                            255
                        } else {
                            y
                        }
                    ).toByte()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[vIndex++] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (u < 0) {
                                0
                            } else if (u > 255) {
                                255
                            } else {
                                u
                            }
                        ).toByte()
                    yuv420sp[uIndex++] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (v < 0) {
                                0
                            } else if (v > 255) {
                                255
                            } else {
                                v
                            }
                        ).toByte()
                }
                index++
            }
        }
    }

    /**
     * Executes encodeYUV420PSP functionality.
     */
    /**
     * Executes encodeyuv420psp operation with thermal imaging domain optimization.
     *
     * @param
     * @param yuv420sp Parameter for operation (type: ByteArray)
     * @param argb Parameter for operation (type: IntArray)
     * @param width Parameter for operation (type: Int)
     * @param height Parameter for operation (type: Int)
     *
     */
    private fun encodeYUV420PSP(
        yuv420sp: ByteArray,
        argb: IntArray,
        width: Int,
        height: Int,
    ) {
        var yIndex = 0
        var index = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (j in 0 until height) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until width) {
                // Val a = argb[index] and -0x1000000 shr 24
                val r = argb[index] and 0xff0000 shr 16
                val g = argb[index] and 0xff00 shr 8
                val b = argb[index] and 0xff shr 0
                val y = (66 * r + 129 * g + 25 * b + 128 shr 8) + 16
                val u = (112 * r - 94 * g - 18 * b + 128 shr 8) + 128
                val v = (-38 * r - 74 * g + 112 * b + 128 shr 8) + 128
                yuv420sp[yIndex++] =
                    (
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (y < 0) {
                            0
                        } else if (y > 255) {
                            255
                        } else {
                            y
                        }
                    ).toByte()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[yIndex + 1] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (v < 0) {
                                0
                            } else if (v > 255) {
                                255
                            } else {
                                v
                            }
                        ).toByte()
                    yuv420sp[yIndex + 3] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (u < 0) {
                                0
                            } else if (u > 255) {
                                255
                            } else {
                                u
                            }
                        ).toByte()
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (index % 2 == 0) {
                    yIndex++
                }
                index++
            }
        }
    }

    /**
     * Executes encodeYUV420PP functionality.
     */
    /**
     * Executes encodeyuv420pp operation with thermal imaging domain optimization.
     *
     * @param
     * @param yuv420sp Parameter for operation (type: ByteArray)
     * @param argb Parameter for operation (type: IntArray)
     * @param width Parameter for operation (type: Int)
     * @param height Parameter for operation (type: Int)
     *
     */
    private fun encodeYUV420PP(
        yuv420sp: ByteArray,
        argb: IntArray,
        width: Int,
        height: Int,
    ) {
        var yIndex = 0
        var vIndex = yuv420sp.size / 2
        var index = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (j in 0 until height) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until width) {
                // Val a = argb[index] and -0x1000000 shr 24
                val r = argb[index] and 0xff0000 shr 16
                val g = argb[index] and 0xff00 shr 8
                val b = argb[index] and 0xff shr 0
                val y = (66 * r + 129 * g + 25 * b + 128 shr 8) + 16
                val u = (112 * r - 94 * g - 18 * b + 128 shr 8) + 128
                val v = (-38 * r - 74 * g + 112 * b + 128 shr 8) + 128
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (j % 2 == 0 && index % 2 == 0) { // 0
                    yuv420sp[yIndex++] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (y < 0) {
                                0
                            } else if (y > 255) {
                                255
                            } else {
                                y
                            }
                        ).toByte()
                    yuv420sp[yIndex + 1] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (v < 0) {
                                0
                            } else if (v > 255) {
                                255
                            } else {
                                v
                            }
                        ).toByte()
                    yuv420sp[vIndex + 1] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (u < 0) {
                                0
                            } else if (u > 255) {
                                255
                            } else {
                                u
                            }
                        ).toByte()
                    yIndex++
                } else if (j % 2 == 0 && index % 2 == 1) { // 1
                    yuv420sp[yIndex++] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (y < 0) {
                                0
                            } else if (y > 255) {
                                255
                            } else {
                                y
                            }
                        ).toByte()
                } else if (j % 2 == 1 && index % 2 == 0) { // 2
                    yuv420sp[vIndex++] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (y < 0) {
                                0
                            } else if (y > 255) {
                                255
                            } else {
                                y
                            }
                        ).toByte()
                    vIndex++
                } else if (j % 2 == 1 && index % 2 == 1) { // 3
                    yuv420sp[vIndex++] =
                        (
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (y < 0) {
                                0
                            } else if (y > 255) {
                                255
                            } else {
                                y
                            }
                        ).toByte()
                }
                index++
            }
        }
    }
}
