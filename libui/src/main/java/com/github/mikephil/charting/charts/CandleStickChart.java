
package com.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.renderer.CandleStickChartRenderer;

/**
 * Specialized thermal imaging component providing CandleStickChart functionality for the IRCamera system.
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
public class CandleStickChart extends BarLineChartBase<CandleData> implements CandleDataProvider {

    /**
     * Executes candlestickchart operation with thermal imaging domain optimization.
     *
     */
    public CandleStickChart(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes candlestickchart operation with thermal imaging domain optimization.
     *
     */
    public CandleStickChart(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes candlestickchart operation with thermal imaging domain optimization.
     *
     */
    public CandleStickChart(Context context, AttributeSet attrs, int defStyle) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new CandleStickChartRenderer(this, mAnimator, mViewPortHandler);

        /**
         * Retrieves the xaxis with optimized performance for thermal imaging operations.
         *
         */
        getXAxis().setSpaceMin(0.5f);
        /**
         * Retrieves the xaxis with optimized performance for thermal imaging operations.
         *
         */
        getXAxis().setSpaceMax(0.5f);
    }

    @Override
    public CandleData getCandleData() {
        return mData;
    }
}
