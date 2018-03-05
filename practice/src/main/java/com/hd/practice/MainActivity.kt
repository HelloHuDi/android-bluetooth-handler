package com.hd.practice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val measureHandler by lazy { MeasureHandler(this, tvResult,svResult) }

    private val entity = BluetoothDeviceEntity()

    private val REQUEST_CODE = 10086

    private var permission_granted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissionGranted()
        entity.deviceName = name.text.toString()
        entity.version = DeviceVersion.BLUETOOTH_4
        entity.targetCharacteristicUuid = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb")// read
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            permission_granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun checkPermissionGranted() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.LOCATION)) {
                    permission_granted = false
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE)
                }
            } else {
                permission_granted = true
            }
    }

    fun start(view: View?=null) {
        if(permission_granted)
        measureHandler.start(entity)
    }

    fun stop(view: View?=null) {
        measureHandler.stop()
    }
}
