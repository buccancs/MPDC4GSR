package com.topdon.module.thermal.ir.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.UnitTools
import com.topdon.lib.ui.widget.MyItemDecoration
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.view.EmissivityView
import com.topdon.lib.core.R as LibR

/**
常用材料emissivity.
 *
 * Created by LCG on 2024/10/14.
/**
 * Specialized thermal imaging component providing IREmissivityActivity functionality for the IRCamera system.
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
class IREmissivityActivity : BaseActivity() {
    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_ir_emissivity

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        val dataArray: Array<ItemBean> = buildDataArray()
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val emissivityView = findViewById<EmissivityView>(R.id.emissivity_view)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val clTitle = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.cl_title)

        tvTitle.text = dataArray[0].name
        emissivityView.refreshText(dataArray[0].buildTextList(this))

        val itemDecoration = MyItemDecoration(this)
        itemDecoration.wholeBottom = 20f

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MyAdapter(this, dataArray)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.addOnScrollListener(MyOnScrollListener(clTitle, layoutManager, dataArray))
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

/**
 * Specialized thermal imaging component providing MyOnScrollListener functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class MyOnScrollListener(val titleView: View, val layoutManager: LinearLayoutManager, val dataArray: Array<ItemBean>) : RecyclerView.OnScrollListener() {
        /**
当前展示的title在列表中的 position
         */
        private var currentPosition: Int = 0

        /**
titletext
         */
        private val tvTitle: TextView = titleView.findViewById(R.id.tv_title)

        /**
         * Executes onscrolled operation with thermal imaging domain optimization.
         *
         * @param
         * @param recyclerView Parameter for operation (type: RecyclerView)
         * @param dx Parameter for operation (type: Int)
         * @param dy Parameter for operation (type: Int)
         *
         */
        override fun onScrolled(
            recyclerView: RecyclerView,
            dx: Int,
            dy: Int,
        ) {
            val seeFirstPosition = layoutManager.findFirstVisibleItemPosition()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (seeFirstPosition == RecyclerView.NO_POSITION) {
                return
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (seeFirstPosition == currentPosition) {
                return
            }

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dataArray[seeFirstPosition].isTitle) { // 往上顶，将下一目录的title顶到顶部了
                currentPosition = seeFirstPosition
                tvTitle.text = dataArray[currentPosition].name
                titleView.translationY = 0f
            } else {
在Visiblerange内查找当前目录最后a项目
                val seeLastPosition = layoutManager.findLastVisibleItemPosition()
                var nextTitlePosition = -1
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in seeFirstPosition..seeLastPosition) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataArray[i].isTitle) {
                        nextTitlePosition = i
                        break
                    }
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (nextTitlePosition < 0) {
当滑得非常快时，dataArray[seeFirstPosition].isTitle == true 判断分支未必触发，这里兜底
                    currentPosition = findTitlePosition(seeFirstPosition)
                    tvTitle.text = dataArray[currentPosition].name
                    titleView.translationY = 0f
                } else {
                    val nextTitleView: View = layoutManager.findViewByPosition(nextTitlePosition) ?: return
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (nextTitleView.top <= titleView.height) {
                        currentPosition = findTitlePosition(seeFirstPosition)
                        tvTitle.text = dataArray[currentPosition].name
                        titleView.translationY = -(titleView.height - nextTitleView.top).toFloat()
                    } else {
                        titleView.translationY = 0f
                    }
                }
            }
        }

        /**
从指定 position 处，往上遍历查找该 position 对应的 title position.
         */
        private fun findTitlePosition(position: Int): Int {
            for (i in position downTo 0) {
                if (dataArray[i].isTitle) {
                    return i
                }
            }
            return 0
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
    private class MyAdapter(val context: Context, val dataArray: Array<ItemBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        /**
         * Retrieves the itemviewtype with optimized performance for thermal imaging operations.
         *
         * @param
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun getItemViewType(position: Int): Int = if (dataArray[position].isTitle) 0 else 1

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
            return if (viewType == 0) { // Title
                /**
                 * Executes titleviewholder operation with thermal imaging domain optimization.
                 *
                 */
                TitleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ir_emissivity_title, parent, false))
            } else { // 内容
                val emissivityView = EmissivityView(context)
                emissivityView.setPadding(SizeUtils.dp2px(12f), 0, SizeUtils.dp2px(12f), 0)
                /**
                 * Executes valueviewholder operation with thermal imaging domain optimization.
                 *
                 */
                ValueViewHolder(emissivityView)
            }
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
            val itemBean: ItemBean = dataArray[position]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (holder is TitleViewHolder) {
                holder.itemView.findViewById<TextView>(R.id.tv_title).text = itemBean.name
                val emissivityView = holder.itemView.findViewById<EmissivityView>(R.id.emissivity_view)
                emissivityView.isAlignTop = true
                emissivityView.drawTopLine = true
                emissivityView.refreshText(itemBean.buildTextList(context))
            } else if (holder is ValueViewHolder) {
                holder.emissivityView.refreshText(itemBean.buildTextList(context))
            }
        }

        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount(): Int = dataArray.size

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for TitleViewHolder display and interaction.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
        private class TitleViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ValueViewHolder display and interaction.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
        private class ValueViewHolder(val emissivityView: EmissivityView) : RecyclerView.ViewHolder(emissivityView)
    }

    /**
一项emissivitydata封装
@param isTitle true-title false-内容
@param name name，如铝、氧化钢等
@param minTemp minimumtemperature，单位摄氏度
@param maxTemp maximumtemperature，单位摄氏度
@param emStr emissivitytext
     */
    private data class ItemBean(
        val isTitle: Boolean = false,
        val name: String,
        val minTemp: Int? = null,
        val maxTemp: Int? = null,
        val emStr: String? = null,
    ) {
        private var textList: ArrayList<String> = ArrayList(3)

    /**
     * Executes buildTextList functionality.
     */
        /**
         * Executes buildtextlist operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun buildTextList(context: Context): ArrayList<String> {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (textList.isEmpty()) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isTitle) {
                    textList.add(context.getString(LibR.string.material_label))
                    textList.add(context.getString(LibR.string.material_temp, UnitTools.showUnit()))
                    textList.add(context.getString(LibR.string.thermal_config_radiation))
                } else {
                    textList.add(name)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (minTemp != null || maxTemp != null) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (minTemp == null || maxTemp == null || minTemp == maxTemp) {
                            textList.add(UnitTools.showNoUnit((minTemp ?: maxTemp)!!.toFloat()))
                        } else {
                            textList.add(UnitTools.showNoUnit(minTemp.toFloat()) + "~" + UnitTools.showNoUnit(maxTemp.toFloat()))
                        }
                    } else {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (emStr != null) {
                            textList.add("-")
                        }
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (emStr != null) {
                        textList.add(emStr)
                    }
                }
            }
            return textList
        }
    }

    /**
     * Executes buildDataArray functionality.
     */
    /**
     * Executes builddataarray operation with thermal imaging domain optimization.
     *
     */
    private fun buildDataArray(): Array<ItemBean> =
        /**
         * Executes arrayof operation with thermal imaging domain optimization.
         *
         */
        arrayOf(
金属
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(true, getString(LibR.string.material_metal)),
铝
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_aluminum)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_polished_aluminum), minTemp = 100, emStr = "0.09"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_comm_aluminum_foil), minTemp = 100, emStr = "0.09"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_mild_alumina), minTemp = 25, maxTemp = 600, emStr = "0.10～0.20"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_alumina), minTemp = 25, maxTemp = 600, emStr = "0.30～0.40"),
黄铜
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_brass)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_bronze_mirror), minTemp = 28, emStr = "0.03"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_oxide), minTemp = 200, maxTemp = 600, emStr = "0.59～0.61"),
铬
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_chromium)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_polished_chromium), minTemp = 40, maxTemp = 1090, emStr = "0.08～0.36"),
铜
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_copper)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_bronze_mirror_1), minTemp = 100, emStr = "0.05"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_copper_oxide), minTemp = 25, emStr = "0.078"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_oxide_bronze), minTemp = 800, maxTemp = 1100, emStr = "0.66～0.54"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_bronze_water), minTemp = 1080, maxTemp = 1280, emStr = "0.16～0.13"),
金
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_gold)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_golden_mirror), minTemp = 230, maxTemp = 630, emStr = "0.02"),
铁
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_iron)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_polished_cast_iron), minTemp = 200, emStr = "0.21"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_process_cast_iron), minTemp = 20, emStr = "0.44"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_full_rusty_surface), minTemp = 20, emStr = "0.69"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(
                name = getString(LibR.string.material_cast_iron_oxidation, UnitTools.showWithUnit(600f)),
                minTemp = 19,
                maxTemp = 600,
                emStr = "0.64～0.78",
            ),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_e_iron_oxide), minTemp = 125, maxTemp = 520, emStr = "0.78～0.82"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_iron_oxide), minTemp = 500, maxTemp = 1200, emStr = "0.85～0.89"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_iron_plate), minTemp = 925, maxTemp = 1120, emStr = "0.87～0.95"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_cast_iron_oxygen), minTemp = 25, emStr = "0.8"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_melt_surface), minTemp = 22, emStr = "0.94"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_melt_cast_iron), minTemp = 1300, maxTemp = 1400, emStr = "0.29"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_pure_iron), minTemp = 1515, maxTemp = 1680, emStr = "0.42～0.45"),
钢
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_steel)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_steel_1, UnitTools.showWithUnit(600f))),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_oxide_steel), minTemp = 100, emStr = "0.74"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_metrot_low_carbon_steel), minTemp = 1600, maxTemp = 1800, emStr = "0.28"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_steel_water), minTemp = 1500, maxTemp = 1650, emStr = "0.42～0.53"),
铅
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_lead)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_pure_lead), minTemp = 125, maxTemp = 225, emStr = "0.06～0.08"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_mild_oxidation_lead), minTemp = 25, maxTemp = 300, emStr = "0.20～0.45"),
镁
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_magnesium)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_magnesium_oxide), minTemp = 275, maxTemp = 825, emStr = "0.55～0.20"),
汞
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_mercury)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_mercury), minTemp = 0, maxTemp = 100, emStr = "0.09～0.12"),
镍
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_nickel)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_plating_polished_nickel), minTemp = 25, emStr = "0.05"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_nickel_not_polished), minTemp = 20, emStr = "0.01"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_nickel_wire), minTemp = 185, maxTemp = 1010, emStr = "0.09～0.19"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_nickel_plate_oxidized), minTemp = 198, maxTemp = 600, emStr = "0.37～0.48"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_nickel_oxide), minTemp = 650, maxTemp = 1255, emStr = "0.59～0.86"),
镍合金
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_nickel_alloy)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_nickel_chromium_alloy_line), minTemp = 50, maxTemp = 1000, emStr = "0.65～0.79"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_nickel_chromium_alloy), minTemp = 50, maxTemp = 1040, emStr = "0.64～0.76"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(
                name = getString(LibR.string.material_nickel_chromium_heat_resistance),
                minTemp = 50,
                maxTemp = 500,
                emStr = "0.95～0.98",
            ),
银
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_silver)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_polished_silver), minTemp = 100, emStr = "0.05"),
不锈钢
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_stainless_steel)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_eight_stainless_steel), minTemp = 25, emStr = "0.16"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = "304（8Cr,18Ni）", minTemp = 215, maxTemp = 490, emStr = "0.44～0.36"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = "310（25Cr,20Ni）", minTemp = 215, maxTemp = 520, emStr = "0.90～0.97"),
锡
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_tin)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_commercial_tin), minTemp = 100, emStr = "0.07"),
锌
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_zinc)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_400c_zinc_oxide, UnitTools.showWithUnit(400f)), minTemp = 400, emStr = "0.01"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_galvanized_brighter_iron_board), minTemp = 28, emStr = "0.23"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_gray_zinc_oxide), minTemp = 25, emStr = "0.28"),
非金属
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(true, getString(LibR.string.material_nonMetal)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_brick), minTemp = 1100, emStr = "0.75"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_fire_brick), minTemp = 1100, emStr = "0.75"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_graphite_black), minTemp = 96, maxTemp = 225, emStr = "0.95"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_enamel_white), minTemp = 18, emStr = "0.9"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_asphalt), minTemp = 0, maxTemp = 200, emStr = "0.85"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_glass_surface), minTemp = 23, emStr = "0.94"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_heat_resistant_glass), minTemp = 200, maxTemp = 540, emStr = "0.85～0.95"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_wall_powder), minTemp = 20, emStr = "0.9"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_oak), minTemp = 20, emStr = "0.9"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_carbon_slice), emStr = "0.85"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_insulating_tablet), emStr = "0.91～0.94"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_metal_piece), emStr = "0.88～0.90"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_glass_tube), emStr = "0.9"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_coil), emStr = "0.87"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_enamel_product), emStr = "0.9"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_enamel_pattern), emStr = "0.83～0.95"),
电容器
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_capacitor)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_rotating_capacitor), emStr = "0.30～0.34"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_ceramic_bottle_capacitor), emStr = "0.9"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_film_capacitance), emStr = "0.90～0.93"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_mica_capacitor), emStr = "0.94～0.95"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_lighting_groove_mica_capacitor), emStr = "0.90～0.93"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_glass_capacitor), emStr = "0.91～0.92"),
半导体
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_semiconductor)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_crystal_tube_plastic_seal), emStr = "0.80～0.90"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_crystal_tube_metal), emStr = "0.30～0.40"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_diode), emStr = "0.89～0.90"),
传输line圈
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_transmission_coil)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_pulse_transmission_coil), emStr = "0.91～0.92"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_flat_white_layer_coil), emStr = "0.88～0.93"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_top_coil), emStr = "0.91～0.92"),
电子材料
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_electronic)),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_epoxy_glass_board), emStr = "0.86"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_epoxy_phenol_board), emStr = "0.8"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_gold_plated_copper), emStr = "0.3"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_copper_with_welded_welds), emStr = "0.35"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_tin_coated_lead_wire), emStr = "0.28"),
            /**
             * Executes itembean operation with thermal imaging domain optimization.
             *
             */
            ItemBean(name = getString(LibR.string.material_copper_wire), emStr = "0.87～0.88"),
        )
}
