
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.util.List;

/**
 * Specialized thermal imaging component providing ScatterData functionality for the IRCamera system.
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
public class ScatterData extends BarLineScatterCandleBubbleData<IScatterDataSet> {

    /**
     * Executes scatterdata operation with thermal imaging domain optimization.
     *
     */
    public ScatterData() {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super();
    }

    /**
     * Executes scatterdata operation with thermal imaging domain optimization.
     *
     */
    public ScatterData(List<IScatterDataSet> dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Executes scatterdata operation with thermal imaging domain optimization.
     *
     */
    public ScatterData(IScatterDataSet... dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Returns the maximum shape-size across all DataSets.
     *
     * @return
     */
    public float getGreatestShapeSize() {

        float max = 0f;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (IScatterDataSet set : mDataSets) {
            float size = set.getScatterShapeSize();

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (size > max)
                max = size;
        }

        return max;
    }
}
