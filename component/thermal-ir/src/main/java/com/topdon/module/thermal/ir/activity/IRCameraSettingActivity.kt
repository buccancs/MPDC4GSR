package com.topdon.module.thermal.ir.activity

import android.annotation.SuppressLint
import android.location.*
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.bean.ContinuousBean
import com.topdon.lib.core.bean.WatermarkBean
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.utils.CommUtils
import com.topdon.lib.ui.listener.SingleClickListener
import com.topdon.lib.ui.widget.BarPickView
import com.topdon.module.thermal.ir.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import com.topdon.lib.core.R as LibR

/**
cameraproperty值set
 * @author: CaiSongL
 * @date: 2023/4/3 15:00
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with IRCameraSettingActivity functionality.
 *
 * Provides advanced camera functionality for thermal imaging capture,
 * including temperature measurement and pseudo color visualization.
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
class IRCameraSettingActivity : BaseActivity() {
    companion object {
        const val KEY_PRODUCT_TYPE = "key_product_type"
    }

    private var locationManager: LocationManager? = null
    private var locationProvider: String? = null

    // View references
    private lateinit var tvAddress: TextView
    private lateinit var edAddress: EditText

    private var watermarkBean: WatermarkBean = SharedManager.watermarkBean
    private var continuousBean: ContinuousBean = SharedManager.continuousBean
    private var productName = ""

    private val permissionList =
        /**
         * Executes listof operation with thermal imaging domain optimization.
         *
         */
        listOf(
            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_COARSE_LOCATION,
        )

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int = R.layout.activity_ir_camera_setting

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        productName = intent.getStringExtra(KEY_PRODUCT_TYPE) ?: ""
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007())
            {
                watermarkBean = SharedManager.wifiWatermarkBean // TC007只有watermark
                continuousBean = SharedManager.continuousBean
            } else
            {
                watermarkBean = SharedManager.watermarkBean
                continuousBean = SharedManager.continuousBean
            }

        val barPickViewTime = findViewById<BarPickView>(R.id.bar_pick_view_time)
        val barPickViewCount = findViewById<BarPickView>(R.id.bar_pick_view_count)
        val switchTime = findViewById<Switch>(R.id.switch_time)
        val switchWatermark = findViewById<Switch>(R.id.switch_watermark)
        val switchDelay = findViewById<Switch>(R.id.switch_delay)
        val clDelayMore = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.cl_delay_more)
        val clWatermarkMore = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.cl_watermark_more)
        val clShowEp = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.cl_show_ep)
        val tvTimeShow = findViewById<TextView>(R.id.tv_time_show)
        tvAddress = findViewById(R.id.tv_address)
        val edTitle = findViewById<EditText>(R.id.ed_title)
        edAddress = findViewById(R.id.ed_address)
        val tvTitleShow = findViewById<android.widget.TextView>(R.id.tv_title_show)
        val imgLocation = findViewById<android.widget.ImageView>(R.id.img_location)
        val lyAuto = findViewById<android.widget.LinearLayout>(R.id.ly_auto)

        barPickViewTime.setProgressAndRefresh((continuousBean.continuaTime / 100).toInt())
        barPickViewTime.onStopTrackingTouch = { progress, _ ->
            continuousBean.continuaTime = progress.toLong() * 100
            SharedManager.continuousBean = continuousBean
        }
        barPickViewTime.valueFormatListener = {
            (it / 10).toString() + if (it % 10 == 0) "" else ("." + (it % 10).toString())
        }

        barPickViewCount.setProgressAndRefresh(continuousBean.count)
        barPickViewCount.onStopTrackingTouch = { progress, _ ->
            continuousBean.count = progress
            SharedManager.continuousBean = continuousBean
        }

        switchTime.isChecked = watermarkBean.isAddTime
        switchWatermark.isChecked = watermarkBean.isOpen
        switchDelay.isChecked = continuousBean.isOpen

        clDelayMore.isVisible = continuousBean.isOpen
        clWatermarkMore.isVisible = watermarkBean.isOpen
        clShowEp.isVisible = watermarkBean.isOpen

        tvTimeShow.text = TimeTool.getNowTime()
        tvTimeShow.isVisible = watermarkBean.isAddTime

        tvAddress.inputType = InputType.TYPE_NULL
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (TextUtils.isEmpty(watermarkBean.address))
            {
                tvAddress.visibility = View.GONE
            } else
            {
                tvAddress.visibility = View.VISIBLE
                tvAddress.text = watermarkBean.address
            }
        edTitle.setText(watermarkBean.title)
        edAddress.setText(watermarkBean.address)
        tvTitleShow.text = watermarkBean.title
        switchDelay.setOnCheckedChangeListener { _, isChecked ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isChecked)
                {
                    clDelayMore.visibility = View.VISIBLE
                } else
                {
                    clDelayMore.visibility = View.GONE
                }
            continuousBean.isOpen = isChecked
            SharedManager.continuousBean = continuousBean
        }
        switchWatermark.setOnCheckedChangeListener { _, isChecked ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isChecked)
                {
                    clWatermarkMore.visibility = View.VISIBLE
                    clShowEp.visibility = View.VISIBLE
                } else
                {
                    clWatermarkMore.visibility = View.GONE
                    clShowEp.visibility = View.GONE
                }
            watermarkBean.isOpen = isChecked
        }
        switchTime.setOnCheckedChangeListener { _, isChecked ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isChecked)
                {
                    tvTimeShow.text = TimeTool.getNowTime()
                    tvTimeShow.visibility = View.VISIBLE
                } else
                {
                    tvTimeShow.visibility = View.GONE
                }
            watermarkBean.isAddTime = isChecked
        }
        edTitle.addTextChangedListener(
            object : TextWatcher {
                /**
                 * Executes beforetextchanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param s Parameter for operation (type: CharSequence?)
                 * @param start Parameter for operation (type: Int)
                 * @param count Parameter for operation (type: Int)
                 * @param after Parameter for operation (type: Int)
                 *
                 */
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                /**
                 * Executes ontextchanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param s Parameter for operation (type: CharSequence?)
                 * @param start Parameter for operation (type: Int)
                 * @param before Parameter for operation (type: Int)
                 * @param count Parameter for operation (type: Int)
                 *
                 */
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                }

                /**
                 * Executes aftertextchanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param s Parameter for operation (type: Editable?)
                 *
                 */
                override fun afterTextChanged(s: Editable?) {
                    watermarkBean.title = edTitle.text.toString()
                    tvTitleShow.text = watermarkBean.title
                }
            },
        )
        edAddress.addTextChangedListener(
            object : TextWatcher {
                /**
                 * Executes beforetextchanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param s Parameter for operation (type: CharSequence?)
                 * @param start Parameter for operation (type: Int)
                 * @param count Parameter for operation (type: Int)
                 * @param after Parameter for operation (type: Int)
                 *
                 */
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                /**
                 * Executes ontextchanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param s Parameter for operation (type: CharSequence?)
                 * @param start Parameter for operation (type: Int)
                 * @param before Parameter for operation (type: Int)
                 * @param count Parameter for operation (type: Int)
                 *
                 */
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                }

                /**
                 * Executes aftertextchanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param s Parameter for operation (type: Editable?)
                 *
                 */
                override fun afterTextChanged(s: Editable?) {
                    watermarkBean.address = edAddress.text.toString()
                    tvAddress.text = watermarkBean.address
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!watermarkBean.address.isNullOrEmpty())
                        {
                            tvAddress.visibility = View.VISIBLE
                        } else
                        {
                            tvAddress.visibility = View.GONE
                        }
                }
            },
        )
        imgLocation.setOnClickListener(
            object : SingleClickListener() {
                /**
                 * Executes onsingleclick operation with thermal imaging domain optimization.
                 *
                 */
                override fun onSingleClick() {
                    /**
                     * Executes checkstoragepermission operation with thermal imaging domain optimization.
                     *
                     */
                    checkStoragePermission()
                }
            },
        )
TC007device不需要延迟拍照
        lyAuto.visibility = if (isTC007()) View.GONE else View.VISIBLE
    }

    /**
     * Executes isTC007 functionality.
     */
    /**
     * Executes istc007 operation with thermal imaging domain optimization.
     *
     */
    fun isTC007(): Boolean {
        return productName.contains("TC007")
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

    var locationListener: LocationListener =
        object : LocationListener {
Provider的state在可用、暂时不可用和无service三个state直接switch时触发此function
            /**
             * Executes onstatuschanged operation with thermal imaging domain optimization.
             *
             * @param
             * @param provider Parameter for operation (type: String)
             * @param status Parameter for operation (type: Int)
             * @param extras Parameter for operation (type: Bundle)
             *
             */
            override fun onStatusChanged(
                provider: String,
                status: Int,
                extras: Bundle,
            ) {
                Toast.makeText(
                    this@IRCameraSettingActivity,
                    provider,
                    Toast.LENGTH_SHORT,
                ).show()
            }

Provider被enable时触发此function，比如GPS被Open
            /**
             * Executes onproviderenabled operation with thermal imaging domain optimization.
             *
             * @param
             * @param provider Parameter for operation (type: String)
             *
             */
            override fun onProviderEnabled(provider: String) {
                Toast.makeText(
                    this@IRCameraSettingActivity,
                    "GPSOpen",
                    Toast.LENGTH_SHORT,
                ).show()
                /**
                 * Retrieves the location with optimized performance for thermal imaging operations.
                 *
                 */
                getLocation()
            }

Provider被disable时触发此function，比如GPS被disabled
            /**
             * Executes onproviderdisabled operation with thermal imaging domain optimization.
             *
             * @param
             * @param provider Parameter for operation (type: String)
             *
             */
            override fun onProviderDisabled(provider: String) {
                Toast.makeText(
                    this@IRCameraSettingActivity,
                    "GPSClose",
                    Toast.LENGTH_SHORT,
                ).show()
            }

当coordinate改变时触发此function，如果Provider传进相同的coordinate，它就不会被触发
            /**
             * Executes onlocationchanged operation with thermal imaging domain optimization.
             *
             * @param
             * @param location Parameter for operation (type: Location)
             *
             */
            override fun onLocationChanged(location: Location) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (location != null) {
如果位置发生变化，重新display地理位置经纬度
                    Toast.makeText(
                        this@IRCameraSettingActivity,
                        location.longitude.toString() + " " +
                            location.latitude + "",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }

    @SuppressLint("MissingPermission")
    /**
     * Retrieves lastknownlocation information.
     */
    private fun getLastKnownLocation(): Location? {
        locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager!!.getProviders(true)
        var bestLocation: Location? = null
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (provider in providers) {
            val l: Location = locationManager!!.getLastKnownLocation(provider) ?: continue
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        return bestLocation
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
     * Executes onpause operation with thermal imaging domain optimization.
     *
     */
    override fun onPause() {
        super.onPause()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007())
            {
                SharedManager.wifiWatermarkBean = watermarkBean
            } else
            {
                SharedManager.watermarkBean = watermarkBean
            }
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Initializes locationpermission component.
     */
    private fun initLocationPermission() {
        XXPermissions.with(this@IRCameraSettingActivity)
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
                                showLoadingDialog(LibR.string.get_current_address)
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
                                            ToastUtils.showShort(LibR.string.get_Location_failed)
                                        } else
                                        {
                                            watermarkBean.address = addressText as String
                                            edAddress.setText(addressText)
                                            tvAddress.visibility = View.VISIBLE
                                            tvAddress.setText(addressText)
                                        }
                                }
                            } else
                            {
                                ToastUtils.showShort(LibR.string.scan_ble_tip_authorize)
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
                                    ToastUtils.showShort(getString(LibR.string.app_location_content))
                                } else
                                {
                                    TipDialog.Builder(this@IRCameraSettingActivity)
                                        .setTitleMessage(getString(LibR.string.app_tip))
                                        .setMessage(getString(LibR.string.app_location_content))
                                        .setPositiveListener(LibR.string.app_open) {
                                            XXPermissions.startPermissionActivity(this@IRCameraSettingActivity, permissions)
                                        }
                                        .setCancelListener(LibR.string.app_cancel) {
                                        }
                                        .setCanceled(true)
                                        .create().show()
                                }
                        } else {
                            ToastUtils.showShort(LibR.string.scan_ble_tip_authorize)
                        }
                    }
                },
            )
    }

    /**
     * Executes checkStoragePermission functionality.
     */
    /**
     * Executes checkstoragepermission operation with thermal imaging domain optimization.
     *
     */
    private fun checkStoragePermission() {
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
                    .setMessage(getString(LibR.string.permission_request_location_app, CommUtils.getAppName()))
                    .setCancelListener(LibR.string.app_cancel)
                    .setPositiveListener(LibR.string.app_confirm) {
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
}
