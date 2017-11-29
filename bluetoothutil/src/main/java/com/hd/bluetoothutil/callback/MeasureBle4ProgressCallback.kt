package com.hd.bluetoothutil.callback

import android.bluetooth.BluetoothGattCharacteristic
import com.hd.bluetoothutil.driver.BluetoothLeService


/**
 * Created by hd on 2017/5/24.
 *
 */

interface MeasureBle4ProgressCallback :MeasureProgressCallback{

    fun write(bluetoothGattCharacteristic: BluetoothGattCharacteristic, bluetoothLeService: BluetoothLeService)

}
