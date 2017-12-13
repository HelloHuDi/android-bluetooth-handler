package com.hd.bletools.view

import android.annotation.SuppressLint
import android.app.Fragment
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.hd.bluetoothutil.BluetoothController
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.utils.BL
import kotlinx.android.synthetic.main.result.*
import java.io.OutputStream
import java.lang.Exception
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

/**
 * Created by hd on 2017/12/13 .
 *
 */
abstract class MeasureFragment : Fragment(), MeasureProgressCallback {

    private var device: BluetoothDevice? = null

    private var version = DeviceVersion.BLUETOOTH_2

    protected var entity = BluetoothDeviceEntity()

    protected var status = BleMeasureStatus.PREPARE

    protected var outputStream: OutputStream? = null

    protected var bluetoothGattCharacteristic: BluetoothGattCharacteristic? = null

    private val dataQueue = LinkedBlockingQueue<ByteArray>()

    private val callback by lazy { setMeasureProgressCallback() }

    @SuppressLint("SupportAnnotationUsage")
    @LayoutRes
    abstract fun setLayoutId(): Int

    abstract fun setMeasureProgressCallback(): MeasureProgressCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        version = arguments.getSerializable("version") as DeviceVersion
        device = arguments.getParcelable("device")
        entity.deviceName = device!!.name
        entity.version = version
        entity.macAddress = device!!.address
        BL.d("measure entity :$entity")
        startMeasure()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(setLayoutId(), container, false)
    }

    override fun onStop() {
        stopMeasure()
        super.onStop()
    }

    fun startMeasure() {
        BL.d("start measure")
        status = BleMeasureStatus.RUNNING
        initWriteThread()
        thread { BluetoothController.init(activity.applicationContext, entity, device, callback).startMeasure() }
    }

    fun stopMeasure() {
        BL.d("stop measure")
        status = BleMeasureStatus.STOPPING
        BluetoothController.stopMeasure()
    }

    fun sendData(data: ByteArray) {
        dataQueue.put(data)
    }

    private fun initWriteThread() {
        thread {
            while (status == BleMeasureStatus.RUNNING) {
                try {
                    val data = dataQueue.take()
                    if (version == DeviceVersion.BLUETOOTH_2) {
                        outputStream?.write(data)
                    } else {
                        bluetoothGattCharacteristic?.value = data
                    }
                    BL.d("write data :" + Arrays.toString(data))
                } catch (e: Exception) {
                    BL.d("write data error :" + e)
                }
            }
        }.start()
    }

    private val handler = android.os.Handler()

    private fun showResult(msg: String) {
        activity.runOnUiThread {
            tvResult.append(msg)
            handler.post({
                sv.fullScroll(ScrollView.FOCUS_DOWN)
            })
        }
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
        showResult("==>receive data :${Arrays.toString(data)} \n")
    }

    override fun disconnect() {
        BL.d(" disconnect current thread:" + Thread.currentThread())
        showResult("==>device disconnect \n")
    }

}
