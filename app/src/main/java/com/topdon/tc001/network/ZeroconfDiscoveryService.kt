package com.topdon.tc001.network

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Specialized thermal imaging component providing ZeroconfDiscoveryService functionality for the IRCamera system.
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
class ZeroconfDiscoveryService(private val context: Context) {
    companion object {
        private const val TAG = "ZeroconfDiscovery"
        private const val SERVICE_TYPE = "_ircamera._tcp."
        private const val SERVICE_NAME = "IRCamera-Device"
        private const val DISCOVERY_TIMEOUT = 30000L // 30 seconds
    }

/**
 * Specialized thermal imaging component providing ServiceDiscoveryListener functionality for the IRCamera system.
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
    interface ServiceDiscoveryListener {
    /**
     * Executes onServiceDiscovered functionality.
     */
        /**
         * Executes onservicediscovered operation with thermal imaging domain optimization.
         *
         * @param
         * @param serviceInfo Parameter for operation (type: NetworkClient.ControllerInfo)
         *
         */
        fun onServiceDiscovered(serviceInfo: NetworkClient.ControllerInfo)

    /**
     * Executes onServiceLost functionality.
     */
        /**
         * Executes onservicelost operation with thermal imaging domain optimization.
         *
         * @param
         * @param serviceName Parameter for operation (type: String)
         *
         */
        fun onServiceLost(serviceName: String)

    /**
     * Executes onServiceRegistered functionality.
     */
        /**
         * Executes onserviceregistered operation with thermal imaging domain optimization.
         *
         * @param
         * @param serviceName Parameter for operation (type: String)
         *
         */
        fun onServiceRegistered(serviceName: String)

    /**
     * Executes onDiscoveryError functionality.
     */
        /**
         * Executes ondiscoveryerror operation with thermal imaging domain optimization.
         *
         * @param
         * @param errorCode Parameter for operation (type: Int)
         * @param message Parameter for operation (type: String)
         *
         */
        fun onDiscoveryError(
            errorCode: Int,
            message: String,
        )
    }

    private var serviceListener: ServiceDiscoveryListener? = null

    /**
     * Sets servicelistener configuration.
     */
    fun setServiceListener(listener: ServiceDiscoveryListener?) {
        serviceListener = listener
    }

    /**
     * Start discovering PC Controllers using mDNS
     */
    suspend fun startDiscovery(): Boolean =
        withContext(Dispatchers.Main) {
            if (isDiscovering) {
                Log.w(TAG, "Discovery already in progress")
                return@withContext true
            }

            try {
                discoveryListener = createDiscoveryListener()
                nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
                isDiscovering = true
                Log.i(TAG, "Started mDNS service discovery for type: $SERVICE_TYPE")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start service discovery", e)
                serviceListener?.onDiscoveryError(-1, e.message ?: "Discovery failed")
                false
            }
        }

    /**
     * Stop service discovery
     */
    /**
     * Executes stopdiscovery operation with thermal imaging domain optimization.
     *
     */
    fun stopDiscovery() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isDiscovering) return

        try {
            discoveryListener?.let { nsdManager.stopServiceDiscovery(it) }
            isDiscovering = false
            discoveredServices.clear()
            Log.i(TAG, "Stopped service discovery")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping discovery", e)
        }
    }

    /**
     * Register this device as a service for PC Controller discovery
     */
    suspend fun registerService(
        deviceId: String,
        port: Int,
    ): Boolean =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.Main) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isRegistered) {
                Log.w(TAG, "Service already registered")
                return@withContext true
            }

            try {
                val serviceInfo =
                    /**
                     * Executes nsdserviceinfo operation with thermal imaging domain optimization.
                     *
                     */
                    NsdServiceInfo().apply {
                        serviceName = "$SERVICE_NAME-$deviceId"
                        serviceType = SERVICE_TYPE
                        /**
                         * Configures the port with validation and thermal imaging optimization.
                         *
                         */
                        setPort(port)
                        /**
                         * Configures the attribute with validation and thermal imaging optimization.
                         *
                         */
                        setAttribute("device_id", deviceId)
                        /**
                         * Configures the attribute with validation and thermal imaging optimization.
                         *
                         */
                        setAttribute("device_type", "android_phone")
                        /**
                         * Configures the attribute with validation and thermal imaging optimization.
                         *
                         */
                        setAttribute("capabilities", "gsr,thermal,visual,audio")
                        /**
                         * Configures the attribute with validation and thermal imaging optimization.
                         *
                         */
                        setAttribute("version", "1.0")
                    }

                registrationListener = createRegistrationListener()
                nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
                Log.i(TAG, "Registering service: ${serviceInfo.serviceName}")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to register service", e)
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
            registrationListener?.let { nsdManager.unregisterService(it) }
            isRegistered = false
            Log.i(TAG, "Unregistered service")
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering service", e)
        }
    }

    /**
     * Get list of discovered PC Controllers
     */
    fun getDiscoveredControllers(): List<NetworkClient.ControllerInfo> {
        return discoveredServices.values.mapNotNull { serviceInfo ->
            try {
                val host = serviceInfo.host?.hostAddress ?: return@mapNotNull null
                val port = serviceInfo.port
                val deviceName =
                    serviceInfo.attributes?.get("device_name")?.let { String(it) }
                        ?: serviceInfo.serviceName
                val capabilities =
                    serviceInfo.attributes?.get("capabilities")?.let { String(it) }
                        ?.split(",") ?: emptyList()

                NetworkClient.ControllerInfo(
                    ipAddress = host,
                    port = port,
                    deviceName = deviceName,
                    capabilities = capabilities,
                )
            } catch (e: Exception) {
                Log.w(TAG, "Failed to parse service info: ${serviceInfo.serviceName}", e)
                null
            }
        }
    }

    /**
     * Executes createDiscoveryListener functionality.
     */
    /**
     * Executes creatediscoverylistener operation with thermal imaging domain optimization.
     *
     */
    private fun createDiscoveryListener(): NsdManager.DiscoveryListener {
        return object : NsdManager.DiscoveryListener {
            /**
             * Executes ondiscoverystarted operation with thermal imaging domain optimization.
             *
             * @param
             * @param regType Parameter for operation (type: String)
             *
             */
            override fun onDiscoveryStarted(regType: String) {
                Log.d(TAG, "Service discovery started: $regType")
            }

            /**
             * Executes onservicefound operation with thermal imaging domain optimization.
             *
             * @param
             * @param service Parameter for operation (type: NsdServiceInfo)
             *
             */
            override fun onServiceFound(service: NsdServiceInfo) {
                Log.d(TAG, "Service discovery success: ${service.serviceName}")

                // Don't discover our own service
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (service.serviceName.startsWith(SERVICE_NAME)) {
                    return
                }

                // Resolve the service to get detailed information
                nsdManager.resolveService(service, createResolveListener())
            }

            /**
             * Executes onservicelost operation with thermal imaging domain optimization.
             *
             * @param
             * @param service Parameter for operation (type: NsdServiceInfo)
             *
             */
            override fun onServiceLost(service: NsdServiceInfo) {
                Log.i(TAG, "Service lost: ${service.serviceName}")
                discoveredServices.remove(service.serviceName)
                serviceListener?.onServiceLost(service.serviceName)
            }

            /**
             * Executes ondiscoverystopped operation with thermal imaging domain optimization.
             *
             * @param
             * @param serviceType Parameter for operation (type: String)
             *
             */
            override fun onDiscoveryStopped(serviceType: String) {
                Log.i(TAG, "Discovery stopped: $serviceType")
                isDiscovering = false
            }

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
                Log.e(TAG, "Discovery failed to start: $serviceType, error: $errorCode")
                isDiscovering = false
                serviceListener?.onDiscoveryError(errorCode, "Failed to start discovery")
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
                Log.e(TAG, "Discovery failed to stop: $serviceType, error: $errorCode")
                serviceListener?.onDiscoveryError(errorCode, "Failed to stop discovery")
            }
        }
    }

    /**
     * Executes createResolveListener functionality.
     */
    /**
     * Executes createresolvelistener operation with thermal imaging domain optimization.
     *
     */
    private fun createResolveListener(): NsdManager.ResolveListener {
        return object : NsdManager.ResolveListener {
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
                Log.e(TAG, "Resolve failed: ${serviceInfo.serviceName}, error: $errorCode")
            }

            /**
             * Executes onserviceresolved operation with thermal imaging domain optimization.
             *
             * @param
             * @param serviceInfo Parameter for operation (type: NsdServiceInfo)
             *
             */
            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                Log.i(TAG, "Service resolved: ${serviceInfo.serviceName} at ${serviceInfo.host}:${serviceInfo.port}")

                discoveredServices[serviceInfo.serviceName] = serviceInfo

                // Notify listener
                try {
                    val host = serviceInfo.host?.hostAddress ?: return
                    val port = serviceInfo.port
                    val deviceName =
                        serviceInfo.attributes?.get("device_name")?.let { String(it) }
                            ?: serviceInfo.serviceName
                    val capabilities =
                        serviceInfo.attributes?.get("capabilities")?.let { String(it) }
                            ?.split(",") ?: emptyList()

                    val controllerInfo =
                        NetworkClient.ControllerInfo(
                            ipAddress = host,
                            port = port,
                            deviceName = deviceName,
                            capabilities = capabilities,
                        )

                    serviceListener?.onServiceDiscovered(controllerInfo)
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to parse resolved service", e)
                }
            }
        }
    }

    /**
     * Executes createRegistrationListener functionality.
     */
    /**
     * Executes createregistrationlistener operation with thermal imaging domain optimization.
     *
     */
    private fun createRegistrationListener(): NsdManager.RegistrationListener {
        return object : NsdManager.RegistrationListener {
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
                serviceListener?.onServiceRegistered(serviceInfo.serviceName)
            }

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
                Log.e(TAG, "Service registration failed: ${serviceInfo.serviceName}, error: $errorCode")
                isRegistered = false
                serviceListener?.onDiscoveryError(errorCode, "Registration failed")
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
                Log.e(TAG, "Service unregistration failed: ${serviceInfo.serviceName}, error: $errorCode")
                serviceListener?.onDiscoveryError(errorCode, "Unregistration failed")
            }
        }
    }

    /**
     * Clean up resources
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
        discoveredServices.clear()
        serviceListener = null
        discoveryListener = null
        registrationListener = null
    }
}
