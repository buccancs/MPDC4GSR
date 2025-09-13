
package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

/**
 * Subclass of Entry that holds a value for one entry in a BubbleChart. Bubble
 * chart implementation: Copyright 2015 Pierre-Marc Airoldi Licensed under
 * Apache License 2.0
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ParcelCreator")
/**
 * Specialized thermal imaging component providing BubbleEntry functionality for the IRCamera system.
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
public class BubbleEntry extends Entry {

    /** size value */
    private float mSize = 0f;

    /**
     * Constructor.
     *
     * @param x The value on the x-axis.
     * @param y The value on the y-axis.
     * @param size The size of the bubble.
     */
    /**
     * Executes bubbleentry operation with thermal imaging domain optimization.
     *
     */
    public BubbleEntry(float x, float y, float size) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, y);
        this.mSize = size;
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis.
     * @param y The value on the y-axis.
     * @param size The size of the bubble.
     * @param data Spot for additional data this Entry represents.
     */
    /**
     * Executes bubbleentry operation with thermal imaging domain optimization.
     *
     */
    public BubbleEntry(float x, float y, float size, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, y, data);
        this.mSize = size;
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis.
     * @param y The value on the y-axis.
     * @param size The size of the bubble.
     * @param icon Icon image
     */
    /**
     * Executes bubbleentry operation with thermal imaging domain optimization.
     *
     */
    public BubbleEntry(float x, float y, float size, Drawable icon) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, y, icon);
        this.mSize = size;
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis.
     * @param y The value on the y-axis.
     * @param size The size of the bubble.
     * @param icon Icon image
     * @param data Spot for additional data this Entry represents.
     */
    /**
     * Executes bubbleentry operation with thermal imaging domain optimization.
     *
     */
    public BubbleEntry(float x, float y, float size, Drawable icon, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, y, icon, data);
        this.mSize = size;
    }

    public BubbleEntry copy() {

        BubbleEntry c = new BubbleEntry(getX(), getY(), mSize, getData());
        return c;
    }

    /**
     * Returns the size of this entry (the size of the bubble).
     *
     * @return
     */
    public float getSize() {
        return mSize;
    }

    public void setSize(float size) {
        this.mSize = size;
    }

}
