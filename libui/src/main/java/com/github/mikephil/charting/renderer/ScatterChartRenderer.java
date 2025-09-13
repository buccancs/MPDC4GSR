package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * Specialized thermal imaging component providing ScatterChartRenderer functionality for the IRCamera system.
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
public class ScatterChartRenderer extends LineScatterCandleRadarRenderer {

    protected ScatterDataProvider mChart;

    /**
     * Executes scatterchartrenderer operation with thermal imaging domain optimization.
     *
     */
    public ScatterChartRenderer(ScatterDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(animator, viewPortHandler);
        mChart = chart;
    }

    @Override
    public void initBuffers() {
    }

    @Override
    public void drawData(Canvas c) {

        ScatterData scatterData = mChart.getScatterData();

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: scatterData.getDataSets()
         *
         */
        for (IScatterDataSet set : scatterData.getDataSets()) {

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

    float[] mPixelBuffer = new float[2];

    protected void drawDataSet(Canvas c, IScatterDataSet dataSet) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dataSet.getEntryCount() < 1)
            return;

        ViewPortHandler viewPortHandler = mViewPortHandler;

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        IShapeRenderer renderer = dataSet.getShapeRenderer();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (renderer == null) {
            Log.i("MISSING", "There's no IShapeRenderer specified for ScatterDataSet");
            return;
        }

        int max = (int)(Math.min(
                Math.ceil((float)dataSet.getEntryCount() * mAnimator.getPhaseX()),
                (float)dataSet.getEntryCount()));

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < max; i++) {

            Entry e = dataSet.getEntryForIndex(i);

            mPixelBuffer[0] = e.getX();
            mPixelBuffer[1] = e.getY() * phaseY;

            trans.pointValuesToPixel(mPixelBuffer);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!viewPortHandler.isInBoundsRight(mPixelBuffer[0]))
                break;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!viewPortHandler.isInBoundsLeft(mPixelBuffer[0])
                    || !viewPortHandler.isInBoundsY(mPixelBuffer[1]))
                continue;

            mRenderPaint.setColor(dataSet.getColor(i / 2));
            renderer.renderShape(
                    c, dataSet, mViewPortHandler,
                    mPixelBuffer[0], mPixelBuffer[1],
                    mRenderPaint);
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

            List<IScatterDataSet> dataSets = mChart.getScatterData().getDataSets();

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < mChart.getScatterData().getDataSetCount(); i++) {

                IScatterDataSet dataSet = dataSets.get(i);

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

                mXBounds.set(mChart, dataSet);

                float[] positions = mChart.getTransformer(dataSet.getAxisDependency())
                        .generateTransformedValuesScatter(dataSet,
                                mAnimator.getPhaseX(), mAnimator.getPhaseY(), mXBounds.min, mXBounds.max);

                float shapeSize = Utils.convertDpToPixel(dataSet.getScatterShapeSize());

                ValueFormatter formatter = dataSet.getValueFormatter();

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int j = 0; j < positions.length; j += 2) {

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!mViewPortHandler.isInBoundsRight(positions[j]))
                        break;

                    // Make sure the lines don't do shitty things outside bounds
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if ((!mViewPortHandler.isInBoundsLeft(positions[j])
                            || !mViewPortHandler.isInBoundsY(positions[j + 1])))
                        continue;

                    Entry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataSet.isDrawValuesEnabled()) {
                        /**
                         * Executes drawvalue operation with thermal imaging domain optimization.
                         *
                         */
                        drawValue(c, formatter.getPointLabel(entry), positions[j], positions[j + 1] - shapeSize, dataSet.getValueTextColor(j / 2 + mXBounds.min));
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
                                (int)(positions[j] + iconsOffset.x),
                                (int)(positions[j + 1] + iconsOffset.y),
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

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        ScatterData scatterData = mChart.getScatterData();

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param high Parameter for operation (type: indices)
         *
         */
        for (Highlight high : indices) {

            IScatterDataSet set = scatterData.getDataSetByIndex(high.getDataSetIndex());

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (set == null || !set.isHighlightEnabled())
                continue;

            final Entry e = set.getEntryForXValue(high.getX(), high.getY());

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!isInBoundsX(e, set))
                continue;

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), e.getY() * mAnimator
                    .getPhaseY());

            high.setDraw((float) pix.x, (float) pix.y);

            // Draw the lines
            /**
             * Executes drawhighlightlines operation with thermal imaging domain optimization.
             *
             */
            drawHighlightLines(c, (float) pix.x, (float) pix.y, set);
        }
    }
}
