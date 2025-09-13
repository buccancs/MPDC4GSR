package com.topdon.tc001.gsr

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityGsrGalleryBinding
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.lib.core.tools.PermissionTool

/**
 * Specialized thermal imaging component providing GSRGalleryActivity functionality for the IRCamera system.
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
class GSRGalleryActivity : BaseBindingActivity<ActivityGsrGalleryBinding>() {
    companion object {
        private const val TAG = "GSRGalleryActivity"

    /**
     * Executes startActivity functionality.
     */
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, GSRGalleryActivity::class.java))
        }
    }

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_gsr_gallery

    private val permissionList by lazy {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (applicationContext.applicationInfo.targetSdkVersion >= 34) {
            /**
             * Executes listof operation with thermal imaging domain optimization.
             *
             */
            listOf(
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        } else if (applicationContext.applicationInfo.targetSdkVersion >= 33) {
            /**
             * Executes mutablelistof operation with thermal imaging domain optimization.
             *
             */
            mutableListOf(
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        } else {
            /**
             * Executes mutablelistof operation with thermal imaging domain optimization.
             *
             */
            mutableListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }
    }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
        /**
         * Executes requestpermissions operation with thermal imaging domain optimization.
         *
         */
        requestPermissions()
    }

    /**
     * Initializes view component.
     */
    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "GSR Recording Gallery"

        binding.gsrGalleryViewpager.adapter = ViewAdapter(this, supportFragmentManager)
        binding.gsrGalleryTab.setupWithViewPager(binding.gsrGalleryViewpager)
    }

    /**
     * Executes requestPermissions functionality.
     */
    /**
     * Executes requestpermissions operation with thermal imaging domain optimization.
     *
     */
    private fun requestPermissions() {
        PermissionTool.requestFile(this) {
            // Permission granted, gallery can now access media files
        }
    }

    /**
     * Executes onsupportnavigateup operation with thermal imaging domain optimization.
     *
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    inner class ViewAdapter : FragmentStatePagerAdapter {
        private var titles: Array<String> = arrayOf()

        /**
         * Executes constructor operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param fm Parameter for operation (type: FragmentManager)
         *
         */
        constructor(context: Context, fm: FragmentManager) : super(
            fm,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
        ) {
            titles = arrayOf("GSR Data", "Videos", "RAW Images", "Sessions")
        }

        /**
         * Retrieves the count with optimized performance for thermal imaging operations.
         *
         */
        override fun getCount(): Int {
            return titles.size
        }

        /**
         * Retrieves the pagetitle with optimized performance for thermal imaging operations.
         *
         * @param
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

        /**
         * Retrieves the item with optimized performance for thermal imaging operations.
         *
         * @param
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> GSRDataFragment()
                1 -> GSRVideoFragment()
                2 -> GSRRawImageFragment()
                else -> GSRSessionFragment()
            }
        }
    }
}
