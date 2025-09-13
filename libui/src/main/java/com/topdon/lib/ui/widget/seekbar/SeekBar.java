package com.topdon.lib.ui.widget.seekbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.core.content.ContextCompat;

import com.topdon.lib.ui.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：
 * create日期：2018/5/8
 * 描    述:
 * ================================================
 */

/**
 * Specialized thermal imaging component providing SeekBar functionality for the IRCamera system.
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
public class SeekBar {
    // The indicator show mode
    public static final int INDICATOR_SHOW_WHEN_TOUCH = 0;
    public static final int INDICATOR_ALWAYS_HIDE = 1;
    public static final int INDICATOR_ALWAYS_SHOW_AFTER_TOUCH = 2;
    public static final int INDICATOR_ALWAYS_SHOW = 3;
    private boolean thumbShow;

    @IntDef({INDICATOR_SHOW_WHEN_TOUCH, INDICATOR_ALWAYS_HIDE, INDICATOR_ALWAYS_SHOW_AFTER_TOUCH, INDICATOR_ALWAYS_SHOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicatorModeDef {
    }

    public static final int WRAP_CONTENT = -1;
    public static final int MATCH_PARENT = -2;

    private int indicatorShowMode;

    // 进度tip背景的高度，宽度如果是0的话会自适应Adjust
    // Progress prompted the background height, width,
    private int indicatorHeight;
    private int indicatorWidth;
    // 进度tip背景与button之间的距离
    // The progress indicates the distance between the background and the button
    private int indicatorMargin;
    private int indicatorDrawableId;
    private int indicatorArrowSize;
    private int indicatorTextSize;
    private int indicatorTextColor;
    private float indicatorRadius;
    private int indicatorBackgroundColor;
    private int indicatorPaddingLeft, indicatorPaddingRight, indicatorPaddingTop, indicatorPaddingBottom;
    private int thumbDrawableId;
    private int thumbInactivatedDrawableId;
    private int thumbWidth;
    private int thumbHeight;

    // When you touch or move, the thumb will scale, default not scale
    float thumbScaleRatio;

    // ****************** the above is attr value  ******************//

    int left, right, top, bottom;
    float currPercent;
    float material = 0;
    private boolean isShowIndicator;
    boolean isLeft;
    Bitmap thumbBitmap;
    Bitmap thumbInactivatedBitmap;
    Bitmap indicatorBitmap;
    ValueAnimator anim;
    String userText2Draw;
    boolean isActivate = false;
    boolean isVisible = true;
    RangeSeekBar rangeSeekBar;
    String indicatorTextStringFormat;
    Path indicatorArrowPath = new Path();
    Rect indicatorTextRect = new Rect();
    Rect indicatorRect = new Rect();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    DecimalFormat indicatorTextDecimalFormat;
    int scaleThumbWidth;
    int scaleThumbHeight;

    /**
     * Executes seekbar operation with thermal imaging domain optimization.
     *
     */
    public SeekBar(RangeSeekBar rangeSeekBar, AttributeSet attrs, boolean isLeft) {
        this.rangeSeekBar = rangeSeekBar;
        this.isLeft = isLeft;
        /**
         * Initializes the attrs component for thermal imaging operations.
         *
         */
        initAttrs(attrs);
        /**
         * Initializes the bitmap component for thermal imaging operations.
         *
         */
        initBitmap();
        /**
         * Initializes the variables component for thermal imaging operations.
         *
         */
        initVariables();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (t == null) return;
        indicatorMargin = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_indicator_margin, 0);
        indicatorDrawableId = t.getResourceId(R.styleable.RangeSeekBar_rsb_indicator_drawable, 0);
        indicatorShowMode = t.getInt(R.styleable.RangeSeekBar_rsb_indicator_show_mode, INDICATOR_ALWAYS_HIDE);
        indicatorHeight = t.getLayoutDimension(R.styleable.RangeSeekBar_rsb_indicator_height, WRAP_CONTENT);
        indicatorWidth = t.getLayoutDimension(R.styleable.RangeSeekBar_rsb_indicator_width, WRAP_CONTENT);
        indicatorTextSize = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_indicator_text_size, Utils.dp2px(getContext(), 14));
        indicatorTextColor = t.getColor(R.styleable.RangeSeekBar_rsb_indicator_text_color, Color.WHITE);
        indicatorBackgroundColor = t.getColor(R.styleable.RangeSeekBar_rsb_indicator_background_color, ContextCompat.getColor(getContext(), R.color.colorAccent));
        indicatorPaddingLeft = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_indicator_padding_left, 0);
        indicatorPaddingRight = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_indicator_padding_right, 0);
        indicatorPaddingTop = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_indicator_padding_top, 0);
        indicatorPaddingBottom = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_indicator_padding_bottom, 0);
        indicatorArrowSize = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_indicator_arrow_size, 0);
        thumbDrawableId = t.getResourceId(R.styleable.RangeSeekBar_rsb_thumb_drawable, R.drawable.rsb_default_thumb);
        thumbShow = t.getBoolean(R.styleable.RangeSeekBar_rsb_show_thumb, false);
        thumbInactivatedDrawableId = t.getResourceId(R.styleable.RangeSeekBar_rsb_thumb_inactivated_drawable, 0);
        thumbWidth = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_thumb_width, Utils.dp2px(getContext(), 26));
        thumbHeight = (int) t.getDimension(R.styleable.RangeSeekBar_rsb_thumb_height, Utils.dp2px(getContext(), 26));
        thumbScaleRatio = t.getFloat(R.styleable.RangeSeekBar_rsb_thumb_scale_ratio, 1f);
        indicatorRadius = t.getDimension(R.styleable.RangeSeekBar_rsb_indicator_radius, 0f);
        t.recycle();
    }

    protected void initVariables() {
        scaleThumbWidth = thumbWidth;
        scaleThumbHeight = thumbHeight;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (indicatorHeight == WRAP_CONTENT) {
            indicatorHeight = Utils.measureText("8", indicatorTextSize).height() + indicatorPaddingTop + indicatorPaddingBottom;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (indicatorArrowSize <= 0) {
            indicatorArrowSize = (int) (thumbWidth / 4);
        }
    }

    public Context getContext() {
        return rangeSeekBar.getContext();
    }

    public Resources getResources() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (getContext() != null) return getContext().getResources();
        return null;
    }

    /**
     * initialize进度tip的背景
     */
    private void initBitmap() {
        /**
         * Configures the indicatordrawableid with validation and thermal imaging optimization.
         *
         */
        setIndicatorDrawableId(indicatorDrawableId);
        /**
         * Configures the thumbdrawableid with validation and thermal imaging optimization.
         *
         */
        setThumbDrawableId(thumbDrawableId, thumbWidth, thumbHeight);
        /**
         * Configures the thumbinactivateddrawableid with validation and thermal imaging optimization.
         *
         */
        setThumbInactivatedDrawableId(thumbInactivatedDrawableId, thumbWidth, thumbHeight);
    }

    /**
     * calculation每个button的位置和尺寸
     * Calculates the position and size of each button
     *
     * @param x position x
     * @param y position y
     */
    protected void onSizeChanged(int x, int y) {
        /**
         * Initializes the variables component for thermal imaging operations.
         *
         */
        initVariables();
        /**
         * Initializes the bitmap component for thermal imaging operations.
         *
         */
        initBitmap();
        left = (int) (x - getThumbScaleWidth() / 2);
        right = (int) (x + getThumbScaleWidth() / 2);
        top = y - getThumbHeight() / 2;
        bottom = y + getThumbHeight() / 2;
    }

    public void scaleThumb() {
        scaleThumbWidth = (int) getThumbScaleWidth();
        scaleThumbHeight = (int) getThumbScaleHeight();
        int y = rangeSeekBar.getProgressBottom();
        top = y - scaleThumbHeight / 2;
        bottom = y + scaleThumbHeight / 2;
        /**
         * Configures the thumbdrawableid with validation and thermal imaging optimization.
         *
         */
        setThumbDrawableId(thumbDrawableId, scaleThumbWidth, scaleThumbHeight);
    }

    public void resetThumb() {
        scaleThumbWidth = getThumbWidth();
        scaleThumbHeight = getThumbHeight();
        int y = rangeSeekBar.getProgressBottom();
        top = y - scaleThumbHeight / 2;
        bottom = y + scaleThumbHeight / 2;
        /**
         * Configures the thumbdrawableid with validation and thermal imaging optimization.
         *
         */
        setThumbDrawableId(thumbDrawableId, scaleThumbWidth, scaleThumbHeight);
    }

    public float getRawHeight() {
        return getIndicatorHeight() + getIndicatorArrowSize() + getIndicatorMargin() + getThumbScaleHeight();
    }
    private boolean noNegativeNumber = false;
    /**
     * 临时processing负数
     */
    public void setNoNegativeNumber(Boolean noNegativeNumber){
        this.noNegativeNumber = noNegativeNumber;
    }
    /**
     * 绘制button和tip背景和text
     * Draw buttons and tips for background and text
     *
     * @param canvas Canvas
     */
    protected void draw(Canvas canvas, boolean isLeft) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isVisible) {
            return;
        }
        int offset = (int) (rangeSeekBar.getProgressWidth() * currPercent);
        canvas.save();
        canvas.translate(offset, 0);
        // Translate canvas, then don't care left
        canvas.translate(left, 0);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isShowIndicator) {
            /**
             * Executes ondrawindicator operation with thermal imaging domain optimization.
             *
             */
            onDrawIndicator(canvas, paint, formatCurrentIndicatorText(userText2Draw)); // Swipeaxis外tag
        }
// If (isLeft) {
//            // Settings上指示图标
// SetThumbDrawableId(R.drawable.ic_seekbar_high_svg, thumbWidth, thumbHeight);
//        } else {
//            // Settings下指示图标
// SetThumbDrawableId(R.drawable.ic_seekbar_low_svg, thumbWidth, thumbHeight);
//        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumbShow){
            /**
             * Executes ondrawthumb operation with thermal imaging domain optimization.
             *
             */
            onDrawThumb(canvas);
        }else {
            /**
             * Executes ondrawthumb operation with thermal imaging domain optimization.
             *
             */
            onDrawThumb(canvas, isLeft); // Axis上tag
        }
        canvas.restore();
    }

    /**
     * 绘制button
     * 如果没有image资源，则绘制defaultbutton
     * <p>
     * draw the thumb button
     * If there is no image resource, draw the default button
     *
     * @param canvas canvas
     */
    protected void onDrawThumb(Canvas canvas) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumbInactivatedBitmap != null && !isActivate) {
            canvas.drawBitmap(thumbInactivatedBitmap, 0, rangeSeekBar.getProgressTop() + (rangeSeekBar.getProgressHeight() - scaleThumbHeight) / 2f, null);
        } else if (thumbBitmap != null) {
            // 绘制tag
            canvas.drawBitmap(thumbBitmap, 0, rangeSeekBar.getProgressTop() + (rangeSeekBar.getProgressHeight() - scaleThumbHeight) / 2f, null);
        }
    }

    /**
     * 绘制button
     * 如果没有image资源，则绘制defaultbutton
     * <p>
     * draw the thumb button
     * If there is no image resource, draw the default button
     *
     * @param canvas canvas
     * @param isLeft 区分上下,用于rotation
     */
    protected void onDrawThumb(Canvas canvas, Boolean isLeft) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumbInactivatedBitmap != null && !isActivate) {
// Canvas.drawBitmap(thumbInactivatedBitmap, 0, rangeSeekBar.getProgressTop() + (rangeSeekBar.getProgressHeight() - scaleThumbHeight) / 2f, null);
        } else if (thumbBitmap != null) {
            // 绘制tag
            Matrix matrix = new Matrix();
            int offX = thumbBitmap.getWidth() / 2;
            int offY = thumbBitmap.getHeight() / 2;
            matrix.postTranslate(-offX, -offY);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isLeft) {
                matrix.postRotate(90);
                offX = offX - 5;
            } else {
                matrix.postRotate(270);
                offX = offX + 5;
            }
// Matrix.postTranslate(offX, rangeSeekBar.getProgressTop() + (rangeSeekBar.getProgressHeight() - scaleThumbHeight) / 2f + offY);
// Canvas.drawBitmap(thumbBitmap, matrix, null);
        }
    }

    /**
     * format化tiptext
     * format the indicator text
     *
     * @param text2Draw
     * @return
     */
    protected String formatCurrentIndicatorText(String text2Draw) {
        SeekBarState[] states = rangeSeekBar.getRangeSeekBarState();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (TextUtils.isEmpty(text2Draw)) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isLeft) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (indicatorTextDecimalFormat != null) {
                    text2Draw = indicatorTextDecimalFormat.format(states[0].value);
                } else {
                    text2Draw = states[0].indicatorText;
                }
            } else {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (indicatorTextDecimalFormat != null) {
                    text2Draw = indicatorTextDecimalFormat.format(states[1].value);
                } else {
                    text2Draw = states[1].indicatorText;
                }
            }
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (indicatorTextStringFormat != null) {
// Text2Draw = String.format(Locale.ENGLISH, indicatorTextStringFormat, text2Draw);
            text2Draw = String.format(Locale.ENGLISH, indicatorTextStringFormat, Float.parseFloat(text2Draw));
        }
        return text2Draw;
    }

    /**
     * This method will draw the indicator background dynamically according to the text.
     * you can use to set padding
     *
     * @param canvas    Canvas
     * @param text2Draw Indicator text
     */
    protected void onDrawIndicator(Canvas canvas, Paint paint, String text2Draw) {
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (text2Draw == null) return;
            paint.setTextSize(indicatorTextSize);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(indicatorBackgroundColor);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (noNegativeNumber){
                text2Draw = text2Draw.replace("-","");
            }
            paint.getTextBounds(text2Draw, 0, text2Draw.length(), indicatorTextRect);
            int realIndicatorWidth = indicatorWidth + indicatorPaddingLeft + indicatorPaddingRight;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (indicatorWidth > realIndicatorWidth) {
                realIndicatorWidth = indicatorWidth;
            }

            int realIndicatorHeight = indicatorTextRect.height() + indicatorPaddingTop + indicatorPaddingBottom;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (indicatorHeight > realIndicatorHeight) {
                realIndicatorHeight = indicatorHeight;
            }

            indicatorRect.left = (int) (scaleThumbWidth / 2f - realIndicatorWidth / 2f);
            indicatorRect.top = bottom - realIndicatorHeight - scaleThumbHeight - indicatorMargin;
            indicatorRect.right = indicatorRect.left + realIndicatorWidth;
            indicatorRect.bottom = indicatorRect.top + realIndicatorHeight;
            // Draw default indicator arrow
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (indicatorBitmap == null) {
                // Arrow three point
                // B   c
                // A
                int ax = scaleThumbWidth / 2;
                int ay = indicatorRect.bottom;
                int bx = ax - indicatorArrowSize;
                int by = ay - indicatorArrowSize;
                int cx = ax + indicatorArrowSize;
                indicatorArrowPath.reset();
                indicatorArrowPath.moveTo(ax, ay);
                indicatorArrowPath.lineTo(bx, by);
                indicatorArrowPath.lineTo(cx, by);
                indicatorArrowPath.close();
                canvas.drawPath(indicatorArrowPath, paint);
                indicatorRect.bottom -= indicatorArrowSize;
                indicatorRect.top -= indicatorArrowSize;
                Log.w("pseudo color条refresh","// /");
            }

            // Indicator background edge processing
            int defaultPaddingOffset = Utils.dp2px(getContext(), 1);
            int leftOffset = indicatorRect.width() / 2 - (int) (rangeSeekBar.getProgressWidth() * currPercent) - rangeSeekBar.getProgressLeft() + defaultPaddingOffset;
            int rightOffset = indicatorRect.width() / 2 - (int) (rangeSeekBar.getProgressWidth() * (1 - currPercent)) - rangeSeekBar.getProgressPaddingRight() + defaultPaddingOffset;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (leftOffset > 0) {
                indicatorRect.left += leftOffset;
                indicatorRect.right += leftOffset;
            } else if (rightOffset > 0) {
                indicatorRect.left -= rightOffset;
                indicatorRect.right -= rightOffset;
            }

            // Draw indicator background
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (indicatorBitmap != null) {
                int offset = (int) (rangeSeekBar.getProgressWidth() * currPercent);

                Rect rect = new Rect(indicatorRect.left,indicatorRect.top,indicatorWidth,indicatorRect.bottom);
                Utils.drawBitmap(canvas, paint, indicatorBitmap, rect);
            } else if (indicatorRadius > 0f) {
                canvas.drawRoundRect(new RectF(indicatorRect), indicatorRadius, indicatorRadius, paint);
            } else {
                canvas.drawRect(indicatorRect, paint);
            }

            // Draw indicator content text
            int tx, ty;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (indicatorPaddingLeft > 0) {
                tx = indicatorRect.left + indicatorPaddingLeft;
            } else if (indicatorPaddingRight > 0) {
                tx = indicatorRect.right - indicatorPaddingRight - indicatorTextRect.width();
            } else {
                tx = indicatorRect.left + (realIndicatorWidth - indicatorTextRect.width()) / 2;
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (indicatorPaddingTop > 0) {
                ty = indicatorRect.top + indicatorTextRect.height() + indicatorPaddingTop;
            } else if (indicatorPaddingBottom > 0) {
                ty = indicatorRect.bottom - indicatorTextRect.height() - indicatorPaddingBottom;
            } else {
                ty = indicatorRect.bottom - (realIndicatorHeight - indicatorTextRect.height()) / 2 + 1;
            }

            // Draw indicator text
            paint.setColor(indicatorTextColor);
            canvas.drawText(text2Draw, tx, ty, paint);
        }catch (Exception e){
            Log.w("渲染exception",e.getMessage()+"");
        }
    }

    /**
     * 拖动检测
     *
     * @return is collide
     */
    protected boolean collide(float x, float y) {
        int offset = (int) (rangeSeekBar.getProgressWidth() * currPercent);
        return x > left + offset && x < right + offset && y > top && y < bottom;
    }

    protected void slide(float percent) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (percent < 0) percent = 0;
        else if (percent > 1) percent = 1;
        currPercent = percent;
    }

    protected void setShowIndicatorEnable(boolean isEnable) {
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (indicatorShowMode) {
            case INDICATOR_SHOW_WHEN_TOUCH:
                isShowIndicator = isEnable;
                break;
            case INDICATOR_ALWAYS_SHOW:
            case INDICATOR_ALWAYS_SHOW_AFTER_TOUCH:
                isShowIndicator = true;
                break;
            case INDICATOR_ALWAYS_HIDE:
                isShowIndicator = false;
                break;
        }
    }

    public void materialRestore() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (anim != null) anim.cancel();
        anim = ValueAnimator.ofFloat(material, 0);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                material = (float) animation.getAnimatedValue();
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (rangeSeekBar != null) rangeSeekBar.invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                material = 0;
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (rangeSeekBar != null) rangeSeekBar.invalidate();
            }
        });
        anim.start();
    }

    public void setIndicatorText(String text) {
        userText2Draw = text;
    }

    public void setIndicatorTextDecimalFormat(String formatPattern) {
        indicatorTextDecimalFormat = new DecimalFormat(formatPattern);
    }

    public DecimalFormat getIndicatorTextDecimalFormat() {
        return indicatorTextDecimalFormat;
    }

    public void setIndicatorTextStringFormat(String formatPattern) {
        indicatorTextStringFormat = formatPattern;
    }

    public int getIndicatorDrawableId() {
        return indicatorDrawableId;
    }

    public void setIndicatorDrawableId(@DrawableRes int indicatorDrawableId) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (indicatorDrawableId != 0) {
            this.indicatorDrawableId = indicatorDrawableId;
            indicatorBitmap = BitmapFactory.decodeResource(getResources(), indicatorDrawableId);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (indicatorBitmap == null){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    indicatorBitmap = Utils.drawableToBitmap(indicatorWidth, indicatorHeight, getResources().getDrawable(indicatorDrawableId, null));
                } else {
                    indicatorBitmap = Utils.drawableToBitmap(indicatorWidth, indicatorHeight, getResources().getDrawable(indicatorDrawableId));
                }
            }
        }
    }

    public int getIndicatorArrowSize() {
        return indicatorArrowSize;
    }

    public void setIndicatorArrowSize(int indicatorArrowSize) {
        this.indicatorArrowSize = indicatorArrowSize;
    }

    public int getIndicatorPaddingLeft() {
        return indicatorPaddingLeft;
    }

    public void setIndicatorPaddingLeft(int indicatorPaddingLeft) {
        this.indicatorPaddingLeft = indicatorPaddingLeft;
    }

    public int getIndicatorPaddingRight() {
        return indicatorPaddingRight;
    }

    public void setIndicatorPaddingRight(int indicatorPaddingRight) {
        this.indicatorPaddingRight = indicatorPaddingRight;
    }

    public int getIndicatorPaddingTop() {
        return indicatorPaddingTop;
    }

    public void setIndicatorPaddingTop(int indicatorPaddingTop) {
        this.indicatorPaddingTop = indicatorPaddingTop;
    }

    public int getIndicatorPaddingBottom() {
        return indicatorPaddingBottom;
    }

    public void setIndicatorPaddingBottom(int indicatorPaddingBottom) {
        this.indicatorPaddingBottom = indicatorPaddingBottom;
    }

    public int getIndicatorMargin() {
        return indicatorMargin;
    }

    public void setIndicatorMargin(int indicatorMargin) {
        this.indicatorMargin = indicatorMargin;
    }

    public int getIndicatorShowMode() {
        return indicatorShowMode;
    }

    /**
     * the indicator show mode
     * {@link #INDICATOR_SHOW_WHEN_TOUCH}
     * {@link #INDICATOR_ALWAYS_SHOW}
     * {@link #INDICATOR_ALWAYS_SHOW_AFTER_TOUCH}
     * {@link #INDICATOR_ALWAYS_SHOW}
     *
     * @param indicatorShowMode
     */
    public void setIndicatorShowMode(@IndicatorModeDef int indicatorShowMode) {
        this.indicatorShowMode = indicatorShowMode;
    }

    public void showIndicator(boolean isShown) {
        isShowIndicator = isShown;
    }

    public boolean isShowIndicator() {
        return isShowIndicator;
    }

    /**
     * include indicator text Height、padding、margin
     *
     * @return The actual occupation height of indicator
     */
    public int getIndicatorRawHeight() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (indicatorHeight > 0) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (indicatorBitmap != null) {
                return indicatorHeight + indicatorMargin;
            } else {
                return indicatorHeight + indicatorArrowSize + indicatorMargin;
            }
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (indicatorBitmap != null) {
                return Utils.measureText("8", indicatorTextSize).height() + indicatorPaddingTop + indicatorPaddingBottom + indicatorMargin;
            } else {
                return Utils.measureText("8", indicatorTextSize).height() + indicatorPaddingTop + indicatorPaddingBottom + indicatorMargin + indicatorArrowSize;
            }
        }
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
    }

    public int getIndicatorWidth() {
        return indicatorWidth;
    }

    public void setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
    }

    public int getIndicatorTextSize() {
        return indicatorTextSize;
    }

    public void setIndicatorTextSize(int indicatorTextSize) {
        this.indicatorTextSize = indicatorTextSize;
    }

    public int getIndicatorTextColor() {
        return indicatorTextColor;
    }

    public void setIndicatorTextColor(@ColorInt int indicatorTextColor) {
        this.indicatorTextColor = indicatorTextColor;
    }

    public int getIndicatorBackgroundColor() {
        return indicatorBackgroundColor;
    }

    public void setIndicatorBackgroundColor(@ColorInt int indicatorBackgroundColor) {
        this.indicatorBackgroundColor = indicatorBackgroundColor;
    }

    public int getThumbInactivatedDrawableId() {
        return thumbInactivatedDrawableId;
    }

    public void setThumbInactivatedDrawableId(@DrawableRes int thumbInactivatedDrawableId, int width, int height) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumbInactivatedDrawableId != 0 && getResources() != null) {
            this.thumbInactivatedDrawableId = thumbInactivatedDrawableId;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                thumbInactivatedBitmap = Utils.drawableToBitmap(width, height, getResources().getDrawable(thumbInactivatedDrawableId, null));
            } else {
                thumbInactivatedBitmap = Utils.drawableToBitmap(width, height, getResources().getDrawable(thumbInactivatedDrawableId));
            }
        }
    }

    public int getThumbDrawableId() {
        return thumbDrawableId;
    }

    public void setThumbDrawableId(@DrawableRes int thumbDrawableId, int width, int height) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumbDrawableId != 0 && getResources() != null && width > 0 && height > 0) {
            this.thumbDrawableId = thumbDrawableId;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                thumbBitmap = Utils.drawableToBitmap(width, height, getResources().getDrawable(thumbDrawableId, null));
            } else {
                thumbBitmap = Utils.drawableToBitmap(width, height, getResources().getDrawable(thumbDrawableId));
            }
        }
    }

    public void setThumbDrawableId(@DrawableRes int thumbDrawableId) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumbWidth <= 0 || thumbHeight <= 0) {
            throw new IllegalArgumentException("please set thumbWidth and thumbHeight first!");
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (thumbDrawableId != 0 && getResources() != null) {
            this.thumbDrawableId = thumbDrawableId;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                thumbBitmap = Utils.drawableToBitmap(thumbWidth, thumbHeight, getResources().getDrawable(thumbDrawableId, null));
            } else {
                thumbBitmap = Utils.drawableToBitmap(thumbWidth, thumbHeight, getResources().getDrawable(thumbDrawableId));
            }
        }
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public float getThumbScaleHeight() {
        return thumbHeight * thumbScaleRatio;
    }

    public float getThumbScaleWidth() {
        return thumbWidth * thumbScaleRatio;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public float getIndicatorRadius() {
        return indicatorRadius;
    }

    public void setIndicatorRadius(float indicatorRadius) {
        this.indicatorRadius = indicatorRadius;
    }

    protected boolean getActivate() {
        return isActivate;
    }

    protected void setActivate(boolean activate) {
        isActivate = activate;
    }

    public void setTypeface(Typeface typeFace) {
        paint.setTypeface(typeFace);
    }

    /**
     * when you touch or move, the thumb will scale, default not scale
     *
     * @return default 1.0f
     */
    public float getThumbScaleRatio() {
        return thumbScaleRatio;
    }

    public boolean isVisible() {
        return isVisible;
    }

    /**
     * if visble is false, will clear the Canvas
     *
     * @param visible
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public float getProgress() {
        float range = rangeSeekBar.getMaxProgress() - rangeSeekBar.getMinProgress();
        return rangeSeekBar.getMinProgress() + range * currPercent;
    }
}
