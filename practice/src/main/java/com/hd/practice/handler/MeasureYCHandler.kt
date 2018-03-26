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

    /**
     * 无条件搜索设备
     */
    private val search_device = byteArrayOf(0x4F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x02.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xB2.toByte())
    /**
     * 向设备发送已经准备就绪命令
     */
    private val connect_device = byteArrayOf(0x4F.toByte(), 0x06.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x03.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFE.toByte(), 0xB4.toByte())

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
                writeData(search_device)
                SystemClock.sleep(100)
                writeData(connect_device)
                BL.d("write data to device :$count")
            }
        }
    }
}