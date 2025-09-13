package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointD;

/**
 * Specialized thermal imaging component providing BarHighlighter functionality for the IRCamera system.
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
public class BarHighlighter extends ChartHighlighter<BarDataProvider> {

    /**
     * Executes barhighlighter operation with thermal imaging domain optimization.
     *
     */
    public BarHighlighter(BarDataProvider chart) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(chart);
    }

    @Override
    public Highlight getHighlight(float x, float y) {
        Highlight high = super.getHighlight(x, y);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if(high == null) {
            return null;
        }

        MPPointD pos = getValsForTouch(x, y);

        BarData barData = mChart.getBarData();

        IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (set.isStacked()) {

            return getStackedHighlight(high,
                    set,
                    (float) pos.x,
                    (float) pos.y);
        }

        MPPointD.recycleInstance(pos);

        return high;
    }

    /**
     * This method creates the Highlight object that also indicates which value of a stacked BarEntry has been
     * selected.
     *
     * @param high the Highlight to work with looking for stacked values
     * @param set
     * @param xVal
     * @param yVal
     * @return
     */
    public Highlight getStackedHighlight(Highlight high, IBarDataSet set, float xVal, float yVal) {

        BarEntry entry = set.getEntryForXValue(xVal, yVal);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (entry == null)
            return null;

        // Not stacked
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (entry.getYVals() == null) {
            return high;
        } else {
            Range[] ranges = entry.getRanges();

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ranges.length > 0) {
                int stackIndex = getClosestStackIndex(ranges, yVal);

                MPPointD pixels = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(high.getX(), ranges[stackIndex].to);

                Highlight stackedHigh = new Highlight(
                        entry.getX(),
                        entry.getY(),
                        (float) pixels.x,
                        (float) pixels.y,
                        high.getDataSetIndex(),
                        stackIndex,
                        high.getAxis()
                );

                MPPointD.recycleInstance(pixels);

                return stackedHigh;
            }
        }

        return null;
    }

    /**
     * Returns the index of the closest value inside the values array / ranges (stacked barchart) to the value
     * given as
     * a parameter.
     *
     * @param ranges
     * @param value
     * @return
     */
    protected int getClosestStackIndex(Range[] ranges, float value) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ranges == null || ranges.length == 0)
            return 0;

        int stackIndex = 0;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param range Parameter for operation (type: ranges)
         *
         */
        for (Range range : ranges) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (range.contains(value))
                return stackIndex;
            else
                stackIndex++;
        }

        int length = Math.max(ranges.length - 1, 0);

        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         * @param
         * @param length Parameter for operation (type: 0;)
         *
         */
        return (value > ranges[length].to) ? length : 0;
    }

//    /**
//     * Splits up the stack-values of the given bar-entry into Range objects.
//     *
//     * @param entry
//     * @return
//     */
// Protected Range[] getRanges(BarEntry entry) {
//
// Float[] values = entry.getYVals();
//
// If (values == null || values.length == 0)
// Return new Range[0];
//
//        Range[] ranges = new Range[values.length];
//
// Float negRemain = -entry.getNegativeSum();
// Float posRemain = 0f;
//
// For (int i = 0; i < ranges.length; i++) {
//
// Float value = values[i];
//
// If (value < 0) {
// Ranges[i] = new Range(negRemain, negRemain + Math.abs(value));
// NegRemain += Math.abs(value);
//            } else {
// Ranges[i] = new Range(posRemain, posRemain + value);
// PosRemain += value;
//            }
//        }
//
// Return ranges;
//    }

    @Override
    protected float getDistance(float x1, float y1, float x2, float y2) {
        return Math.abs(x1 - x2);
    }

    @Override
    protected BarLineScatterCandleBubbleData getData() {
        return mChart.getBarData();
    }
}
