package com.topdon.tc001.gsr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.topdon.tc001.R
import com.topdon.tc001.sensors.unified.model.DeviceInfo

/**
 * RecyclerView Adapter for Shimmer Device List
 * 
 * Displays discovered Shimmer3 GSR+ devices with:
 * - Device name and MAC address
 * - Signal strength (RSSI) indicator
 * - Device type and capabilities
 * - Connection status
 */
class ShimmerDeviceAdapter(
    private val onDeviceClick: (DeviceInfo) -> Unit
) : RecyclerView.Adapter<ShimmerDeviceAdapter.DeviceViewHolder>() {

    private val devices = mutableListOf<DeviceInfo>()
    private var selectedDevice: DeviceInfo? = null

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.textDeviceName)
        val deviceAddress: TextView = itemView.findViewById(R.id.textDeviceAddress)
        val deviceType: TextView = itemView.findViewById(R.id.textDeviceType)
        val signalStrength: TextView = itemView.findViewById(R.id.textSignalStrength)
        val signalIcon: ImageView = itemView.findViewById(R.id.imageSignalStrength)
        val gsrCapableIcon: ImageView = itemView.findViewById(R.id.imageGsrCapable)
        val root: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shimmer_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        
        // Set device information
        holder.deviceName.text = device.name.ifEmpty { "Unknown Shimmer" }
        holder.deviceAddress.text = device.address
        holder.deviceType.text = device.deviceType
        
        // Set signal strength
        val rssiText = "${device.rssi} dBm"
        holder.signalStrength.text = rssiText
        
        // Set signal strength icon based on RSSI value
        val signalIconRes = when {
            device.rssi >= -50 -> R.drawable.ic_signal_strength_4 // Excellent
            device.rssi >= -60 -> R.drawable.ic_signal_strength_3 // Good
            device.rssi >= -70 -> R.drawable.ic_signal_strength_2 // Fair
            device.rssi >= -80 -> R.drawable.ic_signal_strength_1 // Poor
            else -> R.drawable.ic_signal_strength_0 // Very poor
        }
        
        try {
            holder.signalIcon.setImageResource(signalIconRes)
        } catch (e: Exception) {
            // Fallback if signal strength icons don't exist
            holder.signalIcon.visibility = View.GONE
        }
        
        // Set GSR capability indicator
        if (device.isGSRCapable) {
            holder.gsrCapableIcon.setImageResource(R.drawable.ic_device_type_shimmer_gsr)
            holder.gsrCapableIcon.visibility = View.VISIBLE
        } else {
            holder.gsrCapableIcon.visibility = View.GONE
        }
        
        // Highlight selected device
        if (device == selectedDevice) {
            holder.root.setBackgroundResource(R.drawable.item_background_selected)
        } else {
            holder.root.setBackgroundResource(R.drawable.item_background_default)
        }
        
        // Set click listener
        holder.root.setOnClickListener {
            selectDevice(device, position)
            onDeviceClick(device)
        }
    }

    override fun getItemCount(): Int = devices.size

    fun updateDevices(newDevices: List<DeviceInfo>) {
        devices.clear()
        devices.addAll(newDevices)
        notifyDataSetChanged()
    }
    
    fun clearDevices() {
        devices.clear()
        selectedDevice = null
        notifyDataSetChanged()
    }
    
    fun selectDevice(device: DeviceInfo, position: Int) {
        val previousSelection = selectedDevice
        selectedDevice = device
        
        // Refresh the previously selected item and the new selection
        devices.indexOfFirst { it == previousSelection }.takeIf { it >= 0 }?.let {
            notifyItemChanged(it)
        }
        notifyItemChanged(position)
    }
    
    fun getDeviceByAddress(address: String): DeviceInfo? {
        return devices.find { it.address == address }
    }
    
    fun getSelectedDevice(): DeviceInfo? = selectedDevice
}