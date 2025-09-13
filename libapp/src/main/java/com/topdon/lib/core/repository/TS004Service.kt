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
interface TS004Service {
    /**
     * settingspseudo color样式
     */
    @POST("/api/v1/system/setPseudoColor")
    suspend fun setPseudoColor(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 获取pseudo color样式
     */
    @POST("/api/v1/system/getPseudoColor")
    suspend fun getPseudoColor(): TS004Response<PseudoColorBean>

    /**
     * settings测距开关
     */
    @POST("/api/v1/system/setRangeFind")
    suspend fun setRangeFind(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 获取测距
     */
    @POST("/api/v1/system/getRangeFind")
    suspend fun getRangeFind(): TS004Response<RangeBean>

    /**
     * settings屏幕brightness
     */
    @POST("/api/v1/system/setPanelParam")
    suspend fun setPanelParam(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 获取屏幕brightness
     */
    @POST("/api/v1/system/getPanelParam")
    suspend fun getPanelParam(): TS004Response<BrightnessBean>

    /**
     * settings画中画
     */
    @POST("/api/v1/system/setPip")
    suspend fun setPip(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 获取画中画state
     */
    @POST("/api/v1/system/getPip")
    suspend fun getPip(): TS004Response<PipBean>

    /**
     * settings放大倍数
     */
    @POST("/api/v1/system/setZoom")
    suspend fun setZoom(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 获取放大倍数
     */
    @POST("/api/v1/system/getZoom")
    suspend fun getZoom(): TS004Response<ZoomBean>

    /**
     * settings拍照
     */
    @POST("/api/v1/system/snapshot")
    suspend fun setSnapshot(): TS004Response<Boolean>

    /**
     * settings录像
     */
    @POST("/api/v1/system/vrecord")
    suspend fun setVRecord(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 获取录像state
     */
    @POST("/api/v1/system/getRecordStatus")
    suspend fun getVRecord(): TS004Response<RecordStatusBean>

    /**
     * 下载file.
     */
    @GET
    @Streaming
    suspend fun download(
        @Url url: String,
    ): ResponseBody

    /**
     * 同步时间.
     */
    @POST("/api/v1/system/setDateTime")
    suspend fun syncTime(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 同步时区.
     */
    @POST("/api/v1/system/setTimeZone")
    suspend fun syncTimeZone(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 获取versioninfo
     */
    @POST("/api/v1/system/getVersion")
    suspend fun getVersion(): TS004Response<VersionBean>

    /**
     * 获取deviceinfo
     */
    @POST("/api/v1/system/getDeviceInfo")
    suspend fun getDeviceInfo(): TS004Response<DeviceInfo>

    /**
     * 获取file数量
     */
    @POST("/api/v1/system/getFileCount")
    suspend fun getFileCount(
        @Body requestBody: RequestBody,
    ): TS004Response<FileCountBean>

    /**
     * 获取file列表
     */
    @POST("/api/v1/system/getFileList")
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
     * 触发firmware升级
     */
    @POST("/api/v1/system/remoteUpgrade")
    suspend fun firmwareUpdateStart(): TS004Response<Boolean>

    /**
     * firmware升级-start
     */
    @POST("/api/v1/system/sendUpgradeFileStart")
    suspend fun sendUpgradeFileStart(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * firmware升级-上传firmware升级包
     */
    @Headers("Content-type: application/octet-stream")
    @POST("/api/v1/system/sendUpgradeFileData")
    suspend fun sendUpgradeFile(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * firmware升级-end
     */
    @POST("/api/v1/system/sendUpgradeFileEnd")
    suspend fun sendUpgradeFileEnd(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 查询firmware升级state.
     */
    @POST("/api/v1/system/getUpgradeStatus")
    suspend fun getUpgradeStatus(): TS004Response<UpgradeStatus>

    /**
     * 获取storage分区info
     */
    @POST("/api/v1/system/getFreeSpace")
    suspend fun freeSpace(): TS004Response<FreeSpaceBean>

    /**
     * format化storage分区
     */
    @POST("/api/v1/system/formatStorage")
    suspend fun formatStorage(): TS004Response<Boolean>

    /**
     * 恢复出厂settings
     */
    @POST("/api/v1/system/resetAll")
    suspend fun resetAll(): TS004Response<Boolean>

    /**
     * settings超分
     */
    @POST("/api/v1/system/setTISR")
    suspend fun setTISR(
        @Body requestBody: RequestBody,
    ): TS004Response<Boolean>

    /**
     * 获取超分
     */
    @POST("/api/v1/system/getTISR")
    suspend fun getTISR(): TS004Response<TISRBean>
}
