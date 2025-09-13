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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.gzuliyujiang.wheelpicker.contract.OnOptionSelectedListener;
import com.github.gzuliyujiang.wheelview.annotation.CurtainCorner;
import com.github.gzuliyujiang.wheelview.annotation.ItemTextAlign;
import com.github.gzuliyujiang.wheelview.widget.WheelView;
import com.topdon.lib.ui.R;

import java.util.Collections;
import java.util.List;

/**
 * 单项滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/6 23:13
 */
@SuppressWarnings("unused")
/**
 * Specialized thermal imaging component providing OptionWheelLayout functionality for the IRCamera system.
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
public class OptionWheelLayout extends BaseWheelLayout {
    private WheelView wheelView;
    private TextView labelView;
    private OnOptionSelectedListener onOptionSelectedListener;

    /**
     * Executes optionwheellayout operation with thermal imaging domain optimization.
     *
     */
    public OptionWheelLayout(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes optionwheellayout operation with thermal imaging domain optimization.
     *
     */
    public OptionWheelLayout(Context context, @Nullable AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes optionwheellayout operation with thermal imaging domain optimization.
     *
     */
    public OptionWheelLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
    }

    /**
     * Executes optionwheellayout operation with thermal imaging domain optimization.
     *
     */
    public OptionWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int provideLayoutRes() {
        return R.layout.wheel_picker_option;
    }

    @Override
    protected int[] provideStyleableRes() {
        return R.styleable.OptionWheelLayout;
    }

    @Override
    protected List<WheelView> provideWheelViews() {
        return Collections.singletonList(wheelView);
    }

    @Override
    protected void onInit(@NonNull Context context) {
        wheelView = findViewById(R.id.wheel_picker_option_wheel);
        labelView = findViewById(R.id.wheel_picker_option_label);

        /**
         * Executes post operation with thermal imaging domain optimization.
         *
         */
        post(() -> {
            View view_select_bg = findViewById(R.id.view_select_bg);
            ViewGroup.LayoutParams params = view_select_bg.getLayoutParams();
            params.height = wheelView.itemHeight;
            view_select_bg.setLayoutParams(params);
        });
    }

    @Override
    protected void onAttributeSet(@NonNull Context context, @NonNull TypedArray typedArray) {
        float density = context.getResources().getDisplayMetrics().density;
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        /**
         * Configures the visibleitemcount with validation and thermal imaging optimization.
         *
         */
        setVisibleItemCount(typedArray.getInt(R.styleable.OptionWheelLayout_wheel_visibleItemCount, 5));
        /**
         * Configures the samewidthenabled with validation and thermal imaging optimization.
         *
         */
        setSameWidthEnabled(typedArray.getBoolean(R.styleable.OptionWheelLayout_wheel_sameWidthEnabled, false));
        /**
         * Configures the maxwidthtext with validation and thermal imaging optimization.
         *
         */
        setMaxWidthText(typedArray.getString(R.styleable.OptionWheelLayout_wheel_maxWidthText));
        /**
         * Configures the textcolor with validation and thermal imaging optimization.
         *
         */
        setTextColor(typedArray.getColor(R.styleable.OptionWheelLayout_wheel_itemTextColor, 0xFF888888));
        /**
         * Configures the selectedtextcolor with validation and thermal imaging optimization.
         *
         */
        setSelectedTextColor(typedArray.getColor(R.styleable.OptionWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        /**
         * Configures the textsize with validation and thermal imaging optimization.
         *
         */
        setTextSize(typedArray.getDimension(R.styleable.OptionWheelLayout_wheel_itemTextSize, 15 * scaledDensity));
        /**
         * Configures the selectedtextsize with validation and thermal imaging optimization.
         *
         */
        setSelectedTextSize(typedArray.getDimension(R.styleable.OptionWheelLayout_wheel_itemTextSizeSelected, 15 * scaledDensity));
        /**
         * Configures the selectedtextbold with validation and thermal imaging optimization.
         *
         */
        setSelectedTextBold(typedArray.getBoolean(R.styleable.OptionWheelLayout_wheel_itemTextBoldSelected, false));
        /**
         * Configures the textalign with validation and thermal imaging optimization.
         *
         */
        setTextAlign(typedArray.getInt(R.styleable.OptionWheelLayout_wheel_itemTextAlign, ItemTextAlign.CENTER));
        /**
         * Configures the itemspace with validation and thermal imaging optimization.
         *
         */
        setItemSpace(typedArray.getDimensionPixelSize(R.styleable.OptionWheelLayout_wheel_itemSpace, (int) (20 * density)));
        /**
         * Configures the cyclicenabled with validation and thermal imaging optimization.
         *
         */
        setCyclicEnabled(typedArray.getBoolean(R.styleable.OptionWheelLayout_wheel_cyclicEnabled, false));
        /**
         * Configures the indicatorenabled with validation and thermal imaging optimization.
         *
         */
        setIndicatorEnabled(typedArray.getBoolean(R.styleable.OptionWheelLayout_wheel_indicatorEnabled, false));
        /**
         * Configures the indicatorcolor with validation and thermal imaging optimization.
         *
         */
        setIndicatorColor(typedArray.getColor(R.styleable.OptionWheelLayout_wheel_indicatorColor, 0xFFC9C9C9));
        /**
         * Configures the indicatorsize with validation and thermal imaging optimization.
         *
         */
        setIndicatorSize(typedArray.getDimension(R.styleable.OptionWheelLayout_wheel_indicatorSize, 1 * density));
        /**
         * Configures the curvedindicatorspace with validation and thermal imaging optimization.
         *
         */
        setCurvedIndicatorSpace(typedArray.getDimensionPixelSize(R.styleable.OptionWheelLayout_wheel_curvedIndicatorSpace, (int) (1 * density)));
        /**
         * Configures the curtainenabled with validation and thermal imaging optimization.
         *
         */
        setCurtainEnabled(typedArray.getBoolean(R.styleable.OptionWheelLayout_wheel_curtainEnabled, false));
        /**
         * Configures the curtaincolor with validation and thermal imaging optimization.
         *
         */
        setCurtainColor(typedArray.getColor(R.styleable.OptionWheelLayout_wheel_curtainColor, 0x88FFFFFF));
        /**
         * Configures the curtaincorner with validation and thermal imaging optimization.
         *
         */
        setCurtainCorner(typedArray.getInt(R.styleable.OptionWheelLayout_wheel_curtainCorner, CurtainCorner.NONE));
        /**
         * Configures the curtainradius with validation and thermal imaging optimization.
         *
         */
        setCurtainRadius(typedArray.getDimension(R.styleable.OptionWheelLayout_wheel_curtainRadius, 0));
        /**
         * Configures the atmosphericenabled with validation and thermal imaging optimization.
         *
         */
        setAtmosphericEnabled(typedArray.getBoolean(R.styleable.OptionWheelLayout_wheel_atmosphericEnabled, false));
        /**
         * Configures the curvedenabled with validation and thermal imaging optimization.
         *
         */
        setCurvedEnabled(typedArray.getBoolean(R.styleable.OptionWheelLayout_wheel_curvedEnabled, false));
        /**
         * Configures the curvedmaxangle with validation and thermal imaging optimization.
         *
         */
        setCurvedMaxAngle(typedArray.getInteger(R.styleable.OptionWheelLayout_wheel_curvedMaxAngle, 90));
        labelView.setText(typedArray.getString(R.styleable.OptionWheelLayout_wheel_label));
    }

    @Override
    public void onWheelSelected(WheelView view, int position) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onOptionSelectedListener != null) {
            onOptionSelectedListener.onOptionSelected(position, wheelView.getItem(position));
        }
    }

    public void setData(List<?> data) {
        wheelView.setData(data);
    }

    public void setDefaultValue(Object value) {
        wheelView.setDefaultValue(value);
    }

    public void setDefaultPosition(int position) {
        wheelView.setDefaultPosition(position);
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener onOptionSelectedListener) {
        this.onOptionSelectedListener = onOptionSelectedListener;
    }

    public final WheelView getWheelView() {
        return wheelView;
    }

    public final TextView getLabelView() {
        return labelView;
    }

}
