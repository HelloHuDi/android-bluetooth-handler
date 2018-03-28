package com.hd.practice.handler

import android.content.Context
import android.os.SystemClock
import android.widget.ScrollView
import android.widget.TextView
import com.hd.bluetoothutil.utils.BL
import java.io.OutputStream
import kotlin.concurrent.thread


/**
 * Created by hd on 2018/3/26 .
 * blood sugar : yicheng
 */
class MeasureYCHandler(context: Context, tv: TextView, sv: ScrollView) : Measure2Handler(context, tv, sv) {

    private val searchDevice = byteArrayOf(0x4F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x02.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xB2.toByte())

    private val connectDevice = byteArrayOf(0x4F.toByte(), 0x06.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x03.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFE.toByte(), 0xB4.toByte())

    init {
        deviceName="yicheng"
        pain="1234"
    }

    private var count=0

    override fun write(outputStream: OutputStream) {
        super.write(outputStream)
        thread {
            while (!reading) {
                count++
                writeData(searchDevice)
                SystemClock.sleep(100)
                writeData(connectDevice)
                BL.d("write data to device :$count")
            }
        }
    }
}