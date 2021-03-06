package com.hd.bluetoothutil.config

import java.io.Serializable
import java.util.*


/**
 * Created by hd on 2017/9/1 .
 *@param deviceName target device name,the name and address must be confirmed one
 *@param macAddress target device MAC address,the name and address must be confirmed one
 *@param pin target device pin ,only aim at bluetooth 2.0
 *@param targetCharacteristicUuid focus on assigned goals with the Characteristic [android.bluetooth.BluetoothGattCharacteristic] UUID [UUID]
 *@param reconnected reconnected device if the device is disconnected at during measuring
 *@param version bluetooth version
 */
data class BluetoothDeviceEntity(var deviceName: String? = null, var macAddress: String? = null,
                                 var pin: String? = "1234", var targetCharacteristicUuid: UUID?=null,
                                 var reconnected:Boolean=false,var version:DeviceVersion=DeviceVersion.BLUETOOTH_2) :Serializable