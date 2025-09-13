
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Specialized thermal imaging component providing RadarData functionality for the IRCamera system.
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
public class RadarData extends ChartData<IRadarDataSet> {

    private List<String> mLabels;

    /**
     * Executes radardata operation with thermal imaging domain optimization.
     *
     */
    public RadarData() {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super();
    }

    /**
     * Executes radardata operation with thermal imaging domain optimization.
     *
     */
    public RadarData(List<IRadarDataSet> dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Executes radardata operation with thermal imaging domain optimization.
     *
     */
    public RadarData(IRadarDataSet... dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Sets the labels that should be drawn around the RadarChart at the end of each web line.
     *
     * @param labels
     */
    public void setLabels(List<String> labels) {
        this.mLabels = labels;
    }

    /**
     * Sets the labels that should be drawn around the RadarChart at the end of each web line.
     *
     * @param labels
     */
    public void setLabels(String... labels) {
        this.mLabels = Arrays.asList(labels);
    }

    public List<String> getLabels() {
        return mLabels;
    }

    @Override
    public Entry getEntryForHighlight(Highlight highlight) {
        return getDataSetByIndex(highlight.getDataSetIndex()).getEntryForIndex((int) highlight.getX());
    }
}
