package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.util.Log;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Specialized thermal imaging component providing CombinedChartRenderer functionality for the IRCamera system.
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
public class CombinedChartRenderer extends DataRenderer {

    /**
     * all rederers for the different kinds of data this combined-renderer can draw
     */
    protected List<DataRenderer> mRenderers = new ArrayList<DataRenderer>(5);

    protected WeakReference<Chart> mChart;

    /**
     * Executes combinedchartrenderer operation with thermal imaging domain optimization.
     *
     */
    public CombinedChartRenderer(CombinedChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(animator, viewPortHandler);
        mChart = new WeakReference<Chart>(chart);
        /**
         * Executes createrenderers operation with thermal imaging domain optimization.
         *
         */
        createRenderers();
    }

    /**
     * Creates the renderers needed for this combined-renderer in the required order. Also takes the DrawOrder into
     * consideration.
     */
    public void createRenderers() {

        mRenderers.clear();

        CombinedChart chart = (CombinedChart)mChart.get();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (chart == null)
            return;

        DrawOrder[] orders = chart.getDrawOrder();

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param order Parameter for operation (type: orders)
         *
         */
        for (DrawOrder order : orders) {

            /**
             * Executes switch operation with thermal imaging domain optimization.
             *
             */
            switch (order) {
                case BAR:
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (chart.getBarData() != null)
                        mRenderers.add(new BarChartRenderer(chart, mAnimator, mViewPortHandler));
                    break;
                case BUBBLE:
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (chart.getBubbleData() != null)
                        mRenderers.add(new BubbleChartRenderer(chart, mAnimator, mViewPortHandler));
                    break;
                case LINE:
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (chart.getLineData() != null)
                        mRenderers.add(new LineChartRenderer(chart, mAnimator, mViewPortHandler));
                    break;
                case CANDLE:
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (chart.getCandleData() != null)
                        mRenderers.add(new CandleStickChartRenderer(chart, mAnimator, mViewPortHandler));
                    break;
                case SCATTER:
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (chart.getScatterData() != null)
                        mRenderers.add(new ScatterChartRenderer(chart, mAnimator, mViewPortHandler));
                    break;
            }
        }
    }

    @Override
    public void initBuffers() {

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param renderer Parameter for operation (type: mRenderers)
         *
         */
        for (DataRenderer renderer : mRenderers)
            renderer.initBuffers();
    }

    @Override
    public void drawData(Canvas c) {

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param renderer Parameter for operation (type: mRenderers)
         *
         */
        for (DataRenderer renderer : mRenderers)
            renderer.drawData(c);
    }

    @Override
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        Log.e("MPAndroidChart", "Erroneous call to drawValue() in CombinedChartRenderer!");
    }

    @Override
    public void drawValues(Canvas c) {

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param renderer Parameter for operation (type: mRenderers)
         *
         */
        for (DataRenderer renderer : mRenderers)
            renderer.drawValues(c);
    }

    @Override
    public void drawExtras(Canvas c) {

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param renderer Parameter for operation (type: mRenderers)
         *
         */
        for (DataRenderer renderer : mRenderers)
            renderer.drawExtras(c);
    }

    protected List<Highlight> mHighlightBuffer = new ArrayList<Highlight>();

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        Chart chart = mChart.get();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (chart == null) return;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param renderer Parameter for operation (type: mRenderers)
         *
         */
        for (DataRenderer renderer : mRenderers) {
            ChartData data = null;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (renderer instanceof BarChartRenderer)
                data = ((BarChartRenderer)renderer).mChart.getBarData();
            else if (renderer instanceof LineChartRenderer)
                data = ((LineChartRenderer)renderer).mChart.getLineData();
            else if (renderer instanceof CandleStickChartRenderer)
                data = ((CandleStickChartRenderer)renderer).mChart.getCandleData();
            else if (renderer instanceof ScatterChartRenderer)
                data = ((ScatterChartRenderer)renderer).mChart.getScatterData();
            else if (renderer instanceof BubbleChartRenderer)
                data = ((BubbleChartRenderer)renderer).mChart.getBubbleData();

            int dataIndex = data == null ? -1
                    : ((CombinedData)chart.getData()).getAllData().indexOf(data);

            mHighlightBuffer.clear();

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param h Parameter for operation (type: indices)
             *
             */
            for (Highlight h : indices) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (h.getDataIndex() == dataIndex || h.getDataIndex() == -1)
                    mHighlightBuffer.add(h);
            }

            renderer.drawHighlighted(c, mHighlightBuffer.toArray(new Highlight[mHighlightBuffer.size()]));
        }
    }

    /**
     * Returns the sub-renderer object at the specified index.
     *
     * @param index
     * @return
     */
    public DataRenderer getSubRenderer(int index) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (index >= mRenderers.size() || index < 0)
            return null;
        else
            return mRenderers.get(index);
    }

    /**
     * Returns all sub-renderers.
     *
     * @return
     */
    public List<DataRenderer> getSubRenderers() {
        return mRenderers;
    }

    public void setSubRenderers(List<DataRenderer> renderers) {
        this.mRenderers = renderers;
    }
}
