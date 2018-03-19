package com.hd.practice.handler

import android.content.Context
import android.widget.ScrollView
import android.widget.TextView
import java.util.*


/**
 * Created by hd on 2018/3/19 .
 * 测试蓝牙设备名为：SZLSD SPPLE Module 的血压计，该设备需要发送指令工作及发送指令结束
 */
class MeasureSSModuleHandler(context: Context, tv: TextView, sv: ScrollView) : Measure4Handler(context, tv, sv) {

    init {
        deviceName = "SZLSD SPPLE Module"
        writeUUID=UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb")
        readUUID = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb")
        openData = byteArrayOf(0xFE.toByte(), 0x81.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x01.toByte())
        closeData = byteArrayOf(0xFE.toByte(), 0x82.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x02.toByte())
    }
}