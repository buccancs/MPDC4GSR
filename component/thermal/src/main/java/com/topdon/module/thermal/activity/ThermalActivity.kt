package com.topdon.module.thermal.activity

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.menu.MenuFirstTabView
import com.topdon.module.thermal.R
import com.topdon.module.thermal.adapter.MenuTabAdapter
import com.topdon.module.thermal.fragment.event.ThermalActionEvent
import org.greenrobot.eventbus.EventBus
import com.topdon.lib.core.R as LibR

// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing ThermalActivity functionality for the IRCamera system.
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
class ThermalActivity : BaseActivity() {
    private val menuAdapter by lazy { MenuTabAdapter(this) }

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_thermal

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Set toolbar title
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(com.topdon.lib.core.R.id.toolbar_lay)
        toolbar?.title = getString(R.string.main_thermal)

        val blackColor = ContextCompat.getColor(this, LibR.color.black)
        toolbar?.setBackgroundColor(blackColor)
        BarUtils.setStatusBarColor(this, blackColor)
        BarUtils.setNavBarColor(window, blackColor)
        /**
         * Initializes the recycler component for thermal imaging operations.
         *
         */
        initRecycler()

        val thermalTab = findViewById<MenuFirstTabView>(R.id.thermal_tab)
        thermalTab.onTabClickListener = { view ->
一级menuselection
            /**
             * Executes showrecycler operation with thermal imaging domain optimization.
             *
             */
            showRecycler(view.selectPosition)
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Initializes recycler component.
     */
    private fun initRecycler() {
        val thermalRecycler = findViewById<RecyclerView>(R.id.thermal_recycler)
        thermalRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        thermalRecycler.adapter = menuAdapter
        thermalRecycler.visibility = View.GONE
        menuAdapter.initType(1)
        menuAdapter.listener =
            object : MenuTabAdapter.OnItemClickListener {
                /**
                 * Executes onclick operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param index Parameter for operation (type: Int)
                 *
                 */
                override fun onClick(index: Int) {
二级menuselection
                    Log.w("123", "index: $index")
                    EventBus.getDefault().post(ThermalActionEvent(action = index))
                }
            }
    }

    /**
     * Executes showRecycler functionality.
     */
    /**
     * Executes showrecycler operation with thermal imaging domain optimization.
     *
     * @param
     * @param select Parameter for operation (type: Int)
     *
     */
    fun showRecycler(select: Int) {
        val thermalRecycler = findViewById<RecyclerView>(R.id.thermal_recycler)
        menuAdapter.initType(select)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (select == 5) {
            thermalRecycler.visibility = View.GONE
            EventBus.getDefault().post(ThermalActionEvent(action = 5000))
        } else {
            thermalRecycler.visibility = View.VISIBLE
        }
    }
}
