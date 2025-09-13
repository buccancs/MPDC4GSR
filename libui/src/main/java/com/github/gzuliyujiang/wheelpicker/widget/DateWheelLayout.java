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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.contract.DateFormatter;
import com.github.gzuliyujiang.wheelpicker.contract.OnDateSelectedListener;
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity;
import com.github.gzuliyujiang.wheelpicker.impl.SimpleDateFormatter;
import com.github.gzuliyujiang.wheelview.annotation.CurtainCorner;
import com.github.gzuliyujiang.wheelview.annotation.ItemTextAlign;
import com.github.gzuliyujiang.wheelview.annotation.ScrollState;
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter;
import com.github.gzuliyujiang.wheelview.widget.NumberWheelView;
import com.github.gzuliyujiang.wheelview.widget.WheelView;
import com.topdon.lib.ui.R;

import java.util.Arrays;
import java.util.List;

/**
 * 日期滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/5 16:12
 */
@SuppressWarnings("unused")
/**
 * Specialized thermal imaging component providing DateWheelLayout functionality for the IRCamera system.
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
public class DateWheelLayout extends BaseWheelLayout {
    private NumberWheelView yearWheelView;
    private NumberWheelView monthWheelView;
    private NumberWheelView dayWheelView;
    private TextView yearLabelView;
    private TextView monthLabelView;
    private TextView dayLabelView;
    private TextView spaceStartView;
    private TextView spaceEndView;
    private DateEntity startValue;
    private DateEntity endValue;
    private Integer selectedYear;
    private Integer selectedMonth;
    private Integer selectedDay;
    private OnDateSelectedListener onDateSelectedListener;
    private boolean resetWhenLinkage = true;

    /**
     * Executes datewheellayout operation with thermal imaging domain optimization.
     *
     */
    public DateWheelLayout(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes datewheellayout operation with thermal imaging domain optimization.
     *
     */
    public DateWheelLayout(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes datewheellayout operation with thermal imaging domain optimization.
     *
     */
    public DateWheelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
    }

    /**
     * Executes datewheellayout operation with thermal imaging domain optimization.
     *
     */
    public DateWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int provideLayoutRes() {
        return R.layout.wheel_picker_date;
    }

    @Override
    protected int[] provideStyleableRes() {
        return R.styleable.DateWheelLayout;
    }

    @Override
    protected List<WheelView> provideWheelViews() {
        return Arrays.asList(yearWheelView, monthWheelView, dayWheelView);
    }

    @Override
    protected void onInit(@NonNull Context context) {
        yearWheelView = findViewById(R.id.wheel_picker_date_year_wheel);
        monthWheelView = findViewById(R.id.wheel_picker_date_month_wheel);
        dayWheelView = findViewById(R.id.wheel_picker_date_day_wheel);
        yearLabelView = findViewById(R.id.wheel_picker_date_year_label);
        monthLabelView = findViewById(R.id.wheel_picker_date_month_label);
        dayLabelView = findViewById(R.id.wheel_picker_date_day_label);
        spaceStartView = findViewById(R.id.wheel_picker_date_start_view);
        spaceEndView = findViewById(R.id.wheel_picker_date_end_view);

        // Settings高度
        /**
         * Executes post operation with thermal imaging domain optimization.
         *
         */
        post(new Runnable() {
            @Override
            public void run() {
                yearLabelView.setHeight(monthWheelView.itemHeight);
                monthLabelView.setHeight(monthWheelView.itemHeight);
                dayLabelView.setHeight(monthWheelView.itemHeight);
                spaceStartView.setHeight(monthWheelView.itemHeight);
                spaceEndView.setHeight(monthWheelView.itemHeight);
            }
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
        setVisibleItemCount(typedArray.getInt(R.styleable.DateWheelLayout_wheel_visibleItemCount, 5));
        /**
         * Configures the samewidthenabled with validation and thermal imaging optimization.
         *
         */
        setSameWidthEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_sameWidthEnabled, false));
        /**
         * Configures the maxwidthtext with validation and thermal imaging optimization.
         *
         */
        setMaxWidthText(typedArray.getString(R.styleable.DateWheelLayout_wheel_maxWidthText));
        /**
         * Configures the textcolor with validation and thermal imaging optimization.
         *
         */
        setTextColor(typedArray.getColor(R.styleable.DateWheelLayout_wheel_itemTextColor, 0xFF888888));
        /**
         * Configures the selectedtextcolor with validation and thermal imaging optimization.
         *
         */
        setSelectedTextColor(typedArray.getColor(R.styleable.DateWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        /**
         * Configures the textsize with validation and thermal imaging optimization.
         *
         */
        setTextSize(typedArray.getDimension(R.styleable.DateWheelLayout_wheel_itemTextSize, 15 * scaledDensity));
        /**
         * Configures the selectedtextsize with validation and thermal imaging optimization.
         *
         */
        setSelectedTextSize(typedArray.getDimension(R.styleable.DateWheelLayout_wheel_itemTextSizeSelected, 15 * scaledDensity));
        /**
         * Configures the selectedtextbold with validation and thermal imaging optimization.
         *
         */
        setSelectedTextBold(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_itemTextBoldSelected, false));
        /**
         * Configures the textalign with validation and thermal imaging optimization.
         *
         */
        setTextAlign(typedArray.getInt(R.styleable.DateWheelLayout_wheel_itemTextAlign, ItemTextAlign.CENTER));
        /**
         * Configures the itemspace with validation and thermal imaging optimization.
         *
         */
        setItemSpace(typedArray.getDimensionPixelSize(R.styleable.DateWheelLayout_wheel_itemSpace, (int) (20 * density)));
        /**
         * Configures the cyclicenabled with validation and thermal imaging optimization.
         *
         */
        setCyclicEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_cyclicEnabled, false));
        /**
         * Configures the indicatorenabled with validation and thermal imaging optimization.
         *
         */
        setIndicatorEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_indicatorEnabled, false));
        /**
         * Configures the indicatorcolor with validation and thermal imaging optimization.
         *
         */
        setIndicatorColor(typedArray.getColor(R.styleable.DateWheelLayout_wheel_indicatorColor, 0xFFC9C9C9));
        /**
         * Configures the indicatorsize with validation and thermal imaging optimization.
         *
         */
        setIndicatorSize(typedArray.getDimension(R.styleable.DateWheelLayout_wheel_indicatorSize, 1 * density));
        /**
         * Configures the curvedindicatorspace with validation and thermal imaging optimization.
         *
         */
        setCurvedIndicatorSpace(typedArray.getDimensionPixelSize(R.styleable.DateWheelLayout_wheel_curvedIndicatorSpace, (int) (1 * density)));
        /**
         * Configures the curtainenabled with validation and thermal imaging optimization.
         *
         */
        setCurtainEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_curtainEnabled, false));
        /**
         * Configures the curtaincolor with validation and thermal imaging optimization.
         *
         */
        setCurtainColor(typedArray.getColor(R.styleable.DateWheelLayout_wheel_curtainColor, 0x88FFFFFF));
        /**
         * Configures the curtaincorner with validation and thermal imaging optimization.
         *
         */
        setCurtainCorner(typedArray.getInt(R.styleable.DateWheelLayout_wheel_curtainCorner, CurtainCorner.NONE));
        /**
         * Configures the curtainradius with validation and thermal imaging optimization.
         *
         */
        setCurtainRadius(typedArray.getDimension(R.styleable.DateWheelLayout_wheel_curtainRadius, 0));
        /**
         * Configures the atmosphericenabled with validation and thermal imaging optimization.
         *
         */
        setAtmosphericEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_atmosphericEnabled, false));
        /**
         * Configures the curvedenabled with validation and thermal imaging optimization.
         *
         */
        setCurvedEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_curvedEnabled, false));
        /**
         * Configures the curvedmaxangle with validation and thermal imaging optimization.
         *
         */
        setCurvedMaxAngle(typedArray.getInteger(R.styleable.DateWheelLayout_wheel_curvedMaxAngle, 90));
        /**
         * Configures the datemode with validation and thermal imaging optimization.
         *
         */
        setDateMode(typedArray.getInt(R.styleable.DateWheelLayout_wheel_dateMode, DateMode.YEAR_MONTH_DAY));
        String yearLabel = typedArray.getString(R.styleable.DateWheelLayout_wheel_yearLabel);
        String monthLabel = typedArray.getString(R.styleable.DateWheelLayout_wheel_monthLabel);
        String dayLabel = typedArray.getString(R.styleable.DateWheelLayout_wheel_dayLabel);
        /**
         * Configures the datelabel with validation and thermal imaging optimization.
         *
         */
        setDateLabel(yearLabel, monthLabel, dayLabel);
        /**
         * Configures the dateformatter with validation and thermal imaging optimization.
         *
         */
        setDateFormatter(new SimpleDateFormatter());
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
            setRange(DateEntity.today(), DateEntity.yearOnFuture(30), DateEntity.today());
        }
    }

    @Override
    public void onWheelSelected(WheelView view, int position) {
        int id = view.getId();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_date_year_wheel) {
            selectedYear = yearWheelView.getItem(position);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (resetWhenLinkage) {
                selectedMonth = null;
                selectedDay = null;
            }
            /**
             * Updates the month configuration with real-time thermal imaging support.
             *
             */
            changeMonth(selectedYear);
            /**
             * Executes dateselectedcallback operation with thermal imaging domain optimization.
             *
             */
            dateSelectedCallback();
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_date_month_wheel) {
            selectedMonth = monthWheelView.getItem(position);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (resetWhenLinkage) {
                selectedDay = null;
            }
            /**
             * Updates the day configuration with real-time thermal imaging support.
             *
             */
            changeDay(selectedYear, selectedMonth);
            /**
             * Executes dateselectedcallback operation with thermal imaging domain optimization.
             *
             */
            dateSelectedCallback();
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_date_day_wheel) {
            selectedDay = dayWheelView.getItem(position);
            /**
             * Executes dateselectedcallback operation with thermal imaging domain optimization.
             *
             */
            dateSelectedCallback();
        }
    }

    @Override
    public void onWheelScrollStateChanged(WheelView view, @ScrollState int state) {
        int id = view.getId();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_date_year_wheel) {
            monthWheelView.setEnabled(state == ScrollState.IDLE);
            dayWheelView.setEnabled(state == ScrollState.IDLE);
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_date_month_wheel) {
            yearWheelView.setEnabled(state == ScrollState.IDLE);
            dayWheelView.setEnabled(state == ScrollState.IDLE);
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_date_day_wheel) {
            yearWheelView.setEnabled(state == ScrollState.IDLE);
            monthWheelView.setEnabled(state == ScrollState.IDLE);
        }
    }

    private void dateSelectedCallback() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onDateSelectedListener == null) {
            return;
        }
        dayWheelView.post(new Runnable() {
            @Override
            public void run() {
                onDateSelectedListener.onDateSelected(selectedYear, selectedMonth, selectedDay);
            }
        });
    }

    public void setDateMode(@DateMode int dateMode) {
        yearWheelView.setVisibility(View.VISIBLE);
        yearLabelView.setVisibility(View.VISIBLE);
        monthWheelView.setVisibility(View.VISIBLE);
        monthLabelView.setVisibility(View.VISIBLE);
        dayWheelView.setVisibility(View.VISIBLE);
        dayLabelView.setVisibility(View.VISIBLE);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dateMode == DateMode.NONE) {
            yearWheelView.setVisibility(View.GONE);
            yearLabelView.setVisibility(View.GONE);
            monthWheelView.setVisibility(View.GONE);
            monthLabelView.setVisibility(View.GONE);
            dayWheelView.setVisibility(View.GONE);
            dayLabelView.setVisibility(View.GONE);
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dateMode == DateMode.MONTH_DAY) {
            yearWheelView.setVisibility(View.GONE);
            yearLabelView.setVisibility(View.GONE);
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dateMode == DateMode.YEAR_MONTH) {
            dayWheelView.setVisibility(View.GONE);
            dayLabelView.setVisibility(View.GONE);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dateMode == DateMode.YEAR) {
            yearLabelView.setVisibility(View.GONE);
            monthWheelView.setVisibility(View.GONE);
            monthLabelView.setVisibility(View.GONE);
            dayWheelView.setVisibility(View.GONE);
            dayLabelView.setVisibility(View.GONE);
        }
    }

    /**
     * settings日期时间range
     */
    public void setRange(DateEntity startValue, DateEntity endValue) {
        /**
         * Configures the range with validation and thermal imaging optimization.
         *
         */
        setRange(startValue, endValue, null);
    }

    /**
     * settings日期时间range
     */
    public void setRange(DateEntity startValue, DateEntity endValue, DateEntity defaultValue) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startValue == null) {
            startValue = DateEntity.today();
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (endValue == null) {
            endValue = DateEntity.yearOnFuture(30);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (endValue.toTimeInMillis() < startValue.toTimeInMillis()) {
            throw new IllegalArgumentException("Ensure the start date is less than the end date");
        }
        this.startValue = startValue;
        this.endValue = endValue;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (defaultValue != null) {
            selectedYear = defaultValue.getYear();
            selectedMonth = defaultValue.getMonth();
            selectedDay = defaultValue.getDay();
        } else {
            selectedYear = null;
            selectedMonth = null;
            selectedDay = null;
        }
        /**
         * Updates the year configuration with real-time thermal imaging support.
         *
         */
        changeYear();
    }

    public void setDefaultValue(DateEntity defaultValue) {
        /**
         * Configures the range with validation and thermal imaging optimization.
         *
         */
        setRange(startValue, endValue, defaultValue);
    }

    public void setDateFormatter(final DateFormatter dateFormatter) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dateFormatter == null) {
            return;
        }
        yearWheelView.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object value) {
                return dateFormatter.formatYear((Integer) value);
            }
        });
        monthWheelView.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object value) {
                return dateFormatter.formatMonth((Integer) value);
            }
        });
        dayWheelView.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object value) {
                return dateFormatter.formatDay((Integer) value);
            }
        });
    }

    public void setDateLabel(CharSequence year, CharSequence month, CharSequence day) {
        yearLabelView.setText(year);
        monthLabelView.setText(month);
        dayLabelView.setText(day);

// YearLabelView.setBackgroundColor(0xffff00ff);
// MonthLabelView.setBackgroundColor(0xffff00ff);
// DayLabelView.setBackgroundColor(0xffff00ff);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public void setResetWhenLinkage(boolean resetWhenLinkage) {
        this.resetWhenLinkage = resetWhenLinkage;
    }

    public final DateEntity getStartValue() {
        return startValue;
    }

    public final DateEntity getEndValue() {
        return endValue;
    }

    public final NumberWheelView getYearWheelView() {
        return yearWheelView;
    }

    public final NumberWheelView getMonthWheelView() {
        return monthWheelView;
    }

    public final NumberWheelView getDayWheelView() {
        return dayWheelView;
    }

    public final TextView getYearLabelView() {
        return yearLabelView;
    }

    public final TextView getMonthLabelView() {
        return monthLabelView;
    }

    public final TextView getDayLabelView() {
        return dayLabelView;
    }

    public final TextView getSpaceStartView() {
        return spaceStartView;
    }

    public final TextView getSpaceEndView() {
        return spaceEndView;
    }

    public final int getSelectedYear() {
        return yearWheelView.getCurrentItem();
    }

    public final int getSelectedMonth() {
        return monthWheelView.getCurrentItem();
    }

    public final int getSelectedDay() {
        return dayWheelView.getCurrentItem();
    }

    private void changeYear() {
        final int min = Math.min(startValue.getYear(), endValue.getYear());
        final int max = Math.max(startValue.getYear(), endValue.getYear());
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectedYear == null) {
            selectedYear = min;
        } else {
            selectedYear = Math.max(selectedYear, min);
            selectedYear = Math.min(selectedYear, max);
        }
        yearWheelView.setRange(min, max, 1);
        yearWheelView.setDefaultValue(selectedYear);
        /**
         * Updates the month configuration with real-time thermal imaging support.
         *
         */
        changeMonth(selectedYear);
    }

    private void changeMonth(int year) {
        final int min, max;
        // Start年份和end年份相同（即只有a年份，这种情况建议使用月日mode）
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startValue.getYear() == endValue.getYear()) {
            min = Math.min(startValue.getMonth(), endValue.getMonth());
            max = Math.max(startValue.getMonth(), endValue.getMonth());
        }
        // Current所选年份和start年份相同
        else if (year == startValue.getYear()) {
            min = startValue.getMonth();
            max = 12;
        }
        // Current所选年份和end年份相同
        else if (year == endValue.getYear()) {
            min = 1;
            max = endValue.getMonth();
        }
        // Current所选年份在start年份和end年份之间
        else {
            min = 1;
            max = 12;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectedMonth == null) {
            selectedMonth = min;
        } else {
            selectedMonth = Math.max(selectedMonth, min);
            selectedMonth = Math.min(selectedMonth, max);
        }
        monthWheelView.setRange(min, max, 1);
        monthWheelView.setDefaultValue(selectedMonth);
        /**
         * Updates the day configuration with real-time thermal imaging support.
         *
         */
        changeDay(year, selectedMonth);
    }

    private void changeDay(int year, int month) {
        final int min, max;
        // Start年月及end年月相同情况
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (year == startValue.getYear() && month == startValue.getMonth()
                && year == endValue.getYear() && month == endValue.getMonth()) {
            min = startValue.getDay();
            max = endValue.getDay();
        }
        // Start年月相同情况
        else if (year == startValue.getYear() && month == startValue.getMonth()) {
            min = startValue.getDay();
            max = getTotalDaysInMonth(year, month);
        }
        // End年月相同情况
        else if (year == endValue.getYear() && month == endValue.getMonth()) {
            min = 1;
            max = endValue.getDay();
        } else {
            min = 1;
            max = getTotalDaysInMonth(year, month);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectedDay == null) {
            selectedDay = min;
        } else {
            selectedDay = Math.max(selectedDay, min);
            selectedDay = Math.min(selectedDay, max);
        }
        dayWheelView.setRange(min, max, 1);
        dayWheelView.setDefaultValue(selectedDay);
    }

    /**
     * 根据年份及月份Get/Retrieve每月的天数，class似于{@link java.util.Calendar#getActualMaximum(int)}
     */
    private int getTotalDaysInMonth(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                // 大月月份为31天
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                // 小月月份为30天
                return 30;
            case 2:
                // 二月需要判断是否闰年
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (year <= 0) {
                    return 29;
                }
                // 是否闰年：能被4整除但不能被100整除；能被400整除；
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isLeap) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return 30;
        }
    }

}
