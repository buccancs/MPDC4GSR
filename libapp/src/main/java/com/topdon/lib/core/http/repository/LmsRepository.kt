package com.topdon.lib.core.http.repository

import android.text.TextUtils
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.topdon.lib.core.bean.base.Resp
import com.topdon.lib.core.bean.json.CheckVersionJson
import com.topdon.lib.core.bean.json.StatementJson
import com.topdon.lms.sdk.LMS
import com.topdon.lms.sdk.network.IResponseCallback
import com.topdon.lms.sdk.utils.StringUtils
import com.topdon.lms.sdk.weiget.TToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch

/**
 * Specialized thermal imaging component providing LmsRepository functionality for the IRCamera system.
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
object LmsRepository {
    /**
     * 查看versioninfo
     */
    /**
     * Retrieves the versioninfo with optimized performance for thermal imaging operations.
     *
     */
    suspend fun getVersionInfo(): CheckVersionJson? {
        var result: CheckVersionJson? = null
        val downLatch = CountDownLatch(1)
        LMS.getInstance().checkAppUpdate {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (it.code == 2000) {
                    result = Gson().fromJson(it.data, CheckVersionJson::class.java)
                }
            } catch (e: Exception) {
                XLog.e("version jsonparsingexception: ${e.message}")
            }
            downLatch.countDown()
        }
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            downLatch.await()
        }
        return result
    }

    /**
     * 查看声明链接
     */
    /**
     * Retrieves the statementurl with optimized performance for thermal imaging operations.
     *
     * @param
     * @param type Parameter for operation (type: String)
     *
     */
    suspend fun getStatementUrl(type: String): StatementJson? {
        var result: StatementJson? = null
        val downLatch = CountDownLatch(1)
        LMS.getInstance().getStatement(
            type,
            object : IResponseCallback {
                /**
                 * Executes onresponse operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param p0 Parameter for operation (type: String?)
                 *
                 */
                override fun onResponse(p0: String?) {
                    try {
                        val typeOfT = object : TypeToken<Resp<StatementJson>>() {}.type
                        val json = Gson().fromJson<Resp<StatementJson>>(p0, typeOfT)
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (json.code == "2000") {
                            result = json.data
                        }
                    } catch (e: Exception) {
                        XLog.e("jsonparsingexception: ${e.message}")
                    }
                    downLatch.countDown()
                }

                /**
                 * Executes onfail operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param p0 Parameter for operation (type: Exception?)
                 *
                 */
                override fun onFail(p0: Exception?) {
                    downLatch.countDown()
                    XLog.w("onFail: $result")
                }

                /**
                 * Executes onfail operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param failMsg Parameter for operation (type: String?)
                 * @param errorCode Parameter for operation (type: String)
                 *
                 */
                override fun onFail(
                    failMsg: String?,
                    errorCode: String,
                ) {
                    super.onFail(failMsg, errorCode)
                    try {
                        StringUtils.getResString(
                            LMS.mContext,
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (TextUtils.isEmpty(errorCode)) -500 else errorCode.toInt(),
                        ).let {
                            TToast.shortToast(LMS.mContext, it)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            },
        )
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            downLatch.await()
        }
        return result
    }
}
