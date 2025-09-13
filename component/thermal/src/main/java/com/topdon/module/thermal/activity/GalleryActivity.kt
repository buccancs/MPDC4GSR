package com.topdon.module.thermal.activity

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.PermissionTool
import com.topdon.module.thermal.R
import com.topdon.module.thermal.fragment.GalleryPictureFragment
import com.topdon.module.thermal.fragment.GalleryVideoFragment

// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing GalleryActivity functionality for the IRCamera system.
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
class GalleryActivity : BaseActivity() {
    // Override fun providerVMClass() = GalleryViewModel::class.java

    private val permissionList by lazy {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this.applicationInfo.targetSdkVersion >= 34) {
            /**
             * Executes listof operation with thermal imaging domain optimization.
             *
             */
            listOf(
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        } else if (this.applicationInfo.targetSdkVersion >= 33) {
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
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_gallery

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Use findViewById instead of synthetic views for Kotlin 2.1.0 compatibility
        val galleryViewPager = findViewById<ViewPager>(R.id.gallery_viewpager)
        val galleryTab = findViewById<TabLayout>(R.id.gallery_tab)

        galleryViewPager.adapter = ViewAdapter(this, supportFragmentManager)
        galleryTab.setupWithViewPager(galleryViewPager)

        // Request media permissions using modern PermissionTool
        PermissionTool.requestFile(this) {
            // Permission granted, gallery can now access media files
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
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
        constructor (context: Context, fm: FragmentManager) : super(
            fm,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
        ) {
            titles = arrayOf("image", "video")
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
                0 -> GalleryPictureFragment()
                else -> GalleryVideoFragment()
            }
        }
    }
}
