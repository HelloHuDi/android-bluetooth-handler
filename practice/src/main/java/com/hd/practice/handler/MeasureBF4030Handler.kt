package com.hd.practice.handler

import android.content.Context
import android.widget.ScrollView
import android.widget.TextView
import java.util.*


/**
 * Created by hd on 2018/3/28 .
 * temperature : BF4030
 */
class MeasureBF4030Handler(context: Context, tv: TextView, sv: ScrollView) : Measure4Handler(context, tv, sv) {

    init {
        deviceName = "BF4030"
        readUUID = UUID.fromString("00002af0-0000-1000-8000-00805f9b34fb")
    }
}