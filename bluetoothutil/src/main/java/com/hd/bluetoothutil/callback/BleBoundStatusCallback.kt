package com.hd.bluetoothutil.callback

import android.bluetooth.BluetoothDevice

/**
 * Created by hd on 2017/8/8 .
 * bound status
 */
interface BleBoundStatusCallback {

    fun boundStatus(binding: LinkedHashMap<BluetoothDevice,Boolean>)

}
