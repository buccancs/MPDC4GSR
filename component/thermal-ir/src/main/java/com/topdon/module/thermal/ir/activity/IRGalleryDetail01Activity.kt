package com.topdon.module.thermal.ir.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.elvishew.xlog.XLog
import com.topdon.lib.core.bean.GalleryBean
import com.topdon.lib.core.bean.event.GalleryDelEvent
import com.topdon.lib.core.config.ExtraKeyConfig
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.lib.core.tools.FileTools
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.tools.ToastTools
import com.topdon.lib.core.utils.ByteUtils.bytesToInt
import com.topdon.lib.core.utils.Constants.IS_REPORT_FIRST
import com.topdon.lib.core.view.TitleView
import com.topdon.lib.ui.dialog.ProgressDialog
import com.topdon.libcom.ExcelUtil
import com.topdon.module.thermal.ir.R
import com.topdon.module.thermal.ir.event.ImageGalleryEvent
import com.topdon.module.thermal.ir.fragment.GalleryFragment
import com.topdon.module.thermal.ir.frame.FrameTool
import com.topdon.module.thermal.ir.viewmodel.IRGalleryEditViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import com.topdon.lib.core.R as LibR

/**
插件式device、TC007 image详情
 */
// Legacy ARouter route annotation - now using NavigationManager
/**
/**
 * Specialized thermal imaging component providing IRGalleryDetail01Activity functionality for the IRCamera system.
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
class IRGalleryDetail01Activity : BaseActivity(), View.OnClickListener {
    /**
从上一interface传递过来的，当前是否为 TC007 devicetype.
true-TC007 false-其他插件式device
     */
    private var isTC007 = false

    /**
当前展示image在列表中的 position
     */
    private var position = 0

    /**
从上一interface传递过来的，当前展示的image列表.
     */
    private lateinit var dataList: ArrayList<GalleryBean>

    private var irPath: String? = null
    private val irViewModel: IRGalleryEditViewModel by viewModels()

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_ir_gallery_detail_01

    private val frameTool by lazy { FrameTool() }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        position = intent.getIntExtra("position", 0)
        dataList = intent.getParcelableArrayListExtra("list")!!
        isTC007 = intent.getBooleanExtra(ExtraKeyConfig.IS_TC007, false)

        val titleView = findViewById<TitleView>(R.id.title_view)
        titleView.setTitleText("${position + 1}/${dataList.size}")
        titleView.setRightClickListener { actionInfo() }
        titleView.setRight2ClickListener { actionShare() }
        titleView.setRight3ClickListener { deleteImage() }

        /**
         * Initializes the viewpager component for thermal imaging operations.
         *
         */
        initViewPager()

        findViewById<LinearLayout>(R.id.ll_ir_edit_2D)?.setOnClickListener(this)
        findViewById<LinearLayout>(R.id.ll_ir_edit_3D)?.setOnClickListener(this)
        findViewById<LinearLayout>(R.id.ll_ir_report)?.setOnClickListener(this)
        findViewById<LinearLayout>(R.id.ll_ir_ex)?.setOnClickListener(this)

        irViewModel.resultLiveData.observe(this) {
            lifecycleScope.launch {
                val filePath: String?
                /**
                 * Executes withcontext operation with thermal imaging domain optimization.
                 *
                 */
                withContext(Dispatchers.IO) {
                    frameTool.read(it.frame)
                    filePath =
                        ExcelUtil.exportExcel(
                            excelName,
                            192,
                            256,
                            frameTool.getRotate90Temp(frameTool.temperatureBytes),
                        ) { current, total ->
                            lifecycleScope.launch(Dispatchers.Main) {
                                progressDialog?.max = total
                                progressDialog?.progress = current
                            }
                        }
                }
                progressDialog?.dismiss()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (filePath.isNullOrEmpty()) {
                    ToastTools.showShort(LibR.string.liveData_save_error)
                } else {
                    val uri = FileTools.getUri(File(filePath))
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    shareIntent.type = "application/xlsx"
                    /**
                     * Executes startactivity operation with thermal imaging domain optimization.
                     *
                     */
                    startActivity(Intent.createChooser(shareIntent, getString(LibR.string.battery_share)))
                }
            }
        }
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    @SuppressLint("SetTextI18n")
    /**
     * Initializes viewpager component.
     */
    private fun initViewPager() {
        val irGalleryViewpager = findViewById<ViewPager2>(R.id.ir_gallery_viewpager)
        irGalleryViewpager.adapter = GalleryViewPagerAdapter(this)
        irGalleryViewpager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                /**
                 * Executes onpageselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param position Parameter for operation (type: Int)
                 *
                 */
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    this@IRGalleryDetail01Activity.position = position
                    findViewById<TitleView>(R.id.title_view).setTitleText("${position + 1}/${dataList.size}")

                    irPath = "${FileConfig.lineIrGalleryDir}/${dataList[position].name.substringBeforeLast(".")}.ir"
                    val hasIrData = File(irPath!!).exists()
                    findViewById<LinearLayout>(R.id.ll_ir_edit_3D)?.isVisible = hasIrData
                    findViewById<LinearLayout>(R.id.ll_ir_report)?.isVisible = hasIrData
                    findViewById<LinearLayout>(R.id.ll_ir_edit_2D)?.isVisible = hasIrData
                    findViewById<LinearLayout>(R.id.ll_ir_ex)?.isVisible = hasIrData
                }
            },
        )
        irGalleryViewpager?.setCurrentItem(position, false)
    }

    /**
     * Executes actionInfo functionality.
     */
    /**
     * Executes actioninfo operation with thermal imaging domain optimization.
     *
     */
    private fun actionInfo() {
        try {
            val data = dataList[position]
            val exif = ExifInterface(data.path)
            val width = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
            val length = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
            val whStr = "${width}x$length"
            val sizeStr = FileTools.getFileSize(data.path)

            val str = StringBuilder()
            str.append(getString(LibR.string.detail_date)).append("\n")
            str.append(TimeTool.showDateType(data.timeMillis)).append("\n\n")
            str.append(getString(LibR.string.detail_info)).append("\n")
            str.append("${getString(LibR.string.detail_size)}: ").append(whStr).append("\n")
            str.append("${getString(LibR.string.detail_len)}: ").append(sizeStr).append("\n")
            str.append("${getString(LibR.string.detail_path)}: ").append(data.path).append("\n")
            TipDialog.Builder(this).setMessage(str.toString()).setCanceled(true).create().show()
        } catch (e: Exception) {
            ToastTools.showShort(LibR.string.status_error_load_fail)
        }
    }

    /**
     * Executes actionShare functionality.
     */
    /**
     * Executes actionshare operation with thermal imaging domain optimization.
     *
     */
    private fun actionShare() {
        val data = dataList[position]
        val uri = FileTools.getUri(File(data.path))
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/jpeg"
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         */
        startActivity(Intent.createChooser(shareIntent, getString(LibR.string.battery_share)))
    }

    /**
     * Executes deleteImage functionality.
     */
    /**
     * Executes deleteimage operation with thermal imaging domain optimization.
     *
     */
    private fun deleteImage() {
        TipDialog.Builder(this)
            .setMessage(getString(LibR.string.tip_delete))
            .setPositiveListener(LibR.string.app_confirm) {
                val data = dataList[position]
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dataList.size == 1) {
                    /**
                     * Executes file operation with thermal imaging domain optimization.
                     *
                     */
                    File(data.path).delete()
                    /**
                     * Executes finish operation with thermal imaging domain optimization.
                     *
                     */
                    finish()
                } else {
                    /**
                     * Executes file operation with thermal imaging domain optimization.
                     *
                     */
                    File(data.path).delete()
                    dataList.removeAt(position)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (position >= dataList.size) {
                        position = dataList.size - 1
                    }
                    /**
                     * Initializes the viewpager component for thermal imaging operations.
                     *
                     */
                    initViewPager()
                }
                EventBus.getDefault().post(GalleryDelEvent())
            }
            .setCancelListener(LibR.string.app_cancel)
            .create()
            .show()
    }

    /**
export为 excel 时的进度条弹窗.
     */
    private var progressDialog: ProgressDialog? = null
    private var excelName: String = ""

    /**
     * Executes actionExcel functionality.
     */
    /**
     * Executes actionexcel operation with thermal imaging domain optimization.
     *
     */
    private fun actionExcel() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        progressDialog?.show()

        excelName = dataList[position].name.substringBeforeLast(".")
        val irPath = "${FileConfig.lineIrGalleryDir}/$excelName.ir"
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!File(irPath).exists()) {
            ToastTools.showShort(getString(LibR.string.album_report_on_edit))
            progressDialog?.dismiss()
            return
        }
        irViewModel.initData(irPath)
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
            findViewById<LinearLayout>(R.id.ll_ir_edit_2D) -> {
2d编辑
                /**
                 * Executes actioneditorreport operation with thermal imaging domain optimization.
                 *
                 */
                actionEditOrReport(false)
            }

            findViewById<LinearLayout>(R.id.ll_ir_edit_3D) -> {
跳转到3D
                val data = dataList[position]
                val fileName = data.name.substringBeforeLast(".")
                val irPath = "${FileConfig.lineIrGalleryDir}/$fileName.ir"
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!File(irPath).exists()) {
                    ToastTools.showShort(LibR.string.album_report_on_edit)
                    return
                }
                var tempHigh = 0f
                var tempLow = 0f
                lifecycleScope.launch {
// ShowLoading()
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.IO) {
                        val file = File(irPath)
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (!file.exists()) {
                            XLog.w("IRfile不存在: ${file.absolutePath}")
                            return@withContext
                        }
                        XLog.w("IRfile: ${file.absolutePath}")
                        val bytes = file.readBytes()
                        val headLenBytes = ByteArray(2)
                        System.arraycopy(bytes, 0, headLenBytes, 0, 2)
                        val headLen = headLenBytes.bytesToInt()
                        val headDataBytes = ByteArray(headLen)
                        val frameDataBytes = ByteArray(bytes.size - headLen)
                        System.arraycopy(bytes, 0, headDataBytes, 0, headDataBytes.size)
                        System.arraycopy(bytes, headLen, frameDataBytes, 0, frameDataBytes.size)
                        frameTool.read(frameDataBytes)
                        tempHigh = frameTool.getSrcTemp().maxTemperature
                        tempLow = frameTool.getSrcTemp().minTemperature
                    }
// DismissLoading()
                    NavigationManager.getInstance().build(RouterConfig.IR_GALLERY_3D).withString(ExtraKeyConfig.IR_PATH, irPath)
                        .withFloat(ExtraKeyConfig.TEMP_HIGH, tempHigh).withFloat(ExtraKeyConfig.TEMP_LOW, tempLow)
                        .navigation(this@IRGalleryDetail01Activity)
                }
            }

            findViewById<LinearLayout>(R.id.ll_ir_report) -> {
report
                /**
                 * Executes actioneditorreport operation with thermal imaging domain optimization.
                 *
                 */
                actionEditOrReport(true)
            }

            findViewById<LinearLayout>(R.id.ll_ir_ex) -> {
                TipDialog.Builder(this).setMessage(LibR.string.tip_album_temp_exportfile).setPositiveListener(LibR.string.app_confirm) {
                    /**
                     * Executes actionexcel operation with thermal imaging domain optimization.
                     *
                     */
                    actionExcel()
                }.setCancelListener(LibR.string.app_cancel) {}.setCanceled(true).create().show()
            }
        }
    }

    /**
     * Executes actionEditOrReport functionality.
     */
    /**
     * Executes actioneditorreport operation with thermal imaging domain optimization.
     *
     * @param
     * @param isReport Parameter for operation (type: Boolean)
     *
     */
    private fun actionEditOrReport(isReport: Boolean) {
        val data = dataList[position]
        val fileName = data.name.substringBeforeLast(".")
        val irPath = "${FileConfig.lineIrGalleryDir}/$fileName.ir"
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!File(irPath).exists()) {
            ToastTools.showShort(LibR.string.album_report_on_edit)
            return
        }
        NavigationManager.getInstance().build(RouterConfig.IR_GALLERY_EDIT)
            .withBoolean(ExtraKeyConfig.IS_TC007, isTC007)
            .withBoolean(ExtraKeyConfig.IS_PICK_REPORT_IMG, isReport)
            .withBoolean(IS_REPORT_FIRST, true)
            .withString(ExtraKeyConfig.FILE_ABSOLUTE_PATH, irPath)
            .navigation(this)
    }

    inner class GalleryViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount(): Int {
            return dataList.size
        }

        /**
         * Executes createfragment operation with thermal imaging domain optimization.
         *
         * @param
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun createFragment(position: Int): Fragment {
            val fragment = GalleryFragment()
            val bundle = Bundle()
            bundle.putString("path", dataList[position].path)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    /**
     * Executes onSaveFinishBean functionality.
     */
    /**
     * Executes onsavefinishbean operation with thermal imaging domain optimization.
     *
     * @param
     * @param imageGalleryEvent Parameter for operation (type: ImageGalleryEvent)
     *
     */
    fun onSaveFinishBean(imageGalleryEvent: ImageGalleryEvent) {
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }
}
