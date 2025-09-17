package com.topdon.tc001.sensors.unified
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.shimmerresearch.android.Shimmer
import com.shimmerresearch.android.manager.ShimmerBluetoothManagerAndroid
import com.shimmerresearch.driver.ObjectCluster
import com.shimmerresearch.driver.ShimmerDevice
import com.shimmerresearch.bluetooth.ShimmerBluetooth.BT_STATE
import com.topdon.tc001.sensors.unified.model.DeviceInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
class ShimmerDeviceManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    companion object {
        private const val TAG = "ShimmerDeviceManager"
        private const val SCAN_TIMEOUT_MS = 30000L
        private const val CONNECTION_TIMEOUT_MS = 15000L
        private val SHIMMER_MAC_PREFIXES = listOf(
            "00:06:66", 
            "d0:39:72", 
            "00:80:98"  
        )
        private val SHIMMER_NAME_PATTERNS = listOf(
            "shimmer", "gsr", "rn4", "shimmer3"
        )
    }
    private var shimmerManager: ShimmerBluetoothManagerAndroid? = null
    var bluetoothManager: BluetoothManager? = null
        private set
    private var bluetoothAdapter: BluetoothAdapter? = null
    val shimmerBluetoothManager: ShimmerBluetoothManagerAndroid?
        get() = shimmerManager
    private val connectedDevices = ConcurrentHashMap<String, Shimmer>()
    private val discoveredDevices = ConcurrentHashMap<String, DeviceInfo>()
    private val isScanning = AtomicBoolean(false)
    private var scanJob: Job? = null
    private val _scanResults = MutableSharedFlow<List<DeviceInfo>>()
    val scanResults: SharedFlow<List<DeviceInfo>> = _scanResults.asSharedFlow()
    private val _connectionEvents = MutableSharedFlow<ConnectionEvent>()
    val connectionEvents: SharedFlow<ConnectionEvent> = _connectionEvents.asSharedFlow()
    private val mainHandler = Handler(Looper.getMainLooper())
    data class ConnectionEvent(
        val deviceAddress: String,
        val state: ConnectionState,
        val message: String? = null
    )
    enum class ConnectionState {
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        FAILED,
        TIMEOUT
    }
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Initializing Shimmer Device Manager")
        try {
            if (!hasRequiredPermissions()) {
                Log.e(TAG, "Missing required Bluetooth permissions")
                return@withContext false
            }
            bluetoothManager =
                context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothAdapter = bluetoothManager?.adapter
            if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
                Log.e(TAG, "Bluetooth not available or disabled")
                return@withContext false
            }
            shimmerManager = ShimmerBluetoothManagerAndroid(context, mainHandler)
            Log.i(TAG, "Shimmer Device Manager initialized successfully")
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Shimmer Device Manager", e)
            return@withContext false
        }
    }
    suspend fun startDeviceScanning(): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Starting Shimmer device discovery with MAC filtering")
        if (isScanning.get()) {
            Log.w(TAG, "Device scanning already in progress")
            return@withContext true
        }
        val shimmerMgr = shimmerManager ?: run {
            Log.e(TAG, "Shimmer manager not initialized")
            return@withContext false
        }
        try {
            discoveredDevices.clear()
            isScanning.set(true)
            scanJob = lifecycleOwner.lifecycleScope.launch {
                var scanTime = 0L
                while (isScanning.get() && scanTime < SCAN_TIMEOUT_MS) {
                    delay(1000)
                    scanTime += 1000
                    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val pairedDevices = if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        bluetoothAdapter?.bondedDevices ?: emptySet()
                    } else {
                        emptySet()
                    }
                    val detectedDevices = pairedDevices.mapNotNull { btDevice ->
                        if (isValidShimmerDevice(btDevice)) {
                            DeviceInfo(
                                address = btDevice.address,
                                name = btDevice.name ?: "Unknown Shimmer",
                                rssi = -50, 
                                deviceType = "Shimmer3 GSR+",
                                isGSRCapable = true
                            )
                        } else null
                    }
                    detectedDevices.forEach { device ->
                        discoveredDevices[device.address] = device
                    }
                    _scanResults.emit(discoveredDevices.values.toList())
                    Log.d(TAG, "Scan progress: ${discoveredDevices.size} Shimmer devices found")
                }
                stopDeviceScanning()
            }
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "Error starting device scan", e)
            isScanning.set(false)
            return@withContext false
        }
    }
    suspend fun stopDeviceScanning(): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Stopping Shimmer device scanning")
        try {
            isScanning.set(false)
            scanJob?.cancel()
            Log.i(TAG, "Device scanning stopped. Found ${discoveredDevices.size} Shimmer devices")
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping device scan", e)
            return@withContext false
        }
    }
    suspend fun connectToDevice(deviceInfo: DeviceInfo): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Connecting to Shimmer device: ${deviceInfo.address} (${deviceInfo.name})")
        val shimmerMgr = shimmerManager ?: run {
            Log.e(TAG, "Shimmer manager not initialized")
            return@withContext false
        }
        try {
            _connectionEvents.emit(ConnectionEvent(deviceInfo.address, ConnectionState.CONNECTING))
            shimmerMgr.connectShimmerThroughBTAddress(deviceInfo.address)
            var attempts = 0
            val maxAttempts = CONNECTION_TIMEOUT_MS / 1000
            while (attempts < maxAttempts) {
                if (connectedDevices.containsKey(deviceInfo.address)) {
                    Log.i(TAG, "Successfully connected to Shimmer device: ${deviceInfo.address}")
                    return@withContext true
                }
                delay(1000)
                attempts++
            }
            Log.w(TAG, "Connection timeout for device: ${deviceInfo.address}")
            _connectionEvents.emit(ConnectionEvent(deviceInfo.address, ConnectionState.TIMEOUT))
            return@withContext false
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to device: ${deviceInfo.address}", e)
            _connectionEvents.emit(
                ConnectionEvent(
                    deviceInfo.address,
                    ConnectionState.FAILED,
                    e.message
                )
            )
            return@withContext false
        }
    }
    suspend fun disconnectDevice(deviceAddress: String): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Disconnecting Shimmer device: $deviceAddress")
        try {
            val shimmer = connectedDevices[deviceAddress] ?: run {
                Log.w(TAG, "Device not connected: $deviceAddress")
                return@withContext false
            }
            shimmer.stopStreaming()
            shimmer.disconnect()
            connectedDevices.remove(deviceAddress)
            Log.i(TAG, "Successfully disconnected from device: $deviceAddress")
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting device: $deviceAddress", e)
            return@withContext false
        }
    }
    fun getConnectedDevices(): List<DeviceInfo> {
        return connectedDevices.map { (address, shimmer) ->
            DeviceInfo(
                address = address,
                name = "Connected Shimmer ($address)",
                rssi = -50, 
                deviceType = "Shimmer3 GSR+",
                isGSRCapable = true
            )
        }
    }
    fun getConnectedShimmer(deviceAddress: String): Shimmer? {
        return connectedDevices[deviceAddress]
    }
    suspend fun disconnectAllDevices(): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Disconnecting all Shimmer devices")
        val addresses = connectedDevices.keys.toList()
        var allDisconnected = true
        addresses.forEach { address ->
            if (!disconnectDevice(address)) {
                allDisconnected = false
            }
        }
        return@withContext allDisconnected
    }
    suspend fun release() = withContext(Dispatchers.IO) {
        Log.i(TAG, "Releasing Shimmer Device Manager")
        stopDeviceScanning()
        disconnectAllDevices()
        shimmerManager = null
        bluetoothAdapter = null
        bluetoothManager = null
    }
    private fun isValidShimmerDevice(btDevice: BluetoothDevice): Boolean {
        val address = btDevice.address
        val name = if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            btDevice.name
        } else {
            null
        }
        val hasValidPrefix = SHIMMER_MAC_PREFIXES.any { prefix ->
            address.startsWith(prefix, ignoreCase = true)
        }
        val hasValidName = name?.let { deviceName ->
            SHIMMER_NAME_PATTERNS.any { pattern ->
                deviceName.contains(pattern, ignoreCase = true)
            }
        } ?: false
        val isValid = hasValidPrefix || hasValidName
        if (isValid) {
            Log.d(TAG, "Valid Shimmer device detected: $name ($address)")
        }
        return isValid
    }
    private fun hasRequiredPermissions(): Boolean {
        val requiredPermissions =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        return requiredPermissions.all { permission ->
            ActivityCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}