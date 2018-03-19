package com.hd.practice.handler

import android.app.Activity
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.os.SystemClock
import android.widget.ScrollView
import android.widget.TextView
import com.hd.bluetoothutil.BluetoothController
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.driver.BluetoothLeService
import com.hd.bluetoothutil.utils.BL
import com.hd.practice.BuildConfig
import com.hd.practice.HexDump
import java.io.OutputStream
import java.util.*


/**
 * Created by hd on 2018/3/5 .
 * measure handler
 */
abstract class MeasureHandler(private val context: Context, private val tv: TextView, private val sv: ScrollView) //
    : MeasureBle4ProgressCallback, MeasureBle2ProgressCallback {

    init {
        BL.allowLog = BuildConfig.DEBUG
    }

    protected var bluetoothLeService: BluetoothLeService? = null

    protected val entity = BluetoothDeviceEntity()

    protected var openData: ByteArray? = null

    protected var closeData: ByteArray? = null

    var deviceName: String? = null

    open fun start() {
        //main thread
        BluetoothController.init(context = context, entity = entity, device = null, callback = MeasureHandler@ this).startMeasure()
    }

    open fun stop() {
        if (closeData!=null) {
            writeData(closeData!!)
            SystemClock.sleep(1000)
        }
        BluetoothController.stopMeasure()
    }

    open fun writeData(data: ByteArray) {

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

    override fun write(outputStream: OutputStream) {}

    override fun write(bluetoothGattCharacteristic: BluetoothGattCharacteristic?, bluetoothLeService: BluetoothLeService) {}

}