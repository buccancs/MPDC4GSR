package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * Specialized thermal imaging component providing YAxisRendererRadarChart functionality for the IRCamera system.
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
public class YAxisRendererRadarChart extends YAxisRenderer {

    private RadarChart mChart;

    /**
     * Executes yaxisrendererradarchart operation with thermal imaging domain optimization.
     *
     */
    public YAxisRendererRadarChart(ViewPortHandler viewPortHandler, YAxis yAxis, RadarChart chart) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(viewPortHandler, yAxis, null);

        this.mChart = chart;
    }

    @Override
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

        boolean centeringEnabled = mAxis.isCenterAxisLabelsEnabled();
        int n = centeringEnabled ? 1 : 0;

        // Force label count
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mAxis.isForceLabelsEnabled()) {

            float step = (float) range / (float) (labelCount - 1);
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
                v += step;
            }

            n = labelCount;

            // No forced count
        } else {

            double first = interval == 0.0 ? 0.0 : Math.ceil(yMin / interval) * interval;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (centeringEnabled) {
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

            n++;

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
        if (centeringEnabled) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mAxis.mCenteredEntries.length < n) {
                mAxis.mCenteredEntries = new float[n];
            }

            float offset = (mAxis.mEntries[1] - mAxis.mEntries[0]) / 2f;

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < n; i++) {
                mAxis.mCenteredEntries[i] = mAxis.mEntries[i] + offset;
            }
        }

        mAxis.mAxisMinimum = mAxis.mEntries[0];
        mAxis.mAxisMaximum = mAxis.mEntries[n-1];
        mAxis.mAxisRange = Math.abs(mAxis.mAxisMaximum - mAxis.mAxisMinimum);
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled())
            return;

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0,0);
        float factor = mChart.getFactor();

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int j = from; j < to; j++) {

            float r = (mYAxis.mEntries[j] - mYAxis.mAxisMinimum) * factor;

            Utils.getPosition(center, r, mChart.getRotationAngle(), pOut);

            String label = mYAxis.getFormattedLabel(j);

            c.drawText(label, pOut.x + 10, pOut.y, mAxisLabelPaint);
        }
        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
    }

    private Path mRenderLimitLinesPathBuffer = new Path();
    @Override
    public void renderLimitLines(Canvas c) {

        List<LimitLine> limitLines = mYAxis.getLimitLines();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (limitLines == null)
            return;

        float sliceangle = mChart.getSliceAngle();

        // Calculate the factor that is needed for transforming the value to
        // Pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0,0);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine l = limitLines.get(i);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!l.isEnabled())
                continue;

            mLimitLinePaint.setColor(l.getLineColor());
            mLimitLinePaint.setPathEffect(l.getDashPathEffect());
            mLimitLinePaint.setStrokeWidth(l.getLineWidth());

            float r = (l.getLimit() - mChart.getYChartMin()) * factor;

            Path limitPath = mRenderLimitLinesPathBuffer;
            limitPath.reset();

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int j = 0; j < mChart.getData().getMaxEntryCountSet().getEntryCount(); j++) {

                Utils.getPosition(center, r, sliceangle * j + mChart.getRotationAngle(), pOut);

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (j == 0)
                    limitPath.moveTo(pOut.x, pOut.y);
                else
                    limitPath.lineTo(pOut.x, pOut.y);
            }
            limitPath.close();

            c.drawPath(limitPath, mLimitLinePaint);
        }
        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
    }
}
