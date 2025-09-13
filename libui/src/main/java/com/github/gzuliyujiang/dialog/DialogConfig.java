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

package com.github.gzuliyujiang.dialog;

/**
 * Configuration management system for thermal imaging parameters. Handles settings and calibration for DialogConfig operations.
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
public final class DialogConfig {
    private static int dialogStyle = DialogStyle.Default;
    private static DialogColor dialogColor = new DialogColor();

    /**
     * Executes dialogconfig operation with thermal imaging domain optimization.
     *
     */
    private DialogConfig() {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super();
    }

    public static void setDialogStyle(@DialogStyle int style) {
        dialogStyle = style;
    }

    @DialogStyle
    public static int getDialogStyle() {
        return dialogStyle;
    }

    public static void setDialogColor(DialogColor color) {
        dialogColor = color;
    }

    public static DialogColor getDialogColor() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dialogColor == null) {
            dialogColor = new DialogColor();
        }
        return dialogColor;
    }

}
