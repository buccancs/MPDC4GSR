package com.topdon.module.thermal.ir.report.viewmodel

import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.TimeUtils
import com.elvishew.xlog.XLog
import com.topdon.lib.core.ktbase.BaseViewModel
import com.topdon.lib.core.utils.SingleLiveEvent
import com.topdon.lms.sdk.LMS
import com.topdon.lms.sdk.UrlConstant
import com.topdon.lms.sdk.bean.CommonBean
import com.topdon.lms.sdk.network.HttpProxy
import com.topdon.lms.sdk.network.IResponseCallback
import com.topdon.lms.sdk.network.ResponseBean
import com.topdon.lms.sdk.xutils.http.RequestParams
import com.topdon.module.thermal.ir.report.bean.ReportBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.util.concurrent.CountDownLatch

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for UpReportViewModel display and interaction.
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
class UpReportViewModel : BaseViewModel() {
    val commonBeanLD = SingleLiveEvent<CommonBean>()

    val exceptionLD = SingleLiveEvent<Exception?>()

    /**
     * Executes upload functionality.
     */
    /**
     * Executes upload operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     * @param reportBean Parameter for operation (type: ReportBean?)
     *
     */
    fun upload(
        isTC007: Boolean,
        reportBean: ReportBean?,
    ) {
        viewModelScope.launch {
            /**
             * Executes uploadimages operation with thermal imaging domain optimization.
             *
             */
            uploadImages(reportBean)
            /**
             * Executes uploadjson operation with thermal imaging domain optimization.
             *
             */
            uploadJSON(isTC007, reportBean)
        }
    }

    /**
     * Executes uploadimages operation with thermal imaging domain optimization.
     *
     * @param
     * @param reportBean Parameter for operation (type: ReportBean?)
     *
     */
    private suspend fun uploadImages(reportBean: ReportBean?) {
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            val irList = reportBean?.infrared_data
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (irList != null) {
                val downLatch = CountDownLatch(irList.size)
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (reportIrBean in irList) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (reportIrBean.picture_id.isNotEmpty()) {
                        downLatch.countDown()
                        continue
                    }
                    val file = File(reportIrBean.picture_url)
                    LMS.getInstance().uploadFile(file, 0, 13, 20) {
                        XLog.i(it?.data)
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (it?.code == LMS.SUCCESS) {
                            file.delete()
                            val jsonObject = JSONObject(it.data)
                            reportIrBean.picture_id = jsonObject.getString("fileSecret")
                            reportIrBean.picture_url = jsonObject.getString("url")
                        }
                        XLog.i("Upload完一张图")
                        downLatch.countDown()
                    }
                }
                downLatch.await()
                XLog.i("${irList.size} 张图Upload完毕")
            }
        }
    }

    /**
     * Executes uploadjson operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     * @param reportBean Parameter for operation (type: ReportBean?)
     *
     */
    private suspend fun uploadJSON(
        isTC007: Boolean,
        reportBean: ReportBean?,
    ) {
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            val url = UrlConstant.BASE_URL + "api/v1/outProduce/testReport/addTestReport"
            val params = RequestParams()
            params.addBodyParameter("reportType", 2)
            params.addBodyParameter("modelId", if (isTC007) 1783 else 950) // TC001-950, TC002-951, TC003-952 TC007-1783
            params.addBodyParameter("testTime", TimeUtils.getNowString())
            params.addBodyParameter("testInfo", GsonUtils.toJson(reportBean))
            params.addBodyParameter("sn", "")
            HttpProxy.instant.post(
                url,
                params,
                object : IResponseCallback {
                    /**
                     * Executes onresponse operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param response Parameter for operation (type: String?)
                     *
                     */
                    override fun onResponse(response: String?) {
                        commonBeanLD.postValue(ResponseBean.convertCommonBean(response, null))
                    }

                    /**
                     * Executes onfail operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param exception Parameter for operation (type: Exception?)
                     *
                     */
                    override fun onFail(exception: Exception?) {
                        exceptionLD.postValue(exception)
                    }
                },
            )
        }
    }
}
