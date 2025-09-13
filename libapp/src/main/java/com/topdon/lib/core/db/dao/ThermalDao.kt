package com.topdon.lib.core.db.dao

import androidx.room.*
import com.topdon.lib.core.db.entity.ThermalEntity

@Dao
/**
 * Specialized thermal imaging component providing ThermalDao functionality for the IRCamera system.
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
interface ThermalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    /**
     * Executes insert functionality.
     */
    /**
     * Executes insert operation with thermal imaging domain optimization.
     *
     * @param
     * @param entity Parameter for operation (type: ThermalEntity)
     *
     */
    fun insert(entity: ThermalEntity): Long

    @Query("SELECT type AS type, start_time AS startTime, count(*) AS duration FROM thermal GROUP BY start_time ORDER BY start_time DESC")
    /**
     * Executes queryRecordList functionality.
     */
    /**
     * Executes queryrecordlist operation with thermal imaging domain optimization.
     *
     */
    fun queryRecordList(): List<Record>

    @Query("SELECT * FROM thermal WHERE start_time = :startTime ORDER BY create_time")
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
    fun queryDetail(startTime: Long): List<ThermalEntity>

    @Query("DELETE FROM thermal where start_time = :startTime")
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
    fun delDetail(startTime: Long)

    // DeleteUserdata
    @Query("delete from thermal where user_id = :userId")
    /**
     * Executes deleteByUserId functionality.
     */
    /**
     * Executes deletebyuserid operation with thermal imaging domain optimization.
     *
     * @param
     * @param userId Parameter for operation (type: String)
     *
     */
    fun deleteByUserId(userId: String)

    // Delete无用0data
    @Query(
        "delete from thermal where user_id = :userId and thermal=0 and thermal_max=0 and thermal_min=0 and create_time<(select max(create_time) from thermal where thermal=0 and thermal_max=0 and thermal_min=0)",
    )
    /**
     * Executes deleteZero functionality.
     */
    /**
     * Executes deletezero operation with thermal imaging domain optimization.
     *
     * @param
     * @param userId Parameter for operation (type: String)
     *
     */
    fun deleteZero(userId: String)

    // New methods needed by LogViewModel
    @Query("SELECT * FROM thermal WHERE user_id = :userId AND create_time >= :startTime AND create_time <= :endTime ORDER BY create_time")
    /**
     * Retrieves thermalbydate information.
     */
    fun getThermalByDate(
        userId: String,
        startTime: Long,
        endTime: Long,
    ): List<ThermalEntity>

    @Query("SELECT * FROM thermal WHERE user_id = :userId ORDER BY create_time")
    /**
     * Retrieves allthermalbydate information.
     */
    fun getAllThermalByDate(userId: String): List<ThermalEntity>

    @Query("SELECT * FROM thermal WHERE user_id = :userId AND create_time >= :startTime AND create_time <= :endTime ORDER BY create_time")
    /**
     * Executes queryByTime functionality.
     */
    /**
     * Executes querybytime operation with thermal imaging domain optimization.
     *
     * @param
     * @param userId Parameter for operation (type: String)
     * @param startTime Parameter for operation (type: Long)
     * @param endTime Parameter for operation (type: Long)
     *
     */
    fun queryByTime(
        userId: String,
        startTime: Long,
        endTime: Long,
    ): List<ThermalEntity>

    @Query(
        "SELECT * FROM thermal WHERE user_id = :userId AND create_time >= :startTime AND create_time <= :endTime AND type = :type ORDER BY create_time",
    )
    /**
     * Executes queryByTime functionality.
     */
    /**
     * Executes querybytime operation with thermal imaging domain optimization.
     *
     * @param
     * @param userId Parameter for operation (type: String)
     * @param startTime Parameter for operation (type: Long)
     * @param endTime Parameter for operation (type: Long)
     * @param type Parameter for operation (type: String)
     *
     */
    fun queryByTime(
        userId: String,
        startTime: Long,
        endTime: Long,
        type: String,
    ): List<ThermalEntity>

    @Query(
        "SELECT COALESCE(MAX(thermal_max), 0.0) FROM thermal WHERE user_id = :userId AND create_time >= :startTime AND create_time <= :endTime",
    )
    /**
     * Executes queryByTimeMax functionality.
     */
    /**
     * Executes querybytimemax operation with thermal imaging domain optimization.
     *
     * @param
     * @param userId Parameter for operation (type: String)
     * @param startTime Parameter for operation (type: Long)
     * @param endTime Parameter for operation (type: Long)
     *
     */
    fun queryByTimeMax(
        userId: String,
        startTime: Long,
        endTime: Long,
    ): Float

    @Query(
        "SELECT COALESCE(MIN(thermal_min), 0.0) FROM thermal WHERE user_id = :userId AND create_time >= :startTime AND create_time <= :endTime",
    )
    /**
     * Executes queryByTimeMin functionality.
     */
    /**
     * Executes querybytimemin operation with thermal imaging domain optimization.
     *
     * @param
     * @param userId Parameter for operation (type: String)
     * @param startTime Parameter for operation (type: Long)
     * @param endTime Parameter for operation (type: Long)
     *
     */
    fun queryByTimeMin(
        userId: String,
        startTime: Long,
        endTime: Long,
    ): Float

    data class Record(
        var type: String? = "point", // Point-point line-line fence-area
        var startTime: Long = 0, // Start时刻时间戳，单位毫秒
        var duration: Int = 0,
        @Ignore
        var showTitle: Boolean = false,
    )
}
