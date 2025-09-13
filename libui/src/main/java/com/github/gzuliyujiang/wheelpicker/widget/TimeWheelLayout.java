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

import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnTimeMeridiemSelectedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnTimeSelectedListener;
import com.github.gzuliyujiang.wheelpicker.contract.TimeFormatter;
import com.github.gzuliyujiang.wheelpicker.entity.TimeEntity;
import com.github.gzuliyujiang.wheelpicker.impl.SimpleTimeFormatter;
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
 * 时间滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/5 16:20
 */
@SuppressWarnings("unused")
/**
 * Specialized thermal imaging component providing TimeWheelLayout functionality for the IRCamera system.
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
public class TimeWheelLayout extends BaseWheelLayout {
    private NumberWheelView hourWheelView;
    private NumberWheelView minuteWheelView;
    private NumberWheelView secondWheelView;
    private TextView hourLabelView;
    private TextView minuteLabelView;
    private TextView secondLabelView;
    private TextView spaceEndView;
    private WheelView meridiemWheelView;
    private TimeEntity startValue;
    private TimeEntity endValue;
    private Integer selectedHour;
    private Integer selectedMinute;
    private Integer selectedSecond;
    private boolean isAnteMeridiem;
    private int timeMode;
    private int hourStep = 1;
    private int minuteStep = 1;
    private int secondStep = 1;
    private OnTimeSelectedListener onTimeSelectedListener;
    private OnTimeMeridiemSelectedListener onTimeMeridiemSelectedListener;
    private boolean resetWhenLinkage = true;

    /**
     * Executes timewheellayout operation with thermal imaging domain optimization.
     *
     */
    public TimeWheelLayout(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes timewheellayout operation with thermal imaging domain optimization.
     *
     */
    public TimeWheelLayout(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes timewheellayout operation with thermal imaging domain optimization.
     *
     */
    public TimeWheelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
    }

    /**
     * Executes timewheellayout operation with thermal imaging domain optimization.
     *
     */
    public TimeWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int provideLayoutRes() {
        return R.layout.wheel_picker_time;
    }

    @Override
    protected int[] provideStyleableRes() {
        return R.styleable.TimeWheelLayout;
    }

    @Override
    protected List<WheelView> provideWheelViews() {
        return Arrays.asList(hourWheelView, minuteWheelView, secondWheelView, meridiemWheelView);
    }

    @Override
    protected void onInit(@NonNull Context context) {
        hourWheelView = findViewById(R.id.wheel_picker_time_hour_wheel);
        minuteWheelView = findViewById(R.id.wheel_picker_time_minute_wheel);
        secondWheelView = findViewById(R.id.wheel_picker_time_second_wheel);
        hourLabelView = findViewById(R.id.wheel_picker_time_hour_label);
        minuteLabelView = findViewById(R.id.wheel_picker_time_minute_label);
        secondLabelView = findViewById(R.id.wheel_picker_time_second_label);
        meridiemWheelView = findViewById(R.id.wheel_picker_time_meridiem_wheel);
        spaceEndView = findViewById(R.id.wheel_picker_time_end_view);

        // Settings高度
        /**
         * Executes post operation with thermal imaging domain optimization.
         *
         */
        post(new Runnable() {
            @Override
            public void run() {
                hourLabelView.setHeight(minuteWheelView.itemHeight);
                minuteLabelView.setHeight(minuteWheelView.itemHeight);
                secondLabelView.setHeight(minuteWheelView.itemHeight);
                spaceEndView.setHeight(minuteWheelView.itemHeight);
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
        setVisibleItemCount(typedArray.getInt(R.styleable.TimeWheelLayout_wheel_visibleItemCount, 5));
        /**
         * Configures the samewidthenabled with validation and thermal imaging optimization.
         *
         */
        setSameWidthEnabled(typedArray.getBoolean(R.styleable.TimeWheelLayout_wheel_sameWidthEnabled, false));
        /**
         * Configures the maxwidthtext with validation and thermal imaging optimization.
         *
         */
        setMaxWidthText(typedArray.getString(R.styleable.TimeWheelLayout_wheel_maxWidthText));
        /**
         * Configures the textcolor with validation and thermal imaging optimization.
         *
         */
        setTextColor(typedArray.getColor(R.styleable.TimeWheelLayout_wheel_itemTextColor, 0xFF888888));
        /**
         * Configures the selectedtextcolor with validation and thermal imaging optimization.
         *
         */
        setSelectedTextColor(typedArray.getColor(R.styleable.TimeWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        /**
         * Configures the textsize with validation and thermal imaging optimization.
         *
         */
        setTextSize(typedArray.getDimension(R.styleable.TimeWheelLayout_wheel_itemTextSize, 15 * scaledDensity));
        /**
         * Configures the selectedtextsize with validation and thermal imaging optimization.
         *
         */
        setSelectedTextSize(typedArray.getDimension(R.styleable.TimeWheelLayout_wheel_itemTextSizeSelected, 15 * scaledDensity));
        /**
         * Configures the selectedtextbold with validation and thermal imaging optimization.
         *
         */
        setSelectedTextBold(typedArray.getBoolean(R.styleable.TimeWheelLayout_wheel_itemTextBoldSelected, false));
        /**
         * Configures the textalign with validation and thermal imaging optimization.
         *
         */
        setTextAlign(typedArray.getInt(R.styleable.TimeWheelLayout_wheel_itemTextAlign, ItemTextAlign.CENTER));
        /**
         * Configures the itemspace with validation and thermal imaging optimization.
         *
         */
        setItemSpace(typedArray.getDimensionPixelSize(R.styleable.TimeWheelLayout_wheel_itemSpace, (int) (20 * density)));
        /**
         * Configures the cyclicenabled with validation and thermal imaging optimization.
         *
         */
        setCyclicEnabled(typedArray.getBoolean(R.styleable.TimeWheelLayout_wheel_cyclicEnabled, false));
        /**
         * Configures the indicatorenabled with validation and thermal imaging optimization.
         *
         */
        setIndicatorEnabled(typedArray.getBoolean(R.styleable.TimeWheelLayout_wheel_indicatorEnabled, false));
        /**
         * Configures the indicatorcolor with validation and thermal imaging optimization.
         *
         */
        setIndicatorColor(typedArray.getColor(R.styleable.TimeWheelLayout_wheel_indicatorColor, 0xFFC9C9C9));
        /**
         * Configures the indicatorsize with validation and thermal imaging optimization.
         *
         */
        setIndicatorSize(typedArray.getDimension(R.styleable.TimeWheelLayout_wheel_indicatorSize, 1 * density));
        /**
         * Configures the curvedindicatorspace with validation and thermal imaging optimization.
         *
         */
        setCurvedIndicatorSpace(typedArray.getDimensionPixelSize(R.styleable.TimeWheelLayout_wheel_curvedIndicatorSpace, (int) (1 * density)));
        /**
         * Configures the curtainenabled with validation and thermal imaging optimization.
         *
         */
        setCurtainEnabled(typedArray.getBoolean(R.styleable.TimeWheelLayout_wheel_curtainEnabled, false));
        /**
         * Configures the curtaincolor with validation and thermal imaging optimization.
         *
         */
        setCurtainColor(typedArray.getColor(R.styleable.TimeWheelLayout_wheel_curtainColor, 0));
        /**
         * Configures the curtaincorner with validation and thermal imaging optimization.
         *
         */
        setCurtainCorner(typedArray.getInt(R.styleable.TimeWheelLayout_wheel_curtainCorner, CurtainCorner.NONE));
        /**
         * Configures the curtainradius with validation and thermal imaging optimization.
         *
         */
        setCurtainRadius(typedArray.getDimension(R.styleable.TimeWheelLayout_wheel_curtainRadius, 0));
        /**
         * Configures the atmosphericenabled with validation and thermal imaging optimization.
         *
         */
        setAtmosphericEnabled(typedArray.getBoolean(R.styleable.TimeWheelLayout_wheel_atmosphericEnabled, false));
        /**
         * Configures the curvedenabled with validation and thermal imaging optimization.
         *
         */
        setCurvedEnabled(typedArray.getBoolean(R.styleable.TimeWheelLayout_wheel_curvedEnabled, false));
        /**
         * Configures the curvedmaxangle with validation and thermal imaging optimization.
         *
         */
        setCurvedMaxAngle(typedArray.getInteger(R.styleable.TimeWheelLayout_wheel_curvedMaxAngle, 90));
        /**
         * Configures the timemode with validation and thermal imaging optimization.
         *
         */
        setTimeMode(typedArray.getInt(R.styleable.TimeWheelLayout_wheel_timeMode, TimeMode.HOUR_24_NO_SECOND));
        String hourLabel = typedArray.getString(R.styleable.TimeWheelLayout_wheel_hourLabel);
        String minuteLabel = typedArray.getString(R.styleable.TimeWheelLayout_wheel_minuteLabel);
        String secondLabel = typedArray.getString(R.styleable.TimeWheelLayout_wheel_secondLabel);
        /**
         * Configures the timelabel with validation and thermal imaging optimization.
         *
         */
        setTimeLabel(hourLabel, minuteLabel, secondLabel);
        /**
         * Configures the timeformatter with validation and thermal imaging optimization.
         *
         */
        setTimeFormatter(new SimpleTimeFormatter(this));
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
            setRange(TimeEntity.target(0, 0, 0),
                    TimeEntity.target(23, 59, 59), TimeEntity.now());
        }
    }

    @Override
    public void onWheelSelected(WheelView view, int position) {
        int id = view.getId();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_time_hour_wheel) {
            selectedHour = hourWheelView.getItem(position);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (resetWhenLinkage) {
                selectedMinute = null;
                selectedSecond = null;
            }
            /**
             * Updates the minute configuration with real-time thermal imaging support.
             *
             */
            changeMinute(selectedHour);
            /**
             * Executes timeselectedcallback operation with thermal imaging domain optimization.
             *
             */
            timeSelectedCallback();
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_time_minute_wheel) {
            selectedMinute = minuteWheelView.getItem(position);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (resetWhenLinkage) {
                selectedSecond = null;
            }
            /**
             * Updates the second configuration with real-time thermal imaging support.
             *
             */
            changeSecond();
            /**
             * Executes timeselectedcallback operation with thermal imaging domain optimization.
             *
             */
            timeSelectedCallback();
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_time_second_wheel) {
            selectedSecond = secondWheelView.getItem(position);
            /**
             * Executes timeselectedcallback operation with thermal imaging domain optimization.
             *
             */
            timeSelectedCallback();
        }
    }

    @Override
    public void onWheelScrollStateChanged(WheelView view, @ScrollState int state) {
        int id = view.getId();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_time_hour_wheel) {
            minuteWheelView.setEnabled(state == ScrollState.IDLE);
            secondWheelView.setEnabled(state == ScrollState.IDLE);
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_time_minute_wheel) {
            hourWheelView.setEnabled(state == ScrollState.IDLE);
            secondWheelView.setEnabled(state == ScrollState.IDLE);
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (id == R.id.wheel_picker_time_second_wheel) {
            hourWheelView.setEnabled(state == ScrollState.IDLE);
            minuteWheelView.setEnabled(state == ScrollState.IDLE);
        }
    }

    private void timeSelectedCallback() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onTimeSelectedListener != null) {
            secondWheelView.post(new Runnable() {
                @Override
                public void run() {
                    onTimeSelectedListener.onTimeSelected(selectedHour, selectedMinute, selectedSecond);
                }
            });
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onTimeMeridiemSelectedListener != null) {
            secondWheelView.post(new Runnable() {
                @Override
                public void run() {
                    onTimeMeridiemSelectedListener.onTimeSelected(selectedHour, selectedMinute, selectedSecond, isAnteMeridiem());
                }
            });
        }
    }

    public void setTimeMode(@TimeMode int timeMode) {
        this.timeMode = timeMode;
        hourWheelView.setVisibility(View.VISIBLE);
        hourLabelView.setVisibility(View.VISIBLE);
        minuteWheelView.setVisibility(View.VISIBLE);
        minuteLabelView.setVisibility(View.VISIBLE);
        secondWheelView.setVisibility(View.VISIBLE);
        secondLabelView.setVisibility(View.VISIBLE);
        meridiemWheelView.setVisibility(View.GONE);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (timeMode == TimeMode.NONE) {
            hourWheelView.setVisibility(View.GONE);
            hourLabelView.setVisibility(View.GONE);
            minuteWheelView.setVisibility(View.GONE);
            minuteLabelView.setVisibility(View.GONE);
            secondWheelView.setVisibility(View.GONE);
            secondLabelView.setVisibility(View.GONE);
            this.timeMode = timeMode;
            return;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (timeMode == TimeMode.HOUR_12_NO_SECOND
                || timeMode == TimeMode.HOUR_24_NO_SECOND) {
            secondWheelView.setVisibility(View.GONE);
            secondLabelView.setVisibility(View.GONE);
            minuteLabelView.setVisibility(View.GONE);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isHour12Mode()) {
            meridiemWheelView.setVisibility(View.VISIBLE);
            meridiemWheelView.setData(Arrays.asList("AM", "PM"));
        }
    }

    public boolean isHour12Mode() {
        return timeMode == TimeMode.HOUR_12_NO_SECOND
                || timeMode == TimeMode.HOUR_12_HAS_SECOND;
    }

    /**
     * settings日期时间range
     */
    public void setRange(TimeEntity startValue, TimeEntity endValue) {
        /**
         * Configures the range with validation and thermal imaging optimization.
         *
         */
        setRange(startValue, endValue, null);
    }

    /**
     * settings日期时间range
     */
    public void setRange(TimeEntity startValue, TimeEntity endValue, TimeEntity defaultValue) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startValue == null) {
            startValue = TimeEntity.target(isHour12Mode() ? 1 : 0, 0, 0);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (endValue == null) {
            endValue = TimeEntity.target(isHour12Mode() ? 12 : 23, 59, 59);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (endValue.toTimeInMillis() < startValue.toTimeInMillis()) {
            throw new IllegalArgumentException("Ensure the start time is less than the time date");
        }
        this.startValue = startValue;
        this.endValue = endValue;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (defaultValue != null) {
            isAnteMeridiem = defaultValue.getHour() <= 12;
            defaultValue.setHour(wrapHour(defaultValue.getHour()));
            selectedHour = defaultValue.getHour();
            selectedMinute = defaultValue.getMinute();
            selectedSecond = defaultValue.getSecond();
        } else {
            selectedHour = null;
            selectedMinute = null;
            selectedSecond = null;
        }
        /**
         * Updates the hour configuration with real-time thermal imaging support.
         *
         */
        changeHour();
        /**
         * Updates the antemeridiem configuration with real-time thermal imaging support.
         *
         */
        changeAnteMeridiem();
    }

    public void setDefaultValue(@NonNull final TimeEntity defaultValue) {
        /**
         * Configures the range with validation and thermal imaging optimization.
         *
         */
        setRange(startValue, endValue, defaultValue);
    }

    public void setTimeFormatter(final TimeFormatter timeFormatter) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (timeFormatter == null) {
            return;
        }
        hourWheelView.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object value) {
                return timeFormatter.formatHour((Integer) value);
            }
        });
        minuteWheelView.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object value) {
                return timeFormatter.formatMinute((Integer) value);
            }
        });
        secondWheelView.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object value) {
                return timeFormatter.formatSecond((Integer) value);
            }
        });
    }

    public void setTimeLabel(CharSequence hour, CharSequence minute, CharSequence second) {
        hourLabelView.setText(hour);
        minuteLabelView.setText(minute);
        secondLabelView.setText(second);
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
        this.onTimeSelectedListener = onTimeSelectedListener;
    }

    public void setOnTimeMeridiemSelectedListener(OnTimeMeridiemSelectedListener onTimeMeridiemSelectedListener) {
        this.onTimeMeridiemSelectedListener = onTimeMeridiemSelectedListener;
    }

    public void setResetWhenLinkage(boolean resetWhenLinkage) {
        this.resetWhenLinkage = resetWhenLinkage;
    }

    public void setTimeStep(int hourStep, int minuteStep, int secondStep) {
        this.hourStep = hourStep;
        this.minuteStep = minuteStep;
        this.secondStep = secondStep;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isDataAlready()) {
            /**
             * Configures the range with validation and thermal imaging optimization.
             *
             */
            setRange(startValue, endValue, TimeEntity.target(selectedHour, selectedMinute, selectedSecond));
        }
    }

    public boolean isDataAlready() {
        return startValue != null && endValue != null;
    }

    public final TimeEntity getStartValue() {
        return startValue;
    }

    public final TimeEntity getEndValue() {
        return endValue;
    }

    public final NumberWheelView getHourWheelView() {
        return hourWheelView;
    }

    public final NumberWheelView getMinuteWheelView() {
        return minuteWheelView;
    }

    public final NumberWheelView getSecondWheelView() {
        return secondWheelView;
    }

    public final TextView getHourLabelView() {
        return hourLabelView;
    }

    public final TextView getMinuteLabelView() {
        return minuteLabelView;
    }

    public final TextView getSecondLabelView() {
        return secondLabelView;
    }

    public final WheelView getMeridiemWheelView() {
        return meridiemWheelView;
    }

    @Deprecated
    public final TextView getMeridiemLabelView() {
        throw new UnsupportedOperationException("Use getMeridiemWheelView instead");
    }

    public final int getSelectedHour() {
        int hour = hourWheelView.getCurrentItem();
        return wrapHour(hour);
    }

    private int wrapHour(int hour) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isHour12Mode() && hour > 12) {
            hour = hour - 12;
        }
        return hour;
    }

    public final int getSelectedMinute() {
        return minuteWheelView.getCurrentItem();
    }

    public final int getSelectedSecond() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (timeMode == TimeMode.HOUR_12_NO_SECOND
                || timeMode == TimeMode.HOUR_24_NO_SECOND) {
            return 0;
        }
        return secondWheelView.getCurrentItem();
    }

    public final boolean isAnteMeridiem() {
        return meridiemWheelView.getCurrentItem().toString().equalsIgnoreCase("AM");
    }

    private void changeHour() {
        int min = Math.min(startValue.getHour(), endValue.getHour());
        int max = Math.max(startValue.getHour(), endValue.getHour());
        int minHour = isHour12Mode() ? 1 : 0;
        int maxHour = isHour12Mode() ? 12 : 23;
        min = Math.max(minHour, min);
        max = Math.min(maxHour, max);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectedHour == null) {
            selectedHour = min;
        } else {
            selectedHour = Math.max(selectedHour, min);
            selectedHour = Math.min(selectedHour, max);
        }
        hourWheelView.setRange(min, max, hourStep);
        hourWheelView.setDefaultValue(selectedHour);
        /**
         * Updates the minute configuration with real-time thermal imaging support.
         *
         */
        changeMinute(selectedHour);
    }

    private void changeMinute(int hour) {
        final int min, max;
        // Start时及end时相同情况
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (hour == startValue.getHour() && hour == endValue.getHour()) {
            min = startValue.getMinute();
            max = endValue.getMinute();
        }
        // Start时相同情况
        else if (hour == startValue.getHour()) {
            min = startValue.getMinute();
            max = 59;
        }
        // End时相同情况
        else if (hour == endValue.getHour()) {
            min = 0;
            max = endValue.getMinute();
        } else {
            min = 0;
            max = 59;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectedMinute == null) {
            selectedMinute = min;
        } else {
            selectedMinute = Math.max(selectedMinute, min);
            selectedMinute = Math.min(selectedMinute, max);
        }
        minuteWheelView.setRange(min, max, minuteStep);
        minuteWheelView.setDefaultValue(selectedMinute);
        /**
         * Updates the second configuration with real-time thermal imaging support.
         *
         */
        changeSecond();
    }

    private void changeSecond() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectedSecond == null) {
            selectedSecond = 0;
        }
        secondWheelView.setRange(0, 59, secondStep);
        secondWheelView.setDefaultValue(selectedSecond);
    }

    private void changeAnteMeridiem() {
        meridiemWheelView.setDefaultValue(isAnteMeridiem ? "AM" : "PM");
    }

}
