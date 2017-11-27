package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.*
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import com.hd.bluetoothutil.callback.MeasureBle4Callback
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
                        bluetoothAdapter: BluetoothAdapter, val callback: MeasureBle4Callback)
    : BluetoothHandler(context, entity, bluetoothAdapter, callback) {

    private var mTargetDevice: BluetoothDevice? = null
    private var mbluetoothLeService: BluetoothLeService? = null
    private var mBluetoothLeScanner: BluetoothLeScanner? = null

    override fun start() {
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter())
        if (mbluetoothLeService != null) {
            val connectStatus = mbluetoothLeService?.connect(mTargetDevice?.address)
            progressCallback?.connectStatus(connectStatus!!)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
            }
            BL.d("startScan current device sdk :" + Build.VERSION.SDK_INT + "==" + mBluetoothLeScanner)
            startScan()
        }
    }

    override fun release() {
        unBindAndRegister()
    }

    private fun unBindAndRegister() {
        if (mbluetoothLeService != null) {
            context.unbindService(mServiceConnection)
            mbluetoothLeService.disconnect()
            mbluetoothLeService.close()
            mbluetoothLeService = null
        }
        if (mGattUpdateReceiver != null) {
            context.unregisterReceiver(mGattUpdateReceiver)
            mGattUpdateReceiver = null
        }
        bluetoothAdapter = null
    }

    private fun startScan() {
        if (progressCallback != null)
            progressCallback.startSearch()
        Handler(context.mainLooper).postDelayed(Runnable {
            BL.logcat("stop scan ble after 30000 millisecond")
            stopScan()
        }, 30000)
        BL.d("startScan current device sdk :" + Build.VERSION.SDK_INT + "==" + mBluetoothLeScanner)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner.startScan(mScanCallback)
        } else {
            bluetoothAdapter.startLeScan(leScanCallback)
        }
    }

    private fun stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner.stopScan(mScanCallback)
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val currentDeviceName = result.device.name
            BL.logcat("found current device name is ：$currentDeviceName  ,the target device name ：$deviceName")
            if (currentDeviceName != null && currentDeviceName == deviceName) {
                if (progressCallback != null)
                    progressCallback.searchStatus(true)
                stopScan()
                mTargetDevice = result.device
                openService()
            }
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            for (scanresult in results) {
                val currentDeviceName = scanresult.device.name
                if (currentDeviceName != null && currentDeviceName == deviceName) {
                    if (progressCallback != null)
                        progressCallback.searchStatus(true)
                    stopScan()
                    mTargetDevice = scanresult.device
                    openService()
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            BL.logcat("not found device and scan again ,current scan count :" + AGAIN_TIME)
            if (AGAIN_TIME > 0) {
                sleep(300)
                startScan()
                AGAIN_TIME--
            } else {
                stopScan()
                if (progressCallback != null)
                    progressCallback.searchStatus(false)
            }
        }
    }

    private fun sleep(millis: Int) {
        try {
            Thread.sleep(millis.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private var AGAIN_TIME = 3

    private val leScanCallback = BluetoothAdapter.LeScanCallback { bluetoothDevice, i, bytes ->
        if (bluetoothDevice.name == deviceName) {
            if (progressCallback != null)
                progressCallback.searchStatus(true)
            stopScan()
            BL.logcat("has been scanning to the target device :" + deviceName)
            mTargetDevice = bluetoothDevice
            openService()
        }
    }

    private fun openService() {
        if (mbluetoothLeService != null) {
            val connectStatus = mbluetoothLeService.connect(mTargetDevice.getAddress())
            if (progressCallback != null)
                progressCallback.connectStatus(connectStatus)
        } else {
            val gattServiceIntent = Intent(context, BluetoothLeService::class.java)
            context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    // Code to manage Service lifecycle.
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            mbluetoothLeService = (service as BluetoothLeService.LocalBinder).getService()
            if (!mbluetoothLeService.initialize()) {
                BL.d("initialize unSuccess")
                callback.disconnect()
                mbluetoothLeService.disconnect()
                return
            }
            //Automatically connects to the device upon successful start-up initialization.
            val connectStatus = mbluetoothLeService.connect(mTargetDevice.getAddress())
            BL.d("start connect service ,current device type :" + BluetoothClassResolver.resolveDeviceClass(mTargetDevice.getBluetoothClass().getDeviceClass()) + " , it's MajorDeviceClass :" + BluetoothClassResolver.resolveMajorDeviceClass(mTargetDevice.getBluetoothClass().getMajorDeviceClass()))
            if (progressCallback != null)
                progressCallback.connectStatus(connectStatus)
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
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                callback.connect()
                BL.logcat("start connect ")
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                if (status === BleMeasureStatus.RUNNING && mbluetoothLeService != null) {
                    mbluetoothLeService.connect(mTargetDevice.getAddress())//connect again
                    BL.logcat("connect again")
                } else {
                    BL.logcat("device disconnect ")
                    callback.disconnect()
                    if (mbluetoothLeService != null)
                        mbluetoothLeService.disconnect()
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                setNotification()
                if (progressCallback != null)
                    progressCallback.startRead()
                BL.logcat("service discovered and set notification")
            } else {
                val readByte = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA)
                BL.logcat("data be update and start receive :" + HexDump.toHexString(readByte) + "==" + Arrays.toString(readByte))
                (callback as Ble4Callback).read(readByte)
                if (progressCallback != null)
                    progressCallback.reading()
            }
        }
    }


}