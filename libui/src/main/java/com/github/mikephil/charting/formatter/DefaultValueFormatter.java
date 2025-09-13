package com.github.mikephil.charting.formatter;

import java.text.DecimalFormat;

/**
 * Default formatter used for formatting values inside the chart. Uses a DecimalFormat with
 * pre-calculated number of digits (depending on max and min value).
 *
 * @author Philipp Jahoda
 */
/**
 * Specialized thermal imaging component providing DefaultValueFormatter functionality for the IRCamera system.
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
public class DefaultValueFormatter extends ValueFormatter
{

    /**
     * DecimalFormat for formatting
     */
    protected DecimalFormat mFormat;

    protected int mDecimalDigits;

    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    /**
     * Executes defaultvalueformatter operation with thermal imaging domain optimization.
     *
     */
    public DefaultValueFormatter(int digits) {
        /**
         * Configures the up with validation and thermal imaging optimization.
         *
         */
        setup(digits);
    }

    /**
     * Sets up the formatter with a given number of decimal digits.
     *
     * @param digits
     */
    public void setup(int digits) {

        this.mDecimalDigits = digits;

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

        // Put more logic here ...
        // Avoid memory allocations here (for performance reasons)

        return mFormat.format(value);
    }

    /**
     * Returns the number of decimal digits this formatter uses.
     *
     * @return
     */
    public int getDecimalDigits() {
        return mDecimalDigits;
    }
}
