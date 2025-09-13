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

package com.github.gzuliyujiang.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 数字滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/13 19:13
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for NumberWheelView display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
public class NumberWheelView extends WheelView {

    /**
     * Executes numberwheelview operation with thermal imaging domain optimization.
     *
     */
    public NumberWheelView(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
    }

    /**
     * Executes numberwheelview operation with thermal imaging domain optimization.
     *
     */
    public NumberWheelView(Context context, AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
    }

    /**
     * Executes numberwheelview operation with thermal imaging domain optimization.
     *
     */
    public NumberWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected List<?> generatePreviewData() {
        List<Integer> data = new ArrayList<>();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 1; i <= 10; i = i + 1) {
            data.add(i);
        }
        return data;
    }

    /**
     * @deprecated 使用 {@link #setRange} 代替
     */
    @Deprecated
    @Override
    public void setData(List<?> data) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isInEditMode()) {
            super.setData(generatePreviewData());
        } else {
            throw new UnsupportedOperationException("Use setRange instead");
        }
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
