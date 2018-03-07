package com.hd.bletools.view

import android.bluetooth.BluetoothGattCharacteristic
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.hd.bletools.R
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback
import com.hd.bluetoothutil.callback.MeasureProgressCallback
import com.hd.bluetoothutil.driver.BluetoothLeService
import com.hd.bluetoothutil.utils.BL
import kotlinx.android.synthetic.main.fragment_measure_4.*
import org.jetbrains.anko.runOnUiThread


/**
 * Created by hd on 2017/12/13 .
 *
 */
class Measure4Fragment : MeasureFragment(), MeasureBle4ProgressCallback {

    override fun setMeasureProgressCallback(): MeasureProgressCallback {
        return this
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_measure_4
    }

    override fun initView() {
        itemUUID.clear()
        itemUUID.add(0, activity.resources.getString(R.string.select_uuid_title))
        setSpinnerAdapter()
    }

    override fun write(bluetoothGattCharacteristic: BluetoothGattCharacteristic?, bluetoothLeService: BluetoothLeService) {
        this.bluetoothLeService = bluetoothLeService
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic
        if (itemUUID.size == 1) getItemUUID()
    }

    private fun getItemUUID() {
        val supportedGattServices = bluetoothLeService!!.supportedGattServices
        if (supportedGattServices != null) {
            supportedGattServices.flatMap { it.characteristics }.forEach { itemUUIDMap.put(it.uuid.toString(), it) }
            itemUUID.addAll(itemUUIDMap.keys)
            runOnUiThread { arrayAdapter!!.notifyDataSetChanged() }
        }
        BL.d("all uuid :$itemUUID\n all BluetoothGattCharacteristic map : $itemUUIDMap")
    }

    private val itemUUID = mutableListOf<String>()

    private val itemUUIDMap = mutableMapOf<String, BluetoothGattCharacteristic>()

    private var arrayAdapter: ArrayAdapter<String>? = null

    private fun setSpinnerAdapter() {
        arrayAdapter = ArrayAdapter(activity.applicationContext, R.layout.spinner_item, itemUUID)
        arrayAdapter!!.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spUUID.adapter = arrayAdapter
        spUUID.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    notifyTargetCharacteristic(itemUUIDMap[itemUUID[p2]])
                } else {
                    entity.targetCharacteristicUuid = null
                    bluetoothLeService?.setNotification(entity)
                }
            }
        }
    }

    private var lastCharacteristic: BluetoothGattCharacteristic? = null

    private fun notifyTargetCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        entity.targetCharacteristicUuid = characteristic!!.uuid
        bluetoothLeService?.selectiveNotification(characteristic, arrayOf(lastCharacteristic))
        this.bluetoothGattCharacteristic = characteristic
        lastCharacteristic=characteristic
    }
}