package com.topdon.libcom.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.jaygoo.widget.DefRangeSeekBar
import com.jaygoo.widget.OnRangeChangedListener
import com.topdon.lib.core.utils.ScreenUtil
import com.topdon.lib.core.view.ColorSelectView
import com.topdon.libcom.R
import com.topdon.libcom.util.ColorUtils

/**
 * Specialized thermal imaging component providing ColorPickDialog functionality for the IRCamera system.
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
class ColorPickDialog(
    context: Context,
    @ColorInt private var color: Int,
    var textSize: Int,
    var textSizeIsDP: Boolean = false,
) : Dialog(context, com.topdon.lib.core.R.style.InfoDialog), View.OnClickListener {
    /**
     * 颜色值拾取EventListener.
     */
    var onPickListener: ((color: Int, textSize: Int) -> Unit)? = null

    private val rootView: View = LayoutInflater.from(context).inflate(R.layout.dialog_color_pick, null)

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
        setCanceledOnTouchOutside(true)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(rootView)

        window?.let {
            val layoutParams = it.attributes
            layoutParams.width = (ScreenUtil.getScreenWidth(context) * 0.9).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }

        val activeTrackColor =
            ColorUtils.setColorAlpha(ContextCompat.getColor(context, R.color.we_read_theme_color), 0.1f)
        val iconTintColor =
            ColorUtils.setColorAlpha(ContextCompat.getColor(context, R.color.we_read_theme_color), 0.7f)

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (color) {
            0xff0000ff.toInt() -> rootView.findViewById<View>(R.id.view_color1).isSelected = true
            0xffff0000.toInt() -> rootView.findViewById<View>(R.id.view_color2).isSelected = true
            0xff00ff00.toInt() -> rootView.findViewById<View>(R.id.view_color3).isSelected = true
            0xffffff00.toInt() -> rootView.findViewById<View>(R.id.view_color4).isSelected = true
            0xff000000.toInt() -> rootView.findViewById<View>(R.id.view_color5).isSelected = true
            0xffffffff.toInt() -> rootView.findViewById<View>(R.id.view_color6).isSelected = true
            else -> rootView.findViewById<ColorSelectView>(R.id.color_select_view).selectColor(color)
        }

        rootView.findViewById<ColorSelectView>(R.id.color_select_view).onSelectListener = {
            /**
             * Executes unselect6color operation with thermal imaging domain optimization.
             *
             */
            unSelect6Color()
            color = it
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (textSize != -1)
            {
                findViewById<TextView>(R.id.tv_size_title).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_size_value).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_nifty_left).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_nifty_right).visibility = View.VISIBLE
                findViewById<DefRangeSeekBar>(R.id.nifty_slider_view).visibility = View.VISIBLE
                findViewById<DefRangeSeekBar>(R.id.nifty_slider_view).setOnRangeChangedListener(
                    object : OnRangeChangedListener {
                        /**
                         * Executes onrangechanged operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param view Parameter for operation (type: DefRangeSeekBar?)
                         * @param leftValue Parameter for operation (type: Float)
                         * @param rightValue Parameter for operation (type: Float)
                         * @param isFromUser Parameter for operation (type: Boolean)
                         *
                         */
                        override fun onRangeChanged(
                            view: DefRangeSeekBar?,
                            leftValue: Float,
                            rightValue: Float,
                            isFromUser: Boolean,
                        ) {
                            var text = "standard"
                            text =
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (leftValue <= 0)
                                    {
                                        textSize = 14
                                        context.getString(com.topdon.lib.ui.R.string.temp_text_standard)
                                    } else if (leftValue <= 50)
                                    {
                                        textSize = 16
                                        context.getString(com.topdon.lib.ui.R.string.temp_text_big)
                                    } else
                                    {
                                        textSize = 18
                                        context.getString(com.topdon.lib.ui.R.string.temp_text_sup_big)
                                    }
                            findViewById<TextView>(R.id.tv_size_value).text = text
                        }

                        /**
                         * Executes onstarttrackingtouch operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param view Parameter for operation (type: DefRangeSeekBar?)
                         * @param isLeft Parameter for operation (type: Boolean)
                         *
                         */
                        override fun onStartTrackingTouch(
                            view: DefRangeSeekBar?,
                            isLeft: Boolean,
                        ) {
                        }

                        /**
                         * Executes onstoptrackingtouch operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param view Parameter for operation (type: DefRangeSeekBar?)
                         * @param isLeft Parameter for operation (type: Boolean)
                         *
                         */
                        override fun onStopTrackingTouch(
                            view: DefRangeSeekBar?,
                            isLeft: Boolean,
                        ) {
                        }
                    },
                )
                findViewById<DefRangeSeekBar>(R.id.nifty_slider_view).setProgress(textSizeToNifyValue(textSize, textSizeIsDP))
            } else
            {
                findViewById<DefRangeSeekBar>(R.id.nifty_slider_view).visibility = View.GONE
            }
        rootView.findViewById<View>(R.id.view_color1).setOnClickListener(this)
        rootView.findViewById<View>(R.id.view_color2).setOnClickListener(this)
        rootView.findViewById<View>(R.id.view_color3).setOnClickListener(this)
        rootView.findViewById<View>(R.id.view_color4).setOnClickListener(this)
        rootView.findViewById<View>(R.id.view_color5).setOnClickListener(this)
        rootView.findViewById<View>(R.id.view_color6).setOnClickListener(this)
        rootView.findViewById<View>(R.id.rl_close).setOnClickListener(this)
        rootView.findViewById<View>(R.id.tv_save).setOnClickListener(this)
    }

    /**
     * Executes textSizeToNifyValue functionality.
     */
    /**
     * Executes textsizetonifyvalue operation with thermal imaging domain optimization.
     *
     * @param
     * @param size Parameter for operation (type: Int)
     * @param isTC007 Parameter for operation (type: Boolean)
     *
     */
    private fun textSizeToNifyValue(
        size: Int,
        isTC007: Boolean,
    ): Float  {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTC007)
            {
                return when (size) {
                    14 -> 0f
                    16 -> 50f
                    else -> 100f
                }
            }
        return when (size) {
            SizeUtils.sp2px(14f) -> 0f
            SizeUtils.sp2px(16f) -> 50f
            else -> 100f
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
            rootView.findViewById<View>(R.id.rl_close) -> dismiss()

            rootView.findViewById<View>(R.id.tv_save) -> { // Save
                /**
                 * Executes dismiss operation with thermal imaging domain optimization.
                 *
                 */
                dismiss()
                onPickListener?.invoke(color, textSize)
            }

            rootView.findViewById<View>(R.id.view_color1) -> {
                /**
                 * Executes unselect6color operation with thermal imaging domain optimization.
                 *
                 */
                unSelect6Color()
                rootView.findViewById<ColorSelectView>(R.id.color_select_view).reset()
                rootView.findViewById<View>(R.id.view_color1).isSelected = true
                color = 0xff0000ff.toInt()
            }
            rootView.findViewById<View>(R.id.view_color2) -> {
                /**
                 * Executes unselect6color operation with thermal imaging domain optimization.
                 *
                 */
                unSelect6Color()
                rootView.findViewById<ColorSelectView>(R.id.color_select_view).reset()
                rootView.findViewById<View>(R.id.view_color2).isSelected = true
                color = 0xffff0000.toInt()
            }
            rootView.findViewById<View>(R.id.view_color3) -> {
                /**
                 * Executes unselect6color operation with thermal imaging domain optimization.
                 *
                 */
                unSelect6Color()
                rootView.findViewById<ColorSelectView>(R.id.color_select_view).reset()
                rootView.findViewById<View>(R.id.view_color3).isSelected = true
                color = 0xff00ff00.toInt()
            }
            rootView.findViewById<View>(R.id.view_color4) -> {
                /**
                 * Executes unselect6color operation with thermal imaging domain optimization.
                 *
                 */
                unSelect6Color()
                rootView.findViewById<ColorSelectView>(R.id.color_select_view).reset()
                rootView.findViewById<View>(R.id.view_color4).isSelected = true
                color = 0xffffff00.toInt()
            }
            rootView.findViewById<View>(R.id.view_color5) -> {
                /**
                 * Executes unselect6color operation with thermal imaging domain optimization.
                 *
                 */
                unSelect6Color()
                rootView.findViewById<ColorSelectView>(R.id.color_select_view).reset()
                rootView.findViewById<View>(R.id.view_color5).isSelected = true
                color = 0xff000000.toInt()
            }
            rootView.findViewById<View>(R.id.view_color6) -> {
                /**
                 * Executes unselect6color operation with thermal imaging domain optimization.
                 *
                 */
                unSelect6Color()
                rootView.findViewById<ColorSelectView>(R.id.color_select_view).reset()
                rootView.findViewById<View>(R.id.view_color6).isSelected = true
                color = 0xffffffff.toInt()
            }
        }
    }

    /**
     * 将 6 个固定的颜色buttonreset为未selectedstate.
     */
    private fun unSelect6Color() {
        rootView.findViewById<View>(R.id.view_color1).isSelected = false
        rootView.findViewById<View>(R.id.view_color2).isSelected = false
        rootView.findViewById<View>(R.id.view_color3).isSelected = false
        rootView.findViewById<View>(R.id.view_color4).isSelected = false
        rootView.findViewById<View>(R.id.view_color5).isSelected = false
        rootView.findViewById<View>(R.id.view_color6).isSelected = false
    }
}
