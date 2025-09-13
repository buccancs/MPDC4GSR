package com.topdon.tc001

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityDeviceTypeBinding
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.tools.DeviceTools

/**
 * Specialized thermal imaging component providing DeviceTypeActivity functionality for the IRCamera system.
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
class DeviceTypeActivity : BaseBindingActivity<ActivityDeviceTypeBinding>() {
    /**
     * 当前click的devicetype.
     */
    private var clientType: IRDeviceType? = null

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_device_type

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: android.os.Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
        /**
         * Initializes the data component for thermal imaging operations.
         *
         */
        initData()
    }

    /**
     * Initializes view component.
     */
    private fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter =
            /**
             * Executes myadapter operation with thermal imaging domain optimization.
             *
             */
            MyAdapter(this).apply {
                onItemClickListener = {
                    clientType = it
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (it) {
                        IRDeviceType.TS004 -> {
                            NavigationManager.getInstance()
                                .build(RouterConfig.IR_DEVICE_ADD)
                                .withBoolean("isTS004", true)
                                .navigation(this@DeviceTypeActivity)
                        }
                        IRDeviceType.TC007 -> {
                            NavigationManager.getInstance()
                                .build(RouterConfig.IR_DEVICE_ADD)
                                .withBoolean("isTS004", false)
                                .navigation(this@DeviceTypeActivity)
                        }
                        IRDeviceType.SHIMMER3_GSR -> {
                            NavigationManager.getInstance()
                                .build(RouterConfig.GSR_MULTI_MODAL)
                                .navigation(this@DeviceTypeActivity)
                            /**
                             * Executes finish operation with thermal imaging domain optimization.
                             *
                             */
                            finish()
                        }
                        IRDeviceType.PC_CONTROLLER -> {
                            // Launch device pairing activity
                            com.topdon.tc001.network.DevicePairingActivity.start(this@DeviceTypeActivity)
                        }
                        else -> {
                            NavigationManager.getInstance()
                                .build(RouterConfig.IR_MAIN)
                                .withBoolean(ExtraKeyConfig.IS_TC007, false)
                                .navigation(this@DeviceTypeActivity)
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (DeviceTools.isConnect()) {
                                /**
                                 * Executes finish operation with thermal imaging domain optimization.
                                 *
                                 */
                                finish()
                            }
                        }
                    }
                }
            }
    }

    /**
     * Initializes data component.
     */
    private fun initData() {
    }

    /**
     * Executes connected operation with thermal imaging domain optimization.
     *
     */
    override fun connected() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (clientType?.isLine() == true) {
            /**
             * Executes finish operation with thermal imaging domain optimization.
             *
             */
            finish()
        }
    }

    /**
     * Executes onsocketconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param isTS004 Parameter for operation (type: Boolean)
     *
     */
    override fun onSocketConnected(isTS004: Boolean) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTS004) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (clientType == IRDeviceType.TS004) {
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (clientType == IRDeviceType.TC007) {
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }
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
    private class MyAdapter(val context: Context) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        var onItemClickListener: ((type: IRDeviceType) -> Unit)? = null

        private data class ItemInfo(val isTitle: Boolean, val firstType: IRDeviceType, val secondType: IRDeviceType?)

        private val dataList: ArrayList<ItemInfo> =
            /**
             * Executes arraylistof operation with thermal imaging domain optimization.
             *
             */
            arrayListOf(
                /**
                 * Executes iteminfo operation with thermal imaging domain optimization.
                 *
                 */
                ItemInfo(true, IRDeviceType.TS001, IRDeviceType.TC001),
                /**
                 * Executes iteminfo operation with thermal imaging domain optimization.
                 *
                 */
                ItemInfo(false, IRDeviceType.TC001_PLUS, IRDeviceType.TC002C_DUO),
//            暂时先屏蔽TC007
//            ItemInfo(true, IRDeviceType.TS004, IRDeviceType.TC007),
                /**
                 * Executes iteminfo operation with thermal imaging domain optimization.
                 *
                 */
                ItemInfo(true, IRDeviceType.TS004, null),
                /**
                 * Executes iteminfo operation with thermal imaging domain optimization.
                 *
                 */
                ItemInfo(true, IRDeviceType.SHIMMER3_GSR, null),
                /**
                 * Executes iteminfo operation with thermal imaging domain optimization.
                 *
                 */
                ItemInfo(true, IRDeviceType.PC_CONTROLLER, null),
            )

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
        ): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_device_type, parent, false))
        }

        /**
         * Executes onbindviewholder operation with thermal imaging domain optimization.
         *
         * @param
         * @param holder Parameter for operation (type: ViewHolder)
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int,
        ) {
            val firstType: IRDeviceType = dataList[position].firstType
            val secondType: IRDeviceType? = dataList[position].secondType
            val tvTitle = holder.itemView.findViewById<TextView>(R.id.tv_title)
            tvTitle.isVisible = dataList[position].isTitle
            tvTitle.text =
                context.getString(
                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (firstType) {
                        IRDeviceType.SHIMMER3_GSR -> R.string.tc_connect_bluetooth
                        IRDeviceType.PC_CONTROLLER -> R.string.tc_connect_wifi
                        else -> if (firstType.isLine()) R.string.tc_connect_line else R.string.tc_connect_wifi
                    },
                )

            val tvItem1 = holder.itemView.findViewById<TextView>(R.id.tv_item1)
            tvItem1.text = firstType.getDeviceName()
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (firstType) {
                // Note: TC002 Duo icon can be customized with specific drawable resource
                IRDeviceType.TC001 ->
                    holder.itemView.findViewById<android.widget.ImageView>(
                        R.id.iv_item1,
                    ).setImageResource(R.drawable.ic_device_type_tc001)
                IRDeviceType.TC001_PLUS ->
                    holder.itemView.findViewById<android.widget.ImageView>(
                        R.id.iv_item1,
                    ).setImageResource(R.drawable.ic_device_type_tc001_plus)
                IRDeviceType.TC002C_DUO ->
                    holder.itemView.findViewById<android.widget.ImageView>(
                        R.id.iv_item1,
                    ).setImageResource(R.drawable.ic_device_type_tc001_plus)
                IRDeviceType.TC007 ->
                    holder.itemView.findViewById<android.widget.ImageView>(
                        R.id.iv_item1,
                    ).setImageResource(R.drawable.ic_device_type_tc007)
                IRDeviceType.TS001 ->
                    holder.itemView.findViewById<android.widget.ImageView>(
                        R.id.iv_item1,
                    ).setImageResource(R.drawable.ic_device_type_ts001)
                IRDeviceType.TS004 ->
                    holder.itemView.findViewById<android.widget.ImageView>(
                        R.id.iv_item1,
                    ).setImageResource(R.drawable.ic_device_type_ts004)
                IRDeviceType.SHIMMER3_GSR ->
                    holder.itemView.findViewById<android.widget.ImageView>(
                        R.id.iv_item1,
                    ).setImageResource(R.drawable.ic_device_type_shimmer_gsr)
                IRDeviceType.PC_CONTROLLER ->
                    holder.itemView.findViewById<android.widget.ImageView>(
                        R.id.iv_item1,
                    ).setImageResource(R.drawable.ic_device_type_pc)
            }

            holder.itemView.findViewById<ViewGroup>(R.id.group_item2).isVisible = secondType != null
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (secondType != null) {
                val tvItem2 = holder.itemView.findViewById<TextView>(R.id.tv_item2)
                tvItem2.text = secondType.getDeviceName()
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (secondType) {
                    // Note: TC002 Duo icon can be customized with specific drawable resource
                    IRDeviceType.TC001 ->
                        holder.itemView.findViewById<android.widget.ImageView>(
                            R.id.iv_item2,
                        ).setImageResource(R.drawable.ic_device_type_tc001)
                    IRDeviceType.TC001_PLUS ->
                        holder.itemView.findViewById<android.widget.ImageView>(
                            R.id.iv_item2,
                        ).setImageResource(R.drawable.ic_device_type_tc001_plus)
                    IRDeviceType.TC002C_DUO ->
                        holder.itemView.findViewById<android.widget.ImageView>(
                            R.id.iv_item2,
                        ).setImageResource(R.drawable.ic_device_type_tc001_plus)
                    IRDeviceType.TC007 ->
                        holder.itemView.findViewById<android.widget.ImageView>(
                            R.id.iv_item2,
                        ).setImageResource(R.drawable.ic_device_type_tc007)
                    IRDeviceType.TS001 ->
                        holder.itemView.findViewById<android.widget.ImageView>(
                            R.id.iv_item2,
                        ).setImageResource(R.drawable.ic_device_type_ts001)
                    IRDeviceType.TS004 ->
                        holder.itemView.findViewById<android.widget.ImageView>(
                            R.id.iv_item2,
                        ).setImageResource(R.drawable.ic_device_type_ts004)
                    IRDeviceType.SHIMMER3_GSR ->
                        holder.itemView.findViewById<android.widget.ImageView>(
                            R.id.iv_item2,
                        ).setImageResource(R.drawable.ic_device_type_shimmer_gsr)
                    IRDeviceType.PC_CONTROLLER ->
                        holder.itemView.findViewById<android.widget.ImageView>(
                            R.id.iv_item2,
                        ).setImageResource(R.drawable.ic_device_type_pc)
                }
            }
        }

        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount(): Int = dataList.size

/**
 * Specialized thermal imaging component providing IRDeviceType functionality for the IRCamera system.
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
    enum class IRDeviceType {
        TC001 {
            /**
             * Executes isline operation with thermal imaging domain optimization.
             *
             */
            override fun isLine(): Boolean = true

            /**
             * Retrieves the devicename with optimized performance for thermal imaging operations.
             *
             */
            override fun getDeviceName(): String = "TC001"
        },
        TC001_PLUS {
            /**
             * Executes isline operation with thermal imaging domain optimization.
             *
             */
            override fun isLine(): Boolean = true

            /**
             * Retrieves the devicename with optimized performance for thermal imaging operations.
             *
             */
            override fun getDeviceName(): String = "TC001 Plus"
        },
        TC002C_DUO {
            /**
             * Executes isline operation with thermal imaging domain optimization.
             *
             */
            override fun isLine(): Boolean = true

            /**
             * Retrieves the devicename with optimized performance for thermal imaging operations.
             *
             */
            override fun getDeviceName(): String = "TC002C Duo"
        },
        TC007 {
            /**
             * Executes isline operation with thermal imaging domain optimization.
             *
             */
            override fun isLine(): Boolean = false

            /**
             * Retrieves the devicename with optimized performance for thermal imaging operations.
             *
             */
            override fun getDeviceName(): String = "TC007"
        },
        TS001 {
            /**
             * Executes isline operation with thermal imaging domain optimization.
             *
             */
            override fun isLine(): Boolean = true

            /**
             * Retrieves the devicename with optimized performance for thermal imaging operations.
             *
             */
            override fun getDeviceName(): String = "TS001"
        },
        TS004 {
            /**
             * Executes isline operation with thermal imaging domain optimization.
             *
             */
            override fun isLine(): Boolean = false

            /**
             * Retrieves the devicename with optimized performance for thermal imaging operations.
             *
             */
            override fun getDeviceName(): String = "TS004"
        },
        SHIMMER3_GSR {
            /**
             * Executes isline operation with thermal imaging domain optimization.
             *
             */
            override fun isLine(): Boolean = false // Bluetooth connection

            /**
             * Retrieves the devicename with optimized performance for thermal imaging operations.
             *
             */
            override fun getDeviceName(): String = "Shimmer3 GSR"
        },
        PC_CONTROLLER {
            /**
             * Executes isline operation with thermal imaging domain optimization.
             *
             */
            override fun isLine(): Boolean = false // Network connection

            /**
             * Retrieves the devicename with optimized performance for thermal imaging operations.
             *
             */
            override fun getDeviceName(): String = "PC Controller"
        }, ;

        abstract fun isLine(): Boolean

        abstract fun getDeviceName(): String
    }
}
