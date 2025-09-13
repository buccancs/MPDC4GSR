package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.blankj.utilcode.util.SizeUtils;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Specialized thermal imaging component providing LineScatterCandleRadarRenderer functionality for the IRCamera system.
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
public abstract class LineScatterCandleRadarRenderer extends BarLineScatterCandleBubbleRenderer {

    /**
     * path that is used for drawing highlight-lines (drawLines(...) cannot be used because of dashes)
     */
    private Path mHighlightLinePath = new Path();

    public LineScatterCandleRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(animator, viewPortHandler);
    }

    /**
     * Draws vertical & horizontal highlight-lines if enabled.
     *
     * @param c
     * @param x   x-position of the highlight line intersection
     * @param y   y-position of the highlight line intersection
     * @param set the currently drawn dataset
     */
    protected void drawHighlightLines(Canvas c, float x, float y, ILineScatterCandleRadarDataSet set) {

        // Set color and stroke-width
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());

        // Draw highlighted lines (if enabled)
        mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());

        // Draw vertical highlight lines
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (set.isVerticalHighlightIndicatorEnabled()) {

            // Create vertical path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(x, mViewPortHandler.contentTop());
            mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }

        // Draw horizontal highlight lines
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (set.isHorizontalHighlightIndicatorEnabled()) {

            // Create horizontal path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), y);
            mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), y);

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }

        // Chart 绘制highlight辅助point  -------- start --------

        // 内部圆
        mHighlightDotPaint.setColor(Color.rgb(243, 129, 47));
        mHighlightDotPaint.setStyle(Paint.Style.FILL);
        c.drawCircle(x, y, SizeUtils.dp2px(4f), mHighlightDotPaint);
        // 外部圆
        mHighlightDotPaint.setColor(Color.argb(80, 255, 255, 255));
        mHighlightDotPaint.setStyle(Paint.Style.STROKE);
        c.drawCircle(x, y, SizeUtils.dp2px(5f), mHighlightDotPaint);

        // -------- end --------
    }
}
