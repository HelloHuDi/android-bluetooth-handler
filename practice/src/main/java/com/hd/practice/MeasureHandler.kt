package com.hd.practice

import android.app.Activity
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.os.SystemClock
import android.widget.ScrollView
import android.widget.TextView
import com.hd.bluetoothutil.BluetoothController
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.driver.BluetoothLeService
import com.hd.bluetoothutil.utils.BL
import java.util.*


/**
 * Created by hd on 2018/3/5 .
 * measure handler
 */
class MeasureHandler(private val context: Context, private val tv: TextView, private val sv: ScrollView) : MeasureBle4ProgressCallback {

    init {
        BL.allowLog = BuildConfig.DEBUG
    }

    private var writeGattCharacteristic: BluetoothGattCharacteristic? = null

    private var readGattCharacteristic: BluetoothGattCharacteristic? = null

    private var bluetoothLeService: BluetoothLeService? = null

    private val openData = byteArrayOf(0xFE.toByte(), 0x81.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x01.toByte())

    private val closeData = byteArrayOf(0xFE.toByte(), 0x82.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x02.toByte())

    private val writeUUID: UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb")

    val readUUID: UUID = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb")

    fun start(entity: BluetoothDeviceEntity) {
        //main thread
        BluetoothController.init(context = context, entity = entity, device = null, callback = MeasureHandler@ this).startMeasure()
    }

    fun stop() {
        writeData(closeData)
        SystemClock.sleep(1000)
        BluetoothController.stopMeasure()
    }

    private fun showResult(msg: String) {
        (context as Activity).runOnUiThread {
            tv.append(msg)
            sv.post({
                sv.fullScroll(ScrollView.FOCUS_DOWN)
            })
        }
        BL.d("showResult==" + msg)
    }

    override fun startSearch() {
        BL.d(" startSearch current thread:" + Thread.currentThread())
        showResult("==>start search \n")
    }

    override fun searchStatus(success: Boolean) {
        showResult("==>search $success\n")
    }

    override fun startBinding() {
        showResult("==>start binding device \n")
    }

    override fun boundStatus(success: Boolean) {
        showResult("==>binding device status :$success \n")
    }

    override fun startConnect() {
        showResult("==>start connect \n")
    }

    override fun connectStatus(success: Boolean) {
        showResult("==>connect $success \n")
    }

    override fun startRead() {
        showResult("==>start read \n")
    }

    override fun reading(data: ByteArray) {
        BL.d(" reading hex data :" + HexDump.toHexString(data))
        showResult("==>receive data :${Arrays.toString(data)} \n")
    }

    override fun disconnect() {
        BL.d("disconnect current thread:" + Thread.currentThread())
        showResult("==>device disconnect \n")
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
            if (!onlyOneSend && writeGattCharacteristic != null && readGattCharacteristic != null) {
                Thread.sleep(1000)
                writeData(openData)
            }
        }
    }

    private var onlyOneSend = false

    private fun writeData(data: ByteArray) {
        if (writeGattCharacteristic != null) {
            onlyOneSend = true
            writeGattCharacteristic!!.value = data
            bluetoothLeService!!.writeCharacteristic(writeGattCharacteristic!!)
            Thread.sleep(200)
            bluetoothLeService!!.selectiveNotification(readGattCharacteristic!!, arrayOf(writeGattCharacteristic!!))
        }
    }
}