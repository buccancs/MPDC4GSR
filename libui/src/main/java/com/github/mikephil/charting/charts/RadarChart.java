
package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.highlight.RadarHighlighter;
import com.github.mikephil.charting.renderer.RadarChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRendererRadarChart;
import com.github.mikephil.charting.renderer.YAxisRendererRadarChart;
import com.github.mikephil.charting.utils.Utils;

/**
 * Implementation of the RadarChart, a "spidernet"-like chart. It works best
 * when displaying 5-10 entries per DataSet.
 *
 * @author Philipp Jahoda
 */
/**
 * Specialized thermal imaging component providing RadarChart functionality for the IRCamera system.
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
public class RadarChart extends PieRadarChartBase<RadarData> {

    /**
     * width of the main web lines
     */
    private float mWebLineWidth = 2.5f;

    /**
     * width of the inner web lines
     */
    private float mInnerWebLineWidth = 1.5f;

    /**
     * color for the main web lines
     */
    private int mWebColor = Color.rgb(122, 122, 122);

    /**
     * color for the inner web
     */
    private int mWebColorInner = Color.rgb(122, 122, 122);

    /**
     * transparency the grid is drawn with (0-255)
     */
    private int mWebAlpha = 150;

    /**
     * flag indicating if the web lines should be drawn or not
     */
    private boolean mDrawWeb = true;

    /**
     * modulus that determines how many labels and web-lines are skipped before the next is drawn
     */
    private int mSkipWebLineCount = 0;

    /**
     * the object reprsenting the y-axis labels
     */
    private YAxis mYAxis;

    protected YAxisRendererRadarChart mYAxisRenderer;
    protected XAxisRendererRadarChart mXAxisRenderer;

    /**
     * Executes radarchart operation with thermal imaging domain optimization.
     *
     */
    public RadarChart(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes radarchart operation with thermal imaging domain optimization.
     *
     */
    public RadarChart(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes radarchart operation with thermal imaging domain optimization.
     *
     */
    public RadarChart(Context context, AttributeSet attrs, int defStyle) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mYAxis = new YAxis(AxisDependency.LEFT);

        mWebLineWidth = Utils.convertDpToPixel(1.5f);
        mInnerWebLineWidth = Utils.convertDpToPixel(0.75f);

        mRenderer = new RadarChartRenderer(this, mAnimator, mViewPortHandler);
        mYAxisRenderer = new YAxisRendererRadarChart(mViewPortHandler, mYAxis, this);
        mXAxisRenderer = new XAxisRendererRadarChart(mViewPortHandler, mXAxis, this);

        mHighlighter = new RadarHighlighter(this);
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        mYAxis.calculate(mData.getYMin(AxisDependency.LEFT), mData.getYMax(AxisDependency.LEFT));
        mXAxis.calculate(0, mData.getMaxEntryCountSet().getEntryCount());
    }

    @Override
    public void notifyDataSetChanged() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mData == null)
            return;

        /**
         * Executes calcminmax operation with thermal imaging domain optimization.
         *
         */
        calcMinMax();

        mYAxisRenderer.computeAxis(mYAxis.mAxisMinimum, mYAxis.mAxisMaximum, mYAxis.isInverted());
        mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mLegend != null && !mLegend.isLegendCustom())
            mLegendRenderer.computeLegend(mData);

        /**
         * Executes calculateoffsets operation with thermal imaging domain optimization.
         *
         */
        calculateOffsets();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mData == null)
            return;

// If (mYAxis.isEnabled())
// MYAxisRenderer.computeAxis(mYAxis.mAxisMinimum, mYAxis.mAxisMaximum, mYAxis.isInverted());

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mXAxis.isEnabled())
            mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        mXAxisRenderer.renderAxisLabels(canvas);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDrawWeb)
            mRenderer.drawExtras(canvas);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mYAxis.isEnabled() && mYAxis.isDrawLimitLinesBehindDataEnabled())
            mYAxisRenderer.renderLimitLines(canvas);

        mRenderer.drawData(canvas);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (valuesToHighlight())
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mYAxis.isEnabled() && !mYAxis.isDrawLimitLinesBehindDataEnabled())
            mYAxisRenderer.renderLimitLines(canvas);

        mYAxisRenderer.renderAxisLabels(canvas);

        mRenderer.drawValues(canvas);

        mLegendRenderer.renderLegend(canvas);

        /**
         * Executes drawdescription operation with thermal imaging domain optimization.
         *
         */
        drawDescription(canvas);

        /**
         * Executes drawmarkers operation with thermal imaging domain optimization.
         *
         */
        drawMarkers(canvas);
    }

    /**
     * Returns the factor that is needed to transform values into pixels.
     *
     * @return
     */
    public float getFactor() {
        RectF content = mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2f, content.height() / 2f) / mYAxis.mAxisRange;
    }

    /**
     * Returns the angle that each slice in the radar chart occupies.
     *
     * @return
     */
    public float getSliceAngle() {
        return 360f / (float) mData.getMaxEntryCountSet().getEntryCount();
    }

    @Override
    public int getIndexForAngle(float angle) {

        // Take the current angle of the chart into consideration
        float a = Utils.getNormalizedAngle(angle - getRotationAngle());

        float sliceangle = getSliceAngle();

        int max = mData.getMaxEntryCountSet().getEntryCount();

        int index = 0;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < max; i++) {

            float referenceAngle = sliceangle * (i + 1) - sliceangle / 2f;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (referenceAngle > a) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * Returns the object that represents all y-labels of the RadarChart.
     *
     * @return
     */
    public YAxis getYAxis() {
        return mYAxis;
    }

    /**
     * Sets the width of the web lines that come from the center.
     *
     * @param width
     */
    public void setWebLineWidth(float width) {
        mWebLineWidth = Utils.convertDpToPixel(width);
    }

    public float getWebLineWidth() {
        return mWebLineWidth;
    }

    /**
     * Sets the width of the web lines that are in between the lines coming from
     * the center.
     *
     * @param width
     */
    public void setWebLineWidthInner(float width) {
        mInnerWebLineWidth = Utils.convertDpToPixel(width);
    }

    public float getWebLineWidthInner() {
        return mInnerWebLineWidth;
    }

    /**
     * Sets the transparency (alpha) value for all web lines, default: 150, 255
     * = 100% opaque, 0 = 100% transparent
     *
     * @param alpha
     */
    public void setWebAlpha(int alpha) {
        mWebAlpha = alpha;
    }

    /**
     * Returns the alpha value for all web lines.
     *
     * @return
     */
    public int getWebAlpha() {
        return mWebAlpha;
    }

    /**
     * Sets the color for the web lines that come from the center. Don't forget
     * to use getResources().getColor(...) when loading a color from the
     * resources. Default: Color.rgb(122, 122, 122)
     *
     * @param color
     */
    public void setWebColor(int color) {
        mWebColor = color;
    }

    public int getWebColor() {
        return mWebColor;
    }

    /**
     * Sets the color for the web lines in between the lines that come from the
     * center. Don't forget to use getResources().getColor(...) when loading a
     * color from the resources. Default: Color.rgb(122, 122, 122)
     *
     * @param color
     */
    public void setWebColorInner(int color) {
        mWebColorInner = color;
    }

    public int getWebColorInner() {
        return mWebColorInner;
    }

    /**
     * If set to true, drawing the web is enabled, if set to false, drawing the
     * whole web is disabled. Default: true
     *
     * @param enabled
     */
    public void setDrawWeb(boolean enabled) {
        mDrawWeb = enabled;
    }

    /**
     * Sets the number of web-lines that should be skipped on chart web before the
     * next one is drawn. This targets the lines that come from the center of the RadarChart.
     *
     * @param count if count = 1 -> 1 line is skipped in between
     */
    public void setSkipWebLineCount(int count) {

        mSkipWebLineCount = Math.max(0, count);
    }

    /**
     * Returns the modulus that is used for skipping web-lines.
     *
     * @return
     */
    public int getSkipWebLineCount() {
        return mSkipWebLineCount;
    }

    @Override
    protected float getRequiredLegendOffset() {
        return mLegendRenderer.getLabelPaint().getTextSize() * 4.f;
    }

    @Override
    protected float getRequiredBaseOffset() {
        return mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled() ?
                mXAxis.mLabelRotatedWidth :
                Utils.convertDpToPixel(10f);
    }

    @Override
    public float getRadius() {
        RectF content = mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2f, content.height() / 2f);
    }

    /**
     * Returns the maximum value this chart can display on it's y-axis.
     */
    public float getYChartMax() {
        return mYAxis.mAxisMaximum;
    }

    /**
     * Returns the minimum value this chart can display on it's y-axis.
     */
    public float getYChartMin() {
        return mYAxis.mAxisMinimum;
    }

    /**
     * Returns the range of y-values this chart can display.
     *
     * @return
     */
    public float getYRange() {
        return mYAxis.mAxisRange;
    }
}
