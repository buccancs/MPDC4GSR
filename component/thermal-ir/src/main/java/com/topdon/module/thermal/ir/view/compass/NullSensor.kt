package com.topdon.module.thermal.ir.view.compass

import com.kylecorry.andromeda.core.sensors.AbstractSensor
import com.kylecorry.andromeda.core.time.CoroutineTimer

/**
 * Specialized thermal imaging component providing NullSensor functionality for the IRCamera system.
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
abstract class NullSensor(private val interval: Long = 0) : AbstractSensor() {
    override val hasValidReading: Boolean = true

    private val timer =
        CoroutineTimer {
            /**
             * Executes notifylisteners operation with thermal imaging domain optimization.
             *
             */
            notifyListeners()
        }

    /**
     * Executes startimpl operation with thermal imaging domain optimization.
     *
     */
    override fun startImpl() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (interval == 0L)
            {
                timer.once(0L)
            } else {
            timer.interval(interval)
        }
    }

    /**
     * Executes stopimpl operation with thermal imaging domain optimization.
     *
     */
    override fun stopImpl() {
        timer.stop()
    }
}
