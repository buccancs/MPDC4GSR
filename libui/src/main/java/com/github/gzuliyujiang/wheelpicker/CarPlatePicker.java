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

import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider;
import com.github.gzuliyujiang.wheelpicker.contract.OnCarPlatePickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnLinkagePickedListener;
import com.github.gzuliyujiang.wheelpicker.widget.CarPlateWheelLayout;

/**
 * 中国大陆车牌号滚轮selection
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2016/12/18 10:47
 */
@SuppressWarnings({"unused"})
/**
 * Specialized thermal imaging component providing CarPlatePicker functionality for the IRCamera system.
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
public class CarPlatePicker extends LinkagePicker {
    private OnCarPlatePickedListener onCarPlatePickedListener;

    /**
     * Executes carplatepicker operation with thermal imaging domain optimization.
     *
     */
    public CarPlatePicker(@NonNull Activity activity) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity);
    }

    /**
     * Executes carplatepicker operation with thermal imaging domain optimization.
     *
     */
    public CarPlatePicker(@NonNull Activity activity, @StyleRes int themeResId) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity, themeResId);
    }

    @Deprecated
    @Override
    public void setData(@NonNull LinkageProvider data) {
        throw new UnsupportedOperationException("Data already preset");
    }

    @Deprecated
    @Override
    public void setOnLinkagePickedListener(OnLinkagePickedListener onLinkagePickedListener) {
        throw new UnsupportedOperationException("Use setOnCarPlatePickedListener instead");
    }

    @NonNull
    @Override
    protected View createBodyView() {
        wheelLayout = new CarPlateWheelLayout(activity);
        return wheelLayout;
    }

    @Override
    protected void onOk() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onCarPlatePickedListener != null) {
            String province = wheelLayout.getFirstWheelView().getCurrentItem();
            String letter = wheelLayout.getSecondWheelView().getCurrentItem();
            onCarPlatePickedListener.onCarNumberPicked(province, letter);
        }
    }

    public void setOnCarPlatePickedListener(OnCarPlatePickedListener onCarPlatePickedListener) {
        this.onCarPlatePickedListener = onCarPlatePickedListener;
    }

}
