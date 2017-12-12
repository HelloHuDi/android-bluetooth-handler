package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.help.BluetoothSecurityCheck
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/12/10 .
 *
 */
abstract class BluetoothBaseScanner {

    protected var context: Context? = null

    protected var bluetoothAdapter: BluetoothAdapter? = null

    protected var callback: ScannerCallback? = null

    protected var entity: BluetoothDeviceEntity? = null

    protected var status = BleMeasureStatus.PREPARE

    /** the scan results include devices that have been bound by the machine*/
    abstract fun startScan()

    abstract fun stopScan()

    protected fun scanComplete(device: BluetoothDevice? = null) {
        BL.d("found current device is ：${device?.name} + $status+${device?.bondState}")
        callback!!.scan(!continueScan(), device)
        /** scanning to the target device */
        if (continueScan() && sameDevice(device)) {
            BL.d("found target device is ：${device!!.name} + ${device.bondState}")
            stopScan()
        }
    }

    protected fun sameDevice(device: BluetoothDevice?)=BluetoothSecurityCheck.newInstance(context!!).checkSameDevice(device, entity)

    protected fun continueScan() = status == BleMeasureStatus.RUNNING

    fun init(context: Context, bluetoothAdapter: BluetoothAdapter,
             entity: BluetoothDeviceEntity, callback: ScannerCallback) {
        this.context = context.applicationContext
        this.bluetoothAdapter = bluetoothAdapter
        this.entity = entity
        this.callback = callback
        status = BleMeasureStatus.RUNNING
    }

}