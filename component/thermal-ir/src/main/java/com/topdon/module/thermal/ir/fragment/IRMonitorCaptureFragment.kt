package com.topdon.module.thermal.ir.fragment

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.ktbase.BaseFragment
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.socket.WebSocketProxy
import com.topdon.lib.core.tools.DeviceTools
import com.topdon.lib.core.tools.ToastTools
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.activity.IRMonitorActivity

/**
 * Specialized thermal imaging component providing IRMonitorCaptureFragment functionality for the IRCamera system.
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
class IRMonitorCaptureFragment : BaseFragment() {
    /**
从上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    // View properties
    private lateinit var animationView: LottieAnimationView
    private lateinit var viewStart: View
    private lateinit var ivIcon: ImageView
    private lateinit var tvStart: TextView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.fragment_ir_monitor_capture

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        isTC007 = arguments?.getBoolean(ExtraKeyConfig.IS_TC007, false) ?: false

        animationView = requireView().findViewById(R.id.animation_view)
        viewStart = requireView().findViewById(R.id.view_start)
        ivIcon = requireView().findViewById(R.id.iv_icon)
        tvStart = requireView().findViewById(R.id.tv_start)

        animationView.setAnimation(if (isTC007) "TC007AnimationJSON.json" else "TDAnimationJSON.json")

        viewStart.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isTC007) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (WebSocketProxy.getInstance().isTC007Connect()) {
                    NavigationManager.getInstance().build(RouterConfig.IR_MONITOR_CAPTURE_07).navigation(requireContext())
                } else {
                    ToastTools.showShort(R.string.device_connect_tip)
                }
            } else {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (DeviceTools.isConnect()) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (DeviceTools.isTC001LiteConnect())
                        {
                            NavigationManager.getInstance().build(RouterConfig.IR_THERMAL_MONITOR_LITE).navigation(requireContext())
                        } else if (DeviceTools.isHikConnect()) {
                        NavigationManager.getInstance().build(RouterConfig.IR_HIK_MONITOR_CAPTURE1).navigation(requireContext())
                    } else
                        {
                            startActivity(Intent(requireContext(), IRMonitorActivity::class.java))
                        }
                } else {
                    ToastTools.showShort(R.string.device_connect_tip)
                }
            }
        }

        /**
         * Executes refreshui operation with thermal imaging domain optimization.
         *
         */
        refreshUI(if (isTC007) WebSocketProxy.getInstance().isTC007Connect() else DeviceTools.isConnect())
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        /**
         * Executes refreshui operation with thermal imaging domain optimization.
         *
         */
        refreshUI(if (isTC007) WebSocketProxy.getInstance().isTC007Connect() else DeviceTools.isConnect())
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
refreshconnectionstate
     */
    /**
     * Executes refreshui operation with thermal imaging domain optimization.
     *
     * @param
     * @param isConnect Parameter for operation (type: Boolean)
     *
     */
    private fun refreshUI(isConnect: Boolean) {
        animationView.isVisible = !isConnect
        ivIcon.isVisible = isConnect
        viewStart.isVisible = isConnect
        tvStart.isVisible = isConnect
    }

    /**
     * Executes connected operation with thermal imaging domain optimization.
     *
     */
    override fun connected() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTC007) {
            /**
             * Executes refreshui operation with thermal imaging domain optimization.
             *
             */
            refreshUI(true)
        }
    }

    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    override fun disConnected() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isTC007) {
            /**
             * Executes refreshui operation with thermal imaging domain optimization.
             *
             */
            refreshUI(false)
        }
    }

    /**
     * Executes onsocketconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTS004 Parameter for operation (type: Boolean)
     *
     */
    override fun onSocketConnected(isTS004: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007 && !isTS004) {
            /**
             * Executes refreshui operation with thermal imaging domain optimization.
             *
             */
            refreshUI(true)
        }
    }

    /**
     * Executes onsocketdisconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTS004 Parameter for operation (type: Boolean)
     *
     */
    override fun onSocketDisConnected(isTS004: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007 && !isTS004) {
            /**
             * Executes refreshui operation with thermal imaging domain optimization.
             *
             */
            refreshUI(false)
        }
    }
}
