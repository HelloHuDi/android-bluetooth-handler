package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothAdapter
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.DeviceVersion


/**
 * Created by hd on 2017/12/10 .
 * bluetooth 2.0 device scanner
 */
class Bluetooth2Scanner() :BluetoothScanner(){

    override fun startScan(bluetoothAdapter: BluetoothAdapter, version: DeviceVersion, callback: ScannerCallback) {
    }


}