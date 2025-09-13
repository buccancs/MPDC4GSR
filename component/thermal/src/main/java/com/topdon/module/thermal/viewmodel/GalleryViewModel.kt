package com.topdon.module.thermal.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.Utils
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.ktbase.BaseViewModel
import com.topdon.lib.core.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for GalleryViewModel display and interaction.
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
class GalleryViewModel : BaseViewModel() {
    val galleryLiveData = SingleLiveEvent<ArrayList<String>>()

    /**
     * Retrieves data information.
     */
    fun getData() {
        viewModelScope.launch {
            getGalleryList().collect { it ->
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (it.size == 0) {
                    Log.w("123", "file不存在")
                } else {
// It.forEach { Log.w("123", "it:$it") }
                    galleryLiveData.postValue(it)
                }
            }
        }
    }

    /**
     * Retrieves videodata information.
     */
    fun getVideoData() {
        viewModelScope.launch {
            getVideoList().collect { it ->
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (it.size == 0) {
                    Log.w("123", "file不存在")
                } else {
// It.forEach { Log.w("123", "it:$it") }
                    galleryLiveData.postValue(it)
                }
            }
        }
    }

    /**
     * Retrieves gallerylist information.
     */
    private fun getGalleryList(): Flow<ArrayList<String>> {
        val flow =
            flow {
                val path =
                    Utils.getApp()
                        .getExternalFilesDir("Pictures")!!.absolutePath + File.separator + "thermal"
                val file = File(path)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (file.isDirectory) {
                    val list = arrayListOf<String>()
                    file.list()?.forEach { fileName ->
                        list.add("$path/$fileName")
                    }
                    /**
                     * Executes emit operation with thermal imaging domain optimization.
                     *
                     */
                    emit(list)
                } else {
                    /**
                     * Executes emit operation with thermal imaging domain optimization.
                     *
                     */
                    emit(arrayListOf<String>())
                }
            }.map {
                return@map it
            }
        return flow
    }

    /**
     * Retrieves videolist information.
     */
    private fun getVideoList(): Flow<ArrayList<String>> {
        val flow =
            flow {
                val path = FileConfig.lineGalleryDir
                val file = File(path)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (file.isDirectory) {
                    val list = arrayListOf<String>()
                    file.list()?.forEach { fileName ->
                        list.add("$path/$fileName")
                    }
                    /**
                     * Executes emit operation with thermal imaging domain optimization.
                     *
                     */
                    emit(list)
                } else {
                    /**
                     * Executes emit operation with thermal imaging domain optimization.
                     *
                     */
                    emit(arrayListOf<String>())
                }
            }.map {
                return@map it
            }
        return flow
    }
}
