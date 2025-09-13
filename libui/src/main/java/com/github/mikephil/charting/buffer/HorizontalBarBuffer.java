
package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

/**
 * Specialized thermal imaging component providing HorizontalBarBuffer functionality for the IRCamera system.
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
public class HorizontalBarBuffer extends BarBuffer {

    /**
     * Executes horizontalbarbuffer operation with thermal imaging domain optimization.
     *
     */
    public HorizontalBarBuffer(int size, int dataSetCount, boolean containsStacks) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(size, dataSetCount, containsStacks);
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

                float bottom = x - barWidthHalf;
                float top = x + barWidthHalf;
                float left, right;
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (mInverted) {
                    left = y >= 0 ? y : 0;
                    right = y <= 0 ? y : 0;
                } else {
                    right = y >= 0 ? y : 0;
                    left = y <= 0 ? y : 0;
                }

                // Multiply the height of the rect with the phase
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (right > 0)
                    right *= phaseY;
                else
                    left *= phaseY;

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
                    if (value >= 0f) {
                        y = posY;
                        yStart = posY + value;
                        posY = yStart;
                    } else {
                        y = negY;
                        yStart = negY + Math.abs(value);
                        negY += Math.abs(value);
                    }

                    float bottom = x - barWidthHalf;
                    float top = x + barWidthHalf;
                    float left, right;
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (mInverted) {
                        left = y >= yStart ? y : yStart;
                        right = y <= yStart ? y : yStart;
                    } else {
                        right = y >= yStart ? y : yStart;
                        left = y <= yStart ? y : yStart;
                    }

                    // Multiply the height of the rect with the phase
                    right *= phaseY;
                    left *= phaseY;

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
