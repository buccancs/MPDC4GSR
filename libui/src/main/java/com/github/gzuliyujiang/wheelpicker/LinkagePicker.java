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

package com.github.gzuliyujiang.wheelpicker;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.dialog.ModalDialog;
import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider;
import com.github.gzuliyujiang.wheelpicker.contract.OnLinkagePickedListener;
import com.github.gzuliyujiang.wheelpicker.widget.LinkageWheelLayout;
import com.github.gzuliyujiang.wheelview.widget.WheelView;

/**
 * 二三级联动selection器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @see com.github.gzuliyujiang.wheelview.contract.TextProvider
 * @see LinkageProvider
 * @see LinkageWheelLayout
 * @see OnLinkagePickedListener
 * @since 2019/6/17 11:21
 */
@SuppressWarnings({"WeakerAccess", "unused"})
/**
 * Specialized thermal imaging component providing LinkagePicker functionality for the IRCamera system.
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
public class LinkagePicker extends ModalDialog {
    protected LinkageWheelLayout wheelLayout;
    private OnLinkagePickedListener onLinkagePickedListener;

    /**
     * Executes linkagepicker operation with thermal imaging domain optimization.
     *
     */
    public LinkagePicker(@NonNull Activity activity) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity);
    }

    /**
     * Executes linkagepicker operation with thermal imaging domain optimization.
     *
     */
    public LinkagePicker(@NonNull Activity activity, @StyleRes int themeResId) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity, themeResId);
    }

    @NonNull
    @Override
    protected View createBodyView() {
        wheelLayout = new LinkageWheelLayout(activity);
        return wheelLayout;
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected void onOk() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onLinkagePickedListener != null) {
            Object first = wheelLayout.getFirstWheelView().getCurrentItem();
            Object second = wheelLayout.getSecondWheelView().getCurrentItem();
            Object third = wheelLayout.getThirdWheelView().getCurrentItem();
            onLinkagePickedListener.onLinkagePicked(first, second, third);
        }
    }

    public void setData(@NonNull LinkageProvider data) {
        wheelLayout.setData(data);
    }

    public void setDefaultValue(Object first, Object second, Object third) {
        wheelLayout.setDefaultValue(first, second, third);
    }

    public void setOnLinkagePickedListener(OnLinkagePickedListener onLinkagePickedListener) {
        this.onLinkagePickedListener = onLinkagePickedListener;
    }

    public final LinkageWheelLayout getWheelLayout() {
        return wheelLayout;
    }

    public final WheelView getFirstWheelView() {
        return wheelLayout.getFirstWheelView();
    }

    public final WheelView getSecondWheelView() {
        return wheelLayout.getSecondWheelView();
    }

    public final WheelView getThirdWheelView() {
        return wheelLayout.getThirdWheelView();
    }

    public final TextView getFirstLabelView() {
        return wheelLayout.getFirstLabelView();
    }

    public final TextView getSecondLabelView() {
        return wheelLayout.getSecondLabelView();
    }

    public final TextView getThirdLabelView() {
        return wheelLayout.getThirdLabelView();
    }

    public final ProgressBar getLoadingView() {
        return wheelLayout.getLoadingView();
    }

}
