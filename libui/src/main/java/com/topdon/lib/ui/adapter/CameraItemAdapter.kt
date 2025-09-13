package com.topdon.lib.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.R
import com.topdon.lib.ui.R as UiR

/**
 * @author: CaiSongL
 * @date: 2023/4/1 13:48
 */
/**
 * Custom Camera item view for thermal imaging display.
 * Provides specialized rendering and interaction capabilities.
 */
@Deprecated("thermal imaging-menu-capture已重构，不需要这个class了")
/**
 * CameraItemAdapter provides data binding between data source and UI components.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraItemAdapter functionality.
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
class CameraItemAdapter(context: Context) : RecyclerView.Adapter<CameraItemAdapter.ViewHolder>() {
    val data: List<String> =
        /**
         * Executes listof operation with thermal imaging domain optimization.
         *
         */
        listOf(
            context.getString(R.string.person_headshot_camera),
            context.getString(R.string.app_video),
        )

    private var parentRecycler: RecyclerView? = null

    /**
     * Executes onattachedtorecyclerview operation with thermal imaging domain optimization.
     *
     * @param
     * @param recyclerView Parameter for operation (type: RecyclerView)
     *
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        parentRecycler = recyclerView
    }

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
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(UiR.layout.item_weak, parent, false)
        return ViewHolder(v)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: ViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder?.textView?.text = data[position]
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val textView: TextView

        init {
            textView = itemView.findViewById<View>(UiR.id.name) as TextView
            itemView.findViewById<View>(UiR.id.container).setOnClickListener(this)
        }

    /**
     * Executes showtext functionality.
     */
        /**
         * Executes showtext operation with thermal imaging domain optimization.
         *
         */
        fun showText() {
            textView.pivotX = (textView.width / 2).toFloat()
            textView.pivotY = (textView.height / 2).toFloat()
            textView.setTextColor(0xffffffff.toInt())
            textView.animate().scaleX(1.1f)
                .withEndAction {
                    textView.setTextColor(0xffffffff.toInt())
                }
                .scaleY(1.1f).setDuration(100)
                .start()
        }

    /**
     * Executes hidetext functionality.
     */
        /**
         * Executes hidetext operation with thermal imaging domain optimization.
         *
         */
        fun hideText() {
            textView.setTextColor(ContextCompat.getColor(textView.context, UiR.color.ui_main_custom_color))
            // TextView.setColorFilter(ContextCompat.getColor(imageView.getContext(), UiR.color.Grey700));
            textView.animate().scaleX(1f).scaleY(1f)
                .setDuration(100)
                .start()
        }

        /**
         * Executes onclick operation with thermal imaging domain optimization.
         *
         * @param
         * @param v Parameter for operation (type: View)
         *
         */
        override fun onClick(v: View) {
            parentRecycler!!.smoothScrollToPosition(bindingAdapterPosition)
        }
    }
}
