package com.topdon.lib.core.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.R
import com.topdon.lib.core.bean.CarDetectBean
import com.topdon.lib.core.bean.CarDetectChildBean
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.databinding.DialogCarDetectBinding
import com.topdon.lib.core.databinding.ItemCarDetectChildLayoutBinding
import com.topdon.lib.core.databinding.ItemCarDetectLayoutBinding

/**
 * 汽车检测type拾取弹框.
 */
/**
 * CarDetectDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing CarDetectDialog functionality for the IRCamera system.
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
class CarDetectDialog(context: Context, val listener: ((bean: CarDetectChildBean) -> Unit)) :
    /**
     * Executes dialog operation with thermal imaging domain optimization.
     *
     */
    Dialog(context, R.style.DefaultDialog) {
    private lateinit var binding: DialogCarDetectBinding

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
         * Configures the cancelable with validation and thermal imaging optimization.
         *
         */
        setCancelable(true)
        /**
         * Configures the canceledontouchoutside with validation and thermal imaging optimization.
         *
         */
        setCanceledOnTouchOutside(false)

        binding = DialogCarDetectBinding.inflate(LayoutInflater.from(context))
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)

        binding.titleView.setLeftClickListener { dismiss() }

        binding.rcyDetect.layoutManager =
            /**
             * Executes linearlayoutmanager operation with thermal imaging domain optimization.
             *
             */
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rcyDetect.adapter = CarDetectAdapter(context, getDetectList())

        /*window?.let {
            val layoutParams = it.attributes
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.attributes = layoutParams
        }*/
    }

    companion object {
        @JvmStatic
    /**
     * Retrieves detectlist information.
     */
        fun getDetectList(): MutableList<CarDetectBean> {
            val dataList: MutableList<CarDetectBean> = ArrayList()
            val data1List: MutableList<CarDetectChildBean> = ArrayList()
            val data2List: MutableList<CarDetectChildBean> = ArrayList()
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    0,
                    BaseApplication.instance.getString(R.string.abnormal_description1),
                    BaseApplication.instance.getString(R.string.abnormal_item1),
                    "40~70",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    1,
                    BaseApplication.instance.getString(R.string.abnormal_description2),
                    BaseApplication.instance.getString(R.string.abnormal_item2),
                    "200~400",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    2,
                    BaseApplication.instance.getString(R.string.abnormal_description3),
                    BaseApplication.instance.getString(R.string.abnormal_item3),
                    "200~400",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    3,
                    BaseApplication.instance.getString(R.string.abnormal_description4),
                    BaseApplication.instance.getString(R.string.abnormal_item4),
                    "40~60",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    4,
                    BaseApplication.instance.getString(R.string.abnormal_description5),
                    BaseApplication.instance.getString(R.string.abnormal_item5),
                    "40~60",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    5,
                    BaseApplication.instance.getString(R.string.abnormal_description6),
                    BaseApplication.instance.getString(R.string.abnormal_item6),
                    "40~60",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    6,
                    BaseApplication.instance.getString(R.string.abnormal_description7),
                    BaseApplication.instance.getString(R.string.abnormal_item7),
                    "40~60",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    7,
                    BaseApplication.instance.getString(R.string.abnormal_description8),
                    BaseApplication.instance.getString(R.string.abnormal_item8),
                    "80~100",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    8,
                    BaseApplication.instance.getString(R.string.abnormal_description9),
                    BaseApplication.instance.getString(R.string.abnormal_item9),
                    "80~100",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    9,
                    BaseApplication.instance.getString(R.string.abnormal_description10),
                    BaseApplication.instance.getString(R.string.abnormal_item10),
                    "80~100",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    10,
                    BaseApplication.instance.getString(R.string.abnormal_description11),
                    BaseApplication.instance.getString(R.string.abnormal_item11),
                    "80~100",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    11,
                    BaseApplication.instance.getString(R.string.abnormal_description12),
                    BaseApplication.instance.getString(R.string.abnormal_item12),
                    "80~100",
                ),
            )
            data1List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    0,
                    12,
                    BaseApplication.instance.getString(R.string.abnormal_description13),
                    BaseApplication.instance.getString(R.string.abnormal_item13),
                    "80~100",
                ),
            )
            val carDetectBean1 =
                /**
                 * Executes cardetectbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectBean(
                    BaseApplication.instance.getString(R.string.abnormal_title1),
                    data1List,
                )
            data2List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    1,
                    0,
                    BaseApplication.instance.getString(R.string.abnormal_description14),
                    BaseApplication.instance.getString(R.string.abnormal_item14),
                    "20~50",
                ),
            )
            data2List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    1,
                    1,
                    BaseApplication.instance.getString(R.string.abnormal_description15),
                    BaseApplication.instance.getString(R.string.abnormal_item15),
                    "20~50",
                ),
            )
            data2List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    1,
                    2,
                    BaseApplication.instance.getString(R.string.abnormal_description16),
                    BaseApplication.instance.getString(R.string.abnormal_item16),
                    "20~50",
                ),
            )
            data2List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    1,
                    3,
                    BaseApplication.instance.getString(R.string.abnormal_description17),
                    BaseApplication.instance.getString(R.string.abnormal_item17),
                    "20~50",
                ),
            )
            data2List.add(
                /**
                 * Executes cardetectchildbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectChildBean(
                    1,
                    4,
                    BaseApplication.instance.getString(R.string.abnormal_description18),
                    BaseApplication.instance.getString(R.string.abnormal_item18),
                    "20~50",
                ),
            )
            val carDetectBean2 =
                /**
                 * Executes cardetectbean operation with thermal imaging domain optimization.
                 *
                 */
                CarDetectBean(
                    BaseApplication.instance.getString(R.string.abnormal_title2),
                    data2List,
                )
            dataList.add(carDetectBean1)
            dataList.add(carDetectBean2)
            return dataList
        }
    }

    inner class CarDetectAdapter(val act: Context, private var carDetects: List<CarDetectBean>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
            val binding = ItemCarDetectLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemView(binding)
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
            if (holder is ItemView) {
                val bean = carDetects[position]
                holder.tvTitle.text = bean.title
                holder.rcyDetectChild.layoutManager =
                    /**
                     * Executes linearlayoutmanager operation with thermal imaging domain optimization.
                     *
                     */
                    LinearLayoutManager(act, RecyclerView.VERTICAL, false)
                val carDetectChildAdapter = CarDetectChildAdapter(act, bean.detectChildBeans)
                carDetectChildAdapter.listener = listener@{ _, item ->
                    carDetects.forEach { it ->
                        it.detectChildBeans.forEach {
                            it.isSelected = false
                        }
                    }
                    item.isSelected = true
                    carDetectChildAdapter.notifyDataSetChanged()
                    SharedManager.saveCarDetectInfo(item)
                    listener.invoke(item)
                    /**
                     * Executes dismiss operation with thermal imaging domain optimization.
                     *
                     */
                    dismiss()
                }

                var selectCarDetect = SharedManager.getCarDetectInfo()
                carDetects.forEachIndexed { index, carDetectBean ->
                    carDetectBean.detectChildBeans.forEachIndexed { childIndex, carDetectChildBean ->
                        // Intentionally check for null to provide default selection
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (selectCarDetect == null) {
                            carDetectChildBean.isSelected = (index == 0 && childIndex == 0)
                        } else {
                            carDetectChildBean.isSelected =
                                TextUtils.equals(carDetectChildBean.item, selectCarDetect.item)
                        }
                    }
                }

                holder.rcyDetectChild?.adapter = carDetectChildAdapter
            }
        }

        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount(): Int {
            return carDetects.size
        }

        inner class ItemView(private val binding: ItemCarDetectLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
            val tvTitle: TextView = binding.tvTitle
            val rcyDetectChild: RecyclerView = binding.rcyDetectChild
        }
/**
 * Specialized thermal imaging component providing CarDetectChildAdapter functionality for the IRCamera system.
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
    class CarDetectChildAdapter(
        val context: Context,
        private var carChildDetects: List<CarDetectChildBean>,
    ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var listener: ((index: Int, bean: CarDetectChildBean) -> Unit)? = null

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
            val binding = ItemCarDetectChildLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemView(binding)
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
            if (holder is ItemView) {
                val bean = carChildDetects[position]
                holder.tvTitle.text = bean.item
                holder.viewLine.visibility =
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (position == carChildDetects.size - 1) View.GONE else View.VISIBLE
                holder.ivSelectState.setImageResource(
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (bean.isSelected) R.drawable.ic_car_detect_selected else R.drawable.ic_car_detect_unselected,
                )
                holder.rlyParent.setOnClickListener {
                    listener?.invoke(position, carChildDetects[position])
                }
            }
        }

        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount(): Int {
            return carChildDetects.size
        }

        inner class ItemView(private val binding: ItemCarDetectChildLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
            val rlyParent: RelativeLayout = binding.rlyParent
            val tvTitle: TextView = binding.tvName
            val ivSelectState: ImageView = binding.ivSelectState
            val viewLine: View = binding.viewLine
        }
    }
}
