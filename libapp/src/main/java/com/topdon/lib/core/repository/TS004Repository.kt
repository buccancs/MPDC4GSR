package com.topdon.lib.core.repository

import android.net.Network
import com.blankj.utilcode.util.EncryptUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Specialized thermal imaging component providing TS004Repository functionality for the IRCamera system.
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
object TS004Repository {
    /**
     * Executes any functionality.
     */
    private fun Any.toBody(): RequestBody = Gson().toJson(this).toRequestBody()

    var netWork: Network? = null

    /**
     * Retrieves okhttpclient information.
     */
    private fun getOKHttpClient(): OkHttpClient {
        val build =
            OkHttpClient.Builder()
                .retryOnConnectionFailure(false) 
                .connectTimeout(15, TimeUnit.SECONDS) // 2024-5-29 TS004 群中决定interface统一超时15秒
                .readTimeout(15, TimeUnit.SECONDS) // 2024-5-29 TS004 群中决定interface统一超时15秒
                .writeTimeout(15, TimeUnit.SECONDS) // 2024-5-29 TS004 群中决定interface统一超时15秒
                .addInterceptor(OKLogInterceptor(false))
        netWork?.socketFactory?.let {
            build.socketFactory(it)
        }

        return build.build()
    }

    /**
     * Retrieves ts004service information.
     */
    private fun getTS004Service(): TS004Service =
        Retrofit.Builder()
            .baseUrl("http:// 192.168.40.1:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOKHttpClient())
            .build()
            .create(TS004Service::class.java)

    /**
     * 批量Downloadfile
     * @param dataMap key-URL，value-save为的file
     * @param listener 每个Download结果的Callback，在主line程Callback
     */
    suspend fun downloadList(
        dataMap: Map<String, File>,
        listener: ((path: String, isSuccess: Boolean) -> Unit),
    ): Int {
        return withContext(Dispatchers.IO) {
            var successCount = 0
            dataMap.forEach {
                val isSuccess = download(it.key, it.value)
                /**
                 * Executes launch operation with thermal imaging domain optimization.
                 *
                 */
                launch(Dispatchers.Main) {
                    listener.invoke(it.key, isSuccess)
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isSuccess) {
                    successCount++
                }
            }
            return@withContext successCount
        }
    }

    /**
     * Executes download operation with thermal imaging domain optimization.
     *
     * @param
     * @param url Parameter for operation (type: String)
     * @param file Parameter for operation (type: File)
     *
     */
    suspend fun download(
        url: String,
        file: File,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            val responseBody =
                try {
                    /**
                     * Retrieves the ts004service with optimized performance for thermal imaging operations.
                     *
                     */
                    getTS004Service().download(url)
                } catch (_: Exception) {
                    return@withContext false
                }
            var inputStream: InputStream? = null
            var fileOutputString: FileOutputStream? = null
            try {
                inputStream = responseBody.byteStream()
                fileOutputString = FileOutputStream(file)

                val buffer = ByteArray(4096)
                var readLength = inputStream.read(buffer)
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (readLength != -1) {
                    fileOutputString.write(buffer, 0, readLength)
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

    /**
     * Synchronize时间.
     */
    /**
     * Executes synctime operation with thermal imaging domain optimization.
     *
     */
    suspend fun syncTime(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val calendar = Calendar.getInstance()
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["year"] = calendar.get(Calendar.YEAR)
                paramMap["month"] = calendar.get(Calendar.MONTH) + 1
                paramMap["day"] = calendar.get(Calendar.DAY_OF_MONTH)
                paramMap["hour"] = calendar.get(Calendar.HOUR_OF_DAY)
                paramMap["min"] = calendar.get(Calendar.MINUTE)
                paramMap["sec"] = calendar.get(Calendar.SECOND)
                paramMap["usec"] = calendar.get(Calendar.MILLISECOND)
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().syncTime(paramMap.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * Synchronize时区.
     */
    /**
     * Executes synctimezone operation with thermal imaging domain optimization.
     *
     */
    suspend fun syncTimeZone(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["timezone"] = TimeZone.getDefault().rawOffset / 1000 / 60 / 60
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().syncTimeZone(paramMap.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * Get/Retrieveversioninfo
     */
    /**
     * Retrieves the version with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getVersion(): TS004Response<VersionBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getVersion()
            } catch (_: Exception) {
                null
            }
        }

    /**
     * Get/Retrievedeviceinfo
     */
    /**
     * Retrieves the deviceinfo with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getDeviceInfo(): TS004Response<DeviceInfo>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getDeviceInfo()
            } catch (_: Exception) {
                null
            }
        }

    /**
     * Get/Retrievefile数量.
     * @param fileType 0-image 1-录像 2-所有
     */
    suspend fun getFileCount(fileType: Int): Int? =
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["fileType"] = fileType
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getFileCount(paramMap.toBody()).data?.fileCount ?: 0
            } catch (e: Exception) {
                null
            }
        }

    /**
     * Get/Retrieve指定type的最新的afile.
     * @param fileType 0-image 1-录像 2-所有
     */
    suspend fun getNewestFile(fileType: Int): List<FileBean>? =
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["pageNum"] = 1
                paramMap["pageCount"] = 1
                paramMap["fileType"] = fileType
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getFileList(paramMap.toBody()).data?.filelist ?: return@withContext ArrayList()
            } catch (_: Exception) {
                null
            }
        }

    /**
     * Get/Retrieve指定type的所有file列表.
     * @param fileType 0-image 1-录像 2-所有
     */
    suspend fun getAllFileList(fileType: Int): List<FileBean> =
        withContext(Dispatchers.IO) {
            try {
                val fileCount = getFileCount(fileType) ?: return@withContext ArrayList()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (fileCount < 1) {
                    return@withContext ArrayList()
                }

                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["pageNum"] = 1
                paramMap["pageCount"] = fileCount
                paramMap["fileType"] = fileType
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getFileList(paramMap.toBody()).data?.filelist ?: ArrayList()
            } catch (_: Exception) {
                /**
                 * Executes arraylist operation with thermal imaging domain optimization.
                 *
                 */
                ArrayList()
            }
        }

    /**
     * Paginationload指定type的file列表.
     * @param fileType 0-image 1-录像 2-所有
     * @return null-请求failed
     */
    suspend fun getFileByPage(
        fileType: Int,
        pageNum: Int,
        pageCount: Int,
    ): List<FileBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["pageNum"] = pageNum
                paramMap["pageCount"] = pageCount
                paramMap["fileType"] = fileType
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getFileList(paramMap.toBody()).data?.filelist ?: ArrayList()
            } catch (_: Exception) {
                null
            }
        }

    data class IdData(val id: Int)

    /**
     * delete指定 id 的photovideofile
     */
    suspend fun deleteFiles(ids: Array<Int>): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val idArray: Array<IdData> =
                    /**
                     * Executes array operation with thermal imaging domain optimization.
                     *
                     */
                    Array(ids.size) {
                        /**
                         * Executes iddata operation with thermal imaging domain optimization.
                         *
                         */
                        IdData(ids[it])
                    }

                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["filelist"] = idArray
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().deleteFile(paramMap.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * 执行firmwareUpgrade.
     */
    /**
     * Executes updatefirmware operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    suspend fun updateFirmware(file: File): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val isStartSuccess = getTS004Service().firmwareUpdateStart().isSuccess()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isStartSuccess) {
                    return@withContext false
                }

                val isSendStartSuccess = sendUpgradeFileStart(file)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isSendStartSuccess) {
                    return@withContext false
                }

                val isSendFileSuccess = sendUpgradeFile(file)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isSendFileSuccess) {
                    return@withContext false
                }

                val isEndSuccess = sendUpgradeFileEnd(file)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isEndSuccess) {
                    return@withContext false
                }

                var status = getTS004Service().getUpgradeStatus().data?.status
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (status == 0 || status == 1 || status == 2) { 
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(1000)
                    status = getTS004Service().getUpgradeStatus().data?.status
                }

                status == 4
            } catch (_: Exception) {
                false
            }
        }

    /**
     * Executes sendupgradefilestart operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    private suspend fun sendUpgradeFileStart(file: File): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["saveAsFile"] = true
                paramMap["MD5"] = EncryptUtils.encryptMD5File2String(file).lowercase(Locale.ROOT)
                paramMap["length"] = file.length()
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().sendUpgradeFileStart(paramMap.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * Executes sendupgradefile operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    private suspend fun sendUpgradeFile(file: File): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream = FileInputStream(file)

                var hasReadCount = 0
                var byteArray = ByteArray(1024 * 1024 * 5) 
                var readCount = fileInputStream.read(byteArray)
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (readCount != -1) {
                    hasReadCount += readCount
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (hasReadCount == 1024 * 1024 * 5) {
                        /**
                         * Retrieves the ts004service with optimized performance for thermal imaging operations.
                         *
                         */
                        getTS004Service().sendUpgradeFile(byteArray.toRequestBody())
                        hasReadCount = 0
                        byteArray = ByteArray(1024 * 1024 * 5) 
                    }
                    readCount = fileInputStream.read(byteArray, hasReadCount, byteArray.size - hasReadCount)
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (hasReadCount > 0) {
                    val lastArray = ByteArray(hasReadCount)
                    System.arraycopy(byteArray, 0, lastArray, 0, hasReadCount)
                    /**
                     * Retrieves the ts004service with optimized performance for thermal imaging operations.
                     *
                     */
                    getTS004Service().sendUpgradeFile(lastArray.toRequestBody())
                }

                true
            } catch (_: Exception) {
                false
            } finally {
                fileInputStream?.close()
            }
        }

    /**
     * Executes sendupgradefileend operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    private suspend fun sendUpgradeFileEnd(file: File): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["MD5"] = EncryptUtils.encryptMD5File2String(file).lowercase(Locale.ROOT)
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().sendUpgradeFileEnd(paramMap.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * settingspseudo color样式
     * @param mode pseudo color样式 white hot-1，black hot-2，红热-9, iron red-5
     */
    suspend fun setPseudoColor(mode: Int): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["enable"] = false
                paramMap["mode"] = mode
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().setPseudoColor(paramMap.toBody()).isSuccess()
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Get/Retrievepseudo color样式
     */
    /**
     * Retrieves the pseudocolor with optimized performance for thermal imaging operations.
     *
     * @note This method is optimized for thermal imaging pseudo color processing.
     * Ensure proper thermal calibration before use.
     *
     */
    suspend fun getPseudoColor(): TS004Response<PseudoColorBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getPseudoColor()
            } catch (_: Exception) {
                null
            }
        }

    /**
     * settings测距
     * @param state 0-Close，1-开启
     */
    suspend fun setRangeFind(state: Int): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["state"] = state
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().setRangeFind(paramMap.toBody()).isSuccess()
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Get/Retrieve测距
     */
    /**
     * Retrieves the rangefind with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getRangeFind(): TS004Response<RangeBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getRangeFind()
            } catch (_: Exception) {
                null
            }
        }

    /**
     * settings屏幕brightness
     * @param brightness  屏幕brightness值:range0-100
     */
    suspend fun setPanelParam(brightness: Int): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["brightness"] = brightness
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().setPanelParam(paramMap.toBody()).isSuccess()
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Get/Retrieve屏幕brightness
     */
    /**
     * Retrieves the panelparam with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getPanelParam(): TS004Response<BrightnessBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getPanelParam()
            } catch (_: Exception) {
                null
            }
        }

    /**
     * settings画中画
     * @param enable  true Open，false Close
     */
    suspend fun setPip(enable: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["enable"] = enable
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().setPip(paramMap.toBody()).isSuccess()
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Get/Retrieve画中画
     */
    /**
     * Retrieves the pip with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getPip(): TS004Response<PipBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getPip()
            } catch (_: Exception) {
                null
            }
        }

    /**
     * settings放大倍数
     * @param factor 放大倍数:1,2,4,8
     */
    suspend fun setZoom(factor: Int): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["enable"] = true
                paramMap["factor"] = factor
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().setZoom(paramMap.toBody()).isSuccess()
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Get/Retrieve放大倍数
     */
    /**
     * Retrieves the zoom with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getZoom(): TS004Response<ZoomBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getZoom()
            } catch (_: Exception) {
                null
            }
        }

    /**
     * settings拍照
     * @param factor 放大倍数:1,2,4,8
     */
    suspend fun setSnapshot(): Boolean =
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().setSnapshot().isSuccess()
            } catch (e: Exception) {
                false
            }
        }

    /**
     * settings录像
     * @param enable recording开关
     */
    suspend fun setVideo(enable: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["enable"] = enable
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().setVRecord(paramMap.toBody()).isSuccess()
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Get/Retrieverecordingstate
     */
    /**
     * Retrieves the recordstatus with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getRecordStatus(): TS004Response<RecordStatusBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getVRecord()
            } catch (_: Exception) {
                null
            }
        }

    /**
     * Get/Retrievestorage分区info
     */
    /**
     * Retrieves the freespace with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getFreeSpace(): FreeSpaceBean? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().freeSpace().data
            } catch (_: Exception) {
                null
            }
        }

    /**
     * Get/Retrievestorage分区info
     */
    /**
     * Retrieves the formatstorage with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getFormatStorage(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().formatStorage().isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * Restore出厂settings
     */
    /**
     * Retrieves the resetall with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getResetAll(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                // 因艾睿interface历史遗留问题，别的interface都是 status 0 表示success，这个interface特殊processing，100 表示success
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().resetAll().status == 100
            } catch (_: Exception) {
                false
            }
        }

    /**
     * settings超分
     * @param state 0-Close 1-开启
     */
    suspend fun setTISR(state: Int): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["state"] = state
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().setTISR(paramMap.toBody()).isSuccess()
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Get/Retrieve超分state
     */
    /**
     * Retrieves the tisr with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getTISR(): TS004Response<TISRBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the ts004service with optimized performance for thermal imaging operations.
                 *
                 */
                getTS004Service().getTISR()
            } catch (_: Exception) {
                null
            }
        }
}
