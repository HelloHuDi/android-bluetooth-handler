package com.hd.bluetoothutil.driver

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.hd.bluetoothutil.utils.BL
import java.util.*

/**
 * Created by hd on 2017/5/24 0008.
 *
 *
 * 蓝牙4.0服务
 */
class BluetoothLeService : Service() {
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothDeviceAddress: String? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private val buf = ByteArray(10)
    private var bufIndex = 0

    private val mBinder = LocalBinder()

    //public final static UUID UUID_HEART_RATE_MEASUREMENT =
    // UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private val mGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                broadcastUpdate(ACTION_GATT_CONNECTED, null)
                val discoverServices = mBluetoothGatt!!.discoverServices()
                BL.d("onConnectionStateChange STATE_CONNECTED :" + discoverServices)
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                BL.d("onConnectionStateChange STATE_DISCONNECTED")
                broadcastUpdate(ACTION_GATT_DISCONNECTED, null)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            BL.d("onServicesDiscovered")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED, null)
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            BL.d("onCharacteristicRead")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_BE_READ, characteristic)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            BL.d("onCharacteristicWrite")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_BE_WRITE, characteristic)
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            BL.d("onCharacteristicChanged")
            broadcastUpdate(ACTION_DATA_BE_UPDATATED, characteristic)
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after `BluetoothGatt#discoverServices()` completes successfully.
     *
     * @return A `List` of supported services.
     */
    val supportedGattServices: List<BluetoothGattService>?
        get() = if (mBluetoothGatt == null) null else mBluetoothGatt!!.services

    inner class LocalBinder : Binder() {
        val service: BluetoothLeService
            get() = this@BluetoothLeService
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close()
        return super.onUnbind(intent)
    }

    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic?) {
        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        val intent = Intent(action)
        if (characteristic == null) {
            sendBroadcast(intent)
            return
        }
        BL.d("get data from ble :" + Arrays.toString(characteristic.value) + ",BluetoothGattCharacteristic name " + GattAttributeResolver.getAttributeName(characteristic.uuid.toString() + "", "unknown uuid"))
        if (UUID_CHARACTER_RECEIVE == characteristic.uuid) {
            val data = characteristic.value
            for (b in data) {
                buf[bufIndex] = b
                bufIndex++
                if (bufIndex == buf.size) {
                    intent.putExtra(EXTRA_DATA, buf)
                    sendBroadcast(intent)
                    bufIndex = 0
                }
            }
        } else if (UUID_HEART_RATE_MEASUREMENT == characteristic.uuid) {
            // 这是心率测量配置文件。
            val flag = characteristic.properties
            val format  = if (flag and 0x01 != 0) {
                BluetoothGattCharacteristic.FORMAT_UINT16
            } else {
                BluetoothGattCharacteristic.FORMAT_UINT8
            }
            val heartRate = characteristic.getIntValue(format, 1)!!
            BL.d("结果：" + heartRate)
            intent.putExtra(EXTRA_DATA, intToBytes(heartRate))
            sendBroadcast(intent)
        } else {
            // For all other profiles, writes the data formatted in HEX.
            val data = characteristic.value
            BL.d("接收到蓝牙结果：" + Arrays.toString(data))
            if (data != null && data.isNotEmpty()) {
                intent.putExtra(EXTRA_DATA, data)
                sendBroadcast(intent)
            }
        }
    }

    private fun intToBytes(value: Int): ByteArray {
        val bytes = ByteArray(4)
        bytes[3] = (value and -0x1000000 shr 24).toByte()
        bytes[2] = (value and 0x00FF0000 shr 16).toByte()
        bytes[1] = (value and 0x0000FF00 shr 8).toByte()
        bytes[0] = (value and 0x000000FF).toByte()
        return bytes
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    fun initialize(): Boolean {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        val mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        if (mBluetoothManager == null) {
            BL.d("Unable to initialize BluetoothManager.")
            return false
        }
        mBluetoothAdapter = mBluetoothManager.adapter
        if (mBluetoothAdapter == null) {
            BL.d("Unable to obtain a BluetoothAdapter.")
            return false
        }
        return true
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * `BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)`
     * callback.
     */
    fun connect(address: String?): Boolean {
        if (mBluetoothAdapter == null || address == null) {
            BL.d("BluetoothAdapter not initialized or unspecified address.")
            return false
        }
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address == mBluetoothDeviceAddress && mBluetoothGatt != null) {
            BL.d("Trying to use an existing mBluetoothGatt for connection.")
            return if (mBluetoothGatt!!.connect()) {
                BL.d("mBluetoothGatt  connection success")
                true
            } else {
                BL.d("mBluetoothGatt  connection unSuccess")
                false
            }
        }
        val device = mBluetoothAdapter!!.getRemoteDevice(address)
        if (device == null) {
            BL.d("Device not found.  Unable to connect.")
            return false
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback)
        BL.d("Trying to create a new connection.")
        mBluetoothDeviceAddress = address
        val refresh = refreshDeviceCache(mBluetoothGatt)
        BL.d("refresh device ：" + refresh)
        return true
    }

    /**
     * Clears the internal cache and forces a refresh of the services from the
     * remote device.
     *
     * @param gatt
     *
     * @return
     */
    private fun refreshDeviceCache(gatt: BluetoothGatt?): Boolean {
        try {
            val localMethod = gatt!!.javaClass.getMethod("refresh", *arrayOfNulls(0))
            if (localMethod != null) {
                return localMethod.invoke(gatt, *arrayOfNulls(0)) as Boolean
            }
        } catch (localException: Exception) {
            BL.e(TAG, "An exception occured while refreshing device")
        }

        return false
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * `BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)`
     * callback.
     */
    fun disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            BL.d("BluetoothAdapter not initialized")
            return
        }
        mBluetoothGatt!!.disconnect()
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    fun close() {
        if (mBluetoothGatt == null) {
            return
        }
        mBluetoothGatt!!.close()
        mBluetoothGatt = null
    }

    /**
     * Request a read on a given `BluetoothGattCharacteristic`. The read result is reported
     * asynchronously through the `BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)`
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            BL.d("BluetoothAdapter not initialized")
            return
        }
        BL.d("开始读取：")
        mBluetoothGatt!!.readCharacteristic(characteristic)
    }

    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            BL.d("BluetoothAdapter not initialized")
            return
        }
        BL.d("开始写入")
        mBluetoothGatt!!.writeCharacteristic(characteristic)
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic, enabled: Boolean) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return
        }
        mBluetoothGatt!!.setCharacteristicNotification(characteristic, enabled)
        // This is specific to Oximeter Data Transfer.
        val descriptor = characteristic.getDescriptor(UUID_CLIENT_CHARACTER_CONFIG)
        if (descriptor != null) {
            if (UUID_CHARACTER_RECEIVE == characteristic.uuid) {
                if (enabled) {
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                } else {
                    descriptor.value = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                }
            } else {
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            }
            mBluetoothGatt!!.writeDescriptor(descriptor)
        }
    }

    companion object {
        /**
         * 蓝牙连接状态
         */
        val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        /**
         * 蓝牙断开状态
         */
        val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
        /**
         * 蓝牙服务被发现
         */
        val ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        /**
         * 数据被读取
         */
        val ACTION_DATA_BE_READ = "com.example.bluetooth.le.ACTION_DATA_BE_READ"
        /**
         * 数据被写入
         */
        val ACTION_DATA_BE_WRITE = "com.example.bluetooth.le.ACTION_DATA_BE_WRITE"
        /**
         * 数据被更新
         */
        val ACTION_DATA_BE_UPDATATED = "com.example.bluetooth.le.ACTION_DATA_BE_UPDATATED"
        /**
         * 接收蓝牙结果
         */
        val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"
        /**
         * 蓝牙心率解析uuid
         */
        val UUID_HEART_RATE_MEASUREMENT = UUID.fromString(GattAttributeResolver.HEART_RATE_MEASUREMENT)
        val UUID_SERVICE_DATA = UUID.fromString("49535343-fe7d-4ae5-8fa9-9fafd205e455")
        val UUID_CHARACTER_RECEIVE = UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616")
        val UUID_MODIFY_BT_NAME = UUID.fromString("00005343-0000-1000-8000-00805F9B34FB")
        val UUID_CLIENT_CHARACTER_CONFIG = UUID.fromString(GattAttributeResolver.CLIENT_CHARACTERISTIC_CONFIG)
        private val TAG = BluetoothLeService::class.java.simpleName
    }
}