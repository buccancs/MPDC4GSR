package com.topdon.module.thermal.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.db.AppDatabase
import com.topdon.lib.core.db.entity.ThermalDayEntity
import com.topdon.lib.core.db.entity.ThermalEntity
import com.topdon.lib.core.db.entity.ThermalHourEntity
import com.topdon.lib.core.db.entity.ThermalMinuteEntity
import com.topdon.lib.core.ktbase.BaseViewModel
import com.topdon.lib.core.tools.TimeTool
import kotlinx.coroutines.*
import java.util.*

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for LogViewModel display and interaction.
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
class LogViewModel : BaseViewModel() {
    val resultLiveData = MutableLiveData<ChartList>()

    private var queryJob: Job? = null

    /**
     * Executes queryLogByType functionality.
     */
    /**
     * Executes querylogbytype operation with thermal imaging domain optimization.
     *
     * @param
     * @param selectType Parameter for operation (type: Int)
     *
     */
    fun queryLogByType(selectType: Int) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (queryJob != null && queryJob!!.isActive) {
            queryJob!!.cancel()
            queryJob = null
        }
        queryJob =
            viewModelScope.launch(Dispatchers.IO) {
                var dataList: ArrayList<ThermalEntity>? = arrayListOf()
                var startTime = 0L
                var endTime = 0L
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (selectType) {
                    1 -> {
                        Log.w("123", "查询秒")
秒
                        endTime = Date().time
                        startTime = endTime - 7200 * 1000L // 2小时
                        Log.w("123", "query startTime:$startTime, endTime:$endTime")
                        dataList =
                            AppDatabase.getInstance().thermalDao()
                                .getThermalByDate(
                                    SharedManager.getUserId(),
                                    startTime,
                                    endTime,
                                ) as ArrayList<ThermalEntity>
                        Log.w("123", "data size: ${dataList.size}")
                    }
                    2 -> {
分
                        endTime = Date().time
                        startTime = endTime - 7200 * 60 * 1000L
                        dataList =
                            AppDatabase.getInstance().thermalDao()
                                .getThermalByDate(
                                    SharedManager.getUserId(),
                                    startTime,
                                    endTime,
                                ) as ArrayList<ThermalEntity>
                    }
                    3 -> {
时
                        endTime = Date().time
                        startTime = endTime - 7200 * 60 * 60 * 1000L
                        dataList =
                            AppDatabase.getInstance().thermalDao()
                                .getThermalByDate(
                                    SharedManager.getUserId(),
                                    startTime,
                                    endTime,
                                ) as ArrayList<ThermalEntity>
                    }
                    else -> {
天
                        dataList =
                            AppDatabase.getInstance().thermalDao()
                                .getAllThermalByDate(SharedManager.getUserId()) as ArrayList<ThermalEntity>
                    }
                }
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(500)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dataList == null) {
                    dataList = arrayListOf()
                } else {
                    Log.w("123", "dataList size:${dataList.size}")
                }
                resultLiveData.postValue(ChartList(dataList = dataList))
            }
    }

    /**
第一项实时图表的历史Record查询
/**
 * Executes 查询历史电压data operation with thermal imaging domain optimization.
 *
 */
查询历史电压data(等待bluetooth传输历史Recordend后触发)
时间区间: 现在时间 => 倒退到startEvent
     */
    suspend fun queryLogThermals(
        selectTimeType: Int,
        endLogTime: Long = System.currentTimeMillis(),
        action: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = SharedManager.getUserId()
            val bean = ChartList()
查询之前先Synchronizedata
            val job = async { syncVol(selectTimeType) }
            job.await()
            syncRun = false // Synchronizeend
            val startLogTime =
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (selectTimeType) {
                    /**
7200data
秒:2小时
分:5天
时:300天
天:20年
                     */
                    1 -> endLogTime - 7200 * 1000L // 秒(2小时)
                    2 -> endLogTime - 7200 * 60 * 1000L // 分(5天)
                    3 -> endLogTime - 7200 * 60 * 60 * 1000L // 时(300天)
                    4 -> endLogTime - 1 * 365 * 24 * 60 * 60 * 1000L // 天(1年)
                    else -> endLogTime - 7200 * 1000L
                }
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (selectTimeType) {
                1 -> {
                    bean.dataList =
                        AppDatabase.getInstance().thermalDao()
                            .queryByTime(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            ) as ArrayList<ThermalEntity>
                    bean.maxVol =
                        AppDatabase.getInstance().thermalDao()
                            .queryByTimeMax(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            )
                    bean.minVol =
                        AppDatabase.getInstance().thermalDao()
                            .queryByTimeMin(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            )
                    Log.w("chart", "电压data:${bean.dataList.size}")
                    Log.w("chart", "电压datamax vol:${bean.maxVol},min vol:${bean.minVol}")
                }
                2 -> {
                    val resultList =
                        AppDatabase.getInstance().thermalMinDao()
                            .queryByTime(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            ) as ArrayList<ThermalMinuteEntity>
                    resultList.forEach {
                        val entity = ThermalEntity()
                        entity.userId = it.userId
                        entity.sn = it.sn
                        entity.thermal = it.thermal
                        entity.thermalMax = it.thermalMax
                        entity.thermalMin = it.thermalMin
                        entity.info = it.info
                        entity.type = it.type
                        entity.createTime = it.createTime
                        bean.dataList.add(entity)
                    }
                    bean.maxVol =
                        AppDatabase.getInstance().thermalMinDao()
                            .queryByTimeMax(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            )
                    bean.minVol =
                        AppDatabase.getInstance().thermalMinDao()
                            .queryByTimeMin(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            )
                    Log.w("chart", "电压data:${bean.dataList.size}")
                    Log.w("chart", "电压datamax vol:${bean.maxVol},min vol:${bean.minVol}")
                }
                3 -> {
                    val resultList =
                        AppDatabase.getInstance().thermalHourDao()
                            .queryByTime(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            ) as ArrayList<ThermalHourEntity>
                    resultList.forEach {
                        val entity = ThermalEntity()
                        entity.userId = it.userId
                        entity.sn = it.sn
                        entity.thermal = it.thermal
                        entity.thermalMax = it.thermalMax
                        entity.thermalMin = it.thermalMin
                        entity.info = it.info
                        entity.type = it.type
                        entity.createTime = it.createTime
                        bean.dataList.add(entity)
                    }
                    bean.maxVol =
                        AppDatabase.getInstance().thermalHourDao()
                            .queryByTimeMax(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            )
                    bean.minVol =
                        AppDatabase.getInstance().thermalHourDao()
                            .queryByTimeMin(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            )
                    Log.w("chart", "电压data:${bean.dataList.size}")
                    Log.w("chart", "电压datamax vol:${bean.maxVol},min vol:${bean.minVol}")
                }
                4 -> {
                    val resultList =
                        AppDatabase.getInstance().thermalDayDao()
                            .queryByTime(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            ) as ArrayList<ThermalDayEntity>
                    resultList.forEach {
                        val entity = ThermalEntity()
                        entity.userId = it.userId
                        entity.sn = it.sn
                        entity.thermal = it.thermal
                        entity.thermalMax = it.thermalMax
                        entity.thermalMin = it.thermalMin
                        entity.info = it.info
                        entity.type = it.type
                        entity.createTime = it.createTime
                        bean.dataList.add(entity)
                    }
                    bean.maxVol =
                        AppDatabase.getInstance().thermalDayDao()
                            .queryByTimeMax(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            )
                    bean.minVol =
                        AppDatabase.getInstance().thermalDayDao()
                            .queryByTimeMin(
                                userId = userId,
                                startTime = startLogTime,
                                endTime = endLogTime,
                            )
                    Log.w("chart", "电压data:${bean.dataList.size}")
                    Log.w("chart", "电压datamax vol:${bean.maxVol},min vol:${bean.minVol}")
                }
            }
            bean.action = action
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (action == 4) {
                val startTime = TimeTool.showDateType(bean.dataList.first().createTime)
                val endTime = TimeTool.showDateType(bean.dataList.last().createTime)
                Log.w("123", "log start:$startTime, end:$endTime")
            }
            resultLiveData.postValue(bean)
        }
    }

    /**
第二项历史Record查询
/**
 * Executes 查询历史电压data operation with thermal imaging domain optimization.
 *
 */
查询历史电压data(等待bluetooth传输历史Recordend后触发)
时间区间: 初始时间 => 推进到endEvent
     */
    suspend fun queryLogVolsByStartTime(
        type: Int = 3,
        selectTimeType: Int,
        endLogTime: Long = System.currentTimeMillis(),
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = SharedManager.getUserId()
                val typeStr =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (type) {
                        1 -> "point"
                        2 -> "line"
                        else -> "fence"
                    }
                val bean = ChartList()
查询之前先Synchronizedata
                val job = async { syncVol(selectTimeType) }
                job.await()
                syncRun = false // Synchronizeend
                val startLogTime =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (selectTimeType) {
                        /**
7200data
秒:2小时
分:5天
时:300天
天:20年
                         */
1 -> startLogTime + 2 * 60 * 60 * 1000L // 秒(2小时)
2 -> startLogTime + 24 * 60 * 60 * 1000L // 分(1天)
3 -> startLogTime + 30 * 24 * 60 * 60 * 1000L // 时(30天)
4 -> startLogTime + 1 * 365 * 24 * 60 * 60 * 1000L // 天(1年)

1 -> startLogTime + 7200 * 1000L // 秒(2小时)
2 -> startLogTime + 7200 * 60 * 1000L // 分(5天)
3 -> startLogTime + 7200 * 60 * 60 * 1000L // 时(300天)
4 -> startLogTime + 1 * 365 * 24 * 60 * 60 * 1000L // 天(1年)
// Else -> startLogTime + 7200 * 1000L

                        1 -> endLogTime - 7200 * 1000L // 秒(2小时)
                        2 -> endLogTime - 7200 * 60 * 1000L // 分(5天)
                        3 -> endLogTime - 7200 * 60 * 60 * 1000L // 时(300天)
                        4 -> endLogTime - 10 * 365 * 24 * 60 * 60 * 1000L // 天(10年)
                        else -> endLogTime - 7200 * 1000L
                    }
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (selectTimeType) {
                    1 -> {
                        bean.dataList =
                            AppDatabase.getInstance().thermalDao()
                                .queryByTime(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                    type = typeStr,
                                ) as ArrayList<ThermalEntity>
                        bean.maxVol =
                            AppDatabase.getInstance().thermalDao()
                                .queryByTimeMax(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                )
                        bean.minVol =
                            AppDatabase.getInstance().thermalDao()
                                .queryByTimeMin(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                )
                        Log.w("chart", "电压data:${bean.dataList.size}")
                    }
                    2 -> {
                        val resultList =
                            AppDatabase.getInstance().thermalMinDao()
                                .queryByTime(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                    type = typeStr,
                                ) as ArrayList<ThermalMinuteEntity>
                        resultList.forEach {
                            val entity = ThermalEntity()
                            entity.userId = it.userId
                            entity.sn = it.sn
                            entity.thermal = it.thermal
                            entity.thermalMax = it.thermalMax
                            entity.thermalMin = it.thermalMin
                            entity.info = it.info
                            entity.type = it.type
                            entity.createTime = it.createTime
                            bean.dataList.add(entity)
                        }
                        bean.maxVol =
                            AppDatabase.getInstance().thermalMinDao()
                                .queryByTimeMax(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                )
                        bean.minVol =
                            AppDatabase.getInstance().thermalMinDao()
                                .queryByTimeMin(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                )
                        Log.w("chart", "电压data:${bean.dataList.size}")
                    }
                    3 -> {
                        val resultList =
                            AppDatabase.getInstance().thermalHourDao()
                                .queryByTime(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                    type = typeStr,
                                ) as ArrayList<ThermalHourEntity>
                        resultList.forEach {
                            val entity = ThermalEntity()
                            entity.userId = it.userId
                            entity.sn = it.sn
                            entity.thermal = it.thermal
                            entity.thermalMax = it.thermalMax
                            entity.thermalMin = it.thermalMin
                            entity.info = it.info
                            entity.type = it.type
                            entity.createTime = it.createTime
                            bean.dataList.add(entity)
                        }
                        bean.maxVol =
                            AppDatabase.getInstance().thermalHourDao()
                                .queryByTimeMax(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                )
                        bean.minVol =
                            AppDatabase.getInstance().thermalHourDao()
                                .queryByTimeMin(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                )
                        Log.w("chart", "电压data:${bean.dataList.size}")
                    }
                    4 -> {
                        val resultList =
                            AppDatabase.getInstance().thermalDayDao()
                                .queryByTime(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                    type = typeStr,
                                ) as ArrayList<ThermalDayEntity>
                        resultList.forEach {
                            val entity = ThermalEntity()
                            entity.userId = it.userId
                            entity.sn = it.sn
                            entity.thermal = it.thermal
                            entity.thermalMax = it.thermalMax
                            entity.thermalMin = it.thermalMin
                            entity.info = it.info
                            entity.type = it.type
                            entity.createTime = it.createTime
                            bean.dataList.add(entity)
                        }
                        bean.maxVol =
                            AppDatabase.getInstance().thermalDayDao()
                                .queryByTimeMax(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                )
                        bean.minVol =
                            AppDatabase.getInstance().thermalDayDao()
                                .queryByTimeMin(
                                    userId = userId,
                                    startTime = startLogTime,
                                    endTime = endLogTime,
                                )
                        Log.w("chart", "电压data:${bean.dataList.size}")
                    }
                }
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(500)
                resultLiveData.postValue(bean)
            } catch (e: Exception) {
                XLog.e("data查询exception:${e.message}")
                resultLiveData.postValue(ChartList())
            }
        }
    }

    /**
@param type 1:秒 2:分 3:时 4:天
     */
    /**
     * Retrieves the newvoldata with optimized performance for thermal imaging operations.
     *
     * @param
     * @param data Parameter for operation (type: List<ThermalEntity>)
     * @param type Parameter for operation (type: Int = 2)
     *
     */
    private fun getNewVolData(
        data: List<ThermalEntity>,
        type: Int = 2,
    ): ArrayList<ThermalEntity> {
        val newData: ArrayList<ThermalEntity> = arrayListOf()
        var startIndex = 0
        var endIndex = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in data.indices) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i == 0) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (i == data.size - 1) {
default整个区间
                    /**
                     * Executes adddata operation with thermal imaging domain optimization.
                     *
                     */
                    addData(data, newData, 0, endIndex)
                }
            } else {
                // [1]..[size-1]
                val currencyTime = TimeTool.showDateType(data[i].createTime, type)
                val previewTime = TimeTool.showDateType(data[i - 1].createTime, type)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (i == data.size - 1) {
最后a值
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (currencyTime != previewTime) {
同时calculation上a区间和当前最后a区间
最后上a区间
                        endIndex = i - 1
                        /**
                         * Executes adddata operation with thermal imaging domain optimization.
                         *
                         */
                        addData(data, newData, startIndex, endIndex)
                        startIndex = i
最后a区间
                        endIndex = i
                        /**
                         * Executes adddata operation with thermal imaging domain optimization.
                         *
                         */
                        addData(data, newData, startIndex, endIndex)
                    } else {
                        endIndex = i
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (newData.size == 0) {
default整个区间
                            /**
                             * Executes adddata operation with thermal imaging domain optimization.
                             *
                             */
                            addData(data, newData, 0, endIndex)
                        } else {
最后a区间
                            /**
                             * Executes adddata operation with thermal imaging domain optimization.
                             *
                             */
                            addData(data, newData, startIndex, endIndex)
                        }
                    }
                } else {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (currencyTime != previewTime) {
calculation上a区间
                        endIndex = i - 1
                        /**
                         * Executes adddata operation with thermal imaging domain optimization.
                         *
                         */
                        addData(data, newData, startIndex, endIndex)
新时间段
                        startIndex = i
                    }
                }
            }
        }
        return newData
    }

calculationaverage值
    /**
     * Executes addData functionality.
     */
    /**
     * Executes adddata operation with thermal imaging domain optimization.
     *
     * @param
     * @param data Parameter for operation (type: List<ThermalEntity>)
     * @param newData Parameter for operation (type: ArrayList<ThermalEntity>)
     * @param startIndex Parameter for operation (type: Int)
     * @param endIndex Parameter for operation (type: Int)
     *
     */
    private fun addData(
        data: List<ThermalEntity>,
        newData: ArrayList<ThermalEntity>,
        startIndex: Int,
        endIndex: Int,
    ) {
        val tempVolEntity = data[startIndex]
        var temp = 0f
        var tempMax = 0f
        var tempMin = 0f
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (x in startIndex..endIndex) {
            temp += data[x].thermal
            tempMax += data[x].thermalMax
            tempMin += data[x].thermalMin
        }
tempVol:0f    startIndex:2    endIndex:1 会出现vol:NaN
        tempVolEntity.thermal = temp / (endIndex - startIndex + 1) // 区间电压average值
        tempVolEntity.thermalMax = tempMax / (endIndex - startIndex + 1) // 区间电压average值
        tempVolEntity.thermalMin = tempMin / (endIndex - startIndex + 1) // 区间电压average值
        newData.add(tempVolEntity)
    }

    @Volatile
    private var syncRun = false

    /**
Synchronizedata
最早时间: 1609430400000 (2021-1-1 00:00:00)
     *
1. 查询saveRecord的最新时间
2. get要update的时间段data[最新data ~ 最新a时间区间的起始point]
3. 秒data转分data的average值
4. add到分data库
5. delete多余的data
     */
    /**
     * Executes syncvol operation with thermal imaging domain optimization.
     *
     * @param
     * @param selectTimeType Parameter for operation (type: Int)
     *
     */
    private suspend fun syncVol(selectTimeType: Int) {
        Log.i("chart", "syncVol: $syncRun")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (syncRun) {
有task正在执行
            return
        }
        Log.i("chart", "syncVol start")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectTimeType == 1) {
秒data不用processing
            return
        }
        syncRun = true
        val userId = SharedManager.getUserId()
查询saveRecord的最新时间
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (selectTimeType) {
            2 -> {
                val minuteTime = TimeTool.timeToMinute(System.currentTimeMillis(), 2)
Check最新时间段有没有dataSynchronize
                val minuteVolLatestList =
                    AppDatabase.getInstance().thermalMinDao()
                        .queryByTime(
                            userId = userId,
                            startTime = minuteTime,
                            endTime = System.currentTimeMillis(),
                        )
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (minuteVolLatestList.isNotEmpty()) {
                    Log.w("chart", "最新时间段已经有Record，不需要Synchronizeupdate")
                    return
                }
                val maxTime =
                    AppDatabase.getInstance().thermalMinDao().queryMaxTime(userId = userId)
                Log.w("chart", "minute latest time: $maxTime, ${TimeTool.showDateType(maxTime)}")
get要update的时间段data
                val secondVolList =
                    AppDatabase.getInstance().thermalDao()
                        .queryByTime(
                            userId = userId,
                            startTime = maxTime,
// EndTime = Long.MAX_VALUE
                            endTime = minuteTime,
                        ) as ArrayList<ThermalEntity>
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (secondVolList.size > 0) {
                    val startTime = TimeTool.showDateType(secondVolList.first().createTime)
                    val endTime = TimeTool.showDateType(secondVolList.last().createTime)
                    Log.w("chart", "要processing${secondVolList.size}个data, start:$startTime, end:$endTime")
                } else {
                    Log.w("chart", "无dataprocessing")
                }
秒data转分data的average值
                val minVolList = getNewVolData(secondVolList, 2)
add到分data库
                minVolList.forEach {
                    val bean = ThermalMinuteEntity()
                    try {
                        bean.userId = it.userId
                        bean.sn = it.sn
                        bean.thermal = it.thermal
                        bean.thermalMax = it.thermalMax
                        bean.thermalMin = it.thermalMin
                        bean.info = it.info
                        bean.type = it.type
                        bean.createTime = TimeTool.timeToMinute(it.createTime, 2) // Adjust精确到分
                        bean.updateTime = System.currentTimeMillis()
                        AppDatabase.getInstance().thermalMinDao().insert(bean)
                    } catch (e: Exception) {
                        XLog.e("insert error:${e.message}")
                    }
                }
                val bean = ThermalMinuteEntity()
                try {
                    bean.userId = userId
                    bean.thermal = 0f
                    bean.thermalMax = 0f
                    bean.thermalMin = 0f
                    bean.createTime = TimeTool.timeToMinute(System.currentTimeMillis(), 2) // Adjust精确到分
                    bean.updateTime = System.currentTimeMillis()
                    AppDatabase.getInstance().thermalMinDao().insert(bean)
                } catch (e: Exception) {
                    XLog.e("insert error:${e.message}")
                }
delete多余的data
                AppDatabase.getInstance().thermalMinDao()
                    .deleteRepeatVol(userId)
            }
            3 -> {
                val hourTime = TimeTool.timeToMinute(System.currentTimeMillis(), 3)
Check最新时间段有没有dataSynchronize
                val hourVolLatestList =
                    AppDatabase.getInstance().thermalHourDao()
                        .queryByTime(
                            userId = userId,
                            startTime = hourTime,
                            endTime = System.currentTimeMillis(),
                        )
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (hourVolLatestList.isNotEmpty()) {
                    Log.w("chart", "最新时间段已经有Record，不需要Synchronizeupdate")
                    return
                }
                val maxTime =
                    AppDatabase.getInstance().thermalHourDao().queryMaxTime(userId = userId)
                Log.w("chart", "hour latest  time: $maxTime, ${TimeTool.showDateType(maxTime)}")
get要update的时间段data
                val secondVolList =
                    AppDatabase.getInstance().thermalDao()
                        .queryByTime(
                            userId = userId,
                            startTime = maxTime,
                            endTime = hourTime,
                        ) as ArrayList<ThermalEntity>
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (secondVolList.size > 0) {
                    val startTime = TimeTool.showDateType(secondVolList.first().createTime)
                    val endTime = TimeTool.showDateType(secondVolList.last().createTime)
                    Log.w("chart", "要processing${secondVolList.size}个data, start:$startTime, end:$endTime")
                } else {
                    Log.w("chart", "无dataprocessing")
                }
秒data转分data的average值
                val hourVolList = getNewVolData(secondVolList, 3)
add到分data库
                hourVolList.forEach {
                    val bean = ThermalHourEntity()
                    bean.userId = it.userId
                    bean.sn = it.sn
                    bean.thermal = it.thermal
                    bean.thermalMax = it.thermalMax
                    bean.thermalMin = it.thermalMin
                    bean.info = it.info
                    bean.type = it.type
                    bean.createTime = TimeTool.timeToMinute(it.createTime, 3) // Adjust精确到分
                    bean.updateTime = System.currentTimeMillis()
                    AppDatabase.getInstance().thermalHourDao().insert(bean)
                }
                val bean = ThermalHourEntity()
                bean.userId = userId
                bean.thermal = 0f
                bean.thermalMax = 0f
                bean.thermalMin = 0f
                bean.createTime = TimeTool.timeToMinute(System.currentTimeMillis(), 3) // Adjust精确到分
                bean.updateTime = System.currentTimeMillis()
                AppDatabase.getInstance().thermalHourDao().insert(bean)
delete多余的data
                AppDatabase.getInstance().thermalHourDao().deleteRepeatVol(userId)
            }
            4 -> {
                val todayStartTime =
                    TimeTool.timeToMinute(System.currentTimeMillis(), 4) // 天只update到今天凌晨的data
Check今天有没有dataSynchronize
                val todayVolLatestList =
                    AppDatabase.getInstance().thermalDayDao()
                        .queryByTime(
                            userId = userId,
                            startTime = todayStartTime,
                            endTime = System.currentTimeMillis(),
                        )
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (todayVolLatestList.isNotEmpty()) {
今天已经有Record，description已经Synchronize到今天，不需要Synchronizeupdate
                    Log.w("chart", "今天已经有Record，不需要Synchronizeupdate")
                    return
                }
                val maxTime =
                    AppDatabase.getInstance().thermalDayDao().queryMaxTime(userId = userId)
                Log.w("chart", "day latest time: $maxTime, ${TimeTool.showDateType(maxTime)}")
get要update的时间段data
                val secondVolList =
                    AppDatabase.getInstance().thermalDao()
                        .queryByTime(
                            userId = userId,
                            startTime = maxTime,
                            endTime = todayStartTime,
                        ) as ArrayList<ThermalEntity>
秒data转分data的average值
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (secondVolList.size > 0) {
                    val startTime = TimeTool.showDateType(secondVolList.first().createTime)
                    val endTime = TimeTool.showDateType(secondVolList.last().createTime)
                    Log.w("chart", "要processing${secondVolList.size}个data, start:$startTime, end:$endTime")
                } else {
                    Log.w("chart", "无dataprocessing")
                }
                val dayVolList = getNewVolData(secondVolList, 4)
add到分data库
                dayVolList.forEach {
                    val bean = ThermalDayEntity()
                    bean.userId = it.userId
                    bean.sn = it.sn
                    bean.thermal = it.thermal
                    bean.thermalMax = it.thermalMax
                    bean.thermalMin = it.thermalMin
                    bean.info = it.info
                    bean.type = it.type
                    bean.createTime = TimeTool.timeToMinute(it.createTime, 4) // Adjust精确到分
                    bean.updateTime = System.currentTimeMillis()
                    AppDatabase.getInstance().thermalDayDao().insert(bean)
此处只update到今天凌晨
                }
今天的电压值还没Checkend，不能出average值结果，并值adda无效data(0f)，用updateTime来判断已Synchronize到最新的时间节point
                val bean = ThermalDayEntity()
                bean.userId = userId
                bean.thermal = 0f
                bean.thermalMax = 0f
                bean.thermalMin = 0f
                bean.createTime = TimeTool.timeToMinute(System.currentTimeMillis(), 4) // Adjust精确到分
                bean.updateTime = System.currentTimeMillis()
                AppDatabase.getInstance().thermalDayDao().insert(bean)
delete多余的data
                AppDatabase.getInstance().thermalDayDao().deleteRepeatVol(userId)
            }
        }
        syncRun = false
        Log.w("chart", "syncVol end")
    }

/**
 * Chart list utility class for thermal imaging operations.
 * Provides helper functions and common functionality.
 */
    data class ChartList(
        var dataList: ArrayList<ThermalEntity> = arrayListOf(),
        var maxVol: Float = 0f,
        var minVol: Float = 0f,
        var action: Int = 0,
    )
}
