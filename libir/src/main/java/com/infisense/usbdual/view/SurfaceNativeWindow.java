package com.infisense.usbdual.view;

/*
 * @Description: 使用GPU绘制，减少CPU和memory占用，可以自定义
 * @Author:         brilliantzhao
 * @CreateDate:     2022.9.8 10:26
 * @UpdateUser:
 * @UpdateDate:     2022.9.8 10:26
 * @UpdateRemark:
 */
/**
 * Specialized thermal imaging component providing SurfaceNativeWindow functionality for the IRCamera system.
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
public class SurfaceNativeWindow {

    static {
        System.loadLibrary("native-window");
    }

    public native void onCreateSurface(Object surface, int width, int height);

    public native void onDrawFrame(byte[] ARGBdata, int width, int height);

    public native void onReleaseSurface();

    public native void drawBitmap(Object surface, Object bitmap);
}
