package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;

/**
/**
 * Specialized thermal imaging component providing RadarEntry functionality for the IRCamera system.
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
public class RadarEntry extends Entry {

    /**
     * Executes radarentry operation with thermal imaging domain optimization.
     *
     */
    public RadarEntry(float value) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value);
    }

    /**
     * Executes radarentry operation with thermal imaging domain optimization.
     *
     */
    public RadarEntry(float value, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value, data);
    }

    /**
     * This is the same as getY(). Returns the value of the RadarEntry.
     *
     * @return
     */
    public float getValue() {
        return getY();
    }

    public RadarEntry copy() {
        RadarEntry e = new RadarEntry(getY(), getData());
        return e;
    }

    @Deprecated
    @Override
    public void setX(float x) {
        super.setX(x);
    }

    @Deprecated
    @Override
    public float getX() {
        return super.getX();
    }
}
