package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/12/10 .
 * bluetooth 4.0 device scanner
 */
class Bluetooth4Scanner : BluetoothBaseScanner() {

    private var mBluetoothLeScanner: BluetoothLeScanner? = null

    private val handler = Handler()

    private val stopRunnable = stopRunnable()

    override fun startScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && bluetoothAdapter!!.isOffloadedScanBatchingSupported) {
            mBluetoothLeScanner = bluetoothAdapter!!.bluetoothLeScanner
            BL.d("=" + bluetoothAdapter!!.isOffloadedFilteringSupported + "=" + mBluetoothLeScanner)
        }
        handler.postDelayed(stopRunnable, 10000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mBluetoothLeScanner != null) {
            mBluetoothLeScanner!!.startScan(mScanCallback)
        } else {
            bluetoothAdapter!!.startLeScan(leScanCallback)
        }
    }

    private fun stopRunnable() = Runnable {
        BL.d("stop scan ble after 10000 millisecond")
        stopScan()
    }

    override fun stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mBluetoothLeScanner != null) {
            BL.d("$mBluetoothLeScanner=$mScanCallback")
            mBluetoothLeScanner?.stopScan(mScanCallback)
        } else {
            bluetoothAdapter!!.stopLeScan(leScanCallback)
        }
        handler.removeCallbacks(stopRunnable)
        scanComplete()
    }

    private val leScanCallback = BluetoothAdapter.LeScanCallback { bluetoothDevice, _, _ ->
        scanComplete(bluetoothDevice)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if (continueScan())
                scanComplete(result.device)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            if (continueScan())
                results.filter { continueScan() }.forEach { scanComplete(it.device) }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            BL.d("scan failed :" + errorCode)
            stopScan()
        }
    }
}