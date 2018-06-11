package com.hd.bluetoothutil.driver

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.utils.BL
import java.util.*

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class BluetoothLeService : Service() {
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothDeviceAddress: String? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private val buf = ByteArray(10)
    private var bufIndex = 0

    private val mBinder = LocalBinder()

    //public final static UUID UUID_HEART_RATE_MEASUREMENT =
    // UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    // Implements progressCallback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private val mGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                broadcastUpdate(ACTION_GATT_CONNECTED, null)
                val discoverServices = mBluetoothGatt!!.discoverServices()
                BL.d("onConnectionStateChange STATE_CONNECTED :$discoverServices")
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
        BL.d("get data from ble :" + Arrays.toString(characteristic.value) + ",BluetoothGattCharacteristic name " //
                + GattAttributeResolver.getAttributeName(characteristic.uuid.toString() + "", "unknown uuid"))
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
            val flag = characteristic.properties
            val format = if (flag and 0x01 != 0) {
                BluetoothGattCharacteristic.FORMAT_UINT16
            } else {
                BluetoothGattCharacteristic.FORMAT_UINT8
            }
            val heartRate = characteristic.getIntValue(format, 1)!!
            BL.d("result ：$heartRate")
            intent.putExtra(EXTRA_DATA, intToBytes(heartRate))
            sendBroadcast(intent)
        } else {
            val data = characteristic.value
            BL.d("receive bluetooth result ：" + Arrays.toString(data))
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
     * progressCallback.
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
        BL.d("refresh device ：$refresh")
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
     * progressCallback.
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
     * progressCallback.
     *
     * @param characteristic The characteristic to read from.
     */
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            BL.d("BluetoothAdapter not initialized")
            return
        }
        BL.d("start read")
        mBluetoothGatt!!.readCharacteristic(characteristic)
    }

    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            BL.d("BluetoothAdapter not initialized")
            return
        }
        BL.d("start write")
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

    /** global setting*/
    fun setNotification(entity: BluetoothDeviceEntity? = null, callback: MeasureBle4ProgressCallback? = null) {
        val thread = Thread(Runnable {
            val setNotifition = booleanArrayOf(false)
            // Show all the supported services and characteristics on the user interface.
            val supportedGattServices = supportedGattServices
            if (supportedGattServices != null) {
                for (bluetoothGattService in supportedGattServices) {
                    for (bluetoothGattCharacteristic in bluetoothGattService.characteristics) {
                        if (bluetoothGattCharacteristic == null)
                            continue
                        callback?.write(bluetoothGattCharacteristic, this)
                        BL.d("start write data to device: " + Arrays.toString(bluetoothGattCharacteristic.value) //
                                + "==" + entity?.targetCharacteristicUuid + "==" + bluetoothGattCharacteristic.uuid)
                        if(setNotifition[0])continue
                        setNotifition(entity, bluetoothGattCharacteristic, setNotifition)
                    }
                }
            }
        })
        thread.start()
    }

    private fun setNotifition(entity: BluetoothDeviceEntity? = null,bluetoothGattCharacteristic: BluetoothGattCharacteristic,//
                              setNotifition: BooleanArray) {
        if(entity?.targetCharacteristicUuid != null){
            if(bluetoothGattCharacteristic.uuid == entity.targetCharacteristicUuid){
                BL.d("target uuid : " + bluetoothGattCharacteristic.uuid)
                setCharacteristicNotification(bluetoothGattCharacteristic, true)
                readCharacteristic(bluetoothGattCharacteristic)
                setNotifition[0] = true
            }
        }else{
            setCharacteristicNotification(bluetoothGattCharacteristic, true)
        }
    }

    fun selectiveNotification(currentCharacteristic: BluetoothGattCharacteristic,//
                              lastNotifyCharacteristic: Array<BluetoothGattCharacteristic?>) {
        val charaProp = currentCharacteristic.properties
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_READ > 0) {
            // If there is an active notification on a characteristic, clear
            // it first so it doesn't update the data field on the user interface.
            if (lastNotifyCharacteristic[0] != null) {
                setCharacteristicNotification(lastNotifyCharacteristic[0]!!, false)
                lastNotifyCharacteristic[0] = null
            }
            readCharacteristic(currentCharacteristic)
        }
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
            lastNotifyCharacteristic[0] = currentCharacteristic
            setCharacteristicNotification(currentCharacteristic, true)
        }
    }

    companion object {
        const val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_BE_READ = "com.example.bluetooth.le.ACTION_DATA_BE_READ"
        const val ACTION_DATA_BE_WRITE = "com.example.bluetooth.le.ACTION_DATA_BE_WRITE"
        const val ACTION_DATA_BE_UPDATATED = "com.example.bluetooth.le.ACTION_DATA_BE_UPDATATED"
        const val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"
        val UUID_HEART_RATE_MEASUREMENT = UUID.fromString(GattAttributeResolver.HEART_RATE_MEASUREMENT)!!
        val UUID_CHARACTER_RECEIVE = UUID.fromString(GattAttributeResolver.CHARACTER_RECEIVE)!!
        val UUID_CLIENT_CHARACTER_CONFIG = UUID.fromString(GattAttributeResolver.CLIENT_CHARACTERISTIC_CONFIG)!!
        private val TAG = BluetoothLeService::class.java.simpleName
    }
}