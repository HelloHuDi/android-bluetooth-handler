package com.hd.bluetoothutil.device

import com.hd.bluetoothutil.config.DeviceVersion


/**
 * Created by hd on 2017/9/1 .
 *@param name target device name
 *@param mac target device MAC path
 *@param pin target device pin ,only aim at bluetooth 2.0
 *@param version bluetooth version
 */
data class BluetoothDeviceEntity(var name: String? = null, var mac: String? = null,
  private var pin: String? = "1234", var version:DeviceVersion=DeviceVersion.BLUETOOTH_2)