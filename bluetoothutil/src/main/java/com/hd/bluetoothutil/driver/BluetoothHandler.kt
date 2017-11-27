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
                                val bluetoothAdapter: BluetoothAdapter, val callback: MeasureCallback) {

    protected var progressCallback: ProgressCallback? = null

    protected var status = BleMeasureStatus.STOPPED

    fun addProgressCallback(progressCallback: ProgressCallback?) {
        this.progressCallback = progressCallback
    }

    abstract fun start()

    abstract fun stop()

}