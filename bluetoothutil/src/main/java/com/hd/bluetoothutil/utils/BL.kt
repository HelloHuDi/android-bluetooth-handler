package com.hd.bluetoothutil.utils

import android.util.Log

/**
 * Created by hd on 2017/6/10.
 *
 */
object BL {
    var allowLog = false
    private val TAG = "sihealBleLog"

    fun i(i: String) {
        i(TAG, i)
    }

    fun d(d: String) {
        d(TAG, d)
    }

    fun w(w: String) {
        w(TAG, w)
    }

    fun e(e: String) {
        e(TAG, e)
    }

    fun i(tag: String?, i: String) {
        print { Log.i(tag ?: TAG, i) }
    }

    fun d(tag: String?, d: String) {
        print { Log.d(tag ?: TAG, d) }
    }

    fun w(tag: String?, w: String) {
        print { Log.w(tag ?: TAG, w) }
    }

    fun e(tag: String?, e: String) {
        print { Log.e(tag ?: TAG, e) }
    }

    private inline fun print(log: () -> Unit) {
        if (allowLog) log()
    }

}
