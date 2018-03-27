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
 * base scanner
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

    fun terminationScan() {
        if (!continueScan()) return
        BL.d("stop scan")
        status = BleMeasureStatus.STOPPING
        stopScan()
        status = BleMeasureStatus.STOPPED
        release()
    }

    fun release(){
        bluetoothAdapter=null
        context=null
        callback=null
        status=BleMeasureStatus.PREPARE
    }

    protected fun scanComplete(device: BluetoothDevice? = null) {
        val same = sameDevice(device)
        BL.d("found current device is ：${device?.name} + $status+${device?.bondState}+${continueScan()}+$same")
        if (same) status = BleMeasureStatus.STOPPING
        callback?.scan(!continueScan(), device)
        if (same) {
            callback = null
            terminationScan()
        }
    }

    protected fun sameDevice(device: BluetoothDevice?) = BluetoothSecurityCheck.newInstance(context!!).checkSameDevice(device, entity)

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