package com.topdon.module.thermal.ir.activity

import android.content.Intent
import android.widget.TextView
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.event.ManualFinishBean
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
dual light校正 - 第1步.
 * Created by LCG on 2023/12/29.
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing ManualStep1Activity functionality for the IRCamera system.
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
class ManualStep1Activity : BaseActivity() {
    // View declarations
    private lateinit var tvManual: TextView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_manual_step1

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views
        tvManual = findViewById(R.id.tv_manual)

        tvManual.setOnClickListener {
            startActivity(Intent(this, ManualStep2Activity::class.java))
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    override fun disConnected() {
        super.disConnected()
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes onManualFinishBean functionality.
     */
    /**
     * Executes onmanualfinishbean operation with thermal imaging domain optimization.
     *
     * @param
     * @param manualFinishBean Parameter for operation (type: ManualFinishBean)
     *
     */
    fun onManualFinishBean(manualFinishBean: ManualFinishBean) {
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }
}
