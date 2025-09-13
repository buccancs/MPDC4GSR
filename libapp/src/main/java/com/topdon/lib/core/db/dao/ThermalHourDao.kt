package com.topdon.lib.core.db.dao

import androidx.room.*
import com.topdon.lib.core.db.entity.ThermalHourEntity

@Dao
/**
 * Specialized thermal imaging component providing ThermalHourDao functionality for the IRCamera system.
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
interface ThermalHourDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    /**
     * Executes insert functionality.
     */
    /**
     * Executes insert operation with thermal imaging domain optimization.
     *
     * @param
     * @param entity Parameter for operation (type: ThermalHourEntity)
     *
     */
    fun insert(entity: ThermalHourEntity): Long

    @Query(
        "SELECT * FROM thermal_hour WHERE user_id = :userId AND create_time >= :startTime AND create_time <= :endTime ORDER BY create_time",
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
     *
     */
    fun queryByTime(
        userId: String,
        startTime: Long,
        endTime: Long,
    ): List<ThermalHourEntity>

    @Query(
        "SELECT * FROM thermal_hour WHERE user_id = :userId AND create_time >= :startTime AND create_time <= :endTime AND type = :type ORDER BY create_time",
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
    ): List<ThermalHourEntity>

    @Query(
        "SELECT COALESCE(MAX(thermal_max), 0.0) FROM thermal_hour WHERE user_id = :userId AND create_time >= :startTime AND create_time <= :endTime",
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
        "SELECT COALESCE(MIN(thermal_min), 0.0) FROM thermal_hour WHERE user_id = :userId AND create_time >= :startTime AND create_time <= :endTime",
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

    @Query("SELECT COALESCE(MAX(create_time), 0) FROM thermal_hour WHERE user_id = :userId")
    /**
     * Executes queryMaxTime functionality.
     */
    /**
     * Executes querymaxtime operation with thermal imaging domain optimization.
     *
     * @param
     * @param userId Parameter for operation (type: String)
     *
     */
    fun queryMaxTime(userId: String): Long

    @Query(
        "DELETE FROM thermal_hour WHERE user_id = :userId AND id NOT IN (SELECT MAX(id) FROM thermal_hour WHERE user_id = :userId GROUP BY create_time)",
    )
    /**
     * Executes deleteRepeatVol functionality.
     */
    /**
     * Executes deleterepeatvol operation with thermal imaging domain optimization.
     *
     * @param
     * @param userId Parameter for operation (type: String)
     *
     */
    fun deleteRepeatVol(userId: String)

    @Query("DELETE FROM thermal_hour WHERE user_id = :userId")
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
}
