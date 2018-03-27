package com.hd.bluetoothutil.help

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.support.annotation.StringRes
import com.hd.bluetoothutil.R
import com.hd.bluetoothutil.callback.SecurityCheckCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.utils.BL

/**
 * Created by hd on 2017/8/8 .
 * check device feature
 */
class BluetoothSecurityCheck private constructor(private var context: Context, val callback: SecurityCheckCallback?) {

    fun check(deviceVersion: DeviceVersion): BluetoothAdapter? {
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            hint(NOT_SUPPORT_BLUETOOTH, R.string.error_bluetooth_not_supported)
            return null
        }
        val bluetoothManager: BluetoothManager?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothManager = this.context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        } else {
            hint(NOT_BLUETOOTH_MANAGER, R.string.get_ble_service_error)
            return null
        }
        return getBluetoothAdapter(deviceVersion, bluetoothManager)
    }

    fun checkSameDevice(searchDevice: BluetoothDevice?, entity: BluetoothDeviceEntity?) = checkSameDevice(searchDevice, entity?.deviceName, entity?.macAddress)

    fun checkSameDevice(searchDevice: BluetoothDevice?, targetDevice: BluetoothDevice?) = checkSameDevice(searchDevice, targetDevice?.name, targetDevice?.address)

    fun checkSameDevice(searchDevice: BluetoothDevice?, targetDeviceName: String?, targetDeviceAddress: String?): Boolean {
        return if (searchDevice != null) {
            if (targetDeviceAddress.isNullOrEmpty()) {
                if (searchDevice.name.isNullOrEmpty()) {
                    false
                } else {
                    searchDevice.name == targetDeviceName
                }
            } else {
                if (targetDeviceName.isNullOrEmpty()) {
                    searchDevice.address == targetDeviceAddress
                } else {
                    searchDevice.name == targetDeviceName && searchDevice.address == targetDeviceAddress
                }
            }
        } else {
            false
        }
    }

    private fun getBluetoothAdapter(deviceVersion: DeviceVersion, bluetoothManager: BluetoothManager?): BluetoothAdapter? {
        val mBluetoothAdapter = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 && bluetoothManager != null) {
            bluetoothManager.adapter
        } else {
            BluetoothAdapter.getDefaultAdapter()
        }
        if (mBluetoothAdapter != null) {
            if (checkFeature(deviceVersion, mBluetoothAdapter))
                return mBluetoothAdapter
        } else {
            BL.e("current device without bluetooth module")
            hint(NOT_SUPPORT_BLUETOOTH, R.string.error_bluetooth_not_supported)
        }
        return null
    }

    private fun checkFeature(deviceVersion: DeviceVersion, mBluetoothAdapter: BluetoothAdapter): Boolean {
        var enable = true
        if (!mBluetoothAdapter.isEnabled) {
            enable = mBluetoothAdapter.enable()
            SystemClock.sleep(1000)
        }
        BL.d("bluetooth enable status :$enable==${mBluetoothAdapter.state}")
        if (enable && mBluetoothAdapter.state == BluetoothAdapter.STATE_ON) {
            return if (deviceVersion === DeviceVersion.BLUETOOTH_4) {
                checkBle()
            } else {
                true
            }
        } else if (enable && mBluetoothAdapter.state != BluetoothAdapter.STATE_ON) {
            SystemClock.sleep(100)
            return checkFeature(deviceVersion, mBluetoothAdapter)
        } else {
            hint(OPEN_BLUETOOTH_FAILED, R.string.open_ble_error)
        }
        return false
    }

    private fun checkBle(): Boolean {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            val permissionCheck1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            } else {
                1
            }
            val permissionCheck2 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                1
            }
            return if (permissionCheck1 == -1 || permissionCheck2 == -1) {
                hint(PERMISSION_NOT_GRANTED, R.string.not_request_bluetooth_permission)
                false
            } else
                true
        } else {
            hint(NOT_SUPPORT_BLE_4, R.string.ble_not_supported)
        }
        return false
    }

    private fun hint(errorCode: Int, @StringRes resId: Int) {
        BL.e(context.resources.getString(resId))
        callback?.securityHint(errorCode)
    }

    companion object {

        const val NOT_SUPPORT_BLE_4 = -1

        const val PERMISSION_NOT_GRANTED = -2

        const val OPEN_BLUETOOTH_FAILED = -3

        const val NOT_SUPPORT_BLUETOOTH = -4

        const val NOT_BLUETOOTH_MANAGER = -5

        fun newInstance(context: Context, callback: SecurityCheckCallback? = null): BluetoothSecurityCheck {
            return BluetoothSecurityCheck(context.applicationContext, callback)
        }
    }
}
