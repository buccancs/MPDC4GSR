package com.topdon.module.thermal.ir.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.repository.GalleryRepository.DirType
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.fragment.IRGalleryTabFragment
import com.topdon.module.thermal.ir.viewmodel.IRGalleryTabViewModel

/**
图库.
 *
需要传递parameter：
- [ExtraKeyConfig.DIR_TYPE] - 要查看的目录type 具体取值由 [DirType] 定义
 *
 * Created by LCG on 2024/2/22.
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing IRGalleryHomeActivity functionality for the IRCamera system.
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
class IRGalleryHomeActivity : BaseActivity() {
    private var isTS004Remote = false

    private val viewModel: IRGalleryTabViewModel by viewModels()

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_ir_gallery_home

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isTS004Remote = intent.getIntExtra(ExtraKeyConfig.DIR_TYPE, 0) == DirType.TS004_REMOTE.ordinal

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putBoolean(ExtraKeyConfig.CAN_SWITCH_DIR, false)
            bundle.putBoolean(ExtraKeyConfig.HAS_BACK_ICON, true)
            bundle.putInt(ExtraKeyConfig.DIR_TYPE, intent.getIntExtra(ExtraKeyConfig.DIR_TYPE, 0))

            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, IRGalleryTabFragment::class.java, bundle)
                .commit()
        }

        val callback =
            object : OnBackPressedCallback(true) {
                /**
                 * Executes handleonbackpressed operation with thermal imaging domain optimization.
                 *
                 */
                override fun handleOnBackPressed() {
                    viewModel.isEditModeLD.value = false
                }
            }
        onBackPressedDispatcher.addCallback(this, callback)

        viewModel.isEditModeLD.observe(this) {
            callback.isEnabled = it
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
    override fun initData() {
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
    }
}
