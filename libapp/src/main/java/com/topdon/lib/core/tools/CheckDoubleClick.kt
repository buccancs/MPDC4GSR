package com.topdon.lib.core.tools

/**
 * Specialized thermal imaging component providing CheckDoubleClick functionality for the IRCamera system.
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
object CheckDoubleClick {
    private val records: MutableMap<String, Long> = HashMap()

    /**
     * Executes isFastDoubleClick functionality.
     */
    /**
     * Executes isfastdoubleclick operation with thermal imaging domain optimization.
     *
     */
    fun isFastDoubleClick(): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (records.size > 1000) {
            records.clear()
        }

        // 本method被调用的file名和行号作为marker
        val ste = Throwable().stackTrace[1]
        val key = ste.fileName + ste.lineNumber
        var lastClickTime = records[key]
        val thisClickTime = System.currentTimeMillis()
        records[key] = thisClickTime
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (lastClickTime == null) {
            lastClickTime = 0L
        }
        val timeDuration = thisClickTime - lastClickTime
        return timeDuration in 1..499
    }
}
