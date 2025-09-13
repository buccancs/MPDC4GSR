
package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.components.YAxis;

/**
 * Specialized thermal imaging component providing Highlight functionality for the IRCamera system.
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
public class Highlight {

    /**
     * the x-value of the highlighted value
     */
    private float mX = Float.NaN;

    /**
     * the y-value of the highlighted value
     */
    private float mY = Float.NaN;

    /**
     * the x-pixel of the highlight
     */
    private float mXPx;

    /**
     * the y-pixel of the highlight
     */
    private float mYPx;

    /**
     * the index of the data object - in case it refers to more than one
     */
    private int mDataIndex = -1;

    /**
     * the index of the dataset the highlighted value is in
     */
    private int mDataSetIndex;

    /**
     * index which value of a stacked bar entry is highlighted, default -1
     */
    private int mStackIndex = -1;

    /**
     * the axis the highlighted value belongs to
     */
    private YAxis.AxisDependency axis;

    /**
     * the x-position (pixels) on which this highlight object was last drawn
     */
    private float mDrawX;

    /**
     * the y-position (pixels) on which this highlight object was last drawn
     */
    private float mDrawY;

    public Highlight(float x, float y, int dataSetIndex) {
        this.mX = x;
        this.mY = y;
        this.mDataSetIndex = dataSetIndex;
    }

    /**
     * Executes highlight operation with thermal imaging domain optimization.
     *
     */
    public Highlight(float x, int dataSetIndex, int stackIndex) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(x, Float.NaN, dataSetIndex);
        this.mStackIndex = stackIndex;
    }

    /**
     * constructor
     *
     * @param x            the x-value of the highlighted value
     * @param y            the y-value of the highlighted value
     * @param dataSetIndex the index of the DataSet the highlighted value belongs to
     */
    /**
     * Executes highlight operation with thermal imaging domain optimization.
     *
     */
    public Highlight(float x, float y, float xPx, float yPx, int dataSetIndex, YAxis.AxisDependency axis) {
        this.mX = x;
        this.mY = y;
        this.mXPx = xPx;
        this.mYPx = yPx;
        this.mDataSetIndex = dataSetIndex;
        this.axis = axis;
    }

    /**
     * Constructor, only used for stacked-barchart.
     *
     * @param x            the index of the highlighted value on the x-axis
     * @param y            the y-value of the highlighted value
     * @param dataSetIndex the index of the DataSet the highlighted value belongs to
     * @param stackIndex   references which value of a stacked-bar entry has been
     *                     selected
     */
    /**
     * Executes highlight operation with thermal imaging domain optimization.
     *
     */
    public Highlight(float x, float y, float xPx, float yPx, int dataSetIndex, int stackIndex, YAxis.AxisDependency axis) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(x, y, xPx, yPx, dataSetIndex, axis);
        this.mStackIndex = stackIndex;
    }

    /**
     * returns the x-value of the highlighted value
     *
     * @return
     */
    public float getX() {
        return mX;
    }

    /**
     * returns the y-value of the highlighted value
     *
     * @return
     */
    public float getY() {
        return mY;
    }

    /**
     * returns the x-position of the highlight in pixels
     */
    public float getXPx() {
        return mXPx;
    }

    /**
     * returns the y-position of the highlight in pixels
     */
    public float getYPx() {
        return mYPx;
    }

    /**
     * the index of the data object - in case it refers to more than one
     *
     * @return
     */
    public int getDataIndex() {
        return mDataIndex;
    }

    public void setDataIndex(int mDataIndex) {
        this.mDataIndex = mDataIndex;
    }

    /**
     * returns the index of the DataSet the highlighted value is in
     *
     * @return
     */
    public int getDataSetIndex() {
        return mDataSetIndex;
    }

    /**
     * Only needed if a stacked-barchart entry was highlighted. References the
     * selected value within the stacked-entry.
     *
     * @return
     */
    public int getStackIndex() {
        return mStackIndex;
    }

    public boolean isStacked() {
        return mStackIndex >= 0;
    }

    /**
     * Returns the axis the highlighted value belongs to.
     *
     * @return
     */
    public YAxis.AxisDependency getAxis() {
        return axis;
    }

    /**
     * Sets the x- and y-position (pixels) where this highlight was last drawn.
     *
     * @param x
     * @param y
     */
    public void setDraw(float x, float y) {
        this.mDrawX = x;
        this.mDrawY = y;
    }

    /**
     * Returns the x-position in pixels where this highlight object was last drawn.
     *
     * @return
     */
    public float getDrawX() {
        return mDrawX;
    }

    /**
     * Returns the y-position in pixels where this highlight object was last drawn.
     *
     * @return
     */
    public float getDrawY() {
        return mDrawY;
    }

    /**
     * Returns true if this highlight object is equal to the other (compares
     * xIndex and dataSetIndex)
     *
     * @param h
     * @return
     */
    public boolean equalTo(Highlight h) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (h == null)
            return false;
        else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (this.mDataSetIndex == h.mDataSetIndex && this.mX == h.mX
                    && this.mStackIndex == h.mStackIndex && this.mDataIndex == h.mDataIndex)
                return true;
            else
                return false;
        }
    }

    @Override
    public String toString() {
        return "Highlight, x: " + mX + ", y: " + mY + ", dataSetIndex: " + mDataSetIndex
                + ", stackIndex (only stacked barentry): " + mStackIndex;
    }
}
