package com.topdon.lib.core.bean;

import com.topdon.lib.core.utils.TemperatureUtil;

/**
 * Specialized thermal imaging component providing CarDetectChildBean functionality for the IRCamera system.
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
public class CarDetectChildBean {
    public int type;
    public int pos;
    public String description;
    public String item;
    public String temperature;
    public boolean isSelected;

    /**
     * Executes cardetectchildbean operation with thermal imaging domain optimization.
     *
     */
    public CarDetectChildBean(int type,int pos,String description, String item, String temperature) {
        this.type = type;
        this.pos = pos;
        this.description = description;
        this.item = item;
        this.temperature = temperature;
    }

    public String buildString() {
        String[] temperatures = temperature.split("~");
        return item + TemperatureUtil.INSTANCE.getTempStr(Integer.parseInt(temperatures[0]), Integer.parseInt(temperatures[1]));
    }
}
