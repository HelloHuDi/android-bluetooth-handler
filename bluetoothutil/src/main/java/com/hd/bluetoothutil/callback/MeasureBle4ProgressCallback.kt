package com.hd.bluetoothutil.callback

import android.bluetooth.BluetoothGattCharacteristic
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.driver.BluetoothLeService


/**
 * Created by hd on 2017/5/24.
 *
 */

interface MeasureBle4ProgressCallback :MeasureProgressCallback{

    /**it will be called many times if you do not specify a special UUID[BluetoothDeviceEntity.targetCharacteristicUuid]*/
    fun write(bluetoothGattCharacteristic: BluetoothGattCharacteristic, bluetoothLeService: BluetoothLeService)

}
