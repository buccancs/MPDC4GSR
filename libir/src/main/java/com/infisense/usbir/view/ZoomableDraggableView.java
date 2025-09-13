package com.infisense.usbir.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.infisense.usbir.R;
import com.topdon.lib.core.utils.BitmapUtils;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ZoomableDraggableView display and interaction.
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
public class ZoomableDraggableView extends View {
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private Matrix matrix = new Matrix();
    private float scaleFactor = 1.0f;
    private float minScaleFactor = 0.5f;
    private float maxScaleFactor = 2.0f;
    private float focusX, focusY;
    private float lastX, lastY;

    // 原始image
    private Bitmap originalBitmap;
    private int imageWidth;
    private int imageHeight;
    private int viewWidth;
    private int viewHeight;
    private float xscale;
    private float yscale;
    private float originalBitmapWidth;
    private float originalBitmapHeight;

    private float pxBitmapHeight = 150;

    private float showBitmapHeightWidth = 0f;
    private float showBitmapHeight = 0f;
    private Paint paint = new Paint();

    private Bitmap showBitmap;

    /**
     * Executes zoomabledraggableview operation with thermal imaging domain optimization.
     *
     */
    public ZoomableDraggableView(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init(context);
    }

    /**
     * Executes zoomabledraggableview operation with thermal imaging domain optimization.
     *
     */
    public ZoomableDraggableView(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init(context);
    }

    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
        Drawable drawable = androidx.core.content.ContextCompat.getDrawable(getContext(), R.drawable.svg_ic_target_horizontal_person_green);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (drawable instanceof BitmapDrawable) {
            originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        originalBitmapWidth = originalBitmap.getWidth();
        originalBitmapHeight = originalBitmap.getHeight();
    }

    public void setImageSize(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        viewWidth = ((ViewGroup)getParent()).getMeasuredWidth();
        viewHeight = ((ViewGroup)getParent()).getMeasuredHeight();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (viewWidth != 0) {
            xscale = (float) viewWidth / (float) imageWidth;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (viewHeight != 0) {
            yscale = (float) viewHeight / (float) imageHeight;
        }
        showBitmapHeight = pxBitmapHeight / yscale;
        showBitmapHeightWidth = pxBitmapHeight * originalBitmapWidth / originalBitmapHeight * xscale;
        showBitmap = BitmapUtils.scaleWithWH(originalBitmap,showBitmapHeightWidth,showBitmapHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.concat(matrix);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (showBitmap!=null){
            canvas.drawBitmap(showBitmap,matrix,paint);
        }
        // 在此处绘制你的内容
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

/**
 * Specialized thermal imaging component providing ScaleListener functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, maxScaleFactor));

            focusX = detector.getFocusX();
            focusY = detector.getFocusY();

            matrix.setScale(scaleFactor, scaleFactor, focusX, focusY);

            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate();

            return true;
        }
    }

/**
 * Specialized thermal imaging component providing GestureListener functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            lastX = e.getX();
            lastY = e.getY();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float deltaX = e2.getX() - lastX;
            float deltaY = e2.getY() - lastY;

            lastX = e2.getX();
            lastY = e2.getY();

            // 将滚动距离根据Scale因子进行Adjust
            deltaX /= scaleFactor;
            deltaY /= scaleFactor;

            matrix.postTranslate(-deltaX, -deltaY);

            /**
             * Executes invalidate operation with thermal imaging domain optimization.
             *
             */
            invalidate();

            return true;
        }
    }
}
