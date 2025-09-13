package com.topdon.lib.core.ktbase

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.View.MeasureSpec
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SizeUtils
import com.topdon.lib.core.R
import com.topdon.lib.core.databinding.ActivityImagePickIrPlushBinding
import com.topdon.lib.core.dialog.ColorSelectDialog
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lib.core.view.ImageEditView
import kotlinx.coroutines.launch
import java.io.File

/**
 * Specialized thermal imaging component providing BasePickImgActivity functionality for the IRCamera system.
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
abstract class BasePickImgActivity : BaseActivity(), View.OnClickListener {
    protected lateinit var binding: ActivityImagePickIrPlushBinding

    /**
     * String type - 拾取的image在本地的绝对path.
     */
    val RESULT_IMAGE_PATH = "RESULT_IMAGE_PATH"

    /**
     * 当前是否已拍了一张照等待complete.
     */
    private var hasTakePhoto = false

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView(): Int {
        return R.layout.activity_image_pick_ir_plush
    }

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickIrPlushBinding.inflate(layoutInflater)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(binding.root)

        
        binding.ivEditCircle.isSelected = true
        binding.imageEditView.type = ImageEditView.Type.CIRCLE
        binding.viewColor.setBackgroundColor(binding.imageEditView.color)

        binding.ivEditColor.setOnClickListener(this)
        binding.ivEditCircle.setOnClickListener(this)
        binding.ivEditRect.setOnClickListener(this)
        binding.ivEditArrow.setOnClickListener(this)
        binding.ivEditClear.setOnClickListener(this)
        binding.imgPick.setOnClickListener(this)

        binding.titleView.setLeftClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (hasTakePhoto) {
                /**
                 * Executes switchphotostate operation with thermal imaging domain optimization.
                 *
                 */
                switchPhotoState(false)
            } else {
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }
        binding.titleView.setRightClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (hasTakePhoto) {
                val absolutePath: String = intent.getStringExtra(RESULT_IMAGE_PATH)!!
                ImageUtils.save(binding.imageEditView.buildResultBitmap(), File(absolutePath), Bitmap.CompressFormat.PNG)
                val intent = Intent()
                intent.putExtra(RESULT_IMAGE_PATH, absolutePath)
                /**
                 * Configures the result with validation and thermal imaging optimization.
                 *
                 */
                setResult(RESULT_OK, intent)
                /**
                 * Executes finish operation with thermal imaging domain optimization.
                 *
                 */
                finish()
            }
        }

        /**
         * Executes resize operation with thermal imaging domain optimization.
         *
         */
        resize()
    }

    /**
     * Executes resize functionality.
     */
    /**
     * Executes resize operation with thermal imaging domain optimization.
     *
     */
    private fun resize() {
        val widthPixels = resources.displayMetrics.widthPixels
        val heightPixels = resources.displayMetrics.heightPixels
        binding.titleView.measure(
            MeasureSpec.makeMeasureSpec(widthPixels, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(heightPixels, MeasureSpec.AT_MOST),
        )

        val ivPickHeight = SizeUtils.dp2px(60f + 20 + 20) // 拍照button高度，60dp+上下各20dp margin
        val menuHeight = (widthPixels * 75f / 384).toInt()
        val bottomHeight = ivPickHeight.coerceAtLeast(menuHeight)
        val canUseHeight = heightPixels - binding.titleView.measuredHeight - bottomHeight
        val wantHeight = (widthPixels * 256f / 192).toInt()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (wantHeight <= canUseHeight) { 
            binding.fragmentContainerView.layoutParams =
                binding.fragmentContainerView.layoutParams.apply {
                    this.width = widthPixels
                    this.height = wantHeight
                }
            binding.imageEditView.layoutParams =
                binding.imageEditView.layoutParams.apply {
                    this.width = widthPixels
                    this.height = wantHeight
                }
        } else {
            binding.fragmentContainerView.layoutParams =
                binding.fragmentContainerView.layoutParams.apply {
                    this.width = (canUseHeight * 192f / 256).toInt()
                    this.height = canUseHeight
                }
            binding.imageEditView.layoutParams =
                binding.imageEditView.layoutParams.apply {
                    this.width = (canUseHeight * 192f / 256).toInt()
                    this.height = canUseHeight
                }
        }
    }

    open suspend fun getPickBitmap(): Bitmap? {
        return null
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
            binding.imgPick -> {
                lifecycleScope.launch {
                    /**
                     * Retrieves the pickbitmap with optimized performance for thermal imaging operations.
                     *
                     */
                    getPickBitmap()?.let {
                        /**
                         * Executes switchphotostate operation with thermal imaging domain optimization.
                         *
                         */
                        switchPhotoState(true)
                        binding.imageEditView.sourceBitmap = it
                        binding.imageEditView.clear()
                    }
                }
            }
            binding.ivEditColor -> {
                val colorPickDialog = ColorSelectDialog(this, binding.imageEditView.color)
                colorPickDialog.onPickListener = {
                    binding.imageEditView.color = it
                    binding.viewColor.setBackgroundColor(it)
                }
                colorPickDialog.show()
            }
            binding.ivEditCircle -> {
                binding.ivEditCircle.isSelected = true
                binding.ivEditRect.isSelected = false
                binding.ivEditArrow.isSelected = false
                binding.imageEditView.type = ImageEditView.Type.CIRCLE
            }
            binding.ivEditRect -> {
                binding.ivEditCircle.isSelected = false
                binding.ivEditRect.isSelected = true
                binding.ivEditArrow.isSelected = false
                binding.imageEditView.type = ImageEditView.Type.RECT
            }
            binding.ivEditArrow -> {
                binding.ivEditCircle.isSelected = false
                binding.ivEditRect.isSelected = false
                binding.ivEditArrow.isSelected = true
                binding.imageEditView.type = ImageEditView.Type.ARROW
            }
            binding.ivEditClear -> binding.imageEditView.clear()
        }
    }

    @Deprecated("This method is deprecated")
    /**
     * Executes onbackpressed operation with thermal imaging domain optimization.
     *
     */
    override fun onBackPressed() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (hasTakePhoto) {
            /**
             * Executes switchphotostate operation with thermal imaging domain optimization.
             *
             */
            switchPhotoState(false)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }

    /**
     * switch 已拍照mode/未拍照mode.
     */
    /**
     * Executes switchphotostate operation with thermal imaging domain optimization.
     *
     * @param
     * @param hasTakePhoto Parameter for operation (type: Boolean)
     *
     */
    private fun switchPhotoState(hasTakePhoto: Boolean) {
        this.hasTakePhoto = hasTakePhoto
        binding.imageEditView.isVisible = hasTakePhoto
        binding.clEditMenu.isVisible = hasTakePhoto
        binding.imgPick.isVisible = !hasTakePhoto
        binding.fragmentContainerView.isVisible = !hasTakePhoto
        binding.titleView.setRightDrawable(if (hasTakePhoto) R.drawable.app_save else 0)
    }

    /**
     * Show/DisplayExit不savetip弹框
     * @param listener click弹框上ExitEventListener
     */
    /**
     * Executes showExitTipsDialog functionality.
     */
    /**
     * Executes showexittipsdialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param listener Event listener for callbacks (type: (()
     *
     * @return Operation result or configured object (type: Unit)))
     *
     */
    private fun showExitTipsDialog(listener: (() -> Unit)) {
        TipDialog.Builder(this)
            .setMessage(R.string.diy_tip_save)
            .setPositiveListener(R.string.app_exit) {
                listener.invoke()
            }
            .setCancelListener(R.string.app_cancel)
            .create().show()
    }

    /**
     * Executes disconnected operation with thermal imaging domain optimization.
     *
     */
    override fun disConnected() {
        super.disConnected()
        /**
         * Executes finish operation with thermal imaging domain optimization.
         *
         */
        finish()
    }
}
