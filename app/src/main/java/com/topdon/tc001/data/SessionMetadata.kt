package com.topdon.tc001.data
import android.os.SystemClock
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
data class SessionMetadata(
    val sessionId: String,
    val sessionStartTimestampMs: Long,
    val sessionEndTimestampMs: Long? = null,
    val sessionStartMonotonicNs: Long,
    val sessionEndMonotonicNs: Long? = null,
    val sessionStartIso: String,
    val sessionEndIso: String? = null,
    val deviceModel: String = android.os.Build.MODEL,
    val deviceManufacturer: String = android.os.Build.MANUFACTURER,
    val timingSource: String = "android_monotonic_realtime",
    val modalityFiles: MutableMap<String, String> = mutableMapOf(),
    val syncEvents: MutableList<SyncEvent> = mutableListOf(),
    val recordingDurationMs: Long? = null
) {
    companion object {
        private const val TAG = "SessionMetadata"
        fun createSessionStart(sessionId: String): SessionMetadata {
            val wallClockStartMs = System.currentTimeMillis()
            val monotonicStartNs = SystemClock.elapsedRealtimeNanos()
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
            return SessionMetadata(
                sessionId = sessionId,
                sessionStartTimestampMs = wallClockStartMs,
                sessionStartMonotonicNs = monotonicStartNs,
                sessionStartIso = isoFormatter.format(Date(wallClockStartMs))
            )
        }
    }
    fun markSessionEnd(): SessionMetadata {
        val wallClockEndMs = System.currentTimeMillis()
        val monotonicEndNs = SystemClock.elapsedRealtimeNanos()
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val durationMs = (monotonicEndNs - sessionStartMonotonicNs) / 1_000_000L
        return this.copy(
            sessionEndTimestampMs = wallClockEndMs,
            sessionEndMonotonicNs = monotonicEndNs,
            sessionEndIso = isoFormatter.format(Date(wallClockEndMs)),
            recordingDurationMs = durationMs
        )
    }
    fun addModalityFile(modalityType: String, fileName: String, startOffsetMs: Long = 0) {
        modalityFiles[modalityType] = fileName
        syncEvents.add(
            SyncEvent(
                eventType = "${modalityType}_START",
                timestampMs = sessionStartTimestampMs + startOffsetMs,
                monotonicOffsetNs = startOffsetMs * 1_000_000L,
                metadata = mapOf(
                    "modality" to modalityType,
                    "file" to fileName,
                    "offset_ms" to startOffsetMs.toString()
                )
            )
        )
    }
    fun addSyncEvent(eventType: String, metadata: Map<String, String> = emptyMap()) {
        val currentWallMs = System.currentTimeMillis()
        val currentMonotonicNs = SystemClock.elapsedRealtimeNanos()
        val offsetFromStartNs = currentMonotonicNs - sessionStartMonotonicNs
        syncEvents.add(
            SyncEvent(
                eventType = eventType,
                timestampMs = currentWallMs,
                monotonicOffsetNs = offsetFromStartNs,
                metadata = metadata
            )
        )
    }
    fun getRelativeTimestamp(): Long {
        val currentMonotonicNs = SystemClock.elapsedRealtimeNanos()
        return (currentMonotonicNs - sessionStartMonotonicNs) / 1_000_000L 
    }
    fun monotonicToWallClock(monotonicNs: Long): Long {
        val offsetFromStartNs = monotonicNs - sessionStartMonotonicNs
        return sessionStartTimestampMs + (offsetFromStartNs / 1_000_000L)
    }
    fun saveToFile(sessionDirectory: File): File {
        val metadataFile = File(sessionDirectory, "session_metadata.json")
        val gson = GsonBuilder().setPrettyPrinting().create()
        try {
            metadataFile.writeText(gson.toJson(this))
            android.util.Log.i(TAG, "Session metadata saved: ${metadataFile.absolutePath}")
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Failed to save session metadata", e)
        }
        return metadataFile
    }
    fun createTimingHeader(): String {
        return buildString {
            appendLine("# Multi-Modal Recording Session Timing Information")
            appendLine("# Session ID: $sessionId")
            appendLine("# Session Start: $sessionStartIso (${sessionStartTimestampMs}ms UTC)")
            appendLine("# Monotonic Start: ${sessionStartMonotonicNs}ns")
            appendLine("# Timing Source: $timingSource")
            appendLine("# Device: $deviceManufacturer $deviceModel")
            appendLine("#")
            appendLine("# Timestamps in this file are:")
            appendLine("#   - Wall clock: UTC milliseconds since epoch")
            appendLine("#   - Relative: milliseconds since session start (monotonic)")
            appendLine("#   - Monotonic: nanoseconds since boot (for interval calculation)")
            appendLine("#")
        }
    }
}
data class SyncEvent(
    val eventType: String,
    val timestampMs: Long,
    val monotonicOffsetNs: Long,
    val metadata: Map<String, String> = emptyMap()
)