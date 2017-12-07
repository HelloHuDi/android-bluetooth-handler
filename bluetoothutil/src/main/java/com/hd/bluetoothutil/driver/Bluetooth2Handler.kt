package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.SystemClock
import com.hd.bluetoothutil.callback.BleBoundStatusCallback
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import com.hd.bluetoothutil.config.BleMeasureStatus
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.help.BluetoothSecurityCheck
import com.hd.bluetoothutil.help.BoundBluetoothDevice
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
        searchDevice()
    }

    override fun release() {
        clear()
    }

    private fun cancelSearch() {
        if (bluetoothAdapter.isDiscovering) bluetoothAdapter.cancelDiscovery()
    }

    private fun searchDevice() {
        callback.startSearch()
        val bound = BoundBluetoothDevice.newInstance(context, object : BleBoundStatusCallback {
            override fun boundStatus(boundMap: LinkedHashMap<BluetoothDevice, Boolean>) {
                for ((bluetoothDevice, boundStatus) in boundMap) {
                    if (boundStatus) {
                        BL.d("device status is binding , start connect and measure")
                        callback.searchStatus(true)
                        cancelSearch()
                        connectDevice(bluetoothDevice)
                    } else {
                        startBound()
                    }
                }
            }
        }).queryBoundStatus(entity)
        if (!bound) startBound()
    }

    private fun startBound() {
        var searchComplete = false
        BoundBluetoothDevice.newInstance(context, object : BleBoundStatusCallback {
            override fun boundStatus(boundMap: LinkedHashMap<BluetoothDevice, Boolean>) {
                for ((device, bound) in boundMap) {
                    if (BluetoothSecurityCheck.newInstance(context).checkSameDevice(device,entity)
                            && bound && !searchComplete) {
                        searchComplete = true
                        cancelSearch()
                        startConnect(device)
                        break
                    }
                }
                callback.searchStatus(searchComplete)
            }
        }).boundDevice(entity)
        BL.d("start found target device")
        bluetoothAdapter.startDiscovery()
    }

    private fun clear() {
        cancelSearch()
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

    private val byteBuffer = ByteBuffer.allocate(1024)

    private fun connectDevice(ownDevice: BluetoothDevice) {
        Thread(ConnectRunnable(ownDevice)).start()
    }

    private fun startConnect(device: BluetoothDevice) {
        cancelSearch()
        connectDevice(device)
    }

    private val default_connect_again_time = 5

    /**
     * connect again count
     */
    private var connect_again_time = default_connect_again_time

    private inner class ConnectRunnable internal constructor(private val device: BluetoothDevice) : Runnable {

        init {
            BL.d("connect runnable:$device=$status")
        }

        override fun run() {
            if (status !== BleMeasureStatus.RUNNING) return
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(GattAttributeResolver.SPP))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (bluetoothSocket == null) {
                BL.d("connect error ,the bluetoothSocket is null ")
                connectAgain()
                return
            }
            callback.startConnect()
            if (!BluetoothConnector.newInstance().connectSocket(device, bluetoothSocket!!)) {
                BL.d("connect again time ：$connect_again_time=$status")
                connectAgain()
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
            connect_again_time = default_connect_again_time
            while (status === BleMeasureStatus.RUNNING) {
                if (reading()) return
            }
        }

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
                    BL.d("reading error ：$status==$ignored=$connect_again_time")
                    connectAgain()
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

        private fun connectAgain() {
            if (status === BleMeasureStatus.RUNNING && connect_again_time > 0) {
                connect_again_time--
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