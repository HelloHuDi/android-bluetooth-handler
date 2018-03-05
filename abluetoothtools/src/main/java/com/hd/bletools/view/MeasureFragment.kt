package com.hd.bletools.view

import android.annotation.SuppressLint
import android.app.Fragment
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.hd.bletools.R
import com.hd.bletools.util.HexDump
import com.hd.bluetoothutil.BluetoothController
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.driver.BluetoothLeService
import com.hd.bluetoothutil.utils.BL
import kotlinx.android.synthetic.main.result.*
import java.io.OutputStream
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

/**
 * Created by hd on 2017/12/13 .
 *
 */
abstract class MeasureFragment : Fragment(), MeasureProgressCallback {

    protected var entity = BluetoothDeviceEntity()

    protected var outputStream: OutputStream? = null

    protected var bluetoothGattCharacteristic: BluetoothGattCharacteristic? = null

    protected var bluetoothLeService: BluetoothLeService? = null

    private var hexShow = false

    private var status = BleMeasureStatus.PREPARE

    private var device: BluetoothDevice? = null

    private var version = DeviceVersion.BLUETOOTH_2

    private val dataQueue = LinkedBlockingQueue<ByteArray>()

    private val callback by lazy { setMeasureProgressCallback() }

    @SuppressLint("SupportAnnotationUsage")
    @LayoutRes
    abstract fun setLayoutId(): Int

    abstract fun setMeasureProgressCallback(): MeasureProgressCallback

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        version = arguments.getSerializable("version") as DeviceVersion
        device = arguments.getParcelable("device")
        entity.deviceName = device!!.name
        entity.version = version
        entity.macAddress = device!!.address
        BL.d("measure entity :$entity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(setLayoutId(), container, false)
    }

    override fun onResume() {
        super.onResume()
        initView()
        startMeasure()
    }

    override fun onPause() {
        super.onPause()
        stopMeasure()
    }

    fun startMeasure() {
        BL.d("start measure")
        if (status == BleMeasureStatus.RUNNING) stopMeasure()
        status = BleMeasureStatus.RUNNING
        tvResult.text = ""
        showResult("==>$entity")
        snack(R.string.start_measure)
        initWriteThread()
        thread { BluetoothController.init(activity.applicationContext, entity, device, callback).startMeasure() }
    }

    fun stopMeasure() {
        BL.d("stop measure")
        status = BleMeasureStatus.STOPPING
        snack(R.string.stop_measure)
        BluetoothController.stopMeasure()
        status = BleMeasureStatus.STOPPED
    }

    fun sendData(data: ByteArray) {
        dataQueue.put(data)
    }

    fun snack(string: String) {
        Snackbar.make(sv, string, Snackbar.LENGTH_LONG).show()
    }

    fun hexShow(hexShow: Boolean) {
        this.hexShow = hexShow
    }

    fun sendData(arrayList: ArrayList<ByteArray>) {
        arrayList.forEach { sendData(it) }
    }

    private fun initWriteThread() {
        thread {
            while (status == BleMeasureStatus.RUNNING) {
                try {
                    val data = dataQueue.poll() ?: continue
                    if (data.isNotEmpty()) {
                        BL.d("write data :" + Arrays.toString(data))
                        if (version == DeviceVersion.BLUETOOTH_2) {
                            outputStream?.write(data)
                        } else {
                            bluetoothGattCharacteristic?.value = data
                            bluetoothLeService?.writeCharacteristic(bluetoothGattCharacteristic!!)
                        }
                    }
                } catch (e: Exception) {
                    BL.d("write data error :" + e)
                }
            }
        }
    }

    private val handler = android.os.Handler()

    private fun showResult(msg: String) {
        activity?.runOnUiThread {
            tvResult.append(msg)
            handler.post({
                sv.fullScroll(ScrollView.FOCUS_DOWN)
            })
        }
        BL.d("showResult==" + msg)
    }

    private fun snack(@StringRes strId: Int) {
        snack(resources.getString(strId))
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
        val result = if (hexShow) {
            HexDump.toHexString(data)
        } else {
            Arrays.toString(data)
        }
        showResult("==>receive data :$result \n")
    }

    override fun disconnect() {
        BL.d("disconnect current thread:" + Thread.currentThread())
        showResult("==>device disconnect \n")
    }
}
