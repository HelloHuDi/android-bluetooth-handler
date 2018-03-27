package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothDevice
import com.hd.bluetoothutil.callback.BleBoundStatusCallback
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.help.BoundBluetoothDevice
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/12/10 .
 * bluetooth 2.0 device scanner
 */
class Bluetooth2Scanner : BluetoothBaseScanner() {

    override fun startScan() {
        status = BleMeasureStatus.RUNNING
        searchNativeDevice()
    }

    override fun stopScan() {
        cancelDiscovery()
        scanComplete()
        release()
    }

    private fun cancelDiscovery() {
        if (bluetoothAdapter!!.isDiscovering && bluetoothAdapter!!.isEnabled)
            bluetoothAdapter!!.cancelDiscovery()
    }

    /** querying native devices that have been bound */
    private fun searchNativeDevice() {
        BL.d("start search native device")
        val devices = bluetoothAdapter?.bondedDevices
        if (devices != null && devices.isNotEmpty()) {
            devices.filter { continueScan() }.forEach { scanComplete(it) }
            if (continueScan()) searchNearbyDevice()
        } else {
            searchNearbyDevice()
        }
    }

    /** querying nearby devices ，bound device if the device is scanned*/
    private fun searchNearbyDevice() {
        BL.d("start search nearby device ")
        BoundBluetoothDevice.newInstance(context!!)!!.boundDevice(entity!!, object : BleBoundStatusCallback {
            override fun boundStatus(discoveryFinished: Boolean, boundMap: LinkedHashMap<BluetoothDevice, Boolean>) {
                if (continueScan())
                    if (!discoveryFinished) {
                        /** maybe ,target device bound complete*/
                        boundMap.filter { continueScan() }.forEach { scanComplete(it.key) }
                    } else {
                        terminationScan()
                    }
            }
        }, object : ScannerCallback {
            override fun scan(scanComplete: Boolean, device: BluetoothDevice?) {
                if (continueScan())
                    if (device != null) {
                        /** your need bound target device,so,the same device will not be notified here */
                        if (!sameDevice(device))
                            scanComplete(device)
                    }
            }
        })
        cancelDiscovery()
        BL.d("start discovery: " + bluetoothAdapter!!.startDiscovery())
    }
}