package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.content.*
import android.os.IBinder
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/9/1 .
 *
 */
class Bluetooth4Handler(context: Context, entity: BluetoothDeviceEntity,
                        bluetoothAdapter: BluetoothAdapter, callback: MeasureBle4ProgressCallback)
    : BluetoothHandler(context, entity, bluetoothAdapter, callback), ScannerCallback {

    private var mbluetoothLeService: BluetoothLeService? = null

    override fun start() {
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter())
        if (mbluetoothLeService != null) {
            connectService()
        } else {
            startScan()
        }
    }

    override fun release() {
        unBindAndRegister()
    }

    override fun startConnect() {
        callback.boundStatus(true)
        if (mbluetoothLeService != null) {
            connectService()
        } else {
            val gattServiceIntent = Intent(context, BluetoothLeService::class.java)
            context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unBindAndRegister() {
        if (mbluetoothLeService != null) {
            context.unbindService(mServiceConnection)
            mbluetoothLeService!!.disconnect()
            mbluetoothLeService!!.close()
            mbluetoothLeService = null
        }
        if (mGattUpdateReceiver != null) {
            context.unregisterReceiver(mGattUpdateReceiver)
            mGattUpdateReceiver = null
        }
    }

    private fun connectService() {
        callback.startConnect()
        //Automatically connects to the device upon successful start-up initialization.
        val connectStatus = mbluetoothLeService!!.connect(targetDevice!!.address)
        BL.d("start connect service ,current device type :" + //
                BluetoothClassResolver.resolveDeviceClass(targetDevice!!.bluetoothClass.deviceClass)//
                + " , it's MajorDeviceClass :" + //
                BluetoothClassResolver.resolveMajorDeviceClass(targetDevice!!.bluetoothClass.majorDeviceClass)
                + "==connect status :" + connectStatus)
        (callback as MeasureBle4ProgressCallback).write(bluetoothLeService = mbluetoothLeService!!)
    }

    // Code to manage Service lifecycle.
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            mbluetoothLeService = (service as BluetoothLeService.LocalBinder).service
            if (!mbluetoothLeService!!.initialize()) {
                BL.d("initialize unSuccess")
                callback.disconnect()
                mbluetoothLeService!!.disconnect()
                return
            }
            connectService()
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            callback.disconnect()
        }
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_BE_READ)
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_BE_UPDATATED)
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_BE_WRITE)
        return intentFilter
    }

    private var mGattUpdateReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when (action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    callback.connectStatus(true)
                    BL.d("start connect ")
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    if (status === BleMeasureStatus.RUNNING && mbluetoothLeService != null && entity.reconnected) {
                        //try reconnect if the equipment is disconnected at during operation
                        val connectStatus = mbluetoothLeService!!.connect(targetDevice!!.address)
                        BL.d("connect again :$connectStatus")
                        callback.connectStatus(connectStatus)
                    } else {
                        BL.d("device disconnect ")
                        callback.disconnect()
                        mbluetoothLeService?.disconnect()
                    }
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    mbluetoothLeService?.setNotification(entity, callback)
                    callback.startRead()
                    BL.d("service discovered and set notification")
                }
                else -> {
                    val readByte = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA)
                    reading(readByte)
                }
            }
        }
    }
}