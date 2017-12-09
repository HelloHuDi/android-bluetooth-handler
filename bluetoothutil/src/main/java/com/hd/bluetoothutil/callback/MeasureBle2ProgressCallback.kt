package com.hd.bluetoothutil.callback

import java.io.OutputStream

/**
 * Created by hd on 2017/5/24.
 *
 */

interface MeasureBle2ProgressCallback : MeasureProgressCallback {

    /**it will be called many times*/
    fun write(outputStream: OutputStream)

}
