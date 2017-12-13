package com.hd.bletools.view

import android.annotation.SuppressLint
import android.app.Fragment
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hd.bluetoothutil.BluetoothController
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.utils.BL

/**
 * Created by hd on 2017/12/13 .
 *
 */
abstract class MeasureFragment : Fragment(), MeasureProgressCallback {

    protected var device: BluetoothDevice? = null

    protected var version = DeviceVersion.BLUETOOTH_2

    protected var entity = BluetoothDeviceEntity()

    @SuppressLint("SupportAnnotationUsage")
    @LayoutRes
    abstract fun setLayoutId(): Int

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

    override fun onStop() {
        super.onStop()
        stopMeasure()
    }

    fun startMeasure(callback: MeasureProgressCallback) {
        BluetoothController.init(activity.applicationContext, entity, device, callback).startMeasure()
    }

    fun stopMeasure() {
        BluetoothController.stopMeasure()
    }

    override fun startSearch() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchStatus(success: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startBinding() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun boundStatus(success: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startConnect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun connectStatus(success: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startRead() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reading(data: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disconnect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
