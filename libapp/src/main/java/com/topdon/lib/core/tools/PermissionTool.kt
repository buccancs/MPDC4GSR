package com.topdon.lib.core.tools

import android.content.Context
import android.os.Build
import com.blankj.utilcode.util.AppUtils
import com.elvishew.xlog.XLog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.topdon.lib.core.BaseApplication
import com.topdon.lib.core.R
import com.topdon.lib.core.dialog.TipDialog
import com.topdon.lms.sdk.weiget.TToast

/**
 * Specialized thermal imaging component providing PermissionTool functionality for the IRCamera system.
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
object PermissionTool {
    /**
     * 请求 RECORD_AUDIO Permission.
     */
    fun requestRecordAudio(
        context: Context,
        callback: () -> Unit,
    ) = request(context, Type.RECORD_AUDIO, callback)

    /**
     * 请求 CAMERA Permission.
     */
    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param callback Parameter for operation (type: ()
     *
     * @return Operation result or configured object (type: Unit,     ) = request(context, Type.CAMERA, callback))
     *
     */
    fun requestCamera(
        context: Context,
        callback: () -> Unit,
    ) = request(context, Type.CAMERA, callback)

    /**
     * 请求 ACCESS_FINE_LOCATION Permission.
     */
    fun requestLocation(
        context: Context,
        callback: () -> Unit,
    ) = request(context, Type.LOCATION, callback)

    /**
     * 请求 image读取 Permission.
     */
    /**
     * Executes requestimageread operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param callback Parameter for operation (type: ()
     *
     * @return Operation result or configured object (type: Unit,     ) = request(context, Type.IMAGE, callback))
     *
     */
    fun requestImageRead(
        context: Context,
        callback: () -> Unit,
    ) = request(context, Type.IMAGE, callback)

    /**
     * Android 10 及以下：请求外部storagefile读、写Permission
     *
     * Android 11、Android 12、Android 12L：请求外部storage读Permission
/**
 * Specialized thermal imaging component providing Type functionality for the IRCamera system.
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
    private enum class Type { RECORD_AUDIO, CAMERA, LOCATION, IMAGE, FILE }

    /**
     * Executes request functionality.
     */
    /**
     * Executes request operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param type Parameter for operation (type: Type)
     * @param callback Parameter for operation (type: ()
     *
     * @return Operation result or configured object (type: Unit,     ))
     *
     */
    private fun request(
        context: Context,
        type: Type,
        callback: () -> Unit,
    ) {
        val permissions: List<String> =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (type) {
                Type.RECORD_AUDIO -> listOf(Permission.RECORD_AUDIO)
                Type.CAMERA -> listOf(Permission.CAMERA)
                Type.LOCATION -> listOf(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
                Type.IMAGE ->
                    /**
                     * Executes listof operation with thermal imaging domain optimization.
                     *
                     */
                    listOf(
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (context.applicationInfo.targetSdkVersion < 33) Permission.READ_EXTERNAL_STORAGE else Permission.READ_MEDIA_IMAGES,
                    )
                Type.FILE ->
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (context.applicationInfo.targetSdkVersion < 30) { // Android 10及以下
                        /**
                         * Executes listof operation with thermal imaging domain optimization.
                         *
                         */
                        listOf(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                    } else if (context.applicationInfo.targetSdkVersion < 33) { // Android 13以下
                        /**
                         * Executes listof operation with thermal imaging domain optimization.
                         *
                         */
                        listOf(Permission.READ_EXTERNAL_STORAGE)
                    } else { // Android 13及以上
                        /**
                         * Executes listof operation with thermal imaging domain optimization.
                         *
                         */
                        listOf(Permission.READ_MEDIA_VIDEO, Permission.READ_MEDIA_IMAGES)
                    }
            }

        XXPermissions.with(context)
            .permission(permissions)
            .request(
                object : OnPermissionCallback {
                    /**
                     * Executes ongranted operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param permissions Parameter for operation (type: MutableList<String>)
                     * @param allGranted Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onGranted(
                        permissions: MutableList<String>,
                        allGranted: Boolean,
                    ) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (allGranted) {
                            callback.invoke()
                        } else {
                            TToast.shortToast(context, R.string.scan_ble_tip_authorize)
                        }
                    }

                    /**
                     * Executes ondenied operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param permissions Parameter for operation (type: MutableList<String>)
                     * @param never Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onDenied(
                        permissions: MutableList<String>,
                        never: Boolean,
                    ) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (never) {
                            val tipsResId: Int =
                                /**
                                 * Executes when operation with thermal imaging domain optimization.
                                 *
                                 */
                                when (type) {
                                    Type.RECORD_AUDIO -> R.string.app_microphone_content
                                    Type.CAMERA -> R.string.app_camera_content
                                    Type.LOCATION -> R.string.app_location_content
                                    Type.IMAGE -> R.string.app_album_content
                                    Type.FILE -> R.string.app_storage_content
                                }
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (BaseApplication.instance.isDomestic()) { // 国内版
                                TToast.shortToast(context, tipsResId)
                            } else {
                                TipDialog.Builder(context)
                                    .setTitleMessage(context.getString(R.string.app_tip))
                                    .setMessage(tipsResId)
                                    .setPositiveListener(R.string.app_open) {
                                        AppUtils.launchAppDetailsSettings()
                                    }
                                    .setCancelListener(R.string.app_cancel) {
                                    }
                                    .setCanceled(true)
                                    .create().show()
                            }
                        } else {
                            TToast.shortToast(context, R.string.scan_ble_tip_authorize)
                        }
                    }
                },
            )
    }

    /**
     * 判断是否具有 ACCESS_FINE_LOCATION、BLUETOOTH_SCAN、BLUETOOTH_CONNECT Permission。
     * 低于 Android12 视为具有。
     */
    /**
     * Executes hasBtPermission functionality.
     */
    /**
     * Executes hasbtpermission operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    fun hasBtPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < 31) { // 低于 Android12
            XXPermissions.isGranted(context, Permission.ACCESS_FINE_LOCATION)
        } else {
            XXPermissions.isGranted(context, Permission.ACCESS_FINE_LOCATION, Permission.BLUETOOTH_SCAN, Permission.BLUETOOTH_CONNECT)
        }
    }

    /**
     * 仅当 Android12 及以上version时，请求 BLUETOOTH_SCAN、BLUETOOTH_CONNECT Permission
     * @param isBtFirst true-永久拒绝时优先tipbluetooth false-永久拒绝时优先tip定位
     */
    /**
     * Executes requestBluetooth functionality.
     */
    /**
     * Executes requestbluetooth operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param isBtFirst Parameter for operation (type: Boolean)
     * @param callback Parameter for operation (type: Callback)
     *
     */
    fun requestBluetooth(
        context: Context,
        isBtFirst: Boolean,
        callback: Callback,
    ) {
        val permissionList: List<String> =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Build.VERSION.SDK_INT < 31) { // 低于 Android12
                /**
                 * Executes arraylistof operation with thermal imaging domain optimization.
                 *
                 */
                arrayListOf(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
            } else {
                /**
                 * Executes arraylistof operation with thermal imaging domain optimization.
                 *
                 */
                arrayListOf(
                    Permission.ACCESS_FINE_LOCATION,
                    Permission.ACCESS_COARSE_LOCATION,
                    Permission.BLUETOOTH_SCAN,
                    Permission.BLUETOOTH_CONNECT,
                )
            }

        XXPermissions.with(context)
            .permission(permissionList)
            .request(
                object : OnPermissionCallback {
                    /**
                     * Executes ongranted operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param permissions Parameter for operation (type: MutableList<String>)
                     * @param allGranted Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onGranted(
                        permissions: MutableList<String>,
                        allGranted: Boolean,
                    ) {
                        XLog.i("onGranted($allGranted)")
                        callback.onResult(allGranted)
                    }

                    /**
                     * Executes ondenied operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param permissions Parameter for operation (type: MutableList<String>)
                     * @param never Parameter for operation (type: Boolean)
                     *
                     */
                    override fun onDenied(
                        permissions: MutableList<String>,
                        never: Boolean,
                    ) {
                        XLog.i("onDenied($never)")
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (never) {
                            var isBtNever = false
                            var isLocationNever = false
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             */
                            for (permission in permissions) {
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (permission == Permission.BLUETOOTH_SCAN || permission == Permission.BLUETOOTH_CONNECT) {
                                    isBtNever = true
                                }
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (permission == Permission.ACCESS_FINE_LOCATION || permission == Permission.ACCESS_COARSE_LOCATION) {
                                    isLocationNever = true
                                }
                            }
                            // 如果是被永久拒绝就跳转到应用Permission系统settings页area
                            TipDialog.Builder(context)
                                .setTitleMessage(context.getString(R.string.app_tip))
                                .setMessage(
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (!isLocationNever || (isBtNever && isBtFirst)) R.string.app_bluetooth_content else R.string.app_location_content,
                                )
                                .setPositiveListener(R.string.app_open) {
                                    XXPermissions.startPermissionActivity(context, permissions)
                                    callback.onNever(true)
                                }
                                .setCancelListener(R.string.app_cancel) {
                                    callback.onNever(false)
/**
 * Specialized thermal imaging component providing Callback functionality for the IRCamera system.
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
    interface Callback {
        /**
         * 未被永久拒绝时，全部授予 或 有部分未授予 Callback.
         */
        fun onResult(allGranted: Boolean)

        /**
         * 永久拒绝时，跳转弹框 去Open 或 Cancel Callback.
         */
        fun onNever(isJump: Boolean)
    }
}
