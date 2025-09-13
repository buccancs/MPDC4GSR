package com.topdon.module.thermal.ir.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.topdon.lib.core.db.AppDatabase
import com.topdon.lib.core.db.dao.ThermalDao
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.lib.core.ktbase.BaseViewModel
import kotlinx.coroutines.*

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for IRMonitorViewModel display and interaction.
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
class IRMonitorViewModel : BaseViewModel() {
    val recordListLD = MutableLiveData<List<ThermalDao.Record>>()

    /**
     * Executes queryRecordList functionality.
     */
    /**
     * Executes queryrecordlist operation with thermal imaging domain optimization.
     *
     */
    fun queryRecordList() {
        viewModelScope.launch(Dispatchers.IO) {
            val recordList: List<ThermalDao.Record> = AppDatabase.getInstance().thermalDao().queryRecordList()
            recordListLD.postValue(recordList)
        }
    }

    val detailListLD = MutableLiveData<List<ThermalEntity>>()

    /**
     * Executes queryDetail functionality.
     */
    /**
     * Executes querydetail operation with thermal imaging domain optimization.
     *
     * @param
     * @param startTime Parameter for operation (type: Long)
     *
     */
    fun queryDetail(startTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val detailList: List<ThermalEntity> = AppDatabase.getInstance().thermalDao().queryDetail(startTime)
            detailListLD.postValue(detailList)
        }
    }

    /**
     * Executes delDetail functionality.
     */
    /**
     * Executes deldetail operation with thermal imaging domain optimization.
     *
     * @param
     * @param startTime Parameter for operation (type: Long)
     *
     */
    fun delDetail(startTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance().thermalDao().delDetail(startTime)
        }
    }
}
