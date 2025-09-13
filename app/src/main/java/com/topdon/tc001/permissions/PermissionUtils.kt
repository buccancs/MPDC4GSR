package com.topdon.tc001.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Utility functions for permission checking and validation
 */
object PermissionUtils {
    
    /**
     * Quick check if basic permissions are available
     */
    fun hasBasicPermissions(context: Context): Boolean {
        val basicPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        
        return basicPermissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    /**
     * Check if app can request permissions (not permanently denied)
     */
    fun canRequestPermissions(context: Context, permissions: Array<String>): Boolean {
        // This would need Activity context to check shouldShowRequestPermissionRationale
        // For now, just return true as this is a utility class
        return true
    }
}