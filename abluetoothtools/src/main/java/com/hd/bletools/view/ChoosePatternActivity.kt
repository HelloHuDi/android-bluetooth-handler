package com.hd.bletools.view

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.hd.bletools.R
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.scan.Scanner

class ChoosePatternActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_pattern)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scan_type, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scan2 -> scan(DeviceVersion.BLUETOOTH_2)
            R.id.scan4 -> scan(DeviceVersion.BLUETOOTH_4)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun scan(version: DeviceVersion) {
        Scanner.scan(this, entity = BluetoothDeviceEntity(version = version),
                callback = object : ScannerCallback {
            override fun scan(scanComplete: Boolean, device: BluetoothDevice?) {

            }
        })
    }
}
