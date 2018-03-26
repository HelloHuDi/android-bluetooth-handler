package com.hd.practice.handler

import android.content.Context
import android.widget.ScrollView
import android.widget.TextView
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.utils.BL
import java.io.OutputStream
import java.lang.Exception


/**
 * Created by hd on 2018/3/26 .
 * measure 2.0
 */
abstract class Measure2Handler(context: Context, tv: TextView, sv: ScrollView) : MeasureHandler(context, tv, sv) {

    protected var pain = "1234"

    private var outputStream: OutputStream? = null

    override fun start() {
        entity.deviceName = deviceName
        entity.version = DeviceVersion.BLUETOOTH_2
        entity.pin = pain
        super.start()
    }

    override fun write(outputStream: OutputStream) {
        BL.d("write outputStream")
        this.outputStream = outputStream
    }

    override fun writeData(data: ByteArray) {
        if(outputStream!=null){
            try {
                outputStream!!.write(data)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}