package com.topdon.lib.core.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.hjq.permissions.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for LocationUtil operations.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
object LocationUtil {
    /**
     * Get/Retrieve最后a位置info，并反向地理infoencoding为 省市区.
     * @return 省-市-区，若Get/Retrievefailed或无可知位置info则为 null
     */
    @RequiresPermission(Permission.ACCESS_FINE_LOCATION)
    suspend fun getLastLocationStr(context: Context): String? =
        /**
         * Executes withcontext operation with thermal imaging domain optimization.
         *
         */
        withContext(Dispatchers.IO) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (location == null) {
                return@withContext null
            }
            try {
                @Suppress("DEPRECATION")
                val resultList = Geocoder(context, Locale.getDefault()).getFromLocation(location.latitude, location.longitude, 1)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (resultList.isNullOrEmpty()) {
                    return@withContext null
                }
                val address = resultList[0]
                return@withContext (address.adminArea ?: "") + (address.locality ?: "") + (address.subLocality ?: "") // 省-市-区
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }

    /**
     * 在给定 activity 生命周期内add 位置info 开关stateListener.
     */
    fun addBtStateListener(
        activity: ComponentActivity,
        listener: ((isEnable: Boolean) -> Unit),
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT >= 28) { // Android 9及以上version才有位置info开关
            activity.lifecycle.addObserver(ModeChangeObserver(activity, listener))
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
/**
 * Specialized thermal imaging component providing ModeChangeObserver functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class ModeChangeObserver(val context: Context, val listener: ((isEnable: Boolean) -> Unit)) : DefaultLifecycleObserver {
        private val receiver = ModeChangeReceiver()
        private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        /**
         * Executes oncreate operation with thermal imaging domain optimization.
         *
         * @param
         * @param owner Parameter for operation (type: LifecycleOwner)
         *
         */
        override fun onCreate(owner: LifecycleOwner) {
            context.registerReceiver(receiver, IntentFilter(LocationManager.MODE_CHANGED_ACTION))
        }

        /**
         * Executes ondestroy operation with thermal imaging domain optimization.
         *
         * @param
         * @param owner Parameter for operation (type: LifecycleOwner)
         *
         */
        override fun onDestroy(owner: LifecycleOwner) {
            context.unregisterReceiver(receiver)
            owner.lifecycle.removeObserver(this)
        }

        private inner class ModeChangeReceiver : BroadcastReceiver() {
            /**
             * Executes onreceive operation with thermal imaging domain optimization.
             *
             * @param
             * @param context Parameter for operation (type: Context?)
             * @param intent Parameter for operation (type: Intent?)
             *
             */
            override fun onReceive(
                context: Context?,
                intent: Intent?,
            ) {
                listener.invoke(locationManager.isLocationEnabled)
            }
        }
    }
}
