package com.hd.bletools.view

import com.hd.bletools.R
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import java.io.OutputStream


/**
 * Created by hd on 2017/12/13 .
 * measure bluetooth 2.0
 */
class Measure2Fragment : MeasureFragment(), MeasureBle2ProgressCallback {

    override fun setMeasureProgressCallback(): MeasureProgressCallback {
        return this
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_measure_2
    }

    override fun write(outputStream: OutputStream) {
        this.outputStream = outputStream
    }
}