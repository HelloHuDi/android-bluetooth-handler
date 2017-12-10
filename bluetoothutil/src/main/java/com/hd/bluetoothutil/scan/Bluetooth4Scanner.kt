package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Build
import android.os.Handler
import android.os.SystemClock
import android.support.annotation.RequiresApi
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/12/10 .
 * bluetooth 4.0 device scanner
 */
class Bluetooth4Scanner : BluetoothScanner() {

    private var mBluetoothLeScanner: BluetoothLeScanner? = null

    override fun startScan(bluetoothAdapter: BluetoothAdapter, version: DeviceVersion, callback: ScannerCallback) {
        this.bluetoothAdapter = bluetoothAdapter
        this.callback = callback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
        startScan()
    }

    private fun startScan() {
        Handler().postDelayed({
            BL.d("stop scan ble after 30000 millisecond")
            stopScan()
        }, 30000)
        BL.d("startScan current device sdk :" + Build.VERSION.SDK_INT + "==" + mBluetoothLeScanner)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner!!.startScan(mScanCallback)
        } else {
            bluetoothAdapter!!.startLeScan(leScanCallback)
        }
    }

    private fun stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner?.stopScan(mScanCallback)
        } else {
            bluetoothAdapter!!.stopLeScan(leScanCallback)
        }
        scanComplete(true, null)
    }

    private val leScanCallback = BluetoothAdapter.LeScanCallback { bluetoothDevice, _, _ ->
        scanComplete(false, bluetoothDevice)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            scanComplete(false, result.device)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            for (sanest in results) {
                scanComplete(false, sanest.device)
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
            }
        }
    }

    private val devices = mutableListOf<BluetoothDevice>()

    private fun scanComplete(scanComplete: Boolean, targetDevice: BluetoothDevice?) {
        BL.d("found current device is ：$targetDevice")
        if (targetDevice != null)
            devices.add(targetDevice)
        callback!!.scan(scanComplete, devices)
    }

}