package com.hd.bletools.view

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/12/12 .
 * measure
 */
class MeasureActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.getBundleExtra("bundle") as Bundle
        val version = bundle.getSerializable("version")
        val device = bundle.getParcelable<BluetoothDevice>("device")
        BL.d("$version+$device")
    }
}