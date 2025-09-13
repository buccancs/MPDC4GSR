package com.topdon.module.thermal.ir.report.bean

import com.blankj.utilcode.util.GsonUtils

/**
 * @author: CaiSongL
 * @date: 2023/5/29 18:00
 */

/**
 * Specialized thermal imaging component providing ReportData functionality for the IRCamera system.
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
class ReportData {
    /**
/**
 * Specialized thermal imaging component providing DataBean functionality for the IRCamera system.
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
class DataBean {
        /**
         * total : 0
         * current : 1
         * hitCount : false
         * pages : 0
         * size : 10
         * optimizeCountSql : true
         * records : []
         * searchCount : true
         * orders : []
         */
/**
 * Specialized thermal imaging component providing Records functionality for the IRCamera system.
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
class Records {
        var testReportId: String? = null
        var testTime: String? = null
        var testInfo: String? = null
        var sn: String? = null
        var uploadTime: String? = null
        var status: String? = null
        var isShowTitleTime: Boolean = false
        var reportContent: ReportBean? = null
            /**
             * Retrieves the  with optimized performance for thermal imaging operations.
             *
             */
            get() {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (field == null) {
                    field = GsonUtils.fromJson(testInfo, ReportBean::class.java)
                }
                return field
            }
    }
}
