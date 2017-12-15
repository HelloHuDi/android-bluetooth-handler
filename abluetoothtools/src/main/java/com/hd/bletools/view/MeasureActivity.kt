package com.hd.bletools.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.hd.bletools.R
import kotlinx.android.synthetic.main.activity_measure.*
import kotlinx.android.synthetic.main.send_data_sheet.*
import java.io.UnsupportedEncodingException
import java.util.*


/**
 * Created by hd on 2017/12/12 .
 * measure
 */
class MeasureActivity : BaseActivity() {

    private var measureFragment: MeasureFragment? = null

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure)
//        val bundle = intent.getBundleExtra("bundle") as Bundle
//        val version = bundle.getSerializable("version") as DeviceVersion
//        val device = bundle.getParcelable<BluetoothDevice>("device")
//        BL.d("$version+${device!!.name}")
//        measureFragment = if (version == DeviceVersion.BLUETOOTH_2) {
//            Measure2Fragment()
//        } else {
//            Measure4Fragment()
//        }
//        measureFragment!!.arguments = bundle

        measureFragment = Measure2Fragment()
        fragmentManager.beginTransaction().replace(R.id.container, measureFragment).commit()
        cb_hex_rev.setOnCheckedChangeListener { p0, p1 ->
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_measure, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.start_measure -> measureFragment?.startMeasure()
            R.id.stop_measure -> measureFragment?.stopMeasure()
            R.id.control_bottom_bar -> controlBottomBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun controlBottomBar() {
        if (bottom_bar.visibility == View.VISIBLE) {
            bottom_bar.visibility = View.GONE
        } else {
            bottom_bar.visibility = View.VISIBLE
        }
    }

    fun sendData(view: View) {
        val arrayList = ArrayList<ByteArray>()
        val ins = et_write_data.text.toString().trim()
        if (cb_hex.isChecked) {// HEX
            if (ins.isNotEmpty()) {
                sendHexData(ins, arrayList)
            } else {
                receiveDataWithLineFeed(resources.getString(R.string.send_data_is_null))
            }
        } else {
            try {
                arrayList.add(ins.toByteArray(charset("UTF-8")))
                writeData(arrayList)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                receiveDataWithLineFeed(resources.getString(R.string.translate_coding_failure))
            }
        }
    }

    private fun sendHexData(ins: String, arrayList: ArrayList<ByteArray>) {
        val hexs = ins.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val bytes = ByteArray(hexs.size)
        var okflag = true
        for (index in hexs.indices) {
            val hex = hexs[index]
            try {
                val d = Integer.parseInt(hex, 16)
                if (d > 255) {
                    receiveDataWithLineFeed(String.format(resources.getString(R.string.greater_than_ff), hex))
                    okflag = false
                } else {
                    bytes[index] = d.toByte()
                }
            } catch (e: NumberFormatException) {
                receiveDataWithLineFeed(String.format(resources.getString(R.string.is_not_hex), hex))
                e.printStackTrace()
                okflag = false
            }
        }
        arrayList.add(bytes)
        if (okflag && arrayList.size > 0) {
            writeData(arrayList)
        }
    }

    private fun writeData(arrayList: ArrayList<ByteArray>) {
        measureFragment?.sendData(arrayList)
    }

    private fun receiveDataWithLineFeed(string: String) {
        measureFragment?.snack(string)
    }
}

