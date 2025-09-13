package com.topdon.module.user.ble

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.topdon.ble.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

/**
 * BLE Device Manager for User Component - Enhanced device pairing and management interface
 *
 * Provides systematic BLE device management capabilities for the Multi-Modal Physiological
 * Sensing Platform user interface, enabling enhanced device pairing, monitoring, and
 * configuration through Nordic BLE enhanced backend.
 *
 * Features:
 * - Enhanced device discovery with filtering
 * - Automatic GSR sensor detection and pairing
 * - Real-time device status monitoring
 * - User-friendly device management interface
 * - Multi-device coordination for hub-spoke systems
 *
 * @author IRCamera User Component Enhancement Team
 */
/**
 * Specialized thermal imaging component providing BleDeviceManager functionality for the IRCamera system.
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
class BleDeviceManager(private val context: Context) : CoroutineScope {
    companion object {
        private const val TAG = "BleDeviceManager"

        // Known GSR sensor identifiers
        private val GSR_DEVICE_NAMES =
            /**
             * Configures the of with validation and thermal imaging optimization.
             *
             */
            setOf(
                "Shimmer3 GSR+",
                "Shimmer",
                "GSR_Unit",
            )
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()

    // Unified BLE components for consolidated functionality
    private val unifiedBleManager = UnifiedBleManager.getInstance(context)
    private var easyBLE: EasyBLE? = null

    // Device management state
    private val _discoveredDevices = MutableLiveData<List<BleDeviceInfo>>()
    val discoveredDevices: LiveData<List<BleDeviceInfo>> = _discoveredDevices

    private val _pairedDevices = MutableLiveData<List<BleDeviceInfo>>()
    val pairedDevices: LiveData<List<BleDeviceInfo>> = _pairedDevices

    private val _deviceStatus = MutableLiveData<Map<String, DeviceConnectionStatus>>()
    val deviceStatus: LiveData<Map<String, DeviceConnectionStatus>> = _deviceStatus

    private val deviceConnections = ConcurrentHashMap<String, Connection>()
    private val deviceInfoMap = ConcurrentHashMap<String, BleDeviceInfo>()

    /**
     * BLE device information for user interface
     */
    data class BleDeviceInfo(
        val address: String,
        val name: String?,
        val rssi: Int,
        val isGsrSensor: Boolean,
        val isPaired: Boolean,
        val lastSeen: Long = System.currentTimeMillis(),
    )

    /**
     * Device connection status for monitoring
     */
    data class DeviceConnectionStatus(
        val address: String,
        val connectionState: ConnectionState,
        val reliabilityScore: Double,
        val dataIntegrity: Double,
        val isActive: Boolean,
    )

    /**
     * Initialize BLE Device Manager with enhanced Nordic backend
     */
    fun initialize(enableNordicBackend: Boolean = true) {
        launch {
            Log.i(TAG, "Initializing BLE Device Manager with Nordic backend: $enableNordicBackend")

            // Initialize enhanced BLE manager
            unifiedBleManager.initialize(context, enableNordicBackend)
            unifiedBleManager.enableMultiDeviceMode(true)

            // Get EasyBLE instance with Nordic backend
            easyBLE =
                EasyBLE.getBuilder()
                    .setUseNordicBleBackend(enableNordicBackend)
                    .build().apply {
                        /**
                         * Initializes the ialize component for thermal imaging operations.
                         *
                         */
                        initialize(context.applicationContext as android.app.Application)
                    }

            // Setup device discovery listener
            /**
             * Configures the updevicediscovery with validation and thermal imaging optimization.
             *
             */
            setupDeviceDiscovery()

            Log.i(TAG, "BLE Device Manager initialized successfully")
        }
    }

    /**
     * Setup enhanced device discovery with GSR sensor detection
     */
    private fun setupDeviceDiscovery() {
        easyBLE?.addScanListener(
            object : com.topdon.ble.callback.ScanListener {
                /**
                 * Executes onscanstart operation with thermal imaging domain optimization.
                 *
                 */
                override fun onScanStart() {
                    Log.d(TAG, "Enhanced BLE scan started")
                }

                /**
                 * Executes onscanstop operation with thermal imaging domain optimization.
                 *
                 */
                override fun onScanStop() {
                    Log.d(TAG, "Enhanced BLE scan stopped")
                }

                /**
                 * Executes onscanresult operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param device Parameter for operation (type: Device)
                 * @param isConnectedBySys Parameter for operation (type: Boolean)
                 *
                 */
                override fun onScanResult(
                    device: Device,
                    isConnectedBySys: Boolean,
                ) {
                    val deviceInfo =
                        /**
                         * Executes bledeviceinfo operation with thermal imaging domain optimization.
                         *
                         */
                        BleDeviceInfo(
                            address = device.address,
                            name = device.name,
                            rssi = device.getRssi(),
                            isGsrSensor = isGsrSensorDevice(device),
                            isPaired = isConnectedBySys,
                        )

                    deviceInfoMap[device.address] = deviceInfo

                    // Mark GSR sensors in enhanced manager
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (deviceInfo.isGsrSensor) {
                        unifiedBleManager.markAsGsrSensor(device.address)
                        Log.i(TAG, "GSR sensor detected: ${device.name} (${device.address})")
                    }

                    /**
                     * Executes updatediscovereddevices operation with thermal imaging domain optimization.
                     *
                     */
                    updateDiscoveredDevices()
                }

                /**
                 * Executes onscanerror operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param errorCode Parameter for operation (type: Int)
                 * @param errorMsg Parameter for operation (type: String?)
                 *
                 */
                override fun onScanError(
                    errorCode: Int,
                    errorMsg: String?,
                ) {
                    Log.e(TAG, "Enhanced BLE scan failed with error code: $errorCode, message: $errorMsg")
                }
            },
        )
    }

    /**
     * Detect if a device is a GSR sensor based on name and characteristics
     */
    private fun isGsrSensorDevice(device: Device): Boolean {
        val deviceName = device.name?.uppercase() ?: return false
        return GSR_DEVICE_NAMES.any { gsrName ->
            deviceName.contains(gsrName.uppercase())
        }
    }

    /**
     * Start enhanced device discovery
     */
    fun startDeviceDiscovery() {
        launch {
            Log.i(TAG, "Starting enhanced device discovery")
            easyBLE?.startScan()
        }
    }

    /**
     * Stop device discovery
     */
    /**
     * Executes stopdevicediscovery operation with thermal imaging domain optimization.
     *
     */
    fun stopDeviceDiscovery() {
        launch {
            Log.i(TAG, "Stopping device discovery")
            easyBLE?.stopScan()
        }
    }

    /**
     * Connect to a BLE device with enhanced monitoring
     */
    fun connectToDevice(deviceAddress: String): Boolean {
        return try {
            Log.i(TAG, "Attempting enhanced connection to device: $deviceAddress")

            val deviceInfo = deviceInfoMap[deviceAddress]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (deviceInfo == null) {
                Log.e(TAG, "Device info not found for address: $deviceAddress")
                return false
            }

            val connection = unifiedBleManager.connectWithEnhancements(deviceAddress)

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (connection != null) {
                deviceConnections[deviceAddress] = connection
                /**
                 * Executes updatedevicestatus operation with thermal imaging domain optimization.
                 *
                 */
                updateDeviceStatus()
                Log.i(TAG, "Enhanced connection successful for device: $deviceAddress")
                true
            } else {
                Log.e(TAG, "Enhanced connection failed for device: $deviceAddress")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to device $deviceAddress", e)
            false
        }
    }

    /**
     * Create optimal connection configuration for different device types
     */
    private fun createOptimalConnectionConfig(isGsrSensor: Boolean): ConnectionConfiguration {
        return ConnectionConfiguration().apply {
            if (isGsrSensor) {
                // Optimized settings for GSR sensors using public methods
                /**
                 * Configures the connecttimeoutmillis with validation and thermal imaging optimization.
                 *
                 */
                setConnectTimeoutMillis(10000)
                /**
                 * Configures the autoreconnect with validation and thermal imaging optimization.
                 *
                 */
                setAutoReconnect(true)
                Log.d(TAG, "Applied GSR-optimized connection configuration")
            } else {
                // Standard settings for other devices
                /**
                 * Configures the connecttimeoutmillis with validation and thermal imaging optimization.
                 *
                 */
                setConnectTimeoutMillis(5000)
                /**
                 * Configures the autoreconnect with validation and thermal imaging optimization.
                 *
                 */
                setAutoReconnect(false)
            }
        }
    }

    /**
     * Create enhanced device observer with user component integration
     */
    private fun createDeviceObserver(deviceAddress: String): EventObserver {
        return object : EventObserver {
            override fun onConnectionStateChanged(device: Device) {
                launch {
                    val connectionState = device.connectionState
                    Log.i(TAG, "Device connection state changed: $deviceAddress, state: $connectionState")

                    /**
                     * Executes when operation with thermal imaging domain optimization.
                     *
                     */
                    when (connectionState) {
                        ConnectionState.SERVICE_DISCOVERED -> {
                            val deviceInfo = deviceInfoMap[deviceAddress]?.copy(isPaired = true)
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (deviceInfo != null) {
                                deviceInfoMap[deviceAddress] = deviceInfo
                            }
                            /**
                             * Executes updatepaireddevices operation with thermal imaging domain optimization.
                             *
                             */
                            updatePairedDevices()
                        }
                        ConnectionState.DISCONNECTED -> {
                            deviceConnections.remove(deviceAddress)
                            val deviceInfo = deviceInfoMap[deviceAddress]?.copy(isPaired = false)
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (deviceInfo != null) {
                                deviceInfoMap[deviceAddress] = deviceInfo
                            }
                            /**
                             * Executes updatepaireddevices operation with thermal imaging domain optimization.
                             *
                             */
                            updatePairedDevices()
                        }
                        else -> {
                            // Handle other connection states
                            Log.d(TAG, "Connection state: $connectionState for device: $deviceAddress")
                        }
                    }
                    /**
                     * Executes updatedevicestatus operation with thermal imaging domain optimization.
                     *
                     */
                    updateDeviceStatus()
                }
            }

            /**
             * Executes onconnectfailed operation with thermal imaging domain optimization.
             *
             * @param
             * @param device Parameter for operation (type: Device)
             * @param failType Parameter for operation (type: Int)
             *
             */
            override fun onConnectFailed(
                device: Device,
                failType: Int,
            ) {
                launch {
                    Log.w(TAG, "Device connection failed: $deviceAddress, error: $failType")
                    /**
                     * Executes updatedevicestatus operation with thermal imaging domain optimization.
                     *
                     */
                    updateDeviceStatus()
                }
            }

            /**
             * Executes onconnecttimeout operation with thermal imaging domain optimization.
             *
             * @param
             * @param device Parameter for operation (type: Device)
             * @param type Parameter for operation (type: Int)
             *
             */
            override fun onConnectTimeout(
                device: Device,
                type: Int,
            ) {
                launch {
                    Log.w(TAG, "Device connection timeout: $deviceAddress, type: $type")
                    /**
                     * Executes updatedevicestatus operation with thermal imaging domain optimization.
                     *
                     */
                    updateDeviceStatus()
                }
            }

            /**
             * Executes oncharacteristicchanged operation with thermal imaging domain optimization.
             *
             * @param
             * @param device Parameter for operation (type: Device)
             * @param service Parameter for operation (type: java.util.UUID)
             * @param characteristic Parameter for operation (type: java.util.UUID)
             * @param value Parameter for operation (type: ByteArray?)
             *
             */
            override fun onCharacteristicChanged(
                device: Device,
                service: java.util.UUID,
                characteristic: java.util.UUID,
                value: ByteArray?,
            ) {
                Log.d(TAG, "Characteristic changed for $deviceAddress: service=$service, char=$characteristic")
            }

            /**
             * Executes onnotificationchanged operation with thermal imaging domain optimization.
             *
             * @param
             * @param request Parameter for operation (type: Request)
             * @param isEnabled Parameter for operation (type: Boolean)
             *
             */
            override fun onNotificationChanged(
                request: Request,
                isEnabled: Boolean,
            ) {
                Log.d(TAG, "Notification changed for $deviceAddress: ${request.type}, enabled: $isEnabled")
            }

            /**
             * Executes oncharacteristicread operation with thermal imaging domain optimization.
             *
             * @param
             * @param request Parameter for operation (type: Request)
             * @param value Parameter for operation (type: ByteArray?)
             *
             */
            override fun onCharacteristicRead(
                request: Request,
                value: ByteArray?,
            ) {
                Log.d(TAG, "Characteristic read for $deviceAddress: ${request.type}")
            }

            /**
             * Executes oncharacteristicwrite operation with thermal imaging domain optimization.
             *
             * @param
             * @param request Parameter for operation (type: Request)
             * @param value Parameter for operation (type: ByteArray?)
             *
             */
            override fun onCharacteristicWrite(
                request: Request,
                value: ByteArray?,
            ) {
                Log.d(TAG, "Characteristic write for $deviceAddress: ${request.type}")
            }

            /**
             * Executes onrequestfailed operation with thermal imaging domain optimization.
             *
             * @param
             * @param request Parameter for operation (type: Request)
             * @param failType Parameter for operation (type: Int)
             * @param value Parameter for operation (type: Any?)
             *
             */
            override fun onRequestFailed(
                request: Request,
                failType: Int,
                value: Any?,
            ) {
                Log.w(TAG, "Request failed for $deviceAddress: ${request.type}, fail type: $failType")
            }

            /**
             * Executes onmtuchanged operation with thermal imaging domain optimization.
             *
             * @param
             * @param request Parameter for operation (type: Request)
             * @param mtu Parameter for operation (type: Int)
             *
             */
            override fun onMtuChanged(
                request: Request,
                mtu: Int,
            ) {
                Log.i(TAG, "MTU changed for $deviceAddress: $mtu")
            }

            /**
             * Executes onrssiread operation with thermal imaging domain optimization.
             *
             * @param
             * @param request Parameter for operation (type: Request)
             * @param rssi Parameter for operation (type: Int)
             *
             */
            override fun onRssiRead(
                request: Request,
                rssi: Int,
            ) {
                Log.d(TAG, "RSSI read for $deviceAddress: $rssi")
            }

            /**
             * Executes ondescriptorread operation with thermal imaging domain optimization.
             *
             * @param
             * @param request Parameter for operation (type: Request)
             * @param value Parameter for operation (type: ByteArray?)
             *
             */
            override fun onDescriptorRead(
                request: Request,
                value: ByteArray?,
            ) {
                Log.d(TAG, "Descriptor read for $deviceAddress: ${request.type}")
            }

            /**
             * Executes onbluetoothadapterstatechanged operation with thermal imaging domain optimization.
             *
             * @param
             * @param state Parameter for operation (type: Int)
             *
             */
            override fun onBluetoothAdapterStateChanged(state: Int) {
                Log.d(TAG, "Bluetooth adapter state changed: $state")
            }

            /**
             * Executes onphychange operation with thermal imaging domain optimization.
             *
             * @param
             * @param request Parameter for operation (type: Request)
             * @param txPhy Parameter for operation (type: Int)
             * @param rxPhy Parameter for operation (type: Int)
             *
             */
            override fun onPhyChange(
                request: Request,
                txPhy: Int,
                rxPhy: Int,
            ) {
                Log.d(TAG, "PHY change for $deviceAddress: TX=$txPhy, RX=$rxPhy")
            }
        }
    }

    /**
     * Disconnect from a specific device
     */
    fun disconnectDevice(deviceAddress: String) {
        launch {
            Log.i(TAG, "Disconnecting device: $deviceAddress")
            deviceConnections[deviceAddress]?.disconnect()
        }
    }

    /**
     * Get current system BLE status for user interface
     */
    fun getSystemBleStatus(): UnifiedBleManager.SystemBleStatus? {
        return try {
            unifiedBleManager.getSystemStatus()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting system BLE status", e)
            null
        }
    }

    /**
     * Update discovered devices list
     */
    private fun updateDiscoveredDevices() {
        _discoveredDevices.postValue(deviceInfoMap.values.toList())
    }

    /**
     * Update paired devices list
     */
    /**
     * Executes updatepaireddevices operation with thermal imaging domain optimization.
     *
     */
    private fun updatePairedDevices() {
        val paired = deviceInfoMap.values.filter { it.isPaired }
        _pairedDevices.postValue(paired)
    }

    /**
     * Update device status with metrics
     */
    private fun updateDeviceStatus() {
        val statusMap =
            deviceConnections.mapValues { (address, connection) ->
                /**
                 * Executes deviceconnectionstatus operation with thermal imaging domain optimization.
                 *
                 */
                DeviceConnectionStatus(
                    address = address,
                    connectionState = connection.connectionState,
                    reliabilityScore = 1.0, // Simplified since metrics method doesn't exist
                    dataIntegrity = 1.0,
                    isActive = connection.connectionState == ConnectionState.SERVICE_DISCOVERED,
                )
            }
        _deviceStatus.postValue(statusMap)
    }

    /**
     * Release all resources
     */
    /**
     * Executes release operation with thermal imaging domain optimization.
     *
     */
    fun release() {
        launch {
            Log.i(TAG, "Releasing BLE Device Manager")

            /**
             * Executes stopdevicediscovery operation with thermal imaging domain optimization.
             *
             */
            stopDeviceDiscovery()

            // Disconnect all devices
            deviceConnections.values.forEach { connection ->
                connection.disconnect()
            }

            deviceConnections.clear()
            deviceInfoMap.clear()

            // Simplified cleanup
            easyBLE?.release()

            Log.i(TAG, "BLE Device Manager released successfully")
        }
    }
}
