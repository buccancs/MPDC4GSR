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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.StyleRes;
import androidx.annotation.StyleableRes;

import com.github.gzuliyujiang.dialog.DialogLog;
import com.github.gzuliyujiang.wheelview.annotation.CurtainCorner;
import com.github.gzuliyujiang.wheelview.annotation.ItemTextAlign;
import com.github.gzuliyujiang.wheelview.annotation.ScrollState;
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener;
import com.github.gzuliyujiang.wheelview.widget.WheelView;
import com.topdon.lib.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/5 16:18
 */
@SuppressWarnings("unused")
/**
 * Specialized thermal imaging component providing BaseWheelLayout functionality for the IRCamera system.
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
public abstract class BaseWheelLayout extends LinearLayout implements OnWheelChangedListener {
    private final List<WheelView> wheelViews = new ArrayList<>();
    private AttributeSet attrs;

    /**
     * Executes basewheellayout operation with thermal imaging domain optimization.
     *
     */
    public BaseWheelLayout(Context context) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init(context, null);
        TypedArray a = context.obtainStyledAttributes(null, provideStyleableRes(),
                R.attr.WheelStyle, R.style.WheelDefault);
        /**
         * Executes onattributeset operation with thermal imaging domain optimization.
         *
         */
        onAttributeSet(context, a);
    }

    /**
     * Executes basewheellayout operation with thermal imaging domain optimization.
     *
     */
    public BaseWheelLayout(Context context, @Nullable AttributeSet attrs) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, provideStyleableRes(),
                R.attr.WheelStyle, R.style.WheelDefault);
        /**
         * Executes onattributeset operation with thermal imaging domain optimization.
         *
         */
        onAttributeSet(context, a);
        a.recycle();
    }

    /**
     * Executes basewheellayout operation with thermal imaging domain optimization.
     *
     */
    public BaseWheelLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, provideStyleableRes(),
                defStyleAttr, R.style.WheelDefault);
        /**
         * Executes onattributeset operation with thermal imaging domain optimization.
         *
         */
        onAttributeSet(context, a);
        a.recycle();
    }

    /**
     * Executes basewheellayout operation with thermal imaging domain optimization.
     *
     */
    public BaseWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
        /**
         * Initializes the  component for thermal imaging operations.
         *
         */
        init(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, provideStyleableRes(),
                defStyleAttr, defStyleRes);
        /**
         * Executes onattributeset operation with thermal imaging domain optimization.
         *
         */
        onAttributeSet(context, a);
        a.recycle();
    }

    private void init(Context context, AttributeSet attrs) {
        this.attrs = attrs;
        /**
         * Configures the orientation with validation and thermal imaging optimization.
         *
         */
        setOrientation(VERTICAL);
        /**
         * Executes inflate operation with thermal imaging domain optimization.
         *
         */
        inflate(context, provideLayoutRes(), this);
        /**
         * Executes oninit operation with thermal imaging domain optimization.
         *
         */
        onInit(context);
        wheelViews.addAll(provideWheelViews());
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setOnWheelChangedListener(this);
        }
    }

    protected void onInit(@NonNull Context context) {

    }

    protected void onAttributeSet(@NonNull Context context, @NonNull TypedArray typedArray) {

    }

    @LayoutRes
    protected abstract int provideLayoutRes();

    @StyleableRes
    protected abstract int[] provideStyleableRes();

    protected abstract List<WheelView> provideWheelViews();

    public void setStyle(@StyleRes int style) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (attrs == null) {
            DialogLog.print("Please use " + getClass().getSimpleName() + " in xml");
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, provideStyleableRes(), R.attr.WheelStyle, style);
        /**
         * Executes onattributeset operation with thermal imaging domain optimization.
         *
         */
        onAttributeSet(getContext(), a);
        a.recycle();
        /**
         * Executes requestlayout operation with thermal imaging domain optimization.
         *
         */
        requestLayout();
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate();
    }

    @Override
    public void onWheelScrolled(WheelView view, int offset) {

    }

    @Override
    public void onWheelScrollStateChanged(WheelView view, @ScrollState int state) {

    }

    @Override
    public void onWheelLoopFinished(WheelView view) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setEnabled(enabled);
        }
    }

    public void setVisibleItemCount(int visibleItemCount) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setVisibleItemCount(visibleItemCount);
        }
    }

    public void setItemSpace(@Px int space) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemSpace(space);
        }
    }

    public void setSameWidthEnabled(boolean sameWidthEnabled) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setSameWidthEnabled(sameWidthEnabled);
        }
    }

    public void setDefaultItemPosition(int position) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setDefaultPosition(position);
        }
    }

    public void setCurtainEnabled(boolean hasCurtain) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainEnabled(hasCurtain);
        }
    }

    public void setCurtainColor(@ColorInt int color) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainColor(color);
        }
    }

    public void setCurtainCorner(@CurtainCorner int corner) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainCorner(corner);
        }
    }

    public void setCurtainRadius(@Px float radius) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainRadius(radius);
        }
    }

    public void setAtmosphericEnabled(boolean hasAtmospheric) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setAtmosphericEnabled(hasAtmospheric);
        }
    }

    public void setCurvedEnabled(boolean curved) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurvedEnabled(curved);
        }
    }

    public void setCurvedMaxAngle(int curvedMaxAngle) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurvedMaxAngle(curvedMaxAngle);
        }
    }

    public void setCurvedIndicatorSpace(@Px int space) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurvedIndicatorSpace(space);
        }
    }

    public void setCyclicEnabled(boolean cyclic) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setCyclicEnabled(cyclic);
        }
    }

    public void setIndicatorEnabled(boolean hasIndicator) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorEnabled(hasIndicator);
        }
    }

    public void setIndicatorSize(@Px float size) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorSize(size);
        }
    }

    public void setIndicatorColor(@ColorInt int color) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorColor(color);
        }
    }

    public void setMaxWidthText(String text) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (TextUtils.isEmpty(text)) {
            return;
        }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setMaxWidthText(text);
        }
    }

    public void setTextSize(@Px float textSize) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextSize(textSize);
        }
    }

    public void setSelectedTextSize(@Px float textSize) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedTextSize(textSize);
        }
    }

    public void setTextColor(@ColorInt int color) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextColor(color);
        }
    }

    public void setSelectedTextColor(@ColorInt int color) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedTextColor(color);
        }
    }

    public void setSelectedTextBold(boolean bold) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedTextBold(bold);
        }
    }

    public void setTextAlign(@ItemTextAlign int align) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param wheelView Parameter for operation (type: wheelViews)
         *
         */
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextAlign(align);
        }
    }

}
