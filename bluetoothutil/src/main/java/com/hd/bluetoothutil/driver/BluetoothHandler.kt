package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.help.BluetoothSecurityCheck
import com.hd.bluetoothutil.scan.Scanner
import com.hd.bluetoothutil.utils.BL
import java.util.*


/**
 * Created by hd on 2017/9/1 .
 * bluetooth handler
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
abstract class BluetoothHandler(val context: Context, val entity: BluetoothDeviceEntity,
                                val bluetoothAdapter: BluetoothAdapter, val callback: MeasureProgressCallback)
    : ScannerCallback {

    protected var targetDevice: BluetoothDevice? = null

    protected var status = BleMeasureStatus.PREPARE

    protected abstract fun start()

    protected abstract fun release()

    protected abstract fun startConnect()

    fun initDevice(device: BluetoothDevice?) {
        this.targetDevice = device
    }

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
            Scanner.stopScan()
            release()
            status = BleMeasureStatus.STOPPED
        }
    }

    protected fun startScan() {
        callback.startSearch()
        if (targetDevice != null && //
                (entity.version == DeviceVersion.BLUETOOTH_4 ||// bluetooth 4.0
                        (entity.version == DeviceVersion.BLUETOOTH_2 //  bluetooth 2.0 and bound
                                && targetDevice!!.bondState == BluetoothDevice.BOND_BONDED))) {
            scan(true, targetDevice)
        } else {
            Scanner.scan(context, bluetoothAdapter, entity, this)
        }
    }

    override fun scan(scanComplete: Boolean, device: BluetoothDevice?) {
        BL.d("scan result :$scanComplete+${device?.name}")
        if (!scanComplete) {
            if (BluetoothSecurityCheck.newInstance(context).checkSameDevice(device, entity)) {
                Scanner.stopScan()
                targetDevice = device
                notificationSearchStatus()
            }
        } else {
            if (BluetoothSecurityCheck.newInstance(context).checkSameDevice(device, entity)) {
                targetDevice = device
            }
            notificationSearchStatus()
        }
    }

    private var searchComplete = false

    private fun notificationSearchStatus() {
        if (!searchComplete) {
            searchComplete = true
            if (targetDevice == null) {
                callback.searchStatus(false)
            } else {
                callback.searchStatus(true)
                callback.startBinding()
                startConnect()
            }
        }
    }

    fun reading(data: ByteArray) {
        callback.reading(data)
        BL.d("receive data :" + Arrays.toString(data))
    }

}