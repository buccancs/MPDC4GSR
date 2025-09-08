package com.topdon.lib.core.example

import android.content.Context
import android.util.Log
import com.topdon.lib.core.discovery.NetworkDiscoveryService
import com.topdon.lib.core.messaging.ReliableMessageService
import com.topdon.lib.core.security.CertificateManager
import com.topdon.lib.core.socket.WebSocketProxy
import com.topdon.lib.core.sync.TimeSyncService
import com.topdon.tc001.network.NetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * Example demonstrating usage of the enhanced networking features.
 * This shows how to integrate TLS, discovery, time sync, and reliable messaging.
 */
class EnhancedNetworkingExample(private val context: Context) {
    companion object {
        private const val TAG = "NetworkingExample"
    }

    private val networkClient = NetworkClient(context)
    private val webSocketProxy = WebSocketProxy.getInstance()
    private val exampleScope = CoroutineScope(Dispatchers.IO)

    /**
     * Demonstrates the complete networking enhancement workflow
     */
    fun demonstrateEnhancedNetworking() {
        exampleScope.launch {
            try {
                Log.i(TAG, "=== Enhanced Networking Demo Started ===")

                // Step 1: Initialize enhanced network client
                Log.i(TAG, "1. Initializing enhanced network client...")
                val initialized = networkClient.initialize()
                if (!initialized) {
                    Log.e(TAG, "Failed to initialize network client")
                    return@launch
                }

                // Step 2: Initialize WebSocket with security
                Log.i(TAG, "2. Initializing secure WebSocket...")
                webSocketProxy.initializeSecurity(context)

                // Step 3: Discover devices using mDNS/Zeroconf
                Log.i(TAG, "3. Starting device discovery...")
                val discoveredControllers = networkClient.discoverControllers()
                Log.i(TAG, "Found ${discoveredControllers.size} PC controllers")

                discoveredControllers.forEach { controller ->
                    Log.i(TAG, "  - Controller: ${controller.deviceName} at ${controller.ipAddress}:${controller.port}")
                }

                // Step 4: Connect with TLS and time synchronization
                if (discoveredControllers.isNotEmpty()) {
                    val controller = discoveredControllers.first()
                    Log.i(TAG, "4. Connecting to ${controller.ipAddress} with TLS...")
                    
                    val connected = networkClient.connectToController(
                        controller.ipAddress,
                        controller.port,
                        useSecure = true
                    )

                    if (connected) {
                        Log.i(TAG, "✓ Connected with security: ${networkClient.isSecureConnection()}")
                        
                        // Step 5: Demonstrate synchronized timestamps
                        val syncTimestamp = networkClient.getSynchronizedTimestamp()
                        Log.i(TAG, "5. Synchronized timestamp: $syncTimestamp")

                        // Step 6: Send reliable messages
                        demonstrateReliableMessaging()

                        // Step 7: Connect thermal camera via secure WebSocket
                        demonstrateSecureWebSocket()

                    } else {
                        Log.w(TAG, "Failed to connect to controller")
                    }
                }

                Log.i(TAG, "=== Enhanced Networking Demo Completed ===")

            } catch (e: Exception) {
                Log.e(TAG, "Demo failed", e)
            }
        }
    }

    /**
     * Demonstrates reliable messaging with acknowledgments
     */
    private suspend fun demonstrateReliableMessaging() {
        Log.i(TAG, "6. Demonstrating reliable messaging...")

        val reliableMessaging = ReliableMessageService()
        reliableMessaging.setTransport(object : ReliableMessageService.MessageTransport {
            override suspend fun sendMessage(host: String, port: Int, message: JSONObject): Boolean {
                // This would use the actual network connection
                Log.d(TAG, "Sending message to $host:$port - ${message.optString("message_type")}")
                return true
            }
        })

        reliableMessaging.initialize()

        // Register message handlers
        reliableMessaging.registerMessageHandler("session_start") { message ->
            Log.i(TAG, "Received session start: ${message.optString("session_id")}")
            JSONObject().apply {
                put("message_type", "session_ack")
                put("status", "ready")
            }
        }

        // Send a critical message with reliability
        val messageId = reliableMessaging.sendMessage(
            targetHost = "192.168.40.1",
            targetPort = 8080,
            messageType = "measurement_start",
            content = JSONObject().apply {
                put("session_id", "demo_session_123")
                put("sensors", listOf("gsr", "thermal", "visual"))
            },
            priority = ReliableMessageService.MessagePriority.CRITICAL,
            callback = object : ReliableMessageService.MessageCallback {
                override fun onAcknowledged(messageId: String) {
                    Log.i(TAG, "✓ Message acknowledged: $messageId")
                }

                override fun onFailed(messageId: String, error: String) {
                    Log.e(TAG, "✗ Message failed: $messageId - $error")
                }

                override fun onRetrying(messageId: String, attempt: Int) {
                    Log.w(TAG, "↻ Retrying message: $messageId (attempt $attempt)")
                }
            }
        )

        Log.i(TAG, "Sent reliable message with ID: $messageId")
    }

    /**
     * Demonstrates secure WebSocket connection to thermal camera
     */
    private fun demonstrateSecureWebSocket() {
        Log.i(TAG, "7. Demonstrating secure WebSocket connection...")

        // Start secure WebSocket connection to thermal camera
        webSocketProxy.startWebSocket("TS004_DEMO_DEVICE")

        // Send command to thermal camera
        val command = JSONObject().apply {
            put("cmd", "get_temperature")
            put("timestamp", System.currentTimeMillis())
        }
        
        webSocketProxy.sendMessage(command.toString())
        Log.i(TAG, "Sent command to thermal camera via secure WebSocket")
    }

    /**
     * Cleanup all resources
     */
    fun cleanup() {
        networkClient.cleanup()
        webSocketProxy.stopWebSocket()
    }
}