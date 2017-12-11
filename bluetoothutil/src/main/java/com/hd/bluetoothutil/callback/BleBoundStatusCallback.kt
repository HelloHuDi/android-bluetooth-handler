package com.hd.bluetoothutil.callback

import android.bluetooth.BluetoothDevice

/**
 * Created by hd on 2017/8/8 .
 * bound status
 */
interface BleBoundStatusCallback {

    /** the bound status for target device  */
    fun boundStatus(discoveryFinished:Boolean,boundMap: LinkedHashMap<BluetoothDevice,Boolean>)

}
