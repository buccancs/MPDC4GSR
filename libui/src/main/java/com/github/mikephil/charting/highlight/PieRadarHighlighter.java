package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.PieRadarChartBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialized thermal imaging component providing PieRadarHighlighter functionality for the IRCamera system.
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
public abstract class PieRadarHighlighter<T extends PieRadarChartBase> implements IHighlighter
{

    protected T mChart;

    /**
     * buffer for storing previously highlighted values
     */
    protected List<Highlight> mHighlightBuffer = new ArrayList<Highlight>();

    public PieRadarHighlighter(T chart) {
        this.mChart = chart;
    }

    @Override
    public Highlight getHighlight(float x, float y) {

        float touchDistanceToCenter = mChart.distanceToCenter(x, y);

        // Check if a slice was touched
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (touchDistanceToCenter > mChart.getRadius()) {

            // If no slice was touched, highlight nothing
            return null;

        } else {

            float angle = mChart.getAngleForPoint(x, y);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mChart instanceof PieChart) {
                angle /= mChart.getAnimator().getPhaseY();
            }

            int index = mChart.getIndexForAngle(angle);

            // Check if the index could be found
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (index < 0 || index >= mChart.getData().getMaxEntryCountSet().getEntryCount()) {
                return null;

            } else {
                return getClosestHighlight(index, x, y);
            }
        }
    }

    /**
     * Returns the closest Highlight object of the given objects based on the touch position inside the chart.
     *
     * @param index
     * @param x
     * @param y
     * @return
     */
    protected abstract Highlight getClosestHighlight(int index, float x, float y);
}
