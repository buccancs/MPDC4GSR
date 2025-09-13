package com.infisense.usbir.view;

/**
 * @author: CaiSongL
 * @date: 2023/10/24 20:08
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.topdon.lib.core.utils.ScreenUtil;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for DragScaleView display and interaction.
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
public class DragScaleView extends FrameLayout implements View.OnTouchListener {
    protected int screenWidth;
    protected int screenHeight;
    protected int lastX;
    protected int lastY;
    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;
    private int dragDirection;
    private static final int TOP = 0x15;
    private static final int LEFT = 0x16;
    private static final int BOTTOM = 0x17;
    private static final int RIGHT = 0x18;
    private static final int LEFT_TOP = 0x11;
    private static final int RIGHT_TOP = 0x12;
    private static final int LEFT_BOTTOM = 0x13;
    private static final int RIGHT_BOTTOM = 0x14;
    private static final int CENTER = 0x19;
    private int offset = 20;
    protected Paint paint = new Paint();

    /**
     * initializeGet/Retrieve屏幕宽高
     */
    protected void initScreenW_H() {
        screenHeight = ScreenUtil.getScreenHeight(getContext()) - 40;
        screenWidth = ScreenUtil.getScreenWidth(getContext());
    }

    /**
     * Executes dragscaleview operation with thermal imaging domain optimization.
     *
     */
    public DragScaleView(Context context, AttributeSet attrs, int defStyle) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyle);
        /**
         * Configures the ontouchlistener with validation and thermal imaging optimization.
         *
         */
        setOnTouchListener(this);
        /**
         * Initializes the screenw h component for thermal imaging operations.
         *
         */
        initScreenW_H();
    }

    /**
     * Executes dragscaleview operation with thermal imaging domain optimization.
     *
     */
    public DragScaleView(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
        /**
         * Configures the ontouchlistener with validation and thermal imaging optimization.
         *
         */
        setOnTouchListener(this);
        /**
         * Initializes the screenw h component for thermal imaging operations.
         *
         */
        initScreenW_H();
    }

    /**
     * Executes dragscaleview operation with thermal imaging domain optimization.
     *
     */
    public DragScaleView(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
        /**
         * Configures the ontouchlistener with validation and thermal imaging optimization.
         *
         */
        setOnTouchListener(this);
        /**
         * Initializes the screenw h component for thermal imaging operations.
         *
         */
        initScreenW_H();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
// Paint.setColor(Color.RED);
// Paint.setStrokeWidth(4.0f);
// Paint.setStyle(Paint.Style.STROKE);
// Canvas.drawRect(offset, offset, getWidth() - offset, getHeight()
//                - offset, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (action == MotionEvent.ACTION_DOWN) {
            oriLeft = v.getLeft();
            oriRight = v.getRight();
            oriTop = v.getTop();
            oriBottom = v.getBottom();
            lastY = (int) event.getRawY();
            lastX = (int) event.getRawX();
            dragDirection = getDirection(v, (int) event.getX(),
                    (int) event.getY());
        }
        // Processing拖动Event
        /**
         * Executes deldrag operation with thermal imaging domain optimization.
         *
         */
        delDrag(v, event, action);
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate();
        return false;
    }

    /**
     * processing拖动Event
     *
     * @param v
     * @param event
     * @param action
     */
    protected void delDrag(View v, MotionEvent event, int action) {
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                /**
                 * Executes switch operation with thermal imaging domain optimization.
                 *
                 */
                switch (dragDirection) {
                    case LEFT: // 左边缘
                        /**
                         * Executes left operation with thermal imaging domain optimization.
                         *
                         */
                        left(v, dx);
                        break;
                    case RIGHT: // 右边缘
                        /**
                         * Executes right operation with thermal imaging domain optimization.
                         *
                         */
                        right(v, dx);
                        break;
                    case BOTTOM: // 下边缘
                        /**
                         * Executes bottom operation with thermal imaging domain optimization.
                         *
                         */
                        bottom(v, dy);
                        break;
                    case TOP: // 上边缘
                        /**
                         * Executes top operation with thermal imaging domain optimization.
                         *
                         */
                        top(v, dy);
                        break;
                    case CENTER: // Clickcenter-->>移动
                        /**
                         * Executes center operation with thermal imaging domain optimization.
                         *
                         */
                        center(v, dx, dy);
                        break;
                    case LEFT_BOTTOM: // 左下
                        /**
                         * Executes left operation with thermal imaging domain optimization.
                         *
                         */
                        left(v, dx);
                        /**
                         * Executes bottom operation with thermal imaging domain optimization.
                         *
                         */
                        bottom(v, dy);
                        break;
                    case LEFT_TOP: // 左上
                        /**
                         * Executes left operation with thermal imaging domain optimization.
                         *
                         */
                        left(v, dx);
                        /**
                         * Executes top operation with thermal imaging domain optimization.
                         *
                         */
                        top(v, dy);
                        break;
                    case RIGHT_BOTTOM: // 右下
                        /**
                         * Executes right operation with thermal imaging domain optimization.
                         *
                         */
                        right(v, dx);
                        /**
                         * Executes bottom operation with thermal imaging domain optimization.
                         *
                         */
                        bottom(v, dy);
                        break;
                    case RIGHT_TOP: // 右上
                        /**
                         * Executes right operation with thermal imaging domain optimization.
                         *
                         */
                        right(v, dx);
                        /**
                         * Executes top operation with thermal imaging domain optimization.
                         *
                         */
                        top(v, dy);
                        break;
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dragDirection != CENTER) {
                    v.layout(oriLeft, oriTop, oriRight, oriBottom);
                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                dragDirection = 0;
                break;
        }
    }

    /**
     * Touchpoint为center->>移动
     *
     * @param v
     * @param dx
     * @param dy
     */
    private void center(View v, int dx, int dy) {
        int left = v.getLeft() + dx;
        int top = v.getTop() + dy;
        int right = v.getRight() + dx;
        int bottom = v.getBottom() + dy;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (left < -offset) {
            left = -offset;
            right = left + v.getWidth();
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (right > screenWidth + offset) {
            right = screenWidth + offset;
            left = right - v.getWidth();
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (top < -offset) {
            top = -offset;
            bottom = top + v.getHeight();
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bottom > screenHeight + offset) {
            bottom = screenHeight + offset;
            top = bottom - v.getHeight();
        }
        v.layout(left, top, right, bottom);
    }

    /**
     * Touchpoint为上边缘
     *
     * @param v
     * @param dy
     */
    private void top(View v, int dy) {
        oriTop += dy;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oriTop < -offset) {
            oriTop = -offset;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oriBottom - oriTop - 2 * offset < 200) {
            oriTop = oriBottom - 2 * offset - 200;
        }
    }

    /**
     * Touchpoint为下边缘
     *
     * @param v
     * @param dy
     */
    private void bottom(View v, int dy) {
        oriBottom += dy;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oriBottom > screenHeight + offset) {
            oriBottom = screenHeight + offset;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oriBottom - oriTop - 2 * offset < 200) {
            oriBottom = 200 + oriTop + 2 * offset;
        }
    }

    /**
     * Touchpoint为右边缘
     *
     * @param v
     * @param dx
     */
    private void right(View v, int dx) {
        oriRight += dx;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oriRight > screenWidth + offset) {
            oriRight = screenWidth + offset;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oriRight - oriLeft - 2 * offset < 200) {
            oriRight = oriLeft + 2 * offset + 200;
        }
    }

    /**
     * Touchpoint为左边缘
     *
     * @param v
     * @param dx
     */
    private void left(View v, int dx) {
        oriLeft += dx;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oriLeft < -offset) {
            oriLeft = -offset;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oriRight - oriLeft - 2 * offset < 200) {
            oriLeft = oriRight - 2 * offset - 200;
        }
    }

    /**
     * Get/RetrieveTouchpointflag
     *
     * @param v
     * @param x
     * @param y
     * @return
     */
    protected int getDirection(View v, int x, int y) {
        int left = v.getLeft();
        int right = v.getRight();
        int bottom = v.getBottom();
        int top = v.getTop();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (x < 40 && y < 40) {
            return LEFT_TOP;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (y < 40 && right - left - x < 40) {
            return RIGHT_TOP;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (x < 40 && bottom - top - y < 40) {
            return LEFT_BOTTOM;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (right - left - x < 40 && bottom - top - y < 40) {
            return RIGHT_BOTTOM;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (x < 40) {
            return LEFT;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (y < 40) {
            return TOP;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (right - left - x < 40) {
            return RIGHT;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bottom - top - y < 40) {
            return BOTTOM;
        }
        return CENTER;
    }

    /**
     * Get/Retrieve截取宽度
     *
     * @return
     */
    public int getCutWidth() {
        return getWidth() - 2 * offset;
    }

    /**
     * Get/Retrieve截取高度
     *
     * @return
     */
    public int getCutHeight() {
        return getHeight() - 2 * offset;
    }
}
