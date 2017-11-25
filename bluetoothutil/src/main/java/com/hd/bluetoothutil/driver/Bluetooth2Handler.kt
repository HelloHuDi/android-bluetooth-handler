package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureCallback
import com.hd.bluetoothutil.device.BluetoothDeviceEntity


/**
 * Created by hd on 2017/9/1 .
 *
 */
class Bluetooth2Handler(context: Context, entity: BluetoothDeviceEntity,
                        bluetoothAdapter: BluetoothAdapter, callback: MeasureCallback)
    : BluetoothHandler(context,entity,bluetoothAdapter,callback) {

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