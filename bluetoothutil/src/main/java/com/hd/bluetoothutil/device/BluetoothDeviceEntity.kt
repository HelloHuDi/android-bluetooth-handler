package com.hd.bluetoothutil.device

import com.hd.bluetoothutil.config.DeviceVersion


/**
 * Created by hd on 2017/9/1 .
 *@param deviceName target device name
 *@param macAddress target device MAC address
 *@param pin target device pin ,only aim at bluetooth 2.0
 *@param version bluetooth version
 */
data class BluetoothDeviceEntity(var deviceName: String? = null,var macAddress: String? = null,
                                 var pin: String? = "1234", var version:DeviceVersion=DeviceVersion.BLUETOOTH_2)