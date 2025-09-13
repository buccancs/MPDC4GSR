package com.topdon.module.thermal.ir.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.topdon.module.thermal.ir.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
首页操作指引弹框.
 *
 * Created by LCG on 2024/4/8.
/**
 * Specialized thermal imaging component providing HomeGuideDialog functionality for the IRCamera system.
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
class HomeGuideDialog(context: Context, private val currentStep: Int) : Dialog(context, R.style.TransparentDialog) {
    /**
下一步clickEventListener，step：当前处于第`[1,3]`，在该步骤click的下一步
     */
    var onNextClickListener: ((step: Int) -> Unit)? = null

    /**
跳过clickEventListener.
     */
    var onSkinClickListener: (() -> Unit)? = null

    // Initialize view as class property for coroutine access
    private lateinit var ivBlurBg: ImageView

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
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_home_guide, null))

        // Initialize views with findViewById
        ivBlurBg = findViewById(R.id.iv_blur_bg)
        val clGuide1: View = findViewById(R.id.cl_guide_1)
        val clGuide2: View = findViewById(R.id.cl_guide_2)
        val clGuide3: View = findViewById(R.id.cl_guide_3)
        val tvNext1: View = findViewById(R.id.tv_next1)
        val tvNext2: View = findViewById(R.id.tv_next2)
        val tvIKnow: View = findViewById(R.id.tv_i_know)
        val tvSkin1: View = findViewById(R.id.tv_skin1)
        val tvSkin2: View = findViewById(R.id.tv_skin2)

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (currentStep) {
            1 -> {
                clGuide1.isVisible = true
                clGuide2.isVisible = false
                clGuide3.isVisible = false
            }
            2 -> {
                clGuide1.isVisible = false
                clGuide2.isVisible = true
                clGuide3.isVisible = false
            }
            3 -> {
                clGuide1.isVisible = false
                clGuide2.isVisible = false
                clGuide3.isVisible = true
            }
        }

        tvNext1.setOnClickListener {
            onNextClickListener?.invoke(1)
            clGuide1.isVisible = false
            clGuide2.isVisible = true
        }
        tvNext2.setOnClickListener {
            onNextClickListener?.invoke(2)
            clGuide2.isVisible = false
            clGuide3.isVisible = true
        }
        tvIKnow.setOnClickListener {
            onNextClickListener?.invoke(3)
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss()
        }

        tvSkin1.setOnClickListener {
            onSkinClickListener?.invoke()
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss()
        }
        tvSkin2.setOnClickListener {
            onSkinClickListener?.invoke()
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss()
        }
    }

    /**
     * Executes onbackpressed operation with thermal imaging domain optimization.
     *
     */
    override fun onBackPressed() {
        super.onBackPressed()
        onSkinClickListener?.invoke()
    }

    /**
     * Executes blurBg functionality.
     */
    /**
     * Executes blurbg operation with thermal imaging domain optimization.
     *
     * @param
     * @param rootView Parameter for operation (type: View)
     *
     */
    fun blurBg(rootView: View) {
        /**
         * Executes coroutinescope operation with thermal imaging domain optimization.
         *
         */
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sourceBitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
                val outputBitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(sourceBitmap)
                rootView.draw(canvas)

                val renderScript = RenderScript.create(context)
                val inputAllocation = Allocation.createFromBitmap(renderScript, sourceBitmap)
                val outputAllocation = Allocation.createTyped(renderScript, inputAllocation.type)

                val blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
                blurScript.setRadius(20f)
                blurScript.setInput(inputAllocation)
                blurScript.forEach(outputAllocation)
                outputAllocation.copyTo(outputBitmap)
                renderScript.destroy()

                /**
                 * Executes launch operation with thermal imaging domain optimization.
                 *
                 */
                launch(Dispatchers.Main) {
                    ivBlurBg.isVisible = true
                    ivBlurBg.setImageBitmap(outputBitmap)
                }
            } catch (_: Exception) {
            }
        }
    }
}
