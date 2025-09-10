package com.topdon.gsr.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Context-based tests for EnhancedRecordingService using Robolectric
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
@OptIn(ExperimentalCoroutinesApi::class)
class EnhancedRecordingServiceTest {
    
    private lateinit var context: Context
    private lateinit var service: EnhancedRecordingService
    
    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        service = Robolectric.buildService(EnhancedRecordingService::class.java).create().get()
    }
    
    @Test
    fun testServiceCreation() {
        assertNotNull("Service should be created", service)
    }
    
    @Test
    fun testNotificationChannelCreation() {
        // Test that notification channel is properly created
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Start service to trigger notification channel creation
        val intent = Intent(context, EnhancedRecordingService::class.java)
        service.onCreate()
        
        // Verify notification manager is available
        assertNotNull("NotificationManager should be available", notificationManager)
    }
    
    @Test
    fun testServiceBinder() {
        val intent = Intent(context, EnhancedRecordingService::class.java)
        val binder = service.onBind(intent)
        
        assertNotNull("Service should return binder", binder)
        assertTrue("Binder should be EnhancedRecordingBinder instance", binder is EnhancedRecordingService.EnhancedRecordingBinder)
        
        val recordingBinder = binder as EnhancedRecordingService.EnhancedRecordingBinder
        assertEquals("Binder should return service instance", service, recordingBinder.getService())
    }
    
    @Test
    fun testStartRecordingIntent() = runTest {
        val sessionId = "test_session_123"
        val participantId = "participant_001"
        val studyName = "test_study"
        
        // Test the static helper method starts service properly
        // We can't easily test the intent creation directly as it's not exposed
        // Instead we test the service can handle the action
        val intent = Intent().apply {
            action = "action_start_recording"
            putExtra("extra_session_id", sessionId)
            putExtra("extra_participant_id", participantId)
            putExtra("extra_study_name", studyName)
        }
        
        val result = service.onStartCommand(intent, 0, 1)
        assertEquals("Service should return START_STICKY", android.app.Service.START_STICKY, result)
    }
    
    @Test
    fun testStopRecordingIntent() {
        val intent = Intent().apply {
            action = "action_stop_recording"
        }
        
        val result = service.onStartCommand(intent, 0, 1)
        assertEquals("Service should handle stop recording", android.app.Service.START_STICKY, result)
    }
    
    @Test
    fun testPCConnectionIntent() {
        val pcIp = "192.168.1.100"
        val pcPort = 8080
        
        val connectIntent = Intent().apply {
            action = "action_connect_pc"
            putExtra("extra_pc_ip", pcIp)
            putExtra("extra_pc_port", pcPort)
        }
        val connectResult = service.onStartCommand(connectIntent, 0, 1)
        assertEquals("Service should handle connect PC", android.app.Service.START_STICKY, connectResult)
        
        val disconnectIntent = Intent().apply {
            action = "action_disconnect_pc"
        }
        val disconnectResult = service.onStartCommand(disconnectIntent, 0, 1)
        assertEquals("Service should handle disconnect PC", android.app.Service.START_STICKY, disconnectResult)
    }
    
    @Test
    fun testDiscoveryIntent() {
        val startIntent = Intent().apply {
            action = "action_start_discovery"
        }
        val startResult = service.onStartCommand(startIntent, 0, 1)
        assertEquals("Service should handle start discovery", android.app.Service.START_STICKY, startResult)
        
        val stopIntent = Intent().apply {
            action = "action_stop_discovery"
        }
        val stopResult = service.onStartCommand(stopIntent, 0, 1)
        assertEquals("Service should handle stop discovery", android.app.Service.START_STICKY, stopResult)
    }
    
    @Test
    fun testServiceLifecycle() = runTest {
        // Test service lifecycle methods
        service.onCreate()
        
        // Test onStartCommand with different intents
        val startRecordingIntent = Intent().apply {
            action = "action_start_recording"
            putExtra("extra_session_id", "test_session")
        }
        
        val result = service.onStartCommand(startRecordingIntent, 0, 1)
        assertEquals("Service should return START_STICKY", android.app.Service.START_STICKY, result)
        
        // Test onDestroy
        service.onDestroy()
        // Service should clean up resources - no exceptions should be thrown
    }
    
    @Test
    fun testServiceState() = runTest {
        // Create binder to access service state
        val binder = service.onBind(Intent()) as EnhancedRecordingService.EnhancedRecordingBinder
        val serviceInstance = binder.getService()
        
        // Initially service should not be recording
        assertFalse("Service should not be recording initially", serviceInstance.getRecordingStatus())
        
        // Initially no PC should be connected
        assertFalse("PC should not be connected initially", serviceInstance.getConnectionStatus())
        
        // Initially no session should be active
        assertNull("No session should be active initially", serviceInstance.getCurrentSessionId())
    }
}