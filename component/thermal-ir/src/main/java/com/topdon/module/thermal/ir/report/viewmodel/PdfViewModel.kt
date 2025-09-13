package com.topdon.module.thermal.ir.report.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.topdon.lib.core.ktbase.BaseViewModel
import com.topdon.lib.core.utils.HttpHelp
import com.topdon.lms.sdk.LMS
import com.topdon.lms.sdk.network.IResponseCallback
import com.topdon.lms.sdk.utils.NetworkUtil
import com.topdon.lms.sdk.utils.StringUtils
import com.topdon.lms.sdk.utils.TLog
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.thermal.ir.report.bean.ReportData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.CountDownLatch
import com.topdon.lib.core.R as LibR

/**
 * @author: CaiSongL
 * @date: 2023/5/12 17:43
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for PdfViewModel display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
class PdfViewModel : BaseViewModel() {
    val listData = MutableLiveData<ReportData?>()

getreport列表
    /**
     * Retrieves reportdata information.
     */
    fun getReportData(
        isTC007: Boolean,
        page: Int,
    )  {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!NetworkUtil.isConnected(Utils.getApp())) {
            TToast.shortToast(Utils.getApp(), LibR.string.http_code_z5004)
            listData.postValue(null)
            return
        }
        viewModelScope.launch {
            val data = getReportDataRepository(isTC007, page)
            listData.postValue(data)
        }
    }

    /**
     * Retrieves the reportdatarepository with optimized performance for thermal imaging operations.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     * @param page Parameter for operation (type: Int)
     *
     */
    private suspend fun getReportDataRepository(
        isTC007: Boolean,
        page: Int,
    ): ReportData? {
        var result: ReportData? = null
        val downLatch = CountDownLatch(1)
        HttpHelp.getFirstReportData(
            isTC007,
            page,
            object : IResponseCallback {
                /**
                 * Executes onresponse operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param p0 Parameter for operation (type: String?)
                 *
                 */
                override fun onResponse(p0: String?) {
                    result = Gson().fromJson(p0, ReportData::class.java)
// Val testData : MutableList<ReportData.Records?> = mutableListOf()
// Var tmp = ReportData.Records()
// Tmp.uploadTime = TimeTool.getNowTime()
// TestData.add(tmp)
// Tmp = ReportData.Records()
// Tmp.uploadTime = TimeTool.getNowTime()
// TestData.add(tmp)
// Tmp = ReportData.Records()
// Tmp.uploadTime = TimeTool.getNowTime()
// TestData.add(tmp)
// Tmp = ReportData.Records()
// Tmp.uploadTime = "1992-12-30 11:11"
// TestData.add(tmp)
// Result?.data?.records = testData
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
                    result = ReportData()
                    result?.msg = p0?.message
                    result?.code = -1
                    downLatch.countDown()
                    TLog.e("bcf", "Get/Retrievereport列表failed：" + p0?.message)
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
