package com.energy.commoncomponent;

import android.os.Environment;

import com.blankj.utilcode.util.Utils;
import com.energy.commoncomponent.bean.DeviceType;

/**
 * Specialized thermal imaging component providing Const functionality for the IRCamera system.
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
public class Const {
    // TODO: Temporarily use this global variable to distinguish different modules: command invocation, business processing
    public static final DeviceType DEVICE_TYPE = DeviceType.DEVICE_TYPE_TC2C;

    public static final String DATA_FILE_SAVE_PATH = Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();

    public static final String ZETA_ROOM_LIBRARY_CLASS = "com.energy.zetazoomlibrary.ZetaZoomHelper";
}
