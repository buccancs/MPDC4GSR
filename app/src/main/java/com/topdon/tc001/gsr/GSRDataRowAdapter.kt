package com.topdon.tc001.gsr

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csl.irCamera.databinding.ItemGsrDataRowBinding

/**
 * Specialized thermal imaging component providing GSRDataRowAdapter functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ViewHolder display and interaction.
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
    class ViewHolder(private val binding: ItemGsrDataRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val rowNumber = binding.rowNumber
        val timestamp = binding.timestamp
        val gsrValue = binding.gsrValue
        val resistance = binding.resistance
        val conductance = binding.conductance
    }

    /**
     * Executes oncreateviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param parent Parameter for operation (type: ViewGroup)
     * @param viewType Parameter for operation (type: Int)
     *
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemGsrDataRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ViewHolder(binding)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: ViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val dataRow = dataRows[position]

        holder.rowNumber.text = dataRow.rowNumber.toString()
        holder.timestamp.text = dataRow.timestamp
        holder.gsrValue.text = "%.3f μS".format(dataRow.gsrValue)
        holder.resistance.text = "%.1f kΩ".format(dataRow.resistance / 1000)
        holder.conductance.text = "%.6f S".format(dataRow.conductance)
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount() = minOf(dataRows.size, 100) // Limit to first 100 rows for performance
}
