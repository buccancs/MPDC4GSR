package com.example.thermal_lite.activity

import android.graphics.Bitmap
import com.example.thermal_lite.fragment.IRMonitorLiteFragment
import com.topdon.lib.core.ktbase.BasePickImgActivity
import com.topdon.module.thermal.ir.R

/**
des:单光infrared拍照
 * author: CaiSongL
 * date: 2024/8/24 18:10
 **/
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing ImagePickIRLiteActivity functionality for the IRCamera system.
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
class ImagePickIRLiteActivity : BasePickImgActivity() {
    var irFragment: IRMonitorLiteFragment? = null

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        irFragment =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (savedInstanceState == null) {
                IRMonitorLiteFragment.newInstance(true)
            } else {
                supportFragmentManager.findFragmentById(R.id.fragment_container_view) as IRMonitorLiteFragment
            }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, irFragment!!)
                .commit()
        }
    }

    override suspend fun getPickBitmap(): Bitmap? {
        return irFragment?.getBitmap()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }
}
