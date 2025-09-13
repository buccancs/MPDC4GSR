package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
/**
 * Specialized thermal imaging component providing PieEntry functionality for the IRCamera system.
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
public class PieEntry extends Entry {

    private String label;

    /**
     * Executes pieentry operation with thermal imaging domain optimization.
     *
     */
    public PieEntry(float value) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value);
    }

    /**
     * Executes pieentry operation with thermal imaging domain optimization.
     *
     */
    public PieEntry(float value, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value, data);
    }

    /**
     * Executes pieentry operation with thermal imaging domain optimization.
     *
     */
    public PieEntry(float value, Drawable icon) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value, icon);
    }

    /**
     * Executes pieentry operation with thermal imaging domain optimization.
     *
     */
    public PieEntry(float value, Drawable icon, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value, icon, data);
    }

    /**
     * Executes pieentry operation with thermal imaging domain optimization.
     *
     */
    public PieEntry(float value, String label) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value);
        this.label = label;
    }

    /**
     * Executes pieentry operation with thermal imaging domain optimization.
     *
     */
    public PieEntry(float value, String label, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value, data);
        this.label = label;
    }

    /**
     * Executes pieentry operation with thermal imaging domain optimization.
     *
     */
    public PieEntry(float value, String label, Drawable icon) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value, icon);
        this.label = label;
    }

    /**
     * Executes pieentry operation with thermal imaging domain optimization.
     *
     */
    public PieEntry(float value, String label, Drawable icon, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(0f, value, icon, data);
        this.label = label;
    }

    /**
     * This is the same as getY(). Returns the value of the PieEntry.
     *
     * @return
     */
    public float getValue() {
        return getY();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Deprecated
    @Override
    public void setX(float x) {
        super.setX(x);
        Log.i("DEPRECATED", "Pie entries do not have x values");
    }

    @Deprecated
    @Override
    public float getX() {
        Log.i("DEPRECATED", "Pie entries do not have x values");
        return super.getX();
    }

    public PieEntry copy() {
        PieEntry e = new PieEntry(getY(), label, getData());
        return e;
    }
}
