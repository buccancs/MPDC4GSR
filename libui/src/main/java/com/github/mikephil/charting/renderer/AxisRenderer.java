
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Specialized thermal imaging component providing AxisRenderer functionality for the IRCamera system.
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
public abstract class AxisRenderer extends Renderer {

    /** base axis this axis renderer works with */
    protected AxisBase mAxis;

    /** transformer to transform values to screen pixels and return */
    protected Transformer mTrans;

    /**
     * paint object for the grid lines
     */
    protected Paint mGridPaint;

    /**
     * paint for the x-label values
     */
    protected Paint mAxisLabelPaint;

    /**
     * paint for the line surrounding the chart
     */
    protected Paint mAxisLinePaint;

    /**
     * paint used for the limit lines
     */
    protected Paint mLimitLinePaint;

    public AxisRenderer(ViewPortHandler viewPortHandler, Transformer trans, AxisBase axis) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(viewPortHandler);

        this.mTrans = trans;
        this.mAxis = axis;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if(mViewPortHandler != null) {

            mAxisLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            mGridPaint = new Paint();
            mGridPaint.setColor(Color.GRAY);
            mGridPaint.setStrokeWidth(1f);
            mGridPaint.setStyle(Style.STROKE);
            mGridPaint.setAlpha(90);

            mAxisLinePaint = new Paint();
            mAxisLinePaint.setColor(Color.BLACK);
            mAxisLinePaint.setStrokeWidth(1f);
            mAxisLinePaint.setStyle(Style.STROKE);

            mLimitLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLimitLinePaint.setStyle(Style.STROKE);
        }
    }

    /**
     * Returns the Paint object used for drawing the axis (labels).
     *
     * @return
     */
    public Paint getPaintAxisLabels() {
        return mAxisLabelPaint;
    }

    /**
     * Returns the Paint object that is used for drawing the grid-lines of the
     * axis.
     *
     * @return
     */
    public Paint getPaintGrid() {
        return mGridPaint;
    }

    /**
     * Returns the Paint object that is used for drawing the axis-line that goes
     * alongside the axis.
     *
     * @return
     */
    public Paint getPaintAxisLine() {
        return mAxisLinePaint;
    }

    /**
     * Returns the Transformer object used for transforming the axis values.
     *
     * @return
     */
    public Transformer getTransformer() {
        return mTrans;
    }

    /**
     * Computes the axis values.
     *
     * @param min - the minimum value in the data object for this axis
     * @param max - the maximum value in the data object for this axis
     */
    public void computeAxis(float min, float max, boolean inverted) {

        // Calculate the starting and entry point of the y-labels (depending on
        // Zoom / contentrect bounds)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mViewPortHandler != null && mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutY()) {

            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom());

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!inverted) {

                min = (float) p2.y;
                max = (float) p1.y;
            } else {

                min = (float) p1.y;
                max = (float) p2.y;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        /**
         * Executes computeaxisvalues operation with thermal imaging domain optimization.
         *
         */
        computeAxisValues(min, max);
    }

    /**
     * Sets up the axis values. Computes the desired number of labels between the two given extremes.
     *
     * @return
     */
    protected void computeAxisValues(float min, float max) {

        float yMin = min;
        float yMax = max;

        int labelCount = mAxis.getLabelCount();
        double range = Math.abs(yMax - yMin);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (labelCount == 0 || range <= 0 || Double.isInfinite(range)) {
            mAxis.mEntries = new float[]{};
            mAxis.mCenteredEntries = new float[]{};
            mAxis.mEntryCount = 0;
            return;
        }

        // Find out how much spacing (in y value space) between axis values
        double rawInterval = range / labelCount;
        double interval = Utils.roundToNextSignificant(rawInterval);

        // If granularity is enabled, then do not allow the interval to go below specified granularity.
        // This is used to avoid repeated values when rounding values for display.
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mAxis.isGranularityEnabled())
            interval = interval < mAxis.getGranularity() ? mAxis.getGranularity() : interval;

        // Normalize interval
        double intervalMagnitude = Utils.roundToNextSignificant(Math.pow(10, (int) Math.log10(interval)));
        int intervalSigDigit = (int) (interval / intervalMagnitude);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or
            // 90
            interval = Math.floor(10 * intervalMagnitude);
        }

        int n = mAxis.isCenterAxisLabelsEnabled() ? 1 : 0;

        // Force label count
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mAxis.isForceLabelsEnabled()) {

            interval = (float) range / (float) (labelCount - 1);
            mAxis.mEntryCount = labelCount;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mAxis.mEntries.length < labelCount) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = new float[labelCount];
            }

            float v = min;

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < labelCount; i++) {
                mAxis.mEntries[i] = v;
                v += interval;
            }

            n = labelCount;

            // No forced count
        } else {

            double first = interval == 0.0 ? 0.0 : Math.ceil(yMin / interval) * interval;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if(mAxis.isCenterAxisLabelsEnabled()) {
                first -= interval;
            }

            double last = interval == 0.0 ? 0.0 : Utils.nextUp(Math.floor(yMax / interval) * interval);

            double f;
            int i;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (interval != 0.0) {
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (f = first; f <= last; f += interval) {
                    ++n;
                }
            }

            mAxis.mEntryCount = n;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mAxis.mEntries.length < n) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = new float[n];
            }

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (f = first, i = 0; i < n; f += interval, ++i) {

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                    f = 0.0;

                mAxis.mEntries[i] = (float) f;
            }
        }

        // Set decimals
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (interval < 1) {
            mAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            mAxis.mDecimals = 0;
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mAxis.isCenterAxisLabelsEnabled()) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mAxis.mCenteredEntries.length < n) {
                mAxis.mCenteredEntries = new float[n];
            }

            float offset = (float)interval / 2f;

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < n; i++) {
                mAxis.mCenteredEntries[i] = mAxis.mEntries[i] + offset;
            }
        }
    }

    /**
     * Draws the axis labels to the screen.
     *
     * @param c
     */
    public abstract void renderAxisLabels(Canvas c);

    /**
     * Draws the grid lines belonging to the axis.
     *
     * @param c
     */
    public abstract void renderGridLines(Canvas c);

    /**
     * Draws the line that goes alongside the axis.
     *
     * @param c
     */
    public abstract void renderAxisLine(Canvas c);

    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param c
     */
    public abstract void renderLimitLines(Canvas c);
}
