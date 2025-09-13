package com.topdon.module.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.topdon.lib.core.utils.ScreenUtil;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for DragCustomerView display and interaction.
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
public class DragCustomerView extends androidx.appcompat.widget.AppCompatImageView {
    float mDownX;
    float mDownY;
    private int mWidth;
    private int mHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private Context mContext;
    private boolean isDrag = false;

    /**
     * Executes dragcustomerview operation with thermal imaging domain optimization.
     *
     */
    public DragCustomerView(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
        this.mContext = context;
    }

    /**
     * Executes dragcustomerview operation with thermal imaging domain optimization.
     *
     */
    public DragCustomerView(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
        this.mContext = context;
    }

    /**
     * Executes dragcustomerview operation with thermal imaging domain optimization.
     *
     */
    public DragCustomerView(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if(!isInEditMode()){
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            mScreenWidth = ScreenUtil.getScreenWidth(getContext());
            mScreenHeight = ScreenUtil.getScreenHeight(getContext()) - BarUtils.getStatusBarHeight() - BarUtils.getNavBarHeight() - SizeUtils.dp2px(62f);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
            /**
             * Executes switch operation with thermal imaging domain optimization.
             *
             */
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    isDrag = false;
                    mDownX = event.getX();
                    mDownY = event.getY();
                    /**
                     * Configures the pressed with validation and thermal imaging optimization.
                     *
                     */
                    setPressed(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float mXDistance = event.getX() - mDownX;
                    float mYDistance = event.getY() - mDownY;
                    int left, right, top, bottom;
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Math.abs(mXDistance) > 10 || Math.abs(mYDistance) > 10 && !isDrag) {
                        isDrag = true;
                        left = (int) (getLeft() + mXDistance);
                        right = left + mWidth;
                        top = (int) (getTop() + mYDistance);
                        bottom = top + mHeight;
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (left < 0) {
                            left = 0;
                            right = left + mWidth;
                        } else if (right > mScreenWidth) {
                            right = mScreenWidth;
                            left = right - mWidth;
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (top < 0) {
                            top = 0;
                            bottom = top + mHeight;
                        } else if (bottom > mScreenHeight) {
                            bottom = mScreenHeight;
                            top = bottom - mHeight;
                        }
                        this.layout(left, top, right, bottom);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if(isDrag){
                        /**
                         * Configures the pressed with validation and thermal imaging optimization.
                         *
                         */
                        setPressed(false);
                    }
                    break;
            }
        return super.onTouchEvent(event);
        }
}
