package com.topdon.tc001.camera.core

import android.util.Log

/**
 * Specialized thermal imaging component providing ModeManager functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraMode functionality.
 *
 * Provides advanced camera functionality for thermal imaging capture,
 * including temperature measurement and pseudo color visualization.
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
    enum class CameraMode {
        RAW_50MP,
        VIDEO_4K,
        PREVIEW_ONLY,
    }

/**
 * Specialized thermal imaging component providing State functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    enum class State {
        IDLE,
        SWITCHING,
        RAW_ACTIVE,
        VIDEO_ACTIVE,
        PREVIEW_ACTIVE,
    }

    private var currentMode = CameraMode.PREVIEW_ONLY
    private var currentState = State.IDLE
    private var deviceCaps: DeviceCaps? = null

    // State change callbacks
    var onModeChanged: ((CameraMode, State) -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    /**
     * Initialize with device capabilities
     */
    fun initialize(caps: DeviceCaps) {
        deviceCaps = caps
        Log.i(TAG, "Mode manager initialized with device capabilities")
    }

    /**
     * Request mode switch with validation
     */
    fun requestModeSwitch(targetMode: CameraMode): Boolean {
        if (currentState == State.SWITCHING) {
            Log.w(TAG, "Mode switch already in progress")
            return false
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (currentMode == targetMode) {
            Log.i(TAG, "Already in target mode: $targetMode")
            return true
        }

        // Validate mode is supported
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isModeSupported(targetMode)) {
            val error = "Mode $targetMode not supported on this device"
            Log.e(TAG, error)
            onError?.invoke(error)
            return false
        }

        // Start switching
        currentState = State.SWITCHING
        val previousMode = currentMode
        currentMode = targetMode

        Log.i(TAG, "Mode switch: $previousMode -> $targetMode")
        onModeChanged?.invoke(currentMode, currentState)

        return true
    }

    /**
     * Confirm mode switch completed successfully
     */
    fun confirmModeSwitch() {
        if (currentState != State.SWITCHING) {
            Log.w(TAG, "No mode switch in progress to confirm")
            return
        }

        currentState =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (currentMode) {
                CameraMode.RAW_50MP -> State.RAW_ACTIVE
                CameraMode.VIDEO_4K -> State.VIDEO_ACTIVE
                CameraMode.PREVIEW_ONLY -> State.PREVIEW_ACTIVE
            }

        Log.i(TAG, "Mode switch confirmed: $currentMode active")
        onModeChanged?.invoke(currentMode, currentState)
    }

    /**
     * Report mode switch failed
     */
    /**
     * Executes reportmodeswitchfailed operation with thermal imaging domain optimization.
     *
     * @param
     * @param error Parameter for operation (type: String)
     *
     */
    fun reportModeSwitchFailed(error: String) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (currentState != State.SWITCHING) {
            Log.w(TAG, "No mode switch in progress to fail")
            return
        }

        Log.e(TAG, "Mode switch failed: $error")

        // Revert to previous state
        currentState = State.IDLE
        onError?.invoke("Mode switch failed: $error")
    }

    /**
     * Get current mode
     */
    /**
     * Retrieves the currentmode with optimized performance for thermal imaging operations.
     *
     */
    fun getCurrentMode(): CameraMode = currentMode

    /**
     * Get current state
     */
    /**
     * Retrieves the currentstate with optimized performance for thermal imaging operations.
     *
     */
    fun getCurrentState(): State = currentState

    /**
     * Check if mode is supported by device
     */
    fun isModeSupported(mode: CameraMode): Boolean {
        val caps = deviceCaps ?: return false

        return when (mode) {
            CameraMode.RAW_50MP -> caps.supportsRaw && caps.rawSize.width > 0
            CameraMode.VIDEO_4K -> true // Basic video always supported
            CameraMode.PREVIEW_ONLY -> true // Always supported
        }
    }

    /**
     * Get available modes for this device
     */
    fun getAvailableModes(): List<CameraMode> {
        val modes = mutableListOf(CameraMode.PREVIEW_ONLY, CameraMode.VIDEO_4K)

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isModeSupported(CameraMode.RAW_50MP)) {
            modes.add(CameraMode.RAW_50MP)
        }

        return modes
    }

    /**
     * Check if currently switching modes
     */
    fun isSwitching(): Boolean = currentState == State.SWITCHING

    /**
     * Check if a mode switch is allowed from current state
     */
    fun canSwitchMode(): Boolean {
        return currentState != State.SWITCHING
    }

    /**
     * Get recommended frame rate for current mode
     */
    fun getRecommendedFrameRate(): Int {
        val caps = deviceCaps ?: return 30

        return when (currentMode) {
            CameraMode.RAW_50MP -> 15 // Conservative for Samsung RAW
            CameraMode.VIDEO_4K -> if (caps.supports4k60) 60 else 30
            CameraMode.PREVIEW_ONLY -> 30
        }
    }
}
