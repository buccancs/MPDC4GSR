package com.topdon.tc001.sensors.shimmer.model

/**
 * **GSR Sample Data Model - Research Grade**
 * 
 * Represents a single GSR (Galvanic Skin Response) measurement from Shimmer3 GSR+ sensor
 * with comprehensive metadata for research-grade data collection.
 * 
 * ## Key Features:
 * - **12-bit ADC Precision**: Raw ADC values in 0-4095 range as specified in requirements
 * - **Microsiemens Calibration**: Properly converted GSR values using Shimmer feedback resistor
 * - **Quality Validation**: Real-time quality scoring for research compliance 
 * - **Temporal Precision**: Nanosecond timestamp resolution for multi-sensor synchronization
 * - **Connection Monitoring**: RSSI and connection quality for data validation
 * 
 * @property timestampNanos Nanosecond-precision timestamp for temporal alignment
 * @property gsrMicrosiemens Calibrated GSR value in microsiemens (µS)
 * @property rawADC12Bit Raw 12-bit ADC value (0-4095 range) - CRITICAL for validation
 * @property resistanceOhms Calculated skin resistance in Ohms
 * @property qualityScore Data quality score (0.0-1.0) for research validation
 * @property connectionRSSI Bluetooth connection strength in dBm
 * @property sessionId Recording session identifier for data grouping
 * 
 * @author IRCamera Shimmer Integration Team
 */
data class GSRSample(
    val timestampNanos: Long,
    val gsrMicrosiemens: Double,
    val rawADC12Bit: Int,  // **CRITICAL: Must be 0-4095 range for 12-bit ADC compliance**
    val resistanceOhms: Double,
    val qualityScore: Double,
    val connectionRSSI: Int,
    val sessionId: String
) {
    
    /**
     * Validate that the sample meets research-grade requirements
     */
    fun isValid(): Boolean {
        return rawADC12Bit in 0..4095 &&  // 12-bit ADC validation
               gsrMicrosiemens >= 0.0 &&
               resistanceOhms > 0.0 &&
               qualityScore in 0.0..1.0 &&
               timestampNanos > 0
    }
    
    /**
     * Get human-readable timestamp
     */
    fun getFormattedTimestamp(): String {
        val millis = timestampNanos / 1_000_000
        return java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault())
            .format(java.util.Date(millis))
    }
    
    /**
     * Get quality level classification
     */
    fun getQualityLevel(): String = when {
        qualityScore >= 0.95 -> "Excellent"
        qualityScore >= 0.85 -> "Good"  
        qualityScore >= 0.70 -> "Fair"
        qualityScore >= 0.50 -> "Poor"
        else -> "Critical"
    }
    
    /**
     * Convert to CSV row format with comprehensive metadata
     */
    fun toCsvRow(): String {
        return "$timestampNanos,$gsrMicrosiemens,$rawADC12Bit,$resistanceOhms,$qualityScore,$connectionRSSI"
    }
    
    /**
     * Create a copy with updated quality score (for real-time quality assessment)
     */
    fun withQuality(newQualityScore: Double): GSRSample {
        return copy(qualityScore = newQualityScore.coerceIn(0.0, 1.0))
    }
}