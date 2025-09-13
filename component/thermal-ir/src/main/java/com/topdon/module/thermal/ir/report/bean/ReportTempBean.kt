package com.topdon.module.thermal.ir.report.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Report temp data model for thermal imaging information.
 * Encapsulates thermal measurement and configuration data.
 */
@Parcelize
data class ReportTempBean(
    val max_temperature: String?, // 带单位符号的maximum温，单位跟随用户configuration
    val is_max_temperature: Int, // 是否显示maximum温
    val min_temperature: String?, // 带单位符号的minimum温，单位跟随用户configuration
    val is_min_temperature: Int, // 是否显示minimum温
    val comment: String?, // comment
    val is_comment: Int, // 是否显示comment
    val mean_temperature: String? = null, // 带单位符号的average温，单位跟随用户configuration
    val is_mean_temperature: Int = 0, // 是否显示average温
    val temperature: String? = null, // 带单位符号的pointtemperature，单位跟随用户configuration
    val is_temperature: Int = 0, // 是否显示pointtemperature
) : Parcelable {
    constructor(temperature: String?, is_temperature: Int, comment: String?, is_comment: Int) : this(
        null,
        0,
        null,
        0,
        comment,
        is_comment,
        null,
        0,
        temperature,
        is_temperature,
    )

    fun isMaxOpen() = is_max_temperature == 1

    fun isMinOpen() = is_min_temperature == 1

    fun isAverageOpen() = is_mean_temperature == 1

    fun isExplainOpen() = is_comment == 1

    fun isTempOpen() = is_temperature == 1
}
