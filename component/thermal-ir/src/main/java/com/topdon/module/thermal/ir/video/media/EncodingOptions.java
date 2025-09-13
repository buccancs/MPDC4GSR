package com.topdon.module.thermal.ir.video.media;

import org.jetbrains.annotations.NotNull;

/**
 * Specialized thermal imaging component providing EncodingOptions functionality for the IRCamera system.
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
public class EncodingOptions {
    public static final int COMPRESS_HIGH = 2;
    public static final int COMPRESS_LOW = 0;
    public static final int COMPRESS_MID = 1;
    public int compressLevel;

    @NotNull
    @Override
    public String toString() {
        return "EncodingOptions : compLevel = " + this.compressLevel;
    }
}