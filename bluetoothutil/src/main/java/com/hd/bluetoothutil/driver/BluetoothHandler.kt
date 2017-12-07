package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.device.BluetoothDeviceEntity


/**
 * Created by hd on 2017/9/1 .
 * bluetooth handler
 */
abstract class BluetoothHandler(val context: Context, val entity: BluetoothDeviceEntity,
                                val bluetoothAdapter: BluetoothAdapter, callback: MeasureProgressCallback) {

    protected var status = BleMeasureStatus.PREPARE

    protected abstract fun start()

    protected abstract fun release()

    fun checkSameDevice(device: BluetoothDevice?): Boolean {
        return if (device != null) {
            if (entity.macAddress.isNullOrEmpty()) {
                device.name == entity.deviceName
            } else {
                device.name == entity.deviceName && device.address == entity.macAddress
            }
        } else {
            false
        }
    }

    fun startMeasure() {
        if (status != BleMeasureStatus.RUNNING) {
            status = BleMeasureStatus.RUNNING
            start()
        } else {
            stopMeasure()
            startMeasure()
        }
    }

    fun stopMeasure() {
        if (status == BleMeasureStatus.RUNNING) {
            status = BleMeasureStatus.STOPPING
            release()
            status = BleMeasureStatus.STOPPED
        }
    }

}