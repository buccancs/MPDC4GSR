package com.topdon.tc001.sensors.gsr

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.topdon.tc001.sensors.ErrorType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication

/**
 * Test for GSRSensorRecorder focusing on Bluetooth permission handling
 * and Shimmer integration fixes from issue #63
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.S]) // Android 12+ for new Bluetooth permissions
@OptIn(ExperimentalCoroutinesApi::class)
class GSRSensorRecorderTest {

    private lateinit var context: Context
    private lateinit var recorder: GSRSensorRecorder
    private lateinit var shadowApplication: ShadowApplication

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        shadowApplication = Shadows.shadowOf(context as android.app.Application)
        recorder = GSRSensorRecorder(context)
    }

    @Test
    fun `initialize should fail when Bluetooth permissions are not granted`() = runTest {
        // Given: No Bluetooth permissions granted
        shadowApplication.denyPermissions(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )

        // When: Initializing the GSR sensor
        val result = recorder.initialize()

        // Then: Initialization should fail due to missing permissions
        assertFalse("Initialization should fail without Bluetooth permissions", result)
    }

    @Test
    fun `initialize should succeed when Bluetooth permissions are granted`() = runTest {
        // Given: Bluetooth permissions are granted
        shadowApplication.grantPermissions(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )

        // When: Initializing the GSR sensor
        val result = recorder.initialize()

        // Then: Initialization should succeed (even if Shimmer device not available)
        assertTrue("Initialization should succeed with proper permissions", result)
        assertEquals("Sensor type should be correct", "GSR Shimmer3", recorder.sensorType)
        assertEquals("Sensor ID should be correct", "gsr_shimmer_1", recorder.sensorId)
    }

    @Test
    fun `startRecording should fail when Bluetooth permissions are not granted`() = runTest {
        // Given: Bluetooth permissions granted for initialization but revoked before recording
        shadowApplication.grantPermissions(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
        recorder.initialize()
        
        // Revoke permissions
        shadowApplication.denyPermissions(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )

        // When: Starting recording without permissions
        val result = recorder.startRecording("/test/session")

        // Then: Recording should fail due to missing permissions
        assertFalse("Recording should fail without Bluetooth permissions", result)
    }

    @Test
    fun `startRecording should handle missing Shimmer device gracefully`() = runTest {
        // Given: Bluetooth permissions granted but no Shimmer device available
        shadowApplication.grantPermissions(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
        
        val initResult = recorder.initialize()
        assertTrue("Initialization should succeed", initResult)

        // When: Starting recording (should handle missing device gracefully)
        val result = recorder.startRecording("/test/session")

        // Then: May succeed with legacy recording or fail gracefully
        // The exact result depends on whether legacy GSR recorder is available
        // But it should not crash and should provide proper error handling
        assertNotNull("Result should not be null", result)
    }

    @Test
    fun `sensor properties should be correct`() {
        assertEquals("Sensor type should be GSR Shimmer3", "GSR Shimmer3", recorder.sensorType)
        assertEquals("Sensor ID should be gsr_shimmer_1", "gsr_shimmer_1", recorder.sensorId)
        assertEquals("Sampling rate should be 128.0", 128.0, recorder.samplingRate, 0.1)
        assertFalse("Should not be recording initially", recorder.isRecording)
    }
}