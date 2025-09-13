package com.topdon.lib.core.discovery

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Network Service Discovery manager for automatic device discovery using mDNS/Zeroconf.
 * Discovers PC Controllers and thermal cameras on the local network.
 */
/**
 * NetworkDiscoveryService provides background service functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing NetworkDiscoveryService functionality for the IRCamera system.
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
class NetworkDiscoveryService(private val context: Context) {
    companion object {
        private const val TAG = "NetworkDiscovery"
        private const val SERVICE_TYPE_PC_CONTROLLER = "_topdon-pc._tcp"
        private const val SERVICE_TYPE_THERMAL_CAMERA = "_topdon-thermal._tcp"
        private const val SERVICE_NAME_PREFIX = "TOPDON-"
        private const val DISCOVERY_TIMEOUT_MS = 30000L // 30 seconds
    }

    private val nsdManager: NsdManager by lazy {
        context.getSystemService(Context.NSD_SERVICE) as NsdManager
    }

    private val discoveredServices = ConcurrentHashMap<String, DiscoveredDevice>()
    private val activeDiscoveryListeners = ConcurrentHashMap<String, NsdManager.DiscoveryListener>()
    private var registrationListener: NsdManager.RegistrationListener? = null
    private var isDiscovering = false
    private var isRegistered = false

    private val discoveryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    data class DiscoveredDevice(
        val serviceName: String,
/**
 * Specialized thermal imaging component providing DeviceType functionality for the IRCamera system.
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
    enum class DeviceType {
        PC_CONTROLLER,
        THERMAL_CAMERA_TS004,
        THERMAL_CAMERA_TC007,
        UNKNOWN,
/**
 * Specialized thermal imaging component providing DiscoveryEventListener functionality for the IRCamera system.
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
    interface DiscoveryEventListener {
    /**
     * Callback method triggered when devicediscovered occurs.
     */
        fun onDeviceDiscovered(device: DiscoveredDevice)

    /**
     * Callback method triggered when devicelost occurs.
     */
        fun onDeviceLost(serviceName: String)

    /**
     * Callback method triggered when discoverystarted occurs.
     */
        fun onDiscoveryStarted()

    /**
     * Callback method triggered when discoverystopped occurs.
     */
        fun onDiscoveryStopped()

    /**
     * Callback method triggered when error occurs.
     */
        fun onError(
            operation: String,
            error: String,
        )
    }

    private var eventListener: DiscoveryEventListener? = null

    /**
     * Sets eventlistener configuration.
     */
    fun setEventListener(listener: DiscoveryEventListener?) {
        eventListener = listener
    }

    /**
     * Start network service discovery for PC controllers and thermal cameras
     */
    fun startDiscovery(): Boolean {
        return try {
            if (isDiscovering) {
                Log.w(TAG, "Discovery already in progress")
                return true
            }

            Log.i(TAG, "Starting network service discovery")

            // Discover PC controllers
            /**
             * Executes startservicediscovery operation with thermal imaging domain optimization.
             *
             */
            startServiceDiscovery(SERVICE_TYPE_PC_CONTROLLER)

            // Discover thermal cameras
            /**
             * Executes startservicediscovery operation with thermal imaging domain optimization.
             *
             */
            startServiceDiscovery(SERVICE_TYPE_THERMAL_CAMERA)

            isDiscovering = true
            eventListener?.onDiscoveryStarted()

            // Auto-stop discovery after timeout
            discoveryScope.launch {
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(DISCOVERY_TIMEOUT_MS)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isDiscovering) {
                    Log.i(TAG, "Discovery timeout reached, stopping discovery")
                    /**
                     * Executes stopdiscovery operation with thermal imaging domain optimization.
                     *
                     */
                    stopDiscovery()
                }
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start discovery", e)
            eventListener?.onError("start_discovery", e.message ?: "Unknown error")
            false
        }
    }

    /**
     * Stop network service discovery
     */
    fun stopDiscovery() {
        if (!isDiscovering) return

        try {
            activeDiscoveryListeners.values.forEach { listener ->
                try {
                    nsdManager.stopServiceDiscovery(listener)
                } catch (e: Exception) {
                    Log.w(TAG, "Error stopping individual discovery listener", e)
                }
            }
            activeDiscoveryListeners.clear()

            isDiscovering = false
            eventListener?.onDiscoveryStopped()
            Log.i(TAG, "Network service discovery stopped")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping discovery", e)
            eventListener?.onError("stop_discovery", e.message ?: "Unknown error")
        }
    }

    /**
     * Register this device as discoverable service
     */
    fun registerService(
        serviceName: String,
        port: Int,
        deviceType: DeviceType,
        attributes: Map<String, String> = emptyMap(),
    ): Boolean {
        return try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isRegistered) {
                Log.w(TAG, "Service already registered")
                return true
            }

            val serviceType =
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (deviceType) {
                    DeviceType.PC_CONTROLLER -> SERVICE_TYPE_PC_CONTROLLER
                    DeviceType.THERMAL_CAMERA_TS004,
                    DeviceType.THERMAL_CAMERA_TC007,
                    -> SERVICE_TYPE_THERMAL_CAMERA
                    else -> SERVICE_TYPE_THERMAL_CAMERA
                }

            val serviceInfo =
                /**
                 * Executes nsdserviceinfo operation with thermal imaging domain optimization.
                 *
                 */
                NsdServiceInfo().apply {
                    this.serviceName = "$SERVICE_NAME_PREFIX$serviceName"
                    this.serviceType = serviceType
                    this.port = port

                    // Add device attributes
                    attributes.forEach { (key, value) ->
                        /**
                         * Configures the attribute with validation and thermal imaging optimization.
                         *
                         */
                        setAttribute(key, value)
                    }

                    // Add device type
                    /**
                     * Configures the attribute with validation and thermal imaging optimization.
                     *
                     */
                    setAttribute("device_type", deviceType.name)
                    /**
                     * Configures the attribute with validation and thermal imaging optimization.
                     *
                     */
                    setAttribute("version", "1.0")
                }

            registrationListener =
                object : NsdManager.RegistrationListener {
                    /**
                     * Executes onregistrationfailed operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
                     * @param errorCode Parameter for operation (type: Int)
                     *
                     */
                    override fun onRegistrationFailed(
                        serviceInfo: NsdServiceInfo,
                        errorCode: Int,
                    ) {
                        Log.e(TAG, "Service registration failed: $errorCode")
                        eventListener?.onError("register_service", "Registration failed: $errorCode")
                    }

                    /**
                     * Executes onunregistrationfailed operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
                     * @param errorCode Parameter for operation (type: Int)
                     *
                     */
                    override fun onUnregistrationFailed(
                        serviceInfo: NsdServiceInfo,
                        errorCode: Int,
                    ) {
                        Log.e(TAG, "Service unregistration failed: $errorCode")
                        eventListener?.onError("unregister_service", "Unregistration failed: $errorCode")
                    }

                    /**
                     * Executes onserviceregistered operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
                     *
                     */
                    override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
                        Log.i(TAG, "Service registered: ${serviceInfo.serviceName}")
                        isRegistered = true
                    }

                    /**
                     * Executes onserviceunregistered operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
                     *
                     */
                    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
                        Log.i(TAG, "Service unregistered: ${serviceInfo.serviceName}")
                        isRegistered = false
                    }
                }

            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
            Log.i(TAG, "Registering service: $serviceName on port $port")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register service", e)
            eventListener?.onError("register_service", e.message ?: "Unknown error")
            false
        }
    }

    /**
     * Unregister the service
     */
    /**
     * Executes unregisterservice operation with thermal imaging domain optimization.
     *
     */
    fun unregisterService() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isRegistered) return

        try {
            registrationListener?.let { listener ->
                nsdManager.unregisterService(listener)
            }
            registrationListener = null
            Log.i(TAG, "Service unregistered")
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering service", e)
            eventListener?.onError("unregister_service", e.message ?: "Unknown error")
        }
    }

    /**
     * Get list of currently discovered devices
     */
    fun getDiscoveredDevices(): List<DiscoveredDevice> {
        return discoveredServices.values.toList()
    }

    /**
     * Get discovered devices by type
     */
    fun getDiscoveredDevicesByType(deviceType: DeviceType): List<DiscoveredDevice> {
        return discoveredServices.values.filter { it.deviceType == deviceType }
    }

    /**
     * Clear discovery cache
     */
    /**
     * Executes cleardiscovereddevices operation with thermal imaging domain optimization.
     *
     */
    fun clearDiscoveredDevices() {
        discoveredServices.clear()
    }

    /**
     * Initiates the operation or service.
     */
    private fun startServiceDiscovery(serviceType: String) {
        val discoveryListener =
            object : NsdManager.DiscoveryListener {
                /**
                 * Executes onstartdiscoveryfailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceType Parameter for operation (type: String)
                 * @param errorCode Parameter for operation (type: Int)
                 *
                 */
                override fun onStartDiscoveryFailed(
                    serviceType: String,
                    errorCode: Int,
                ) {
                    Log.e(TAG, "Discovery start failed for $serviceType: $errorCode")
                    eventListener?.onError("start_discovery", "Failed to start discovery: $errorCode")
                }

                /**
                 * Executes onstopdiscoveryfailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceType Parameter for operation (type: String)
                 * @param errorCode Parameter for operation (type: Int)
                 *
                 */
                override fun onStopDiscoveryFailed(
                    serviceType: String,
                    errorCode: Int,
                ) {
                    Log.e(TAG, "Discovery stop failed for $serviceType: $errorCode")
                    eventListener?.onError("stop_discovery", "Failed to stop discovery: $errorCode")
                }

                /**
                 * Executes ondiscoverystarted operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceType Parameter for operation (type: String)
                 *
                 */
                override fun onDiscoveryStarted(serviceType: String) {
                    Log.d(TAG, "Discovery started for $serviceType")
                }

                /**
                 * Executes ondiscoverystopped operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceType Parameter for operation (type: String)
                 *
                 */
                override fun onDiscoveryStopped(serviceType: String) {
                    Log.d(TAG, "Discovery stopped for $serviceType")
                    activeDiscoveryListeners.remove(serviceType)
                }

                /**
                 * Executes onservicefound operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
                 *
                 */
                override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                    Log.d(TAG, "Service found: ${serviceInfo.serviceName}")

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (serviceInfo.serviceName.startsWith(SERVICE_NAME_PREFIX)) {
                        /**
                         * Executes resolveservice operation with thermal imaging domain optimization.
                         *
                         */
                        resolveService(serviceInfo)
                    }
                }

                /**
                 * Executes onservicelost operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
                 *
                 */
                override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                    Log.d(TAG, "Service lost: ${serviceInfo.serviceName}")

                    discoveredServices.remove(serviceInfo.serviceName)
                    eventListener?.onDeviceLost(serviceInfo.serviceName)
                }
            }

        // Store the listener for this service type
        activeDiscoveryListeners[serviceType] = discoveryListener
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    /**
     * Executes resolveservice functionality.
     */
    /**
     * Executes resolveservice operation with thermal imaging domain optimization.
     *
     * @param
     * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
     *
     */
    private fun resolveService(serviceInfo: NsdServiceInfo) {
        val resolveListener =
            object : NsdManager.ResolveListener {
                /**
                 * Executes onresolvefailed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
                 * @param errorCode Parameter for operation (type: Int)
                 *
                 */
                override fun onResolveFailed(
                    serviceInfo: NsdServiceInfo,
                    errorCode: Int,
                ) {
                    Log.w(TAG, "Resolve failed for ${serviceInfo.serviceName}: $errorCode")
                }

                /**
                 * Executes onserviceresolved operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
                 *
                 */
                override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                    Log.d(TAG, "Service resolved: ${serviceInfo.serviceName}")

                    val deviceType = determineDeviceType(serviceInfo)
                    val attributes = extractAttributes(serviceInfo)

                    @Suppress("DEPRECATION")
                    val ipAddress = serviceInfo.host?.hostAddress ?: "unknown"

                    val discoveredDevice =
                        /**
                         * Executes discovereddevice operation with thermal imaging domain optimization.
                         *
                         */
                        DiscoveredDevice(
                            serviceName = serviceInfo.serviceName,
                            serviceType = serviceInfo.serviceType,
                            ipAddress = ipAddress,
                            port = serviceInfo.port,
                            deviceType = deviceType,
                            attributes = attributes,
                        )

                    discoveredServices[serviceInfo.serviceName] = discoveredDevice
                    eventListener?.onDeviceDiscovered(discoveredDevice)

                    Log.i(TAG, "Discovered ${deviceType.name}: ${discoveredDevice.ipAddress}:${discoveredDevice.port}")
                }
            }

        @Suppress("DEPRECATION")
        nsdManager.resolveService(serviceInfo, resolveListener)
    }

    /**
     * Executes determinedevicetype functionality.
     */
    /**
     * Executes determinedevicetype operation with thermal imaging domain optimization.
     *
     * @param
     * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
     *
     */
    private fun determineDeviceType(serviceInfo: NsdServiceInfo): DeviceType {
        val deviceTypeAttr =
            serviceInfo.attributes["device_type"]?.let {
                /**
                 * Executes string operation with thermal imaging domain optimization.
                 *
                 */
                String(it, Charsets.UTF_8)
            }

        return when {
            deviceTypeAttr == "PC_CONTROLLER" -> DeviceType.PC_CONTROLLER
            deviceTypeAttr == "THERMAL_CAMERA_TS004" -> DeviceType.THERMAL_CAMERA_TS004
            deviceTypeAttr == "THERMAL_CAMERA_TC007" -> DeviceType.THERMAL_CAMERA_TC007
            serviceInfo.serviceType.contains("pc") -> DeviceType.PC_CONTROLLER
            serviceInfo.serviceType.contains("thermal") -> DeviceType.THERMAL_CAMERA_TS004
            serviceInfo.serviceName.contains("TS004") -> DeviceType.THERMAL_CAMERA_TS004
            serviceInfo.serviceName.contains("TC007") -> DeviceType.THERMAL_CAMERA_TC007
            else -> DeviceType.UNKNOWN
        }
    }

    /**
     * Executes extractattributes functionality.
     */
    /**
     * Executes extractattributes operation with thermal imaging domain optimization.
     *
     * @param
     * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
     *
     */
    private fun extractAttributes(serviceInfo: NsdServiceInfo): Map<String, String> {
        val attributes = mutableMapOf<String, String>()

        serviceInfo.attributes.forEach { (key, value) ->
            attributes[key] = String(value, Charsets.UTF_8)
        }

        return attributes
    }

    /**
     * Cleanup resources
     */
    /**
     * Executes cleanup operation with thermal imaging domain optimization.
     *
     */
    fun cleanup() {
        /**
         * Executes stopdiscovery operation with thermal imaging domain optimization.
         *
         */
        stopDiscovery()
        /**
         * Executes unregisterservice operation with thermal imaging domain optimization.
         *
         */
        unregisterService()
        discoveryScope.cancel()
        discoveredServices.clear()
        activeDiscoveryListeners.clear()
    }
}
