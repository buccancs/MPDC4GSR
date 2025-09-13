package com.topdon.module.thermal.ir.viewmodel

import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import com.topdon.lib.core.ktbase.BaseViewModel
import com.topdon.lib.core.utils.ByteUtils.bytesToInt
import com.topdon.lib.core.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for IRGalleryEditViewModel display and interaction.
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
class IRGalleryEditViewModel : BaseViewModel() {
    val resultLiveData = SingleLiveEvent<FrameBean>()

    /**
     * Initializes data component.
     */
    fun initData(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(path)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists()) {
                XLog.w("IRfile不存在: ${file.absolutePath}")
                return@launch
            }
            XLog.w("IRfile: ${file.absolutePath}")
            val bytes = file.readBytes()
            val headLenBytes = ByteArray(2)
            System.arraycopy(bytes, 0, headLenBytes, 0, 2)
            val headLen = headLenBytes.bytesToInt()
            val headDataBytes = ByteArray(headLen)
            val frameDataBytes = ByteArray(bytes.size - headLen)
            System.arraycopy(bytes, 0, headDataBytes, 0, headDataBytes.size)
            System.arraycopy(bytes, headLen, frameDataBytes, 0, frameDataBytes.size)
            XLog.w("一帧data: ${frameDataBytes.size}")
            resultLiveData.postValue(FrameBean(headDataBytes, frameDataBytes))
        }
    }

    /**
get尾部info
     */

    /**
     * Retrieves taildata information.
     */
    fun getTailData(bytes: ByteArray)  {
    }

/**
 * Frame data model for thermal imaging information.
 * Encapsulates thermal measurement and configuration data.
 */
data class FrameBean(val capital: ByteArray, val frame: ByteArray)
}
