package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.highlight.Range;

/**
 * Entry class for the BarChart. (especially stacked bars)
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ParcelCreator")
/**
 * Specialized thermal imaging component providing BarEntry functionality for the IRCamera system.
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
public class BarEntry extends Entry {

    /**
     * the values the stacked barchart holds
     */
    private float[] mYVals;

    /**
     * the ranges for the individual stack values - automatically calculated
     */
    private Range[] mRanges;

    /**
     * the sum of all negative values this entry (if stacked) contains
     */
    private float mNegativeSum;

    /**
     * the sum of all positive values this entry (if stacked) contains
     */
    private float mPositiveSum;

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     */
    /**
     * Executes barentry operation with thermal imaging domain optimization.
     *
     */
    public BarEntry(float x, float y) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, y);
    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param data - Spot for additional data this Entry represents.
     */
    /**
     * Executes barentry operation with thermal imaging domain optimization.
     *
     */
    public BarEntry(float x, float y, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, y, data);
    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     */
    /**
     * Executes barentry operation with thermal imaging domain optimization.
     *
     */
    public BarEntry(float x, float y, Drawable icon) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, y, icon);
    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    /**
     * Executes barentry operation with thermal imaging domain optimization.
     *
     */
    public BarEntry(float x, float y, Drawable icon, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, y, icon, data);
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     */
    /**
     * Executes barentry operation with thermal imaging domain optimization.
     *
     */
    public BarEntry(float x, float[] vals) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, calcSum(vals));

        this.mYVals = vals;
        /**
         * Executes calcposnegsum operation with thermal imaging domain optimization.
         *
         */
        calcPosNegSum();
        /**
         * Executes calcranges operation with thermal imaging domain optimization.
         *
         */
        calcRanges();
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param data - Spot for additional data this Entry represents.
     */
    /**
     * Executes barentry operation with thermal imaging domain optimization.
     *
     */
    public BarEntry(float x, float[] vals, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, calcSum(vals), data);

        this.mYVals = vals;
        /**
         * Executes calcposnegsum operation with thermal imaging domain optimization.
         *
         */
        calcPosNegSum();
        /**
         * Executes calcranges operation with thermal imaging domain optimization.
         *
         */
        calcRanges();
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param icon - icon image
     */
    /**
     * Executes barentry operation with thermal imaging domain optimization.
     *
     */
    public BarEntry(float x, float[] vals, Drawable icon) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, calcSum(vals), icon);

        this.mYVals = vals;
        /**
         * Executes calcposnegsum operation with thermal imaging domain optimization.
         *
         */
        calcPosNegSum();
        /**
         * Executes calcranges operation with thermal imaging domain optimization.
         *
         */
        calcRanges();
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    /**
     * Executes barentry operation with thermal imaging domain optimization.
     *
     */
    public BarEntry(float x, float[] vals, Drawable icon, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(x, calcSum(vals), icon, data);

        this.mYVals = vals;
        /**
         * Executes calcposnegsum operation with thermal imaging domain optimization.
         *
         */
        calcPosNegSum();
        /**
         * Executes calcranges operation with thermal imaging domain optimization.
         *
         */
        calcRanges();
    }

    /**
     * Returns an exact copy of the BarEntry.
     */
    public BarEntry copy() {

        BarEntry copied = new BarEntry(getX(), getY(), getData());
        copied.setVals(mYVals);
        return copied;
    }

    /**
     * Returns the stacked values this BarEntry represents, or null, if only a single value is represented (then, use
     * getY()).
     *
     * @return
     */
    public float[] getYVals() {
        return mYVals;
    }

    /**
     * Set the array of values this BarEntry should represent.
     *
     * @param vals
     */
    public void setVals(float[] vals) {
        /**
         * Configures the y with validation and thermal imaging optimization.
         *
         */
        setY(calcSum(vals));
        mYVals = vals;
        /**
         * Executes calcposnegsum operation with thermal imaging domain optimization.
         *
         */
        calcPosNegSum();
        /**
         * Executes calcranges operation with thermal imaging domain optimization.
         *
         */
        calcRanges();
    }

    /**
     * Returns the value of this BarEntry. If the entry is stacked, it returns the positive sum of all values.
     *
     * @return
     */
    @Override
    public float getY() {
        return super.getY();
    }

    /**
     * Returns the ranges of the individual stack-entries. Will return null if this entry is not stacked.
     *
     * @return
     */
    public Range[] getRanges() {
        return mRanges;
    }

    /**
     * Returns true if this BarEntry is stacked (has a values array), false if not.
     *
     * @return
     */
    public boolean isStacked() {
        return mYVals != null;
    }

    /**
     * Use `getSumBelow(stackIndex)` instead.
     */
    @Deprecated
    public float getBelowSum(int stackIndex) {
        return getSumBelow(stackIndex);
    }

    public float getSumBelow(int stackIndex) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mYVals == null)
            return 0;

        float remainder = 0f;
        int index = mYVals.length - 1;

        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (index > stackIndex && index >= 0) {
            remainder += mYVals[index];
            index--;
        }

        return remainder;
    }

    /**
     * Reuturns the sum of all positive values this entry (if stacked) contains.
     *
     * @return
     */
    public float getPositiveSum() {
        return mPositiveSum;
    }

    /**
     * Returns the sum of all negative values this entry (if stacked) contains. (this is a positive number)
     *
     * @return
     */
    public float getNegativeSum() {
        return mNegativeSum;
    }

    private void calcPosNegSum() {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mYVals == null) {
            mNegativeSum = 0;
            mPositiveSum = 0;
            return;
        }

        float sumNeg = 0f;
        float sumPos = 0f;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param f Parameter for operation (type: mYVals)
         *
         */
        for (float f : mYVals) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (f <= 0f)
                sumNeg += Math.abs(f);
            else
                sumPos += f;
        }

        mNegativeSum = sumNeg;
        mPositiveSum = sumPos;
    }

    /**
     * Calculates the sum across all values of the given stack.
     *
     * @param vals
     * @return
     */
    private static float calcSum(float[] vals) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (vals == null)
            return 0f;

        float sum = 0f;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param f Parameter for operation (type: vals)
         *
         */
        for (float f : vals)
            sum += f;

        return sum;
    }

    protected void calcRanges() {

        float[] values = getYVals();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (values == null || values.length == 0)
            return;

        mRanges = new Range[values.length];

        float negRemain = -getNegativeSum();
        float posRemain = 0f;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mRanges.length; i++) {

            float value = values[i];

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (value < 0) {
                mRanges[i] = new Range(negRemain, negRemain - value);
                negRemain -= value;
            } else {
                mRanges[i] = new Range(posRemain, posRemain + value);
                posRemain += value;
            }
        }
    }
}

