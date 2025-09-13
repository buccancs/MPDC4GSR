package com.topdon.tc001.camera.core

import android.graphics.SurfaceTexture
import android.util.Log
import android.view.Surface
import android.view.TextureView

/**
 * Specialized thermal imaging component providing UiBridge functionality for the IRCamera system.
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
class UiBridge(private val textureView: TextureView) {
    companion object {
        private const val TAG = "UiBridge"
    }

    private var previewSurface: Surface? = null
    private var isTextureAvailable = false

    // Callbacks to UI
    var onError: ((String) -> Unit)? = null
    var onProgress: ((String) -> Unit)? = null
    var onModeChanged: ((String) -> Unit)? = null

    init {
        /**
         * Configures the uptextureview with validation and thermal imaging optimization.
         *
         */
        setupTextureView()
    }

    /**
     * Get preview surface for camera session
     */
    fun getPreviewSurface(): Surface? = previewSurface

    /**
     * Check if texture is ready
     */
    /**
     * Executes istextureready operation with thermal imaging domain optimization.
     *
     */
    fun isTextureReady(): Boolean = isTextureAvailable

    /**
     * Update UI with mode change
     */
    /**
     * Executes updatemode operation with thermal imaging domain optimization.
     *
     * @param
     * @param mode Parameter for operation (type: String)
     *
     */
    fun updateMode(mode: String) {
        Log.i(TAG, "Mode updated: $mode")
        onModeChanged?.invoke(mode)
    }

    /**
     * Report error to UI
     */
    /**
     * Executes reporterror operation with thermal imaging domain optimization.
     *
     * @param
     * @param error Parameter for operation (type: String)
     *
     */
    fun reportError(error: String) {
        Log.e(TAG, "Error: $error")
        onError?.invoke(error)
    }

    /**
     * Report progress to UI
     */
    /**
     * Executes reportprogress operation with thermal imaging domain optimization.
     *
     * @param
     * @param message Parameter for operation (type: String)
     *
     */
    fun reportProgress(message: String) {
        Log.i(TAG, "Progress: $message")
        onProgress?.invoke(message)
    }

    /**
     * Update preview size
     */
    /**
     * Executes updatepreviewsize operation with thermal imaging domain optimization.
     *
     * @param
     * @param width Parameter for operation (type: Int)
     * @param height Parameter for operation (type: Int)
     *
     */
    fun updatePreviewSize(
        width: Int,
        height: Int,
    ) {
        textureView.surfaceTexture?.setDefaultBufferSize(width, height)
        Log.d(TAG, "Preview size updated: ${width}x$height")
    }

    /**
     * Release resources
     */
    /**
     * Executes release operation with thermal imaging domain optimization.
     *
     */
    fun release() {
        previewSurface?.release()
        previewSurface = null
        isTextureAvailable = false
        Log.d(TAG, "UiBridge released")
    }

    /**
     * Sets uptextureview configuration.
     */
    private fun setupTextureView() {
        textureView.surfaceTextureListener =
            object : TextureView.SurfaceTextureListener {
                /**
                 * Executes onsurfacetextureavailable operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param texture Parameter for operation (type: SurfaceTexture)
                 * @param width Parameter for operation (type: Int)
                 * @param height Parameter for operation (type: Int)
                 *
                 */
                override fun onSurfaceTextureAvailable(
                    texture: SurfaceTexture,
                    width: Int,
                    height: Int,
                ) {
                    previewSurface?.release()
                    previewSurface = Surface(texture)
                    isTextureAvailable = true

                    Log.i(TAG, "TextureView surface available: ${width}x$height")
                    /**
                     * Executes reportprogress operation with thermal imaging domain optimization.
                     *
                     */
                    reportProgress("Preview surface ready")
                }

                /**
                 * Executes onsurfacetexturesizechanged operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param texture Parameter for operation (type: SurfaceTexture)
                 * @param width Parameter for operation (type: Int)
                 * @param height Parameter for operation (type: Int)
                 *
                 */
                override fun onSurfaceTextureSizeChanged(
                    texture: SurfaceTexture,
                    width: Int,
                    height: Int,
                ) {
                    Log.d(TAG, "TextureView size changed: ${width}x$height")
                }

                /**
                 * Executes onsurfacetexturedestroyed operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param texture Parameter for operation (type: SurfaceTexture)
                 *
                 */
                override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
                    previewSurface?.release()
                    previewSurface = null
                    isTextureAvailable = false

                    Log.i(TAG, "TextureView surface destroyed")
                    return true
                }

                /**
                 * Executes onsurfacetextureupdated operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param texture Parameter for operation (type: SurfaceTexture)
                 *
                 */
                override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {
                    // Frame updated - high frequency, no logging
                }
            }
    }
}
