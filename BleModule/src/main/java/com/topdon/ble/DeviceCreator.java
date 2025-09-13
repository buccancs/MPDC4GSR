package com.topdon.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

import androidx.annotation.Nullable;


/**
 * {@link Device}实例构建器，搜索到BLEdevice时，使用此构建器实例化{@link Device}
 * <p>
 * date: 2021/8/12 00:07
 * author: bichuanfeng
 */
public interface DeviceCreator {
    /**
     * 搜索到bluetoothdevice后，根据广播data实例化{@link Device}，并且根据广播过滤device
     *
     * @param scanResult 搜索的结果data
     * @return 如果不是需要的device，返回null，过滤掉，那么它不会触发搜索结果回调，否则返回实例化的{@link Device}，触发搜索结果回调
     */
    @Nullable
    Device create(BluetoothDevice device, ScanResult scanResult);
}
