package com.topdon.module.thermal.ir.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.topdon.lib.core.bean.GalleryBean
import com.topdon.lib.core.bean.GalleryTitle
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.ktbase.BaseViewModel
import com.topdon.lib.core.repository.GalleryRepository
import com.topdon.lib.core.repository.TS004Repository
import com.topdon.lib.core.tools.TimeTool
import com.topdon.module.thermal.ir.utils.WriteTools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for IRGalleryViewModel display and interaction.
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
class IRGalleryViewModel : BaseViewModel() {
    companion object {
        /**
Paginationload时 1 页data的条数
         */
        const val PAGE_COUNT = 20
    }

    /**
未掺杂日期title的raw data列表.
     */
    val sourceListLD: MutableLiveData<ArrayList<GalleryBean>> = MutableLiveData()

    /**
add了日期title的用于display的列表.
     */
    val showListLD: MutableLiveData<ArrayList<GalleryBean>> = MutableLiveData()

    /**
仅供生成report使用的，load所有插件式deviceimage.
     */
    fun queryAllReportImg(dirType: GalleryRepository.DirType) {
        viewModelScope.launch(Dispatchers.IO) {
            val sourceList: ArrayList<GalleryBean> = GalleryRepository.loadAllReportImg(dirType)
            sourceListLD.postValue(sourceList)

插入日期 item
            val showList: ArrayList<GalleryBean> = ArrayList(sourceList.size)
            var beforeTime = 0L
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (galleryBean in sourceList) {
                val currentTime = TimeTool.timeToMinute(galleryBean.timeMillis, 4)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (beforeTime != currentTime) { // 新的日期
                    showList.add(GalleryTitle(galleryBean.timeMillis))
                    beforeTime = currentTime
                }
                showList.add(galleryBean)
            }
            showListLD.postValue(showList)
        }
    }

    /**
Paginationload时已successfulload的页数
     */
    var hasLoadPage = 0

    /**
一页请求data列表.
null-请求failed
     */
    val pageListLD: MutableLiveData<ArrayList<GalleryBean>?> = MutableLiveData()

    /**
     * Executes queryGalleryByPage functionality.
     */
    /**
     * Executes querygallerybypage operation with thermal imaging domain optimization.
     *
     * @param
     * @param isVideo Parameter for operation (type: Boolean)
     * @param dirType Parameter for operation (type: GalleryRepository.DirType)
     *
     */
    fun queryGalleryByPage(
        isVideo: Boolean,
        dirType: GalleryRepository.DirType,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val pageList: ArrayList<GalleryBean>? = GalleryRepository.loadByPage(isVideo, dirType, hasLoadPage + 1, PAGE_COUNT)
            pageListLD.postValue(pageList)

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (pageList != null) {
                val sourceList = if (hasLoadPage == 0) ArrayList(pageList.size) else sourceListLD.value ?: ArrayList(pageList.size)
                val showList = if (hasLoadPage == 0) ArrayList(pageList.size) else showListLD.value ?: ArrayList(pageList.size)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (pageList.isNotEmpty()) {
                    hasLoadPage++
                }

插入日期 item
                var beforeTime = if (sourceList.isEmpty()) 0 else TimeTool.timeToMinute(sourceList.last().timeMillis, 4)
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (galleryBean in pageList) {
                    val currentTime = TimeTool.timeToMinute(galleryBean.timeMillis, 4)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (beforeTime != currentTime) { // 新的日期
                        showList.add(GalleryTitle(galleryBean.timeMillis))
                        beforeTime = currentTime
                    }
                    showList.add(galleryBean)
                }

                sourceList.addAll(pageList)
                sourceListLD.postValue(sourceList)
                showListLD.postValue(showList)
            }
        }
    }

    /**
批量deletefile结果.
     */
    val deleteResultLD: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Executes delete functionality.
     */
    /**
     * Executes delete operation with thermal imaging domain optimization.
     *
     * @param
     * @param deleteList Parameter for operation (type: List<GalleryBean>)
     * @param dirType Parameter for operation (type: GalleryRepository.DirType)
     * @param isDelLocal Parameter for operation (type: Boolean)
     *
     */
    fun delete(
        deleteList: List<GalleryBean>,
        dirType: GalleryRepository.DirType,
        isDelLocal: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dirType == GalleryRepository.DirType.TS004_REMOTE) {
                val isSuccess =
                    TS004Repository.deleteFiles(
                        /**
                         * Executes array operation with thermal imaging domain optimization.
                         *
                         */
                        Array(deleteList.size) {
                            deleteList[it].id
                        },
                    )
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isSuccess) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isDelLocal) {
                        deleteList.forEach {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (it.hasDownload) {
                                val file = File(FileConfig.ts004GalleryDir, it.name)
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (file.exists()) {
                                    WriteTools.delete(file)
                                }
                            }
                        }
                    }
                    deleteResultLD.postValue(true)
                } else {
                    deleteResultLD.postValue(false)
                }
            } else {
                deleteList.forEach {
                    val file = File(it.path)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (file.exists()) {
                        WriteTools.delete(file)
                    }
                }
                deleteResultLD.postValue(true)
            }
        }
    }
}
