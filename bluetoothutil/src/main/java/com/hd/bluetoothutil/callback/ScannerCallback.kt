package com.hd.bluetoothutil.callback

import android.bluetooth.BluetoothDevice


/**
 * Created by hd on 2017/12/10 .
 *
 */
interface ScannerCallback {

    /** it will be called many times when the device is scanned
     * @param scanComplete return true at time of timeout or stop
     * */
    fun scan(scanComplete:Boolean,devices: List<BluetoothDevice>)
}