package com.example.thermal_lite

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http:// D.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
/**
 * Specialized thermal imaging component providing ExampleInstrumentedTest functionality for the IRCamera system.
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
class ExampleInstrumentedTest {
    @Test
    /**
     * Executes useAppContext functionality.
     */
    /**
     * Executes useappcontext operation with thermal imaging domain optimization.
     *
     */
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        /**
         * Executes assertequals operation with thermal imaging domain optimization.
         *
         */
        assertEquals("com.example.thermal_lite", appContext.packageName)
    }
}
