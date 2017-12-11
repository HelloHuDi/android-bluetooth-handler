package com.hd.bluetoothutil.scan

import android.bluetooth.BluetoothDevice
import android.os.SystemClock
import com.hd.bluetoothutil.callback.BleBoundStatusCallback
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.help.BluetoothSecurityCheck
import com.hd.bluetoothutil.help.BoundBluetoothDevice
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/12/10 .
 * bluetooth 2.0 device scanner
 */
class Bluetooth2Scanner : BluetoothBaseScanner() {

    override fun startScan() {
        searchNativeDevice()
    }

    override fun stopScan() {
        BL.d("stop scan ")
        status = BleMeasureStatus.STOPPING
        if (bluetoothAdapter!!.isDiscovering) bluetoothAdapter!!.cancelDiscovery()
        scanComplete()
        status = BleMeasureStatus.STOPPED
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
        BL.d("start search nearby device")
        bluetoothAdapter!!.startDiscovery()
        BoundBluetoothDevice.newInstance(context!!).boundDevice(entity!!, object : BleBoundStatusCallback {
            override fun boundStatus(discoveryFinished: Boolean, boundMap: LinkedHashMap<BluetoothDevice, Boolean>) {
                if (continueScan())
                    if (!discoveryFinished) {
                        boundMap.filter { continueScan() }.forEach { scanComplete(it.key) }
                    } else {
                        stopScan()
                    }
            }
        }, object : ScannerCallback {
            override fun scan(scanComplete: Boolean, device: BluetoothDevice?) {
                if (continueScan()) {
                    if (device != null) {
                        /** sleep 200 millisecond, completion of device binding*/
                        if (BluetoothSecurityCheck.newInstance(context!!).checkSameDevice(device, entity)
                                && device.bondState != BluetoothDevice.BOND_BONDED) {
                            SystemClock.sleep(200)
                        }
                        scanComplete(device)
                    }
                }
            }
        })
    }

}