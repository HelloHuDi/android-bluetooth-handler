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
     * start binding bluetooth device
     */
    fun startBinding()

    /**
     * [com.hd.bluetoothutil.config.DeviceVersion.BLUETOOTH_4] default is binding
     * @param success return true if the device binding is successful
     */
    fun boundStatus(success: Boolean)

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

}
