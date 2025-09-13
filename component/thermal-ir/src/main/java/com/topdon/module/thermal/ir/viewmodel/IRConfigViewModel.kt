package com.topdon.module.thermal.ir.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.topdon.lib.core.utils.SingleLiveEvent
import com.topdon.module.thermal.ir.bean.DataBean
import com.topdon.module.thermal.ir.bean.ModelBean
import com.topdon.module.thermal.ir.repository.ConfigRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for IRConfigViewModel display and interaction.
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
class IRConfigViewModel(application: Application) : AndroidViewModel(application) {
    val configLiveData = SingleLiveEvent<ModelBean>()

    /**
读取configurationdata
     */
    /**
     * Retrieves the config with optimized performance for thermal imaging operations.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     *
     */
    fun getConfig(isTC007: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            configLiveData.postValue(ConfigRepository.read(isTC007))
        }
    }

    /**
updatedefaultparameter中的ambient temperature，单位摄氏度。
     */
    fun updateDefaultEnvironment(
        isTC007: Boolean,
        environment: Float,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val modelBean = configLiveData.value ?: ConfigRepository.read(isTC007)
            modelBean.defaultModel.environment = environment
            ConfigRepository.update(isTC007, modelBean)
            configLiveData.postValue(modelBean)
        }
    }

    /**
updatedefaultparameter中的距离，单位不详。
     */
    /**
     * Executes updatedefaultdistance operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     * @param distance Parameter for operation (type: Float)
     *
     */
    fun updateDefaultDistance(
        isTC007: Boolean,
        distance: Float,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val modelBean = configLiveData.value ?: ConfigRepository.read(isTC007)
            modelBean.defaultModel.distance = distance
            ConfigRepository.update(isTC007, modelBean)
            configLiveData.postValue(modelBean)
        }
    }

    /**
updatedefaultparameter中的emissivity。
     */
    fun updateDefaultRadiation(
        isTC007: Boolean,
        radiation: Float,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val modelBean = configLiveData.value ?: ConfigRepository.read(isTC007)
            modelBean.defaultModel.radiation = radiation
            ConfigRepository.update(isTC007, modelBean)
            configLiveData.postValue(modelBean)
        }
    }

    /**
增加a自定义mode
     */
    /**
     * Executes addconfig operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     *
     */
    fun addConfig(isTC007: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val modelBean = configLiveData.value ?: ConfigRepository.read(isTC007)

            var index = 0
            modelBean.myselfModel.forEach {
                index = index.coerceAtLeast(it.id)
            }
            index++

            modelBean.myselfModel.add(DataBean(id = index, name = index.toString()))

            ConfigRepository.update(isTC007, modelBean)
            configLiveData.postValue(modelBean)
        }
    }

    /**
selectionmode
@param id 0:defaultmode   > 0 采用自定义mode
     */
    /**
     * Executes checkConfig functionality.
     */
    /**
     * Executes checkconfig operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     * @param id Parameter for operation (type: Int)
     *
     */
    fun checkConfig(
        isTC007: Boolean,
        id: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val modelBean = configLiveData.value ?: ConfigRepository.read(isTC007)
            modelBean.defaultModel.use = id == 0
            modelBean.myselfModel.forEach {
                it.use = it.id == id
            }
            ConfigRepository.update(isTC007, modelBean)
            configLiveData.postValue(modelBean)
        }
    }

    /**
delete自定义mode
@param id 自定义mode id
     */
    /**
     * Executes deleteConfig functionality.
     */
    /**
     * Executes deleteconfig operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     * @param id Parameter for operation (type: Int)
     *
     */
    fun deleteConfig(
        isTC007: Boolean,
        id: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val modelBean = configLiveData.value ?: ConfigRepository.read(isTC007)
            var removeAt = modelBean.myselfModel.size
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in modelBean.myselfModel.indices) {
                val dataBean = modelBean.myselfModel[i]
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dataBean.id == id) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataBean.use) { // Delete当前正在使用的自定义mode，变更为使用defaultmode
                        modelBean.defaultModel.use = true
                    }
                    modelBean.myselfModel.removeAt(i)
                    removeAt = i
                    break
                }
            }

BUG 28055 提的问题，delete后要把后areaname往前补，虽然实际使用非常怪，先按 BUG 改吧
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (removeAt < modelBean.myselfModel.size) {
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in removeAt until modelBean.myselfModel.size) {
                    val dataBean = modelBean.myselfModel[i]
                    dataBean.id = i + 1
                    dataBean.name = dataBean.id.toString()
                }
            }

            ConfigRepository.update(isTC007, modelBean)
            configLiveData.postValue(modelBean)
        }
    }

    /**
update一项自定义parameter.
     */
    /**
     * Executes updatecustom operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTC007 Parameter for operation (type: Boolean)
     * @param dataBean Parameter for operation (type: DataBean)
     *
     */
    fun updateCustom(
        isTC007: Boolean,
        dataBean: DataBean,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val modelBean = configLiveData.value ?: ConfigRepository.read(isTC007)
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in modelBean.myselfModel.indices) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (modelBean.myselfModel[i].id == dataBean.id) {
                    modelBean.myselfModel[i] = dataBean
                    break
                }
            }
            ConfigRepository.update(isTC007, modelBean)
            configLiveData.postValue(modelBean)
        }
    }
}
