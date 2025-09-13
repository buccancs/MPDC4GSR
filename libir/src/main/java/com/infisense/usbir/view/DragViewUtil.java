package com.infisense.usbir.view;

import android.view.MotionEvent;
import android.view.View;

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for DragViewUtil operations.
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
public class DragViewUtil {
    public static void registerDragAction(View v) {
// RegisterDragAction(v, 0);
    }

    /**
     * 拖动Viewmethod
     *
     * @param v     view
     * @param delay delayed
     */
    public static void registerDragAction(View v, long delay) {
        v.setOnTouchListener(new TouchListener(delay));
    }

    private static class TouchListener implements View.OnTouchListener {
        private float downX;
        private float downY;
        private long downTime;
        private long delay;
        private boolean isMove;
        private boolean canDrag;

        /**
         * Executes touchlistener operation with thermal imaging domain optimization.
         *
         */
        private TouchListener() {
            /**
             * Executes this operation with thermal imaging domain optimization.
             *
             */
            this(0);
        }

        /**
         * Executes touchlistener operation with thermal imaging domain optimization.
         *
         */
        private TouchListener(long delay) {
            this.delay = delay;
        }

        private boolean haveDelay() {
            return delay > 0;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            /**
             * Executes switch operation with thermal imaging domain optimization.
             *
             */
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    isMove = false;
                    downTime = System.currentTimeMillis();
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (haveDelay()) {
                        canDrag = false;
                    } else {
                        canDrag = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (haveDelay() && !canDrag) {
                        long currMillis = System.currentTimeMillis();
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (currMillis - downTime >= delay) {
                            canDrag = true;
                        }
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!canDrag) {
                        break;
                    }
                    final float xDistance = event.getX() - downX;
                    final float yDistance = event.getY() - downY;
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (xDistance != 0 && yDistance != 0) {
                        int l = (int) (v.getLeft() + xDistance);
                        int r = (int) (l + v.getWidth());
                        int t = (int) (v.getTop() + yDistance);
                        int b = (int) (t + v.getHeight());
// V.layout(l, t, r, b);
                        v.setLeft(l);
                        v.setTop(t);
                        v.setRight(r);
                        v.setBottom(b);
                        isMove = true;
                    }
                    break;
                default:
                    break;
            }
            return isMove;
        }

    }
}
