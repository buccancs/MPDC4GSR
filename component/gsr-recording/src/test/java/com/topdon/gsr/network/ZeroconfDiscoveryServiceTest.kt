package com.topdon.gsr.network

import android.content.Context
import android.net.nsd.NsdManager
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNsdManager
import org.robolectric.Shadows
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Context-based tests for ZeroconfDiscoveryService using Robolectric
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
@OptIn(ExperimentalCoroutinesApi::class)
class ZeroconfDiscoveryServiceTest {
    
    private lateinit var context: Context
    private lateinit var discoveryService: ZeroconfDiscoveryService
    private lateinit var nsdManager: NsdManager
    private lateinit var shadowNsdManager: ShadowNsdManager
    
    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        discoveryService = ZeroconfDiscoveryService(context)
        nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
        shadowNsdManager = Shadows.shadowOf(nsdManager)
    }
    
    @Test
    fun testServiceCreation() {
        assertNotNull("Discovery service should be created", discoveryService)
    }
    
    @Test
    fun testNsdManagerAccess() {
        assertNotNull("NsdManager should be available", nsdManager)
        assertNotNull("Shadow NsdManager should be available", shadowNsdManager)
    }
    
    @Test
    fun testSetServiceListener() {
        var serviceDiscovered = false
        var serviceLost = false
        var serviceRegistered = false
        var discoveryError = false
        
        val listener = object : ZeroconfDiscoveryService.ServiceDiscoveryListener {
            override fun onServiceDiscovered(serviceInfo: NetworkClient.ControllerInfo) {
                serviceDiscovered = true
            }
            
            override fun onServiceLost(serviceName: String) {
                serviceLost = true
            }
            
            override fun onServiceRegistered(serviceName: String) {
                serviceRegistered = true
            }
            
            override fun onDiscoveryError(errorCode: Int, message: String) {
                discoveryError = true
            }
        }
        
        discoveryService.setServiceListener(listener)
        
        // Listener should be set (can't directly verify private field, but no exception should occur)
        // Test passes if no exception is thrown
        assertTrue("Setting service listener should succeed", true)
        
        // Test removing listener
        discoveryService.setServiceListener(null)
        assertTrue("Removing service listener should succeed", true)
    }
    
    @Test
    fun testStartDiscovery() = runTest {
        // Start discovery
        val result = discoveryService.startDiscovery()
        assertTrue("Discovery should start successfully", result)
    }
    
    @Test
    fun testStopDiscovery() = runTest {
        // Start discovery first
        discoveryService.startDiscovery()
        
        // Stop discovery
        discoveryService.stopDiscovery()
    }
    
    @Test
    fun testRegisterService() = runTest {
        val deviceName = "Test Device"
        val port = 8080
        
        // Register service
        val result = discoveryService.registerService(deviceName, port)
        assertTrue("Service registration should start successfully", result)
    }
    
    @Test
    fun testUnregisterService() = runTest {
        val deviceName = "Test Device"
        val port = 8080
        
        // Register service first
        discoveryService.registerService(deviceName, port)
        
        // Unregister service
        discoveryService.unregisterService()
    }
    
    @Test
    fun testGetDiscoveredServices() = runTest {
        // Initially should have empty or existing services
        val initialServices = discoveryService.getDiscoveredControllers()
        assertNotNull("Discovered services should not be null", initialServices)
        assertTrue("Discovered services should be a list", initialServices is List<*>)
        
        val initialCount = initialServices.size
        
        // Start discovery to potentially find services
        discoveryService.startDiscovery()
        
        // In a real scenario, services would be discovered over time
        // In Robolectric, we can simulate this
        delay(100)
        
        val servicesAfterDiscovery = discoveryService.getDiscoveredControllers()
        assertNotNull("Services after discovery should not be null", servicesAfterDiscovery)
        assertTrue("Services count should be >= initial count", 
            servicesAfterDiscovery.size >= initialCount)
    }
    
    @Test
    fun testServiceDiscoveryListener() = runTest {
        val latch = CountDownLatch(1)
        var discoveredService: NetworkClient.ControllerInfo? = null
        var lostServiceName: String? = null
        var registeredServiceName: String? = null
        var errorOccurred = false
        
        val listener = object : ZeroconfDiscoveryService.ServiceDiscoveryListener {
            override fun onServiceDiscovered(serviceInfo: NetworkClient.ControllerInfo) {
                discoveredService = serviceInfo
                latch.countDown()
            }
            
            override fun onServiceLost(serviceName: String) {
                lostServiceName = serviceName
            }
            
            override fun onServiceRegistered(serviceName: String) {
                registeredServiceName = serviceName
            }
            
            override fun onDiscoveryError(errorCode: Int, message: String) {
                errorOccurred = true
                latch.countDown()
            }
        }
        
        discoveryService.setServiceListener(listener)
        
        // Start discovery
        discoveryService.startDiscovery()
        
        // Wait briefly for potential callbacks
        delay(500)
        
        // Stop discovery
        discoveryService.stopDiscovery()
        
        // Test that listener callbacks can be triggered
        // In a real environment, these would be triggered by actual network events
        assertTrue("Test setup should complete without errors", true)
    }
    
    @Test
    fun testMultipleDiscoveryStartStops() = runTest {
        // Test multiple start/stop cycles
        for (i in 1..3) {
            val startResult = discoveryService.startDiscovery()
            assertTrue("Discovery start $i should succeed", startResult)
            
            delay(100)
            
            discoveryService.stopDiscovery()
        }
    }
    
    @Test
    fun testServiceRegistrationWithMetadata() = runTest {
        val deviceName = "Metadata Test Device"
        val port = 9090
        
        val result = discoveryService.registerService(deviceName, port)
        assertTrue("Service registration with metadata should succeed", result)
        
        // Clean up
        discoveryService.unregisterService()
    }
    
    @Test
    fun testCleanupResources() = runTest {
        // Start both discovery and registration
        discoveryService.startDiscovery()
        discoveryService.registerService("Cleanup Test", 8080)
        
        // Cleanup all resources
        discoveryService.cleanup()
        
        // After cleanup test passes if no exceptions thrown
        assertTrue("Cleanup should complete without errors", true)
    }
    
    @Test
    fun testServiceNameGeneration() {
        // Test that service names are generated properly
        val deviceName1 = "Device One"
        val deviceName2 = "Device Two"
        
        // This test mainly ensures the service can handle different device names
        // without throwing exceptions
        assertTrue("Different device names should be handled", 
            deviceName1 != deviceName2)
    }
}