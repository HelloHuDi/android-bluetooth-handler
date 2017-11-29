package com.hd.bluetoothutil.callback

/**
 * Created by hd on 2017/6/10.
 * provide measuring progress
 */
interface MeasureProgressCallback {

    /**
     * start search bluetooth device
     */
    fun startSearch()

    /**
     * provide status in searching bluetooth device
     * @param success return true if search device complete
     */
    fun searchStatus(success: Boolean)

    /**
     * start connect bluetooth device ,it may be called many times at connect again
     */
    fun startConnect()

    /**
     * provide status in connecting bluetooth device
     * @param success return true if connect device success
     */
    fun connectStatus(success: Boolean)

    /**
     * start read data from bluetooth device,will be called only once
     */
    fun startRead()

    /**
     * reading data from bluetooth device，be called multiple times
     */
    fun reading(data: ByteArray)

    /**
     * disconnect bluetooth device
     */
    fun disconnect()

    /**
     * unknown errors in the measurement process
     */
    fun failed(string: String="测量失败")

}
