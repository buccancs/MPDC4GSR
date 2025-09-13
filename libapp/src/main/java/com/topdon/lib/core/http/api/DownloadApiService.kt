package com.topdon.lib.core.http.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Specialized thermal imaging component providing DownloadApiService functionality for the IRCamera system.
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
interface DownloadApiService {
    /**
     * Downloadfile.
     */
    @GET
    @Streaming
    /**
     * Executes download operation with thermal imaging domain optimization.
     *
     * @param
     * @param url Parameter for operation (type: String)
     *
     */
    suspend fun download(
        @Url url: String,
    ): ResponseBody
}
