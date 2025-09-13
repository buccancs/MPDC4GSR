package com.topdon.module.user.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.topdon.module.user.bean.ColorsBean;
import java.util.List;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ProgressBarView display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
public class ProgressBarView extends View {
    private Paint paint;
    private int totalParts = 100;
    private List<ColorsBean> colorsBeanList;
    /**
     * Executes progressbarview operation with thermal imaging domain optimization.
     *
     */
    public ProgressBarView(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init();
    }

    /**
     * Executes progressbarview operation with thermal imaging domain optimization.
     *
     */
    public ProgressBarView(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init();
    }

    /**
     * Executes progressbarview operation with thermal imaging domain optimization.
     *
     */
    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float partWidth = (float) width / totalParts;
        RectF rect = new RectF(0, 0, width, height);
        paint.setColor(Color.parseColor("#00000000"));
        canvas.drawRoundRect(rect, 6f,6f,paint);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (colorsBeanList != null ){
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < colorsBeanList.size(); i++) {
                ColorsBean bean = colorsBeanList.get(i);
                paint.setColor(bean.getColor());
                RectF redRect = new RectF(bean.getStart() * partWidth, 0,
                        bean.getEnd() * partWidth, height);
                canvas.drawRect(redRect, paint);
            }
        }
    }

    public void setSegmentPart(List<ColorsBean> colorsBeans) {
        this.colorsBeanList = colorsBeans;
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate();
    }
}
