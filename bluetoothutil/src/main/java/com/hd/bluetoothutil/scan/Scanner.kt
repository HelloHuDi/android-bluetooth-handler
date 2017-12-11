package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion


/**
 * Created by hd on 2017/12/10 .
 * bluetooth device scanner
 */
object Scanner {

    /** start scan bluetooth device*/
    fun scan(context: Context, bluetoothAdapter: BluetoothAdapter, entity: BluetoothDeviceEntity, callback: ScannerCallback) {
        val scan = if (entity.version == DeviceVersion.BLUETOOTH_2) {
            Bluetooth2Scanner()
        } else {
            Bluetooth4Scanner()
        }
        scan.init(context,bluetoothAdapter, entity, callback)
        scan.startScan()
    }

}