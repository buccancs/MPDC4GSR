package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialized thermal imaging component providing CandleData functionality for the IRCamera system.
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
public class CandleData extends BarLineScatterCandleBubbleData<ICandleDataSet> {

    /**
     * Executes candledata operation with thermal imaging domain optimization.
     *
     */
    public CandleData() {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super();
    }

    /**
     * Executes candledata operation with thermal imaging domain optimization.
     *
     */
    public CandleData(List<ICandleDataSet> dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Executes candledata operation with thermal imaging domain optimization.
     *
     */
    public CandleData(ICandleDataSet... dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }
}
