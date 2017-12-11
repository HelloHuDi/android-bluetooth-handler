package com.hd.bluetoothutil.callback

import android.bluetooth.BluetoothDevice

/**
 * Created by hd on 2017/8/8 .
 * bluetooth device bound progress progressCallback
 */
interface BleBoundProgressCallback {

    /**
     * set pin code for device bound
     */
    val pin: String?

    /**
     * set target device name
     */
    val deviceName: String?

    /**
     * set target device name
     */
    val macAddress: String?

    /**
     * notify found device
     */
    fun foundDevice(bluetoothDevice: BluetoothDevice)

    /**
     * update device bound state
     *
     * @param bluetoothDevice maybe is null or isn't target device
     */
    fun actionBondStateChanged(bluetoothDevice: BluetoothDevice)

    /**
     * bluetooth status changed
     *
     * @param extraState [android.bluetooth.BluetoothAdapter.EXTRA_STATE]
     * *
     * @param extraPreviousState [android.bluetooth.BluetoothAdapter.EXTRA_PREVIOUS_STATE]
     */
    fun actionStateChanged(extraState: Int, extraPreviousState: Int)

    /**
     * bluetooth scan finished

     * @param searchComplete is true if found target device
     */
    fun actionDiscoveryFinished(searchComplete: Boolean)
}
