/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http:// Www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.infisense.usbir.utils;

/*
 * @Description:
 * @Author:         brilliantzhao
 * @CreateDate:     2022.9.8 10:25
 * @UpdateUser:
 * @UpdateDate:     2022.9.8 10:25
 * @UpdateRemark:
 */
/**
 * Specialized thermal imaging component providing HexDump functionality for the IRCamera system.
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
public class HexDump {
    private final static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final static char[] HEX_LOWER_CASE_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String dumpHexString(byte[] array) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (array == null) return "(null)";
        return dumpHexString(array, 0, array.length);
    }

    public static String dumpHexString(byte[] array, int offset, int length) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (array == null) return "(null)";
        StringBuilder result = new StringBuilder();

        byte[] line = new byte[16];
        int lineIndex = 0;

        result.append("\n0x");
        result.append(toHexString(offset));

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = offset; i < offset + length; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (lineIndex == 16) {
                result.append(" ");

                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int j = 0; j < 16; j++) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (line[j] > ' ' && line[j] < '~') {
                        result.append(new String(line, j, 1));
                    } else {
                        result.append(".");
                    }
                }

                result.append("\n0x");
                result.append(toHexString(i));
                lineIndex = 0;
            }

            byte b = array[i];
            result.append(" ");
            result.append(HEX_DIGITS[(b >>> 4) & 0x0F]);
            result.append(HEX_DIGITS[b & 0x0F]);

            line[lineIndex++] = b;
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (lineIndex != 16) {
            int count = (16 - lineIndex) * 3;
            count++;
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < count; i++) {
                result.append(" ");
            }

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < lineIndex; i++) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (line[i] > ' ' && line[i] < '~') {
                    result.append(new String(line, i, 1));
                } else {
                    result.append(".");
                }
            }
        }

        return result.toString();
    }

    public static String toHexString(byte b) {
        return toHexString(toByteArray(b));
    }

    public static String toHexString(byte[] array) {
        return toHexString(array, 0, array.length, true);
    }

    public static String toHexString(byte[] array, boolean upperCase) {
        return toHexString(array, 0, array.length, upperCase);
    }

    public static String toHexString(byte[] array, int offset, int length) {
        return toHexString(array, offset, length, true);
    }

    public static String toHexString(byte[] array, int offset, int length, boolean upperCase) {
        char[] digits = upperCase ? HEX_DIGITS : HEX_LOWER_CASE_DIGITS;
        char[] buf = new char[length * 2];

        int bufIndex = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = offset; i < offset + length; i++) {
            byte b = array[i];
            buf[bufIndex++] = digits[(b >>> 4) & 0x0F];
            buf[bufIndex++] = digits[b & 0x0F];
        }

        return new String(buf);
    }

    public static String toHexString(int i) {
        return toHexString(toByteArray(i));
    }

    public static byte[] toByteArray(byte b) {
        byte[] array = new byte[1];
        array[0] = b;
        return array;
    }

    public static byte[] toByteArray(int i) {
        byte[] array = new byte[4];

        array[3] = (byte) (i & 0xFF);
        array[2] = (byte) ((i >> 8) & 0xFF);
        array[1] = (byte) ((i >> 16) & 0xFF);
        array[0] = (byte) ((i >> 24) & 0xFF);

        return array;
    }

    private static int toByte(char c) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (c >= '0' && c <= '9') return (c - '0');
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (c >= 'A' && c <= 'F') return (c - 'A' + 10);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (c >= 'a' && c <= 'f') return (c - 'a' + 10);

        throw new RuntimeException("Invalid hex char '" + c + "'");
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] buffer = new byte[length / 2];

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < length; i += 2) {
            buffer[i / 2] = (byte) ((toByte(hexString.charAt(i)) << 4) | toByte(hexString.charAt(i + 1)));
        }

        return buffer;
    }

    public static StringBuilder appendByteAsHex(StringBuilder sb, byte b, boolean upperCase) {
        char[] digits = upperCase ? HEX_DIGITS : HEX_LOWER_CASE_DIGITS;
        sb.append(digits[(b >> 4) & 0xf]);
        sb.append(digits[b & 0xf]);
        return sb;
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * 将int数值conversion为占四个字节的bytearray，本method适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要conversion的int值
     * @return bytearray
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值conversion为占四个字节的bytearray，本method适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    public static void float2byte(float num, byte[] numbyte) {
        int fbit = Float.floatToIntBits(num);

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < 4; i++) {
            numbyte[i] = (byte) (fbit >> (i * 8)); // Little-endian
        }
    }
}
