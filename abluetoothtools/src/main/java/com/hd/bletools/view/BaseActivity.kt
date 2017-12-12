package com.hd.bletools.view

import com.hd.bletools.BuildConfig
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/12/12 .
 *
 */
abstract class BaseActivity: android.support.v7.app.AppCompatActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        BL.allowLog=BuildConfig.DEBUG
//        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
//        window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
//        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }


}