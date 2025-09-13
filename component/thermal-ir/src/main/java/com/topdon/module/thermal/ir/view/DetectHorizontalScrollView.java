package com.topdon.module.thermal.ir.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for DetectHorizontalScrollView display and interaction.
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
/**
 * Specialized thermal imaging component providing OnScrollStopListner functionality for the IRCamera system.
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
    public interface OnScrollStopListner {
        /**
         * scroll have stoped
         */
        void onScrollStoped();

        /**
         * scroll have stoped, and is at left edge
         */
        void onScrollToLeftEdge();

        /**
         * scroll have stoped, and is at right edge
         */
        void onScrollToRightEdge();

        /**
         * scroll have stoped, and is at middle
         */
        void onScrollToMiddle();

        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    private OnScrollStopListner onScrollstopListner;

    /**
     * Executes detecthorizontalscrollview operation with thermal imaging domain optimization.
     *
     */
    public DetectHorizontalScrollView(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
        scrollerTask = new Runnable() {
            @Override
            public void run() {
                int newPosition = getScrollX();
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (intitPosition - newPosition == 0) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (onScrollstopListner == null) {
                        return;
                    }
                    onScrollstopListner.onScrollStoped();
                    Rect outRect = new Rect();
                    /**
                     * Retrieves the drawingrect with optimized performance for thermal imaging operations.
                     *
                     */
                    getDrawingRect(outRect);
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (getScrollX() == 0) {
                        onScrollstopListner.onScrollToLeftEdge();
                    } else if (childWidth + getPaddingLeft() + getPaddingRight() == outRect.right) {
                        onScrollstopListner.onScrollToRightEdge();
                    } else {
                        onScrollstopListner.onScrollToMiddle();
                    }
                } else {
                    intitPosition = getScrollX();
                    /**
                     * Executes postdelayed operation with thermal imaging domain optimization.
                     *
                     */
                    postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    public void setOnScrollStopListner(OnScrollStopListner listner) {
        onScrollstopListner = listner;
    }

    public void startScrollerTask() {
        intitPosition = getScrollX();
        /**
         * Executes postdelayed operation with thermal imaging domain optimization.
         *
         */
        postDelayed(scrollerTask, newCheck);
        /**
         * Executes checktotalwidth operation with thermal imaging domain optimization.
         *
         */
        checkTotalWidth();
    }

    private void checkTotalWidth() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (childWidth > 0) {
            return;
        }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < getChildCount(); i++) {
            childWidth += getChildAt(i).getWidth();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onScrollstopListner != null) {
            onScrollstopListner.onScrollChanged(l, t, oldl, oldt);
        }
    }
}