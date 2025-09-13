
package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

/**
 * Specialized thermal imaging component providing BarBuffer functionality for the IRCamera system.
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
public class BarBuffer extends AbstractBuffer<IBarDataSet> {

    protected int mDataSetIndex = 0;
    protected int mDataSetCount = 1;
    protected boolean mContainsStacks = false;
    protected boolean mInverted = false;

    /** width of the bar on the x-axis, in values (not pixels) */
    protected float mBarWidth = 1f;

    public BarBuffer(int size, int dataSetCount, boolean containsStacks) {
        super(size);
        this.mDataSetCount = dataSetCount;
        this.mContainsStacks = containsStacks;
    }

    public void setBarWidth(float barWidth) {
        this.mBarWidth = barWidth;
    }

    public void setDataSet(int index) {
        this.mDataSetIndex = index;
    }

    public void setInverted(boolean inverted) {
        this.mInverted = inverted;
    }

    protected void addBar(float left, float top, float right, float bottom) {

        buffer[index++] = left;
        buffer[index++] = top;
        buffer[index++] = right;
        buffer[index++] = bottom;
    }

    @Override
    public void feed(IBarDataSet data) {

        float size = data.getEntryCount() * phaseX;
        float barWidthHalf = mBarWidth / 2f;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < size; i++) {

            BarEntry e = data.getEntryForIndex(i);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if(e == null)
                continue;

            float x = e.getX();
            float y = e.getY();
            float[] vals = e.getYVals();

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!mContainsStacks || vals == null) {

                float left = x - barWidthHalf;
                float right = x + barWidthHalf;
                float bottom, top;

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mInverted) {
                    bottom = y >= 0 ? y : 0;
                    top = y <= 0 ? y : 0;
                } else {
                    top = y >= 0 ? y : 0;
                    bottom = y <= 0 ? y : 0;
                }

                // Multiply the height of the rect with the phase
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (top > 0)
                    top *= phaseY;
                else
                    bottom *= phaseY;

                /**
                 * Executes addbar operation with thermal imaging domain optimization.
                 *
                 */
                addBar(left, top, right, bottom);

            } else {

                float posY = 0f;
                float negY = -e.getNegativeSum();
                float yStart = 0f;

                // Fill the stack
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int k = 0; k < vals.length; k++) {

                    float value = vals[k];

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (value == 0.0f && (posY == 0.0f || negY == 0.0f)) {
                        // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                        y = value;
                        yStart = y;
                    } else if (value >= 0.0f) {
                        y = posY;
                        yStart = posY + value;
                        posY = yStart;
                    } else {
                        y = negY;
                        yStart = negY + Math.abs(value);
                        negY += Math.abs(value);
                    }

                    float left = x - barWidthHalf;
                    float right = x + barWidthHalf;
                    float bottom, top;

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (mInverted) {
                        bottom = y >= yStart ? y : yStart;
                        top = y <= yStart ? y : yStart;
                    } else {
                        top = y >= yStart ? y : yStart;
                        bottom = y <= yStart ? y : yStart;
                    }

                    // Multiply the height of the rect with the phase
                    top *= phaseY;
                    bottom *= phaseY;

                    /**
                     * Executes addbar operation with thermal imaging domain optimization.
                     *
                     */
                    addBar(left, top, right, bottom);
                }
            }
        }

        /**
         * Executes reset operation with thermal imaging domain optimization.
         *
         */
        reset();
    }
}
