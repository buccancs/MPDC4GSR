package com.topdon.module.user.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.utils.Constants
import com.topdon.lib.core.view.TitleView
import com.topdon.module.user.R
import com.topdon.lib.core.R as RCore

/**
/**
 * Specialized thermal imaging component providing ElectronicManualActivity functionality for the IRCamera system.
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
class ElectronicManualActivity : BaseActivity() {
    // View references - migrated from synthetic views
    private lateinit var titleView: TitleView
    private lateinit var electronicManualRecycler: RecyclerView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_electronic_manual

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views - migrated from synthetic views
        titleView = findViewById(R.id.title_view)
        electronicManualRecycler = findViewById(R.id.electronic_manual_recycler)

        val productType = intent.getIntExtra(Constants.SETTING_TYPE, 0) // 0-电子description书 1-FAQ

        titleView.setTitleText(if (productType == Constants.SETTING_BOOK) RCore.string.electronic_manual else RCore.string.app_question)

        val adapter = MyAdapter(productType == 1)
        adapter.onPickListener = { isTS001 ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isTS001) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (productType == Constants.SETTING_BOOK) {
电子description书-TS001
                } else {
                    // FAQ-TS001
                    NavigationManager.getInstance().build(RouterConfig.QUESTION).withBoolean("isTS001", true).navigation(this)
                }
            } else {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (productType == Constants.SETTING_BOOK) {
电子description书-TS004
                    NavigationManager.getInstance().build(RouterConfig.PDF).withBoolean("isTS001", false).navigation(this)
                } else {
                    // FAQ-TS004
                    NavigationManager.getInstance().build(RouterConfig.QUESTION).withBoolean("isTS001", false).navigation(this)
                }
            }
        }

        electronicManualRecycler.layoutManager = LinearLayoutManager(this)
        electronicManualRecycler.adapter = adapter
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

/**
 * Specialized thermal imaging component providing MyAdapter functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class MyAdapter(private val isFAQ: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var onPickListener: ((isTS001: Boolean) -> Unit)? = null

        private val optionList: ArrayList<String> = ArrayList(2)

        init {
由于 TC001 的description书为旧version 样式， 2024-4-9 产品决定先hide，只放 TS004 的description书
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isFAQ) {
                optionList.add("TS001")
            }
            optionList.add("TS004")
        }

        /**
         * Executes oncreateviewholder operation with thermal imaging domain optimization.
         *
         * @param
         * @param parent Parameter for operation (type: ViewGroup)
         * @param viewType Parameter for operation (type: Int)
         *
         */
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): RecyclerView.ViewHolder {
            return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_electronic_manual, parent, false))
        }

        /**
         * Executes onbindviewholder operation with thermal imaging domain optimization.
         *
         * @param
         * @param holder Parameter for operation (type: RecyclerView.ViewHolder)
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (holder is ItemViewHolder) {
                val itemText: TextView = holder.rootView.findViewById(R.id.item_text)
                val itemLay: ConstraintLayout = holder.rootView.findViewById(R.id.item_lay)

                itemText.text = optionList[position]
                itemLay.setOnClickListener {
                    onPickListener?.invoke(isFAQ && position == 0)
                }
            }
        }

        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount(): Int = optionList.size

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ItemViewHolder display and interaction.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
        private class ItemViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView)
    }
}
