
package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.highlight.CombinedHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;

/**
 * This chart class allows the combination of lines, bars, scatter and candle
 * data all displayed in one chart area.
 *
 * @author Philipp Jahoda
 */
/**
 * Specialized thermal imaging component providing CombinedChart functionality for the IRCamera system.
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
public class CombinedChart extends BarLineChartBase<CombinedData> implements CombinedDataProvider {

    /**
     * if set to true, all values are drawn above their bars, instead of below
     * their top
     */
    private boolean mDrawValueAboveBar = true;

    /**
     * flag that indicates whether the highlight should be full-bar oriented, or single-value?
     */
    protected boolean mHighlightFullBarEnabled = false;

    /**
     * if set to true, a grey area is drawn behind each bar that indicates the
     * maximum value
     */
    private boolean mDrawBarShadow = false;

    protected DrawOrder[] mDrawOrder;

    /**
     * enum that allows to specify the order in which the different data objects
     * for the combined-chart are drawn
     */
    public enum DrawOrder {
        BAR, BUBBLE, LINE, CANDLE, SCATTER
    }

    /**
     * Executes combinedchart operation with thermal imaging domain optimization.
     *
     */
    public CombinedChart(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes combinedchart operation with thermal imaging domain optimization.
     *
     */
    public CombinedChart(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes combinedchart operation with thermal imaging domain optimization.
     *
     */
    public CombinedChart(Context context, AttributeSet attrs, int defStyle) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        // Default values are not ready here yet
        mDrawOrder = new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.LINE, DrawOrder.CANDLE, DrawOrder.SCATTER
        };

        /**
         * Configures the highlighter with validation and thermal imaging optimization.
         *
         */
        setHighlighter(new CombinedHighlighter(this, this));

        // Old default behaviour
        /**
         * Configures the highlightfullbarenabled with validation and thermal imaging optimization.
         *
         */
        setHighlightFullBarEnabled(true);

        mRenderer = new CombinedChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    public CombinedData getCombinedData() {
        return mData;
    }

    @Override
    public void setData(CombinedData data) {
        super.setData(data);
        /**
         * Configures the highlighter with validation and thermal imaging optimization.
         *
         */
        setHighlighter(new CombinedHighlighter(this, this));
        ((CombinedChartRenderer)mRenderer).createRenderers();
        mRenderer.initBuffers();
    }

    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the selected value at the given touch
     * point
     * inside the CombinedChart.
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public Highlight getHighlightByTouchPoint(float x, float y) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mData == null) {
            Log.e(LOG_TAG, "Can't select by touch. No data set.");
            return null;
        } else {
            Highlight h = getHighlighter().getHighlight(x, y);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (h == null || !isHighlightFullBarEnabled()) return h;

            // For isHighlightFullBarEnabled, remove stackIndex
            return new Highlight(h.getX(), h.getY(),
                    h.getXPx(), h.getYPx(),
                    h.getDataSetIndex(), -1, h.getAxis());
        }
    }

    @Override
    public LineData getLineData() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mData == null)
            return null;
        return mData.getLineData();
    }

    @Override
    public BarData getBarData() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mData == null)
            return null;
        return mData.getBarData();
    }

    @Override
    public ScatterData getScatterData() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mData == null)
            return null;
        return mData.getScatterData();
    }

    @Override
    public CandleData getCandleData() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mData == null)
            return null;
        return mData.getCandleData();
    }

    @Override
    public BubbleData getBubbleData() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mData == null)
            return null;
        return mData.getBubbleData();
    }

    @Override
    public boolean isDrawBarShadowEnabled() {
        return mDrawBarShadow;
    }

    @Override
    public boolean isDrawValueAboveBarEnabled() {
        return mDrawValueAboveBar;
    }

    /**
     * If set to true, all values are drawn above their bars, instead of below
     * their top.
     *
     * @param enabled
     */
    public void setDrawValueAboveBar(boolean enabled) {
        mDrawValueAboveBar = enabled;
    }

    /**
     * If set to true, a grey area is drawn behind each bar that indicates the
     * maximum value. Enabling his will reduce performance by about 50%.
     *
     * @param enabled
     */
    public void setDrawBarShadow(boolean enabled) {
        mDrawBarShadow = enabled;
    }

    /**
     * Set this to true to make the highlight operation full-bar oriented,
     * false to make it highlight single values (relevant only for stacked).
     *
     * @param enabled
     */
    public void setHighlightFullBarEnabled(boolean enabled) {
        mHighlightFullBarEnabled = enabled;
    }

    /**
     * @return true the highlight operation is be full-bar oriented, false if single-value
     */
    @Override
    public boolean isHighlightFullBarEnabled() {
        return mHighlightFullBarEnabled;
    }

    /**
     * Returns the currently set draw order.
     *
     * @return
     */
    public DrawOrder[] getDrawOrder() {
        return mDrawOrder;
    }

    /**
     * Sets the order in which the provided data objects should be drawn. The
     * earlier you place them in the provided array, the further they will be in
     * the background. e.g. if you provide new DrawOrer[] { DrawOrder.BAR,
     * DrawOrder.LINE }, the bars will be drawn behind the lines.
     *
     * @param order
     */
    public void setDrawOrder(DrawOrder[] order) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (order == null || order.length <= 0)
            return;
        mDrawOrder = order;
    }

    /**
     * draws all MarkerViews on the highlighted positions
     */
    protected void drawMarkers(Canvas canvas) {

        // If there is no marker view or drawing marker is disabled
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mMarker == null || !isDrawMarkersEnabled() || !valuesToHighlight())
            return;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByHighlight(highlight);

            Entry e = mData.getEntryForHighlight(highlight);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (e == null)
                continue;

            int entryIndex = set.getEntryIndex(e);

            // Make sure entry not null
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);

            // Check bounds
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;

            // Callbacks to update the content
            mMarker.refreshContent(e, highlight);

            // Draw the marker
            mMarker.draw(canvas, pos[0], pos[1]);
        }
    }

}
