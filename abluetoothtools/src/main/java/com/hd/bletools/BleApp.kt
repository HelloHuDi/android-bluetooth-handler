package com.hd.bletools

import android.app.Application
import com.hd.bluetoothutil.utils.BL
import com.squareup.leakcanary.LeakCanary


/**
 * Created by hd on 2017/12/13 .
 *
 */
class BleApp:Application(){

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // Normal app init code...
        BL.allowLog=BuildConfig.DEBUG
    }
}