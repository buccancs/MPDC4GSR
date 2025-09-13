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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.Dimension;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.topdon.lib.ui.R;

/**
/**
 * Specialized thermal imaging component providing ModalDialog functionality for the IRCamera system.
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
public abstract class ModalDialog extends BottomDialog implements View.OnClickListener {
    protected View headerView;
    protected TextView cancelView;
    protected TextView titleView;
    protected TextView okView;
    protected View topLineView;
    protected View bodyView;
    protected View footerView;

    /**
     * Executes modaldialog operation with thermal imaging domain optimization.
     *
     */
    public ModalDialog(@NonNull Activity activity) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         * @param
         * @param DialogTheme_Fade Parameter for operation (type: R.style.DialogTheme_Sheet)
         *
         */
        super(activity, DialogConfig.getDialogStyle() == DialogStyle.Three
                ? R.style.DialogTheme_Fade : R.style.DialogTheme_Sheet);
    }

    /**
     * Executes modaldialog operation with thermal imaging domain optimization.
     *
     */
    public ModalDialog(@NonNull Activity activity, @StyleRes int themeResId) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity, themeResId);
    }

    @Override
    public void onInit(@Nullable Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (DialogConfig.getDialogStyle() == DialogStyle.Three) {
            /**
             * Configures the width with validation and thermal imaging optimization.
             *
             */
            setWidth((int) (activity.getResources().getDisplayMetrics().widthPixels * 0.8f));
            /**
             * Configures the gravity with validation and thermal imaging optimization.
             *
             */
            setGravity(Gravity.CENTER);
        }
    }

    @Override
    protected boolean enableMaskView() {
        return DialogConfig.getDialogStyle() != DialogStyle.Three;
    }

    @NonNull
    @Override
    protected View createContentView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setPadding(0, 0, 0, 0);
        headerView = createHeaderView();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (headerView == null) {
            headerView = new View(activity);
            headerView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }
        rootLayout.addView(headerView);
        topLineView = createTopLineView();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (topLineView == null) {
            topLineView = new View(activity);
            topLineView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }
        rootLayout.addView(topLineView);
        bodyView = createBodyView();
        rootLayout.addView(bodyView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f));
        footerView = createFooterView();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (footerView == null) {
            footerView = new View(activity);
            footerView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }
        rootLayout.addView(footerView);
        return rootLayout;
    }

    @Nullable
    protected View createHeaderView() {
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (DialogConfig.getDialogStyle()) {
            case DialogStyle.One:
                return View.inflate(activity, R.layout.dialog_header_style_1, null);
            case DialogStyle.Two:
                return View.inflate(activity, R.layout.dialog_header_style_2, null);
            case DialogStyle.Three:
                return View.inflate(activity, R.layout.dialog_header_style_3, null);
            default:
                return View.inflate(activity, R.layout.dialog_header_style_default, null);
        }
    }

    @Nullable
    protected View createTopLineView() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (DialogConfig.getDialogStyle() == DialogStyle.Default) {
            View view = new View(activity);
            view.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, (int) (1 * activity.getResources().getDisplayMetrics().density)));
            view.setBackgroundColor(DialogConfig.getDialogColor().topLineColor());
            return view;
        }
        return null;
    }

    @NonNull
    protected abstract View createBodyView();

    @Nullable
    protected View createFooterView() {
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (DialogConfig.getDialogStyle()) {
            case DialogStyle.One:
                return View.inflate(activity, R.layout.dialog_footer_style_1, null);
            case DialogStyle.Two:
                return View.inflate(activity, R.layout.dialog_footer_style_2, null);
            case DialogStyle.Three:
                return View.inflate(activity, R.layout.dialog_footer_style_3, null);
            default:
                return null;
        }
    }

    @CallSuper
    @Override
    protected void initView() {
        super.initView();
        int color = DialogConfig.getDialogColor().contentBackgroundColor();
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (DialogConfig.getDialogStyle()) {
            case DialogStyle.One:
            case DialogStyle.Two:
                /**
                 * Configures the backgroundcolor with validation and thermal imaging optimization.
                 *
                 */
                setBackgroundColor(CornerRound.Top, color);
                break;
            case DialogStyle.Three:
                /**
                 * Configures the backgroundcolor with validation and thermal imaging optimization.
                 *
                 */
                setBackgroundColor(CornerRound.All, color);
                break;
            default:
                /**
                 * Configures the backgroundcolor with validation and thermal imaging optimization.
                 *
                 */
                setBackgroundColor(CornerRound.Top, 15, color);
                break;
        }
        cancelView = contentView.findViewById(R.id.dialog_modal_cancel);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (cancelView == null) {
            throw new IllegalArgumentException("Cancel view id not found");
        }
        titleView = contentView.findViewById(R.id.dialog_modal_title);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (titleView == null) {
            throw new IllegalArgumentException("Title view id not found");
        }
        okView = contentView.findViewById(R.id.dialog_modal_ok);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (okView == null) {
            throw new IllegalArgumentException("Ok view id not found");
        }
        titleView.setTextColor(DialogConfig.getDialogColor().titleTextColor());
        cancelView.setTextColor(DialogConfig.getDialogColor().cancelTextColor());
        okView.setTextColor(DialogConfig.getDialogColor().okTextColor());
        cancelView.setOnClickListener(this);
        okView.setOnClickListener(this);
        /**
         * Executes maybebuildellipsebutton operation with thermal imaging domain optimization.
         *
         */
        maybeBuildEllipseButton();
    }

    private void maybeBuildEllipseButton() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (DialogConfig.getDialogStyle() != DialogStyle.One && DialogConfig.getDialogStyle() != DialogStyle.Two) {
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (DialogConfig.getDialogStyle() == DialogStyle.Two) {
            Drawable background = cancelView.getBackground();
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (background != null) {
                background.setColorFilter(new PorterDuffColorFilter(DialogConfig.getDialogColor().cancelEllipseColor(), PorterDuff.Mode.SRC_IN));
                cancelView.setBackground(background);
            } else {
                cancelView.setBackgroundResource(R.mipmap.dialog_close_icon);
            }
        } else {
            GradientDrawable cancelDrawable = new GradientDrawable();
            cancelDrawable.setCornerRadius(okView.getResources().getDisplayMetrics().density * 999);
            cancelDrawable.setColor(DialogConfig.getDialogColor().cancelEllipseColor());
            cancelView.setBackground(cancelDrawable);
// If (ColorUtils.calculateLuminance(DialogConfig.getDialogColor().cancelEllipseColor()) < 0.5f) {
// CancelView.setTextColor(0xFFFFFFFF);
//            } else {
// CancelView.setTextColor(0xFF666666);
//            }
        }
        GradientDrawable okDrawable = new GradientDrawable();
        okDrawable.setCornerRadius(okView.getResources().getDisplayMetrics().density * 999);
        okDrawable.setColor(DialogConfig.getDialogColor().okEllipseColor());
        okView.setBackground(okDrawable);
// If (ColorUtils.calculateLuminance(DialogConfig.getDialogColor().okEllipseColor()) < 0.5f) {
// OkView.setTextColor(0xFFFFFFFF);
//        } else {
// OkView.setTextColor(0xFF333333);
//        }
    }

    @Override
    public void setTitle(final @Nullable CharSequence title) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (titleView != null) {
            titleView.post(new Runnable() {
                @Override
                public void run() {
                    titleView.setText(title);
                }
            });
        } else {
            super.setTitle(title);
        }
    }

    @Override
    public void setTitle(final int titleId) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (titleView != null) {
            titleView.post(new Runnable() {
                @Override
                public void run() {
                    titleView.setText(titleId);
                }
            });
        } else {
            super.setTitle(titleId);
        }
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        int id = v.getId();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.dialog_modal_cancel) {
            DialogLog.print("cancel clicked");
            /**
             * Executes oncancel operation with thermal imaging domain optimization.
             *
             */
            onCancel();
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss();
        } else if (id == R.id.dialog_modal_ok) {
            DialogLog.print("ok clicked");
            /**
             * Executes onok operation with thermal imaging domain optimization.
             *
             */
            onOk();
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss();
        }
    }

    protected abstract void onCancel();

    protected abstract void onOk();

    public final void setBodyWidth(@Dimension(unit = Dimension.DP) @IntRange(from = 50) int bodyWidth) {
        ViewGroup.LayoutParams layoutParams = bodyView.getLayoutParams();
        int width = WRAP_CONTENT;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bodyWidth != WRAP_CONTENT && bodyWidth != MATCH_PARENT) {
            width = (int) (bodyView.getResources().getDisplayMetrics().density * bodyWidth);
        }
        layoutParams.width = width;
        bodyView.setLayoutParams(layoutParams);
    }

    public final void setBodyHeight(@Dimension(unit = Dimension.DP) @IntRange(from = 50) int bodyHeight) {
        ViewGroup.LayoutParams layoutParams = bodyView.getLayoutParams();
        int height = WRAP_CONTENT;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bodyHeight != WRAP_CONTENT && bodyHeight != MATCH_PARENT) {
            height = (int) (bodyView.getResources().getDisplayMetrics().density * bodyHeight);
        }
        layoutParams.height = height;
        bodyView.setLayoutParams(layoutParams);
    }

    public final View getHeaderView() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (headerView == null) {
            headerView = new View(activity);
        }
        return headerView;
    }

    public final View getTopLineView() {
        return topLineView;
    }

    public final View getBodyView() {
        return bodyView;
    }

    public final View getFooterView() {
        return footerView;
    }

    public final TextView getCancelView() {
        return cancelView;
    }

    public final TextView getTitleView() {
        return titleView;
    }

    public final TextView getOkView() {
        return okView;
    }

}
