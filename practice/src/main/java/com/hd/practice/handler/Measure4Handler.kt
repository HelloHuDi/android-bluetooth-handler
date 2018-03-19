package com.hd.practice.handler

import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.widget.ScrollView
import android.widget.TextView
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.driver.BluetoothLeService
import java.util.*


/**
 * Created by hd on 2018/3/19 .
 * measure 4.0
 */
abstract class Measure4Handler(context: Context, tv: TextView, sv: ScrollView) : MeasureHandler(context, tv, sv) {

    private var writeGattCharacteristic: BluetoothGattCharacteristic? = null

    private var readGattCharacteristic: BluetoothGattCharacteristic? = null

    protected var writeUUID: UUID? = null

    protected var readUUID: UUID? = null

    private var onlyOneSend = false

    override fun start() {
        entity.deviceName = deviceName
        entity.version = DeviceVersion.BLUETOOTH_4
        entity.targetCharacteristicUuid = readUUID
        super.start()
    }

    override fun write(bluetoothGattCharacteristic: BluetoothGattCharacteristic?, bluetoothLeService: BluetoothLeService) {
        if (bluetoothGattCharacteristic != null) {
            if (writeUUID == bluetoothGattCharacteristic.uuid) {
                writeGattCharacteristic = bluetoothGattCharacteristic
                MeasureHandler@ this.bluetoothLeService = bluetoothLeService
            }
            if (readUUID == bluetoothGattCharacteristic.uuid) {
                readGattCharacteristic = bluetoothGattCharacteristic
                MeasureHandler@ this.bluetoothLeService = bluetoothLeService
            }
            if (openData != null && !onlyOneSend && writeGattCharacteristic != null && readGattCharacteristic != null) {
                Thread.sleep(1000)
                writeData(openData!!)
            }
        }
    }

    override fun writeData(data: ByteArray) {
        if (writeGattCharacteristic != null) {
            onlyOneSend = true
            writeGattCharacteristic!!.value = data
            bluetoothLeService!!.writeCharacteristic(writeGattCharacteristic!!)
            Thread.sleep(200)
            bluetoothLeService!!.selectiveNotification(readGattCharacteristic!!, arrayOf(writeGattCharacteristic!!))
        }
    }
}