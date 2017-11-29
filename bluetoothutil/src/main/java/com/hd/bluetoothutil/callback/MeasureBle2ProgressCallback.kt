package com.hd.bluetoothutil.callback

import java.io.OutputStream

/**
 * Created by hd on 2017/5/24.
 */

interface MeasureBle2ProgressCallback : MeasureProgressCallback {

    fun write(outputStream: OutputStream)

}
