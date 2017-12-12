package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.help.BluetoothSecurityCheck


/**
 * Created by hd on 2017/12/10 .
 * bluetooth device scanner
 */
object Scanner {

    /** start scan bluetooth device*/
    fun scan(context: Context, bluetoothAdapter: BluetoothAdapter?=null, entity: BluetoothDeviceEntity, callback: ScannerCallback) {
        val adapter = bluetoothAdapter ?: BluetoothSecurityCheck.newInstance(context).check(entity.version)
        if (adapter == null) {
            callback.scan(true)
        } else {
            val scan = if (entity.version == DeviceVersion.BLUETOOTH_2) {
                Bluetooth2Scanner()
            } else {
                Bluetooth4Scanner()
            }
            scan.init(context, adapter, entity, callback)
            scan.startScan()
        }
    }

}