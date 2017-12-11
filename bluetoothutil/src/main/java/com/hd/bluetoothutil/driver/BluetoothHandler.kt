package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.help.BluetoothSecurityCheck
import com.hd.bluetoothutil.scan.Scanner
import com.hd.bluetoothutil.utils.BL
import java.util.*


/**
 * Created by hd on 2017/9/1 .
 * bluetooth handler
 */
abstract class BluetoothHandler(val context: Context, val entity: BluetoothDeviceEntity,
                                val bluetoothAdapter: BluetoothAdapter, val callback: MeasureProgressCallback)
    : ScannerCallback {

    protected var targetDevice: BluetoothDevice? = null

    protected var status = BleMeasureStatus.PREPARE

    protected abstract fun start()

    protected abstract fun release()

    protected abstract fun startConnect()

    fun startMeasure() {
        if (status != BleMeasureStatus.RUNNING) {
            status = BleMeasureStatus.RUNNING
            start()
        } else {
            stopMeasure()
            startMeasure()
        }
    }

    fun stopMeasure() {
        if (status == BleMeasureStatus.RUNNING) {
            status = BleMeasureStatus.STOPPING
            release()
            status = BleMeasureStatus.STOPPED
        }
    }

    protected fun startScan() {
        callback.startSearch()
        Scanner.scan(context, bluetoothAdapter, entity, this)
    }

    override fun scan(scanComplete: Boolean, device: BluetoothDevice?) {
        BL.d("scan result :$scanComplete+$device")
        if (!scanComplete) {
            if (BluetoothSecurityCheck.newInstance(context).checkSameDevice(device, entity)) {
                targetDevice = device
                notificationSearchStatus()
                startConnect()
            }
        } else {
            notificationSearchStatus()
        }
    }

    private var searchComplete = false

    private fun notificationSearchStatus() {
        if (!searchComplete) {
            searchComplete = true
            if (targetDevice == null)
                callback.searchStatus(false)
            else
                callback.searchStatus(true)
        }
    }

    fun reading(data: ByteArray) {
        callback.reading(data)
        BL.d("receive data :" + Arrays.toString(data))
    }

}