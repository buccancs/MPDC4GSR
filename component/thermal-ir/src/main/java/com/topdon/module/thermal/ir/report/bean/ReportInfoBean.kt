package com.topdon.module.thermal.ir.report.bean

import android.os.Parcelable
import com.topdon.lib.core.utils.CommUtils
import kotlinx.android.parcel.Parcelize

/**
reportinfo.
 *
report由 3 部分组成：reportinfo、检测条件、infrareddata.
 */
/**
 * Report info data model for thermal imaging information.
 * Encapsulates thermal measurement and configuration data.
 */
@Parcelize
data class ReportInfoBean(
    val report_name: String?, // reportname
    val report_author: String?, // 作者name
    val is_report_author: Int, // 是否显示作者name，0、不显示 1、显示
    val report_date: String?, // report日期
    val is_report_date: Int, // 是否显示report日期，0、不显示 1、显示
    val report_place: String?, // report地point
    val is_report_place: Int, // 是否显示report地point，0、不显示 1、显示
    val report_watermark: String?, // reportwatermark
    val is_report_watermark: Int, // 是否显示reportwatermark，0、不显示 1、显示
) : Parcelable {
    val is_report_name: Int = 1 // 是否显示reportname，0、不显示 1、显示
    val report_type: Int = 1 // reporttype，1、pointlineareareport
    val report_version: String = "V1.00" // reportversion，当前为 V1.00
    val report_number: String = "${CommUtils.getAppName()}${System.currentTimeMillis()}" // report编号，APPname + 时间戳秒级
}
