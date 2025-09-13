package com.topdon.tc001.camera

import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.topdon.tc001.camera.core.ModeManager
import kotlinx.coroutines.*

/**
 * Specialized thermal imaging component providing DemoActivity functionality for the IRCamera system.
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
class DemoActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "DemoActivity"
    }

    private lateinit var textureView: TextureView
    private lateinit var camera2System: Camera2System
    private lateinit var buttonRaw: Button
    private lateinit var buttonVideo: Button
    private lateinit var buttonPreview: Button
    private lateinit var buttonRecord: Button

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create simple layout programmatically for demo
        textureView = TextureView(this)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(textureView)

        // Initialize clean Camera2 system
        camera2System = Camera2System(this, textureView)
        /**
         * Configures the upcallbacks with validation and thermal imaging optimization.
         *
         */
        setupCallbacks()

        // Initialize camera
        lifecycleScope.launch {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (camera2System.initialize()) {
                Log.i(TAG, "Camera2System initialized successfully")
                val caps = camera2System.getDeviceCaps()
                Log.i(TAG, "Device capabilities: RAW=${caps?.supportsRaw}, 4K60=${caps?.supports4k60}")
            } else {
                Log.e(TAG, "Failed to initialize Camera2System")
            }
        }
    }

    /**
     * Sets upcallbacks configuration.
     */
    private fun setupCallbacks() {
        camera2System.onError = { error ->
            Log.e(TAG, "Camera error: $error")
            runOnUiThread {
                Toast.makeText(this, "Camera error: $error", Toast.LENGTH_SHORT).show()
            }
        }

        camera2System.onProgress = { message ->
            Log.i(TAG, "Progress: $message")
        }

        camera2System.onModeChanged = { mode ->
            Log.i(TAG, "Mode changed to: $mode")
            runOnUiThread {
                Toast.makeText(this, "Mode: $mode", Toast.LENGTH_SHORT).show()
            }
        }

        camera2System.onRecordingStarted = {
            Log.i(TAG, "Recording started")
            runOnUiThread {
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
            }
        }

        camera2System.onRecordingStopped = {
            Log.i(TAG, "Recording stopped")
            runOnUiThread {
                Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Demo method to test mode switching
    /**
     * Executes testModeSwitch functionality.
     */
    /**
     * Executes testmodeswitch operation with thermal imaging domain optimization.
     *
     */
    private fun testModeSwitch() {
        lifecycleScope.launch {
            // Test RAW mode
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (camera2System.switchMode(ModeManager.CameraMode.RAW_50MP)) {
                Log.i(TAG, "Switched to RAW mode")
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(2000)

                // Test recording
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (camera2System.startRecording("demo_session_raw")) {
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(5000) // Record for 5 seconds
                    camera2System.stopRecording()
                }
            }

            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(1000)

            // Test Video mode
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (camera2System.switchMode(ModeManager.CameraMode.VIDEO_4K)) {
                Log.i(TAG, "Switched to Video mode")
                /**
                 * Executes delay operation with thermal imaging domain optimization.
                 *
                 */
                delay(2000)

                // Test recording
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (camera2System.startRecording("demo_session_video")) {
                    /**
                     * Executes delay operation with thermal imaging domain optimization.
                     *
                     */
                    delay(5000) // Record for 5 seconds
                    camera2System.stopRecording()
                }
            }
        }
    }

    /**
     * Executes onresume operation with thermal imaging domain optimization.
     *
     */
    override fun onResume() {
        super.onResume()
        // Start demo after a delay
        lifecycleScope.launch {
            /**
             * Executes delay operation with thermal imaging domain optimization.
             *
             */
            delay(3000) // Wait for camera to initialize
            /**
             * Executes testmodeswitch operation with thermal imaging domain optimization.
             *
             */
            testModeSwitch()
        }
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        camera2System.release()
    }
}
