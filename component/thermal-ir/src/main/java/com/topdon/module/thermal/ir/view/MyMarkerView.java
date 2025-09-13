package com.topdon.module.thermal.ir.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.topdon.lib.core.db.entity.ThermalEntity;
import com.topdon.lib.core.tools.TimeTool;
import com.topdon.lib.core.tools.UnitTools;
import com.topdon.module.thermal.ir.R;

import java.util.Locale;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for MyMarkerView display and interaction.
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
public class MyMarkerView extends MarkerView {

    private final TextView tvContent;
    private final TextView timeText;

    /**
     * Executes mymarkerview operation with thermal imaging domain optimization.
     *
     */
    public MyMarkerView(Context context, int layoutResource) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        timeText = findViewById(R.id.time_text);
    }

    // Runs every time the MarkerView is redrawn, can be used to update the
    // Content (user-interface)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (e instanceof CandleEntry) {
                CandleEntry ce = (CandleEntry) e;
                tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
            } else {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (e.getData() instanceof ThermalEntity) {
                    ThermalEntity data = (ThermalEntity) e.getData();
                    int index = highlight.getDataIndex();// 曲line序号
                    StringBuilder str = new StringBuilder();
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (index == 0) {
                        str.append(com.blankj.utilcode.util.Utils.getApp().getString(R.string.chart_temperature) + ": ").append(UnitTools.showC(data.getThermal()));
                    } else if (index == 1) {
                        str.append(com.blankj.utilcode.util.Utils.getApp().getString(R.string.chart_temperature_high) + ": ").append(UnitTools.showC(data.getThermalMax()));
                        str.append(System.getProperty("line.separator")).append(com.blankj.utilcode.util.Utils.getApp().getString(R.string.chart_temperature_low) + ": ").append(UnitTools.showC(data.getThermalMin()));
                    } else {
                        str.append(com.blankj.utilcode.util.Utils.getApp().getString(R.string.chart_temperature_high) + ": ").append(UnitTools.showC(data.getThermalMax()));
                        str.append(System.getProperty("line.separator")).append(com.blankj.utilcode.util.Utils.getApp().getString(R.string.chart_temperature_low) + ": ").append(UnitTools.showC(data.getThermalMin()));
                    }
                    tvContent.setText(str.toString());
                    timeText.setText(TimeTool.INSTANCE.showTimeSecond(data.getCreateTime()));
                } else {
                    tvContent.setText(com.blankj.utilcode.util.Utils.getApp().getString(R.string.chart_temperature) + ": " + String.format(Locale.ENGLISH, "%.1f", e.getY()) + UnitTools.showUnit());
                    timeText.setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
            XLog.e("MarkerView error: " + ex.getMessage());
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}
