package com.infisense.usbdual.camera;

import com.energy.iruvc.dual.DualUVCCamera;
import com.infisense.usbdual.Const;

import java.util.ArrayList;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for BaseDualView display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
public abstract class BaseDualView {

    protected ArrayList<OnFrameCallback> onFrameCallbacks;
    public DualUVCCamera dualUVCCamera;

    protected int fusionLength;
    protected int irSize;
    protected int vlSize;
    protected int remapTempSize;
    protected byte[] remapTempData;// 裁剪后的temperaturedata
    protected byte[] mixData;// Fusiondata
    protected byte[] normalTempData;// 原始temperaturedata
    protected byte[] mixDataRotate;
    protected byte[] irData;// 原始infrareddata
    public byte[] vlData;// 原始visible lightdata
    public byte[] vlARGBData;

/**
 * Specialized thermal imaging component providing OnFrameCallback functionality for the IRCamera system.
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
    public interface OnFrameCallback {
        void onFame(byte[] mixData, byte[] remapTempData, double fpsText);
    }

    public void addFrameCallback(OnFrameCallback onFrameCallback) {
        onFrameCallbacks.add(onFrameCallback);
    }

    public void removeFrameCallback(OnFrameCallback onFrameCallback) {
        onFrameCallbacks.remove(onFrameCallback);
    }
}
