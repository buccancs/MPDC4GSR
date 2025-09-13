package com.topdon.module.thermal.ir.report.activity

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.Utils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseViewModelActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.socket.WebSocketProxy
import com.topdon.lib.core.utils.NetWorkUtils
import com.topdon.lib.core.view.TitleView
import com.topdon.libcom.view.CommLoadMoreView
import com.topdon.lms.sdk.LMS
import com.topdon.lms.sdk.UrlConstant
import com.topdon.lms.sdk.network.HttpProxy
import com.topdon.lms.sdk.network.IResponseCallback
import com.topdon.lms.sdk.utils.LanguageUtil
import com.topdon.lms.sdk.utils.StringUtils
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.lms.sdk.xutils.http.RequestParams
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.adapter.PDFAdapter
import com.topdon.module.thermal.ir.report.viewmodel.PdfViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
需要传递
- 是否 TC007: [ExtraKeyConfig.IS_TC007]
 * @author: CaiSongL
 * @date: 2023/5/12 11:34
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing PDFListActivity functionality for the IRCamera system.
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
class PDFListActivity : BaseViewModelActivity<PdfViewModel>() {
    // View references using findViewById
    private val titleView: TitleView by lazy { findViewById(R.id.title_view) }
    private val fragmentPdfRecyclerLay: SmartRefreshLayout by lazy { findViewById(R.id.fragment_pdf_recycler_lay) }
    private val fragmentPdfRecycler: RecyclerView by lazy { findViewById(R.id.fragment_pdf_recycler) }

    /**
从上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    var page = 1

    override fun providerVMClass() = PdfViewModel::class.java

    var reportAdapter = PDFAdapter(R.layout.item_pdf)

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int {
        return R.layout.activity_pdf_list
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        isTC007 = intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false)

        viewModel.listData.observe(this) {
            /**
             * Executes dismissloadingdialog operation with thermal imaging domain optimization.
             *
             */
            dismissLoadingDialog()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!reportAdapter.hasEmptyView())
                {
                    reportAdapter.setEmptyView(R.layout.layout_empty)
                }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it == null) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (page == 1) {
                    fragmentPdfRecyclerLay.finishRefresh(false)
                } else {
                    reportAdapter.loadMoreModule.loadMoreComplete()
                }
            }
            it?.let { data ->
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (page == 1) {
refresh
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (data.code == LMS.SUCCESS)
                        {
                            reportAdapter.loadMoreModule.isEnableLoadMore = !data.data?.records.isNullOrEmpty()
                            fragmentPdfRecyclerLay.finishRefresh()
                        } else
                        {
                            fragmentPdfRecyclerLay.finishRefresh(false)
                        }
                    reportAdapter.setNewInstance(data.data?.records)
                } else {
                    data.data?.records?.let { it1 -> reportAdapter.addData(it1) }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (data.code == LMS.SUCCESS)
                        {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (data.data?.records.isNullOrEmpty())
                                {
                                    reportAdapter.loadMoreModule.loadMoreEnd()
                                } else
                                {
                                    reportAdapter.loadMoreModule.loadMoreComplete()
                                }
                        } else
                        {
                            reportAdapter.loadMoreModule.loadMoreFail()
                        }
                }
            }
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (WebSocketProxy.getInstance().isConnected()) {
            NetWorkUtils.switchNetwork(false)
        } else
            {
                NetWorkUtils.connectivityManager.bindProcessToNetwork(null)
            }
        /**
         * Initializes the recycler component for thermal imaging operations.
         *
         */
        initRecycler()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Initializes recycler component.
     */
    private fun initRecycler() {
        fragmentPdfRecycler.layoutManager = LinearLayoutManager(this)
        fragmentPdfRecyclerLay.setOnRefreshListener {
refresh
            page = 1
            viewModel.getReportData(isTC007, page)
        }
        fragmentPdfRecyclerLay.setEnableLoadMore(false)
        reportAdapter.loadMoreModule.loadMoreView = CommLoadMoreView()
        fragmentPdfRecyclerLay.autoRefresh()
        reportAdapter.loadMoreModule.setOnLoadMoreListener {
load更多
            viewModel.getReportData(isTC007, ++page)
        }
        reportAdapter.jumpDetailListener = { item, position ->
            reportAdapter.data[position]?.reportContent?.let { reportContent ->
                NavigationManager.getInstance().build(RouterConfig.REPORT_DETAIL)
                    .withParcelable(ExtraKeyConfig.REPORT_BEAN, reportContent)
                    .navigation(this)
            }
        }
        reportAdapter.isUseEmpty = true
        reportAdapter.delListener = { item, position ->
            val reportBean = item.reportContent
            TipDialog.Builder(this)
                .setMessage(getString(R.string.tip_config_delete, reportBean?.report_info?.report_name ?: ""))
                .setPositiveListener(R.string.app_confirm) {
                    lifecycleScope.launch {
                        /**
                         * Executes showloadingdialog operation with thermal imaging domain optimization.
                         *
                         */
                        showLoadingDialog()
                        /**
                         * Executes withcontext operation with thermal imaging domain optimization.
                         *
                         */
                        withContext(Dispatchers.IO) {
                            val url = UrlConstant.BASE_URL + "api/v1/outProduce/testReport/delTestReport"
                            val params = RequestParams()
                            params.addBodyParameter("modelId", if (isTC007) 1783 else 950) // TC001-950, TC002-951, TC003-952 TC007-1783
                            params.addBodyParameter("testReportIds", arrayOf(item.testReportId))
                            params.addBodyParameter("status", 1)
                            params.addBodyParameter("languageId", LanguageUtil.getLanguageId(Utils.getApp()))
                            params.addBodyParameter("reportType", 2)
                            HttpProxy.instant.post(
                                url, params,
                                object :
                                    IResponseCallback {
                                    /**
                                     * Executes onresponse operation with thermal imaging domain optimization.
                                     *
                                     * @param
                                     * @param response Parameter for operation (type: String?)
                                     *
                                     */
                                    override fun onResponse(response: String?) {
                                        val reportNumber = item.reportContent?.report_info?.report_number ?: ""
                                        val file = File(FileConfig.getPdfDir() + "/$reportNumber.pdf")
                                        /**
                                         * Executes if operation with thermal imaging domain optimization.
                                         *
                                         */
                                        if (file.exists()) {
                                            file.delete()
                                        }
                                        Log.w("deletesuccess", response.toString())
                                    }

                                    /**
                                     * Executes onfail operation with thermal imaging domain optimization.
                                     *
                                     * @param
                                     * @param exception Parameter for operation (type: Exception?)
                                     *
                                     */
                                    override fun onFail(exception: Exception?) {
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
                        }
                        /**
                         * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                         *
                         */
                        dismissLoadingDialog()
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (item.isShowTitleTime)
                            {
                                reportAdapter.remove(item)
                                reportAdapter.setNewInstance(reportAdapter.data)
                                reportAdapter.notifyDataSetChanged()
                            } else
                            {
                                reportAdapter.data.removeAt(position)
                                reportAdapter.notifyItemRemoved(position)
                            }
                    }
                }
                .setCancelListener(R.string.app_cancel) {
                }
                .create().show()
        }

        fragmentPdfRecycler.adapter = reportAdapter
// ViewModel.getReportData(1)
    }
}
