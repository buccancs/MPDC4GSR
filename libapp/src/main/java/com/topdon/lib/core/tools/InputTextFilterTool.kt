package com.topdon.lib.core.tools

import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.EditText
import java.util.regex.Pattern

/**
 * Specialized thermal imaging component providing InputTextFilterTool functionality for the IRCamera system.
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
class InputTextFilterTool {
    /**
     * settingseditTextFilter器
     *
     * @param editText
     */
    /**
     * Sets edittextfilter configuration.
     */
    fun setEditTextFilter(editText: EditText) {
        val oldFilters = editText.filters
        val oldFiltersLength = oldFilters.size
        val newFilters = arrayOfNulls<InputFilter>(oldFiltersLength + 1)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (oldFiltersLength > 0) {
            System.arraycopy(oldFilters, 0, newFilters, 0, oldFiltersLength)
        }
        // Add新的Filter规则
        newFilters[oldFiltersLength] = mInputFilter
        editText.filters = newFilters
    }

    // Filter表情
    private var mInputFilter: InputFilter =
        object : InputFilter {
            //        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            var emoji =
                Pattern.compile(
                    "[^\u0020-\u007E\u00A0-\u00BE\u2E80-\uA4CF\uF900-\uFAFF\uFE30-\uFE4F\uFF00-\uFFEF\u0080-\u009F\u2000-\u201f\\r\\n]",
                    Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE,
                )

            /**
             * Executes filter operation with thermal imaging domain optimization.
             *
             * @param
             * @param source Parameter for operation (type: CharSequence)
             * @param start Parameter for operation (type: Int)
             * @param end Parameter for operation (type: Int)
             * @param dest Parameter for operation (type: Spanned)
             * @param dstart Parameter for operation (type: Int)
             * @param dend Parameter for operation (type: Int)
             *
             */
            override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned,
                dstart: Int,
                dend: Int,
            ): CharSequence? {
                val emojiMatcher = emoji.matcher(source)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (emojiMatcher.find()) {
                    Log.w("123", "不支持输入表情")
                    return ""
                }
                return null
            }
        }
}
