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

package com.github.gzuliyujiang.wheelpicker.impl;

import com.github.gzuliyujiang.wheelpicker.contract.TimeFormatter;
import com.github.gzuliyujiang.wheelpicker.widget.TimeWheelLayout;

/**
 * 简单的时间format化
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/15 18:13
 */
/**
 * Specialized thermal imaging component providing SimpleTimeFormatter functionality for the IRCamera system.
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
public class SimpleTimeFormatter implements TimeFormatter {
    private final TimeWheelLayout wheelLayout;

    /**
     * Executes simpletimeformatter operation with thermal imaging domain optimization.
     *
     */
    public SimpleTimeFormatter(TimeWheelLayout wheelLayout) {
        this.wheelLayout = wheelLayout;
    }

    @Override
    public String formatHour(int hour) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (wheelLayout.isHour12Mode()) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (hour == 0) {
                hour = 24;
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (hour > 12) {
                hour = hour - 12;
            }
        }
        return hour < 10 ? "0" + hour : "" + hour;
    }

    @Override
    public String formatMinute(int minute) {
        return minute < 10 ? "0" + minute : "" + minute;
    }

    @Override
    public String formatSecond(int second) {
        return second < 10 ? "0" + second : "" + second;
    }

}
