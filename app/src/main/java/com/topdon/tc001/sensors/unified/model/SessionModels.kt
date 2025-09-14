package com.topdon.tc001.sensors.unified.model

import org.json.JSONObject

/**
 * **Session Data Models**
 * 
 * Comprehensive data models for unified session management in the IRCamera extension.
 * 
 * These models support the complete session lifecycle from configuration to completion,
 * with integration for multi-modal sensor recording, PC controller communication,
 * and comprehensive logging and quality monitoring.
 * 
 * @author IRCamera Unified Sensor Integration
 */

/**
 * Session configuration for creating new recording sessions
 */
data class SessionConfig(
    val sessionName: String,
    val studyName: String,
    val participantId: String,
    val enabledSensors: List<String>,
    val sessionType: SessionType = SessionType.LOCAL,
    val maxDuration: Long? = null,
    val metadata: Map<String, Any> = emptyMap()
) {
    companion object {
        fun fromJson(json: JSONObject): SessionConfig {
            val enabledSensors = mutableListOf<String>()
            val sensorsArray = json.optJSONArray("enabled_sensors")
            if (sensorsArray != null) {
                for (i in 0 until sensorsArray.length()) {
                    enabledSensors.add(sensorsArray.getString(i))
                }
            }
            
            val metadata = mutableMapOf<String, Any>()
            val metadataObj = json.optJSONObject("metadata")
            metadataObj?.keys()?.forEach { key ->
                metadata[key] = metadataObj.get(key)
            }
            
            return SessionConfig(
                sessionName = json.getString("session_name"),
                studyName = json.optString("study_name", ""),
                participantId = json.getString("participant_id"),
                enabledSensors = enabledSensors,
                sessionType = SessionType.valueOf(json.optString("session_type", "LOCAL")),
                maxDuration = if (json.has("max_duration")) json.getLong("max_duration") else null,
                metadata = metadata
            )
        }
    }
}

/**
 * Complete session information including state and metadata
 */
data class SessionInfo(
    val sessionId: String,
    val sessionName: String,
    val studyName: String,
    val participantId: String,
    val sessionDirectory: String,
    val enabledSensors: List<String>,
    val sessionType: SessionType,
    val createdAt: Long,
    val startedAt: Long? = null,
    val completedAt: Long? = null,
    val metadata: Map<String, Any> = emptyMap()
) {
    /**
     * Session duration if completed, or current duration if active
     */
    val duration: Long
        get() = when {
            completedAt != null && startedAt != null -> completedAt - startedAt
            startedAt != null -> System.currentTimeMillis() - startedAt
            else -> 0L
        }
    
    /**
     * Check if session is currently active
     */
    val isActive: Boolean
        get() = startedAt != null && completedAt == null
    
    /**
     * Convert to JSON for network transmission
     */
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("session_id", sessionId)
            put("session_name", sessionName)
            put("study_name", studyName)
            put("participant_id", participantId)
            put("session_directory", sessionDirectory)
            put("enabled_sensors", enabledSensors.joinToString(","))
            put("session_type", sessionType.name)
            put("created_at", createdAt)
            put("started_at", startedAt)
            put("completed_at", completedAt)
            put("duration", duration)
            put("is_active", isActive)
            put("metadata", JSONObject(metadata))
        }
    }
}

/**
 * Session type classification
 */
enum class SessionType {
    /**
     * Local device-controlled recording with local storage only
     */
    LOCAL,
    
    /**
     * PC-controlled recording with real-time streaming to PC
     */
    REMOTE,
    
    /**
     * Combined local storage with PC coordination and monitoring
     */
    HYBRID,
    
    /**
     * Research session with enhanced logging and quality controls
     */
    RESEARCH
}

/**
 * Session status enumeration
 */
enum class SessionStatus(val displayName: String) {
    /**
     * No session is active
     */
    IDLE("Idle"),
    
    /**
     * Session created but not started
     */
    CREATED("Created"),
    
    /**
     * Session is starting up
     */
    STARTING("Starting"),
    
    /**
     * Session is actively recording
     */
    RECORDING("Recording"),
    
    /**
     * Session is paused (if supported)
     */
    PAUSED("Paused"),
    
    /**
     * Session is stopping
     */
    STOPPING("Stopping"),
    
    /**
     * Session completed successfully
     */
    COMPLETED("Completed"),
    
    /**
     * Session encountered an error
     */
    ERROR("Error");
    
    /**
     * Check if session is in an active state
     */
    val isActive: Boolean
        get() = this == RECORDING || this == PAUSED
    
    /**
     * Check if session is transitioning
     */
    val isTransitioning: Boolean
        get() = this == STARTING || this == STOPPING
    
    /**
     * Check if session is completed (successfully or with error)
     */
    val isCompleted: Boolean
        get() = this == COMPLETED || this == ERROR
}

/**
 * Session quality metrics
 */
data class SessionQuality(
    val overallQuality: Double = 0.0,
    val networkQuality: Double = 0.0,
    val gsrQuality: Double = 0.0,
    val thermalQuality: Double = 0.0,
    val rgbQuality: Double = 0.0,
    val gsrSampleCount: Long = 0L,
    val thermalFrameCount: Long = 0L,
    val rgbFrameCount: Long = 0L,
    val syncMarkerCount: Long = 0L,
    val errorCount: Long = 0L,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    /**
     * Quality classification
     */
    val qualityLevel: QualityLevel
        get() = when {
            overallQuality >= 0.9 -> QualityLevel.EXCELLENT
            overallQuality >= 0.7 -> QualityLevel.GOOD
            overallQuality >= 0.5 -> QualityLevel.FAIR
            overallQuality >= 0.3 -> QualityLevel.POOR
            else -> QualityLevel.CRITICAL
        }
    
    /**
     * Total sample/frame count across all sensors
     */
    val totalSamples: Long
        get() = gsrSampleCount + thermalFrameCount + rgbFrameCount
    
    /**
     * Check if session has acceptable quality
     */
    val isAcceptableQuality: Boolean
        get() = overallQuality >= 0.6 && errorCount < 10
    
    /**
     * Convert to map for JSON serialization
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "overall_quality" to overallQuality,
            "network_quality" to networkQuality,
            "gsr_quality" to gsrQuality,
            "thermal_quality" to thermalQuality,
            "rgb_quality" to rgbQuality,
            "gsr_sample_count" to gsrSampleCount,
            "thermal_frame_count" to thermalFrameCount,
            "rgb_frame_count" to rgbFrameCount,
            "sync_marker_count" to syncMarkerCount,
            "error_count" to errorCount,
            "total_samples" to totalSamples,
            "quality_level" to qualityLevel.name,
            "is_acceptable_quality" to isAcceptableQuality,
            "last_updated" to lastUpdated
        )
    }
    
    enum class QualityLevel {
        CRITICAL,
        POOR,
        FAIR,
        GOOD,
        EXCELLENT
    }
}

/**
 * Session statistics for monitoring and reporting
 */
data class SessionStatistics(
    val sessionId: String?,
    val isActive: Boolean,
    val duration: Long,
    val status: SessionStatus,
    val enabledSensors: List<String>,
    val dataQuality: Double,
    val networkQuality: Double,
    val gsrSamples: Long,
    val thermalFrames: Long,
    val rgbFrames: Long,
    val syncMarkers: Long,
    val errors: Long
) {
    /**
     * Total data points collected
     */
    val totalDataPoints: Long
        get() = gsrSamples + thermalFrames + rgbFrames + syncMarkers
    
    /**
     * Average sampling rate across all sensors (samples per second)
     */
    val averageSamplingRate: Double
        get() = if (duration > 0) {
            (totalDataPoints * 1000.0) / duration
        } else 0.0
    
    /**
     * Data quality status
     */
    val qualityStatus: String
        get() = when {
            dataQuality >= 0.9 -> "Excellent"
            dataQuality >= 0.7 -> "Good"
            dataQuality >= 0.5 -> "Fair"
            dataQuality >= 0.3 -> "Poor"
            else -> "Critical"
        }
    
    /**
     * Convert to map for display/export
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "session_id" to sessionId,
            "is_active" to isActive,
            "duration" to duration,
            "duration_formatted" to formatDuration(duration),
            "status" to status.displayName,
            "enabled_sensors" to enabledSensors,
            "data_quality" to dataQuality,
            "network_quality" to networkQuality,
            "gsr_samples" to gsrSamples,
            "thermal_frames" to thermalFrames,
            "rgb_frames" to rgbFrames,
            "sync_markers" to syncMarkers,
            "errors" to errors,
            "total_data_points" to totalDataPoints,
            "average_sampling_rate" to averageSamplingRate,
            "quality_status" to qualityStatus
        )
    }
    
    private fun formatDuration(durationMs: Long): String {
        val seconds = durationMs / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        
        return when {
            hours > 0 -> "${hours}h ${minutes % 60}m ${seconds % 60}s"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }
}

/**
 * Session summary generated at completion
 */
data class SessionSummary(
    val sessionId: String,
    val duration: Long,
    val totalSamples: Long,
    val averageQuality: Double,
    val completedSuccessfully: Boolean,
    val errorCount: Long,
    val dataSize: Long,
    val metadata: Map<String, Any> = emptyMap()
) {
    /**
     * Data size in human-readable format
     */
    val dataSizeFormatted: String
        get() = formatBytes(dataSize)
    
    /**
     * Success rate percentage
     */
    val successRate: Double
        get() = if (totalSamples > 0) {
            ((totalSamples - errorCount).toDouble() / totalSamples.toDouble()) * 100.0
        } else 0.0
    
    /**
     * Convert to map for export
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "session_id" to sessionId,
            "duration" to duration,
            "total_samples" to totalSamples,
            "average_quality" to averageQuality,
            "completed_successfully" to completedSuccessfully,
            "error_count" to errorCount,
            "data_size" to dataSize,
            "data_size_formatted" to dataSizeFormatted,
            "success_rate" to successRate,
            "metadata" to metadata
        )
    }
    
    private fun formatBytes(bytes: Long): String {
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        
        return when {
            gb >= 1.0 -> String.format("%.2f GB", gb)
            mb >= 1.0 -> String.format("%.2f MB", mb)
            kb >= 1.0 -> String.format("%.2f KB", kb)
            else -> "$bytes B"
        }
    }
}

/**
 * Sensor configuration for individual sensors in a session
 */
data class SensorConfig(
    val sensorType: String,
    val enabled: Boolean,
    val samplingRate: Double? = null,
    val configuration: Map<String, Any> = emptyMap()
) {
    /**
     * Check if sensor is GSR
     */
    val isGSR: Boolean
        get() = sensorType.equals("gsr", ignoreCase = true)
    
    /**
     * Check if sensor is thermal camera
     */
    val isThermal: Boolean
        get() = sensorType.equals("thermal", ignoreCase = true)
    
    /**
     * Check if sensor is RGB camera
     */
    val isRGB: Boolean
        get() = sensorType.equals("rgb", ignoreCase = true)
}