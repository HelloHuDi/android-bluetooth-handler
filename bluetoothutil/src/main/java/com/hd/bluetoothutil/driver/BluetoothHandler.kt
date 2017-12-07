package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.utils.BL
import java.util.*


/**
 * Created by hd on 2017/9/1 .
 * bluetooth handler
 */
abstract class BluetoothHandler(val context: Context, val entity: BluetoothDeviceEntity,
                                val bluetoothAdapter: BluetoothAdapter, val callback: MeasureProgressCallback) {

    protected var status = BleMeasureStatus.PREPARE

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
            status = BleMeasureStatus.STOPPED
        }
    }

    fun reading(data: ByteArray) {
        callback.reading(data)
        BL.d("receive data :" + Arrays.toString(data))
    }

}