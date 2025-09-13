package com.example.thermal_lite;

/**
 * Specialized thermal imaging component providing IrConst functionality for the IRCamera system.
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
public class IrConst {

default的出图data，可在“USB基础info”中modify
    public static final int DEFAULT_STREAM_WIDTH = 256;
    public static final int DEFAULT_STREAM_HEIGHT = 386;
    public static final int DEFAULT_STREAM_FPS = 25;
    public static final float DEFAULT_STREAM_BANDWIDTH = 1.0f;
    public static final boolean DEFAULT_DOUBLE_IMAGE = true;

    public static final String KEY_DEFAULT_STREAM_WIDTH = "KEY_DEFAULT_STREAM_WIDTH";
    public static final String KEY_DEFAULT_STREAM_HEIGHT = "KEY_DEFAULT_STREAM_HEIGHT";
    public static final String KEY_DEFAULT_STREAM_FPS = "KEY_DEFAULT_STREAM_FPS";
    public static final String KEY_DEFAULT_STREAM_BANDWIDTH = "KEY_DEFAULT_STREAM_BANDWIDTH";
    public static final String KEY_DEFAULT_DOUBLE_IMAGE = "KEY_DEFAULT_DOUBLE_IMAGE";
统一modify当前load的距离修正表
    public static final String TAU_HIGH_GAIN_ASSET_PATH = "lite/highF.bin";
    public static final String TAU_LOW_GAIN_ASSET_PATH = "lite/lowF.bin";

}
