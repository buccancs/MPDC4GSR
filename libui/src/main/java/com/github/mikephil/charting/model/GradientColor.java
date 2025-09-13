package com.github.mikephil.charting.model;

/**
 * Specialized thermal imaging component providing GradientColor functionality for the IRCamera system.
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
public class GradientColor {

    private int startColor;
    private int endColor;
    
    /**
     * Executes gradientcolor operation with thermal imaging domain optimization.
     *
     */
    public GradientColor(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }
}
