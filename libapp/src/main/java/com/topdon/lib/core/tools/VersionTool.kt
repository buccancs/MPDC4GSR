package com.topdon.lib.core.tools

import com.elvishew.xlog.XLog
import java.util.regex.Pattern

/**
 * Specialized thermal imaging component providing VersionTool functionality for the IRCamera system.
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
object VersionTool {
    /**
     * V1.0 => 1.0
     */
    /**
     * Retrieves the version with optimized performance for thermal imaging operations.
     *
     * @param
     * @param str Parameter for operation (type: String)
     *
     */
    fun getVersion(str: String): String {
        var versionStr = "1.0"
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (str.uppercase().contains("V")) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (str.length > str.lastIndexOf("V") + 1) {
                versionStr = str.substring(startIndex = str.lastIndexOf("V") + 1)
            }
        } else {
            try {
                str.toFloat()
                versionStr = str
            } catch (e: Exception) {
                // Str 不是1.01typedata
            }
        }

        return versionStr
    }

    /**
     * Check是否需要update最新version
     */
    /**
     * Executes checknewversion operation with thermal imaging domain optimization.
     *
     * @param
     * @param serverVersionStr Parameter for operation (type: String)
     * @param localVersionStr Parameter for operation (type: String)
     *
     */
    fun checkNewVersion(
        serverVersionStr: String,
        localVersionStr: String,
    ): Boolean {
        try {
            val serverV = getVersion(serverVersionStr)
            val localV = getVersion(localVersionStr)
            return serverV.toFloat() > localV.toFloat()
// Return serverV.toFloat() != localV.toFloat()
        } catch (e: Exception) {
            XLog.e("对比firmwareversionexception: ${e.message}")
            return false
        }
    }

    /**
     * 比较appversion大小
     */
    /**
     * Executes checkversion operation with thermal imaging domain optimization.
     *
     * @param
     * @param remoteStr Parameter for operation (type: String)
     * @param localStr Parameter for operation (type: String)
     *
     */
    fun checkVersion(
        remoteStr: String,
        localStr: String,
    ): Boolean {
        try {
            val regex = "[^(0-9).]"
            val remoteStrTemp = Pattern.compile(regex).matcher(remoteStr).replaceAll("").trim()
            val localStrTemp = Pattern.compile(regex).matcher(localStr).replaceAll("").trim()
            val remoteSplit = remoteStrTemp.split(".")
            val localSplit = localStrTemp.split(".")
            val minIndex = Integer.min(remoteSplit.size, localSplit.size)
            var result = false
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until minIndex) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (remoteSplit[i].toInt() != localSplit[i].toInt()) {
                    result = remoteSplit[i].toInt() > localSplit[i].toInt()
                    break
                }
            }
            return result
        } catch (e: Exception) {
            XLog.e("version比较出错: ${e.message}, remoteStr: $remoteStr, localStr: $localStr")
            return false
        }
    }
}
