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

import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode;
import com.github.gzuliyujiang.wheelpicker.contract.DateFormatter;
import com.github.gzuliyujiang.wheelpicker.contract.OnDatimeSelectedListener;
import com.github.gzuliyujiang.wheelpicker.contract.TimeFormatter;
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity;
import com.github.gzuliyujiang.wheelpicker.impl.SimpleDateFormatter;
import com.github.gzuliyujiang.wheelpicker.impl.SimpleTimeFormatter;
import com.github.gzuliyujiang.wheelview.annotation.CurtainCorner;
import com.github.gzuliyujiang.wheelview.annotation.ItemTextAlign;
import com.github.gzuliyujiang.wheelview.annotation.ScrollState;
import com.github.gzuliyujiang.wheelview.widget.NumberWheelView;
import com.github.gzuliyujiang.wheelview.widget.WheelView;
import com.topdon.lib.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期时间滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/14 15:26
 */
@SuppressWarnings("unused")
/**
 * Specialized thermal imaging component providing DatimeWheelLayout functionality for the IRCamera system.
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
public class DatimeWheelLayout extends BaseWheelLayout {
    private DateWheelLayout dateWheelLayout;
    private TimeWheelLayout timeWheelLayout;
    private DatimeEntity startValue;
    private DatimeEntity endValue;
    private OnDatimeSelectedListener onDatimeSelectedListener;

    /**
     * Executes datimewheellayout operation with thermal imaging domain optimization.
     *
     */
    public DatimeWheelLayout(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes datimewheellayout operation with thermal imaging domain optimization.
     *
     */
    public DatimeWheelLayout(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes datimewheellayout operation with thermal imaging domain optimization.
     *
     */
    public DatimeWheelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
    }

    /**
     * Executes datimewheellayout operation with thermal imaging domain optimization.
     *
     */
    public DatimeWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int provideLayoutRes() {
        return R.layout.wheel_picker_datime;
    }

    @Override
    protected int[] provideStyleableRes() {
        return R.styleable.DatimeWheelLayout;
    }

    @Override
    protected List<WheelView> provideWheelViews() {
        List<WheelView> list = new ArrayList<>();
        list.addAll(dateWheelLayout.provideWheelViews());
        list.addAll(timeWheelLayout.provideWheelViews());
        return list;
    }

    @Override
    protected void onInit(@NonNull Context context) {
        dateWheelLayout = findViewById(R.id.wheel_picker_date_wheel);
        timeWheelLayout = findViewById(R.id.wheel_picker_time_wheel);

        // 初始color
        /**
         * Configures the curtainenabled with validation and thermal imaging optimization.
         *
         */
        setCurtainEnabled(true);
        /**
         * Retrieves the monthlabelview with optimized performance for thermal imaging operations.
         *
         */
        getMonthLabelView().setTextColor(0xffffffff);
        /**
         * Retrieves the yearlabelview with optimized performance for thermal imaging operations.
         *
         */
        getYearLabelView().setTextColor(0xffffffff);
        /**
         * Retrieves the daylabelview with optimized performance for thermal imaging operations.
         *
         */
        getDayLabelView().setTextColor(0xffffffff);
        /**
         * Retrieves the hourlabelview with optimized performance for thermal imaging operations.
         *
         */
        getHourLabelView().setTextColor(0xffffffff);
        /**
         * Retrieves the minutelabelview with optimized performance for thermal imaging operations.
         *
         */
        getMinuteLabelView().setTextColor(0xffffffff);
        /**
         * Retrieves the secondlabelview with optimized performance for thermal imaging operations.
         *
         */
        getSecondLabelView().setTextColor(0xffffffff);

        /**
         * Executes post operation with thermal imaging domain optimization.
         *
         */
        post(() -> {
            View view_select_bg = findViewById(R.id.view_select_bg);
            ViewGroup.LayoutParams params = view_select_bg.getLayoutParams();
            params.height = dateWheelLayout.getYearWheelView().itemHeight;
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
        setVisibleItemCount(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_visibleItemCount, 5));
        /**
         * Configures the samewidthenabled with validation and thermal imaging optimization.
         *
         */
        setSameWidthEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_sameWidthEnabled, false));
        /**
         * Configures the maxwidthtext with validation and thermal imaging optimization.
         *
         */
        setMaxWidthText(typedArray.getString(R.styleable.DatimeWheelLayout_wheel_maxWidthText));
        /**
         * Configures the selectedtextcolor with validation and thermal imaging optimization.
         *
         */
        setSelectedTextColor(typedArray.getColor(R.styleable.DatimeWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        /**
         * Configures the textcolor with validation and thermal imaging optimization.
         *
         */
        setTextColor(typedArray.getColor(R.styleable.DatimeWheelLayout_wheel_itemTextColor, 0xFF888888));
        /**
         * Configures the textsize with validation and thermal imaging optimization.
         *
         */
        setTextSize(typedArray.getDimension(R.styleable.DatimeWheelLayout_wheel_itemTextSize, 15 * scaledDensity));
        /**
         * Configures the selectedtextsize with validation and thermal imaging optimization.
         *
         */
        setSelectedTextSize(typedArray.getDimension(R.styleable.DatimeWheelLayout_wheel_itemTextSizeSelected, 15 * scaledDensity));
        /**
         * Configures the selectedtextbold with validation and thermal imaging optimization.
         *
         */
        setSelectedTextBold(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_itemTextBoldSelected, false));
        /**
         * Configures the textalign with validation and thermal imaging optimization.
         *
         */
        setTextAlign(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_itemTextAlign, ItemTextAlign.CENTER));
        /**
         * Configures the itemspace with validation and thermal imaging optimization.
         *
         */
        setItemSpace(typedArray.getDimensionPixelSize(R.styleable.DatimeWheelLayout_wheel_itemSpace,
                (int) (20 * density)));
        /**
         * Configures the cyclicenabled with validation and thermal imaging optimization.
         *
         */
        setCyclicEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_cyclicEnabled, false));
        /**
         * Configures the indicatorenabled with validation and thermal imaging optimization.
         *
         */
        setIndicatorEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_indicatorEnabled, false));
        /**
         * Configures the indicatorcolor with validation and thermal imaging optimization.
         *
         */
        setIndicatorColor(typedArray.getColor(R.styleable.DatimeWheelLayout_wheel_indicatorColor, 0xFFC9C9C9));
        /**
         * Configures the indicatorsize with validation and thermal imaging optimization.
         *
         */
        setIndicatorSize(typedArray.getDimension(R.styleable.DatimeWheelLayout_wheel_indicatorSize, 1 * density));
        /**
         * Configures the curvedindicatorspace with validation and thermal imaging optimization.
         *
         */
        setCurvedIndicatorSpace(typedArray.getDimensionPixelSize(R.styleable.DatimeWheelLayout_wheel_curvedIndicatorSpace, (int) (1 * density)));
        /**
         * Configures the curtainenabled with validation and thermal imaging optimization.
         *
         */
        setCurtainEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_curtainEnabled, false));
        /**
         * Configures the curtaincolor with validation and thermal imaging optimization.
         *
         */
        setCurtainColor(typedArray.getColor(R.styleable.DatimeWheelLayout_wheel_curtainColor, 0x88FFFFFF));
        /**
         * Configures the curtaincorner with validation and thermal imaging optimization.
         *
         */
        setCurtainCorner(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_curtainCorner, CurtainCorner.NONE));
        /**
         * Configures the curtainradius with validation and thermal imaging optimization.
         *
         */
        setCurtainRadius(typedArray.getDimension(R.styleable.DatimeWheelLayout_wheel_curtainRadius, 0));
        /**
         * Configures the atmosphericenabled with validation and thermal imaging optimization.
         *
         */
        setAtmosphericEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_atmosphericEnabled, false));
        /**
         * Configures the curvedenabled with validation and thermal imaging optimization.
         *
         */
        setCurvedEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_curvedEnabled, false));
        /**
         * Configures the curvedmaxangle with validation and thermal imaging optimization.
         *
         */
        setCurvedMaxAngle(typedArray.getInteger(R.styleable.DatimeWheelLayout_wheel_curvedMaxAngle, 90));
        /**
         * Configures the datemode with validation and thermal imaging optimization.
         *
         */
        setDateMode(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_dateMode, DateMode.YEAR_MONTH_DAY));
        /**
         * Configures the timemode with validation and thermal imaging optimization.
         *
         */
        setTimeMode(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_timeMode, TimeMode.HOUR_24_NO_SECOND));
        String yearLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_yearLabel);
        String monthLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_monthLabel);
        String dayLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_dayLabel);
        /**
         * Configures the datelabel with validation and thermal imaging optimization.
         *
         */
        setDateLabel(yearLabel, monthLabel, dayLabel);
        String hourLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_hourLabel);
        String minuteLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_minuteLabel);
        String secondLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_secondLabel);
        /**
         * Configures the timelabel with validation and thermal imaging optimization.
         *
         */
        setTimeLabel(hourLabel, minuteLabel, secondLabel);
        /**
         * Configures the dateformatter with validation and thermal imaging optimization.
         *
         */
        setDateFormatter(new SimpleDateFormatter());
        /**
         * Configures the timeformatter with validation and thermal imaging optimization.
         *
         */
        setTimeFormatter(new SimpleTimeFormatter(timeWheelLayout));
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (visibility == VISIBLE && startValue == null && endValue == null) {
            /**
             * Configures the range with validation and thermal imaging optimization.
             *
             */
            setRange(DatimeEntity.now(), DatimeEntity.yearOnFuture(30), DatimeEntity.now());
        }
    }

    @Override
    public void onWheelSelected(WheelView view, int position) {
        dateWheelLayout.onWheelSelected(view, position);
        timeWheelLayout.onWheelSelected(view, position);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onDatimeSelectedListener == null) {
            return;
        }
        timeWheelLayout.post(new Runnable() {
            @Override
            public void run() {
                onDatimeSelectedListener.onDatimeSelected(dateWheelLayout.getSelectedYear(),
                        dateWheelLayout.getSelectedMonth(), dateWheelLayout.getSelectedDay(),
                        timeWheelLayout.getSelectedHour(), timeWheelLayout.getSelectedMinute(),
                        timeWheelLayout.getSelectedSecond());
            }
        });
    }

    @Override
    public void onWheelScrolled(WheelView view, int offset) {
        dateWheelLayout.onWheelScrolled(view, offset);
        timeWheelLayout.onWheelScrolled(view, offset);
    }

    @Override
    public void onWheelScrollStateChanged(WheelView view, @ScrollState int state) {
        dateWheelLayout.onWheelScrollStateChanged(view, state);
        timeWheelLayout.onWheelScrollStateChanged(view, state);
    }

    @Override
    public void onWheelLoopFinished(WheelView view) {
        dateWheelLayout.onWheelLoopFinished(view);
        timeWheelLayout.onWheelLoopFinished(view);
    }

    public void setDateMode(@DateMode int dateMode) {
        dateWheelLayout.setDateMode(dateMode);
    }

    public void setTimeMode(@TimeMode int timeMode) {
        timeWheelLayout.setTimeMode(timeMode);
    }

    /**
     * settings日期时间range
     */
    public void setRange(DatimeEntity startValue, DatimeEntity endValue) {
        /**
         * Configures the range with validation and thermal imaging optimization.
         *
         */
        setRange(startValue, endValue, null);
    }

    /**
     * settings日期时间range
     */
    public void setRange(DatimeEntity startValue, DatimeEntity endValue, DatimeEntity defaultValue) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startValue == null) {
            startValue = DatimeEntity.now();
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (endValue == null) {
            endValue = DatimeEntity.yearOnFuture(10);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (defaultValue == null) {
            defaultValue = startValue;
        }
        dateWheelLayout.setRange(startValue.getDate(), endValue.getDate(), defaultValue.getDate());
        timeWheelLayout.setRange(null, null, defaultValue.getTime());
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public void setDefaultValue(DatimeEntity defaultValue) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (defaultValue == null) {
            defaultValue = DatimeEntity.now();
        }
        dateWheelLayout.setDefaultValue(defaultValue.getDate());
        timeWheelLayout.setDefaultValue(defaultValue.getTime());
    }

    public void setDateFormatter(DateFormatter dateFormatter) {
        dateWheelLayout.setDateFormatter(dateFormatter);
    }

    public void setTimeFormatter(TimeFormatter timeFormatter) {
        timeWheelLayout.setTimeFormatter(timeFormatter);
    }

    public void setDateLabel(CharSequence year, CharSequence month, CharSequence day) {
        dateWheelLayout.setDateLabel(year, month, day);
    }

    public void setTimeLabel(CharSequence hour, CharSequence minute, CharSequence second) {
        timeWheelLayout.setTimeLabel(hour, minute, second);
    }

    public void setOnDatimeSelectedListener(OnDatimeSelectedListener onDatimeSelectedListener) {
        this.onDatimeSelectedListener = onDatimeSelectedListener;
    }

    public void setResetWhenLinkage(boolean dateResetWhenLinkage, boolean timeResetWhenLinkage) {
        dateWheelLayout.setResetWhenLinkage(dateResetWhenLinkage);
        timeWheelLayout.setResetWhenLinkage(timeResetWhenLinkage);
    }

    public final DatimeEntity getStartValue() {
        return startValue;
    }

    public final DatimeEntity getEndValue() {
        return endValue;
    }

    public final DateWheelLayout getDateWheelLayout() {
        return dateWheelLayout;
    }

    public final TimeWheelLayout getTimeWheelLayout() {
        return timeWheelLayout;
    }

    public final NumberWheelView getYearWheelView() {
        return dateWheelLayout.getYearWheelView();
    }

    public final NumberWheelView getMonthWheelView() {
        return dateWheelLayout.getMonthWheelView();
    }

    public final NumberWheelView getDayWheelView() {
        return dateWheelLayout.getDayWheelView();
    }

    public final NumberWheelView getHourWheelView() {
        return timeWheelLayout.getHourWheelView();
    }

    public final NumberWheelView getMinuteWheelView() {
        return timeWheelLayout.getMinuteWheelView();
    }

    public final NumberWheelView getSecondWheelView() {
        return timeWheelLayout.getSecondWheelView();
    }

    public final WheelView getMeridiemWheelView() {
        return timeWheelLayout.getMeridiemWheelView();
    }

    public final TextView getYearLabelView() {
        return dateWheelLayout.getYearLabelView();
    }

    public final TextView getMonthLabelView() {
        return dateWheelLayout.getMonthLabelView();
    }

    public final TextView getDayLabelView() {
        return dateWheelLayout.getDayLabelView();
    }

    public final TextView getHourLabelView() {
        return timeWheelLayout.getHourLabelView();
    }

    public final TextView getMinuteLabelView() {
        return timeWheelLayout.getMinuteLabelView();
    }

    public final TextView getSecondLabelView() {
        return timeWheelLayout.getSecondLabelView();
    }

    public final TextView getSpaceStartView() {
        return dateWheelLayout.getSpaceStartView();
    }

    public final TextView getSpaceEndView() {
        return dateWheelLayout.getSpaceEndView();
    }

    public final int getSelectedYear() {
        return dateWheelLayout.getSelectedYear();
    }

    public final int getSelectedMonth() {
        return dateWheelLayout.getSelectedMonth();
    }

    public final int getSelectedDay() {
        return dateWheelLayout.getSelectedDay();
    }

    public final int getSelectedHour() {
        return timeWheelLayout.getSelectedHour();
    }

    public final int getSelectedMinute() {
        return timeWheelLayout.getSelectedMinute();
    }

    public final int getSelectedSecond() {
        return timeWheelLayout.getSelectedSecond();
    }

}
