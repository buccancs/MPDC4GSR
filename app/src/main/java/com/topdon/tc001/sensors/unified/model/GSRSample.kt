package com.topdon.tc001.sensors.unified.model

/**
 * **GSR Sample Data Model**
 * 
 * Represents a single GSR (Galvanic Skin Response) measurement from Shimmer3 GSR+ sensor.
 * 
 * This data structure follows research-grade standards with:
 * - High-precision timestamps (nanosecond resolution)
 * - 12-bit ADC validation (0-4095 range)
 * - Quality scoring for data integrity
 * - Multi-modal correlation support (PPG, connection metrics)
 * 
 * @param timestamp Nanosecond precision timestamp (System.nanoTime())
 * @param timestampIso Human-readable ISO timestamp
 * @param gsrMicrosiemens Calibrated GSR value in microsiemens (μS)
 * @param gsrRaw Raw 12-bit ADC value (0-4095 range)
 * @param ppgRaw Raw PPG value if available from ExG sensors
 * @param qualityScore Data quality score (0.0-1.0)
 * @param connectionRssi BLE connection signal strength (dBm)
 * 
 * @author IRCamera Unified Sensor Integration
 */
data class GSRSample(
    val timestamp: Long,
    val timestampIso: String,
    val gsrMicrosiemens: Double,
    val gsrRaw: Int,
    val ppgRaw: Int = 0,
    val qualityScore: Double,
    val connectionRssi: Int
) {
    
    /**
     * Validate if this sample meets research-grade quality standards
     */
    val isValid: Boolean
        get() = gsrRaw in 0..4095 && 
                gsrMicrosiemens > 0.0 && 
                qualityScore >= 0.5
    
    /**
     * Calculate the resistance value in ohms from GSR microsiemens
     */
    val resistanceOhms: Double
        get() = if (gsrMicrosiemens > 0) 1_000_000.0 / gsrMicrosiemens else Double.MAX_VALUE
    
    /**
     * Get quality classification
     */
    val qualityLevel: QualityLevel
        get() = when {
            qualityScore >= 0.9 -> QualityLevel.EXCELLENT
            qualityScore >= 0.7 -> QualityLevel.GOOD
            qualityScore >= 0.5 -> QualityLevel.FAIR
            else -> QualityLevel.POOR
        }
    
    /**
     * Convert to CSV row format
     */
    fun toCsvRow(): String {
        return "$timestamp,$timestampIso,$gsrMicrosiemens,$gsrRaw,$ppgRaw,$qualityScore,$connectionRssi"
    }
    
    /**
     * Convert to JSON-compatible map
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "timestamp" to timestamp,
            "timestamp_iso" to timestampIso,
            "gsr_microsiemens" to gsrMicrosiemens,
            "gsr_raw" to gsrRaw,
            "ppg_raw" to ppgRaw,
            "quality_score" to qualityScore,
            "connection_rssi" to connectionRssi,
            "resistance_ohms" to resistanceOhms,
            "is_valid" to isValid,
            "quality_level" to qualityLevel.name
        )
    }
    
    /**
     * Data quality classification
     */
    enum class QualityLevel {
        EXCELLENT,  // > 0.9
        GOOD,       // 0.7 - 0.9
        FAIR,       // 0.5 - 0.7
        POOR        // < 0.5
    }
    
    companion object {
        
        /**
         * CSV header for data export
         */
        const val CSV_HEADER = "timestamp_ns,timestamp_iso,gsr_microsiemens,gsr_raw,ppg_raw,quality_score,connection_rssi"
        
        /**
         * Create GSR sample from raw Shimmer data
         */
        fun fromRawData(
            timestamp: Long,
            timestampIso: String,
            gsrCalibratedValue: Double,
            gsrRawValue: Int,
            ppgRawValue: Int = 0,
            connectionRssi: Int = -50
        ): GSRSample {
            
            // Calculate quality score based on data validity
            val qualityScore = when {
                gsrRawValue < 0 || gsrRawValue > 4095 -> 0.0  // Out of ADC range
                gsrCalibratedValue <= 0 -> 0.3  // Calibration issue
                gsrRawValue < 50 || gsrRawValue > 4000 -> 0.6  // Near ADC limits
                else -> 0.9  // Good data
            }
            
            return GSRSample(
                timestamp = timestamp,
                timestampIso = timestampIso,
                gsrMicrosiemens = gsrCalibratedValue,
                gsrRaw = gsrRawValue,
                ppgRaw = ppgRawValue,
                qualityScore = qualityScore,
                connectionRssi = connectionRssi
            )
        }
        
        /**
         * Parse GSR sample from CSV row
         */
        fun fromCsvRow(csvRow: String): GSRSample? {
            return try {
                val parts = csvRow.split(",")
                if (parts.size >= 7) {
                    GSRSample(
                        timestamp = parts[0].toLong(),
                        timestampIso = parts[1],
                        gsrMicrosiemens = parts[2].toDouble(),
                        gsrRaw = parts[3].toInt(),
                        ppgRaw = parts[4].toInt(),
                        qualityScore = parts[5].toDouble(),
                        connectionRssi = parts[6].toInt()
                    )
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }
}