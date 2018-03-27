package com.hd.bluetoothutil.help

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import android.os.Build
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
class BoundBluetoothDevice constructor(val context: Context) : BleBoundProgressCallback {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var boundBluetoothDevice:BoundBluetoothDevice?=null

        fun newInstance(context: Context): BoundBluetoothDevice? {
            if(boundBluetoothDevice==null) {
                boundBluetoothDevice = BoundBluetoothDevice(context)
            }
            return boundBluetoothDevice
        }
    }

    private val bluetoothAdapter by lazy { BluetoothSecurityCheck.newInstance(context.applicationContext).check(DeviceVersion.BLUETOOTH_2) }

    /** query single device bound status*/
    fun queryBoundStatus(entity: BluetoothDeviceEntity): Boolean {
        if (entity.version == DeviceVersion.BLUETOOTH_4) return true
        val devices = bluetoothAdapter?.bondedDevices
        if (devices != null && devices.isNotEmpty())
            devices.filter { BluetoothSecurityCheck.newInstance(context).checkSameDevice(it, entity) }.forEach { return true }
        return false
    }

    private val boundMap = linkedMapOf<BluetoothDevice, Boolean>()

    private var entity: BluetoothDeviceEntity? = null

    private var callback1: BleBoundStatusCallback? = null

    private var callback2: ScannerCallback? = null

    private var manual_Binding = false

    /** bound single device */
    fun boundDevice(entity: BluetoothDeviceEntity, callback1: BleBoundStatusCallback, callback2: ScannerCallback? = null) {
        BL.d("android sdk  :" + Build.VERSION.SDK_INT)
        if (entity.version == DeviceVersion.BLUETOOTH_4) {
            actionDiscoveryFinished()
        } else {
            this.entity = entity
            this.callback1 = callback1
            this.callback2 = callback2
            boundMap.clear()
            manual_Binding = false
            BleBroadCastReceiver.newInstance(this)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                if (bluetoothAdapter != null) {
                    manualBinding()
                } else {
                    actionDiscoveryFinished()
                }
            }
        }
    }

    private val bleReceive by lazy { BleBroadCastReceiver() }

    private fun manualBinding() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        context.applicationContext.registerReceiver(bleReceive, intentFilter)
        manual_Binding = true
    }

    override val pin: String? get() = entity!!.pin

    override val deviceName: String? get() = entity!!.deviceName

    override val macAddress: String? get() = entity!!.macAddress

    override fun foundDevice(bluetoothDevice: BluetoothDevice) {
        BL.d("notify found device :" + bluetoothDevice.name + "=" + bluetoothDevice.address)
        callback2?.scan(false, bluetoothDevice)
    }

    override fun actionBondStateChanged(bluetoothDevice: BluetoothDevice) {
        BL.d("device bond state changed :" + bluetoothDevice.name + "==" + bluetoothDevice.bondState)
        if (bluetoothDevice.bondState == BluetoothDevice.BOND_BONDED) {
            boundMap[bluetoothDevice] = true
            callback1!!.boundStatus(false, boundMap)
        }
    }

    override fun actionStateChanged(extraState: Int, extraPreviousState: Int) {
        BL.d("action state changed :$extraState=$extraPreviousState")
        if (extraPreviousState == BluetoothAdapter.STATE_ON &&//
                (extraState == BluetoothAdapter.STATE_OFF || //
                        extraState == BluetoothAdapter.STATE_TURNING_OFF)) {
            actionDiscoveryFinished()
        }
    }

    override fun actionDiscoveryFinished() {
        BL.d("actionDiscoveryFinished")
        if (boundMap.size > 0) {
            boundMap.clear()
        }
        callback1!!.boundStatus(true, boundMap)
        callback2?.scan(true)
        BleBroadCastReceiver.clear()
        unregisterReceiver()
    }

    private fun unregisterReceiver() {
        if (manual_Binding) {
            context.applicationContext.unregisterReceiver(bleReceive)
            manual_Binding = false
        }
    }
}