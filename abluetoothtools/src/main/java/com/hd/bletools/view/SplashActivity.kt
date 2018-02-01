package com.hd.bletools.view

import android.os.Bundle
import com.hd.bletools.R
import com.hd.splashscreen.SimpleConfig
import com.hd.splashscreen.SimpleSplashFinishCallback
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity


/**
 * Created by hd on 2018/2/1 .
 *
 */
class SplashActivity : BaseActivity(), SimpleSplashFinishCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        screen.addConfig(getSimpleConfig())
        screen.start()
    }

    private fun getSimpleConfig(): SimpleConfig {
        val simpleConfig = SimpleConfig()
        simpleConfig.text = "BLUETOOTHTOOL"
        simpleConfig.textColor = R.color.colorPrimary
        simpleConfig.textSize = 30f
        simpleConfig.iconId = R.mipmap.icon
        simpleConfig.callback = this
        return simpleConfig
    }

    override fun loadFinish() {
        startActivity<ChoosePatternActivity>()
        finish()
    }

}