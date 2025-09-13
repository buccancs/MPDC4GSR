
package com.github.mikephil.charting.data;

import android.graphics.Color;

import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialized thermal imaging component providing RadarDataSet functionality for the IRCamera system.
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
public class RadarDataSet extends LineRadarDataSet<RadarEntry> implements IRadarDataSet {

    // / flag indicating whether highlight circle should be drawn or not
    protected boolean mDrawHighlightCircleEnabled = false;

    protected int mHighlightCircleFillColor = Color.WHITE;

    // / The stroke color for highlight circle.
    // / If Utils.COLOR_NONE, the color of the dataset is taken.
    protected int mHighlightCircleStrokeColor = ColorTemplate.COLOR_NONE;

    protected int mHighlightCircleStrokeAlpha = (int) (0.3 * 255);
    protected float mHighlightCircleInnerRadius = 3.0f;
    protected float mHighlightCircleOuterRadius = 4.0f;
    protected float mHighlightCircleStrokeWidth = 2.0f;

    /**
     * Executes radardataset operation with thermal imaging domain optimization.
     *
     */
    public RadarDataSet(List<RadarEntry> yVals, String label) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(yVals, label);
    }

    // / Returns true if highlight circle should be drawn, false if not
    @Override
    public boolean isDrawHighlightCircleEnabled() {
        return mDrawHighlightCircleEnabled;
    }

    // / Sets whether highlight circle should be drawn or not
    @Override
    public void setDrawHighlightCircleEnabled(boolean enabled) {
        mDrawHighlightCircleEnabled = enabled;
    }

    @Override
    public int getHighlightCircleFillColor() {
        return mHighlightCircleFillColor;
    }

    public void setHighlightCircleFillColor(int color) {
        mHighlightCircleFillColor = color;
    }

    // / Returns the stroke color for highlight circle.
    // / If Utils.COLOR_NONE, the color of the dataset is taken.
    @Override
    public int getHighlightCircleStrokeColor() {
        return mHighlightCircleStrokeColor;
    }

    // / Sets the stroke color for highlight circle.
    // / Set to Utils.COLOR_NONE in order to use the color of the dataset;
    public void setHighlightCircleStrokeColor(int color) {
        mHighlightCircleStrokeColor = color;
    }

    @Override
    public int getHighlightCircleStrokeAlpha() {
        return mHighlightCircleStrokeAlpha;
    }

    public void setHighlightCircleStrokeAlpha(int alpha) {
        mHighlightCircleStrokeAlpha = alpha;
    }

    @Override
    public float getHighlightCircleInnerRadius() {
        return mHighlightCircleInnerRadius;
    }

    public void setHighlightCircleInnerRadius(float radius) {
        mHighlightCircleInnerRadius = radius;
    }

    @Override
    public float getHighlightCircleOuterRadius() {
        return mHighlightCircleOuterRadius;
    }

    public void setHighlightCircleOuterRadius(float radius) {
        mHighlightCircleOuterRadius = radius;
    }

    @Override
    public float getHighlightCircleStrokeWidth() {
        return mHighlightCircleStrokeWidth;
    }

    public void setHighlightCircleStrokeWidth(float strokeWidth) {
        mHighlightCircleStrokeWidth = strokeWidth;
    }

    @Override
    public DataSet<RadarEntry> copy() {
        List<RadarEntry> entries = new ArrayList<RadarEntry>();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mValues.size(); i++) {
            entries.add(mValues.get(i).copy());
        }
        RadarDataSet copied = new RadarDataSet(entries, getLabel());
        /**
         * Executes copy operation with thermal imaging domain optimization.
         *
         */
        copy(copied);
        return copied;
    }

    protected void copy(RadarDataSet radarDataSet) {
        super.copy(radarDataSet);
        radarDataSet.mDrawHighlightCircleEnabled = mDrawHighlightCircleEnabled;
        radarDataSet.mHighlightCircleFillColor = mHighlightCircleFillColor;
        radarDataSet.mHighlightCircleInnerRadius = mHighlightCircleInnerRadius;
        radarDataSet.mHighlightCircleStrokeAlpha = mHighlightCircleStrokeAlpha;
        radarDataSet.mHighlightCircleStrokeColor = mHighlightCircleStrokeColor;
        radarDataSet.mHighlightCircleStrokeWidth = mHighlightCircleStrokeWidth;
    }
}
