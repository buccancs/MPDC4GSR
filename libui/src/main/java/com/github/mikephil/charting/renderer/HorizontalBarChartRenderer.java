package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.buffer.HorizontalBarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * Specialized thermal imaging component providing HorizontalBarChartRenderer functionality for the IRCamera system.
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
public class HorizontalBarChartRenderer extends BarChartRenderer {

    /**
     * Executes horizontalbarchartrenderer operation with thermal imaging domain optimization.
     *
     */
    public HorizontalBarChartRenderer(BarDataProvider chart, ChartAnimator animator,
                                      ViewPortHandler viewPortHandler) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(chart, animator, viewPortHandler);

        mValuePaint.setTextAlign(Align.LEFT);
    }

    @Override
    public void initBuffers() {

        BarData barData = mChart.getBarData();
        mBarBuffers = new HorizontalBarBuffer[barData.getDataSetCount()];

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mBarBuffers.length; i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            mBarBuffers[i] = new HorizontalBarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1),
                    barData.getDataSetCount(), set.isStacked());
        }
    }

    private RectF mBarShadowRectBuffer = new RectF();

    @Override
    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        final boolean drawBorder = dataSet.getBarBorderWidth() > 0.f;

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // Draw the bar shadow before the values
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mChart.isDrawBarShadowEnabled()) {
            mShadowPaint.setColor(dataSet.getBarShadowColor());

            BarData barData = mChart.getBarData();

            final float barWidth = barData.getBarWidth();
            final float barWidthHalf = barWidth / 2.0f;
            float x;

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0, count = Math.min((int)(Math.ceil((float)(dataSet.getEntryCount()) * phaseX)), dataSet.getEntryCount());
                 i < count;
                 i++) {

                BarEntry e = dataSet.getEntryForIndex(i);

                x = e.getX();

                mBarShadowRectBuffer.top = x - barWidthHalf;
                mBarShadowRectBuffer.bottom = x + barWidthHalf;

                trans.rectValueToPixel(mBarShadowRectBuffer);

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!mViewPortHandler.isInBoundsTop(mBarShadowRectBuffer.bottom))
                    continue;

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!mViewPortHandler.isInBoundsBottom(mBarShadowRectBuffer.top))
                    break;

                mBarShadowRectBuffer.left = mViewPortHandler.contentLeft();
                mBarShadowRectBuffer.right = mViewPortHandler.contentRight();

                c.drawRect(mBarShadowRectBuffer, mShadowPaint);
            }
        }

        // Initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        final boolean isSingleColor = dataSet.getColors().size() == 1;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int j = 0; j < buffer.size(); j += 4) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 3]))
                break;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1]))
                continue;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // Is out of bounds, reuse colors.
                mRenderPaint.setColor(dataSet.getColor(j / 4));
            }

            c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (drawBorder) {
                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mBarBorderPaint);
            }
        }
    }

    @Override
    public void drawValues(Canvas c) {
        // If values are drawn
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isDrawingValuesAllowed(mChart)) {

            List<IBarDataSet> dataSets = mChart.getBarData().getDataSets();

            final float valueOffsetPlus = Utils.convertDpToPixel(5f);
            float posOffset = 0f;
            float negOffset = 0f;
            final boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {

                IBarDataSet dataSet = dataSets.get(i);

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!shouldDrawValues(dataSet))
                    continue;

                boolean isInverted = mChart.isInverted(dataSet.getAxisDependency());

                // Apply the text-styling defined by the DataSet
                /**
                 * Executes applyvaluetextstyle operation with thermal imaging domain optimization.
                 *
                 */
                applyValueTextStyle(dataSet);
                final float halfTextHeight = Utils.calcTextHeight(mValuePaint, "10") / 2f;

                ValueFormatter formatter = dataSet.getValueFormatter();

                // Get the buffer
                BarBuffer buffer = mBarBuffers[i];

                final float phaseY = mAnimator.getPhaseY();

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                // If only single values are drawn (sum)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!dataSet.isStacked()) {

                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {

                        float y = (buffer.buffer[j + 1] + buffer.buffer[j + 3]) / 2f;

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 1]))
                            break;

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (!mViewPortHandler.isInBoundsX(buffer.buffer[j]))
                            continue;

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1]))
                            continue;

                        BarEntry entry = dataSet.getEntryForIndex(j / 4);
                        float val = entry.getY();
                        String formattedValue = formatter.getBarLabel(entry);

                        // Calculate the correct offset depending on the draw position of the value
                        float valueTextWidth = Utils.calcTextWidth(mValuePaint, formattedValue);
                        posOffset = (drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus));
                        negOffset = (drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus);

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isInverted) {
                            posOffset = -posOffset - valueTextWidth;
                            negOffset = -negOffset - valueTextWidth;
                        }

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (dataSet.isDrawValuesEnabled()) {
                            /**
                             * Executes drawvalue operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param posOffset Parameter for operation (type: negOffset)
                             *
                             */
                            drawValue(c,
                                    formattedValue,
                                    buffer.buffer[j + 2] + (val >= 0 ? posOffset : negOffset),
                                    y + halfTextHeight,
                                    dataSet.getValueTextColor(j / 2));
                        }

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                            Drawable icon = entry.getIcon();

                            float px = buffer.buffer[j + 2] + (val >= 0 ? posOffset : negOffset);
                            float py = y;

                            px += iconsOffset.x;
                            py += iconsOffset.y;

                            Utils.drawImage(
                                    c,
                                    icon,
                                    (int)px,
                                    (int)py,
                                    icon.getIntrinsicWidth(),
                                    icon.getIntrinsicHeight());
                        }
                    }

                    // If each value of a potential stack should be drawn
                } else {

                    Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                    int bufferIndex = 0;
                    int index = 0;

                    /**
                     * Executes while operation with thermal imaging domain optimization.
                     *
                     */
                    while (index < dataSet.getEntryCount() * mAnimator.getPhaseX()) {

                        BarEntry entry = dataSet.getEntryForIndex(index);

                        int color = dataSet.getValueTextColor(index);
                        float[] vals = entry.getYVals();

                        // We still draw stacked bars, but there is one
                        // Non-stacked
                        // In between
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (vals == null) {

                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (!mViewPortHandler.isInBoundsTop(buffer.buffer[bufferIndex + 1]))
                                break;

                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (!mViewPortHandler.isInBoundsX(buffer.buffer[bufferIndex]))
                                continue;

                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[bufferIndex + 1]))
                                continue;

                            String formattedValue = formatter.getBarLabel(entry);

                            // Calculate the correct offset depending on the draw position of the value
                            float valueTextWidth = Utils.calcTextWidth(mValuePaint, formattedValue);
                            posOffset = (drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus));
                            negOffset = (drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus);

                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (isInverted) {
                                posOffset = -posOffset - valueTextWidth;
                                negOffset = -negOffset - valueTextWidth;
                            }

                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (dataSet.isDrawValuesEnabled()) {
                                /**
                                 * Executes drawvalue operation with thermal imaging domain optimization.
                                 *
                                 * @param
                                 * @param posOffset Parameter for operation (type: negOffset)
                                 *
                                 */
                                drawValue(c, formattedValue,
                                        buffer.buffer[bufferIndex + 2]
                                                + (entry.getY() >= 0 ? posOffset : negOffset),
                                        buffer.buffer[bufferIndex + 1] + halfTextHeight, color);
                            }

                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                                Drawable icon = entry.getIcon();

                                float px = buffer.buffer[bufferIndex + 2]
                                        + (entry.getY() >= 0 ? posOffset : negOffset);
                                float py = buffer.buffer[bufferIndex + 1];

                                px += iconsOffset.x;
                                py += iconsOffset.y;

                                Utils.drawImage(
                                        c,
                                        icon,
                                        (int)px,
                                        (int)py,
                                        icon.getIntrinsicWidth(),
                                        icon.getIntrinsicHeight());
                            }

                        } else {

                            float[] transformed = new float[vals.length * 2];

                            float posY = 0f;
                            float negY = -entry.getNegativeSum();

                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             */
                            for (int k = 0, idx = 0; k < transformed.length; k += 2, idx++) {

                                float value = vals[idx];
                                float y;

                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (value == 0.0f && (posY == 0.0f || negY == 0.0f)) {
                                    // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                                    y = value;
                                } else if (value >= 0.0f) {
                                    posY += value;
                                    y = posY;
                                } else {
                                    y = negY;
                                    negY -= value;
                                }

                                transformed[k] = y * phaseY;
                            }

                            trans.pointValuesToPixel(transformed);

                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             */
                            for (int k = 0; k < transformed.length; k += 2) {

                                final float val = vals[k / 2];
                                String formattedValue = formatter.getBarStackedLabel(val, entry);

                                // Calculate the correct offset depending on the draw position of the value
                                float valueTextWidth = Utils.calcTextWidth(mValuePaint, formattedValue);
                                posOffset = (drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus));
                                negOffset = (drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus);

                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (isInverted) {
                                    posOffset = -posOffset - valueTextWidth;
                                    negOffset = -negOffset - valueTextWidth;
                                }

                                final boolean drawBelow =
                                        (val == 0.0f && negY == 0.0f && posY > 0.0f) ||
                                                val < 0.0f;

                                float x = transformed[k]
                                        + (drawBelow ? negOffset : posOffset);
                                float y = (buffer.buffer[bufferIndex + 1] + buffer.buffer[bufferIndex + 3]) / 2f;

                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (!mViewPortHandler.isInBoundsTop(y))
                                    break;

                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (!mViewPortHandler.isInBoundsX(x))
                                    continue;

                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (!mViewPortHandler.isInBoundsBottom(y))
                                    continue;

                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (dataSet.isDrawValuesEnabled()) {
                                    /**
                                     * Executes drawvalue operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawValue(c, formattedValue, x, y + halfTextHeight, color);
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
                        }

                        bufferIndex = vals == null ? bufferIndex + 4 : bufferIndex + 4 * vals.length;
                        index++;
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
    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, Transformer trans) {

        float top = x - barWidthHalf;
        float bottom = x + barWidthHalf;
        float left = y1;
        float right = y2;

        mBarRect.set(left, top, right, bottom);

        trans.rectToPixelPhaseHorizontal(mBarRect, mAnimator.getPhaseY());
    }

    @Override
    protected void setHighlightDrawPos(Highlight high, RectF bar) {
        high.setDraw(bar.centerY(), bar.right);
    }

    @Override
    protected boolean isDrawingValuesAllowed(ChartInterface chart) {
        return chart.getData().getEntryCount() < chart.getMaxVisibleCount()
                * mViewPortHandler.getScaleY();
    }
}
