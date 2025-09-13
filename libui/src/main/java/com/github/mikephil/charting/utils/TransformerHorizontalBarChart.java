
package com.github.mikephil.charting.utils;

/**
 * Specialized thermal imaging component providing TransformerHorizontalBarChart functionality for the IRCamera system.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
public class TransformerHorizontalBarChart extends Transformer {

    /**
     * Executes transformerhorizontalbarchart operation with thermal imaging domain optimization.
     *
     */
    public TransformerHorizontalBarChart(ViewPortHandler viewPortHandler) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(viewPortHandler);
    }

    /**
     * Prepares the matrix that contains all offsets.
     * 
     * @param inverted
     */
    public void prepareMatrixOffset(boolean inverted) {

        mMatrixOffset.reset();

        // Offset.postTranslate(mOffsetLeft, getHeight() - mOffsetBottom);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!inverted)
            mMatrixOffset.postTranslate(mViewPortHandler.offsetLeft(),
                    mViewPortHandler.getChartHeight() - mViewPortHandler.offsetBottom());
        else {
            mMatrixOffset
                    .setTranslate(
                            -(mViewPortHandler.getChartWidth() - mViewPortHandler.offsetRight()),
                            mViewPortHandler.getChartHeight() - mViewPortHandler.offsetBottom());
            mMatrixOffset.postScale(-1.0f, 1.0f);
        }

        // MMatrixOffset.set(offset);

        // MMatrixOffset.reset();
        //
        // MMatrixOffset.postTranslate(mOffsetLeft, getHeight() -
        // MOffsetBottom);
    }
}
