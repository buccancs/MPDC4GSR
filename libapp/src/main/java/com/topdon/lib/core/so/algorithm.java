package com.topdon.lib.core.so;

/**
 * Specialized thermal imaging component providing algorithm functionality for the IRCamera system.
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
public class algorithm {
    public static native byte[] AdjustPhoto(String strFilePath,byte[] bytes);
    public static native byte[] maxTempL(byte[] imgBytes,byte[] tempByte,int width,int height);
    public static native byte[] lowTemTrack(byte[] imgBytes,byte[] tempByte,int width,int height);

}
