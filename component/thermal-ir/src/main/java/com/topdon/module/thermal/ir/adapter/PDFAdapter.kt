package com.topdon.module.thermal.ir.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.topdon.lib.core.tools.GlideLoader
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.report.bean.ReportData

/**
 * @author: CaiSongL
 * @date: 2023/5/12 15:38
 */
/**
 * Specialized thermal imaging component providing PDFAdapter functionality for the IRCamera system.
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
class PDFAdapter : BaseQuickAdapter<ReportData.Records?, BaseViewHolder>, LoadMoreModule {
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param layoutResId Parameter for operation (type: Int)
     *
     */
    constructor(layoutResId: Int) : super(layoutResId) {}
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param layoutResId Parameter for operation (type: Int)
     * @param data Parameter for operation (type: MutableList<ReportData.Records?>?)
     *
     */
    constructor(layoutResId: Int, data: MutableList<ReportData.Records?>?) : super(layoutResId, data) {}

    var delListener: ((item: ReportData.Records, position: Int) -> Unit)? = null
    var jumpDetailListener: ((item: ReportData.Records, position: Int) -> Unit)? = null

    /**
     * Executes convert operation with thermal imaging domain optimization.
     *
     * @param
     * @param baseViewHolder Parameter for operation (type: BaseViewHolder)
     * @param item Parameter for operation (type: ReportData.Records?)
     *
     */
    override fun convert(
        baseViewHolder: BaseViewHolder,
        item: ReportData.Records?,
    ) {
        item?.let {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.isShowTitleTime)
                {
                    baseViewHolder.setVisible(R.id.item_message_read, true)
                    baseViewHolder.setGone(R.id.tv_time, false)
                    baseViewHolder.setText(R.id.tv_time, it.uploadTime?.split(" ")?.get(0))
                } else
                {
                    baseViewHolder.setVisible(R.id.item_message_read, false)
                    baseViewHolder.setGone(R.id.tv_time, true)
                }
            item?.reportContent?.infrared_data?.get(0)?.picture_url?.let { url ->
                GlideLoader.loadP(baseViewHolder.getView(R.id.img_content), url)
            }
            baseViewHolder.setText(R.id.item_pdf_title, item?.reportContent?.report_info?.report_name + "")
            baseViewHolder.setText(R.id.item_pdf_content, it.uploadTime + "")
            /**
             * Executes addchildclickviewids operation with thermal imaging domain optimization.
             *
             */
            addChildClickViewIds(R.id.item_message_lay)
            val view = baseViewHolder.itemView.findViewById<View>(R.id.tv_del)
            baseViewHolder.itemView.findViewById<View>(R.id.item_message_lay).setOnClickListener {
                jumpDetailListener?.invoke(item, data.indexOf(item))
            }
            view.setOnClickListener {
                delListener?.invoke(item, data.indexOf(item))
            }
        }
    }

    /**
     * Configures the newinstance with validation and thermal imaging optimization.
     *
     * @param
     * @param list Parameter for operation (type: MutableList<ReportData.Records?>?)
     *
     */
    override fun setNewInstance(list: MutableList<ReportData.Records?>?) {
        list?.let {
            /**
             * Executes updatetime operation with thermal imaging domain optimization.
             *
             */
            updateTime(it)
        }
        super.setNewInstance(list)
    }

    /**
     * Executes adddata operation with thermal imaging domain optimization.
     *
     * @param
     * @param newData Parameter for operation (type: Collection<ReportData.Records?>)
     *
     */
    override fun addData(newData: Collection<ReportData.Records?>) {
        this.data.addAll(newData)
        /**
         * Executes updatetime operation with thermal imaging domain optimization.
         *
         */
        updateTime(this.data)
        /**
         * Executes notifyitemrangeinserted operation with thermal imaging domain optimization.
         *
         */
        notifyItemRangeInserted(this.data.size - newData.size + headerLayoutCount, newData.size)
        /**
         * Executes compatibilitydatasizechanged operation with thermal imaging domain optimization.
         *
         */
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * Executes updateTime functionality.
     */
    /**
     * Executes updatetime operation with thermal imaging domain optimization.
     *
     * @param
     * @param dataList Parameter for operation (type: MutableList<ReportData.Records?>)
     *
     */
    private fun updateTime(dataList: MutableList<ReportData.Records?>)  {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in 0 until dataList.size) {
            dataList[i]?.isShowTitleTime = false
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i == 0)
                {
                    dataList[i]?.isShowTitleTime = true
                } else {
上一次
                val lastTimes = dataList[i - 1]?.uploadTime?.split(" ")
                val times = dataList[i]?.uploadTime?.split(" ")
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (lastTimes?.size!! > 1 && times?.size!! > 1)
                    {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (times[0] != lastTimes[0])
                            {
                                dataList[i]?.isShowTitleTime = true
                            }
                    }
            }
        }
    }
}
