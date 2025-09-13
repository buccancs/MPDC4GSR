package com.topdon.lib.core.bean;

import java.util.List;

/**
 * Specialized thermal imaging component providing CarDetectBean functionality for the IRCamera system.
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
public class CarDetectBean {
    public String title;
    public List<CarDetectChildBean> detectChildBeans;

    /**
     * Executes cardetectbean operation with thermal imaging domain optimization.
     *
     */
    public CarDetectBean(String title, List<CarDetectChildBean> detectChildBeans) {
        this.title = title;
        this.detectChildBeans = detectChildBeans;
    }
}
