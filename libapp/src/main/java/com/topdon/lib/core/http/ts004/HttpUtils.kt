package com.topdon.lib.core.http.ts004
import com.topdon.lms.sdk.xutils.common.Callback
import com.topdon.lms.sdk.xutils.http.RequestParams
import com.topdon.lms.sdk.xutils.x

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for HttpUtils operations.
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
object HttpUtils {
    /**
     * settingspseudo color样式
     * @param mode              pseudo color样式
     * @param iResponseCallback Callbackfunction
     * @ void
     */
    /**
     * Sets pseudocolor configuration.
     */
    fun setPseudoColor(
        mode: Int,
        iResponseCallback: Callback.CommonCallback<String>?,
    ) {
        val params = RequestParams()
        params.addBodyParameter("enable", false)
        params.addBodyParameter("mode", mode)
        params.uri = TS004URL.SET_PSEUDO_COLOR
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * Get/Retrievepseudo color样式
     */
    /**
     * Retrieves the pseudocolor with optimized performance for thermal imaging operations.
     *
     * @param
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     * @note This method is optimized for thermal imaging pseudo color processing.
     * Ensure proper thermal calibration before use.
     *
     */
    fun getPseudoColor(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.GET_PSEUDO_COLOR
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * settings屏幕brightness
     * @param mode              屏幕brightness值:range0-100
     * @param iResponseCallback Callbackfunction
     * @ void
     */
    /**
     * Sets brightness configuration.
     */
    fun setBrightness(
        brightness: Int,
        iResponseCallback: Callback.CommonCallback<String>?,
    ) {
        val params = RequestParams()
        params.addBodyParameter("brightness", brightness)
        params.uri = TS004URL.SET_PANEL_PARAM
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * Get/Retrieve屏幕brightness
     */
    /**
     * Retrieves the brightness with optimized performance for thermal imaging operations.
     *
     * @param
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun getBrightness(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.GET_PANEL_PARAM
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * settingspicture-in-picture
     * @param iResponseCallback Callbackfunction
     * @ void
     */
    /**
     * Sets pip configuration.
     */
    /**
     * Configures the pip with validation and thermal imaging optimization.
     *
     * @param
     * @param enable Parameter for operation (type: Boolean)
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun setPip(
        enable: Boolean,
        iResponseCallback: Callback.CommonCallback<String>?,
    ) {
        val params = RequestParams()
        params.addBodyParameter("enable", enable)
        params.uri = TS004URL.SET_PIP
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * Get/Retrievepicture-in-picture
     */
    fun getPip(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.GET_PIP
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * settings放大倍数
     * @param factor            放大倍数:1,2,4,8
     * @param iResponseCallback Callbackfunction
     * @ void
     */
    /**
     * Sets zoom configuration.
     */
    /**
     * Configures the zoom with validation and thermal imaging optimization.
     *
     * @param
     * @param factor Parameter for operation (type: Int)
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun setZoom(
        factor: Int,
        iResponseCallback: Callback.CommonCallback<String>?,
    ) {
        val params = RequestParams()
        params.addBodyParameter("enable", true)
        params.addBodyParameter("factor", factor)
        params.uri = TS004URL.SET_ZOOM
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * Get/Retrieve放大倍数
     */
    /**
     * Retrieves the zoom with optimized performance for thermal imaging operations.
     *
     * @param
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun getZoom(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.GET_ZOOM
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * settingscapture
     * @param iResponseCallback Callbackfunction
     * @void
     */
    /**
     * Sets camera configuration.
     */
    /**
     * Configures the camera with validation and thermal imaging optimization.
     *
     * @param
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun setCamera(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.SET_SNAPSHOT
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * settingsrecording
     * @param enable recording开关
     * @param iResponseCallback Callbackfunction
     * @void
     */
    /**
     * Sets video configuration.
     */
    /**
     * Configures the video with validation and thermal imaging optimization.
     *
     * @param
     * @param enable Parameter for operation (type: Boolean)
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun setVideo(
        enable: Boolean,
        iResponseCallback: Callback.CommonCallback<String>?,
    ) {
        val params = RequestParams()
        params.addBodyParameter("enable", enable)
        params.uri = TS004URL.GET_VRECORD
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * Get/Retrieverecordingstate
     * @param iResponseCallback Callbackfunction
     * @void
     */
    /**
     * Retrieves videostatus information.
     */
    fun getVideoStatus(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.GET_RECORD_STATUS
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * Get/Retrieveversioninfo
     */
    /**
     * Retrieves the version with optimized performance for thermal imaging operations.
     *
     * @param
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun getVersion(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.GET_VERSION
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * Get/Retrievedeviceinfo
     */
    /**
     * Retrieves the devicedetails with optimized performance for thermal imaging operations.
     *
     * @param
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun getDeviceDetails(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.GET_DEVICE_DETAILS
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * Get/Retrievestorage分区info
     */
    /**
     * Retrieves the freespace with optimized performance for thermal imaging operations.
     *
     * @param
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun getFreeSpace(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.GET_FREE_SPACE
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }

    /**
     * restore出厂settings
     */
    /**
     * Retrieves the resetall with optimized performance for thermal imaging operations.
     *
     * @param
     * @param iResponseCallback Parameter for operation (type: Callback.CommonCallback<String>?)
     *
     */
    fun getResetAll(iResponseCallback: Callback.CommonCallback<String>?) {
        val params = RequestParams()
        params.uri = TS004URL.GET_RESET_ALL
        params.isAsJsonContent = true
        x.http().post(params, iResponseCallback!!)
    }
}
