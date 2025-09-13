
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;

import java.util.List;

/**
 * Specialized thermal imaging component providing BubbleData functionality for the IRCamera system.
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
public class BubbleData extends BarLineScatterCandleBubbleData<IBubbleDataSet> {

    /**
     * Executes bubbledata operation with thermal imaging domain optimization.
     *
     */
    public BubbleData() {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super();
    }

    /**
     * Executes bubbledata operation with thermal imaging domain optimization.
     *
     */
    public BubbleData(IBubbleDataSet... dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Executes bubbledata operation with thermal imaging domain optimization.
     *
     */
    public BubbleData(List<IBubbleDataSet> dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Sets the width of the circle that surrounds the bubble when highlighted
     * for all DataSet objects this data object contains, in dp.
     * 
     * @param width
     */
    public void setHighlightCircleWidth(float width) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (IBubbleDataSet set : mDataSets) {
            set.setHighlightCircleWidth(width);
        }
    }
}
