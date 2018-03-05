package com.hd.practice

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val measureHandler by lazy { MeasureHandler(this, tvResult,svResult) }

    private val entity = BluetoothDeviceEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        entity.deviceName = name.text.toString()
        entity.version = DeviceVersion.BLUETOOTH_4
        entity.targetCharacteristicUuid = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb")// read
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    fun start(view: View?=null) {
        measureHandler.start(entity)
    }

    fun stop(view: View?=null) {
        measureHandler.stop()
    }
}
