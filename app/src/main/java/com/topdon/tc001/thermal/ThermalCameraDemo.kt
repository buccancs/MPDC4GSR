package com.topdon.tc001.thermal

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.topdon.tc001.R
import com.topdon.tc001.sensors.thermal.ThermalCameraRecorder
import kotlinx.coroutines.launch
import java.io.File

/**
 * Demo activity to showcase the USB permission flow and thermal camera integration
 * for the Topdon TC001 thermal camera.
 */
class ThermalCameraDemo : AppCompatActivity() {

    companion object {
        private const val TAG = "ThermalCameraDemo"
    }

    private lateinit var thermalRecorder: ThermalCameraRecorder
    private lateinit var statusText: TextView
    private lateinit var thermalPreview: ImageView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            Log.i(TAG, "All permissions granted")
            initializeThermalCamera()
        } else {
            Log.w(TAG, "Permissions denied")
            updateStatus("Permissions required for thermal camera operation")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Note: You would need to create activity_thermal_demo.xml layout
        // setContentView(R.layout.activity_thermal_demo)

        initializeThermalCamera() // Direct initialization for demo
    }

    private fun initializeThermalCamera() {
        lifecycleScope.launch {
            try {
                updateStatus("Initializing thermal camera...")

                thermalRecorder = ThermalCameraRecorder(this@ThermalCameraDemo)

                // Set up thermal preview callback
                thermalRecorder.setThermalPreviewCallback(object :
                    ThermalCameraRecorder.ThermalPreviewCallback {
                    override fun onThermalFrame(
                        bitmap: Bitmap?,
                        temperatureData: Any?
                    ) {
                        Log.d(TAG, "Thermal frame received: ${bitmap?.width}x${bitmap?.height}")
                    }
                })

                val success = thermalRecorder.initialize()

                if (success) {
                    updateStatus("Thermal camera ready. Plug in TC001 for hardware mode or use simulation.")

                    // Monitor status and errors
                    lifecycleScope.launch {
                        thermalRecorder.getStatusFlow().collect { status ->
                            updateStatus("${status.sensorType}: ${if (status.isRecording) "Recording" else "Idle"} - Frames: ${status.samplesRecorded}")
                        }
                    }

                    lifecycleScope.launch {
                        thermalRecorder.getErrorFlow().collect { error ->
                            updateStatus("Error: ${error.errorMessage}")
                        }
                    }

                } else {
                    updateStatus("Failed to initialize thermal camera")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize thermal camera", e)
                updateStatus("Initialization failed: ${e.message}")
            }
        }
    }

    private fun updateStatus(message: String) {
        Log.i(TAG, message)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            if (::thermalRecorder.isInitialized) {
                thermalRecorder.cleanup()
            }
        }
    }
}