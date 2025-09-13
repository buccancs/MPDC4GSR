package com.topdon.module.user.activity

import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import com.topdon.lib.core.bean.event.SocketMsgEvent
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.repository.*
import com.topdon.lib.core.socket.SocketCmdUtil
import com.topdon.lib.core.utils.WsCmdConstants
import com.topdon.lib.core.view.TitleView
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.user.R
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import com.topdon.lib.core.R as RCore

// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing TISRActivity functionality for the IRCamera system.
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
class TISRActivity : BaseActivity() {

    // View references - migrated from synthetic views
    private lateinit var titleView: TitleView
    private lateinit var settingItemTisrSelect: SwitchCompat

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_tisr

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views - migrated from synthetic views
        titleView = findViewById(R.id.title_view)
        settingItemTisrSelect = findViewById(R.id.setting_item_tisr_select)

        titleView.setTitleText("TISR")
        settingItemTisrSelect.isChecked = SharedManager.is04TISR
        settingItemTisrSelect.setOnCheckedChangeListener { _, isChecked ->
            /**
             * Executes updatetisr operation with thermal imaging domain optimization.
             *
             */
            updateTISR(if (isChecked) 1 else 0)
            SharedManager.is04TISR = isChecked
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
        lifecycleScope.launch {
            val tisrBean = TS004Repository.getTISR()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (tisrBean?.isSuccess()!!)
                {
                    val isTISR = tisrBean.data?.enable!! == 1
                    settingItemTisrSelect.isChecked = isTISR
                    SharedManager.is04TISR = isTISR
                } else
                {
                    TToast.shortToast(this@TISRActivity, RCore.string.operation_failed_tips)
                }
        }
    }

    /**
     * Executes updateTISR functionality.
     */
    /**
     * Executes updatetisr operation with thermal imaging domain optimization.
     *
     * @param
     * @param state Parameter for operation (type: Int)
     *
     */
    private fun updateTISR(state: Int) {
        lifecycleScope.launch {
            val isSuccess = TS004Repository.setTISR(state)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSuccess)
                {
                } else
                {
                    TToast.shortToast(this@TISRActivity, RCore.string.operation_failed_tips)
                }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes onSocketMsgEvent functionality.
     */
    /**
     * Executes onsocketmsgevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: SocketMsgEvent)
     *
     */
    fun onSocketMsgEvent(event: SocketMsgEvent) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (SocketCmdUtil.getCmdResponse(event.text)) {
            WsCmdConstants.AR_COMMAND_TISR_GET -> { // Get/Retrieve超分state
                try {
                    val webSocketIp = SocketCmdUtil.getIpResponse(event.text)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (webSocketIp == WsCmdConstants.AR_COMMAND_IP)
                        {
                            val data: JSONObject = JSONObject(event.text).getJSONObject("data")
                            val state: Int = data.getInt("state")
                            val isTISR = state == 1
                            settingItemTisrSelect.isChecked = isTISR
                            SharedManager.is04TISR = isTISR
                        }
                } catch (_: Exception) {
                }
            }
        }
    }
}
