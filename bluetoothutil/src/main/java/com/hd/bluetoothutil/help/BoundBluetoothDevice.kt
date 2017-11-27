package com.hd.bluetoothutil.help

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.hd.bluetoothutil.callback.BleBoundStatusCallback
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.device.BluetoothDeviceEntity


/**
 * Created by hd on 2017/9/1 .
 * bound bluetooth device
 */
class BoundBluetoothDevice constructor(context: Context, private val callback: BleBoundStatusCallback?) {

    private val bluetoothAdapter = BluetoothSecurityCheck.newInstance(context.applicationContext).check(DeviceVersion.BLUETOOTH_2)

    private val boundMap = linkedMapOf<BluetoothDevice, Boolean>()

    companion object {
        fun newInstance(context: Context, callback: BleBoundStatusCallback?) = BoundBluetoothDevice(context, callback)
    }

    fun queryBoundStatus(entity: BluetoothDeviceEntity, needCallback: Boolean = true): Boolean {
        if (entity.version == DeviceVersion.BLUETOOTH_4) return false
        val devices = bluetoothAdapter?.bondedDevices
        if (devices != null && devices.isNotEmpty()) {
            for (boundDevice in devices) {
                if (!entity.macAddress.isNullOrEmpty()) {
                    if (boundDevice.name == entity.deviceName) {
                        boundMap.put(boundDevice, true)
                        if (needCallback) callback?.boundStatus(boundMap)
                        return true
                    }
                } else {
                    if (!entity.deviceName.isNullOrEmpty() && boundDevice.name == entity.deviceName) {
                        boundMap.put(boundDevice, true)
                        if (needCallback) callback?.boundStatus(boundMap)
                        return true
                    }
                }
            }
        }
        return false
    }

    fun queryBoundStatus(entities: List<BluetoothDeviceEntity>) {
        entities.forEach { queryBoundStatus(it,false) }
        callback?.boundStatus(boundMap)
    }

    fun boundDevice(entity: BluetoothDeviceEntity){

    }

    fun boundDevice(entities: List<BluetoothDeviceEntity>){

    }
}