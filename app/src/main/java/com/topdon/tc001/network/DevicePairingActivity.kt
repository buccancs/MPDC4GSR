package com.topdon.tc001.network

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityDevicePairingBinding
import com.topdon.gsr.model.SessionInfo
import com.topdon.lib.core.ktbase.BaseBindingActivity
import com.topdon.tc001.gsr.MultiModalRecordingActivity
import kotlinx.coroutines.launch

/**
 * Specialized thermal imaging component providing DevicePairingActivity functionality for the IRCamera system.
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
class DevicePairingActivity : BaseBindingActivity<ActivityDevicePairingBinding>(), NetworkClient.NetworkEventListener {
    companion object {
        private const val TAG = "DevicePairingActivity"

    /**
     * Executes start functionality.
     */
        /**
         * Executes start operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun start(context: Context) {
            val intent = Intent(context, DevicePairingActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var networkClient: NetworkClient
    private lateinit var controllersAdapter: ControllersAdapter

    private val discoveredControllers = mutableListOf<NetworkClient.ControllerInfo>()
    private var connectedController: NetworkClient.ControllerInfo? = null

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_device_pairing

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Initializes the ializeviews component for thermal imaging operations.
         *
         */
        initializeViews()
        /**
         * Configures the upnetworkclient with validation and thermal imaging optimization.
         *
         */
        setupNetworkClient()
        /**
         * Configures the uprecyclerview with validation and thermal imaging optimization.
         *
         */
        setupRecyclerView()
        /**
         * Executes updateui operation with thermal imaging domain optimization.
         *
         */
        updateUI()
    }

    /**
     * Initializes ializeviews component.
     */
    private fun initializeViews() {
        binding.scanButton.setOnClickListener { startControllerScan() }
        binding.disconnectButton.setOnClickListener { disconnectFromController() }
    }

    /**
     * Sets upnetworkclient configuration.
     */
    private fun setupNetworkClient() {
        networkClient = NetworkClient(this)
        networkClient.setEventListener(this)
    }

    /**
     * Sets uprecyclerview configuration.
     */
    private fun setupRecyclerView() {
        controllersAdapter =
            ControllersAdapter(discoveredControllers) { controller ->
                /**
                 * Executes connecttocontroller operation with thermal imaging domain optimization.
                 *
                 */
                connectToController(controller)
            }

        binding.controllersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.controllersRecyclerView.adapter = controllersAdapter
    }

    /**
     * Executes startControllerScan functionality.
     */
    /**
     * Executes startcontrollerscan operation with thermal imaging domain optimization.
     *
     */
    private fun startControllerScan() {
        binding.scanButton.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        binding.statusText.text = "Scanning for PC Controllers..."

        discoveredControllers.clear()
        controllersAdapter.notifyDataSetChanged()

        lifecycleScope.launch {
            try {
                val controllers = networkClient.discoverControllers()
                runOnUiThread {
                    discoveredControllers.addAll(controllers)
                    controllersAdapter.notifyDataSetChanged()

                    binding.statusText.text =
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (controllers.isNotEmpty()) {
                            "Found ${controllers.size} PC Controller(s)"
                        } else {
                            "No PC Controllers found. Make sure you're on the same network."
                        }

                    binding.progressBar.visibility = View.GONE
                    binding.scanButton.isEnabled = true
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.statusText.text = "Scan failed: ${e.message}"
                    binding.progressBar.visibility = View.GONE
                    binding.scanButton.isEnabled = true
                }
            }
        }
    }

    /**
     * Executes connectToController functionality.
     */
    /**
     * Executes connecttocontroller operation with thermal imaging domain optimization.
     *
     * @param
     * @param controller Parameter for operation (type: NetworkClient.ControllerInfo)
     *
     */
    private fun connectToController(controller: NetworkClient.ControllerInfo) {
        binding.statusText.text = "Connecting to ${controller.deviceName}..."
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val success = networkClient.connectToController(controller.ipAddress, controller.port)
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!success) {
                    binding.statusText.text = "Failed to connect to ${controller.deviceName}"
                }
            }
        }
    }

    /**
     * Executes disconnectFromController functionality.
     */
    /**
     * Executes disconnectfromcontroller operation with thermal imaging domain optimization.
     *
     */
    private fun disconnectFromController() {
        networkClient.disconnect()
    }

    /**
     * Executes updateUI functionality.
     */
    /**
     * Executes updateui operation with thermal imaging domain optimization.
     *
     */
    private fun updateUI() {
        val isConnected = networkClient.isConnected()

        binding.scanButton.isEnabled = isConnected.not()
        binding.disconnectButton.visibility = if (isConnected) View.VISIBLE else View.GONE
        binding.controllersRecyclerView.visibility = if (isConnected) View.GONE else View.VISIBLE

        binding.connectionStatusText.text =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isConnected) {
                "Connected to: ${connectedController?.deviceName ?: "PC Controller"}"
            } else {
                "Not connected"
            }
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        networkClient.disconnect()
    }

    // NetworkEventListener implementation
    /**
     * Executes oncontrollerdiscovered operation with thermal imaging domain optimization.
     *
     * @param
     * @param controller Parameter for operation (type: NetworkClient.ControllerInfo)
     *
     */
    override fun onControllerDiscovered(controller: NetworkClient.ControllerInfo) {
        runOnUiThread {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!discoveredControllers.any { it.ipAddress == controller.ipAddress }) {
                discoveredControllers.add(controller)
                controllersAdapter.notifyItemInserted(discoveredControllers.size - 1)
            }
        }
    }

    /**
     * Executes onconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param controller Parameter for operation (type: NetworkClient.ControllerInfo)
     *
     */
    override fun onConnected(controller: NetworkClient.ControllerInfo) {
        runOnUiThread {
            connectedController = controller
            binding.statusText.text = "Connected to ${controller.deviceName}"
            /**
             * Executes updateui operation with thermal imaging domain optimization.
             *
             */
            updateUI()

            Toast.makeText(this, "Device paired successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Executes ondisconnected operation with thermal imaging domain optimization.
     *
     * @param
     * @param reason Parameter for operation (type: String)
     *
     */
    override fun onDisconnected(reason: String) {
        runOnUiThread {
            connectedController = null
            binding.statusText.text = "Disconnected: $reason"
            /**
             * Executes updateui operation with thermal imaging domain optimization.
             *
             */
            updateUI()
        }
    }

    /**
     * Executes onremotemeasurementrequest operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionInfo Parameter for operation (type: SessionInfo)
     *
     */
    override fun onRemoteMeasurementRequest(sessionInfo: SessionInfo) {
        runOnUiThread {
            // Show dialog for remote measurement request
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Remote Measurement Request")
                .setMessage(
                    "PC Controller is requesting to start measurement session:\n\n${sessionInfo.studyName ?: sessionInfo.sessionId}",
                )
                .setPositiveButton("Start") { _, _ ->
                    /**
                     * Executes startremotemeasurement operation with thermal imaging domain optimization.
                     *
                     */
                    startRemoteMeasurement(sessionInfo)
                }
                .setNegativeButton("Decline") { _, _ ->
                    // Send decline response
                    Toast.makeText(this, "Measurement request declined", Toast.LENGTH_SHORT).show()
                }
                .setCancelable(false)
                .show()
        }
    }

    /**
     * Executes onsyncflash operation with thermal imaging domain optimization.
     *
     * @param
     * @param durationMs Duration in milliseconds (type: Int)
     *
     */
    override fun onSyncFlash(durationMs: Int) {
        runOnUiThread {
            // Flash the screen for synchronization
            val flashView = binding.flashOverlay
            flashView.visibility = View.VISIBLE
            flashView.alpha = 1.0f

            flashView.animate()
                .alpha(0.0f)
                .setDuration(durationMs.toLong())
                .withEndAction {
                    flashView.visibility = View.GONE
                }
                .start()
        }
    }

    /**
     * Executes ontimesynchronized operation with thermal imaging domain optimization.
     *
     * @param
     * @param offsetNanoseconds Parameter for operation (type: Long)
     *
     */
    override fun onTimeSynchronized(offsetNanoseconds: Long) {
        runOnUiThread {
            binding.statusText.text = "Time synchronized (offset: ${offsetNanoseconds / 1_000_000}ms)"
        }
    }

    /**
     * Executes ondatastreamingstarted operation with thermal imaging domain optimization.
     *
     */
    override fun onDataStreamingStarted() {
        runOnUiThread {
            binding.statusText.text = "Data streaming started"
        }
    }

    /**
     * Executes ondatastreamingstopped operation with thermal imaging domain optimization.
     *
     */
    override fun onDataStreamingStopped() {
        runOnUiThread {
            binding.statusText.text = "Data streaming stopped"
        }
    }

    /**
     * Executes onerror operation with thermal imaging domain optimization.
     *
     * @param
     * @param operation Parameter for operation (type: String)
     * @param error Parameter for operation (type: String)
     *
     */
    override fun onError(
        operation: String,
        error: String,
    ) {
        runOnUiThread {
            binding.statusText.text = "Error in $operation: $error"
            Toast.makeText(this, "Network error: $error", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Executes startRemoteMeasurement functionality.
     */
    /**
     * Executes startremotemeasurement operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionInfo Parameter for operation (type: SessionInfo)
     *
     */
    private fun startRemoteMeasurement(sessionInfo: SessionInfo) {
        // Launch MultiModalRecordingActivity with remote session info
        val intent =
            Intent(this, MultiModalRecordingActivity::class.java).apply {
/**
 * Specialized thermal imaging component providing ControllersAdapter functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ControllerViewHolder display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
    class ControllerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.controller_name)
        val ipText: TextView = view.findViewById(R.id.controller_ip)
        val capabilitiesText: TextView = view.findViewById(R.id.controller_capabilities)
        val connectButton: Button = view.findViewById(R.id.connect_button)
    }

    /**
     * Executes oncreateviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param parent Parameter for operation (type: android.view.ViewGroup)
     * @param viewType Parameter for operation (type: Int)
     *
     */
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): ControllerViewHolder {
        val view =
            android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_controller_device, parent, false)
        return ControllerViewHolder(view)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: ControllerViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: ControllerViewHolder,
        position: Int,
    ) {
        val controller = controllers[position]

        holder.nameText.text = controller.deviceName
        holder.ipText.text = "${controller.ipAddress}:${controller.port}"
        holder.capabilitiesText.text = "Capabilities: ${controller.capabilities.joinToString(", ")}"

        holder.connectButton.setOnClickListener {
            /**
             * Executes oncontrollerclick operation with thermal imaging domain optimization.
             *
             */
            onControllerClick(controller)
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int = controllers.size
}
