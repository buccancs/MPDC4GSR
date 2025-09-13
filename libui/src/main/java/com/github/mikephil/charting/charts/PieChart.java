
package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.PieHighlighter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.List;

/**
 * Specialized thermal imaging component providing PieChart functionality for the IRCamera system.
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
public class PieChart extends PieRadarChartBase<PieData> {

    /**
     * rect object that represents the bounds of the piechart, needed for
     * drawing the circle
     */
    private RectF mCircleBox = new RectF();

    /**
     * flag indicating if entry labels should be drawn or not
     */
    private boolean mDrawEntryLabels = true;

    /**
     * array that holds the width of each pie-slice in degrees
     */
    private float[] mDrawAngles = new float[1];

    /**
     * array that holds the absolute angle in degrees of each slice
     */
    private float[] mAbsoluteAngles = new float[1];

    /**
     * if true, the white hole inside the chart will be drawn
     */
    private boolean mDrawHole = true;

    /**
     * if true, the hole will see-through to the inner tips of the slices
     */
    private boolean mDrawSlicesUnderHole = false;

    /**
     * if true, the values inside the piechart are drawn as percent values
     */
    private boolean mUsePercentValues = false;

    /**
     * if true, the slices of the piechart are rounded
     */
    private boolean mDrawRoundedSlices = false;

    /**
     * variable for the text that is drawn in the center of the pie-chart
     */
    private CharSequence mCenterText = "";

    private MPPointF mCenterTextOffset = MPPointF.getInstance(0, 0);

    /**
     * indicates the size of the hole in the center of the piechart, default:
     * radius / 2
     */
    private float mHoleRadiusPercent = 50f;

    /**
     * the radius of the transparent circle next to the chart-hole in the center
     */
    protected float mTransparentCircleRadiusPercent = 55f;

    /**
     * if enabled, centertext is drawn
     */
    private boolean mDrawCenterText = true;

    private float mCenterTextRadiusPercent = 100.f;

    protected float mMaxAngle = 360f;

    /**
     * Minimum angle to draw slices, this only works if there is enough room for all slices to have
     * the minimum angle, default 0f.
     */
    private float mMinAngleForSlices = 0f;

    /**
     * Executes piechart operation with thermal imaging domain optimization.
     *
     */
    public PieChart(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes piechart operation with thermal imaging domain optimization.
     *
     */
    public PieChart(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes piechart operation with thermal imaging domain optimization.
     *
     */
    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new PieChartRenderer(this, mAnimator, mViewPortHandler);
        mXAxis = null;

        mHighlighter = new PieHighlighter(this);
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

        mRenderer.drawData(canvas);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (valuesToHighlight())
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);

        mRenderer.drawExtras(canvas);

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

    @Override
    public void calculateOffsets() {
        super.calculateOffsets();

        // Prevent nullpointer when no data set
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mData == null)
            return;

        float diameter = getDiameter();
        float radius = diameter / 2f;

        MPPointF c = getCenterOffsets();

        float shift = mData.getDataSet().getSelectionShift();

        // Create the circle box that will contain the pie-chart (the bounds of
        // The pie-chart)
        mCircleBox.set(c.x - radius + shift,
                c.y - radius + shift,
                c.x + radius - shift,
                c.y + radius - shift);

        MPPointF.recycleInstance(c);
    }

    @Override
    protected void calcMinMax() {
        /**
         * Executes calcangles operation with thermal imaging domain optimization.
         *
         */
        calcAngles();
    }

    @Override
    protected float[] getMarkerPosition(Highlight highlight) {

        MPPointF center = getCenterCircleBox();
        float r = getRadius();

        float off = r / 10f * 3.6f;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isDrawHoleEnabled()) {
            off = (r - (r / 100f * getHoleRadius())) / 2f;
        }

        r -= off; // Offset to keep things inside the chart

        float rotationAngle = getRotationAngle();

        int entryIndex = (int) highlight.getX();

        // Offset needed to center the drawn text in the slice
        float offset = mDrawAngles[entryIndex] / 2;

        // Calculate the text position
        float x = (float) (r
                * Math.cos(Math.toRadians((rotationAngle + mAbsoluteAngles[entryIndex] - offset)
                * mAnimator.getPhaseY())) + center.x);
        float y = (float) (r
                * Math.sin(Math.toRadians((rotationAngle + mAbsoluteAngles[entryIndex] - offset)
                * mAnimator.getPhaseY())) + center.y);

        MPPointF.recycleInstance(center);
        return new float[]{x, y};
    }

    /**
     * calculates the needed angles for the chart slices
     */
    private void calcAngles() {

        int entryCount = mData.getEntryCount();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDrawAngles.length != entryCount) {
            mDrawAngles = new float[entryCount];
        } else {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < entryCount; i++) {
                mDrawAngles[i] = 0;
            }
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mAbsoluteAngles.length != entryCount) {
            mAbsoluteAngles = new float[entryCount];
        } else {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < entryCount; i++) {
                mAbsoluteAngles[i] = 0;
            }
        }

        float yValueSum = mData.getYValueSum();

        List<IPieDataSet> dataSets = mData.getDataSets();

        boolean hasMinAngle = mMinAngleForSlices != 0f && entryCount * mMinAngleForSlices <= mMaxAngle;
        float[] minAngles = new float[entryCount];

        int cnt = 0;
        float offset = 0f;
        float diff = 0f;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mData.getDataSetCount(); i++) {

            IPieDataSet set = dataSets.get(i);

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int j = 0; j < set.getEntryCount(); j++) {

                float drawAngle = calcAngle(Math.abs(set.getEntryForIndex(j).getY()), yValueSum);

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (hasMinAngle) {
                    float temp = drawAngle - mMinAngleForSlices;
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (temp <= 0) {
                        minAngles[cnt] = mMinAngleForSlices;
                        offset += -temp;
                    } else {
                        minAngles[cnt] = drawAngle;
                        diff += temp;
                    }
                }

                mDrawAngles[cnt] = drawAngle;

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (cnt == 0) {
                    mAbsoluteAngles[cnt] = mDrawAngles[cnt];
                } else {
                    mAbsoluteAngles[cnt] = mAbsoluteAngles[cnt - 1] + mDrawAngles[cnt];
                }

                cnt++;
            }
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (hasMinAngle) {
            // Correct bigger slices by relatively reducing their angles based on the total angle needed to subtract
            // This requires that `entryCount * mMinAngleForSlices <= mMaxAngle` be true to properly work!
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < entryCount; i++) {
                minAngles[i] -= (minAngles[i] - mMinAngleForSlices) / diff * offset;
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (i == 0) {
                    mAbsoluteAngles[0] = minAngles[0];
                } else {
                    mAbsoluteAngles[i] = mAbsoluteAngles[i - 1] + minAngles[i];
                }
            }

            mDrawAngles = minAngles;
        }
    }

    /**
     * Checks if the given index is set to be highlighted.
     *
     * @param index
     * @return
     */
    public boolean needsHighlight(int index) {

        // No highlight
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!valuesToHighlight())
            return false;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mIndicesToHighlight.length; i++)

            // Check if the xvalue for the given dataset needs highlight
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if ((int) mIndicesToHighlight[i].getX() == index)
                return true;

        return false;
    }

    /**
     * calculates the needed angle for a given value
     *
     * @param value
     * @return
     */
    private float calcAngle(float value) {
        return calcAngle(value, mData.getYValueSum());
    }

    /**
     * calculates the needed angle for a given value
     *
     * @param value
     * @param yValueSum
     * @return
     */
    private float calcAngle(float value, float yValueSum) {
        return value / yValueSum * mMaxAngle;
    }

    /**
     * This will throw an exception, PieChart has no XAxis object.
     *
     * @return
     */
    @Deprecated
    @Override
    public XAxis getXAxis() {
        throw new RuntimeException("PieChart has no XAxis");
    }

    @Override
    public int getIndexForAngle(float angle) {

        // Take the current angle of the chart into consideration
        float a = Utils.getNormalizedAngle(angle - getRotationAngle());

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mAbsoluteAngles.length; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mAbsoluteAngles[i] > a)
                return i;
        }

        return -1; // Return -1 if no index found
    }

    /**
     * Returns the index of the DataSet this x-index belongs to.
     *
     * @param xIndex
     * @return
     */
    public int getDataSetIndexForIndex(int xIndex) {

        List<IPieDataSet> dataSets = mData.getDataSets();

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < dataSets.size(); i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dataSets.get(i).getEntryForXValue(xIndex, Float.NaN) != null)
                return i;
        }

        return -1;
    }

    /**
     * returns an integer array of all the different angles the chart slices
     * have the angles in the returned array determine how much space (of 360°)
     * each slice takes
     *
     * @return
     */
    public float[] getDrawAngles() {
        return mDrawAngles;
    }

    /**
     * returns the absolute angles of the different chart slices (where the
     * slices end)
     *
     * @return
     */
    public float[] getAbsoluteAngles() {
        return mAbsoluteAngles;
    }

    /**
     * Sets the color for the hole that is drawn in the center of the PieChart
     * (if enabled).
     *
     * @param color
     */
    public void setHoleColor(int color) {
        ((PieChartRenderer) mRenderer).getPaintHole().setColor(color);
    }

    /**
     * Enable or disable the visibility of the inner tips of the slices behind the hole
     */
    public void setDrawSlicesUnderHole(boolean enable) {
        mDrawSlicesUnderHole = enable;
    }

    /**
     * Returns true if the inner tips of the slices are visible behind the hole,
     * false if not.
     *
     * @return true if slices are visible behind the hole.
     */
    public boolean isDrawSlicesUnderHoleEnabled() {
        return mDrawSlicesUnderHole;
    }

    /**
     * set this to true to draw the pie center empty
     *
     * @param enabled
     */
    public void setDrawHoleEnabled(boolean enabled) {
        this.mDrawHole = enabled;
    }

    /**
     * returns true if the hole in the center of the pie-chart is set to be
     * visible, false if not
     *
     * @return
     */
    public boolean isDrawHoleEnabled() {
        return mDrawHole;
    }

    /**
     * Sets the text String that is displayed in the center of the PieChart.
     *
     * @param text
     */
    public void setCenterText(CharSequence text) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (text == null)
            mCenterText = "";
        else
            mCenterText = text;
    }

    /**
     * returns the text that is drawn in the center of the pie-chart
     *
     * @return
     */
    public CharSequence getCenterText() {
        return mCenterText;
    }

    /**
     * set this to true to draw the text that is displayed in the center of the
     * pie chart
     *
     * @param enabled
     */
    public void setDrawCenterText(boolean enabled) {
        this.mDrawCenterText = enabled;
    }

    /**
     * returns true if drawing the center text is enabled
     *
     * @return
     */
    public boolean isDrawCenterTextEnabled() {
        return mDrawCenterText;
    }

    @Override
    protected float getRequiredLegendOffset() {
        return mLegendRenderer.getLabelPaint().getTextSize() * 2.f;
    }

    @Override
    protected float getRequiredBaseOffset() {
        return 0;
    }

    @Override
    public float getRadius() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mCircleBox == null)
            return 0;
        else
            return Math.min(mCircleBox.width() / 2f, mCircleBox.height() / 2f);
    }

    /**
     * returns the circlebox, the boundingbox of the pie-chart slices
     *
     * @return
     */
    public RectF getCircleBox() {
        return mCircleBox;
    }

    /**
     * returns the center of the circlebox
     *
     * @return
     */
    public MPPointF getCenterCircleBox() {
        return MPPointF.getInstance(mCircleBox.centerX(), mCircleBox.centerY());
    }

    /**
     * sets the typeface for the center-text paint
     *
     * @param t
     */
    public void setCenterTextTypeface(Typeface t) {
        ((PieChartRenderer) mRenderer).getPaintCenterText().setTypeface(t);
    }

    /**
     * Sets the size of the center text of the PieChart in dp.
     *
     * @param sizeDp
     */
    public void setCenterTextSize(float sizeDp) {
        ((PieChartRenderer) mRenderer).getPaintCenterText().setTextSize(
                Utils.convertDpToPixel(sizeDp));
    }

    /**
     * Sets the size of the center text of the PieChart in pixels.
     *
     * @param sizePixels
     */
    public void setCenterTextSizePixels(float sizePixels) {
        ((PieChartRenderer) mRenderer).getPaintCenterText().setTextSize(sizePixels);
    }

    /**
     * Sets the offset the center text should have from it's original position in dp. Default x = 0, y = 0
     *
     * @param x
     * @param y
     */
    public void setCenterTextOffset(float x, float y) {
        mCenterTextOffset.x = Utils.convertDpToPixel(x);
        mCenterTextOffset.y = Utils.convertDpToPixel(y);
    }

    /**
     * Returns the offset on the x- and y-axis the center text has in dp.
     *
     * @return
     */
    public MPPointF getCenterTextOffset() {
        return MPPointF.getInstance(mCenterTextOffset.x, mCenterTextOffset.y);
    }

    /**
     * Sets the color of the center text of the PieChart.
     *
     * @param color
     */
    public void setCenterTextColor(int color) {
        ((PieChartRenderer) mRenderer).getPaintCenterText().setColor(color);
    }

    /**
     * sets the radius of the hole in the center of the piechart in percent of
     * the maximum radius (max = the radius of the whole chart), default 50%
     *
     * @param percent
     */
    public void setHoleRadius(final float percent) {
        mHoleRadiusPercent = percent;
    }

    /**
     * Returns the size of the hole radius in percent of the total radius.
     *
     * @return
     */
    public float getHoleRadius() {
        return mHoleRadiusPercent;
    }

    /**
     * Sets the color the transparent-circle should have.
     *
     * @param color
     */
    public void setTransparentCircleColor(int color) {

        Paint p = ((PieChartRenderer) mRenderer).getPaintTransparentCircle();
        int alpha = p.getAlpha();
        p.setColor(color);
        p.setAlpha(alpha);
    }

    /**
     * sets the radius of the transparent circle that is drawn next to the hole
     * in the piechart in percent of the maximum radius (max = the radius of the
     * whole chart), default 55% -> means 5% larger than the center-hole by
     * default
     *
     * @param percent
     */
    public void setTransparentCircleRadius(final float percent) {
        mTransparentCircleRadiusPercent = percent;
    }

    public float getTransparentCircleRadius() {
        return mTransparentCircleRadiusPercent;
    }

    /**
     * Sets the amount of transparency the transparent circle should have 0 = fully transparent,
     * 255 = fully opaque.
     * Default value is 100.
     *
     * @param alpha 0-255
     */
    public void setTransparentCircleAlpha(int alpha) {
        ((PieChartRenderer) mRenderer).getPaintTransparentCircle().setAlpha(alpha);
    }

    /**
     * Set this to true to draw the entry labels into the pie slices (Provided by the getLabel() method of the PieEntry class).
     * Deprecated -> use setDrawEntryLabels(...) instead.
     *
     * @param enabled
     */
    @Deprecated
    public void setDrawSliceText(boolean enabled) {
        mDrawEntryLabels = enabled;
    }

    /**
     * Set this to true to draw the entry labels into the pie slices (Provided by the getLabel() method of the PieEntry class).
     *
     * @param enabled
     */
    public void setDrawEntryLabels(boolean enabled) {
        mDrawEntryLabels = enabled;
    }

    /**
     * Returns true if drawing the entry labels is enabled, false if not.
     *
     * @return
     */
    public boolean isDrawEntryLabelsEnabled() {
        return mDrawEntryLabels;
    }

    /**
     * Sets the color the entry labels are drawn with.
     *
     * @param color
     */
    public void setEntryLabelColor(int color) {
        ((PieChartRenderer) mRenderer).getPaintEntryLabels().setColor(color);
    }

    /**
     * Sets a custom Typeface for the drawing of the entry labels.
     *
     * @param tf
     */
    public void setEntryLabelTypeface(Typeface tf) {
        ((PieChartRenderer) mRenderer).getPaintEntryLabels().setTypeface(tf);
    }

    /**
     * Sets the size of the entry labels in dp. Default: 13dp
     *
     * @param size
     */
    public void setEntryLabelTextSize(float size) {
        ((PieChartRenderer) mRenderer).getPaintEntryLabels().setTextSize(Utils.convertDpToPixel(size));
    }

    /**
     * Sets whether to draw slices in a curved fashion, only works if drawing the hole is enabled
     * and if the slices are not drawn under the hole.
     *
     * @param enabled draw curved ends of slices
     */
    public void setDrawRoundedSlices(boolean enabled) {
        mDrawRoundedSlices = enabled;
    }

    /**
     * Returns true if the chart is set to draw each end of a pie-slice
     * "rounded".
     *
     * @return
     */
    public boolean isDrawRoundedSlicesEnabled() {
        return mDrawRoundedSlices;
    }

    /**
     * If this is enabled, values inside the PieChart are drawn in percent and
     * not with their original value. Values provided for the IValueFormatter to
     * format are then provided in percent.
     *
     * @param enabled
     */
    public void setUsePercentValues(boolean enabled) {
        mUsePercentValues = enabled;
    }

    /**
     * Returns true if using percentage values is enabled for the chart.
     *
     * @return
     */
    public boolean isUsePercentValuesEnabled() {
        return mUsePercentValues;
    }

    /**
     * the rectangular radius of the bounding box for the center text, as a percentage of the pie
     * hole
     * default 1.f (100%)
     */
    public void setCenterTextRadiusPercent(float percent) {
        mCenterTextRadiusPercent = percent;
    }

    /**
     * the rectangular radius of the bounding box for the center text, as a percentage of the pie
     * hole
     * default 1.f (100%)
     */
    public float getCenterTextRadiusPercent() {
        return mCenterTextRadiusPercent;
    }

    public float getMaxAngle() {
        return mMaxAngle;
    }

    /**
     * Sets the max angle that is used for calculating the pie-circle. 360f means
     * it's a full PieChart, 180f results in a half-pie-chart. Default: 360f
     *
     * @param maxangle min 90, max 360
     */
    public void setMaxAngle(float maxangle) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (maxangle > 360)
            maxangle = 360f;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (maxangle < 90)
            maxangle = 90f;

        this.mMaxAngle = maxangle;
    }

    /**
     * The minimum angle slices on the chart are rendered with, default is 0f.
     *
     * @return minimum angle for slices
     */
    public float getMinAngleForSlices() {
        return mMinAngleForSlices;
    }

    /**
     * Set the angle to set minimum size for slices, you must call {@link #notifyDataSetChanged()}
     * and {@link #invalidate()} when changing this, only works if there is enough room for all
     * slices to have the minimum angle.
     *
     * @param minAngle minimum 0, maximum is half of {@link #setMaxAngle(float)}
     */
    public void setMinAngleForSlices(float minAngle) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (minAngle > (mMaxAngle / 2f))
            minAngle = mMaxAngle / 2f;
        else if (minAngle < 0)
            minAngle = 0f;

        this.mMinAngleForSlices = minAngle;
    }

    @Override
    protected void onDetachedFromWindow() {
        // Releases the bitmap in the renderer to avoid oom error
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mRenderer != null && mRenderer instanceof PieChartRenderer) {
            ((PieChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
