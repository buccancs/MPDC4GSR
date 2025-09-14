package com.topdon.tc001.sensors.unified.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csl.irCamera.R
import com.topdon.tc001.sensors.unified.model.DeviceInfo

/**
 * **Device Adapter for Shimmer Device List**
 * 
 * RecyclerView adapter for displaying discovered Shimmer devices with selection support.
 * 
 * Features:
 * - Display device name, MAC address, and signal strength
 * - Visual indicators for GSR+ devices and signal quality
 * - Single selection support with click handling
 * - Recommendation badges for optimal devices
 * 
 * @author IRCamera Unified Sensor Integration
 */
class DeviceAdapter(
    private val onDeviceClick: (DeviceInfo) -> Unit
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {
    
    private val devices = mutableListOf<DeviceInfo>()
    private var selectedPosition = RecyclerView.NO_POSITION
    
    /**
     * Update the device list
     */
    fun updateDevices(newDevices: List<DeviceInfo>) {
        devices.clear()
        devices.addAll(DeviceInfo.sortByPriority(newDevices))
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }
    
    /**
     * Get the currently selected device
     */
    fun getSelectedDevice(): DeviceInfo? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            devices[selectedPosition]
        } else null
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shimmer_device, parent, false)
        return DeviceViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position], position == selectedPosition)
    }
    
    override fun getItemCount(): Int = devices.size
    
    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val deviceNameText: TextView = itemView.findViewById(R.id.deviceNameText)
        private val deviceAddressText: TextView = itemView.findViewById(R.id.deviceAddressText)
        private val deviceStatusText: TextView = itemView.findViewById(R.id.deviceStatusText)
        private val signalStrengthText: TextView = itemView.findViewById(R.id.signalStrengthText)
        
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val previousPosition = selectedPosition
                    selectedPosition = position
                    
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                    
                    onDeviceClick(devices[position])
                }
            }
        }
        
        fun bind(device: DeviceInfo, isSelected: Boolean) {
            deviceNameText.text = device.displayName
            deviceAddressText.text = device.address
            deviceStatusText.text = device.statusSummary
            signalStrengthText.text = "${device.rssi} dBm"
            
            // Set background color based on selection
            itemView.setBackgroundColor(
                if (isSelected) Color.LTGRAY else Color.TRANSPARENT
            )
            
            // Set signal strength color
            signalStrengthText.setTextColor(
                when (device.signalStrength) {
                    DeviceInfo.SignalStrength.EXCELLENT -> Color.GREEN
                    DeviceInfo.SignalStrength.GOOD -> Color.BLUE
                    DeviceInfo.SignalStrength.FAIR -> Color.YELLOW
                    DeviceInfo.SignalStrength.POOR -> Color.ORANGE
                    DeviceInfo.SignalStrength.VERY_POOR -> Color.RED
                }
            )
            
            // Highlight GSR+ devices
            if (device.isGSRPlusDevice) {
                deviceNameText.setTextColor(Color.BLUE)
                deviceNameText.text = "${device.name} ★"
            } else {
                deviceNameText.setTextColor(Color.BLACK)
            }
            
            // Show recommendation badge
            if (device.isRecommended) {
                deviceStatusText.text = "${device.statusSummary} • Recommended"
                deviceStatusText.setTextColor(Color.GREEN)
            } else {
                deviceStatusText.setTextColor(Color.GRAY)
            }
        }
    }
}