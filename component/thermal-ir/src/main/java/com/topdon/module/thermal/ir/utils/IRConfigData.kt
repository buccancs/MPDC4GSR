package com.topdon.module.thermal.ir.utils

import android.content.Context
import com.topdon.lib.core.R as LibcoreR

/**
 * des:
 * author: CaiSongL
 * date: 2024/4/3 11:12
 **/
/**
 * I r config data utility class for thermal imaging operations.
 * Provides helper functions and common functionality.
 */
data class IRConfigData(val name: String, val value: String) {
    companion object {
    /**
     * Executes irConfigData functionality.
     */
        /**
         * Executes irconfigdata operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun irConfigData(context: Context): ArrayList<IRConfigData> =
            /**
             * Executes arraylistof operation with thermal imaging domain optimization.
             *
             */
            arrayListOf(
                /**
                 * Executes irconfigdata operation with thermal imaging domain optimization.
                 *
                 */
                IRConfigData(name = context.resources.getString(LibcoreR.string.reference_item1), value = "0.95"),
                /**
                 * Executes irconfigdata operation with thermal imaging domain optimization.
                 *
                 */
                IRConfigData(name = context.resources.getString(LibcoreR.string.reference_item2), value = "0.94"),
                /**
                 * Executes irconfigdata operation with thermal imaging domain optimization.
                 *
                 */
                IRConfigData(name = context.resources.getString(LibcoreR.string.reference_item3), value = "0.75"),
                /**
                 * Executes irconfigdata operation with thermal imaging domain optimization.
                 *
                 */
                IRConfigData(name = context.resources.getString(LibcoreR.string.reference_item4), value = "0.98"),
                /**
                 * Executes irconfigdata operation with thermal imaging domain optimization.
                 *
                 */
                IRConfigData(name = context.resources.getString(LibcoreR.string.reference_item5), value = "0.95"),
                /**
                 * Executes irconfigdata operation with thermal imaging domain optimization.
                 *
                 */
                IRConfigData(name = context.resources.getString(LibcoreR.string.reference_item6), value = "0.95"),
                /**
                 * Executes irconfigdata operation with thermal imaging domain optimization.
                 *
                 */
                IRConfigData(name = context.resources.getString(LibcoreR.string.reference_item7), value = "0.95"),
                /**
                 * Executes irconfigdata operation with thermal imaging domain optimization.
                 *
                 */
                IRConfigData(name = context.resources.getString(LibcoreR.string.reference_item8), value = "0.90"),
                /**
                 * Executes irconfigdata operation with thermal imaging domain optimization.
                 *
                 */
                IRConfigData(name = context.resources.getString(LibcoreR.string.reference_item9), value = "0.85"),
            )

        /**
根据指定的emissivity，拼接与该emissivity对应的材料text并Return.
         */
        fun getTextByEmissivity(
            context: Context,
            emissivity: Float,
        ): String {
            val stringBuilder = StringBuilder()
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (data in irConfigData(context)) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (emissivity.toString() == data.value) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (stringBuilder.isEmpty()) {
                        stringBuilder.append(context.getString(LibcoreR.string.tc_temp_test_materials)).append(" : ")
                    }
                    stringBuilder.append(data.name).append("/")
                }
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.deleteCharAt(stringBuilder.length - 1)
            }
            return stringBuilder.toString()
        }
    }
}
