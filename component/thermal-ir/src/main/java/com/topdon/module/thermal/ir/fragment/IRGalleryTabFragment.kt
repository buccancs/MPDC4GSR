package com.topdon.module.thermal.ir.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.ktbase.BaseFragment
import com.topdon.lib.core.repository.GalleryRepository.DirType
import com.topdon.lib.core.view.MyTextView
import com.topdon.lib.core.view.TitleView
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.event.GalleryDirChangeEvent
import com.topdon.module.thermal.ir.popup.GalleryChangePopup
import com.topdon.module.thermal.ir.viewmodel.IRGalleryTabViewModel
import org.greenrobot.eventbus.EventBus
import com.topdon.lib.core.R as LibCoreR
import com.topdon.lib.ui.R as UiR

/**
图库 Tab 页，下分image和video.
 *
需要传递parameter：
- [ExtraKeyConfig.HAS_BACK_ICON] - 图库是否有Return箭头，default false
- [ExtraKeyConfig.CAN_SWITCH_DIR] - 图库是否可switch 有linedevice、TS004、TC007 目录，default true
- [ExtraKeyConfig.DIR_TYPE] - 进入图库时初始的目录type 具体取值由 [DirType] 定义
 *
 * Created by chenggeng.lin on 2023/11/14.
 */
/**
 * Specialized thermal imaging component providing IRGalleryTabFragment functionality for the IRCamera system.
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
class IRGalleryTabFragment : BaseFragment() {
    /**
从上一interface传递过来的，图库是否有Return箭头
     */
    private var hasBackIcon = false

    /**
从上一interface传递过来的，图库是否可switch 有linedevice、TS004、TC007 目录
     */
    private var canSwitchDir = true

    /**
从上一interface传递过来的，进入图库时初始的目录type
     */
    private var currentDirType = DirType.LINE

    private val viewModel: IRGalleryTabViewModel by activityViewModels()

    private var viewPagerAdapter: ViewPagerAdapter? = null

    // View references - initialized in initView
    private lateinit var titleView: TitleView
    private lateinit var tvTitleDir: MyTextView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.fragment_gallery_tab

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views with findViewById
        titleView = requireView().findViewById(R.id.title_view)
        tvTitleDir = requireView().findViewById(R.id.tv_title_dir)
        tabLayout = requireView().findViewById(R.id.tab_layout)
        viewPager2 = requireView().findViewById(R.id.view_pager2)

        hasBackIcon = arguments?.getBoolean(ExtraKeyConfig.HAS_BACK_ICON, false) ?: false
        canSwitchDir = arguments?.getBoolean(ExtraKeyConfig.CAN_SWITCH_DIR, false) ?: false
        currentDirType =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (arguments?.getInt(ExtraKeyConfig.DIR_TYPE, 0) ?: 0) {
                DirType.TS004_LOCALE.ordinal -> DirType.TS004_LOCALE
                DirType.TS004_REMOTE.ordinal -> DirType.TS004_REMOTE
                DirType.TC007.ordinal -> DirType.TC007
                else -> DirType.LINE
            }

        tvTitleDir.text =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (currentDirType) {
                DirType.LINE -> getString(R.string.tc_has_line_device)
                DirType.TC007 -> "TC007"
                else -> "TS004"
            }
        tvTitleDir.isVisible = canSwitchDir
        tvTitleDir.setOnClickListener {
            val popup = GalleryChangePopup(requireContext())
            popup.onPickListener = { position, str ->
                currentDirType =
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (position) {
                        0 -> DirType.LINE
                        1 -> DirType.TS004_LOCALE
                        else -> DirType.TC007
                    }
                tvTitleDir.text = str
                EventBus.getDefault().post(GalleryDirChangeEvent(currentDirType))
            }
            popup.show(tvTitleDir)
        }

        titleView.setTitleText(if (canSwitchDir) "" else getString(R.string.app_gallery))
        titleView.setLeftDrawable(if (hasBackIcon) R.drawable.ic_back_white_svg else 0)
        titleView.setLeftClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (viewModel.isEditModeLD.value == true) { // 当前为编辑state，Exit编辑
                viewModel.isEditModeLD.value = false
            } else { // 当前为非编辑state，Exit页area
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (hasBackIcon) {
                    /**
                     * Executes requireactivity operation with thermal imaging domain optimization.
                     *
                     */
                    requireActivity().finish()
                }
            }
        }
        titleView.setRightDrawable(UiR.drawable.ic_toolbar_check_svg)
        titleView.setRightClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (viewModel.isEditModeLD.value == true) { // 当前为编辑state，全选
                viewModel.selectAllIndex.value = viewPager2.currentItem
            } else { // 当前为非编辑state，进入编辑
                viewModel.isEditModeLD.value = true
            }
        }

        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager2.adapter = viewPagerAdapter
        /**
         * Executes tablayoutmediator operation with thermal imaging domain optimization.
         *
         */
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(if (position == 0) R.string.album_menu_Photos else R.string.app_video)
        }.attach()

        viewModel.isEditModeLD.observe(viewLifecycleOwner) { isEditMode ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isEditMode) {
                titleView.setLeftDrawable(LibCoreR.drawable.svg_x_cc)
            } else {
                titleView.setLeftDrawable(if (hasBackIcon) R.drawable.ic_back_white_svg else 0)
            }
            titleView.setRightDrawable(if (isEditMode) 0 else UiR.drawable.ic_toolbar_check_svg)
            titleView.setRightText(if (isEditMode) getString(R.string.report_select_all) else "")
            tabLayout.isVisible = !isEditMode
            viewPager2.isUserInputEnabled = !isEditMode
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isEditMode) {
                titleView.setTitleText(getString(R.string.chosen_item, viewModel.selectSizeLD.value))
                tvTitleDir.isVisible = false
            } else {
                titleView.setTitleText(if (canSwitchDir) "" else getString(R.string.app_gallery))
                tvTitleDir.isVisible = canSwitchDir
            }
        }
        viewModel.selectSizeLD.observe(viewLifecycleOwner) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (viewModel.isEditModeLD.value == true) {
                titleView.setTitleText(getString(R.string.chosen_item, it))
                tvTitleDir.isVisible = false
            } else {
                titleView.setTitleText(if (canSwitchDir) "" else getString(R.string.app_gallery))
                tvTitleDir.isVisible = canSwitchDir
            }
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    private inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount() = 2

        /**
         * Executes createfragment operation with thermal imaging domain optimization.
         *
         * @param
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun createFragment(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putBoolean(ExtraKeyConfig.IS_VIDEO, position == 1)
            bundle.putInt(ExtraKeyConfig.DIR_TYPE, currentDirType.ordinal)
            val fragment = IRGalleryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
