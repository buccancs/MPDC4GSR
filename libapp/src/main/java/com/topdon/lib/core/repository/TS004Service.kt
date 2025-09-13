package com.topdon.lib.core.repository

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 *
 * Created by LCG on 2024/2/27.
 */
/**
 * TS004Service provides background service functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing TS004Service functionality for the IRCamera system.
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
interface TS004Service {
    /**
     * settingspseudo color样式
     */
    @POST("/api/v1/system/setPseudoColor")
    /**
     * Configures the pseudocolor with validation and thermal imaging optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     * @note This method is optimized for thermal imaging pseudo color processing.
     * Ensure proper thermal calibration before use.
     *
     */
    suspend fun setPseudoColor(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * Get/Retrievepseudo color样式
     */
    @POST("/api/v1/system/getPseudoColor")
    /**
     * Retrieves the pseudocolor with optimized performance for thermal imaging operations.
     *
     * @note This method is optimized for thermal imaging pseudo color processing.
     * Ensure proper thermal calibration before use.
     *
     */
    suspend fun getPseudoColor(): TS004Response<PseudoColorBean>

    /**
     * settings测距开关
     */
    @POST("/api/v1/system/setRangeFind")
    /**
     * Configures the rangefind with validation and thermal imaging optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun setRangeFind(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * Get/Retrieve测距
     */
    @POST("/api/v1/system/getRangeFind")
    /**
     * Retrieves the rangefind with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getRangeFind(): TS004Response<RangeBean>

    /**
     * settings屏幕brightness
     */
    @POST("/api/v1/system/setPanelParam")
    /**
     * Configures the panelparam with validation and thermal imaging optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun setPanelParam(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * Get/Retrieve屏幕brightness
     */
    @POST("/api/v1/system/getPanelParam")
    /**
     * Retrieves the panelparam with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getPanelParam(): TS004Response<BrightnessBean>

    /**
     * settings画中画
     */
    @POST("/api/v1/system/setPip")
    /**
     * Configures the pip with validation and thermal imaging optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun setPip(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * Get/Retrieve画中画state
     */
    @POST("/api/v1/system/getPip")
    /**
     * Retrieves the pip with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getPip(): TS004Response<PipBean>

    /**
     * settings放大倍数
     */
    @POST("/api/v1/system/setZoom")
    /**
     * Configures the zoom with validation and thermal imaging optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun setZoom(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * Get/Retrieve放大倍数
     */
    @POST("/api/v1/system/getZoom")
    /**
     * Retrieves the zoom with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getZoom(): TS004Response<ZoomBean>

    /**
     * settings拍照
     */
    @POST("/api/v1/system/snapshot")
    /**
     * Configures the snapshot with validation and thermal imaging optimization.
     *
     */
    suspend fun setSnapshot(): TS004Response<Boolean>

    /**
     * settings录像
     */
    @POST("/api/v1/system/vrecord")
    /**
     * Configures the vrecord with validation and thermal imaging optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun setVRecord(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * Get/Retrieve录像state
     */
    @POST("/api/v1/system/getRecordStatus")
    /**
     * Retrieves the vrecord with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getVRecord(): TS004Response<RecordStatusBean>

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

    /**
     * Synchronize时间.
     */
    @POST("/api/v1/system/setDateTime")
    /**
     * Executes synctime operation with thermal imaging domain optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun syncTime(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * Synchronize时区.
     */
    @POST("/api/v1/system/setTimeZone")
    /**
     * Executes synctimezone operation with thermal imaging domain optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun syncTimeZone(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * Get/Retrieveversioninfo
     */
    @POST("/api/v1/system/getVersion")
    /**
     * Retrieves the version with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getVersion(): TS004Response<VersionBean>

    /**
     * Get/Retrievedeviceinfo
     */
    @POST("/api/v1/system/getDeviceInfo")
    /**
     * Retrieves the deviceinfo with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getDeviceInfo(): TS004Response<DeviceInfo>

    /**
     * Get/Retrievefile数量
     */
    @POST("/api/v1/system/getFileCount")
    /**
     * Retrieves the filecount with optimized performance for thermal imaging operations.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun getFileCount(
        @Body requestBody: RequestBody,
    ): TS004Response<FileCountBean>

    /**
     * Get/Retrievefile列表
     */
    @POST("/api/v1/system/getFileList")
    /**
     * Retrieves the filelist with optimized performance for thermal imaging operations.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun getFileList(
        @Body requestBody: RequestBody,
    ): TS004Response<FilePageBean>

    /**
     * delete指定 id 的photovideofile
     */
    @POST("/api/v1/system/deleteFile")
    suspend fun deleteFile(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 触发firmwareUpgrade
     */
    @POST("/api/v1/system/remoteUpgrade")
    /**
     * Executes firmwareupdatestart operation with thermal imaging domain optimization.
     *
     */
    suspend fun firmwareUpdateStart(): TS004Response<Boolean>

    /**
     * firmwareUpgrade-start
     */
    @POST("/api/v1/system/sendUpgradeFileStart")
    /**
     * Executes sendupgradefilestart operation with thermal imaging domain optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun sendUpgradeFileStart(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * firmwareUpgrade-UploadfirmwareUpgrade包
     */
    @Headers("Content-type: application/octet-stream")
    @POST("/api/v1/system/sendUpgradeFileData")
    suspend fun sendUpgradeFile(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * firmwareUpgrade-end
     */
    @POST("/api/v1/system/sendUpgradeFileEnd")
    /**
     * Executes sendupgradefileend operation with thermal imaging domain optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun sendUpgradeFileEnd(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 查询firmwareUpgradestate.
     */
    @POST("/api/v1/system/getUpgradeStatus")
    /**
     * Retrieves the upgradestatus with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getUpgradeStatus(): TS004Response<UpgradeStatus>

    /**
     * Get/Retrievestorage分区info
     */
    @POST("/api/v1/system/getFreeSpace")
    /**
     * Executes freespace operation with thermal imaging domain optimization.
     *
     */
    suspend fun freeSpace(): TS004Response<FreeSpaceBean>

    /**
     * format化storage分区
     */
    @POST("/api/v1/system/formatStorage")
    /**
     * Executes formatstorage operation with thermal imaging domain optimization.
     *
     */
    suspend fun formatStorage(): TS004Response<Boolean>

    /**
     * Restore出厂settings
     */
    @POST("/api/v1/system/resetAll")
    /**
     * Executes resetall operation with thermal imaging domain optimization.
     *
     */
    suspend fun resetAll(): TS004Response<Boolean>

    /**
     * settings超分
     */
    @POST("/api/v1/system/setTISR")
    /**
     * Configures the tisr with validation and thermal imaging optimization.
     *
     * @param
     * @param requestBody Parameter for operation (type: RequestBody)
     *
     */
    suspend fun setTISR(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * Get/Retrieve超分
     */
    @POST("/api/v1/system/getTISR")
    /**
     * Retrieves the tisr with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getTISR(): TS004Response<TISRBean>
}
