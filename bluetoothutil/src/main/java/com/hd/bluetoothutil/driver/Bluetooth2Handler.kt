package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.SystemClock
import com.hd.bluetoothutil.R
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.utils.BL
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.*


/**
 * Created by hd on 2017/9/1 .
 *
 */
class Bluetooth2Handler(context: Context, entity: BluetoothDeviceEntity,
                        bluetoothAdapter: BluetoothAdapter, callback: MeasureBle2ProgressCallback)
    : BluetoothHandler(context, entity, bluetoothAdapter, callback) {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var bluetoothSocket: BluetoothSocket? = null

    override fun start() {
        startScan()
    }

    override fun release() {
        clear()
    }

    private fun clear() {
        try {
            inputStream?.close()
        } catch (e: IOException) {
            BL.d("inputStream close error :" + e)
        } finally {
            inputStream = null
        }
        try {
            outputStream?.close()
        } catch (e: IOException) {
            BL.d("outputStream close error :" + e)
        } finally {
            outputStream = null
        }
        closeSocket()
    }

    private fun closeSocket() {
        try {
            if (bluetoothSocket != null && bluetoothSocket!!.isConnected)
                bluetoothSocket!!.close()
        } catch (e: IOException) {
            BL.d("bluetoothSocket close error :" + e)
        }
    }

    override fun startConnect() {
        BL.d("start connect device : $targetDevice + ${targetDevice!!.bondState}")
        if (targetDevice!!.bondState != BluetoothDevice.BOND_BONDED) {
            BL.e("device is not bound ")
            SystemClock.sleep(100)
        }
        if (targetDevice!!.bondState != BluetoothDevice.BOND_BONDED) {
            callback.error(context.resources.getString(R.string.binding_failed))
        } else {
            Thread(ConnectRunnable(targetDevice!!)).start()
        }
    }

    private inner class ConnectRunnable internal constructor(private val device: BluetoothDevice) : Runnable {

        init {
            BL.d("connect runnable:$device=$status")
        }

        override fun run() {
            if (status !== BleMeasureStatus.RUNNING) return
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(GattAttributeResolver.SPP))
            } catch (e: IOException) {
                BL.d("createRfcommSocketToServiceRecord error :" + e)
            }
            if (bluetoothSocket == null) {
                BL.d("connect error ,the bluetoothSocket is null ")
                reconnected()
                return
            }
            callback.startConnect()
            if (!BluetoothConnector.newInstance().connectSocket(device, bluetoothSocket!!)) {
                BL.d("connect again time ：$reconnected_number=$status")
                reconnected()
                return
            } else {
                callback.connectStatus(true)
            }
            try {
                inputStream = bluetoothSocket!!.inputStream
            } catch (e: IOException) {
                BL.d("bluetoothSocket inputStream error :" + e)
            }
            try {
                outputStream = bluetoothSocket!!.outputStream
            } catch (e: IOException) {
                BL.d("bluetoothSocket outputStream error :" + e)
            }
            callback.startRead()
            reconnected_number = default_reconnected_number
            while (status === BleMeasureStatus.RUNNING) {
                if (reading()) return
            }
        }

        private val byteBuffer = ByteBuffer.allocate(1024)

        private fun reading(): Boolean {
            if (outputStream != null && inputStream != null) {
                try {
                    (callback as MeasureBle2ProgressCallback).write(outputStream!!)
                } catch (ignored: Exception) {
                    BL.d("write data error ：" + ignored)
                }
                val len: Int
                try {
                    len = inputStream!!.read(byteBuffer.array())
                } catch (ignored: Exception) {
                    BL.d("reading error ：$status==$ignored=$reconnected_number")
                    reconnected()
                    return true
                }
                if (len > 0) {
                    val result = ByteArray(len)
                    byteBuffer.get(result, 0, len)
                    reading(result)
                }
                byteBuffer.clear()
                return false
            } else {
                error(NullPointerException(Bluetooth2Handler::class.java.simpleName))
            }
            return true
        }

        private fun error(e: Exception? = null) {
            BL.d("bluetooth handler2 error :" + e)
            stopMeasure()
            callback.disconnect()
            callback.error()
        }

        private val default_reconnected_number = 5

        private var reconnected_number = default_reconnected_number

        private fun reconnected() {
            if (status === BleMeasureStatus.RUNNING && reconnected_number > 0 && entity.reconnected) {
                reconnected_number--
                BL.d("reconnected:" + reconnected_number)
                closeSocket()
                SystemClock.sleep(200)
                run()
            } else {
                callback.connectStatus(false)
                error(IOException(Bluetooth2Handler::class.java.simpleName))
            }
        }
    }

}