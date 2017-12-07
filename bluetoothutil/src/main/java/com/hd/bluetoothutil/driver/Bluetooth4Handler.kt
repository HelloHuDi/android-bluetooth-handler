package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.*
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.support.annotation.RequiresApi
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.device.BluetoothDeviceEntity
import com.hd.bluetoothutil.utils.BL
import com.hd.bluetoothutil.utils.HexDump
import java.util.*


/**
 * Created by hd on 2017/9/1 .
 *
 */
class Bluetooth4Handler(context: Context, entity: BluetoothDeviceEntity,
                        bluetoothAdapter: BluetoothAdapter, val callback: MeasureBle4ProgressCallback)
    : BluetoothHandler(context, entity, bluetoothAdapter, callback) {

    private var targetDevice: BluetoothDevice? = null
    private var mbluetoothLeService: BluetoothLeService? = null
    private var mBluetoothLeScanner: BluetoothLeScanner? = null

    override fun start() {
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter())
        if (mbluetoothLeService != null) {
            val connectStatus=mbluetoothLeService!!.connect(targetDevice?.address)
            callback.connectStatus(connectStatus)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
            }
            BL.d("startScan current device sdk :" + Build.VERSION.SDK_INT + "==" + mBluetoothLeScanner)
            callback.startSearch()
            startScan()
        }
    }

    override fun release() {
        unBindAndRegister()
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

    private fun startScan() {
        Handler(context.mainLooper).postDelayed({
            BL.d("stop scan ble after 30000 millisecond")
            stopScan()
        }, 30000)
        BL.d("startScan current device sdk :" + Build.VERSION.SDK_INT + "==" + mBluetoothLeScanner)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner!!.startScan(mScanCallback)
        } else {
            bluetoothAdapter.startLeScan(leScanCallback)
        }
    }

    private fun stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner?.stopScan(mScanCallback)
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            scanComplete(result.device)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            for (sanest in results) {
                scanComplete(sanest.device)
            }
        }

        private var AGAIN_TIME = 3

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            BL.d("not found device and scan again ,current scan count :" + AGAIN_TIME)
            if (AGAIN_TIME > 0) {
                SystemClock.sleep(300)
                startScan()
                AGAIN_TIME--
            } else {
                stopScan()
                callback.searchStatus(false)
            }
        }
    }

    private fun scanComplete(targetDevice: BluetoothDevice) {
        BL.d("found current device name is ：${targetDevice.name}  ,the target device name ：${entity.deviceName}")
        if (checkSameDevice(targetDevice)) {
            callback.searchStatus(true)
            stopScan()
            this.targetDevice = targetDevice
            openService()
        }
    }

    private val leScanCallback = BluetoothAdapter.LeScanCallback { bluetoothDevice, _, _ ->
        scanComplete(bluetoothDevice)
    }

    private fun openService() {
        if (mbluetoothLeService != null) {
            val connectStatus = mbluetoothLeService!!.connect(targetDevice!!.address)
            callback.connectStatus(connectStatus)
        } else {
            val gattServiceIntent = Intent(context, BluetoothLeService::class.java)
            context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
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
            //Automatically connects to the device upon successful start-up initialization.
            val connectStatus = mbluetoothLeService!!.connect(targetDevice!!.address)
            BL.d("start connect service ,current device type :" + //
                    BluetoothClassResolver.resolveDeviceClass(targetDevice!!.bluetoothClass.deviceClass)//
                    + " , it's MajorDeviceClass :" + //
                    BluetoothClassResolver.resolveMajorDeviceClass(targetDevice!!.bluetoothClass.majorDeviceClass))
            callback.connectStatus(connectStatus)
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
            if (BluetoothLeService.ACTION_GATT_CONNECTED == action) {
                callback.startConnect()
                BL.d("start connect ")
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED == action) {
                if (status === BleMeasureStatus.RUNNING && mbluetoothLeService != null) {
                   val connectStatus= mbluetoothLeService!!.connect(targetDevice!!.address)//connect again
                    BL.d("connect again")
                    callback.connectStatus(connectStatus)
                } else {
                    BL.d("device disconnect ")
                    callback.disconnect()
                    mbluetoothLeService?.disconnect()
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED == action) {
                setNotification()
                callback.startRead()
                BL.d("service discovered and set notification")
            } else {
                val readByte = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA)
                BL.d("data be update and start receive :" + HexDump.toHexString(readByte) + "==" + Arrays.toString(readByte))
                callback.reading(readByte)
            }
        }
    }

    private fun setNotification() {
        if (mbluetoothLeService == null)return
        val thread = Thread(Runnable {
            val setNotifition = booleanArrayOf(false)
            val mNotifyCharacteristic = arrayOfNulls<BluetoothGattCharacteristic>(1)
            // Show all the supported services and characteristics on the user interface.
            val supportedGattServices=mbluetoothLeService!!.supportedGattServices
            if(supportedGattServices!=null) {
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
        callback.write(bluetoothGattCharacteristic, mbluetoothLeService!!)
        BL.d("start write data to device: " + Arrays.toString(bluetoothGattCharacteristic.value) //
                + "==" + entity.targetCharacteristicUuid + "==" + bluetoothGattCharacteristic.uuid)
        val hasTarget = entity.targetCharacteristicUuid  != null && bluetoothGattCharacteristic.uuid == entity.targetCharacteristicUuid
        if (entity.targetCharacteristicUuid != null) {
            if (hasTarget) {
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