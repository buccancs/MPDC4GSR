package com.example.thermal_lite.ui.activity;

/**
 * Specialized thermal imaging component providing IrDisplayActivity functionality for the IRCamera system.
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
public class IrDisplayActivity{
    public static final int SHOW_LOADING = 1001;
    public static final int HIDE_LOADING = 1002;
    public static final int HANDLE_INIT_FAIL = 1003;
    public static final int HANDLE_SHOW_TOAST = 1004;
    public static final int PREVIEW_FAIL = 1005;
    public static final int HANDLE_SHOW_FPS = 1006;
    public static final int HANDLE_SHOW_SUN_PROTECT_FLAG = 1007;
}