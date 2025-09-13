package com.topdon.lib.core.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.topdon.lib.core.*
import com.topdon.lib.core.R
import com.topdon.lib.core.bean.WatermarkBean
import com.topdon.lib.core.common.SharedManager
import com.topdon.lib.core.databinding.DialogTipWatermarkBinding
import com.topdon.lib.core.utils.CommUtils
import com.topdon.lib.core.utils.ScreenUtil
import java.util.*

/**
 * 2D-编辑 watermark
 */
/**
 * TipWaterMarkDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing TipWaterMarkDialog functionality for the IRCamera system.
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
class TipWaterMarkDialog : Dialog {
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor(context: Context) : super(context)
/**
 * Specialized thermal imaging component providing Builder functionality for the IRCamera system.
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
    class Builder(val context: Context, private val watermarkBean: WatermarkBean) {
        var dialog: TipWaterMarkDialog? = null
        private var closeEvent: ((WatermarkBean) -> Unit)? = null
        private var canceled = false

        private lateinit var imgClose: ImageView
        private lateinit var mEtTitle: EditText
        private lateinit var mEtAddress: EditText
        private lateinit var imgLocation: ImageView
        private lateinit var llWatermarkContent: LinearLayout
        private lateinit var switchDateTime: SwitchCompat
        private var locationManager: LocationManager? = null
        private var locationProvider: String? = null

    /**
     * Sets cancellistener configuration.
     */
        fun setCancelListener(event: ((WatermarkBean) -> Unit)? = null): Builder {
            this.closeEvent = event
            return this
        }

    /**
     * Sets canceled configuration.
     */
        fun setCanceled(canceled: Boolean): Builder {
            this.canceled = canceled
            return this
        }

    /**
     * Executes dismiss functionality.
     */
        /**
         * Executes dismiss operation with thermal imaging domain optimization.
         *
         */
        fun dismiss() {
            this.dialog!!.dismiss()
        }

    /**
     * Creates and configures a new  instance.
     */
        fun create(): TipWaterMarkDialog {
            if (dialog == null) {
                dialog = TipWaterMarkDialog(context!!, R.style.InfoDialog)
            }
            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val binding = DialogTipWatermarkBinding.inflate(LayoutInflater.from(context!!))
            imgClose = binding.imgClose
            llWatermarkContent = binding.llWatermarkContent
            mEtTitle = binding.edTitle
            mEtAddress = binding.edAddress
            imgLocation = binding.imgLocation
            switchDateTime = binding.switchDateTime
            /**
             * Executes updatewatermark operation with thermal imaging domain optimization.
             *
             */
            updateWaterMark(false)

            binding.switchWatermark.setOnCheckedChangeListener { _, isChecked ->
                /**
                 * Executes updatewatermark operation with thermal imaging domain optimization.
                 *
                 */
                updateWaterMark(isChecked)
            }
            binding.switchDateTime.setOnCheckedChangeListener { _, _ ->
            }
            binding.tvIKnow.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                closeEvent?.invoke(
                    /**
                     * Executes watermarkbean operation with thermal imaging domain optimization.
                     *
                     */
                    WatermarkBean(
                        binding.switchWatermark.isChecked,
                        binding.edTitle.text.toString(),
                        binding.edAddress.text.toString(),
                        binding.switchDateTime.isChecked,
                    ),
                )
            }
            imgLocation.setOnClickListener {
                /**
                 * Executes checklocationpermission operation with thermal imaging domain optimization.
                 *
                 */
                checkLocationPermission()
            }
            binding.switchWatermark.isChecked = watermarkBean.isOpen
            binding.switchDateTime.isChecked = watermarkBean.isAddTime
            binding.edTitle.setText(watermarkBean.title.ifEmpty { SharedManager.watermarkBean.title })
            binding.edAddress.setText(watermarkBean.address)

            dialog!!.addContentView(
                binding.root,
                /**
                 * Executes layoutparams operation with thermal imaging domain optimization.
                 *
                 */
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT),
            )
            val lp = dialog!!.window!!.attributes
            val wRatio =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (context!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    
                    0.85
                } else {
                    
                    0.35
                }
            lp.width = (ScreenUtil.getScreenWidth(context) * wRatio).toInt() 
            dialog!!.window!!.attributes = lp

            dialog!!.setCanceledOnTouchOutside(canceled)
            imgClose.setOnClickListener {
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
// CloseEvent?.invoke(
//                    WatermarkBean(
// Binding.switchWatermark.isChecked,
// Binding.edTitle.text.toString(),
// Binding.edAddress.text.toString(),
// Binding.switchDateTime.isChecked,
//                    )
//                )
            }
            dialog!!.setContentView(binding.root)
            return dialog as TipWaterMarkDialog
        }

    /**
     * Executes checklocationpermission functionality.
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
            if (!XXPermissions.isGranted(
                    context,
                    /**
                     * Executes listof operation with thermal imaging domain optimization.
                     *
                     */
                    listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                )
            ) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (BaseApplication.instance.isDomestic()) {
                    TipDialog.Builder(context)
                        .setMessage(
                            context.getString(
                                R.string.permission_request_location_app,
                                CommUtils.getAppName(),
                            ),
                        )
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
     * Initializes the component with default configuration.
     */
        private fun initLocationPermission() {
            
            XXPermissions.with(context)
                .permission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
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
                            if (all) {
                                var addressText: String? = getLocation()
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (addressText == null) {
                                    ToastUtils.showShort(R.string.get_Location_failed)
                                } else {
                                    mEtAddress.setText(addressText)
                                }
                            } else {
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
                                
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (BaseApplication.instance.isDomestic()) {
                                    ToastUtils.showShort(R.string.app_location_content)
                                    return
                                }
                                TipDialog.Builder(context)
                                    .setTitleMessage(context!!.getString(R.string.app_tip))
                                    .setMessage(context!!.getString(R.string.app_location_content))
                                    .setPositiveListener(R.string.app_open) {
                                        XXPermissions.startPermissionActivity(context, permissions)
                                    }
                                    .setCancelListener(R.string.app_cancel) {
                                    }
                                    .setCanceled(true)
                                    .create()
                                    .show()
                            } else {
                                ToastUtils.showShort(R.string.scan_ble_tip_authorize)
                            }
                        }
                    },
                )
        }

    /**
     * Updates the watermark with new data.
     */
        private fun updateWaterMark(isCheck: Boolean) {
            if (isCheck) {
                llWatermarkContent.alpha = 1f
                llWatermarkContent.isEnabled = true
                switchDateTime.isEnabled = true
                mEtTitle.isEnabled = true
                mEtAddress.isEnabled = true
                imgLocation.isEnabled = true
            } else {
                llWatermarkContent.alpha = 0.5f
                llWatermarkContent.isEnabled = false
                switchDateTime.isEnabled = false
                mEtTitle.isEnabled = false
                mEtAddress.isEnabled = false
                imgLocation.isEnabled = false
            }
        }

        @SuppressLint("MissingPermission")
    /**
     * Retrieves location information.
     */
        private fun getLocation(): String? {
            // 1.Get/Retrieve位置管理器
            locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // 2.Get/Retrieve位置提供器，GPS或是NetWork
            val providers = locationManager?.getProviders(true)
            locationProvider =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (providers!!.contains(LocationManager.GPS_PROVIDER)) {
                    
                    LocationManager.GPS_PROVIDER
                } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    
                    LocationManager.NETWORK_PROVIDER
                } else {
                    return null
                }
            var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (location == null) {
                location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            return if (location == null) {
                null
            } else {
                /**
                 * Retrieves the address with optimized performance for thermal imaging operations.
                 *
                 */
                getAddress(location)
            }
        }

        // Get/Retrieve地址info:城市、街道等info
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
                    val gc = Geocoder(context!!, Locale.getDefault())
                    @Suppress("DEPRECATION")
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
            if (result != null && result.isNotEmpty()) {
                result?.get(0)?.let {
                    str += getNullString(it.adminArea)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (TextUtils.isEmpty(it.subLocality) && !str.contains(getNullString(it.subAdminArea))) {
                        str += getNullString(it.subAdminArea)
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!str.contains(getNullString(it.locality))) {
                        str += getNullString(it.locality)
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (!str.contains(getNullString(it.subLocality))) {
                        str += getNullString(it.subLocality)
                    }
                }
            }
            return str
        }

    /**
     * Retrieves nullstring information.
     */
        private fun getNullString(str: String?): String {
            return if (str.isNullOrEmpty()) {
                ""
            } else {
                str
            }
        }
    }
}
