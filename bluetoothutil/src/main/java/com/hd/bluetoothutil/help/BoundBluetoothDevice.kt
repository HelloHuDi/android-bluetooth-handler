package com.hd.bluetoothutil.help

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.hd.bluetoothutil.callback.BleBoundProgressCallback
import com.hd.bluetoothutil.callback.BleBoundStatusCallback
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/9/1 .
 * bound bluetooth device
 */
class BoundBluetoothDevice constructor(val context: Context) {

    companion object {
        fun newInstance(context: Context) = BoundBluetoothDevice(context)
    }

    private val bluetoothAdapter = BluetoothSecurityCheck.newInstance(context.applicationContext)
            .check(DeviceVersion.BLUETOOTH_2)

    /** query single device bound status*/
    fun queryBoundStatus(entity: BluetoothDeviceEntity): Boolean {
        if (entity.version == DeviceVersion.BLUETOOTH_4) return true
        val devices = bluetoothAdapter?.bondedDevices
        if (devices != null && devices.isNotEmpty())
            devices.filter { BluetoothSecurityCheck.newInstance(context).checkSameDevice(it, entity) }
                    .forEach { return true }
        return false
    }

    private val boundMap = linkedMapOf<BluetoothDevice, Boolean>()

    /** bound single device */
    fun boundDevice(entity: BluetoothDeviceEntity, callback1: BleBoundStatusCallback,
                    callback2: ScannerCallback? = null) {
        if (entity.version == DeviceVersion.BLUETOOTH_4) return
        boundMap.clear()
        BleBroadCastReceiver.newInstance(object : BleBoundProgressCallback {

            override val pin: String? get() = entity.pin

            override val deviceName: String? get() = entity.deviceName

            override val macAddress: String? get() = entity.macAddress

            override fun foundDevice(bluetoothDevice: BluetoothDevice) {
                BL.d("notify found device :" + bluetoothDevice.name+"="+bluetoothDevice.address)
                callback2?.scan(false, bluetoothDevice)
            }

            override fun actionBondStateChanged(bluetoothDevice: BluetoothDevice) {
                BL.d("device bond state changed :" + bluetoothDevice.name + "==" + bluetoothDevice.bondState)
                if(bluetoothDevice.bondState == BluetoothDevice.BOND_BONDED) {
                    boundMap.put(bluetoothDevice, true)
                    callback1.boundStatus(false, boundMap)
                }
            }

            override fun actionStateChanged(extraState: Int, extraPreviousState: Int) {
                BL.d("action state changed :$extraState=$extraPreviousState")
                if (extraPreviousState == BluetoothAdapter.STATE_ON &&//
                        (extraState == BluetoothAdapter.STATE_OFF || //
                                extraState == BluetoothAdapter.STATE_TURNING_OFF)) {
                    BleBroadCastReceiver.clear()
                }
            }

            override fun actionDiscoveryFinished() {
                BL.d("actionDiscoveryFinished")
                if (boundMap.size > 0) {
                    boundMap.clear()
                }
                callback1.boundStatus(true, boundMap)
                callback2?.scan(true)
            }
        })
    }
}