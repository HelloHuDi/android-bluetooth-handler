package com.hd.bluetoothutil.callback


/**
 * Created by hd on 2017/11/30 .
 * set security check callback
 */
interface SecurityCheckCallback {

    /**hint that the security message is at a time when there is a problem */
    fun securityHint(int: Int)
}