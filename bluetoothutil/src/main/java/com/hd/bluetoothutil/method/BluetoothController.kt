package com.hd.bluetoothutil.method

import android.annotation.SuppressLint
import android.content.Context
import com.hd.bluetoothutil.callback.MeasureBle2Callback
import com.hd.bluetoothutil.callback.MeasureBle4Callback
import com.hd.bluetoothutil.callback.MeasureCallback
import com.hd.bluetoothutil.callback.ProgressCallback

import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.device.BluetoothDeviceEntity
import com.hd.bluetoothutil.driver.Bluetooth2Handler
import com.hd.bluetoothutil.driver.Bluetooth4Handler
import com.hd.bluetoothutil.driver.BluetoothHandler
import com.hd.bluetoothutil.help.BluetoothSecurityCheck
import com.hd.bluetoothutil.utils.BL
import java.util.*

/**
 * Created by hd on 2017/5/24.
 * control bluetooth device measure
 */
class BluetoothController {

    private var bluetoothHandler: BluetoothHandler? = null
    private var entity: BluetoothDeviceEntity? = null

    fun init(context: Context, entity: BluetoothDeviceEntity, callback: MeasureCallback): BluetoothController? {
        initBlueHandler(context, entity, callback)
        this.entity = entity
        return instance
    }

    private fun initBlueHandler(context: Context, entity: BluetoothDeviceEntity, callback: MeasureCallback) {
        val targetVersion = entity.version
        val mBluetoothAdapter = BluetoothSecurityCheck.newInstance(context).check(targetVersion)
        if (mBluetoothAdapter != null) {
            if (targetVersion === DeviceVersion.BLUETOOTH_2 && callback is MeasureBle2Callback) {
                bluetoothHandler = Bluetooth2Handler(context, entity, mBluetoothAdapter, callback)
            } else if (targetVersion === DeviceVersion.BLUETOOTH_4 && callback is MeasureBle4Callback) {
                bluetoothHandler = Bluetooth4Handler(context, entity, mBluetoothAdapter, callback)
            }
        } else {
            TODO()
        }
    }

    private var uuid: UUID? = null

    /**focus on assigned goals with the Characteristic [BluetoothGattCharacteristic] UUID [UUID] */
    fun setTargetCharacteristicUuid(uuid: UUID): BluetoothController? {
        this.uuid = uuid
        return instance
    }

    private var progressCallback: ProgressCallback? = null

    /** provide measure progress*/
    fun addProgressCallback(progressCallback: ProgressCallback): BluetoothController? {
        this.progressCallback = progressCallback
        return instance
    }

    fun startMeasure() {
        if (bluetoothHandler != null) {
            bluetoothHandler!!.addProgressCallback(progressCallback)
            if (bluetoothHandler is Bluetooth2Handler) {
                (bluetoothHandler as Bluetooth2Handler).setPin(entity!!.pin)
            } else if (bluetoothHandler is Bluetooth4Handler) {
                (bluetoothHandler as Bluetooth4Handler).setTargetCharacteristicUuid(uuid)
            }
            bluetoothHandler!!.start()
        } else {
            BL.d("initialize bluetooth handler error")
        }
    }

    fun stopMeasure() {
        BL.d("BluetoothController stop")
        bluetoothHandler?.stop()
        progressCallback = null
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
