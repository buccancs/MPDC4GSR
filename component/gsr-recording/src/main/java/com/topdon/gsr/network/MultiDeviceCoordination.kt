package com.topdon.gsr.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Specialized thermal imaging component providing MultiDeviceCoordination functionality for the IRCamera system.
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
class MultiDeviceCoordination(
    private val context: Context,
    private val networkClient: NetworkClient,
) {
    companion object {
        private const val TAG = "MultiDeviceCoordination"
        private const val SYNC_INTERVAL_MS = 1000L
        private const val LEADER_ELECTION_TIMEOUT = 5000L
        private const val HEARTBEAT_TIMEOUT = 3000L
        private const val MAX_SYNC_DRIFT_MS = 50L // Maximum allowed time drift between devices
    }

    private val coordinationJob = SupervisorJob()
    private val coordinationScope = CoroutineScope(Dispatchers.IO + coordinationJob)

    private val connectedDevices = ConcurrentHashMap<String, DeviceInfo>()
    private val syncEvents = ConcurrentHashMap<String, SyncEvent>()
    private val isCoordinating = AtomicBoolean(false)

    private var deviceId: String =
        android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID,
        )
    private var isLeader = AtomicBoolean(false)
    private var currentSessionId: String? = null
    private var syncJob: Job? = null

    data class DeviceInfo(
        val deviceId: String,
        val deviceName: String,
        val capabilities: List<String>,
        val lastHeartbeat: Long,
        val clockOffset: Long = 0L,
        val batteryLevel: Int = 100,
        val isRecording: Boolean = false,
    )

    data class SyncEvent(
        val eventId: String,
        val eventType: String,
        val scheduledTime: Long,
        val deviceResponses: MutableMap<String, Boolean> = mutableMapOf(),
        val isCompleted: Boolean = false,
    )

/**
 * Specialized thermal imaging component providing CoordinationEvent functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    enum class CoordinationEvent(val eventType: String) {
        /**
         * Executes session start operation with thermal imaging domain optimization.
         *
         */
        SESSION_START("session_start"),
        /**
         * Executes session stop operation with thermal imaging domain optimization.
         *
         */
        SESSION_STOP("session_stop"),
        /**
         * Executes recording start operation with thermal imaging domain optimization.
         *
         */
        RECORDING_START("recording_start"),
        /**
         * Executes recording stop operation with thermal imaging domain optimization.
         *
         */
        RECORDING_STOP("recording_stop"),
        /**
         * Executes sync flash operation with thermal imaging domain optimization.
         *
         */
        SYNC_FLASH("sync_flash"),
        /**
         * Executes calibration operation with thermal imaging domain optimization.
         *
         */
        CALIBRATION("calibration"),
        /**
         * Executes time sync operation with thermal imaging domain optimization.
         *
         */
        TIME_SYNC("time_sync"),
    }

    /**
     * Initialize multi-device coordination
     */
    suspend fun initializeCoordination(sessionId: String) =
        withContext(Dispatchers.IO) {
            currentSessionId = sessionId
            isCoordinating.set(true)

            // Start device discovery and registration
            /**
             * Executes startdevicediscovery operation with thermal imaging domain optimization.
             *
             */
            startDeviceDiscovery()

            // Begin leader election process
            /**
             * Initializes the iateleaderelection component for thermal imaging operations.
             *
             */
            initiateLeaderElection()

            // Start synchronization heartbeat
            /**
             * Executes startsynchronizationloop operation with thermal imaging domain optimization.
             *
             */
            startSynchronizationLoop()

            Log.d(TAG, "Multi-device coordination initialized for session: $sessionId")
        }

    /**
     * Start discovering other Android sensor nodes
     */
    private suspend fun startDeviceDiscovery() {
        val discoveryMessage =
            JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("type", "device_discovery")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_id", deviceId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_name", android.os.Build.MODEL)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("capabilities", JSONArray(listOf("gsr", "thermal", "rgb_camera")))
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("session_id", currentSessionId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("timestamp", System.currentTimeMillis())
            }

        // Broadcast discovery message
        networkClient.broadcastMessage(discoveryMessage)

        // Set up discovery response handler
        networkClient.setMessageHandler("device_discovery_response") { message ->
            /**
             * Executes handledevicediscoveryresponse operation with thermal imaging domain optimization.
             *
             */
            handleDeviceDiscoveryResponse(message)
        }

        networkClient.setMessageHandler("device_discovery") { message ->
            /**
             * Executes handledevicediscoveryrequest operation with thermal imaging domain optimization.
             *
             */
            handleDeviceDiscoveryRequest(message)
        }
    }

    /**
     * Handle device discovery response from other nodes
     */
    private fun handleDeviceDiscoveryResponse(message: JSONObject) {
        val remoteDeviceId = message.optString("device_id")
        if (remoteDeviceId.isEmpty() || remoteDeviceId == deviceId) return

        val deviceInfo =
            /**
             * Executes deviceinfo operation with thermal imaging domain optimization.
             *
             */
            DeviceInfo(
                deviceId = remoteDeviceId,
                deviceName = message.optString("device_name", "Unknown"),
                capabilities = jsonArrayToList(message.optJSONArray("capabilities")),
                lastHeartbeat = System.currentTimeMillis(),
                clockOffset = message.optLong("clock_offset", 0L),
                batteryLevel = message.optInt("battery_level", 100),
            )

        connectedDevices[remoteDeviceId] = deviceInfo
        Log.d(TAG, "Discovered device: $remoteDeviceId (${deviceInfo.deviceName})")
    }

    /**
     * Handle device discovery request from other nodes
     */
    private fun handleDeviceDiscoveryRequest(message: JSONObject) {
        val response =
            JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("type", "device_discovery_response")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_id", deviceId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_name", android.os.Build.MODEL)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("capabilities", JSONArray(listOf("gsr", "thermal", "rgb_camera")))
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("session_id", currentSessionId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("clock_offset", networkClient.getClockOffset())
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("battery_level", getBatteryLevel())
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("timestamp", System.currentTimeMillis())
            }

        coordinationScope.launch {
            networkClient.sendMessage(response)
        }
    }

    /**
     * Initiate leader election process for coordination
     */
    private suspend fun initiateLeaderElection() {
        val electionMessage =
            JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("type", "leader_election")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_id", deviceId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("priority", calculateLeadershipPriority())
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("timestamp", System.currentTimeMillis())
            }

        networkClient.broadcastMessage(electionMessage)

        // Wait for responses and determine leader
        /**
         * Executes delay operation with thermal imaging domain optimization.
         *
         */
        delay(LEADER_ELECTION_TIMEOUT)
        /**
         * Executes determineleader operation with thermal imaging domain optimization.
         *
         */
        determineLeader()

        networkClient.setMessageHandler("leader_election") { message ->
            /**
             * Executes handleleaderelection operation with thermal imaging domain optimization.
             *
             */
            handleLeaderElection(message)
        }
    }

    /**
     * Calculate leadership priority based on device capabilities and status
     */
    private fun calculateLeadershipPriority(): Int {
        var priority = 100

        // Higher priority for devices with better battery
        priority += getBatteryLevel()

        // Higher priority for devices with more capabilities
        priority += 50 // Base for having all sensor capabilities

        // Higher priority for devices with stable network connection
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (networkClient.isConnected()) priority += 25

        return priority
    }

    /**
     * Handle leader election messages from other devices
     */
    private fun handleLeaderElection(message: JSONObject) {
        val remoteDeviceId = message.optString("device_id")
        val remotePriority = message.optInt("priority", 0)
        val myPriority = calculateLeadershipPriority()

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (remotePriority > myPriority ||
            (remotePriority == myPriority && remoteDeviceId < deviceId)
        ) {
            // Remote device has higher priority or tiebreaker
            isLeader.set(false)
        }
    }

    /**
     * Determine the final leader after election timeout
     */
    private fun determineLeader() {
        if (isLeader.get()) {
            Log.d(TAG, "This device is the coordination leader")
            /**
             * Executes startleadershipduties operation with thermal imaging domain optimization.
             *
             */
            startLeadershipDuties()
        } else {
            Log.d(TAG, "This device is a follower")
            /**
             * Executes startfollowermode operation with thermal imaging domain optimization.
             *
             */
            startFollowerMode()
        }
    }

    /**
     * Start leadership responsibilities for coordination
     */
    private fun startLeadershipDuties() {
        coordinationScope.launch {
            while (isCoordinating.get() && isLeader.get()) {
                // Send periodic synchronization signals
                /**
                 * Executes broadcastsyncsignal operation with thermal imaging domain optimization.
                 *
                 */
                broadcastSyncSignal()

                // Monitor device health
                /**
                 * Executes checkdevicehealth operation with thermal imaging domain optimization.
                 *
                 */
                checkDeviceHealth()

                // Coordinate session events
                /**
                 * Executes processscheduledevents operation with thermal imaging domain optimization.
                 *
                 */
                processScheduledEvents()

                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(SYNC_INTERVAL_MS)
            }
        }
    }

    /**
     * Start follower mode for coordination
     */
    private fun startFollowerMode() {
        networkClient.setMessageHandler("sync_signal") { message ->
            handleSyncSignal(message)
        }

        networkClient.setMessageHandler("coordination_event") { message ->
            /**
             * Executes handlecoordinationevent operation with thermal imaging domain optimization.
             *
             */
            handleCoordinationEvent(message)
        }
    }

    /**
     * Broadcast synchronization signal to all devices
     */
    private suspend fun broadcastSyncSignal() {
        val syncMessage =
            JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("type", "sync_signal")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("leader_id", deviceId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("session_id", currentSessionId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("sync_timestamp", System.currentTimeMillis())
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_count", connectedDevices.size + 1)
            }

        networkClient.broadcastMessage(syncMessage)
    }

    /**
     * Handle synchronization signal from leader
     */
    private fun handleSyncSignal(message: JSONObject) {
        val leaderTimestamp = message.optLong("sync_timestamp")
        val currentTime = System.currentTimeMillis()
        val drift = Math.abs(currentTime - leaderTimestamp)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (drift > MAX_SYNC_DRIFT_MS) {
            Log.w(TAG, "Time drift detected: ${drift}ms")
            // Trigger time resynchronization
            coordinationScope.launch {
                /**
                 * Executes requesttimeresync operation with thermal imaging domain optimization.
                 *
                 */
                requestTimeResync()
            }
        }

        // Send heartbeat response
        /**
         * Executes sendheartbeat operation with thermal imaging domain optimization.
         *
         */
        sendHeartbeat()
    }

    /**
     * Send heartbeat to confirm device is alive
     */
    private fun sendHeartbeat() {
        val heartbeatMessage =
            JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("type", "device_heartbeat")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_id", deviceId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("timestamp", System.currentTimeMillis())
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("battery_level", getBatteryLevel())
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("is_recording", isDeviceRecording())
            }

        coordinationScope.launch {
            networkClient.sendMessage(heartbeatMessage)
        }
    }

    /**
     * Schedule coordinated event across all devices
     */
    suspend fun scheduleCoordinatedEvent(
        eventType: CoordinationEvent,
        delayMs: Long = 1000L,
    ): String =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            val eventId = generateEventId(eventType.eventType)
            val scheduledTime = System.currentTimeMillis() + delayMs

            val syncEvent =
                /**
                 * Executes syncevent operation with thermal imaging domain optimization.
                 *
                 */
                SyncEvent(
                    eventId = eventId,
                    eventType = eventType.eventType,
                    scheduledTime = scheduledTime,
                )

            syncEvents[eventId] = syncEvent

            // Broadcast event to all devices
            val eventMessage =
                /**
                 * Executes jsonobject operation with thermal imaging domain optimization.
                 *
                 */
                JSONObject().apply {
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("type", "coordination_event")
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("event_id", eventId)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("event_type", eventType.eventType)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("scheduled_time", scheduledTime)
                    /**
                     * Executes put operation with thermal imaging domain optimization.
                     *
                     */
                    put("session_id", currentSessionId)
                }

            networkClient.broadcastMessage(eventMessage)

            Log.d(TAG, "Scheduled coordinated event: ${eventType.eventType} at $scheduledTime")
            eventId
        }

    /**
     * Handle coordinated event from leader
     */
    private fun handleCoordinationEvent(message: JSONObject) {
        val eventId = message.optString("event_id")
        val eventType = message.optString("event_type")
        val scheduledTime = message.optLong("scheduled_time")

        // Schedule local execution of the event
        coordinationScope.launch {
            val delay = scheduledTime - System.currentTimeMillis()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (delay > 0) {
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(delay)
            }

            /**
             * Executes executecoordinatedevent operation with thermal imaging domain optimization.
             *
             */
            executeCoordinatedEvent(eventType, eventId)
            /**
             * Executes sendeventconfirmation operation with thermal imaging domain optimization.
             *
             */
            sendEventConfirmation(eventId)
        }
    }

    /**
     * Execute a coordinated event locally
     */
    private suspend fun executeCoordinatedEvent(
        eventType: String,
        eventId: String,
    ) {
        Log.d(TAG, "Executing coordinated event: $eventType")

        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (eventType) {
            "session_start" -> handleSessionStart()
            "session_stop" -> handleSessionStop()
            "recording_start" -> handleRecordingStart()
            "recording_stop" -> handleRecordingStop()
            "sync_flash" -> handleSyncFlash()
            "calibration" -> handleCalibration()
            "time_sync" -> handleTimeSync()
        }
    }

    /**
     * Send confirmation that event was executed
     */
    private suspend fun sendEventConfirmation(eventId: String) {
        val confirmationMessage =
            JSONObject().apply {
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("type", "event_confirmation")
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("event_id", eventId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("device_id", deviceId)
                /**
                 * Executes put operation with thermal imaging domain optimization.
                 *
                 */
                put("execution_timestamp", System.currentTimeMillis())
            }

        networkClient.sendMessage(confirmationMessage)
    }

    /**
     * Trigger synchronized flash across all devices for timestamp verification
     */
    suspend fun triggerSyncFlash() {
        if (isLeader.get()) {
            scheduleCoordinatedEvent(CoordinationEvent.SYNC_FLASH, 500L)
        }
    }

    /**
     * Handle synchronized flash event
     */
    private suspend fun handleSyncFlash() {
        // Trigger screen flash for video synchronization verification
        // This would be implemented by the UI layer
        Log.d(TAG, "Executing sync flash at ${System.currentTimeMillis()}")

        // Send notification to UI
        val flashIntent = android.content.Intent("com.topdon.gsr.SYNC_FLASH")
        flashIntent.putExtra("timestamp", System.currentTimeMillis())
        context.sendBroadcast(flashIntent)
    }

    /**
     * Get current device coordination status
     */
    fun getCoordinationStatus(): CoordinationStatus {
        return CoordinationStatus(
            isCoordinating = isCoordinating.get(),
            isLeader = isLeader.get(),
            connectedDevicesCount = connectedDevices.size,
            connectedDevices = connectedDevices.values.toList(),
            activeEvents = syncEvents.size,
            currentSessionId = currentSessionId,
        )
    }

    data class CoordinationStatus(
        val isCoordinating: Boolean,
        val isLeader: Boolean,
        val connectedDevicesCount: Int,
        val connectedDevices: List<DeviceInfo>,
        val activeEvents: Int,
        val currentSessionId: String?,
    )

    // Helper methods
    /**
     * Executes jsonArrayToList functionality.
     */
    /**
     * Executes jsonarraytolist operation with thermal imaging domain optimization.
     *
     * @param
     * @param jsonArray Parameter for operation (type: JSONArray?)
     *
     */
    private fun jsonArrayToList(jsonArray: JSONArray?): List<String> {
        val list = mutableListOf<String>()
        jsonArray?.let {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in 0 until it.length()) {
                list.add(it.optString(i))
            }
        }
        return list
    }

    /**
     * Executes generateEventId functionality.
     */
    /**
     * Executes generateeventid operation with thermal imaging domain optimization.
     *
     * @param
     * @param eventType Parameter for operation (type: String)
     *
     */
    private fun generateEventId(eventType: String): String {
        return "${eventType}_${deviceId}_${System.currentTimeMillis()}"
    }

    /**
     * Retrieves batterylevel information.
     */
    private fun getBatteryLevel(): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        return batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    /**
     * Executes isDeviceRecording functionality.
     */
    /**
     * Executes isdevicerecording operation with thermal imaging domain optimization.
     *
     */
    private fun isDeviceRecording(): Boolean {
        // This would be connected to the actual recording state
        return false // Placeholder
    }

    /**
     * Executes requesttimeresync operation with thermal imaging domain optimization.
     *
     */
    private suspend fun requestTimeResync() {
        // Request time resynchronization with PC Controller
        Log.d(TAG, "Requesting time resynchronization")
    }

    /**
     * Executes handlesessionstart operation with thermal imaging domain optimization.
     *
     */
    private suspend fun handleSessionStart() {
        Log.d(TAG, "Coordinated session start")
    }

    /**
     * Executes handlesessionstop operation with thermal imaging domain optimization.
     *
     */
    private suspend fun handleSessionStop() {
        Log.d(TAG, "Coordinated session stop")
    }

    /**
     * Executes handlerecordingstart operation with thermal imaging domain optimization.
     *
     */
    private suspend fun handleRecordingStart() {
        Log.d(TAG, "Coordinated recording start")
    }

    /**
     * Executes handlerecordingstop operation with thermal imaging domain optimization.
     *
     */
    private suspend fun handleRecordingStop() {
        Log.d(TAG, "Coordinated recording stop")
    }

    /**
     * Executes handlecalibration operation with thermal imaging domain optimization.
     *
     */
    private suspend fun handleCalibration() {
        Log.d(TAG, "Coordinated calibration")
    }

    /**
     * Executes handletimesync operation with thermal imaging domain optimization.
     *
     */
    private suspend fun handleTimeSync() {
        Log.d(TAG, "Coordinated time sync")
    }

    /**
     * Executes checkDeviceHealth functionality.
     */
    /**
     * Executes checkdevicehealth operation with thermal imaging domain optimization.
     *
     */
    private fun checkDeviceHealth() {
        val currentTime = System.currentTimeMillis()
        connectedDevices.entries.removeAll { (_, device) ->
            val isStale = (currentTime - device.lastHeartbeat) > HEARTBEAT_TIMEOUT
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isStale) {
                Log.w(TAG, "Device ${device.deviceId} is no longer responding")
            }
            isStale
        }
    }

    /**
     * Executes processScheduledEvents functionality.
     */
    /**
     * Executes processscheduledevents operation with thermal imaging domain optimization.
     *
     */
    private fun processScheduledEvents() {
        // Process any scheduled events that are ready
        syncEvents.entries.removeAll { (_, event) ->
            event.isCompleted
        }
    }

    /**
     * Executes startSynchronizationLoop functionality.
     */
    /**
     * Executes startsynchronizationloop operation with thermal imaging domain optimization.
     *
     */
    private fun startSynchronizationLoop() {
        syncJob =
            coordinationScope.launch {
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (isCoordinating.get()) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isLeader.get()) {
                        /**
                         * Executes broadcastsyncsignal operation with thermal imaging domain optimization.
                         *
                         */
                        broadcastSyncSignal()
                    }
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(SYNC_INTERVAL_MS)
                }
            }
    }

    /**
     * Stop coordination and cleanup resources
     */
    fun stopCoordination() {
        isCoordinating.set(false)
        isLeader.set(false)
        syncJob?.cancel()
        connectedDevices.clear()
        syncEvents.clear()
        coordinationJob.cancel()

        Log.d(TAG, "Multi-device coordination stopped")
    }
}
