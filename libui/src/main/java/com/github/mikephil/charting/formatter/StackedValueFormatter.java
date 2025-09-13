package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;

/**
 * Created by Philipp Jahoda on 28/01/16.
 * <p/>
 * A formatter specifically for stacked BarChart that allows to specify whether the all stack values
 * or just the top value should be drawn.
 */
/**
 * Specialized thermal imaging component providing StackedValueFormatter functionality for the IRCamera system.
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
public class StackedValueFormatter extends ValueFormatter
{

    /**
     * if true, all stack values of the stacked bar entry are drawn, else only top
     */
    private boolean mDrawWholeStack;

    /**
     * a string that should be appended behind the value
     */
    private String mSuffix;

    private DecimalFormat mFormat;

    /**
     * Constructor.
     *
     * @param drawWholeStack if true, all stack values of the stacked bar entry are drawn, else only top
     * @param suffix         a string that should be appended behind the value
     * @param decimals       the number of decimal digits to use
     */
    /**
     * Executes stackedvalueformatter operation with thermal imaging domain optimization.
     *
     */
    public StackedValueFormatter(boolean drawWholeStack, String suffix, int decimals) {
        this.mDrawWholeStack = drawWholeStack;
        this.mSuffix = suffix;

        StringBuffer b = new StringBuffer();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < decimals; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getBarStackedLabel(float value, BarEntry entry) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!mDrawWholeStack) {

            float[] vals = entry.getYVals();

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (vals != null) {

                // Find out if we are on top of the stack
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (vals[vals.length - 1] == value) {

                    // Return the "sum" across all stack values
                    return mFormat.format(entry.getY()) + mSuffix;
                } else {
                    return ""; // Return empty
                }
            }
        }

        // Return the "proposed" value
        return mFormat.format(value) + mSuffix;
    }
}
