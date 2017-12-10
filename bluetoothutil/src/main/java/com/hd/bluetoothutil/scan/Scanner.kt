package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothAdapter
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.DeviceVersion


/**
 * Created by hd on 2017/12/10 .
 * bluetooth device scanner
 */
object Scanner {

    /** start scan bluetooth device*/
    fun scan(bluetoothAdapter: BluetoothAdapter, version: DeviceVersion, callback: ScannerCallback) {
        val scan = if (version == DeviceVersion.BLUETOOTH_2) {
            Bluetooth2Scanner()
        } else {
            Bluetooth4Scanner()
        }
        scan.startScan(bluetoothAdapter, version, callback)
    }

}