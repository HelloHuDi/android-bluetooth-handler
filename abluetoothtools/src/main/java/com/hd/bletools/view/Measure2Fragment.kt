package com.hd.bletools.view

import com.hd.bletools.R
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import java.io.OutputStream


/**
 * Created by hd on 2017/12/13 .
 *
 */
class Measure2Fragment : MeasureFragment(), MeasureBle2ProgressCallback {

    override fun setLayoutId(): Int {
        return R.layout.fragment_measure_2
    }

    override fun write(outputStream: OutputStream) {

    }

}