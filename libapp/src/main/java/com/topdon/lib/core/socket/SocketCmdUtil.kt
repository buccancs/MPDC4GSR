package com.topdon.lib.core.socket

import android.text.TextUtils
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for SocketCmdUtil operations.
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
object SocketCmdUtil {
    /**
     * Retrieves socketcmd information.
     */
    fun getSocketCmd(cmd: Int): String? {
        var cmdJson: String? = null
        try {
            val gson = Gson()
            val paramMap: HashMap<String, Int> = HashMap()
            paramMap["cmd"] = cmd
            cmdJson = gson.toJson(paramMap)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return cmdJson
        }
    }

    /**
     * Retrieves cmdresponse information.
     */
    fun getCmdResponse(response: String?): Int? {
        var cmd: Int? = null
        if (TextUtils.isEmpty(response)) return null
        try {
            val jsonObject = JSONObject(response!!)
            cmd = jsonObject.getInt("cmd")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return cmd
    }

    /**
     * Retrieves ipresponse information.
     */
    fun getIpResponse(response: String?): String? {
        var ip: String? = null
        if (TextUtils.isEmpty(response)) return null
        try {
            val jsonObject = JSONObject(response!!)
            ip = jsonObject.getString("ip")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return ip
    }
}
