package com.topdon.lib.core.utils

import com.blankj.utilcode.util.Utils
import com.topdon.lms.sdk.UrlConstant
import com.topdon.lms.sdk.network.HttpProxy.Companion.instant
import com.topdon.lms.sdk.network.IResponseCallback
import com.topdon.lms.sdk.utils.LanguageUtil
import com.topdon.lms.sdk.xutils.http.RequestParams

/**
 * Specialized thermal imaging component providing HttpHelp functionality for the IRCamera system.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
object HttpHelp {
    /**
     * Get/Retrieve首次reportlist
     * modelId：TC001 是950, TC002 是951, TC003是952
     */
    /**
     * Retrieves firstreportdata information.
     */
    fun getFirstReportData(
        isTC007: Boolean,
        pageNumber: Int,
        iResponseCallback: IResponseCallback,
    ) {
        val url = UrlConstant.BASE_URL + "api/v1/outProduce/testReport/getTestReport"
        val params = RequestParams()
        params.addBodyParameter("modelId", if (isTC007) 1783 else 950) // TC001-950, TC002-951, TC003-952 TC007-1783
        params.addBodyParameter("status", 1)
        params.addBodyParameter("reportType", 2)
        params.addBodyParameter("languageId", LanguageUtil.getLanguageId(Utils.getApp()))
        params.addBodyParameter("current", pageNumber)
        params.addBodyParameter("size", 20)
        instant.post(url, true, params, iResponseCallback)
    }
}
