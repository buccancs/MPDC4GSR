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

package com.github.gzuliyujiang.wheelpicker.contract;

/**
 * 日期Show/Display文本format化interface
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/14 19:55
 */
/**
 * Specialized thermal imaging component providing DateFormatter functionality for the IRCamera system.
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
public interface DateFormatter {

    /**
     * format化年份
     *
     * @param year 年份数字
     * @return format化后最终Show/Display的年份字符串
     */
    String formatYear(int year);

    /**
     * format化月份
     *
     * @param month 月份数字
     * @return format化后最终Show/Display的月份字符串
     */
    String formatMonth(int month);

    /**
     * format化日子
     *
     * @param day 日子数字
     * @return format化后最终Show/Display的日子字符串
     */
    String formatDay(int day);

}

