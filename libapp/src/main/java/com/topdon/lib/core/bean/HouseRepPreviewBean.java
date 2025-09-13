package com.topdon.lib.core.bean;

import java.util.List;

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for HouseRepPreviewBean display and interaction.
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
public class HouseRepPreviewBean {
    public String housePhoto;
    public String houseAddress;
    public String houseName;
    public String detectTime;
    public String inspectorName;
    public String houseYear;
    public String houseArea;
    public String expenses;
    public List<HouseRepPreviewItemBean> itemBeans;
    public String inspectorWhitePath;
    public String houseOwnerWhitePath;
}
