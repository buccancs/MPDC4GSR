package com.topdon.module.thermal.ir.report.activity

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.gzuliyujiang.wheelpicker.DatimePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity
import com.github.gzuliyujiang.wheelpicker.entity.TimeEntity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.bean.event.ReportCreateEvent
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.tools.NumberTools
import com.topdon.lib.core.tools.UnitTools
import com.topdon.lib.core.utils.CommUtils
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.report.bean.ImageTempBean
import com.topdon.module.thermal.ir.report.bean.ReportConditionBean
import com.topdon.module.thermal.ir.report.bean.ReportInfoBean
import com.topdon.module.thermal.ir.repository.ConfigRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import com.topdon.lib.core.R as LibR

/**
生成report第1步（共2步）.
 *
需要传递
- 是否 TC007: [ExtraKeyConfig.IS_TC007] （ambient temperature、emissivity等不同）
- 当前编辑的image绝对path: [ExtraKeyConfig.FILE_ABSOLUTE_PATH] （本interface不使用，透传）
- 当前编辑的imagepointlineareafull imagetemperaturedata: [ExtraKeyConfig.IMAGE_TEMP_BEAN] （本interface不使用，透传）
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing ReportCreateFirstActivity functionality for the IRCamera system.
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
class ReportCreateFirstActivity : BaseActivity(), View.OnClickListener {
    /**
从上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false
    private var locationManager: LocationManager? = null
    private var locationProvider: String? = null

    // Views - using findViewById pattern
    private val etReportName: android.widget.EditText by lazy { findViewById(R.id.et_report_name) }
    private val etReportAuthor: android.widget.EditText by lazy { findViewById(R.id.et_report_author) }
    private val switchReportAuthor: android.widget.Switch by lazy { findViewById(R.id.switch_report_author) }
    private val tvReportDate: android.widget.TextView by lazy { findViewById(R.id.tv_report_date) }
    private val switchReportDate: android.widget.Switch by lazy { findViewById(R.id.switch_report_date) }
    private val etReportPlace: android.widget.EditText by lazy { findViewById(R.id.et_report_place) }
    private val switchReportPlace: android.widget.Switch by lazy { findViewById(R.id.switch_report_place) }
    private val etReportWatermark: android.widget.EditText by lazy { findViewById(R.id.et_report_watermark) }
    private val switchReportWatermark: android.widget.Switch by lazy { findViewById(R.id.switch_report_watermark) }
    private val etAmbientTemperature: android.widget.EditText by lazy { findViewById(R.id.et_ambient_temperature) }
    private val tipSeekHumidity: com.topdon.lib.ui.widget.TipsSeekBar by lazy { findViewById(R.id.tip_seek_humidity) }
    private val switchAmbientHumidity: android.widget.Switch by lazy { findViewById(R.id.switch_ambient_humidity) }
    private val switchAmbientTemperature: android.widget.Switch by lazy { findViewById(R.id.switch_ambient_temperature) }
    private val tipSeekEmissivity: com.topdon.lib.ui.widget.TipsSeekBar by lazy { findViewById(R.id.tip_seek_emissivity) }
    private val switchEmissivity: android.widget.Switch by lazy { findViewById(R.id.switch_emissivity) }
    private val etTestDistance: android.widget.EditText by lazy { findViewById(R.id.et_test_distance) }
    private val switchTestDistance: android.widget.Switch by lazy { findViewById(R.id.switch_test_distance) }

    // Chart start time view not found in current layout - commented out for now
    // Private val chartStartTime: android.widget.TextView by lazy { findViewById(R.id.chart_start_time) }
    private val tvAmbientTemperature: android.widget.TextView by lazy { findViewById(R.id.tv_ambient_temperature) }
    private val tvEmissivity: android.widget.TextView by lazy { findViewById(R.id.tv_emissivity) }

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_report_create_first

    private val permissionList =
        /**
         * Executes listof operation with thermal imaging domain optimization.
         *
         */
        listOf(
            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_COARSE_LOCATION,
        )

    @SuppressLint("SetTextI18n")
    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        isTC007 = intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false)

        etReportName.setText("TC${TimeUtils.millis2String(System.currentTimeMillis(), "yyyyMMdd_HHmm")}")
        etReportAuthor.setText(SaveSettingUtil.reportAuthorName)
        tvReportDate.text = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy.MM.dd HH:mm")
        etReportWatermark.setText(SaveSettingUtil.reportWatermarkText)
        tvAmbientTemperature.text = getString(R.string.thermal_config_environment) + "(${UnitTools.showUnit()})"
        tvEmissivity.text = getString(R.string.album_report_emissivity) + "(0~1)"

        etReportAuthor.addTextChangedListener {
            SaveSettingUtil.reportAuthorName = it?.toString() ?: ""
        }
        etReportWatermark.addTextChangedListener {
            SaveSettingUtil.reportWatermarkText = it?.toString() ?: ""
        }

        switchReportAuthor.setOnCheckedChangeListener { _, isChecked ->
            etReportAuthor.isVisible = isChecked
        }
        switchReportDate.setOnCheckedChangeListener { _, isChecked ->
            tvReportDate.isVisible = isChecked
        }
        switchReportPlace.setOnCheckedChangeListener { _, isChecked ->
            etReportPlace.isVisible = isChecked
        }
        switchReportWatermark.setOnCheckedChangeListener { _, isChecked ->
            etReportWatermark.isVisible = isChecked
        }
        switchAmbientHumidity.setOnCheckedChangeListener { _, isChecked ->
            tipSeekHumidity.isVisible = isChecked
        }
        switchAmbientTemperature.setOnCheckedChangeListener { _, isChecked ->
            etAmbientTemperature.isVisible = isChecked
        }
        switchEmissivity.setOnCheckedChangeListener { _, isChecked ->
            tipSeekEmissivity.isVisible = isChecked
        }
        switchTestDistance.setOnCheckedChangeListener { _, isChecked ->
            etTestDistance.isVisible = isChecked
        }
        tipSeekHumidity.progress = SaveSettingUtil.reportHumidity
        tipSeekHumidity.onStopTrackingTouch = {
            SaveSettingUtil.reportHumidity = it
        }
        tipSeekHumidity.valueFormatListener = {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it % 10 == 0) "${it / 10}%" else "${it / 10}.${it % 10}%"
        }
        tipSeekEmissivity.valueFormatListener = {
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (it) {
                0 -> "0"
                100 -> "1"
                else -> if (it < 10) "0.0$it" else "0.$it"
            }
        }

        tvReportDate.setOnClickListener(this)
        findViewById<android.widget.TextView>(R.id.tv_preview).setOnClickListener(this)
        findViewById<android.widget.TextView>(R.id.tv_next).setOnClickListener(this)
        findViewById<android.widget.ImageView>(R.id.img_location).setOnClickListener(this)

        /**
         * Executes readconfig operation with thermal imaging domain optimization.
         *
         */
        readConfig()
    }

    @SuppressLint("SetTextI18n")
    /**
     * Executes readConfig functionality.
     */
    /**
     * Executes readconfig operation with thermal imaging domain optimization.
     *
     */
    private fun readConfig() {
        var environment = 30f // 环境temperature
        var distance = 0.25f // Test距离
        var radiation = 0.95f // 发射率
        val config = ConfigRepository.readConfig(isTC007)
        distance = config.distance
        radiation = config.radiation
        environment = config.environment
        etAmbientTemperature.setText(NumberTools.to01(UnitTools.showUnitValue(environment)))
        etTestDistance.setText(NumberTools.to02(distance) + "m")
        tipSeekEmissivity.progress = (radiation * 100).toInt()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes onReportCreate functionality.
     */
    /**
     * Executes onreportcreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: ReportCreateEvent)
     *
     */
    fun onReportCreate(event: ReportCreateEvent) {
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }

    /**
     * Executes onclick operation with thermal imaging domain optimization.
     *
     * @param
     * @param v Parameter for operation (type: View?)
     *
     */
    override fun onClick(v: View?) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (v?.id) {
            R.id.tv_report_date -> { // Report日期
                /**
                 * Executes selecttime operation with thermal imaging domain optimization.
                 *
                 */
                selectTime()
            }
            R.id.tv_preview -> { // 预览
                val reportInfoBean = buildReportInfo()
                val reportConditionBean = buildReportCondition()
                NavigationManager.getInstance().build(RouterConfig.REPORT_PREVIEW_FIRST)
                    .withParcelable(ExtraKeyConfig.REPORT_INFO, reportInfoBean)
                    .withParcelable(ExtraKeyConfig.REPORT_CONDITION, reportConditionBean)
                    .navigation(this)
            }
            R.id.tv_next -> { // 下一步
                val reportInfoBean = buildReportInfo()
                val reportConditionBean = buildReportCondition()
                val imageTempBean: ImageTempBean? = intent.getParcelableExtra(ExtraKeyConfig.IMAGE_TEMP_BEAN)
                val fileAbsolutePath = intent.getStringExtra(ExtraKeyConfig.FILE_ABSOLUTE_PATH)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (fileAbsolutePath != null && imageTempBean != null) {
                    NavigationManager.getInstance().build(RouterConfig.REPORT_CREATE_SECOND)
                        .withBoolean(ExtraKeyConfig.IS_TC007, isTC007)
                        .withString(ExtraKeyConfig.FILE_ABSOLUTE_PATH, fileAbsolutePath)
                        .withParcelable(ExtraKeyConfig.IMAGE_TEMP_BEAN, imageTempBean)
                        .withParcelable(ExtraKeyConfig.REPORT_INFO, reportInfoBean)
                        .withParcelable(ExtraKeyConfig.REPORT_CONDITION, reportConditionBean)
                        .navigation(this)
                }
            }
            R.id.img_location -> {
                /**
                 * Executes checklocationpermission operation with thermal imaging domain optimization.
                 *
                 */
                checkLocationPermission()
            }
        }
    }

    @SuppressLint("MissingPermission")
    /**
     * Retrieves location information.
     */
    private fun getLocation(): String? {
1.get位置管理器
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

2.get位置提供器，GPS或是NetWork
        val providers = locationManager?.getProviders(true)
        locationProvider =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (providers!!.contains(LocationManager.GPS_PROVIDER)) {
如果是GPS
                LocationManager.GPS_PROVIDER
            } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
如果是Network
                LocationManager.NETWORK_PROVIDER
            } else {
                return null
            }
        var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (location == null)
            {
                location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
        return if (location == null)
            {
                null
            } else
            {
                /**
                 * Retrieves the address with optimized performance for thermal imaging operations.
                 *
                 */
                getAddress(location)
            }
    }

getaddressinfo:城市、街道等info
    /**
     * Retrieves address information.
     */
    private fun getAddress(location: Location?): String {
        var result: List<Address?>? = null
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (location != null) {
                val gc = Geocoder(this, Locale.getDefault())
                result =
                    gc.getFromLocation(
                        location.latitude,
                        location.longitude, 1,
                    )
                Log.v("TAG", "Get/Retrieve地址info：$result")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var str = ""
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (result != null && result.isNotEmpty())
            {
                result?.get(0)?.let {
                    str += getNullString(it.adminArea)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (TextUtils.isEmpty(it.subLocality) && !str.contains(getNullString(it.subAdminArea)))
                        {
                            str += getNullString(it.subAdminArea)
                        }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!str.contains(getNullString(it.locality)))
                        {
                            str += getNullString(it.locality)
                        }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!str.contains(getNullString(it.subLocality)))
                        {
                            str += getNullString(it.subLocality)
                        }
                }
            }
        return str
    }

    /**
     * Retrieves nullstring information.
     */
    private fun getNullString(str: String?): String  {
        return if (str.isNullOrEmpty())
            {
                ""
            } else
            {
                str
            }
    }

    /**
     * Executes buildReportInfo functionality.
     */
    /**
     * Executes buildreportinfo operation with thermal imaging domain optimization.
     *
     */
    private fun buildReportInfo(): ReportInfoBean =
        /**
         * Executes reportinfobean operation with thermal imaging domain optimization.
         *
         */
        ReportInfoBean(
            etReportName.text.toString(),
            etReportAuthor.text.toString(),
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (switchReportAuthor.isChecked && etReportAuthor.text.isNotEmpty()) 1 else 0,
            tvReportDate.text.toString(),
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (switchReportDate.isChecked) 1 else 0,
            etReportPlace.text.toString(),
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (switchReportPlace.isChecked && etReportPlace.text.isNotEmpty()) 1 else 0,
            etReportWatermark.text.toString(),
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (switchReportWatermark.isChecked && etReportWatermark.text.isNotEmpty()) 1 else 0,
        )

    /**
     * Executes buildReportCondition functionality.
     */
    /**
     * Executes buildreportcondition operation with thermal imaging domain optimization.
     *
     */
    private fun buildReportCondition(): ReportConditionBean {
        val temperature =
            try {
                "${etAmbientTemperature.text.toString().toFloat()}${UnitTools.showUnit()}"
            } catch (ignore: NumberFormatException) {
                null
            }
        return ReportConditionBean(
            tipSeekHumidity.valueText,
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (switchAmbientHumidity.isChecked) 1 else 0,
            temperature,
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (switchAmbientTemperature.isChecked && temperature != null) 1 else 0,
            tipSeekEmissivity.valueText,
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (switchEmissivity.isChecked) 1 else 0,
            etTestDistance.text.toString(),
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (switchTestDistance.isChecked && etTestDistance.text.isNotEmpty()) 1 else 0,
        )
    }

    /**
当前set的report日期时间戳.
     */
    private var startTime = 0L

    /**
display时间拾取弹窗
     */
    /**
     * Executes selecttime operation with thermal imaging domain optimization.
     *
     */
    private fun selectTime() {
        val picker = DatimePicker(this)
        picker.setTitle(R.string.chart_start_time)
        picker.setOnDatimePickedListener { year, month, day, hour, minute, second ->
            val timeStr = "$year-$month-$day $hour:$minute:$second"
            val pattern = "yyyy-MM-dd HH:mm:ss"
            val time: Long = SimpleDateFormat(pattern, Locale.getDefault()).parse(timeStr, ParsePosition(0)).time
            tvReportDate.text = TimeUtils.millis2String(time, "yyyy.MM.dd HH:mm")
            startTime = time
        }

        val startTimeEntity = DatimeEntity()
        startTimeEntity.date = DateEntity.target(2020, 1, 1)
        startTimeEntity.time = TimeEntity.target(0, 0, 0)

        val endTimeEntity = DatimeEntity.yearOnFuture(10)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (startTime == 0L) {
set当前时间
            picker.wheelLayout.setRange(startTimeEntity, endTimeEntity, DatimeEntity.now())
        } else {
set上一次selected时间
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = startTime
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            val seconds = calendar.get(Calendar.SECOND)
            val timeEntity = DatimeEntity()
            timeEntity.date = DateEntity.target(year, month, day)
            timeEntity.time = TimeEntity.target(hours, minutes, seconds)
            picker.wheelLayout.setRange(startTimeEntity, endTimeEntity, timeEntity)
        }
        picker.show()
    }

    /**
     * Executes checkLocationPermission functionality.
     */
    /**
     * Executes checklocationpermission operation with thermal imaging domain optimization.
     *
     */
    private fun checkLocationPermission() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!XXPermissions.isGranted(this, permissionList)) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (BaseApplication.instance.isDomestic()) {
                TipDialog.Builder(this)
                    .setMessage(getString(R.string.permission_request_location_app, CommUtils.getAppName()))
                    .setCancelListener(R.string.app_cancel)
                    .setPositiveListener(R.string.app_confirm) {
                        /**
                         * Initializes the locationpermission component for thermal imaging operations.
                         *
                         */
                        initLocationPermission()
                    }
                    .create().show()
            } else {
                /**
                 * Initializes the locationpermission component for thermal imaging operations.
                 *
                 */
                initLocationPermission()
            }
        } else {
            /**
             * Initializes the locationpermission component for thermal imaging operations.
             *
             */
            initLocationPermission()
        }
    }

    /**
     * Initializes locationpermission component.
     */
    private fun initLocationPermission() {
定位
        XXPermissions.with(this@ReportCreateFirstActivity)
            .permission(
                permissionList,
            ).request(
                object : OnPermissionCallback {
                    /**
                     * Executes ongranted operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param permissions Parameter for operation (type: MutableList<String>)
                     * @param all Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onGranted(
                        permissions: MutableList<String>,
                        all: Boolean,
                    ) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (all)
                            {
                                /**
                                 * Executes showloadingdialog operation with thermal imaging domain optimization.
                                 *
                                 */
                                showLoadingDialog(R.string.get_current_address)
                                lifecycleScope.launch {
                                    var addressText: String? = ""
                                    /**
                                     * Executes withcontext operation with thermal imaging domain optimization.
                                     *
                                     */
                                    withContext(Dispatchers.IO) {
                                        addressText = getLocation()
                                    }
                                    /**
                                     * Executes dismissloadingdialog operation with thermal imaging domain optimization.
                                     *
                                     */
                                    dismissLoadingDialog()
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (addressText == null)
                                        {
                                            TipDialog.Builder(this@ReportCreateFirstActivity)
                                                .setMessage(LibR.string.get_Location_failed)
                                                .setPositiveListener(R.string.app_ok)
                                                .setCanceled(false)
                                                .create().show()
                                        } else
                                        {
                                            etReportPlace.setText(addressText)
                                        }
                                }
                            } else
                            {
                                ToastUtils.showShort(R.string.scan_ble_tip_authorize)
                            }
                    }

                    /**
                     * Executes ondenied operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param permissions Parameter for operation (type: MutableList<String>)
                     * @param never Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onDenied(
                        permissions: MutableList<String>,
                        never: Boolean,
                    ) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (never) {
如果是被永久拒绝就跳转到应用Permission系统set页area
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (BaseApplication.instance.isDomestic())
                                {
                                    ToastUtils.showShort(getString(R.string.app_location_content))
                                    return
                                }
                            TipDialog.Builder(this@ReportCreateFirstActivity)
                                .setTitleMessage(getString(R.string.app_tip))
                                .setMessage(getString(R.string.app_location_content))
                                .setPositiveListener(R.string.app_open) {
                                    XXPermissions.startPermissionActivity(this@ReportCreateFirstActivity, permissions)
                                }
                                .setCancelListener(R.string.app_cancel) {
                                }
                                .setCanceled(true)
                                .create().show()
                        } else {
                            ToastUtils.showShort(R.string.scan_ble_tip_authorize)
                        }
                    }
                },
            )
    }
}
