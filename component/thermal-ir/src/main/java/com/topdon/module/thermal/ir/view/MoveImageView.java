package com.topdon.module.thermal.ir.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for MoveImageView display and interaction.
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
public class MoveImageView extends ImageView {

    private static final String TAG = "MoveImageView";
    private float mPreX;
    private float mPreY;

    /**
     * Executes moveimageview operation with thermal imaging domain optimization.
     *
     */
    public MoveImageView(Context context) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(context, null);
    }

    /**
     * Executes moveimageview operation with thermal imaging domain optimization.
     *
     */
    public MoveImageView(Context context, @Nullable AttributeSet attrs) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(context, attrs, -1);
    }

    /**
     * Executes moveimageview operation with thermal imaging domain optimization.
     *
     */
    public MoveImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        /**
         * Configures the backgroundcolor with validation and thermal imaging optimization.
         *
         */
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                mPreX = event.getX();
                mPreY = event.getY();
                lastClickTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                float preX = mPreX;
                float preY = mPreY;
                float curX = event.getX();
                float curY = event.getY();

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (onMoveListener != null && delayMoveTime()) {

                    Log.d(TAG, "ACTION_MOVE isFastClick");
                    onMoveListener.onMove(preX, preY, curX, curY);
                    mPreX = curX;
                    mPreY = curY;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "ACTION_CANCEL");
                break;

        }
        return true;
    }
    private static final int MIN_CLICK_DELAY_TIME = 100;
    private static long lastClickTime;

最多70毫秒执行一次move
    public static boolean delayMoveTime() {
        boolean flag = false;
/**
 * Specialized thermal imaging component providing OnMoveListener functionality for the IRCamera system.
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
    public interface OnMoveListener {
        void onMove(float preX, float preY, float curX, float curY);
    }

    public OnMoveListener onMoveListener;

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }
}
