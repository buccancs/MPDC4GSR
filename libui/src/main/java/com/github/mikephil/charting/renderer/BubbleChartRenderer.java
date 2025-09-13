package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * Specialized thermal imaging component providing BubbleChartRenderer functionality for the IRCamera system.
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
public class BubbleChartRenderer extends BarLineScatterCandleBubbleRenderer {

    protected BubbleDataProvider mChart;

    /**
     * Executes bubblechartrenderer operation with thermal imaging domain optimization.
     *
     */
    public BubbleChartRenderer(BubbleDataProvider chart, ChartAnimator animator,
                               ViewPortHandler viewPortHandler) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(animator, viewPortHandler);
        mChart = chart;

        mRenderPaint.setStyle(Style.FILL);

        mHighlightPaint.setStyle(Style.STROKE);
        mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(1.5f));
    }

    @Override
    public void initBuffers() {

    }

    @Override
    public void drawData(Canvas c) {

        BubbleData bubbleData = mChart.getBubbleData();

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: bubbleData.getDataSets()
         *
         */
        for (IBubbleDataSet set : bubbleData.getDataSets()) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (set.isVisible())
                /**
                 * Executes drawdataset operation with thermal imaging domain optimization.
                 *
                 */
                drawDataSet(c, set);
        }
    }

    private float[] sizeBuffer = new float[4];
    private float[] pointBuffer = new float[2];

    protected float getShapeSize(float entrySize, float maxSize, float reference, boolean normalizeSize) {
        final float factor = normalizeSize ? ((maxSize == 0f) ? 1f : (float) Math.sqrt(entrySize / maxSize)) :
                entrySize;
        final float shapeSize = reference * factor;
        return shapeSize;
    }

    protected void drawDataSet(Canvas c, IBubbleDataSet dataSet) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dataSet.getEntryCount() < 1)
            return;

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        mXBounds.set(mChart, dataSet);

        sizeBuffer[0] = 0f;
        sizeBuffer[2] = 1f;

        trans.pointValuesToPixel(sizeBuffer);

        boolean normalizeSize = dataSet.isNormalizeSizeEnabled();

        // Calcualte the full width of 1 step on the x-axis
        final float maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
        final float maxBubbleHeight = Math.abs(mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
        final float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {

            final BubbleEntry entry = dataSet.getEntryForIndex(j);

            pointBuffer[0] = entry.getX();
            pointBuffer[1] = (entry.getY()) * phaseY;
            trans.pointValuesToPixel(pointBuffer);

            float shapeHalf = getShapeSize(entry.getSize(), dataSet.getMaxSize(), referenceSize, normalizeSize) / 2f;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf)
                    || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf))
                continue;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf))
                continue;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf))
                break;

            final int color = dataSet.getColor((int) entry.getX());

            mRenderPaint.setColor(color);
            c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mRenderPaint);
        }
    }

    @Override
    public void drawValues(Canvas c) {

        BubbleData bubbleData = mChart.getBubbleData();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bubbleData == null)
            return;

        // If values are drawn
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isDrawingValuesAllowed(mChart)) {

            final List<IBubbleDataSet> dataSets = bubbleData.getDataSets();

            float lineHeight = Utils.calcTextHeight(mValuePaint, "1");

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < dataSets.size(); i++) {

                IBubbleDataSet dataSet = dataSets.get(i);

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!shouldDrawValues(dataSet) || dataSet.getEntryCount() < 1)
                    continue;

                // Apply the text-styling defined by the DataSet
                /**
                 * Executes applyvaluetextstyle operation with thermal imaging domain optimization.
                 *
                 */
                applyValueTextStyle(dataSet);

                final float phaseX = Math.max(0.f, Math.min(1.f, mAnimator.getPhaseX()));
                final float phaseY = mAnimator.getPhaseY();

                mXBounds.set(mChart, dataSet);

                final float[] positions = mChart.getTransformer(dataSet.getAxisDependency())
                        .generateTransformedValuesBubble(dataSet, phaseY, mXBounds.min, mXBounds.max);

                final float alpha = phaseX == 1 ? phaseY : phaseX;

                ValueFormatter formatter = dataSet.getValueFormatter();

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int j = 0; j < positions.length; j += 2) {

                    int valueTextColor = dataSet.getValueTextColor(j / 2 + mXBounds.min);
                    valueTextColor = Color.argb(Math.round(255.f * alpha), Color.red(valueTextColor),
                            Color.green(valueTextColor), Color.blue(valueTextColor));

                    float x = positions[j];
                    float y = positions[j + 1];

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!mViewPortHandler.isInBoundsRight(x))
                        break;

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if ((!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y)))
                        continue;

                    BubbleEntry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataSet.isDrawValuesEnabled()) {
                        /**
                         * Executes drawvalue operation with thermal imaging domain optimization.
                         *
                         */
                        drawValue(c, formatter.getBubbleLabel(entry), x, y + (0.5f * lineHeight), valueTextColor);
                    }

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                        Drawable icon = entry.getIcon();

                        Utils.drawImage(
                                c,
                                icon,
                                (int)(x + iconsOffset.x),
                                (int)(y + iconsOffset.y),
                                icon.getIntrinsicWidth(),
                                icon.getIntrinsicHeight());
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    @Override
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        mValuePaint.setColor(color);
        c.drawText(valueText, x, y, mValuePaint);
    }

    @Override
    public void drawExtras(Canvas c) {
    }

    private float[] _hsvBuffer = new float[3];

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        BubbleData bubbleData = mChart.getBubbleData();

        float phaseY = mAnimator.getPhaseY();

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param high Parameter for operation (type: indices)
         *
         */
        for (Highlight high : indices) {

            IBubbleDataSet set = bubbleData.getDataSetByIndex(high.getDataSetIndex());

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (set == null || !set.isHighlightEnabled())
                continue;

            final BubbleEntry entry = set.getEntryForXValue(high.getX(), high.getY());

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (entry.getY() != high.getY())
                continue;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isInBoundsX(entry, set))
                continue;

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            sizeBuffer[0] = 0f;
            sizeBuffer[2] = 1f;

            trans.pointValuesToPixel(sizeBuffer);

            boolean normalizeSize = set.isNormalizeSizeEnabled();

            // Calcualte the full width of 1 step on the x-axis
            final float maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
            final float maxBubbleHeight = Math.abs(
                    mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
            final float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

            pointBuffer[0] = entry.getX();
            pointBuffer[1] = (entry.getY()) * phaseY;
            trans.pointValuesToPixel(pointBuffer);

            high.setDraw(pointBuffer[0], pointBuffer[1]);

            float shapeHalf = getShapeSize(entry.getSize(),
                    set.getMaxSize(),
                    referenceSize,
                    normalizeSize) / 2f;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf)
                    || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf))
                continue;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf))
                continue;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf))
                break;

            final int originalColor = set.getColor((int) entry.getX());

            Color.RGBToHSV(Color.red(originalColor), Color.green(originalColor),
                    Color.blue(originalColor), _hsvBuffer);
            _hsvBuffer[2] *= 0.5f;
            final int color = Color.HSVToColor(Color.alpha(originalColor), _hsvBuffer);

            mHighlightPaint.setColor(color);
            mHighlightPaint.setStrokeWidth(set.getHighlightCircleWidth());
            c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mHighlightPaint);
        }
    }
}
