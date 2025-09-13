package com.topdon.module.thermal.ir.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.fragment.IRCorrectionFragment

/**
 *
锅盖矫正
 * @author: CaiSongL
 * @date: 2023/8/4 9:06
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing IRCorrectionThreeActivity functionality for the IRCamera system.
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
class IRCorrectionThreeActivity : BaseActivity() {
    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_ir_correction_three

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment: IRCorrectionFragment =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (savedInstanceState == null) {
                /**
                 * Executes ircorrectionfragment operation with thermal imaging domain optimization.
                 *
                 */
                IRCorrectionFragment()
            } else {
                supportFragmentManager.findFragmentById(R.id.fragment_container_view) as IRCorrectionFragment
            }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, fragment)
                .commit()
        }

        findViewById<TextView>(R.id.tv_correction).setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (fragment.frameReady) {
                val intent = Intent(this, IRCorrectionFourActivity::class.java)
                /**
                 * Executes startactivity operation with thermal imaging domain optimization.
                 *
                 */
                startActivity(intent)
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {}
}
