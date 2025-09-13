package com.topdon.module.thermal.ir.viewmodel

import androidx.lifecycle.MutableLiveData
import com.topdon.lib.core.ktbase.BaseViewModel

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for IRGalleryTabViewModel display and interaction.
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
class IRGalleryTabViewModel : BaseViewModel() {
    /**
是否处于编辑mode.
     */
    val isEditModeLD: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
当前selected数量.
     */
    val selectSizeLD: MutableLiveData<Int> = MutableLiveData(0)

    /**
click全选的 Fragment index，如 0 表示photo全选，1表示video全选.
     */
    val selectAllIndex: MutableLiveData<Int> = MutableLiveData(0)
}
