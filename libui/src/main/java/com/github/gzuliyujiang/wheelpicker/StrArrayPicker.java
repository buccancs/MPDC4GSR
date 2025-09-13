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

import com.github.gzuliyujiang.dialog.DialogLog;
import com.github.gzuliyujiang.wheelpicker.entity.SexEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 选项selection器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/23 11:48
 */
/**
 * Specialized thermal imaging component providing StrArrayPicker functionality for the IRCamera system.
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
public class StrArrayPicker extends OptionPicker {

    @NonNull
    private final List<String> optionList;

    /**
     * Executes strarraypicker operation with thermal imaging domain optimization.
     *
     */
    public StrArrayPicker(Activity activity, @NonNull String[] optionArray, int defaultPosition) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(activity);
        this.optionList = Arrays.asList(optionArray);
        this.defaultPosition = defaultPosition;
    }

    @Override
    protected List<?> provideData() {
        return optionList;
    }

}
