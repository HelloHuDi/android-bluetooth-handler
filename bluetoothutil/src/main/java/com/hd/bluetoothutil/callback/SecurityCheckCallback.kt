package com.hd.bluetoothutil.callback


/**
 * Created by hd on 2017/11/30 .
 * set security check callback
 */
interface SecurityCheckCallback {

    /**hint that the security message is at a time when there is a problem
     * [com.hd.bluetoothutil.help.BluetoothSecurityCheck.NOT_SUPPORT_BLE_4]
     * [com.hd.bluetoothutil.help.BluetoothSecurityCheck.PERMISSION_NOT_GRANTED]
     * [com.hd.bluetoothutil.help.BluetoothSecurityCheck.OPEN_BLUETOOTH_FAILED]
     * [com.hd.bluetoothutil.help.BluetoothSecurityCheck.NOT_SUPPORT_BLUETOOTH]
     * [com.hd.bluetoothutil.help.BluetoothSecurityCheck.NOT_BLUETOOTH_MANAGER]
     * */
    fun securityHint(int: Int)
}