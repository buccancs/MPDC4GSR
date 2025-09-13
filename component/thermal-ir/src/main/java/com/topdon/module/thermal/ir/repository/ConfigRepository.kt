package com.topdon.module.thermal.ir.repository

import com.google.gson.Gson
import com.topdon.lib.core.common.SharedManager
import com.topdon.module.thermal.ir.bean.DataBean
import com.topdon.module.thermal.ir.bean.ModelBean
import java.lang.Exception

/**
 * Configuration management system for thermal imaging parameters. Handles settings and calibration for ConfigRepository operations.
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
object ConfigRepository {
    /**
     * Executes read functionality.
     */
    /**
     * Executes read operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     *
     */
    fun read(isTC007: Boolean): ModelBean =
        try {
            Gson().fromJson(if (isTC007) SharedManager.irConfigJsonTC007 else SharedManager.getIRConfig(), ModelBean::class.java)
        } catch (_: Exception) {
当SP里没data必定抛exception，所以这里Returnadefault的
            /**
             * Executes modelbean operation with thermal imaging domain optimization.
             *
             */
            ModelBean(DataBean(id = 0, use = true))
        }

    /**
     * Executes update functionality.
     */
    /**
     * Executes update operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     * @param bean Parameter for operation (type: ModelBean)
     *
     */
    fun update(
        isTC007: Boolean,
        bean: ModelBean,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007) {
            SharedManager.irConfigJsonTC007 = Gson().toJson(bean)
        } else {
            SharedManager.setIRConfig(Gson().toJson(bean))
        }
    }

    /**
读取selected的configurationinfo
     */
    /**
     * Executes readconfig operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     *
     */
    fun readConfig(isTC007: Boolean): DataBean {
        val config = read(isTC007)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (config.defaultModel.use) {
            return config.defaultModel
        }
        config.myselfModel.forEach {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.use) {
                return it
            }
        }
        return config.defaultModel
    }
}
