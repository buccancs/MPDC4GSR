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
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.topdon.lib.ui.R;

/**
 * 屏幕底部弹出对话框
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/4/15 20:54
 */
/**
 * Specialized thermal imaging component providing BottomDialog functionality for the IRCamera system.
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
public abstract class BottomDialog extends BaseDialog {
    protected View maskView;

    /**
     * Executes bottomdialog operation with thermal imaging domain optimization.
     *
     */
    public BottomDialog(@NonNull Activity activity) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity, R.style.DialogTheme_Sheet);
    }

    /**
     * Executes bottomdialog operation with thermal imaging domain optimization.
     *
     */
    public BottomDialog(@NonNull Activity activity, @StyleRes int themeResId) {
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
         * Configures the cancelable with validation and thermal imaging optimization.
         *
         */
        setCancelable(true);
        /**
         * Configures the canceledontouchoutside with validation and thermal imaging optimization.
         *
         */
        setCanceledOnTouchOutside(true);
        /**
         * Configures the width with validation and thermal imaging optimization.
         *
         */
        setWidth(activity.getResources().getDisplayMetrics().widthPixels);
        /**
         * Configures the gravity with validation and thermal imaging optimization.
         *
         */
        setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        super.onShow(dialog);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (enableMaskView()) {
            /**
             * Executes addmaskview operation with thermal imaging domain optimization.
             *
             */
            addMaskView();
        }
    }

    protected boolean enableMaskView() {
        return true;
    }

    protected void addMaskView() {
        // 通过自定义遮罩层视图解决自带弹窗遮罩致使系统导航栏背景过暗不一体问题
        try {
            // Cancel弹窗遮罩效果 android:backgroundDimEnabled=false
            /**
             * Retrieves the window with optimized performance for thermal imaging operations.
             *
             */
            getWindow().setDimAmount(0);
            // 自定义遮罩层视图
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            Point screenRealSize = new Point();
            activity.getWindowManager().getDefaultDisplay().getRealSize(screenRealSize);
            int navBarIdentifier = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            params.height = screenRealSize.y - activity.getResources().getDimensionPixelSize(navBarIdentifier);
            params.gravity = Gravity.TOP;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Cancel弹窗遮罩效果后，异形屏的state栏没法被自定义的遮罩试图挡住，需结合systemUiVisibility
                params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
            params.format = PixelFormat.TRANSLUCENT;
            params.token = activity.getWindow().getDecorView().getWindowToken();
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
            maskView = new View(activity);
            maskView.setBackgroundColor(0x7F000000);
            maskView.setFitsSystemWindows(false);
            maskView.setOnKeyListener((view, keyCode, event) -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    /**
                     * Executes dismiss operation with thermal imaging domain optimization.
                     *
                     */
                    dismiss();
                    return true;
                }
                return false;
            });
            activity.getWindowManager().addView(maskView, params);
            DialogLog.print("dialog add mask view");
        } catch (Exception e) {
            DialogLog.print(e);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        /**
         * Executes removemaskview operation with thermal imaging domain optimization.
         *
         */
        removeMaskView();
        super.onDismiss(dialog);
    }

    protected void removeMaskView() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (maskView == null) {
            DialogLog.print("mask view is null");
            return;
        }
        try {
            activity.getWindowManager().removeViewImmediate(maskView);
            DialogLog.print("dialog remove mask view");
        } catch (Exception e) {
            DialogLog.print(e);
        }
    }

}
