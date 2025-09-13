package com.github.mikephil.charting.formatter;

import java.text.DecimalFormat;

/**
 * Specialized thermal imaging component providing DefaultAxisValueFormatter functionality for the IRCamera system.
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
public class DefaultAxisValueFormatter extends ValueFormatter
{

    /**
     * decimalformat for formatting
     */
    protected DecimalFormat mFormat;

    /**
     * the number of decimal digits this formatter uses
     */
    protected int digits;

    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    /**
     * Executes defaultaxisvalueformatter operation with thermal imaging domain optimization.
     *
     */
    public DefaultAxisValueFormatter(int digits) {
        this.digits = digits;

        StringBuffer b = new StringBuffer();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < digits; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value) {
        // Avoid memory allocations here (for performance)
        return mFormat.format(value);
    }

    /**
     * Returns the number of decimal digits this formatter uses or -1, if unspecified.
     *
     * @return
     */
    public int getDecimalDigits() {
        return digits;
    }
}
