package com.example.connectlisten;

import com.topdon.lib.core.so.algorithm;

/**
 * Specialized thermal imaging component providing JNITest functionality for the IRCamera system.
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
public class JNITest {
    static {
        System.loadLibrary("opencv_java4");
//        System.loadLibrary("SRImage");
//        System.loadLibrary("minMaxTemperatureDetect");
    }

    public static byte[] maxTempL(byte[] imgBytes,byte[] tempByte,int width,int height) {
        return  algorithm.maxTempL(imgBytes, tempByte,width,height);
    }
    public static byte[] lowTemTrack(byte[] imgBytes,byte[] tempByte,int width,int height) {
        return  algorithm.lowTemTrack(imgBytes, tempByte,width,height);
    }
}
