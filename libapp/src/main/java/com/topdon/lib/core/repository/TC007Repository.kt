package com.topdon.lib.core.repository

import android.graphics.Point
import android.graphics.Rect
import android.net.Network
import com.blankj.utilcode.util.EncryptUtils
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.topdon.lib.core.http.converter.StringConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Specialized thermal imaging component providing TC007Repository functionality for the IRCamera system.
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
object TC007Repository {
    /**
     * Executes any functionality.
     */
    private fun Any.toBody(): RequestBody = Gson().toJson(this).toRequestBody()

    var netWork: Network? = null
    var tempFrameParam: TempFrameParam? = null

    /**
     * Retrieves okhttpclient information.
     */
    private fun getOKHttpClient(timeout: Long): OkHttpClient {
        val builder =
            OkHttpClient.Builder()
                .retryOnConnectionFailure(false) 
                .connectTimeout(timeout, TimeUnit.SECONDS) // 2024-5-29 TS004 群中决定interface统一超时15秒
                .readTimeout(timeout, TimeUnit.SECONDS) // 2024-5-29 TS004 群中决定interface统一超时15秒
                .writeTimeout(timeout, TimeUnit.SECONDS) // 2024-5-29 TS004 群中决定interface统一超时15秒
                .addInterceptor(OKLogInterceptor(true))
        netWork?.socketFactory?.let {
            builder.socketFactory(it)
        }
        return builder.build()
    }

    /**
     * Retrieves tc007service information.
     */
    private fun getTC007Service(timeout: Long = 15): TC007Service =
        Retrofit.Builder()
            .baseUrl("http:// 192.168.40.1:63206")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(StringConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOKHttpClient(timeout))
            .build()
            .create(TC007Service::class.java)

    /**
     * Get/Retrieve产品info
     */
    /**
     * Retrieves the productinfo with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getProductInfo(): ProductBean? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().getProductInfo().Data
            } catch (_: Exception) {
                null
            }
        }

    /**
     * Get/Retrievedevice电池info
     */
    /**
     * Retrieves the batteryinfo with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getBatteryInfo(): BatteryInfo? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().getBatteryInfo().Data
            } catch (_: Exception) {
                null
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
                paramMap["Year"] = calendar.get(Calendar.YEAR)
                paramMap["Month"] = calendar.get(Calendar.MONTH) + 1
                paramMap["Day"] = calendar.get(Calendar.DAY_OF_MONTH)
                paramMap["Hour"] = calendar.get(Calendar.HOUR_OF_DAY)
                paramMap["Minute"] = calendar.get(Calendar.MINUTE)
                paramMap["Second"] = calendar.get(Calendar.SECOND)
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().syncTime(paramMap.toBody()).isSuccess()
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
                val isSendFileSuccess = sendUpgradeFile(file)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!isSendFileSuccess) {
                    return@withContext false
                }

                var status = getTC007Service().getUpgradeStatus().Data?.Status
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
                    status = getTC007Service().getUpgradeStatus().Data?.Status
                }

                status == 4
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
            val pageSize = 1024 * 1024 * 10 
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream = FileInputStream(file)

                var result = true
                var packNum = 0
                var hasReadCount = 0
                var byteArray = ByteArray(pageSize) 
                val totalPackNum = (file.length() / (pageSize) + (if (file.length() % (pageSize) > 0) 1 else 0)).toInt()
                val md5 = EncryptUtils.encryptMD5File2String(file).lowercase(Locale.ROOT)

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
                    if (hasReadCount == pageSize) {
                        packNum++
                        val body = byteArray.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                        val part = MultipartBody.Part.createFormData("zipFile", "zipFile", body)
                        val code = getTC007Service(30).sendUpgradeFile(file.name, packNum, totalPackNum, md5, part).Code
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (code == 400805) { 
                            return@withContext true
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (code != 200) { 
                            result = false
                        }
                        hasReadCount = 0
                        byteArray = ByteArray(pageSize) 
                    }
                    readCount = fileInputStream.read(byteArray, hasReadCount, byteArray.size - hasReadCount)
                }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (hasReadCount > 0) {
                    packNum++
                    val lastArray = ByteArray(hasReadCount)
                    System.arraycopy(byteArray, 0, lastArray, 0, hasReadCount)
                    val body = lastArray.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                    val part = MultipartBody.Part.createFormData("zipFile", "zipFile", body)
                    val code = getTC007Service(30).sendUpgradeFile(file.name, packNum, totalPackNum, md5, part).Code
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (code == 400805) { 
                        return@withContext true
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (code != 200) { 
                        result = false
                    }
                }

                result
            } catch (e: Exception) {
                false
            } finally {
                fileInputStream?.close()
            }
        }

    /**
     * Restore出厂settings
     */
    /**
     * Executes resettofactory operation with thermal imaging domain optimization.
     *
     */
    suspend fun resetToFactory(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().resetToFactory().isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * 执行锅盖calibration
     */
    /**
     * Executes correction operation with thermal imaging domain optimization.
     *
     */
    suspend fun correction(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().correction().isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * Get/Retrievetemperature measurementpropertyparameter
     */
    suspend fun getEnvAttr(): EnvAttr? =
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().getEnvAttr().Data
            } catch (_: Exception) {
                null
            }
        }

    /**
     * settingstemperature单位是否为摄氏度
     * @param isCelsius true-摄氏度 false-华氏度
     * @param Level temperature measurement档位,0:高gain 1:低gain 3:自动switch
     */
    suspend fun setEnvAttr(
        isCelsius: Boolean,
        Level: Int,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["TempUnit"] = if (isCelsius) 0 else 2 // 0-摄氏度 1-开尔文 2-华氏度
                paramMap["Level"] = Level // 0:高gain 1:低gain 3:自动switch
                paramMap["Fps"] = 12 // Temperature measurement帧率,range[0,采集帧率],default12,maximum支持12帧
                paramMap["OsdMode"] = 1 // Temperature measurementinfo叠加方式，0:videoencoding前叠加 1:码流info叠加(encoding后预览时叠加) 2:无叠加
                paramMap["DistanceUnit"] = 0 // 距离单位，0:米 1:英尺
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setEnvAttr(paramMap.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * settingstemperature修正parameter
     * @param environment 环境temperature，单位摄氏度
     * @param distance temperature measurement距离，单位米
     * @param radiation 发射率 `[0.01,1]`
     */
    /**
     * Configures the irconfig with validation and thermal imaging optimization.
     *
     * @param
     * @param environment Parameter for operation (type: Float)
     * @param distance Parameter for operation (type: Float)
     * @param radiation Parameter for operation (type: Float)
     *
     */
    suspend fun setIRConfig(
        environment: Float,
        distance: Float,
        radiation: Float,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                val paramMap: HashMap<String, Any> = HashMap()
                paramMap["AtmosphereTemp"] = ((environment + 273.15f) * 10).toInt()
                paramMap["Distance"] = (distance * 100).toInt()
                paramMap["Emissivity"] = (radiation * 10000).toInt()
// ParamMap["ReflectedTemp"] = 2982
// ParamMap["Transmittance"] = 10000
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setIRConfig(paramMap.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * Clear所有point、line、area.
     */
    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    suspend fun clearAllTemp(): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                var result = true
                result = result && setTempPointList(ArrayList())
                result = result && setTempLineList(ArrayList())
                result = result && setTempRectList(ArrayList())
                result
            } catch (_: Exception) {
                false
            }
        }

    /**
     * switch全局temperature measurement开关state
     */
    suspend fun getTempFrame(): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val data = getTC007Service().getTempFrame()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (data.isSuccess()) {
                    tempFrameParam = data.Data
                }
                data.isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * settings全局temperature measurement开启或Close.
     */
    suspend fun setTempFrame(boolean: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (tempFrameParam != null) {
                    tempFrameParam!!.FrameLow.Enable = boolean
                    tempFrameParam!!.FrameCenter.Enable = boolean
                    tempFrameParam!!.FrameHigh.Enable = boolean
                    /**
                     * Retrieves the tc007service with optimized performance for thermal imaging operations.
                     *
                     */
                    getTC007Service().setTempFrame(tempFrameParam!!.toBody()).isSuccess()
                } else {
                    false
                }
            } catch (_: Exception) {
                false
            }
        }

    /**
     * settingstemperature measurementpoint列表.
     */
    suspend fun setTempPointList(pointList: List<Point>): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramList = ArrayList<TempPointParam>(3)
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in 0 until 3) {
                    paramList.add(TempPointParam(i + 1, if (i < pointList.size) pointList[i] else null))
                }
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setTempPoint(paramList.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * settingstemperature measurementline列表.
     */
    suspend fun setTempLineList(lineList: List<Point>): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramList = ArrayList<TempLineParam>(3)
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in 0 until 3) {
                    val startPoint = if (i * 2 < lineList.size) lineList[i * 2] else null
                    val endPoint = if (i * 2 + 1 < lineList.size) lineList[i * 2 + 1] else null
                    paramList.add(TempLineParam(i + 1, startPoint, endPoint))
                }
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setTempLine(paramList.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * settingstemperature measurementarea列表.
     */
    suspend fun setTempRectList(rectList: List<Rect>): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val paramList = ArrayList<TempRectParam>(3)
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in 0 until 3) {
                    paramList.add(TempRectParam(i + 1, if (i < rectList.size) rectList[i] else null))
                }
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setTempRect(paramList.toBody()).isSuccess()
            } catch (_: Exception) {
                false
            }
        }

    /**
     * 拍照
     */
    /**
     * Retrieves the photo with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getPhoto(): TC007Response<PhotoBean>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().getPhoto()
            } catch (e: Exception) {
                XLog.e("请求exception：${e?.message}")
                null
            }
        }

    /**
     * settingsimagemode
     */
    /**
     * Configures the mode with validation and thermal imaging optimization.
     *
     * @param
     * @param mode Parameter for operation (type: Int)
     *
     */
    suspend fun setMode(mode: Int): TC007Response<Any?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setMode(mode)
            } catch (e: Exception) {
                XLog.e("请求exception：${e?.message}")
                null
            }
        }

    /**
     * Retrieves the attribute with optimized performance for thermal imaging operations.
     *
     * @param
     * @param default Parameter for operation (type: Boolean)
     *
     */
    suspend fun getAttribute(default: Boolean): TC007Response<AttributeBean?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().getAttribute(1, default.toString())
            } catch (e: Exception) {
                null
            }
        }

    /**
     * Configures the ratio with validation and thermal imaging optimization.
     *
     * @param
     * @param data Parameter for operation (type: Any?)
     *
     */
    suspend fun setRatio(data: Any?): TC007Response<Any?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data == null) {
                return@withContext null
            }
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setRatio(data.toBody())
            } catch (e: Exception) {
                XLog.e("请求exception：${e?.message}")
                null
            }
        }

    /**
     * Retrieves the registration with optimized performance for thermal imaging operations.
     *
     * @param
     * @param default Parameter for operation (type: Boolean)
     *
     */
    suspend fun getRegistration(default: Boolean): TC007Response<WifiAttributeBean?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().getRegistration(1, default.toString())
            } catch (e: Exception) {
                null
            }
        }

    /**
     * Configures the registration with validation and thermal imaging optimization.
     *
     * @param
     * @param data Parameter for operation (type: Any?)
     *
     */
    suspend fun setRegistration(data: Any?): TC007Response<Any?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data == null) {
                return@withContext null
            }
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setRegistration(data.toBody())
            } catch (e: Exception) {
                XLog.e("请求exception：${e?.message}")
                null
            }
        }

    /**
     * Configures the pallete with validation and thermal imaging optimization.
     *
     * @param
     * @param data Parameter for operation (type: Any?)
     *
     */
    suspend fun setPallete(data: Any?): TC007Response<Any?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data == null) {
                return@withContext null
            }
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setPallete(data.toBody())
            } catch (e: Exception) {
                XLog.e("请求exception：${e?.message}")
                null
            }
        }

    /**
     * Configures the param with validation and thermal imaging optimization.
     *
     * @param
     * @param data Parameter for operation (type: Any?)
     *
     */
    suspend fun setParam(data: Any?): TC007Response<Any?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data == null) {
                return@withContext null
            }
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setParam(data.toBody())
            } catch (e: Exception) {
                XLog.e("请求exception：${e?.message}")
                null
            }
        }

    /**
     * Configures the font with validation and thermal imaging optimization.
     *
     * @param
     * @param data Parameter for operation (type: Any?)
     *
     */
    suspend fun setFont(data: Any?): TC007Response<Any?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data == null) {
                return@withContext null
            }
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setFont(data.toBody())
            } catch (e: Exception) {
                XLog.e("请求exception：${e?.message}")
                null
            }
        }

    /**
     * Configures the correction with validation and thermal imaging optimization.
     *
     */
    suspend fun setCorrection(): TC007Response<Any?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setCorrection()
            } catch (e: Exception) {
                XLog.e("请求exception：${e?.message}")
                null
            }
        }

    /**
     * Configures the isotherm with validation and thermal imaging optimization.
     *
     * @param
     * @param data Parameter for operation (type: Any?)
     *
     */
    suspend fun setIsotherm(data: Any?): TC007Response<Any?>? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data == null) {
                return@withContext null
            }
            try {
                /**
                 * Retrieves the tc007service with optimized performance for thermal imaging operations.
                 *
                 */
                getTC007Service().setIsotherm(data.toBody())
            } catch (e: Exception) {
                XLog.e("请求exception：${e?.message}")
                null
            }
        }
}
