package com.hd.bluetoothutil.help

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.hd.bluetoothutil.callback.BleBoundProgressCallback
import com.hd.bluetoothutil.callback.BleBoundStatusCallback
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.utils.BL
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Created by hd on 2017/9/1 .
 * bound bluetooth device
 */
class BoundBluetoothDevice constructor(val context: Context, val callback: BleBoundStatusCallback?) {

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
        val restrain = AtomicBoolean(false)
        BleBroadCastReceiver.newInstance(object : BleBoundProgressCallback {

            override val pin: String? get() = entity.pin

            override val deviceName: String? get() = entity.deviceName

            override val macAddress: String? get() = entity.macAddress

            override fun actionBondStateChanged(bluetoothDevice: BluetoothDevice) {
                if (!restrain.get() &&
                        BluetoothSecurityCheck.newInstance(context).checkSameDevice(bluetoothDevice, entity)) {
                    BL.d("device bond state changed :" + bluetoothDevice.bondState)
                    when (bluetoothDevice.bondState) {
                        BluetoothDevice.BOND_BONDED -> {
                            restrain.set(true)
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
                    response()
                }
            }

            override fun actionDiscoveryFinished(searchComplete: Boolean) {
                BleBroadCastReceiver.clear()
                response()
            }

            private fun response() {
                if (!restrain.get()) {
                    restrain.set(true)
                    boundMap.clear()
                    callback?.boundStatus(boundMap)
                }
            }
        })
    }

    private fun reset() {
        boundMap.clear()
        sameNameDeviceList.clear()
    }
}