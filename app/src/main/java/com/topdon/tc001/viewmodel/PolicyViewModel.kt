package com.topdon.tc001.viewmodel

import androidx.lifecycle.viewModelScope
import com.topdon.lib.core.http.repository.LmsRepository
import com.topdon.lib.core.ktbase.BaseViewModel
import com.topdon.lib.core.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for PolicyViewModel display and interaction.
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
class PolicyViewModel : BaseViewModel() {
    val htmlViewData = SingleLiveEvent<HtmlBean>()

    /**
     * @param type 1: Userserviceprotocol 2: 隐私政策 3: 第三方component
     */
    fun getUrl(type: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val urlType =
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (type) {
                    1 -> 21
                    2 -> 22
                    3 -> 23
                    else -> 21
                }
            val result = LmsRepository.getStatementUrl(urlType.toString())
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (result != null && !result.htmlContent.isNullOrBlank()) {
                htmlViewData.postValue(HtmlBean(body = result.htmlContent, action = 1))
            } else {
                htmlViewData.postValue(HtmlBean())
            }
        }
    }

    data class HtmlBean(val body: String? = null, val action: Int = 0)
}
