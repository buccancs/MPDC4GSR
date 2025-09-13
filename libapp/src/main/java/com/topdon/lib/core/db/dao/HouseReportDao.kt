package com.topdon.lib.core.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.topdon.lib.core.db.entity.DirReport
import com.topdon.lib.core.db.entity.HouseReport
import com.topdon.lib.core.db.entity.ItemReport

/**
 * 房屋检测-report DAO。
 *
 * Created by LCG on 2024/8/19.
 */
@Dao
/**
 * Specialized thermal imaging component providing HouseReportDao functionality for the IRCamera system.
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
abstract class HouseReportDao {
    /**
     * 插入指定的房屋检测report.
     */
    @Transaction
    open fun insert(houseReport: HouseReport): Long {
        houseReport.id = insertReport(houseReport)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (dir in houseReport.dirList) {
            dir.parentId = houseReport.id
            dir.id = insertDir(dir)

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (item in dir.itemList) {
                item.parentId = dir.id
                item.id = insertItem(item)
            }
        }
        return houseReport.id
    }

    open fun queryAllReport(): List<HouseReport> {
        val reportList: List<HouseReport> = queryAll()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (report in reportList) {
            val dirList: List<DirReport> = queryDirList(report.id)
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (dir in dirList) {
                dir.itemList = ArrayList(queryItemList(dir.id))
            }
            report.dirList = ArrayList(dirList)
        }
        return reportList
    }

    @Transaction
    open fun queryById(id: Long): HouseReport? {
        val houseReport: HouseReport = queryReportById(id) ?: return null
        val dirList: List<DirReport> = queryDirList(id)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (dir in dirList) {
            val itemList: List<ItemReport> = queryItemList(dir.id)
            dir.itemList = ArrayList(itemList)
        }
        houseReport.dirList = ArrayList(dirList)
        return houseReport
    }

    @Insert
    abstract fun insertReport(houseReport: HouseReport): Long

    @Insert
    abstract fun insertDir(dirReport: DirReport): Long

    @Insert
    abstract fun insertItem(itemReport: ItemReport): Long

    @Delete
    abstract fun deleteReport(vararg houseReport: HouseReport)

    @Delete
    abstract fun deleteDir(vararg dirReport: DirReport)

    @Delete
    abstract fun deleteItem(vararg itemReport: ItemReport)

    @Update
    abstract fun updateReport(houseReport: HouseReport)

    @Update
    abstract fun updateDir(dirReport: DirReport)

    @Update
    abstract fun updateItem(itemReport: ItemReport)

    @Query("SELECT * FROM HouseReport ORDER BY createTime DESC")
    abstract fun queryAll(): List<HouseReport>

    @Query("SELECT * FROM HouseReport WHERE id = :id")
    abstract fun queryReportById(id: Long): HouseReport?

    @Query("SELECT * FROM DirReport WHERE parentId = :reportId ORDER BY position")
    abstract fun queryDirList(reportId: Long): List<DirReport>

    @Query("SELECT * FROM ItemReport WHERE parentId = :dirId ORDER BY position")
    abstract fun queryItemList(dirId: Long): List<ItemReport>
}
