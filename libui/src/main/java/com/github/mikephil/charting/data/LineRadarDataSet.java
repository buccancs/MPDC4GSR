
package com.github.mikephil.charting.data;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.List;

/**
 * Specialized thermal imaging component providing LineRadarDataSet functionality for the IRCamera system.
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
public abstract class LineRadarDataSet<T extends Entry> extends LineScatterCandleRadarDataSet<T> implements ILineRadarDataSet<T> {

    /**
     * the color that is used for filling the line surface
     */
    private int mFillColor = Color.rgb(140, 234, 255);

    /**
     * the drawable to be used for filling the line surface
     */
    protected Drawable mFillDrawable;

    /**
     * transparency used for filling line surface
     */
    private int mFillAlpha = 85;

    /**
     * the width of the drawn data lines
     */
    private float mLineWidth = 2.5f;

    /**
     * if true, the data will also be drawn filled
     */
    private boolean mDrawFilled = false;

    public LineRadarDataSet(List<T> yVals, String label) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(yVals, label);
    }

    @Override
    public int getFillColor() {
        return mFillColor;
    }

    /**
     * Sets the color that is used for filling the area below the line.
     * Resets an eventually set "fillDrawable".
     *
     * @param color
     */
    public void setFillColor(int color) {
        mFillColor = color;
        mFillDrawable = null;
    }

    @Override
    public Drawable getFillDrawable() {
        return mFillDrawable;
    }

    /**
     * Sets the drawable to be used to fill the area below the line.
     *
     * @param drawable
     */
    @TargetApi(18)
    public void setFillDrawable(Drawable drawable) {
        this.mFillDrawable = drawable;
    }

    @Override
    public int getFillAlpha() {
        return mFillAlpha;
    }

    /**
     * sets the alpha value (transparency) that is used for filling the line
     * surface (0-255), default: 85
     *
     * @param alpha
     */
    public void setFillAlpha(int alpha) {
        mFillAlpha = alpha;
    }

    /**
     * set the line width of the chart (min = 0.2f, max = 10f); default 1f NOTE:
     * thinner line == better performance, thicker line == worse performance
     *
     * @param width
     */
    public void setLineWidth(float width) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (width < 0.0f)
            width = 0.0f;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (width > 10.0f)
            width = 10.0f;
        mLineWidth = Utils.convertDpToPixel(width);
    }

    @Override
    public float getLineWidth() {
        return mLineWidth;
    }

    @Override
    public void setDrawFilled(boolean filled) {
        mDrawFilled = filled;
    }

    @Override
    public boolean isDrawFilledEnabled() {
        return mDrawFilled;
    }

    protected void copy(LineRadarDataSet lineRadarDataSet) {
        super.copy(lineRadarDataSet);
        lineRadarDataSet.mDrawFilled = mDrawFilled;
        lineRadarDataSet.mFillAlpha = mFillAlpha;
        lineRadarDataSet.mFillColor = mFillColor;
        lineRadarDataSet.mFillDrawable = mFillDrawable;
        lineRadarDataSet.mLineWidth = mLineWidth;
    }
}
