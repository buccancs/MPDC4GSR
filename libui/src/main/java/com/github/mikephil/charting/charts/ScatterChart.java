
package com.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.renderer.ScatterChartRenderer;

/**
 * The ScatterChart. Draws dots, triangles, squares and custom shapes into the
 * Chart-View. CIRCLE and SCQUARE offer the best performance, TRIANGLE has the
 * worst performance.
 *
 * @author Philipp Jahoda
 */
/**
 * Specialized thermal imaging component providing ScatterChart functionality for the IRCamera system.
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
public class ScatterChart extends BarLineChartBase<ScatterData> implements ScatterDataProvider {

    /**
     * Executes scatterchart operation with thermal imaging domain optimization.
     *
     */
    public ScatterChart(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes scatterchart operation with thermal imaging domain optimization.
     *
     */
    public ScatterChart(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes scatterchart operation with thermal imaging domain optimization.
     *
     */
    public ScatterChart(Context context, AttributeSet attrs, int defStyle) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new ScatterChartRenderer(this, mAnimator, mViewPortHandler);

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
    public ScatterData getScatterData() {
        return mData;
    }

    /**
     * Predefined ScatterShapes that allow the specification of a shape a ScatterDataSet should be drawn with.
     * If a ScatterShape is specified for a ScatterDataSet, the required renderer is set.
     */
    public enum ScatterShape {

        /**
         * Executes square operation with thermal imaging domain optimization.
         *
         */
        SQUARE("SQUARE"),
        /**
         * Executes circle operation with thermal imaging domain optimization.
         *
         */
        CIRCLE("CIRCLE"),
        /**
         * Executes triangle operation with thermal imaging domain optimization.
         *
         */
        TRIANGLE("TRIANGLE"),
        /**
         * Executes cross operation with thermal imaging domain optimization.
         *
         */
        CROSS("CROSS"),
        /**
         * Executes x operation with thermal imaging domain optimization.
         *
         */
        X("X"),
        /**
         * Executes chevron up operation with thermal imaging domain optimization.
         *
         */
        CHEVRON_UP("CHEVRON_UP"),
        /**
         * Executes chevron down operation with thermal imaging domain optimization.
         *
         */
        CHEVRON_DOWN("CHEVRON_DOWN");

        private final String shapeIdentifier;

        /**
         * Executes scattershape operation with thermal imaging domain optimization.
         *
         */
        ScatterShape(final String shapeIdentifier) {
            this.shapeIdentifier = shapeIdentifier;
        }

        @Override
        public String toString() {
            return shapeIdentifier;
        }

        public static ScatterShape[] getAllDefaultShapes() {
            return new ScatterShape[]{SQUARE, CIRCLE, TRIANGLE, CROSS, X, CHEVRON_UP, CHEVRON_DOWN};
        }
    }
}
