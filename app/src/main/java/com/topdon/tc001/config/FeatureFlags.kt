package com.topdon.tc001.config

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * Specialized thermal imaging component providing FeatureFlags functionality for the IRCamera system.
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
object FeatureFlags {
    private const val TAG = "FeatureFlags"
    private const val PREFS_NAME = "pc_to_phone_features"

    // Feature flag keys
    private const val KEY_COMM_USE_WSS = "COMM_USE_WSS"
    private const val KEY_TLS_ENABLE = "TLS_ENABLE"
    private const val KEY_MDNS_ENABLE = "MDNS_ENABLE"
    private const val KEY_FILE_UPLOAD_PROTOCOL = "FILE_UPLOAD_PROTOCOL"
    private const val KEY_TIME_SYNC_MODE = "TIME_SYNC_MODE"

    // Default values - Phase 1: Enable WSS by default
    private const val DEFAULT_COMM_USE_WSS = true // Phase 1: Enable WebSocket Secure by default
    private const val DEFAULT_TLS_ENABLE = true
    private const val DEFAULT_MDNS_ENABLE = true
    private const val DEFAULT_FILE_UPLOAD_PROTOCOL = "tcp" // Options: tcp, http, websocket
    private const val DEFAULT_TIME_SYNC_MODE = "ntp" // Options: ntp, manual, disabled

    private var prefs: SharedPreferences? = null

    /**
     * Initialize feature flags system
     */
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        Log.i(TAG, "Feature flags initialized with defaults")
        /**
         * Executes logcurrentconfiguration operation with thermal imaging domain optimization.
         *
         */
        logCurrentConfiguration()
    }

    /**
     * WebSocket Secure communication flag
     * Phase 0: false (TCP), Phase 1: true (WSS)
     */
    val COMM_USE_WSS: Boolean
        get() = prefs?.getBoolean(KEY_COMM_USE_WSS, DEFAULT_COMM_USE_WSS) ?: DEFAULT_COMM_USE_WSS

    /**
     * TLS encryption enable flag
     */
    val TLS_ENABLE: Boolean
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = prefs?.getBoolean(KEY_TLS_ENABLE, DEFAULT_TLS_ENABLE) ?: DEFAULT_TLS_ENABLE

    /**
     * mDNS/Zeroconf discovery enable flag
     */
    val MDNS_ENABLE: Boolean
        get() = prefs?.getBoolean(KEY_MDNS_ENABLE, DEFAULT_MDNS_ENABLE) ?: DEFAULT_MDNS_ENABLE

    /**
     * File upload protocol selection
     * Options: "tcp", "http", "websocket"
     */
    val FILE_UPLOAD_PROTOCOL: String
        get() = prefs?.getString(KEY_FILE_UPLOAD_PROTOCOL, DEFAULT_FILE_UPLOAD_PROTOCOL) ?: DEFAULT_FILE_UPLOAD_PROTOCOL

    /**
     * Time synchronization mode
     * Options: "ntp", "manual", "disabled"
     */
    val TIME_SYNC_MODE: String
        get() = prefs?.getString(KEY_TIME_SYNC_MODE, DEFAULT_TIME_SYNC_MODE) ?: DEFAULT_TIME_SYNC_MODE

    /**
     * Set feature flag values (for testing and dynamic configuration)
     */
    fun setCommUseWSS(enabled: Boolean) {
        prefs?.edit()?.putBoolean(KEY_COMM_USE_WSS, enabled)?.apply()
        Log.i(TAG, "COMM_USE_WSS set to $enabled")
    }

    /**
     * Sets tlsenable configuration.
     */
    fun setTlsEnable(enabled: Boolean) {
        prefs?.edit()?.putBoolean(KEY_TLS_ENABLE, enabled)?.apply()
        Log.i(TAG, "TLS_ENABLE set to $enabled")
    }

    /**
     * Sets mdnsenable configuration.
     */
    fun setMdnsEnable(enabled: Boolean) {
        prefs?.edit()?.putBoolean(KEY_MDNS_ENABLE, enabled)?.apply()
        Log.i(TAG, "MDNS_ENABLE set to $enabled")
    }

    /**
     * Sets fileuploadprotocol configuration.
     */
    fun setFileUploadProtocol(protocol: String) {
        prefs?.edit()?.putString(KEY_FILE_UPLOAD_PROTOCOL, protocol)?.apply()
        Log.i(TAG, "FILE_UPLOAD_PROTOCOL set to $protocol")
    }

    /**
     * Sets timesyncmode configuration.
     */
    fun setTimeSyncMode(mode: String) {
        prefs?.edit()?.putString(KEY_TIME_SYNC_MODE, mode)?.apply()
        Log.i(TAG, "TIME_SYNC_MODE set to $mode")
    }

    /**
     * Get all feature flags as a map
     */
    fun getAllFlags(): Map<String, Any> {
        return mapOf(
            KEY_COMM_USE_WSS to COMM_USE_WSS,
            KEY_TLS_ENABLE to TLS_ENABLE,
            KEY_MDNS_ENABLE to MDNS_ENABLE,
            KEY_FILE_UPLOAD_PROTOCOL to FILE_UPLOAD_PROTOCOL,
            KEY_TIME_SYNC_MODE to TIME_SYNC_MODE,
        )
    }

    /**
     * Reset all flags to defaults
     */
    fun resetToDefaults() {
        prefs?.edit()?.clear()?.apply()
        Log.i(TAG, "Feature flags reset to defaults")
        /**
         * Executes logcurrentconfiguration operation with thermal imaging domain optimization.
         *
         */
        logCurrentConfiguration()
    }

    /**
     * Log current configuration for debugging
     */
    private fun logCurrentConfiguration() {
        val flags = getAllFlags()
        Log.i(TAG, "Current feature flag configuration:")
        flags.forEach { (key, value) ->
            Log.i(TAG, "  $key: $value")
        }
    }

    /**
     * Validate configuration consistency
     */
    fun validateConfiguration(): List<String> {
        val warnings = mutableListOf<String>()

        // WSS requires TLS
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (COMM_USE_WSS && !TLS_ENABLE) {
            warnings.add("COMM_USE_WSS=true but TLS_ENABLE=false - WebSocket Secure requires TLS")
        }

        // Validate protocol options
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (FILE_UPLOAD_PROTOCOL !in listOf("tcp", "http", "websocket")) {
            warnings.add("Invalid FILE_UPLOAD_PROTOCOL: $FILE_UPLOAD_PROTOCOL")
        }

        // Validate time sync options
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (TIME_SYNC_MODE !in listOf("ntp", "manual", "disabled")) {
            warnings.add("Invalid TIME_SYNC_MODE: $TIME_SYNC_MODE")
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (warnings.isNotEmpty()) {
            warnings.forEach { warning ->
                Log.w(TAG, "Configuration warning: $warning")
            }
        }

        return warnings
    }
}
