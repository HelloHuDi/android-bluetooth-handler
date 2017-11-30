package com.hd.bluetoothutil.callback


/**
 * Created by hd on 2017/11/30 .
 * set security check callback
 */
interface SecurityCheckCallback {

    /** hint security message */
    fun securityHint(hint: String)
}