
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * Specialized thermal imaging component providing YAxisRendererHorizontalBarChart functionality for the IRCamera system.
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
public class YAxisRendererHorizontalBarChart extends YAxisRenderer {

    /**
     * Executes yaxisrendererhorizontalbarchart operation with thermal imaging domain optimization.
     *
     */
    public YAxisRendererHorizontalBarChart(ViewPortHandler viewPortHandler, YAxis yAxis,
                                           Transformer trans) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(viewPortHandler, yAxis, trans);

        mLimitLinePaint.setTextAlign(Align.LEFT);
    }

    /**
     * Computes the axis values.
     *
     * @param yMin - the minimum y-value in the data object for this axis
     * @param yMax - the maximum y-value in the data object for this axis
     */
    @Override
    public void computeAxis(float yMin, float yMax, boolean inverted) {

        // Calculate the starting and entry point of the y-labels (depending on
        // Zoom / contentrect bounds)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mViewPortHandler.contentHeight() > 10 && !mViewPortHandler.isFullyZoomedOutX()) {

            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentRight(),
                    mViewPortHandler.contentTop());

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!inverted) {
                yMin = (float) p1.x;
                yMax = (float) p2.x;
            } else {
                yMin = (float) p2.x;
                yMax = (float) p1.x;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        /**
         * Executes computeaxisvalues operation with thermal imaging domain optimization.
         *
         */
        computeAxisValues(yMin, yMax);
    }

    /**
     * draws the y-axis labels to the screen
     */
    @Override
    public void renderAxisLabels(Canvas c) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled())
            return;

        float[] positions = getTransformedPositions();

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());
        mAxisLabelPaint.setTextAlign(Align.CENTER);

        float baseYOffset = Utils.convertDpToPixel(2.5f);
        float textHeight = Utils.calcTextHeight(mAxisLabelPaint, "Q");

        AxisDependency dependency = mYAxis.getAxisDependency();
        YAxisLabelPosition labelPosition = mYAxis.getLabelPosition();

        float yPos = 0f;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dependency == AxisDependency.LEFT) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                yPos = mViewPortHandler.contentTop() - baseYOffset;
            } else {
                yPos = mViewPortHandler.contentTop() - baseYOffset;
            }

        } else {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                yPos = mViewPortHandler.contentBottom() + textHeight + baseYOffset;
            } else {
                yPos = mViewPortHandler.contentBottom() + textHeight + baseYOffset;
            }
        }

        /**
         * Executes drawylabels operation with thermal imaging domain optimization.
         *
         */
        drawYLabels(c, yPos, positions, mYAxis.getYOffset());
    }

    @Override
    public void renderAxisLine(Canvas c) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled())
            return;

        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mYAxis.getAxisDependency() == AxisDependency.LEFT) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentTop(), mAxisLinePaint);
        } else {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
        }
    }

    /**
     * draws the y-labels on the specified x-position
     *
     * @param fixedPosition
     * @param positions
     */
    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = from; i < to; i++) {

            String text = mYAxis.getFormattedLabel(i);

            c.drawText(text, positions[i * 2], fixedPosition - offset, mAxisLabelPaint);
        }
    }

    @Override
    protected float[] getTransformedPositions() {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if(mGetTransformedPositionsBuffer.length != mYAxis.mEntryCount * 2) {
            mGetTransformedPositionsBuffer = new float[mYAxis.mEntryCount * 2];
        }
        float[] positions = mGetTransformedPositionsBuffer;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < positions.length; i += 2) {
            // Only fill x values, y values are not needed for x-labels
            positions[i] = mYAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);
        return positions;
    }

    @Override
    public RectF getGridClippingRect() {
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(-mAxis.getGridLineWidth(), 0.f);
        return mGridClippingRect;
    }

    @Override
    protected Path linePath(Path p, int i, float[] positions) {

        p.moveTo(positions[i], mViewPortHandler.contentTop());
        p.lineTo(positions[i], mViewPortHandler.contentBottom());

        return p;
    }

    protected Path mDrawZeroLinePathBuffer = new Path();

    @Override
    protected void drawZeroLine(Canvas c) {

        int clipRestoreCount = c.save();
        mZeroLineClippingRect.set(mViewPortHandler.getContentRect());
        mZeroLineClippingRect.inset(-mYAxis.getZeroLineWidth(), 0.f);
        c.clipRect(mLimitLineClippingRect);

        // Draw zero line
        MPPointD pos = mTrans.getPixelForValues(0f, 0f);

        mZeroLinePaint.setColor(mYAxis.getZeroLineColor());
        mZeroLinePaint.setStrokeWidth(mYAxis.getZeroLineWidth());

        Path zeroLinePath = mDrawZeroLinePathBuffer;
        zeroLinePath.reset();

        zeroLinePath.moveTo((float) pos.x - 1, mViewPortHandler.contentTop());
        zeroLinePath.lineTo((float) pos.x - 1, mViewPortHandler.contentBottom());

        // Draw a path because lines don't support dashing on lower android versions
        c.drawPath(zeroLinePath, mZeroLinePaint);

        c.restoreToCount(clipRestoreCount);
    }

    protected Path mRenderLimitLinesPathBuffer = new Path();
    protected float[] mRenderLimitLinesBuffer = new float[4];
    /**
     * Draws the LimitLines associated with this axis to the screen.
     * This is the standard XAxis renderer using the YAxis limit lines.
     *
     * @param c
     */
    @Override
    public void renderLimitLines(Canvas c) {

        List<LimitLine> limitLines = mYAxis.getLimitLines();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (limitLines == null || limitLines.size() <= 0)
            return;

        float[] pts = mRenderLimitLinesBuffer;
        pts[0] = 0;
        pts[1] = 0;
        pts[2] = 0;
        pts[3] = 0;
        Path limitLinePath = mRenderLimitLinesPathBuffer;
        limitLinePath.reset();

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

            int clipRestoreCount = c.save();
            mLimitLineClippingRect.set(mViewPortHandler.getContentRect());
            mLimitLineClippingRect.inset(-l.getLineWidth(), 0.f);
            c.clipRect(mLimitLineClippingRect);

            pts[0] = l.getLimit();
            pts[2] = l.getLimit();

            mTrans.pointValuesToPixel(pts);

            pts[1] = mViewPortHandler.contentTop();
            pts[3] = mViewPortHandler.contentBottom();

            limitLinePath.moveTo(pts[0], pts[1]);
            limitLinePath.lineTo(pts[2], pts[3]);

            mLimitLinePaint.setStyle(Paint.Style.STROKE);
            mLimitLinePaint.setColor(l.getLineColor());
            mLimitLinePaint.setPathEffect(l.getDashPathEffect());
            mLimitLinePaint.setStrokeWidth(l.getLineWidth());

            c.drawPath(limitLinePath, mLimitLinePaint);
            limitLinePath.reset();

            String label = l.getLabel();

            // If drawing the limit-value label is enabled
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (label != null && !label.equals("")) {

                mLimitLinePaint.setStyle(l.getTextStyle());
                mLimitLinePaint.setPathEffect(null);
                mLimitLinePaint.setColor(l.getTextColor());
                mLimitLinePaint.setTypeface(l.getTypeface());
                mLimitLinePaint.setStrokeWidth(0.5f);
                mLimitLinePaint.setTextSize(l.getTextSize());

                float xOffset = l.getLineWidth() + l.getXOffset();
                float yOffset = Utils.convertDpToPixel(2f) + l.getYOffset();

                final LimitLine.LimitLabelPosition position = l.getLabelPosition();

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {

                    final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    c.drawText(label, pts[0] + xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
                } else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {

                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    c.drawText(label, pts[0] + xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
                } else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {

                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                    c.drawText(label, pts[0] - xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
                } else {

                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    c.drawText(label, pts[0] - xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
                }
            }

            c.restoreToCount(clipRestoreCount);
        }
    }
}
