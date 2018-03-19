package com.hd.practice.handler

import android.content.Context
import android.widget.ScrollView
import android.widget.TextView
import java.util.*


/**
 * Created by hd on 2018/3/19 .
 * pressure : FSRKB_BT-001
 */
class MeasureFBT01Handler(context: Context, tv: TextView, sv: ScrollView) : Measure4Handler(context, tv, sv){

    init {
        deviceName = "FSRKB_BT-001"
        readUUID = UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb")
    }
}