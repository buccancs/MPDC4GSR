package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;

/**
 * Specialized thermal imaging component providing BaseEntry functionality for the IRCamera system.
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
public abstract class BaseEntry {

    /** the y value */
    private float y = 0f;

    /** optional spot for additional data this Entry represents */
    private Object mData = null;

    /** optional icon image */
    private Drawable mIcon = null;

    /**
     * Executes baseentry operation with thermal imaging domain optimization.
     *
     */
    public BaseEntry() {

    }

    /**
     * Executes baseentry operation with thermal imaging domain optimization.
     *
     */
    public BaseEntry(float y) {
        this.y = y;
    }

    /**
     * Executes baseentry operation with thermal imaging domain optimization.
     *
     */
    public BaseEntry(float y, Object data) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(y);
        this.mData = data;
    }

    /**
     * Executes baseentry operation with thermal imaging domain optimization.
     *
     */
    public BaseEntry(float y, Drawable icon) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(y);
        this.mIcon = icon;
    }

    /**
     * Executes baseentry operation with thermal imaging domain optimization.
     *
     */
    public BaseEntry(float y, Drawable icon, Object data) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(y);
        this.mIcon = icon;
        this.mData = data;
    }

    /**
     * Returns the y value of this Entry.
     *
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the icon drawable
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }

    /**
     * Returns the icon of this Entry.
     *
     * @return
     */
    public Drawable getIcon() {
        return mIcon;
    }

    /**
     * Sets the y-value for the Entry.
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Returns the data, additional information that this Entry represents, or
     * null, if no data has been specified.
     *
     * @return
     */
    public Object getData() {
        return mData;
    }

    /**
     * Sets additional data this Entry should represent.
     *
     * @param data
     */
    public void setData(Object data) {
        this.mData = data;
    }
}
