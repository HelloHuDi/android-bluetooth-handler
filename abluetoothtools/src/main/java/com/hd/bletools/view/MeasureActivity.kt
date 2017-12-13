package com.hd.bletools.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import com.hd.bletools.R
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/12/12 .
 * measure
 */
class MeasureActivity : BaseActivity() {

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure)
        val bundle = intent.getBundleExtra("bundle") as Bundle
        val version = bundle.getSerializable("version") as DeviceVersion
        val device = bundle.getParcelable<BluetoothDevice>("device")
        BL.d("$version+${device!!.name}")
        val fragmentTransaction = fragmentManager.beginTransaction()
        val measureFragment = if (version == DeviceVersion.BLUETOOTH_2) {
            Measure2Fragment()
        } else {
            Measure4Fragment()
        }
        measureFragment.arguments=bundle
        fragmentTransaction.replace(R.id.container, measureFragment).commit()
    }

}

