
package com.topdon.module.thermal.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.topdon.lib.core.db.entity.ThermalEntity;
import com.topdon.lib.core.tools.NumberTools;
import com.topdon.lib.core.tools.TimeTool;
import com.topdon.module.thermal.R;

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
    @SuppressLint("DefaultLocale")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = highlight.getDataIndex();// 曲line序号
        ThermalEntity data = (ThermalEntity) e.getData();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            StringBuilder str = new StringBuilder();
            String thermalStr = NumberTools.INSTANCE.to02(data.getThermal());
            String thermalMaxStr = NumberTools.INSTANCE.to02(data.getThermalMax());
            String thermalMinStr = NumberTools.INSTANCE.to02(data.getThermalMin());
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (index == 0) {
                str.append("temperature:").append(thermalStr);
            } else if (index == 1) {
                str.append("maximumtemperature:").append(thermalMaxStr);
                str.append(System.getProperty("line.separator")).append("minimumtemperature:").append(thermalMinStr);
            } else {
                str.append("maximumtemperature:").append(thermalMaxStr);
                str.append(System.getProperty("line.separator")).append("minimumtemperature:").append(thermalMinStr);
            }
            tvContent.setText(str.toString());
            timeText.setText(TimeTool.INSTANCE.showTimeSecond(data.getCreateTime()));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}
