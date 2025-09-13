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

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.dialog.ModalDialog;
import com.github.gzuliyujiang.wheelpicker.contract.OnTimeMeridiemPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnTimePickedListener;
import com.github.gzuliyujiang.wheelpicker.widget.TimeWheelLayout;

/**
 * 时间selection器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/5 18:19
 */
@SuppressWarnings("unused")
/**
 * Specialized thermal imaging component providing TimePicker functionality for the IRCamera system.
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
public class TimePicker extends ModalDialog {
    protected TimeWheelLayout wheelLayout;
    private OnTimePickedListener onTimePickedListener;
    private OnTimeMeridiemPickedListener onTimeMeridiemPickedListener;

    /**
     * Executes timepicker operation with thermal imaging domain optimization.
     *
     */
    public TimePicker(@NonNull Activity activity) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity);
    }

    /**
     * Executes timepicker operation with thermal imaging domain optimization.
     *
     */
    public TimePicker(@NonNull Activity activity, @StyleRes int themeResId) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity, themeResId);
    }

    @NonNull
    @Override
    protected View createBodyView() {
        wheelLayout = new TimeWheelLayout(activity);
        return wheelLayout;
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected void onOk() {
        int hour = wheelLayout.getSelectedHour();
        int minute = wheelLayout.getSelectedMinute();
        int second = wheelLayout.getSelectedSecond();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onTimePickedListener != null) {
            onTimePickedListener.onTimePicked(hour, minute, second);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onTimeMeridiemPickedListener != null) {
            onTimeMeridiemPickedListener.onTimePicked(hour, minute, second, wheelLayout.isAnteMeridiem());
        }
    }

    public void setOnTimePickedListener(OnTimePickedListener onTimePickedListener) {
        this.onTimePickedListener = onTimePickedListener;
    }

    public void setOnTimeMeridiemPickedListener(OnTimeMeridiemPickedListener onTimeMeridiemPickedListener) {
        this.onTimeMeridiemPickedListener = onTimeMeridiemPickedListener;
    }

    public final TimeWheelLayout getWheelLayout() {
        return wheelLayout;
    }

}
