package com.topdon.lib.core.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.TimeUtils

/**
 * Specialized thermal imaging component providing HouseBase functionality for the IRCamera system.
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
open class HouseBase {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    /**
     * reportname
     */
    @ColumnInfo
    var name: String = ""

    /**
     * 检测师姓名
     */
    @ColumnInfo
    var inspectorName: String = ""

    /**
     * 房屋详细地址
     */
    @ColumnInfo
    var address: String = ""

    /**
     * 房屋image在本地绝对path
     */
    @ColumnInfo
    var imagePath: String = ""

    /**
     * 建筑年份
     */
    @ColumnInfo
    var year: Int? = null

    /**
     * 建筑area积.
     */
    @ColumnInfo
    var houseSpace: String = ""

    /**
     * 建筑area积单位 0-英亩 1-平方米 2-公顷
     */
    @ColumnInfo
    var houseSpaceUnit: Int = 0

    /**
     * 检测费用
     */
    @ColumnInfo
    var cost: String = ""

    /**
     * 检测费用单位，0-美元USD 1-欧元EUR 2-英镑GBP 3-澳元AUD 4-日元JPY 5-加元CAD 6-新西兰NZD 7-人民币RMB 8-港币HKD
     */
    @ColumnInfo
    var costUnit: Int = 0

    /**
     * 该检测或report由Userselection的“检测时间”时间戳，单位毫秒
     */
    @ColumnInfo
    var detectTime: Long = 0

    /**
     * 该检测或reportcreate时间戳，单位毫秒
     */
    @ColumnInfo
    var createTime: Long = 0

    /**
     * 该检测或reportupdate时间戳，单位毫秒
     */
    @ColumnInfo
    var updateTime: Long = 0

    /**
     * Executes equals operation with thermal imaging domain optimization.
     *
     * @param
     * @param other Parameter for operation (type: Any?)
     *
     */
    override fun equals(other: Any?): Boolean = other is HouseBase && other.id == id

    /**
     * Executes hashcode operation with thermal imaging domain optimization.
     *
     */
    override fun hashCode(): Int = id.toInt()

    /**
     * Get/Retrieve房屋area积单位.
     */
    /**
     * Retrieves the spaceunitstr with optimized performance for thermal imaging operations.
     *
     */
    fun getSpaceUnitStr(): String =
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (houseSpaceUnit) {
            0 -> "ac"
            1 -> "m²"
            else -> "ha"
        }

    /**
     * Get/Retrieve检测费用货币单位.
     */
    /**
     * Retrieves the costunitstr with optimized performance for thermal imaging operations.
     *
     */
    fun getCostUnitStr(): String =
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (costUnit) {
            1 -> "EUR" // 欧元EUR
            2 -> "GBP" // 英镑GBP
            3 -> "AUD" // 澳元AUD
            4 -> "JPY" // 日元JPY
            5 -> "CAD" // 加元CAD
            6 -> "NZD" // 新西兰NZD
            7 -> "RMB" // 人民币RMB
            8 -> "HKD" // 港币HKD
            else -> "USD" // 美元USD
/**
 * Specialized thermal imaging component providing HouseDetect functionality for the IRCamera system.
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
class HouseDetect : HouseBase() {
    /**
     * 该检测或下的目录列表
     */
    @Ignore
    var dirList: ArrayList<DirDetect> = ArrayList()

    /**
     * Returna id 为 0，nameadd (1)，其余property完全一致的新对象.
     */
    fun copyOne(): HouseDetect {
        val newDetect = HouseDetect()
        newDetect.id = 0
        newDetect.name = "$name(1)"
        newDetect.inspectorName = inspectorName
        newDetect.address = address
        newDetect.imagePath = imagePath
        newDetect.year = year
        newDetect.houseSpace = houseSpace
        newDetect.houseSpaceUnit = houseSpaceUnit
        newDetect.cost = cost
        newDetect.costUnit = costUnit
        newDetect.detectTime = detectTime
        newDetect.createTime = createTime
        newDetect.updateTime = updateTime
        return newDetect
    }

    /**
     * Executes toHouseReport functionality.
     */
    /**
     * Executes tohousereport operation with thermal imaging domain optimization.
     *
     */
    fun toHouseReport(): HouseReport {
        val houseReport = HouseReport()
        houseReport.id = 0
        houseReport.name = name
        houseReport.inspectorName = inspectorName
        houseReport.address = address
        houseReport.imagePath = imagePath
        houseReport.year = year
        houseReport.houseSpace = houseSpace
        houseReport.houseSpaceUnit = houseSpaceUnit
        houseReport.cost = cost
        houseReport.costUnit = costUnit
        houseReport.detectTime = detectTime
        houseReport.createTime = createTime
        houseReport.updateTime = updateTime

        val newDirList: ArrayList<DirReport> = ArrayList(dirList.size)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (dirDetect in dirList) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dirDetect.itemList.isNotEmpty()) {
                val dirRepost: DirReport = dirDetect.toDirReport()
/**
 * Specialized thermal imaging component providing HouseReport functionality for the IRCamera system.
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
class HouseReport : HouseBase() {
    /**
     * 检测师签名image（白色笔刷版）在本地绝对path
     */
    @ColumnInfo
    var inspectorWhitePath: String = ""

    /**
     * 检测师签名image（黑色笔刷版）在本地绝对path
     */
    @ColumnInfo
    var inspectorBlackPath: String = ""

    /**
     * 房主签名image（白色笔刷版）在本地绝对path
     */
    @ColumnInfo
    var houseOwnerWhitePath: String = ""

    /**
     * 房主签名image（黑色笔刷版）在本地绝对path
     */
    @ColumnInfo
    var houseOwnerBlackPath: String = ""

    /**
     * 该report下的目录列表
     */
    @Ignore
    var dirList: ArrayList<DirReport> = ArrayList()
}
