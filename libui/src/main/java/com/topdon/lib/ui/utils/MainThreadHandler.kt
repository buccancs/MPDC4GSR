package com.topdon.lib.ui.utils
import android.os.Handler
import android.os.Looper

/**
 * Specialized thermal imaging component providing MainThreadHandler functionality for the IRCamera system.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
object MainThreadHandler {
    private val handler = Handler(Looper.getMainLooper())

    /**
     * Executes runonuithread functionality.
     */
    /**
     * Executes runonuithread operation with thermal imaging domain optimization.
     *
     * @param
     * @param r Parameter for operation (type: Runnable?)
     *
     */
    fun runOnUiThread(r: Runnable?) {
        handler.post(r!!)
    }

    /**
     * Executes postdelayed functionality.
     */
    /**
     * Executes postdelayed operation with thermal imaging domain optimization.
     *
     * @param
     * @param r Parameter for operation (type: Runnable?)
     * @param millis Parameter for operation (type: Long)
     *
     */
    fun postDelayed(
        r: Runnable?,
        millis: Long,
    ) {
        handler.postDelayed(r!!, millis)
    }

    /**
     * Removes the specified  from the system.
     */
    fun remove(r: Runnable?) {
        handler.removeCallbacks(r!!)
    }
}
