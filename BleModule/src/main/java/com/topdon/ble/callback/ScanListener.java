package com.topdon.ble.callback;

import android.Manifest;



import com.topdon.ble.Device;

/**
 * bluetooth搜索监听器
 * <p>
 * date: 2021/8/12 09:17
 * author: bichuanfeng
 */
public interface ScanListener {
    /**
     * 缺少定位权限。 {@link Manifest.permission#ACCESS_COARSE_LOCATION} 或者 {@link Manifest.permission#ACCESS_FINE_LOCATION}
     */
    int ERROR_LACK_LOCATION_PERMISSION = 0;
    /**
     * 系统位置service未开启
     */
    int ERROR_LOCATION_SERVICE_CLOSED = 1;
    /**
     * 搜索error
     */
    int ERROR_SCAN_FAILED = 2;
    /**
     * 缺少bluetooth权限。 {@link Manifest.permission#BLUETOOTH_SCAN} 或者 {@link Manifest.permission#BLUETOOTH_CONNECT}
     */
    int ERROR_LACK_BLUETOOTH_PERMISSION = 3;

    /**
     * bluetooth搜索start
     */
    void onScanStart();

    /**
     * bluetooth搜索stop
     */
    void onScanStop();

    /**
     * 搜索到BLEdevice
     *
     * @deprecated 使用 {@link #onScanResult(Device, boolean)}，不要再覆写此method，因为不再会被回调
     */
    @Deprecated
    default void onScanResult(Device device) {
    }

    /**
     * 搜索到BLEdevice
     *
     * @param device           搜索到的device
     * @param isConnectedBySys 是否已被系统bluetoothconnection上
     */
    void onScanResult(Device device, boolean isConnectedBySys);

    /**
     * 搜索error
     *
     * @param errorCode {@link #ERROR_LACK_LOCATION_PERMISSION}, {@link #ERROR_LOCATION_SERVICE_CLOSED}, {@link #ERROR_LACK_BLUETOOTH_PERMISSION}
     */
    void onScanError(int errorCode, String errorMsg);
}
