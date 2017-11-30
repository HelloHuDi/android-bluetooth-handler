package com.hd.bluetoothutil.help

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.hd.bluetoothutil.callback.BleBoundProgressCallback
import com.hd.bluetoothutil.callback.BleBoundStatusCallback
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.device.BluetoothDeviceEntity
import com.hd.bluetoothutil.utils.BL
import java.util.*


/**
 * Created by hd on 2017/9/1 .
 * bound bluetooth device
 */
class BoundBluetoothDevice constructor(context: Context, val callback: BleBoundStatusCallback?) {

    companion object {
        fun newInstance(context: Context, callback: BleBoundStatusCallback?) = BoundBluetoothDevice(context, callback)
    }

    private val bluetoothAdapter = BluetoothSecurityCheck.newInstance(context.applicationContext).check(DeviceVersion.BLUETOOTH_2)

    private val boundMap = linkedMapOf<BluetoothDevice, Boolean>()

    private val sameNameDeviceList = ArrayList<BluetoothDevice>()

    /** query multiple device bound status*/
    fun queryBoundStatus(entities: LinkedList<BluetoothDeviceEntity>) {
        entities.forEach { queryBoundStatus(it) }
    }

    /** bound multiple device */
    fun boundDevice(entities: List<BluetoothDeviceEntity>) {
        entities.forEach { boundDevice(it) }
    }

    /** query single device bound status*/
    fun queryBoundStatus(entity: BluetoothDeviceEntity): Boolean {
        reset()
        if (entity.version == DeviceVersion.BLUETOOTH_4) return true
        val devices = bluetoothAdapter?.bondedDevices
        if (devices != null && devices.isNotEmpty()) {
            val nullName = entity.deviceName.isNullOrEmpty()
            val nullAddress = entity.macAddress.isNullOrEmpty()
            if (nullName && !nullAddress) {
                devices.filter { it.address == entity.macAddress }.forEach { boundMap.put(it, it.bondState == BluetoothDevice.BOND_BONDED) }
            } else if (!nullName && nullAddress) {
                devices.filter { it.name == entity.deviceName }.forEach { boundMap.put(it, it.bondState == BluetoothDevice.BOND_BONDED) }
            } else if (!nullName && !nullAddress) {
                devices.filter { it.name == entity.deviceName && it.address == entity.macAddress }
                        .forEach { boundMap.put(it, it.bondState == BluetoothDevice.BOND_BONDED) }
            }
            if (boundMap.size > 0) {
                callback?.boundStatus(boundMap)
                return true
            }
        }
        return false
    }

    /** bound single device */
    fun boundDevice(entity: BluetoothDeviceEntity) {
        if (entity.version == DeviceVersion.BLUETOOTH_4) return
        reset()
        BleBroadCastReceiver.newInstance(object : BleBoundProgressCallback {

            override val pin: String? get() = entity.pin

            override val deviceName: String? get() = entity.deviceName

            override fun actionBondStateChanged(bluetoothDevice: BluetoothDevice) {
                if (bluetoothDevice.name == deviceName) {
                    BL.d("device bond state changed :" + bluetoothDevice.bondState)
                    when (bluetoothDevice.bondState) {
                        BluetoothDevice.BOND_BONDED -> {
                            boundMap.put(bluetoothDevice, true)
                            callback?.boundStatus(boundMap)
                        }
                        else -> BL.d("bound device status :" + bluetoothDevice.bondState)
                    }
                }
            }

            override fun actionStateChanged(extraState: Int, extraPreviousState: Int) {
                if (extraPreviousState == BluetoothAdapter.STATE_ON &&//
                        (extraState == BluetoothAdapter.STATE_OFF || //
                                extraState == BluetoothAdapter.STATE_TURNING_OFF)) {
                    BleBroadCastReceiver.clear()
                }
            }

            override fun actionDiscoveryFinished(searchComplete: Boolean) {
                BleBroadCastReceiver.clear()
            }
        })
    }

    private fun reset() {
        boundMap.clear()
        sameNameDeviceList.clear()
    }
}