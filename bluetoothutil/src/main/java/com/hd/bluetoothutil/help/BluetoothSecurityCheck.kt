package com.hd.bluetoothutil.help

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.support.annotation.StringRes
import android.widget.Toast

import com.hd.bluetoothutil.R
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.utils.BL

/**
 * Created by hd on 2017/8/8 .
 * check device feature
 */
class BluetoothSecurityCheck private constructor(var context: Context) {

    fun check(deviceVersion: DeviceVersion): BluetoothAdapter? {
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            showToast(R.string.error_bluetooth_not_supported)
            return null
        }
        var bluetoothManager: BluetoothManager? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothManager = this.context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        }
        return getBluetoothAdapter(deviceVersion, bluetoothManager)
    }

    private fun getBluetoothAdapter(deviceVersion: DeviceVersion, bluetoothManager: BluetoothManager?): BluetoothAdapter? {
        val mBluetoothAdapter: BluetoothAdapter?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 && bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.adapter
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        }
        if (mBluetoothAdapter != null) {
            if (checkFeature(deviceVersion, mBluetoothAdapter))
                return mBluetoothAdapter
        } else {
            BL.e("current device without bluetooth module")
            showToast(R.string.error_bluetooth_not_supported)
        }
        return null
    }

    private fun checkFeature(deviceVersion: DeviceVersion, mBluetoothAdapter: BluetoothAdapter): Boolean {
        var enable = true
        if (!mBluetoothAdapter.isEnabled) {
            enable = mBluetoothAdapter.enable()
            SystemClock.sleep(1000)
        }
        if (enable) {
            if (deviceVersion === DeviceVersion.BLUETOOTH_4) {
                return checkBle()
            } else {
                return true
            }
        } else {
            showToast(R.string.open_ble_error)
        }
        return false
    }

    private fun checkBle(): Boolean {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                val permissionCheck1 = context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                val permissionCheck2 = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionCheck1 == -1 || permissionCheck2 == -1) {
                    showToast(R.string.not_request_bluetooth_permission)
                    return false
                }
            }
            return true
        } else {
            showToast(R.string.ble_not_supported)
        }
        return false
    }

    private fun showToast(@StringRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    companion object {

        fun newInstance(context: Context): BluetoothSecurityCheck {
            return BluetoothSecurityCheck(context)
        }
    }
}
