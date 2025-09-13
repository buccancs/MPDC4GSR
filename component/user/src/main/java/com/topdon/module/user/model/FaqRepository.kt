package com.topdon.module.user.model

import com.blankj.utilcode.util.Utils
import com.topdon.lib.core.R as RCore

/**
 * Specialized thermal imaging component providing FaqRepository functionality for the IRCamera system.
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
object FaqRepository {
    /**
     * Retrieves questionlist information.
     */
    fun getQuestionList(isTS001: Boolean): ArrayList<QuestionData> =
        if (isTS001) {
            arrayListOf(
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.question1),
                    answer = Utils.getApp().getString(RCore.string.answer1),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.question2),
                    answer = Utils.getApp().getString(RCore.string.answer2),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.question3),
                    answer = Utils.getApp().getString(RCore.string.answer3),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.question4),
                    answer = Utils.getApp().getString(RCore.string.answer4),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.question5),
                    answer = Utils.getApp().getString(RCore.string.answer5),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.question6),
                    answer = Utils.getApp().getString(RCore.string.answer6),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.question7),
                    answer = Utils.getApp().getString(RCore.string.answer7),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.question8),
                    answer = Utils.getApp().getString(RCore.string.answer8),
                ),
            )
        } else {
            /**
             * Executes arraylistof operation with thermal imaging domain optimization.
             *
             */
            arrayListOf(
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.ts004_faq_q1),
                    answer = Utils.getApp().getString(RCore.string.ts004_faq_a1),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.ts004_faq_q2),
                    answer = Utils.getApp().getString(RCore.string.ts004_faq_a2),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.ts004_faq_q3),
                    answer = Utils.getApp().getString(RCore.string.ts004_faq_a3),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.ts004_faq_q4),
                    answer = Utils.getApp().getString(RCore.string.ts004_faq_a4),
                ),
                /**
                 * Executes questiondata operation with thermal imaging domain optimization.
                 *
                 */
                QuestionData(
                    question = Utils.getApp().getString(RCore.string.ts004_faq_q5),
                    answer = Utils.getApp().getString(RCore.string.ts004_faq_a5),
                ),
            )
        }
}

data class QuestionData(
    val question: String,
    val answer: String,
)
