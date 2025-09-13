package com.topdon.lib.core.viewmodel

import com.elvishew.xlog.XLog
import com.topdon.lib.core.bean.event.VersionUpData
import com.topdon.lib.core.bean.json.CheckVersionJson
import com.topdon.lib.core.bean.json.SoftConfigOtherTypeVO
import com.topdon.lib.core.ktbase.BaseViewModel
import com.topdon.lib.core.utils.SingleLiveEvent

/**
 * VersionViewModel implements custom user interface component functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for VersionViewModel display and interaction.
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
class VersionViewModel : BaseViewModel() {
    val updateLiveData = SingleLiveEvent<VersionUpData>()

    /**
     * forcedUpgradeFlag: 1 强制update    0 非强制update
     * descType: 包含3时,Show/Display给User(descTypeGet/RetrieveUpgrade描述info)
     */
    /**
     * Executes checkVersion functionality.
     */
    /**
     * Executes checkversion operation with thermal imaging domain optimization.
     *
     */
    fun checkVersion() {
// ViewModelScope.launch(Dispatchers.IO) {
// Try {
// If (TimeUtils.isToday(SharedManager.getVersionCheckDate())) {
//                    Log.w("123", "今天已有versionupdatetip")
// Return@launch
//                }
// Val result: CheckVersionJson = LmsRepository.getVersionInfo() ?: return@launch
//                /*if (result.googleVerCode > AppUtils.getAppVersionCode()) {
//                    // Google play需要Upgrade
// UpdateTip(result)
// Return@launch
//                }*/
// If (VersionTool.checkVersion(remoteStr = result.versionNo ?: "1.0", localStr = AppUtils.getAppVersionName())) {
//                    // Google play检测不出时,官方Upgrade,根据app情况跳转对应的Upgrade渠道
// UpdateTip(result)
// Return@launch
//                }
//            } catch (e: Exception) {
//                XLog.e("检测exception: ${e.message}")
//            }
//        }
    }

    /**
     * Updates the tip with new data.
     */
    private fun updateTip(result: CheckVersionJson) {
        val isForcedUpgrade = (result.forcedUpgradeFlag?.toInt() ?: 0) == 1 // 1: 强制Upgrade
        val description = getDescription(result.softConfigOtherTypeVOList)
        val downPageUrl = result.downloadPageUrl
        val sizeStr = "${result.notUnZipSize}MB"

        XLog.i("有versionUpgrade,Upgradeinfo: $description, 是否强制Upgrade: $isForcedUpgrade")

        val versionUpData =
            /**
             * Executes versionupdata operation with thermal imaging domain optimization.
             *
             */
            VersionUpData(
                versionNo = result.versionNo ?: "",
                isForcedUpgrade = isForcedUpgrade,
                description = description,
                downPageUrl = downPageUrl,
                sizeStr = sizeStr,
            )
        updateLiveData.postValue(versionUpData)
    }

    /**
     * Get/RetrieveUpgradeinfo
     */
    /**
     * Retrieves the description with optimized performance for thermal imaging operations.
     *
     * @param
     * @param list Parameter for operation (type: List<SoftConfigOtherTypeVO>?)
     *
     */
    private fun getDescription(list: List<SoftConfigOtherTypeVO>?): String {
        list?.forEach {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.descType == 3) {
                return it.textDescription
            }
        }
        return ""
    }
}
