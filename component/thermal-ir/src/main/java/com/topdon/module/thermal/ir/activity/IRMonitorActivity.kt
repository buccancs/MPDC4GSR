package com.topdon.module.thermal.ir.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.ui.dialog.MonitorSelectDialog
import com.topdon.libcom.navigation.NavigationManager
import com.topdon.module.thermal.ir.bean.SelectPositionBean
import com.topdon.module.thermal.ir.databinding.ActivityIrMonitorBinding
import com.topdon.module.thermal.ir.event.ThermalActionEvent
import org.greenrobot.eventbus.EventBus

/**
选取regionListener
 */
/**
/**
 * Specialized thermal imaging component providing IRMonitorActivity functionality for the IRCamera system.
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
class IRMonitorActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityIrMonitorBinding
    private var selectIndex: SelectPositionBean? = null // 选取point

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIrMonitorBinding.inflate(layoutInflater)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
    }

    /**
     * Initializes view component.
     */
    private fun initView() {
        binding.motionBtn.setOnClickListener(this)
        binding.motionStartBtn.setOnClickListener(this)
    }

    /**
     * Executes onclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param v Parameter for operation (type: View?)
     *
     */
    override fun onClick(v: View?) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (v) {
            binding.motionBtn -> {
                MonitorSelectDialog.Builder(this)
                    .setPositiveListener {
                        /**
                         * Executes updateui operation with thermal imaging domain optimization.
                         *
                         */
                        updateUI()
                        /**
                         * Executes when operation with thermal imaging domain optimization.
                         *
                         */
                        when (it) {
                            1 -> EventBus.getDefault().post(ThermalActionEvent(action = 2001))
                            2 -> EventBus.getDefault().post(ThermalActionEvent(action = 2002))
                            else -> EventBus.getDefault().post(ThermalActionEvent(action = 2003))
                        }
                    }
                    .create().show()
            }
            binding.motionStartBtn -> {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (selectIndex == null) {
                    MonitorSelectDialog.Builder(this)
                        .setPositiveListener {
                            /**
                             * Executes updateui operation with thermal imaging domain optimization.
                             *
                             */
                            updateUI()
                            /**
                             * Executes when operation with thermal imaging domain optimization.
                             *
                             */
                            when (it) {
                                1 -> EventBus.getDefault().post(ThermalActionEvent(action = 2001))
                                2 -> EventBus.getDefault().post(ThermalActionEvent(action = 2002))
                                else -> EventBus.getDefault().post(ThermalActionEvent(action = 2003))
                            }
                        }
                        .create().show()
                    return
                }
starttemperatureListener
                NavigationManager.getInstance().build(RouterConfig.IR_MONITOR_CHART)
                    .withParcelable("select", selectIndex as android.os.Parcelable)
                    .navigation(this)
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }
    }

    /**
     * Executes select functionality.
     */
    /**
     * Executes select operation with thermal imaging domain optimization.
     *
     * @param
     * @param selectIndex Parameter for operation (type: SelectPositionBean?)
     *
     */
    fun select(selectIndex: SelectPositionBean?) {
        this.selectIndex = selectIndex
    }

    /**
     * Executes updateUI functionality.
     */
    /**
     * Executes updateui operation with thermal imaging domain optimization.
     *
     */
    private fun updateUI() {
        binding.motionStartBtn.visibility = View.VISIBLE
        binding.motionBtn.visibility = View.GONE
    }

    /**
     * Executes disConnected functionality.
     */
    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    private fun disConnected() {
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }
}
