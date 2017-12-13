package com.hd.bletools.view

import android.bluetooth.BluetoothGattCharacteristic
import com.hd.bletools.R
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.driver.BluetoothLeService

/**
 * Created by hd on 2017/12/13 .
 *
 */
class Measure4Fragment : MeasureFragment(), MeasureBle4ProgressCallback {

    override fun setLayoutId(): Int {
        return R.layout.fragment_measure_4
    }

    override fun write(bluetoothGattCharacteristic: BluetoothGattCharacteristic, bluetoothLeService: BluetoothLeService) {

    }

}