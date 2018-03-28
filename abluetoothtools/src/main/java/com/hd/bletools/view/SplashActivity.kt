package com.hd.bletools.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.hd.bletools.R
import com.hd.splashscreen.text.SimpleConfig
import com.hd.splashscreen.text.SimpleSplashFinishCallback
import kotlinx.android.synthetic.main.activity_splash.*


/**
 * Created by hd on 2018/2/1 .
 * request permission
 */
class SplashActivity : BaseActivity(), SimpleSplashFinishCallback {

    private val requestCode = 10086

    private var permissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        screen.addConfig(getSimpleConfig())
        screen.start()
    }

    fun scan(view:View){
        if(permissionGranted) {
            val version = when(view.id){
                R.id.scan2-> 2
                else-> 4
            }
            val intent=Intent(this,ScanDeviceActivity::class.java)
            intent.putExtra("version",version)
            startActivity(intent)
            finish()
        }else{
            checkPermissionGranted()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun getSimpleConfig(): SimpleConfig {
        val simpleConfig = SimpleConfig(this)
        simpleConfig.text = "BLUETOOTHTOOL"
        simpleConfig.setTextColorFromResources(R.color.colorPrimary)
        simpleConfig.textSize = 30f
        simpleConfig.setIconId(R.mipmap.icon)
        simpleConfig.callback = this
        return simpleConfig
    }

    override fun loadFinish() {
        checkPermissionGranted()
    }

    private fun checkPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.LOCATION)) {
                permissionGranted = false
                snack(R.string.permission_failure)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), requestCode)
            }
        } else {
            permissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true
            } else {
                permissionGranted = false
                snack(R.string.permission_failure)
            }
        }
    }

    private fun snack(@StringRes strId: Int) {
        Snackbar.make(splash, resources.getString(strId), Snackbar.LENGTH_LONG).show()
    }
}