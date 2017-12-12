package com.hd.bluetooth

import android.Manifest
import android.bluetooth.BluetoothGattCharacteristic
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ScrollView
import android.widget.Toast
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

    private var version: Int = 4

    private val REQUEST_CODE = 10086

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                measure()
            } else {
                Toast.makeText(this, "权限请求失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //set allow log
        BL.allowLog = BuildConfig.DEBUG
        if (version == 4 && ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.LOCATION)) {
                Toast.makeText(this, "注意权限问题", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE)
            }
        } else {
            measure()
        }
    }

    private fun measure() {
        val entity = addEntity()
        showResult("==>current device entity : " + entity.toString() + "\n")
        BluetoothController.init(this, entity, addCallback()).startMeasure()
    }

    override fun onStop() {
        super.onStop()
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

        override fun startSearch() {
            BL.d(" startSearch current thread:" + Thread.currentThread())
            showResult("==>start search \n")
        }

        override fun searchStatus(success: Boolean) {
            showResult("==>search $success\n")
        }

        override fun startBinding() {
            showResult("==>start binding device \n")
        }

        override fun boundStatus(success: Boolean) {
            showResult("==>binding device status :$success \n")
        }

        override fun startConnect() {
            showResult("==>start connect \n")
        }

        override fun connectStatus(success: Boolean) {
            showResult("==>connect $success \n")
        }

        override fun startRead() {
            showResult("==>start read \n")
        }

        override fun write(bluetoothGattCharacteristic: BluetoothGattCharacteristic, bluetoothLeService: BluetoothLeService) {
            BL.d("it's now allowed to write")
        }

        override fun reading(data: ByteArray) {
            showResult("==>receive data :${Arrays.toString(data)} \n")
        }

        override fun disconnect() {
            BL.d("disconnect current thread:" + Thread.currentThread())
            showResult("==>device disconnect \n")
        }
    }

    private fun addProgress2Callback() = object : MeasureBle2ProgressCallback {

        override fun startSearch() {
            BL.d(" startSearch current thread:" + Thread.currentThread())
            showResult("==>start search \n")
        }

        override fun searchStatus(success: Boolean) {
            showResult("==>search $success\n")
        }

        override fun startBinding() {
            showResult("==>start binding device \n")
        }

        override fun boundStatus(success: Boolean) {
            showResult("==>binding device status :$success \n")
        }

        override fun startConnect() {
            showResult("==>start connect \n")
        }

        override fun connectStatus(success: Boolean) {
            showResult("==>connect $success \n")
        }

        override fun startRead() {
            showResult("==>start read \n")
        }

        override fun write(outputStream: OutputStream) {
            BL.d("it's now allowed to write")
        }

        override fun reading(data: ByteArray) {
            showResult("==>receive data :${Arrays.toString(data)} \n")
        }

        override fun disconnect() {
            BL.d(" disconnect current thread:" + Thread.currentThread())
            showResult("==>device disconnect \n")
        }
    }

    private val handler = android.os.Handler()
    private fun showResult(msg: String) {
        runOnUiThread {
            result.append(msg)
            handler.post({
                sv.fullScroll(ScrollView.FOCUS_DOWN)
            })
        }
    }
}
