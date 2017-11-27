package com.hd.bluetoothutil.callback

import android.bluetooth.BluetoothGattCharacteristic
import com.hd.bluetoothutil.driver.BluetoothLeService


/**
 * Created by hd on 2017/5/24.
 *
 */

interface MeasureBle4Callback : MeasureCallback {

    fun write(bluetoothGattCharacteristic: BluetoothGattCharacteristic, bluetoothLeService: BluetoothLeService)

    fun read(data: ByteArray)
}
