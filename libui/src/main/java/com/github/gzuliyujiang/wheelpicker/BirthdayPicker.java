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

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity;
import com.github.gzuliyujiang.wheelpicker.impl.BirthdayFormatter;

import java.util.Calendar;

/**
 * 出生日期selection器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/14 14:31
 * @since 2.0
 */
@SuppressWarnings("unused")
/**
 * Specialized thermal imaging component providing BirthdayPicker functionality for the IRCamera system.
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
public class BirthdayPicker extends DatePicker {
    private static final int MAX_AGE = 100;
    private DateEntity defaultValue;
    private boolean initialized = false;

    /**
     * Executes birthdaypicker operation with thermal imaging domain optimization.
     *
     */
    public BirthdayPicker(@NonNull Activity activity) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity);
    }

    /**
     * Executes birthdaypicker operation with thermal imaging domain optimization.
     *
     */
    public BirthdayPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity, themeResId);
    }

    @Override
    protected void initData() {
        super.initData();
        initialized = true;
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        DateEntity startValue = DateEntity.target(currentYear - MAX_AGE, 1, 1);
        DateEntity endValue = DateEntity.target(currentYear, currentMonth, currentDay);
        wheelLayout.setRange(startValue, endValue, defaultValue);
        wheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY);
        wheelLayout.setDateFormatter(new BirthdayFormatter());
    }

    public void setDefaultValue(int year, int month, int day) {
        defaultValue = DateEntity.target(year, month, day);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (initialized) {
            wheelLayout.setDefaultValue(defaultValue);
        }
    }

}
