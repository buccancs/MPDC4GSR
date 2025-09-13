// Package com.topdon.module.thermal.activity.temp
//
// Import android.util.Log
// Import androidx.lifecycle.lifecycleScope
// Import com.github.aachartmodel.aainfographics.aachartcreator.*
// Import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAScrollablePlotArea
// Import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
// Import com.topdon.lib.core.config.RouterConfig
// Import com.topdon.lib.core.ktbase.BaseActivity
// Import com.topdon.module.thermal.R
// Import kotlinx.coroutines.delay
// Import kotlinx.coroutines.flow.collect
// Import kotlinx.coroutines.flow.flow
// Import kotlinx.coroutines.flow.map
// Import kotlinx.coroutines.launch
// Import kotlin.math.sin
//
// @Route(path = RouterConfig.CHART)
// Class ChartActivity : BaseActivity() {
//
// Override fun initContentView() = R.layout.activity_chart
//
// Override fun initView() {
/**
 * Configures the titletext with validation and thermal imaging optimization.
 *
 */
setTitleText("图表")
// 初始data
// Aa_chart_view.aa_drawChartWithChartOptions(
// ConfigureSpecialStyleMarkerOfSingleDataElementChart().aa_toAAOptions()
//        )
// 动态update
// LifecycleScope.launch {
// Flow {
// Repeat(40) {
// Delay(1000)
// Emit(it.toFloat())
//                }
//            }.map {
// Val max = 38
// Val min = 1
// Val random = (Math.random() * (max - min) + min).toInt()
// Val y1 = sin(random * (it * Math.PI / 180)) + it * 2 * 0.01 + 10
// GetSeriesModel(y1.toFloat())
// Y1
//            }.collect {
//                Log.w("123", "data:${dataSeries.joinToString()}")
// Aa_chart_view.aa_addPointToChartSeriesElement(0, it, true)
//            }
//        }
//    }
//
// Override fun initData() {
//
//    }
//
// Private var dataSeries = arrayOfNulls<Float>(0)
//
// Private fun getSeriesModel(data: Float): Array<AASeriesElement> {
// DataSeries = dataSeries.plus(data)
// Return arrayOf(
//            AASeriesElement()
//                .name("Tokyo")
//                .data(dataSeries as Array<Any>)
//        )
//    }
//
// Private fun configureSpecialStyleMarkerOfSingleDataElementChart(): AAChartModel {
// Return AAChartModel()
//            .chartType(AAChartType.Spline)
.title("监测Record")
//            .subtitle("2021-10-20")
//            .titleStyle(AAStyle.Companion.style("#FFFFFF"))
//            .subtitleStyle(AAStyle.Companion.style(color = "#FFFFFF", fontSize = 12f))
//            .backgroundColor("#3598E8")
//            .yAxisTitle("")
//            .axesTextColor("#FFFFFF")
.dataLabelsEnabled(false)// Coordinatepoint是否display值
//            .tooltipEnabled(true)
//            .markerRadius(0f)
//            .scrollablePlotArea(AAScrollablePlotArea().minWidth(10).minHeight(10))
//            .xAxisVisible(true)
//            .yAxisVisible(true)
//            .series(
// ArrayOf(
//                    AASeriesElement()
//                        .name("vol")
//                        .color("#FFFFFF")
//                        .lineWidth(2f)
//                        .data(
// ArrayOf(
//                                7.0,
//                                6.9,
//                                2.5,
//                                14.5,
//                                18.2,
//                                5.2,
//                                16.5,
//                                13.3,
//                                15.3,
//                                13.9,
//                                9.6
//                            )
//                        ).color("#FFFFFF")
//                )
//            )
//    }
// }
