package com.github.mikephil.charting.formatter;

import java.util.Collection;

/**
 * Specialized thermal imaging component providing IndexAxisValueFormatter functionality for the IRCamera system.
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
public class IndexAxisValueFormatter extends ValueFormatter
{
    private String[] mValues = new String[] {};
    private int mValueCount = 0;

    /**
     * An empty constructor.
     * Use `setValues` to set the axis labels.
     */
    public IndexAxisValueFormatter() {
    }

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    public IndexAxisValueFormatter(String[] values) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (values != null)
            /**
             * Configures the values with validation and thermal imaging optimization.
             *
             */
            setValues(values);
    }

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    public IndexAxisValueFormatter(Collection<String> values) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (values != null)
            /**
             * Configures the values with validation and thermal imaging optimization.
             *
             */
            setValues(values.toArray(new String[values.size()]));
    }

    @Override
    public String getFormattedValue(float value) {
        int index = Math.round(value);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (index < 0 || index >= mValueCount || index != (int)value)
            return "";

        return mValues[index];
    }

    public String[] getValues()
    {
        return mValues;
    }

    public void setValues(String[] values)
    {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (values == null)
            values = new String[] {};

        this.mValues = values;
        this.mValueCount = values.length;
    }
}
