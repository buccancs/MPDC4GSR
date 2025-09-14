package com.topdon.tc001.sensors.shimmer.model

/**
 * **Connection Quality Enumeration**
 * 
 * Defines connection quality levels for Shimmer3 GSR+ sensor monitoring.
 * Used for real-time quality assessment and research-grade data validation.
 * 
 * Quality levels are based on:
 * - Signal strength (RSSI)
 * - Data transmission stability  
 * - Sample timing precision
 * - Connection drop frequency
 * 
 * @author IRCamera Shimmer Integration Team
 */
enum class ConnectionQuality(
    val displayName: String,
    val description: String,
    val minScore: Double,
    val color: String  // For UI display
) {
    
    /**
     * Excellent quality - Research grade data collection
     * - RSSI > -50dBm
     * - Quality score >= 0.95
     * - Minimal jitter and drops
     */
    EXCELLENT(
        displayName = "Excellent", 
        description = "Research-grade quality, minimal noise",
        minScore = 0.95,
        color = "#4CAF50"  // Green
    ),
    
    /**
     * Good quality - Suitable for most research applications
     * - RSSI > -60dBm  
     * - Quality score >= 0.85
     * - Occasional minor drops
     */
    GOOD(
        displayName = "Good",
        description = "High quality, suitable for research",
        minScore = 0.85,
        color = "#8BC34A"  // Light Green
    ),
    
    /**
     * Fair quality - Acceptable with some limitations
     * - RSSI > -70dBm
     * - Quality score >= 0.70  
     * - Noticeable timing variations
     */
    FAIR(
        displayName = "Fair",
        description = "Acceptable quality with minor issues",
        minScore = 0.70,
        color = "#FF9800"  // Orange
    ),
    
    /**
     * Poor quality - May affect data reliability
     * - RSSI > -80dBm
     * - Quality score >= 0.50
     * - Frequent drops and jitter
     */
    POOR(
        displayName = "Poor", 
        description = "Poor quality, data reliability affected",
        minScore = 0.50,
        color = "#FF5722"  // Deep Orange
    ),
    
    /**
     * Critical quality - Unreliable for research
     * - RSSI < -80dBm
     * - Quality score < 0.50
     * - Severe connection issues
     */
    CRITICAL(
        displayName = "Critical",
        description = "Critical issues, data unreliable", 
        minScore = 0.0,
        color = "#F44336"  // Red
    ),
    
    /**
     * Unknown quality - Initial state or error
     */
    UNKNOWN(
        displayName = "Unknown",
        description = "Quality assessment unavailable",
        minScore = 0.0,
        color = "#9E9E9E"  // Grey
    );
    
    companion object {
        
        /**
         * Determine connection quality from score
         */
        fun fromScore(score: Double): ConnectionQuality = when {
            score >= EXCELLENT.minScore -> EXCELLENT
            score >= GOOD.minScore -> GOOD
            score >= FAIR.minScore -> FAIR  
            score >= POOR.minScore -> POOR
            score >= 0.0 -> CRITICAL
            else -> UNKNOWN
        }
        
        /**
         * Determine connection quality from RSSI
         */
        fun fromRSSI(rssi: Int): ConnectionQuality = when {
            rssi >= -50 -> EXCELLENT
            rssi >= -60 -> GOOD
            rssi >= -70 -> FAIR
            rssi >= -80 -> POOR
            rssi > -100 -> CRITICAL
            else -> UNKNOWN
        }
        
        /**
         * Get minimum acceptable quality for research
         */
        fun getMinimumResearchQuality(): ConnectionQuality = FAIR
        
        /**
         * Check if quality meets research standards
         */
        fun isResearchGrade(quality: ConnectionQuality): Boolean {
            return quality.minScore >= getMinimumResearchQuality().minScore
        }
    }
    
    /**
     * Check if this quality level is acceptable for research
     */
    fun isAcceptableForResearch(): Boolean {
        return minScore >= FAIR.minScore
    }
    
    /**
     * Get quality as percentage
     */
    fun toPercentage(): Int {
        return (minScore * 100).toInt()
    }
    
    /**
     * Get recommendation text based on quality level
     */
    fun getRecommendation(): String = when (this) {
        EXCELLENT -> "Optimal for all research applications"
        GOOD -> "Suitable for most research protocols" 
        FAIR -> "Consider improving signal strength"
        POOR -> "Move closer to device or check interference"
        CRITICAL -> "Connection too unstable for reliable data"
        UNKNOWN -> "Establishing connection quality..."
    }
}