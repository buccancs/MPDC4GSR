package com.infisense.usbir.utils;

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for HexUtils operations.
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
public class HexUtils {

    /**
     * 将字节arrayconversion成十六进制的字符串
     * @return
     */
    public static String binaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param b Parameter for operation (type: bytes)
         *
         */
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex + " ";
        }
        return result;
    }
}
