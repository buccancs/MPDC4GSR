package com.topdon.lib.ui.widget.seekbar;

import static com.topdon.lib.ui.widget.seekbar.SeekBar.INDICATOR_ALWAYS_HIDE;
import static com.topdon.lib.ui.widget.seekbar.SeekBar.INDICATOR_ALWAYS_SHOW;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.topdon.lib.ui.R;
import com.topdon.menu.util.PseudoColorConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Specialized thermal imaging component providing RangeSeekBar functionality for the IRCamera system.
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
public class RangeSeekBar extends View {

    // Pseudo colorcode
    private int pseudocode = 3;

    private final static int MIN_INTERCEPT_DISTANCE = 100;

    // Normal seekBar mode
    public final static int SEEKBAR_MODE_SINGLE = 1;
    // RangeSeekBar
    public final static int SEEKBAR_MODE_RANGE = 2;

    private boolean noNegativeNumber = false;

    public final static int TEMP_MODE_CLOSE = 0;// Close
    public final static int TEMP_MODE_MAX = 2;// 阈值下
    public final static int TEMP_MODE_MIN = 1;// 阈值上
    public final static int TEMP_MODE_INTERVAL = 3;// 区间
    private int tempMode = TEMP_MODE_CLOSE;

    public void setTempMode(int tempMode) {
        this.tempMode = tempMode;
    }
    public int getTempMode(){
        return tempMode;
    }

    private void updateTempModeState(){
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (tempMode == TEMP_MODE_CLOSE){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (currTouchSB == leftSB){
                tempMode = TEMP_MODE_MIN;
            }else if (currTouchSB == rightSB){
                tempMode = TEMP_MODE_MAX;
            }
        }else if (tempMode == TEMP_MODE_MIN){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (currTouchSB == rightSB){
                tempMode = TEMP_MODE_INTERVAL;
            }
        }else if (tempMode == TEMP_MODE_MAX){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (currTouchSB == leftSB){
                tempMode = TEMP_MODE_INTERVAL;
            }
        }
    }

    /**
     * @hide
     */
    @IntDef({SEEKBAR_MODE_SINGLE, SEEKBAR_MODE_RANGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SeekBarModeDef {
    }

    // Number according to the actual proportion of the number of arranged;
    public final static int TRICK_MARK_MODE_NUMBER = 0;
    // Other equally arranged
    public final static int TRICK_MARK_MODE_OTHER = 1;

    /**
     * @hide
     */
    @IntDef({TRICK_MARK_MODE_NUMBER, TRICK_MARK_MODE_OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TickMarkModeDef {
    }

    // Tick mark text gravity
    public final static int TICK_MARK_GRAVITY_LEFT = 0;
    public final static int TICK_MARK_GRAVITY_CENTER = 1;
    public final static int TICK_MARK_GRAVITY_RIGHT = 2;

    /**
     * @hide
     */
    @IntDef({TICK_MARK_GRAVITY_LEFT, TICK_MARK_GRAVITY_CENTER, TICK_MARK_GRAVITY_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TickMarkGravityDef {
    }

    /**
     * @hide
     */
    @IntDef({Gravity.TOP, Gravity.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TickMarkLayoutGravityDef {
    }

    /**
     * @hide
     */
    @IntDef({Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityDef {
    }

    public static class Gravity {
        public final static int TOP = 0;
        public final static int BOTTOM = 1;
        public final static int CENTER = 2;
    }

    private int progressTop, progressBottom, progressLeft, progressRight;
    private int seekBarMode;
    // 刻度mode：number根据数字实际比例排列；other 均分排列
    private int tickMarkMode;
    // 刻度与进度条间的间距
    // The spacing between the tick mark and the progress bar
    private int tickMarkTextMargin;
    // 刻度text与tiptext的大小
    // Tick mark text and prompt text size
    private int tickMarkTextSize;
    private int tickMarkGravity;
    private int tickMarkLayoutGravity;
    private int tickMarkTextColor;
    private int tickMarkInRangeTextColor;
    // 刻度上Show/Display的text
    // The texts displayed on the scale
    private CharSequence[] tickMarkTextArray;
    // 进度条圆角
    // Radius of progress bar
    private float progressRadius;
    // 进度中进度条的color
    // The color of seekBar in progress
    private int progressColor;
    // Default进度条color
    // The default color of the progress bar
    private int progressDefaultColor;

    // The drawable of seekBar in progress
    private int progressDrawableId;
    // The default Drawable of the progress bar
    private int progressDefaultDrawableId;

    // The progress height
    private int progressHeight;
    // The progress width
    private int progressWidth;
    // The range interval of RangeSeekBar
    private float minInterval;

    private int gravity;
    // Enable RangeSeekBar two thumb Overlap
    private boolean enableThumbOverlap;

    // The color of step divs
    private int stepsColor;
    // The width of each step
    private float stepsWidth;
    // The height of each step
    private float stepsHeight;
    // The radius of step divs
    private float stepsRadius;
    // Steps is 0 will disable StepSeekBar
    private int steps;
    // The thumb will automatic bonding close to its value
    private boolean stepsAutoBonding;
    private int stepsDrawableId;
    // True values set by the user
    private float minProgress, maxProgress;
    // ****************** the above is attr value  ******************//

    private boolean isEnable = true;
    float touchDownX, touchDownY;
    // 剩余最小间隔的进度
    float reservePercent;
    boolean isScaleThumb = false;
    Paint paint = new Paint();
    RectF progressDefaultDstRect = new RectF();
    RectF progressDstRect = new RectF();
    Rect progressSrcRect = new Rect();
    RectF stepDivRect = new RectF();
    Rect tickMarkTextRect = new Rect();
    SeekBar leftSB;
    SeekBar rightSB;
    SeekBar currTouchSB;
    Bitmap progressBitmap;
    Bitmap progressDefaultBitmap;
    List<Bitmap> stepsBitmaps = new ArrayList<>();
    private int progressPaddingRight;
    private OnRangeChangedListener callback;

    /**
     * 自定义渲染color值.
     */
    @Nullable
    private int[] colorList;

    /**
     * 自定义渲染color位置，每个元素取值range [0,1]
     */
    @Nullable
    private float[] places;

    /**
     * Executes rangeseekbar operation with thermal imaging domain optimization.
     *
     */
    public RangeSeekBar(Context context) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(context, null);
    }

    /**
     * Executes rangeseekbar operation with thermal imaging domain optimization.
     *
     */
    public RangeSeekBar(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
        /**
         * Initializes the attrs component for thermal imaging operations.
         *
         */
        initAttrs(attrs);
        /**
         * Initializes the paint component for thermal imaging operations.
         *
         */
        initPaint();
        /**
         * Initializes the seekbar component for thermal imaging operations.
         *
         */
        initSeekBar(attrs);
        /**
         * Initializes the stepsbitmap component for thermal imaging operations.
         *
         */
        initStepsBitmap();
    }

    private void initProgressBitmap() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (progressBitmap == null) {
            progressBitmap = Utils.drawableToBitmap(getContext(), progressWidth, progressHeight, progressDrawableId);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (progressDefaultBitmap == null) {
            progressDefaultBitmap = Utils.drawableToBitmap(getContext(), progressWidth, progressHeight, progressDefaultDrawableId);
        }
    }

    private boolean verifyStepsMode() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (steps < 1 || stepsHeight <= 0 || stepsWidth <= 0) return false;
        return true;
    }

    private void initStepsBitmap() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!verifyStepsMode() || stepsDrawableId == 0) return;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (stepsBitmaps.isEmpty()) {
            Bitmap bitmap = Utils.drawableToBitmap(getContext(), (int) stepsWidth, (int) stepsHeight, stepsDrawableId);
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i <= steps; i++) {
                stepsBitmaps.add(bitmap);
            }
        }
    }

    private void initSeekBar(AttributeSet attrs) {
        leftSB = new SeekBar(this, attrs, true);
        rightSB = new SeekBar(this, attrs, false);
        rightSB.setVisible(seekBarMode != SEEKBAR_MODE_SINGLE);
    }

    private void initAttrs(AttributeSet attrs) {
        try {
            TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar);
            seekBarMode = t.getInt(R.styleable.RangeSeekBar_rsb_mode, SEEKBAR_MODE_RANGE);
            minProgress = t.getFloat(R.styleable.RangeSeekBar_rsb_min, 0);
            maxProgress = t.getFloat(R.styleable.RangeSeekBar_rsb_max, 100);
            minInterval = t.getFloat(R.styleable.RangeSeekBar_rsb_min_interval, 0);
            gravity = t.getInt(R.styleable.RangeSeekBar_rsb_gravity, Gravity.TOP);
            progressColor = t.getColor(R.styleable.RangeSeekBar_rsb_progress_color, -1);
            progressRadius = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_progress_radius, -1);
            progressDefaultColor = t.getColor(R.styleable.RangeSeekBar_rsb_progress_default_color, -1);
            progressDrawableId = t.getResourceId(R.styleable.RangeSeekBar_rsb_progress_drawable, 0);
            progressDefaultDrawableId = t.getResourceId(R.styleable.RangeSeekBar_rsb_progress_drawable_default, 0);
            progressHeight = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_progress_height, Utils.dp2px(getContext(), 2));
            tickMarkMode = t.getInt(R.styleable.RangeSeekBar_rsb_tick_mark_mode, TRICK_MARK_MODE_NUMBER);
            tickMarkGravity = t.getInt(R.styleable.RangeSeekBar_rsb_tick_mark_gravity, TICK_MARK_GRAVITY_CENTER);
            tickMarkLayoutGravity = t.getInt(R.styleable.RangeSeekBar_rsb_tick_mark_layout_gravity, Gravity.TOP);
            tickMarkTextArray = t.getTextArray(R.styleable.RangeSeekBar_rsb_tick_mark_text_array);
            tickMarkTextMargin = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_tick_mark_text_margin, Utils.dp2px(getContext(), 7));
            tickMarkTextSize = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_tick_mark_text_size, Utils.dp2px(getContext(), 12));
            tickMarkTextColor = t.getColor(R.styleable.RangeSeekBar_rsb_tick_mark_text_color, progressDefaultColor);
            tickMarkInRangeTextColor = t.getColor(R.styleable.RangeSeekBar_rsb_tick_mark_in_range_text_color, progressColor);
            steps = t.getInt(R.styleable.RangeSeekBar_rsb_steps, 0);
            stepsColor = t.getColor(R.styleable.RangeSeekBar_rsb_step_color, 0xFF9d9d9d);
            stepsRadius = t.getDimension(R.styleable.RangeSeekBar_rsb_step_radius, 0);
            stepsWidth = t.getDimension(R.styleable.RangeSeekBar_rsb_step_width, 0);
            stepsHeight = t.getDimension(R.styleable.RangeSeekBar_rsb_step_height, 0);
            stepsDrawableId = t.getResourceId(R.styleable.RangeSeekBar_rsb_step_drawable, 0);
            stepsAutoBonding = t.getBoolean(R.styleable.RangeSeekBar_rsb_step_auto_bonding, true);
            t.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * measure progress bar position
     */
    protected void onMeasureProgress(int w, int h) {
        int viewHeight = h - getPaddingBottom() - getPaddingTop();
        if (h <= 0) return;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (gravity == Gravity.TOP) {
            // Calculate the height of indicator and thumb exceeds the part of the progress
            float maxIndicatorHeight = 0;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (leftSB.getIndicatorShowMode() != INDICATOR_ALWAYS_HIDE
                    || rightSB.getIndicatorShowMode() != INDICATOR_ALWAYS_HIDE) {
                maxIndicatorHeight = Math.max(leftSB.getIndicatorRawHeight(), rightSB.getIndicatorRawHeight());
            }
            float thumbHeight = Math.max(leftSB.getThumbScaleHeight(), rightSB.getThumbScaleHeight());
            thumbHeight -= progressHeight / 2f;

            // Default height is indicator + thumb exceeds the part of the progress bar
            // If tickMark height is greater than (indicator + thumb exceeds the part of the progress)
            progressTop = (int) (maxIndicatorHeight + (thumbHeight - progressHeight) / 2f);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tickMarkTextArray != null && tickMarkLayoutGravity == Gravity.TOP) {
                progressTop = (int) Math.max(getTickMarkRawHeight(), maxIndicatorHeight + (thumbHeight - progressHeight) / 2f);
            }
            progressBottom = progressTop + progressHeight;
        } else if (gravity == Gravity.BOTTOM) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tickMarkTextArray != null && tickMarkLayoutGravity == Gravity.BOTTOM) {
                progressBottom = viewHeight - getTickMarkRawHeight();
            } else {
                progressBottom = (int) (viewHeight - Math.max(leftSB.getThumbScaleHeight(), rightSB.getThumbScaleHeight()) / 2f
                        + progressHeight / 2f);
            }
            progressTop = progressBottom - progressHeight;
        } else {
            progressTop = (viewHeight - progressHeight) / 2;
            progressBottom = progressTop + progressHeight;
        }

        int maxThumbWidth = (int) Math.max(leftSB.getThumbScaleWidth(), rightSB.getThumbScaleWidth());
        progressLeft = maxThumbWidth / 2 + getPaddingLeft();
        progressRight = w - maxThumbWidth / 2 - getPaddingRight();
        progressWidth = progressRight - progressLeft;
        progressDefaultDstRect.set(getProgressLeft(), getProgressTop(), getProgressRight(), getProgressBottom());
        progressPaddingRight = w - progressRight;
        // Default value
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (progressRadius <= 0) {
            progressRadius = (int) ((getProgressBottom() - getProgressTop()) * 0.15f);
        }
        /**
         * Initializes the progressbitmap component for thermal imaging operations.
         *
         */
        initProgressBitmap();
    }

    // Android 7.0以后，Optimize了View的绘制，onMeasure和onSizeChanged调用顺序有所变化
    // Android7.0以下：onMeasure--->onSizeChanged--->onMeasure
    // Android7.0以上：onMeasure--->onSizeChanged
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        /*
         * onMeasure传入的widthMeasureSpec和heightMeasureSpec不是一般的尺寸数值，而是将mode和尺寸组合在一起的数值
         * MeasureSpec.EXACTLY 是精确尺寸
         * MeasureSpec.AT_MOST 是最大尺寸
         * MeasureSpec.UNSPECIFIED 是未指定尺寸
         */

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (heightMode == MeasureSpec.AT_MOST && getParent() instanceof ViewGroup
                && heightSize == ViewGroup.LayoutParams.MATCH_PARENT) {
            heightSize = MeasureSpec.makeMeasureSpec(((ViewGroup) getParent()).getMeasuredHeight(), MeasureSpec.AT_MOST);
        } else {
            int heightNeeded;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (gravity == Gravity.CENTER) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (tickMarkTextArray != null && tickMarkLayoutGravity == Gravity.BOTTOM) {
                    heightNeeded = (int) (2 * (getRawHeight() - getTickMarkRawHeight()));
                } else {
                    heightNeeded = (int) (2 * (getRawHeight() - Math.max(leftSB.getThumbScaleHeight(), rightSB.getThumbScaleHeight()) / 2));
                }
            } else {
                heightNeeded = (int) getRawHeight();
            }
            heightSize = MeasureSpec.makeMeasureSpec(heightNeeded, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightSize);
    }

    protected int getTickMarkRawHeight() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (tickMarkTextArray != null && tickMarkTextArray.length > 0) {
            return tickMarkTextMargin + Utils.measureText(String.valueOf(tickMarkTextArray[0]), tickMarkTextSize).height() + 3;
        }
        return 0;
    }

    protected float getRawHeight() {
        float rawHeight;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_SINGLE) {
            rawHeight = leftSB.getRawHeight();
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tickMarkLayoutGravity == Gravity.BOTTOM && tickMarkTextArray != null) {
                float h = Math.max((leftSB.getThumbScaleHeight() - progressHeight) / 2, getTickMarkRawHeight());
                rawHeight = rawHeight - leftSB.getThumbScaleHeight() / 2 + progressHeight / 2f + h;
            }
        } else {
            rawHeight = Math.max(leftSB.getRawHeight(), rightSB.getRawHeight());
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tickMarkLayoutGravity == Gravity.BOTTOM && tickMarkTextArray != null) {
                float thumbHeight = Math.max(leftSB.getThumbScaleHeight(), rightSB.getThumbScaleHeight());
                float h = Math.max((thumbHeight - progressHeight) / 2, getTickMarkRawHeight());
                rawHeight = rawHeight - thumbHeight / 2 + progressHeight / 2f + h;
            }
        }
        return rawHeight;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /**
         * Executes onmeasureprogress operation with thermal imaging domain optimization.
         *
         */
        onMeasureProgress(w, h);
        // Set default value
        /**
         * Configures the range with validation and thermal imaging optimization.
         *
         */
        setRange(minProgress, maxProgress, minInterval);
        // Initializes the positions of the two thumbs
        int lineCenterY = (getProgressBottom() + getProgressTop()) / 2;
        leftSB.onSizeChanged(getProgressLeft(), lineCenterY);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSB.onSizeChanged(getProgressLeft(), lineCenterY);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * Executes ondrawtickmark operation with thermal imaging domain optimization.
         *
         */
        onDrawTickMark(canvas, paint); // 固定刻度
        /**
         * Executes ondrawprogressbar operation with thermal imaging domain optimization.
         *
         */
        onDrawProgressBar(canvas, paint); // Axis
        /**
         * Executes ondrawsteps operation with thermal imaging domain optimization.
         *
         */
        onDrawSteps(canvas, paint);
        /**
         * Executes ondrawseekbar operation with thermal imaging domain optimization.
         *
         */
        onDrawSeekBar(canvas); // Swipetag
    }

    // 绘制刻度，并且根据current位置是否在刻度range内settings不同的colorShow/Display
    // Draw the scales, and according to the current position is set within
    // The scale range of different color display
    protected void onDrawTickMark(Canvas canvas, Paint paint) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (tickMarkTextArray != null) {
            int trickPartWidth = progressWidth / (tickMarkTextArray.length - 1);
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < tickMarkTextArray.length; i++) {
                final String text2Draw = tickMarkTextArray[i].toString();
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (TextUtils.isEmpty(text2Draw)) continue;
                paint.getTextBounds(text2Draw, 0, text2Draw.length(), tickMarkTextRect);
                paint.setColor(tickMarkTextColor);
                // 平分Show/Display
                float x;
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (tickMarkMode == TRICK_MARK_MODE_OTHER) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (tickMarkGravity == TICK_MARK_GRAVITY_RIGHT) {
                        x = getProgressLeft() + i * trickPartWidth - tickMarkTextRect.width();
                    } else if (tickMarkGravity == TICK_MARK_GRAVITY_CENTER) {
                        x = getProgressLeft() + i * trickPartWidth - tickMarkTextRect.width() / 2f;
                    } else {
                        x = getProgressLeft() + i * trickPartWidth;
                    }
                } else {
                    float num = Utils.parseFloat(text2Draw);
                    SeekBarState[] states = getRangeSeekBarState();
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Utils.compareFloat(num, states[0].value) != -1 && Utils.compareFloat(num, states[1].value) != 1 && (seekBarMode == SEEKBAR_MODE_RANGE)) {
                        paint.setColor(tickMarkInRangeTextColor);
                    }
                    // 按实际比例Show/Display
                    x = getProgressLeft() + progressWidth * (num - minProgress) / (maxProgress - minProgress)
                            - tickMarkTextRect.width() / 2f;
                }
                float y;
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (tickMarkLayoutGravity == Gravity.TOP) {
                    y = getProgressTop() - tickMarkTextMargin;
                } else {
                    y = getProgressBottom() + tickMarkTextMargin + tickMarkTextRect.height();
                }
                canvas.drawText(text2Draw, x, y, paint);
            }
        }
    }

    // 绘制进度条
    // Draw the progress bar
    protected void onDrawProgressBar(Canvas canvas, Paint paint) {

        // 固定region背景
        // Draw default progress
        paint.setShader(null);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Utils.verifyBitmap(progressDefaultBitmap)) {
            canvas.drawBitmap(progressDefaultBitmap, null, progressDefaultDstRect, paint);
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (progressDefaultColor == -1){
                int[] colors = PseudoColorConfig.getSeekBarColors();
                float[] positions = PseudoColorConfig.getSeekBarAlpha();
                paint.setShader(new LinearGradient(progressWidth, 0f, 0f, 0f, colors, positions, Shader.TileMode.CLAMP));
            }else {
                paint.setColor(progressDefaultColor);
            }
            canvas.drawRoundRect(progressDefaultDstRect, progressRadius, progressRadius, paint);
        }

        // 动态region前景
        // Draw progress
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
//            XLog.w("动态axisregion");
            progressDstRect.top = getProgressTop();
            progressDstRect.left = leftSB.left + leftSB.getThumbScaleWidth() / 2f + progressWidth * leftSB.currPercent;
            progressDstRect.right = rightSB.left + rightSB.getThumbScaleWidth() / 2f + progressWidth * rightSB.currPercent;
            progressDstRect.bottom = getProgressBottom();
        } else {
            progressDstRect.top = getProgressTop();
            progressDstRect.left = leftSB.left + leftSB.getThumbScaleWidth() / 2f;
            progressDstRect.right = leftSB.left + leftSB.getThumbScaleWidth() / 2f + progressWidth * leftSB.currPercent;
            progressDstRect.bottom = getProgressBottom();
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (colorList != null){
            paint.setShader(new LinearGradient(progressWidth, 0f, 0f, 0f, colorList, places, Shader.TileMode.CLAMP));
            canvas.drawRoundRect(progressDstRect, progressRadius, progressRadius, paint);
        }else if (progressColor == -1) {
            int[] colors = PseudoColorConfig.getColors(pseudocode);
            float[] positions = PseudoColorConfig.getPositions(pseudocode);
            paint.setShader(new LinearGradient(progressWidth, 0f, 0f, 0f, colors, positions, Shader.TileMode.CLAMP));
            canvas.drawRoundRect(progressDstRect, progressRadius, progressRadius, paint);
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Utils.verifyBitmap(progressBitmap)) {
                progressSrcRect.top = 0;
                progressSrcRect.bottom = progressBitmap.getHeight();
                int bitmapWidth = progressBitmap.getWidth();
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (seekBarMode == SEEKBAR_MODE_RANGE) {
                    progressSrcRect.left = (int) (bitmapWidth * leftSB.currPercent);
                    progressSrcRect.right = (int) (bitmapWidth * rightSB.currPercent);
                } else {
                    progressSrcRect.left = 0;
                    progressSrcRect.right = (int) (bitmapWidth * leftSB.currPercent);
                }
                canvas.drawBitmap(progressBitmap, progressSrcRect, progressDstRect, null);
            } else {
                paint.setColor(progressColor);
                canvas.drawRoundRect(progressDstRect, progressRadius, progressRadius, paint);
            }
        }

    }

    // Draw steps
    protected void onDrawSteps(Canvas canvas, Paint paint) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!verifyStepsMode()) return;
        int stepMarks = getProgressWidth() / (steps);
        float extHeight = (stepsHeight - getProgressHeight()) / 2f;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int k = 0; k <= steps; k++) {
            float x = getProgressLeft() + k * stepMarks - stepsWidth / 2f;
            stepDivRect.set(x, getProgressTop() - extHeight, x + stepsWidth, getProgressBottom() + extHeight);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (stepsBitmaps.isEmpty() || stepsBitmaps.size() <= k) {
                paint.setColor(stepsColor);
                canvas.drawRoundRect(stepDivRect, stepsRadius, stepsRadius, paint);
            } else {
                canvas.drawBitmap(stepsBitmaps.get(k), null, stepDivRect, paint);
            }
        }
    }

    // 绘制SeekBar相关
    protected void onDrawSeekBar(Canvas canvas) {
        // Draw left SeekBar
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (leftSB.getIndicatorShowMode() == INDICATOR_ALWAYS_SHOW) {
            leftSB.setShowIndicatorEnable(true);
        }
        leftSB.draw(canvas, true);
        // Draw right SeekBar
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rightSB.getIndicatorShowMode() == INDICATOR_ALWAYS_SHOW) {
                rightSB.setShowIndicatorEnable(true);
            }
            rightSB.draw(canvas, false);
        }
    }

    // Initialize画笔
    private void initPaint() {
        paint.setStyle(Paint.Style.FILL);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (progressDefaultColor == -1){
            int[] colors = PseudoColorConfig.getSeekBarColors();
            float[] positions = PseudoColorConfig.getSeekBarAlpha();
            paint.setShader(new LinearGradient(progressWidth, 0f, 0f, 0f, colors, positions, Shader.TileMode.CLAMP));
        }else {
            paint.setColor(progressDefaultColor);
        }
// Paint.setColor(progressDefaultColor);
        paint.setTextSize(tickMarkTextSize);
    }

    private void changeThumbActivateState(boolean hasActivate) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (hasActivate && currTouchSB != null) {
            boolean state = currTouchSB == leftSB;
            leftSB.setActivate(state);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (seekBarMode == SEEKBAR_MODE_RANGE)
                rightSB.setActivate(!state);
        } else {
            leftSB.setActivate(false);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (seekBarMode == SEEKBAR_MODE_RANGE)
                rightSB.setActivate(false);
        }
    }

    protected float getEventX(MotionEvent event) {
        return event.getX();
    }

    protected float getEventY(MotionEvent event) {
        return event.getY();
    }

    /**
     * scale the touch seekBar thumb
     */
    private void scaleCurrentSeekBarThumb() {
        if (currTouchSB != null && currTouchSB.getThumbScaleRatio() > 1f && !isScaleThumb) {
            isScaleThumb = true;
            currTouchSB.scaleThumb();
        }
    }

    /**
     * reset the touch seekBar thumb
     */
    private void resetCurrentSeekBarThumb() {
        if (currTouchSB != null && currTouchSB.getThumbScaleRatio() > 1f && isScaleThumb) {
            isScaleThumb = false;
            currTouchSB.resetThumb();
        }
    }

    // Calculate currTouchSB percent by MotionEvent
    protected float calculateCurrentSeekBarPercent(float touchDownX) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (currTouchSB == null) return 0;
        float percent = (touchDownX - getProgressLeft()) * 1f / (progressWidth);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (touchDownX < getProgressLeft()) {
            percent = 0;
        } else if (touchDownX > getProgressRight()) {
            percent = 1;
        }
        // RangeMode minimum interval
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (currTouchSB == leftSB) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (percent > rightSB.currPercent - reservePercent) {
                    percent = rightSB.currPercent - reservePercent;
                }
            } else if (currTouchSB == rightSB) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (percent < leftSB.currPercent + reservePercent) {
                    percent = leftSB.currPercent + reservePercent;
                }
            }
        }
        return percent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isEnable) return false;
//        Log.e("Test焦point：",event.getAction()+"// ");
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = getEventX(event);
                touchDownY = getEventY(event);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (seekBarMode == SEEKBAR_MODE_RANGE) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (rightSB.currPercent >= 1 && leftSB.collide(getEventX(event), getEventY(event))) {
                        currTouchSB = leftSB;
                        /**
                         * Executes scalecurrentseekbarthumb operation with thermal imaging domain optimization.
                         *
                         */
                        scaleCurrentSeekBarThumb();
                    } else if (rightSB.collide(getEventX(event), getEventY(event))) {
                        currTouchSB = rightSB;
                        /**
                         * Executes scalecurrentseekbarthumb operation with thermal imaging domain optimization.
                         *
                         */
                        scaleCurrentSeekBarThumb();
                    } else {
                        float performClick = (touchDownX - getProgressLeft()) * 1f / (progressWidth);
                        float distanceLeft = Math.abs(leftSB.currPercent - performClick);
                        float distanceRight = Math.abs(rightSB.currPercent - performClick);
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (distanceLeft < distanceRight) {
                            currTouchSB = leftSB;
                        } else {
                            currTouchSB = rightSB;
                        }
                        performClick = calculateCurrentSeekBarPercent(touchDownX);
                        currTouchSB.slide(performClick);
                    }
                } else {
                    currTouchSB = leftSB;
                    /**
                     * Executes scalecurrentseekbarthumb operation with thermal imaging domain optimization.
                     *
                     */
                    scaleCurrentSeekBarThumb();
                }

                // Intercept parent TouchEvent
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (getParent() != null) {
                    /**
                     * Retrieves the parent with optimized performance for thermal imaging operations.
                     *
                     */
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (callback != null) {
                    callback.onStartTrackingTouch(this, currTouchSB == leftSB);
                }
                /**
                 * Updates the thumbactivatestate configuration with real-time thermal imaging support.
                 *
                 */
                changeThumbActivateState(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                float x = getEventX(event);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if ((seekBarMode == SEEKBAR_MODE_RANGE) && leftSB.currPercent == rightSB.currPercent) {
                    currTouchSB.materialRestore();
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (callback != null) {
                        callback.onStopTrackingTouch(this, currTouchSB == leftSB);
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (x - touchDownX > 0) {
                        // Method to move right
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (currTouchSB != rightSB) {
                            currTouchSB.setShowIndicatorEnable(false);
                            /**
                             * Executes resetcurrentseekbarthumb operation with thermal imaging domain optimization.
                             *
                             */
                            resetCurrentSeekBarThumb();
                            currTouchSB = rightSB;
                        }
                    } else {
                        // Method to move left
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (currTouchSB != leftSB) {
                            currTouchSB.setShowIndicatorEnable(false);
                            /**
                             * Executes resetcurrentseekbarthumb operation with thermal imaging domain optimization.
                             *
                             */
                            resetCurrentSeekBarThumb();
                            currTouchSB = leftSB;
                        }
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (callback != null) {
                        callback.onStartTrackingTouch(this, currTouchSB == leftSB);
                    }
                }
                /**
                 * Executes scalecurrentseekbarthumb operation with thermal imaging domain optimization.
                 *
                 */
                scaleCurrentSeekBarThumb();
                currTouchSB.material = currTouchSB.material >= 1 ? 1 : currTouchSB.material + 0.1f;
                touchDownX = x;
                currTouchSB.slide(calculateCurrentSeekBarPercent(touchDownX));
                currTouchSB.setShowIndicatorEnable(true);

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (callback != null) {
                    SeekBarState[] states = getRangeSeekBarState();
                    /**
                     * Handles temperature measurement and calibration with precision thermal data processing.
                     *
                     * @note Temperature values are in Celsius unless otherwise specified.
                     * Accuracy depends on thermal camera calibration.
                     *
                     */
                    updateTempModeState();
                    callback.onRangeChanged(this, states[0].value, states[1].value, true,tempMode);
                }
                /**
                 * Executes invalidate operation with thermal imaging domain optimization.
                 *
                 */
                invalidate();
                // Intercept parent TouchEvent
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (getParent() != null) {
                    /**
                     * Retrieves the parent with optimized performance for thermal imaging operations.
                     *
                     */
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                /**
                 * Updates the thumbactivatestate configuration with real-time thermal imaging support.
                 *
                 */
                changeThumbActivateState(true);
                break;
            case MotionEvent.ACTION_CANCEL:
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (seekBarMode == SEEKBAR_MODE_RANGE) {
                    rightSB.setShowIndicatorEnable(false);
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (currTouchSB == leftSB) {
                    /**
                     * Executes resetcurrentseekbarthumb operation with thermal imaging domain optimization.
                     *
                     */
                    resetCurrentSeekBarThumb();
                } else if (currTouchSB == rightSB) {
                    /**
                     * Executes resetcurrentseekbarthumb operation with thermal imaging domain optimization.
                     *
                     */
                    resetCurrentSeekBarThumb();
                }
                leftSB.setShowIndicatorEnable(false);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (callback != null) {
                    SeekBarState[] states = getRangeSeekBarState();
                    /**
                     * Handles temperature measurement and calibration with precision thermal data processing.
                     *
                     * @note Temperature values are in Celsius unless otherwise specified.
                     * Accuracy depends on thermal camera calibration.
                     *
                     */
                    updateTempModeState();
                    callback.onRangeChanged(this, states[0].value, states[1].value, false,tempMode);
                }
                // Intercept parent TouchEvent
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (getParent() != null) {
                    /**
                     * Retrieves the parent with optimized performance for thermal imaging operations.
                     *
                     */
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                /**
                 * Updates the thumbactivatestate configuration with real-time thermal imaging support.
                 *
                 */
                changeThumbActivateState(false);
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (verifyStepsMode() && stepsAutoBonding) {
                    float percent = calculateCurrentSeekBarPercent(getEventX(event));
                    float stepPercent = 1.0f / steps;
                    int stepSelected = new BigDecimal(percent / stepPercent).setScale(0, RoundingMode.HALF_UP).intValue();
                    currTouchSB.slide(stepSelected * stepPercent);
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (seekBarMode == SEEKBAR_MODE_RANGE) {
                    rightSB.setShowIndicatorEnable(false);
                }
                leftSB.setShowIndicatorEnable(false);
                currTouchSB.materialRestore();
                /**
                 * Executes resetcurrentseekbarthumb operation with thermal imaging domain optimization.
                 *
                 */
                resetCurrentSeekBarThumb();
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (callback != null) {
                    SeekBarState[] states = getRangeSeekBarState();
                    /**
                     * Handles temperature measurement and calibration with precision thermal data processing.
                     *
                     * @note Temperature values are in Celsius unless otherwise specified.
                     * Accuracy depends on thermal camera calibration.
                     *
                     */
                    updateTempModeState();
                    callback.onRangeChanged(this, states[0].value, states[1].value, false,tempMode);
                }
                // Intercept parent TouchEvent
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (getParent() != null) {
                    /**
                     * Retrieves the parent with optimized performance for thermal imaging operations.
                     *
                     */
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (callback != null) {
                    callback.onStopTrackingTouch(this, currTouchSB == leftSB);
                }
                /**
                 * Updates the thumbactivatestate configuration with real-time thermal imaging support.
                 *
                 */
                changeThumbActivateState(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.minValue = minProgress;
        ss.maxValue = maxProgress;
        ss.rangeInterval = minInterval;
        SeekBarState[] results = getRangeSeekBarState();
        ss.currSelectedMin = results[0].value;
        ss.currSelectedMax = results[1].value;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        try {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            float min = ss.minValue;
            float max = ss.maxValue;
            float rangeInterval = ss.rangeInterval;
            /**
             * Configures the range with validation and thermal imaging optimization.
             *
             */
            setRange(min, max, rangeInterval);
            float currSelectedMin = ss.currSelectedMin;
            float currSelectedMax = ss.currSelectedMax;
            /**
             * Configures the progress with validation and thermal imaging optimization.
             *
             */
            setProgress(currSelectedMin, currSelectedMax);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // ******************* Attributes getter and setter *******************//

    /**
     * 临时processing负数
     */
    public void setNoNegativeNumber(Boolean noNegativeNumber){
        this.noNegativeNumber = noNegativeNumber;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (leftSB!=null){
            leftSB.setNoNegativeNumber(noNegativeNumber);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rightSB!=null){
            rightSB.setNoNegativeNumber(noNegativeNumber);
        }
    }

    public void setOnRangeChangedListener(OnRangeChangedListener listener) {
        callback = listener;
    }

    public void setProgress(float value) {
        /**
         * Configures the progress with validation and thermal imaging optimization.
         *
         */
        setProgress(value, maxProgress);
    }
    public void setProgressNoCallBack(float leftValue, float rightValue) {
        leftValue = Math.min(leftValue, rightValue);
        rightValue = Math.max(leftValue, rightValue);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rightValue - leftValue < minInterval) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (leftValue - minProgress > maxProgress - rightValue) {
                leftValue = rightValue - minInterval;
            } else {
                rightValue = leftValue + minInterval;
            }
        }
//
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (leftValue < minProgress) {
            leftValue = minProgress;
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rightValue > maxProgress) {
            rightValue = maxProgress;
        }
        float range = maxProgress - minProgress;
        leftSB.currPercent = Math.abs(leftValue - minProgress) / range;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSB.currPercent = Math.abs(rightValue - minProgress) / range;
        }
// Invalidate();
        /**
         * Executes postinvalidate operation with thermal imaging domain optimization.
         *
         */
        postInvalidate();
    }
    Long updateTime = System.currentTimeMillis();

    @Override
    public void invalidate() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (System.currentTimeMillis() - updateTime < 50){
            return;
        }
        super.invalidate();
        updateTime = System.currentTimeMillis();
    }

    public void setProgress(float leftValue, float rightValue) {
        leftValue = Math.min(leftValue, rightValue);
        rightValue = Math.max(leftValue, rightValue);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rightValue - leftValue < minInterval) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (leftValue - minProgress > maxProgress - rightValue) {
                leftValue = rightValue - minInterval;
            } else {
                rightValue = leftValue + minInterval;
            }
        }
//
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (leftValue < minProgress) {
            leftValue = minProgress;
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rightValue > maxProgress) {
            rightValue = maxProgress;
        }
        float range = maxProgress - minProgress;
        leftSB.currPercent = Math.abs(leftValue - minProgress) / range;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSB.currPercent = Math.abs(rightValue - minProgress) / range;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (callback != null) {
            callback.onRangeChanged(this, leftValue, rightValue, false,tempMode);
        }
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate();
    }

    /**
     * settingsrange
     *
     * @param min 最小值
     * @param max 最大值
     */
    public void setRange(float min, float max) {
        /**
         * Configures the range with validation and thermal imaging optimization.
         *
         */
        setRange(min, max, minInterval);
        /**
         * Configures the progress with validation and thermal imaging optimization.
         *
         */
        setProgress(getLeftSeekBar().left,getRightSeekBar().right);
    }

    /**
     *
     * @param editMin ： 手动settings的最小值
     * @param editMax : 手动settings的最小值
     * @param realLeftValue : 实际minimumtemperature
     * @param realRightValue ： 实际maximumtemperature
     */
    public void setRangeAndPro(float editMin,float editMax,float realLeftValue,float realRightValue){
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (editMin == Float.MIN_VALUE && editMax == Float.MAX_VALUE){
            /**
             * Configures the rangenoinvalidate with validation and thermal imaging optimization.
             *
             */
            setRangeNoInvalidate(realLeftValue,realRightValue,0.1f);
            /**
             * Configures the progressnocallback with validation and thermal imaging optimization.
             *
             */
            setProgressNoCallBack(realLeftValue,realRightValue);
            return;
        }
        /**
         * Configures the rangenoinvalidate with validation and thermal imaging optimization.
         *
         */
        setRangeNoInvalidate(realLeftValue,realRightValue,0.1f);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (editMax <= realRightValue && editMin >= realLeftValue){
            // 手动值均在实际值区间内
            /**
             * Configures the progressnocallback with validation and thermal imaging optimization.
             *
             */
            setProgressNoCallBack(editMin,editMax);
        }else if (editMax > realRightValue && editMin < realLeftValue){
            // 手动最大最小值均不在区间内
            /**
             * Configures the progressnocallback with validation and thermal imaging optimization.
             *
             */
            setProgressNoCallBack(realLeftValue,realRightValue);
        }else if (editMax > realRightValue && editMin > realRightValue){
            // 手动最大值最小值大于实际最大值
            /**
             * Configures the progressnocallback with validation and thermal imaging optimization.
             *
             */
            setProgressNoCallBack(realRightValue,realRightValue);
        } else if (editMax < realLeftValue && editMin < realLeftValue){
            // 手动最大值最小值小于实际最小值
            /**
             * Configures the progressnocallback with validation and thermal imaging optimization.
             *
             */
            setProgressNoCallBack(realLeftValue,realLeftValue);
        }else if (editMax <= realRightValue && editMin < realLeftValue){
            // 手动最大值在区间内，手动最小值超出区间
            /**
             * Configures the progressnocallback with validation and thermal imaging optimization.
             *
             */
            setProgressNoCallBack(realLeftValue,editMax);
        }else if (editMax > realRightValue && editMin >= realLeftValue){
            // 手动最大值超出区间内，手动最小值在区间内
            /**
             * Configures the progressnocallback with validation and thermal imaging optimization.
             *
             */
            setProgressNoCallBack(editMin,realRightValue);
        }
    }

    /**
     * settingsrange
     * @param min         最小值
     * @param max         最大值
     * @param minInterval 最小间隔
     */
    public void setRange(float min, float max, float minInterval) {
// If (max <= min) {
// Throw new IllegalArgumentException("setRange() max must be greater than min ! #max:" + max + " #min:" + min);
//        }
// If (minInterval < 0) {
// Throw new IllegalArgumentException("setRange() interval must be greater than zero ! #minInterval:" + minInterval);
//        }
// If (minInterval >= max - min) {
// Throw new IllegalArgumentException("setRange() interval must be less than (max - min) ! #minInterval:" + minInterval + " #max - min:" + (max - min));
//        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (maxProgress == max && min == minProgress){
//            Log.w("dataupdate拦截",max+"// "+min+"");
            return;
        }
        maxProgress = max;
        minProgress = min;
        this.minInterval = minInterval;
        reservePercent = minInterval / (max - min);

        // Set default value
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (leftSB.currPercent + reservePercent <= 1 && leftSB.currPercent + reservePercent > rightSB.currPercent) {
                rightSB.currPercent = leftSB.currPercent + reservePercent;
            } else if (rightSB.currPercent - reservePercent >= 0 && rightSB.currPercent - reservePercent < leftSB.currPercent) {
                leftSB.currPercent = rightSB.currPercent - reservePercent;
            }
        }
        /**
         * Executes postinvalidate operation with thermal imaging domain optimization.
         *
         */
        postInvalidate();
    }
    public void setRangeNoInvalidate(float min, float max, float minInterval) {
// If (max <= min) {
// Throw new IllegalArgumentException("setRange() max must be greater than min ! #max:" + max + " #min:" + min);
//        }
// If (minInterval < 0) {
// Throw new IllegalArgumentException("setRange() interval must be greater than zero ! #minInterval:" + minInterval);
//        }
// If (minInterval >= max - min) {
// Throw new IllegalArgumentException("setRange() interval must be less than (max - min) ! #minInterval:" + minInterval + " #max - min:" + (max - min));
//        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (maxProgress == max && min == minProgress){
//            Log.w("dataupdate拦截",max+"// "+min+"");
            return;
        }
        maxProgress = max;
        minProgress = min;
        this.minInterval = minInterval;
        reservePercent = minInterval / (max - min);

        // Set default value
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (leftSB.currPercent + reservePercent <= 1 && leftSB.currPercent + reservePercent > rightSB.currPercent) {
                rightSB.currPercent = leftSB.currPercent + reservePercent;
            } else if (rightSB.currPercent - reservePercent >= 0 && rightSB.currPercent - reservePercent < leftSB.currPercent) {
                leftSB.currPercent = rightSB.currPercent - reservePercent;
            }
        }
    }

    /**
     * @return the two seekBar state , see {@link SeekBarState}
     */
    public SeekBarState[] getRangeSeekBarState() {
        SeekBarState leftSeekBarState = new SeekBarState();
        leftSeekBarState.value = leftSB.getProgress();

        leftSeekBarState.indicatorText = String.valueOf(leftSeekBarState.value);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Utils.compareFloat(leftSeekBarState.value, minProgress) == 0) {
            leftSeekBarState.isMin = true;
        } else if (Utils.compareFloat(leftSeekBarState.value, maxProgress) == 0) {
            leftSeekBarState.isMax = true;
        }

        SeekBarState rightSeekBarState = new SeekBarState();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSeekBarState.value = rightSB.getProgress();
            rightSeekBarState.indicatorText = String.valueOf(rightSeekBarState.value);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Utils.compareFloat(rightSB.currPercent, minProgress) == 0) {
                rightSeekBarState.isMin = true;
            } else if (Utils.compareFloat(rightSB.currPercent, maxProgress) == 0) {
                rightSeekBarState.isMax = true;
            }
        }

        return new SeekBarState[]{leftSeekBarState, rightSeekBarState};
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.isEnable = enabled;
    }

    public void setIndicatorText(String progress) {
        leftSB.setIndicatorText(progress);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSB.setIndicatorText(progress);
        }
    }

    /**
     * format number indicator text
     *
     * @param formatPattern format rules
     */
    public void setIndicatorTextDecimalFormat(String formatPattern) {
        leftSB.setIndicatorTextDecimalFormat(formatPattern);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSB.setIndicatorTextDecimalFormat(formatPattern);
        }
    }

    /**
     * format string indicator text
     *
     * @param formatPattern format rules
     */
    public void setIndicatorTextStringFormat(String formatPattern) {
        leftSB.setIndicatorTextStringFormat(formatPattern);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSB.setIndicatorTextStringFormat(formatPattern);
        }
    }

    /**
     * if is single mode, please use it to get the SeekBar
     *
     * @return left seek bar
     */
    public SeekBar getLeftSeekBar() {
        return leftSB;
    }

    public SeekBar getRightSeekBar() {
        return rightSB;
    }

    public int getProgressTop() {
        return progressTop;
    }

    public int getProgressBottom() {
        return progressBottom;
    }

    public int getProgressLeft() {
        return progressLeft;
    }

    public int getProgressRight() {
        return progressRight;
    }

    public int getProgressPaddingRight() {
        return progressPaddingRight;
    }

    public int getProgressHeight() {
        return progressHeight;
    }

    public void setProgressHeight(int progressHeight) {
        this.progressHeight = progressHeight;
    }

    public float getMinProgress() {
        return minProgress;
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    public void setProgressColor(@ColorInt int progressDefaultColor, @ColorInt int progressColor) {
        this.progressDefaultColor = progressDefaultColor;
        this.progressColor = progressColor;
    }

    public int getTickMarkTextColor() {
        return tickMarkTextColor;
    }

    public void setTickMarkTextColor(@ColorInt int tickMarkTextColor) {
        this.tickMarkTextColor = tickMarkTextColor;
    }

    public int getTickMarkInRangeTextColor() {
        return tickMarkInRangeTextColor;
    }

    public void setTickMarkInRangeTextColor(@ColorInt int tickMarkInRangeTextColor) {
        this.tickMarkInRangeTextColor = tickMarkInRangeTextColor;
    }

    public int getSeekBarMode() {
        return seekBarMode;
    }

    /**
     * {@link #SEEKBAR_MODE_SINGLE} is single SeekBar
     * {@link #SEEKBAR_MODE_RANGE} is range SeekBar
     *
     * @param seekBarMode
     */
    public void setSeekBarMode(@SeekBarModeDef int seekBarMode) {
        this.seekBarMode = seekBarMode;
        rightSB.setVisible(seekBarMode != SEEKBAR_MODE_SINGLE);
    }

    public int getTickMarkMode() {
        return tickMarkMode;
    }

    /**
     * {@link #TICK_MARK_GRAVITY_LEFT} is number tick mark, it will locate the position according to the value.
     * {@link #TICK_MARK_GRAVITY_RIGHT} is text tick mark, it will be equally positioned.
     *
     * @param tickMarkMode
     */
    public void setTickMarkMode(@TickMarkModeDef int tickMarkMode) {
        this.tickMarkMode = tickMarkMode;
    }

    public int getTickMarkTextMargin() {
        return tickMarkTextMargin;
    }

    public void setTickMarkTextMargin(int tickMarkTextMargin) {
        this.tickMarkTextMargin = tickMarkTextMargin;
    }

    public int getTickMarkTextSize() {
        return tickMarkTextSize;
    }

    public void setTickMarkTextSize(int tickMarkTextSize) {
        this.tickMarkTextSize = tickMarkTextSize;
    }

    public int getTickMarkGravity() {
        return tickMarkGravity;
    }

    /**
     * the tick mark text gravity
     * {@link #TICK_MARK_GRAVITY_LEFT}
     * {@link #TICK_MARK_GRAVITY_RIGHT}
     * {@link #TICK_MARK_GRAVITY_CENTER}
     *
     * @param tickMarkGravity
     */
    public void setTickMarkGravity(@TickMarkGravityDef int tickMarkGravity) {
        this.tickMarkGravity = tickMarkGravity;
    }

    public CharSequence[] getTickMarkTextArray() {
        return tickMarkTextArray;
    }

    public void setTickMarkTextArray(CharSequence[] tickMarkTextArray) {
        this.tickMarkTextArray = tickMarkTextArray;
    }

    public float getMinInterval() {
        return minInterval;
    }

    public float getProgressRadius() {
        return progressRadius;
    }

    public void setProgressRadius(float progressRadius) {
        this.progressRadius = progressRadius;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(@ColorInt int progressColor) {
        this.progressColor = progressColor;
    }

    public int getProgressDefaultColor() {
        return progressDefaultColor;
    }

    public void setProgressDefaultColor(@ColorInt int progressDefaultColor) {
        this.progressDefaultColor = progressDefaultColor;
    }

    public int getProgressDrawableId() {
        return progressDrawableId;
    }

    public void setProgressDrawableId(@DrawableRes int progressDrawableId) {
        this.progressDrawableId = progressDrawableId;
        progressBitmap = null;
        /**
         * Initializes the progressbitmap component for thermal imaging operations.
         *
         */
        initProgressBitmap();
    }

    public int getProgressDefaultDrawableId() {
        return progressDefaultDrawableId;
    }

    public void setProgressDefaultDrawableId(@DrawableRes int progressDefaultDrawableId) {
        this.progressDefaultDrawableId = progressDefaultDrawableId;
        progressDefaultBitmap = null;
        /**
         * Initializes the progressbitmap component for thermal imaging operations.
         *
         */
        initProgressBitmap();
    }

    public int getProgressWidth() {
        return progressWidth;
    }

    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    public void setTypeface(Typeface typeFace) {
        paint.setTypeface(typeFace);
    }

    public boolean isEnableThumbOverlap() {
        return enableThumbOverlap;
    }

    public void setEnableThumbOverlap(boolean enableThumbOverlap) {
        this.enableThumbOverlap = enableThumbOverlap;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }

    public int getStepsColor() {
        return stepsColor;
    }

    public void setStepsColor(@ColorInt int stepsColor) {
        this.stepsColor = stepsColor;
    }

    public float getStepsWidth() {
        return stepsWidth;
    }

    public void setStepsWidth(float stepsWidth) {
        this.stepsWidth = stepsWidth;
    }

    public float getStepsHeight() {
        return stepsHeight;
    }

    public void setStepsHeight(float stepsHeight) {
        this.stepsHeight = stepsHeight;
    }

    public float getStepsRadius() {
        return stepsRadius;
    }

    public void setStepsRadius(float stepsRadius) {
        this.stepsRadius = stepsRadius;
    }

    public void setProgressTop(int progressTop) {
        this.progressTop = progressTop;
    }

    public void setProgressBottom(int progressBottom) {
        this.progressBottom = progressBottom;
    }

    public void setProgressLeft(int progressLeft) {
        this.progressLeft = progressLeft;
    }

    public void setProgressRight(int progressRight) {
        this.progressRight = progressRight;
    }

    public int getTickMarkLayoutGravity() {
        return tickMarkLayoutGravity;
    }

    /**
     * the tick mark layout gravity
     * Gravity.TOP and Gravity.BOTTOM
     *
     * @param tickMarkLayoutGravity
     */
    public void setTickMarkLayoutGravity(@TickMarkLayoutGravityDef int tickMarkLayoutGravity) {
        this.tickMarkLayoutGravity = tickMarkLayoutGravity;
    }

    public int getGravity() {
        return gravity;
    }

    /**
     * the RangeSeekBar gravity
     * Gravity.TOP and Gravity.BOTTOM
     *
     * @param gravity
     */
    public void setGravity(@GravityDef int gravity) {
        this.gravity = gravity;
    }

    public boolean isStepsAutoBonding() {
        return stepsAutoBonding;
    }

    public void setStepsAutoBonding(boolean stepsAutoBonding) {
        this.stepsAutoBonding = stepsAutoBonding;
    }

    public int getStepsDrawableId() {
        return stepsDrawableId;
    }

    public void setStepsDrawableId(@DrawableRes int stepsDrawableId) {
        this.stepsBitmaps.clear();
        this.stepsDrawableId = stepsDrawableId;
        /**
         * Initializes the stepsbitmap component for thermal imaging operations.
         *
         */
        initStepsBitmap();
    }

    public List<Bitmap> getStepsBitmaps() {
        return stepsBitmaps;
    }

    public void setStepsBitmaps(List<Bitmap> stepsBitmaps) {
// If (stepsBitmaps == null || stepsBitmaps.isEmpty() || stepsBitmaps.size() <= steps) {
// Throw new IllegalArgumentException("stepsBitmaps must > steps !");
//        }
        this.stepsBitmaps.clear();
        this.stepsBitmaps.addAll(stepsBitmaps);
    }

    public void setStepsDrawable(List<Integer> stepsDrawableIds) {
// If (stepsDrawableIds == null || stepsDrawableIds.isEmpty() || stepsDrawableIds.size() <= steps) {
// Throw new IllegalArgumentException("stepsDrawableIds must > steps !");
//        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!verifyStepsMode()) {
            throw new IllegalArgumentException("stepsWidth must > 0, stepsHeight must > 0,steps must > 0 First!!");
        }
        List<Bitmap> stepsBitmaps = new ArrayList<>();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < stepsDrawableIds.size(); i++) {
            stepsBitmaps.add(Utils.drawableToBitmap(getContext(), (int) stepsWidth, (int) stepsHeight, stepsDrawableIds.get(i)));
        }
        /**
         * Configures the stepsbitmaps with validation and thermal imaging optimization.
         *
         */
        setStepsBitmaps(stepsBitmaps);
    }

    public void setPseudocode(int pseudocode) {
        this.pseudocode = pseudocode;
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate();
    }

    public void setColorList(@Nullable int[] colorList) {
        this.colorList = colorList;
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate();
    }

    public void setPlaces(@Nullable float[] newPlaces) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (newPlaces == null) {
            places = null;
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (places == null || places.length != newPlaces.length) {
                places = new float[newPlaces.length];
            }
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < newPlaces.length; i++) {
                places[places.length - 1 - i] = 1 - newPlaces[i];
            }
        }
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate();
    }
}
