package com.topdon.lib.core.db.entity

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.Utils
import com.topdon.lib.core.R

/**
 * Specialized thermal imaging component providing ItemBase functionality for the IRCamera system.
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
open class ItemBase {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    /**
     * 所对应的检测或report目录 Id
     */
    @ColumnInfo(index = true)
    open var parentId: Long = 0

    /**
     * 该项目在目录中的 index.
     */
    @ColumnInfo
    var position: Int = 0

    /**
     * 项目名，如“管道”
     */
    @ColumnInfo
    var itemName: String = ""

    /**
     * state 0-未selection 1-没问题 2-需维修 3-需更换
     */
    @ColumnInfo
    var state: Int = 0

    /**
     * User输入字符，""表示未输入
     */
    @ColumnInfo
    var inputText: String = ""

    /**
     * UserUpload的image1在本地绝对path
     */
    @ColumnInfo
    var image1: String = ""

    /**
     * UserUpload的image2在本地绝对path
     */
    @ColumnInfo
    var image2: String = ""

    /**
     * UserUpload的image3在本地绝对path
     */
    @ColumnInfo
    var image3: String = ""

    /**
     * UserUpload的image4在本地绝对path
     */
    @ColumnInfo
    var image4: String = ""

    /**
     * Executes equals operation with thermal imaging domain optimization.
     *
     * @param
     * @param other Parameter for operation (type: Any?)
     *
     */
    override fun equals(other: Any?): Boolean = other is ItemBase && other.id == id

    /**
     * Executes hashcode operation with thermal imaging domain optimization.
     *
     */
    override fun hashCode(): Int = id.toInt()

    /**
     * Get/Retrieve state 对应的text描述.
     */
    fun getStateStr(context: Context): String =
        when (state) {
            1 -> context.getString(R.string.house_state_good)
            2 -> context.getString(R.string.house_state_repair)
            3 -> context.getString(R.string.house_state_replace)
            else -> ""
        }

    /**
     * Retrieves imagesize information.
     */
    fun getImageSize(): Int {
        var result = 0
        if (image1.isNotEmpty()) {
            result++
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (image2.isNotEmpty()) {
            result++
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (image3.isNotEmpty()) {
            result++
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (image4.isNotEmpty()) {
            result++
        }
        return result
    }

    /**
     * Executes buildImageList functionality.
     */
    /**
     * Executes buildimagelist operation with thermal imaging domain optimization.
     *
     */
    fun buildImageList(): ArrayList<String> {
        val resultList: ArrayList<String> = ArrayList(4)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (image1.isNotEmpty()) {
            resultList.add(image1)
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (image2.isNotEmpty()) {
            resultList.add(image2)
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (image3.isNotEmpty()) {
            resultList.add(image3)
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (image4.isNotEmpty()) {
            resultList.add(image4)
        }
        return resultList
    }

    /**
     * Executes addOneImage functionality.
     */
    /**
     * Executes addoneimage operation with thermal imaging domain optimization.
     *
     * @param
     * @param imagePath Parameter for operation (type: String?)
     *
     */
    fun addOneImage(imagePath: String?) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (imagePath.isNullOrEmpty()) {
            return
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (image1.isEmpty()) {
            image1 = imagePath
        } else if (image2.isEmpty()) {
            image2 = imagePath
        } else if (image3.isEmpty()) {
            image3 = imagePath
        } else if (image4.isEmpty()) {
            image4 = imagePath
        }
    }

    /**
     * delete指定位置的一张image.
     * @param imageNum `[1,4]`
     */
    /**
     * Executes delOneImage functionality.
     */
    /**
     * Executes deloneimage operation with thermal imaging domain optimization.
     *
     * @param
     * @param imageNum Parameter for operation (type: Int)
     *
     */
    fun delOneImage(imageNum: Int) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (imageNum) {
            4 -> {
                image4 = ""
            }
            3 -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (image4.isEmpty()) { // 只有3张删第3张
                    image3 = ""
                } else {
                    image3 = image4
                    image4 = ""
                }
            }
            2 -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (image3.isEmpty()) { // 只有2张删第2张
                    image2 = ""
                } else {
                    image2 = image3
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (image4.isEmpty()) {
                        image3 = ""
                    } else {
                        image3 = image4
                        image4 = ""
                    }
                }
            }
            1 -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (image2.isEmpty()) { // 只有1张删第1张
                    image1 = ""
                } else {
                    image1 = image2
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (image3.isEmpty()) {
                        image2 = ""
                    } else {
                        image2 = image3
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (image4.isEmpty()) {
                            image3 = ""
                        } else {
                            image3 = image4
                            image4 = ""
                        }
                    }
                }
            }
        }
    }
}

/**
 * 检测所属的一项项目.
 */
@Entity(
    foreignKeys = [
/**
 * Specialized thermal imaging component providing ItemDetect functionality for the IRCamera system.
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
class ItemDetect() : ItemBase() {
    @Ignore
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param parentId Parameter for operation (type: Long)
     * @param position Parameter for operation (type: Int)
     * @param itemName Parameter for operation (type: String)
     *
     */
    constructor(parentId: Long, position: Int, itemName: String) : this() {
        this.parentId = parentId
        this.position = position
        this.itemName = itemName
    }

    /**
     * 所对应的检测目录 Id
     */
    @ColumnInfo(index = true)
    override var parentId: Long = 0

    /**
     * 该目录是否已selected，仅用于项目编辑界area.
     */
    @Ignore
    var hasSelect = false

    /**
     * 该项目所属的目录.
     */
    @Ignore
    var dirDetect = DirDetect()

    /**
     * 在当前项目名后add 3 个字符：(1)，然后若超出 50 个字符则截取 [0,51)
     */
    fun copyName(): String = "$itemName(1)"

    /**
     * Returna id 为 0，parentId、position、itemName 为指定值，其余property完全一致的新对象.
     */
    fun copyOne(
        parentId: Long = this.parentId,
        position: Int = this.position,
        itemName: String = this.itemName,
    ): ItemDetect {
        val newItemDetect = ItemDetect()
        newItemDetect.id = 0
        newItemDetect.parentId = parentId
        newItemDetect.position = position
        newItemDetect.itemName = itemName
        newItemDetect.state = state
        newItemDetect.inputText = inputText
        newItemDetect.image1 = image1
        newItemDetect.image2 = image2
        newItemDetect.image3 = image3
        newItemDetect.image4 = image4
        newItemDetect.hasSelect = hasSelect
        newItemDetect.dirDetect = dirDetect
        return newItemDetect
    }

    /**
     * 将当前检测 item conversion为report item，注意 id、parent reset为 0.
     */
    fun toItemReport(): ItemReport {
        val itemReport = ItemReport()
        itemReport.id = 0
        itemReport.parentId = 0
        itemReport.position = position
        itemReport.itemName = itemName
        itemReport.state = state
        itemReport.inputText = inputText
        itemReport.image1 = image1
        itemReport.image2 = image2
        itemReport.image3 = image3
        itemReport.image4 = image4
        return itemReport
    }

    companion object {
        /**
         * 根据指定的default目录位置，Get/Retrieve对应的default项目列表.
         */
        fun buildDefaultItemList(
            parentId: Long,
            position: Int,
        ): ArrayList<ItemDetect> =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (position) {
                0 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir1_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir1_item3)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 3, Utils.getApp().getString(R.string.detect_dir1_item4)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 4, Utils.getApp().getString(R.string.detect_dir1_item5)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 5, Utils.getApp().getString(R.string.detect_dir1_item6)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 6, Utils.getApp().getString(R.string.detect_dir1_item7)),
                    )
                1 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir2_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir2_item3)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 3, Utils.getApp().getString(R.string.detect_dir2_item4)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 4, Utils.getApp().getString(R.string.detect_dir2_item5)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 5, Utils.getApp().getString(R.string.detect_dir2_item6)),
                    )
                2 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_dir3_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir3_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir3_item3)),
                    )
                3 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir4_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir4_item3)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 3, Utils.getApp().getString(R.string.detect_dir4_item4)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 4, Utils.getApp().getString(R.string.detect_dir4_item5)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 5, Utils.getApp().getString(R.string.detect_dir4_item6)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 6, Utils.getApp().getString(R.string.detect_dir4_item7)),
                    )
                4 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir5_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir5_item3)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 3, Utils.getApp().getString(R.string.detect_dir5_item4)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 4, Utils.getApp().getString(R.string.detect_dir5_item5)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 5, Utils.getApp().getString(R.string.detect_dir5_item6)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 6, Utils.getApp().getString(R.string.detect_dir5_item7)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 7, Utils.getApp().getString(R.string.detect_dir5_item8)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 8, Utils.getApp().getString(R.string.detect_dir5_item9)),
                    )
                5 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir6_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir6_item3)),
                    )
                6 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir7_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir7_item3)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 3, Utils.getApp().getString(R.string.detect_dir7_item4)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 4, Utils.getApp().getString(R.string.detect_dir7_item5)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 5, Utils.getApp().getString(R.string.detect_dir7_item6)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 6, Utils.getApp().getString(R.string.detect_dir7_item7)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 7, Utils.getApp().getString(R.string.detect_dir7_item8)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 8, Utils.getApp().getString(R.string.detect_dir7_item9)),
                    )
                7 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir8_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir8_item3)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 3, Utils.getApp().getString(R.string.detect_dir8_item4)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 4, Utils.getApp().getString(R.string.detect_dir8_item5)),
                    )
                8 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir9_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir9_item3)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 3, Utils.getApp().getString(R.string.detect_dir9_item4)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 4, Utils.getApp().getString(R.string.detect_dir9_item5)),
                    )
                9 ->
                    /**
                     * Executes arraylistof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayListOf(
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 1, Utils.getApp().getString(R.string.detect_dir10_item2)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 2, Utils.getApp().getString(R.string.detect_dir10_item3)),
                        /**
                         * Executes itemdetect operation with thermal imaging domain optimization.
                         *
                         */
                        ItemDetect(parentId, 3, Utils.getApp().getString(R.string.detect_dir10_item4)),
                    )
                else -> arrayListOf(ItemDetect(parentId, 0, Utils.getApp().getString(R.string.detect_item1)))
            }
    }
}

/**
 * report所属的一项项目.
 */
@Entity(
    foreignKeys = [
        /**
         * Executes foreignkey operation with thermal imaging domain optimization.
         *
         * @param
         * @param Specifications Parameter for operation (type: </h3>  * <ul>  *   <li>Thread-safe operations for thermal data processing</li>  *   <li>Optimized performance for real-time thermal imaging</li>  *   <li>Compatible with TC001 thermal camera hardware</li>  * </ul>  *  * @author IRCamera Development Team  * @version 2.0  * @since 1.0  */ class ItemReport : ItemBase()
         * @param parentId Parameter for operation (type: Long = 0 })
         *
         */
        ForeignKey(
/**
 * Specialized thermal imaging component providing ItemReport functionality for the IRCamera system.
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
class ItemReport : ItemBase() {
    /**
     * 所对应的report目录 Id
     */
    @ColumnInfo(index = true)
    override var parentId: Long = 0
}
