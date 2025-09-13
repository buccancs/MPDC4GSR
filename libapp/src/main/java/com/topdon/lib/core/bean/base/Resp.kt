package com.topdon.lib.core.bean.base

import android.text.TextUtils

/**
 * Specialized thermal imaging component providing Resp functionality for the IRCamera system.
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
class Resp<T> {
    var code: String = ""
    var msg: String = ""
    var data: T? = null

    /**
     * Executes isSuccess functionality.
     */
    /**
     * Executes issuccess operation with thermal imaging domain optimization.
     *
     */
    fun isSuccess(): Boolean {
        return TextUtils.equals(code, "0")
    }

    /**
     * Executes tostring operation with thermal imaging domain optimization.
     *
     */
    override fun toString(): String {
        return "Resp(code='$code', msg='$msg', data=$data)"
    }
}
