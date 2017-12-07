package com.hd.bluetoothutil

import android.annotation.SuppressLint
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.callback.SecurityCheckCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.driver.Bluetooth2Handler
import com.hd.bluetoothutil.driver.Bluetooth4Handler
import com.hd.bluetoothutil.driver.BluetoothHandler
import com.hd.bluetoothutil.help.BluetoothSecurityCheck
import com.hd.bluetoothutil.utils.BL

/**
 * Created by hd on 2017/5/24.
 * control bluetooth handler
 */
object BluetoothController {

    @SuppressLint("StaticFieldLeak")
    private var bluetoothHandler: BluetoothHandler? = null

    private var entity: BluetoothDeviceEntity? = null

    private var callback: MeasureProgressCallback? = null

    private fun reset() {
        bluetoothHandler = null
        callback = null
        entity = null
    }

    fun init(context: Context, entity: BluetoothDeviceEntity, callback: MeasureProgressCallback): BluetoothController {
        reset()
        initBlueHandler(context.applicationContext, entity, callback)
        this.entity = entity
        this.callback = callback
        return this@BluetoothController
    }

    private fun initBlueHandler(context: Context, entity: BluetoothDeviceEntity, callback: MeasureProgressCallback) {
        val targetVersion = entity.version
        val mBluetoothAdapter = BluetoothSecurityCheck.newInstance(context, object : SecurityCheckCallback {
            override fun securityHint(hint: String) {
                callback.error(hint)
            }
        }).check(targetVersion)
        if (mBluetoothAdapter != null) {
            if (targetVersion === DeviceVersion.BLUETOOTH_2 && callback is MeasureBle2ProgressCallback) {
                bluetoothHandler = Bluetooth2Handler(context, entity, mBluetoothAdapter, callback)
            } else if (targetVersion === DeviceVersion.BLUETOOTH_4 && callback is MeasureBle4ProgressCallback) {
                bluetoothHandler = Bluetooth4Handler(context, entity, mBluetoothAdapter, callback)
            }
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
        reset()
    }
}
