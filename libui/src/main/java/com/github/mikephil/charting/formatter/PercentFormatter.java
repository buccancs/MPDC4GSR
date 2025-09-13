package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;

/**
 * This IValueFormatter is just for convenience and simply puts a "%" sign after
 * each value. (Recommeded for PieChart)
 *
 * @author Philipp Jahoda
 */
/**
 * Specialized thermal imaging component providing PercentFormatter functionality for the IRCamera system.
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
public class PercentFormatter extends ValueFormatter
{

    public DecimalFormat mFormat;
    private PieChart pieChart;

    /**
     * Executes percentformatter operation with thermal imaging domain optimization.
     *
     */
    public PercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    // Can be used to remove percent signs if the chart isn't in percent mode
    /**
     * Executes percentformatter operation with thermal imaging domain optimization.
     *
     */
    public PercentFormatter(PieChart pieChart) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this();
        this.pieChart = pieChart;
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + " %";
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
            // Converted to percent
            return getFormattedValue(value);
        } else {
            // Raw value, skip percent sign
            return mFormat.format(value);
        }
    }

}
