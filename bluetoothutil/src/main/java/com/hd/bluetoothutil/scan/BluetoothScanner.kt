package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothAdapter
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.DeviceVersion


/**
 * Created by hd on 2017/12/10 .
 *
 */
abstract class BluetoothScanner{

    protected var bluetoothAdapter: BluetoothAdapter? = null

    protected var callback: ScannerCallback? = null

    /** start scan device */
    abstract fun startScan(bluetoothAdapter: BluetoothAdapter, version: DeviceVersion, callback: ScannerCallback)

}