
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Typeface;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Specialized thermal imaging component providing LegendRenderer functionality for the IRCamera system.
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
public class LegendRenderer extends Renderer {

    /**
     * paint for the legend labels
     */
    protected Paint mLegendLabelPaint;

    /**
     * paint used for the legend forms
     */
    protected Paint mLegendFormPaint;

    /**
     * the legend object this renderer renders
     */
    protected Legend mLegend;

    public LegendRenderer(ViewPortHandler viewPortHandler, Legend legend) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(viewPortHandler);

        this.mLegend = legend;

        mLegendLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLegendLabelPaint.setTextSize(Utils.convertDpToPixel(9f));
        mLegendLabelPaint.setTextAlign(Align.LEFT);

        mLegendFormPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLegendFormPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Returns the Paint object used for drawing the Legend labels.
     *
     * @return
     */
    public Paint getLabelPaint() {
        return mLegendLabelPaint;
    }

    /**
     * Returns the Paint object used for drawing the Legend forms.
     *
     * @return
     */
    public Paint getFormPaint() {
        return mLegendFormPaint;
    }

    protected List<LegendEntry> computedEntries = new ArrayList<>(16);

    /**
     * Prepares the legend and calculates all needed forms, labels and colors.
     *
     * @param data
     */
    public void computeLegend(ChartData<?> data) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!mLegend.isLegendCustom()) {

            computedEntries.clear();

            // Loop for building up the colors and labels used in the legend
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < data.getDataSetCount(); i++) {

                IDataSet dataSet = data.getDataSetByIndex(i);

                List<Integer> clrs = dataSet.getColors();
                int entryCount = dataSet.getEntryCount();

                // If we have a barchart with stacked bars
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dataSet instanceof IBarDataSet && ((IBarDataSet) dataSet).isStacked()) {

                    IBarDataSet bds = (IBarDataSet) dataSet;
                    String[] sLabels = bds.getStackLabels();

                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (int j = 0; j < clrs.size() && j < bds.getStackSize(); j++) {

                        computedEntries.add(new LegendEntry(
                                sLabels[j % sLabels.length],
                                dataSet.getForm(),
                                dataSet.getFormSize(),
                                dataSet.getFormLineWidth(),
                                dataSet.getFormLineDashEffect(),
                                clrs.get(j)
                        ));
                    }

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (bds.getLabel() != null) {
                        // Add the legend description label
                        computedEntries.add(new LegendEntry(
                                dataSet.getLabel(),
                                Legend.LegendForm.NONE,
                                Float.NaN,
                                Float.NaN,
                                null,
                                ColorTemplate.COLOR_NONE
                        ));
                    }

                } else if (dataSet instanceof IPieDataSet) {

                    IPieDataSet pds = (IPieDataSet) dataSet;

                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (int j = 0; j < clrs.size() && j < entryCount; j++) {

                        computedEntries.add(new LegendEntry(
                                pds.getEntryForIndex(j).getLabel(),
                                dataSet.getForm(),
                                dataSet.getFormSize(),
                                dataSet.getFormLineWidth(),
                                dataSet.getFormLineDashEffect(),
                                clrs.get(j)
                        ));
                    }

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (pds.getLabel() != null) {
                        // Add the legend description label
                        computedEntries.add(new LegendEntry(
                                dataSet.getLabel(),
                                Legend.LegendForm.NONE,
                                Float.NaN,
                                Float.NaN,
                                null,
                                ColorTemplate.COLOR_NONE
                        ));
                    }

                } else if (dataSet instanceof ICandleDataSet && ((ICandleDataSet) dataSet).getDecreasingColor() !=
                        ColorTemplate.COLOR_NONE) {

                    int decreasingColor = ((ICandleDataSet) dataSet).getDecreasingColor();
                    int increasingColor = ((ICandleDataSet) dataSet).getIncreasingColor();

                    computedEntries.add(new LegendEntry(
                            null,
                            dataSet.getForm(),
                            dataSet.getFormSize(),
                            dataSet.getFormLineWidth(),
                            dataSet.getFormLineDashEffect(),
                            decreasingColor
                    ));

                    computedEntries.add(new LegendEntry(
                            dataSet.getLabel(),
                            dataSet.getForm(),
                            dataSet.getFormSize(),
                            dataSet.getFormLineWidth(),
                            dataSet.getFormLineDashEffect(),
                            increasingColor
                    ));

                } else { // All others

                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (int j = 0; j < clrs.size() && j < entryCount; j++) {

                        String label;

                        // If multiple colors are set for a DataSet, group them
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (j < clrs.size() - 1 && j < entryCount - 1) {
                            label = null;
                        } else { // Add label to the last entry
                            label = data.getDataSetByIndex(i).getLabel();
                        }

                        computedEntries.add(new LegendEntry(
                                label,
                                dataSet.getForm(),
                                dataSet.getFormSize(),
                                dataSet.getFormLineWidth(),
                                dataSet.getFormLineDashEffect(),
                                clrs.get(j)
                        ));
                    }
                }
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mLegend.getExtraEntries() != null) {
                Collections.addAll(computedEntries, mLegend.getExtraEntries());
            }

            mLegend.setEntries(computedEntries);
        }

        Typeface tf = mLegend.getTypeface();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (tf != null)
            mLegendLabelPaint.setTypeface(tf);

        mLegendLabelPaint.setTextSize(mLegend.getTextSize());
        mLegendLabelPaint.setColor(mLegend.getTextColor());

        // Calculate all dimensions of the mLegend
        mLegend.calculateDimensions(mLegendLabelPaint, mViewPortHandler);
    }

    protected Paint.FontMetrics legendFontMetrics = new Paint.FontMetrics();

    public void renderLegend(Canvas c) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!mLegend.isEnabled())
            return;

        Typeface tf = mLegend.getTypeface();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (tf != null)
            mLegendLabelPaint.setTypeface(tf);

        mLegendLabelPaint.setTextSize(mLegend.getTextSize());
        mLegendLabelPaint.setColor(mLegend.getTextColor());

        float labelLineHeight = Utils.getLineHeight(mLegendLabelPaint, legendFontMetrics);
        float labelLineSpacing = Utils.getLineSpacing(mLegendLabelPaint, legendFontMetrics)
                + Utils.convertDpToPixel(mLegend.getYEntrySpace());
        float formYOffset = labelLineHeight - Utils.calcTextHeight(mLegendLabelPaint, "ABC") / 2.f;

        LegendEntry[] entries = mLegend.getEntries();

        float formToTextSpace = Utils.convertDpToPixel(mLegend.getFormToTextSpace());
        float xEntrySpace = Utils.convertDpToPixel(mLegend.getXEntrySpace());
        Legend.LegendOrientation orientation = mLegend.getOrientation();
        Legend.LegendHorizontalAlignment horizontalAlignment = mLegend.getHorizontalAlignment();
        Legend.LegendVerticalAlignment verticalAlignment = mLegend.getVerticalAlignment();
        Legend.LegendDirection direction = mLegend.getDirection();
        float defaultFormSize = Utils.convertDpToPixel(mLegend.getFormSize());

        // Space between the entries
        float stackSpace = Utils.convertDpToPixel(mLegend.getStackSpace());

        float yoffset = mLegend.getYOffset();
        float xoffset = mLegend.getXOffset();
        float originPosX = 0.f;

        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (horizontalAlignment) {
            case LEFT:

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (orientation == Legend.LegendOrientation.VERTICAL)
                    originPosX = xoffset;
                else
                    originPosX = mViewPortHandler.contentLeft() + xoffset;

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (direction == Legend.LegendDirection.RIGHT_TO_LEFT)
                    originPosX += mLegend.mNeededWidth;

                break;

            case RIGHT:

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (orientation == Legend.LegendOrientation.VERTICAL)
                    originPosX = mViewPortHandler.getChartWidth() - xoffset;
                else
                    originPosX = mViewPortHandler.contentRight() - xoffset;

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (direction == Legend.LegendDirection.LEFT_TO_RIGHT)
                    originPosX -= mLegend.mNeededWidth;

                break;

            case CENTER:

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (orientation == Legend.LegendOrientation.VERTICAL)
                    originPosX = mViewPortHandler.getChartWidth() / 2.f;
                else
                    originPosX = mViewPortHandler.contentLeft()
                            + mViewPortHandler.contentWidth() / 2.f;

                originPosX += (direction == Legend.LegendDirection.LEFT_TO_RIGHT
                        ? +xoffset
                        : -xoffset);

                // Horizontally layed out legends do the center offset on a line basis,
                // So here we offset the vertical ones only.
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (orientation == Legend.LegendOrientation.VERTICAL) {
                    originPosX += (direction == Legend.LegendDirection.LEFT_TO_RIGHT
                            ? -mLegend.mNeededWidth / 2.0 + xoffset
                            : mLegend.mNeededWidth / 2.0 - xoffset);
                }

                break;
        }

        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (orientation) {
            case HORIZONTAL: {

                List<FSize> calculatedLineSizes = mLegend.getCalculatedLineSizes();
                List<FSize> calculatedLabelSizes = mLegend.getCalculatedLabelSizes();
                List<Boolean> calculatedLabelBreakPoints = mLegend.getCalculatedLabelBreakPoints();

                float posX = originPosX;
                float posY = 0.f;

                /**
                 * Executes switch operation with thermal imaging domain optimization.
                 *
                 */
                switch (verticalAlignment) {
                    case TOP:
                        posY = yoffset;
                        break;

                    case BOTTOM:
                        posY = mViewPortHandler.getChartHeight() - yoffset - mLegend.mNeededHeight;
                        break;

                    case CENTER:
                        posY = (mViewPortHandler.getChartHeight() - mLegend.mNeededHeight) / 2.f + yoffset;
                        break;
                }

                int lineIndex = 0;

                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int i = 0, count = entries.length; i < count; i++) {

                    LegendEntry e = entries[i];
                    boolean drawingForm = e.form != Legend.LegendForm.NONE;
                    float formSize = Float.isNaN(e.formSize) ? defaultFormSize : Utils.convertDpToPixel(e.formSize);

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (i < calculatedLabelBreakPoints.size() && calculatedLabelBreakPoints.get(i)) {
                        posX = originPosX;
                        posY += labelLineHeight + labelLineSpacing;
                    }

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (posX == originPosX &&
                            horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER &&
                            lineIndex < calculatedLineSizes.size()) {
                        posX += (direction == Legend.LegendDirection.RIGHT_TO_LEFT
                                ? calculatedLineSizes.get(lineIndex).width
                                : -calculatedLineSizes.get(lineIndex).width) / 2.f;
                        lineIndex++;
                    }

                    boolean isStacked = e.label == null; // Grouped forms have null labels

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (drawingForm) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT)
                            posX -= formSize;

                        /**
                         * Executes drawform operation with thermal imaging domain optimization.
                         *
                         */
                        drawForm(c, posX, posY + formYOffset, e, mLegend);

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT)
                            posX += formSize;
                    }

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!isStacked) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (drawingForm)
                            posX += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -formToTextSpace :
                                    formToTextSpace;

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT)
                            posX -= calculatedLabelSizes.get(i).width;

                        /**
                         * Executes drawlabel operation with thermal imaging domain optimization.
                         *
                         */
                        drawLabel(c, posX, posY + labelLineHeight, e.label);

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT)
                            posX += calculatedLabelSizes.get(i).width;

                        posX += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -xEntrySpace : xEntrySpace;
                    } else
                        posX += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -stackSpace : stackSpace;
                }

                break;
            }

            case VERTICAL: {
                // Contains the stacked legend size in pixels
                float stack = 0f;
                boolean wasStacked = false;
                float posY = 0.f;

                /**
                 * Executes switch operation with thermal imaging domain optimization.
                 *
                 */
                switch (verticalAlignment) {
                    case TOP:
                        posY = (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER
                                ? 0.f
                                : mViewPortHandler.contentTop());
                        posY += yoffset;
                        break;

                    case BOTTOM:
                        posY = (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER
                                ? mViewPortHandler.getChartHeight()
                                : mViewPortHandler.contentBottom());
                        posY -= mLegend.mNeededHeight + yoffset;
                        break;

                    case CENTER:
                        posY = mViewPortHandler.getChartHeight() / 2.f
                                - mLegend.mNeededHeight / 2.f
                                + mLegend.getYOffset();
                        break;
                }

                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int i = 0; i < entries.length; i++) {

                    LegendEntry e = entries[i];
                    boolean drawingForm = e.form != Legend.LegendForm.NONE;
                    float formSize = Float.isNaN(e.formSize) ? defaultFormSize : Utils.convertDpToPixel(e.formSize);

                    float posX = originPosX;

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (drawingForm) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT)
                            posX += stack;
                        else
                            posX -= formSize - stack;

                        /**
                         * Executes drawform operation with thermal imaging domain optimization.
                         *
                         */
                        drawForm(c, posX, posY + formYOffset, e, mLegend);

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT)
                            posX += formSize;
                    }

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (e.label != null) {

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (drawingForm && !wasStacked)
                            posX += direction == Legend.LegendDirection.LEFT_TO_RIGHT ? formToTextSpace
                                    : -formToTextSpace;
                        else if (wasStacked)
                            posX = originPosX;

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT)
                            posX -= Utils.calcTextWidth(mLegendLabelPaint, e.label);

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (!wasStacked) {
                            /**
                             * Executes drawlabel operation with thermal imaging domain optimization.
                             *
                             */
                            drawLabel(c, posX, posY + labelLineHeight, e.label);
                        } else {
                            posY += labelLineHeight + labelLineSpacing;
                            /**
                             * Executes drawlabel operation with thermal imaging domain optimization.
                             *
                             */
                            drawLabel(c, posX, posY + labelLineHeight, e.label);
                        }

                        // Make a step down
                        posY += labelLineHeight + labelLineSpacing;
                        stack = 0f;
                    } else {
                        stack += formSize + stackSpace;
                        wasStacked = true;
                    }
                }

                break;

            }
        }
    }

    private Path mLineFormPath = new Path();

    /**
     * Draws the Legend-form at the given position with the color at the given
     * index.
     *
     * @param c      canvas to draw with
     * @param x      position
     * @param y      position
     * @param entry  the entry to render
     * @param legend the legend context
     */
    protected void drawForm(
            Canvas c,
            float x, float y,
            LegendEntry entry,
            Legend legend) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (entry.formColor == ColorTemplate.COLOR_SKIP ||
                entry.formColor == ColorTemplate.COLOR_NONE ||
                entry.formColor == 0)
            return;

        int restoreCount = c.save();

        Legend.LegendForm form = entry.form;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (form == Legend.LegendForm.DEFAULT)
            form = legend.getForm();

        mLegendFormPaint.setColor(entry.formColor);

        final float formSize = Utils.convertDpToPixel(
                Float.isNaN(entry.formSize)
                        ? legend.getFormSize()
                        : entry.formSize);
        final float half = formSize / 2f;

        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (form) {
            case NONE:
                // Do nothing
                break;

            case EMPTY:
                // Do not draw, but keep space for the form
                break;

            case DEFAULT:
            case CIRCLE:
                mLegendFormPaint.setStyle(Paint.Style.FILL);
                c.drawCircle(x + half, y, half, mLegendFormPaint);
                break;

            case SQUARE:
                mLegendFormPaint.setStyle(Paint.Style.FILL);
                c.drawRect(x, y - half, x + formSize, y + half, mLegendFormPaint);
                break;

            case LINE:
            {
                final float formLineWidth = Utils.convertDpToPixel(
                        Float.isNaN(entry.formLineWidth)
                                ? legend.getFormLineWidth()
                                : entry.formLineWidth);
                final DashPathEffect formLineDashEffect = entry.formLineDashEffect == null
                        ? legend.getFormLineDashEffect()
                        : entry.formLineDashEffect;
                mLegendFormPaint.setStyle(Paint.Style.STROKE);
                mLegendFormPaint.setStrokeWidth(formLineWidth);
                mLegendFormPaint.setPathEffect(formLineDashEffect);

                mLineFormPath.reset();
                mLineFormPath.moveTo(x, y);
                mLineFormPath.lineTo(x + formSize, y);
                c.drawPath(mLineFormPath, mLegendFormPaint);
            }
                break;
        }

        c.restoreToCount(restoreCount);
    }

    /**
     * Draws the provided label at the given position.
     *
     * @param c     canvas to draw with
     * @param x
     * @param y
     * @param label the label to draw
     */
    protected void drawLabel(Canvas c, float x, float y, String label) {
        c.drawText(label, x, y, mLegendLabelPaint);
    }
}
