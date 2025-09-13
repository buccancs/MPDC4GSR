package com.topdon.gsr.util

import org.junit.Assert.*
import org.junit.Test

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for TimeUtilTest operations.
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
class TimeUtilTest {
    @Test
    /**
     * Executes testPcTimeOffset functionality.
     */
    /**
     * Executes testpctimeoffset operation with thermal imaging domain optimization.
     *
     */
    fun testPcTimeOffset() {
        // Initialize timing system first to ensure proper state
        TimeUtil.initializeGroundTruthTiming()

        val initialOffset = TimeUtil.getPcTimeOffset()
        /**
         * Executes assertequals operation with thermal imaging domain optimization.
         *
         */
        assertEquals(0L, initialOffset)

        val testOffset = 5000L
        TimeUtil.setPcTimeOffset(testOffset)
        /**
         * Executes assertequals operation with thermal imaging domain optimization.
         *
         */
        assertEquals(testOffset, TimeUtil.getPcTimeOffset())

        // Reset for other tests
        TimeUtil.setPcTimeOffset(0L)
    }

    @Test
    /**
     * Executes testUtcTimestamp functionality.
     */
    /**
     * Executes testutctimestamp operation with thermal imaging domain optimization.
     *
     */
    fun testUtcTimestamp() {
        val offset = 1000L
        TimeUtil.setPcTimeOffset(offset)

        val systemTime = System.currentTimeMillis()
        val utcTime = TimeUtil.getUtcTimestamp()

        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("UTC time should be greater than system time", utcTime > systemTime)
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue(
            "UTC time should be close to system time + offset",
            Math.abs(utcTime - (systemTime + offset)) < 100,
        )

        // Reset
        TimeUtil.setPcTimeOffset(0L)
    }

    @Test
    /**
     * Executes testTimeConversion functionality.
     */
    /**
     * Executes testtimeconversion operation with thermal imaging domain optimization.
     *
     */
    fun testTimeConversion() {
        // Initialize ground truth timing first
        TimeUtil.initializeGroundTruthTiming()

        val offset = 2000L
        TimeUtil.setPcTimeOffset(offset)

        val systemTime = System.currentTimeMillis()
        val utcTime = TimeUtil.systemToUtc(systemTime)
        val backToSystem = TimeUtil.utcToSystem(utcTime)

        // With ground truth timing, the conversion includes device base offset
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("UTC time should include PC offset", Math.abs(utcTime - (systemTime + offset)) < 100)
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Back conversion should be close to original", Math.abs(backToSystem - systemTime) < 100)

        // Reset
        TimeUtil.setPcTimeOffset(0L)
    }

    @Test
    /**
     * Executes testFormatTimestamp functionality.
     */
    /**
     * Executes testformattimestamp operation with thermal imaging domain optimization.
     *
     */
    fun testFormatTimestamp() {
        val timestamp = 1640995200000L // 2022-01-01 00:00:00 UTC
        val formatted = TimeUtil.formatTimestamp(timestamp)

        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Formatted time should contain year", formatted.contains("2022") || formatted.contains("2021"))
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Formatted time should contain time separator", formatted.contains(":"))
    }

    @Test
    /**
     * Executes testGenerateSessionId functionality.
     */
    /**
     * Executes testgeneratesessionid operation with thermal imaging domain optimization.
     *
     */
    fun testGenerateSessionId() {
        val sessionId1 = TimeUtil.generateSessionId()
        val sessionId2 = TimeUtil.generateSessionId()
        val customId = TimeUtil.generateSessionId("CUSTOM")

        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Session ID should start with GSR", sessionId1.startsWith("GSR_"))
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Session ID should start with GSR", sessionId2.startsWith("GSR_"))
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Custom session ID should start with CUSTOM", customId.startsWith("CUSTOM_"))

        // Check that IDs are not empty and contain underscores
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Session ID should not be empty", sessionId1.length > 4)
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Session ID should contain underscore", sessionId1.contains("_"))
    }

    @Test
    /**
     * Executes testGroundTruthTiming functionality.
     */
    /**
     * Executes testgroundtruthtiming operation with thermal imaging domain optimization.
     *
     */
    fun testGroundTruthTiming() {
        // Initialize ground truth
        TimeUtil.initializeGroundTruthTiming()

        val groundTruthBase = TimeUtil.getGroundTruthBase()
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue(
            "Ground truth base should be recent",
            System.currentTimeMillis() - groundTruthBase < 1000,
        )

        val syncTime = TimeUtil.getSynchronizedTimestamp()
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Synchronized timestamp should be valid", syncTime > 0)
    }

    @Test
    /**
     * Executes testTimingMetadata functionality.
     */
    /**
     * Executes testtimingmetadata operation with thermal imaging domain optimization.
     *
     */
    fun testTimingMetadata() {
        TimeUtil.initializeGroundTruthTiming()
        TimeUtil.setPcTimeOffset(1500L)

        val metadata = TimeUtil.getTimingMetadata()

        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Should contain ground truth base", metadata.containsKey("ground_truth_base"))
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Should contain PC offset", metadata.containsKey("pc_offset_ms"))
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Should contain device model", metadata.containsKey("device_model"))
        /**
         * Executes asserttrue operation with thermal imaging domain optimization.
         *
         */
        assertTrue("Should contain timing mode", metadata.containsKey("timing_mode"))

        /**
         * Executes assertequals operation with thermal imaging domain optimization.
         *
         */
        assertEquals("1500", metadata["pc_offset_ms"])
        // Device model is now dynamic based on actual device detection
        /**
         * Executes assertnotnull operation with thermal imaging domain optimization.
         *
         */
        assertNotNull("Device model should be detected", metadata["device_model"])
        /**
         * Executes assertequals operation with thermal imaging domain optimization.
         *
         */
        assertEquals("unified_ntp_style", metadata["timing_mode"])

        // Verify processor is detected
        /**
         * Executes assertnotnull operation with thermal imaging domain optimization.
         *
         */
        assertNotNull("Device processor should be detected", metadata["device_processor"])

        // Reset
        TimeUtil.setPcTimeOffset(0L)
    }
}
