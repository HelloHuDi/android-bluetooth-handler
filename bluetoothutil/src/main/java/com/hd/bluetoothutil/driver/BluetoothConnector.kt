package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket

import com.hd.bluetoothutil.utils.BL

import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by hd on 2017/7/18.
 * connect device
 */
class BluetoothConnector {

    private var port = 0

    internal fun connectSocket(ownDevice: BluetoothDevice, bluetoothSocket: BluetoothSocket): Boolean {
        var connectStatus = connectSocketByUUID(bluetoothSocket)
        if (!connectStatus) {
            BL.d("connectSocket connectBtByChannel")
            connectStatus = connectBtByChannel(ownDevice, bluetoothSocket)
        }
        if (!connectStatus) {
            BL.d("connectSocket connectBtBySco")
            connectStatus = connectBtBySco(ownDevice, bluetoothSocket)
        }
        return connectStatus
    }

    /**
     * Initiating a Bluetooth connection by reflection principle
     */
    private fun connectBtBySco(mBluetoothDevice: BluetoothDevice, mBluetoothSocket: BluetoothSocket): Boolean {
        var bluetoothSocket = mBluetoothSocket
        var method: Method? = null
        try {
            method = mBluetoothDevice.javaClass.getMethod("createScoSocket")
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }

        if (method != null)
            try {
                bluetoothSocket = method.invoke(mBluetoothDevice) as BluetoothSocket
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        return try {
            bluetoothSocket.connect()
            true
        } catch (e: IOException) {
            BL.d("connectBtBySco connect error :$e")
            false
        }
    }

    /**
     * Using the principle of reflection,
     * using a random port to initiate a Bluetooth connection
     */
    private fun connectBtByChannel(ownDevice: BluetoothDevice, bluetoothSocket: BluetoothSocket): Boolean {
        var socket = bluetoothSocket
        var method: Method? = null
        try {
            method = ownDevice.javaClass.getMethod("createRfcommSocket", *arrayOf<Class<*>>(Int::class.javaPrimitiveType!!))
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
        port = port++ % 30
        if (port == 0) {
            port = 1
        }
        if (method != null) {
            try {
                socket = method.invoke(ownDevice, port) as BluetoothSocket//1-30 port
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            return try {
                socket.connect()
                true
            } catch (e: IOException) {
                BL.d("connectBtByChannel connect error :$e")
                false
            }
        }
        return false
    }

    private fun connectSocketByUUID(bluetoothSocket: BluetoothSocket): Boolean {
        return try {
            bluetoothSocket.connect()
            true
        } catch (e: IOException) {
            BL.d("connectSocketByUUID connect error :$e")
            false
        }

    }

    companion object {

        fun newInstance(): BluetoothConnector {
            return BluetoothConnector()
        }
    }

}
