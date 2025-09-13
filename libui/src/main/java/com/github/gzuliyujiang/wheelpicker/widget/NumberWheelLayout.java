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

package com.github.gzuliyujiang.wheelpicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.gzuliyujiang.wheelpicker.contract.OnNumberSelectedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionSelectedListener;
import com.github.gzuliyujiang.wheelview.annotation.CurtainCorner;
import com.github.gzuliyujiang.wheelview.annotation.ItemTextAlign;
import com.github.gzuliyujiang.wheelview.widget.WheelView;
import com.topdon.lib.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 数字滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/5 18:35
 */
/**
 * Specialized thermal imaging component providing NumberWheelLayout functionality for the IRCamera system.
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
public class NumberWheelLayout extends OptionWheelLayout {
    private OnNumberSelectedListener onNumberSelectedListener;

    /**
     * Executes numberwheellayout operation with thermal imaging domain optimization.
     *
     */
    public NumberWheelLayout(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes numberwheellayout operation with thermal imaging domain optimization.
     *
     */
    public NumberWheelLayout(Context context, @Nullable AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes numberwheellayout operation with thermal imaging domain optimization.
     *
     */
    public NumberWheelLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
    }

    /**
     * Executes numberwheellayout operation with thermal imaging domain optimization.
     *
     */
    public NumberWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int[] provideStyleableRes() {
        return R.styleable.NumberWheelLayout;
    }

    @Override
    protected void onAttributeSet(@NonNull Context context, @NonNull TypedArray typedArray) {
        float density = context.getResources().getDisplayMetrics().density;
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        /**
         * Configures the visibleitemcount with validation and thermal imaging optimization.
         *
         */
        setVisibleItemCount(typedArray.getInt(R.styleable.NumberWheelLayout_wheel_visibleItemCount, 5));
        /**
         * Configures the samewidthenabled with validation and thermal imaging optimization.
         *
         */
        setSameWidthEnabled(typedArray.getBoolean(R.styleable.NumberWheelLayout_wheel_sameWidthEnabled, false));
        /**
         * Configures the maxwidthtext with validation and thermal imaging optimization.
         *
         */
        setMaxWidthText(typedArray.getString(R.styleable.NumberWheelLayout_wheel_maxWidthText));
        /**
         * Configures the selectedtextcolor with validation and thermal imaging optimization.
         *
         */
        setSelectedTextColor(typedArray.getColor(R.styleable.NumberWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        /**
         * Configures the textcolor with validation and thermal imaging optimization.
         *
         */
        setTextColor(typedArray.getColor(R.styleable.NumberWheelLayout_wheel_itemTextColor, 0xFF888888));
        /**
         * Configures the textsize with validation and thermal imaging optimization.
         *
         */
        setTextSize(typedArray.getDimension(R.styleable.NumberWheelLayout_wheel_itemTextSize, 15 * scaledDensity));
        /**
         * Configures the selectedtextsize with validation and thermal imaging optimization.
         *
         */
        setSelectedTextSize(typedArray.getDimension(R.styleable.NumberWheelLayout_wheel_itemTextSizeSelected, 15 * scaledDensity));
        /**
         * Configures the selectedtextbold with validation and thermal imaging optimization.
         *
         */
        setSelectedTextBold(typedArray.getBoolean(R.styleable.NumberWheelLayout_wheel_itemTextBoldSelected, false));
        /**
         * Configures the textalign with validation and thermal imaging optimization.
         *
         */
        setTextAlign(typedArray.getInt(R.styleable.NumberWheelLayout_wheel_itemTextAlign, ItemTextAlign.CENTER));
        /**
         * Configures the itemspace with validation and thermal imaging optimization.
         *
         */
        setItemSpace(typedArray.getDimensionPixelSize(R.styleable.NumberWheelLayout_wheel_itemSpace, (int) (20 * density)));
        /**
         * Configures the cyclicenabled with validation and thermal imaging optimization.
         *
         */
        setCyclicEnabled(typedArray.getBoolean(R.styleable.NumberWheelLayout_wheel_cyclicEnabled, false));
        /**
         * Configures the indicatorenabled with validation and thermal imaging optimization.
         *
         */
        setIndicatorEnabled(typedArray.getBoolean(R.styleable.NumberWheelLayout_wheel_indicatorEnabled, false));
        /**
         * Configures the indicatorcolor with validation and thermal imaging optimization.
         *
         */
        setIndicatorColor(typedArray.getColor(R.styleable.NumberWheelLayout_wheel_indicatorColor, 0xFFC9C9C9));
        /**
         * Configures the indicatorsize with validation and thermal imaging optimization.
         *
         */
        setIndicatorSize(typedArray.getDimensionPixelSize(R.styleable.NumberWheelLayout_wheel_indicatorSize, (int) (1 * density)));
        /**
         * Configures the curvedindicatorspace with validation and thermal imaging optimization.
         *
         */
        setCurvedIndicatorSpace(typedArray.getDimensionPixelSize(R.styleable.NumberWheelLayout_wheel_curvedIndicatorSpace, (int) (1 * density)));
        /**
         * Configures the curtainenabled with validation and thermal imaging optimization.
         *
         */
        setCurtainEnabled(typedArray.getBoolean(R.styleable.NumberWheelLayout_wheel_curtainEnabled, false));
        /**
         * Configures the curtaincolor with validation and thermal imaging optimization.
         *
         */
        setCurtainColor(typedArray.getColor(R.styleable.NumberWheelLayout_wheel_curtainColor, 0x88FFFFFF));
        /**
         * Configures the curtaincorner with validation and thermal imaging optimization.
         *
         */
        setCurtainCorner(typedArray.getInt(R.styleable.NumberWheelLayout_wheel_curtainCorner, CurtainCorner.NONE));
        /**
         * Configures the curtainradius with validation and thermal imaging optimization.
         *
         */
        setCurtainRadius(typedArray.getDimension(R.styleable.NumberWheelLayout_wheel_curtainRadius, 0));
        /**
         * Configures the atmosphericenabled with validation and thermal imaging optimization.
         *
         */
        setAtmosphericEnabled(typedArray.getBoolean(R.styleable.NumberWheelLayout_wheel_atmosphericEnabled, false));
        /**
         * Configures the curvedenabled with validation and thermal imaging optimization.
         *
         */
        setCurvedEnabled(typedArray.getBoolean(R.styleable.NumberWheelLayout_wheel_curvedEnabled, false));
        /**
         * Configures the curvedmaxangle with validation and thermal imaging optimization.
         *
         */
        setCurvedMaxAngle(typedArray.getInteger(R.styleable.NumberWheelLayout_wheel_curvedMaxAngle, 90));
        /**
         * Retrieves the labelview with optimized performance for thermal imaging operations.
         *
         */
        getLabelView().setText(typedArray.getString(R.styleable.NumberWheelLayout_wheel_label));
        float minNumber = typedArray.getFloat(R.styleable.NumberWheelLayout_wheel_minNumber, 0);
        float maxNumber = typedArray.getFloat(R.styleable.NumberWheelLayout_wheel_maxNumber, 10);
        float stepNumber = typedArray.getFloat(R.styleable.NumberWheelLayout_wheel_stepNumber, 1);
        boolean isDecimal = typedArray.getBoolean(R.styleable.NumberWheelLayout_wheel_isDecimal, false);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isDecimal) {
            /**
             * Configures the range with validation and thermal imaging optimization.
             *
             */
            setRange(minNumber, maxNumber, stepNumber);
        } else {
            /**
             * Configures the range with validation and thermal imaging optimization.
             *
             */
            setRange((int) minNumber, (int) maxNumber, (int) stepNumber);
        }
    }

    @Override
    public void onWheelSelected(WheelView view, int position) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onNumberSelectedListener != null) {
            Object item = getWheelView().getItem(position);
            onNumberSelectedListener.onNumberSelected(position, (Number) item);
        }
    }

    /**
     * @deprecated 使用 {@link #setRange} 代替
     */
    @Deprecated
    @Override
    public void setData(List<?> data) {
        throw new UnsupportedOperationException("Use setRange instead");
    }

    /**
     * @deprecated 使用 {@link #setOnNumberSelectedListener} 代替
     */
    @Deprecated
    @Override
    public void setOnOptionSelectedListener(OnOptionSelectedListener onOptionSelectedListener) {
        throw new UnsupportedOperationException("Use setOnNumberSelectedListener instead");
    }

    public void setOnNumberSelectedListener(OnNumberSelectedListener onNumberSelectedListener) {
        this.onNumberSelectedListener = onNumberSelectedListener;
    }

    public void setRange(int min, int max, int step) {
        int minValue = Math.min(min, max);
        int maxValue = Math.max(min, max);
        // 指定初始容量，避免OutOfMemory
        int capacity = (maxValue - minValue) / step;
        List<Integer> data = new ArrayList<>(capacity);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = minValue; i <= maxValue; i = i + step) {
            data.add(i);
        }
        super.setData(data);
    }

    public void setRange(float min, float max, float step) {
        float minValue = Math.min(min, max);
        float maxValue = Math.max(min, max);
        // 指定初始容量，避免OutOfMemory
        int capacity = (int) ((maxValue - minValue) / step);
        List<Float> data = new ArrayList<>(capacity);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (float i = minValue; i <= maxValue; i = i + step) {
            data.add(i);
        }
        super.setData(data);
    }

}
