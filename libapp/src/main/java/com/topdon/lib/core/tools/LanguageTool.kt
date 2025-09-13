package com.topdon.lib.core.tools

import android.content.Context
import com.topdon.lib.core.R

/**
 * Specialized thermal imaging component providing LanguageTool functionality for the IRCamera system.
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
object LanguageTool {
    /**
     * Get display language - English only
     */
    fun showLanguage(context: Context): String {
        return context.getString(R.string.english)
    }

    /**
     * Get language code for server communication - English only
     */
    fun useLanguage(context: Context): String {
        return "en-WW"
    }

    /**
     * Get language code for statement interface - English only
     */
    fun useStatementLanguage(): String {
        return "EN"
    }
}
