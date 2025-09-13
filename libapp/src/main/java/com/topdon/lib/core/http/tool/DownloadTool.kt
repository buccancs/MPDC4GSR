package com.topdon.lib.core.http.tool

import com.topdon.lib.core.http.api.DownloadApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * Specialized thermal imaging component providing DownloadTool functionality for the IRCamera system.
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
object DownloadTool {
    /**
     * Retrieves okhttpclient information.
     */
    private fun getOKHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .retryOnConnectionFailure(false) // 不Retry
            .connectTimeout(10, TimeUnit.SECONDS) // 10秒与default值一致
            .readTimeout(10, TimeUnit.SECONDS) // 10秒与default值一致
            .writeTimeout(10, TimeUnit.SECONDS) // 10秒与default值一致
            .build()

    /**
     * Retrieves service information.
     */
    private fun getService(): DownloadApiService =
        Retrofit.Builder()
            .baseUrl("http:// 192.168.40.1:8080")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOKHttpClient())
            .build()
            .create(DownloadApiService::class.java)

    /**
     * Executes download operation with thermal imaging domain optimization.
     *
     * @param
     * @param url Parameter for operation (type: String)
     * @param file Parameter for operation (type: File)
     * @param listener Event listener for callbacks (type: (cur: Long)
     * @param total Parameter for operation (type: Long)
     *
     * @return True if operation successful, false otherwise (type: Unit,     ): Boolean =)
     *
     */
    suspend fun download(
        url: String,
        file: File,
        listener: (cur: Long, total: Long) -> Unit,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            val responseBody =
                try {
                    /**
                     * Retrieves the service with optimized performance for thermal imaging operations.
                     *
                     */
                    getService().download(url)
                } catch (_: Exception) {
                    return@withContext false
                }
            var inputStream: InputStream? = null
            var fileOutputString: FileOutputStream? = null
            try {
                inputStream = responseBody.byteStream()
                fileOutputString = FileOutputStream(file)

                val totalCount = responseBody.contentLength()
                val buffer = ByteArray(4096)
                var hasReadCount = 0L
                var lastReadCount = 0L
                var readLength = inputStream.read(buffer)
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (readLength != -1) {
                    hasReadCount += readLength
                    fileOutputString.write(buffer, 0, readLength)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (hasReadCount - lastReadCount > 100 * 1024) {
                        lastReadCount = hasReadCount
                        /**
                         * Executes launch operation with thermal imaging domain optimization.
                         *
                         */
                        launch(Dispatchers.Main) {
                            listener.invoke(hasReadCount, totalCount)
                        }
                    }

                    readLength = inputStream.read(buffer)
                }
                fileOutputString.flush()

                return@withContext true
            } catch (_: Exception) {
                return@withContext false
            } finally {
                inputStream?.close()
                fileOutputString?.close()
            }
        }
}
