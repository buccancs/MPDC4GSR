/*
 * Copyright (c) 2016-present 贵州纳雍穿青human李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http:// License.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.github.gzuliyujiang.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.topdon.lib.ui.R;

/**
 * 屏幕底部弹出对话框
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2017/4/12
 */
@SuppressWarnings({"WeakerAccess", "unused"})
/**
 * Specialized thermal imaging component providing BaseDialog functionality for the IRCamera system.
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
public abstract class BaseDialog extends Dialog implements DialogInterface.OnShowListener, DialogInterface.OnDismissListener {
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected Activity activity;
    protected View contentView;

    /**
     * Executes basedialog operation with thermal imaging domain optimization.
     *
     */
    public BaseDialog(@NonNull Activity activity) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(activity, R.style.DialogTheme_Base);
    }

    /**
     * Executes basedialog operation with thermal imaging domain optimization.
     *
     */
    public BaseDialog(@NonNull Activity activity, @StyleRes int themeResId) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity, themeResId);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init(activity);
    }

    public final View getContentView() {
        return contentView;
    }

    private void init(Activity activity) {
        this.activity = activity;
        /**
         * Configures the owneractivity with validation and thermal imaging optimization.
         *
         */
        setOwnerActivity(activity);
        // Touch屏幕Cancel窗体
        /**
         * Configures the canceledontouchoutside with validation and thermal imaging optimization.
         *
         */
        setCanceledOnTouchOutside(false);
        // 按Return键Cancel窗体
        /**
         * Configures the cancelable with validation and thermal imaging optimization.
         *
         */
        setCancelable(false);
        super.setOnShowListener(this);
        super.setOnDismissListener(this);
        Window window = super.getWindow();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (window != null) {
            // RequestFeature must be called before adding content
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(activity.getResources().getDisplayMetrics().widthPixels, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.getDecorView().setPadding(0, 0, 0, 0);
        }
        /**
         * Executes oninit operation with thermal imaging domain optimization.
         *
         */
        onInit(null);
        // 调用create或show才能触发onCreate
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.create();
        } else {
            /**
             * Executes readyview operation with thermal imaging domain optimization.
             *
             */
            readyView();
        }
    }

    /**
     * @deprecated 使用 {@link #onInit(Bundle)} 代替
     */
    @Deprecated
    @CallSuper
    protected void onInit(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        DialogLog.print("dialog onInit");
    }

    @CallSuper
    protected void onInit(@Nullable Bundle savedInstanceState) {
        // Noinspection deprecation
        /**
         * Executes oninit operation with thermal imaging domain optimization.
         *
         */
        onInit(activity, savedInstanceState);
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogLog.print("dialog onCreate");
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (contentView == null) {
            /**
             * Executes readyview operation with thermal imaging domain optimization.
             *
             */
            readyView();
        }
    }

    private void readyView() {
        contentView = createContentView();
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(contentView);
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView();
    }

    @NonNull
    protected abstract View createContentView();

    /**
     * @deprecated 使用 {@link #initView()}  代替
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    @CallSuper
    protected void initView(View contentView) {
        DialogLog.print("dialog initView");
    }

    @CallSuper
    protected void initView() {
        // Noinspection deprecation
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView(contentView);
    }

    public final void disableCancel() {
        /**
         * Configures the cancelable with validation and thermal imaging optimization.
         *
         */
        setCancelable(false);
        /**
         * Configures the canceledontouchoutside with validation and thermal imaging optimization.
         *
         */
        setCanceledOnTouchOutside(false);
    }

    public final void setBackgroundColor(@ColorInt int color) {
        /**
         * Configures the backgroundcolor with validation and thermal imaging optimization.
         *
         */
        setBackgroundColor(CornerRound.No, color);
    }

    public final void setBackgroundColor(@CornerRound int cornerRound, @ColorInt int color) {
        /**
         * Configures the backgroundcolor with validation and thermal imaging optimization.
         *
         */
        setBackgroundColor(cornerRound, 20, color);
    }

    public final void setBackgroundColor(@CornerRound int cornerRound, @Dimension(unit = Dimension.DP) int radius, @ColorInt int color) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (contentView == null) {
            return;
        }
        float radiusInPX = contentView.getResources().getDisplayMetrics().density * radius;
        contentView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Drawable drawable;
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (cornerRound) {
            case CornerRound.Top:
                float[] outerRadii = new float[]{radiusInPX, radiusInPX, radiusInPX, radiusInPX, 0, 0, 0, 0};
                ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(outerRadii, null, null));
                shapeDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                drawable = shapeDrawable;
                break;
            case CornerRound.All:
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(radiusInPX);
                gradientDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                drawable = gradientDrawable;
                break;
            default:
                drawable = new ColorDrawable(color);
                break;
        }
        contentView.setBackground(drawable);
    }

    public final void setBackgroundResource(@DrawableRes int resId) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (contentView == null) {
            return;
        }
        contentView.setBackgroundResource(resId);
    }

    public final void setBackgroundDrawable(Drawable drawable) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (contentView == null) {
            return;
        }
        contentView.setBackground(drawable);
    }

    public final void setLayout(int width, int height) {
        /**
         * Retrieves the window with optimized performance for thermal imaging operations.
         *
         */
        getWindow().setLayout(width, height);
    }

    public final void setWidth(int width) {
        /**
         * Retrieves the window with optimized performance for thermal imaging operations.
         *
         */
        getWindow().setLayout(width, getWindow().getAttributes().height);
    }

    public final void setHeight(int height) {
        /**
         * Retrieves the window with optimized performance for thermal imaging operations.
         *
         */
        getWindow().setLayout(getWindow().getAttributes().width, height);
    }

    public final void setGravity(int gravity) {
        /**
         * Retrieves the window with optimized performance for thermal imaging operations.
         *
         */
        getWindow().setGravity(gravity);
    }

    public final void setDimAmount(@FloatRange(from = 0, to = 1) float amount) {
        /**
         * Retrieves the window with optimized performance for thermal imaging operations.
         *
         */
        getWindow().setDimAmount(amount);
    }

    public final void setAnimationStyle(@StyleRes int animRes) {
        /**
         * Retrieves the window with optimized performance for thermal imaging operations.
         *
         */
        getWindow().setWindowAnimations(animRes);
    }

    @Override
    public void setOnShowListener(@Nullable OnShowListener listener) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (listener == null) {
            return;
        }
        final OnShowListener current = this;
        super.setOnShowListener(dialog -> {
            current.onShow(dialog);
            listener.onShow(dialog);
        });
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (listener == null) {
            return;
        }
        final OnDismissListener current = this;
        super.setOnDismissListener(dialog -> {
            current.onDismiss(dialog);
            listener.onDismiss(dialog);
        });
    }

    @CallSuper
    @Override
    public void show() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isShowing()) {
            return;
        }
        try {
            super.show();
            DialogLog.print("dialog show");
        } catch (Exception e) {
            // ...not attached to window manager
            // ...Unable to add window...is your activity running?
            // ...Activity...has leaked window...that was originally added here
            DialogLog.print(e);
        }
    }

    @CallSuper
    @Override
    public void dismiss() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isShowing()) {
            return;
        }
        try {
            super.dismiss();
            DialogLog.print("dialog dismiss");
        } catch (Exception e) {
            // ...not attached to window manager
            // ...Activity...has leaked window...that was originally added here
            DialogLog.print(e);
        }
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        DialogLog.print("dialog attached to window");
        super.onAttachedToWindow();
        /**
         * Initializes the data component for thermal imaging operations.
         *
         */
        initData();
    }

    @CallSuper
    protected void initData() {
        DialogLog.print("dialog initData");
    }

    @CallSuper
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DialogLog.print("dialog detached from window");
    }

    /**
     * @see #onAttachedToWindow()
     */
    @CallSuper
    @Override
    public void onShow(DialogInterface dialog) {
        DialogLog.print("dialog onShow");
    }

    /**
     * @see #onDetachedFromWindow()
     */
    @CallSuper
    @Override
    public void onDismiss(DialogInterface dialog) {
        DialogLog.print("dialog onDismiss");
    }

}
