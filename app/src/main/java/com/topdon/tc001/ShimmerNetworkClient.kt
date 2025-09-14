package com.topdon.tc001

import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.*
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Network client for sending GSR data to PC Controller
 * 
 * This handles the communication between Android Shimmer device and PC controller
 * using TCP sockets and JSON messages.
 */
class ShimmerNetworkClient(
    private val serverHost: String = "192.168.1.100", // Default PC IP
    private val serverPort: Int = 8888
) {
    companion object {
        private const val TAG = "ShimmerNetworkClient"
        private const val CONNECTION_TIMEOUT_MS = 5000
        private const val RECONNECT_DELAY_MS = 3000L
    }
    
    private var socket: Socket? = null
    private var outputStream: PrintWriter? = null
    private var inputStream: BufferedReader? = null
    private val isConnected = AtomicBoolean(false)
    private val isRunning = AtomicBoolean(false)
    
    private val networkScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var connectionJob: Job? = null
    private var heartbeatJob: Job? = null
    
    // Connection callbacks
    var onConnected: (() -> Unit)? = null
    var onDisconnected: (() -> Unit)? = null
    var onError: ((String) -> Unit)? = null
    
    /**
     * Connect to PC controller
     */
    suspend fun connect(): Boolean = withContext(Dispatchers.IO) {
        try {
            if (isConnected.get()) {
                Log.i(TAG, "Already connected to server")
                return@withContext true
            }
            
            Log.i(TAG, "Connecting to PC Controller at $serverHost:$serverPort")
            
            socket = Socket()
            socket?.connect(java.net.InetSocketAddress(serverHost, serverPort), CONNECTION_TIMEOUT_MS)
            
            outputStream = PrintWriter(socket?.getOutputStream(), true)
            inputStream = BufferedReader(InputStreamReader(socket?.getInputStream()))
            
            isConnected.set(true)
            isRunning.set(true)
            
            // Start message listener
            startMessageListener()
            
            // Start heartbeat
            startHeartbeat()
            
            Log.i(TAG, "Connected to PC Controller successfully")
            withContext(Dispatchers.Main) {
                onConnected?.invoke()
            }
            
            return@withContext true
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to connect to PC Controller: ${e.message}")
            cleanup()
            withContext(Dispatchers.Main) {
                onError?.invoke("Connection failed: ${e.message}")
            }
            return@withContext false
        }
    }
    
    /**
     * Disconnect from PC controller
     */
    fun disconnect() {
        networkScope.launch {
            try {
                Log.i(TAG, "Disconnecting from PC Controller")
                cleanup()
                
                withContext(Dispatchers.Main) {
                    onDisconnected?.invoke()
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error during disconnect: ${e.message}")
            }
        }
    }
    
    /**
     * Send GSR sample to PC controller
     */
    fun sendGSRSample(sample: ShimmerMvpActivity.GSRSample, sequenceNumber: Long) {
        if (!isConnected.get()) return
        
        networkScope.launch {
            try {
                val message = JSONObject().apply {
                    put("type", "gsr_sample")
                    put("timestamp_ms", sample.timestamp)
                    put("gsr_microsiemens", sample.gsrValue)
                    put("raw_value", sample.rawValue)
                    put("resistance_kohm", sample.resistance / 1000.0)
                    put("sample_sequence", sequenceNumber)
                }
                
                sendMessage(message)
                
            } catch (e: Exception) {
                Log.w(TAG, "Error sending GSR sample: ${e.message}")
            }
        }
    }
    
    /**
     * Send recording start notification
     */
    fun sendRecordingStart(sessionId: String) {
        networkScope.launch {
            try {
                val message = JSONObject().apply {
                    put("type", "recording_start")
                    put("session_id", sessionId)
                    put("timestamp_ms", System.currentTimeMillis())
                }
                
                sendMessage(message)
                Log.i(TAG, "Sent recording start notification")
                
            } catch (e: Exception) {
                Log.w(TAG, "Error sending recording start: ${e.message}")
            }
        }
    }
    
    /**
     * Send recording stop notification
     */
    fun sendRecordingStop(sessionId: String, sampleCount: Long) {
        networkScope.launch {
            try {
                val message = JSONObject().apply {
                    put("type", "recording_stop")
                    put("session_id", sessionId)
                    put("timestamp_ms", System.currentTimeMillis())
                    put("total_samples", sampleCount)
                }
                
                sendMessage(message)
                Log.i(TAG, "Sent recording stop notification")
                
            } catch (e: Exception) {
                Log.w(TAG, "Error sending recording stop: ${e.message}")
            }
        }
    }
    
    /**
     * Send sync marker
     */
    fun sendSyncMarker(markerType: String, metadata: Map<String, String> = emptyMap()) {
        networkScope.launch {
            try {
                val message = JSONObject().apply {
                    put("type", "sync_marker")
                    put("marker_type", markerType)
                    put("timestamp_ms", System.currentTimeMillis())
                    put("metadata", JSONObject(metadata))
                }
                
                sendMessage(message)
                Log.i(TAG, "Sent sync marker: $markerType")
                
            } catch (e: Exception) {
                Log.w(TAG, "Error sending sync marker: ${e.message}")
            }
        }
    }
    
    /**
     * Send JSON message to server
     */
    private fun sendMessage(message: JSONObject) {
        try {
            outputStream?.let { out ->
                val messageStr = message.toString() + "\n"
                out.print(messageStr)
                out.flush()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            // Connection might be broken, trigger reconnect
            handleConnectionError(e)
        }
    }
    
    /**
     * Start listening for messages from server
     */
    private fun startMessageListener() {
        connectionJob = networkScope.launch {
            try {
                while (isRunning.get() && isConnected.get()) {
                    inputStream?.let { input ->
                        val line = input.readLine()
                        if (line != null) {
                            processServerMessage(line)
                        } else {
                            // Connection closed by server
                            Log.w(TAG, "Server closed connection")
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Message listener error: ${e.message}")
                handleConnectionError(e)
            }
        }
    }
    
    /**
     * Start heartbeat to keep connection alive
     */
    private fun startHeartbeat() {
        heartbeatJob = networkScope.launch {
            while (isRunning.get() && isConnected.get()) {
                try {
                    delay(30000) // Send heartbeat every 30 seconds
                    
                    val heartbeat = JSONObject().apply {
                        put("type", "heartbeat")
                        put("timestamp_ms", System.currentTimeMillis())
                    }
                    
                    sendMessage(heartbeat)
                    
                } catch (e: Exception) {
                    Log.w(TAG, "Heartbeat error: ${e.message}")
                    break
                }
            }
        }
    }
    
    /**
     * Process incoming message from server
     */
    private fun processServerMessage(message: String) {
        try {
            val json = JSONObject(message)
            val type = json.getString("type")
            
            when (type) {
                "connection_ack" -> {
                    Log.i(TAG, "Received connection acknowledgment from PC Controller")
                }
                "sync_request" -> {
                    Log.i(TAG, "Received sync request from PC Controller")
                    // Handle sync request if needed
                }
                else -> {
                    Log.d(TAG, "Received message: $type")
                }
            }
            
        } catch (e: Exception) {
            Log.w(TAG, "Error processing server message: ${e.message}")
        }
    }
    
    /**
     * Handle connection errors and attempt reconnect
     */
    private fun handleConnectionError(error: Exception) {
        Log.w(TAG, "Connection error: ${error.message}")
        
        if (isRunning.get()) {
            cleanup()
            
            // Attempt reconnect after delay
            networkScope.launch {
                delay(RECONNECT_DELAY_MS)
                if (isRunning.get()) {
                    Log.i(TAG, "Attempting to reconnect...")
                    connect()
                }
            }
        }
    }
    
    /**
     * Clean up resources
     */
    private fun cleanup() {
        isConnected.set(false)
        isRunning.set(false)
        
        connectionJob?.cancel()
        heartbeatJob?.cancel()
        
        try {
            outputStream?.close()
            inputStream?.close()
            socket?.close()
        } catch (e: Exception) {
            Log.w(TAG, "Error during cleanup: ${e.message}")
        }
        
        outputStream = null
        inputStream = null
        socket = null
    }
    
    /**
     * Check if currently connected
     */
    fun isConnected(): Boolean = isConnected.get()
    
    /**
     * Get connection status
     */
    fun getConnectionStatus(): String {
        return when {
            isConnected.get() -> "Connected to $serverHost:$serverPort"
            isRunning.get() -> "Connecting..."
            else -> "Disconnected"
        }
    }
    
    /**
     * Update server address
     */
    fun updateServerAddress(host: String, port: Int = serverPort): ShimmerNetworkClient {
        return ShimmerNetworkClient(host, port)
    }
}