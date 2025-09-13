
package com.github.mikephil.charting.charts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.animation.Easing.EasingFunction;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/**
 * Specialized thermal imaging component providing PieRadarChartBase functionality for the IRCamera system.
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
public abstract class PieRadarChartBase<T extends ChartData<? extends IDataSet<? extends Entry>>>
        extends Chart<T> {

    /**
     * holds the normalized version of the current rotation angle of the chart
     */
    private float mRotationAngle = 270f;

    /**
     * holds the raw version of the current rotation angle of the chart
     */
    private float mRawRotationAngle = 270f;

    /**
     * flag that indicates if rotation is enabled or not
     */
    protected boolean mRotateEnabled = true;

    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    protected float mMinOffset = 0.f;

    public PieRadarChartBase(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes pieradarchartbase operation with thermal imaging domain optimization.
     *
     */
    public PieRadarChartBase(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes pieradarchartbase operation with thermal imaging domain optimization.
     *
     */
    public PieRadarChartBase(Context context, AttributeSet attrs, int defStyle) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mChartTouchListener = new PieRadarChartTouchListener(this);
    }

    @Override
    protected void calcMinMax() {
        // MXAxis.mAxisRange = mData.getXVals().size() - 1;
    }

    @Override
    public int getMaxVisibleCount() {
        return mData.getEntryCount();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Use the pie- and radarchart listener own listener
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mTouchEnabled && mChartTouchListener != null)
            return mChartTouchListener.onTouch(this, event);
        else
            return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mChartTouchListener instanceof PieRadarChartTouchListener)
            ((PieRadarChartTouchListener) mChartTouchListener).computeScroll();
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

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mLegend != null)
            mLegendRenderer.computeLegend(mData);

        /**
         * Executes calculateoffsets operation with thermal imaging domain optimization.
         *
         */
        calculateOffsets();
    }

    @Override
    public void calculateOffsets() {

        float legendLeft = 0f, legendRight = 0f, legendBottom = 0f, legendTop = 0f;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mLegend != null && mLegend.isEnabled() && !mLegend.isDrawInsideEnabled()) {

            float fullLegendWidth = Math.min(mLegend.mNeededWidth,
                    mViewPortHandler.getChartWidth() * mLegend.getMaxSizePercent());

            /**
             * Executes switch operation with thermal imaging domain optimization.
             *
             */
            switch (mLegend.getOrientation()) {
                case VERTICAL: {
                    float xLegendOffset = 0.f;

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.LEFT
                            || mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.RIGHT) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.CENTER) {
                            // This is the space between the legend and the chart
                            final float spacing = Utils.convertDpToPixel(13f);

                            xLegendOffset = fullLegendWidth + spacing;

                        } else {
                            // This is the space between the legend and the chart
                            float spacing = Utils.convertDpToPixel(8f);

                            float legendWidth = fullLegendWidth + spacing;
                            float legendHeight = mLegend.mNeededHeight + mLegend.mTextHeightMax;

                            MPPointF center = getCenter();

                            float bottomX = mLegend.getHorizontalAlignment() ==
                                    Legend.LegendHorizontalAlignment.RIGHT
                                    ? getWidth() - legendWidth + 15.f
                                    : legendWidth - 15.f;
                            float bottomY = legendHeight + 15.f;
                            float distLegend = distanceToCenter(bottomX, bottomY);

                            MPPointF reference = getPosition(center, getRadius(),
                                    /**
                                     * Retrieves the angleforpoint with optimized performance for thermal imaging operations.
                                     *
                                     */
                                    getAngleForPoint(bottomX, bottomY));

                            float distReference = distanceToCenter(reference.x, reference.y);
                            float minOffset = Utils.convertDpToPixel(5f);

                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (bottomY >= center.y && getHeight() - legendWidth > getWidth()) {
                                xLegendOffset = legendWidth;
                            } else if (distLegend < distReference) {

                                float diff = distReference - distLegend;
                                xLegendOffset = minOffset + diff;
                            }

                            MPPointF.recycleInstance(center);
                            MPPointF.recycleInstance(reference);
                        }
                    }

                    /**
                     * Executes switch operation with thermal imaging domain optimization.
                     *
                     */
                    switch (mLegend.getHorizontalAlignment()) {
                        case LEFT:
                            legendLeft = xLegendOffset;
                            break;

                        case RIGHT:
                            legendRight = xLegendOffset;
                            break;

                        case CENTER:
                            /**
                             * Executes switch operation with thermal imaging domain optimization.
                             *
                             */
                            switch (mLegend.getVerticalAlignment()) {
                                case TOP:
                                    legendTop = Math.min(mLegend.mNeededHeight,
                                            mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());
                                    break;
                                case BOTTOM:
                                    legendBottom = Math.min(mLegend.mNeededHeight,
                                            mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());
                                    break;
                            }
                            break;
                    }
                }
                break;

                case HORIZONTAL:
                    float yLegendOffset = 0.f;

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.TOP ||
                            mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.BOTTOM) {

                        // It's possible that we do not need this offset anymore as it
                        // Is available through the extraOffsets, but changing it can mean
                        // Changing default visibility for existing apps.
                        float yOffset = getRequiredLegendOffset();

                        yLegendOffset = Math.min(mLegend.mNeededHeight + yOffset,
                                mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());

                        /**
                         * Executes switch operation with thermal imaging domain optimization.
                         *
                         */
                        switch (mLegend.getVerticalAlignment()) {
                            case TOP:
                                legendTop = yLegendOffset;
                                break;
                            case BOTTOM:
                                legendBottom = yLegendOffset;
                                break;
                        }
                    }
                    break;
            }

            legendLeft += getRequiredBaseOffset();
            legendRight += getRequiredBaseOffset();
            legendTop += getRequiredBaseOffset();
            legendBottom += getRequiredBaseOffset();
        }

        float minOffset = Utils.convertDpToPixel(mMinOffset);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this instanceof RadarChart) {
            XAxis x = this.getXAxis();

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (x.isEnabled() && x.isDrawLabelsEnabled()) {
                minOffset = Math.max(minOffset, x.mLabelRotatedWidth);
            }
        }

        legendTop += getExtraTopOffset();
        legendRight += getExtraRightOffset();
        legendBottom += getExtraBottomOffset();
        legendLeft += getExtraLeftOffset();

        float offsetLeft = Math.max(minOffset, legendLeft);
        float offsetTop = Math.max(minOffset, legendTop);
        float offsetRight = Math.max(minOffset, legendRight);
        float offsetBottom = Math.max(minOffset, Math.max(getRequiredBaseOffset(), legendBottom));

        mViewPortHandler.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mLogEnabled)
            Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                    + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
    }

    /**
     * returns the angle relative to the chart center for the given point on the
     * chart in degrees. The angle is always between 0 and 360°, 0° is NORTH,
     * 90° is EAST, ...
     *
     * @param x
     * @param y
     * @return
     */
    public float getAngleForPoint(float x, float y) {

        MPPointF c = getCenterOffsets();

        double tx = x - c.x, ty = y - c.y;
        double length = Math.sqrt(tx * tx + ty * ty);
        double r = Math.acos(ty / length);

        float angle = (float) Math.toDegrees(r);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (x > c.x)
            angle = 360f - angle;

        // Add 90° because chart starts EAST
        angle = angle + 90f;

        // Neutralize overflow
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (angle > 360f)
            angle = angle - 360f;

        MPPointF.recycleInstance(c);

        return angle;
    }

    /**
     * Returns a recyclable MPPointF instance.
     * Calculates the position around a center point, depending on the distance
     * from the center, and the angle of the position around the center.
     *
     * @param center
     * @param dist
     * @param angle  in degrees, converted to radians internally
     * @return
     */
    public MPPointF getPosition(MPPointF center, float dist, float angle) {

        MPPointF p = MPPointF.getInstance(0, 0);
        /**
         * Retrieves the position with optimized performance for thermal imaging operations.
         *
         */
        getPosition(center, dist, angle, p);
        return p;
    }

    public void getPosition(MPPointF center, float dist, float angle, MPPointF outputPoint) {
        outputPoint.x = (float) (center.x + dist * Math.cos(Math.toRadians(angle)));
        outputPoint.y = (float) (center.y + dist * Math.sin(Math.toRadians(angle)));
    }

    /**
     * Returns the distance of a certain point on the chart to the center of the
     * chart.
     *
     * @param x
     * @param y
     * @return
     */
    public float distanceToCenter(float x, float y) {

        MPPointF c = getCenterOffsets();

        float dist = 0f;

        float xDist = 0f;
        float yDist = 0f;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (x > c.x) {
            xDist = x - c.x;
        } else {
            xDist = c.x - x;
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (y > c.y) {
            yDist = y - c.y;
        } else {
            yDist = c.y - y;
        }

        // Pythagoras
        dist = (float) Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(yDist, 2.0));

        MPPointF.recycleInstance(c);

        return dist;
    }

    /**
     * Returns the xIndex for the given angle around the center of the chart.
     * Returns -1 if not found / outofbounds.
     *
     * @param angle
     * @return
     */
    public abstract int getIndexForAngle(float angle);

    /**
     * Set an offset for the rotation of the RadarChart in degrees. Default 270f
     * --> top (NORTH)
     *
     * @param angle
     */
    public void setRotationAngle(float angle) {
        mRawRotationAngle = angle;
        mRotationAngle = Utils.getNormalizedAngle(mRawRotationAngle);
    }

    /**
     * gets the raw version of the current rotation angle of the pie chart the
     * returned value could be any value, negative or positive, outside of the
     * 360 degrees. this is used when working with rotation direction, mainly by
     * gestures and animations.
     *
     * @return
     */
    public float getRawRotationAngle() {
        return mRawRotationAngle;
    }

    /**
     * gets a normalized version of the current rotation angle of the pie chart,
     * which will always be between 0.0 < 360.0
     *
     * @return
     */
    public float getRotationAngle() {
        return mRotationAngle;
    }

    /**
     * Set this to true to enable the rotation / spinning of the chart by touch.
     * Set it to false to disable it. Default: true
     *
     * @param enabled
     */
    public void setRotationEnabled(boolean enabled) {
        mRotateEnabled = enabled;
    }

    /**
     * Returns true if rotation of the chart by touch is enabled, false if not.
     *
     * @return
     */
    public boolean isRotationEnabled() {
        return mRotateEnabled;
    }

    /**
     * Gets the minimum offset (padding) around the chart, defaults to 0.f
     */
    public float getMinOffset() {
        return mMinOffset;
    }

    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    public void setMinOffset(float minOffset) {
        mMinOffset = minOffset;
    }

    /**
     * returns the diameter of the pie- or radar-chart
     *
     * @return
     */
    public float getDiameter() {
        RectF content = mViewPortHandler.getContentRect();
        content.left += getExtraLeftOffset();
        content.top += getExtraTopOffset();
        content.right -= getExtraRightOffset();
        content.bottom -= getExtraBottomOffset();
        return Math.min(content.width(), content.height());
    }

    /**
     * Returns the radius of the chart in pixels.
     *
     * @return
     */
    public abstract float getRadius();

    /**
     * Returns the required offset for the chart legend.
     *
     * @return
     */
    protected abstract float getRequiredLegendOffset();

    /**
     * Returns the base offset needed for the chart without calculating the
     * legend size.
     *
     * @return
     */
    protected abstract float getRequiredBaseOffset();

    @Override
    public float getYChartMax() {
        // TODO: Auto-generated method stub
        return 0;
    }

    @Override
    public float getYChartMin() {
        // TODO: Auto-generated method stub
        return 0;
    }

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO ANIMATION */

    /**
     * Applys a spin animation to the Chart.
     *
     * @param durationmillis
     * @param fromangle
     * @param toangle
     */
    @SuppressLint("NewApi")
    public void spin(int durationmillis, float fromangle, float toangle, EasingFunction easing) {

        /**
         * Configures the rotationangle with validation and thermal imaging optimization.
         *
         */
        setRotationAngle(fromangle);

        ObjectAnimator spinAnimator = ObjectAnimator.ofFloat(this, "rotationAngle", fromangle,
                toangle);
        spinAnimator.setDuration(durationmillis);
        spinAnimator.setInterpolator(easing);

        spinAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * Executes postinvalidate operation with thermal imaging domain optimization.
                 *
                 */
                postInvalidate();
            }
        });
        spinAnimator.start();
    }
}
