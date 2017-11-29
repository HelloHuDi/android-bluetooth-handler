package com.hd.bluetoothutil.method

import android.annotation.SuppressLint
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.device.BluetoothDeviceEntity
import com.hd.bluetoothutil.driver.Bluetooth2Handler
import com.hd.bluetoothutil.driver.Bluetooth4Handler
import com.hd.bluetoothutil.driver.BluetoothHandler
import com.hd.bluetoothutil.help.BluetoothSecurityCheck
import com.hd.bluetoothutil.utils.BL

/**
 * Created by hd on 2017/5/24.
 * control bluetooth device measure
 */
class BluetoothController {

    private var bluetoothHandler: BluetoothHandler? = null
    private var entity: BluetoothDeviceEntity? = null

    fun init(context: Context, entity: BluetoothDeviceEntity, callback: MeasureProgressCallback): BluetoothController? {
        initBlueHandler(context, entity, callback)
        this.entity = entity
        return instance
    }

    private fun initBlueHandler(context: Context, entity: BluetoothDeviceEntity, callback: MeasureProgressCallback) {
        val targetVersion = entity.version
        val mBluetoothAdapter = BluetoothSecurityCheck.newInstance(context).check(targetVersion)
        if (mBluetoothAdapter != null) {
            if (targetVersion === DeviceVersion.BLUETOOTH_2 && callback is MeasureBle2ProgressCallback) {
                bluetoothHandler = Bluetooth2Handler(context, entity, mBluetoothAdapter, callback)
            } else if (targetVersion === DeviceVersion.BLUETOOTH_4 && callback is MeasureBle4ProgressCallback) {
                bluetoothHandler = Bluetooth4Handler(context, entity, mBluetoothAdapter, callback)
            }
        } else {
            TODO()
        }
    }

    fun startMeasure() {
        if (bluetoothHandler != null) {
            bluetoothHandler!!.startMeasure()
        } else {
            BL.d("initialize bluetooth handler error")
        }
    }

    fun stopMeasure() {
        BL.d("BluetoothController stop")
        bluetoothHandler?.stopMeasure()
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: BluetoothController? = null

        fun newInstance(): BluetoothController {
            if (instance == null) {
                instance = BluetoothController()
            }
            return instance as BluetoothController
        }
    }
}
