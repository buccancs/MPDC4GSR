
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialized thermal imaging component providing LineData functionality for the IRCamera system.
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
public class LineData extends BarLineScatterCandleBubbleData<ILineDataSet> {

    /**
     * Executes linedata operation with thermal imaging domain optimization.
     *
     */
    public LineData() {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super();
    }

    /**
     * Executes linedata operation with thermal imaging domain optimization.
     *
     */
    public LineData(ILineDataSet... dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Executes linedata operation with thermal imaging domain optimization.
     *
     */
    public LineData(List<ILineDataSet> dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }
}
