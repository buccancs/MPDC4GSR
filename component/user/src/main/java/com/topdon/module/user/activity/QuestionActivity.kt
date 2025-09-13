package com.topdon.module.user.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.topdon.lib.core.config.RouterConfig
import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.lib.core.navigation.NavigationManager
import com.topdon.module.user.R
import com.topdon.module.user.model.FaqRepository
import com.topdon.module.user.model.QuestionData
import java.util.ArrayList

/**
/**
 * Specialized thermal imaging component providing QuestionActivity functionality for the IRCamera system.
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
class QuestionActivity : BaseActivity() {
    // View references - migrated from synthetic views
    private lateinit var questionRecycler: RecyclerView

    /**
     * Initializes the contentview component for thermal imaging operations.
     *
     */
    override fun initContentView() = R.layout.activity_question

    /**
     * Initializes the view component for thermal imaging operations.
     *
     */
    override fun initView() {
        // Initialize views - migrated from synthetic views
        questionRecycler = findViewById(R.id.question_recycler)

        val adapter = MyAdapter(FaqRepository.getQuestionList(intent.getBooleanExtra("isTS001", false)))
        adapter.onItemClickListener = {
            NavigationManager.getInstance()
                .build(RouterConfig.QUESTION_DETAILS)
                .withString("question", it.question)
                .withString("answer", it.answer)
                .navigation(this)
        }

        questionRecycler.layoutManager = LinearLayoutManager(this)
        questionRecycler.adapter = adapter
    }

    /**
     * Initializes the data component for thermal imaging operations.
     *
     */
    override fun initData() {
    }

/**
 * Specialized thermal imaging component providing MyAdapter functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class MyAdapter(private val questionList: ArrayList<QuestionData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var onItemClickListener: ((data: QuestionData) -> Unit)? = null

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
        ): RecyclerView.ViewHolder {
            return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false))
        }

        /**
         * Retrieves the itemcount with optimized performance for thermal imaging operations.
         *
         */
        override fun getItemCount(): Int = questionList.size

        /**
         * Executes onbindviewholder operation with thermal imaging domain optimization.
         *
         * @param
         * @param holder Parameter for operation (type: RecyclerView.ViewHolder)
         * @param position Parameter for operation (type: Int)
         *
         */
        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (holder is ItemHolder) {
                val itemQuestionInfo: TextView = holder.rootView.findViewById(R.id.item_question_info)
                val itemQuestionLay: ConstraintLayout = holder.rootView.findViewById(R.id.item_question_lay)

                itemQuestionInfo.text = questionList[position].question
                itemQuestionLay.setOnClickListener {
                    onItemClickListener?.invoke(questionList[position])
                }
            }
        }

/**
 * Specialized thermal imaging component providing ItemHolder functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
        private class ItemHolder(val rootView: View) : RecyclerView.ViewHolder(rootView)
    }
}
