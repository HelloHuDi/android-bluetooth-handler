package com.hd.bluetoothutil.help

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import com.hd.bluetoothutil.callback.BleBoundProgressCallback
import com.hd.bluetoothutil.utils.BL
import com.hd.bluetoothutil.utils.ClsUtils
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by hd on 2017/6/17.
 * automatic binding equipment
 */
class BleBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (callbackWeakReference == null)
            return
        val action = intent.action
        val callback = callbackWeakReference!!.get()
        if (callback != null) {
            when (action) {
                BluetoothDevice.ACTION_FOUND -> foundDevice(context, intent)
                BluetoothDevice.ACTION_PAIRING_REQUEST -> bondDevice(intent)
                BluetoothDevice.ACTION_BOND_STATE_CHANGED ->
                    callback.actionBondStateChanged(intent.getParcelableExtra<Parcelable>(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice)
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val extraState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    val extraPreviousState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1)
                    BL.d("current bluetooth status :$extraState,previous bluetooth status :$extraPreviousState")
                    callback.actionStateChanged(extraState, extraPreviousState)
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> callback.actionDiscoveryFinished(searchComplete.get())
            }
        } else {
            BL.d("BleBoundProgressCallback is null")
        }
    }

    private fun bondDevice(intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        BL.d("bond device ,set pin :" + device + "=" + callbackWeakReference?.get()?.deviceName)
        if (device != null && device.name != null && device.name == callbackWeakReference?.get()?.deviceName) {
            abortBroadcast()
            setPain(0, device)
        }
    }

    private fun setPain(index: Int, device: BluetoothDevice) {
        var position = index
        var pair = false
        try {
            val painCode = pairArray!![position]
            BL.d("automatic matching count :$position=$painCode")
            if (painCode != null) {
                pair = ClsUtils.setPin(device.javaClass, device, painCode)
                BL.d("bond device success :" + pair + "automatic matching complete")
            }
        } catch (ignored: Exception) {
            pair = false
        }
        BL.d("automatic matching bond state :" + pair + "==" + device.bondState + "=" + position)
        if (!pair) {
            if (++position >= pairArray!!.size)
                return
            setPain(position, device)
        }
    }

    private fun foundDevice(context: Context, intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        BL.d("found device :"+device)
        if (BluetoothSecurityCheck.newInstance(context).checkSameDevice(device,
                callbackWeakReference!!.get()?.deviceName, callbackWeakReference!!.get()?.macAddress)) {
            startBound(device)
        }
    }

    private fun startBound(device: BluetoothDevice) {
        searchComplete.set(true)
        BL.d("found target device :" + device.name + "=" + searchComplete)
        if (BluetoothDevice.BOND_NONE == device.bondState) {
            var bondSuccess: Boolean
            try {
                bondSuccess = ClsUtils.createBond(device.javaClass, device)
                BL.d("start bond device 1：" + bondSuccess)
            } catch (e: Exception) {
                bondSuccess = false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !bondSuccess) {
                BL.d("start bond device 2")
                device.createBond()
            }
        } else {
            BL.d("target device bond status :" + device.bondState)
        }
    }

    companion object {
        private var pairArray: Array<String?>? = null
        private val searchComplete = AtomicBoolean(false)
        private var callbackWeakReference: WeakReference<BleBoundProgressCallback>? = null

        fun newInstance(bleBoundProgressCallback: BleBoundProgressCallback) {
            callbackWeakReference = WeakReference(bleBoundProgressCallback)
            pairArray = arrayOf(bleBoundProgressCallback.pin, "1234", "0000")
        }

        fun clear() {
            BL.d("broad cast receiver clear callback")
            if (callbackWeakReference != null && callbackWeakReference!!.get() != null) {
                callbackWeakReference!!.clear()
            }
            searchComplete.set(false)
        }
    }
}
