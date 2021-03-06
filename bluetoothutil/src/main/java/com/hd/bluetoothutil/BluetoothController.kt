package com.hd.bluetoothutil

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.callback.MeasureProgressCallback
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
 * API 18+
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
object BluetoothController {

    @SuppressLint("StaticFieldLeak")
    private var bluetoothHandler: BluetoothHandler? = null

    private var entity: BluetoothDeviceEntity? = null

    private var callback: MeasureProgressCallback? = null

    private fun reset() {
        BL.d("BluetoothController reset")
        bluetoothHandler = null
        callback = null
        entity = null
    }

    fun init(context: Context, entity: BluetoothDeviceEntity, device: BluetoothDevice? = null,
             callback: MeasureProgressCallback): BluetoothController {
        reset()
        initBlueHandler(context.applicationContext, entity, device, callback)
        this.entity = entity
        this.callback = callback
        return this@BluetoothController
    }

    private fun initBlueHandler(context: Context, entity: BluetoothDeviceEntity, device: BluetoothDevice? = null, callback: MeasureProgressCallback) {
        BL.d("BluetoothController initBlueHandler")
        val targetVersion = entity.version
        val mBluetoothAdapter = BluetoothSecurityCheck.newInstance(context).check(targetVersion)
        if (mBluetoothAdapter != null) {
            if (targetVersion === DeviceVersion.BLUETOOTH_2 && callback is MeasureBle2ProgressCallback) {
                bluetoothHandler = Bluetooth2Handler(context, entity, mBluetoothAdapter, callback)
            } else if (targetVersion === DeviceVersion.BLUETOOTH_4 && callback is MeasureBle4ProgressCallback) {
                bluetoothHandler = Bluetooth4Handler(context, entity, mBluetoothAdapter, callback)
            } else {
                BL.e("init bluetooth handler failed:$callback==$entity")
            }
            bluetoothHandler?.initDevice(device)
        } else {
            BL.e("init bluetooth adapter failed")
        }
    }

    fun startMeasure() {
        if (bluetoothHandler != null) {
            bluetoothHandler!!.startMeasure()
        } else {
            BL.d("initialize bluetooth handler error,do not start measure")
        }
    }

    fun stopMeasure() {
        BL.d("BluetoothController stop")
        bluetoothHandler?.stopMeasure()
        reset()
    }
}
