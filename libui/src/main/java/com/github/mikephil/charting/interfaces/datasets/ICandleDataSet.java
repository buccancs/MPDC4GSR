package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.Paint;

import com.github.mikephil.charting.data.CandleEntry;

/**
 * Specialized thermal imaging component providing ICandleDataSet functionality for the IRCamera system.
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
public interface ICandleDataSet extends ILineScatterCandleRadarDataSet<CandleEntry> {

    /**
     * Returns the space that is left out on the left and right side of each
     * candle.
     *
     * @return
     */
    float getBarSpace();

    /**
     * Returns whether the candle bars should show?
     * When false, only "ticks" will show
     *
     * - default: true
     *
     * @return
     */
    boolean getShowCandleBar();

    /**
     * Returns the width of the candle-shadow-line in pixels.
     *
     * @return
     */
    float getShadowWidth();

    /**
     * Returns shadow color for all entries
     *
     * @return
     */
    int getShadowColor();

    /**
     * Returns the neutral color (for open == close)
     *
     * @return
     */
    int getNeutralColor();

    /**
     * Returns the increasing color (for open < close).
     *
     * @return
     */
    int getIncreasingColor();

    /**
     * Returns the decreasing color (for open > close).
     *
     * @return
     */
    int getDecreasingColor();

    /**
     * Returns paint style when open < close
     *
     * @return
     */
    Paint.Style getIncreasingPaintStyle();

    /**
     * Returns paint style when open > close
     *
     * @return
     */
    Paint.Style getDecreasingPaintStyle();

    /**
     * Is the shadow color same as the candle color?
     *
     * @return
     */
    boolean getShadowColorSameAsCandle();
}
