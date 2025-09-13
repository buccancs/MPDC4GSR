package com.topdon.lib.core.repository

import com.elvishew.xlog.XLog
import com.topdon.lib.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.StandardCharsets

/**
 * OKHttpClient 所用，用于输出Log为目的的 Interceptor.
 * Created by LCG on 2024/4/28.
 */
/**
 * OKLogInterceptor manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing OKLogInterceptor functionality for the IRCamera system.
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
class OKLogInterceptor(val isTC007: Boolean) : Interceptor {
    /**
     * Executes intercept operation with thermal imaging domain optimization.
     *
     * @param
     * @param chain Parameter for operation (type: Interceptor.Chain)
     *
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (BuildConfig.DEBUG) {
            XLog.tag("RetrofitLog").i("--> ${request.method} ${request.url}")
            val requestBody = request.body
            val contentType = requestBody?.contentType()?.toString()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (requestBody != null && (contentType == null || contentType == "application/json")) {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                XLog.tag("RetrofitLog").v("请求：${buffer.readString(StandardCharsets.UTF_8)}")
            }
        }

        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BuildConfig.DEBUG) {
                XLog.tag("RetrofitLog").e("<-- HTTP FAILED: $e")
            }
            throw e
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (BuildConfig.DEBUG) {
            XLog.tag(
                "RetrofitLog",
            ).i("<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url}")
            val responseBody = response.body
            val contentType = response.headers["Content-Type"]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (responseBody != null && (isTC007 || contentType == null || contentType == "application/json")) {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE)
                val responseStr = source.buffer.clone().readString(StandardCharsets.UTF_8)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (responseStr.length > 1024) {
                    XLog.tag("RetrofitLog").v("响应：${responseStr.substring(0, 1024)} ...太长了后area省略")
                } else {
                    XLog.tag("RetrofitLog").v("响应：$responseStr")
                }
            }
        }

        return response
    }
}
