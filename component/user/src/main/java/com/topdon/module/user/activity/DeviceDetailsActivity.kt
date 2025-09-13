package com.topdon.module.user.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.repository.ProductBean
import com.topdon.lib.core.repository.TC007Repository
import com.topdon.lib.core.repository.TS004Repository
import com.topdon.lms.sdk.utils.TLog
import com.topdon.lms.sdk.weiget.TToast
import com.topdon.module.user.R
import kotlinx.coroutines.launch
import com.topdon.lib.core.R as RCore

/**
TS004、TC007 deviceinfo
 *
需要传递parameter：
- [ExtraKeyConfig.IS_TC007] - 当前device是否为 TC007
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
 * Specialized thermal imaging component providing DeviceDetailsActivity functionality for the IRCamera system.
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
class DeviceDetailsActivity : BaseActivity(), View.OnClickListener {
    // View references - migrated from synthetic views
    private lateinit var clLayoutCopy: ConstraintLayout
    private lateinit var tvSnValue: TextView
    private lateinit var tvDeviceModelValue: TextView
    private lateinit var tvSn: TextView
    private lateinit var tvDeviceModel: TextView

    /**
从上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_device_details

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views - migrated from synthetic views
        clLayoutCopy = findViewById(R.id.cl_layout_copy)
        tvSnValue = findViewById(R.id.tv_sn_value)
        tvDeviceModelValue = findViewById(R.id.tv_device_model_value)
        tvSn = findViewById(R.id.tv_sn)
        tvDeviceModel = findViewById(R.id.tv_device_model)

        isTC007 = intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false)
        clLayoutCopy.setOnClickListener(this)
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
        /**
         * Retrieves the devicedetails with optimized performance for thermal imaging operations.
         *
         */
        getDeviceDetails()
    }

    /**
     * Retrieves devicedetails information.
     */
    private fun getDeviceDetails() {
        lifecycleScope.launch {
            if (isTC007) {
                val productBean: ProductBean? = TC007Repository.getProductInfo()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (productBean == null) {
                    TToast.shortToast(this@DeviceDetailsActivity, RCore.string.operation_failed_tips)
                } else {
                    tvSnValue.text = productBean.ProductSN
                    tvDeviceModelValue.text = productBean.ProductName
                }
            } else {
                val deviceDetailsBean = TS004Repository.getDeviceInfo()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (deviceDetailsBean?.isSuccess()!!) {
                    TLog.d("ts004-->response", "${deviceDetailsBean.data}")
                    tvSnValue.text = deviceDetailsBean.data!!.sn
                    tvDeviceModelValue.text = deviceDetailsBean.data!!.model
                } else {
                    TToast.shortToast(this@DeviceDetailsActivity, RCore.string.operation_failed_tips)
                }
            }
        }
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
        when (v) {
            clLayoutCopy -> { // Copyinfo
                val text = "${tvSn.text}:${tvSnValue.text}  ${tvDeviceModel.text}:${tvDeviceModelValue.text}"
                val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                val mClipData = ClipData.newPlainText("text", text)
                cm!!.setPrimaryClip(mClipData)
                TToast.shortToast(this@DeviceDetailsActivity, RCore.string.ts004_copy_success)
            }
        }
    }
}
