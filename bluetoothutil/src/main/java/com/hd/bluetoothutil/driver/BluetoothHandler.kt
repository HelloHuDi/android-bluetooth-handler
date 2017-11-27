package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureCallback
import com.hd.bluetoothutil.callback.ProgressCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.device.BluetoothDeviceEntity


/**
 * Created by hd on 2017/9/1 .
 * bluetooth handler
 */
abstract class BluetoothHandler(val context: Context, val entity: BluetoothDeviceEntity,
                                val bluetoothAdapter: BluetoothAdapter,callback: MeasureCallback) {

    protected var progressCallback: ProgressCallback? = null

    protected var status = BleMeasureStatus.PREPARE

    fun addProgressCallback(progressCallback: ProgressCallback?) {
        this.progressCallback = progressCallback
    }

    protected abstract fun start()

    protected abstract fun release()

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
            progressCallback=null
            status = BleMeasureStatus.STOPPED
        }
    }

}