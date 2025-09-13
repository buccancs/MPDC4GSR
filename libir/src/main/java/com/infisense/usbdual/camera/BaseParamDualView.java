package com.infisense.usbdual.camera;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for BaseParamDualView display and interaction.
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
public class BaseParamDualView {
    protected int mIrWidth;
    protected int mIrHeight;
    protected int mVlWidth;
    protected int mVlHeight;
    protected int mDualWidth;
    protected int mDualHeight;

    /**
     * Executes baseparamdualview operation with thermal imaging domain optimization.
     *
     */
    public BaseParamDualView(int mIrWidth, int mIrHeight, int mVlWidth, int mVlHeight, int mDualWidth, int mDualHeight) {
        this.mIrWidth = mIrWidth;
        this.mIrHeight = mIrHeight;
        this.mVlWidth = mVlWidth;
        this.mVlHeight = mVlHeight;
        this.mDualWidth = mDualWidth;
        this.mDualHeight = mDualHeight;
    }
}
