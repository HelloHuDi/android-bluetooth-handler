package com.hd.bluetoothutil.callback

import android.bluetooth.BluetoothDevice


/**
 * Created by hd on 2017/12/10 .
 *
 */
interface ScannerCallback {

    /**
     *  it will be called many times when the device is scanned,
     *  note, that the scanned device may be repeated
     * @param scanComplete return true at time of timeout or stop,the [device] is null if [scanComplete] return true
     * @param device include devices that have been bound by the system
     */
    fun scan(scanComplete: Boolean, device: BluetoothDevice? = null)
}