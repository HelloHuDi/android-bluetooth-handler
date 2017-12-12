package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattCharacteristic
import android.content.*
import android.os.IBinder
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.utils.BL
import java.util.*


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
            val connectStatus = mbluetoothLeService!!.connect(targetDevice?.address)
            callback.connectStatus(connectStatus)
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
            val connectStatus = mbluetoothLeService!!.connect(targetDevice!!.address)
            callback.connectStatus(connectStatus)
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
            callback.startConnect()
            //Automatically connects to the device upon successful start-up initialization.
            val connectStatus = mbluetoothLeService!!.connect(targetDevice!!.address)
            BL.d("start connect service ,current device type :" + //
                    BluetoothClassResolver.resolveDeviceClass(targetDevice!!.bluetoothClass.deviceClass)//
                    + " , it's MajorDeviceClass :" + //
                    BluetoothClassResolver.resolveMajorDeviceClass(targetDevice!!.bluetoothClass.majorDeviceClass)
                    + "==connect status :" + connectStatus)
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
                        BL.d("connect again :" + connectStatus)
                        callback.connectStatus(connectStatus)
                    } else {
                        BL.d("device disconnect ")
                        callback.disconnect()
                        mbluetoothLeService?.disconnect()
                    }
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    setNotification()
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

    private fun setNotification() {
        if (mbluetoothLeService == null) return
        val thread = Thread(Runnable {
            val setNotifition = booleanArrayOf(false)
            val mNotifyCharacteristic = arrayOfNulls<BluetoothGattCharacteristic>(1)
            // Show all the supported services and characteristics on the user interface.
            val supportedGattServices = mbluetoothLeService!!.supportedGattServices
            if (supportedGattServices != null) {
                for (bluetoothGattService in supportedGattServices) {
                    if (setNotifition[0])
                        return@Runnable
                    for (bluetoothGattCharacteristic in bluetoothGattService.characteristics) {
                        if (setNotifition[0])
                            return@Runnable
                        if (bluetoothGattCharacteristic == null)
                            continue
                        setNotifition(bluetoothGattCharacteristic, setNotifition, mNotifyCharacteristic)
                    }
                }
            }
        })
        thread.start()
    }

    private fun setNotifition(bluetoothGattCharacteristic: BluetoothGattCharacteristic, //
                              setNotifition: BooleanArray, mNotifyCharacteristic: Array<BluetoothGattCharacteristic?>) {
        BL.d("start write data to device: " + Arrays.toString(bluetoothGattCharacteristic.value) //
                + "==" + entity.targetCharacteristicUuid + "==" + bluetoothGattCharacteristic.uuid)
        val hasTarget = entity.targetCharacteristicUuid != null && bluetoothGattCharacteristic.uuid == entity.targetCharacteristicUuid
        if (entity.targetCharacteristicUuid != null) {
            if (hasTarget) {
                (callback as MeasureBle4ProgressCallback).write(bluetoothGattCharacteristic, mbluetoothLeService!!)
                mbluetoothLeService!!.setCharacteristicNotification(bluetoothGattCharacteristic, true)
                setNotifition[0] = true
            } else {
                BL.d("do not set notification :" + bluetoothGattCharacteristic.uuid)
            }
        } else {
            notifitionAll(bluetoothGattCharacteristic, mNotifyCharacteristic)
        }
    }

    private fun notifitionAll(bluetoothGattCharacteristic: BluetoothGattCharacteristic, mNotifyCharacteristic: Array<BluetoothGattCharacteristic?>) {
        val charaProp = bluetoothGattCharacteristic.properties
        (callback as MeasureBle4ProgressCallback).write(bluetoothGattCharacteristic, mbluetoothLeService!!)
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_READ > 0) {
            // If there is an active notification on a characteristic, clear
            // it first so it doesn't update the data field on the user interface.
            if (mNotifyCharacteristic[0] != null) {
                mbluetoothLeService!!.setCharacteristicNotification(mNotifyCharacteristic[0]!!, false)
                mNotifyCharacteristic[0] = null
            }
            mbluetoothLeService!!.readCharacteristic(bluetoothGattCharacteristic)
        }
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
            mNotifyCharacteristic[0] = bluetoothGattCharacteristic
            mbluetoothLeService!!.setCharacteristicNotification(bluetoothGattCharacteristic, true)
        }
    }
}