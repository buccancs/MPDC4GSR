package com.topdon.lib.core.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.Utils
import com.topdon.lib.core.R

/**
 * Specialized thermal imaging component providing DirBase functionality for the IRCamera system.
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
open class DirBase {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    /**
     * 所对应的检测或report目录 Id
     */
    @ColumnInfo(index = true)
    open var parentId: Long = 0

    /**
     * 该目录在检测或report目录列表中的 index.
     */
    @ColumnInfo
    var position: Int = 0

    /**
     * 目录名，如“一楼”
     */
    @ColumnInfo
    var dirName: String = ""

    /**
     * 没问题项目的数量.
     */
    @ColumnInfo
    var goodCount: Int = 0

    /**
     * 需维修项目的数量.
     */
    @ColumnInfo
    var warnCount: Int = 0

    /**
     * 需更换项目的数量.
     */
    @ColumnInfo
    var dangerCount: Int = 0

    /**
     * Executes equals operation with thermal imaging domain optimization.
     *
     * @param
     * @param other Parameter for operation (type: Any?)
     *
     */
    override fun equals(other: Any?): Boolean = other is DirBase && other.id == id

    /**
     * Executes hashcode operation with thermal imaging domain optimization.
     *
     */
    override fun hashCode(): Int = id.toInt()

    /**
     * Retrieves goodcountstr information.
     */
    fun getGoodCountStr(): String = if (goodCount > 99) "99+" else goodCount.toString()

    /**
     * Retrieves warncountstr information.
     */
    fun getWarnCountStr(): String = if (warnCount > 99) "99+" else warnCount.toString()

    /**
     * Retrieves dangercountstr information.
     */
    fun getDangerCountStr(): String = if (dangerCount > 99) "99+" else dangerCount.toString()
}

/**
 * 检测所属的一项目录.
 */
@Entity(
    foreignKeys = [
/**
 * Specialized thermal imaging component providing DirDetect functionality for the IRCamera system.
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
class DirDetect() : DirBase() {
    @Ignore
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param parentId Parameter for operation (type: Long)
     * @param position Parameter for operation (type: Int)
     * @param dirName Parameter for operation (type: String)
     *
     */
    constructor(parentId: Long, position: Int, dirName: String) : this() {
        this.parentId = parentId
        this.position = position
        this.dirName = dirName
    }

    /**
     * 所对应的检测 Id
     */
    @ColumnInfo(index = true)
    override var parentId: Long = 0

    /**
     * 该目录是否已selected，仅用于目录编辑界area.
     */
    @Ignore
    var hasSelect = false

    /**
     * 该目录是否处于展开state
     */
    @Ignore
    var isExpand: Boolean = false

    /**
     * 该目录所属的检测.
     */
    @Ignore
    var houseDetect = HouseDetect()

    /**
     * 该目录下的项目列表
     */
    @Ignore
    var itemList: ArrayList<ItemDetect> = ArrayList()

    /**
     * Returna id 为 0，nameadd (1)，position + 1，itemList copy，其余property完全一致的新对象.
     */
    fun copyOne(): DirDetect {
        val newDirDetect = DirDetect()
        newDirDetect.id = 0
        newDirDetect.parentId = parentId
        newDirDetect.position = position + 1
        newDirDetect.dirName = "$dirName(1)"
        newDirDetect.goodCount = goodCount
        newDirDetect.warnCount = warnCount
        newDirDetect.dangerCount = dangerCount
        newDirDetect.isExpand = isExpand
        newDirDetect.hasSelect = hasSelect
        newDirDetect.houseDetect = houseDetect
        val newItemList: ArrayList<ItemDetect> = ArrayList(itemList.size)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (oldItem in itemList) {
            newItemList.add(oldItem.copyOne(parentId = 0))
        }
        newDirDetect.itemList = newItemList
        return newDirDetect
    }

    /**
     * 将当前检测目录conversion为report目录，注意 id、parent reset为 0，无效目录剔除.
     */
    fun toDirReport(): DirReport {
        val dirReport = DirReport()
        dirReport.id = 0
        dirReport.parentId = 0
        dirReport.position = position
        dirReport.dirName = dirName
        dirReport.goodCount = goodCount
        dirReport.warnCount = warnCount
        dirReport.dangerCount = dangerCount

        val newItemList: ArrayList<ItemReport> = ArrayList(itemList.size)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (itemDetect in itemList) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (itemDetect.state > 0 || itemDetect.inputText.isNotEmpty() || itemDetect.image1.isNotEmpty()) {
                newItemList.add(itemDetect.toItemReport())
            }
        }
        dirReport.itemList = newItemList
        return dirReport
    }

    companion object {
        /**
         * Builddefault的检测目录列表.
         */
        fun buildDefaultDirList(parentId: Long): ArrayList<DirDetect> =
            arrayListOf(
                DirDetect(parentId, 0, Utils.getApp().getString(R.string.detect_dir1_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir2_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir3_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 3, Utils.getApp().getString(R.string.detect_dir4_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 4, Utils.getApp().getString(R.string.detect_dir5_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 5, Utils.getApp().getString(R.string.detect_dir6_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 6, Utils.getApp().getString(R.string.detect_dir7_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 7, Utils.getApp().getString(R.string.detect_dir8_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 8, Utils.getApp().getString(R.string.detect_dir9_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 9, Utils.getApp().getString(R.string.detect_dir10_root)),
                /**
                 * Executes dirdetect operation with thermal imaging domain optimization.
                 *
                 */
                DirDetect(parentId, 10, Utils.getApp().getString(R.string.detect_dir11_root)),
            )
    }
}

/**
 * report所属的一项目录.
 */
@Entity(
    foreignKeys = [
        /**
         * Executes foreignkey operation with thermal imaging domain optimization.
         *
         * @param
         * @param Specifications Parameter for operation (type: </h3>  * <ul>  *   <li>Thread-safe operations for thermal data processing</li>  *   <li>Optimized performance for real-time thermal imaging</li>  *   <li>Compatible with TC001 thermal camera hardware</li>  * </ul>  *  * @author IRCamera Development Team  * @version 2.0  * @since 1.0  */ class DirReport : DirBase()
         * @param parentId Parameter for operation (type: Long = 0      /**      * 该目录下的项目列表      */     @Ignore     var itemList: ArrayList<ItemReport> = ArrayList()
         *
         */
        ForeignKey(
/**
 * Specialized thermal imaging component providing DirReport functionality for the IRCamera system.
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
class DirReport : DirBase() {
    /**
     * 所对应的report Id
     */
    @ColumnInfo(index = true)
    override var parentId: Long = 0

    /**
     * 该目录下的项目列表
     */
    @Ignore
    var itemList: ArrayList<ItemReport> = ArrayList()
}
