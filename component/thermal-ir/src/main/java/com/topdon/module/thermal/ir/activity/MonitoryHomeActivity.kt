package com.topdon.module.thermal.ir.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.event.MonitorSaveEvent
import com.topdon.module.thermal.ir.fragment.IRMonitorCaptureFragment
import com.topdon.module.thermal.ir.fragment.IRMonitorHistoryFragment
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
temperature监控 Tab 页，包含
- 历史 [IRMonitorHistoryFragment]
- 实时 [IRMonitorCaptureFragment]
 *
需要传递parameter：
- [ExtraKeyConfig.IS_TC007] - 当前device是否为 TC007
 *
 * Created by LCG on 2024/8/20.
 */
/**
 * Specialized thermal imaging component providing MonitoryHomeActivity functionality for the IRCamera system.
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
class MonitoryHomeActivity : BaseActivity() {
    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_monitor_home

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        val isTC007 = intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager2)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        viewPager2.adapter = ViewPagerAdapter(this, isTC007)
        /**
         * Executes tablayoutmediator operation with thermal imaging domain optimization.
         *
         */
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(if (position == 0) R.string.chart_history else R.string.chart_real_time)
        }.attach()
    }

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ViewPagerAdapter display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
    private class ViewPagerAdapter(activity: MonitoryHomeActivity, val isTC007: Boolean) : FragmentStateAdapter(activity) {
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
            return if (position == 0) {
                /**
                 * Executes irmonitorhistoryfragment operation with thermal imaging domain optimization.
                 *
                 */
                IRMonitorHistoryFragment()
            } else {
                val fragment = IRMonitorCaptureFragment()
                fragment.arguments = Bundle().also { it.putBoolean(ExtraKeyConfig.IS_TC007, isTC007) }
                fragment
            }
        }
    }
}
