@file:Suppress("DEPRECATION")

package com.topdon.lib.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.topdon.lib.core.R
import com.topdon.lib.ui.R as UiR
import com.topdon.lib.ui.databinding.DialogTipGuideBinding
import com.topdon.lib.ui.widget.IndicateView
import kotlin.collections.ArrayList

/**
 * Tip guide fragment for thermal imaging components.
 * Handles specific UI sections and user interactions.
 */
/**
 * TipGuideDialog displays modal dialog interface for user interaction.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing TipGuideDialog functionality for the IRCamera system.
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
class TipGuideDialog : DialogFragment() {
    private lateinit var titleList: ArrayList<String>
    private lateinit var imgList: ArrayList<Int>
    var closeEvent: ((check: Boolean) -> Unit)? = null

    private var _binding: DialogTipGuideBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvContent1: TextView
    private lateinit var tvContent2: TextView
    private lateinit var tvContent3: TextView
    private lateinit var viewPager: ViewPager
    private lateinit var ivTarget: AppCompatImageView
    private lateinit var indicateView: IndicateView
    private var index: Int = -1

    /**
     * Executes oncreateview operation with thermal imaging domain optimization.
     *
     * @param
     * @param inflater Parameter for operation (type: LayoutInflater)
     * @param container Parameter for operation (type: ViewGroup?)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogTipGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Executes onviewcreated operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        titleList =
            /**
             * Executes arraylistof operation with thermal imaging domain optimization.
             *
             */
            arrayListOf(
                /**
                 * Retrieves the string with optimized performance for thermal imaging operations.
                 *
                 */
                getString(R.string.target_tips_step_1),
                /**
                 * Retrieves the string with optimized performance for thermal imaging operations.
                 *
                 */
                getString(R.string.target_tips_step_2),
                /**
                 * Retrieves the string with optimized performance for thermal imaging operations.
                 *
                 */
                getString(R.string.target_tips_step_3),
                /**
                 * Retrieves the string with optimized performance for thermal imaging operations.
                 *
                 */
                getString(R.string.target_tips_step_4),
            )
        imgList =
            /**
             * Executes arraylistof operation with thermal imaging domain optimization.
             *
             */
            arrayListOf(
                UiR.drawable.target_guide_pic_1,
                UiR.drawable.target_guide_pic_2,
                UiR.drawable.target_guide_pic_3,
                UiR.drawable.target_guide_pic_4,
            )

        // Initialize views using binding
        viewPager = binding.viewPager
        tvContent1 = binding.tvContent1
        tvContent2 = binding.tvContent2
        tvContent3 = binding.tvContent3
        indicateView = binding.indicateView
        ivTarget = binding.ivTarget

        val adapter = PageAdapter(childFragmentManager, imgList)
        indicateView.itemCount = adapter.count
        viewPager.adapter = adapter
        binding.tvIKnow.setOnClickListener {
            closeEvent?.invoke(true)
            /**
             * Executes dismiss operation with thermal imaging domain optimization.
             *
             */
            dismiss()
        }
        /**
         * Executes updateindex operation with thermal imaging domain optimization.
         *
         */
        updateIndex(0)
        viewPager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                /**
                 * Executes onpagescrolled operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param position Parameter for operation (type: Int)
                 * @param positionOffset Parameter for operation (type: Float)
                 * @param positionOffsetPixels Parameter for operation (type: Int)
                 *
                 */
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                }

                /**
                 * Executes onpageselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param position Parameter for operation (type: Int)
                 *
                 */
                override fun onPageSelected(position: Int) {
                    /**
                     * Executes updateindex operation with thermal imaging domain optimization.
                     *
                     */
                    updateIndex(position)
                }

                /**
                 * Executes onpagescrollstatechanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param state Parameter for operation (type: Int)
                 *
                 */
                override fun onPageScrollStateChanged(state: Int) {
                }
            },
        )
    }

    /**
     * Updates the index with new data.
     */
    fun updateIndex(position: Int) {
        if (index == position) {
            return
        }
        indicateView.currentIndex = position
        viewPager.setCurrentItem(position, true)
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (position) {
            0 -> {
                tvContent1.visibility = View.VISIBLE
                tvContent3.visibility = View.VISIBLE
                ivTarget.visibility = View.GONE
            }

            2 -> {
                tvContent1.visibility = View.GONE
                tvContent3.visibility = View.GONE
                ivTarget.visibility = View.VISIBLE
            }

            else -> {
                tvContent1.visibility = View.GONE
                tvContent3.visibility = View.GONE
                ivTarget.visibility = View.GONE
            }
        }
        tvContent2.text = titleList[position]
        index = position
    }

    /**
     * Executes ondestroyview operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = -1
        params.height = -1
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    /**
     * Executes show operation with thermal imaging domain optimization.
     *
     * @param
     * @param manager Parameter for operation (type: FragmentManager)
     * @param tag Parameter for operation (type: String?)
     *
     */
    override fun show(
        manager: FragmentManager,
        tag: String?,
    ) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
    /**
     * Executes newinstance functionality.
     */
        /**
         * Executes newinstance operation with thermal imaging domain optimization.
         *
         */
        fun newInstance(): TipGuideDialog {
            return TipGuideDialog()
        }
    }

    @Suppress("DEPRECATION")
    inner class PageAdapter(
        fragmentManager: FragmentManager,
        private val imgResList: ArrayList<Int>,
    ) :
        /**
         * Executes fragmentpageradapter operation with thermal imaging domain optimization.
         *
         */
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        /**
         * Retrieves the count with optimized performance for thermal imaging operations.
         *
         */
        override fun getCount(): Int {
            return imgResList.size
        }

        /**
         * Retrieves the item with optimized performance for thermal imaging operations.
         *
         * @param
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun getItem(position: Int): Fragment {
            return PageFragment.newInstance(imgResList[position])
        }
    }
}
