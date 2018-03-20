package com.hd.practice.handler

import android.content.Context
import android.widget.ScrollView
import android.widget.TextView
import java.util.*


/**
 * Created by hd on 2018/3/19 .
 * pressure : FSRKB_BT-001
 */
class MeasureFBT01Handler(context: Context, tv: TextView, sv: ScrollView) : Measure4Handler(context, tv, sv) {

    init {
        deviceName = "FSRKB_BT-001"
        writeUUID = UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb")
        readUUID = UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb")
        openData = byteArrayOf(0xBE.toByte(), 0xB0.toByte(), 0x01.toByte(), 0xC0.toByte(), 0x36.toByte())
        closeData = byteArrayOf(0xBE.toByte(), 0xB0.toByte(), 0x01.toByte(), 0xC1.toByte(), 0x68.toByte())
    }
}