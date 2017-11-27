package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureBle2Callback
import com.hd.bluetoothutil.device.BluetoothDeviceEntity


/**
 * Created by hd on 2017/9/1 .
 *
 */
class Bluetooth2Handler(context: Context, entity: BluetoothDeviceEntity,
                        bluetoothAdapter: BluetoothAdapter, callback: MeasureBle2Callback)
    : BluetoothHandler(context, entity, bluetoothAdapter, callback) {

    private var pin: String? = null

    fun setPin(pin: String?) {
        this.pin = pin
    }

    override fun start() {
        cancelSearch()
        searchDevice()
    }

    override fun stop() {

    }

    private fun cancelSearch() {
        if (bluetoothAdapter.isDiscovering)
            bluetoothAdapter.cancelDiscovery()
    }

    private fun searchDevice() {

    }
}