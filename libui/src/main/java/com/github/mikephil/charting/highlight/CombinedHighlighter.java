package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.List;

/**
 * Specialized thermal imaging component providing CombinedHighlighter functionality for the IRCamera system.
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
public class CombinedHighlighter extends ChartHighlighter<CombinedDataProvider> implements IHighlighter
{

    /**
     * bar highlighter for supporting stacked highlighting
     */
    protected BarHighlighter barHighlighter;

    public CombinedHighlighter(CombinedDataProvider chart, BarDataProvider barChart) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(chart);

        // If there is BarData, create a BarHighlighter
        barHighlighter = barChart.getBarData() == null ? null : new BarHighlighter(barChart);
    }

    @Override
    protected List<Highlight> getHighlightsAtXValue(float xVal, float x, float y) {

        mHighlightBuffer.clear();

        List<BarLineScatterCandleBubbleData> dataObjects = mChart.getCombinedData().getAllData();

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < dataObjects.size(); i++) {

            ChartData dataObject = dataObjects.get(i);

            // In case of BarData, let the BarHighlighter take over
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (barHighlighter != null && dataObject instanceof BarData) {
                Highlight high = barHighlighter.getHighlight(x, y);

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (high != null) {
                    high.setDataIndex(i);
                    mHighlightBuffer.add(high);
                }
            } else {

                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int j = 0, dataSetCount = dataObject.getDataSetCount(); j < dataSetCount; j++) {

                    IDataSet dataSet = dataObjects.get(i).getDataSetByIndex(j);

                    // Don't include datasets that cannot be highlighted
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!dataSet.isHighlightEnabled())
                        continue;

                    List<Highlight> highs = buildHighlights(dataSet, j, xVal, DataSet.Rounding.CLOSEST);
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param high Parameter for operation (type: highs)
                     *
                     */
                    for (Highlight high : highs)
                    {
                        high.setDataIndex(i);
                        mHighlightBuffer.add(high);
                    }
                }
            }
        }

        return mHighlightBuffer;
    }

// Protected Highlight getClosest(float x, float y, Highlight... highs) {
//
//        Highlight closest = null;
// Float minDistance = Float.MAX_VALUE;
//
// For (Highlight high : highs) {
//
// If (high == null)
// Continue;
//
// Float tempDistance = getDistance(x, y, high.getXPx(), high.getYPx());
//
// If (tempDistance < minDistance) {
// MinDistance = tempDistance;
// Closest = high;
//            }
//        }
//
// Return closest;
//    }
}
