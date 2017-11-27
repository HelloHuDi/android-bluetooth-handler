package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureBle4Callback
import com.hd.bluetoothutil.device.BluetoothDeviceEntity
import java.util.*


/**
 * Created by hd on 2017/9/1 .
 *
 */
class Bluetooth4Handler(context: Context, entity: BluetoothDeviceEntity,
                        bluetoothAdapter: BluetoothAdapter, callback: MeasureBle4Callback)
    : BluetoothHandler(context,entity,bluetoothAdapter,callback) {

    fun setTargetCharacteristicUuid(uuid: UUID?){

    }
    override fun start() {

    }

    override fun stop() {

    }

}