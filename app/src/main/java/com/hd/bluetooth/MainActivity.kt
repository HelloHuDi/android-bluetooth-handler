package com.hd.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hd.bluetoothutil.BluetoothController
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.driver.BluetoothLeService
import com.hd.bluetoothutil.utils.BL
import kotlinx.android.synthetic.main.activity_main.*
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    private var version:Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        BL.allowLog=BuildConfig.DEBUG
        val entity=addEntity()
        result.append("current device entity : "+entity.toString()+"\n")
        BluetoothController.init(this,entity, addCallback()).startMeasure()
    }

    override fun onPause() {
        super.onPause()
        BluetoothController.stopMeasure()
    }

    private fun addCallback(): MeasureProgressCallback {
        return if (version == 2) {
            addProgress2Callback()
        } else {
            addProgress4Callback()
        }
    }

    /** test*/
    private fun addEntity(): BluetoothDeviceEntity {
        return if (version == 2) {//bluetooth 2.0 device
            BluetoothDeviceEntity("BF4030", version = DeviceVersion.BLUETOOTH_2)
        } else {//bluetooth 4.0 device
            BluetoothDeviceEntity("BerryMed",
                    targetCharacteristicUuid = UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616"),
                    version = DeviceVersion.BLUETOOTH_4)
        }
    }

    private fun addProgress4Callback() = object : MeasureBle4ProgressCallback {

        override fun write(bluetoothGattCharacteristic: BluetoothGattCharacteristic, bluetoothLeService: BluetoothLeService) {
            BL.d("it's now allowed to write")
        }

        override fun startSearch() {
            BL.d(" startSearch current thread:" + Thread.currentThread())
            result.append("start search \n")
        }

        override fun searchStatus(success: Boolean) {
            result.append("search $success\n")
        }

        override fun startConnect() {
            result.append("start connect \n")
        }

        override fun connectStatus(success: Boolean) {
            result.append("connect $success \n")
        }

        override fun startRead() {
            result.append("start read \n")
        }

        override fun reading(data: ByteArray) {
            result.append("receive data :${Arrays.toString(data)} \n")
        }

        override fun disconnect() {
            BL.d(" disconnect current thread:" + Thread.currentThread())
            result.append("device disconnect \n")
        }

        override fun error(string: String) {
            result.append("measure error : $string \n")
        }
    }

    private fun addProgress2Callback() = object : MeasureBle2ProgressCallback {
        override fun write(outputStream: OutputStream) {
            BL.d("it's now allowed to write")
        }

        override fun startSearch() {
            BL.d(" startSearch current thread:" + Thread.currentThread())
            result.append("start search \n")
        }

        override fun searchStatus(success: Boolean) {
            result.append("search $success\n")
        }

        override fun startConnect() {
            result.append("start connect \n")
        }

        override fun connectStatus(success: Boolean) {
            result.append("connect $success \n")
        }

        override fun startRead() {
            result.append("start read \n")
        }

        override fun reading(data: ByteArray) {
            result.append("receive data :${Arrays.toString(data)} \n")
        }

        override fun disconnect() {
            BL.d(" disconnect current thread:" + Thread.currentThread())
            result.append("device disconnect \n")
        }

        override fun error(string: String) {
            result.append("measure error : $string \n")
        }
    }
}
